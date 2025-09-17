package amphitheremod.mixin.common.amphithere_inventory;

import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public abstract class AmphithereInventory extends EntityAnimal implements IAmphithereData {

    public AmphithereInventory(World worldIn) {
        super(worldIn);
    }

    @Unique
    private void amphiMod_master$openGui(EntityPlayer playerEntity) {
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        if (!amphithere.world.isRemote && amphithere.isTamed() && amphithere.isOwner(playerEntity)) {
            playerEntity.openGui(IceAndFire.INSTANCE, 1, amphithere.world, amphithere.getEntityId(), 0, 0);
        }
    }

    @Inject(method = "processInteract", at = @At(value = "HEAD"), cancellable = true)
    private void processInventoryInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        if (amphithere.isTamed() && amphithere.isOwner(player) && player.isSneaking()) {
            this.amphiMod_master$openGui(player);
            cir.setReturnValue(true);
        }
    }
}
