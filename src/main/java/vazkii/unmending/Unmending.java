package vazkii.unmending;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Unmending.MOD_ID, name = Unmending.MOD_NAME, version = Unmending.VERSION)
public class Unmending {

	// Mod Constants
	public static final String MOD_ID = "unmending";
	public static final String MOD_NAME = "Unmending";
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String PREFIX = MOD_ID + ":";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(KillingMendingAndOtherTales.class);
	}
	
}
