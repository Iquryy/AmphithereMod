package amphitheremod.util;

import org.spongepowered.asm.mixin.Unique;

public interface IAmphithereData {
    @Unique boolean amphiMod_master$getGender();
    @Unique void amphiMod_master$setGender(boolean gender);

    @Unique String amphiMod_master$getSpecialVariant();
    @Unique void amphiMod_master$setSpecialVariant(String specialVariant);

    @Unique String amphiMod_master$getWingPattern();
    @Unique void amphiMod_master$setWingPattern(String wingPattern);

    @Unique boolean amphiMod_master$getBounded();
    @Unique void amphiMod_master$setBounded(boolean bound);

    @Unique float amphiMod_master$getStamina();
    @Unique void amphiMod_master$setStamina(float stamina);

    @Unique float amphiMod_master$getMaxStamina();
    @Unique void amphiMod_master$setMaxStamina(float maxStamina);

    @Unique int amphiMod_master$getStaminaCD();

    @Unique void amphiMod_master$setStaminaCD(int cd);
}