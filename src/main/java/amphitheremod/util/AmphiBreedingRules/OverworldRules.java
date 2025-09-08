package amphitheremod.util.AmphiBreedingRules;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;

public class OverworldRules extends AmphiBreedRules {
    OverworldRules(EnumAmphiType amphi1, EnumAmphiType amphi2, EntityAmphithere amphithere) {
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
