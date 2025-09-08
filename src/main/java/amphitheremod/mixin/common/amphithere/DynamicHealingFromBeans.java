package amphitheremod.mixin.common.amphithere;

import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityAmphithere.class)
public abstract class DynamicHealingFromBeans {
    @ModifyConstant(method = "processInteract", constant = @Constant(floatValue = 5.0F), remap = false)
    private float FeedingBeans(float constant){
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        float maxHp = amphi.getMaxHealth();
        return amphimod$simpleCaclHeal(maxHp);
    }

    @Unique
    private static float amphimod$simpleCaclHeal(float maxHp) {
        return maxHp / ConfigHandler.general.amphithereHealDivisor;
    }
}