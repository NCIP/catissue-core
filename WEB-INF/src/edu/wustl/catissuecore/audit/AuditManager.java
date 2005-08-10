/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.audit;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.AuditEvent;
import edu.wustl.catissuecore.domain.AuditEventDetails;
import edu.wustl.catissuecore.domain.AuditEventLog;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.exception.AuditException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditManager 
{
	private AuditEvent auditEvent;  
	private final Set dataSet = new HashSet(); 
	public AuditManager()
	{
		dataSet.add(Byte.class);
		dataSet.add(Double.class);
		dataSet.add(Float.class);
		dataSet.add(Integer.class);
		dataSet.add(Long.class);
		dataSet.add(Short.class);
		
		dataSet.add(String.class);
		dataSet.add(Boolean.class);
		dataSet.add(Character.class);
		
		dataSet.add(Date.class);
		
		
		auditEvent = new AuditEvent();
		auditEvent.setIpAddress("10.10.10.10");
		auditEvent.setUser(null);
	}
	
	private boolean isVariable(Field filed)
	{
		return dataSet.contains(filed.getType());
	}

	public void compare(AbstractDomainObject currentObj, AbstractDomainObject previousObj,String eventType) throws AuditException
	{
		try
		{
			AuditEventLog auditEventLog = new AuditEventLog();
			
			auditEventLog.setObjectIdentifier(currentObj.getSystemIdentifier());
			auditEventLog.setObjectName(currentObj.getClass().getName());	
			auditEventLog.setEventType(eventType);
			
			auditEvent.getAuditEventLogCollection().add(auditEventLog);
			
			Set auditEventDetailsCollection = new HashSet(); 
			
			Class currentObjClass = currentObj.getClass();
			Class previousObjClass = currentObjClass; 
			if(previousObj!=null)
				previousObjClass = previousObj.getClass();
			
			if(previousObjClass.equals(currentObjClass))
			{
				Field[] fields = previousObjClass.getDeclaredFields();
				
				for (int i = 0; i < fields.length; i++)
				{
					AuditEventDetails auditEventDetails = processField(fields[i], previousObj, currentObj);
					if(auditEventDetails!=null)
						auditEventDetailsCollection.add(auditEventDetails);
				}
			}
			auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
		}
		catch(Exception ex)
		{
			throw new AuditException();
		}
	}
	
	private AuditEventDetails processField(Field field, Object previousObj, Object currentObj) throws Exception
	{
		if(isVariable(field))
		{
			System.out.println(field.getName()+": "+field.getType());
			
			field.setAccessible(true);
			
			Object prevVal = null;
			if(previousObj!=null)
				prevVal = field.get(previousObj);
			Object currVal = field.get(currentObj);
			
			System.out.println("prevVal "+prevVal);
			System.out.println("currVal "+currVal);
			
			AuditEventDetails auditEventDetails = compareValue(prevVal, currVal);
			if(auditEventDetails!=null)
			{
				auditEventDetails.setElementName(field.getName());
				return auditEventDetails;
			}
		}
		return null;
	}
	
	private AuditEventDetails compareValue(Object prevVal, Object currVal) 
	{
		AuditEventDetails auditEventDetails = new AuditEventDetails();
		
		if(prevVal==null || currVal==null )
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
			else 
				return null;
		}
		else if(!prevVal.equals(currVal))
		{
			auditEventDetails.setPreviousValue(prevVal.toString());
			auditEventDetails.setCurrentValue(currVal.toString());
		}
		System.out.println("auditEventDetails "+auditEventDetails);
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
		
		dao.insert(auditEvent,false);
		Iterator auditLogIterator = auditEvent.getAuditEventLogCollection().iterator();
		while(auditLogIterator.hasNext())
		{
			AuditEventLog auditEventLog = (AuditEventLog)auditLogIterator.next();
			auditEventLog.setAuditEvent(auditEvent);
			dao.insert(auditEventLog,false);
			
  			Iterator auditEventDetailsIterator = auditEventLog.getAuditEventDetailsCollcetion().iterator();
  			while(auditEventDetailsIterator.hasNext())
  			{
  				AuditEventDetails auditEventDetails = (AuditEventDetails)auditEventDetailsIterator.next();
  				auditEventDetails.setAuditEventLog(auditEventLog);
  				dao.insert(auditEventDetails,false);
  			}
		}
		auditEvent = new AuditEvent();
	}
}
