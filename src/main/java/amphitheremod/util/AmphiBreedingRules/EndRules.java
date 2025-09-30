package amphitheremod.util.AmphiBreedingRules;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;

public class EndRules extends AmphiBreedRules {
    static EnumAmphiType[] endVariants = {EnumAmphiType.BLACK, EnumAmphiType.BLACK_GEM, EnumAmphiType.PURPLE, EnumAmphiType.PURPLE_GEM, EnumAmphiType.PINK_GEM, EnumAmphiType.WHITE, EnumAmphiType.WHITE_GEM, EnumAmphiType.IQURY};

    EndRules(EnumAmphiType amphi1, EnumAmphiType amphi2, EntityAmphithere amphithere) {
        if (isAffectedByBreedRules(amphithere.getRNG()))
            amphiVariant = EnumAmphiType.getIntFromEnum(endVariants[amphithere.getRNG().nextInt(endVariants.length)]);
        else
            amphiVariant = rollVariant(amphithere.getRNG(), true);
    }
}

