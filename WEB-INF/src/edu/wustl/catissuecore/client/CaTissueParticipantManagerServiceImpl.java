
package edu.wustl.catissuecore.client;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.wustl.catissuecore.bizlogic.ParticipantUtil;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.participant.client.IParticipantManager;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class CaTissueParticipantManagerServiceImpl implements IParticipantManager
{

	public String getPICordinatorsofProtocol()
	{
		// TODO Auto-generated method stub
		final StringBuffer hql = new StringBuffer();
		hql
				.append("select CPReg.collectionProtocol.principalInvestigator.firstName,CPReg.collectionProtocol.p"
						+ "rincipalInvestigator.lastName from edu.wustl.catissuecore.domain.CollectionProtocolRegistration "
						+ " CPReg where CPReg.participant.id= ? ");
		// need to provide the implemenattion for fetching the coordinators name for the collection protocol
		return hql.toString();
	}

	public Set<String> getAssocitedMutliInstProtocolIdList(Long csId) throws ApplicationException
	{
		return new HashSet<String>();
	}

	/**
	 * Prepare the query fetch participants based on MRN
	 *
	 * @param protocol id set
	 *
	 * @return the fetch by MRN query
	 *
	 */
	public String getMRNQuery(Set<Long> protocolIdSet)
	{

		String protocolIdStr = ParticipantManagerUtility.getProtocolIdsString(protocolIdSet);
		String fetchByMRNQry = null;
		if (protocolIdSet == null || protocolIdSet.isEmpty())
		{
			fetchByMRNQry = " select  pmi.participant from  "
					+ ParticipantMedicalIdentifier.class.getName()
					+ "  pmi where  pmi.medicalRecordNumber=? and pmi.site.id=? and pmi.participant.activityStatus!=? ";
		}
		else
		{
			fetchByMRNQry = " select pmi.participant from"
					+ ParticipantMedicalIdentifier.class.getName()
					+ " pmi, "
					+ CollectionProtocolRegistration.class.getName()
					+ " cpr join cpr.participant "
					+ " where cpr.collectionProtocol.id in ("
					+ protocolIdStr
					+ ") and pmi.participant.id=cpr.participant.id and pmi.medicalRecordNumber=? and pmi.site.id=? and pmi.participant.activityStatus!=? ";
		}

		return fetchByMRNQry;
	}

	/**
	 * Prepare the query fetch participants based on SSN
	 *
	 * @param protocol id set
	 *
	 * @return the fetch by SSN query
	 *
	 */
	public String getSSNQuery(Set<Long> protocolIdSet)
	{
		String protocolIdStr = ParticipantManagerUtility.getProtocolIdsString(protocolIdSet);
		String fetchBySSNQry = null;
		if (protocolIdSet == null || protocolIdSet.isEmpty())
		{
			fetchBySSNQry = " from "
					+ Participant.class.getName()
					+ " participant where participant.socialSecurityNumber=?  and participant.activityStatus!=?";
		}
		else
		{

			fetchBySSNQry = "select cpr.participant from  "
					+ CollectionProtocolRegistration.class.getName()
					+ "  cpr join cpr.participant where cpr.collectionProtocol.id in ("
					+ protocolIdStr
					+ ") and cpr.participant.socialSecurityNumber=? and cpr.participant.activityStatus!=?";
		}
		return fetchBySSNQry;
	}

	/**
	 * Prepare the query fetch participants based on Last name
	 *
	 * @param protocol id set
	 *
	 * @return the fetch by Last name query
	 *
	 */
	public String getLastNameQuery(Set<Long> protocolIdSet)
	{
		String protocolIdStr = ParticipantManagerUtility.getProtocolIdsString(protocolIdSet);
		String fetchByNameQry = null;
		if (protocolIdSet == null || protocolIdSet.isEmpty())
		{
			fetchByNameQry = " from "
					+ Participant.class.getName()
					+ " participant where participant.lastName like ?  and participant.activityStatus!=?";
		}
		else
		{
			fetchByNameQry = "select cpr.participant from  "
					+ CollectionProtocolRegistration.class.getName()
					+ "  cpr join cpr.participant where cpr.collectionProtocol.id in ("
					+ protocolIdStr
					+ ") and cpr.participant.lastName like ? and cpr.participant.activityStatus!=?";
		}
		return fetchByNameQry;
	}

	/**
	 * Prepare the query fetch participants based on MetaPhone code
	 *
	 * @param protocol id set
	 *
	 * @return the fetch by MetaPhone code query
	 *
	 */
	public String getMetaPhoneCodeQuery(Set<Long> protocolIdSet)
	{

		String protocolIdStr = ParticipantManagerUtility.getProtocolIdsString(protocolIdSet);
		String fetchByMetaPhoneQry = null;
		if (protocolIdSet == null || protocolIdSet.isEmpty())
		{
			fetchByMetaPhoneQry = " from "
					+ Participant.class.getName()
					+ " participant where participant.metaPhoneCode=?  and participant.activityStatus!=?";
		}
		else
		{
			fetchByMetaPhoneQry = "select cpr.participant from  "
					+ CollectionProtocolRegistration.class.getName()
					+ " cpr join cpr.participant where cpr.collectionProtocol.id in ("
					+ protocolIdStr
					+ ") and cpr.participant.metaPhoneCode=? and cpr.participant.activityStatus!=?";
		}
		return fetchByMetaPhoneQry;
	}

	public Set<Long> getProtocolIdLstForMICSEnabledForMatching(Long arg0)
			throws ApplicationException
	{
		Set<Long> csIdList = new HashSet<Long>();
		// TODO Auto-generated method stub
		return csIdList;
	}

	//changes by amol
	public LinkedHashSet<Long> getParticipantPICordinators(long participantId)
			throws ApplicationException
	{
		LinkedHashSet<Long> userIdSet = null;
		DAO dao = null;
		try
		{
			dao = ParticipantManagerUtility.getDAO();
			userIdSet = (LinkedHashSet<Long>) ParticipantUtil.getParticipantPICordinators(
					participantId, dao);
		}
		catch (DAOException e)
		{
			// TODO Auto-generated catch block
			throw new ApplicationException(null, e, e.getMsgValues());
		}
		finally
		{
			dao.closeSession();
		}
		return userIdSet;
	}

	//changes by amol
	public String getIsEmpiEnabledQuery()
	{
		String query = " SELECT CP.IS_EMPI_ENABLE FROM CATISSUE_COLLECTION_PROTOCOL CP JOIN  CATISSUE_COLL_PROT_REG CPR  "
				+ " ON CP.IDENTIFIER = CPR.COLLECTION_PROTOCOL_ID WHERE CPR.PARTICIPANT_ID=?";
		return query;
	}

}
