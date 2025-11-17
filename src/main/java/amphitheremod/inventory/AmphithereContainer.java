package amphitheremod.inventory;

import amphitheremod.config.ConfigHandler;
import amphitheremod.item.amphithere_armor.ArmorBase;
import amphitheremod.item.amphithere_beak_attachment.BeakBase;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import wiresegal.classyhats.item.ItemHat;

public class AmphithereContainer extends Container {
    private final IInventory amphithereInventory;
    private final EntityAmphithere amphithere;

    public AmphithereContainer(EntityAmphithere amphithere, EntityPlayer player) {
        this.amphithere = amphithere;

        this.amphithereInventory = new EquipmentSlotInventory(amphithere,
                EntityEquipmentSlot.MAINHAND,  // Beak
                EntityEquipmentSlot.HEAD,     // Head
                EntityEquipmentSlot.CHEST,    // Body
                EntityEquipmentSlot.LEGS,     // Wings
                EntityEquipmentSlot.FEET,     // Tail
                EntityEquipmentSlot.OFFHAND   // Classy Hat
        );

        amphithereInventory.openInventory(player);

        int yOffset = (3 - 4) * 18;

        // Beak (MAINHAND) - Slot 0
        if(ConfigHandler.amphithereArmor.enableAmphithereArmor) {
            this.addSlotToContainer(new Slot(amphithereInventory, 0, 8, 18) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem() instanceof BeakBase;
                }
            });

            // Head (HEAD) - Slot 1
            this.addSlotToContainer(new Slot(amphithereInventory, 1, 8, 36) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem() instanceof ArmorBase && ((ArmorBase) stack.getItem()).armorType == EntityEquipmentSlot.HEAD;
                }
            });

            // Body (CHEST) - Slot 2
            this.addSlotToContainer(new Slot(amphithereInventory, 2, 8, 72) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem() instanceof ArmorBase && ((ArmorBase) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
                }
            });

            // Wings (LEGS) - Slot 3
            this.addSlotToContainer(new Slot(amphithereInventory, 3, 8, 54) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem() instanceof ArmorBase && ((ArmorBase) stack.getItem()).armorType == EntityEquipmentSlot.LEGS;
                }
            });

            // Tail (FEET) - Slot 4
            this.addSlotToContainer(new Slot(amphithereInventory, 4, 8, 90) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem() instanceof ArmorBase && ((ArmorBase) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
                }
            });
        }

        // Classy Hat (OFFHAND) - Slot 5
        if (Loader.isModLoaded("classyhats")) {
            this.addSlotToContainer(new Slot(amphithereInventory, 5, 153, 18) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem() instanceof ItemHat;
                }
            });
        }

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9,
                        8 + col * 18, 150 + row * 18 + yOffset));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 208 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.amphithere.isEntityAlive() && this.amphithere.getDistance(playerIn) < 8.0F;
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack sourceStack = slot.getStack();
            itemstack = sourceStack.copy();

            int containerSize = 6;

            if (index < containerSize) {
                if (!this.mergeItemStack(sourceStack, containerSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                Item item = sourceStack.getItem();

                if (item instanceof BeakBase) {
                    if (!this.mergeItemStack(sourceStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (item instanceof ArmorBase && ConfigHandler.amphithereArmor.enableAmphithereArmor) {
                    ArmorBase armor = (ArmorBase) item;
                    if (armor.armorType == EntityEquipmentSlot.HEAD) {
                        if (!this.mergeItemStack(sourceStack, 1, 2, false)) return ItemStack.EMPTY;
                    } else if (armor.armorType == EntityEquipmentSlot.CHEST) {
                        if (!this.mergeItemStack(sourceStack, 2, 3, false)) return ItemStack.EMPTY;
                    } else if (armor.armorType == EntityEquipmentSlot.LEGS) {
                        if (!this.mergeItemStack(sourceStack, 3, 4, false)) return ItemStack.EMPTY;
                    } else if (armor.armorType == EntityEquipmentSlot.FEET) {
                        if (!this.mergeItemStack(sourceStack, 4, 5, false)) return ItemStack.EMPTY;
                    }
                } else if (item instanceof ItemHat && Loader.isModLoaded("classyhats")) {
                    if (!this.mergeItemStack(sourceStack, 5, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    int playerInventoryStartIndex = containerSize;
                    int hotbarStartIndex = this.inventorySlots.size() - 9;

                    if (index >= playerInventoryStartIndex && index < hotbarStartIndex) {
                        if (!this.mergeItemStack(sourceStack, hotbarStartIndex, this.inventorySlots.size(), false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= hotbarStartIndex) {
                        if (!this.mergeItemStack(sourceStack, playerInventoryStartIndex, hotbarStartIndex, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (sourceStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.amphithereInventory.closeInventory(playerIn);
    }
}