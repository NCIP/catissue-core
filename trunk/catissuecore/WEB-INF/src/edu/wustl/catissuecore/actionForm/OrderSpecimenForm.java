
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

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
	private Map values = new HashMap();

	/**
	 * String containing the class of specimen 
	 */
	private String specimenClassName;

	/**
	 * String containing the type of specimen 
	 */
	private String specimenType;

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

		if (selectedItems != null)
		{
			boolean isNumber = true;
			HttpSession session = request.getSession();

			List defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");

			Map dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

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
				if (specimenClassName.equals("-1") || specimenClassName.equals("-- Select --"))
				{
					errors.add("className", new ActionError("errors.specimenClass.required"));
					values.clear();
				}
				if (specimenType.equals("-1") || specimenType.equals("-- Select --"))
				{
					errors.add("type", new ActionError("errors.specimenType.required"));
					values.clear();
				}

				if (!addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals(specimenClassName))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					values.clear();
				}
			}
			if (values != null && values.size() != 0)
			{
				String cnt = null;
				String key = null;
				String keyClass = null;
				int reqQntyError = 0;
				for (int i = 0; i < selectedItems.length; i++)
				{
					cnt = selectedItems[i];
					key = "OrderSpecimenBean:" + cnt + "_requestedQuantity";

					if ((values.get(key)) == null || (values.get(key)).equals(""))
					{
						errors.add("values", new ActionError("errors.requestedQuantity.required"));
						values.clear();
						break;
					}

					else
					{
						isNumber = isNumeric(values.get(key).toString());
						if (!(isNumber))
						{
							errors.add("values", new ActionError(
									"errors.requestedQuantityBeNumeric.required"));
							values.clear();
							break;
						}
						else
						{
							Double reqQnty = new Double(values.get(key).toString());
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
	 * @return specimenClassName
	 */
	public String getSpecimenClassName()
	{
		return specimenClassName;
	}

	/**
	 * @param specimenClassName String containing the class of specimen 
	 */
	public void setSpecimenClassName(String specimenClassName)
	{
		this.specimenClassName = specimenClassName;
	}

	/**
	 * @return specimenType
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * @param specimenType String containing the type of specimen 
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

}
