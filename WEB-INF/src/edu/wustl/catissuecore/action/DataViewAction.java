/**
 * <p>Title: DataViewAction Class>
 * <p>Description:	DataViewAction is used to show the query results data 
 * in spreadsheet or individaul view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.query.ResultData;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * DataViewAction is used to show the query results data 
 * in spreadsheet or individaul view.
 * @author gautam_shetty
 */
public class DataViewAction extends Action
{
    
    /**
     * Overrides the execute method in Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String nodeName = request.getParameter("nodeName");
        StringTokenizer str = new StringTokenizer(nodeName,":");
        String name = str.nextToken();
        int id = 0;
        
        if (!name.equals(Constants.ROOT))
            id = Integer.parseInt(str.nextToken());

        //get the type of view to show (spreadsheet/individual)
        String viewType = request.getParameter(Constants.VIEW_TYPE);
        
        if (viewType.equals(Constants.SPREADSHEET_VIEW))
        {
            List list = null;
            String[] columnList = null;

            ResultData resultData = new ResultData();
            HttpSession session = request.getSession();
            columnList = (String[]) session.getAttribute(Constants.SELECT_COLUMN_LIST);
            
            if (columnList == null)
            {
                columnList = Constants.DEFAULT_SPREADSHEET_COLUMNS;
            }
            
            list = resultData.getSpreadsheetViewData(name,id,columnList);
            request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnList);
            request.setAttribute(Constants.SPREADSHEET_DATA_LIST,list);
        }
        else
        {
            String url = null;
            if (name.equals(Constants.PARTICIPANT))
            {
                url = new String(Constants.QUERY_PARTICIPANT_SEARCH_ACTION+id);
            }
            else
            {
                if (name.equals(Constants.ACCESSION))
                {
                    url = new String(Constants.QUERY_ACCESSION_SEARCH_ACTION+id);
                }
            }
            
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request,response);
        }
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
