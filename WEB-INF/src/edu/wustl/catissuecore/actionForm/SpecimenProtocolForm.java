/*
 * Created on Jul 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SpecimenProtocolForm extends AbstractActionForm
{

	/**
	 *
	 */
	private static final long serialVersionUID = -2955751730317671805L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenProtocolForm.class);
	protected long principalInvestigatorId;

	protected String irbID;

	protected String descriptionURL;

	protected String title;

	protected String shortTitle;

	protected String startDate;

	protected String endDate;

	protected String enrollment;

	protected boolean generateLabel = false;

	protected boolean defaultLabelGen = false;


	public boolean isDefaultLabelGen()
	{
		return defaultLabelGen;
	}




	public void setDefaultLabelGen(boolean defaultLabelGen)
	{
		this.defaultLabelGen = defaultLabelGen;
	}

	protected String specimenLabelFormat;


	public String getSpecimenLabelFormat()
	{
		return specimenLabelFormat;
	}



	public void setSpecimenLabelFormat(String labelFormat)
	{
		this.specimenLabelFormat = labelFormat;
	}


	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	/**
	 * Patch Id : Collection_Event_Protocol_Order_8 (Changed from HashMap to LinkedHashMap)
	 * Description : To get CollectionProtocol Events in order
	 */
	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new LinkedHashMap();

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 *
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return this.values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return this.values.values();
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public SpecimenProtocolForm()
	{
		this.reset();
	}

	/**
	 * @return Returns the descriptionurl.
	 */
	public String getDescriptionURL()
	{
		return this.descriptionURL;
	}

	/**
	 * @param descriptionurl
	 *            The descriptionurl to set.
	 */
	public void setDescriptionURL(String descriptionurl)
	{
		this.descriptionURL = descriptionurl;
	}

	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate()
	{
		return this.endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the irbid.
	 */
	public String getIrbID()
	{
		return this.irbID;
	}

	/**
	 * @param irbid
	 *            The irbid to set.
	 */
	public void setIrbID(String irbid)
	{
		this.irbID = irbid;
	}

	/**
	 * @return Returns the participants.
	 */
	public String getEnrollment()
	{
		return this.enrollment;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setEnrollment(String participants)
	{
		this.enrollment = participants;
	}

	/**
	 * @return Returns the principalinvestigatorid.
	 */
	public long getPrincipalInvestigatorId()
	{
		return this.principalInvestigatorId;
	}

	/**
	 * @param principalInvestigatorId
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorId)
	{
		this.principalInvestigatorId = principalInvestigatorId;
	}

	/**
	 * @return Returns the shorttitle.
	 */
	public String getShortTitle()
	{
		return this.shortTitle;
	}

	/**
	 * @param shorttitle
	 *            The shorttitle to set.
	 */
	public void setShortTitle(String shorttitle)
	{
		this.shortTitle = shorttitle;
	}

	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate()
	{
		return this.startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return Returns the values.
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * Copies the data from an AbstractDomain object to a CollectionProtocolForm
	 * object.
	 *
	 * @param abstractDomain
	 *            An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final SpecimenProtocol protocol = (SpecimenProtocol) abstractDomain;

		this.setId(protocol.getId().longValue());

		if (protocol.getPrincipalInvestigator() != null
				&& protocol.getPrincipalInvestigator().getId() != null)
		{
			this.principalInvestigatorId = protocol.getPrincipalInvestigator().getId().longValue();
		}
		else
		{
			this.principalInvestigatorId = -1;
		}

		this.generateLabel=protocol.getGenerateLabel();
		this.specimenLabelFormat=protocol.getSpecimenLabelFormat();
		this.title = CommonUtilities.toString(protocol.getTitle());
		this.shortTitle = CommonUtilities.toString(protocol.getShortTitle());
		this.startDate = CommonUtilities.parseDateToString(protocol.getStartDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		this.endDate = CommonUtilities.parseDateToString(protocol.getEndDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		this.irbID = CommonUtilities.toString(protocol.getIrbIdentifier());
		this.enrollment = CommonUtilities.toString(protocol.getEnrollment());
		this.descriptionURL = CommonUtilities.toString(protocol.getDescriptionURL());

		this.setActivityStatus(CommonUtilities.toString(protocol.getActivityStatus()));
	}

	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 */
	@Override
	protected void reset()
	{
		this.principalInvestigatorId = 0;
		this.title = null;
		this.shortTitle = null;
		this.startDate = null;
		this.endDate = null;
		this.irbID = null;
		this.enrollment = null;
		this.descriptionURL = null;

		this.values = new LinkedHashMap();
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
		ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			if (this.getOperation() != null
					&& (this.getOperation().equals(Constants.ADD)
							|| this.getOperation().equals(Constants.EDIT) || this.getOperation()
							.equals("AssignPrivilegePage")))
			{
				if (this.principalInvestigatorId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
							ApplicationProperties
									.getValue("collectionprotocol.principalinvestigator")));
				}

				if (Validator.isEmpty(this.title))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("collectionprotocol.protocoltitle")));
				}

				if (Validator.isEmpty(this.shortTitle))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("collectionprotocol.shorttitle")));
				}

				//              if (validator.isEmpty(this.irbID))
				//              {
				//                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.irbid")));
				//              }

				// --- startdate
				//  date validation according to bug id  722 and 730 and 939
				String errorKey = validator.validateDate(this.startDate, false);
				if (errorKey.trim().length() > 0)
				{
					logger.debug("startdate errorKey : " + errorKey);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,
							ApplicationProperties.getValue("collectionprotocol.startdate")));
				}

				//  --- end date
				if (!Validator.isEmpty(this.endDate))
				{
					//  date validation according to bug id  722 and 730 and 939
					errorKey = validator.validateDate(this.endDate, false);
					if (errorKey.trim().length() > 0 && !errorKey.equals(""))
					{
						logger.debug("enddate errorKey: " + errorKey);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,
								ApplicationProperties.getValue("collectionprotocol.enddate")));
					}
				}

				// code added as per bug id 235
				// code to validate startdate less than end date
				// check the start date less than end date
				if (validator.checkDate(this.startDate) && validator.checkDate(this.endDate))
				{
					if (!validator.compareDates(this.startDate, this.endDate))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"specimenprotocol.invaliddate", ApplicationProperties
										.getValue("specimenprotocol.invaliddate")));
					}
				}
				if(this.generateLabel && validator.isEmpty(this.specimenLabelFormat))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"collectionProtocol.labelformat", ApplicationProperties
							.getValue("collectionProtocol.labelformat")));
					//"Label Format is mandatory for custom label generation"
				}
				else if(this.defaultLabelGen)
				{
					this.generateLabel = this.defaultLabelGen;
				}
				if (!Validator.isEmpty(this.enrollment))
				{

					try
					{
						//Integer intEnrollment = new Integer(enrollment);
					}
					catch (final NumberFormatException e)
					{
						SpecimenProtocolForm.logger.error(e.getMessage(),e);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.enrollment",
								ApplicationProperties.getValue("collectionprotocol.participants")));
					}
				}
			}
		}
		catch (final Exception excp)
		{
			// use of logger as per bug 79
			SpecimenProtocolForm.logger.error("error in SPForm : " + excp.getMessage(), excp);
			excp.printStackTrace();
			errors = new ActionErrors();
		}
		return errors;
	}

	/**
	 *
	 * @param key Atring Array of Key
	 * @param requirement Specimen Requirement
	 */
	protected void setSpecimenRequirement(String[] key, DistributionSpecimenRequirement requirement)
	{
		this.values.put(key[0], requirement.getSpecimenClass());
		this.values.put(key[1], AppUtility.getUnit(requirement.getSpecimenClass(), requirement
				.getSpecimenType()));
		this.values.put(key[2], requirement.getSpecimenType());
		this.values.put(key[3], requirement.getTissueSite());
		this.values.put(key[4], requirement.getPathologyStatus());
		//values.put(key[5] , AppUtility.toString(requirement.getQuantity().getValue()));

		if (!AppUtility.isQuantityDouble(requirement.getSpecimenClass(), requirement
				.getSpecimenType()))
		{
			final Double doubleQuantity = requirement.getQuantity();
			if (doubleQuantity == null)
			{
				this.values.put(key[5], "0");
			}
			else if (doubleQuantity.toString().contains("E"))
			{
				this.values.put(key[5], doubleQuantity.toString());
			}
			else
			{
				final long longQuantity = doubleQuantity.longValue();
				this.values.put(key[5], new Long(longQuantity).toString());

			}

		}
		else
		{
			if (requirement.getQuantity() == null)
			{
				this.values.put(key[5], "0");
			}
			else
			{
				this.values.put(key[5], requirement.getQuantity().toString());
			}
		}

		this.values.put(key[6], CommonUtilities.toString(requirement.getId()));

		if (requirement.getSpecimenClass().equals(Constants.TISSUE)
				&& requirement.getQuantity() != null)
		{
			final String tissueType = requirement.getSpecimenType();
			if (tissueType.equalsIgnoreCase(Constants.FROZEN_TISSUE_SLIDE)
					|| tissueType.equalsIgnoreCase(Constants.FIXED_TISSUE_BLOCK)
					|| tissueType.equalsIgnoreCase(Constants.FROZEN_TISSUE_BLOCK)
					|| tissueType.equalsIgnoreCase(Constants.FIXED_TISSUE_SLIDE))
			{
				this.values.put(key[5], CommonUtilities.toString(new Integer(requirement
						.getQuantity().intValue())));
			}
		}
	}
}