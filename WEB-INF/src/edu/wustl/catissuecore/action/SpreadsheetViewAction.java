/*
 * Created on Aug 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpreadsheetViewAction extends Action
{
    
    /**
     * This method get call for simple search as well as advanced search.
     * This method also get call when user clicks on page no of Pagination tag 
     * from simple search result page as well as advanced search result page.
     * Each time it calculates the paginationList to be displayed by grid control
     * by getting pageNum from request object.
     *    
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {        
    	HttpSession session = request.getSession();
    	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
    	if (pageOf == null)
    	{
    		pageOf = (String)request.getParameter(Constants.PAGEOF);
    	}
    	if (request.getAttribute(Constants.IDENTIFIER_FIELD_INDEX) == null) {
    		String identifierFieldIndex = request.getParameter(Constants.IDENTIFIER_FIELD_INDEX);
    		if(identifierFieldIndex != null && !identifierFieldIndex.equals("")) 
    		{
    			request.setAttribute(Constants.IDENTIFIER_FIELD_INDEX, new Integer(identifierFieldIndex));
    		}    		
    	}
        Logger.out.debug("Pageof in spreadsheetviewaction.........:"+pageOf); 
        
        
        if (request.getParameter("isPaging") == null) {
        	
        	List list = (List)request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
            List columnNames = (List)request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);                  
            
            //Set the SPREADSHEET_DATA_LIST and SPREADSHEET_COLUMN_LIST in the session.
            //Next time when user clicks on pages of pagination Tag, it get the same list from the session
            //and based on current page no, it will calculate paginationDataList to be displayed by grid control.
            session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnNames);
            session.setAttribute(Constants.SPREADSHEET_DATA_LIST,list);	
            
            AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm)request.getAttribute("advanceSearchForm");
            if (advanceSearchForm != null)
            {
            	session.setAttribute("advanceSearchForm",advanceSearchForm);	
            }
        }        	
    	
        int pageNum = Constants.START_PAGE;
        List paginationDataList = null, dataList = null, columnList = null;
        
        //Get the SPREADSHEET_DATA_LIST and SPREADSHEET_COLUMN_LIST from the session.
        dataList = (List) session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
        columnList = (List) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
        
        if(request.getParameter(Constants.PAGE_NUMBER) != null) 
    	{
        	pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));       
    	}
        
        if (dataList != null) 
        {
        	//Set the start index of the list.
            int startIndex = (pageNum-1) * Constants.NUMBER_RESULTS_PER_PAGE_SEARCH;            
            //Set the end index of the list.
            int endIndex = startIndex + Constants.NUMBER_RESULTS_PER_PAGE_SEARCH;
	        if (endIndex > dataList.size())
	        {
	            endIndex = dataList.size();
	        }   	
	        //Get the paginationDataList from startIndex to endIndex of the dataList.
	        paginationDataList = dataList.subList(startIndex,endIndex);   
	        
	        //Set the total no of records in the request object to be used by pagination tag.
	        request.setAttribute(Constants.TOTAL_RESULTS,Integer.toString(dataList.size()));	        
        }
        else
        {
        	//Set the total no of records in the request object to be used by pagination tag.
        	request.setAttribute(Constants.TOTAL_RESULTS,Integer.toString(0));
        }
        
        //Set the paginationDataList in the request to be shown by grid control.
        request.setAttribute(Constants.PAGINATION_DATA_LIST,paginationDataList); 
        
        //Set the columnList in the request to be shown by grid control.
        request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnList); 
        
        //Set the current pageNum in the request to be uesd by pagination Tag.
        request.setAttribute(Constants.PAGE_NUMBER,Integer.toString(pageNum));
        
        //Set the result per page attribute in the request to be uesd by pagination Tag.
        request.setAttribute(Constants.RESULTS_PER_PAGE,Integer.toString(Constants.NUMBER_RESULTS_PER_PAGE_SEARCH));
       
        request.setAttribute(Constants.PAGEOF, pageOf);
        return mapping.findForward(pageOf);
    }

}
