package amphitheremod.util;

import net.minecraft.util.ResourceLocation;

public enum EnumAmphiType {
    BLUE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/blue", true),
    GREEN(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/green", true),
    OLIVE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/olive", true),
    RED(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/red", true),
    YELLOW(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/yellow", true),
    PURPLE(Group.NORMAL, Eyes.YELLOW, Glow.NONE, "default_variants/purple", true),

    BLACK(Group.RARE, Eyes.YELLOW, Glow.NONE, "new_variants/rare/black", true),
    WHITE(Group.RARE, Eyes.PINK, Glow.NONE, "new_variants/rare/white", true),
    RADISH(Group.RARE, Eyes.LIME, Glow.NONE, "new_variants/rare/radishe", true),

    BLACK_GEM(Group.GEM_RARE, Eyes.LIME, Glow.NONE, "new_variants/rare_gem/black", true),
    WHITE_GEM(Group.GEM_RARE, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/rare_gem/white", true),

    BLUE_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/blue", true),
    CYAN_GEM(Group.GEM, Eyes.LIME, Glow.NONE, "new_variants/gem/cyan", true),
    GREEN_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/green", true),
    LIME_GEM(Group.GEM, Eyes.ORANGE, Glow.NONE, "new_variants/gem/lime", true),
    OLIVE_GEM(Group.GEM, Eyes.PINK, Glow.NONE, "new_variants/gem/olive", true),
    RED_GEM(Group.GEM, Eyes.PURPLE, Glow.NONE, "new_variants/gem/red", true),
    YELLOW_GEM(Group.GEM, Eyes.MAGENTA, Glow.NONE, "new_variants/gem/yellow", true),
    PINK_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/pink", true),
    PURPLE_GEM(Group.GEM, Eyes.LIGHT_BLUE, Glow.NONE, "new_variants/gem/purple", true),

    SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/skeleton", false),
    WITHER_SKELETON(Group.SKELETON, Eyes.NONE, Glow.NONE, "new_variants/skeleton/wither_skeleton", false),

    RAINBOW(Group.SPECIAL, Eyes.YELLOW, Glow.NONE, "new_variants/special/rainbow", true),
    SHIVAXI(Group.SPECIAL, Eyes.SHIVAXI, Glow.SHIVAXI_GLOW, "new_variants/special/shivaxi", true),
    IQURY(Group.SPECIAL, Eyes.IQURY, Glow.IQURY_GLOW, "new_variants/special/iqury", true),
    BLACKEAGLE(Group.SPECIAL, Eyes.BLACKEAGLE, Glow.BLACKEAGLE_GLOW, "new_variants/special/black_eagle", true),
    CRAFTY(Group.SPECIAL, Eyes.CRAFTY, Glow.NONE, "new_variants/special/crafty", true);

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

    EnumAmphiType(Group group, Eyes eyes, Glow glow, String loc, boolean hasBlinkVariant) {
        this.group = group;
        this.eyes = eyes;
        this.glow = glow;
        this.loc = new ResourceLocation("amphitheremod:textures/entity/amphithere/"+loc+".png");
        this.loc_blink = hasBlinkVariant ? new ResourceLocation("amphitheremod:textures/entity/amphithere/"+loc+"_blink.png") : null;
    }

    public enum Group {
        NORMAL,
        RARE,
        GEM_RARE,
        GEM,
        SKELETON,
        SPECIAL
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
        IQURY_GLOW,
        BLACKEAGLE_GLOW
    }
}
