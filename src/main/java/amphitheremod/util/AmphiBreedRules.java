package amphitheremod.util;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AmphiBreedRules {

    //Weights
    private static final int[] WEIGHTS_SPAWN = {60, 20, 20};
    private static final int[] WEIGHTS_CHILD = {40, 30, 30};
    private static final int WEIGHTS_TOTAL_SPAWN = Arrays.stream(WEIGHTS_SPAWN).sum();
    private static final int WEIGHTS_TOTAL_CHILD = Arrays.stream(WEIGHTS_CHILD).sum();
    private static final int RAINBOW = EnumAmphiType.RAINBOW.ordinal();
    static int amphiVariant = 0;

    // Variant ids
    private static final List<List<Integer>> VARIANT_ENUMS = Arrays.asList(
            EnumAmphiType.getIntsByGroup(EnumAmphiType.Group.NORMAL),
            EnumAmphiType.getIntsByGroup(EnumAmphiType.Group.RARE),
            EnumAmphiType.getIntsByGroup(EnumAmphiType.Group.GEM)
    );


    public static int isValid(EnumAmphiType amphi1, EnumAmphiType amphi2, int dimension, EntityAmphithere amphithere) {
        if (amphi1 != null && amphi2 != null) {
            int roll = amphithere.getRNG().nextInt(100) + 1;
            // 70% chance to not inherit parents variant
            if (roll < 80)
                VariantMixRules(amphi1, amphi2, amphithere);
            else {
                // The 30% to inherit one of the parents variant
                if (amphithere.getRNG().nextBoolean())
                    return EnumAmphiType.getIntFromEnum(amphi1);
                else
                    return EnumAmphiType.getIntFromEnum(amphi2);
            }
        } else {
            amphiVariant = rollVariant(amphithere.getRNG(), true);
        }
        return amphiVariant;
    }

    public static int rollVariant(Random rand, boolean isFromBreed) {
        if (rand.nextInt(100) == 0) return RAINBOW;
        int randRoll = rand.nextInt(isFromBreed ? WEIGHTS_TOTAL_CHILD : WEIGHTS_TOTAL_SPAWN);
        int[] weights = isFromBreed ? WEIGHTS_CHILD : WEIGHTS_SPAWN;

        //Weighted roll
        for (int i = 0; i < weights.length; i++) {
            randRoll -= weights[i];
            if (randRoll < 0) return getRandomEntryForAmphiType(rand, i);
        }
        return getRandomEntryForAmphiType(rand, 0);
    }

    private static int getRandomEntryForAmphiType(Random rand, int type) {
        List<Integer> group = VARIANT_ENUMS.get(type);
        return group.get(rand.nextInt(group.size()));
    }

    static boolean isAffectedByBreedRules(Random rand) {
        return rand.nextBoolean();
    }

    static void VariantMixRules(EnumAmphiType amphi1, EnumAmphiType amphi2, EntityAmphithere amphithere) {
        if (isAffectedByBreedRules(amphithere.getRNG())) {
            if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.YELLOW) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN);
            } else if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.RED) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PURPLE);
            } else if (amphi1 == EnumAmphiType.RED && amphi2 == EnumAmphiType.YELLOW) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.OLIVE);
            } else if (amphi1 == EnumAmphiType.GREEN && amphi2 == EnumAmphiType.RED) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.RADISH);
            } else if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.RED_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PURPLE_GEM);
            } else if (amphi1 == EnumAmphiType.RED && amphi2 == EnumAmphiType.BLUE_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PURPLE_GEM);
            } else if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.YELLOW_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN);
            } else if (amphi1 == EnumAmphiType.RED && amphi2 == EnumAmphiType.YELLOW_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.OLIVE);
            } else if (amphi1 == EnumAmphiType.YELLOW && amphi2 == EnumAmphiType.BLUE_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN_GEM);
            } else if (amphi1 == EnumAmphiType.BLUE_GEM && amphi2 == EnumAmphiType.YELLOW_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN_GEM);
            } else if (amphi1 == EnumAmphiType.BLUE_GEM && amphi2 == EnumAmphiType.RED_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PURPLE_GEM);
            } else if (amphi1 == EnumAmphiType.RED_GEM && amphi2 == EnumAmphiType.YELLOW_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.OLIVE_GEM);
            } else if (amphi1 == EnumAmphiType.RED && amphi2 == EnumAmphiType.WHITE) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PINK_GEM);
            } else if (amphi1 == EnumAmphiType.PURPLE && amphi2 == EnumAmphiType.WHITE) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PINK_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE && amphi2 == EnumAmphiType.BLUE_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.CYAN_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE && amphi2 == EnumAmphiType.RED_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PINK_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE && amphi2 == EnumAmphiType.GREEN_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.LIME_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE && amphi2 == EnumAmphiType.PURPLE_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PINK_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE_GEM && amphi2 == EnumAmphiType.RED_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.PINK_GEM);
            } else if (amphi1 == EnumAmphiType.WHITE_GEM && amphi2 == EnumAmphiType.GREEN_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.LIME_GEM);
            } else if (amphi1 == EnumAmphiType.BLACK && amphi2 == EnumAmphiType.LIME_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN_GEM);
            } else if (amphi1 == EnumAmphiType.BLACK && amphi2 == EnumAmphiType.YELLOW_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.OLIVE_GEM);
            } else if (amphi1 == EnumAmphiType.BLACK_GEM && amphi2 == EnumAmphiType.LIME_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.GREEN_GEM);
            } else if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.GREEN) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.CYAN_GEM);
            } else if (amphi1 == EnumAmphiType.BLUE && amphi2 == EnumAmphiType.GREEN_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.CYAN_GEM);
            } else if (amphi1 == EnumAmphiType.YELLOW && amphi2 == EnumAmphiType.GREEN) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.LIME_GEM);
            } else if (amphi1 == EnumAmphiType.YELLOW && amphi2 == EnumAmphiType.GREEN_GEM) {
                amphiVariant = EnumAmphiType.getIntFromEnum(EnumAmphiType.LIME_GEM);
            }
        } else {
            amphiVariant = rollVariant(amphithere.getRNG(), true);
        }
    }
}