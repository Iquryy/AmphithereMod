package amphitheremod.client.layer.armor;

import amphitheremod.client.layer.AbstractAmphithereLayer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.Refs.EMPTY;

@SideOnly(Side.CLIENT)
public class LayerAmphithereTailArmor extends AbstractAmphithereLayer {
    public LayerAmphithereTailArmor(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (amphithere.hasItemInSlot(EntityEquipmentSlot.FEET)) {
            ItemArmor headSlot = (ItemArmor) amphithere.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem();
            String location = getArmorFolder(headSlot.getArmorMaterial());
            return new ResourceLocation("amphitheremod:textures/amphithere_armor/"+location+"/tail_armor.png");
        } else return EMPTY;
    }
}