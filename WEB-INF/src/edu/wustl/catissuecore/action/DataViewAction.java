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
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.query.ResultData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * DataViewAction is used to show the query results data 
 * in spreadsheet or individaul view.
 * @author gautam_shetty
 */
public class DataViewAction extends BaseAction
{
    
    /**
     * Overrides the execute method in Action class.
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String target = Constants.SUCCESS;
    	String nodeName = request.getParameter("nodeName");
    	Logger.out.debug("nodename of selected node"+nodeName);
        StringTokenizer str = new StringTokenizer(nodeName,":");
        String name = str.nextToken().trim();
        HttpSession session = request.getSession();
        String id = new String();
        String parentName = new String();
    	String parentId = new String();
        
		//Get column display names from session which is set in session in AdvanceSearchResultsAction
    	List columnDisplayNames = (List)session.getAttribute(Constants.COLUMN_DISPLAY_NAMES);
    	/*List filteredColumnDisplayNames = new ArrayList();
        
    	if (name.equals(Constants.ROOT)|| name.equals(Constants.PARTICIPANT))
        {
    		filteredColumnDisplayNames = columnDisplayNames;
        }*/
        if (!name.equals(Constants.ROOT))
        {
        	id = str.nextToken();
        }
        /*Incase of collection protocol selected, the whereCondition should contain the participant conditions also
         * as Collection Protocol and Participant have many to many relationship
         */ 
        if(str.hasMoreTokens())
        {
        	parentName = str.nextToken();
        	parentId = str.nextToken();
        }

        //get the type of view to show (spreadsheet/individual)
        String viewType = request.getParameter(Constants.VIEW_TYPE);
        
        if (viewType.equals(Constants.SPREADSHEET_VIEW))
        {
        	String[] columnList= null;
        	
        	if (!name.equals(Constants.ROOT))
            {	
        		Map columnIdsMap = (Map)session.getAttribute(Constants.COLUMN_ID_MAP);
                /*if (!name.equals(Constants.PARTICIPANT))
                {
                	columnList = getColumnNames(name,columnIdsMap,columnDisplayNames,filteredColumnDisplayNames);
                }*/
                
                Logger.out.debug("alias name of selected node in adv tree:"+name);
                Logger.out.debug("column ids map in data view action"+columnIdsMap);
                String key = name+"."+Constants.IDENTIFIER;
                int columnId = ((Integer)columnIdsMap.get(name+"."+Constants.IDENTIFIER)).intValue()-1;
                Logger.out.debug("columnid of selected node:"+columnId+"in the map for key:"+key);
                name=Constants.COLUMN+columnId;
                Logger.out.debug("Column name of the selected column in the tree:"+name);
                if(!parentName.equals(""))
                {
                    key = parentName+"."+Constants.IDENTIFIER;
                    columnId = ((Integer)columnIdsMap.get(parentName+"."+Constants.IDENTIFIER)).intValue()-1;
                    parentName=Constants.COLUMN+columnId;

                }
            }
        	String[] whereColumnName= new String[1]; 
        	
            String[] whereColumnValue = new String[1];
            
            String[] whereColumnCondition = new String[1];

            if(!parentName.equals(""))
            {
            	whereColumnName=new String[2];
            	whereColumnValue = new String[2];
            	whereColumnCondition = new String[2];

            	Logger.out.debug("parentname & id for coll prot"+parentName+"&"+parentId);
            	whereColumnName[1]=parentName;
            	whereColumnValue[1]=parentId;
            	whereColumnCondition[1]="=";
            }
            whereColumnName[0]=name;
            whereColumnValue[0] =id;
            whereColumnCondition[0]="=";

        	List list = null;
            
            //String[] columnList = {"Participant1_IDENTIFIER","CollectionProtocol1_IDENTIFIER","SpecimenCollectionGroup1_IDENTIFIER","Specimen1_IDENTIFIER"};
            

            ResultData resultData = new ResultData();
            
            //columnList = (String[]) session.getAttribute(Constants.SELECT_COLUMN_LIST);
            
            /*if (columnList == null)
            {
                columnList = Constants.DEFAULT_SPREADSHEET_COLUMNS;
            }*/
            
            list = resultData.getSpreadsheetViewData(whereColumnName,whereColumnValue,whereColumnCondition,columnList, getSessionData(request), Constants.OBJECT_LEVEL_SECURE_RETRIEVE);
            Logger.out.debug("list of data after advance search"+list);
     		// If the result contains no data, show error message.
    		if (list.isEmpty()) 
    		{
    			Logger.out.debug("inside if condition for empty list");
    			ActionErrors errors = new ActionErrors();
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("advanceQuery.noRecordsFound"));
    			saveErrors(request, errors);
    			request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_QUERY_RESULTS);
    			
    			//target = Constants.FAILURE;
    		}
    		else
    		{
    			
            	//Created temporary column display names for the spreadsheet view
            	/*List rowList = (List)list.get(0);
    			int columnSize=rowList.size();
    			for(int i=0;i<columnSize;i++)
    				columnDisplayNames.add("Column"+i);
            	Map columnIdsMap = (Map)session.getAttribute(Constants.COLUMN_ID_MAP);
            	Set keySet = columnIdsMap.keySet();
            	//Split the keys which is in the form of aliasName.columnNames to get the column Names 
            	Iterator keySetitr = keySet.iterator();
            	List columnDisplayNames = new ArrayList(keySet);*/
    			
    			//request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,filteredColumnDisplayNames);
    			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnDisplayNames);
    			request.setAttribute(Constants.SPREADSHEET_DATA_LIST,list);
    			request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_QUERY_RESULTS);
    		}
        }
        
        else
        {
            String url = null;
            Logger.out.debug("selected node name in object view:"+name+"object");
            if (name.equals(Constants.PARTICIPANT))
            {
            	Logger.out.debug("indside participant object view");
                url = new String(Constants.QUERY_PARTICIPANT_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
                														Constants.PAGEOF_PARTICIPANT_QUERY);
            }
            else if (name.equals(Constants.COLLECTION_PROTOCOL))
            {
            	url = new String(Constants.QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_COLLECTION_PROTOCOL_QUERY);
            			
            }
            else if (name.equals(Constants.SPECIMEN_COLLECTION_GROUP))
            {
            	url = new String(Constants.QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_SPECIMEN_COLLECTION_GROUP_QUERY);
            			
            }
            else if (name.equals(Constants.SPECIMEN))
            {
            	url = new String(Constants.QUERY_SPECIMEN_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_SPECIMEN_QUERY);
            			
            }
            //request.setAttribute(Constants.OPERATION,Constants.VIEW);
            Logger.out.debug("url of object view:"+url);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request,response);
            
        }
        
        return mapping.findForward(target);
    }
    //Function for filtering column names for Spreadsheet view for individual objects.
   /* private String[] getColumnNames(String name,Map columnIdsMap,List columnDisplayNames,List filteredColumnDisplayNames)
    {
    	String[] columnNames = new String[columnIdsMap.size()];
    	Set columnIdsKeySet = columnIdsMap.keySet();
    	Iterator columnKeysItr = columnIdsKeySet.iterator();
    	Logger.out.debug("size of columnids map:"+columnIdsMap.size());
    	Logger.out.debug("size of column display names list:"+columnDisplayNames.size());
    	//Logger.out.debug("size of columnids map:"+columnIdsMap.size());
    	//List filteredColumnDisplayNames = new ArrayList();
    	int i=0;
    	while(columnKeysItr.hasNext())
    	{
    		String key = (String)columnKeysItr.next();
    		Logger.out.debug("key in map for column list:"+key);
    		Logger.out.debug("value in map for column list:"+columnIdsMap.get(key));
    		if(name.equals(Constants.COLLECTION_PROTOCOL))
    		{
    			if(!key.startsWith(Constants.PARTICIPANT))
    			{
    				int columnId = ((Integer)columnIdsMap.get(key)).intValue()-1;
    				Logger.out.debug("column id inside if condition"+columnId);
    				columnNames[i++] = Constants.COLUMN+columnId;
    				filteredColumnDisplayNames.add(columnDisplayNames.get(columnId));
    			}
    		}
    		else if(name.equals(Constants.SPECIMEN_COLLECTION_GROUP))
    		{
    			if(!key.startsWith(Constants.PARTICIPANT) && !key.startsWith(Constants.COLLECTION_PROTOCOL))
    			{
    				int columnId = ((Integer)columnIdsMap.get(key)).intValue()-1;
    				columnNames[i++] = Constants.COLUMN+columnId;
    				filteredColumnDisplayNames.add(columnDisplayNames.get(columnId));
    			}
    		}
    		else if(name.equals(Constants.SPECIMEN))
    		{
    			if(key.startsWith(Constants.SPECIMEN))
    			{
    				int columnId = ((Integer)columnIdsMap.get(key)).intValue()-1;
    				columnNames[i++] = Constants.COLUMN+columnId;
    				filteredColumnDisplayNames.add(columnDisplayNames.get(columnId));
    			}
    		}
    	}
    	return columnNames;
    }*/

}
