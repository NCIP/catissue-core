package edu.wustl.catissuecore.action;


/**
 * Common action class used for initializing field's value of JSP 
 * @author namita_srivastava
 *
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.HTMLField;
import edu.wustl.catissuecore.vo.SearchFieldData;
import edu.wustl.common.util.SearchUtil;
import edu.wustl.common.util.logger.Logger;
public abstract class AdvanceSearchUIAction extends BaseAction 
{
	
	/**
	 * Used for creating key of field which is further used for mapping
	 * @param tableName Used for creating key of field
	 * @param fieldName Represents name of field
	 * @param isOperator Checking whether the key is of operator field or not
	 * @param dataType Datatype of field
	 * @return
	 */
	
	private String createKey(String tableName,String fieldName,boolean isOperator,String dataType)
    {
		//Checking whether the key is of operator field or not
    	if(isOperator)
    	{
    		return "value(Operator:" + tableName + ":" + fieldName +")";
    	}
    	else
    	{
    		if(dataType.equals("Date"))
    		{
   			//Condition for date & numeric as ')' will be appended in JSP
    			return "value(" + tableName + ":" + fieldName;
    		}
    		else
    		{
    			return "value(" + tableName + ":" + fieldName + ")";
    		}
    	}
    }
   
	
	/**
	 * Used for initializing HTMLField class which set the value of each field(text,comboBox etc)
	 * @param dataType  Datatype of field
	 * @param msgKey  Label of row
	 * @param tblName  Used for creating key of field
	 * @param colName  Represents name of field
	 * @param id  Represents id of field
	 * @param opList  For populating value of comboBox
	 * @param valueList  For populating value in case of comboBox in 3rd column of table
	 * @param unitFieldKey  Used for specimen page only 
	 * @return  instance of SearchFieldData
	 */
	
	protected SearchFieldData initSearchUIData(String dataType,String msgKey, String tblName,String colName,String id,
    		String opList,String valueList,String unitFieldKey)
    {
		//Initializing HTMLField class
    	HTMLField oprationField = new HTMLField(createKey(tblName,colName,true,dataType),id+"Combo",opList);
    	HTMLField valueField = new HTMLField(createKey(tblName,colName,false,dataType),id,valueList);
    	
    	//Function Name used according to dataType of field
    	String funcName = "";
    	
    	//Condition for diffent function name for enabing/disabling next field
    	if( dataType.equals(SearchUtil.DATE) || (dataType).equals(SearchUtil.NUMERIC))
    	{
    		//Passing 3 parameters out of this two are representing id of textbox
			funcName = "onDateOperatorChange(this,'" + id + "1','" + id + "2')";
    	}
    	else
    	{
    		//passing id of field from where function is called and next field's id
    		funcName = "onOperatorChange('" + (id+"Combo") + "','" + id +"')";
    	}
    	
    	return new SearchFieldData(dataType, msgKey ,oprationField,valueField,funcName,unitFieldKey);
    }
    
	
	/**
	 * Used for setting value of map of form in edit case
	 * @param aForm Common form form for all Search Pages
	 * @param str represents id as string
	 * @param request HttpServletRequest
	 */
	protected void setMapValue(AdvanceSearchForm aForm,String str,HttpServletRequest request)
    {
		//Represents checked checbox's id
    	Integer nodeId = Integer.decode(str);
    	HttpSession session = request.getSession();
    	
    	//ConditionNode repesents map of condition given by user in jsp page
    	Map ConditionNode = (HashMap)session.getAttribute(Constants.ADVANCED_CONDITION_NODES_MAP);
    	
    	//Represent conditions of according to id
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)ConditionNode.get(nodeId);
    	
    	AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)node.getUserObject();
    	
    	//Represent vector of conditions of paticular row
    	Vector vectorOfCondtions = advConditionNode.getObjectConditions();
    	
    	Condition con = null;
		DataElement dataElement = null;
		Operator op = null;
		String column = "";
		String temp ="";
		String tempOperator = "";
		Map map = new HashMap();
		Iterator it = vectorOfCondtions.iterator();
		
		while(it.hasNext())
		{
			con  = (Condition)it.next();
			dataElement = con.getDataElement();
			
			//Used for creating key of field
			String tableName = dataElement.getTable();
			
	        op = con.getOperator();
	        column = dataElement.getField();
	        String valuekey = "";
	        
	        //Condition for checking date values as operator keys are same
	        if(map.containsKey(temp))
	        {
	        	//Key for value field ie 3rd column
		        valuekey = tableName+":"+column + ":HLIMIT";
	        	
	        }
	        else 
	        {
//	        	Key for value field ie 3rd column
		        valuekey = tableName+":"+column;
	        }
	        
	        //Key for value field ie 3rd column
			String opKey = "Operator:" + tableName+":"+column;
			
			//Explicitly setting operator keys value as they are same
			if(map.containsKey(temp))
	        {
				
				if(tempOperator.equals(Operator.GREATER_THAN_OR_EQUALS))
					map.put(opKey,"Between");
				else
					map.put(opKey,"Not Between");
				
	        }
			else
			{
				map.put(opKey,op.getOperator());
				tempOperator = op.getOperator();
			}
			
			//Setting value of map in edit case
			map.put(valuekey,con.getValue());
			temp = valuekey;
		}
		
		//Setting map in form
		aForm.setValues(map);
		setMapOfNodes(map);
		
    }
	
	
	/**
	 * Used for setting value of map of form in Edit case
	 * @param searchFieldData Instance of class for setting field's value
	 * @param map Map from form containing keys of field
	 */
	
	protected void setIsDisableValue(SearchFieldData[] searchFieldData,Map map)
	{
		for(int i = 0; i < searchFieldData.length ; i++)
		{
			//Represents name of field i.e value(...)
			String temp = searchFieldData[i].getValueField().getName();
			
			//DataType of field
			String	dType = searchFieldData[i].getDataType();
			String name = "";
			
			//Condition for removing brackets as valueField's name in Date case contains only '('
			if(dType.equals(SearchUtil.DATE))
			{
				name = temp.substring(temp.indexOf("(")+1);
			}
			else	
			{
				name = temp.substring(temp.indexOf("(")+1,temp.indexOf(")"));
			}
			
			//Enabling or disabling the field according condition whether map contains value or not
			Object element = map.get(name);
			if( (element == null) || (  element.equals("Any")  ) )
			{
				searchFieldData[i].getValueField().setDisabled(true) ;
			}
			else
			{
				searchFieldData[i].getValueField().setDisabled(false) ;
			}
		}
	}
	
	
	private void setMapOfNodes(Map map)
	{
		
	}
}