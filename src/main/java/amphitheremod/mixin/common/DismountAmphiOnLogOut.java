package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayServer.class)
public abstract class DismountAmphiOnLogOut {
    @Shadow public EntityPlayerMP player;

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void aaa(ITextComponent reason, CallbackInfo ci){
        if(player == null) return;
        if(!player.isRiding()) return;
        if(!(player.getRidingEntity() instanceof EntityAmphithere)) return;
        EntityAmphithere amphithere = (EntityAmphithere) player.getRidingEntity();
        amphithere.setCommand(2);
        player.dismountRidingEntity();
    }
}
