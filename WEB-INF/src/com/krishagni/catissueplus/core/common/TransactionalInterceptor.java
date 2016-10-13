
package com.krishagni.catissueplus.core.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.EmailUtil;

@Aspect
public class TransactionalInterceptor {
	private static final Log logger = LogFactory.getLog(TransactionalInterceptor.class);

	private PlatformTransactionManager transactionManager;
	
	private TransactionTemplate txTmpl;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		this.txTmpl = new TransactionTemplate(transactionManager);
		this.txTmpl.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	}

	@Pointcut("execution(@com.krishagni.catissueplus.core.common.PlusTransactional * *(..))")
	public void transactionalMethod() {
	}

	@Around("transactionalMethod()")
	public Object startTransaction(final ProceedingJoinPoint pjp) {
		return doWork(pjp, false);
	}

	@Pointcut("execution(@com.krishagni.catissueplus.core.common.RollbackTransaction * *(..))")
	public void rollbackMethod() {
	}

	@Around("rollbackMethod()")
	public Object rollbackTransaction(final ProceedingJoinPoint pjp) {
		return doWork(pjp, true);
	}

	private Object doWork(ProceedingJoinPoint pjp, boolean rollback) {
		try {
			return doWork0(pjp, rollback);
		} catch (Throwable t) {
			logger.error("Error doing work inside " + pjp.getSignature(), t);
			notifyUncaughtServerError(t.getCause());
			throw t;
		}
	}

	private Object doWork0(ProceedingJoinPoint pjp, boolean rollback) {
		return txTmpl.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					if (rollback) {
						status.setRollbackOnly();
					}

					Object result = pjp.proceed();
					if (result instanceof ResponseEvent) {
						ResponseEvent<?> resp = (ResponseEvent<?>)result;
						if (!resp.isSuccessful() && !resp.isForceTxCommitEnabled()) {
							status.setRollbackOnly();
							if (resp.isSystemError() || resp.isUnknownError()) {
								logger.error("Error doing work inside " + pjp.getSignature(), resp.getError().getException());
								notifyUncaughtServerError(resp.getError().getException());
							}
						} else if (resp.isRollback()) {
							status.setRollbackOnly();
						}
					}
				
					return result ;
				} catch (OpenSpecimenException ose) {
					status.setRollbackOnly();
					throw ose;
				} catch (Throwable t) {
					logger.error("Error doing work inside " + pjp.getSignature(), t);
					status.setRollbackOnly();
					notifyUncaughtServerError(t);
					throw OpenSpecimenException.serverError(t);
				}
			}
		});
	}

	private void notifyUncaughtServerError(Throwable t) {
		try {
			String itAdminEmailId = ConfigUtil.getInstance().getItAdminEmailId();
			if (StringUtils.isBlank(itAdminEmailId)) {
				logger.error("No admin or IT admin email ID has been configured to send uncaught system errors");
				return;
			}

			StackTraceElement ste = getSte(t);
			if (seenMaxTimesInLastInterval(ste)) {
				return;
			}

			EmailUtil.getInstance().sendEmail(
				SERVER_ERROR_TMPL,
				new String[] {itAdminEmailId},
				new File[] {getErrorLog(t)},
				getErrorDetail(t, ste));
		} catch (Exception e) {
			logger.error("Error notifying uncaught server errors", e);
		}
    }

	private File getErrorLog(Throwable t)
	throws IOException {
		PrintStream ps = null;
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("exception_" + sdf.format(Calendar.getInstance().getTime()), ".log");
			ps = new PrintStream(tmpFile);
			t.printStackTrace(ps);
			return tmpFile;
		} finally {
			IOUtils.closeQuietly(ps);

			if (tmpFile != null) {
				tmpFile.deleteOnExit();
			}
		}
	}

	private StackTraceElement getSte(Throwable t) {
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			if (stackTrace[i].getMethodName().contains("startTransaction")) {
				return (i + 1 < stackTrace.length) ? stackTrace[++i] : stackTrace[i];
			}
		}

		return null;
	}

	private Map<String, Object> getErrorDetail(Throwable t, StackTraceElement ste) {
		Map<String, Object> emailProps = new HashMap<>();
		emailProps.put("userName", AuthUtil.getCurrentUser().getUsername());
		emailProps.put("ste", ste);
		emailProps.put("time", new Date());

		/*
		 * For hibernate exceptions
		 * Throwable "t" gives -> org.hibernate.exception.DataException: could not execute statement
		 * "t.getCause()" gives -> actual error like
		 *   com.mysql.jdbc.MysqlDataTruncation: Data truncation: Data too long for column 'NAME' at row 1
		 *
		 * For java.lang.*
		 * Throwable "t" gives -> java.lang.NullPointerException
		 * "t.getCause()" gives -> null
		 */
		emailProps.put("exception", t.getCause() != null ? t.getCause() : t);
		return emailProps;
	}

	private synchronized boolean seenMaxTimesInLastInterval(StackTraceElement ste) {
		long currentTime = System.currentTimeMillis();
		List<Long> recentTimes = errorNotifTimes.get(ste);
		if (recentTimes == null) {
			recentTimes = new ArrayList<>();
			errorNotifTimes.put(ste, recentTimes);
		}

		if (recentTimes.size() >= MAX_INTERVAL_ERRORS) {
			//
			// Our remembering capacity has exceeded.
			// Try to remove times that do not fall in interval
			//
			Iterator<Long> iter = recentTimes.iterator();
			while (iter.hasNext()) {
				long t1 = iter.next();
				if (areTimesInSameInterval(t1, currentTime, INTERVAL_IN_MINS)) {
					break;
				}

				iter.remove();
			}
		}

		boolean seenMaxTimes = (recentTimes.size() >= MAX_INTERVAL_ERRORS);
		if (!seenMaxTimes) {
			recentTimes.add(currentTime);
		}

		return seenMaxTimes;
	}

	private boolean areTimesInSameInterval(long t1, long t2, int interval) {
		return ((t2 - t1) / (60 * 1000)) < interval;
	}

	//
	// Error notification variables
	//
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

	private static Map<StackTraceElement, List<Long>> errorNotifTimes = new HashMap<>();

	private static final int MAX_INTERVAL_ERRORS = 5;

	private static final int INTERVAL_IN_MINS = 60;

	private static final String SERVER_ERROR_TMPL = "server_error";
}
