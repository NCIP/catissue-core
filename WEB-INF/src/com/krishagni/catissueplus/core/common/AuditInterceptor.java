
package com.krishagni.catissueplus.core.common;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Audit.Operation;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

@Aspect
public class AuditInterceptor {

	private static Logger LOGGER = Logger.getCommonLogger(AuditInterceptor.class);

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Pointcut("within(com.krishagni.catissueplus.core.*.services.impl.*)")
	public void anyPublicMethod() {
	}

	@Around("anyPublicMethod() && @annotation(audit)")
	@PlusTransactional
	public void audit(ProceedingJoinPoint pjp, Audit audit) {
		Operation operation = audit.operation();
		Object obj = null;
		Object[] params = pjp.getArgs();
		RequestEvent event = (RequestEvent) params[0];
		SessionDataBean sessionDataBean = event.getSessionDataBean();
		try {
			obj = pjp.proceed();
		}
		catch (Throwable e) {
			LOGGER.error(e.getCause(), e);
			LOGGER.error(e.getMessage(), e);
			throw new OpenSpecimenException(e);
		}

		finally {
			insertAudit(audit, operation, sessionDataBean, obj);
		}
	}

	private void insertAudit(Audit audit, Operation operation, SessionDataBean sessionDataBean, Object obj) {
		com.krishagni.catissueplus.core.audit.domain.Audit auditObject = new com.krishagni.catissueplus.core.audit.domain.Audit();
				ResponseEvent response =  (ResponseEvent)obj;
		//		auditObject.setId(id)
		auditObject.setIpAddress(sessionDataBean.getIpAddress());
		auditObject.setUserId(sessionDataBean.getUserId());
		auditObject.setUpdatedDate(new Date());
//		auditObject.setObjectType(audit.object());
		auditObject.setOperation(operation.name());
		auditObject.setReasonForChange(operation.name());
		auditObject.setObjectId(1l);
		daoFactory.getAuditDao().saveOrUpdate(auditObject);

		//		auditObject.setIpAddress(ipAddress);
	}

}
