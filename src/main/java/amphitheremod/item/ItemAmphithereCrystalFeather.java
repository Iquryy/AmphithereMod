package amphitheremod.item;

import amphitheremod.AmphithereMod;
import amphitheremod.util.AmphithereWorldPosData;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static amphitheremod.AmphithereMod.modIdWithDot;
public class ItemAmphithereCrystalFeather extends Item {
    private ForgeChunkManager.Ticket lastChunkTicket = null;

    public ItemAmphithereCrystalFeather(String name, CreativeTabs tab) {
        this.setTranslationKey(modIdWithDot + name);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setCreativeTab(tab);
        this.addPropertyOverride(new ResourceLocation("has_amphithere"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return ItemAmphithereCrystalFeather.hasAmphithere(stack) ? 1.0F : 0.0F;
            }
        });
        this.setMaxStackSize(1);
    }

    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean flag = false;

        String desc = "entity." + modIdWithDot + "amphithere.name";
        if (stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Amphithere")) {
                    NBTTagCompound amphithereTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    String amphithereName = I18n.format(desc);
                    if (!amphithereTag.getString("CustomName").isEmpty()) {
                        amphithereName = amphithereTag.getString("CustomName");
                    }
                    tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.summoning_crystal.bound", new Object[]{amphithereName}));
                    flag = true;
                }
            }
        }

        if (!flag) {
            tooltip.add(TextFormatting.GRAY + I18n.format("item." + modIdWithDot + "summoning_crystal.desc_0", new Object[0]));
            tooltip.add(TextFormatting.GRAY + I18n.format("item." + modIdWithDot + "summoning_crystal.desc_1", new Object[0]));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            if (!worldIn.isRemote) {
                if (!player.isRiding()) {
                    ItemStack stack = player.getHeldItem(hand);
                    boolean found = false;
                    BlockPos offsetPos = pos.offset(facing);
                    float yaw = player.rotationYaw;
                    boolean tryAgain = false;
                    boolean displayError = false;
                    boolean wrongDimension = false;

                    if (stack.getItem() == this && hasAmphithere(stack)) {
                        if (stack.getTagCompound() != null) {
                            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                                if (tagInfo.contains("Amphithere")) {
                                    NBTTagCompound amphithereTag = stack.getTagCompound().getCompoundTag(tagInfo);
                                    UUID id = amphithereTag.getUniqueId("AmphithereUUID");
                                    if (id != null) {
                                        Entity entity = worldIn.getMinecraftServer().getEntityFromUuid(id);

                                        if (entity != null) {
                                            if (entity.dimension == player.dimension) {
                                                found = this.summonEntity(entity, worldIn, offsetPos, yaw);
                                                displayError = !found;
                                            } else {
                                                wrongDimension = true;
                                                found = false;
                                                displayError = false;
                                            }
                                        } else {
                                            displayError = true;
                                        }

                                        if (this.lastChunkTicket != null) {
                                            ForgeChunkManager.releaseTicket(this.lastChunkTicket);
                                            this.lastChunkTicket = null;
                                        }

                                        if (!found && !wrongDimension) {
                                            BlockPos amphithereChunkPos = AmphithereWorldPosData.get(worldIn).getAmphitherePos(id);
                                            if (amphithereChunkPos != null && !worldIn.isBlockLoaded(amphithereChunkPos)) {
                                                ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(IceAndFire.INSTANCE, player.getName(), worldIn, Type.NORMAL);
                                                if (ticket != null) {
                                                    ForgeChunkManager.forceChunk(ticket, new ChunkPos(amphithereChunkPos));
                                                    this.lastChunkTicket = ticket;
                                                }
                                                tryAgain = true;
                                                displayError = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (found) {
                            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
                            player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                            player.swingArm(hand);
                            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "amphithereTeleport"), true);
                        } else if (tryAgain) {
                            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "tryagain"), true);
                        } else if (wrongDimension) {
                            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "wrongDimension"), true);
                        } else if (displayError) {
                            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "noAmphithereTeleport"), true);
                        }
                    }
                }
        }
        return EnumActionResult.PASS;
    }

    public boolean summonEntity(Entity entity, World worldIn, BlockPos offsetPos, float yaw) {
        if (entity instanceof EntityLivingBase) {
            entity.setLocationAndAngles((double) offsetPos.getX() + 0.5F, (double) offsetPos.getY() + 0.5F, (double) offsetPos.getZ() + 0.5F, yaw, 0.0F);
            AmphithereWorldPosData.get(worldIn).removeAmphithere(entity.getUniqueID());
            return true;
        }
        return false;
    }

    public static boolean hasAmphithere(ItemStack stack) {
        if (stack.getItem() instanceof ItemAmphithereCrystalFeather && stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Amphithere")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBoundTo(ItemStack stack, EntityAmphithere amphithere) {
        if (stack.getItem() instanceof ItemAmphithereCrystalFeather && stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Amphithere")) {
                    NBTTagCompound amphithereTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    UUID id = amphithereTag.getUniqueId("AmphithereUUID");
                    return amphithere.getUniqueID().equals(id);
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        boolean shouldGlow = ItemAmphithereCrystalFeather.hasAmphithere(stack);
        return shouldGlow || super.hasEffect(stack);
    }
}