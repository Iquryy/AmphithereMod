package amphitheremod.server.inventory;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class EquipmentSlotInventory implements IInventory {

    private final EntityLiving entity;
    private final EntityEquipmentSlot[] slots;

    public EquipmentSlotInventory(EntityLiving entity, EntityEquipmentSlot... slots) {
        this.entity = entity;
        this.slots = slots;
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public boolean isEmpty() {
        for (EntityEquipmentSlot slot : slots) {
            if (!entity.getItemStackFromSlot(slot).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return entity.getItemStackFromSlot(slots[index]);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = entity.getItemStackFromSlot(slots[index]);
        ItemStack result = stack.splitStack(count);
        entity.setItemStackToSlot(slots[index], stack);
        return result;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        entity.setItemStackToSlot(slots[index], stack);
    }

    @Override
    public boolean isUsableByPlayer(net.minecraft.entity.player.EntityPlayer player) {
        return entity.isEntityAlive() && player.getDistanceSq(entity) < 64.0D;
    }

    @Override
    public void openInventory(net.minecraft.entity.player.EntityPlayer player) {}
    @Override
    public void closeInventory(net.minecraft.entity.player.EntityPlayer player) {}
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) { return true; }
    @Override
    public int getInventoryStackLimit() { return 1; }
    @Override
    public void clear() {
        for (EntityEquipmentSlot slot : slots) {
            entity.setItemStackToSlot(slot, ItemStack.EMPTY);
        }
    }
    @Override public ITextComponent getDisplayName() { return new TextComponentString("Amphithere Equipment"); }
    @Override public boolean hasCustomName() { return false; }
    @Override public String getName() { return "Amphithere Equipment"; }
    @Override public int getField(int id) { return 0; }
    @Override public void setField(int id, int value) {}
    @Override public int getFieldCount() { return 0; }
    @Override public void markDirty() {}
}
