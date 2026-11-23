package amphitheremod.event;

import amphitheremod.AmphithereMod;
import amphitheremod.config.AmphiConfig;
import amphitheremod.item.ItemCrystalFeather;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.SoulBindData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

/**
 * Crystal Feather soul bonds: binding by right clicking an amphithere, and "if one dies, both die". An amphithere whose
 * owner died while it was unloaded (or stored in a feather) is doomed and dies as soon as it's loaded again.
 */
public final class SoulBindEvents {
    /** Bypasses armor, resistance and creative mode (data/amphitheremod/damage_type/soul.json and its tags). */
    public static final ResourceKey<DamageType> SOUL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AmphithereMod.MODID, "soul"));

    private SoulBindEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ItemCrystalFeather) || !(event.getTarget() instanceof EntityAmphithere amphi) || !AmphiConfig.ENABLE_CRYSTAL_FEATHER.get()) {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (!event.getLevel().isClientSide) {
            ItemCrystalFeather.useOnAmphithere(stack, event.getEntity(), amphi);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity dead = event.getEntity();
        if (dead.level().isClientSide || dead.level().getServer() == null) {
            return;
        }
        MinecraftServer server = dead.level().getServer();
        SoulBindData data = SoulBindData.get(server);
        if (dead instanceof EntityAmphithere) {
            UUID amphiId = dead.getUUID();
            UUID ownerId = data.breakBond(amphiId);
            if (ownerId == null && !data.isDoomed(amphiId)) {
                return;
            }
            data.markDead(amphiId);
            ((IAmphithereData) dead).amphiMod$setBound(false);
            // The owner dies with it, unless this death was itself caused by the owner dying
            if (ownerId != null && !event.getSource().is(SOUL)) {
                Player owner = server.getPlayerList().getPlayer(ownerId);
                if (owner != null) {
                    soulKill(owner);
                }
            }
            return;
        }
        if (dead instanceof Player player) {
            // The owner's feathers vanish; an amphithere stored in one is lost with it
            Inventory inv = player.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack s = inv.getItem(i);
                if (isOwnBoundFeather(s, player)) {
                    vanishFeather(s, data);
                    inv.setItem(i, ItemStack.EMPTY);
                }
            }
            ItemStack carried = player.containerMenu.getCarried();
            if (isOwnBoundFeather(carried, player)) {
                vanishFeather(carried, data);
                player.containerMenu.setCarried(ItemStack.EMPTY);
            }
            // Bound amphitheres die now if loaded, or when next loaded
            for (UUID amphiId : data.amphisBoundTo(player.getUUID())) {
                data.breakBond(amphiId);
                data.doom(amphiId);
                Entity amphi = findEntity(server, amphiId);
                if (amphi instanceof EntityAmphithere bound && bound.isAlive()) {
                    soulKill(bound);
                }
            }
        }
    }

    private static boolean isOwnBoundFeather(ItemStack s, Player player) {
        return s.getItem() instanceof ItemCrystalFeather && ItemCrystalFeather.isBound(s) && player.getUUID().equals(ItemCrystalFeather.getBondOwner(s));
    }

    private static void vanishFeather(ItemStack s, SoulBindData data) {
        UUID amphiId = ItemCrystalFeather.getBoundAmphi(s);
        if (amphiId != null && ItemCrystalFeather.holdsAmphi(s)) {
            data.breakBond(amphiId);
            data.markDead(amphiId);
        }
    }

    private static Entity findEntity(MinecraftServer server, UUID id) {
        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(id);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    /** Doomed amphitheres die once loaded again. */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity e = event.getEntity();
        if (e.level().isClientSide || !(e instanceof EntityAmphithere) || e.tickCount % 20 != 0 || !((IAmphithereData) e).amphiMod$isBound()) {
            return;
        }
        if (SoulBindData.get(e.level().getServer()).isDoomed(e.getUUID())) {
            soulKill(e);
        }
    }

    /** A soul-killed amphithere drops nothing. */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityAmphithere && event.getSource().is(SOUL)) {
            event.getDrops().clear();
            event.setCanceled(true);
        }
    }

    public static void soulKill(LivingEntity target) {
        if (target.isRemoved() || target.getHealth() <= 0.0F) {
            return;
        }
        DamageSource soul = new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SOUL));
        target.invulnerableTime = 0;
        target.hurt(soul, Float.MAX_VALUE);
        // Totems and similar can stop the hit; the bond is absolute
        if (!target.isRemoved() && target.getHealth() > 0.0F) {
            target.setHealth(0.0F);
            target.die(soul);
        }
    }
}
