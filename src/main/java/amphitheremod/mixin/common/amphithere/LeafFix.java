package amphitheremod.mixin.common.amphithere;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = EntityAmphithere.class, remap = false)
public class LeafFix {

    /**
     * @author iqy
     * @reason leaf fix
     */
    @Overwrite
    public boolean onLeaves() {
        return false;
    }

    /**
     * @author iqy
     * @reason leaf fix
     */
    @Overwrite
    public boolean canPhaseThroughBlock(IBlockState state, World world, BlockPos pos) {
        return false;
    }
}
