package amphitheremod.mixin.common.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * Smooths the escort / home orbit of tamed amphitheres in Ice and Fire's AIFlyCircle.
 *
 * Each orbit step moves the target only 10 degrees round the circle: about 0.9 blocks on the 5-block escort
 * orbit around the owner. But the AI counts a target as reached within 5 blocks, so it was always "reached",
 * a new point was picked every tick, and the target raced round the owner while the amphithere spun after it.
 *
 * This keeps stepping round the orbit until the target is at least MIN_AHEAD blocks away, just past the
 * 5-block "reached" distance. The amphithere always has a real stretch to fly, so FlyMoveHelper keeps steering
 * it and turning it to face its direction of travel. (Holding the target in place instead let it get within
 * 1 block, where FlyMoveHelper only brakes and stops updating the facing.)
 *
 * Wild amphitheres are left on Ice and Fire's own behaviour.
 *
 * getPositionInOrbit is called through reflection because the Ice and Fire jar on the compile classpath has a
 * different signature (EntityAmphithere, BlockPos) from the one in the game (EntityAmphithere, World, BlockPos,
 * Random). The redirect only matches the four-argument version, so on any other build it does nothing.
 */
@Mixin(targets = "com.github.alexthe666.iceandfire.entity.EntityAmphithere$AIFlyCircle")
public abstract class AmphiOrbitFix {

    /** The next orbit point must be at least this far away. Ice and Fire counts under 5 blocks as reached. */
    @Unique
    private static final double amphithereMod$MIN_AHEAD = 6.0D;
    /** 36 steps of 10 degrees is a full lap. */
    @Unique
    private static final int amphithereMod$MAX_STEPS = 36;

    @Unique
    private static Method amphithereMod$orbitMethod;
    @Unique
    private static boolean amphithereMod$lookedUp = false;

    @Redirect(method = "updateTask",
            at = @At(value = "INVOKE",
                    target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;getPositionInOrbit(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Lnet/minecraft/util/math/BlockPos;",
                    remap = false),
            require = 0)
    private BlockPos amphithereMod$pickPointAhead(EntityAmphithere amphi, World world, BlockPos orbit, Random rand) {
        BlockPos target = amphithereMod$nextOrbitPoint(amphi, world, orbit, rand);
        if (target == null || !amphi.isTamed()) return target;

        double minSq = amphithereMod$MIN_AHEAD * amphithereMod$MIN_AHEAD;
        for (int i = 1; i < amphithereMod$MAX_STEPS && amphithereMod$distSq(amphi, target) < minSq; i++) {
            BlockPos next = amphithereMod$nextOrbitPoint(amphi, world, orbit, rand);
            if (next == null) break;
            target = next;
        }
        return target;
    }

    /** Calls Ice and Fire's EntityAmphithere.getPositionInOrbit(EntityAmphithere, World, BlockPos, Random). */
    @Unique
    private static BlockPos amphithereMod$nextOrbitPoint(EntityAmphithere amphi, World world, BlockPos orbit, Random rand) {
        if (!amphithereMod$lookedUp) {
            amphithereMod$lookedUp = true;
            try {
                amphithereMod$orbitMethod = EntityAmphithere.class.getDeclaredMethod("getPositionInOrbit",
                        EntityAmphithere.class, World.class, BlockPos.class, Random.class);
                amphithereMod$orbitMethod.setAccessible(true);
            } catch (Throwable t) {
                LogManager.getLogger("AmphithereMod").warn("Amphithere orbit fix disabled: getPositionInOrbit not found", t);
            }
        }
        if (amphithereMod$orbitMethod == null) return null;
        try {
            return (BlockPos) amphithereMod$orbitMethod.invoke(null, amphi, world, orbit, rand);
        } catch (Throwable t) {
            return null;
        }
    }

    @Unique
    private static double amphithereMod$distSq(EntityAmphithere amphi, BlockPos pos) {
        double dx = pos.getX() + 0.5D - amphi.posX;
        double dy = pos.getY() + 0.5D - amphi.posY;
        double dz = pos.getZ() + 0.5D - amphi.posZ;
        return dx * dx + dy * dy + dz * dz;
    }
}