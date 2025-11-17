package amphitheremod.mixin.common.mountfixes;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public abstract class AmphiMountFix {
    @WrapWithCondition(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;dismountRidingEntity()V"))
    private boolean www(Entity instance){
        return false;
    }

    @WrapWithCondition(method = "processInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;startRiding(Lnet/minecraft/entity/Entity;Z)Z"))
    private boolean zzz(EntityPlayer instance, Entity entity, boolean b) {
        return !entity.world.isRemote;
    }
}