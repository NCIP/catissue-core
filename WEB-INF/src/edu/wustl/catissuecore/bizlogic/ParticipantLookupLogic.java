
package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.patientLookUp.domain.PatientInformation;
import edu.wustl.patientLookUp.lookUpServiceBizLogic.PatientInfoLookUpService;
import edu.wustl.patientLookUp.queryExecutor.IQueryExecutor;
import edu.wustl.patientLookUp.queryExecutor.SQLQueryExecutorImpl;
import edu.wustl.patientLookUp.util.PatientLookupException;

/**
 * @author santosh_chandak This class is for finding out the matching
 *         participant with respect to given participant. It implements the
 *         lookUp method of LookupLogic interface which returns the list of all
 *         matching participants to the given participant.
 */
public class ParticipantLookupLogic implements LookupLogic
{

	// Getting points from the xml file in static variables
	private static final int pointsForSSNExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_SSN_EXACT));
	private static final int pointsForSSNPartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_SSN_PARTIAL));
	private static final int pointsForPMIExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_PMI_EXACT));
	private static final int pointsForPMIPartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_PMI_PARTIAL));
	private static final int pointsForDOBExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_DOB_EXACT));
	private static final int pointsForDOBPartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_DOB_PARTIAL));
	private static final int pointsForLastNameExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_LAST_NAME_EXACT));
	private static final int pointsForLastNamePartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_LAST_NAME_PARTIAL));
	private static final int pointsForFirstNameExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_FIRST_NAME_EXACT));
	private static final int pointsForFirstNamePartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_FIRST_NAME_PARTIAL));
	private static final int pointsForMiddleNameExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_MIDDLE_NAME_EXACT));
	private static final int pointsForMiddleNamePartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_MIDDLE_NAME_PARTIAL));
	private static final int pointsForRaceExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_RACE_EXACT));
	private static final int pointsForRacePartial = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_RACE_PARTIAL));
	private static final int pointsForGenderExact = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_GENDER_EXACT));
	private static final int bonusPoints = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_BONUS));
	private static final int matchCharactersForLastName = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_MATCH_CHARACTERS_FOR_LAST_NAME));
	private static final int cutoffPointsFromProperties = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF));
	private static final int totalPointsFromProperties = pointsForFirstNameExact
			+ pointsForMiddleNameExact + pointsForLastNameExact + pointsForDOBExact
			+ pointsForSSNExact + pointsForGenderExact + pointsForRaceExact + pointsForPMIExact;
	private int cutoffPoints;
	private int totalPoints;
	private boolean isSSNOrPMI = false;
	private int maxNoOfParticipantsToReturn;
	/**
	 * This function first retrieves all the participants present in the
	 * PARTICIPANT table. Then it checks for possible match of given participant
	 * and the list of participants retrieved from database. Based on the
	 * criteria in the MPI matching algorithm, it returns the list of all
	 * matching participants.
	 * 
	 * @param params
	 *            - LookupParameters
	 * @return list - List of matching Participants.
	 * @throws Exception : Exception
	 */
	public List lookup(LookupParameters params) throws Exception
	{
		// Done for Junit
		if (params == null)
		{
			throw new Exception("Params can not be null");
		}

		final DefaultLookupParameters participantParams = (DefaultLookupParameters) params;

		// Getting the participant object created by user
		final Participant participant = (Participant) participantParams.getObject();

		// if cutoff is greater than total points, throw exception
		//if (this.cutoffPoints > totalPointsFromProperties)
		//{
			//throw new Exception(ApplicationProperties.getValue("errors.lookup.cutoff"));
		//}
		// get total points depending on Participant object created by user
		this.cutoffPoints = Integer.valueOf(XMLPropertyHandler.getValue(Constants.CUTTOFFPOINTS));
		this.maxNoOfParticipantsToReturn= Integer.valueOf(XMLPropertyHandler.getValue(Constants.MAX_NO_OF_PARTICIPANTS_TO_RETURN));
		
		// adjust cutoffPoints as per new total points
		//this.cutoffPoints = cutoffPointsFromProperties * this.totalPoints/ totalPointsFromProperties;
		
		PatientInformation patientInformation = new PatientInformation();
		patientInformation.setLastName(participant.getLastName());
		patientInformation.setFirstName(participant.getFirstName());
		patientInformation.setMiddleName(participant.getMiddleName());
		String ssn= participant.getSocialSecurityNumber();
	    if(ssn!=null){
	    	String ssnValue[]=ssn.split("-");
	    	ssn=ssnValue[0];
	    	ssn+=ssnValue[1];
	    	ssn+=ssnValue[2];
	    }
		patientInformation.setSsn(ssn);
		
		patientInformation.setDob(participant.getBirthDate());
		patientInformation.setGender(participant.getGender());

		Collection<String> participantInfoMedicalIdentifierCollection = new ArrayList<String>();

		Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = participant
				.getParticipantMedicalIdentifierCollection();
		if (participantMedicalIdentifierCollection != null)
		{
			Iterator<ParticipantMedicalIdentifier> itr = participantMedicalIdentifierCollection.iterator();
			while (itr.hasNext())
			{
				ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) itr
						.next();
				participantInfoMedicalIdentifierCollection.add(participantMedicalIdentifier
						.getMedicalRecordNumber());
				participantInfoMedicalIdentifierCollection.add(String.valueOf(participantMedicalIdentifier.getSite().getId()));
			}
		}
		patientInformation
				.setParticipantMedicalIdentifierCollection(participantInfoMedicalIdentifierCollection);

		Collection<String> participantInfoRaceCollection = new HashSet<String>();

		Collection<Race> participantRaceCollection = participant.getRaceCollection();
		if (participantRaceCollection != null)
		{
			Iterator<Race> itr = participantRaceCollection.iterator();
			while (itr.hasNext())
			{
				Race race = (Race) itr.next();
				if (race != null)
				{
					//participantInfoRaceCollection.add((String)itr.next());
					participantInfoRaceCollection.add(race.getRaceName());
				}
			}
		}
		patientInformation.setRaceCollection(participantInfoRaceCollection);
		List<DefaultLookupResult> participants = searchMatchingParticipant(patientInformation);
		return participants;

	}

	/**
	 * @param patientInformation
	 * @return
	 * @throws PatientLookupException
	 * @throws ParseException
	 * @throws ApplicationException
	 */
	protected List<DefaultLookupResult> searchMatchingParticipant(PatientInformation patientInformation) throws PatientLookupException,ParseException,ApplicationException
	{
		List<DefaultLookupResult> matchingParticipantsList=new ArrayList<DefaultLookupResult>();
		PatientInfoLookUpService patientLookupObj = new PatientInfoLookUpService();
		JDBCDAO jdbcDAO =AppUtility.openJDBCSession();
		IQueryExecutor queryExecutor = new SQLQueryExecutorImpl(jdbcDAO);
		List<PatientInformation> patientInfoList = patientLookupObj.patientLookupService(patientInformation,
				queryExecutor, cutoffPoints, maxNoOfParticipantsToReturn);

		
		if (patientInfoList != null && patientInfoList.size() > 0)
		{
			for (int i = 0; i < patientInfoList.size(); i++)
			{
				patientInformation = (PatientInformation) patientInfoList.get(i);
				DefaultLookupResult result = new DefaultLookupResult();
				Participant partcipantNew = new Participant();
				partcipantNew.setId(new Long(patientInformation.getUpi()));

				partcipantNew.setLastName(patientInformation.getLastName());
				partcipantNew.setFirstName(patientInformation.getFirstName());
				partcipantNew.setMiddleName(patientInformation.getMiddleName());
				
				partcipantNew.setBirthDate(patientInformation.getDob());
				
				partcipantNew.setGender(patientInformation.getGender());
				
				partcipantNew.setActivityStatus("Active");
				if (patientInformation.getSsn() != null && patientInformation.getSsn() != "")
				{
					String ssn=AppUtility.getSSN(patientInformation.getSsn());
					partcipantNew.setSocialSecurityNumber(ssn);
				}
			   Collection<String> participantInfoMedicalIdentifierCollection = patientInformation.getParticipantMedicalIdentifierCollection();
				Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollectionNew=new LinkedHashSet<ParticipantMedicalIdentifier>(); 
				if (participantInfoMedicalIdentifierCollection != null && participantInfoMedicalIdentifierCollection.size()>0)
				{
					Iterator<String> iterator = participantInfoMedicalIdentifierCollection.iterator();
					while (iterator.hasNext())
					{
						String mrn = (String) iterator.next();
						String siteId= (String) iterator.next();
						Site site = new Site();
						site.setId(Long.valueOf(siteId));
						ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
						participantMedicalIdentifier.setMedicalRecordNumber(mrn);
						participantMedicalIdentifier.setSite(site);
						participantMedicalIdentifierCollectionNew.add(participantMedicalIdentifier);
					}
				}
				partcipantNew.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollectionNew);
				result.setObject(partcipantNew);
				matchingParticipantsList.add(result);
			}
		}
		AppUtility.closeJDBCSession(jdbcDAO);

		return matchingParticipantsList;
	}

	
	/**
	 * This function calculates the total based on values entered by user
	 * 
	 * @param participant
	 *            - participant object
	 * @return - cutoff points
	 */
	private int calculateTotalPoints(Participant participant)
	{
		int totalPointsForParticipant = 0;
		if (participant.getBirthDate() != null)
		{
			totalPointsForParticipant += pointsForDOBExact;
		}
		if (participant.getFirstName() != null && !participant.getFirstName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForFirstNameExact;
		}
		if (participant.getMiddleName() != null && !participant.getMiddleName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForMiddleNameExact;
		}
		if (participant.getLastName() != null && !participant.getLastName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForLastNameExact;
		}
		if (participant.getSocialSecurityNumber() != null
				&& !participant.getSocialSecurityNumber().trim().equals(""))
		{
			totalPointsForParticipant += pointsForSSNExact;
		}
		if (participant.getGender() != null && !participant.getGender().trim().equals("")
				&& (!participant.getGender().equals("Unspecified")))
		{
			totalPointsForParticipant += pointsForGenderExact;
		}
		if (participant.getRaceCollection() != null
				&& participant.getRaceCollection().isEmpty() == false)
		{
			final Iterator<Race> raceIterator = participant.getRaceCollection().iterator();
			final Race race = raceIterator.next();
			if (!race.getRaceName().equals("Unknown"))
			{
				totalPointsForParticipant += pointsForRaceExact;
			}
		}
		if (participant.getParticipantMedicalIdentifierCollection() != null
				&& participant.getParticipantMedicalIdentifierCollection().size() > 0)
		{
			totalPointsForParticipant += pointsForPMIExact;
		}
		return totalPointsForParticipant;
	}
}