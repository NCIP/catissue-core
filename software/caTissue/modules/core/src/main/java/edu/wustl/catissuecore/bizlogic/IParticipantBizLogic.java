package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public interface IParticipantBizLogic {

	/**
	 * @param participant - participant object.
	 * @param lookupLogic - LookupLogic object
	 * @return - List of matched participant.
	 * @throws Exception - throws exception.
	 */
	public abstract List<DefaultLookupResult> getListOfMatchingParticipants(
			Participant participant, LookupLogic lookupLogic) throws Exception;

	/**
	 * @return Map of all participant.
	 * @throws BizLogicException throws BizLogicException
	 */
	public abstract Map<Long, Participant> getAllParticipants()
			throws BizLogicException;

	/**
	 * Finds a {@link Participant} by Grid ID.
	 * @param gridId
	 * @return
	 * @throws BizLogicException
	 */
	public abstract Participant getParticipantByGridId(String gridId)
			throws BizLogicException;

	/**
	 * This function takes identifier as parameter and returns
	 * corresponding Participant.
	 * @param identifier Participantidentifier
	 * @return - Participant object
	 */
	public abstract Participant getParticipantById(Long identifier)
			throws Exception;

	/**
	 * logic: check whether this participant object is having
	 * @param participant :participant.
	 * @param cpid : cpid
	 * @param sessionDataBean :sessionDataBean
	 * @throws ApplicationException : ApplicationException
	 */
	public abstract Long registerParticipant(Object object, Long cpid,
			String userName) throws ApplicationException;
	
	public void saveOrUpdateParticipant(Participant participant, String userName) throws BizLogicException;

	/**
	 * Returns a {@link Participant} that can be identified at least by one of the identifiers in the list. 
	 * @param pmiList
	 * @return
	 * @throws BizLogicException 
	 */
	public abstract Participant getParticipantByPMI(
			Collection<ParticipantMedicalIdentifier> pmiList) throws BizLogicException;
	
	/**
	 * Returns a {@link Participant} that can be identified by the given {@link ParticipantMedicalIdentifier}. 
	 * @param pmiList
	 * @return
	 * @throws BizLogicException 
	 */
	public abstract Participant getParticipantByPMI(
			ParticipantMedicalIdentifier pmiList) throws BizLogicException;

	public abstract void resisterCPR(final DAO dao, SessionDataBean sessionDataBean, final Participant participant)
			throws DAOException, BizLogicException;
	
	/**
	 * @param cpr
	 */
	public void init(CollectionProtocolRegistration cpr);

	public abstract void registerToCPR(DAO dao, SessionDataBean sessionDataBean,
			Participant participant, final CollectionProtocolRegistration cpr) throws BizLogicException;

	public abstract boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean);
}