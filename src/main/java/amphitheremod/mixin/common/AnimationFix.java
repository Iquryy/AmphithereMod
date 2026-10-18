package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = EntityAmphithere.class)
public abstract class AnimationFix extends Entity {

    public AnimationFix(World worldIn) {
        super(worldIn);
    }

    // Landing pose blends in at 1 per tick instead of 2 ("groundProgress += 2.0F").
    @ModifyConstant(method = "onLivingUpdate", constant = @Constant(floatValue = 2.0F, ordinal = 2))
    private float ggg(float originalValue) {
        return 1.0F;
    }

    // Removed:
    // - UpDownFix: it injected right before Ice and Fire's own "rotationPitch = rider.rotationPitch / 2" line,
    //   so its value was overwritten straight away. Its inverted pitch was a workaround for the base pitch
    //   buffer tilting the wrong way, which the ChainBuffer mixin now handles for ridden amphitheres.
    // - 10.0F -> 0 (ordinal 2) and 0.5F -> 1 (ordinal 3): these hit the pitch buffer's decrement and divisor.
    //   With a decrement of 0 the body tilt never returned to level, so any amphithere not ridden by its
    //   owner kept tipping as it flew and could lock up to 90 degrees nose-up or nose-down.
}