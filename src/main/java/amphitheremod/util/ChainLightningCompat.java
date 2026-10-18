package amphitheremod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Calls Ice and Fire's ChainLightningUtils.createChainLightningFromTarget without
 * binding to one exact signature at compile time. Different RLCraft Ice and Fire
 * builds ship different overloads (e.g. the last parameter is int[] in some, int
 * or missing in others), which caused a NoSuchMethodError crash.
 */
public final class ChainLightningCompat {
    private static final Logger LOGGER = LogManager.getLogger("AmphithereMod");

    private static boolean initialized = false;
    private static boolean failedLogged = false;
    private static final List<Method> CANDIDATES = new ArrayList<>();
    private static Method chosen = null;

    private static boolean paralysis = false;
    private static Object ticksPerHopConfig = null;

    private ChainLightningCompat() {}

    private static void init() {
        if (initialized) return;
        initialized = true;
        try {
            Class<?> utils = Class.forName("com.github.alexthe666.iceandfire.api.ChainLightningUtils");
            for (Method m : utils.getMethods()) {
                if (m.getName().equals("createChainLightningFromTarget")) CANDIDATES.add(m);
            }
            // Prefer the overload with the most parameters we know how to fill.
            CANDIDATES.sort((a, b) -> b.getParameterCount() - a.getParameterCount());
        } catch (Throwable t) {
            LOGGER.warn("Ice and Fire ChainLightningUtils not found; lightning beak chain effect disabled.");
        }

        Object p = readMiscSetting("chainLightningParalysis");
        if (p instanceof Boolean) paralysis = (Boolean) p;
        ticksPerHopConfig = readMiscSetting("chainLightningParalysisTicksPerHop");

        if (CANDIDATES.isEmpty()) {
            LOGGER.warn("No createChainLightningFromTarget overload found; lightning beak chain effect disabled.");
        } else {
            LOGGER.info("Chain lightning overloads found: {}", CANDIDATES);
        }
    }

    private static Object readMiscSetting(String name) {
        try {
            Class<?> cfg = Class.forName("com.github.alexthe666.iceandfire.IceAndFireConfig");
            Object misc = cfg.getField("MISC_SETTINGS").get(null);
            return misc.getClass().getField(name).get(misc);
        } catch (Throwable t) {
            return null;
        }
    }

    public static void createChainLightningFromTarget(World world, EntityLivingBase target, Entity attacker,
                                                      float[] damages, int range) {
        init();
        if (CANDIDATES.isEmpty()) return;

        try {
            if (chosen != null) {
                Object[] args = buildArgs(chosen.getParameterTypes(), world, target, attacker, damages, range);
                if (args != null) {
                    chosen.invoke(null, args);
                    return;
                }
            }
            for (Method m : CANDIDATES) {
                Object[] args = buildArgs(m.getParameterTypes(), world, target, attacker, damages, range);
                if (args == null) continue;
                chosen = m;
                m.invoke(null, args);
                return;
            }
            logFailureOnce("no compatible overload among " + CANDIDATES, null);
        } catch (Throwable t) {
            logFailureOnce("call failed", t);
        }
    }

    private static Object[] buildArgs(Class<?>[] types, World world, EntityLivingBase target, Entity attacker,
                                      float[] damages, int range) {
        Object[] args = new Object[types.length];
        int entityIdx = 0;
        int intIdx = 0;
        for (int i = 0; i < types.length; i++) {
            Class<?> t = types[i];
            if (t.isInstance(world)) {
                args[i] = world;
            } else if (Entity.class.isAssignableFrom(t)) {
                Entity e = entityIdx++ == 0 ? target : attacker;
                if (e != null && !t.isInstance(e)) return null;
                args[i] = e;
            } else if (t == float[].class) {
                args[i] = damages;
            } else if (t == int.class) {
                args[i] = intIdx++ == 0 ? range : ticksScalar();
            } else if (t == boolean.class) {
                args[i] = paralysis;
            } else if (t == int[].class) {
                args[i] = ticksArray(damages.length);
            } else {
                return null; // unknown parameter type, can't fill safely
            }
        }
        return args;
    }

    private static int ticksScalar() {
        if (ticksPerHopConfig instanceof int[]) {
            int[] arr = (int[]) ticksPerHopConfig;
            return arr.length > 0 ? arr[0] : 0;
        }
        if (ticksPerHopConfig instanceof Number) return ((Number) ticksPerHopConfig).intValue();
        return 0;
    }

    private static int[] ticksArray(int hops) {
        if (ticksPerHopConfig instanceof int[]) return (int[]) ticksPerHopConfig;
        int[] arr = new int[hops];
        if (ticksPerHopConfig instanceof Number) Arrays.fill(arr, ((Number) ticksPerHopConfig).intValue());
        return arr;
    }

    private static void logFailureOnce(String msg, Throwable t) {
        if (failedLogged) return;
        failedLogged = true;
        if (t != null) LOGGER.error("Lightning beak chain lightning " + msg + " (further errors suppressed)", t);
        else LOGGER.error("Lightning beak chain lightning " + msg + " (further errors suppressed)");
    }
}