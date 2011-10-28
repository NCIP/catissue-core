
package edu.wustl.catissuecore.actionForm;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateSpecimenTemplateForm.
 */
public class CreateSpecimenTemplateForm extends AbstractActionForm
{

	/** serial version id. */
	private static final long serialVersionUID = 1L;

	/** logger Logger - Generic logger. */
	private static Logger logger = Logger.getCommonLogger(CreateSpecimenTemplateForm.class);

	/** Display Name. */
	protected String displayName;

	/** Type of specimen. e.g. Tissue, Molecular, Cell, Fluid */
	protected String className;

	/** Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc. */
	protected String type;

	/** Anatomic site from which the specimen was derived. */
	private String tissueSite;

	/** For bilateral sites, left or right. */
	private String tissueSide;

	/** Histopathological character of the specimen e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant. */
	private String pathologicalStatus;

	/** The storage location for specimen. */
	private String storageLocationForSpecimen;

	/** The storage location for aliquot specimen. */
	private String storageLocationForAliquotSpecimen;

	/** Concentration of specimen. */
	protected String concentration = "0";

	/** Amount of Specimen. */
	protected String quantity;

	/** A historical information about the specimen i.e. whether the specimen is a new specimen or a derived specimen or an aliquot. */
	private String lineage;

	/** Unit of specimen. */
	protected String unit;

	/** A number that tells how many aliquots to be created. */
	private String noOfAliquots;

	/** Initial quantity per aliquot. */
	private String quantityPerAliquot;

	/** Collection of aliquot specimens derived from this specimen. */
	protected Collection aliquotSpecimenCollection;

	/** Collection of derive specimens derived from this specimen. */
	protected Collection deriveSpecimenCollection;

	/** The collection event id. */
	private long collectionEventId; // Mandar : CollectionEvent 10-July-06

	/** The collection event specimen id. */
	private long collectionEventSpecimenId;

	/** The collection event user id. */
	//private long collectionEventUserId;

	/** The collection user name. */
	private String collectionUserName = null;

	/**
	 * Gets the collection user name.
	 *
	 * @return collectionUserName
	 */
	public String getCollectionUserName()
	{
		return this.collectionUserName;
	}

	/**
	 * Sets the collection user name.
	 *
	 * @param collectionUserName collectionUserName
	 */
	public void setCollectionUserName(String collectionUserName)
	{
		this.collectionUserName = collectionUserName;
	}

	/** The received event id. */
	private long receivedEventId;

	/** The received event specimen id. */
	private long receivedEventSpecimenId;

	/** The received event user id. */
	private long receivedEventUserId;

	/** The received user name. */
	private String receivedUserName = null;

	private String processingSPPForSpecimen;

	private String creationEventForSpecimen;

	public String getProcessingSPPForSpecimen() {
		return processingSPPForSpecimen;
	}

	public void setProcessingSPPForSpecimen(String processingSPPForSpecimen) {
		this.processingSPPForSpecimen = processingSPPForSpecimen;
	}

	public String getCreationEventForSpecimen() {
		return creationEventForSpecimen;
	}

	public void setCreationEventForSpecimen(
			String creationEventForSpecimen) {
		this.creationEventForSpecimen = creationEventForSpecimen;
	}

	private String processingSPPForAliquot;

	private String creationEventForAliquot;


	public String getProcessingSPPForAliquot() {
		return processingSPPForAliquot;
	}

	public void setProcessingSPPForAliquot(String processingSPPForAliquot) {
		this.processingSPPForAliquot = processingSPPForAliquot;
	}

	public String getCreationEventForAliquot() {
		return creationEventForAliquot;
	}

	public void setCreationEventForAliquot(String creationEventForAliquot) {
		this.creationEventForAliquot = creationEventForAliquot;
	}

	/**
	 * Gets the received user name.
	 *
	 * @return receivedUserName
	 */
	public String getReceivedUserName()
	{
		return this.receivedUserName;
	}

	/**
	 * Sets the received user name.
	 *
	 * @param receivedUserName receivedUserName
	 */
	public void setReceivedUserName(String receivedUserName)
	{
		this.receivedUserName = receivedUserName;
	}

	/** The collection event collection procedure. */
	private String collectionEventCollectionProcedure;

	/** The collection event container. */
	private String collectionEventContainer;

	/** The received event received quality. */
//	private String receivedEventReceivedQuality;

	/** The no of derive specimen. */
	private int noOfDeriveSpecimen;

	/** The derive specimen values. */
	private Map deriveSpecimenValues = new LinkedHashMap();

	/** The node key. */
	private String nodeKey = null;

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * Sets the class name.
	 *
	 * @param className the new class name
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Gets the tissue site.
	 *
	 * @return the tissue site
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * Sets the tissue site.
	 *
	 * @param tissueSite the new tissue site
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Gets the concentration.
	 *
	 * @return the concentration
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * Sets the concentration.
	 *
	 * @param concentration the new concentration
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return this.quantity;
	}

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Gets the no of aliquots.
	 *
	 * @return the no of aliquots
	 */
	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	/**
	 * Sets the no of aliquots.
	 *
	 * @param noOfAliquots the new no of aliquots
	 */
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
		if (noOfAliquots != null && noOfAliquots.equals("0"))
		{
			this.noOfAliquots = "";
		}
	}

	/**
	 * Gets the quantity per aliquot.
	 *
	 * @return the quantity per aliquot
	 */
	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	/**
	 * Sets the quantity per aliquot.
	 *
	 * @param quantityPerAliquot the new quantity per aliquot
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	/**
	 * Gets the collection event collection procedure.
	 *
	 * @return the collection event collection procedure
	 */
	/*public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}*/

	/**
	 * Sets the collection event collection procedure.
	 *
	 * @param collectionEventCollectionProcedure the new collection event collection procedure
	 */
	/*public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}*/

	/**
	 * Gets the collection event container.
	 *
	 * @return the collection event container
	 */
	/*public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}*/

	/**
	 * Sets the collection event container.
	 *
	 * @param collectionEventContainer the new collection event container
	 */
	/*public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}
*/
	/**
	 * Gets the received event received quality.
	 *
	 * @return the received event received quality
	 */
	/*public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}*/

	/**
	 * Sets the received event received quality.
	 *
	 * @param receivedEventReceivedQuality the new received event received quality
	 */
	/*public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}*/

	/**
	 * Gets the no of derive specimen.
	 *
	 * @return the no of derive specimen
	 */
	public int getNoOfDeriveSpecimen()
	{
		return this.noOfDeriveSpecimen;
	}

	/**
	 * Sets the no of derive specimen.
	 *
	 * @param noOfDeriveSpecimen the new no of derive specimen
	 */
	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	/**
	 * Gets the derive specimen value.
	 *
	 * @param key the key
	 *
	 * @return the derive specimen value
	 */
	public Object getDeriveSpecimenValue(String key)
	{
		return this.deriveSpecimenValues.get(key);
	}

	/**
	 * Sets the derive specimen value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setDeriveSpecimenValue(String key, Object value)
	{
		if (this.isMutable())
		{
			if (this.deriveSpecimenValues == null)
			{
				this.deriveSpecimenValues = new LinkedHashMap();
			}
			this.deriveSpecimenValues.put(key, value);
		}
	}

	/**
	 * Gets the derive specimen values.
	 *
	 * @return the derive specimen values
	 */
	public Map getDeriveSpecimenValues()
	{
		return this.deriveSpecimenValues;
	}

	/**
	 * Sets the derive specimen values.
	 *
	 * @param deriveSpecimenValues the new derive specimen values
	 */
	public void setDeriveSpecimenValues(Map deriveSpecimenValues)
	{
		this.deriveSpecimenValues = deriveSpecimenValues;
	}

	/**
	 * Gets the all derive specimen valuess.
	 *
	 * @return Returns the values.
	 */
	public Collection getAllDeriveSpecimenValuess()
	{
		return this.deriveSpecimenValues.values();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 */
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 */
	@Override
	protected void reset()
	{

	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.IValueObject#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the lineage.
	 *
	 * @return the lineage
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Sets the lineage.
	 *
	 * @param lineage the new lineage
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * Gets the aliquot specimen collection.
	 *
	 * @return the aliquot specimen collection
	 */
	public Collection getAliquotSpecimenCollection()
	{
		return this.aliquotSpecimenCollection;
	}

	/**
	 * Sets the aliquot specimen collection.
	 *
	 * @param aliquotSpecimenCollection the new aliquot specimen collection
	 */
	public void setAliquotSpecimenCollection(Collection aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}

	/**
	 * Gets the derive specimen collection.
	 *
	 * @return the derive specimen collection
	 */
	public Collection getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	/**
	 * Sets the derive specimen collection.
	 *
	 * @param deriveSpecimenCollection the new derive specimen collection
	 */
	public void setDeriveSpecimenCollection(Collection deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * Gets the unit.
	 *
	 * @return the unit
	 */
	public String getUnit()
	{
		return this.unit;
	}

	/**
	 * Sets the unit.
	 *
	 * @param unit the new unit
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	/**
	 * Gets the collection event id.
	 *
	 * @return the collection event id
	 */
	public long getCollectionEventId()
	{
		return this.collectionEventId;
	}

	/**
	 * Sets the collection event id.
	 *
	 * @param collectionEventId the new collection event id
	 */
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}

	/**
	 * Gets the collection event specimen id.
	 *
	 * @return the collection event specimen id
	 */
	public long getCollectionEventSpecimenId()
	{
		return this.collectionEventSpecimenId;
	}

	/**
	 * Gets the collection event user id.
	 *
	 * @return the collection event user id
	 */
	/*public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
	}
*/
	/**
	 * Sets the collection event specimen id.
	 *
	 * @param collectionEventSpecimenId the new collection event specimen id
	 */
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}

	/**
	 * Gets the received event id.
	 *
	 * @return the received event id
	 */
	public long getReceivedEventId()
	{
		return this.receivedEventId;
	}

	/**
	 * Sets the collection event user id.
	 *
	 * @param collectionEventUserId the new collection event user id
	 */
	/*public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}*/



	/**
	 * Sets the received event id.
	 *
	 * @param receivedEventId the new received event id
	 */
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}



	/**
	 * Sets the received event specimen id.
	 *
	 * @param receivedEventSpecimenId the new received event specimen id
	 */
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}

	/**
	 * Gets the received event user id.
	 *
	 * @return the received event user id
	 */
	public long getReceivedEventUserId()
	{
		return this.receivedEventUserId;
	}

	/**
	 * Gets the received event specimen id.
	 *
	 * @return the received event specimen id
	 */
	public long getReceivedEventSpecimenId()
	{
		return this.receivedEventSpecimenId;
	}

	/**
	 * Sets the received event user id.
	 *
	 * @param receivedEventUserId the new received event user id
	 */
	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}

	/**
	 * Gets the storage location for specimen.
	 *
	 * @return the storage location for specimen
	 */
	public String getStorageLocationForSpecimen()
	{
		return this.storageLocationForSpecimen;
	}

	/**
	 * Sets the storage location for specimen.
	 *
	 * @param storageLocationForSpecimen the new storage location for specimen
	 */
	public void setStorageLocationForSpecimen(String storageLocationForSpecimen)
	{
		this.storageLocationForSpecimen = storageLocationForSpecimen;
	}

	/**
	 * Gets the storage location for aliquot specimen.
	 *
	 * @return the storage location for aliquot specimen
	 */
	public String getStorageLocationForAliquotSpecimen()
	{
		return this.storageLocationForAliquotSpecimen;
	}

	/**
	 * Sets the storage location for aliquot specimen.
	 *
	 * @param storageLocationForAliquotSpecimen the new storage location for aliquot specimen
	 */
	public void setStorageLocationForAliquotSpecimen(String storageLocationForAliquotSpecimen)
	{
		this.storageLocationForAliquotSpecimen = storageLocationForAliquotSpecimen;
	}

	/**
	 * Gets the tissue side.
	 *
	 * @return the tissue side
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * Sets the tissue side.
	 *
	 * @param tissueSide the new tissue side
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * Gets the pathological status.
	 *
	 * @return the pathological status
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * Sets the pathological status.
	 *
	 * @param pathologicalStatus the new pathological status
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		double aliquotQuantity = 0;
		double initialQuantity = 0;
		try
		{
			if (validator.isEmpty(this.className))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.type")));
			}
//			if(this.labelGenType.equals("2") && validator.isEmpty(this.labelFormat))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
//						"collectionProtocol.labelformat", ApplicationProperties
//						.getValue("collectionProtocol.labelformat")));
//			}
			if (validator.isEmpty(this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.subType")));
			}
			final List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_SPECIMEN_CLASS, null);
			if (!Validator.isEnumeratedValue(specimenClassList, this.className))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.type")));
			}

			if (!Validator
					.isEnumeratedValue(AppUtility.getSpecimenTypes(this.className), this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.subType")));
			}

			if (!validator.isEmpty(this.quantity))
			{
				try
				{
					this.quantity = new BigDecimal(this.quantity).toPlainString();
					if (AppUtility.isQuantityDouble(this.className, this.type))
					{
						if (!validator.isDouble(this.quantity, true))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.required", ApplicationProperties
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
				}
				catch (final NumberFormatException exp)
				{
					CreateSpecimenTemplateForm.logger.info(exp.getMessage(),exp);
					exp.printStackTrace();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("specimen.quantity")));
				}

			}
			else
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("specimen.quantity")));
			}

			if (!this.quantityPerAliquot.equals(""))
			{
				if (this.quantityPerAliquot != null && this.quantityPerAliquot.trim().length() != 0)
				{
					try
					{
						this.quantityPerAliquot = new BigDecimal(this.quantityPerAliquot)
								.toPlainString();
						if (AppUtility.isQuantityDouble(this.className, this.type))
						{
							if (!validator.isDouble(this.quantityPerAliquot.trim()))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("aliquots.qtyPerAliquot")));

							}
						}
						else
						{
							if (!validator.isPositiveNumeric(this.quantityPerAliquot.trim(), 1))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("aliquots.qtyPerAliquot")));
							}
						}
					}
					catch (final NumberFormatException exp)
					{
						CreateSpecimenTemplateForm.logger.error(exp.getMessage(),exp);
						exp.printStackTrace() ;
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
					}
				}
			}

			if (!this.noOfAliquots.equals(""))
			{
				this.noOfAliquots = AppUtility.isValidCount(this.noOfAliquots, errors);

				if (!validator.isNumeric(this.noOfAliquots, 1))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("specimenArrayAliquots.noOfAliquots")));
				}
				else
				{
					try
					{
						if (this.quantityPerAliquot.equals(""))
						{
							if (this.quantity.equals("0") || this.quantity.equals("0"))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.invalid", ApplicationProperties
												.getValue("specimen.quantity")));
							}
							else
							{
								aliquotQuantity = Double.parseDouble(this.quantity)
										/ Double.parseDouble(this.noOfAliquots);
								initialQuantity = Double.parseDouble(this.quantity)
										- (aliquotQuantity * Double.parseDouble(this.noOfAliquots));
							}
						}
						else
						{
							aliquotQuantity = Double.parseDouble(this.quantityPerAliquot);
							initialQuantity = Double.parseDouble(this.quantity);
							initialQuantity = initialQuantity
									- (aliquotQuantity * Double.parseDouble(this.noOfAliquots));
						}
						if (initialQuantity < 0)
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",
									ApplicationProperties
											.getValue("cpbasedentry.quantityperaliquot")));
						}
//						if(this.labelGenTypeForAliquot.equals("2") && validator.isEmpty(this.labelFormatForAliquot))
//						{
//							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
//									"sp.req.aliquot.labelformat", ApplicationProperties
//									.getValue("sp.req.aliquot.labelformat")));
//						}
					}
					catch (final NumberFormatException exp)
					{
						CreateSpecimenTemplateForm.logger.error(exp.getMessage(),exp);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("specimen.quantity")));
					}
				}
			}

			if (this.tissueSite.equals(""))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.tissueSite")));
			}

			if (this.tissueSide.equals(""))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.tissueSide")));
			}

			if (this.noOfAliquots != null && !this.noOfAliquots.equals(""))
			{
				if (!this.storageLocationForAliquotSpecimen.equals("Auto")
						&& !this.storageLocationForAliquotSpecimen.equals("Manual")
						&& !this.storageLocationForAliquotSpecimen.equals("Virtual"))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("cpbasedentry.aliquotstoragelocation")));
				}
			}
			if (!this.storageLocationForSpecimen.equals("Auto")
					&& !this.storageLocationForSpecimen.equals("Manual")
					&& !this.storageLocationForSpecimen.equals("Virtual"))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("cpbasedentry.specimenstoragelocation")));
			}
			if (this.pathologicalStatus.equals(""))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimen.pathologicalStatus")));
			}
			final List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_TISSUE_SITE, null);
			if (!Validator.isEnumeratedValue(tissueSiteList, this.tissueSite))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.tissueSite")));
			}

			final List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_TISSUE_SIDE, null);
			if (!Validator.isEnumeratedValue(tissueSideList, this.tissueSide))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.tissueSide")));
			}

			final List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

			if (!Validator.isEnumeratedValue(pathologicalStatusList, this.pathologicalStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("specimen.pathologicalStatus")));
			}

			/*if ((this.collectionEventUserId) == 0L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						"Collection Event's user"));
			}*/

			// checks the collectionProcedure
			/*if (!validator.isValidOption(this.getCollectionEventCollectionProcedure()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						message));
			}*/

			/*final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
			if (!Validator.isEnumeratedValue(procedureList, this
					.getCollectionEventCollectionProcedure()))
			{
				final String message = ApplicationProperties
						.getValue("cpbasedentry.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));
			}*/
			//Container validation
			/*if (!validator.isValidOption(this.getCollectionEventContainer()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						message));
			}*/
			/*final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CONTAINER, null);
			if (!Validator.isEnumeratedValue(containerList, this.getCollectionEventContainer()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));
			}*/
			/*if ((this.receivedEventUserId) == 0L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						"Received Event's user"));
			}*/
			/*final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_RECEIVED_QUALITY, null);
			if (!Validator.isEnumeratedValue(qualityList, this.receivedEventReceivedQuality))
			{
				final String message = ApplicationProperties
						.getValue("cpbasedentry.receivedquality");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));

			}*/

			if (this.className.equals(Constants.MOLECULAR))
			{
				if (!validator.isDouble(this.concentration, true))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("specimen.concentration")));
				}
			}

			if (this.noOfDeriveSpecimen >= 1)
			{
				boolean bSpecimenClass = false;
				boolean bSpecimenType = false;
//				boolean validateLabel = false;
//				boolean labelGenType = false;
//				boolean labelFormat = false;
				final Map deriveSpecimenMap = this.deriveSpecimenMap();
				final Iterator it = deriveSpecimenMap.keySet().iterator();
				while (it.hasNext())
				{
					final String key = (String) it.next();
					String mapValue = (String) deriveSpecimenMap.get(key);
					if (!bSpecimenClass)
					{
						if (key.indexOf("specimenClass") != -1
								&& !validator.isValidOption(mapValue))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.selected", ApplicationProperties
											.getValue("collectionprotocol.specimenclass")));
							bSpecimenClass = true;
						}
					}
//					if(!labelGenType)
//					{
//						if ((key.indexOf("_labelGenType")) != -1 && mapValue != null)
//						{
//							if (mapValue.equals("2"))
//							{
//								validateLabel=true;
//								labelGenType = true;
//							}
//						}
//
//					}
//					if(!labelFormat && labelGenType)
//					{
//						if(validateLabel && (key.indexOf("_labelFormat")) != -1 && validator.isEmpty(mapValue))
//						{
//							labelFormat = true;
//							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
//									"sp.req.derivative.labelformat", ApplicationProperties
//									.getValue("sp.req.derivative.labelformat")));
//						}
//					}
					if ((key.indexOf("_concentration")) != -1 && mapValue != null)
					{
						mapValue = new BigDecimal(mapValue).toPlainString();
						if (!validator.isDouble(mapValue, true))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.format", ApplicationProperties
											.getValue("specimen.concentration")));
						}
					}

					if (!bSpecimenType)
					{
						if (key.indexOf("specimenType") != -1 && !validator.isValidOption(mapValue))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.selected", ApplicationProperties
											.getValue("collectionprotocol.specimetype")));
							bSpecimenType = true;
						}
					}

					if ((key.indexOf("_quantity")) != -1)
					{
						if (!validator.isEmpty(mapValue))
						{
							try
							{
								mapValue = new BigDecimal(mapValue).toPlainString();
								if (AppUtility.isQuantityDouble(this.className, this.type))
								{
									if (!validator.isDouble(mapValue, true))
									{
										errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
												"errors.item.required", ApplicationProperties
														.getValue("specimen.quantity")));
									}
								}
								else
								{
									if (!validator.isNumeric(mapValue, 0))
									{
										errors
												.add(
														ActionErrors.GLOBAL_ERROR,
														new ActionError(
																"errors.item.format",
																ApplicationProperties
																		.getValue("cpbasedentry.derivedspecimen.quantity")));
									}
								}
							}
							catch (final NumberFormatException exp)
							{
								CreateSpecimenTemplateForm.logger.error(exp.getMessage(),exp);
								errors
										.add(
												ActionErrors.GLOBAL_ERROR,
												new ActionError(
														"errors.item.format",
														ApplicationProperties
																.getValue("cpbasedentry.derivedspecimen.quantity")));
							}
						}
						else
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.format", ApplicationProperties
											.getValue("cpbasedentry.derivedspecimen.quantity")));
						}
					}
				}
			}
		}
		catch (final Exception excp)
		{
			CreateSpecimenTemplateForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	/**
	 * Derive specimen map.
	 *
	 * @return the map
	 */
	public Map deriveSpecimenMap()
	{
		int iCount;
		boolean genLabel = false;
		final Map deriveSpecimenMap = new LinkedHashMap<String, String>();
		for (iCount = 1; iCount <= this.noOfDeriveSpecimen; iCount++)
		{
			String key = null;
			key = "DeriveSpecimenBean:" + iCount + "_specimenClass";
			final String specimenClass = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, specimenClass);

			key = "DeriveSpecimenBean:" + iCount + "_id";
			final String id = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, id);

			key = "DeriveSpecimenBean:" + iCount + "_specimenType";
			final String specimenType = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, specimenType);

			key = "DeriveSpecimenBean:" + iCount + "_quantity";
			final String quantity = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, quantity);

			key = "DeriveSpecimenBean:" + iCount + "_concentration";
			final String conc = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, conc);

			key = "DeriveSpecimenBean:" + iCount + "_storageLocation";
			final String storageLocation = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, storageLocation);

			key = "DeriveSpecimenBean:" + iCount + "_creationEvent";
			final String creationEvent = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, creationEvent);

			key = "DeriveSpecimenBean:" + iCount + "_processingSPP";
			final String processingSPP = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, processingSPP);

			key = "DeriveSpecimenBean:" + iCount + "_labelFormat";
			final String labelFormat = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, labelFormat);

		}
		return deriveSpecimenMap;
	}

	/**
	 * Gets the node key.
	 *
	 * @return the node key
	 */
	public String getNodeKey()
	{
		return this.nodeKey;
	}

	/**
	 * Sets the node key.
	 *
	 * @param nodeKey the new node key
	 */
	public void setNodeKey(String nodeKey)
	{
		this.nodeKey = nodeKey;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAddNewObjectIdentifier(java.lang.String, java.lang.Long)
	 */
	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}



	/** For SCG labeling,this will be exposed through API and not in the model. */
	private String labelFormat;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 *
	 * @return the label format
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 *
	 * @param labelFormat the label format
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}



	/**
	 * Gets the label format for aliquot.
	 *
	 * @return the label format for aliquot
	 */
	public String getLabelFormatForAliquot()
	{
		return labelFormatForAliquot;
	}


	/**
	 * Sets the label format for aliquot.
	 *
	 * @param labelFormatForAliquot the new label format for aliquot
	 */
	public void setLabelFormatForAliquot(String labelFormatForAliquot)
	{
		this.labelFormatForAliquot = labelFormatForAliquot;
	}

	/** The label format for aliquot. */
	private String labelFormatForAliquot;

}
