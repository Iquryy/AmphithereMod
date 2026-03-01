package amphitheremod.mixin.common.processInteract;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static amphitheremod.handlers.ModItemRegistry.ANTI_COTH_COOKIE;

@Mixin(EntityAmphithere.class)
public class AntiCothCookie {
    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true, order = 20)
    private void preventHealingAtFullHealth(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = player.getHeldItem(hand);
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!(itemstack.getItem() == ANTI_COTH_COOKIE)) return;
        if(amphi.getEntityData().getBoolean("CothImmunie")) return;
        amphi.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
        amphi.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F, 1.5F);
        amphi.getEntityData().setBoolean("CothImmunie", true);
        amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(amphi.getMaxHealth() / 1.1111f);
        amphi.setHealth(amphi.getMaxHealth());
        cir.setReturnValue(true);
        if (player.isCreative()) return;
        itemstack.shrink(1);
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    void aaa(CallbackInfo ci){
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        Potion cothPotion = Potion.getPotionFromResourceLocation("srparasites:coth");
        if(cothPotion == null) return;
        if(amphi.getEntityData().getBoolean("CothImmunie"))
            amphi.removePotionEffect(cothPotion);
    }
}
