package amphitheremod.mixin.client;

import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IFChainBuffer.class)
public class ChainBuffer {


    @Shadow (remap = false) private float pitchVariation;
    @Shadow (remap = false) private float prevPitchVariation;

    @Inject(method = "calculateChainWaveBuffer(FIFFLnet/minecraft/entity/EntityLivingBase;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void stopBufferForRiders(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere && entity.getControllingPassenger() instanceof EntityPlayer) {
            this.pitchVariation = MathHelper.clamp(entity.rotationPitch, -50f, 50f);
            this.prevPitchVariation = MathHelper.clamp(entity.prevRotationPitch, -50f, 50f);
            ci.cancel();
        }
    }
}