
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 *
 */
public class ProtocolEventDetailsForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(ProtocolEventDetailsForm.class);
	private String clinicalDiagnosis;

	private String clinicalStatus;

	/**
	 * Defines the required collectionPointLabel.
	 */
	protected String collectionPointLabel;

	/**
	 * Defines the relative time point in days, with respect to the registration date of participant on this protocol, when the specimen should be collected from participant.
	 */
	protected Double studyCalendarEventPoint = 1D;

	protected String collectionProtocolEventkey;
	/**
	 * Event Attributes
	 */
	private long collectionEventId; // Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionEventCollectionProcedure;
	private String collectionEventContainer;
	private String collectionEventComments = "";

	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedEventReceivedQuality;
	private String receivedEventComments = "";

	private String[] specimenProcessingProcedure;

	public String[] getSpecimenProcessingProcedure() {
		return specimenProcessingProcedure;
	}

	public void setSpecimenProcessingProcedure(String[] specimenProcessingProcedure) {
		this.specimenProcessingProcedure = specimenProcessingProcedure;
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
		// TODO Auto-generated method stub

	}

	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	public String getClinicalDiagnosis()
	{
		return this.clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus()
	{
		return this.clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
	}

	public String getCollectionPointLabel()
	{
		return this.collectionPointLabel;
	}

	public void setCollectionPointLabel(String collectionPointLabel)
	{
		this.collectionPointLabel = collectionPointLabel;
	}

	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
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

	public String getCollectionEventComments()
	{
		return this.collectionEventComments;
	}

	public void setCollectionEventComments(String collectionEventComments)
	{
		this.collectionEventComments = collectionEventComments;
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

	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	public String getReceivedEventComments()
	{
		return this.receivedEventComments;
	}

	public void setReceivedEventComments(String receivedEventComments)
	{
		this.receivedEventComments = receivedEventComments;
	}

	public String getCollectionProtocolEventkey()
	{
		return this.collectionProtocolEventkey;
	}

	public void setCollectionProtocolEventkey(String collectionProtocolEventkey)
	{
		this.collectionProtocolEventkey = collectionProtocolEventkey;
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
			this.setRedirectValue(validator);
			final HttpSession session = request.getSession();
			final Map collectionProtocolEventMap = (Map) session
					.getAttribute("collectionProtocolEventMap");
			if (collectionProtocolEventMap != null)
			{
				final Collection collectionProtocolEventBeanCollection = collectionProtocolEventMap
						.values();
				final Iterator collectionProtocolEventBeanCollectionItr = collectionProtocolEventBeanCollection
						.iterator();
				while (collectionProtocolEventBeanCollectionItr.hasNext())
				{
					final CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventBeanCollectionItr
							.next();
					final String collectionPointLabel = collectionProtocolEventBean
							.getCollectionPointLabel();
					if (!collectionProtocolEventBean.getUniqueIdentifier().equals(
							this.collectionProtocolEventkey))
					{
						if (this.getCollectionPointLabel().equals(collectionPointLabel))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.unique", ApplicationProperties
											.getValue("collectionprotocol.collectionpointlabel")));
							break;
						}
					}
				}
			}

			final double dblValue = Double.parseDouble(this.studyCalendarEventPoint.toString());
			if (Double.isNaN(dblValue))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
			}
			if (Validator.isEmpty(this.collectionPointLabel.toString()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("collectionprotocol.collectionpointlabel")));
			}

			// Mandatory Field : clinical Diagnosis
			if (!validator.isValidOption(this.clinicalDiagnosis))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item.selected", ApplicationProperties
								.getValue("specimenCollectionGroup.clinicalDiagnosis")));
			}

			// Mandatory Field : clinical Status
			if (!validator.isValidOption(this.clinicalStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus")));
			}
			/*
			 Commented by Virender
			if ((collectionEventUserId) == -1L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
			}

			// checks the collectionProcedure
			if (!validator.isValidOption(this.getCollectionEventCollectionProcedure()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",message));
			}

			List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
			if (!Validator.isEnumeratedValue(procedureList, this.getCollectionEventCollectionProcedure()))
			{
				String message = ApplicationProperties.getValue("cpbasedentry.collectionprocedure");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));
			}
			//Container validation
			if (!validator.isValidOption(this.getCollectionEventContainer()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",message));
			}
			List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
			if (!Validator.isEnumeratedValue(containerList, this.getCollectionEventContainer()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.container");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));
			}
			if ((receivedEventUserId) == -1L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
			}
			List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
			if (!Validator.isEnumeratedValue(qualityList, this.receivedEventReceivedQuality))
			{
				String message = ApplicationProperties.getValue("cpbasedentry.receivedquality");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",message));

			}*/

		}
		catch (final Exception excp)
		{
			// use of logger as per bug 79
			ProtocolEventDetailsForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			errors = new ActionErrors();
		}
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

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

}
