package amphitheremod.handlers;

import amphitheremod.AmphithereMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static amphitheremod.handlers.ModItemRegistry.itemsToRegister;

@Mod.EventBusSubscriber(modid = AmphithereMod.MODID, value = Side.CLIENT)
public class ClientModRegistry {
    @SubscribeEvent
    public static void modelRegisterEvent(ModelRegistryEvent event) {
        for (Item item : itemsToRegister)
            registerModels(item);
    }

    private static void registerModels(Item... values) {
        for (Item entry : values) {
            ModelLoader.setCustomModelResourceLocation(entry, 0, new ModelResourceLocation(entry.getRegistryName(), "inventory"));
        }
    }
}