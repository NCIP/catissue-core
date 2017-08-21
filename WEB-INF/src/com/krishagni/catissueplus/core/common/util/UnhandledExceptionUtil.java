package com.krishagni.catissueplus.core.common.util;

import java.util.Calendar;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.common.domain.UnhandledException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.service.CommonService;

@Configurable
public class UnhandledExceptionUtil {
	
	private static Log logger = LogFactory.getLog(UnhandledExceptionUtil.class);

	private static UnhandledExceptionUtil instance = null;
	
	@Autowired
	private CommonService commonSvc;

	public static UnhandledExceptionUtil getInstance() {
		if (instance == null || instance.commonSvc == null) {
			instance = new UnhandledExceptionUtil();
		}

		return instance;
	}
	
	public Long saveUnhandledException(Throwable t, StackTraceElement ste, Object[] inputArgs) {
		if (commonSvc == null) {
			logger.warn("Unhandled exception sub-system not initialised yet.");
			return null;
		}

		UnhandledException exception = new UnhandledException();
		exception.setUser(AuthUtil.getCurrentUser());
		exception.setClassName(ste.getFileName());
		exception.setMethodName(ste.getMethodName());
		exception.setTimestamp(Calendar.getInstance().getTime());
		exception.setException(t.getCause() != null ? t.getCause().toString() : t.toString());
		exception.setStackTrace(ExceptionUtils.getStackTrace(t));

		if (inputArgs.length != 0) {
			try {
				Object args = inputArgs;
				if (inputArgs[0] instanceof RequestEvent) {
					args = ((RequestEvent)inputArgs[0]).getPayload();
				}

				ObjectMapper mapper = new ObjectMapper();
				exception.setInputArgs(mapper.writeValueAsString(args));
			} catch (JsonProcessingException e) {
				String errorMsg = "Error marshalling input arguments";
				exception.setInputArgs(errorMsg);
				logger.error(errorMsg, e);
			}
		}

		return commonSvc.saveUnhandledException(exception);
	}

}
