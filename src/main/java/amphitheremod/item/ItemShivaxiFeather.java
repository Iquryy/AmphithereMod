package amphitheremod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/** Dropped by the Shivaxi amphithere. Glints and is epic rarity, like 1.12.2. */
public class ItemShivaxiFeather extends Item {
    public ItemShivaxiFeather() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
