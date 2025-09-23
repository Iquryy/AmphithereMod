package amphitheremod.mixin.common.amphithere;

import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.SharedMonsterAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public abstract class TamingDamage {
    @ModifyExpressionValue(method = "updatePassenger", at = @At(value = "FIELD", target = "Lcom/github/alexthe666/iceandfire/IceAndFireConfig$EntityConfig;amphithereTameDamage:D", remap = false))
    private double tamingDamage(double original) {
        // Thanks to Nischi and cdstk_ALT
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        return Math.max(IceAndFireConfig.ENTITY_SETTINGS.amphithereTameDamage, amphi.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * (IceAndFireConfig.ENTITY_SETTINGS.amphithereTameDamage / IceAndFireConfig.ENTITY_SETTINGS.amphithereAttackStrength));
    }
}