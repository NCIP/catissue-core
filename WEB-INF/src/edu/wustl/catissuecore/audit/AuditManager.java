/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.audit;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.AuditEventDetails;
import edu.wustl.catissuecore.domain.AuditEventLog;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.User;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditManager 
{
//	String str = {"Long","Double","In"}
//	List list = new
	public Set compare(Object previousObj, Object currentObj) throws IllegalAccessException, Exception
	{
		Set auditEventDetailsCollection = new HashSet(); 
		
		Class previousObjClass = previousObj.getClass();
		Class currentObjClass = previousObj.getClass();
		
		if(previousObjClass.equals(currentObjClass))
		{
			Field[] fields = previousObjClass.getDeclaredFields();
			//previousObjClass.getDeclaredField("specimenRequirementCollection");
			
			for (int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
				System.out.println(fields[i].getName()+": "+fields[i].getType());
				
				
				Object prevVal = fields[i].get(previousObj);
				Object currVal = fields[i].get(currentObj);
				
				if(prevVal!=null && currVal!=null)
				{
					if(!prevVal.equals(currVal))
					{
						AuditEventDetails auditEventDetails = new AuditEventDetails();
						auditEventDetails.setElementName(fields[i].getName());
						auditEventDetails.setPreviousValue(prevVal.toString());
						auditEventDetails.setCurrentValue(currVal.toString());
						
						auditEventDetailsCollection.add(auditEventDetails);
					}
				}
			}
		}
		return auditEventDetailsCollection;
	}
	
	public static void main(String[] args)  throws IllegalAccessException, Exception
	{
		CellSpecimenRequirement dept1 = new CellSpecimenRequirement();
		//dept1.setActivityStatus("dept1aas");
		
		
		CellSpecimenRequirement dept2 = new CellSpecimenRequirement();
//		dept2.setActivityStatus("dept2");
		
		AuditManager aAuditManager = new AuditManager();
		
		Rectangle r = new Rectangle(100, 325);

		Set aSet = aAuditManager.compare(dept1,dept2);
		System.out.println(aSet);
	}
}
