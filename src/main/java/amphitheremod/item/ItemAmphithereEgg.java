package amphitheremod.item;

import amphitheremod.AmphithereMod;
import amphitheremod.entity.EntityAmphithereEgg;
import amphitheremod.util.UsefulStiff;
import amphitheremod.util.EnumAmphiType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class ItemAmphithereEgg extends Item {
    public EnumAmphiType variant;

    public ItemAmphithereEgg(String name, EnumAmphiType variant, CreativeTabs tab) {
        this.setHasSubtypes(false);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + "amphithere_egg");
        this.setCreativeTab(tab);
        this.variant = variant;
        maxStackSize = 1;
    }

    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.variant != null) {
            tooltip.add(UsefulStiff.translateToLocal("group."+modIdWithDot+"amphithere." + this.variant.toString().toLowerCase()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        EnumAmphiType type = EnumAmphiType.getEnumNameFromInt(variant.ordinal());
        return type.getGroup().getRarity();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos placePos = pos.up();
        if (!player.canPlayerEdit(placePos, facing, itemstack))
            return EnumActionResult.FAIL;
        if (!worldIn.isRemote) {
            EntityAmphithereEgg eggEntity = new EntityAmphithereEgg(worldIn);
            eggEntity.setType(this.variant);
            eggEntity.setPosition(placePos.getX() + 0.5D, placePos.getY(), placePos.getZ() + 0.5D);
            worldIn.spawnEntity(eggEntity);
        }
        if (!player.capabilities.isCreativeMode)
            itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}