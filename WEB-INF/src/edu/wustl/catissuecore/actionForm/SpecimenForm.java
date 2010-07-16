/**
 * <p>Title: SpecimenForm Class>
 * <p>Description:  SpecimenForm Class is used to encapsulate all the request parameters passed
 * from New/Create Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenForm Class is used to encapsulate all the request parameters passed
 * from New/Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class SpecimenForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenForm.class);

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String className = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_SPECIMEN);

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_SPECIMEN_TYPE);

	/**
	 * Specifies if the barcode is editable or not
	 */
	private String isBarcodeEditable = (String) DefaultValueManager
			.getDefaultValue(Constants.IS_BARCODE_EDITABLE);

	/**
	 * Concentration of specimen.
	 */
	protected String concentration;

	/**
	 * Amount of Specimen.
	 */
	protected String quantity = "0";

	/**
	 * Available Amount of Specimen.
	 */
	protected String availableQuantity;

	/**
	 * Is quantity available?
	 */
	protected boolean available;

	/**
	 * Unit of specimen.
	 */
	protected String unit;

	/**
	 * A physically discreet container that is used to store a specimen.
	 * e.g. Box, Freezer etc
	 */
	protected String storageContainer = "";

	/**
	 * Reference to dimensional position one of the specimen in Storage Container.
	 */
	protected String positionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in Storage Container.
	 */
	protected String positionDimensionTwo;

	/**
	 * Barcode assigned to the specimen.
	 */
	protected String barcode;

	/**
	 * Comments on specimen.
	 */
	protected String comments = "";

	/**
	 * A label name of this specimen.
	 */
	protected String label;

	/**
	 * Number of external identifier rows.
	 */
	protected int exIdCounter = 1;

	/**
	 * Parent Container Identifier.
	 */
	private String parentContainerId;

	private String buttonClicked = "";

	protected String positionInStorageContainer;

	/**
	 * Map to handle all the data of external identifiers.
	 */
	protected Map externalIdentifier = new HashMap();

	protected boolean virtuallyLocated;

	/**
	 * Identify whether coming from multiple specimen or simple specimen page.
	 * if "1" then from multipleSpecimen
	 * else from other page
	 */
	protected String multipleSpecimen = "0";

	/**
	 * Radio button to choose dropdown or map to select storage container.
	 */
	private int stContSelection = 1;
	/**
	 * Storage container name selected from map
	 */
	private String selectedContainerName;
	/**
	 * Storage pos1 selected from map
	 */
	private String pos1;
	/**
	 * Storage pos2 selected from map
	 */
	private String pos2;
	/**
	 * Storage Id selected from map
	 */
	private String containerId;

	protected boolean generateLabel;

	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	/**
	 * Patch ID: 3835_1_29
	 * See also: 1_1 to 1_5
	 * Description : Added createdDate in form for domain object's createdOn field
	 */

	private String createdDate;

	/**
	 * Returns the concentration.
	 * @return String the concentration.
	 * @see #setConcentration(String)
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * Sets the concentration.
	 * @param concentration The concentration.
	 * @see #getConcentration()
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setExternalIdentifierValue(String key, Object value)
	{

		if (this.isMutable())
		{
			this.externalIdentifier.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getExternalIdentifierValue(String key)
	{
		return this.externalIdentifier.get(key);
	}

	/**
	 * Returns all the values in the map.
	 * @return Collection all the values in the map.
	 */
	public Collection getAllExternalIdentifiers()
	{
		return this.externalIdentifier.values();
	}

	/**
	 * Sets the map.
	 * @param externalIdentifier the map to be set.
	 * @see #getExternalIdentifier()
	 */
	public void setExternalIdentifier(Map externalIdentifier)
	{
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * Returns the map of external identifiers.
	 * @return Map the map of external identifiers.
	 * @see #setExternalIdentifier(Map)
	 */
	public Map getExternalIdentifier()
	{
		return this.externalIdentifier;
	}

	/**
	 * Returns the comments.
	 * @return String the comments.
	 * @see #setComments(String)
	 */
	public String getComments()
	{
		return this.comments;
	}

	/**
	 * Sets the comments.
	 * @param comments The comments.
	 * @see #getComments()
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * Returns the position dimension one.
	 * @return String the position dimension one.
	 * @see #setPositionDimensionOne(String)
	 */
	public String getPositionDimensionOne()
	{
		return this.positionDimensionOne;
	}

	/**
	 * Sets the position dimension one.
	 * @param positionDimensionOne The position dimension one.
	 * @see #getPositionDimensionOne()
	 */
	public void setPositionDimensionOne(String positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}

	/**
	 * Returns the position dimension two.
	 * @return String the position dimension two.
	 * @see #setPositionDimensionTwo(String)
	 */
	public String getPositionDimensionTwo()
	{
		return this.positionDimensionTwo;
	}

	/**
	 * Sets the position dimension two.
	 * @param positionDimensionTwo The position dimension two.
	 * @see #getPositionDimensionTwo()
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * Returns the barcode of this specimen.
	 * @return String the barcode of this specimen.
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return this.barcode;
	}

	/**
	 * Sets the barcode of this specimen.
	 * @param barcode The barcode of this specimen.
	 * @see #getBarcode()
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * Returns the quantity.
	 * @return String the quantity.
	 * @see #setQuantity(String)
	 */
	public String getQuantity()
	{
		return this.quantity;
	}

	/**
	 * Sets the quantity.
	 * @param quantity The quantity.
	 * @see #getQuantity()
	 */
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Returns the available quantity.
	 * @return String the available quantity.
	 * @see #setAvailableQuantity(String)
	 */
	public String getAvailableQuantity()
	{
		return this.availableQuantity;
	}

	/**
	 * Sets the available quantity.
	 * @param availableQuantity The available quantity.
	 * @see #getAvailableQuantity()
	 */
	public void setAvailableQuantity(String availableQuantity)
	{
		if (this.isMutable())
		{
			this.availableQuantity = availableQuantity;
		}

	}

	/**
	 * Returns the unit of this specimen.
	 * @return String the unit of this specimen.
	 * @see #setUnit(String)
	 */
	public String getUnit()
	{
		return this.unit;
	}

	/**
	 * Sets the unit of this specimen.
	 * @param unit The unit of this specimen.
	 * @see #getUnit()
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	/**
	 * Returns the storage container of this specimen.
	 * @return String the storage container of this specimen.
	 * @see #setStorageContainer(String)
	 */
	public String getStorageContainer()
	{
		return this.storageContainer;
	}

	/**
	 * Sets the storage container of this specimen.
	 * @param storageContainer The storage container of this specimen.
	 * @see #getStorageContainer()
	 */
	public void setStorageContainer(String storageContainer)
	{
		this.storageContainer = storageContainer;
	}

	/**
	 * Returns the subtype of this specimen.
	 * @return String the subtype of this specimen.
	 * @see #setType(String)
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the subtype of this specimen.
	 * @param subType The subtype of this specimen.
	 * @see #getType()
	 */
	public void setType(String subType)
	{
		this.type = subType;
	}

	/**
	 * Returns the className of this specimen.
	 * @return String the className of this specimen.
	 * @see #setClassName(String)
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * Sets the className of this specimen.
	 * @param className The className of this specimen.
	 * @see #getClassName()
	 */
	public void setClassName(String className)
	{
		if (className != null)
		{
			className = className.trim();
		}
		this.className = className;
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
		this.className = null;
		this.type = null;
		this.storageContainer = null;
		this.comments = null;
		this.externalIdentifier = new HashMap();
	}

	/**
	 * @return Returns the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return -1;
	}

	/**
	 * @return Returns the pos2.
	 */
	public String getPos2()
	{
		return this.pos2;
	}

	/**
	 * @param pos2 The pos2 to set.
	 */
	public void setPos2(String pos2)
	{
		this.pos2 = pos2;
	}

	/**
	 * @return Returns the selectedContainerName.
	 */
	public String getSelectedContainerName()
	{
		return this.selectedContainerName;
	}

	/**
	 * @param selectedContainerName The selectedContainerName to set.
	 */
	public void setSelectedContainerName(String selectedContainerName)
	{
		this.selectedContainerName = selectedContainerName;
	}

	/**
	 * @return Returns the stContSelection.
	 */
	public int getStContSelection()
	{
		return this.stContSelection;
	}

	/**
	 * @param stContSelection The stContSelection to set.
	 */
	public void setStContSelection(int stContSelection)
	{
		this.stContSelection = stContSelection;
	}

	/**
	 * This function Copies the data from an Specimen object to a SpecimenForm object.
	 * @param abstractDomain An object containing the information about site.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final Specimen specimen = (Specimen) abstractDomain;
		this.setId(specimen.getId().longValue());
		this.type = specimen.getSpecimenType();
		this.concentration = "";
		this.comments = specimen.getComment();
		this.setActivityStatus(specimen.getActivityStatus());
//		this.generateLabel=specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel();
		/**
		 * Patch ID: 3835_1_30
		 * See also: 1_1 to 1_5
		 * Description : set createdOn date from database object
		 */
		this.createdDate = CommonUtilities.parseDateToString(specimen.getCreatedOn(),
				CommonServiceLocator.getInstance().getDatePattern());

		if (specimen.getIsAvailable() != null)
		{
			this.available = specimen.getIsAvailable().booleanValue();
		}

		if (specimen != null && specimen.getSpecimenPosition() != null)
		{
			final StorageContainer container = specimen.getSpecimenPosition().getStorageContainer();
			logger.info("-----------Container while getting from domain--:" + container);
			this.storageContainer = String.valueOf(container.getId());
			this.selectedContainerName = container.getName();
			this.positionDimensionOne = String.valueOf(specimen.getSpecimenPosition()
					.getPositionDimensionOne());
			this.positionDimensionTwo = String.valueOf(specimen.getSpecimenPosition()
					.getPositionDimensionTwo());
			this.positionInStorageContainer = container.getStorageType().getName() + " : "
					+ container.getId() + " Pos(" + this.positionDimensionOne + ","
					+ this.positionDimensionTwo + ")";
			this.setStContSelection(2);
		}
		//Bug 12374 and 12662
		//if the condition is true means specimen is virtually located.
		else if (specimen.getSpecimenPosition() == null
				&& specimen.getCollectionStatus().equals(Constants.COLLECTION_STATUS_COLLECTED))
		{
			this.setStContSelection(Constants.STORAGE_TYPE_POSITION_VIRTUAL_VALUE);
		}
		else
		{
			// bug #11177
			if (specimen.getSpecimenRequirement() != null)
			{
				final String storageType = specimen.getSpecimenRequirement().getStorageType();
				if (storageType.equals(Constants.STORAGE_TYPE_POSITION_VIRTUAL))
				{
					this.setStContSelection(Constants.STORAGE_TYPE_POSITION_VIRTUAL_VALUE);
				}
				else if (storageType.equals(Constants.STORAGE_TYPE_POSITION_AUTO))
				{
					this.setStContSelection(Constants.STORAGE_TYPE_POSITION_AUTO_VALUE);
				}
				else if (storageType.equals(Constants.STORAGE_TYPE_POSITION_MANUAL))
				{
					this.setStContSelection(Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE);
				}
			}
		}

		this.barcode = specimen.getBarcode();
		this.label = specimen.getLabel();

		/* ********* Aniruddha :16/06/06 TO BE DELETED LATER ***********
		 * if (specimen instanceof CellSpecimen)
		 {
		 this.className = "Cell";
		 this.quantity = String.valueOf(((CellSpecimen) specimen)
		 .getQuantityInCellCount());
		 this.availableQuantity = String.valueOf(((CellSpecimen) specimen).getAvailableQuantityInCellCount());
		 }
		 else if (specimen instanceof FluidSpecimen)
		 {
		 this.className = "Fluid";
		 this.quantity = String.valueOf(((FluidSpecimen) specimen)
		 .getQuantityInMilliliter());
		 this.availableQuantity = String.valueOf(((FluidSpecimen) specimen).getAvailableQuantityInMilliliter());
		 }
		 else if (specimen instanceof MolecularSpecimen)
		 {
		 this.className = "Molecular";
		 this.quantity = String.valueOf(((MolecularSpecimen) specimen)
		 .getQuantityInMicrogram());
		 this.availableQuantity = String.valueOf(((MolecularSpecimen) specimen).getAvailableQuantityInMicrogram());
		 if (((MolecularSpecimen) specimen)
		 .getConcentrationInMicrogramPerMicroliter() != null)
		 this.concentration = String
		 .valueOf(((MolecularSpecimen) specimen)
		 .getConcentrationInMicrogramPerMicroliter());
		 }
		 else if (specimen instanceof TissueSpecimen)
		 {
		 this.className = "Tissue";
		 this.quantity = String.valueOf(((TissueSpecimen) specimen)
		 .getQuantityInGram());
		 this.availableQuantity = String.valueOf(((TissueSpecimen) specimen).getAvailableQuantityInGram());
		 }*/

		if (specimen instanceof CellSpecimen)
		{
			this.className = Constants.CELL;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			this.className = Constants.FLUID;
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			this.className = Constants.MOLECULAR;
			this.concentration = CommonUtilities.toString(((MolecularSpecimen) specimen)
					.getConcentrationInMicrogramPerMicroliter());
		}
		else if (specimen instanceof TissueSpecimen)
		{
			this.className = Constants.TISSUE;
		}
		else
		{
			this.className = specimen.getParentSpecimen().getClassName();
		}

		if (!AppUtility.isQuantityDouble(this.className, this.type))
		{
			Double doubleQuantity = specimen.getInitialQuantity();
			if (doubleQuantity.toString().contains("E"))
			{
				this.quantity = doubleQuantity.toString();
			}
			else
			{
				final long longQuantity = doubleQuantity.longValue();
				this.quantity = new Long(longQuantity).toString();
			}

			doubleQuantity = specimen.getAvailableQuantity();
			if (doubleQuantity.toString().contains("E"))
			{
				this.availableQuantity = doubleQuantity.toString();
			}
			else
			{
				this.availableQuantity = new Long(doubleQuantity.longValue()).toString();
			}

			//			long intQuantity = (long) specimen.getQuantity().getValue().doubleValue();
			//			long intAvailableQuantity = (long) specimen.getAvailableQuantity().getValue().doubleValue();
			//
			//			this.quantity = new Long(intQuantity).toString();
			//			this.availableQuantity = new Long(intAvailableQuantity).toString();

			//			this.quantity = new BigDecimal(specimen.getQuantity().getValue().toString()).toPlainString();
			//			this.availableQuantity = new BigDecimal(specimen.getQuantity().getValue().toString()).toPlainString();
		}
		else
		{
			this.quantity = specimen.getInitialQuantity().toString();
			this.availableQuantity = specimen.getAvailableQuantity().toString();
		}

		specimen.getSpecimenCharacteristics();

		final Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		this.exIdCounter = 1;

		if (externalIdentifierCollection != null && externalIdentifierCollection.size() != 0)
		{
			this.externalIdentifier = new HashMap();

			int i = 1;

			final Iterator it = externalIdentifierCollection.iterator();

			while (it.hasNext())
			{
				final String key1 = "ExternalIdentifier:" + i + "_name";
				final String key2 = "ExternalIdentifier:" + i + "_value";
				final String key3 = "ExternalIdentifier:" + i + "_id";

				final ExternalIdentifier externalId = (ExternalIdentifier) it.next();

				this.externalIdentifier.put(key1, externalId.getName());
				this.externalIdentifier.put(key2, externalId.getValue());
				this.externalIdentifier.put(key3, String.valueOf(externalId.getId()));

				i++;
			}

			this.exIdCounter = externalIdentifierCollection.size();
		}
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		try
		{
			if (this.getOperation().equals(Constants.ADD)
					|| this.getOperation().equals(Constants.EDIT))
			{
				/**
				     * Patch ID: 3835_1_31
				     * See also: 1_1 to 1_5
				     * Description : Validated the createdOn date field.
				     */
				if (!Validator.isEmpty(this.createdDate))
				{

					final String errorKeyForCreatedDate = validator.validateDate(this.createdDate,
							true);
					if (errorKeyForCreatedDate.trim().length() > 0)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								errorKeyForCreatedDate, ApplicationProperties
										.getValue("specimen.createdDate")));
					}
				}

				//Changed by falguni
//				if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl
				
				//Changed by Santosh Ganacharya
				if((!Variables.isSpecimenLabelGeneratorAvl)&&(!this.generateLabel) && (Validator.isEmpty(this.label)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.label")));
				}

				if (Validator.isEmpty(this.className))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.type")));
				}
				if (Validator.isEmpty(this.type))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.subType")));
				}

				boolean isQuantityValid = true;
				if (!Validator.isEmpty(this.quantity))
				{
					try
					{
						this.quantity = new BigDecimal(this.quantity).toPlainString();
						if (AppUtility.isQuantityDouble(this.className, this.type))
						{
							if (!validator.isDouble(this.quantity, true))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("specimen.quantity")));
							}
						}
						else
						{
							if (!validator.isNumeric(this.quantity, 0))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("specimen.quantity")));
							}
						}

						//bug#7788
						if (Constants.FIXED_TISSUE_BLOCK.equals(this.type)
								|| Constants.FIXED_TISSUE_SLIDE.equals(this.type)
								|| Constants.FROZEN_TISSUE_BLOCK.equals(this.type)
								|| Constants.FROZEN_TISSUE_SLIDE.equals(this.type)
								|| Constants.NOT_SPECIFIED.equals(this.type))
						{
							final Double initialFloorQty = Math.floor(Double
									.parseDouble(this.quantity));
							final Double initialQty = Double.parseDouble(this.quantity);
							final int initialQtyDiff = initialQty.compareTo(initialFloorQty);
							if (initialQtyDiff != 0)
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.specimen.quantity", ApplicationProperties
												.getValue("specimen.quantity"), this.type));
							}
						}
						//#7788 ends
					}
					catch (final NumberFormatException exp)
					{
						SpecimenForm.logger.error(exp.getMessage(),exp);
						exp.printStackTrace();
						isQuantityValid = false;
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("specimen.quantity")));
					}

				}
				else
				{
					this.quantity = "0";
				}
				if (this instanceof NewSpecimenForm
						&& this.getOperation().equalsIgnoreCase(Constants.EDIT))
				{
					if (!Validator.isEmpty(this.availableQuantity))
					{
						try
						{
							this.availableQuantity = new BigDecimal(this.availableQuantity)
									.toPlainString();
							if (isQuantityValid
									&& 1 == new BigDecimal(this.availableQuantity)
											.compareTo(new BigDecimal(this.quantity)))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.availablequantity", ApplicationProperties
												.getValue("specimen.availableQuantity")));
							}
							if (AppUtility.isQuantityDouble(this.className, this.type))
							{
								if (!validator.isDouble(this.availableQuantity, true))
								{
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
											"errors.item.format", ApplicationProperties
													.getValue("specimen.availableQuantity")));
								}
							}
							else
							{
								if (!validator.isNumeric(this.quantity, 0))
								{
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
											"errors.item.format", ApplicationProperties
													.getValue("specimen.availableQuantity")));
								}
							}

							//bug#7788
							if (Constants.FIXED_TISSUE_BLOCK.equals(this.type)
									|| Constants.FIXED_TISSUE_SLIDE.equals(this.type)
									|| Constants.FROZEN_TISSUE_BLOCK.equals(this.type)
									|| Constants.FROZEN_TISSUE_SLIDE.equals(this.type)
									|| Constants.NOT_SPECIFIED.equals(this.type))
							{
								final Double availableFloorQty = Math.floor(Double
										.parseDouble(this.availableQuantity));
								final Double availableQty = Double
										.parseDouble(this.availableQuantity);
								final int availableQtyDiff = availableQty
										.compareTo(availableFloorQty);
								if (availableQtyDiff != 0)
								{
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
											"errors.specimen.quantity", ApplicationProperties
													.getValue("specimen.availableQuantity"),
											this.type));
								}
							}
							//#7788 ends
						}
						catch (final NumberFormatException exp)
						{
							SpecimenForm.logger.error(exp.getMessage(),exp);
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.format", ApplicationProperties
											.getValue("specimen.availableQuantity")));
						}
					}
					else
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("specimen.availableQuantity")));
					}
				}

				// If not multiple specimen then validate storage container
				if (!this.multipleSpecimen.equals("1"))
				{
					//					if (!isVirtuallyLocated())
					//					{
					//						if(stContSelection==2)
					//						{
					//							if (validator.isEmpty(positionDimensionOne)
					//									|| validator.isEmpty(positionDimensionTwo)
					//									|| validator.isEmpty(storageContainer))
					//							{
					//								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					//										"errors.item.required", ApplicationProperties
					//												.getValue("specimen.positionInStorageContainer")));
					//							}
					//							else
					//							{
					//								if (!validator.isNumeric(positionDimensionOne, 1)
					//										|| !validator.isNumeric(positionDimensionTwo, 1)
					//										|| !validator.isNumeric(storageContainer, 1))
					//								{
					//									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					//											"errors.item.format", ApplicationProperties
					//													.getValue("specimen.positionInStorageContainer")));
					//								}
					//
					//							}
					//						}
					//						else if(stContSelection==3)
					//						{
					//
					//						}
					//					}
				}
				//Validations for External Identifier Add-More Block
				final String className = "ExternalIdentifier:";
				final String key1 = "_name";
				final String key2 = "_value";
				final String key3 = "_" + Constants.SYSTEM_IDENTIFIER;

				int index = 1;

				while (true)
				{
					final String keyOne = className + index + key1;
					final String keyTwo = className + index + key2;
					final String keyThree = className + index + key3;

					final String value1 = (String) this.externalIdentifier.get(keyOne);
					final String value2 = (String) this.externalIdentifier.get(keyTwo);

					if (value1 == null || value2 == null)
					{
						break;
					}
					else if (value1.trim().equals("") && value2.trim().equals(""))
					{
						this.externalIdentifier.remove(keyOne);
						this.externalIdentifier.remove(keyTwo);
						this.externalIdentifier.remove(keyThree);
					}
					else if ((!value1.trim().equals("") && value2.trim().equals(""))
							|| (value1.trim().equals("") && !value2.trim().equals("")))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.specimen.externalIdentifier.missing", ApplicationProperties
										.getValue("specimen.msg")));
						break;
					}
					index++;
				}
				final boolean flag = StorageContainerUtil.checkPos1AndPos2(this.pos1, this.pos2);
				if (flag)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("specimen.positionInStorageContainer")));
				}
			}
		}
		catch (final Exception excp)
		{
			SpecimenForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
		}
		return errors;
	}

	/**
	 * Returns the counter that holds no. of external identifier rows.
	 * @return The counter that holds no. of external identifier rows.
	 * @see #setExIdCounter(int)
	 */
	public int getExIdCounter()
	{
		return this.exIdCounter;
	}

	/**
	 * Sets the counter that holds no. of external identifier rows.
	 * @param exIdCounter The counter that holds no. of external identifier rows.
	 * @see #getExIdCounter()
	 */
	public void setExIdCounter(int exIdCounter)
	{
		this.exIdCounter = exIdCounter;
	}

	/**
	 * Returns the position in storage container.
	 * @return The position in storage container.
	 * @see #setPositionInStorageContainer(String)
	 */
	public String getPositionInStorageContainer()
	{
		return this.positionInStorageContainer;
	}

	/**
	 * Sets the position in storage container.
	 * @param positionInStorageContainer The position in storage container.
	 * @see #getPositionInStorageContainer()
	 */
	public void setPositionInStorageContainer(String positionInStorageContainer)
	{
		this.positionInStorageContainer = positionInStorageContainer;
	}

	/**
	 * Returns True/False, whether the quatity is available or not.
	 * @return True/False, whether the quatity is available or not.
	 * @see #setAvailable(boolean)
	 */
	public boolean isAvailable()
	{
		return this.available;
	}

	/**
	 * Sets True/False depending upon the availability of the quantity.
	 * @param available True/False depending upon the availability of the quantity.
	 * @see #getAvailable()
	 */
	public void setAvailable(boolean available)
	{
		this.available = available;
	}

	/**
	 * Returns the parent container id.
	 * @return The parent container id.
	 * @see #setParentContainerId(String)
	 */
	public String getParentContainerId()
	{
		return this.parentContainerId;
	}

	/**
	 * Sets the parent container id.
	 * @param parentContainerId The parent container id.
	 * @see #getParentContainerId()
	 */
	public void setParentContainerId(String parentContainerId)
	{
		this.parentContainerId = parentContainerId;
	}

	/**
	 * Returns the label name of specimen.
	 * @return the label name of specimen.
	 * @see #setLabel(String)
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * @param label Sets the label name of specimen.
	 * @see #getLabel()
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return virtuallyLocated
	 */
	public boolean isVirtuallyLocated()
	{
		return this.virtuallyLocated;
	}

	/**
	 * @return true or false
	 */
	public String getVirtuallyLocated()
	{
		if (this.virtuallyLocated)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}

	/**
	 * @param virtuallyLocated Set virtuallyLocated
	 */
	public void setVirtuallyLocated(boolean virtuallyLocated)
	{
		this.virtuallyLocated = virtuallyLocated;
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
	public void setButtonClicked(String buttonClicked)
	{
		this.buttonClicked = buttonClicked;
	}

	/**
	 * @return Returns the multipleSpecimen.
	 */
	public String getMultipleSpecimen()
	{
		return this.multipleSpecimen;
	}

	/**
	 * @param multipleSpecimen The multipleSpecimen to set.
	 */
	public void setMultipleSpecimen(String multipleSpecimen)
	{
		this.multipleSpecimen = multipleSpecimen;
	}

	/**
	 * Patch ID: 3835_1_32
	 * See also: 1_1 to 1_5
	 * @return createdDate
	 * Description : getter setter methods  for createdOn date.
	 */
	public String getCreatedDate()
	{
		return this.createdDate;
	}

	/**
	 * @param createdDate Set Create Date
	 */
	public void setCreatedDate(String createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * @return isBarcodeEditable
	 */
	public String getIsBarcodeEditable()
	{
		return this.isBarcodeEditable;
	}

	/**
	 * @return Returns the containerId.
	 */
	public String getContainerId()
	{
		return this.containerId;
	}

	/**
	 * @param containerId The containerId to set.
	 */
	public void setContainerId(String containerId)
	{
		this.containerId = containerId;
	}

	/**
	 * @return Returns the pos1.
	 */
	public String getPos1()
	{
		return this.pos1;
	}

	/**
	 * @param pos1 The pos1 to set.
	 */
	public void setPos1(String pos1)
	{
		this.pos1 = pos1;
	}

	/**
	 * @param isBarcodeEditable Setter method for isBarcodeEditable
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}