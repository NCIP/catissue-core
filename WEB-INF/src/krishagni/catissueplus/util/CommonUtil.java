package krishagni.catissueplus.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import java.util.Calendar;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatterFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;


public class CommonUtil
{
	private static final Logger LOGGER = Logger.getCommonLogger(CommonUtil.class);
	public static Object getCorrespondingOldObject(Collection objectCollection, Long identifier)
	{
		Iterator iterator = objectCollection.iterator();
		AbstractDomainObject abstractDomainObject = null;
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObj = (AbstractDomainObject) iterator.next();

			if (identifier != null && identifier.equals(abstractDomainObj.getId()))
			{
				abstractDomainObject = abstractDomainObj;
				break;
			}
		}
		return abstractDomainObject;
	}

	private  static String formatException(Exception exception, Object obj, String operation) // NOPMD
	{
		String errMsg;
		String tableName = null;
		try
		{
			if (exception == null)
			{
				ErrorKey errorKey = ErrorKey.getErrorKey("biz.formatex.error");
				throw new ApplicationException(errorKey, null, "exception is null.");
			}
			// Get ExceptionFormatter
			ExceptionFormatter exFormatter = ExceptionFormatterFactory.getFormatter(exception);
			// call for Formating Message
			if (exFormatter == null)
			{
				errMsg = exception.getMessage();
			}
			else
			{
				if(obj instanceof LinkedHashSet && !((LinkedHashSet)obj).isEmpty())
				{
					obj = ((LinkedHashSet)obj).iterator().next();
				}
				String appName = CommonServiceLocator.getInstance().getAppName();
				HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
				.getHibernateMetaData(appName);
				if (hibernateMetaData != null)
				{
					tableName = hibernateMetaData.getTableName(obj.getClass());
				}
				else
				{
					tableName = "";
				}
				errMsg = exFormatter.formatMessage(exception);
			}
		}
		catch (ApplicationException except)
		{
			LOGGER.error(except.getMessage(), except);
			String[] arg = {operation, tableName};
			errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01", arg);
		}
		return errMsg;
	}


	public static String getErrorMessage(ApplicationException exception, Object obj, String operation) // NOPMD
    {
    	StringBuffer buff = new StringBuffer();

    	if (exception.getWrapException() == null)
    	{

    		if(exception.getErrorKey()== null && exception.toMsgValuesArray()
    				!= null && exception.toMsgValuesArray().length>0)
    		{
    			for(String msg :exception.toMsgValuesArray())
    			{
    				buff.append(msg).append("  ");
    			}
    		}
    		else
    		{
    			buff.append(exception.getMessage());
    		}
    	}
    	else
    	{
    		Exception exp = getWrapException(exception);
    		if(exp != null)
    		{
    			buff.append(formatException(exp,obj,operation));
    		}
    		else
    		{
    			buff.append(exception.getMessage());
    		}
    	}
        return buff.toString();
    }
    /**
     * This method returns root exception used in message formatter.
     * @param exception ApplicationException
     * @return exception used in message formatter.
     */
    private static Exception getWrapException(ApplicationException exception)
    {
    	Throwable rootException=null;
    	Throwable wrapException=exception;
		while(true)
		{
    		if((wrapException instanceof ApplicationException))
    		{
    			wrapException= wrapException.getCause();
    			continue;
    		}
    		else
    		{
    			rootException= wrapException;
    			break;
    		}
		}
    	return (Exception)rootException;
    }
    
   public static String appendTimestamp(String name) {
  		Calendar cal = Calendar.getInstance();
  		name = name + "_" + cal.getTimeInMillis();
  		return name;
  	}
}
