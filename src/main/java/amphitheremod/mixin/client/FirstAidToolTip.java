package amphitheremod.mixin.client;

import amphitheremod.item.amphithere_armor.ArmorBase;
import ichttt.mods.firstaid.client.ClientEventHandler;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEventHandler.class)
public abstract class FirstAidToolTip {
    @Inject(method = "tooltipItems", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fix(ItemTooltipEvent event, CallbackInfo ci) {
        if (event.getItemStack().getItem() instanceof ArmorBase) ci.cancel();
    }
}