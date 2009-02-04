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
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.logger.Logger;

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
    	String fileName = Variables.applicationHome + System.getProperty("file.separator") + session.getId() + ".csv";
    	
    	//Extracting map from formbean which gives the serial numbers of selected rows
    	Map map = searchForm.getValues();
    	Object [] obj = map.keySet().toArray();
    	
    	//Getting column data & grid data from session
    	List columnList = (List)session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
    	List dataList = (List)session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
    	
    	//Mandar 06-Apr-06 Bugid:1165 : Extra ID columns displayed.  start
    	
    	Logger.out.debug("---------------------------------------------------------------------------------");
       	Logger.out.debug("06-apr-06 : cl size :-"+columnList.size()  );
    	Logger.out.debug(columnList); 
    	Logger.out.debug("--");
    	Logger.out.debug("06-apr-06 : dl size :-"+dataList.size()  );
    	Logger.out.debug(dataList); 
    	
    	List tmpColumnList = new ArrayList();
    	int idColCount=0;
    	// count no. of ID columns
    	for(int cnt=0;cnt<columnList.size();cnt++  )
    	{
    		String columnName = (String)columnList.get(cnt );
    		Logger.out.debug(columnName + " : " + columnName.length()    );
    		if(columnName.trim().equalsIgnoreCase("ID") )
    		{
    			idColCount++; 
    		}
    	}
    	// remove ID columns
    	for(int cnt=0;cnt<(columnList.size()-idColCount) ;cnt++  )
    	{
    		tmpColumnList.add(columnList.get(cnt)  ); 
    	}
    	
    	// datalist filtration for ID data.
    	List tmpDataList = new ArrayList();
    	for(int dataListCnt=0; dataListCnt<dataList.size(); dataListCnt++    )
    	{
    		List tmpList = (List) dataList.get(dataListCnt);
    		List tmpNewList = new ArrayList();
        	for(int cnt=0;cnt<(tmpList.size()-idColCount) ;cnt++  )
        	{
        		tmpNewList.add(tmpList.get(cnt)  ); 
        	}
        	tmpDataList.add(tmpNewList ); 
    	}
    	
    	Logger.out.debug("--");
    	Logger.out.debug("tmpcollist :" + tmpColumnList.size() ); 
    	Logger.out.debug(tmpColumnList); 
    	Logger.out.debug("--");
    	Logger.out.debug("tmpdatalist :" + tmpDataList.size() ); 
    	Logger.out.debug(tmpDataList); 

    	Logger.out.debug("---------------------------------------------------------------------------------");
    	columnList = tmpColumnList ;
    	dataList = tmpDataList ;
    	//    	Mandar 06-Apr-06 Bugid:1165 : Extra ID columns end  
    	
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
    	String delimiter = Constants.DELIMETER;
    	//Exporting the data to the given file & sending it to user
    	ExportReport report = new ExportReport(fileName);
		report.writeData(exportList,delimiter);
		report.closeFile();
    	 
    	SendFile.sendFileToClient(response,fileName,Constants.SEARCH_RESULT,"application/download");
    	
    	return null;
    }
}