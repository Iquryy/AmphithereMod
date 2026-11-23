package amphitheremod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Every amphithere variant. The ordinal is what's stored in Ice and Fire's "Variant" field, so the first five
 * (blue, green, olive, red, yellow) line up with Ice and Fire's own variants and existing amphitheres keep their look.
 * Don't reorder: saved amphitheres and egg items refer to these by ordinal.
 */
public enum EnumAmphiType {
    BLUE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/blue", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.PURPLE, WingPattern.BLUE)),
    GREEN(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/green", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.PURPLE, WingPattern.BLUE)),
    OLIVE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/olive", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.PURPLE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.AQUA)),
    RED(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/red", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    YELLOW(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/yellow", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    PURPLE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/purple", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.PURPLE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.AQUA, WingPattern.CYAN)),
    BLACK(Group.RARE, Eyes.YELLOW, Glow.NONE, "new_variants/rare/black", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    WHITE(Group.RARE, Eyes.PINK, Glow.NONE, "new_variants/rare/white", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    RADISH(Group.RARE, Eyes.LIME, Glow.NONE, "new_variants/rare/radishe", true, wings(WingPattern.BLACK, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE)),
    BLACK_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/black", true, wings(WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.BLACK, WingPattern.WHITE, WingPattern.RED, WingPattern.ORANGE)),
    WHITE_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/white", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE)),
    BLUE_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/blue", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE)),
    CYAN_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/cyan", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE)),
    GREEN_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/green", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE)),
    LIME_GEM(Group.GEM, Eyes.ORANGE, Glow.NONE, "new_variants/gem/lime", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    OLIVE_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/olive", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    RED_GEM(Group.GEM, Eyes.PURPLE, Glow.NONE, "new_variants/gem/red", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    YELLOW_GEM(Group.GEM, Eyes.MAGENTA, Glow.NONE, "new_variants/gem/yellow", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    PINK_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/pink", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    PURPLE_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/purple", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA)),
    SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/skeleton", false),
    WITHER_SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/wither_skeleton", false),
    RAINBOW(Group.SPECIAL, Eyes.YELLOW, Glow.NONE, "new_variants/special/rainbow", true, wings(WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE)),
    SHIVAXI(Group.SPECIAL, Eyes.SHIVAXI, Glow.SHIVAXI_GLOW, "new_variants/special/shivaxi", true),
    IQURY(Group.SPECIAL, Eyes.IQURY, Glow.IQURY_GLOW, "new_variants/special/iqury", true),
    BLACKEAGLE(Group.SPECIAL, Eyes.BLACKEAGLE, Glow.NONE, "new_variants/special/black_eagle", true),
    CRAFTY(Group.SPECIAL, Eyes.CRAFTY, Glow.NONE, "new_variants/special/crafty", true);

    private final String texturePath;
    private final Group group;
    private final Eyes eyes;
    private final Glow glow;
    private final WingPattern[] wingPattern;
    private final ResourceLocation loc;
    private final ResourceLocation locBlink;

    EnumAmphiType(Group group, Eyes eyes, Glow glow, String loc, boolean hasBlinkVariant, WingPattern... wingPattern) {
        this.group = group;
        this.eyes = eyes;
        this.glow = glow;
        // Variants with no pattern list can't roll a wing pattern
        this.wingPattern = wingPattern.length == 0 ? new WingPattern[]{WingPattern.NONE} : wingPattern;
        this.texturePath = loc;
        this.loc = new ResourceLocation("amphitheremod", "textures/entity/amphithere/" + loc + ".png");
        this.locBlink = hasBlinkVariant ? new ResourceLocation("amphitheremod", "textures/entity/amphithere/" + loc + "_blink.png") : null;
    }

    private static WingPattern[] wings(WingPattern... patterns) {
        return patterns;
    }

    public String getTexturePath() {
        return this.texturePath;
    }

    public Group getGroup() {
        return this.group;
    }

    public Eyes getEyes() {
        return this.eyes;
    }

    public Glow getGlow() {
        return this.glow;
    }

    public WingPattern[] getWingPattern() {
        return this.wingPattern;
    }

    public ResourceLocation getTexture(boolean isBlinking) {
        return isBlinking && this.locBlink != null ? this.locBlink : this.loc;
    }

    /** Variant ordinal to type; out of range values (from another mod or a bad command) fall back to blue. */
    public static EnumAmphiType fromOrdinal(int variant) {
        EnumAmphiType[] values = values();
        return variant >= 0 && variant < values.length ? values[variant] : BLUE;
    }

    public static List<Integer> getIntsByGroup(Group group) {
        return Arrays.stream(values()).filter(t -> t.getGroup() == group).map(Enum::ordinal).collect(Collectors.toList());
    }

    public enum WingPattern {
        NONE, AQUA, BLUE, CYAN, DARK_PURPLE, GREEN, ORANGE, PURPLE, RED, TEAL, BLACK, WHITE;

        /** Overlay texture, or null for NONE / unknown names. */
        public static ResourceLocation texture(String name) {
            if (name == null || name.isEmpty() || name.equals("NONE")) {
                return null;
            }
            for (WingPattern p : values()) {
                if (p.name().equals(name)) {
                    return new ResourceLocation("amphitheremod", "textures/entity/amphithere/wing_patterns/dented/" + name.toLowerCase() + ".png");
                }
            }
            return null;
        }
    }

    public enum Glow {
        NONE(null), SHIVAXI_GLOW("shivaxi_glow"), IQURY_GLOW("iqury_glow");

        public final ResourceLocation texture;

        Glow(String file) {
            this.texture = file == null ? null : new ResourceLocation("amphitheremod", "textures/entity/amphithere/glow/" + file + ".png");
        }
    }

    public enum Eyes {
        NONE(null), NORMAL(null), YELLOW("yellow_eyes"), PINK("pink_eyes"), LIME("lime_eyes"), LIGHT_BLUE("light_blue_eyes"),
        MAGENTA("magenta_eyes"), ORANGE("orange_eyes"), PURPLE("purple_eyes"), SHIVAXI("shivaxi_eyes"), IQURY("iqury_eyes"),
        BLACKEAGLE("black_eagle_eyes"), CRAFTY("crafty_eyes");

        /** Glowing eye overlay, or null for no overlay. */
        public final ResourceLocation texture;

        Eyes(String file) {
            this.texture = file == null ? null : new ResourceLocation("amphitheremod", "textures/entity/amphithere/eyes/" + file + ".png");
        }
    }

    public enum Group {
        NORMAL(Rarity.COMMON), RARE(Rarity.UNCOMMON), GEM(Rarity.RARE), SKELETON(Rarity.EPIC), SPECIAL(Rarity.EPIC);

        private final Rarity rarity;

        Group(Rarity rarity) {
            this.rarity = rarity;
        }

        public Rarity getRarity() {
            return this.rarity;
        }
    }
}
