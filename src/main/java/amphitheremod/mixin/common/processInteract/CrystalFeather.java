package amphitheremod.mixin.common.processInteract;

import amphitheremod.handlers.ModItemRegistry;
import amphitheremod.item.ItemAmphithereCrystalFeather;
import amphitheremod.util.AmphithereWorldPosData;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export=true)
@Mixin(EntityAmphithere.class)
public abstract class CrystalFeather extends EntityLiving {

    public CrystalFeather(World worldIn) {
        super(worldIn);
    }

    @Debug(export = true)
    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true, order = -699)
    private void processInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack == null || stack.isEmpty()) return;
        if (stack.getItem() != ModItemRegistry.AMPHITHERE_CRYSTAL_FEATHER) return;
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.isTamed() && amphi.isOwner(player)) {
            IAmphithereData data = (IAmphithereData) this;
            if (stack.getTagCompound() == null)
                stack.setTagCompound(new NBTTagCompound());
            if (ItemAmphithereCrystalFeather.isBoundTo(stack, amphi)) {
                stack.setTagCompound(new NBTTagCompound());
                data.amphiMod_master$setBounded(false);
                amphi.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
                player.swingArm(hand);
                cir.setReturnValue(true);
                return;
            }
            if (!ItemAmphithereCrystalFeather.hasAmphithere(stack)) {
                data.amphiMod_master$setBounded(true);
                NBTTagCompound compound = stack.getTagCompound();
                if (compound == null) compound = new NBTTagCompound();

                NBTTagCompound amphithereTag = new NBTTagCompound();
                amphithereTag.setUniqueId("AmphithereUUID", amphi.getUniqueID());
                amphithereTag.setInteger("AmphithereDimension", amphi.dimension);
                amphithereTag.setString("CustomName", amphi.getCustomNameTag());
                compound.setTag("Amphithere", amphithereTag);
                stack.setTagCompound(compound);
                amphi.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
                player.swingArm(hand);
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void onLivingUpdate(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!this.world.isRemote) {
            if (amphi.isTamed())
                if (this.ticksExisted % 20 == 0)
                    AmphithereWorldPosData.get(this.world).addAmphithere(this.getUniqueID(), this.getPosition());
        }
    }
}