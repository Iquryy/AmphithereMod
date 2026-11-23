package amphitheremod.item;

import amphitheremod.entity.EntityAmphithereEgg;
import amphitheremod.registry.AmphiEntities;
import amphitheremod.util.EnumAmphiType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** One egg item per variant. Placed on top of a block it becomes an EntityAmphithereEgg of that variant. */
public class ItemAmphithereEgg extends Item {
    public final EnumAmphiType variant;

    public ItemAmphithereEgg(EnumAmphiType variant) {
        super(new Item.Properties().stacksTo(1));
        this.variant = variant;
    }

    // Every egg is called "Amphithere Egg"; the tooltip names the variant
    @Override
    public String getDescriptionId() {
        return "item.amphitheremod.amphithere_egg";
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.variant.getGroup().getRarity();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("group.amphitheremod.amphithere." + this.variant.name().toLowerCase()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        }
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos placePos = context.getClickedPos().above();
        if (player != null && !player.mayUseItemAt(placePos, Direction.UP, stack)) {
            return InteractionResult.FAIL;
        }
        Level level = context.getLevel();
        if (!level.isClientSide) {
            EntityAmphithereEgg egg = new EntityAmphithereEgg(AmphiEntities.AMPHITHERE_EGG.get(), level);
            egg.setVariantType(this.variant);
            egg.setPos(placePos.getX() + 0.5, placePos.getY(), placePos.getZ() + 0.5);
            level.addFreshEntity(egg);
        }
        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
