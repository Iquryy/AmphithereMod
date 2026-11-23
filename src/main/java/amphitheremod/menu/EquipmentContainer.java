package amphitheremod.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** A Container view over a mob's equipment slots, so menu slots read and write the amphithere's gear directly. */
public class EquipmentContainer implements Container {
    private final Mob entity;
    private final EquipmentSlot[] slots;

    public EquipmentContainer(Mob entity, EquipmentSlot... slots) {
        this.entity = entity;
        this.slots = slots;
    }

    @Override
    public int getContainerSize() {
        return this.slots.length;
    }

    @Override
    public boolean isEmpty() {
        for (EquipmentSlot slot : this.slots) {
            if (!this.entity.getItemBySlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.entity.getItemBySlot(this.slots[index]);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.entity.getItemBySlot(this.slots[index]);
        ItemStack result = stack.split(count);
        this.entity.setItemSlot(this.slots[index], stack);
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.entity.getItemBySlot(this.slots[index]);
        this.entity.setItemSlot(this.slots[index], ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.entity.setItemSlot(this.slots[index], stack);
        // Gear put on by the owner always drops on death (event.CommonEvents) instead of the 8.5% mob chance
        this.entity.setGuaranteedDrop(this.slots[index]);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.entity.isAlive() && player.distanceToSqr(this.entity) < 64.0;
    }

    @Override
    public void clearContent() {
        for (EquipmentSlot slot : this.slots) {
            this.entity.setItemSlot(slot, ItemStack.EMPTY);
        }
    }
}
