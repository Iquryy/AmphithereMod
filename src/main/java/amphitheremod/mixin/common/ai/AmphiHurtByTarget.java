package amphitheremod.mixin.common.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAmphithere.class)
public abstract class AmphiHurtByTarget {

    @Unique private boolean amphithereMod$wasDefending = false;

    @Inject(method = "onUpdate", at = @At("TAIL"))
    void onUpdate(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.world.isRemote || !amphi.isTamed() || amphi.isBeingRidden()) return;
        EntityPlayer owner = (EntityPlayer) amphi.getOwner();
        if (owner == null) return;
        EntityLivingBase revengeTarget = amphi.getRevengeTarget();
        boolean shouldStartDefending = amphi.getCommand() == 1 && !amphithereMod$wasDefending && amphi.getDistanceSq(owner) >= 121.0 && revengeTarget != null && revengeTarget.isEntityAlive();
        if (shouldStartDefending) {
            amphithereMod$wasDefending = true;
            amphi.setCommand(0);
            amphi.setAttackTarget(revengeTarget);
        }
        boolean shouldStopDefending = amphithereMod$wasDefending && (revengeTarget == null || !revengeTarget.isEntityAlive());
        if (shouldStopDefending) {
            amphithereMod$wasDefending = false;
            amphi.setAttackTarget(null);
            amphi.setCommand(1);
        }
    }
}