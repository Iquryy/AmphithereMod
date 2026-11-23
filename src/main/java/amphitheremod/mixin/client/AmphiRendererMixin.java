package amphitheremod.mixin.client;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.client.render.entity.RenderAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Every variant's texture (with blink frames) instead of Ice and Fire's 5. Naming one "Risky" makes it rainbow. */
@Mixin(RenderAmphithere.class)
public abstract class AmphiRendererMixin {
    // The refmap can't map an override with the entity's own type in its signature, so both the dev name and the SRG
    // name are listed with remap off (only the one that exists matches)
    @Inject(method = {"getTextureLocation(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;)Lnet/minecraft/resources/ResourceLocation;",
            "m_5478_(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;)Lnet/minecraft/resources/ResourceLocation;"},
            at = @At("HEAD"), cancellable = true, remap = false)
    private void amphiMod$variantTexture(EntityAmphithere amphi, CallbackInfoReturnable<ResourceLocation> cir) {
        boolean blink = amphi.isBlinking();
        if (amphi.hasCustomName() && "risky".equals(ChatFormatting.stripFormatting(amphi.getCustomName().getString().toLowerCase()))) {
            cir.setReturnValue(EnumAmphiType.RAINBOW.getTexture(blink));
            return;
        }
        cir.setReturnValue(EnumAmphiType.fromOrdinal(amphi.getVariant()).getTexture(blink));
    }
}
