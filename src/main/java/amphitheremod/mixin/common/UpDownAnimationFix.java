package amphitheremod.mixin.common;

import amphitheremod.util.IceAndFireUtil;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAmphithere.class)
public abstract class UpDownAnimationFix extends Entity {

    public UpDownAnimationFix(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;rotationPitch:F", ordinal = 0))
    private void UpDownFix(CallbackInfo ci) {
        if (!(IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT)) return;
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!(amphi.getControllingPassenger() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) amphi.getControllingPassenger();
        if(player == null) return;
        float playerPitch = player.rotationPitch;
        amphi.rotationPitch = (playerPitch * -1f) / 1.5f;
        if(amphi.isOnGround() && !amphi.isFlying())
            amphi.rotationPitch = 0;
    }

    @ModifyConstant(method = "onLivingUpdate", constant = @Constant(floatValue = 2.0F, ordinal = 2))
    private float ggg(float originalValue) {
        return 1.0F;
    }

    @ModifyConstant(method = "onLivingUpdate", constant = @Constant(floatValue = 10.0F, ordinal = 2))
    private float aaa(float originalValue) {
        return 0.0F;
    }

    @ModifyConstant(method = "onLivingUpdate", constant = @Constant(floatValue = 0.5F, ordinal = 3))
    private float sss(float originalValue) {
        return 1.0F;
    }
}
