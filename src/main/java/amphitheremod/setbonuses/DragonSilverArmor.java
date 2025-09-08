package amphitheremod.setbonuses;

import amphitheremod.AmphithereMod;
import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class DragonSilverArmor {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (ConfigHandler.general.enableSilverSetBonus) {
            if (Loader.isModLoaded("potioncore")) {
                if (!(event.getEntityLiving() instanceof EntityDragonBase)) return;
                EntityDragonBase dragon = (EntityDragonBase) event.getEntityLiving();
                if (dragon.world.isRemote) return;
                Potion curePotion = Potion.getPotionFromResourceLocation("potioncore:cure");
                if (curePotion == null) return;
                ItemStack head = dragon.dragonInv.getStackInSlot(0);
                ItemStack neck = dragon.dragonInv.getStackInSlot(1);
                ItemStack body = dragon.dragonInv.getStackInSlot(2);
                ItemStack tail = dragon.dragonInv.getStackInSlot(3);
                boolean isWearingFullSet = !head.isEmpty() && head.getItem() == IafItemRegistry.dragon_armor_silver && !neck.isEmpty() && neck.getItem() == IafItemRegistry.dragon_armor_silver && !body.isEmpty() && body.getItem() == IafItemRegistry.dragon_armor_silver && !tail.isEmpty() && tail.getItem() == IafItemRegistry.dragon_armor_silver;
                boolean hasEffect = dragon.isPotionActive(curePotion);
                if (dragon.ticksExisted % 100 != 0) return;
                if (isWearingFullSet && !hasEffect)
                    dragon.addPotionEffect(new PotionEffect(curePotion, 200, 0, true, false));
                else if (!isWearingFullSet && hasEffect)
                    dragon.removePotionEffect(curePotion);
            }
        }
    }
}