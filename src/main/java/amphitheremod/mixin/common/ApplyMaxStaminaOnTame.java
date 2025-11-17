package amphitheremod.mixin.common;

import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static amphitheremod.config.ConfigHandler.amphiStamina;

@Mixin(EntityAmphithere.class)
public class ApplyMaxStaminaOnTame {

    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setEntityState(Lnet/minecraft/entity/Entity;B)V"))
    void aaaaa(CallbackInfo ci){
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
