/**
 * 
 */
package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.collections.CollectionUtils;

/**
 * Utility class that helps transfer error messages to UI layer from deep within
 * other layers of application. It can be convenient in situations when there is
 * no direct invocation chain from UI layer to other layer and throwing an
 * exception is not a good option either (e.g. when error is ignorable). In this
 * case we bind error messages to the current thread and let UI retrieve it from
 * there, eh?
 * 
 * @author Denis G. Krylov
 * 
 */
public final class MessagesHolder {

	private static final ThreadLocal<Collection<String>> errorMessages = new ThreadLocal<Collection<String>>();

	private static final ThreadLocal<Collection<String>> infoMessages = new ThreadLocal<Collection<String>>();

	/**
	 * Clear all messages.
	 */
	public static void clear() {
		errorMessages.remove();
		infoMessages.remove();
	}

	/**
	 * @param msg
	 */
	public static void addErrorMessage(String msg) {
		add(errorMessages, msg);
	}

	/**
	 * @param msg
	 */
	public static void addInformationalMessage(String msg) {
		add(infoMessages, msg);
	}

	public static Collection<String> getErrorMessages() {
		return errorMessages.get() != null ? errorMessages.get()
				: CollectionUtils.EMPTY_COLLECTION;
	}

	public static Collection<String> getInformationalMessages() {
		return infoMessages.get() != null ? infoMessages.get()
				: CollectionUtils.EMPTY_COLLECTION;
	}

	private static void add(ThreadLocal<Collection<String>> holder, String msg) {
		Collection<String> c = holder.get();
		if (c == null) {
			c = new LinkedHashSet<String>();
			holder.set(c);
		}
		c.add(msg);
	}

}
