package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.common.util.logger.Logger;




public class ConfigureSimpleSearchAction extends Action
{

	/**
	 * This is the action class to set view elements for configuring Simple Search
	 * @author Poornima Govindrao
	 *  
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	
    		//Set the tables for the configuration 
    		ConfigureResultViewForm cForm =  (ConfigureResultViewForm)form;
    		String[] selectedColumns = cForm.getSelectedColumnNames();
    		Vector vector = setViewElements(selectedColumns); 
    		//HttpSession session = request.getSession();
    		request.setAttribute("resultViewVector",vector);
    	return (mapping.findForward("success"));
    }
    private Vector setViewElements(String []selectedColumnsList) 
	{
    	/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames 
    	 * and set the dataelement object.
    	 */
	    Vector vector = new Vector();
	    for(int i=0;i<selectedColumnsList.length;i++)
	    {
	    	StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
	    	DataElement dataElement = new DataElement();
	    	while (st.hasMoreTokens())
			{
	    		dataElement.setTable(st.nextToken());
	    		dataElement.setField(st.nextToken());
	    		Logger.out.debug(st.nextToken());
	    	}
	        vector.add(dataElement);
	    }
		return vector;
	}
}
    