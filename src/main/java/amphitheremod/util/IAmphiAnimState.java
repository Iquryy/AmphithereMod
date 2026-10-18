package amphitheremod.util;

/**
 * Per-amphithere animation state, ticked on the client in AmphiAnimState and read by the model.
 * Every getter blends between the last two ticks with partialTicks, so the animation stays smooth at any frame rate.
 */
public interface IAmphiAnimState {
    /** 0..1, how far the wings are blended into the dive pose by pitch. */
    float amphiAnim$diveBlend(float partialTicks);

    /** 0..1, how much of the flapping is kept (fades out past the "flap stops" pitch). */
    float amphiAnim$flapKeep(float partialTicks);

    /** Bank angle into the turn, in degrees. */
    float amphiAnim$bankRoll(float partialTicks);

    /** How far the neck should turn toward where the amphithere (or its rider) is facing, in degrees. */
    float amphiAnim$neckLead(float partialTicks);

    /** Sideways swing of tail segment i (0..4: Tail1..Tail4, Club) from turning, in degrees. */
    float amphiAnim$turnTail(int i, float partialTicks);

    /** Up/down swing of tail segment i from the body bob, in degrees. */
    float amphiAnim$bobTail(int i, float partialTicks);

    /*
     * Ice and Fire changes flapProgress, diveProgress and groundProgress once per tick and the model reads them
     * raw, so flap strength and the dive / landing poses moved in 20 steps per second. These blend between ticks.
     */
    float amphiAnim$flapProgress(float partialTicks);

    float amphiAnim$diveProgress(float partialTicks);

    float amphiAnim$groundProgress(float partialTicks);

    /** This tick's body bob in blocks (+ = lower), so the rider can bob with the body. */
    float amphiAnim$bobBlocks();
}
