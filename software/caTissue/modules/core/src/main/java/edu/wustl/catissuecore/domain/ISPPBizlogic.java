
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;

import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.sop.ActionApplication;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.domain.sop.SOPApplication;
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
	Collection<SOPApplication> getSPPApplicationCollection();

	/**
	 * Update sop application.
	 *
	 * @param spp the spp
	 * @param processingSOPApplication the processing sop application
	 * @param actionApplicationCollection the action application collection
	 * @param sessionLoginInfo the session login info
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	void updateSOPApplication(SOP spp, SOPApplication processingSOPApplication,
			Collection<ActionApplication> actionApplicationCollection,
			SessionDataBean sessionLoginInfo) throws BizLogicException;

	/**
	 * Update.
	 *
	 * @param processingSOPApplication the processing sop application
	 * @param sessionLoginInfo the session login info
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	void update(SOPApplication processingSOPApplication,
			SessionDataBean sessionLoginInfo) throws BizLogicException;

	/**
	 * Insert action application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param processingSOPApplication the processing sop application
	 * @param reasonOfDeviation the reason of deviation
	 * @param user the user
	 * @param actionAppRecordEntry the action app record entry
	 *
	 * @return the action application
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	ActionApplication insertActionApplication(IBizLogic actionAppBizLogic,
			SOPApplication processingSOPApplication, String reasonOfDeviation, User user,
			ActionApplicationRecordEntry actionAppRecordEntry)throws BizLogicException;

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
