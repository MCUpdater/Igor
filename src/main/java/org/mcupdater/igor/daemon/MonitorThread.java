package org.mcupdater.igor.daemon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mcupdater.igor.Version;

public class MonitorThread extends Thread {
	private static Logger	log	= LogManager.getLogger(Version.MOD_NAME+"_Monitor");

	private final Daemon	_daemon;

	public MonitorThread(Daemon daemon) {
		_daemon = daemon;
		log.info("Monitor "+(_daemon.forked?"process":"thread")+" created...");
	}

	@Override
	public void run() {
		while (true) {
		}
	}
}
