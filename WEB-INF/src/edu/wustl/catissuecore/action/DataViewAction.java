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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.ResultData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
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
    	AdvanceSearchForm advForm = (AdvanceSearchForm)form;
    	String[] selectedColumns = advForm.getSelectedColumnNames();
    	HttpSession session = request.getSession();
    	String target = Constants.SUCCESS;
    	String nodeName = request.getParameter("nodeName");
    	if(nodeName==null)
    		nodeName = (String)session.getAttribute(Constants.SELECTED_NODE);
    		
        StringTokenizer str = new StringTokenizer(nodeName,":");
        String name = str.nextToken().trim();
        
        String id = new String();
        String parentName = new String();
    	String parentId = new String();
    	
    	//Get the listData, column display names and select column names if it is configured and set in session
    	List filteredColumnDisplayNames=(List)session.getAttribute(Constants.CONFIGURED_COLUMN_DISPLAY_NAMES);
    	Logger.out.debug("ColumnDisplayNames from configuration"+filteredColumnDisplayNames);
    	String[] columnList= (String[])session.getAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST);
    	
    	
    	//Retrieve the columnIdsMap from session
    	Map columnIdsMap = (Map)session.getAttribute(Constants.COLUMN_ID_MAP);
    	
    	//Set default select column list and column display names in case of Root and Participant nodes are selected
    	if (name.equals(Constants.ROOT)|| name.equals(Constants.PARTICIPANT))
        {
    		
    		if(filteredColumnDisplayNames==null)
    			filteredColumnDisplayNames = (List)session.getAttribute(Constants.COLUMN_DISPLAY_NAMES);
    		if(columnList==null)
    		{
    			columnList=new String[filteredColumnDisplayNames.size()];
    			for(int count=0;count<filteredColumnDisplayNames.size();count++)
    			{
    				columnList[count]=Constants.COLUMN+count;
    			}
    		}
    		
        }
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
        if(viewType==null)
        	viewType=Constants.SPREADSHEET_VIEW;
        if (viewType.equals(Constants.SPREADSHEET_VIEW))
        {
    		
        	if (!name.equals(Constants.ROOT))
            {	
                if (!name.equals(Query.PARTICIPANT) && columnList==null)
                {
                	filteredColumnDisplayNames=new ArrayList();
                	columnList = getColumnNamesForFilter(name,filteredColumnDisplayNames,columnIdsMap);
                }
                String key = name+"."+Constants.IDENTIFIER;
                int columnId = ((Integer)columnIdsMap.get(name+"."+Constants.IDENTIFIER)).intValue()-1;
                name=Constants.COLUMN+columnId;
                if(!parentName.equals(""))
                {
                    key = parentName+"."+Constants.IDENTIFIER;
                    columnId = ((Integer)columnIdsMap.get(parentName+"."+Constants.IDENTIFIER)).intValue()-1;
                    parentName=Constants.COLUMN+columnId;
                }
            }
    		//Add specimen identifier column if it is not there,required for shopping cart action
            int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.IDENTIFIER)).intValue()-1;
            String specimenColumn = Constants.COLUMN+specimenColumnId;
            boolean exists = false;
            if(columnList!=null)
            {
            	for(int i=0;i<columnList.length;i++)
            	{
            		if(columnList[i].equals(specimenColumn))
            		{
            			exists=true;
            		}
            	}
                if(!exists)
                {
                	String columnListWithoutSpecimenId[] = columnList;
                	columnList = new String[columnListWithoutSpecimenId.length+1];
                    for(int i=0;i<columnListWithoutSpecimenId.length;i++)
                    {
                    	columnList[i] = columnListWithoutSpecimenId[i];
                    }
                    columnList[columnListWithoutSpecimenId.length]=specimenColumn;
                	//filteredColumnDisplayNames.add("Identifier");
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

            	whereColumnName[1]=parentName;
            	whereColumnValue[1]=parentId;
            	whereColumnCondition[1]="=";
            }
            whereColumnName[0]=name;
            whereColumnValue[0] =id;
            whereColumnCondition[0]="=";

        	List list = null;
            ResultData resultData = new ResultData();
            list = resultData.getSpreadsheetViewData(whereColumnName,whereColumnValue,whereColumnCondition,columnList, getSessionData(request), Constants.OBJECT_LEVEL_SECURE_RETRIEVE);
     		// If the result contains no data, show error message.
    		if (list.isEmpty()) 
    		{
    			ActionErrors errors = new ActionErrors();
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("advanceQuery.noRecordsFound"));
    			saveErrors(request, errors);
    			request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_QUERY_RESULTS);
    			//target = Constants.FAILURE;
    		}
    		else
    		{

    			if(selectedColumns==null)
    			{
    				Vector tablesVector = new Vector();
    				tablesVector.add(Query.PARTICIPANT);
    				tablesVector.add(Query.COLLECTION_PROTOCOL);
    				tablesVector.add(Query.COLLECTION_PROTOCOL_REGISTRATION);
    				tablesVector.add(Query.SPECIMEN_COLLECTION_GROUP);
    				tablesVector.add(Query.SPECIMEN);

    				List selectedColumnNames = new ArrayList();
    				QueryBizLogic queryBizLogic = (QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
    				Iterator tableVectorItr = tablesVector.iterator();
    				while(tableVectorItr.hasNext())
    				{
    					String table = (String)tableVectorItr.next();
    					selectedColumnNames.addAll(queryBizLogic.setColumnNames(table));
    				}
    				selectedColumns = new String[selectedColumnNames.size()];
    				Iterator columnNameItr = selectedColumnNames.iterator();
    				int i=0;
    				while(columnNameItr.hasNext())
    				{
    					selectedColumns[i]=((NameValueBean)columnNameItr.next()).getValue();
    					i++;
    				}
    				advForm.setSelectedColumnNames(selectedColumns);
    				Logger.out.debug("size of the configure default columns after setting:"+selectedColumns.length);
					
    			}

    			//if specimen id is added to the columns then add display name identifier to the filteredColumnDisplayNames
    	        /*if(columnList!=null)
    	        {
    	        	if(columnList.length-filteredColumnDisplayNames.size()==1)
    	        		filteredColumnDisplayNames.add("Identifier");
    	        }*/
    			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,filteredColumnDisplayNames);
   				request.setAttribute(Constants.SPREADSHEET_DATA_LIST,list);
    			request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_QUERY_RESULTS);
        		session.setAttribute(Constants.SELECT_COLUMN_LIST,columnList);
    			session.setAttribute(Constants.SELECTED_NODE,nodeName);
    		}
        }
        else
        {
            String url = null;
            Logger.out.debug("selected node name in object view:"+name+"object");
            
//       	 Aarti: Check whether user has use permission to update this object
    		// or not
            if(!SecurityManager.getInstance(this.getClass()).isAuthorized(getUserLoginName(request)
            		,Constants.PACKAGE_DOMAIN+"."+name,Permissions.UPDATE))
    		{
            	ActionErrors errors = new ActionErrors();
             	ActionError error = new ActionError("access.edit.object.denied",getUserLoginName(request),Constants.PACKAGE_DOMAIN+"."+name
             	        				);
             	errors.add(ActionErrors.GLOBAL_ERROR,error);
             	saveErrors(request,errors);
            	return mapping.findForward(Constants.FAILURE);
    		}
            
            if (name.equals(Constants.PARTICIPANT))
            {
                url = new String(Constants.QUERY_PARTICIPANT_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
                														Constants.PAGEOF_PARTICIPANT_QUERY_EDIT);
            }
            else if (name.equals(Constants.COLLECTION_PROTOCOL))
            {
            	url = new String(Constants.QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_COLLECTION_PROTOCOL_QUERY_EDIT);
            			
            }
            else if (name.equals(Constants.SPECIMEN_COLLECTION_GROUP))
            {
            	url = new String(Constants.QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_SPECIMEN_COLLECTION_GROUP_QUERY_EDIT);
            			
            }
            else if (name.equals(Constants.SPECIMEN))
            {
            	url = new String(Constants.QUERY_SPECIMEN_SEARCH_ACTION+id+"&"+Constants.PAGEOF+"="+
            															Constants.PAGEOF_SPECIMEN_QUERY_EDIT);
            			
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request,response);
        }
        return mapping.findForward(target);
    }

    //returns the filtered select column list to be shown when clicked on a node of the results tree
    private String[] getColumnNamesForFilter(String aliasName,List filteredColumnDisplayNames,Map columnIdsMap) throws DAOException,ClassNotFoundException
    {
    	List columnDisplayNames=new ArrayList();
    	QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
    	List columns =new ArrayList();
    	//Filter the data according to the node clicked. Show only the data lower in the heirarchy 
    	if(aliasName.equals(Query.COLLECTION_PROTOCOL))
		{
    		columns.addAll(bizLogic.setColumnNames(Query.COLLECTION_PROTOCOL));
    		columns.addAll(bizLogic.setColumnNames(Query.COLLECTION_PROTOCOL_REGISTRATION));
    		columns.addAll(bizLogic.setColumnNames(Query.SPECIMEN_COLLECTION_GROUP));
    		columns.addAll(bizLogic.setColumnNames(Query.SPECIMEN));
		}
		else if(aliasName.equals(Query.SPECIMEN_COLLECTION_GROUP))
		{
    		columns.addAll(bizLogic.setColumnNames(Query.SPECIMEN_COLLECTION_GROUP));
    		columns.addAll(bizLogic.setColumnNames(Query.SPECIMEN));
		}
		else if(aliasName.equals(Query.SPECIMEN))
		{
    		columns.addAll(bizLogic.setColumnNames(Query.SPECIMEN));
		}
		String selectColumnList[] = new String[columns.size()];
    	
    	Iterator columnsItr = columns.iterator();
    	int i=0;
    	while(columnsItr.hasNext())
    	{
    		NameValueBean columnsNameValues = (NameValueBean)columnsItr.next();
    		StringTokenizer columnsTokens = new StringTokenizer(columnsNameValues.getValue(),".");
    		Logger.out.debug("value in namevaluebean:"+columnsNameValues.getValue());
    		int columnId = ((Integer)columnIdsMap.get(columnsTokens.nextToken()+"."+columnsTokens.nextToken())).intValue()-1;
    		selectColumnList[i++]=(Constants.COLUMN+columnId);
    		columnDisplayNames.add(columnsTokens.nextToken());
    	}
    	filteredColumnDisplayNames.addAll(columnDisplayNames);
    	return selectColumnList;
    }

}