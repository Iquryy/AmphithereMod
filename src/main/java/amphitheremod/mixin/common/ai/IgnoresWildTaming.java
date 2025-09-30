package amphitheremod.mixin.common.ai;

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
        if(target instanceof EntityAmphithere && (!((EntityAmphithere) target).isTamed()) && (((EntityAmphithere) target).getRider() == owner)) {
            ((EntityAmphithere) target).setAttackTarget(null);
            target.setRevengeTarget(null);
            ((EntityAmphithere) target).setCommand(1);
            return false;
        }
        return super.shouldAttackEntity(target, owner);
    }
}