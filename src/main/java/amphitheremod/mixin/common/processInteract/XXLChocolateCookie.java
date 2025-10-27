package amphitheremod.mixin.common.processInteract;

import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static amphitheremod.handlers.ModItemRegistry.XXL_CHOCOLATE_COOKIE;

@Mixin(EntityAmphithere.class)
public abstract class XXLChocolateCookie {
    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void preventHealingAtFullHealth(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = player.getHeldItem(hand);
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!(itemstack.getItem() == XXL_CHOCOLATE_COOKIE)) return;
        if (amphi.isInLove()) return;
        amphi.setInLove(player);
        amphi.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
        amphi.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, ConfigHandler.xxlCookieBuffs.xxlCookieBuffDuration * 20, ConfigHandler.xxlCookieBuffs.xxlCookieEffectLevel));
        amphi.setInLove(player);
        cir.setReturnValue(true);
        if (player.isCreative()) return;
        itemstack.shrink(1);
    }
}