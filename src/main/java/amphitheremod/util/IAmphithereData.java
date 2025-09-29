package amphitheremod.util;

import org.spongepowered.asm.mixin.Unique;

public interface IAmphithereData {
    @Unique boolean amphiMod_master$getGender();
    @Unique void amphiMod_master$setGender(boolean gender);

    @Unique String amphiMod_master$getSpecialVariant();
    @Unique void amphiMod_master$setSpecialVariant(String specialVariant);

    @Unique boolean amphiMod_master$getBounded();
    @Unique void amphiMod_master$setBounded(boolean bound);
}