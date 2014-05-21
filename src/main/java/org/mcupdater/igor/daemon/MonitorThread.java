package org.mcupdater.igor.daemon;

import java.util.logging.Logger;

import org.mcupdater.igor.Version;

public class MonitorThread extends Thread {
	private static Logger	log	= Logger.getLogger(Version.MOD_NAME+" Monitor");

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
