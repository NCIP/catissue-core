
package edu.wustl.catissuecore.uiobject;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.ISPPBizlogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.sop.ActionApplication;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.domain.sop.SOPApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.ObjectCloner;

public class SpecimenWrapper implements ISPPBizlogic
{

	Specimen specimen;

	@Override
	public Collection<SOPApplication> getSPPApplicationCollection()
	{
		Collection<SOPApplication> sppApplicationCollection = new HashSet<SOPApplication>();
		SOPApplication processingSOPApplication = this.specimen.getProcessingSOPApplication();
		if (processingSOPApplication != null)
		{
			sppApplicationCollection.add(processingSOPApplication);
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
	public void updateSOPApplication(SOP spp, SOPApplication processingSOPApplication,
			Collection<ActionApplication> actionApplicationCollection,
			SessionDataBean sessionLoginInfo) throws BizLogicException
	{
		ObjectCloner cloner = new ObjectCloner();
		SOPApplication clonedSOPApplication = cloner.clone(processingSOPApplication);

		processingSOPApplication.setSop(spp);
		processingSOPApplication.setSopActionApplicationCollection(actionApplicationCollection);
		IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
		defaultBizLogic.update(processingSOPApplication, clonedSOPApplication, sessionLoginInfo);

	}

	@Override
	public void update(SOPApplication processingSOPApplication, SessionDataBean sessionLoginInfo)
			throws BizLogicException
	{
		IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();
		//update specimen object
		Specimen newSpecimen = (Specimen) defaultBizLogic.retrieve(Specimen.class.getName(),
				this.specimen.getId());
		newSpecimen.setProcessingSOPApplication(processingSOPApplication);
		defaultBizLogic.update(newSpecimen, this.specimen, sessionLoginInfo);

	}

	@Override
	public ActionApplication insertActionApplication(IBizLogic actionAppBizLogic,
			SOPApplication processingSOPApplication, String reasonOfDeviation, User user,
			ActionApplicationRecordEntry actionAppRecordEntry) throws BizLogicException
	{
		ActionApplication actionApplication = new ActionApplication();
		actionApplication.setReasonDeviation(reasonOfDeviation);
		actionApplication.setTimestamp(new Date());
		actionApplication.setSopApplication(processingSOPApplication);
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
