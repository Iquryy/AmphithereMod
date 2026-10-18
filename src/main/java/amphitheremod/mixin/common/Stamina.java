package amphitheremod.mixin.common;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.IceAndFireUtil;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static amphitheremod.config.ConfigHandler.amphiStamina;

@Mixin(value = EntityAmphithere.class)
public abstract class Stamina {

    /** How many ticks a single wing-flap drain is spread over (matches the 30-tick flap interval). */
    @Unique
    private static final float amphithereMod$FLAP_SPREAD_TICKS = 30F;
    /** How many ticks regen takes to ramp from 0 to full speed after the cooldown ends. */
    @Unique
    private static final float amphithereMod$REGEN_RAMP_TICKS = 20F;

    /** Flap drain that has been triggered but not yet taken out of the stamina bar. */
    @Unique
    private float amphithereMod$pendingFlapDrain = 0F;
    /** 0..1 multiplier that eases regen in instead of switching it on at full speed. */
    @Unique
    private float amphithereMod$regenRamp = 0F;

    // STAMINA
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.world.isRemote) return;
        if (!amphi.isTamed()) return;

        IAmphithereData data = (IAmphithereData) amphi;
        float stamina = data.amphiMod_master$getStamina();
        float maxStamina = data.amphiMod_master$getMaxStamina();
        float staminaDrainPerSecond = 0;
        float staminaRegenPerSecond = 0;

        boolean isGliding = !amphi.onGround && amphi.flapProgress <= 4;
        boolean onDive = !amphi.onGround && amphi.diveProgress > 2;
        boolean onGround = amphi.onGround;
        boolean flying = amphi.isFlying();
        // Stamina only limits an amphithere while a player rides it. On its own (following, escorting, wandering)
        // it never drains and is never forced down; it just keeps regenerating by the usual rules.
        boolean playerRider = amphithereMod$getRider(amphi) != null;

        // Count the cooldown down first, so anything below that resets it actually holds it.
        int staminaCooldown = data.amphiMod_master$getStaminaCD();
        if (staminaCooldown > 0) staminaCooldown--;

        // Continuous drain while actively flying (not gliding or diving)
        if (playerRider && stamina > 0 && flying && !isGliding && !onDive) {
            staminaDrainPerSecond += ConfigHandler.amphiStamina.staminaDrain.flyingDrainPerSecond;
            staminaCooldown = ConfigHandler.amphiStamina.staminaRegCDFlapping * 20;
        }

        // Wing flap: queue the drain instead of removing it all in one tick
        float flapDrain = ConfigHandler.amphiStamina.staminaDrain.flapDrain;
        if (playerRider && flying && amphi.flapProgress > 6 && amphi.ticksExisted % 30 == 0) {
            amphithereMod$pendingFlapDrain += flapDrain;
            staminaCooldown = ConfigHandler.amphiStamina.staminaRegCDFlapping * 20;
        }
        if (!playerRider) amphithereMod$pendingFlapDrain = 0F;

        // Spend queued flap drain gradually; steady flapping becomes a constant drain rate
        if (amphithereMod$pendingFlapDrain > 0) {
            float step = Math.min(amphithereMod$pendingFlapDrain, Math.max(flapDrain / amphithereMod$FLAP_SPREAD_TICKS, 0.01F));
            amphithereMod$pendingFlapDrain -= step;
            staminaDrainPerSecond += step * 20;
        }

        // Regen, eased in after the cooldown ends
        if (stamina < maxStamina && staminaCooldown == 0) {
            if (onGround) {
                staminaRegenPerSecond += maxStamina * ConfigHandler.amphiStamina.staminaRegeneration.maxStaminaRegen;
                staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.onGround;
            }
            if (isGliding)
                staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.gliding;
            if (onDive)
                staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.diving;
            if (amphi.isInLove())
                staminaRegenPerSecond *= ConfigHandler.amphiStamina.staminaRegeneration.inLove;

            amphithereMod$regenRamp = Math.min(1F, amphithereMod$regenRamp + 1F / amphithereMod$REGEN_RAMP_TICKS);
            staminaRegenPerSecond *= amphithereMod$regenRamp;
        } else {
            amphithereMod$regenRamp = 0F;
        }

        // Apply everything once per tick, clamped to [0, max]
        float newStamina = MathHelper.clamp(stamina + (staminaRegenPerSecond - staminaDrainPerSecond) / 20F, 0F, maxStamina);
        data.amphiMod_master$setStamina(newStamina);

        // On stamina run out (only with a rider; see playerRider above)
        if (playerRider && newStamina <= 0 && !onGround && flying) {
            amphi.setFlying(false);
            amphi.up(false);
            if (!(IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT)) {
                // Dividing by 0.15 multiplies speed by about 6.7, which flung a climbing amphithere upwards.
                // Stop any climb first, so only a fall gets sped up.
                if (amphi.motionY > 0) amphi.motionY = 0;
                amphi.motionY /= 0.15;
            }
            amphithereMod$pendingFlapDrain = 0F;
            // Stamina regen cooldown after running out of stamina while flying
            staminaCooldown = ConfigHandler.amphiStamina.staminaCDWhenStaminaExhaust * 20;
        }

        data.amphiMod_master$setStaminaCD(staminaCooldown);

        if (!ConfigHandler.amphiStamina.staminaDebug) return;
        Entity rider = amphithereMod$getRider(amphi);
        if (!(rider instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) rider;
        if (amphi.ticksExisted % 5 == 0)
            player.sendMessage(new TextComponentString(String.format("Stamina " + TextFormatting.AQUA + "%.1f" + TextFormatting.RESET + "/%.0f - regen " + TextFormatting.YELLOW + "%.1f" + TextFormatting.RESET + "+/s - drain " + TextFormatting.RED + "%.1f" + TextFormatting.RESET + "/s - Cooldown %st", newStamina, maxStamina, staminaRegenPerSecond, staminaDrainPerSecond, "" + TextFormatting.GRAY + staminaCooldown + TextFormatting.RESET)));
    }

    // Prevents amphi jump on 0-5 stamina.
    // Only the jump input is blocked. The old version cancelled the whole updateClientControls method, so at low
    // stamina the descend, attack and dismount keys stopped working and the control packet was never sent. The
    // server then kept the last "up" state, and its "up() && !isFlying() -> take off" check relaunched the
    // amphithere every time stamina ran out. It also called setFlying(false) on the client only, which made the
    // client and server disagree about whether it was flying.
    @Redirect(method = "updateClientControls", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;up(Z)V"), remap = false)
    private void preventJumpOnNoStamina(EntityAmphithere amphi, boolean jumpPressed) {
        IAmphithereData data = (IAmphithereData) amphi;
        amphi.up(jumpPressed && data.amphiMod_master$getStamina() > 5);
    }

    // Prevent amphi from launching into space if player takes off wihle it's in love
    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;isInLove()Z"))
    private boolean redirectInLoveCheckForFlight(EntityAmphithere instance) {
        return instance.isInLove() && !instance.isBeingRidden();
    }

    @Unique
    @Nullable
    public Entity amphithereMod$getRider(EntityAmphithere amphi) {
        for (Entity passenger : amphi.getPassengers()) {
            if (passenger instanceof EntityPlayer)
                return passenger;
        }
        return null;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setEntityState(Lnet/minecraft/entity/Entity;B)V"))
    void ppp(CallbackInfo ci) {
        amphithereMod$setNewMaxStamina((EntityAmphithere) (Object) this);
    }

    @Unique
    private void amphithereMod$setNewMaxStamina(EntityAmphithere amphithere) {
        IAmphithereData data = (IAmphithereData) amphithere;
        float maxHealth = amphithere.getMaxHealth();
        float maxStamina = data.amphiMod_master$getMaxStamina();
        double staminaModifier = 0;
        if (amphiStamina.maxStaminaMulti > 0)
            staminaModifier = (double) amphithere.getRNG().nextInt(amphiStamina.maxStaminaMulti) / 100.0;
        int newMaxStamina = (int) ((maxHealth / 2 + maxStamina) * (1 + staminaModifier));
        data.amphiMod_master$setMaxStamina(newMaxStamina);
        data.amphiMod_master$setStamina(newMaxStamina);
    }
}