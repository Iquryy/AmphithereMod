package amphitheremod.mixin.client;

import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Turns off Ice and Fire's roll, tail and pitch buffers for amphitheres: the flight animation (AmphiAnimStateMixin +
 * AmphiModelMixin) does bank, tail swing and body pitch itself. The buffers keep their 0 variation, so applying them
 * does nothing. A sharp turn still makes the wings flap, like the roll buffer did.
 */
@Mixin(value = IFChainBuffer.class, remap = false)
public abstract class AmphiChainBufferMixin {
    @Inject(method = {"calculateChainFlapBuffer(FIFFLnet/minecraft/world/entity/LivingEntity;)V",
            "calculateChainFlapBufferHead(FIFFLnet/minecraft/world/entity/LivingEntity;)V"}, at = @At("HEAD"), cancellable = true)
    private void amphiMod$noRollBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere amphi) {
            if (Math.abs(entity.yBodyRotO - entity.yBodyRot) > 15.0F) {
                amphi.flapWings();
            }
            ci.cancel();
        }
    }

    @Inject(method = {"calculateChainSwingBuffer(FIFFLnet/minecraft/world/entity/LivingEntity;)V",
            "calculateChainPitchBuffer(FIFFLnet/minecraft/world/entity/LivingEntity;)V"}, at = @At("HEAD"), cancellable = true)
    private void amphiMod$noTailOrPitchBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere) {
            ci.cancel();
        }
    }
}
