/**
 * <p>Title: SpreadsheetExportAction Class>
 * <p>Description:	This class exports the data of a spreadsheet to a file.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 24, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;

/**
 * @author aniruddha_phadnis
 */
public class SpreadsheetExportAction  extends BaseAction
{
    /**
     * This class exports the data of a spreadsheet to a file.
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	AdvanceSearchForm searchForm = (AdvanceSearchForm)form;
    	HttpSession session = request.getSession();
    	String fileName = Variables.catissueHome + System.getProperty("file.separator") + session.getId() + ".tsv";
    	
    	//Extracting map from formbean which gives the serial numbers of selected rows
    	Map map = searchForm.getValues();
    	Object [] obj = map.keySet().toArray();
    	
    	//Getting column data & grid data from session
    	List columnList = (List)session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
    	List dataList = (List)session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
    	
    	List exportList = new ArrayList();
    	
    	//Adding first row(column names) to exportData
    	exportList.add(columnList);
    	
    	//Adding the selected rows to exportData
    	for(int i=0;i<obj.length;i++)
    	{
    		int indexOf = obj[i].toString().indexOf("_") + 1;
    		int index = Integer.parseInt(obj[i].toString().substring(indexOf));
    		exportList.add((List)dataList.get(index));
    	}
    	String delimiter = Constants.TAB_DELIMETER;
    	//Exporting the data to the given file & sending it to user
    	ExportReport report = new ExportReport(fileName);
		report.writeData(exportList,delimiter);
		report.closeFile();
    	 
    	SendFile.sendFileToClient(response,fileName,Constants.SEARCH_RESULT,"application/download");
    	
    	return null;
    }
}