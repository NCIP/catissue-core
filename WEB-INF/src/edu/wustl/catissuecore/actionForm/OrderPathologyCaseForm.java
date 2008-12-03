package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.Validator;

public class OrderPathologyCaseForm extends AbstractActionForm 
{
	/**
	 * Map containing the key-value pairs
	 */
	private Map values = new HashMap();

	/**
	 * String containing the class of specimen
	 */
	private String className;

	/**
	 * String containing the type of specimen
	 */
	private String type;

	/**
	 * String containing the tissue site
	 */
	private String tissueSite;

	/**
	 * String containing the pathological status
	 */
	private String pathologicalStatus;

	/**
	 * String containing the type of pathological case
	 */
	private String typeOfCase = "derivative";

	/**
	 * String containing the distribution protocol
	 */
	private String distrbutionProtocol;

	/**
	 * String containing the name of array to which to add the pathological case
	 */
	private String addToArray = "";

	/**
	 * List containing DefineArray objects
	 */
	private List defineArrayObj;

	/**
	 * OrderForm object
	 */
	private OrderForm orderForm;

	/**
	 * String array containing the selected items
	 */
	private String[] selectedItems = null;

	/**
	 * String containing unit
	 */
	private String unit;

	/**
	 * String containing concentration
	 */
	private String concentration;

	/**
	 * String array containing the items to be removed
	 */
	private String[] itemsToRemove = null;

	/**
	 * @return itemsToRemove
	 */
	public String[] getItemsToRemove() 
	{
		return itemsToRemove;
	}

	/**
	 * @param itemsToRemove String array containing the items to be removed
	 */
	public void setItemsToRemove(String[] itemsToRemove) 
	{
		this.itemsToRemove = itemsToRemove;
	}

	/**
	 * @return concentration
	 */
	public String getConcentration() 
	{
		return concentration;
	}

	/**
	 * @param concentration String containing concentration
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return String array containing the selected items
	 */
	public String[] getSelectedItems() 
	{
		return selectedItems;
	}

	/**
	 * @param selectedItems String array containing the selected items
	 */
	public void setSelectedItems(String[] selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	/**
	 * @return name of array in which to add the specimen
	 */
	public String getAddToArray() 
	{
		return addToArray;
	}

	/**
	 * @param addToArray String containing the name of array to which to add the pathological case
	 */
	public void setAddToArray(String addToArray)
	{
		this.addToArray = addToArray;
	}

	/**
	 * @return list of define array objects
	 */
	public List getDefineArrayObj()
	{
		return defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing DefineArray objects
	 */
	public void setDefineArrayObj(List defineArrayObj) 
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * @return OrderForm object
	 */
	public OrderForm getOrderForm() 
	{
		return orderForm;
	}

	/** 
	 * @param orderForm OrderForm object
	 */
	public void setOrderForm(OrderForm orderForm)
	{
		this.orderForm = orderForm;
	}

	/**
	 * @return pathological status of the specimen
	 */
	public String getPathologicalStatus() 
	{
		return pathologicalStatus;
	}

	/**
	 * @param pathologicalStatus String containing the pathological status
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	
	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return type of specimen
	 */
	public String getType() 
	{
		return type;
	}

	/**
	 * @param type String containing the type of specimen
	 */
	public void setType(String type) 
	{
		this.type = type;
	}

	/**
	 * @return tissue site of specimen
	 */
	public String getTissueSite() 
	{
		return tissueSite;
	}

	/**
	 * @param tissueSite String containing the tissue site
	 */
	public void setTissueSite(String tissueSite) 
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * @return map values
	 */
	public Map getValues() 
	{
		return values;
	}

	/**
	 * @param values Map containing key-value pairs
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * reset function
	 */
	protected void reset() 
	{
	}

	/**
	 * @param key String
	 * @param value Object
	 */
	public void setValue(String key, Object value) 
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * @param key String
	 * @return value in map corresponding to the key
	 */
	public Object getValue(String key) 
	{
		return values.get(key);
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return Constants.ORDER_PATHOLOGY_FORM_ID
	 */
	public int getFormId() 
	{
		return Constants.ORDER_PATHOLOGY_FORM_ID;
	}

	/**
	 * @return type of case
	 */
	public String getTypeOfCase()
	{
		return typeOfCase;
	}

	/**
	 * @param typeOfCase String containing the type of pathological case
	 */
	public void setTypeOfCase(String typeOfCase) 
	{
		this.typeOfCase = typeOfCase;
	}

	/**
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol() 
	{
		return distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing the distribution protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol)
	{
		this.distrbutionProtocol = distrbutionProtocol;
	}

	/**
	 * @return unit
	 */
	public String getUnit() 
	{
		return unit;
	}

	/**
	 * @param unit String containing the unit
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) 
	{

		ActionErrors errors = new ActionErrors();
		

		if (selectedItems != null) 
		{
			HttpSession session = request.getSession();
			List defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");
			Map dataMap=(Map)session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
			DefineArrayForm defineArrayFormObj = null;
			int capacity = 0;
			
		    if (defineArrayFormList != null)
			{
				defineArrayFormObj = (DefineArrayForm) searchDefineArrayList(defineArrayFormList,
						addToArray);
				capacity = Integer.parseInt(defineArrayFormObj.getDimenmsionX())
						* Integer.parseInt(defineArrayFormObj.getDimenmsionY());
			}
			if(!addToArray.equalsIgnoreCase("None"))
			{ 
				if(dataMap != null && dataMap.containsKey(addToArray))
				{
					List orderItems=(List)dataMap.get(addToArray);
					if(orderItems.size()+selectedItems.length>capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						values.clear();
					}
				}
				else
				{
					if(selectedItems.length>capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						values.clear();
					}
				}
			}
			
			if (typeOfCase.equals("false")) 
			{
				if (className.equals("-1")
						|| className.equals("-- Select --") || className.equals("") ) 
				{
					errors.add("specimenClass", new ActionError(
							"errors.specimenClass.required"));
					values.clear();

				}
				if (type.equals("-1") || type.equals("-- Select --") || type.equals("") )
				{
					errors.add("type", new ActionError(
							"errors.specimenType.required"));
					values.clear();
				}
				if (!addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals(className))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					values.clear();
				}
			}
			else
			{
				if(!addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals("Tissue"))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					values.clear();
				}
			}
			
			if (pathologicalStatus.equals("-1")
					|| pathologicalStatus.equals("-- Select --")) 
			{
				errors.add("pathologicalStatus", new ActionError(
						"errors.pathologicalStatus.required"));
				values.clear();
			}
			if (tissueSite.equals("-1") || tissueSite.equals("-- Select --"))
			{
				errors.add("tissueSite", new ActionError(
						"errors.tissueSite.required"));
				values.clear();
			}

			boolean isNumber = true;
			if (values != null && values.size() != 0) 
			{
				String cnt = null;
				int reqQntyError = 0;
				for (int i = 0; i < selectedItems.length; i++)
				{
					cnt = selectedItems[i];
					String key="OrderSpecimenBean:" + cnt
					+ "_requestedQuantity";
					String colprotKey = "OrderSpecimenBean:" + cnt + "_collectionProtocol";
					
//					to check for Same CP:Only those SPR can be ordered which belongs to same CP
					if (dataMap!=null && dataMap.containsKey("None"))
					{
						List orderItems = (List) dataMap.get("None");
						if(!orderItems.isEmpty() && orderItems.size()>0)
						{	
							OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItems.get(0);
							if(!orderSpecimenBean.getCollectionProtocol().equals(values.get(colprotKey)))
							{
								errors.add("values", new ActionError("errors.same.collectionProtocol"));
								values.clear();
								break;
							}
						}
						
					}
					
					
					if ((values.get(key)) == null
							|| (values.get(key)).equals("")) 
					{
						errors.add("values", new ActionError(
						"errors.requestedQuantity.required"));
						values.clear();
						break;
					} 
					else
					{
						isNumber = isNumeric(values.get(
								key).toString());
						if (!(isNumber))
						{
							errors.add("values", new ActionError(
							"errors.requestedQuantityBeNumeric.required"));
							values.clear();
							break;
						}
						else 
						{
							Double reqQnty = new Double(values.get(
									key).toString());
							if (reqQnty < 0.0 || reqQnty == 0.0)
							{
								errors.add("values", new ActionError(
								"errors.requestedQuantity.required"));
								values.clear();
								break;
							}
						}
					}
				}
			}
		}
		return errors;
	}

	/**
	 * @param defineArrayFormList List containing DefineArrayForm objects
	 * @param arrayName String containing name of array
	 * @return defineArrayFormObj 
	 */
	private DefineArrayForm searchDefineArrayList(List defineArrayFormList, String arrayName)
	{
		Iterator iter = defineArrayFormList.iterator();
		DefineArrayForm defineArrayFormObj = null;
		while (iter.hasNext())
		{
			defineArrayFormObj = (DefineArrayForm) iter.next();
			if (defineArrayFormObj.getArrayName().equalsIgnoreCase(arrayName))
			{
				break;
			}
		}
		return defineArrayFormObj;
	}

	/**
	 * @param sText String containing the text to be checked 
	 * @return boolean isNumber-returns true if given String is in proper number
	 *         format or else returns false
	 */
	private boolean isNumeric(String sText)
	{
		String validChars = "0123456789.";
		boolean isNumber = true;
		Character charTemp;

		for (int i = 0; i < sText.length() && isNumber; i++) 
		{
			charTemp = sText.charAt(i);
			if (validChars.indexOf(charTemp) == -1)
			{
				isNumber = false;
			}
		}
		return isNumber;
	}

	/**
	 * @return boolean true
	 */
	public boolean isAddOperation() 
	{
		return true;
	}

}
