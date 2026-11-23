package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stamina for tamed amphitheres ridden by a player. Powered flight and wing flaps drain it; gliding, diving and standing
 * regenerate it after a short delay. At 0 the amphithere drops out of the air and can't take off again until it has a
 * little stamina back. Numbers are the same as 1.12.2 (all in the stamina config section).
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiStaminaMixin {
    /** Ticks a flap's drain is spread over, so the bar slides down instead of jumping. */
    @Unique
    private static final float amphiMod$FLAP_SPREAD_TICKS = 30.0F;
    /** Stamina needed to take off. */
    @Unique
    private static final float amphiMod$TAKE_OFF_STAMINA = 5.0F;
    @Unique
    private float amphiMod$pendingFlapDrain = 0.0F;
    @Unique
    private float amphiMod$regenRamp = 0.0F;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void amphiMod$tickStamina(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.level().isClientSide || !amphi.isTame() || !AmphiConfig.ENABLE_STAMINA.get()) {
            return;
        }
        IAmphithereData data = (IAmphithereData) amphi;
        float stamina = data.amphiMod$getStamina();
        float maxStamina = data.amphiMod$getMaxStamina();
        float drainPerSecond = 0.0F;
        float regenPerSecond = 0.0F;
        boolean onGround = amphi.onGround();
        boolean gliding = !onGround && amphi.flapProgress <= 4.0F;
        boolean diving = !onGround && amphi.diveProgress > 2.0F;
        boolean flying = amphi.isFlying();
        Player rider = amphiMod$getPlayerRider(amphi);
        boolean playerRider = rider != null;
        int cooldown = data.amphiMod$getStaminaCooldown();
        if (cooldown > 0) {
            cooldown--;
        }
        // Powered flight (not gliding or diving) drains steadily and resets the regen delay
        if (playerRider && stamina > 0.0F && flying && !gliding && !diving) {
            drainPerSecond += AmphiConfig.FLYING_DRAIN.get().floatValue();
            cooldown = AmphiConfig.REGEN_DELAY.get() * 20;
        }
        // Each wing beat (once per 30 ticks while the wings are up) queues a flap's worth of drain
        float flapDrain = AmphiConfig.FLAP_DRAIN.get().floatValue();
        if (playerRider && flying && amphi.flapProgress > 6.0F && amphi.tickCount % 30 == 0) {
            this.amphiMod$pendingFlapDrain += flapDrain;
            cooldown = AmphiConfig.REGEN_DELAY.get() * 20;
        }
        if (!playerRider) {
            this.amphiMod$pendingFlapDrain = 0.0F;
        }
        if (this.amphiMod$pendingFlapDrain > 0.0F) {
            float step = Math.min(this.amphiMod$pendingFlapDrain, Math.max(flapDrain / amphiMod$FLAP_SPREAD_TICKS, 0.01F));
            this.amphiMod$pendingFlapDrain -= step;
            drainPerSecond += step * 20.0F;
        }
        if (stamina < maxStamina && cooldown == 0) {
            if (onGround) {
                regenPerSecond += maxStamina * AmphiConfig.REGEN_FROM_MAX.get().floatValue();
                regenPerSecond += AmphiConfig.REGEN_GROUND.get().floatValue();
            }
            if (gliding) {
                regenPerSecond += AmphiConfig.REGEN_GLIDING.get().floatValue();
            }
            if (diving) {
                regenPerSecond += AmphiConfig.REGEN_DIVING.get().floatValue();
            }
            if (amphi.isInLove()) {
                regenPerSecond *= AmphiConfig.REGEN_IN_LOVE.get().floatValue();
            }
            // Regen eases in over a second instead of starting at full speed
            this.amphiMod$regenRamp = Math.min(1.0F, this.amphiMod$regenRamp + 0.05F);
            regenPerSecond *= this.amphiMod$regenRamp;
        } else {
            this.amphiMod$regenRamp = 0.0F;
        }
        float newStamina = Mth.clamp(stamina + (regenPerSecond - drainPerSecond) / 20.0F, 0.0F, maxStamina);
        data.amphiMod$setStamina(newStamina);
        if (playerRider && newStamina <= 0.0F && !onGround && flying) {
            // Exhausted: stop flying so it falls (Ice and Fire's travel() applies gravity when not flying)
            amphi.setFlying(false);
            amphi.up(false);
            this.amphiMod$pendingFlapDrain = 0.0F;
            cooldown = AmphiConfig.EXHAUSTION_COOLDOWN.get() * 20;
        }
        data.amphiMod$setStaminaCooldown(cooldown);
        if (AmphiConfig.STAMINA_DEBUG.get() && rider != null && amphi.tickCount % 5 == 0) {
            rider.displayClientMessage(Component.literal(String.format("Stamina %s%.1f%s/%.0f - regen %s%.1f%s+/s - drain %s%.1f%s/s - Cooldown %s%dt",
                    ChatFormatting.AQUA, newStamina, ChatFormatting.RESET, maxStamina, ChatFormatting.YELLOW, regenPerSecond, ChatFormatting.RESET,
                    ChatFormatting.RED, drainPerSecond, ChatFormatting.RESET, ChatFormatting.GRAY, cooldown)), false);
        }
    }

    // Can't climb or take off with almost no stamina. isGoingUp is read by the riding client's tickRidden and by aiStep's
    // take-off check, and stamina is synced, so both sides agree.
    @Inject(method = "isGoingUp", at = @At("RETURN"), cancellable = true, remap = false)
    private void amphiMod$noClimbWhenExhausted(CallbackInfoReturnable<Boolean> cir) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (cir.getReturnValueZ() && AmphiConfig.ENABLE_STAMINA.get() && amphi.isTame() && amphiMod$getPlayerRider(amphi) != null
                && ((IAmphithereData) amphi).amphiMod$getStamina() <= amphiMod$TAKE_OFF_STAMINA) {
            cir.setReturnValue(false);
        }
    }

    // Taming happens right after Ice and Fire broadcasts the hearts effect (event 45): roll the new max stamina then
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private void amphiMod$onTamed(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        IAmphithereData data = (IAmphithereData) amphi;
        int variance = AmphiConfig.MAX_STAMINA_VARIANCE.get();
        double modifier = variance > 0 ? amphi.getRandom().nextInt(variance) / 100.0 : 0.0;
        float newMax = (int) ((amphi.getMaxHealth() / 2.0F + AmphiConfig.MAX_STAMINA.get()) * (1.0 + modifier));
        data.amphiMod$setMaxStamina(newMax);
        data.amphiMod$setStamina(newMax);
    }

    @Unique
    @Nullable
    private static Player amphiMod$getPlayerRider(EntityAmphithere amphi) {
        for (Entity passenger : amphi.getPassengers()) {
            if (passenger instanceof Player player) {
                return player;
            }
        }
        return null;
    }
}
