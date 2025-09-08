package amphitheremod.mixin.common.amphithere;

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
        IAmphithereData amphi2 = (IAmphithereData) otherAnimal;
        if (!super.canMateWith(otherAnimal)) return false;
        if (this.isChild() || otherAnimal.isChild()) return false;
        if (this.isBeingRidden() || otherAnimal.isBeingRidden()) return false;

        if (this.amphiMod_master$getShivaxi() || amphi2.amphiMod_master$getShivaxi()) return false;
        else return this.amphiMod_master$getGender() != ((IAmphithereData) otherAnimal).amphiMod_master$getGender();
    }
}