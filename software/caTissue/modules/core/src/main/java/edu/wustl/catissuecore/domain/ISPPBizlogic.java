
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;

import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;

/**
 * The Interface ISPPInterface.
 */
public interface ISPPBizlogic
{

	/**
	 * Sets the wrapper object.
	 *
	 * @param wrapperObject the new wrapper object
	 */
	void setWrapperObject(Object wrapperObject);

	/**
	 * Gets the wrapper object.
	 *
	 * @return the wrapper object
	 */
	Object getWrapperObject();

	/**
	 * Gets the sPP application collection.
	 *
	 * @return the sPP application collection
	 */
	Collection<SpecimenProcessingProcedureApplication> getSPPApplicationCollection();

	/**
	 * Update spp application.
	 *
	 * @param spp the spp
	 * @param processingSPPApplication the processing spp application
	 * @param actionApplicationCollection the action application collection
	 * @param sessionLoginInfo the session login info
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	void updateSPPApplication(SpecimenProcessingProcedure spp, SpecimenProcessingProcedureApplication processingSPPApplication,
			Collection<ActionApplication> actionApplicationCollection,
			SessionDataBean sessionLoginInfo) throws BizLogicException;

	/**
	 * Update.
	 *
	 * @param processingSPPApplication the processing spp application
	 * @param sessionLoginInfo the session login info
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	void update(SpecimenProcessingProcedureApplication processingSPPApplication,
			SessionDataBean sessionLoginInfo) throws BizLogicException;

	/**
	 * Insert action application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param processingSPPApplication the processing spp application
	 * @param reasonOfDeviation the reason of deviation
	 * @param user the user
	 * @param actionAppRecordEntry the action app record entry
	 *
	 * @return the action application
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	ActionApplication insertActionApplication(IBizLogic actionAppBizLogic,
			SpecimenProcessingProcedureApplication processingSPPApplication, String reasonOfDeviation, User user,
			Date dateOfEvent, ActionApplicationRecordEntry actionAppRecordEntry)throws BizLogicException;

	/**
	 * Update action application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param dateOfEvent the date of event
	 * @param actionApplication the action application
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	void updateActionApplication(IBizLogic actionAppBizLogic, Date dateOfEvent,
			ActionApplication actionApplication) throws BizLogicException;

}
