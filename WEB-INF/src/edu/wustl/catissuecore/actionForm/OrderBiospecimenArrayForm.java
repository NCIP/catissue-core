package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.Constants;

public class OrderBiospecimenArrayForm extends AbstractActionForm 
{

	/**
	 * A String containing type of Array
	 */
	private String typeOfArray = "existingArray";

	/**
	 * A String array containing selectedItems
	 */
	private String[] selectedItems = null;

	private OrderForm orderForm;

	private Map values = new HashMap();

	/**
	 * A String containing Distribution Protocol
	 */
	private String distrbutionProtocol;

	/**
	 * A List containing the DefineArray objects
	 */
	private List defineArrayObj;

	/**
	 * A String array containing itemsToRemove 
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
	 * @param itemsToRemove String array containing itemsToRemove 
	 */
	public void setItemsToRemove(String[] itemsToRemove) 
	{
		this.itemsToRemove = itemsToRemove;
	}

	/**
	 * @return List of defineArrayObj
	 */
	public List getDefineArrayObj() 
	{
		return defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing the DefineArray objects
	 */
	public void setDefineArrayObj(List defineArrayObj)
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * @return boolean true 
	 */
	public boolean isAddOperation()
	
	{
		return true;
	}

	/**
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol()
	{
		return distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing Distribution Protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol) 
	{
		this.distrbutionProtocol = distrbutionProtocol;
	}

	/**
	 * @return object of OrderForm
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
	 * @return type of array- whether existing array or tissue slide from a
	 *         block
	 */
	public String getTypeOfArray()
	{
		return typeOfArray;
	}

	/**
	 * @param typeOfArray String containing type of Array
	 */
	public void setTypeOfArray(String typeOfArray)
	{
		this.typeOfArray = typeOfArray;
	}

	/**
	 * @return FormId
	 */
	public int getFormId() 
	{
		return Constants.ORDER_ARRAY_FORM_ID;
	}

	/**
	 * reset function
	 */
	protected void reset()
	{
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
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
			boolean isNumber = true;
			if (values != null && values.size() != 0) 
			{
				String cnt = null;
				int reqQntyError = 0;
				String reqQntyValue=null;	
				for (int i = 0; i < selectedItems.length; i++) 
				{
					cnt = selectedItems[i];
					String key = "OrderSpecimenBean:" + cnt + "_requestedQuantity";	
					if (typeOfArray.equals("false"))
					{
						values.put(key, "0.0");
					}

					else 
					{
						if ((values.get(key)) == null
								|| (values.get(key)).equals("")) 
						{
							reqQntyError = 1;
							break;
						}

						else 
						{
							isNumber = isNumeric(values.get(key).toString());
							if (!(isNumber))
							{
								reqQntyError = 2;
								break;
							}
							else
							{
								Double reqQnty = new Double(values.get(
										key).toString());
								if (reqQnty < 0.0 || reqQnty==0.0)
								{
									reqQntyError = 1;
									break;
								}
							}
						}
					}
				}
				if (reqQntyError == 1)
				{
					errors.add("values", new ActionError(
							"errors.requestedQuantity.required"));
					values.clear();
				}
				if (reqQntyError == 2)
				{
					errors.add("values", new ActionError(
							"errors.requestedQuantityBeNumeric.required"));
					values.clear();
				}
			}
		}
		return errors;
	}

	/**
	 * @param sText String containing the text to be checked 
	 * @return boolean isNumber-returns true if given String is in proper number
	 *         format or else returns false
	 */
	private boolean isNumeric(String sText)
	{
		String validChars = "0123456789";
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
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	}

	/**
	 * @return String array selectedItems-indices of selected items
	 */
	public String[] getSelectedItems()
	{
		return selectedItems;
	}

	/**
	 * @param selectedItems String array containing selectedItems
	 */
	public void setSelectedItems(String[] selectedItems) 
	{
		this.selectedItems = selectedItems;
	}

	/**
	 * @param key String containing the key of map 
	 * @param value String containing the value of map 
	 */
	public void setValue(String key, Object value) 
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * @param key String containing the key of map 
	 * @return value in map corresponding to the key
	 */
	public Object getValue(String key) 
	{
		return values.get(key);
	}

	/**
	 * @return map values
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @param values Map containing the key-value pairs 
	 */
	public void setValues(Map values) 
	{
		this.values = values;
	}

}
