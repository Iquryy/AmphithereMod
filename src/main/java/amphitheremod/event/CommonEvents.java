package amphitheremod.event;

import amphitheremod.config.AmphiConfig;
import amphitheremod.item.AmphiMaterial;
import amphitheremod.item.AmphithereArmorItem;
import amphitheremod.item.AmphithereBeakItem;
import amphitheremod.registry.AmphiStats;
import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/** Forge events that don't need a mixin. Soul bond events are in SoulBindEvents. */
public final class CommonEvents {
    // The original authors' accounts: naming their own amphithere after themselves gives it their variant
    private static final UUID IQURY_UUID = UUID.fromString("87dd25be-5eb9-451f-a839-c74ce939b76b");
    private static final UUID CRAFTY_UUID = UUID.fromString("c6cbbc0f-3b57-4cd7-961c-42edfe6e71f3");
    private static final UUID NISCHHELM_UUID = UUID.fromString("eb8c824d-6f85-4047-8ca1-f50c8e50bfe0");

    private CommonEvents() {
    }

    /** Counts tamed amphithere deaths in the owner's statistics. */
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof EntityAmphithere amphi) || !amphi.isTame()) {
            return;
        }
        if (amphi.getOwner() instanceof Player owner) {
            owner.awardStat(AmphiStats.TAMED_AMPHITHERE_DEATH_COUNT_ID);
        }
    }

    /** 1 in 51 lightning strikes turn an amphithere into a skeleton. */
    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (!(event.getEntity() instanceof EntityAmphithere amphi) || amphi.level().isClientSide) {
            return;
        }
        if (!isSkeleton(amphi) && amphi.getRandom().nextInt(51) == 50) {
            amphi.playSound(SoundEvents.SKELETON_HURT, 2.0F, 1.0F);
            amphi.setVariant(EnumAmphiType.SKELETON.ordinal());
        }
    }

    /** Burning (the on-fire damage tick) has a 1 in 51 chance to turn an amphithere into a wither skeleton. */
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityAmphithere amphi) || amphi.level().isClientSide) {
            return;
        }
        if (event.getSource().is(DamageTypes.ON_FIRE) && !isSkeleton(amphi) && amphi.getRandom().nextInt(51) == 50) {
            amphi.playSound(SoundEvents.WITHER_SKELETON_HURT, 2.0F, 1.0F);
            amphi.setVariant(EnumAmphiType.WITHER_SKELETON.ordinal());
        }
    }

    // 1.12.2 meant to skip amphitheres that are already skeletons, but its checks were always true / always false
    private static boolean isSkeleton(EntityAmphithere amphi) {
        EnumAmphiType type = EnumAmphiType.fromOrdinal(amphi.getVariant());
        return type == EnumAmphiType.SKELETON || type == EnumAmphiType.WITHER_SKELETON;
    }

    /** Beak attachments aren't weapons for players. */
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity().getMainHandItem().getItem() instanceof AmphithereBeakItem) {
            event.setCanceled(true);
        }
    }

    /** Owner's name easter eggs when mounting a named amphithere. */
    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (!event.isMounting() || event.getLevel().isClientSide || !(event.getEntityBeingMounted() instanceof EntityAmphithere amphi)
                || !(event.getEntityMounting() instanceof Player player)) {
            return;
        }
        if (!amphi.isTame() || !amphi.isOwnedBy(player) || !amphi.hasCustomName()) {
            return;
        }
        UUID id = player.getUUID();
        String name = amphi.getCustomName().getString();
        if (id.equals(IQURY_UUID) && name.equalsIgnoreCase("iqury") && amphi.getVariant() != EnumAmphiType.IQURY.ordinal()) {
            amphi.setVariant(EnumAmphiType.IQURY.ordinal());
            amphi.setCustomName(null);
        } else if (id.equals(CRAFTY_UUID) && name.equalsIgnoreCase("craftie") && amphi.getVariant() != EnumAmphiType.CRAFTY.ordinal()) {
            amphi.setVariant(EnumAmphiType.CRAFTY.ordinal());
            amphi.setCustomName(null);
        } else if (id.equals(NISCHHELM_UUID) && name.equalsIgnoreCase("nischhelm") && amphi.getVariant() != EnumAmphiType.RADISH.ordinal()) {
            amphi.setVariant(EnumAmphiType.RADISH.ordinal());
            amphi.setCustomName(null);
        }
        if (name.equals("deez__nuts")) {
            amphi.setCustomName(Component.literal("Ancient ").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)
                    .append(Component.literal("Deez Nuts").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
        }
    }

    /** Full silver set (beak + 4 armor pieces): a Cure effect from the silverSetBonusEffects config list, if a mod adds one. */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof EntityAmphithere amphi) || amphi.level().isClientSide || amphi.tickCount % 20 != 0
                || !AmphiConfig.SILVER_SET_BONUS.get()) {
            return;
        }
        if (!isSilver(amphi.getMainHandItem(), null)) {
            return;
        }
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (!isSilver(amphi.getItemBySlot(slot), slot)) {
                return;
            }
        }
        MobEffect cure = cureEffect();
        if (cure != null) {
            amphi.addEffect(new MobEffectInstance(cure, 40, 0, true, false));
        }
    }

    /** The first effect in the silverSetBonusEffects list that some installed mod registers, or null if none. */
    @Nullable
    private static MobEffect cureEffect() {
        for (String id : AmphiConfig.SILVER_SET_EFFECTS.get()) {
            ResourceLocation key = ResourceLocation.tryParse(id);
            if (key != null && ForgeRegistries.MOB_EFFECTS.containsKey(key)) {
                return ForgeRegistries.MOB_EFFECTS.getValue(key);
            }
        }
        return null;
    }

    private static boolean isSilver(ItemStack stack, EquipmentSlot slot) {
        if (slot == null) {
            return stack.getItem() instanceof AmphithereBeakItem beak && beak.material == AmphiMaterial.SILVER;
        }
        return stack.getItem() instanceof AmphithereArmorItem armor && armor.material == AmphiMaterial.SILVER && armor.slot == slot;
    }
}
