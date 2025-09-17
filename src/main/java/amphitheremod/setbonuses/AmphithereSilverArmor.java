package amphitheremod.setbonuses;

import amphitheremod.AmphithereMod;
import amphitheremod.config.ConfigHandler;
import amphitheremod.handlers.ModRegistry;
import amphitheremod.util.IceAndFireUtil;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class AmphithereSilverArmor {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (ConfigHandler.general.enableSilverSetBonus) {
            if (IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT) {
                if (Loader.isModLoaded("potioncore")) {
                    if (!(event.getEntityLiving() instanceof EntityAmphithere)) return;
                    EntityAmphithere amphi = (EntityAmphithere) event.getEntityLiving();
                    if (amphi.world.isRemote) return;
                    Potion curePotion = Potion.getPotionFromResourceLocation("potioncore:cure");
                    if (curePotion == null) return;
                    ItemStack beak = amphi.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    ItemStack head = amphi.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    ItemStack body = amphi.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    ItemStack wings = amphi.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                    ItemStack tail = amphi.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                    boolean isWearingFullSet = !beak.isEmpty() && beak.getItem() == ModRegistry.AMPHITHERE_SILVER_BEAK_ATTACHMENT && !head.isEmpty() && head.getItem() == ModRegistry.AMPHITHERE_SILVER_HEAD_ARMOR && !body.isEmpty() && body.getItem() == ModRegistry.AMPHITHERE_SILVER_BODY_ARMOR && !wings.isEmpty() && wings.getItem() == ModRegistry.AMPHITHERE_SILVER_WING_ARMOR && !tail.isEmpty() && tail.getItem() == ModRegistry.AMPHITHERE_SILVER_TAIL_ARMOR;
                    boolean hasEffect = amphi.isPotionActive(curePotion);
                    if (amphi.ticksExisted % 100 != 0) return;
                    if (isWearingFullSet && !hasEffect)
                        amphi.addPotionEffect(new PotionEffect(curePotion, 200, 0, true, false));
                    else if (!isWearingFullSet && hasEffect)
                        amphi.removePotionEffect(curePotion);
                }
            }
        }
    }
}