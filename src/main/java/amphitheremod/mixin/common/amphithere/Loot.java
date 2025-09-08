package amphitheremod.mixin.common.amphithere;

import amphitheremod.config.ConfigHandler;
import amphitheremod.handlers.ModRegistry;
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
public abstract class Loot {
    @Inject(method = "getLootTable", at = @At("TAIL"))
    private void onDeathDropInventory(CallbackInfoReturnable<ResourceLocation> cir) {
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        IAmphithereData data = (IAmphithereData) amphithere;
        if (data.amphiMod_master$getShivaxi() && !amphithere.world.isRemote) {
            int featherCount = 1 + amphithere.getRNG().nextInt(14);
            if (featherCount > 0)
                amphithere.entityDropItem(new ItemStack(ModRegistry.SHIVAXI_FEATHER, featherCount), 0.0F);
        }
        if (ConfigHandler.general.enableAmphithereInventory) {
            if (!amphithere.world.isRemote) {
                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                    ItemStack stack = amphithere.getItemStackFromSlot(slot);
                    if (!stack.isEmpty()) {
                        amphithere.entityDropItem(stack, 0.0F);
                    }
                }
            }
        }
    }
}
