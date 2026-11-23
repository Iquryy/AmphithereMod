package amphitheremod.util;

/**
 * Extra data this mod adds to Ice and Fire's EntityAmphithere (through mixin.AmphithereDataMixin). Every value is synced
 * to clients so renderers, the inventory screen and the stamina bar can read it. Cast an EntityAmphithere to use it.
 */
public interface IAmphithereData {
    /** true = female, false = male. */
    boolean amphiMod$isFemale();

    void amphiMod$setFemale(boolean female);

    /** "" for none, otherwise "Shivaxi" or "Black Eagle". These keep their special stats on reload. */
    String amphiMod$getSpecialVariant();

    void amphiMod$setSpecialVariant(String specialVariant);

    /** EnumAmphiType.WingPattern name, or "" for none. */
    String amphiMod$getWingPattern();

    void amphiMod$setWingPattern(String wingPattern);

    /** Soul bound to its owner with a Crystal Feather. */
    boolean amphiMod$isBound();

    void amphiMod$setBound(boolean bound);

    float amphiMod$getStamina();

    /** Clamped to 0..max stamina. */
    void amphiMod$setStamina(float stamina);

    float amphiMod$getMaxStamina();

    void amphiMod$setMaxStamina(float maxStamina);

    /** Ticks left before stamina can regenerate. */
    int amphiMod$getStaminaCooldown();

    void amphiMod$setStaminaCooldown(int cooldown);

    /** Fed a Bloody Cookie: COTH is removed every tick. */
    boolean amphiMod$isCothImmune();

    void amphiMod$setCothImmune(boolean immune);

    /** Bloody Cookie: immune to COTH from now on, at the cost of about 10% max health. */
    void amphiMod$makeCothImmune();
}
