/**
 * <p>Title: AliquotForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed
 * from Aliquot.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on May 11, 2006
 */

package edu.wustl.catissuecore.actionForm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Aliquot.jsp page.
 * @author aniruddha_phadnis
 * */
public class AliquotForm extends AbstractActionForm implements IPrinterTypeLocation
{
	/**
	 * Logger instance.
	 */
	private final transient Logger logger =
			Logger.getCommonLogger(AliquotForm.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An label of a specimen.
	 */
	private String specimenLabel = "";

	/**
	 * An label of a specimen.
	 */
	private String specimenID;

	/**
	 * A number that tells how many aliquots to be created.
	 */
	private String noOfAliquots = "";

	/**
	 * An identifier of Specimen Collection Group.
	 */
	private long spCollectionGroupId;

	/**
	 * An identifier of collection protocol.
	 */
	private long colProtId;

	/**
	 * @return the collection protocol id.
	 */
	public long getColProtId()
	{
		return this.colProtId;
	}

	/**
	 * Set the collection protocol id.
	 * @param colProtId
	 */
	public void setColProtId(long colProtId)
	{
		this.colProtId = colProtId;
	}

	/**
	 * Specimen Collection Group Name
	 */
	private String scgName;

	/**
	 * A class of the specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	private String className;

	/**
	 * A type of the specimen.
	 */
	private String type;

	/**
	 * Anatomic site from which the specimen was derived.
	 */
	private String tissueSite;

	/**
	 * A bilateral site. e.g. Left or Right.
	 */
	private String tissueSide;

	/**
	 * Histopathological character of the specimen.
	 */
	private String pathologicalStatus;

	/**
	 * Available quantity of the parent specimen.
	 */
	private String initialAvailableQuantity;

	/**
	 * Available quantity of the parent specimen after creating aliquots.
	 */
	private String availableQuantity;

	/**
	 * Concentration of the molecular specimen.
	 */
	private String concentration;

	/**
	 * Initial quantity per aliquot.
	 */
	private String quantityPerAliquot = "";

	/**
	 * Barcode assigned of the parent specimen.
	 */
	private String barcode = "";

	/**
	 * Radio button to choose barcode/specimen identifier
	 */
	private String checkedButton = "1";

	/**
	 * A map that contains distinguished fields (quantity,barcode,location) per aliquot.
	 */
	private Map aliquotMap = new HashMap();

	/**
	 * decides whether to store all aliquotes in the same container
	 */
	private boolean aliqoutInSameContainer = false;

	/**
	 * decides whether to dispose Parent Specimen when its quantity becomes zero
	 */
	private boolean disposeParentSpecimen = false;

	/**
	 * identifies the button clicked
	 */
	private String buttonClicked = "";
	/**
	 * Patch ID: 3835_1_8
	 * See also: 1_1 to 1_5
	 * Description : Added createdOn in formbean
	 */

	private String createdDate;

	/**
	 * Next forwardto in case of Print
	 */
	private String nextForwardTo;
	/**
	 * print checkbox
	 */
	private String printCheckbox;

	private String printerType;

	private String printerLocation;

	private List<AbstractDomainObject> specimenList = new LinkedList<AbstractDomainObject>();

	private boolean generateLabel;

	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}
	public String getNextForwardTo()
	{
		return this.nextForwardTo;
	}

	public void setNextForwardTo(final String nextForwardTo)
	{
		this.nextForwardTo = nextForwardTo;
	}

	public String getPrintCheckbox()
	{
		return this.printCheckbox;
	}

	public void setPrintCheckbox(final String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}

	/**
	 * Returns the map that contains distinguished fields per aliquots.
	 * @return The map that contains distinguished fields per aliquots.
	 * @see #setAliquotMap(Map)
	 */
	public Map getAliquotMap()
	{
		return this.aliquotMap;
	}

	/**
	 * Sets the map of distinguished fields of aliquots.
	 * @param aliquotMap A map of distinguished fields of aliquots.
	 * @see #getAliquotMap()
	 */
	public void setAliquotMap(final Map aliquotMap)
	{
		this.aliquotMap = aliquotMap;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is to be mapped.
	 */
	public void setValue(final String key, final Object value)
	{
		this.aliquotMap.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(final String key)
	{
		return this.aliquotMap.get(key);
	}

	/**
	 * Returns the available quantity of parent specimen after creating aliquots.
	 * @return The available quantity of parent specimen after creating aliquots.
	 * @see #setAvailableQuantity(String)
	 */
	public String getAvailableQuantity()
	{
		return this.availableQuantity;
	}

	/**
	 * Sets the available quantity of parent specimen after creating aliquots.
	 * @param availableQuantity The available quantity of parent specimen after creating aliquots.
	 * @see #getAvailableQuantity()
	 */
	public void setAvailableQuantity(final String availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the concentration of parent specimen.
	 * @return The concentration of parent specimen.
	 * @see #setConcentration(String)
	 */
	public String getConcentration()
	{
		if (this.concentration == null)
		{
			this.concentration = "";
		}
		return this.concentration;
	}

	/**
	 * Sets the concentration of parent specimen.
	 * @param concentration The concentration of parent specimen.
	 * @see #getConcentration()
	 */
	public void setConcentration(final String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * Returns the no. of aliquots to be created.
	 * @return The no. of aliquots to be created.
	 * @see #setNoOfAliquots(String)
	 */
	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	/**
	 * Sets the no. of aliquots to be created.
	 * @param noOfAliquots The no. of aliquots to be created.
	 * @see #getNoOfAliquots()
	 */
	public void setNoOfAliquots(final String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	/**
	 * Returns the pathological status of parent specimen.
	 * @return The pathological status of parent specimen.
	 * @see #setPathologicalStatus(String)
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * Sets the pathological status of parent specimen.
	 * @param pathologicalStatus The pathological status of parent specimen.
	 * @see #getPathologicalStatus()
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * Returns the specimen collection group identifier of parent specimen.
	 * @return The specimen collection group identifier of parent specimen.
	 * @see #setSpCollectionGroupId(long)
	 */
	public long getSpCollectionGroupId()
	{
		return this.spCollectionGroupId;
	}

	/**
	 * Sets the specimen collection group identifier of parent specimen.
	 * @param spCollectionGroupId The specimen collection group identifier of parent specimen.
	 * @see #getSpCollectionGroupId()
	 */
	public void setSpCollectionGroupId(long spCollectionGroupId)
	{
		this.spCollectionGroupId = spCollectionGroupId;
	}

	/**
	 * Returns the specimen class of parent specimen.
	 * @return The specimen class of parent specimen.
	 * @see #setClassName(String)
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * Sets the specimen class of parent specimen.
	 * @param className The specimen class of parent specimen.
	 * @see #getClassName()
	 */
	public void setClassName(final String className)
	{
		this.className = className;
	}

	/**
	 * Returns the specimen label of parent specimen.
	 * @return The specimen label of parent specimen.
	 * @see #setSpecimenLabel(String)
	 */
	public String getSpecimenLabel()
	{
		return this.specimenLabel;
	}

	/**
	 * Sets the specimen label of parent specimen.
	 * @param specimenLabel The specimen label of parent specimen.
	 * @see #getSpecimenLabel()
	 */
	public void setSpecimenLabel(final String specimenLabel)
	{
		this.specimenLabel = specimenLabel;
	}

	/**
	 * Returns the tissue side of parent specimen.
	 * @return The tissue side of parent specimen.
	 * @see #setTissueSide(String)
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * Sets the tissue side of parent specimen.
	 * @param tissueSide The tissue side of parent specimen.
	 * @see #getTissueSide()
	 */
	public void setTissueSide(final String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * Returns the tissue site of parent specimen.
	 * @return The tissue site of parent specimen.
	 * @see #setTissueSite(String)
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * Sets the tissue site of parent specimen.
	 * @param tissueSite The tissue site of parent specimen.
	 * @see #getTissueSite()
	 */
	public void setTissueSite(final String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Returns the type of parent specimen.
	 * @return The type of parent specimen.
	 * @see #setType(String)
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type of parent specimen.
	 * @param type The type of parent specimen.
	 * @see #getTissueSite()
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * Returns the identifier assigned to form bean.
	 * @return The identifier assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return Constants.ALIQUOT_FORM_ID;
	}

	/**
	 * This method resets the form fields.
	 */
	@Override
	public void reset()
	{
	}

	/**
	 * This method Copies the data from an Specimen object to a AliquotForm object.
	 * @param abstractDomain An object of Specimen class.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error
	 * @param mapping
	 * @param request
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		if (Constants.PAGE_OF_ALIQUOT_SUMMARY.equals(request.getParameter(Constants.PAGE_OF)))
		{
			/**
			  * Patch ID: 3835_1_9
			  * See also: 1_1 to 1_5
			  * Description : Validated createdOn date field from form bean
			  */
			if (!Validator.isEmpty(this.createdDate))
			{

				final String errorKeyForCreatedDate = validator
						.validateDate(this.createdDate, true);
				if (errorKeyForCreatedDate.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForCreatedDate,
							ApplicationProperties.getValue("specimen.createdDate")));
				}
			}

			final Iterator keyIterator = this.aliquotMap.keySet().iterator();

			while (keyIterator.hasNext())
			{

				final String key = (String) keyIterator.next();

				if (key.endsWith("_quantity"))
				{
					String value = (String) this.aliquotMap.get(key);
					try
					{
						value = new BigDecimal(value).toPlainString();

						if (AppUtility.isQuantityDouble(this.className, this.type))
						{
							if (!validator.isDouble(value, true))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("specimen.quantity")));
								break;
							}
						}
						else
						{
							if (!validator.isNumeric(value, 0))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("specimen.quantity")));
								break;
							}
						}
					}
					catch (final NumberFormatException exp)
					{
						this.logger.error(exp.getMessage(),exp) ;
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("specimen.quantity")));
					}
				}
//				else if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl
				else if(this.generateLabel && key.endsWith("_label"))
				{
					//by Falguni
					final String value = (String) this.aliquotMap.get(key);
					//         			System.out.println("value");
					if (Validator.isEmpty(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("specimen.label")));
					}
				}
				else if (key.indexOf("_positionDimension") != -1)
				{
					final String value = (String) this.aliquotMap.get(key);
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
	 * Returns the initial quantity per aliquot.
	 * @return The initial quantity per aliquot.
	 * @see #setQuantityPerAliquot(String)
	 */
	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	/**
	 * Sets the initial quantity per aliquot.
	 * @param quantityPerAliquot The initial quantity per aliquot.
	 * @see #getQuantityPerAliquot()
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	/**
	 * Returns the available quantity of parent specimen.
	 * @return The available quantity of parent specimen.
	 * @see #setInitialAvailableQuantity(String)
	 */
	public String getInitialAvailableQuantity()
	{
		return this.initialAvailableQuantity;
	}

	/**
	 * Sets the available quantity of parent specimen.
	 * @param initialAvailableQuantity The available quantity of parent specimen.
	 * @see #getInitialAvailableQuantity()
	 */
	public void setInitialAvailableQuantity(String initialAvailableQuantity)
	{
		this.initialAvailableQuantity = initialAvailableQuantity;
	}

	/**
	 * Returns the barcode of the parent specimen.
	 * @return String the barcode of the parent specimen.
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return this.barcode;
	}

	/**
	 * Sets the barcode of the parent specimen.
	 * @param barcode The barcode of the parent specimen.
	 * @see #getBarcode()
	 */
	public void setBarcode(final String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * Returns the value of selected radio button.
	 * @return String the value of selected radio button.
	 * @see #setCheckedButton(String)
	 */
	public String getCheckedButton()
	{
		return this.checkedButton;
	}

	/**
	 * Returns the value of selected radio button.
	 * @param checkedButton The value of selected radio button.
	 * @see #getCheckedButton()
	 */
	public void setCheckedButton(final String checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	/**
	 * @return Returns the specimenID.
	 */
	public String getSpecimenID()
	{
		return this.specimenID;
	}

	/**
	 * @param specimenID The specimenID to set.
	 */
	public void setSpecimenID(final String specimenID)
	{
		this.specimenID = specimenID;
	}

	/**
	 * @return Returns the aliqoutInSameContainer.
	 */
	public boolean isAliqoutInSameContainer()
	{
		return this.aliqoutInSameContainer;
	}

	/**
	 * @param aliqoutInSameContainer The aliqoutInSameContainer to set.
	 */
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainer)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainer;
	}

	/**
	 * @return Returns the buttonClicked.
	 */
	public String getButtonClicked()
	{
		return this.buttonClicked;
	}

	/**
	 * @param buttonClicked The buttonClicked to set.
	 */
	public void setButtonClicked(final String buttonClicked)
	{
		this.buttonClicked = buttonClicked;
	}

	/**
	 * @return Returns the disposeParentSpecimen.
	 */
	public boolean getDisposeParentSpecimen()
	{
		return this.disposeParentSpecimen;
	}

	/**
	 * @param disposeParentSpecimen The disposeParentSpecimen to set.
	 */
	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	/**
	  * Patch ID: 3835_1_10
	  * See also: 1_1 to 1_5
	  * Description : Getter , setter methods for createdOn date
	  * @return createdDate
	  */
	public String getCreatedDate()
	{
		return this.createdDate;
	}

	/**
	 * Set create Date
	 * @param createdDate
	 */
	public void setCreatedDate(final String createdDate)
	{
		this.createdDate = createdDate;
	}

	public List<AbstractDomainObject> getSpecimenList()
	{
		return this.specimenList;
	}

	public void setSpecimenList(final List<AbstractDomainObject> specimenList)
	{
		if (specimenList != null)
		{
			this.specimenList = specimenList;
		}
	}

	public String getScgName()
	{
		return this.scgName;
	}

	public void setScgName(final String scgName)
	{
		this.scgName = scgName;
	}

	public String getPrinterLocation()
	{
		return this.printerLocation;
	}

	public void setPrinterLocation(final String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return this.printerType;
	}

	public void setPrinterType(final String printerType)
	{
		this.printerType = printerType;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub
	}
}
