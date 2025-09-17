package amphitheremod.util;

import net.minecraft.client.resources.I18n;

public class StatCollector {
    public static String translateToLocal(String s) {
        return I18n.format(s, new Object[0]);
    }
}