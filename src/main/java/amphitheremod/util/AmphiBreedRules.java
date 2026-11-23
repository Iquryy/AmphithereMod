package amphitheremod.util;

import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static amphitheremod.util.EnumAmphiType.*;

/**
 * Which variant a wild spawn or a bred baby gets. Same weights and colour mixing rules as 1.12.2.
 */
public final class AmphiBreedRules {
    // Chance per group (normal, rare, gem) for wild spawns and for babies
    private static final int[] WEIGHTS_SPAWN = {60, 20, 20};
    private static final int[] WEIGHTS_CHILD = {40, 30, 30};
    private static final int WEIGHTS_TOTAL_SPAWN = Arrays.stream(WEIGHTS_SPAWN).sum();
    private static final int WEIGHTS_TOTAL_CHILD = Arrays.stream(WEIGHTS_CHILD).sum();
    private static final List<List<Integer>> VARIANT_GROUPS = Arrays.asList(
            getIntsByGroup(Group.NORMAL), getIntsByGroup(Group.RARE), getIntsByGroup(Group.GEM));
    // Colour mixing: (lower ordinal parent, higher ordinal parent) -> baby
    private static final Map<EnumAmphiType, Map<EnumAmphiType, EnumAmphiType>> MIX = new EnumMap<>(EnumAmphiType.class);

    static {
        rule(BLUE, YELLOW, GREEN);
        rule(BLUE, RED, PURPLE);
        rule(RED, YELLOW, OLIVE);
        rule(GREEN, RED, RADISH);
        rule(BLUE, RED_GEM, PURPLE_GEM);
        rule(RED, BLUE_GEM, PURPLE_GEM);
        rule(BLUE, YELLOW_GEM, GREEN);
        rule(RED, YELLOW_GEM, OLIVE);
        rule(YELLOW, BLUE_GEM, GREEN_GEM);
        rule(BLUE_GEM, YELLOW_GEM, GREEN_GEM);
        rule(BLUE_GEM, RED_GEM, PURPLE_GEM);
        rule(RED_GEM, YELLOW_GEM, OLIVE_GEM);
        rule(RED, WHITE, PINK_GEM);
        rule(PURPLE, WHITE, PINK_GEM);
        rule(WHITE, BLUE_GEM, CYAN_GEM);
        rule(WHITE, RED_GEM, PINK_GEM);
        rule(WHITE, GREEN_GEM, LIME_GEM);
        rule(WHITE, PURPLE_GEM, PINK_GEM);
        rule(WHITE_GEM, RED_GEM, PINK_GEM);
        rule(WHITE_GEM, GREEN_GEM, LIME_GEM);
        rule(BLACK, LIME_GEM, GREEN_GEM);
        rule(BLACK, YELLOW_GEM, OLIVE_GEM);
        rule(BLACK_GEM, LIME_GEM, GREEN_GEM);
        rule(BLUE, GREEN, CYAN_GEM);
        rule(BLUE, GREEN_GEM, CYAN_GEM);
        rule(YELLOW, GREEN, LIME_GEM);
        rule(YELLOW, GREEN_GEM, LIME_GEM);
    }

    private AmphiBreedRules() {
    }

    private static void rule(EnumAmphiType a, EnumAmphiType b, EnumAmphiType child) {
        MIX.computeIfAbsent(a, k -> new EnumMap<>(EnumAmphiType.class)).put(b, child);
    }

    /**
     * Baby variant for two parents (given sorted by ordinal). 80%: half the time a colour mixing rule, otherwise a
     * random variant from the baby weights. 20%: one of the parents' variants.
     */
    public static int childVariant(EnumAmphiType parent1, EnumAmphiType parent2, RandomSource rand) {
        if (rand.nextInt(100) + 1 < 80) {
            if (rand.nextBoolean()) {
                EnumAmphiType mixed = MIX.getOrDefault(parent1, Map.of()).get(parent2);
                if (mixed != null) {
                    return mixed.ordinal();
                }
                // 1.12.2 returned whatever the last baby got here (a leftover static field); a fresh roll is what was meant
            }
            return rollVariant(rand, true);
        }
        return rand.nextBoolean() ? parent1.ordinal() : parent2.ordinal();
    }

    /** Random variant: 1% rainbow, otherwise a group by weight and a random variant in it. */
    public static int rollVariant(RandomSource rand, boolean isFromBreed) {
        if (rand.nextInt(100) == 0) {
            return RAINBOW.ordinal();
        }
        int[] weights = isFromBreed ? WEIGHTS_CHILD : WEIGHTS_SPAWN;
        int roll = rand.nextInt(isFromBreed ? WEIGHTS_TOTAL_CHILD : WEIGHTS_TOTAL_SPAWN);
        for (int i = 0; i < weights.length; i++) {
            roll -= weights[i];
            if (roll < 0) {
                List<Integer> group = VARIANT_GROUPS.get(i);
                return group.get(rand.nextInt(group.size()));
            }
        }
        return BLUE.ordinal();
    }

    /** Variants that can't breed: skeletons and the special named ones. */
    public static boolean cannotBreed(EnumAmphiType type) {
        return type == SHIVAXI || type == WITHER_SKELETON || type == SKELETON || type == IQURY || type == CRAFTY || type == BLACKEAGLE;
    }
}
