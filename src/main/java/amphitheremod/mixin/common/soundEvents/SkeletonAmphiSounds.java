package amphitheremod.mixin.common.soundEvents;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.enumm.EnumAmphiType;
import amphitheremod.util.Sounds;
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
        if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_AMBIENT);
        else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT);
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        if (amphiMod$amphi.isTamed() && amphiMod$amphi.getOwner() != null && (!(ConfigHandler.test.test))) {
            if (amphiMod$amphi.getOwner().getName().equals("Nischhelm"))
                if (amphiMod$amphi.getRNG().nextInt(169) == 1)
                    switch (amphiMod$amphi.getRNG().nextInt(3)) {
                        case 0:
                            cir.setReturnValue(Sounds.AMPHITHERE_HURT_0);
                            break;
                        case 1:
                            cir.setReturnValue(Sounds.AMPHITHERE_HURT_1);
                            break;
                        case 2:
                            cir.setReturnValue(Sounds.AMPHITHERE_HURT_2);
                            break;
                    }
        } else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_HURT);
        else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_HURT);
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_SKELETON_DEATH);
        else if (amphiMod$amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON))
            cir.setReturnValue(SoundEvents.ENTITY_WITHER_SKELETON_DEATH);
    }
}
