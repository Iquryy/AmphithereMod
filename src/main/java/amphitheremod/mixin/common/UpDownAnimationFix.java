package amphitheremod.mixin.common;

import amphitheremod.util.enumm.IceAndFireUtil;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAmphithere.class)
public abstract class UpDownAnimationFix extends Entity {
    public UpDownAnimationFix(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;rotationPitch:F", shift = At.Shift.AFTER))
    private void UpDownFix(CallbackInfo ci) {
        if (!(IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT)) return;
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        if (!(amphithere.getControllingPassenger() instanceof EntityPlayer)) return;
        EntityPlayer passenger = (EntityPlayer) amphithere.getControllingPassenger();
        double motionX = amphithere.motionX;
        double motionZ = amphithere.motionZ;
        float horizontalSpeed = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        float dynamicDivisor = 2.0f + (horizontalSpeed * 4.0f);
        float playerPitch = passenger.rotationPitch;
        amphithere.rotationPitch = (playerPitch * -1.9f) / dynamicDivisor;
    }
}