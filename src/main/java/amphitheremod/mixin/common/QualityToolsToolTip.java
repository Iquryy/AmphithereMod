package amphitheremod.mixin.common;

import amphitheremod.item.amphithere_armor.ArmorBase;
import amphitheremod.item.amphithere_beak_attachment.BeakBase;
import com.google.common.collect.Multimap;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QualityToolsHelper.class)
public abstract class QualityToolsToolTip {
    @Inject(method = "generateQualityTag", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fix(ItemStack stack, boolean skipNormal, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof ArmorBase || stack.getItem() instanceof BeakBase) cir.cancel();
    }

    @Inject(method = "canReforgeWith", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fix2(ItemStack tool, ItemStack material, CallbackInfoReturnable<Boolean> cir) {
        if (tool.getItem() instanceof ArmorBase || tool.getItem() instanceof BeakBase) cir.cancel();
    }

    @Inject(method = "applyAttributesForSlot", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fix3(EntityLivingBase entity, ItemStack stack, String slot, Multimap<String, AttributeModifier> modifiersToRemove, CallbackInfo ci) {
        if (stack.getItem() instanceof ArmorBase) ci.cancel();
    }
}