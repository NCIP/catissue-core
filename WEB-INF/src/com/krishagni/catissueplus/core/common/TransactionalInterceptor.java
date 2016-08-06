
package com.krishagni.catissueplus.core.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
	private static Log logger = LogFactory.getLog(TransactionalInterceptor.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

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
		try {
			return doWork(pjp);
		} catch (Throwable t) {
			notifyUncaughtServerError(t.getCause());
			t.printStackTrace();
			throw t;
		}
	}

	private Object doWork(ProceedingJoinPoint pjp) {
		return txTmpl.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					Object result = pjp.proceed();
					if (result instanceof ResponseEvent) {
						ResponseEvent<?> resp = (ResponseEvent<?>)result;
						if (!resp.isSuccessful() && !resp.isForceTxCommitEnabled()) {
							status.setRollbackOnly();
							if (resp.isSystemError() || resp.isUnknownError()) {
								notifyUncaughtServerError(resp.getError().getException());
							}
						}
					}
				
					return result ;
				} catch (OpenSpecimenException ose) {
					status.setRollbackOnly();
					throw ose;
				} catch (Throwable t) {
					t.printStackTrace();
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

			EmailUtil.getInstance().sendEmail(
				SERVER_ERROR_TMPL,
				new String[] {itAdminEmailId},
				new File[] {getErrorLog(t)},
				getErrorDetail(t));
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

	private Map<String, Object> getErrorDetail(Throwable t) {
		StackTraceElement[] stackTrace = t.getStackTrace();
		StackTraceElement ste = null;
		for (int i = 0; i < stackTrace.length; i++) {
			if (stackTrace[i].getMethodName().contains("startTransaction")) {
				ste = stackTrace[++i];
				break;
			}
		}

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

	private static final String SERVER_ERROR_TMPL = "server_error";
}
