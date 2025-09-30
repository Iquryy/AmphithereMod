package amphitheremod;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IceAndFireUtil;
import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class AmphithereModPlugin implements IFMLLoadingPlugin {

    public AmphithereModPlugin() {
        MixinBootstrap.init();
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.mixins.json");
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.crystalfeather.json", () -> Loader.isModLoaded("iceandfire") && IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT);
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.dynbranchphasechange.json", () -> {
            if (Loader.isModLoaded("dynamictrees")) return ConfigHandler.mixins.amphiTamingDmg;
            else return false;
        });
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.dynleavesphasechange.json", () -> {
            if (Loader.isModLoaded("dynamictrees")) return ConfigHandler.mixins.amphiTamingDmg;
            else return false;
        });
    }

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}