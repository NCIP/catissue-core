
package com.krishagni.catissueplus.core.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.ManagedSessionContext;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.common.util.logger.Logger;

@Aspect
public class TransactionalInterceptor {

	private static Logger LOGGER = Logger.getCommonLogger(TransactionalInterceptor.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Pointcut("execution(@com.krishagni.catissueplus.core.common.PlusTransactional * *(..))")
	public void transactionalMethod() {
	}

	@Around("transactionalMethod()")
	public Object startTransaction(ProceedingJoinPoint pjp) {
		boolean isTransactionStarted = false;
		boolean isSessionStarted = !ManagedSessionContext.hasBind(sessionFactory);

		Session session = sessionFactory.getCurrentSession();
 
		Transaction tx = session.getTransaction();
		if (tx != null && !tx.isActive()) {
			tx = session.beginTransaction();
			LOGGER.info("New transaction started");
			isTransactionStarted = true;
		}

		Object object = null;
		try {
			object = pjp.proceed();
			ResponseEvent resp = (object instanceof ResponseEvent) ? (ResponseEvent)object : null; 
			if ((resp == null || resp.isSuccessful()) && 
				isTransactionStarted && 
				tx != null) {				
				tx.commit();
				LOGGER.info("Transaction commited.");
			}
		}
		catch (Throwable e) {
			LOGGER.error(e.getCause(), e);
			LOGGER.error(e.getMessage(), e);
			if (isTransactionStarted && tx != null) {
				tx.rollback();
				LOGGER.info("Error thrown, transaction rolled back.");
			}
			throw OpenSpecimenException.serverError(e);
		}
		finally {
			
			if (session != null && session.isOpen() && isSessionStarted && isTransactionStarted) {
				session.close();
				LOGGER.info("Session closed.");
			}

		}

		return object;
	}
}
