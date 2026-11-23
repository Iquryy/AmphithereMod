package amphitheremod.mixin;

import amphitheremod.compat.IafCompat;
import amphitheremod.item.AmphiMaterial;
import amphitheremod.registry.AmphiItems;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Beak attachment hit effects, applied when the amphithere attacks on its own (doHurtTarget) and when its rider makes it
 * bite (the rider attack in tick). Fire: sets on fire, extra damage to ice dragons. Ice: freezes and slows, extra
 * damage to fire dragons. Lightning: chain lightning, extra damage to fire and ice dragons. Silver: extra damage to
 * undead. Elemental beaks never hit the owner.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiBeakMixin {
    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void amphiMod$onAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        amphiMod$beakEffects();
    }

    // The 4th hurt call in tick is the rider's bite on whatever they're looking at
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", ordinal = 3))
    private void amphiMod$onRiderAttack(CallbackInfo ci) {
        amphiMod$beakEffects();
    }

    @Unique
    private void amphiMod$beakEffects() {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.level().isClientSide) {
            return;
        }
        // Same victim lookup as 1.12.2: what the rider looks at, or what the amphithere itself faces
        LivingEntity looker = amphi.isVehicle() && amphi.getControllingPassenger() instanceof Player rider && amphi.isTame() ? rider : amphi;
        LivingEntity victim = DragonUtils.riderLookingAtEntity(amphi, looker, 2.5);
        if (victim == null) {
            return;
        }
        Item beak = amphi.getMainHandItem().getItem();
        if (AmphiItems.isBeak(beak, AmphiMaterial.SILVER)) {
            if (victim.getMobType() == MobType.UNDEAD) {
                victim.hurt(amphi.level().damageSources().magic(), 2.0F);
            }
            return;
        }
        if (victim == amphi.getOwner()) {
            return;
        }
        if (AmphiItems.isBeak(beak, AmphiMaterial.FIRE)) {
            if (victim instanceof EntityIceDragon) {
                victim.hurt(amphi.level().damageSources().inFire(), 13.5F);
            }
            victim.setSecondsOnFire(5);
        } else if (AmphiItems.isBeak(beak, AmphiMaterial.ICE)) {
            if (victim instanceof EntityFireDragon) {
                victim.hurt(amphi.level().damageSources().drown(), 13.5F);
            }
            EntityDataProvider.getCapability(victim).ifPresent(data -> data.frozenData.setFrozen(victim, 200));
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            victim.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
        } else if (AmphiItems.isBeak(beak, AmphiMaterial.LIGHTNING)) {
            if (victim instanceof EntityFireDragon || victim instanceof EntityIceDragon) {
                victim.hurt(amphi.level().damageSources().lightningBolt(), 6.75F);
            }
            IafCompat.chainLightning(amphi.level(), victim, amphi);
        } else {
            return;
        }
        // Elemental hits knock the victim back from the amphithere
        victim.knockback(1.0, amphi.getX() - victim.getX(), amphi.getZ() - victim.getZ());
    }
}
