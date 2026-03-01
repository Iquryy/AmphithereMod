package amphitheremod.client.layer.armor;

import amphitheremod.client.layer.AbstractAmphithereLayer;
import amphitheremod.item.amphithere_beak_attachment.BeakBase;
import amphitheremod.item.amphithere_beak_attachment.FireDragonBoneBeak;
import amphitheremod.item.amphithere_beak_attachment.IceDragonBoneBeak;
import amphitheremod.item.amphithere_beak_attachment.LightningDragonBoneBeak;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
        if (amphithere.getHeldItemMainhand().getItem() instanceof BeakBase) {
            ItemSword beak = (ItemSword) amphithere.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem();
            Item mainhand = amphithere.getHeldItemMainhand().getItem();
            String material = beak.getToolMaterialName();
            if ((!(mainhand instanceof LightningDragonBoneBeak || mainhand instanceof FireDragonBoneBeak || mainhand instanceof IceDragonBoneBeak))) {
                return new ResourceLocation("amphitheremod:textures/amphithere_beak/" + material + "_beak_attachment.png");
            }
            if (mainhand instanceof LightningDragonBoneBeak || mainhand instanceof IceDragonBoneBeak || mainhand instanceof FireDragonBoneBeak) {
                int ticksPerFrame = 3;
                int totalFrames = 5;
                int currentFrame = (amphithere.ticksExisted / ticksPerFrame) % totalFrames;
                return new ResourceLocation("amphitheremod:textures/amphithere_beak/" + material + "_beak_attachment_" + currentFrame + ".png");
            }
        }
        return EMPTY;
    }
}