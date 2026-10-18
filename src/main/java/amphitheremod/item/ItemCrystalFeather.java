package amphitheremod.item;

import amphitheremod.AmphithereMod;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.SoulBindData;
import amphitheremod.util.UsefulStiff;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static amphitheremod.AmphithereMod.modIdWithDot;
import static amphitheremod.handlers.ModItemRegistry.itemsToRegister;

/**
 * Crystal Feather: soul binds one of your own tamed amphitheres.
 * <ul>
 *   <li>Use it on your own tamed amphithere: the bond is made and the amphithere rests inside the feather.</li>
 *   <li>Use it on a block: the amphithere comes out there. If it's already out (and loaded in your dimension), it's summoned to that spot.</li>
 *   <li>Use it on your bound amphithere while it's out: it goes back inside.</li>
 * </ul>
 * The bond is permanent: if either dies, both die (see {@link amphitheremod.events.SoulBindEvents}).
 */
public class ItemCrystalFeather extends Item {
    private static final String TAG = "SoulBind";
    private static final String MSG = "amphitheremod.crystal_feather.msg.";

    public ItemCrystalFeather(String name, CreativeTabs tab) {
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(tab);
        this.setMaxStackSize(1);
        itemsToRegister.add(this);
    }

    // ------------------------------------------------------------------ stack data

    @Nullable
    private static NBTTagCompound bindTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(TAG) ? tag.getCompoundTag(TAG) : null;
    }

    public static boolean isBound(ItemStack stack) {
        NBTTagCompound t = bindTag(stack);
        return t != null && t.hasUniqueId("Amphi");
    }

    @Nullable
    public static UUID getBoundAmphi(ItemStack stack) {
        NBTTagCompound t = bindTag(stack);
        return t != null && t.hasUniqueId("Amphi") ? t.getUniqueId("Amphi") : null;
    }

    @Nullable
    public static UUID getBondOwner(ItemStack stack) {
        NBTTagCompound t = bindTag(stack);
        return t != null && t.hasUniqueId("Owner") ? t.getUniqueId("Owner") : null;
    }

    /** True while the amphithere is resting inside the feather. */
    public static boolean holdsAmphi(ItemStack stack) {
        NBTTagCompound t = bindTag(stack);
        return t != null && t.hasKey("Stored");
    }

    private static String storedName(ItemStack stack) {
        NBTTagCompound t = bindTag(stack);
        return t == null ? "" : t.getString("Name");
    }

    private static ITextComponent nameComponent(String customName) {
        return customName.isEmpty() ? new TextComponentTranslation("entity." + modIdWithDot + "amphithere.name") : new TextComponentString(customName);
    }

    // ------------------------------------------------------------------ using it on an amphithere (called from SoulBindEvents)

    /** Server side. Returns true when the feather handled the click (so the amphithere's own interaction is skipped). */
    public static boolean useOnAmphithere(ItemStack stack, EntityPlayer player, EntityAmphithere amphi) {
        World world = player.world;
        if (!amphi.isTamed()) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "not_tamed"), true);
            return true;
        }
        if (!amphi.isOwner(player)) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "not_owner"), true);
            return true;
        }

        SoulBindData data = SoulBindData.get(world);
        UUID amphiId = amphi.getUniqueID();

        if (!isBound(stack)) {
            if (data.isBound(amphiId) || ((IAmphithereData) amphi).amphiMod_master$getBounded()) {
                player.sendStatusMessage(new TextComponentTranslation(MSG + "already_bound"), true);
                return true;
            }
            // Make the bond, then put the amphithere inside.
            NBTTagCompound t = new NBTTagCompound();
            t.setUniqueId("Amphi", amphiId);
            t.setUniqueId("Owner", player.getUniqueID());
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setTag(TAG, t);
            ((IAmphithereData) amphi).amphiMod_master$setBounded(true);
            data.bind(amphiId, player.getUniqueID());
            ITextComponent name = amphi.getDisplayName();
            storeAmphi(stack, amphi);
            player.sendStatusMessage(new TextComponentTranslation(MSG + "bound", name), true);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1F, 1.4F);
            return true;
        }

        if (!player.getUniqueID().equals(getBondOwner(stack))) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "not_your_feather"), true);
            return true;
        }
        if (!amphiId.equals(getBoundAmphi(stack))) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "other_amphi"), true);
            return true;
        }
        if (holdsAmphi(stack)) return true; // can't happen (it's inside), but never store twice

        ITextComponent name = amphi.getDisplayName();
        storeAmphi(stack, amphi);
        player.sendStatusMessage(new TextComponentTranslation(MSG + "stored", name), true);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.8F, 1.3F);
        return true;
    }

    private static void storeAmphi(ItemStack stack, EntityAmphithere amphi) {
        amphi.removePassengers();
        amphi.dismountRidingEntity();
        if (amphi.getLeashed()) amphi.clearLeashed(true, true);
        amphi.getNavigator().clearPath();
        amphi.fallDistance = 0;

        NBTTagCompound entityTag = new NBTTagCompound();
        if (!amphi.writeToNBTOptional(entityTag)) return;
        NBTTagCompound t = bindTag(stack);
        if (t == null) return;
        t.setTag("Stored", entityTag);
        t.setString("Name", amphi.hasCustomName() ? amphi.getCustomNameTag() : "");
        amphi.setDead(); // removed from the world, not killed: no death event fires
    }

    // ------------------------------------------------------------------ using it on a block: release or summon

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isBound(stack)) return EnumActionResult.PASS;
        if (world.isRemote) return EnumActionResult.SUCCESS;

        if (!player.getUniqueID().equals(getBondOwner(stack))) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "not_your_feather"), true);
            return EnumActionResult.FAIL;
        }
        if (shatterIfLost(stack, player)) return EnumActionResult.SUCCESS;

        BlockPos spot = pos.offset(facing);
        double x = spot.getX() + 0.5, y = spot.getY(), z = spot.getZ() + 0.5;

        if (holdsAmphi(stack)) {
            NBTTagCompound t = bindTag(stack);
            Entity entity = EntityList.createEntityFromNBT(t.getCompoundTag("Stored"), world);
            if (!(entity instanceof EntityAmphithere)) {
                player.sendStatusMessage(new TextComponentTranslation(MSG + "release_failed"), true);
                return EnumActionResult.FAIL;
            }
            entity.setLocationAndAngles(x, y, z, player.rotationYaw, 0F);
            entity.motionX = entity.motionY = entity.motionZ = 0;
            entity.fallDistance = 0;
            ((IAmphithereData) entity).amphiMod_master$setBounded(true);
            if (!world.spawnEntity(entity)) {
                // Something with this UUID is already in the world; keep it inside rather than duplicate it.
                player.sendStatusMessage(new TextComponentTranslation(MSG + "release_failed"), true);
                return EnumActionResult.FAIL;
            }
            t.removeTag("Stored");
            player.sendStatusMessage(new TextComponentTranslation(MSG + "released", entity.getDisplayName()), true);
            world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.8F, 1.1F);
            player.swingArm(hand);
            return EnumActionResult.SUCCESS;
        }

        // It's out: summon it here, like a dragon summoning crystal.
        UUID amphiId = getBoundAmphi(stack);
        Entity entity = world.getMinecraftServer() == null ? null : world.getMinecraftServer().getEntityFromUuid(amphiId);
        if (!(entity instanceof EntityAmphithere) || entity.isDead || entity.world != world) {
            player.sendStatusMessage(new TextComponentTranslation(MSG + "not_found"), true);
            return EnumActionResult.FAIL;
        }
        entity.removePassengers();
        entity.dismountRidingEntity();
        ((EntityAmphithere) entity).getNavigator().clearPath();
        entity.setLocationAndAngles(x, y, z, player.rotationYaw, 0F);
        entity.motionX = entity.motionY = entity.motionZ = 0;
        entity.fallDistance = 0;
        player.sendStatusMessage(new TextComponentTranslation(MSG + "summoned", entity.getDisplayName()), true);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    /** If the bound amphithere died (or is fated to), the feather shatters. Returns true if it did. */
    public static boolean shatterIfLost(ItemStack stack, EntityPlayer holder) {
        UUID amphiId = getBoundAmphi(stack);
        if (amphiId == null || holder.world.isRemote) return false;
        if (!SoulBindData.get(holder.world).isLost(amphiId)) return false;
        stack.shrink(1);
        holder.sendStatusMessage(new TextComponentTranslation(MSG + "shattered"), false);
        holder.world.playSound(null, holder.posX, holder.posY, holder.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote && entity instanceof EntityPlayer && entity.ticksExisted % 20 == 0 && isBound(stack))
            shatterIfLost(stack, (EntityPlayer) entity);
    }

    /** A dropped feather with a bond doesn't despawn after 5 minutes. */
    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return isBound(stack) ? Integer.MAX_VALUE : super.getEntityLifespan(stack, world);
    }

    // ------------------------------------------------------------------ looks

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (isBound(stack)) {
            String name = storedName(stack);
            if (name.isEmpty()) name = UsefulStiff.translateToLocal("entity." + modIdWithDot + "amphithere.name");
            tooltip.add(TextFormatting.AQUA + net.minecraft.client.resources.I18n.format("amphitheremod.crystal_feather.bound_name", name));
            tooltip.add(TextFormatting.GRAY + UsefulStiff.translateToLocal("amphitheremod.crystal_feather.bound_desc"));
            tooltip.add(TextFormatting.RED + UsefulStiff.translateToLocal("amphitheremod.crystal_feather.empty_1"));
        } else {
            tooltip.add(TextFormatting.GRAY + UsefulStiff.translateToLocal("amphitheremod.crystal_feather.empty_0"));
            tooltip.add(TextFormatting.RED + UsefulStiff.translateToLocal("amphitheremod.crystal_feather.empty_1"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return isBound(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}