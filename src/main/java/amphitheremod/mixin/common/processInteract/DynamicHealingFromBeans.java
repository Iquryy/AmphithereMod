package amphitheremod.mixin.common.processInteract;

import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public abstract class DynamicHealingFromBeans {
    @WrapOperation(method = "processInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;heal(F)V"))
    private void amphimod_varyCocoaBeanHealAmount(EntityAmphithere amphithere, float v, Operation<Void> original) {
        original.call(amphithere, amphimod$simpleCaclHeal(amphithere.getMaxHealth()));
    }

    @Unique
    private static float amphimod$simpleCaclHeal(float maxHp) {
        return maxHp / ConfigHandler.mixins.amphithereHealDivisor;
    }
}