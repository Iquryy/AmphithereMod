package amphitheremod.compat;

import amphitheremod.AmphithereMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Ice and Fire features that only some 1.20.1 builds have, found by reflection so the mod runs on any of them.
 * Chain lightning (com.github.alexthe666.iceandfire.api.ChainLightningUtils) is in the 1.12.2 feature ports of
 * Ice and Fire; without it the lightning beak still does its bonus damage and knockback, just no chain.
 */
public final class IafCompat {
    private static boolean looked;
    private static MethodHandle chainLightning;

    private IafCompat() {
    }

    /** Chain lightning from the target, with Ice and Fire's chain lightning config (damage per hop, range). */
    public static void chainLightning(Level level, LivingEntity target, Entity attacker) {
        if (!looked) {
            looked = true;
            try {
                Class<?> utils = Class.forName("com.github.alexthe666.iceandfire.api.ChainLightningUtils");
                chainLightning = MethodHandles.publicLookup().findStatic(utils, "createChainLightning",
                        MethodType.methodType(void.class, Level.class, LivingEntity.class, Entity.class, float.class, boolean.class));
            } catch (ReflectiveOperationException e) {
                AmphithereMod.LOGGER.info("This Ice and Fire build has no chain lightning; lightning beaks won't chain");
            }
        }
        if (chainLightning == null) {
            return;
        }
        try {
            chainLightning.invokeExact(level, target, attacker, 5.0F, false);
        } catch (Throwable t) {
            AmphithereMod.LOGGER.warn("Chain lightning from a lightning beak failed", t);
        }
    }
}
