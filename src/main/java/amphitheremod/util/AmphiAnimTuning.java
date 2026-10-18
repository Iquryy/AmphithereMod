package amphitheremod.util;

/**
 * Tuning for the amphithere's flight animations. These are the final defaults from the rig sandbox.
 * Units: ticks, degrees, model pixels (1/16 block) unless noted.
 */
public final class AmphiAnimTuning {
    private AmphiAnimTuning() {}

    // ---- Flap cycle ----
    /** Length of one wingbeat in ticks. */
    public static final float BEAT_TICKS = 30F;
    /** Share of the beat spent on the downstroke (the rest is the quick, folded upstroke). */
    public static final float DOWNSTROKE_SHARE = 0.64F;
    /** Overall size of the up/down stroke. */
    public static final float STROKE_SIZE = 1.0F;
    /** How much the wing folds in on the upstroke. */
    public static final float UPSTROKE_FOLD = 1.0F;
    /** How far the forearm and hand trail the arm (the wingtip curl at the turnarounds). */
    public static final float TIP_LAG = 1.0F;
    /** How far the body rises and sinks with each beat, in model pixels. */
    public static final float BODY_BOB_PX = 2.2F;
    /** The fold eases in this share of a beat before the bottom of the stroke and out this much after the top. */
    public static final float FOLD_LEAD = 0.06F, FOLD_TRAIL = 0.06F;

    // ---- Your existing wing pieces, now timed to the beat ----
    public static final float WING_TWIST_STRENGTH = 2.3F;
    public static final float FINGER_CURL_STRENGTH = 2.2F;

    // ---- Tail reacting to the body bob ----
    public static final float TAIL_FOLLOWS_BOB = 0.8F;
    /** Ticks each tail segment waits after the one in front of it. */
    public static final float TAIL_BOB_DELAY = 3F;
    public static final boolean TAIL_BOB_REVERSED = false;
    public static final float BOB_TAIL_STIFF = 0.18F, BOB_TAIL_DAMP = 0.30F, BOB_TAIL_SPEED_MAX = 3F, BOB_TAIL_SEG_MAX = 20F;
    public static final int BOB_HIST = 16;

    // ---- Dive by pitch (positive pitch = nose down; a rider's look pitch is halved by Ice and Fire) ----
    public static final float DIVE_STARTS_DEG = 17F;
    public static final float FULL_DIVE_DEG = 50F;
    public static final float FLAP_STOPS_DEG = 29F;
    /** Flapping fades out over plus/minus this many degrees around FLAP_STOPS_DEG. */
    public static final float FLAP_FADE_DEG = 6F;
    /** Share of the remaining pitch gap closed per tick. */
    public static final float PITCH_EASE = 0.18F;
    /** Most the dive blend / flap fade may change per tick (a full change takes 14 / 10 ticks). */
    public static final float DIVE_RATE = 1F / 14F, FLAP_RATE = 1F / 10F;

    // ---- Turning ----
    /** While a player rides, the body turns with them instantly; the easing below only applies without a rider. */
    public static final boolean RIDER_TURNS_INSTANTLY = true;
    /** Most the body may turn per tick, in degrees. Steering itself still follows the rider instantly. */
    public static final float TURN_MAX_DEG_PER_TICK = 30F;
    public static final float TURN_EASE = 0.35F;
    /** Bank and tail follow a smoothed turn rate: share of the change taken per tick (lower = calmer). */
    public static final float TURN_RATE_SMOOTHING = 0.3F;
    /** Turn rates below about this (degrees per tick) are ignored for bank and tail, so mouse jitter does nothing. */
    public static final float TURN_DEADZONE_DEG = 0.6F;
    public static final float BANK = 4.0F, BANK_MAX_DEG = 60F, BANK_EASE = 0.25F;
    /** How much of the gap between where the rider looks and where the body points the neck turns. */
    public static final float NECK_LEADS = 1.0F;
    public static final float NECK_LEAD_MAX_DEG = 90F;
    public static final float TAIL_FOLLOWS_TURN = 1.35F;
    public static final float TAIL_TURN_DELAY = 0F;
    public static final float TURN_TAIL_STIFF = 0.2F, TURN_TAIL_DAMP = 0.36F, TURN_TAIL_SPEED_MAX = 4F, TURN_TAIL_SEG_MAX = 35F;
    public static final int YAW_HIST = 24;

    // ---- Body tilt from pitch (ChainBuffer) ----
    public static final float BODY_TILT_MAX_DEG_PER_TICK = 5F;

    /** Ice and Fire renders the amphithere at 2x, so one model pixel is 2/16 of a block. */
    public static final float MODEL_PX_TO_BLOCKS = 2F / 16F;

    /** Tail1..Tail4 and the club. */
    public static final int TAIL_SEGMENTS = 5;
}