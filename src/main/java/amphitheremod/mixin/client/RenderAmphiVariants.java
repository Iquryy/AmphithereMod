package amphitheremod.mixin.client;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.client.render.entity.RenderAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderAmphithere.class)
public class RenderAmphiVariants {
    @ModifyReturnValue(
            method = "getEntityTexture(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;)Lnet/minecraft/util/ResourceLocation;",
            at = @At("RETURN"),
            remap = false
    )
    public ResourceLocation amphimod_changeTexture(ResourceLocation original, @Local(argsOnly = true) EntityAmphithere amphithere) {
        String name = TextFormatting.getTextWithoutFormattingCodes(amphithere.getName());
        if(name != null)
            switch (name) {
                case "Risky": return EnumAmphiType.RAINBOW.getTexture(amphithere.isBlinking());
            }

        int amphiVariant = amphithere.getVariant();
        boolean isBlinking = amphithere.isBlinking();
        if(amphiVariant < EnumAmphiType.values().length)
            return EnumAmphiType.values()[amphiVariant].getTexture(isBlinking);
        else return EnumAmphiType.GREEN.getTexture(isBlinking);
    }
}