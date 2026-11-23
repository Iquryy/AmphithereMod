package amphitheremod.util;

/**
 * Client-side flight animation state kept on each amphithere (mixin.AmphiAnimStateMixin) and read by the model mixin.
 * Every getter takes the partial tick and interpolates between the last two ticks so the animation is smooth.
 */
public interface IAmphiAnimState {
    /** 0..1, how far into the dive pose the pitch has pushed it. */
    float amphiAnim$diveBlend(float partialTick);

    /** 0..1, how much of the wing flap to keep (flapping fades out in a steep dive). */
    float amphiAnim$flapKeep(float partialTick);

    /** Degrees of bank roll while turning. */
    float amphiAnim$bankRoll(float partialTick);

    /** Degrees the neck leads the body into a turn. */
    float amphiAnim$neckLead(float partialTick);

    /** Degrees of sideways tail swing for tail segment i (0..4) while turning. */
    float amphiAnim$turnTail(int i, float partialTick);

    /** Degrees of up/down tail swing for tail segment i (0..4) following the body bob. */
    float amphiAnim$bobTail(int i, float partialTick);

    float amphiAnim$flapProgress(float partialTick);

    float amphiAnim$diveProgress(float partialTick);

    float amphiAnim$groundProgress(float partialTick);

    /** Smoothed body pitch in degrees (replaces Ice and Fire's pitch buffer). */
    float amphiAnim$bodyPitch(float partialTick);

    /** How far the body bobs this tick, in blocks (the rider is moved by the same amount). */
    float amphiAnim$bobBlocks();
}
