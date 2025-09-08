package amphitheremod.client.layer.armor;

import amphitheremod.client.layer.AbstractAmphithereLayer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.Refs.EMPTY;

@SideOnly(Side.CLIENT)
public class LayerAmphithereBeakAttachment extends AbstractAmphithereLayer {
    public LayerAmphithereBeakAttachment(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (amphithere.hasItemInSlot(EntityEquipmentSlot.MAINHAND)) {
            ItemSword beak = (ItemSword) amphithere.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();
            String material = beak.getToolMaterialName();
            return new ResourceLocation("amphitheremod:textures/amphithere_beak/"+material+"_beak_attachment.png");
        } else return EMPTY;
    }
}