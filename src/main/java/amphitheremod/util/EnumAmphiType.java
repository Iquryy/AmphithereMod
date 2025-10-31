package amphitheremod.util;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;

public enum EnumAmphiType {
    BLUE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/blue", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.PURPLE, WingPattern.BLUE}),
    GREEN(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/green", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.PURPLE, WingPattern.BLUE}),
    OLIVE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/olive", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.PURPLE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.AQUA}),
    RED(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/red", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    YELLOW(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/yellow", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    PURPLE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/purple", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.ORANGE, WingPattern.PURPLE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.AQUA, WingPattern.CYAN}),

    BLACK(Group.RARE, Eyes.YELLOW, Glow.NONE, "new_variants/rare/black", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    WHITE(Group.RARE, Eyes.PINK, Glow.NONE, "new_variants/rare/white", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    RADISH(Group.RARE, Eyes.LIME, Glow.NONE, "new_variants/rare/radishe", true, new WingPattern[]{WingPattern.BLACK, WingPattern.RED, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE}),

    BLACK_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/black", true, new WingPattern[]{WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE, WingPattern.BLACK, WingPattern.WHITE, WingPattern.RED, WingPattern.ORANGE}),
    WHITE_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/white", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE}),
    BLUE_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/blue", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.BLUE}),
    CYAN_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/cyan", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE}),
    GREEN_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/green", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE}),
    LIME_GEM(Group.GEM, Eyes.ORANGE, Glow.NONE, "new_variants/gem/lime", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    OLIVE_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/olive", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    RED_GEM(Group.GEM, Eyes.PURPLE, Glow.NONE, "new_variants/gem/red", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    YELLOW_GEM(Group.GEM, Eyes.MAGENTA, Glow.NONE, "new_variants/gem/yellow", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    PINK_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/pink", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),
    PURPLE_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/purple", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.TEAL, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE, WingPattern.AQUA}),

    SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/skeleton", false),
    WITHER_SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/wither_skeleton", false),

    RAINBOW(Group.SPECIAL, Eyes.YELLOW, Glow.NONE, "new_variants/special/rainbow", true, new WingPattern[]{WingPattern.WHITE, WingPattern.BLACK, WingPattern.RED, WingPattern.PURPLE, WingPattern.ORANGE, WingPattern.GREEN, WingPattern.DARK_PURPLE, WingPattern.CYAN, WingPattern.BLUE}),
    SHIVAXI(Group.SPECIAL, Eyes.SHIVAXI, Glow.SHIVAXI_GLOW, "new_variants/special/shivaxi", true),
    IQURY(Group.SPECIAL, Eyes.IQURY, Glow.IQURY_GLOW, "new_variants/special/iqury", true),
    BLACKEAGLE(Group.SPECIAL, Eyes.BLACKEAGLE, Glow.NONE, "new_variants/special/black_eagle", true),
    CRAFTY(Group.SPECIAL, Eyes.CRAFTY, Glow.NONE, "new_variants/special/crafty", true);

    private final String texturePath;
    public String getTexturePath() {
        return texturePath;
    }

    private final Group group;
    public Group getGroup(){
        return group;
    }

    private final Eyes eyes;
    public Eyes getEyes(){
        return eyes;
    }

    private final Glow glow;
    public Glow getGlow(){
        return glow;
    }

    private final WingPattern[] wingPattern;
    public WingPattern[] getWingPattern(){
        return wingPattern;
    }

    public static EnumAmphiType getEnumNameFromInt(int variant){
        return EnumAmphiType.values()[variant];
    }

    public static int getIntFromEnum(EnumAmphiType enumName){
        EnumAmphiType type = EnumAmphiType.valueOf(String.valueOf(enumName));
        return type.ordinal();
    }

    private final ResourceLocation loc;
    private final ResourceLocation loc_blink;
    public ResourceLocation getTexture(boolean isBlinking){
        if(isBlinking && loc_blink != null) return loc_blink;
        else return loc;
    }

    EnumAmphiType(Group group, Eyes eyes, Glow glow, String loc, boolean hasBlinkVariant, WingPattern[] wingPattern) {
        this.group = group;
        this.eyes = eyes;
        this.glow = glow;
        this.wingPattern = wingPattern;
        this.texturePath = loc;
        this.loc = new ResourceLocation("amphitheremod:textures/entity/amphithere/"+loc+".png");
        this.loc_blink = hasBlinkVariant ? new ResourceLocation("amphitheremod:textures/entity/amphithere/"+loc+"_blink.png") : null;
    }

    EnumAmphiType(Group group, Eyes eyes, Glow glow, String loc, boolean hasBlinkVariant) {
        this(group, eyes, glow, loc, hasBlinkVariant, new WingPattern[]{WingPattern.NONE});
    }

    public enum Group {
        NORMAL(EnumRarity.COMMON),
        RARE(EnumRarity.UNCOMMON),
        GEM(EnumRarity.RARE),
        SKELETON(EnumRarity.EPIC),
        SPECIAL(EnumRarity.EPIC);

        private final EnumRarity rarity;

        Group(EnumRarity rarity) {
            this.rarity = rarity;
        }

        public EnumRarity getRarity() {
            return this.rarity;
        }
    }

    public enum Eyes {
        NONE, //Does the same as normal (not render the eyes layer) but gets its own type just for safety
        NORMAL,
        YELLOW,
        PINK,
        LIME,
        LIGHT_BLUE,
        MAGENTA,
        ORANGE,
        PURPLE,
        SHIVAXI,
        IQURY,
        BLACKEAGLE,
        CRAFTY
    }

    public enum Glow {
        NONE,
        SHIVAXI_GLOW,
        IQURY_GLOW
    }

    public enum WingPattern {
        NONE,
        AQUA,
        BLUE,
        CYAN,
        DARK_PURPLE,
        GREEN,
        ORANGE,
        PURPLE,
        RED,
        TEAL,
        BLACK,
        WHITE,
    }

    public static java.util.List<Integer> getIntsByGroup(Group group) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getGroup() == group)
                .map(EnumAmphiType::ordinal)
                .collect(java.util.stream.Collectors.toList());
    }
}