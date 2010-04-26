
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

public class CreateSpecimenTemplateForm extends AbstractActionForm
{

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(CreateSpecimenTemplateForm.class);

	/**
	 * Display Name
	 */
	protected String displayName;

	/**
	 * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
	 */
	protected String className;

	/**
	 * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type;

	/**
	 * Anatomic site from which the specimen was derived.
	 */
	private String tissueSite;

	/**
	 * For bilateral sites, left or right.
	 */
	private String tissueSide;

	/**
	 * Histopathological character of the specimen
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	private String pathologicalStatus;

	private String storageLocationForSpecimen;

	private String storageLocationForAliquotSpecimen;

	/**
	 * Concentration of specimen.
	 */
	protected String concentration = "0";

	/**
	 * Amount of Specimen.
	 */
	protected String quantity;

	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot.
	 */
	private String lineage;

	/**
	 * Unit of specimen.
	 */
	protected String unit;

	/**
	* A number that tells how many aliquots to be created.
	*/
	private String noOfAliquots;

	/**
	 * Initial quantity per aliquot.
	 */
	private String quantityPerAliquot;

	/**
	 * Collection of aliquot specimens derived from this specimen.
	 */
	protected Collection aliquotSpecimenCollection;

	/**
	 * Collection of derive specimens derived from this specimen.
	 */
	protected Collection deriveSpecimenCollection;

	private long collectionEventId; // Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionUserName = null;

	/**
	 *
	 * @return collectionUserName
	 */
	public String getCollectionUserName()
	{
		return this.collectionUserName;
	}

	/**
	 *
	 * @param collectionUserName collectionUserName
	 */
	public void setCollectionUserName(String collectionUserName)
	{
		this.collectionUserName = collectionUserName;
	}

	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedUserName = null;

	/**
	 *
	 * @return receivedUserName
	 */
	public String getReceivedUserName()
	{
		return this.receivedUserName;
	}

	/**
	 *
	 * @param receivedUserName receivedUserName
	 */
	public void setReceivedUserName(String receivedUserName)
	{
		this.receivedUserName = receivedUserName;
	}

	private String collectionEventCollectionProcedure;

	private String collectionEventContainer;

	private String receivedEventReceivedQuality;

	private int noOfDeriveSpecimen;

	private Map deriveSpecimenValues = new LinkedHashMap();

	private String nodeKey = null;

	public String getClassName()
	{
		return this.className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTissueSite()
	{
		return this.tissueSite;
	}

	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	public String getConcentration()
	{
		return this.concentration;
	}

	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	public String getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}

	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
		if (noOfAliquots != null && noOfAliquots.equals("0"))
		{
			this.noOfAliquots = "";
		}

	}

	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}

	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}

	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	public int getNoOfDeriveSpecimen()
	{
		return this.noOfDeriveSpecimen;
	}

	public void setNoOfDeriveSpecimen(int noOfDeriveSpecimen)
	{
		this.noOfDeriveSpecimen = noOfDeriveSpecimen;
	}

	public Object getDeriveSpecimenValue(String key)
	{
		return this.deriveSpecimenValues.get(key);
	}

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

	public Map getDeriveSpecimenValues()
	{
		return this.deriveSpecimenValues;
	}

	public void setDeriveSpecimenValues(Map deriveSpecimenValues)
	{
		this.deriveSpecimenValues = deriveSpecimenValues;
	}

	/**
	* @return Returns the values.
	*/
	public Collection getAllDeriveSpecimenValuess()
	{
		return this.deriveSpecimenValues.values();
	}

	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void reset()
	{

	}

	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	public String getLineage()
	{
		return this.lineage;
	}

	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	public Collection getAliquotSpecimenCollection()
	{
		return this.aliquotSpecimenCollection;
	}

	public void setAliquotSpecimenCollection(Collection aliquotSpecimenCollection)
	{
		this.aliquotSpecimenCollection = aliquotSpecimenCollection;
	}

	public Collection getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	public void setDeriveSpecimenCollection(Collection deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getUnit()
	{
		return this.unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public long getCollectionEventId()
	{
		return this.collectionEventId;
	}

	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}

	public long getCollectionEventSpecimenId()
	{
		return this.collectionEventSpecimenId;
	}

	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}

	public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
	}

	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}

	public long getReceivedEventId()
	{
		return this.receivedEventId;
	}

	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}

	public long getReceivedEventSpecimenId()
	{
		return this.receivedEventSpecimenId;
	}

	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}

	public long getReceivedEventUserId()
	{
		return this.receivedEventUserId;
	}

	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}

	public String getStorageLocationForSpecimen()
	{
		return this.storageLocationForSpecimen;
	}

	public void setStorageLocationForSpecimen(String storageLocationForSpecimen)
	{
		this.storageLocationForSpecimen = storageLocationForSpecimen;
	}

	public String getStorageLocationForAliquotSpecimen()
	{
		return this.storageLocationForAliquotSpecimen;
	}

	public void setStorageLocationForAliquotSpecimen(String storageLocationForAliquotSpecimen)
	{
		this.storageLocationForAliquotSpecimen = storageLocationForAliquotSpecimen;
	}

	public String getTissueSide()
	{
		return this.tissueSide;
	}

	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

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
			if(this.labelGenType.equals("2") && validator.isEmpty(this.labelFormat))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"collectionProtocol.labelformat", ApplicationProperties
						.getValue("collectionProtocol.labelformat")));
			}
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
				if (!validator.isNumeric(this.noOfAliquots))
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
						if(this.labelGenTypeForAliquot.equals("2") && validator.isEmpty(this.labelFormatForAliquot))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"sp.req.aliquot.labelformat", ApplicationProperties
									.getValue("sp.req.aliquot.labelformat")));
						}
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

			if ((this.collectionEventUserId) == 0L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						"Collection Event's user"));
			}

			// checks the collectionProcedure
			if (!validator.isValidOption(this.getCollectionEventCollectionProcedure()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						message));
			}

			final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
			if (!Validator.isEnumeratedValue(procedureList, this
					.getCollectionEventCollectionProcedure()))
			{
				final String message = ApplicationProperties
						.getValue("cpbasedentry.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));
			}
			//Container validation
			if (!validator.isValidOption(this.getCollectionEventContainer()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						message));
			}
			final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CONTAINER, null);
			if (!Validator.isEnumeratedValue(containerList, this.getCollectionEventContainer()))
			{
				final String message = ApplicationProperties
						.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));
			}
			if ((this.receivedEventUserId) == 0L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						"Received Event's user"));
			}
			final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_RECEIVED_QUALITY, null);
			if (!Validator.isEnumeratedValue(qualityList, this.receivedEventReceivedQuality))
			{
				final String message = ApplicationProperties
						.getValue("cpbasedentry.receivedquality");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", message));

			}

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
				boolean validateLabel = false;
				boolean labelGenType = false;
				boolean labelFormat = false;
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
					if(!labelGenType)
					{
						if ((key.indexOf("_labelGenType")) != -1 && mapValue != null)
						{
							if (mapValue.equals("2"))
							{
								validateLabel=true;
								labelGenType = true;
							}
						}

					}
					if(!labelFormat && labelGenType)
					{
						if(validateLabel && (key.indexOf("_labelFormat")) != -1 && validator.isEmpty(mapValue))
						{
							labelFormat = true;
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"sp.req.derivative.labelformat", ApplicationProperties
									.getValue("sp.req.derivative.labelformat")));
						}
					}
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

			key = "DeriveSpecimenBean:" + iCount + "_labelGenType";
			final String labelGenType = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, labelGenType);
			if(labelGenType != null && labelGenType.equals("0"))
			{
				genLabel = Boolean.FALSE;
			}
			else if(labelGenType.equals("1") || labelGenType.equals("2"))
			{
				genLabel = Boolean.TRUE;
			}
			this.setDeriveSpecimenValue("DeriveSpecimenBean:" + iCount + "_genLabel", Boolean.toString(genLabel));

			key = "DeriveSpecimenBean:" + iCount + "_labelFormat";
			final String labelFormat = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, labelFormat);

			key = "DeriveSpecimenBean:" + iCount + "_genLabel";
			final String derGenLabel = (String) this.deriveSpecimenValues.get(key);
			deriveSpecimenMap.put(key, derGenLabel);

		}
		return deriveSpecimenMap;
	}

	public String getNodeKey()
	{
		return this.nodeKey;
	}

	public void setNodeKey(String nodeKey)
	{
		this.nodeKey = nodeKey;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

	private String labelGenType;

	public String getLabelGenType()
	{
		return labelGenType;
	}


	public void setLabelGenType(String labelGenType)
	{
		this.labelGenType = labelGenType;
	}

	private boolean genLabel = false;

	public boolean isGenLabel()
	{
		return genLabel;
	}


	public void setGenLabel(boolean genLabel)
	{
		this.genLabel = genLabel;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	private String labelFormat;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

	private boolean genLabelForAliquot;

	public boolean isGenLabelForAliquot()
	{
		return genLabelForAliquot;
	}


	public void setGenLabelForAliquot(boolean genLabelForAliquot)
	{
		this.genLabelForAliquot = genLabelForAliquot;
	}


	public String getLabelFormatForAliquot()
	{
		return labelFormatForAliquot;
	}


	public void setLabelFormatForAliquot(String labelFormatForAliquot)
	{
		this.labelFormatForAliquot = labelFormatForAliquot;
	}

	private String labelFormatForAliquot;

	private String labelGenTypeForAliquot;


	public String getLabelGenTypeForAliquot()
	{
		return labelGenTypeForAliquot;
	}


	public void setLabelGenTypeForAliquot(String labelGenTypeForAliquot)
	{
		this.labelGenTypeForAliquot = labelGenTypeForAliquot;
	}
}
