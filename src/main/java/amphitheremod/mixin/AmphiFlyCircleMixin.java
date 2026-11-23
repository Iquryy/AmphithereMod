package amphitheremod.mixin;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Tamed amphitheres circling a point kept picking orbit points right next to themselves and stuttered in place. Pick
 * the next orbit point at least 6 blocks ahead instead (up to 36 tries), like 1.12.2.
 */
@Mixin(targets = "com.github.alexthe666.iceandfire.entity.EntityAmphithere$AIFlyCircle")
public abstract class AmphiFlyCircleMixin {
    @Unique
    private static final double amphiMod$MIN_AHEAD_SQ = 36.0;
    @Unique
    private static final int amphiMod$MAX_STEPS = 36;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;getPositionInOrbit(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/core/BlockPos;", remap = false))
    private BlockPos amphiMod$pointAhead(EntityAmphithere amphi, Level level, BlockPos orbit, RandomSource rand) {
        BlockPos target = EntityAmphithere.getPositionInOrbit(amphi, level, orbit, rand);
        if (target == null || !amphi.isTame()) {
            return target;
        }
        // getPositionInOrbit steps the orbit angle forward each call, so calling again walks along the circle
        for (int i = 1; i < amphiMod$MAX_STEPS && target.distToCenterSqr(amphi.position()) < amphiMod$MIN_AHEAD_SQ; i++) {
            BlockPos next = EntityAmphithere.getPositionInOrbit(amphi, level, orbit, rand);
            if (next == null) {
                break;
            }
            target = next;
        }
        return target;
    }
}
