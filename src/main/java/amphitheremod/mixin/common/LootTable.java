package amphitheremod.mixin.common;

import amphitheremod.handlers.ModItemRegistry;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public abstract class LootTable {
    @Inject(method = "getLootTable", at = @At("TAIL"))
    private void onDeathDropInventory(CallbackInfoReturnable<ResourceLocation> cir) {
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        if (amphithere.world.isRemote) return;
        IAmphithereData data = (IAmphithereData) amphithere;
        if (data.amphiMod_master$getSpecialVariant().equals("Shivaxi")) {
            int featherCount = amphithere.getRNG().nextInt(14) + 1;
            amphithere.entityDropItem(new ItemStack(ModItemRegistry.SHIVAXI_FEATHER, featherCount), 0.0F);
        }
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack stack = amphithere.getItemStackFromSlot(slot);
            if (!stack.isEmpty()) {
                amphithere.entityDropItem(stack, 0.0F);
            }
        }
    }
}
