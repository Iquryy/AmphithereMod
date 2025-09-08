package amphitheremod.config;

import amphitheremod.AmphithereMod;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = AmphithereMod.MODID)
@SuppressWarnings("unused")
public class MixinToggleConfig {
    @Config.Comment("With this mixin, feeding coco beans to amphithere will heal them 10% of their max hp instead 5 fixed amount")
    @Config.Name("Enable Cocoa Bean Heal Change")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.dynamicfeeding.json", defaultValue = true)
    public boolean changeCocoaBeanHeal = true;

    @Config.Comment("With this mixin, the dragon 3rd person view in F5 can also be used with amphis")
    @Config.Name("Enable Dragon View")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.amphiview.json", defaultValue = true)
    public boolean amphiView = true;

    @Config.Comment("Can only Male with Female Amphithere breed with each other")
    @Config.Name("Male + Female Breeding")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.male_female.json", defaultValue = true)
    public boolean maleAndFemale = true;

    @Config.Comment("Can Amphithere pass trough leaves? (If false then its bad with dynamic trees leaves physics)")
    @Config.Name("Amphithere can't pass trough leaves")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.leaffix.json", defaultValue = true)
    public boolean canPassTroughLeaves = true;

    @Config.Comment("With this mixin amphithere will have taming damage based from other damage increasing sources")
    @Config.Name("Advanced Amphithere Taming Damage")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.tamingdmg.json", defaultValue = true)
    public boolean amphiTamingDmg = true;
}