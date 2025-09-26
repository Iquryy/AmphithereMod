package amphitheremod.mixin.common.amphithere.soundEvents;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAmphithere.class)
public abstract class FlyingSound {
    @Inject(method = "onLivingUpdate", at = @At(value = "HEAD"))
    void addSound(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.world.isRemote) {
            if (amphi.isFlying() && amphi.isBeingRidden()) {
                if (amphi.getRider() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) amphi.getRider();
                    if (amphi.isOwner(player) && amphi.isTamed()) {
                        if (amphi.ticksExisted % 30 == 0 && amphi.isFlapping() && amphi.flapProgress > 1) {
                            player.sendMessage(new TextComponentString("Flap"));
                            amphi.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 0.7f, 1.7f);
                            amphi.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 0.5f, 1.0f);
                            amphi.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 0.7f, 0.7f);
                        }
                    }
                }
            }
        }
    }
}