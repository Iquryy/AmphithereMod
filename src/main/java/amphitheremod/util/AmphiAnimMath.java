package amphitheremod.util;

import net.minecraft.util.math.MathHelper;

import static amphitheremod.util.AmphiAnimTuning.*;

/** Curves shared by the animation state (ticked on the entity) and the model. */
public final class AmphiAnimMath {
    private AmphiAnimMath() {}

    public static final float PI = (float) Math.PI;

    public static float wrap01(float p) {
        return p - MathHelper.floor(p);
    }

    /**
     * Wing stroke for a beat phase: +1 = wings at the top, -1 = at the bottom.
     * Eased both ways, slow downstroke then quick upstroke, so the wing lingers at each turnaround.
     */
    public static float stroke(float phase) {
        float p = wrap01(phase), d = DOWNSTROKE_SHARE;
        return p < d ? MathHelper.cos(PI * p / d) : -MathHelper.cos(PI * (p - d) / (1F - d));
    }

    /** Upstroke fold, 0..1: a sin² bump that eases in just before the bottom and out just after the top. */
    public static float fold(float phase) {
        float start = DOWNSTROKE_SHARE - FOLD_LEAD, len = (1F - DOWNSTROKE_SHARE) + FOLD_LEAD + FOLD_TRAIL;
        float u = wrap01(phase - start) / len;
        if (u >= 1F) return 0F;
        float s = MathHelper.sin(PI * u);
        return s * s;
    }

    public static float smooth01(float a, float b, float x) {
        if (b <= a) return x >= b ? 1F : 0F;
        float t = MathHelper.clamp((x - a) / (b - a), 0F, 1F);
        return t * t * (3F - 2F * t);
    }

    public static float stepToward(float v, float target, float maxStep) {
        return v + MathHelper.clamp(target - v, -maxStep, maxStep);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
