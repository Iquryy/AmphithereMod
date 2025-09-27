package amphitheremod.mixin.common.amphithere;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityAmphithere.class)
public abstract class IgnoresWildTaming extends EntityTameable {

    public IgnoresWildTaming(World world) {
        super(world);
    }

    @Unique
    @Override
    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        // Do not target a Wild Amphi if the owner is riding it
        if(target instanceof EntityAmphithere && target.isPassenger(owner)) {
            return false;
        }

        return super.shouldAttackEntity(target, owner);
    }
}