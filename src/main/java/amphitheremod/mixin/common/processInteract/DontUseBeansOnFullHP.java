package amphitheremod.mixin.common.processInteract;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public abstract class DontUseBeansOnFullHP {
    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true, order = 30)
    private void zzzzzz(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.DYE && itemstack.getItemDamage() == EnumDyeColor.BROWN.getDyeDamage()) {
            if ((amphi.getHealth() >= amphi.getMaxHealth())) {
                cir.setReturnValue(true);
            }
        }
    }
}