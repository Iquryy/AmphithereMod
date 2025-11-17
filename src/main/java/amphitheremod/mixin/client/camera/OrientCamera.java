package amphitheremod.mixin.client.camera;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityRenderer.class)
public class OrientCamera {
    @ModifyConstant(method = "orientCamera", constant = @Constant(intValue = 2))
    int ggg(int constant){
        return 5;
    }
}
