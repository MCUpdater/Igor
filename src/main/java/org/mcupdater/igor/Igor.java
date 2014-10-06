package org.mcupdater.igor;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mcupdater.igor.daemon.Daemon;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Version.MOD_ID, name = Version.MOD_NAME, version = Version.VERSION, acceptedMinecraftVersions = "[1.7.10]", dependencies = "")
public class Igor {
	public static Configuration config;
	public static Logger		log	= LogManager.getLogger(Version.MOD_NAME);

	public static int			httpPort = 8080;
	
	public Daemon				_daemon;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		log.info("Yeth, Marthter?");

		config = new Configuration(evt.getSuggestedConfigurationFile());
		config.load();

		httpPort = config.get(Configuration.CATEGORY_GENERAL, "http.port", httpPort).getInt();
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// actually spin up the butler
		boolean forkDaemon = config.get(Configuration.CATEGORY_GENERAL, "daemon.fork", true).getBoolean(true);
		_daemon = new Daemon(forkDaemon);

		log.info("Marthter, the creathathure awaketh!");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
