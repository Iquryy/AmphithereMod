package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public final class AmphiSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AmphithereMod.MODID);

    /** amphithere_hurt_0 .. 9: the original authors' easter egg hurt sounds (mixin.AmphiSoundsMixin). */
    public static final List<RegistryObject<SoundEvent>> HURT = new ArrayList<>();

    static {
        for (int i = 0; i < 10; i++) {
            String name = "amphithere_hurt_" + i;
            HURT.add(SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AmphithereMod.MODID, name))));
        }
    }

    private AmphiSounds() {
    }
}
