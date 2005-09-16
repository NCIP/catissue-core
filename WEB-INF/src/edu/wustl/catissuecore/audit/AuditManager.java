/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.audit;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AuditEvent;
import edu.wustl.catissuecore.domain.AuditEventDetails;
import edu.wustl.catissuecore.domain.AuditEventLog;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.AuditException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditManager 
{
	private AuditEvent auditEvent;  
	
	public AuditManager()
	{
		auditEvent = new AuditEvent();
	}
	
	public void setUserId(Long userId)
	{
		User user = new User();
		user.setSystemIdentifier(userId);
		auditEvent.setUser(user);
	}
	
	public void setIpAddress(String IPAddress)
	{
		auditEvent.setIpAddress(IPAddress);
	}
	
	private boolean isVariable(Object obj)
	{
		if(obj instanceof Number || obj instanceof String || 
				obj instanceof Boolean || obj instanceof Character || 
				obj instanceof Date || obj instanceof Auditable)
			return true;
		return false;
	}

	public void compare(Auditable currentObj, Auditable previousObj,String eventType) throws AuditException
	{
		if( currentObj == null )
			return;
		
		try
		{
			AuditEventLog auditEventLog = new AuditEventLog();
			
			auditEventLog.setObjectIdentifier(currentObj.getSystemIdentifier());
			auditEventLog.setObjectName(HibernateMetaData.getTableName(currentObj.getClass()));	
			auditEventLog.setEventType(eventType);
			
			Set auditEventDetailsCollection = new HashSet(); 
			
			Class currentObjClass = currentObj.getClass();
			Class previousObjClass = currentObjClass; 
			
			if(previousObj!=null)
				previousObjClass = previousObj.getClass();
			
			if(previousObjClass.equals(currentObjClass))
			{
				//Field[] fields = currentObjClass.getDeclaredFields();
				Method[] methods = currentObjClass.getMethods();
				
				for (int i = 0; i < methods.length; i++)
				{
					if(methods[i].getName().startsWith("get") && methods[i].getParameterTypes().length==0)
					{
						AuditEventDetails auditEventDetails = processField(methods[i], currentObj, previousObj);
						if(auditEventDetails!=null)
							auditEventDetailsCollection.add(auditEventDetails);
					}
				}
			}
			
			auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
			auditEvent.getAuditEventLogCollection().add(auditEventLog);
		}
		catch(Exception ex)
		{
			Logger.out.debug(ex.getMessage(),ex) ;
			throw new AuditException();
		}
	}
	
	private AuditEventDetails processField(Method method, Object currentObj, Object previousObj) throws Exception
	{
		Object prevVal = getValue(method, previousObj);
		Object currVal = getValue(method, currentObj);
		
		AuditEventDetails auditEventDetails = compareValue(prevVal, currVal);
		if(auditEventDetails!=null)
		{
			String attributeName = processAttributeName(method.getName());
			auditEventDetails.setElementName(HibernateMetaData.getColumnName(currentObj.getClass(),attributeName));
		}
		return auditEventDetails;
	}
	
	private String processAttributeName(String methodName) throws Exception
	{
		String attributeName = "";
		int index = methodName.indexOf("get");
		if(index!=-1)
		{
			attributeName = methodName.substring(index+"get".length());
		}
		
		String firstChar = (attributeName.charAt(0)+"").toLowerCase();
		attributeName = firstChar + attributeName.substring(1);
		
		Logger.out.debug("methodName <"+methodName+">");
		Logger.out.debug("attributeName <"+attributeName+">");
		
		return attributeName;
	}
	
	
	private Object getValue(Method method, Object obj) throws Exception
	{
		if(obj!=null)
		{
			//System.out.println("method "+method.getName());
			
			Object val = Utility.getValueFor(obj,method);
			
			if(val instanceof Auditable)
			{
				Auditable auditable = (Auditable)val;
				return auditable.getSystemIdentifier();
			}
			if(isVariable(val))
				return val; 
		}
		return null;
	}
	
	private AuditEventDetails compareValue(Object prevVal, Object currVal) 
	{
		AuditEventDetails auditEventDetails = new AuditEventDetails();
		
		if(prevVal==null && currVal==null)
		{
			return null;
		}
		
		if(prevVal==null || currVal==null)
		{
			if(prevVal==null && currVal!=null)
			{
				auditEventDetails.setPreviousValue(null);
				auditEventDetails.setCurrentValue(currVal.toString());
			}
			else if(prevVal!=null && currVal==null)
			{
				auditEventDetails.setPreviousValue(prevVal.toString());
				auditEventDetails.setCurrentValue(null);
			}
		}
		else if(!prevVal.equals(currVal))
		{
			auditEventDetails.setPreviousValue(prevVal.toString());
			auditEventDetails.setCurrentValue(currVal.toString());
		}
		//System.out.println("auditEventDetails "+auditEventDetails);
		return auditEventDetails;
	}
	
	
	public static void main(String[] args)  throws IllegalAccessException, Exception
	{
		AuditManager aAuditManager = new AuditManager();
		
		Department dept1 = null;
		Department dept2 = new Department();
		dept2.setName("DA");
		
//		User part1 = new User();
//		part1.setActivityStatus(null);
//		part1.setDepartment(new Department());
//		
//		
//		User part2 = new User();
//		part2.setActivityStatus("part2");
//		part2.setDepartment(null);
		
		aAuditManager.compare(dept2,dept1,"");
		System.out.println(aAuditManager.auditEvent.getAuditEventLogCollection());
	}
	
	public void insert(DAO dao) throws DAOException 
	{
		if(auditEvent.getAuditEventLogCollection().isEmpty())
			return;
		
		try
		{
		dao.insert(auditEvent,null, false, false);
		Iterator auditLogIterator = auditEvent.getAuditEventLogCollection().iterator();
		while(auditLogIterator.hasNext())
		{
			AuditEventLog auditEventLog = (AuditEventLog)auditLogIterator.next();
			auditEventLog.setAuditEvent(auditEvent);
			dao.insert(auditEventLog,null, false, false);
			
  			Iterator auditEventDetailsIterator = auditEventLog.getAuditEventDetailsCollcetion().iterator();
  			while(auditEventDetailsIterator.hasNext())
  			{
  				AuditEventDetails auditEventDetails = (AuditEventDetails)auditEventDetailsIterator.next();
  				auditEventDetails.setAuditEventLog(auditEventLog);
  				dao.insert(auditEventDetails,null, false, false);
  			}
		}
		auditEvent = new AuditEvent();
		}
		catch(UserNotAuthorizedException sme)
		{
		    Logger.out.debug("Exception:"+sme.getMessage(),sme);
		}
	}
}