
package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.language.Metaphone;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class will fetch the participants from the DB based on 
 * MRN,SSN,lastname and last name metaPhonecode.
 * Generates the list of participants and returns the list to
 * SPR participant matching algorithm.
 * @author geeta_jaggal
 */
public class ParticipantMatchingBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Logger object.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ParticipantBizLogic.class);

	/**
	 *  This method will return the participant records based on MRN,SSN and last name.
	 * @param userParticipant : Participants object with user entered values.
	 * @return Map of all participant.
	 * @throws BizLogicException : BizLogicException
	 */
	public Map<String, Participant> getAllParticipants(Participant userParticipant)
			throws BizLogicException
	{
		final Map<String, Participant> mapOfParticipants = new HashMap<String, Participant>();
		DAO dao = null;
		Metaphone metaPhoneObj = new Metaphone();
		try
		{
			dao = this.openDAOSession(null);
			if (userParticipant.getParticipantMedicalIdentifierCollection() != null
					&& userParticipant.getParticipantMedicalIdentifierCollection().size() > 0)
			{
				getParticipantsForMRN(dao, mapOfParticipants, userParticipant
						.getParticipantMedicalIdentifierCollection());
			}
			if (userParticipant.getSocialSecurityNumber() != null
					&& userParticipant.getSocialSecurityNumber() != "")
			{
				getParticipantsForSSN(dao, mapOfParticipants, userParticipant
						.getSocialSecurityNumber());
			}
			if (userParticipant.getLastName() != null && userParticipant.getLastName() != "")
			{
				getParticipantsForLName(dao, mapOfParticipants, userParticipant.getLastName());
				String metaPhoneCode = metaPhoneObj.metaphone(userParticipant.getLastName());
				getParticipantsForLNameMetaPhone(dao, mapOfParticipants, metaPhoneCode);
			}

		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return mapOfParticipants;
	}

	/**
	 * This method fetch the participants based on mrn value.
	 * @param dao : DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param partiMedIdCollection : Collection of ParticipantMedicalIdentifier objects
	 * @throws DAOException : DAOException
	 */
	private void getParticipantsForMRN(DAO dao, Map<String, Participant> mapOfParticipants,
			Collection<ParticipantMedicalIdentifier> partiMedIdCollection) throws DAOException
	{
		String participantQueryStr = null;
		String medicalRecordNumber = null;
		String siteId = null;
		List<ParticipantMedicalIdentifier> listOfParticipantsMedId = null;
		Iterator<ParticipantMedicalIdentifier> iterator = partiMedIdCollection.iterator();
		while (iterator.hasNext())
		{
			ParticipantMedicalIdentifier participantMedicalIdentifier =
				(ParticipantMedicalIdentifier) iterator
					.next();
			medicalRecordNumber = participantMedicalIdentifier.getMedicalRecordNumber();
			siteId = String.valueOf(participantMedicalIdentifier.getSite().getId());
			if (medicalRecordNumber != null && siteId != null)
			{
				participantQueryStr = "from " + ParticipantMedicalIdentifier.class.getName()
						+ " as participantMedId" + " where "
						+ " participantMedId.medicalRecordNumber ='"
						+ medicalRecordNumber + "'"
						+ " and participantMedId.site.id='" + siteId
						+ "'";
				// query for MRN exact match ...
				listOfParticipantsMedId = dao.executeQuery(participantQueryStr);
				populateParticipantMapForMRN(mapOfParticipants, listOfParticipantsMedId);
				// perform the fuzzy match.Generate the different combinations of
				//MRN value and fetch the records from DB for that MRN.
				fuzzyMatchOnMRN1(dao, mapOfParticipants, medicalRecordNumber, siteId);
				fuzzyMatchOnMRN2(dao, mapOfParticipants, medicalRecordNumber, siteId);
			}
		}
	}

	/**
	 * This method will perform the fuzzy match on mrn value and for Different combinations
	 * of mrn value fetch the records from the DB.
	 * @param dao : DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param mrn : mrn value.
	 * @param siteId : site id
	 * @throws DAOException : DAOException
	 */
	private void fuzzyMatchOnMRN1(DAO dao, Map<String, Participant> mapOfParticipants, String mrn,
			String siteId) throws DAOException
	{
		StringBuffer tempMRNStr = new StringBuffer();
		String participantQueryStr = null;
		List<ParticipantMedicalIdentifier> listOfParticipantsMedId = null;
		for (int i = 0; i < mrn.length() - 1; i++)
		{
			for (int j = 0; j < mrn.length(); j++)
			{
				if (j == i)
				{
					tempMRNStr.append((mrn.charAt(i + 1)));
					j++;
					tempMRNStr.append((mrn.charAt(i)));
				}
				else
				{
					tempMRNStr.append((mrn.charAt(j)));
				}
			}
			System.out
					.println(" The MRN value  ****************************************************************"
							+ tempMRNStr.toString());
			participantQueryStr = "from " + ParticipantMedicalIdentifier.class.getName()
					+ " as participantMedId" + " where participantMedId.medicalRecordNumber ='"
					+ tempMRNStr.toString() + "'" + " and participantMedId.site.id='" + siteId
					+ "'";
			listOfParticipantsMedId = dao.executeQuery(participantQueryStr);
			populateParticipantMapForMRN(mapOfParticipants, listOfParticipantsMedId);
			tempMRNStr.delete(0, tempMRNStr.length());
		}
	}

	/**
	 * This method will perform the fuzzy match on mrn value and for Different
	 * combinations of mrn value fetch the records from the DB.
	 * @param dao : DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param mrn : mrn value.
	 * @param siteId : site id.
	 * @throws DAOException :  DAOException.
	 */
	private void fuzzyMatchOnMRN2(DAO dao, Map<String, Participant> mapOfParticipants, String mrn,
			String siteId) throws DAOException
	{
		char[] charArray = null;
		char digit = 0;
		StringBuffer tempMRNStr = new StringBuffer();
		charArray = mrn.toCharArray();
		String participantQueryStr = null;
		List<ParticipantMedicalIdentifier> listOfParticipantsMedId = null;
		for (int i = 0; i < mrn.length(); i++)
		{
			digit = charArray[i];
			if (digit < '9')
			{
				charArray[i] = ++charArray[i];
				tempMRNStr.append(charArray);
				System.out
						.println(" The MRN value  **********************************************************"
								+ tempMRNStr.toString());
				participantQueryStr = "from " + ParticipantMedicalIdentifier.class.getName()
						+ " as participantMedId" + " where "
						+ " participantMedId.medicalRecordNumber ='"
						+ tempMRNStr.toString() + "'"
						+ " and participantMedId.site.id='" + siteId
						+ "'";
				listOfParticipantsMedId = dao.executeQuery(participantQueryStr);
				populateParticipantMapForMRN(mapOfParticipants, listOfParticipantsMedId);
				charArray[i] = --charArray[i];
			}
			tempMRNStr = tempMRNStr.delete(0, tempMRNStr.length());
			if (digit > '0')
			{
				charArray[i] = --charArray[i];
				tempMRNStr.append(charArray);
				System.out
						.println(" The MRN value  **********************************************************"
								+ tempMRNStr.toString());
				participantQueryStr = "from " + ParticipantMedicalIdentifier.class.getName()
						+ " as participantMedId" + " where"
						+ " participantMedId.medicalRecordNumber ='"
						+ tempMRNStr.toString() + "'"
						+ " and participantMedId.site.id='" + siteId
						+ "'";
				listOfParticipantsMedId = dao.executeQuery(participantQueryStr);
				populateParticipantMapForMRN(mapOfParticipants, listOfParticipantsMedId);
				charArray[i] = ++charArray[i];
			}
			tempMRNStr = tempMRNStr.delete(0, tempMRNStr.length());

		}

	}

	/**
	 * This methos will fetch the participants from DB based on the SSN.
	 * @param dao :DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param ssn : ssn value.
	 * @throws DAOException : DAOException
	 */
	private void getParticipantsForSSN(DAO dao, Map<String, Participant> mapOfParticipants,
			String ssn) throws DAOException
	{
		String participantQueryStr = null;
		List<Participant> listOfParticipants = null;
		participantQueryStr = "from " + Participant.class.getName() + " as participant"
				+ " where participant.socialSecurityNumber ='" + ssn + "'";
		// Fetch teh SSN exact match records from DB .
		listOfParticipants = dao.executeQuery(participantQueryStr);
		populateParticipantMap(mapOfParticipants, listOfParticipants);
		// perform the fuzzy match.Generate the different
		//combinations of MRN value and fetch the records from DB for that MRN.
		if(ssn!=null){
	    	String ssnValue[]=ssn.split("-");
	    	ssn=ssnValue[0];
	    	ssn+=ssnValue[1];
	    	ssn+=ssnValue[2];
		}
		fuzzyMatchOnSSN1(dao, mapOfParticipants, ssn);
		fuzzyMatchOnSSN2(dao, mapOfParticipants, ssn);
	}

	/**
	 * This method will perform the fuzzy match on SSN value and
	 * for Different combinations of ssn value fetch the records from the DB.
	 * @param dao :DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param ssn : ssn value.
	 * @throws DAOException : DAOException
	 */
	private void fuzzyMatchOnSSN1(DAO dao, Map<String, Participant> mapOfParticipants, String ssn)
			throws DAOException
	{
		List<Participant> listOfParticipants = null;
		StringBuffer tempssnStr = new StringBuffer();
		String participantQueryStr = null;
		for (int i = 0; i < ssn.length() - 1; i++)
		{
			for (int j = 0; j < ssn.length(); j++)
			{
				if (j == i)
				{
					tempssnStr.append((ssn.charAt(i + 1)));
					j++;
					tempssnStr.append((ssn.charAt(i)));
				}
				else
				{
					tempssnStr.append((ssn.charAt(j)));
				}
			}
			String tempSSN=AppUtility.getSSN(tempssnStr.toString());
			System.out
					.println(" The SSN value  ***********************************************************"
							+ tempssnStr.toString() + " -------" + tempSSN);
			participantQueryStr = "from " + Participant.class.getName()
								+ " as participant where "
								+ " participant.socialSecurityNumber ='"
								+ tempSSN + "'";
			listOfParticipants = dao.executeQuery(participantQueryStr);
			populateParticipantMap(mapOfParticipants, listOfParticipants);
			tempssnStr.delete(0, tempssnStr.length());
		}

	}

	/**
	 * This method will perform the fuzzy match on SSN value and for
	 * different combinations of ssn value fetch the records from the DB.
	 * @param dao : DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param ssn : ssn value.
	 * @throws DAOException : DAOException
	 */
	private void fuzzyMatchOnSSN2(DAO dao, Map<String, Participant> mapOfParticipants, String ssn)
			throws DAOException
	{
		List<Participant> listOfParticipants = null;
		StringBuffer tempssnStr = new StringBuffer();
		String participantQueryStr = null;
		char[] charArray = null;
		char digit = 0;
		charArray = ssn.toCharArray();
		for (int i = 0; i < ssn.length(); i++)
		{
			digit = charArray[i];
			if (digit < '9')
			{
				charArray[i] = ++charArray[i];
				tempssnStr.append(charArray);
				String tempSSN=AppUtility.getSSN(tempssnStr.toString());
				System.out
						.println(" The SSN value  ***************************************************************"
								+ tempssnStr.toString()+ "-------" + tempSSN);
				participantQueryStr = "from " + Participant.class.getName() + " as participant"
						+ " where participant.socialSecurityNumber ='"
						+ tempSSN + "'";
				listOfParticipants = dao.executeQuery(participantQueryStr);
				populateParticipantMap(mapOfParticipants, listOfParticipants);
				charArray[i] = --charArray[i];
			}
			tempssnStr = tempssnStr.delete(0, tempssnStr.length());
			if (digit > '0')
			{
				charArray[i] = --charArray[i];
				tempssnStr.append(charArray);
				String tempSSN=AppUtility.getSSN(tempssnStr.toString());
				System.out
						.println(" The SSN value  **************************************************************"
								+ tempssnStr.toString() +" ------- " + tempSSN);
				participantQueryStr = "from " + Participant.class.getName() + " as participant"
						+ " where participant.socialSecurityNumber ='"
						+ tempssnStr.toString()
						+ "'";
				listOfParticipants = dao.executeQuery(participantQueryStr);
				populateParticipantMap(mapOfParticipants, listOfParticipants);
				charArray[i] = ++charArray[i];
			}
			tempssnStr = tempssnStr.delete(0, tempssnStr.length());
		}
	}

	/**
	 * Fetch the records based on last name.
	 * @param dao : DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param lastName : last name.
	 * @throws DAOException : DAOException
	 */
	private void getParticipantsForLName(DAO dao, Map<String, Participant> mapOfParticipants,
			String lastName) throws DAOException
	{
		String participantQueryStr = null;
		List<Participant> listOfParticipants = null;
		participantQueryStr = "from " + Participant.class.getName() + " as participant"
				+ " where participant.lastName  like '" + lastName + "%'";
		listOfParticipants = dao.executeQuery(participantQueryStr);
		populateParticipantMap(mapOfParticipants, listOfParticipants);
	}

	/**
	 * Fetch the participants based on the last name metaPhonic code.
	 * @param dao :DAO object.
	 * @param mapOfParticipants : map of participants.
	 * @param metaPhoneCode :metaPhonic code.
	 * @throws DAOException :DAOException
	 */
	private void getParticipantsForLNameMetaPhone(DAO dao,
			Map<String, Participant> mapOfParticipants, String metaPhoneCode) throws DAOException
	{
		String participantQueryStr = null;
		List<Participant> listOfParticipants = null;
		participantQueryStr = "from " + Participant.class.getName() + " as participant"
				+ " where participant.metaPhoneCode ='" + metaPhoneCode + "'";
		listOfParticipants = dao.executeQuery(participantQueryStr);
		populateParticipantMap(mapOfParticipants, listOfParticipants);
	}

	/**
	 * This method populate the map with participant objects.
	 * @param mapOfParticipants : Map of participants.
	 * @param listOfParticipants : list of participants.
	 */
	private void populateParticipantMap(Map<String, Participant> mapOfParticipants,
			List<Participant> listOfParticipants)
	{
		Participant participant = null;
		if (listOfParticipants != null)
		{
			Iterator<Participant> participantIterator = listOfParticipants.iterator();
			while (participantIterator.hasNext())
			{
				participant = (Participant) participantIterator.next();
				Long participantId = participant.getId();
				mapOfParticipants.put(String.valueOf(participantId), participant);
			}
		}
	}

	/**
	 * This method populate the map with participant objects
	 * from the ParticipantMedicalIdentifier objects.
	 * @param mapOfParticipants : map of participants.
	 * @param listOfParticipantsMedId : list of ParticipantMedicalIdentifier object,
	 */
	private void populateParticipantMapForMRN(Map<String, Participant> mapOfParticipants,
			List<ParticipantMedicalIdentifier> listOfParticipantsMedId)
	{
		Participant participant = null;
		ParticipantMedicalIdentifier participantMedIdObj = null;
		if (listOfParticipantsMedId != null && !listOfParticipantsMedId.isEmpty())
		{
			Iterator<ParticipantMedicalIdentifier> participantIterator = listOfParticipantsMedId
					.iterator();
			while (participantIterator.hasNext())
			{
				participantMedIdObj = (ParticipantMedicalIdentifier) participantIterator.next();
				participant = participantMedIdObj.getParticipant();
				Long participantId = participant.getId();
				mapOfParticipants.put(String.valueOf(participantId), participant);
			}
		}
	}

}
