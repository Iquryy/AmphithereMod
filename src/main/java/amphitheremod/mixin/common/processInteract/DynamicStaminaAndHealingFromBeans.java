package amphitheremod.mixin.common.processInteract;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public abstract class DynamicStaminaAndHealingFromBeans {
    @WrapOperation(method = "processInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;heal(F)V"))
    private void sssssssss(EntityAmphithere amphithere, float v, Operation<Void> original) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        IAmphithereData data = (IAmphithereData) amphi;
        data.amphiMod_master$setStamina(data.amphiMod_master$getStamina() + data.amphiMod_master$getMaxStamina() * ConfigHandler.amphiStamina.staminaRegeneration.staminaBeanAdd);
        original.call(amphithere, amphithere.getMaxHealth() / ConfigHandler.mixins.amphithereHealDivisor);
    }
}