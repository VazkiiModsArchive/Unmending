package vazkii.unmending;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Unmending.MOD_ID)
public class Unmending {

	// Mod Constants
	public static final String MOD_ID = "unmending";
	//public static final String MOD_NAME = "Unmending";
	//public static final String BUILD = "GRADLE:BUILD";
	//public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	//public static final String PREFIX = MOD_ID + ":";

	public Unmending() {
		// Register the setup method for modloading (Prev preInit)
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void setup(final FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(KillingMendingAndOtherTales.class);
	}
	
}
