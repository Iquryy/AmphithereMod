package amphitheremod.util.AmphiBreedingRules;
import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AmphiBreedRules {

    //Weights
    private static final int[] WEIGHTS_SPAWN = {80, 5, 5, 9}; //Normal, added, gem added, gem
    private static final int[] WEIGHTS_CHILD = {60, 10, 10, 20}; //Normal, added, gem added, gem
    private static final int WEIGHTS_TOTAL_SPAWN = Arrays.stream(WEIGHTS_SPAWN).sum();
    private static final int WEIGHTS_TOTAL_CHILD = Arrays.stream(WEIGHTS_CHILD).sum();
    private static final int RAINBOW = 6;

    // Variant ids
    private static final List<List<Integer>> VARIANT_ENUMS = Arrays.asList(
            Arrays.asList(0, 1, 2, 3, 4, 5),                        //Normal
            Arrays.asList(7, 8, 9),                                 //Added
            Arrays.asList(10, 11),                                  //Gem added
            Arrays.asList(12, 13, 14, 15, 16, 17, 18, 19, 20)       /*Gem*/);

    static int amphiVariant = 0;

    public static int isValid(EnumAmphiType amphi1, EnumAmphiType amphi2, int dimension, EntityAmphithere amphithere) {
        if (amphi1 != null && amphi2 != null) {
            int roll = amphithere.getRNG().nextInt(100) + 1;
            // 70% chance to not inherit parents variant
            if (roll < 80)
                CheckDimension(amphi1, amphi2, dimension, amphithere);
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

    static void CheckDimension(EnumAmphiType amphi1, EnumAmphiType amphi2, int dimension, EntityAmphithere amphithere) {
        // 0 = overworld
        // -1 nether
        // 1 = end
        switch (dimension) {
            case 0:
                new OverworldRules(amphi1, amphi2, amphithere);
                break;
            case -1:
                new NetherRules(amphi1, amphi2, amphithere);
                break;
            case 1:
                new EndRules(amphi1, amphi2, amphithere);
                break;
        }
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
        //This should never be called if the weighted roll works correctly
        return getRandomEntryForAmphiType(rand, VARIANT_ENUMS.size() - 1);
    }

    private static int getRandomEntryForAmphiType(Random rand, int type) {
        List<Integer> group = VARIANT_ENUMS.get(type);
        return group.get(rand.nextInt(group.size()));
    }
    static boolean isAffectedByBreedRules(Random rand) {
        return rand.nextBoolean();
    }
}