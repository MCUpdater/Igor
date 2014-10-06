package org.mcupdater.igor.daemon;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mcupdater.igor.Igor;
import org.mcupdater.igor.Version;

public class Daemon {
	private static class ClockThread extends Thread {
		public static final int	SLEEP	= 15000;	// 15 seconds

		@Override
		public void run() {
			Logger log = LogManager.getLogger(Version.MOD_NAME+"_Clock");
			log.entry();
			// int c = 0;
			while (true) {
				try {
					// log.info(( ++c % 2 == 0 ) ? "tick" : "tock");
					Thread.sleep(SLEEP);
					// TODO: poll the system
					// TODO: check for cron tasks
				} catch (InterruptedException e) {
					log.info("Jobth done.");
					break;
				}
			}
			log.exit();
		}
	}

	private final ClockThread	_clock;
	private MonitorThread		_monitor;
	public final boolean		forked;

	public Daemon(boolean fork) {
		// spin up our internal clock thread
		_clock = new ClockThread();
		_clock.start();

		// spin up the monitor thread/process
		forked = startMonitor(fork);
	}

	private boolean startMonitor(boolean fork) {
		boolean result = false;
		if (fork) {
			fork = false;
			// if we're forking an external daemon, do so
			_monitor = null;

			final String separator = System.getProperty("file.separator");
			final String classpath = System.getProperty("java.class.path");
			final String path = System.getProperty("java.home") + separator
					+ "bin" + separator + "java";

			final URL jarURL = Daemon.class.getResource("Daemon.class");
			final String jarProtocol = jarURL.getProtocol();
			String jarPath = jarURL.getPath();
			final ProcessBuilder proc;
			if (jarProtocol.equals("jar")) {
				jarPath = jarPath.substring(1, jarPath.indexOf("!"));
				proc = new ProcessBuilder(path, "-cp",
						classpath, "-jar", jarPath);
			} else if( jarProtocol.equals("file")){
				proc = new ProcessBuilder(path, "-cp",
						classpath, jarPath);
			} else {
				proc = null;
			}
			
			if( proc != null ) {
				try {
					proc.start();
					fork = result = true;
				} catch (IOException e) {
					Igor.log.error("Unable to fork monitor daemon, reverting to thread");
					// try again, this time as an internal thread
				}
			} else {
				// we don't know what's going on, abort abort
				Igor.log.info("Thorry, but I am unable to thpawn the daemon...");
			}
		}
		if (!fork) {
			Igor.log.info("Thpinning up monitor ath an internal thread.");
			// else, we need to spin up the less robust daemon thread
			_monitor = new MonitorThread(this);
			_monitor.start();
		}
		return result;
	}

	public static void main(String[] args) {
		// spin up a daemon as requested
		new Daemon(true);
	}
}
