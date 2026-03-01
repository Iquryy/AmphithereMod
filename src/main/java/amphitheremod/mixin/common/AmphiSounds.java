package amphitheremod.mixin.common;

import amphitheremod.config.ConfigHandler;
import amphitheremod.proxy.CommonProxy;
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
public abstract class AmphiSounds {

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
        if (amphiMod$amphi.isTamed() && amphiMod$amphi.getOwner() != null) {
            if (amphiMod$amphi.getOwner().getName().equals("Nischhelm") || amphiMod$amphi.getOwner().getName().equals("Iqury")) {
                if (amphiMod$amphi.getRNG().nextInt(50) == 1)
                    switch (amphiMod$amphi.getRNG().nextInt(3)) {
                        case 0:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_0);
                            break;
                        case 1:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_1);
                            break;
                        case 2:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_2);
                            break;
                    }
            }
            if (amphiMod$amphi.getOwner().getName().equals("craftoes") || amphiMod$amphi.getOwner().getName().equals("Iqury")) {
                if (amphiMod$amphi.getRNG().nextInt(50) == 1)
                    switch (amphiMod$amphi.getRNG().nextInt(7)) {
                        case 0:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_3);
                            break;
                        case 1:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_4);
                            break;
                        case 2:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_5);
                        case 3:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_6);
                        case 4:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_7);
                        case 5:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_8);
                        case 6:
                            cir.setReturnValue(CommonProxy.AMPHITHERE_HURT_9);
                            break;
                    }
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
