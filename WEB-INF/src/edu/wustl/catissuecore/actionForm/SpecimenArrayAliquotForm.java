/*
 * Created on Sep 21, 2006
 */

package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * This Class is used to encapsulate all the request parameters passed from SpecimenArrayAliquots.jsp page.
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotForm extends AbstractActionForm
{

	/**
	 * Label of the ParentSpecimenArray
	 */
	private String parentSpecimenArrayLabel = "";

	/**
	* Barcode of the ParentSpecimenArray.
	*/
	private String barcode = "";

	/**
	 * A number that tells how many aliquots to be created.
	 */
	private String aliquotCount = "";

	/**
	* Initial quantity per aliquot.
	*/
	private String quantityPerAliquot = "1";

	/**
	 * Radio button to choose barcode/label of parentSpecimen
	 */
	private String checkedButton = "1";

	/**
	 * Submit/Create Button Clicked
	 */
	private String buttonClicked = "";

	/**
	 * specimenClass of the ParentSpecimenArray.
	 */
	private String specimenClass;

	/**
	 * specimenType of of the ParentSpecimenArray.
	 */
	private String[] specimenTypes;

	/**
	 * specimenArrayType of the ParentSpecimenArray.
	 */
	private String specimenArrayType;

	/**
	 * specimenArrayId of the ParentSpecimenArray
	 */
	private String specimenArrayId;

	/**
	 * A map that contains distinguished fields (barcode,location) per aliquot.
	 */
	private Map specimenArrayAliquotMap = new HashMap();

	/**
	 * Returns the identifier assigned to form bean.
	 * @return The identifier assigned to form bean.
	 */
	public int getFormId()
	{
		return Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID;
	}

	/**
	 * This method resets the form fields.
	 */
	protected void reset()
	{

	}

	/**
	 * This method Copies the data from an SpecimenArrayAliquot object to a SpecimenArrayAliquotForm object.
	 * @param arg0 An object of Specimen class.  
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{

	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		if (checkedButton.equals("1"))
		{
			if (validator.isEmpty(parentSpecimenArrayLabel))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimenArrayAliquots.parentLabel")));
			}
		}
		else
		{
			if (barcode == null || barcode.trim().length() == 0)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimenArrayAliquots.barcode")));
			}
		}

		if (!validator.isNumeric(aliquotCount))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
					ApplicationProperties.getValue("specimenArrayAliquots.noOfAliquots")));
		}

		if (request.getParameter(Constants.PAGE_OF).equals(
				Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY))
		{
			Iterator keyIterator = specimenArrayAliquotMap.keySet().iterator();
			while (keyIterator.hasNext())
			{
				String key = (String) keyIterator.next();
				if (key.endsWith("_label"))
				{
					String value = (String) specimenArrayAliquotMap.get(key);

					if (validator.isEmpty(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("specimenArrayAliquots.label")));
					}
				}
				else if (key.indexOf("_positionDimension") != -1)
				{
					String value = (String) specimenArrayAliquotMap.get(key);
					if (value != null && !value.trim().equals("") && !validator.isDouble(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties
										.getValue("specimen.positionInStorageContainer")));
						break;
					}
				}
			}
		}

		return errors;
	}

	/**
	 * @return Returns the aliquotCount.
	 */
	public String getAliquotCount()
	{
		return aliquotCount;
	}

	/**
	 * @param aliquotCount The aliquotCount to set.
	 */
	public void setAliquotCount(String aliquotCount)
	{
		this.aliquotCount = aliquotCount;
	}

	/**
	 * @return Returns the barcode.
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * @return Returns the checkedButton.
	 */
	public String getCheckedButton()
	{
		return checkedButton;
	}

	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(String checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	/**
	 * @return Returns the parentSpecimenArrayLabel.
	 */
	public String getParentSpecimenArrayLabel()
	{
		return parentSpecimenArrayLabel;
	}

	/**
	 * @param parentSpecimenArrayLabel The parentSpecimenArrayLabel to set.
	 */
	public void setParentSpecimenArrayLabel(String parentSpecimenArrayLabel)
	{
		this.parentSpecimenArrayLabel = parentSpecimenArrayLabel;
	}

	/**
	 * @return Returns the quantityPerAliquot.
	 */
	public String getQuantityPerAliquot()
	{
		return quantityPerAliquot;
	}

	/**
	 * @param quantityPerAliquot The quantityPerAliquot to set.
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	/**
	 * @return Returns the buttonClicked.
	 */
	public String getButtonClicked()
	{
		return buttonClicked;
	}

	/**
	 * @param buttonClicked The buttonClicked to set.
	 */
	public void setButtonClicked(String buttonClicked)
	{
		this.buttonClicked = buttonClicked;
	}

	/**
	 * @return Returns the specimenArrayType.
	 */
	public String getSpecimenArrayType()
	{
		return specimenArrayType;
	}

	/**
	 * @param specimenArrayType The specimenArrayType to set.
	 */
	public void setSpecimenArrayType(String specimenArrayType)
	{
		this.specimenArrayType = specimenArrayType;
	}

	/**
	 * @return Returns the specimenClass.
	 */
	public String getSpecimenClass()
	{
		return specimenClass;
	}

	/**
	 * @param specimenClass The specimenClass to set.
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	/**
	 * @return Returns the specimenType.
	 */
	public String[] getSpecimenTypes()
	{
		return specimenTypes;
	}

	/**
	 * @param specimenTypes The specimenType to set.
	 */
	public void setSpecimenTypes(String[] specimenTypes)
	{
		this.specimenTypes = specimenTypes;
	}

	/**
	 * @return Returns the specimenArrayAliquotMap.
	 */
	public Map getSpecimenArrayAliquotMap()
	{
		return specimenArrayAliquotMap;
	}

	/**
	 * @param specimenArrayAliquotMap The specimenArrayAliquotMap to set.
	 */
	public void setSpecimenArrayAliquotMap(Map specimenArrayAliquotMap)
	{
		this.specimenArrayAliquotMap = specimenArrayAliquotMap;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is to be mapped.
	 */
	public void setValue(String key, Object value)
	{
		specimenArrayAliquotMap.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return specimenArrayAliquotMap.get(key);
	}

	/**
	 * @return Returns the specimenArrayId.
	 */
	public String getSpecimenArrayId()
	{
		return specimenArrayId;
	}

	/**
	 * @param specimenArrayId The specimenArrayId to set.
	 */
	public void setSpecimenArrayId(String specimenArrayId)
	{
		this.specimenArrayId = specimenArrayId;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}
