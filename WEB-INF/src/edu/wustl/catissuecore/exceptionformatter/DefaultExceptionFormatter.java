package edu.wustl.catissuecore.exceptionformatter;

import java.text.MessageFormat;

import edu.wustl.common.util.global.ApplicationProperties;

public class DefaultExceptionFormatter implements ExceptionFormatter {

	public String formatMessage(Exception objExcp, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getErrorMessage(String key,Object[] args)
	{
		String message=null;
		message=ApplicationProperties.getValue(key);
		if(message!=null && args!=null)
		{
			message=MessageFormat.format(message,args);
		}
		return message;
	}
}
