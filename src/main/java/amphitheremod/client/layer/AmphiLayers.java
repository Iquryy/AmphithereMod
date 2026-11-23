package amphitheremod.client.layer;

import amphitheremod.config.AmphiConfig;
import amphitheremod.item.AmphithereArmorItem;
import amphitheremod.item.AmphithereBeakItem;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

/** The overlay layers added to Ice and Fire's amphithere renderer, in 1.12.2's order, and the textures they pick. */
public final class AmphiLayers {
    private static final ResourceLocation MALE = tex("textures/entity/amphithere/male.png");
    private static final ResourceLocation FEMALE = tex("textures/entity/amphithere/female.png");
    private static final ResourceLocation HEADLESS = tex("textures/entity/amphithere/headless.png");

    private AmphiLayers() {
    }

    private static ResourceLocation tex(String path) {
        return new ResourceLocation("amphitheremod", path);
    }

    public static void addTo(LivingEntityRenderer<EntityAmphithere, ModelAmphithere> renderer) {
        renderer.addLayer(new AmphiGlowLayer(renderer, AmphiLayers::eyes, false));
        renderer.addLayer(new AmphiGlowLayer(renderer, AmphiLayers::glow, true));
        renderer.addLayer(new AmphiTextureLayer(renderer, AmphiLayers::gender));
        renderer.addLayer(new AmphiTextureLayer(renderer, AmphiLayers::wingPattern));
        renderer.addLayer(new AmphiTextureLayer(renderer, AmphiLayers::headless));
        renderer.addLayer(new AmphiTextureLayer(renderer, AmphiLayers::beak));
        renderer.addLayer(new AmphiTextureLayer(renderer, a -> armor(a, EquipmentSlot.HEAD, "head")));
        renderer.addLayer(new AmphiTextureLayer(renderer, a -> armor(a, EquipmentSlot.FEET, "tail")));
        renderer.addLayer(new AmphiTextureLayer(renderer, a -> armor(a, EquipmentSlot.LEGS, "wing")));
        renderer.addLayer(new AmphiTextureLayer(renderer, a -> armor(a, EquipmentSlot.CHEST, "body")));
    }

    private static String name(EntityAmphithere amphi) {
        return amphi.hasCustomName() ? ChatFormatting.stripFormatting(amphi.getCustomName().getString().toLowerCase()) : "";
    }

    /** The variant to draw: "Risky" is always drawn as rainbow. */
    private static EnumAmphiType type(EntityAmphithere amphi) {
        return "risky".equals(name(amphi)) ? EnumAmphiType.RAINBOW : EnumAmphiType.fromOrdinal(amphi.getVariant());
    }

    @Nullable
    private static ResourceLocation eyes(EntityAmphithere amphi) {
        return amphi.isBlinking() ? null : type(amphi).getEyes().texture;
    }

    @Nullable
    private static ResourceLocation glow(EntityAmphithere amphi) {
        return type(amphi).getGlow().texture;
    }

    /** Male/female markings, not on skeletons or the two special variants. */
    @Nullable
    private static ResourceLocation gender(EntityAmphithere amphi) {
        if (!AmphiConfig.MALE_AND_FEMALE.get()) {
            return null;
        }
        EnumAmphiType type = EnumAmphiType.fromOrdinal(amphi.getVariant());
        if (type == EnumAmphiType.SKELETON || type == EnumAmphiType.WITHER_SKELETON || type == EnumAmphiType.SHIVAXI || type == EnumAmphiType.BLACKEAGLE) {
            return null;
        }
        return ((IAmphithereData) amphi).amphiMod$isFemale() ? FEMALE : MALE;
    }

    @Nullable
    private static ResourceLocation wingPattern(EntityAmphithere amphi) {
        return EnumAmphiType.WingPattern.texture(((IAmphithereData) amphi).amphiMod$getWingPattern());
    }

    /** "H-2" has its head hidden (AmphiModelMixin) and a stump drawn instead. */
    @Nullable
    private static ResourceLocation headless(EntityAmphithere amphi) {
        EnumAmphiType type = EnumAmphiType.fromOrdinal(amphi.getVariant());
        if (type == EnumAmphiType.SKELETON || type == EnumAmphiType.WITHER_SKELETON || !"h-2".equals(name(amphi))) {
            return null;
        }
        return HEADLESS;
    }

    /** Beak attachment; the elemental ones animate through 5 frames, 3 ticks each. */
    @Nullable
    private static ResourceLocation beak(EntityAmphithere amphi) {
        if (!(amphi.getMainHandItem().getItem() instanceof AmphithereBeakItem beak)) {
            return null;
        }
        String base = "textures/amphithere_beak/" + beak.material.name + "_beak_attachment";
        if (beak.material.isElemental()) {
            return tex(base + "_" + (amphi.tickCount / 3 % 5) + ".png");
        }
        return tex(base + ".png");
    }

    @Nullable
    private static ResourceLocation armor(EntityAmphithere amphi, EquipmentSlot slot, String part) {
        Item item = amphi.getItemBySlot(slot).getItem();
        if (!(item instanceof AmphithereArmorItem armor)) {
            return null;
        }
        return tex("textures/amphithere_armor/" + armor.material.name + "/" + part + "_armor.png");
    }
}
