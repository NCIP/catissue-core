/*
 * Created on Oct 8, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author jitendra_agrawal
 */
public class AssignPrivilegePageBizLogic extends DefaultBizLogic
{

	public List getRecordNames(Set recordIds, AssignPrivilegesForm privilegesForm) throws DAOException
	{				
		List recordNames = new ArrayList();
		//Bug: 2508: Jitendra to display name in alphabetically order.
		if(!recordIds.isEmpty())
		{
			Object[] whereColumn = new String[recordIds.size()];
			Iterator itr = recordIds.iterator();
			int i =0;
			while(itr.hasNext())
			{
				NameValueBean nameValueBean = (NameValueBean)itr.next();
				whereColumn[i] = nameValueBean.getValue();		
				i++;
			}					
								
			String sourceObjectName = privilegesForm.getObjectType();
			String[] selectColumnName = new String[2];
			String[] whereColumnName = {"id"};
			String[] whereColumnCondition = {"in"};
			Object[] whereColumnValue = {whereColumn};
			
			if(privilegesForm.getObjectType().equals("edu.wustl.catissuecore.domain.CollectionProtocol"))
			{
				selectColumnName[0] = "title";
				selectColumnName[1] = "id";
			}
			else
			{
				selectColumnName[0] = "name";
				selectColumnName[1] = "id";
			}	

			List list = retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, null);
			
			if (!list.isEmpty())
			{
				for(i=0;i<list.size();i++)
				{
					Object[] obj = (Object[])list.get(i);
					recordNames.add(new NameValueBean(obj[0],obj[1]));							
				}
			}		
			Collections.sort(recordNames);
		}	
		return recordNames;
	}
}