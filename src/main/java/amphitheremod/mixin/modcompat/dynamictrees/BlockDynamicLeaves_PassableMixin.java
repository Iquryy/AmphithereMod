package amphitheremod.mixin.modcompat.dynamictrees;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockDynamicLeaves.class)
public abstract class BlockDynamicLeaves_PassableMixin {

    @ModifyExpressionValue(
            method = "onEntityCollision",
            at = @At(value = "FIELD", target = "Lcom/ferreusveritas/dynamictrees/blocks/BlockDynamicLeaves;passableLeavesModLoaded:Z", remap = false)
    )
    public boolean amphiMod_dynamicTreesBlockDynamicLeaves_onEntityCollisionAmphithere(boolean original, @Local(argsOnly = true) Entity entity){
        return original || entity instanceof EntityAmphithere;
    }
}
