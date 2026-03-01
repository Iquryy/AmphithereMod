package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tschipp.forgottenitems.items.ItemWindTalisman;
import tschipp.forgottenitems.util.FIConfig;

@Mixin(ItemWindTalisman.class)
public class AmphiWindTalisman {

    @Inject(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addVelocity(DDD)V"))
    void aaa(World world, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
        if(!player.isRiding()) return;
        if(!(player.getRidingEntity() instanceof EntityAmphithere)) return;
        EntityAmphithere amphi = (EntityAmphithere) player.getRidingEntity();
        double x = amphi.getLookVec().x;
        double y = amphi.getLookVec().y;
        double z = amphi.getLookVec().z;
        amphi.addVelocity(x * (double) FIConfig.windTalismanVelocityMultiplier, y * (double)FIConfig.windTalismanVelocityMultiplier, z * (double)FIConfig.windTalismanVelocityMultiplier);
    }
}
