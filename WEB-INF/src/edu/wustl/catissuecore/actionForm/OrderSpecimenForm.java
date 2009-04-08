
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

/**
 * orderSpecimenForm for placing an order
 * 
 * @author deepti_phadnis
 * 
 */

public class OrderSpecimenForm extends AbstractActionForm
{

	/**
	 * String containing the type of specimen whether existing or derived
	 */
	private String typeOfSpecimen = "existingSpecimen";

	/**
	 * Map containing the key-value pairs
	 */
	private Map values = new LinkedHashMap();

	/**
	 * String containing the class of specimen 
	 */
	private String className;

	/**
	 * String containing the type of specimen 
	 */
	private String type;

	/**
	 * String array containing the selected items
	 */
	private String[] selectedItems = null;

	/**
	 * String array containing the items to be removed
	 */
	private String[] itemsToRemove = null;

	/**
	 * String containing the unit
	 */
	private String unit;

	/**
	 * OrderForm object
	 */
	private OrderForm orderForm;

	/**
	 * String containing the concentration
	 */
	private String concentration;

	/**
	 * String containing the distrbution Protocol
	 */
	private String distrbutionProtocol;

	/**
	 * String containing the name of array to which to add the specimen
	 */
	private String addToArray = "";

	/**
	 * List containing Define Array objects 
	 */
	private List defineArrayObj;

	/**
	 * @return List of defineArrayObj
	 */
	public List getDefineArrayObj()
	{
		return defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing Define Array objects 
	 */
	public void setDefineArrayObj(List defineArrayObj)
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * 
	 * @return String containing whether speicmens got to be added to list or
	 *         array
	 */
	public String getAddToArray()
	{
		return addToArray;
	}

	/**
	 * @param addToArray String containing the name of array to which to add the specimen
	 */
	public void setAddToArray(String addToArray)
	{
		this.addToArray = addToArray;
	}

	// For 'ADD' operation in CommonAddEditAction.
	/**
	 * @return boolean true
	 */
	public boolean isAddOperation()
	{
		return true;
	}

	/**
	 * @return concentration
	 */
	public String getConcentration()
	{
		return concentration;
	}

	/**
	 * @param concentration String containing the concentration
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return unit of requested quantity
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
	 * @return string of specimen selected
	 */
	public String[] getSelectedItems()
	{
		return this.selectedItems;
	}

	/**
	 * @param selectedItems String array containing the selected items
	 */
	public void setSelectedItems(String[] selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	/**
	 * @return typeOfSpecimen i.e.derived or existing
	 */
	public String getTypeOfSpecimen()
	{
		return typeOfSpecimen;
	}

	/**
	 * @param typeOfSpecimen String containing the type of specimen whether existing or derived
	 */
	public void setTypeOfSpecimen(String typeOfSpecimen)
	{
		this.typeOfSpecimen = typeOfSpecimen;
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return Constants.ORDER_FORM_ID i.e. the FormId
	 */
	public int getFormId()
	{
		return Constants.ORDER_FORM_ID;
	}

	/**
	 * function for resetting 
	 */
	protected void reset()
	{

		this.typeOfSpecimen = null;
		this.selectedItems = null;
	}

	/** ** for map *** */
	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
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
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		HttpSession session = request.getSession();
		Map dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
		if (selectedItems != null)
		{
			boolean isNumber = true;
			List defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");

			DefineArrayForm defineArrayFormObj = null;
			int capacity = 0;

			if (defineArrayFormList != null)
			{
				defineArrayFormObj = (DefineArrayForm) searchDefineArrayList(defineArrayFormList,
						addToArray);
				capacity = Integer.parseInt(defineArrayFormObj.getDimenmsionX())
						* Integer.parseInt(defineArrayFormObj.getDimenmsionY());
			}

			if (!addToArray.equalsIgnoreCase("None"))
			{
				if (dataMap == null)
				{
					if (selectedItems.length > capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						values.clear();
					}
				}
				else
				{
					if (dataMap.containsKey(addToArray))
					{
						List orderItems = (List) dataMap.get(addToArray);
						if (orderItems.size() + selectedItems.length > capacity)
						{
							errors.add("addToArray", new ActionError("errors.order.capacityless"));
							values.clear();
						}
					}
					else
					{
						if (selectedItems.length > capacity)
						{
							errors.add("addToArray", new ActionError("errors.order.capacityless"));
							values.clear();
						}
					}
				}
			}

			if (typeOfSpecimen.equals("true"))
			{
				if (className.equals("-1") || className.equals("-- Select --"))
				{
					errors.add("className", new ActionError("errors.specimenClass.required"));
					values.clear();
				}
				if (type.equals("-1") || type.equals("-- Select --"))
				{
					errors.add("type", new ActionError("errors.specimenType.required"));
					values.clear();
				}

				if (!addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals(className))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					values.clear();
				}
			}
			if (values != null && values.size() != 0)
			{
				String cnt = null;
				String reqQuantKey = null;
				String keyClass = null;
				
				
				
				for (int i = 0; i < selectedItems.length; i++)
				{
					cnt = selectedItems[i];
					reqQuantKey = "OrderSpecimenBean:" + cnt + "_requestedQuantity";
					String disSiteKey = "OrderSpecimenBean:" + cnt + "_distributionSite";
					String availQuantKey = "OrderSpecimenBean:" + cnt + "_availableQuantity";
					String specimenName =(String) values.get("OrderSpecimenBean:" + cnt + "_specimenName");
					String collectionStatuskey =(String) values.get("OrderSpecimenBean:" + cnt + "_collectionStatus");
					String isAvailablekey =(String) values.get("OrderSpecimenBean:" + cnt + "_isAvailable");
					if (dataMap!=null && dataMap.containsKey(addToArray))
					{
						List orderItems = (List) dataMap.get(addToArray);
						if(!orderItems.isEmpty() && orderItems.size()>0)
						{	
							OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItems.get(0);
							if(!orderSpecimenBean.getDistributionSite().equals(values.get(disSiteKey)))
							{
								errors.add("values", new ActionError("errors.same.distributionSite"));
								values.clear();
								break;
							}
						}
						
					}
					
					if((values.get(disSiteKey)) == null || (values.get(disSiteKey)).equals("N/A"))
					{
						errors.add("values", new ActionError("errors.specimenPosition.required", specimenName));
						values.clear();
						break;
					}
					
					if((values.get(disSiteKey)) == null || !isSameSite())
					{
						errors.add("values", new ActionError("errors.same.distributionSite"));
						values.clear();
						break;
					}
					
					if(!Constants.COLLECTION_STATUS_COLLECTED.equals(collectionStatuskey))
					{
						errors.add("values", new ActionError("errors.collectionStatus", specimenName));
						values.clear();
						break;
					}
					
					if((isAvailablekey) == null || (isAvailablekey).equals(""))
					{
						errors.add("values", new ActionError("errors.isAvailable", specimenName));
						values.clear();
						break;
					}else
					{
//						Boolean isAvailable = Boolean.valueOf(isAvailablekey.toString());
						if(!(Boolean.valueOf(isAvailablekey.toString())))
						{
							errors.add("values", new ActionError("errors.isAvailable", specimenName));
							values.clear();
							break;
						}
					}
					
					if((values.get(availQuantKey)) == null || (values.get(availQuantKey)).equals(""))
					{
						errors.add("values", new ActionError("errors.availableQuantity.required",specimenName));
						values.clear();
						break;
					}
					else
					{
						Double reqQnty = new Double(values.get(availQuantKey).toString());
						if (reqQnty < 0.0 || reqQnty == 0.0)
						{
							errors.add("values", new ActionError(
									"errors.availableQuantity.required",specimenName));
							values.clear();
							break;
						}
					}
						
					if ((values.get(reqQuantKey)) == null || (values.get(reqQuantKey)).equals(""))
					{
						errors.add("values", new ActionError("errors.requestedQuantity.required"));
						values.clear();
						break;
					}

					else
					{
						isNumber = isNumeric(values.get(reqQuantKey).toString());
						if (!(isNumber))
						{
							errors.add("values", new ActionError(
									"errors.requestedQuantityBeNumeric.required"));
							values.clear();
							break;
						}
						else
						{
							Double reqQnty = new Double(values.get(reqQuantKey).toString());
							if (reqQnty < 0.0 || reqQnty == 0.0)
							{
								errors.add("values", new ActionError(
										"errors.requestedQuantity.required"));
								values.clear();
								break;
							}
						}
					}

					keyClass = "OrderSpecimenBean:" + cnt + "_specimenClass";
					if (typeOfSpecimen.equals("false")
							&& !addToArray.equalsIgnoreCase("None")
							&& defineArrayFormObj != null
							&& !defineArrayFormObj.getArrayClass().equalsIgnoreCase(
									values.get(keyClass).toString()))
					{
						errors.add("addToArray", new ActionError("errors.order.properclass"));
						values.clear();
						break;
					}

				}
			}
		}
		return errors;
	}
	
	/**
	 * @return
	 */
	private boolean isSameSite()
	{
		boolean isSameSite = true;
		if (values != null && values.size() != 0)
		{
			for (int i = 0; i < selectedItems.length; i++)
			{
				String cnt = selectedItems[i];
				String disSite = (String)values.get("OrderSpecimenBean:" + cnt + "_distributionSite");
				for (int j = 0; j < selectedItems.length; j++)
				{
					String count = selectedItems[j];
					String disSiteInner = (String)values.get("OrderSpecimenBean:" + count + "_distributionSite");
					if(!disSiteInner.equals(disSite))
					{
						return false;
					}
				}
			}
			
		}
				
		return isSameSite;
	}

	/**
	 * @param sText String containing the text to be checked 
	 * @return boolean isNumber -true if given String is in proper number
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
	 * @param key String
	 * @return Object
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return values in map
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/**
	 * @param values Map containing key-value pairs
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * @return Map
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * @return specimens to be removed
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
	 * @return OrderForm
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
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol()
	{
		return distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing the distrbution Protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol)
	{
		this.distrbutionProtocol = distrbutionProtocol;
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
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub
		
	}

	
}
