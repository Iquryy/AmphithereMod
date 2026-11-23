package amphitheremod.compat;

import amphitheremod.AmphithereMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Dynamic Trees support without a compile-time dependency: TreeHelper.isBranch / findRootNode are looked up once by
 * reflection when Dynamic Trees is installed. A branch counts as passable when it isn't straight above the tree's
 * root, so amphitheres fly through side branches but still hit the trunk.
 */
public final class DynamicTreesCompat {
    private static boolean looked;
    private static MethodHandle isBranch;
    private static MethodHandle findRootNode;

    private DynamicTreesCompat() {
    }

    private static void lookUp() {
        looked = true;
        if (!ModList.get().isLoaded("dynamictrees")) {
            return;
        }
        try {
            Class<?> treeHelper = Class.forName("com.ferreusveritas.dynamictrees.api.TreeHelper");
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            isBranch = lookup.findStatic(treeHelper, "isBranch", MethodType.methodType(boolean.class, BlockState.class));
            findRootNode = lookup.findStatic(treeHelper, "findRootNode", MethodType.methodType(BlockPos.class, Level.class, BlockPos.class));
        } catch (ReflectiveOperationException e) {
            AmphithereMod.LOGGER.warn("Dynamic Trees found but TreeHelper couldn't be looked up; amphitheres won't pass through branches", e);
            isBranch = null;
        }
    }

    public static boolean isPassableBranch(Level level, BlockPos pos) {
        if (!looked) {
            lookUp();
        }
        if (isBranch == null) {
            return false;
        }
        try {
            if (!(boolean) isBranch.invokeExact(level.getBlockState(pos))) {
                return false;
            }
            BlockPos root = (BlockPos) findRootNode.invokeExact(level, pos);
            return root != null && (root.getX() != pos.getX() || root.getZ() != pos.getZ());
        } catch (Throwable t) {
            return false;
        }
    }
}
