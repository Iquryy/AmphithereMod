package amphitheremod.mixin.common;

import amphitheremod.config.ConfigHandler;
import amphitheremod.entity.EntityAmphithereEgg;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static amphitheremod.util.AmphiBreedRules.*;

@Mixin(value = EntityAmphithere.class)
public abstract class Variants extends EntityAnimal implements IAmphithereData {

    public Variants(World worldIn) {
        super(worldIn);
    }

    // On breed
    @Inject(method = "createChild", at = @At("RETURN"), cancellable = true)
    public void amphiMod_setChildVariantOnBreed(EntityAgeable ageable, CallbackInfoReturnable<EntityAgeable> cir) {
        EntityAmphithere childAmphithere = (EntityAmphithere) cir.getReturnValue();
        if (childAmphithere == null) return;

        EntityAmphithere parent1 = (EntityAmphithere) (Object) this;
        EntityAmphithere parent2 = (EntityAmphithere) ageable;
        int dimension = parent1.world.provider.getDimension();

        List<Integer> parentVariants = Arrays.asList(parent1.getVariant(), parent2.getVariant());
        Collections.sort(parentVariants);
        EnumAmphiType enumParent1 = EnumAmphiType.getEnumNameFromInt(parentVariants.get(0));
        EnumAmphiType enumParent2 = EnumAmphiType.getEnumNameFromInt(parentVariants.get(1));
        int childVariantOrdinal = isValid(enumParent1, enumParent2, dimension, childAmphithere);

        if (ConfigHandler.amphithereEgg.enableAmphithereEggs) {
            if (!this.world.isRemote) {
                EntityAmphithereEgg eggEntity = new EntityAmphithereEgg(this.world);
                eggEntity.setPosition(this.posX, this.posY, this.posZ);
                eggEntity.setType(EnumAmphiType.values()[childVariantOrdinal]);
                this.world.spawnEntity(eggEntity);
                parent1.setGrowingAge(6000);
                parent2.setGrowingAge(6000);
            }
            cir.setReturnValue(null);

        } else {
            childAmphithere.setVariant(childVariantOrdinal);
        }
    }

    // On spawn
    @WrapOperation(method = "onInitialSpawn", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;setVariant(I)V", remap = false))
    public void amphimod_spawnWithOtherVariants(EntityAmphithere amphithere, int variant, Operation<Void> original) {
        original.call(amphithere, rollVariant(amphithere.getRNG(), false));
    }
}