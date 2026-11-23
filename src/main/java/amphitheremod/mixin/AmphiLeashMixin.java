package amphitheremod.mixin;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Leads on amphitheres stretch to 35 blocks before snapping instead of 10, since they fly ahead of you. */
@Mixin(PathfinderMob.class)
public abstract class AmphiLeashMixin {
    // ordinal 0 is the sitting-pet check; ordinal 1 is the normal "too far, drop the lead" distance
    @ModifyConstant(method = "tickLeash", constant = @Constant(floatValue = 10.0F, ordinal = 1))
    private float amphiMod$leadLength(float original) {
        return (Object) this instanceof EntityAmphithere ? 35.0F : original;
    }
}
