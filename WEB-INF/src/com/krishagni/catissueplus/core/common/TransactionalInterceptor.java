
package com.krishagni.catissueplus.core.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.errors.ErrorCodeEnum;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.common.util.logger.Logger;

@Aspect
public class TransactionalInterceptor {

	private static Logger LOGGER = Logger.getCommonLogger(NewSpecimenBizLogic.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Pointcut("within(com.krishagni.catissueplus.service.impl.* || com.krishagni.catissueplus.core.*.services.impl.*)")
	public void anyPublicMethod() {
	}

	@Around("anyPublicMethod() && @annotation(plusTransactional)")
	public Object startTransaction(ProceedingJoinPoint pjp, PlusTransactional plusTransactional) {
		boolean isTransactionStarted = false;
		boolean isSessionStarted = false;

		Session session = null;
		try {
			session = SessionFactoryUtils.doGetSession(sessionFactory, false);
		}
		catch (IllegalStateException ex) {
			//			ex.printStackTrace();
			LOGGER.info("Session not found. Creating a new session");
			LOGGER.info(ex);
		}

		if (session == null) {
			session = sessionFactory.getCurrentSession();
			LOGGER.info("New session created");
			isSessionStarted = true;
		}

		Transaction tx = session.getTransaction();
		if (tx != null && !tx.isActive()) {
			tx = session.beginTransaction();
			LOGGER.info("New transaction started");
			isTransactionStarted = true;
		}

		Object object = null;
		try {
			object = pjp.proceed();
		}
		catch (Throwable e) {
			LOGGER.error(e.getStackTrace());
			if (isTransactionStarted && tx != null) {
				tx.rollback();
				LOGGER.info("Error thrown, transaction rolled back.");
			}
			throw new CaTissueException(ErrorCodeEnum.QUERY_EXECUTION_ERROR, "Error while executing query");
		}
		finally {
			if (isTransactionStarted && tx != null) {
				tx.commit();
			}
			if (session != null && session.isOpen() && isSessionStarted) {
				session.close();
				LOGGER.info("Session closed.");
			}

		}

		return object;
	}
}
