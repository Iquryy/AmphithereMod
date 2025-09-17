package amphitheremod.mixin.common.amphithere_inventory;

import amphitheremod.server.inventory.AmphithereContainer;
import amphitheremod.server.gui.AmphithereGui;
import com.github.alexthe666.iceandfire.client.GuiHandler;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiHandler.class)
public class AmphithereGuiHandlerMixin {

    @Inject(method = "getServerGuiElement", at = @At("HEAD"), cancellable = true, remap = false)
    private void amphimod_getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z, CallbackInfoReturnable<Object> cir) {
        if (ID == 1) {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityAmphithere) {
                cir.setReturnValue(new AmphithereContainer((EntityAmphithere) entity, player));
            }
        }
    }

    @Inject(method = "getClientGuiElement", at = @At("HEAD"), cancellable = true, remap = false)
    private void amphimod_getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z, CallbackInfoReturnable<Object> cir) {
        if (ID == 1) {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityAmphithere) {
                cir.setReturnValue(new AmphithereGui(player, (EntityAmphithere) entity));
            }
        }
    }
}