package amphitheremod.mixin.common.soundEvents;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static amphitheremod.util.Sounds.AMPHITHERE_WING_FLAP;

@Mixin(EntityAmphithere.class)
public abstract class FlyingSound {
    @Inject(method = "onLivingUpdate", at = @At(value = "TAIL"))
    void addSound(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.world.isRemote) {
            if (amphi.getVariant() == EnumAmphiType.SKELETON.ordinal()) return;
            if (amphi.getVariant() == EnumAmphiType.WITHER_SKELETON.ordinal()) return;
            if (amphi.isFlying()) {
                if (!(amphi.flapProgress > 5)) return;
                if (amphi.ticksExisted % 23 == 0) {
                    if (AMPHITHERE_WING_FLAP == null) return;
                    amphi.world.playSound(null, amphi.posX, amphi.posY, amphi.posZ, AMPHITHERE_WING_FLAP, SoundCategory.NEUTRAL, 2.5F, Math.min(0.8F, 0.8F + (amphi.world.rand.nextFloat() - 0.9F) * 0.25F));
                }
            }
        }
    }
}