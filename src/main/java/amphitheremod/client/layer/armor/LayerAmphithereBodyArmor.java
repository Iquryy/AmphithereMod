package amphitheremod.client.layer.armor;

import amphitheremod.client.layer.AbstractAmphithereLayer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.Refs.EMPTY;

@SideOnly(Side.CLIENT)
public class LayerAmphithereBodyArmor extends AbstractAmphithereLayer {
    public LayerAmphithereBodyArmor(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (amphithere.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            ItemArmor slot = (ItemArmor) amphithere.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
            String location = getArmorFolder(slot.getArmorMaterial());
            return new ResourceLocation("amphitheremod:textures/amphithere_armor/"+location+"/body_armor.png");
        } else return EMPTY;
    }
}