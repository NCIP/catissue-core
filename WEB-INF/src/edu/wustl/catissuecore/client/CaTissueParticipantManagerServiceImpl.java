
package edu.wustl.catissuecore.client;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.participant.client.IParticipantManager;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;

public class CaTissueParticipantManagerServiceImpl implements IParticipantManager
{

	public String getPICordinatorsofProtocol()
	{
		// TODO Auto-generated method stub
		final StringBuffer hql = new StringBuffer();

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
		// TODO Auto-generated method stub
		return null;
	}

}
