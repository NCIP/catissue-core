package edu.wustl.catissuecore.bizlogic;

import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.uiobject.CollectionProtocolRegistrationUIObject;
import edu.wustl.catissuecore.util.ParticipantRegistrationInfo;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;

public interface ICollectionProtocolRegistrationBizLogic {

	/**This method is called for the protocols that are automatically registered
	 * after registration of an arm. In this
	 * method the total offset of upper level hierarchy up to that protocol is
	 * calculated for proper recalculation of the
	 * registration date.
	 * @param collectionProtocolRegistration
	 * CollectionProtocolRegistration Object for current CollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean  The session in which the object is saved.
	 * @throws BizLogicException
	 */
	public void getTotalOffset(
			CollectionProtocolRegistration collectionProtocolRegistration,
			DAO dao, SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * @param collectionProtocol
	 * @param dao
	 * @param sessionDataBean
	 * @param participantId
	 * @param collectionProtocolToRegister
	 * @throws BizLogicException
	 */
	public void calculationOfTotalOffset(CollectionProtocol collectionProtocol,
			DAO dao, SessionDataBean sessionDataBean, Long participantId,
			CollectionProtocol collectionProtocolToRegister)
			throws BizLogicException;

	/**This method is called so as to calculate total offset of SCG for currentCPR.
	 * This method is called when an CP is automatically registered after an arm.
	 * All SCG's of upper level hierarchy which carry offset
	 * are added together for total offset,So that registration date is correct
	 * @param cpr the CollectionProtocol object which has SCG's
	 */
	public void offsetFromSCG(CollectionProtocolRegistration cpr);

	/**The id of CPR is extracted from database with respect to
	 * CollectionProtocol id and Participant id.
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @return
	 * @throws BizLogicException
	 */
	public Long getIdofCPR(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException;

	/** The status of all Specimen Collection Group is changed when another arm is
	 *  registered if they are not collected.
	 * @param collectionProtocolRegistration
	 * The CollectionProtocolRegistration Object for
	 * currentCollectionProtocol.
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException
	 */
	public void changeStatusOfEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException;

	/**In this method the status of Specimen Collection Group of Child CollectionProtocol
	 * is changed if the previous arm has any child.
	 * The status is changed only when the Specimen is not collected
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration Object
	 * for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException
	
	 */
	public void checkForChildStatus(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException;

	/**In this method if there is change in Offset of parent protocol then the offset of child
	 * CollectionProtocol.
	 * also changes.This is basically when upper level Hierarchy Protocol has an offset and below CP's
	 * are registered automatically.
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration
	 * Object for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException bizLogicException
	 */
	public void checkAndUpdateChildDate(DAO dao,
			SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException;

	public void insertCPR(
			CollectionProtocolRegistration collectionProtocolRegistration,
			DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistrationUIObject cprUIObject)
			throws BizLogicException;

	/** In this method if parent CP has any child which can be
	 * automatically registered,then these child are registered.
	 * @param cpr The CollectionProtocol Registration Object of current Collection Protocol
	 * @param dao The DAO object
	 * @param sessionDataBean the SessionDataBean
	 * @throws BizLogicException
	 */

	public void chkForChildCP(CollectionProtocolRegistration cpr, DAO dao,
			SessionDataBean sessionDataBean,
			CollectionProtocolRegistrationUIObject cprUIObject)
			throws BizLogicException;

	public void setRegDate(CollectionProtocolRegistration cpr,
			Double studyeventpointCalendar, Date dateofCP);

	/**
	 * create Clone Of CPR.
	 * @param cpr
	 * @param collProtocol
	 * @return
	 */
	public CollectionProtocolRegistration createCloneOfCPR(
			CollectionProtocolRegistration cpr, CollectionProtocol collProtocol);

	/**
	 * post Insert.
	 * @param obj
	 * @param dao
	 * @param sessionDataBean
	 * @throws BizLogicException bizLogicException
	 */
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException;

	/**
	 * post Update.
	 * @param dao
	 * @param currentObj
	 * @param oldObj
	 * @param sessionDataBean
	 * @throws BizLogicException bizLogicException
	 */
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * Disable all the related collection protocol regitration for a given array
	 * of participant ids.
	 */
	public void disableRelatedObjectsForParticipant(DAO dao,
			Long participantIDArr[]) throws BizLogicException;

	/**
	 * Disable all the related collection protocol regitrations for a given
	 * array of collection protocol ids.
	 */
	public void disableRelatedObjectsForCollectionProtocol(DAO dao,
			Long collectionProtocolIDArr[]) throws BizLogicException;

	/**
	 * Method to check if the unique constraint for PPI and CPid is
	 * violated or not.
	 * @param dao
	 * @param cpId
	 * @param ppi
	 * @return
	 * @throws BizLogicException
	 */
	public boolean checkUniqueConstraint(DAO dao, long cpId, String ppi)
			throws BizLogicException;

	/**
	 * Name: Vijay Pande Reviewer Name: Sachin Lale Bug id: 4477 Method updated
	 * since earlier implemetation was not including CP having no registerd
	 * participant. Also short title is also fetched from DB.
	 */
	/**
	 * This function finds out all the registerd participants for a particular
	 * collection protocol.
	 *
	 * @return List of ParticipantRegInfo
	 * @throws BizLogicException
	 * @throws ClassNotFoundException
	 */
	public List<ParticipantRegistrationInfo> getAllParticipantRegistrationInfo()
			throws BizLogicException;

	/**
	 * This method is called if any Offset is given for shift in anticipated
	 * dates. In this method complete traversal of all the CollectionProtocols
	 * is done and the below hierarchy registered CP's are shifted in
	 * anticipated dates by the number of days as specified by the offset.
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws BizLogicException
	 *             DAOException
	
	 */
	public void updateForOffset(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration,
			int offset) throws BizLogicException;

	/**
	 * This method is called if the CollectionProtocol has Offset and also has
	 * any Child Collection Protocols so as to shift there anticipated dates as
	 * per the Offset specified
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws BizLogicException
	 *             DAOException
	
	 */
	public void checkAndUpdateChildOffset(DAO dao,
			SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration,
			int offset) throws BizLogicException;

	/**
	 * is Read Denied To be Checked.
	 */
	public boolean isReadDeniedTobeChecked();

	/**
	 * get Read Denied Privilege Name.
	 */
	public String getReadDeniedPrivilegeName();

	/**
	 * has Privilege To View.
	 */
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean);

	/**
	 * This method is as a part of removing the cp based cache.
	 * @return This will returns all the CPs from the database.
	 * @throws BizLogicException BizLogic Exception
	 */
	public List<NameValueBean> getCollectionProtocolBeanList()
			throws BizLogicException;

	public CollectionProtocolRegistration getRegistrationByGridId(String gridId) throws BizLogicException;

	/**
	 * Attempts to find a {@link CollectionProtocolRegistration} of the given {@link Participant} on the given
	 * {@link CollectionProtocol}.
	 * @param cp
	 * @param p
	 * @return
	 * @throws BizLogicException 
	 */
	public CollectionProtocolRegistration findRegistration(
			CollectionProtocol cp, Participant p) throws BizLogicException;

	public void saveOrUpdateRegistration(
			CollectionProtocolRegistration existingCpr, String userName) throws BizLogicException;

	public abstract void validateCPR(CollectionProtocolRegistration cpr) throws BizLogicException;
	
	

}