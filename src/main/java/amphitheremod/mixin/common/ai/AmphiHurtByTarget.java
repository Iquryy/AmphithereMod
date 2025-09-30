package amphitheremod.mixin.common.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAmphithere.class)
public abstract class AmphiHurtByTarget {

    @Inject(method = "onUpdate", at = @At(value = "HEAD"))
    void onUpdate(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.getCommand() != 1 || !amphi.isTamed() || amphi.isBeingRidden()) return;
        EntityPlayer owner = (EntityPlayer) amphi.getOwner();
        if (owner == null) return;
        if (amphi.getDistanceSq(owner) >= 121.0) {
            EntityLivingBase target = amphi.getAttackTarget();
            if (target != null && target != owner)
                amphi.setCommand(2);
        }
    }
}