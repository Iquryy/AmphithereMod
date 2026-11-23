package amphitheremod.item;

import amphitheremod.util.IAmphithereData;
import amphitheremod.util.SoulBindData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Crystal Feather. Used on your own tamed amphithere it soul binds the two of you (if either dies, both die) and stores
 * the amphithere inside the feather. Used on a block it releases the stored amphithere there, or, if it's already out,
 * teleports it to you (same dimension). The feather shatters once its amphithere is gone.
 *
 * NBT: SoulBind {Amphi: uuid, Owner: uuid, Stored: entity tag while it's inside, Name: custom name for the tooltip}
 */
public class ItemCrystalFeather extends Item {
    private static final String TAG = "SoulBind";
    private static final String MSG = "amphitheremod.crystal_feather.msg.";

    public ItemCrystalFeather() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Nullable
    private static CompoundTag bindTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TAG) ? tag.getCompound(TAG) : null;
    }

    public static boolean isBound(ItemStack stack) {
        CompoundTag t = bindTag(stack);
        return t != null && t.hasUUID("Amphi");
    }

    @Nullable
    public static UUID getBoundAmphi(ItemStack stack) {
        CompoundTag t = bindTag(stack);
        return t != null && t.hasUUID("Amphi") ? t.getUUID("Amphi") : null;
    }

    @Nullable
    public static UUID getBondOwner(ItemStack stack) {
        CompoundTag t = bindTag(stack);
        return t != null && t.hasUUID("Owner") ? t.getUUID("Owner") : null;
    }

    public static boolean holdsAmphi(ItemStack stack) {
        CompoundTag t = bindTag(stack);
        return t != null && t.contains("Stored");
    }

    private static void message(Player player, String key, Object... args) {
        player.displayClientMessage(Component.translatable(MSG + key, args), true);
    }

    /** Right click on an amphithere (called from event.SoulBindEvents, server side). */
    public static void useOnAmphithere(ItemStack stack, Player player, EntityAmphithere amphi) {
        Level level = player.level();
        if (!amphi.isTame()) {
            message(player, "not_tamed");
            return;
        }
        if (!amphi.isOwnedBy(player)) {
            message(player, "not_owner");
            return;
        }
        SoulBindData data = SoulBindData.get(level.getServer());
        UUID amphiId = amphi.getUUID();
        if (!isBound(stack)) {
            if (data.isBound(amphiId) || ((IAmphithereData) amphi).amphiMod$isBound()) {
                message(player, "already_bound");
                return;
            }
            CompoundTag t = new CompoundTag();
            t.putUUID("Amphi", amphiId);
            t.putUUID("Owner", player.getUUID());
            stack.getOrCreateTag().put(TAG, t);
            ((IAmphithereData) amphi).amphiMod$setBound(true);
            data.bind(amphiId, player.getUUID());
            Component name = amphi.getDisplayName();
            storeAmphi(stack, amphi);
            message(player, "bound", name);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.4F);
            return;
        }
        if (!player.getUUID().equals(getBondOwner(stack))) {
            message(player, "not_your_feather");
            return;
        }
        if (!amphiId.equals(getBoundAmphi(stack))) {
            message(player, "other_amphi");
            return;
        }
        if (holdsAmphi(stack)) {
            return;
        }
        Component name = amphi.getDisplayName();
        storeAmphi(stack, amphi);
        message(player, "stored", name);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.3F);
    }

    /** Saves the amphithere into the feather and removes it from the world. */
    private static void storeAmphi(ItemStack stack, EntityAmphithere amphi) {
        amphi.ejectPassengers();
        amphi.stopRiding();
        if (amphi.isLeashed()) {
            amphi.dropLeash(true, true);
        }
        amphi.getNavigation().stop();
        amphi.fallDistance = 0.0F;
        CompoundTag entityTag = new CompoundTag();
        CompoundTag t = bindTag(stack);
        if (t == null || !amphi.save(entityTag)) {
            return;
        }
        t.put("Stored", entityTag);
        t.putString("Name", amphi.hasCustomName() ? amphi.getCustomName().getString() : "");
        amphi.discard();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player == null || !isBound(stack)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!player.getUUID().equals(getBondOwner(stack))) {
            message(player, "not_your_feather");
            return InteractionResult.FAIL;
        }
        if (shatterIfLost(stack, player)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos spot = context.getClickedPos().relative(context.getClickedFace());
        double x = spot.getX() + 0.5;
        double y = spot.getY();
        double z = spot.getZ() + 0.5;
        if (holdsAmphi(stack)) {
            // Release the stored amphithere
            CompoundTag t = bindTag(stack);
            Entity entity = EntityType.loadEntityRecursive(t.getCompound("Stored"), level, e -> e);
            if (!(entity instanceof EntityAmphithere)) {
                message(player, "release_failed");
                return InteractionResult.FAIL;
            }
            entity.moveTo(x, y, z, player.getYRot(), 0.0F);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.fallDistance = 0.0F;
            ((IAmphithereData) entity).amphiMod$setBound(true);
            if (!level.addFreshEntity(entity)) {
                message(player, "release_failed");
                return InteractionResult.FAIL;
            }
            t.remove("Stored");
            message(player, "released", entity.getDisplayName());
            level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.1F);
            return InteractionResult.SUCCESS;
        }
        // Summon the bound amphithere if it's loaded in this dimension
        Entity entity = ((ServerLevel) level).getEntity(getBoundAmphi(stack));
        if (!(entity instanceof EntityAmphithere amphi) || !entity.isAlive()) {
            message(player, "not_found");
            return InteractionResult.FAIL;
        }
        amphi.ejectPassengers();
        amphi.stopRiding();
        amphi.getNavigation().stop();
        amphi.moveTo(x, y, z, player.getYRot(), 0.0F);
        amphi.setDeltaMovement(Vec3.ZERO);
        amphi.fallDistance = 0.0F;
        message(player, "summoned", amphi.getDisplayName());
        level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    /** If the bound amphithere is gone, destroys the feather and tells the holder. Server side. */
    public static boolean shatterIfLost(ItemStack stack, Player holder) {
        UUID amphiId = getBoundAmphi(stack);
        if (amphiId == null || holder.level().isClientSide) {
            return false;
        }
        if (!SoulBindData.get(holder.level().getServer()).isLost(amphiId)) {
            return false;
        }
        stack.shrink(1);
        holder.displayClientMessage(Component.translatable(MSG + "shattered"), false);
        holder.level().playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && entity instanceof Player player && entity.tickCount % 20 == 0 && isBound(stack)) {
            shatterIfLost(stack, player);
        }
    }

    @Override
    public int getEntityLifespan(ItemStack stack, Level level) {
        // A dropped bound feather never despawns, so the amphithere inside isn't lost
        return isBound(stack) ? Integer.MAX_VALUE : super.getEntityLifespan(stack, level);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (isBound(stack)) {
            CompoundTag t = bindTag(stack);
            String name = t == null ? "" : t.getString("Name");
            Component nameComponent = name.isEmpty() ? Component.translatable("entity.iceandfire.amphithere") : Component.literal(name);
            tooltip.add(Component.translatable("amphitheremod.crystal_feather.bound_name", nameComponent).withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable("amphitheremod.crystal_feather.bound_desc").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("amphitheremod.crystal_feather.empty_0").withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable("amphitheremod.crystal_feather.empty_1").withStyle(ChatFormatting.RED));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isBound(stack);
    }
}
