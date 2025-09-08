package amphitheremod.util.AmphiBreedingRules;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;

public class NetherRules extends AmphiBreedRules {
    static EnumAmphiType[] netherVariants = {EnumAmphiType.RED, EnumAmphiType.RED_GEM, EnumAmphiType.OLIVE, EnumAmphiType.OLIVE_GEM, EnumAmphiType.YELLOW, EnumAmphiType.YELLOW_GEM};

    NetherRules(EnumAmphiType amphi1, EnumAmphiType amphi2, EntityAmphithere amphithere) {
        if (isAffectedByBreedRules(amphithere.getRNG())) {
            amphiVariant = EnumAmphiType.getIntFromEnum(netherVariants[amphithere.getRNG().nextInt(netherVariants.length)]);
        } else {
            amphiVariant = rollVariant(amphithere.getRNG(), true);
        }
    }
}
