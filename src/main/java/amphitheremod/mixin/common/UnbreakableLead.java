package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityCreature.class)
public class UnbreakableLead {
    @ModifyConstant(method = "updateLeashedState", constant = @Constant(floatValue = 10.0F, ordinal = 1))
    float ggg(float constant){
        Entity entity = (Entity)(Object)this;
        if(entity instanceof EntityAmphithere)
            return 35;
        return constant;
    }
}
