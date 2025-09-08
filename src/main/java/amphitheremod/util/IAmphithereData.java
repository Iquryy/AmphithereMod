package amphitheremod.util;

import org.spongepowered.asm.mixin.Unique;

public interface IAmphithereData {
    @Unique boolean amphiMod_master$getGender();
    @Unique void amphiMod_master$setGender(boolean gender);

    @Unique boolean amphiMod_master$getShivaxi();
    @Unique void amphiMod_master$setShivaxi(boolean shivaix);

    @Unique boolean amphiMod_master$getBounded();
    @Unique void amphiMod_master$setBounded(boolean bound);
}