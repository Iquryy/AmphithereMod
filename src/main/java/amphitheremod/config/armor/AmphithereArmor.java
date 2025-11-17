package amphitheremod.config.armor;

import net.minecraftforge.common.config.Config;

public class AmphithereArmor {
    @Config.Comment("Enable or disable the crafting and use of Amphithere Armor.")
    @Config.Name("Enable Amphithere Armor")
    @Config.RequiresMcRestart
    public boolean enableAmphithereArmor = true;

    @Config.Comment("If this is set to true then all armor and beaks are cosmetic items that give no extra armor or damage to Amphitheres")
    @Config.Name("Make Armor/Beak a Cosmetic")
    @Config.RequiresMcRestart
    public boolean cosmeticArmorBeak = false;

    @Config.Comment("Enables a set bonus for full silver armor on Amphitheres, granting the 'Cure' effect if PotionCore is installed. You need in total 67 silver ingot to make a full set.")
    @Config.Name("Enable Silver Armor Set Bonus")
    @Config.RequiresMcRestart
    public boolean enableSilverSetBonus = true;

    @Config.Comment("Divides Amphithere Armor point by given value. 0.5 being 50% decrease in armor.")
    @Config.Name("Amphithere Armor Divider")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.1f, max = 1)
    public double armorPointDivider = 0.5f;

    @Config.Comment("Damage values for Amphitheres Beak Attachments.")
    @Config.Name("Beak Attachment Damages")
    public final BeakDamage beakDamage = new BeakDamage();
}