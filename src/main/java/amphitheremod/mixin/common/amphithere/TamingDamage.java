package amphitheremod.mixin.common.amphithere;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAmphithere.class)
public abstract class TamingDamage extends EntityTameable {
    // Thanks cdsk
    @Unique
    private static Float amphithereMod$configAttackStrength = null;

    public TamingDamage(World world) {
        super(world);
    }

    @Inject(
            method = "applyEntityAttributes",
            at = @At("TAIL")
    )
    private void aaa(CallbackInfo ci){
        if(amphithereMod$configAttackStrength == null) {
            amphithereMod$configAttackStrength = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        }
    }

    @ModifyArg(
            method = "updatePassenger",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"),
            index = 1
    )
    private float www(float configTameDamage){
        return Math.max(configTameDamage,
                (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()
                        * configTameDamage
                        / amphithereMod$configAttackStrength);
    }
}