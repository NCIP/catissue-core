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
    		SimpleQueryInterfaceForm sForm =  (SimpleQueryInterfaceForm)form;
    		Map map = sForm.getValuesMap();
    		Iterator iterator = map.keySet().iterator();
    		int size = Integer.parseInt(sForm.getCounter());
    		String[] selectedTables = new String[size]; 
    		int tableCount=0;
    		while (iterator.hasNext())
    		{
    			String key = (String)iterator.next();
    			if(key.endsWith("_table"))
    			{
    				selectedTables[tableCount]= (String)map.get(key);
    				tableCount++;
    			}
    			
    		}
    		//HttpSession session =request.getSession();
    		request.setAttribute(Constants.TABLE_ALIAS_NAME,selectedTables);
    		
		}
		catch (Exception Exp)
		{
			Logger.out.debug(Exp.getMessage(), Exp);
		}
    	
    	return (mapping.findForward("success"));
    }
}
    