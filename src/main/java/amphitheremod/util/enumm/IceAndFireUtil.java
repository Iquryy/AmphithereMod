package amphitheremod.util.enumm;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class IceAndFireUtil {
    public enum IceAndFireVersion {
        BASE_OLD,
        BASE_1_9_1,
        RLCRAFT,
        ROTN,
        OTHER,
        NONE
    }

    public static IceAndFireVersion getIceAndFireVersion(){
        ModContainer infMod = Loader.instance().getIndexedModList().get("iceandfire");
        if(infMod == null) return IceAndFireVersion.NONE;
        String infVersion = infMod.getVersion();
        String infName = infMod.getName();
        if(infName.equals("Ice and Fire: RotN Edition")) return IceAndFireVersion.ROTN;  //rotn inf: versioning 1.9.1-x.x.x, but don't need to use it bc RotN renamed it
        if(infVersion.matches("^1\\.\\d+\\.\\d+$"))  if(infVersion.equals("1.9.1")) return IceAndFireVersion.BASE_1_9_1; else return IceAndFireVersion.BASE_OLD; //base inf: 1.x.x
        if(infVersion.matches("^2\\.\\d+\\.\\d+$")) return IceAndFireVersion.RLCRAFT;  //rlcraft inf: 2.x.x
        return IceAndFireVersion.OTHER;
    }
}