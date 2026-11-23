package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import amphitheremod.entity.EntityAmphithereEgg;
import amphitheremod.registry.AmphiEntities;
import amphitheremod.util.AmphiBreedRules;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Breeding: babies get a variant from the colour mixing rules (or an egg is laid instead), only a male and a female can
 * breed, and the skeleton and named special variants can't breed at all.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiBreedingMixin extends TamableAnimal {
    protected AmphiBreedingMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "getBreedOffspring", at = @At("HEAD"), cancellable = true)
    private void amphiMod$breed(ServerLevel level, AgeableMob partner, CallbackInfoReturnable<AgeableMob> cir) {
        if (!(partner instanceof EntityAmphithere other)) {
            return;
        }
        EntityAmphithere self = (EntityAmphithere) (Object) this;
        // The mixing rules are keyed with the lower ordinal first
        EnumAmphiType a = EnumAmphiType.fromOrdinal(Math.min(self.getVariant(), other.getVariant()));
        EnumAmphiType b = EnumAmphiType.fromOrdinal(Math.max(self.getVariant(), other.getVariant()));
        int childVariant = AmphiBreedRules.childVariant(a, b, this.random);
        if (AmphiConfig.ENABLE_EGGS.get()) {
            EntityAmphithereEgg egg = new EntityAmphithereEgg(AmphiEntities.AMPHITHERE_EGG.get(), level);
            egg.setVariantType(EnumAmphiType.fromOrdinal(childVariant));
            egg.setPos(this.getX(), this.getY(), this.getZ());
            level.addFreshEntity(egg);
            // Vanilla only resets love and starts the breeding cooldown when a baby is returned, so do it here or the
            // pair would keep laying eggs every tick while still in love
            this.setAge(6000);
            other.setAge(6000);
            this.resetLove();
            other.resetLove();
            cir.setReturnValue(null);
            return;
        }
        EntityAmphithere child = IafEntityRegistry.AMPHITHERE.get().create(level);
        if (child != null) {
            child.setVariant(childVariant);
            ((IAmphithereData) child).amphiMod$setFemale(this.random.nextBoolean());
        }
        cir.setReturnValue(child);
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (!(otherAnimal instanceof EntityAmphithere other) || !super.canMate(otherAnimal)) {
            return false;
        }
        EntityAmphithere self = (EntityAmphithere) (Object) this;
        if (this.isBaby() || other.isBaby() || this.isVehicle() || other.isVehicle()) {
            return false;
        }
        if (AmphiBreedRules.cannotBreed(EnumAmphiType.fromOrdinal(self.getVariant())) || AmphiBreedRules.cannotBreed(EnumAmphiType.fromOrdinal(other.getVariant()))) {
            return false;
        }
        if (AmphiConfig.MALE_AND_FEMALE.get()) {
            return ((IAmphithereData) self).amphiMod$isFemale() != ((IAmphithereData) other).amphiMod$isFemale();
        }
        return true;
    }
}
