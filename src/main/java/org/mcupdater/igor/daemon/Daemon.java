package org.mcupdater.igor.daemon;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.mcupdater.igor.Igor;
import org.mcupdater.igor.Version;

public class Daemon {
	private static class ClockThread extends Thread {
		public static final int	SLEEP	= 15000;	// 15 seconds

		@Override
		public void run() {
			Logger log = Logger.getLogger(Version.MOD_NAME);
			log.entering(ClockThread.class.getCanonicalName(), "run");
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
			log.exiting(ClockThread.class.getCanonicalName(), "run");
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
			// if we're forking an external daemon, do so
			_monitor = null;

			final String separator = System.getProperty("file.separator");
			final String classpath = System.getProperty("java.class.path");
			final String path = System.getProperty("java.home") + separator
					+ "bin" + separator + "java";

			URL jarURL = Daemon.class.getResource("Daemon.class");
			if (jarURL.getProtocol().equals("jar")) {
				String jarPath = jarURL.getPath();
				jarPath = jarPath.substring(1, jarPath.indexOf("!"));
				ProcessBuilder proc = new ProcessBuilder(path, "-cp",
						classpath, "-jar", jarPath);
				try {
					proc.start();
					result = true;
				} catch (IOException e) {
					Igor.log.severe("Unable to fork monitor daemon, reverting to thread");
					// try again, this time as an internal thread
					fork = false;
				}
			} else {
				// we're not running from inside a jar
				fork = false;
			}
		}
		if (!fork) {
			Igor.log.info("Spinning up monitor as an internal thread :(");
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
