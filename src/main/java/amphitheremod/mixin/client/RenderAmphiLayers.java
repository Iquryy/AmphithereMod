package amphitheremod.mixin.client;

import amphitheremod.client.layer.*;
import amphitheremod.client.layer.armor.*;
import com.github.alexthe666.iceandfire.client.render.entity.RenderAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderAmphithere.class)
public abstract class RenderAmphiLayers extends RenderLiving<EntityAmphithere> {
    public RenderAmphiLayers(RenderManager renderManagerIn) {
        super(renderManagerIn, null, 0F);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;)V", at = @At("RETURN"), remap = false)
    private void addLayer(RenderManager renderManager, CallbackInfo ci) {
        RenderAmphithere amphi = (RenderAmphithere) (Object) this;
        this.addLayer(new LayerAmphithereEyes(amphi));
        this.addLayer(new LayerAmphithereGlow(amphi));
        this.addLayer(new LayerAmphithereGender(amphi));
        this.addLayer(new LayerAmphithereWingPattern(amphi));
        this.addLayer(new LayerAmphithereHeadless(amphi));
        this.addLayer(new LayerAmphithereBeakAttachment(amphi));
        this.addLayer(new LayerAmphithereHeadArmor(amphi));
        this.addLayer(new LayerAmphithereTailArmor(amphi));
        this.addLayer(new LayerAmphithereWingArmor(amphi));
        this.addLayer(new LayerAmphithereBodyArmor(amphi));
        this.addLayer(new LayerAmphithereHat(amphi));
    }
}
