
package com.krishagni.catissueplus.core.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.common.util.logger.Logger;

@Aspect
public class TransactionalInterceptor {

	private static Logger LOGGER = Logger.getCommonLogger(TransactionalInterceptor.class);

	private PlatformTransactionManager transactionManager;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Pointcut("execution(@com.krishagni.catissueplus.core.common.PlusTransactional * *(..))")
	public void transactionalMethod() {
	}

	@Around("transactionalMethod()")
	public Object startTransaction(ProceedingJoinPoint pjp) {
		System.out.println("Inside PlusTransactional *******************************");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    TransactionStatus status = transactionManager.getTransaction( def );
		Object object = null;
		System.out.println("getting current transaction *******************************");

		
		try {
			System.out.println("PJP proceed *******************************");
			object = pjp.proceed();
			System.out.println("PJP Completed *******************************");
			ResponseEvent resp = (object instanceof ResponseEvent) ? (ResponseEvent)object : null; 
			if ((resp == null || resp.isSuccessful() || resp.isForceTxCommitEnabled()) && status.isNewTransaction()) {		
				transactionManager.commit( status );
				LOGGER.info("Transaction commited.");
			}
		}
		catch (Throwable e) {
			System.out.println("Error  while executing PJP *******************************");
			LOGGER.error(e.getCause(), e);
			LOGGER.error(e.getMessage(), e);
			if (status.isNewTransaction()) {
				transactionManager.rollback(status);
				LOGGER.info("Error thrown, transaction rolled back.");
			}
			throw OpenSpecimenException.serverError(e);
		}
		finally {
			if (!status.isCompleted() && !status.isNewTransaction()) {
				transactionManager.rollback(status);
			}
		}
		return object;
	}
}
