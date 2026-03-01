package amphitheremod.mixin.common;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.IceAndFireUtil;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
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

    // STAMINA
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.world.isRemote) {
            IAmphithereData data = (IAmphithereData) amphi;
            if (amphi.isTamed()) {
                int staminaCooldown = data.amphiMod_master$getStaminaCD();
                float stamina = data.amphiMod_master$getStamina();
                float maxStamina = data.amphiMod_master$getMaxStamina();
                float staminaDrainPerSecond = 0;
                float staminaRegenPerSecond = 0;

                boolean isGliding = !amphi.onGround && amphi.flapProgress <= 4;
                boolean onDive = !amphi.onGround && amphi.diveProgress > 2;
                boolean onGround = amphi.onGround;

                if (stamina > 0) {
                    // Passive stamina drain when flying and not gliding, diving, on ground
                    if (!isGliding && !onDive && amphi.isFlying()) {
                        staminaDrainPerSecond += ConfigHandler.amphiStamina.staminaDrain.flyingDrainPerSecond;
                        data.amphiMod_master$setStamina(stamina - staminaDrainPerSecond / 20);
                        if (!onGround && amphi.isFlying())
                            data.amphiMod_master$setStaminaCD(ConfigHandler.amphiStamina.staminaRegCDFlapping * 20);
                    }
                }

                // Degrade cooldown
                if (staminaCooldown > 0)
                    data.amphiMod_master$setStaminaCD(staminaCooldown - 1);

                // On stamina run out
                if (stamina <= 0 && !onGround && amphi.isFlying()) {
                    amphi.setFlying(false);
                    amphi.up(false);
                    if (!(IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT))
                        amphi.motionY /= 0.15;
                    // Stamina regen cooldown after running out of stamina while flying
                    data.amphiMod_master$setStaminaCD(ConfigHandler.amphiStamina.staminaCDWhenStaminaExhaust * 20);
                }

                if (stamina < maxStamina && staminaCooldown == 0) {
                    // Regen % max hp
                    if (onGround)
                        staminaRegenPerSecond += maxStamina * ConfigHandler.amphiStamina.staminaRegeneration.maxStaminaRegen;
                    // When in love
                    if (onGround)
                        staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.onGround;
                    if (isGliding)
                        staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.gliding;
                    if (onDive)
                        staminaRegenPerSecond += ConfigHandler.amphiStamina.staminaRegeneration.diving;
                    if (amphi.isInLove())
                        staminaRegenPerSecond *= ConfigHandler.amphiStamina.staminaRegeneration.inLove;
                    data.amphiMod_master$setStamina(stamina + staminaRegenPerSecond / 20);
                }

                // Wing flap stamina drain
                if (amphi.isFlying()) {
                    if (amphi.flapProgress > 6)
                        if (amphi.ticksExisted % 30 == 0) {
                            data.amphiMod_master$setStamina(stamina - ConfigHandler.amphiStamina.staminaDrain.flapDrain);
                            data.amphiMod_master$setStaminaCD(ConfigHandler.amphiStamina.staminaRegCDFlapping * 20);
                        }
                }

                if (amphithereMod$getRider(amphi) == null) return;
                if (!(amphithereMod$getRider(amphi) instanceof EntityPlayer)) return;
                if (!ConfigHandler.amphiStamina.staminaDebug) return;
                EntityPlayer player = (EntityPlayer) amphithereMod$getRider(amphi);
                if (player == null) return;
                if (amphi.ticksExisted % 5 == 0)
                    player.sendMessage(new TextComponentString(String.format("Stamina regen " + TextFormatting.YELLOW + "%.1f" + TextFormatting.RESET + "+/s - Stamina drain " + TextFormatting.RED + "%.1f" + TextFormatting.RESET + "/s - Cooldown %st", staminaRegenPerSecond, staminaDrainPerSecond, "" + TextFormatting.GRAY + staminaCooldown + TextFormatting.RESET)));
            }
        }
    }

    // Prevents amphi jump on 0-5 stamina
    @Inject(method = "updateClientControls", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;up(Z)V"), cancellable = true, remap = false)
    private void preventJumpOnNoStamina(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        IAmphithereData data = (IAmphithereData) amphi;
        if (data.amphiMod_master$getStamina() <= 5) {
            amphi.setFlying(false);
            amphi.up(false);
            ci.cancel();
        } else
            data.amphiMod_master$setStaminaCD(ConfigHandler.amphiStamina.staminaRegCDFlapping * 20);
    }

    // Prevent amphi from launching into space if player takes off wihle it's in love
    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;isInLove()Z"))
    private boolean redirectInLoveCheckForFlight(EntityAmphithere instance) {
        return instance.isInLove() && !instance.isBeingRidden();
    }

    @Unique
    @Nullable
    public Entity amphithereMod$getRider(EntityAmphithere amphi) {
        for(Entity passenger : amphi.getPassengers()) {
            if (passenger instanceof EntityPlayer)
                return passenger;
        }
        return null;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setEntityState(Lnet/minecraft/entity/Entity;B)V"))
    void ppp(CallbackInfo ci){
        amphithereMod$setNewMaxStamina((EntityAmphithere)(Object)this);
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