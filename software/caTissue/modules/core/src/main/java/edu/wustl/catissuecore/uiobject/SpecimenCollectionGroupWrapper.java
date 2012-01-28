
package edu.wustl.catissuecore.uiobject;

import java.util.Collection;
import java.util.Date;

import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ISPPBizlogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;

public class SpecimenCollectionGroupWrapper implements ISPPBizlogic
{

	SpecimenCollectionGroup specimenCollectionGroup;

	@Override
	public Collection<SpecimenProcessingProcedureApplication> getSPPApplicationCollection()
	{
		return this.specimenCollectionGroup.getSppApplicationCollection();
	}

	@Override
	public Object getWrapperObject()
	{
		return specimenCollectionGroup;
	}

	@Override
	public void setWrapperObject(Object wrapperObject)
	{
		this.specimenCollectionGroup = (SpecimenCollectionGroup) wrapperObject;
	}

	@Override
	public void updateSPPApplication(SpecimenProcessingProcedure spp, SpecimenProcessingProcedureApplication processingSPPApplication,
			Collection<ActionApplication> actionApplicationCollection,
			SessionDataBean sessionLoginInfo) throws BizLogicException
	{
		processingSPPApplication.setSpp(spp);
		processingSPPApplication.setSppActionApplicationCollection(actionApplicationCollection);
	}

	@Override
	public void update(SpecimenProcessingProcedureApplication processingSPPApplication, SessionDataBean sessionLoginInfo)
			throws BizLogicException
	{
		IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
		//update specimen object
		SpecimenCollectionGroup oldSCG = (SpecimenCollectionGroup) defaultBizLogic.retrieve(
				SpecimenCollectionGroup.class.getName(), this.specimenCollectionGroup.getId());
		this.specimenCollectionGroup.getSppApplicationCollection().add(processingSPPApplication);

		Collection<ConsentTierStatus> consentTierStatusCollection = (Collection<ConsentTierStatus>) defaultBizLogic
				.retrieveAttribute(SpecimenCollectionGroup.class, this.specimenCollectionGroup
						.getId(), "elements(consentTierStatusCollection)");
		this.specimenCollectionGroup.setConsentTierStatusCollection(consentTierStatusCollection);
		oldSCG.setConsentTierStatusCollection(consentTierStatusCollection);
		defaultBizLogic.update(this.specimenCollectionGroup, oldSCG, sessionLoginInfo);

	}

	@Override
	public ActionApplication insertActionApplication(IBizLogic actionAppBizLogic,
			SpecimenProcessingProcedureApplication processingSPPApplication, String reasonOfDeviation, User user,
			Date dateOfEvent,String comments, ActionApplicationRecordEntry actionAppRecordEntry) throws BizLogicException
	{
		ActionApplication actionApplication = new ActionApplication();
		actionApplication.setReasonDeviation(reasonOfDeviation);
		actionApplication.setTimestamp((dateOfEvent != null) ? dateOfEvent : new Date());
		actionApplication.setPerformedBy(user);
		actionApplication.setComments(comments);
		actionApplication.setApplicationRecordEntry(actionAppRecordEntry);
		return actionApplication;
	}

	/**
	 * Update action application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param dateOfEvent the date of event
	 * @param actionApplication the action application
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public void updateActionApplication(IBizLogic actionAppBizLogic, Date dateOfEvent,
			ActionApplication actionApplication) throws BizLogicException
	{
		//TODO
	}

}
