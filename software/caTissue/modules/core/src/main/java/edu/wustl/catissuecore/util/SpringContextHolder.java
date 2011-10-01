/**
 * 
 */
package edu.wustl.catissuecore.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.wustl.catissuecore.bizlogic.ccts.RegistrationPersistenceListener;

/**
 * Utility class that helps other classes, which are not managed by Spring (such
 * as {@link RegistrationPersistenceListener}, to obtain
 * {@link ApplicationContext} reference. <b>Try not to rely on this class too
 * much; it's not an example of best design.</b>
 * 
 * @author Denis G. Krylov
 * 
 */
public final class SpringContextHolder implements ApplicationContextAware {

	public static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		applicationContext = ctx;

	}

}
