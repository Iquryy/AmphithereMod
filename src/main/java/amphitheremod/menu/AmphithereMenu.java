package amphitheremod.menu;

import amphitheremod.config.AmphiConfig;
import amphitheremod.item.AmphithereArmorItem;
import amphitheremod.item.AmphithereBeakItem;
import amphitheremod.registry.AmphiMenus;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * The amphithere inventory: beak, head, wing, body and tail slots plus the player's inventory. Slot layout matches the
 * 1.12.2 GUI texture. The ClassyHats hat slot isn't ported (ClassyHats has no 1.20.1 version).
 */
public class AmphithereMenu extends AbstractContainerMenu {
    // Equipment slot order in the menu: beak, head, body, wings, tail
    private static final EquipmentSlot[] GEAR = {EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final int[] GEAR_Y = {18, 36, 72, 54, 90};
    private static final int GEAR_SLOTS = GEAR.length;

    @Nullable
    public final EntityAmphithere amphithere;
    private final Container gear;

    public AmphithereMenu(int id, Inventory playerInv, @Nullable EntityAmphithere amphithere) {
        super(AmphiMenus.AMPHITHERE.get(), id);
        this.amphithere = amphithere;
        this.gear = amphithere != null ? new EquipmentContainer(amphithere, GEAR) : new net.minecraft.world.SimpleContainer(GEAR_SLOTS);
        for (int i = 0; i < GEAR_SLOTS; i++) {
            EquipmentSlot equipSlot = GEAR[i];
            this.addSlot(new Slot(this.gear, i, 8, GEAR_Y[i]) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return accepts(equipSlot, stack);
                }

                @Override
                public boolean isActive() {
                    return AmphiConfig.ENABLE_ARMOR.get();
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        }
        // Player inventory and hotbar (same positions as 1.12.2: rows at y 132..168, hotbar at 190)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 132 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 190));
        }
    }

    /** Client side: rebuilds the menu from the amphithere's entity id sent by the server. */
    public static AmphithereMenu fromNetwork(int id, Inventory playerInv, int entityId) {
        Entity entity = playerInv.player.level().getEntity(entityId);
        return new AmphithereMenu(id, playerInv, entity instanceof EntityAmphithere amphi ? amphi : null);
    }

    /** Beaks go in the beak slot, each armor piece in its own slot, only while armor is enabled. */
    private static boolean accepts(EquipmentSlot slot, ItemStack stack) {
        if (stack.isEmpty() || !AmphiConfig.ENABLE_ARMOR.get()) {
            return false;
        }
        if (slot == EquipmentSlot.MAINHAND) {
            return stack.getItem() instanceof AmphithereBeakItem;
        }
        return stack.getItem() instanceof AmphithereArmorItem armor && armor.slot == slot;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.amphithere != null && this.amphithere.isAlive() && this.amphithere.distanceTo(player) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        int hotbarStart = this.slots.size() - 9;
        if (index < GEAR_SLOTS) {
            // Gear -> player inventory
            if (!this.moveItemStackTo(source, GEAR_SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            int target = -1;
            for (int i = 0; i < GEAR_SLOTS; i++) {
                if (accepts(GEAR[i], source) && !this.slots.get(i).hasItem()) {
                    target = i;
                    break;
                }
            }
            if (target >= 0) {
                if (!this.moveItemStackTo(source, target, target + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < hotbarStart) {
                // Inventory <-> hotbar, like any container
                if (!this.moveItemStackTo(source, hotbarStart, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(source, GEAR_SLOTS, hotbarStart, false)) {
                return ItemStack.EMPTY;
            }
        }
        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }
}
