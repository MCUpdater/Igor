package org.mcupdater.igor;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Version.MOD_ID, name = Version.MOD_NAME, version = Version.VERSION, acceptedMinecraftVersions = "[1.6,1.7],", dependencies = "")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class Igor {
	public static Configuration config;
	public static Logger		log	= Logger.getLogger(Version.MOD_NAME);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		log.info("Yeth, Marthter?");

		config = new Configuration(evt.getSuggestedConfigurationFile());
		config.load();

		@SuppressWarnings("unused")
		int httpPort = config.get(Configuration.CATEGORY_GENERAL, "http.port", 8080).getInt();
		
		@SuppressWarnings("unused")
		boolean useDaemon = config.get(Configuration.CATEGORY_GENERAL, "daemon.fork", true).getBoolean(true);

		if (config.hasChanged()) {
			config.save();
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// actually spin up the butler
		log.info("Marthter, the creathathure awaketh!");
	}
}
