/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Hashtable;
import java.util.TimerTask;

import org.springframework.util.ReflectionUtils;

import sun.net.www.protocol.https.Handler;
import edu.wustl.common.util.logger.Logger;

/**
 * Miscellaneous utilities.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class Utils extends TimerTask {

	private static final Logger logger = Logger
			.getCommonLogger(NotificationQueueProcessor.class);

	/**
	 * Globus registers itselfs as a handler for HTTPS protocol on per-VM
	 * (per-webapp?) basis. Not sure why and where that happens, but it breaks a
	 * lot of stuff. Globus exceptions are thrown when simple "https" links are
	 * accessed. This method restores the mapping back to the default
	 * {@link Handler} if Globus is detected.
	 */
	public static void restoreDefaultHttpsURLHandler() {
		try {
			final Field handlersField = ReflectionUtils.findField(URL.class,
					"handlers");
			ReflectionUtils.makeAccessible(handlersField);
			Hashtable handlers = (Hashtable) handlersField.get(null);
			URLStreamHandler handler = (URLStreamHandler) handlers.get("https");
			if (handler instanceof org.globus.net.protocol.httpg.Handler
					|| (handler != null && handler.getClass().getName()
							.contains("org.globus.net.protocol."))) {
				handlers.put("https", new sun.net.www.protocol.https.Handler());
				System.out
						.println("We detected that Globus toolkit has registered its handler as a default handler for the HTTPS protocol. We are restoring it back to sun.net.www.protocol.https.Handler");
			}
		} catch (Exception e) {
			logger.error("Unable to check java.net.URL.handlers for presense of Globus HTTPS handler. "
					+ e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		restoreDefaultHttpsURLHandler();
	}

	@Override
	public void run() {
		restoreDefaultHttpsURLHandler();
	}

}
