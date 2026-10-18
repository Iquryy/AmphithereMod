package amphitheremod.events;

import amphitheremod.AmphithereMod;
import amphitheremod.item.ItemCrystalFeather;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.SoulBindData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

/**
 * Crystal Feather soul bonds: if either the amphithere or its owner dies, both die.
 * An amphithere whose owner died while it wasn't loaded dies when it's next loaded. An owner who is offline when
 * their amphithere dies doesn't die; they only lose the feather.
 */
@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class SoulBindEvents {
    /** Death message: "death.attack.amphitheremod.soul" = "%1$s soul died". Ignores armor, works in creative and isn't stopped by a totem. */
    public static final DamageSource SOUL = new DamageSource(AmphithereMod.MODID + ".soul") {
        { // set from inside a subclass, so it compiles whether these setters are public or protected in your mappings
            setDamageBypassesArmor();
            setDamageAllowedInCreativeMode();
            setDamageIsAbsolute();
        }
    };

    // ------------------------------------------------------------------ binding / storing: right-clicking an amphithere

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ItemCrystalFeather) || !(event.getTarget() instanceof EntityAmphithere)) return;
        // Cancel on both sides so the amphithere doesn't also open its menu or let the player mount.
        event.setCanceled(true);
        event.setCancellationResult(EnumActionResult.SUCCESS);
        if (event.getWorld().isRemote) return;
        ItemCrystalFeather.useOnAmphithere(stack, event.getEntityPlayer(), (EntityAmphithere) event.getTarget());
    }

    // ------------------------------------------------------------------ deaths

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        EntityLivingBase dead = event.getEntityLiving();
        if (dead.world.isRemote) return;
        SoulBindData data = SoulBindData.get(dead.world);

        if (dead instanceof EntityAmphithere) {
            UUID amphiId = dead.getUniqueID();
            UUID ownerId = data.breakBond(amphiId);
            boolean wasDoomed = data.isDoomed(amphiId);
            if (ownerId == null && !wasDoomed) return;
            data.markDead(amphiId);
            ((IAmphithereData) dead).amphiMod_master$setBounded(false);
            // The owner is already dead (that's why this one died), or there was no bond left.
            if (ownerId == null || event.getSource() == SOUL) return;

            EntityPlayer owner = findPlayer(dead.world.getMinecraftServer(), ownerId);
            if (owner != null) soulKill(owner);
            // Offline owners don't die. Their feather is bound to a dead amphithere now,
            // so it shatters within a second of them logging back in (ItemCrystalFeather.onUpdate).
            return;
        }

        if (dead instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) dead;
            MinecraftServer server = player.world.getMinecraftServer();

            // Feathers the player is carrying vanish, along with any amphithere resting inside them.
            InventoryPlayer inv = player.inventory;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack s = inv.getStackInSlot(i);
                if (isOwnBoundFeather(s, player)) {
                    vanishFeather(s, data);
                    inv.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
            ItemStack onCursor = inv.getItemStack();
            if (isOwnBoundFeather(onCursor, player)) {
                vanishFeather(onCursor, data);
                inv.setItemStack(ItemStack.EMPTY);
            }

            // Every amphithere still bound to the player dies too.
            for (UUID amphiId : data.amphisBoundTo(player.getUniqueID())) {
                data.breakBond(amphiId);
                data.doom(amphiId);
                Entity amphi = server == null ? null : server.getEntityFromUuid(amphiId);
                if (amphi instanceof EntityAmphithere && !amphi.isDead)
                    soulKill((EntityAmphithere) amphi);
                // Otherwise it's unloaded, or resting in a feather somewhere else: it dies when it's next loaded,
                // and that feather shatters the next time someone holds or uses it.
            }
        }
    }

    private static boolean isOwnBoundFeather(ItemStack s, EntityPlayer player) {
        return s.getItem() instanceof ItemCrystalFeather && ItemCrystalFeather.isBound(s)
                && player.getUniqueID().equals(ItemCrystalFeather.getBondOwner(s));
    }

    private static void vanishFeather(ItemStack s, SoulBindData data) {
        UUID amphiId = ItemCrystalFeather.getBoundAmphi(s);
        if (amphiId != null && ItemCrystalFeather.holdsAmphi(s)) {
            data.breakBond(amphiId);
            data.markDead(amphiId);
        }
    }

    // ------------------------------------------------------------------ catching up on deaths that happened while unloaded / offline

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase e = event.getEntityLiving();
        if (e.world.isRemote || !(e instanceof EntityAmphithere) || e.ticksExisted % 20 != 0) return;
        if (!((IAmphithereData) e).amphiMod_master$getBounded()) return;
        if (SoulBindData.get(e.world).isDoomed(e.getUniqueID()))
            soulKill(e);
    }

    /** An amphithere killed by its soul bond drops nothing. Players who die from it keep their normal drops. */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityAmphithere && event.getSource() == SOUL) {
            event.getDrops().clear();
            event.setCanceled(true);
        }
    }

    // ------------------------------------------------------------------ helpers

    private static EntityPlayer findPlayer(MinecraftServer server, UUID id) {
        return server == null ? null : server.getPlayerList().getPlayerByUUID(id);
    }

    /** Kills with the soul damage source; falls back to forcing the death if something blocked the damage. */
    public static void soulKill(EntityLivingBase target) {
        if (target.isDead || target.getHealth() <= 0) return;
        target.hurtResistantTime = 0;
        target.attackEntityFrom(SOUL, Float.MAX_VALUE);
        if (!target.isDead && target.getHealth() > 0) {
            target.setHealth(0F);
            target.onDeath(SOUL);
        }
    }
}