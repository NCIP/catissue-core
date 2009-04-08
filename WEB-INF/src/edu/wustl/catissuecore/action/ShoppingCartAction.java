/**
 * <p>Title: ShoppingCartAction Class>
 * <p>Description:	This class initializes the fields of ShoppingCart.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import edu.wustl.catissuecore.bizlogic.ShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.query.ShoppingCart;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.logger.Logger;

public class ShoppingCartAction  extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in ShoppingCart.jsp Page.
     * */
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    { 
    	 HttpSession session = request.getSession(true);
        //Gets the value of the operation parameter.
        String operation = (String)request.getParameter(Constants.OPERATION);
        String pageNo = (String)request.getParameter(Constants.PAGE_NUMBER);
        String recordsPerPageStr = (String)session.getAttribute(Constants.RESULTS_PER_PAGE);//Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
        String pageOff = (String)request.getParameter(Constants.PAGE_OF);
        List paginationDataList = null;
        if(pageNo != null)
        {
        	request.setAttribute(Constants.PAGE_NUMBER,pageNo);
        }
        String target = Constants.SUCCESS;
       
        ShoppingCart cart = (ShoppingCart)session.getAttribute(Constants.SHOPPING_CART);
        ShoppingCartBizLogic bizLogic = (ShoppingCartBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SHOPPING_CART_FORM_ID);
        //ShoppingCartForm shopForm = (ShoppingCartForm)form;
        AdvanceSearchForm advForm = (AdvanceSearchForm)form;
    	String isCheckAllAcrossAllChecked = (String)request.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);
        if(cart == null)
        {
        	cart = new ShoppingCart();
        }
        
        if(operation == null)
        {
        	/*List specimenList = bizLogic.retrieve(Specimen.class.getName());
        	Iterator it = specimenList.iterator();
        	
        	while(it.hasNext())
        	{
        		Specimen specimen = (Specimen)it.next();
        		cart.add(specimen);
        	}
        	
        	session.setAttribute(Constants.SHOPPING_CART,cart);*/
        	
        	request.setAttribute(Constants.SPREADSHEET_DATA_LIST,makeGridData(cart));        		
        }
        else
        { 
        	session.setAttribute("OrderForm","true");
        	if(operation.equals(Constants.ADD)) //IF OPERATION IS "ADD"
	        { 
        		//Get the checkbox map values
        		Map map = advForm.getValues();
	        	Logger.out.debug("map of shopping form:"+map);
	        	Object obj[] = map.keySet().toArray();
	        	
	        	if(pageOff !=null && pageOff.equals(Constants.PAGE_OF_QUERY_MODULE))
	        	{ 
	        		List spreadsheetColumns = (List)session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	        		System.out.println("");
	        	}
	        	//Get the column Ids from session
        		Map columnIdsMap = (Map)session.getAttribute(Constants.COLUMN_ID_MAP);
        		
        		//Get the select column List from session to get the specimen data
        		String[] selectedColumns = (String[])session.getAttribute(Constants.SELECT_COLUMN_LIST);
        		
    			/**
    			 * Name: Deepti
    			 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
    			 * object of class QuerySessionData will be saved in session, which will contain the required information for query execution while navigating through query result pages.
    			 * 
    			 *  Here, as results are not stored in session, the sql is executed again to form the shopping cart list.  
    			 */
        		int recordsPerPage = new Integer(recordsPerPageStr);
        		int pageNum = new Integer(pageNo);	        	
        		QuerySessionData querySessionData = (QuerySessionData)session.getAttribute(edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);
        		if(isCheckAllAcrossAllChecked != null && isCheckAllAcrossAllChecked.equals("true"))
        		{
        			Integer totalRecords = (Integer)session.getAttribute(Constants.TOTAL_RESULTS);
    				recordsPerPage = totalRecords;
    				pageNum = 1;
        		}
        		paginationDataList = AppUtility.getPaginationDataList(request, getSessionData(request), recordsPerPage, pageNum, querySessionData);
	        
	        	request.setAttribute(Constants.PAGINATION_DATA_LIST,paginationDataList);
        		
        		Logger.out.debug("column ids map in shopping cart"+columnIdsMap);
        		
        		//get the specimen column id from the map
        		int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.IDENTIFIER)).intValue()-1;
        		Logger.out.debug("specimen column id in shopping cart"+specimenColumnId);
        		int spreadsheetSpecimenIndex = -1;
        		//get the column in which the specimen column id is displayed in the spreadsheet data
        		for(int k=0;k<selectedColumns.length;k++)
        		{
        			if(selectedColumns[k].equals(Constants.COLUMN+specimenColumnId))
        			{
        				spreadsheetSpecimenIndex=k;
        				break;
        			}
        		}
        		Object []selectedSpecimenIds = null;
        		boolean isError = false;
                //Bug#2003: For having unique records in result view
        		if(spreadsheetSpecimenIndex == -1)
        		{
        			ActionErrors errors = new ActionErrors();
                	ActionError error = new ActionError("error.specimenId.add");
                	errors.add(ActionErrors.GLOBAL_ERROR,error);
                	saveErrors(request,errors);
                    Logger.out.error("Specimen Id column not selected");
                    target = new String(Constants.DUPLICATE_SPECIMEN);
                    isError = true;
        		}
        		else if(isCheckAllAcrossAllChecked != null && isCheckAllAcrossAllChecked.equals("true"))
             	{
        			int listSize = paginationDataList.size();
        			selectedSpecimenIds = new Object[listSize];
        			for(int index=0;index<listSize;index++)
        			{
	        			List selectedRow = (List)paginationDataList.get(index);
			        	Logger.out.debug("index selected :"+index);
			        	selectedSpecimenIds[index]=selectedRow.get(spreadsheetSpecimenIndex);
			        	Logger.out.debug("specimen id to be added to cart :"+selectedSpecimenIds[index]);
        			}
        			System.out.println("selectedSpecimenIds  "+selectedSpecimenIds);
             	}
        		else
        		{
				//Add to cart the selected specified Ids.  
				selectedSpecimenIds = new Object[obj.length];
				for(int j=0;j<obj.length;j++)
				{
					String str = obj[j].toString();
		        	StringTokenizer strTokens = new StringTokenizer(str,"_");
		        	strTokens.nextToken();
		        	int index = Integer.parseInt(strTokens.nextToken());
		        	List selectedRow = (List)paginationDataList.get(index);
		        	Logger.out.debug("index selected :"+index);
		        	selectedSpecimenIds[j]=selectedRow.get(spreadsheetSpecimenIndex);
		        	Logger.out.debug("specimen id to be added to cart :"+selectedSpecimenIds[j]);
				}	
        		//Mandar 27-Apr-06 : bug 1129
        		}
				try
				{
        			bizLogic.add(cart,selectedSpecimenIds);
				}
        		catch(BizLogicException bizEx)
				{
                	ActionErrors errors = new ActionErrors();
                	ActionError error = new ActionError("shoppingcart.error",bizEx.getMessage());
                	errors.add(ActionErrors.GLOBAL_ERROR,error);
                	saveErrors(request,errors);
                    Logger.out.error(bizEx.getMessage(), bizEx);
                    target = new String(Constants.DUPLICATE_SPECIMEN);
                    isError = true;
				}
        		session.setAttribute(Constants.SHOPPING_CART,cart);
       			//List dataList = (List) session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
        		List columnList = (List)session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
        		request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnList);
        		request.setAttribute(Constants.PAGE_OF,Constants.PAGE_OF_QUERY_RESULTS);
        		if(!isError )
        		{
        			target=Constants.SHOPPING_CART_ADD;
        		}
	        }
	        else if(operation.equals(Constants.DELETE)) //IF OPERATION IS "DELETE"
	        {
	        	
	        	//Extracting map from formbean that gives rows to be deleted
	        	Map map = advForm.getValues();
	        	Logger.out.debug("map of shopping form:"+map);
	        	Object obj[] = map.keySet().toArray();
	        	Logger.out.debug("cart in shopping cart action "+cart.getCart());
	        	/*Deleting the selected rows from Shopping Cart object & setting
	        	 *it again in the session 
	        	 */
	        	session.setAttribute(Constants.SHOPPING_CART,bizLogic.delete(cart,obj));
	        }
	        else if(operation.equals(Constants.EXPORT)) //IF OPERATION IS "EXPORT"
	        {
	        	String fileName = Variables.applicationHome + System.getProperty("file.separator") + session.getId() + ".csv";
	        	
	        	//Extracting map from formbean that gives rows to be exported
	        	Map map = advForm.getValues();
	        	Object obj[] = map.keySet().toArray();
	        	
	        	List cartList = bizLogic.export(cart,obj,fileName);
	        	String delimiter = Constants.DELIMETER;
	        	//Exporting the data to shopping cart file & sending it to user
	        	ExportReport report = new ExportReport(fileName);
	    		report.writeData(cartList,delimiter);
	    		report.closeFile();
	        	 
	        	SendFile.sendFileToClient(response,fileName,"ShoppingCart.csv","application/download");
	        	
	        	String path = "/" + fileName;
	        	return new ActionForward(path);
	        }
	        
	        else if(operation.equals("addToOrderList"))
	        {
	        	addToOrderLiist(advForm,request,cart,session);
				target = new String("requestToOrder");
	        }
        	request.setAttribute(Constants.SPREADSHEET_DATA_LIST,makeGridData(cart));
        }
        //Sets the operation attribute to be used in the Add/Edit Shopping Cart Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        
    	request.setAttribute(Constants.MENU_SELECTED,new String("18") );
    	Logger.out.debug(Constants.MENU_SELECTED + " set in ShoppingCart Action : 18  -- "  ); 

        
    	if(advForm.getValues().size()!=0)
    	{
	    	if(session.getAttribute("OrderForm")==null) 
	    	{
	    		ActionErrors errors = new ActionErrors();
	        	ActionError error = new ActionError("errors.order.alreadygiven");
	        	errors.add(ActionErrors.GLOBAL_ERROR,error);
	        	saveErrors(request,errors);
	    	}
	    	else
	    	{
		       	if(session.getAttribute("RequestedBioSpecimens") != null)
		    		session.removeAttribute("RequestedBioSpecimens");
		    	
		    	if(session.getAttribute("OrderForm") != null)
		    		session.removeAttribute("OrderForm");
		    	
		    	if(session.getAttribute("DefineArrayFormObjects")!=null)
		    		session.removeAttribute("DefineArrayFormObjects");
	    	}
	    	request.setAttribute(Constants.IS_SPECIMENID_PRESENT, "true");
    	}
        return mapping.findForward(target);
    }

    //This function prepares the data in Grid Format
    private List makeGridData(ShoppingCart cart)
    {	
		List gridData = new ArrayList(); 
			
    	if(cart != null)
		{
			Hashtable cartTable = cart.getCart();
			
			if(cartTable != null && cartTable.size() != 0)
			{				
				Enumeration cartIterator = cartTable.keys();
				int id = 0;
				
				while(cartIterator.hasMoreElements())
				{
					String key = (String)cartIterator.nextElement();
					Specimen specimen = (Specimen)cartTable.get(key);
					
					List rowData = new ArrayList();
					
					//Adding checkbox as a first column of the grid
				//	rowData.add("<input type='checkbox' name='value(CB_" + specimen.getId() + ")' id='" + id + "' onClick='changeData(this)'>");
					rowData.add(String.valueOf(specimen.getId())); // for check box value
					rowData.add(String.valueOf(specimen.getId()));
					rowData.add(specimen.getClassName());
					
					if(specimen.getSpecimenType() != null)
						rowData.add(specimen.getSpecimenType());
					else
						rowData.add("");
					
					rowData.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowData.add(specimen.getSpecimenCharacteristics().getTissueSide());
					rowData.add(specimen.getPathologicalStatus());
					rowData.add("1");
							
					gridData.add(rowData);
					
					id++;
				}
			}
		}
		
		return gridData;
    }
    
    private void addToOrderLiist(AdvanceSearchForm advForm,HttpServletRequest request,ShoppingCart cart,HttpSession session)
    {
    	Map map = advForm.getValues();
    	Object obj[] = map.keySet().toArray();
		if(cart != null)
		{
			Hashtable table = cart.getCart();
			if(table != null && table.size() != 0)
			{
				List specimenIdList=new ArrayList();
				String strSpecimenId;
				for(int i=0;i<obj.length;i++)
				{
					String str = obj[i].toString();
					
		        	int index = str.indexOf("_") + 1;
		        	String key = str.substring(index);
		        	key = key.trim();
					Specimen specimen = (Specimen)table.get(key);
					strSpecimenId=String.valueOf(specimen.getId());
					specimenIdList.add(strSpecimenId);
				}
				//request.setAttribute("specimenId", specimenIdList);
				session.setAttribute("specimenId", specimenIdList);
			}
		}

    }
    
    
}