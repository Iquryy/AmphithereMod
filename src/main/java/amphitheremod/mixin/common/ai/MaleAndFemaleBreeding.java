package amphitheremod.mixin.common.ai;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.enumm.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import javax.annotation.Nonnull;

@Mixin(value = EntityAmphithere.class)
public abstract class MaleAndFemaleBreeding extends EntityAnimal implements IAmphithereData {
    public MaleAndFemaleBreeding(World worldIn) {
        super(worldIn);
    }

    // Breeding logic
    @Override
    public boolean canMateWith(@Nonnull EntityAnimal otherAnimal) {
        EntityAmphithere amphi2 = (EntityAmphithere) otherAnimal;
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        IAmphithereData amphiAnimal = (IAmphithereData) amphi2;
        if (!super.canMateWith(otherAnimal)) return false;
        if (this.isChild() || otherAnimal.isChild()) return false;
        if (this.isBeingRidden() || otherAnimal.isBeingRidden()) return false;
        if (amphi.getVariant() == EnumAmphiType.SHIVAXI.ordinal() || amphi2.getVariant() == EnumAmphiType.SHIVAXI.ordinal()) return false;
        if (amphi.getVariant() == EnumAmphiType.WITHER_SKELETON.ordinal() || amphi2.getVariant() == EnumAmphiType.WITHER_SKELETON.ordinal()) return false;
        if (amphi.getVariant() == EnumAmphiType.SKELETON.ordinal() || amphi2.getVariant() == EnumAmphiType.SKELETON.ordinal()) return false;
        if (amphi.getVariant() == EnumAmphiType.IQURY.ordinal() || amphi2.getVariant() == EnumAmphiType.IQURY.ordinal()) return false;
        if (amphi.getVariant() == EnumAmphiType.CRAFTY.ordinal() || amphi2.getVariant() == EnumAmphiType.CRAFTY.ordinal()) return false;
        if (amphi.getVariant() == EnumAmphiType.BLACKEAGLE.ordinal() || amphi2.getVariant() == EnumAmphiType.BLACKEAGLE.ordinal()) return false;

        if (ConfigHandler.general.maleAndFemale) return this.amphiMod_master$getGender() != amphiAnimal.amphiMod_master$getGender();
        else return true;
    }
}