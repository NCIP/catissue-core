package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;




public class ConfigureSimpleQueryAction extends Action
{

	/**
	 * This is the initialization action class for configuring Simple Search
	 * @author Poornima Govindrao
	 *  
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	try
		{
    		//Set the tables for the configuration 
    		SimpleQueryInterfaceForm simpleQueryInterfaceForm =  (SimpleQueryInterfaceForm)form;
    		HttpSession session =request.getSession();
    		Map map = simpleQueryInterfaceForm.getValuesMap();
    		Logger.out.debug("map size"+map.size());
    		if(map.size()==0)
    		{
    			map=(Map)session.getAttribute(Constants.SIMPLE_QUERY_MAP);
    			Logger.out.debug("map size"+map.size());
    		}
    		Iterator iterator = map.keySet().iterator();
    		String counter = simpleQueryInterfaceForm.getCounter();
    		if(counter==null)
    			counter = (String)session.getAttribute(Constants.SIMPLE_QUERY_COUNTER);
    		int size = Integer.parseInt(counter);
    		String[] selectedTables = new String[size+1]; 
    		int tableCount=0;
    		while (iterator.hasNext())
    		{
    			String key = (String)iterator.next();
    			Logger.out.debug("map key"+key);
    			if(key.endsWith("_table"))
    			{
    				selectedTables[tableCount]= (String)map.get(key);
    				tableCount++;
    			}
    		}
    		
    		session.setAttribute(Constants.TABLE_ALIAS_NAME,selectedTables);
    		request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_SIMPLE_QUERY_INTERFACE);
    		session.setAttribute(Constants.SIMPLE_QUERY_MAP,map);
    		session.setAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME,simpleQueryInterfaceForm.getAliasName());
    		session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,simpleQueryInterfaceForm.getCounter());
		}
		catch (Exception Exp)
		{
			Logger.out.debug(Exp.getMessage(), Exp);
		}
    	
    	return (mapping.findForward("success"));
    }

}
    