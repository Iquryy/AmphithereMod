package amphitheremod.mixin.client;

import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.event.EventClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EventClient.class)
public class AmphithereView {

    @Inject(method = "onCameraSetup", at = @At("HEAD"), cancellable = true, remap = false)
    private void onCameraSetupAmphithere(EntityViewRenderEvent.CameraSetup event, CallbackInfo ci) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Entity ridingEntity = player.getRidingEntity();
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) return;
        if (ridingEntity instanceof EntityAmphithere) {
            int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
            float scale = ConfigHandler.mixins.ridingViewDistance;

            switch (ConfigHandler.mixins.ridingViewDistance) {
                case 1: scale = 1; break;
                case 2: scale = 2; break;
                case 3: scale = 3; break;
                case 4: scale = 4; break;
                case 5: scale = 5; break;
                case 6: scale = 6; break;
                case 7: scale = 7; break;
                case 8: scale = 8; break;
                case 9: scale = 9; break;
                case 10: scale = 10; break;
            }

            float distanceZ = scale;
            float distanceX = scale;
            float heightY = 0.0F;
            float x = 0.0F;
            float y = heightY;
            float z;
            switch (currentView) {
                case 1:
                    x = 0.0F;
                    break;
                case 2:
                    x = -distanceX;
                    break;
                case 3:
                    x = distanceX;
                    break;
            }

            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
                z = -distanceZ;
            } else {
                z = distanceZ;
                x *= -1;
            }

            GL11.glTranslatef(x, y, z);
            ci.cancel();
        }
    }
}