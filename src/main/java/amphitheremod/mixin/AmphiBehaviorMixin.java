package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import amphitheremod.registry.AmphiItems;
import amphitheremod.registry.AmphiSounds;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Smaller behavior changes from 1.12.2: AI fixes, the flute, taming damage, drops, sounds, leaves and rider pitch.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiBehaviorMixin extends TamableAnimal {
    @Shadow(remap = false)
    protected EntityAmphithere.FlightBehavior flightBehavior;
    @Shadow(remap = false)
    public boolean isFallen;

    /** True while a sitting amphithere has stood up to fight whatever hurt it. */
    @Unique
    private boolean amphiMod$defending = false;

    protected AmphiBehaviorMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    // Tamed amphitheres leave alone a wild amphithere their owner is riding (taming it), and their own owner
    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (AmphiConfig.IGNORE_WILD_TAMING.get()) {
            if (target instanceof EntityAmphithere && target.hasPassenger(owner)) {
                return false;
            }
            if (target instanceof Player && this.isOwnedBy(target)) {
                return false;
            }
        }
        return super.wantsToAttack(target, owner);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void amphiMod$tickTail(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        // The amphithere faces where its rider looks
        if (this.getControllingPassenger() instanceof Player rider) {
            if (!this.level().isClientSide) {
                this.getNavigation().stop();
            }
            this.setYRot(rider.getYRot());
            this.setYHeadRot(rider.getYRot());
        }
        amphiMod$defendWhileSitting(amphi);
    }

    /**
     * A sitting amphithere whose owner is more than 11 blocks away stands up to fight whatever hurt it, then sits back
     * down once it's dead. Other amphitheres and the owner's own vehicle don't count.
     */
    @Unique
    private void amphiMod$defendWhileSitting(EntityAmphithere amphi) {
        if (this.level().isClientSide || !this.isTame() || this.isVehicle() || !(this.getOwner() instanceof Player owner)) {
            return;
        }
        LivingEntity attacker = this.getLastHurtByMob();
        if (attacker == null || attacker instanceof EntityAmphithere || attacker.hasPassenger(owner)) {
            return;
        }
        if (amphi.getCommand() == 1 && !this.amphiMod$defending && this.distanceToSqr(owner) >= 121.0 && attacker.isAlive()) {
            this.amphiMod$defending = true;
            amphi.setCommand(0);
            this.setTarget(attacker);
        }
        if (this.amphiMod$defending && !attacker.isAlive()) {
            this.amphiMod$defending = false;
            this.setTarget(null);
            amphi.setCommand(1);
        }
    }

    // The flute only works for the owner: an airborne, riderless amphithere glides down and follows
    @Inject(method = "onHearFlute", at = @At("HEAD"), cancellable = true, remap = false)
    private void amphiMod$flute(Player player, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (this.isTame() && !this.onGround() && !this.isVehicle() && this.isOwnedBy(player)) {
            this.isFallen = true;
            amphi.setCommand(2);
        }
        ci.cancel();
    }

    // Ice and Fire never resets the flight behavior after a fall, so a fallen amphithere could stop flying for good
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void amphiMod$recoverFromFall(CallbackInfo ci) {
        if (!this.isFallen && this.flightBehavior == EntityAmphithere.FlightBehavior.NONE) {
            this.flightBehavior = EntityAmphithere.FlightBehavior.WANDER;
        }
    }

    // Untamed rider bites scale with the amphithere's attack damage (Strength makes them hurt more), never below the
    // Ice and Fire config value
    @ModifyArg(method = "positionRider", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float amphiMod$tamingDamage(float configDamage) {
        if (!AmphiConfig.ADVANCED_TAMING_DAMAGE.get() || IafConfig.amphithereAttackStrength <= 0) {
            return configDamage;
        }
        return Math.max(configDamage, (float) (this.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
                * configDamage / IafConfig.amphithereAttackStrength));
    }

    // Full rider pitch instead of half, so the amphithere noses up and down as far as the rider looks (1.12.2 fix)
    @Inject(method = "getRiddenRotation", at = @At("RETURN"), cancellable = true, remap = false)
    private void amphiMod$fullPitch(LivingEntity rider, CallbackInfoReturnable<Vec2> cir) {
        cir.setReturnValue(new Vec2(rider.getXRot(), rider.getYRot()));
    }

    // Gear the amphithere wears always drops on death, and the Shivaxi drops 1 to 14 of its feathers
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        if (((IAmphithereData) this).amphiMod$getSpecialVariant().equals("Shivaxi")) {
            this.spawnAtLocation(new ItemStack(AmphiItems.SHIVAXI_FEATHER.get(), this.random.nextInt(14) + 1));
        }
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            ItemStack stack = this.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
                this.setItemSlot(slot, ItemStack.EMPTY);
            }
        }
        super.dropCustomDeathLoot(source, looting, recentlyHit);
    }

    // Skeleton variants sound like skeletons
    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void amphiMod$ambientSound(CallbackInfoReturnable<SoundEvent> cir) {
        EnumAmphiType type = EnumAmphiType.fromOrdinal(((EntityAmphithere) (Object) this).getVariant());
        if (type == EnumAmphiType.SKELETON) {
            cir.setReturnValue(SoundEvents.SKELETON_AMBIENT);
        } else if (type == EnumAmphiType.WITHER_SKELETON) {
            cir.setReturnValue(SoundEvents.WITHER_SKELETON_AMBIENT);
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void amphiMod$hurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        EnumAmphiType type = EnumAmphiType.fromOrdinal(((EntityAmphithere) (Object) this).getVariant());
        if (this.isTame() && this.getOwner() != null) {
            // The original authors' easter egg: their own amphitheres sometimes make custom hurt sounds
            String owner = this.getOwner().getName().getString();
            if ((owner.equals("Nischhelm") || owner.equals("Iqury")) && this.random.nextInt(50) == 1) {
                cir.setReturnValue(AmphiSounds.HURT.get(this.random.nextInt(3)).get());
                return;
            }
            if ((owner.equals("craftoes") || owner.equals("Iqury")) && this.random.nextInt(50) == 1) {
                // 1.12.2's switch fell through and nearly always played the last sound; this picks evenly from 3..9
                cir.setReturnValue(AmphiSounds.HURT.get(3 + this.random.nextInt(7)).get());
                return;
            }
        }
        if (type == EnumAmphiType.SKELETON) {
            cir.setReturnValue(SoundEvents.SKELETON_HURT);
        } else if (type == EnumAmphiType.WITHER_SKELETON) {
            cir.setReturnValue(SoundEvents.WITHER_SKELETON_HURT);
        }
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void amphiMod$deathSound(CallbackInfoReturnable<SoundEvent> cir) {
        EnumAmphiType type = EnumAmphiType.fromOrdinal(((EntityAmphithere) (Object) this).getVariant());
        if (type == EnumAmphiType.SKELETON) {
            cir.setReturnValue(SoundEvents.SKELETON_DEATH);
        } else if (type == EnumAmphiType.WITHER_SKELETON) {
            cir.setReturnValue(SoundEvents.WITHER_SKELETON_DEATH);
        }
    }

    // Leaves: optionally solid for amphitheres
    @Inject(method = "onLeaves", at = @At("RETURN"), cancellable = true, remap = false)
    private void amphiMod$onLeaves(CallbackInfoReturnable<Boolean> cir) {
        if (AmphiConfig.CANT_PASS_LEAVES.get()) {
            cir.setReturnValue(false);
        } else if (!cir.getReturnValueZ() && AmphiConfig.PASS_DYNAMIC_BRANCHES.get()) {
            cir.setReturnValue(amphiMod$isPassableBranch(this.level(), this.blockPosition().below()));
        }
    }

    @Inject(method = "canPhaseThroughBlock", at = @At("RETURN"), cancellable = true, remap = false)
    private void amphiMod$canPhase(net.minecraft.world.level.LevelAccessor world, net.minecraft.core.BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (AmphiConfig.CANT_PASS_LEAVES.get()) {
            cir.setReturnValue(false);
        } else if (!cir.getReturnValueZ() && AmphiConfig.PASS_DYNAMIC_BRANCHES.get() && world instanceof Level level) {
            cir.setReturnValue(amphiMod$isPassableBranch(level, pos));
        }
    }

    /** Dynamic Trees branch that isn't part of the trunk (not straight above the root). */
    @Unique
    private static boolean amphiMod$isPassableBranch(Level level, net.minecraft.core.BlockPos pos) {
        return amphitheremod.compat.DynamicTreesCompat.isPassableBranch(level, pos);
    }
}
