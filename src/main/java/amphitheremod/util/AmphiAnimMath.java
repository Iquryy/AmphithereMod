package amphitheremod.util;

import net.minecraft.util.Mth;

/** Small easing helpers for the flight animation. Values match 1.12.2's AmphiAnimMath/AmphiAnimTuning. */
public final class AmphiAnimMath {
    /** Wing beat length in ticks. */
    public static final float BEAT_TICKS = 30.0F;
    /** Share of the beat spent on the downstroke. */
    private static final float DOWNSTROKE_SHARE = 0.64F;

    private AmphiAnimMath() {
    }

    /** Fractional part, 0..1. */
    public static float wrap01(float p) {
        return p - Mth.floor(p);
    }

    /** Wing stroke curve: a slow cosine downstroke then a quicker upstroke. */
    public static float stroke(float phase) {
        float p = wrap01(phase);
        float d = DOWNSTROKE_SHARE;
        return p < d ? Mth.cos(Mth.PI * p / d) : -Mth.cos(Mth.PI * (p - d) / (1.0F - d));
    }

    /** How folded the wing is during the upstroke (0 open, 1 folded). */
    public static float fold(float phase) {
        float start = 0.58F;
        float len = 0.48F;
        float u = wrap01(phase - start) / len;
        if (u >= 1.0F) {
            return 0.0F;
        }
        float s = Mth.sin(Mth.PI * u);
        return s * s;
    }

    /** Smoothstep from a to b. */
    public static float smooth01(float a, float b, float x) {
        if (b <= a) {
            return x >= b ? 1.0F : 0.0F;
        }
        float t = Mth.clamp((x - a) / (b - a), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    /** Moves v toward target by at most maxStep. */
    public static float stepToward(float v, float target, float maxStep) {
        return v + Mth.clamp(target - v, -maxStep, maxStep);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
