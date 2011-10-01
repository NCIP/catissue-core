/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.springframework.context.ApplicationContext;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.SpringContextHolder;
import edu.wustl.common.util.logger.Logger;

/**
 * Intercepts {@link CollectionProtocolRegistration} persistence events (create,
 * update), interprets them, and broadcasts to C3PR if needed.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class RegistrationPersistenceListener implements
		PostInsertEventListener, PostUpdateEventListener {

	private static final Logger logger = Logger
			.getCommonLogger(RegistrationPersistenceListener.class);

	private static final ThreadLocal<Boolean> suspendFlagHolder = new ThreadLocal<Boolean>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		process(event.getEntity(), event.getOldState(), event.getState());
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		process(event.getEntity(), new Object[0], event.getState());
	}

	private void process(Object entity, Object[] oldState, Object[] newState) {
		if (!Boolean.TRUE.equals(suspendFlagHolder.get())
				&& entity instanceof CollectionProtocolRegistration) {
			try {
				ApplicationContext ctx = SpringContextHolder.applicationContext;
				if (ctx != null) {
					ICctsIntegrationBizLogic bizLogic = (ICctsIntegrationBizLogic) ctx
							.getBean("cctsIntegrationBizLogic");
					bizLogic.sendToRegistrationService(
							(CollectionProtocolRegistration) entity, oldState,
							newState);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 
	 */
	public static void suspendForCurrentThread() {
		suspendFlagHolder.set(Boolean.TRUE);
	}
	
	public static void resumeForCurrentThread() {
		suspendFlagHolder.set(Boolean.FALSE);
	}
	

}
