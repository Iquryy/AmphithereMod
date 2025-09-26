package amphitheremod.mixin.common.amphithere.soundEvents;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public abstract class SkeletonAmphiSounds {

    @Unique
    EntityAmphithere amphiMod$amphi = (EntityAmphithere) (Object) this;

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_AMBIENT);
        } else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT);
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_HURT);
        } else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_HURT);
        }
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_DEATH);
        } else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON)) {
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_DEATH);
        }
    }
}
