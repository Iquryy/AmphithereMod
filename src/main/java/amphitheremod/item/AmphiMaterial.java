package amphitheremod.item;

import net.minecraft.world.entity.EquipmentSlot;

/**
 * Armor and beak materials. Armor points and beak damage are the 1.12.2 armor/tool material values the items were
 * built on (copper and silver were Ice and Fire's materials, the rest vanilla), so the stats match the old mod.
 */
public enum AmphiMaterial {
    // name, armor points {tail(feet), wings(legs), body(chest), head}, sword material attack damage
    COPPER("copper", new int[]{1, 3, 4, 2}, 1.5F),
    IRON("iron", new int[]{2, 5, 6, 2}, 2.0F),
    GOLD("gold", new int[]{1, 3, 5, 2}, 0.0F),
    DIAMOND("diamond", new int[]{3, 6, 8, 3}, 3.0F),
    SILVER("silver", new int[]{1, 4, 5, 2}, 1.0F),
    // Beak-only materials
    DRAGONBONE("dragonbone", null, 4.0F),
    FIRE("fire", null, 5.5F),
    ICE("ice", null, 5.5F),
    LIGHTNING("lightning", null, 5.5F);

    public final String name;
    private final int[] armor;
    private final float swordDamage;

    AmphiMaterial(String name, int[] armor, float swordDamage) {
        this.name = name;
        this.armor = armor;
        this.swordDamage = swordDamage;
    }

    /** Armor points for one piece before the armor divider config. */
    public int armorFor(EquipmentSlot slot) {
        return this.armor == null ? 0 : this.armor[slot.getIndex()];
    }

    /** Total attack damage a 1.12.2 sword of this material had (1 base + 3 + material damage). */
    public float swordAttackDamage() {
        return 4.0F + this.swordDamage;
    }

    /** Elemental beaks have animated textures and on-hit effects. */
    public boolean isElemental() {
        return this == FIRE || this == ICE || this == LIGHTNING;
    }
}
