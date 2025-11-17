package amphitheremod.item;

import amphitheremod.AmphithereMod;
import amphitheremod.config.ConfigHandler;
import amphitheremod.util.AmphithereWorldPosData;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean bound = false;
        String descKey = "entity." + modIdWithDot + "amphithere.name";
        if (stack != null && stack.getTagCompound() != null && stack.getTagCompound().hasKey("Amphithere")) {
            NBTTagCompound amphithereTag = stack.getTagCompound().getCompoundTag("Amphithere");
            String amphithereName = I18n.format(descKey);
            if (!amphithereTag.getString("CustomName").isEmpty()) {
                amphithereName = amphithereTag.getString("CustomName");
            }
            tooltip.add(TextFormatting.GRAY + I18n.format("item.iceandfire.summoning_crystal.bound", amphithereName));
            bound = true;
        }

        if (!bound) {
            tooltip.add(TextFormatting.GRAY + I18n.format("item." + modIdWithDot + "summoning_crystal.desc_0"));
            tooltip.add(TextFormatting.GRAY + I18n.format("item." + modIdWithDot + "summoning_crystal.desc_1"));
        }

        if(!ConfigHandler.general.enableCrystalFeather)
            tooltip.add(TextFormatting.RED + I18n.format("item.amphitheremod.summoning_crystal.disabled"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return EnumActionResult.PASS;
        ItemStack stack = player.getHeldItem(hand);
        boolean summoned = false;
        boolean tryAgain = false;
        boolean displayError = false;
        if (stack.getItem() == this && hasAmphithere(stack)) {
            NBTTagCompound amphithereTag = stack.getTagCompound().getCompoundTag("Amphithere");
            UUID id = amphithereTag.hasUniqueId("AmphithereUUID") ? amphithereTag.getUniqueId("AmphithereUUID") : null;
            if (id != null) {
                if (this.lastChunkTicket != null) {
                    ForgeChunkManager.releaseTicket(this.lastChunkTicket);
                    this.lastChunkTicket = null;
                }
                BlockPos targetPos = pos.offset(facing);
                float yaw = player.rotationYaw;
                summoned = this.summonEntity(id, player, targetPos, yaw);
                displayError = !summoned;
                if (!summoned) {
                    try {
                        int boundDimension = amphithereTag.getInteger("AmphithereDimension");
                        WorldServer entityWorld = worldIn.getMinecraftServer().getWorld(boundDimension);
                        BlockPos amphPos = AmphithereWorldPosData.get(entityWorld).getAmphitherePos(id);
                        if (amphPos != null && !entityWorld.isBlockLoaded(amphPos)) {
                            ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestPlayerTicket(AmphithereMod.MODID, player.getName(), entityWorld, Type.NORMAL);
                            if (ticket != null) {
                                ForgeChunkManager.forceChunk(ticket, new ChunkPos(amphPos));
                                this.lastChunkTicket = ticket;
                                tryAgain = true;
                                displayError = false;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Could not load chunk when summoning amphithere: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        if (summoned) {
            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
            player.swingArm(hand);
            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "amphithereTeleport"), true);
            return EnumActionResult.SUCCESS;
        } else if (tryAgain) {
            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "tryagain"), true);
            return EnumActionResult.SUCCESS;
        } else if (displayError)
            player.sendStatusMessage(new TextComponentTranslation("message." + modIdWithDot + "noAmphithereTeleport"), true);
        return EnumActionResult.PASS;
    }

    public boolean summonEntity(UUID id, EntityPlayer owner, BlockPos targetPos, float yaw) {
        MinecraftServer server = owner.world.getMinecraftServer();
        if (server == null) return false;
        Entity entity = server.getEntityFromUuid(id);
        if (entity instanceof EntityAmphithere) {
            EntityAmphithere amphithere = (EntityAmphithere) entity;
            if (!amphithere.isTamed() || !amphithere.isOwner(owner)) return false;
            Entity entityToTeleport = amphithere;
            if (amphithere.dimension != owner.dimension) {
                entityToTeleport = amphithere.changeDimension(owner.dimension);
                if (entityToTeleport == null) return false;
            }
            entityToTeleport.setLocationAndAngles(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D, yaw, 0.0F);
            AmphithereWorldPosData.get(entityToTeleport.world).removeAmphithere(entityToTeleport.getUniqueID());
            return true;
        }
        return false;
    }

    public static boolean hasAmphithere(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() instanceof ItemAmphithereCrystalFeather && stack.getTagCompound() != null) {
            return stack.getTagCompound().hasKey("Amphithere");
        }
        return false;
    }

    public static boolean isBoundTo(ItemStack stack, EntityAmphithere amphithere) {
        if (stack == null || amphithere == null) return false;
        if (stack.getItem() instanceof ItemAmphithereCrystalFeather && stack.getTagCompound() != null) {
            if (!stack.getTagCompound().hasKey("Amphithere")) return false;
            NBTTagCompound amphTag = stack.getTagCompound().getCompoundTag("Amphithere");
            if (!amphTag.hasUniqueId("AmphithereUUID")) return false;
            UUID id = amphTag.getUniqueId("AmphithereUUID");
            return amphithere.getUniqueID().equals(id);
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return hasAmphithere(stack) || super.hasEffect(stack);
    }
}