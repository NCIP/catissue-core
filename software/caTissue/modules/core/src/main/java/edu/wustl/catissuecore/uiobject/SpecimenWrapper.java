
package edu.wustl.catissuecore.uiobject;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.ISPPBizlogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.ObjectCloner;

public class SpecimenWrapper implements ISPPBizlogic
{

	Specimen specimen;

	@Override
	public Collection<SpecimenProcessingProcedureApplication> getSPPApplicationCollection()
	{
		Collection<SpecimenProcessingProcedureApplication> sppApplicationCollection = new HashSet<SpecimenProcessingProcedureApplication>();
		SpecimenProcessingProcedureApplication processingSPPApplication = this.specimen.getProcessingSPPApplication();
		if (processingSPPApplication != null)
		{
			sppApplicationCollection.add(processingSPPApplication);
		}
		return sppApplicationCollection;
	}

	@Override
	public Object getWrapperObject()
	{
		return specimen;
	}

	@Override
	public void setWrapperObject(Object wrapperObject)
	{
		this.specimen = (Specimen) wrapperObject;

	}

	@Override
	public void updateSPPApplication(SpecimenProcessingProcedure spp, SpecimenProcessingProcedureApplication processingSPPApplication,
			Collection<ActionApplication> actionApplicationCollection,
			SessionDataBean sessionLoginInfo) throws BizLogicException
	{
		ObjectCloner cloner = new ObjectCloner();
		SpecimenProcessingProcedureApplication clonedSPPApplication = cloner.clone(processingSPPApplication);

		processingSPPApplication.setSpp(spp);
		processingSPPApplication.setSppActionApplicationCollection(actionApplicationCollection);
		IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
		defaultBizLogic.update(processingSPPApplication, clonedSPPApplication, sessionLoginInfo);

	}

	@Override
	public void update(SpecimenProcessingProcedureApplication processingSPPApplication, SessionDataBean sessionLoginInfo)
			throws BizLogicException
	{
		IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
		//update specimen object
		Specimen newSpecimen = (Specimen) defaultBizLogic.retrieve(Specimen.class.getName(),
				this.specimen.getId());
		newSpecimen.setProcessingSPPApplication(processingSPPApplication);
		defaultBizLogic.update(newSpecimen, this.specimen, sessionLoginInfo);

	}

	@Override
	public ActionApplication insertActionApplication(IBizLogic actionAppBizLogic,
			SpecimenProcessingProcedureApplication processingSPPApplication, String reasonOfDeviation, User user,
			Date dateOfEvent, ActionApplicationRecordEntry actionAppRecordEntry) throws BizLogicException
	{
		ActionApplication actionApplication = new ActionApplication();
		actionApplication.setReasonDeviation(reasonOfDeviation);
		actionApplication.setTimestamp((dateOfEvent != null) ? dateOfEvent : new Date());
		actionApplication.setSppApplication(processingSPPApplication);
		actionApplication.setSpecimen(this.specimen);
		actionApplication.setPerformedBy(user);
		actionApplication.setApplicationRecordEntry(actionAppRecordEntry);
		actionAppBizLogic.insert(actionApplication);
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
		actionApplication.setTimestamp(dateOfEvent);
		actionAppBizLogic.update(actionApplication);
	}
}
