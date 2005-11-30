package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

public class ConfigureSimpleQueryAction extends BaseAction
{
	/**
	 * This is the initialization action class for configuring Simple Search
	 * @author Poornima Govindrao
	 *  
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		//Set the tables for the configuration 
		String pageOf=request.getParameter(Constants.PAGEOF);
		if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
		{
			SimpleQueryInterfaceForm simpleQueryInterfaceForm =  (SimpleQueryInterfaceForm)form;
			HttpSession session =request.getSession();
			Map map = simpleQueryInterfaceForm.getValuesMap();
			Logger.out.debug("Form map size"+map.size());
			Logger.out.debug("Form map"+map); 
			if(map.size()==0)
			{
				map=(Map)session.getAttribute(Constants.SIMPLE_QUERY_MAP);
				Logger.out.debug("Session map size"+map.size());
				Logger.out.debug("Session map"+map);
			}
			Iterator iterator = map.keySet().iterator();
			
			//Retrieve the size of the condition list to set size of array of tables.
			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
			Collection simpleConditionNodeCollection = parser.generateData(map, true);
			int counter = simpleConditionNodeCollection.size();
			String[] selectedTables = new String[counter]; 
			int tableCount=0;
			while (iterator.hasNext())
			{
				String key = (String)iterator.next();
				Logger.out.debug("map key"+key);
				if(key.endsWith("_table"))
				{
					String table = (String)map.get(key);
					boolean exists = false;
					for(int arrayCount=0;arrayCount<selectedTables.length;arrayCount++)
					{
						if(selectedTables[arrayCount]!=null)
						{
							if(selectedTables[arrayCount].equals(table))
								exists = true;
						}
					}
					if(!exists)
					{
						selectedTables[tableCount]= table;
						tableCount++;
					}
				}
			}
			session.setAttribute(Constants.TABLE_ALIAS_NAME,selectedTables);
			session.setAttribute(Constants.SIMPLE_QUERY_MAP,map);
		}
		
		request.setAttribute(Constants.PAGEOF,pageOf);
		
		return (mapping.findForward(pageOf));
	}
	
}
