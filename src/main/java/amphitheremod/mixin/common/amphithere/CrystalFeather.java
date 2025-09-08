package amphitheremod.mixin.common.amphithere;

import amphitheremod.handlers.ModRegistry;
import amphitheremod.item.ItemAmphithereCrystalFeather;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public abstract class CrystalFeather {
    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void processInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.getHeldItem(hand);
        IAmphithereData data = (IAmphithereData) this;
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;

        if (stack.getItem() == ModRegistry.AMPHITHERE_CRYSTAL_FEATHER) {
            if (!ItemAmphithereCrystalFeather.hasAmphithere(stack)) {
                data.amphiMod_master$setBounded(true);
                NBTTagCompound compound = stack.getTagCompound();
                if (compound == null) {
                    compound = new NBTTagCompound();
                    stack.setTagCompound(compound);
                }

                NBTTagCompound amphithereTag = new NBTTagCompound();
                amphithereTag.setUniqueId("AmphithereUUID", amphi.getUniqueID());
                amphithereTag.setString("CustomName", amphi.getCustomNameTag());
                compound.setTag("Amphithere", amphithereTag);
                amphi.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
                player.swingArm(hand);
                cir.setReturnValue(true);
            }
            else if (ItemAmphithereCrystalFeather.isBoundTo(stack, amphi)) {
                stack.setTagCompound(new NBTTagCompound());
                data.amphiMod_master$setBounded(false);
                amphi.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
                player.swingArm(hand);
                cir.setReturnValue(true);
            }
        }
    }
}