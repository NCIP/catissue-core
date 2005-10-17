/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.audit;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AuditEvent;
import edu.wustl.catissuecore.domain.AuditEventDetails;
import edu.wustl.catissuecore.domain.AuditEventLog;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.AuditException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
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
	//Instance of Audit event. All the change under one database session are added under this event.
	private AuditEvent auditEvent;  
	
	public AuditManager()
	{
		auditEvent = new AuditEvent();
	}
	
	public void setUserId(Long userId)
	{
		if(userId!=null )
		{
			User user = new User();
			user.setSystemIdentifier(userId);
			auditEvent.setUser(user);
		}
		else
			auditEvent.setUser(null);
	}
	
	public void setIpAddress(String IPAddress)
	{
		auditEvent.setIpAddress(IPAddress);
	}
	
	/**
	 * Check whether the object type is a premitive data type or a user defined datatype.
	 * */
	private boolean isVariable(Object obj)
	{
		if(obj instanceof Number || obj instanceof String || 
				obj instanceof Boolean || obj instanceof Character || 
				obj instanceof Date || obj instanceof Auditable)
			return true;
		return false;
	}

	/**
	 * Compares the contents of two objects. 
	 * @param currentObj Current state of object.
	 * @param currentObj Previous state of object.
	 * @param eventType Event for which the comparator will be called. e.g. Insert, update, delete etc.
	 * */
	public void compare(Auditable currentObj, Auditable previousObj, String eventType) throws AuditException
	{
		if( currentObj == null )
			return;
		
		try
		{
			//An auidt event will contain many logs.
			AuditEventLog auditEventLog = new AuditEventLog();
			
			auditEventLog.setObjectIdentifier(currentObj.getSystemIdentifier());
			auditEventLog.setObjectName(HibernateMetaData.getTableName(currentObj.getClass()));
			auditEventLog.setEventType(eventType);
			
			//An event log will contain many event details
			Set auditEventDetailsCollection = new HashSet();
			
			//Class of the object being compared
			Class currentObjClass = currentObj.getClass();
			Class previousObjClass = currentObjClass; 
			
			if(previousObj!=null)
				previousObjClass = previousObj.getClass();
			
			//check the class for both objects are equals or not.
			if(previousObjClass.equals(currentObjClass))
			{
				//Retrieve all the methods defined in the class. 
				Method[] methods = currentObjClass.getMethods();
				
				for (int i = 0; i < methods.length; i++)
				{
					//filter only getter methods.
					if(methods[i].getName().startsWith("get") && methods[i].getParameterTypes().length==0)
					{
						AuditEventDetails auditEventDetails = processField(methods[i], currentObj, previousObj);
						if(auditEventDetails!=null)
							auditEventDetailsCollection.add(auditEventDetails);
					}
				}
			}
			
			if(!auditEventDetailsCollection.isEmpty())
			{
				auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
				auditEvent.getAuditEventLogCollection().add(auditEventLog);
			}
		}
		catch(Exception ex)
		{
			Logger.out.debug(ex.getMessage(),ex);
			throw new AuditException();
		}
	}
	
	private AuditEventDetails processField(Method method, Object currentObj, Object previousObj) throws Exception
	{
		Object prevVal = getValue(method, previousObj);
		Object currVal = getValue(method, currentObj);
		
		String attributeName = processAttributeName(method.getName());
		AuditEventDetails auditEventDetails = compareValue(prevVal, currVal);
		
		if(auditEventDetails!=null)
		{
			//String attributeName = processAttributeName(method.getName());
			String columnName = HibernateMetaData.getColumnName(currentObj.getClass(),attributeName);
		
			//Logger.out.debug("attributeName "+attributeName);
			///Case of transient object
			if(columnName.equals(""))
				return null;
			
			auditEventDetails.setElementName(columnName);
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
		
		//Logger.out.debug("methodName <"+methodName+">");
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
		Logger.out.debug("prevVal <"+prevVal+">");
		Logger.out.debug("currVal <"+currVal+">");
		
		
		
		if(prevVal==null && currVal==null)
		{
			return null;
		}
		
		if(prevVal==null || currVal==null)
		{
			if(prevVal==null && currVal!=null)
			{
				AuditEventDetails auditEventDetails = new AuditEventDetails();
				auditEventDetails.setPreviousValue(null);
				auditEventDetails.setCurrentValue(currVal.toString());
				return auditEventDetails;
			}
			else if(prevVal!=null && currVal==null)
			{
				AuditEventDetails auditEventDetails = new AuditEventDetails();
				auditEventDetails.setPreviousValue(prevVal.toString());
				auditEventDetails.setCurrentValue(null);
				return auditEventDetails;
			}
		}
		else if(!prevVal.equals(currVal))
		{
			AuditEventDetails auditEventDetails = new AuditEventDetails();
			auditEventDetails.setPreviousValue(prevVal.toString());
			auditEventDetails.setCurrentValue(currVal.toString());
			return auditEventDetails;
		}
		
		return null;
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
	
	
	public static void main(String[] args)  throws IllegalAccessException, Exception
	{
		Variables.catissueHome = System.getProperty("user.dir");
		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		Logger.out = org.apache.log4j.Logger.getLogger("A");
		
		Logger.out.info("here");
		
		AuditManager aAuditManager = new AuditManager();

		
//		HibernateDAO dao = new HibernateDAO();
//		dao.openSession(null);
//		Department deptCurr = (Department)dao.retrieve(Department.class.getName(),new Long(2));
//		dao.closeSession();
//
//		dao.openSession(null);
//		Department deptOld = (Department)dao.retrieve(Department.class.getName(),new Long(2));
//		dao.closeSession();
		
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		StorageContainer storageContainerCurr = (StorageContainer)(bizLogic.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1))).get(0);
		StorageContainer storageContainerOld = (StorageContainer)(bizLogic.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1))).get(0);
		
		
		//storageContainerCurr.setTempratureInCentigrade(new Double(80));
		

		
		aAuditManager.compare(storageContainerCurr,storageContainerOld,"UPDATE");
		System.out.println(aAuditManager.auditEvent.getAuditEventLogCollection());
		
		
		Institution a = new Institution();
		a.setName("AA");
		aAuditManager.compare(a,null, "INSERT");
		
		System.out.println(aAuditManager.auditEvent.getAuditEventLogCollection());
	}
	
	public void addAuditEventLogs(Collection auditEventLogsCollection)
	{
	    auditEvent.getAuditEventLogCollection().addAll(auditEventLogsCollection);
	}
	
}