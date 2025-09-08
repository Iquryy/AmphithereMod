package amphitheremod;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class AmphithereModPlugin implements IFMLLoadingPlugin {

	public AmphithereModPlugin() {
		MixinBootstrap.init();
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.inventory.json");
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.mixins.json");
        FermiumRegistryAPI.enqueueMixin(true, "mixins.amphitheremod.dynamicfeeding.json"); // this no workie now too and idk why it also might related to the other issue. I never jhad remap in there before and it worked
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