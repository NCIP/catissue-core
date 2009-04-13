package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
import edu.wustl.common.lookup.MatchingStatus;
import edu.wustl.common.lookup.MatchingStatusForSSNPMI;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;

public class ParticipantLookupLogicForSPR implements LookupLogic
{
	// Getting points from the xml file in static variables
	private static final int pointsForSSNExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_SSN_EXACT));
	private static final int pointsForSSNPartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_SSN_PARTIAL));
	private static final int pointsForPMIExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_PMI_EXACT));
	private static final int pointsForPMIPartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_PMI_PARTIAL));
	private static final int pointsForDOBExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_DOB_EXACT));
	private static final int pointsForDOBPartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_DOB_PARTIAL));
	private static final int pointsForLastNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_LAST_NAME_EXACT));
	private static final int pointsForLastNamePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_LAST_NAME_PARTIAL));
	private static final int pointsForFirstNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_FIRST_NAME_EXACT));
	private static final int pointsForFirstNamePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_FIRST_NAME_PARTIAL));
	private static final int totalPointsFromProperties = pointsForFirstNameExact + pointsForLastNameExact
	+ pointsForDOBExact + pointsForSSNExact +pointsForPMIExact;
	private static final int sprCutOff = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_CUT_OFF));
	
	private int cutoffPoints;
	private int totalPoints;
	private MatchingStatus isSSNTemp = MatchingStatus.NOMATCH;
	private MatchingStatusForSSNPMI isSSNPMITemp = MatchingStatusForSSNPMI.NOMATCH;
	
	
	
	/**
	 * This function first retrieves all the participants present in the PARTICIPANT table. Then it checks 
	 * for possible match of given participant and the list of participants retrieved from database. 
	 * Based on the criteria in the MPI matching algorithm, it returns the list of all matching participants. 
	 * @param params - LookupParameters
	 * @return list - List of matching Participants. 
	 */
	public List<DefaultLookupResult> lookup(LookupParameters params) throws Exception
	{
		// Done for Junit
		if (params == null)
		{
			throw new Exception("Params can not be null");
		}

		DefaultLookupParameters participantParams = (DefaultLookupParameters) params;

		// Getting the participant object created by user
		Participant participant = (Participant) participantParams.getObject();

		// if cutoff is greater than total points, throw exception
		if (cutoffPoints > totalPointsFromProperties)
		{
			throw new Exception(ApplicationProperties.getValue("errors.lookup.cutoff"));
		}

		// get total points depending on Participant object created by user
		totalPoints = calculateTotalPoints(participant);
		
		List listOfParticipants = participantParams.getListOfParticipants();

		// In case List of participants is null or empty, return the Matching Participant List as null.
		if (listOfParticipants == null || listOfParticipants.isEmpty() == true)
		{
			return null;
		}

		// calling the searchMatchingParticipant to filter the participant list according to given cutoff value
		List<DefaultLookupResult> participants = searchMatchingParticipant(participant, listOfParticipants);

		return participants;
	}

	/**
	 *  This function calculates the total based on values entered by user
	 * @param participant - participant object
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
		if (participant.getLastName() != null && !participant.getLastName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForLastNameExact;
		}
		if(participant.getSocialSecurityNumber() != null && !participant.getSocialSecurityNumber().trim().equals(""))
		{
			totalPointsForParticipant += pointsForSSNExact;
		}
		return totalPointsForParticipant;
	}

	/**
	 * This function searches the participant which has matching probablity more than cutoff. The different criterias 
	 * considered for finding a possible match are
	 * 1. Social Security Number
	 * 2. Date Of Birth
	 * 3. Last Name 
	 * 4. First Name 
	 * 5. PMI numbers
	 * Points are given to complete or mis match of these parameters. If the total of all these points is >-140  
	 * then participant is considered as a match for the given participant. List of all such 
	 * participants is returned by this function.  
	 * 
	 * @param userParticipant - participant with which comparision is to be done.
	 * @param listOfParticipants - List of all participants which has atleast one matching parameter.
	 * @param cutoff - is the value such that the participants above the cutoff values are stored in List.
	 * @return list - List of matching Participants. 
	 */
	private List<DefaultLookupResult> searchMatchingParticipant(Participant userParticipant, List<Participant> listOfParticipants) throws Exception
	{
		List<DefaultLookupResult> participants = new ArrayList<DefaultLookupResult>();
		Iterator<Participant> itr = listOfParticipants.iterator();
		// Iterates through all the Participants from the list
		for(Participant existingParticipant : listOfParticipants)
		{
			isSSNTemp =  MatchingStatus.NOMATCH;
			isSSNPMITemp = MatchingStatusForSSNPMI.NOMATCH;
			int weight = 0; // used for calculation of total points.
			int socialSecurityNumberWeight = 0; // points of social security number
			int birthDateWeight = 0; // points of birth date
			// Check for the participant only in case its Activity Status = active
			if (existingParticipant.getActivityStatus() != null && (existingParticipant.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()) || existingParticipant.getActivityStatus().equals(Status.ACTIVITY_STATUS_CLOSED.toString())))
			{
				if(!isEmptyParticipant(existingParticipant))
				{
					/**
					 *  If user has entered Social Security Number and it is present in the participant from database as well,
					 *  check for match between the two.
					 */
					if (userParticipant.getSocialSecurityNumber() != null && !userParticipant.getSocialSecurityNumber().trim().equals("")
							&& existingParticipant.getSocialSecurityNumber() != null && !existingParticipant.getSocialSecurityNumber().trim().equals(""))
					{
						socialSecurityNumberWeight = checkSSN(userParticipant.getSocialSecurityNumber().trim().toLowerCase(),
								existingParticipant.getSocialSecurityNumber().trim().toLowerCase());
						weight = socialSecurityNumberWeight;
					}
					/**
					 *  If user has entered Date of Birth and it is present in the participant from database as well,
					 *  check for match between the two.
					 */
					if (userParticipant.getBirthDate() != null && existingParticipant.getBirthDate() != null)
					{
						birthDateWeight = checkDateOfBirth(userParticipant.getBirthDate(), existingParticipant.getBirthDate());
						weight += birthDateWeight;
					}
					/**
					 *  If user has entered Last Name and it is present in the participant from database as well,
					 *  check for match between the two.
					 */
					if (userParticipant.getLastName() != null && !userParticipant.getLastName().trim().equals("")
							&& existingParticipant.getLastName() != null && !existingParticipant.getLastName().trim().equals(""))
					{
						weight += checkLastName(userParticipant.getLastName().trim().toLowerCase(), existingParticipant.getLastName().trim()
								.toLowerCase());
					}
					/**
					 *  If user has entered First Name and it is present in the participant from database as well,
					 *  check for match between the two.
					 */
					if (userParticipant.getFirstName() != null && !userParticipant.getFirstName().trim().equals("")
							&& existingParticipant.getFirstName() != null && !existingParticipant.getFirstName().trim().equals(""))
					{
						weight += checkFirstName(userParticipant.getFirstName().trim().toLowerCase(), existingParticipant.getFirstName().trim()
								.toLowerCase());
					}
					weight += checkParticipantMedicalIdentifier(userParticipant.getParticipantMedicalIdentifierCollection(), existingParticipant.getParticipantMedicalIdentifierCollection());
					
					if(weight==totalPointsFromProperties || (isSSNPMITemp==MatchingStatusForSSNPMI.EXACT))
					{
						participants.clear();
						DefaultLookupResult result = new DefaultLookupResult();
						result.setIsSSNPMI(isSSNPMITemp);
						result.setObject(existingParticipant);
						result.setWeight(Double.valueOf(weight));
						participants.add(result);
						break;
					}
					else if(((weight!=totalPointsFromProperties)&&(weight !=0)&&(weight > sprCutOff))||(isSSNPMITemp==MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH)||(isSSNPMITemp==MatchingStatusForSSNPMI.ONEMATCHOTHERNULL))
					{
							DefaultLookupResult result = new DefaultLookupResult();
							result.setIsSSNPMI(isSSNPMITemp);
							result.setObject(existingParticipant);
							result.setWeight(Double.valueOf(weight));
							participants.add(result);
					}
				}
			}
		}
		return participants;
	}
	
	/**
	 * Checks whether PMI collection is empty or not
	 * @param pmiCollection
	 * @return boolean - PMICollection is empty or not. 
	 */
	private boolean isPMICollectionEmpty(Collection<ParticipantMedicalIdentifier> pmiCollection)
	{
		boolean flag= false;
		
		if(pmiCollection != null && pmiCollection.size()>0)
		{
			ParticipantMedicalIdentifier participantMedicalIdentifier = pmiCollection.iterator().next();
			String mrn = participantMedicalIdentifier.getMedicalRecordNumber();
			Site site = participantMedicalIdentifier.getSite();
			if(site==null && (mrn==null || mrn.equals("")))
			{
				flag=true;
			}
		}
		else
		{
			flag =true;
		}
		return flag;
	}
	
	/**
	 * Checks whether Participant has all fields null or not.
	 * @param existingParticipant
	 * @return boolean :-Participant has all fields null or not
	 */
	private boolean isEmptyParticipant(Participant existingParticipant) 
	{
		boolean flag = false;
		if((existingParticipant.getSocialSecurityNumber() == null || 
				existingParticipant.getSocialSecurityNumber().trim().equals(""))&& 
				(existingParticipant.getFirstName() == null || 
				existingParticipant.getFirstName().trim().equals(""))&& 
				(existingParticipant.getLastName() == null || 
				existingParticipant.getLastName().trim().equals(""))&& 
				existingParticipant.getBirthDate() == null )
				{
					Collection< ParticipantMedicalIdentifier> pmiCollection=existingParticipant.getParticipantMedicalIdentifierCollection();
					flag = isPMICollectionEmpty(pmiCollection);
				}
		return flag;
	}

	/**
	 * This function compares the two Social Security Numbers. 
	 * The criteria used for partial match is --> Mismatch of single digit with difference = 1 or any consecutive 
	 * pair of digits are transposed it is considered a partial match.  
	 * Only one occurrence of either of these is considered.
	 * 
	 * @param userSecurityNumber - Social Security Number of user
	 * @param existingSecurityNumber - Social Security Number of Participant from database
	 * @param - if this boolean variable is true then points for SSN will be taken else points of PMI
	 * @return int - points for complete, partial or no match
	 */

	private int checkSSN(String userNumber, String existingNumber)
	{
		MatchingStatus status = checkNumber(userNumber, existingNumber);
		
		switch (status) 
		{
			case EXACT:
				isSSNTemp =MatchingStatus.EXACT;
				return pointsForSSNExact;
			case PARTIAL:
				isSSNTemp =MatchingStatus.PARTIAL;
				return pointsForSSNPartial;
		}
		return 0;
	}
	private int checkPMI(String userNumber, String existingNumber)
	{
		MatchingStatus status = checkNumber(userNumber, existingNumber);
		switch (status) 
		{
		case EXACT:
			return pointsForPMIExact;
		case PARTIAL:
			return pointsForPMIPartial;
		}
		return 0;
	}
	private MatchingStatus checkNumber(String userNumber, String existingNumber)
	{
		// complete match
		if (existingNumber.equals(userNumber))
		{
			return MatchingStatus.EXACT;
		}
		else
		// partial match
		{
			return MatchingStatus.PARTIAL;
		}
	}

	/**
	 * This function compares the two Date Of Births.
	 * The criteria used for partial match is --> If the year and month are equal or day and year are equal
	 * or if the month and day are equal and the year is off by no more than 2 years.
	 * 
	 * @param userBirthDate - Birth Date of user
	 * @param existingBirthDate - Birth Date of Participant from database
	 * @return int - points for complete, partial or no match
	 */

	private int checkDateOfBirth(Date userBirthDate, Date existingBirthDate)
	{
		// complete match
		if (userBirthDate.compareTo(existingBirthDate) == 0)
		{
			return pointsForDOBExact;
		}
		// partial match
		else 
		{
			return pointsForDOBPartial;
		}
	}

	/**
	 * This function compares the two Last Names. 
	 * The criteria used for partial match is --> If the first 5 characters of the last name match then it
	 * is considered a partial match.  We also do a metaphone match on the last name.  Metaphone is a 
	 * standard algorithm that is applied to names to match those that sound alike.  If the metaphone 
	 * matches it is also considered partial.
	 * 
	 * @param userLastName - Last Name of user
	 * @param existingLastName - Last Name of Participant from database
	 * @return int - points for complete, partial or no match
	 */
	private int checkLastName(String userLastName, String existingLastName)
	{
		// complete match
		if (userLastName.compareTo(existingLastName) == 0)
		{
			return pointsForLastNameExact;
		}
		// partial match --> Checks whether first 5 digits or metaphones of two last names are equal
		else 
		{
			return pointsForLastNamePartial;
		}
	}

	/**
	 * This function compares the two First Names. 
	 * The criteria used for partial match is --> If the first character matches,
	 * it is considered a partial match
	 * 
	 * @param userFirstName - First Name of user
	 * @param existingFirstName - First Name of Participant from database
	 * @return int - points for complete, partial or no match
	 */
	private int checkFirstName(String userName, String existingName)
	{
		// complete match
		if (userName.compareTo(existingName) == 0)
		{
			return pointsForFirstNameExact;
		}
		else
		{
			return pointsForFirstNamePartial;
		}
	}
	/**
	 * Name : Vipin Bansal
	 * This function compares the two ParticipantMedicalIdentifier. 
	 * The criteria used for partial match is --> A partial is considered if one is missing and the other is there.
	 *  (eg, missing from the input data but in the database or vice versa).
	 * 
	 * @param userParticipantMedicalIdentifier - Race of user
	 * @param existingParticipantMedicalIdentifier - Race of Participant from database
	 * @return int - points for complete, partial or no match
	 */
	
	private int checkParticipantMedicalIdentifier(final Collection<ParticipantMedicalIdentifier> userParticipantMedicalIdentifier,final Collection<ParticipantMedicalIdentifier> existingParticipantMedicalIdentifier)
	{
			int participantMedicalIdentifierWeight = 0;
			int tempParticipantMedicalIdentifierWeight = 0;
			boolean exactMatchFlag = false;
			boolean partialMatchFlag = false;
			boolean noMatchFlag = false;
			List<ParticipantMedicalIdentifier> tempExistingParticipantMedicalIdentifier = new ArrayList<ParticipantMedicalIdentifier>();
			
			if(existingParticipantMedicalIdentifier.size()>0)
			{
				for(ParticipantMedicalIdentifier pmi : existingParticipantMedicalIdentifier)
				{
					tempExistingParticipantMedicalIdentifier.add(pmi);
				}
			}
			if(!(isPMICollectionEmpty(userParticipantMedicalIdentifier))&& ! isPMICollectionEmpty(tempExistingParticipantMedicalIdentifier))
			{
				int len1 = tempExistingParticipantMedicalIdentifier.size();
				int len2 = userParticipantMedicalIdentifier.size();
				if(len1!=len2)
				{
					partialMatchFlag = true;
					exactMatchFlag = false;
					noMatchFlag = false;
				}
				else
				{
					for(ParticipantMedicalIdentifier userPMIdentifier : userParticipantMedicalIdentifier )
					{
						if(userPMIdentifier.getSite() != null && userPMIdentifier.getSite().getId() != null)
						{
							String medicalRecordNo = userPMIdentifier.getMedicalRecordNumber();
							String SiteId = userPMIdentifier.getSite().getId().toString();
							int maxTempPMIW =pointsForPMIPartial;
							for(ParticipantMedicalIdentifier existingPMIdentifier : tempExistingParticipantMedicalIdentifier )
							{
								if(existingPMIdentifier.getSite() != null && existingPMIdentifier.getSite().getId()!= null)
								{
									String existingSiteId = existingPMIdentifier.getSite().getId().toString();
									String existingMedicalRecordNo = existingPMIdentifier.getMedicalRecordNumber();

									if ( existingSiteId.equals(SiteId) && medicalRecordNo != null)
									{
										tempParticipantMedicalIdentifierWeight = checkPMI(existingMedicalRecordNo,medicalRecordNo);
										
										if(maxTempPMIW < tempParticipantMedicalIdentifierWeight)
										{
											maxTempPMIW = tempParticipantMedicalIdentifierWeight;
										}
										if(tempParticipantMedicalIdentifierWeight==pointsForPMIExact)
										{
											tempExistingParticipantMedicalIdentifier.remove(existingPMIdentifier);
											break;
										}
									}
								}
							}
							if(maxTempPMIW == pointsForPMIPartial)
							{
								noMatchFlag = false;
								partialMatchFlag = true;
								exactMatchFlag = false;
								break;
							}
							else if(maxTempPMIW == pointsForPMIExact)
							{
								noMatchFlag = false;
								partialMatchFlag = false;
								exactMatchFlag = true;
								continue;
							}
						}
					}
				}
			}
			else
			{
				noMatchFlag = true;
				partialMatchFlag = false;
				exactMatchFlag = false;
			}
			
			participantMedicalIdentifierWeight = setIsSSNPMIandPMIWeightMethod(exactMatchFlag,partialMatchFlag, noMatchFlag);
			return participantMedicalIdentifierWeight;
	}
/*	private int checkParticipantMedicalIdentifier(Collection<ParticipantMedicalIdentifier> userParticipantMedicalIdentifier, Collection<ParticipantMedicalIdentifier> existingParticipantMedicalIdentifier)
	{
		int participantMedicalIdentifierWeight = 0;
		int tempParticipantMedicalIdentifierWeight = 0;
		
		MatchingStatus tempPMIflag1 =MatchingStatus.NOMATCH;
		if(!(isPMICollectionEmpty(userParticipantMedicalIdentifier))&& ! isPMICollectionEmpty(existingParticipantMedicalIdentifier))
		{ 
			Iterator<ParticipantMedicalIdentifier> existingParticipantMedicalIdentifierItr = existingParticipantMedicalIdentifier.iterator();
			while(existingParticipantMedicalIdentifierItr.hasNext())
			{
				ParticipantMedicalIdentifier participantMedicalIdentifier =(ParticipantMedicalIdentifier) existingParticipantMedicalIdentifierItr.next();
				boolean tempPMIflag =false;
				if(participantMedicalIdentifier.getSite() != null && participantMedicalIdentifier.getSite().getId() != null)
				{
					Collection<ParticipantMedicalIdentifier> userParticipantMedicalIdentifier1=userParticipantMedicalIdentifier;
					String existingmedicalRecordNo = participantMedicalIdentifier.getMedicalRecordNumber();
					String existingSiteId = participantMedicalIdentifier.getSite().getId().toString();
					Iterator<ParticipantMedicalIdentifier> userParticipantMedicalIdentifierItr1 = userParticipantMedicalIdentifier1.iterator();
					
					while (userParticipantMedicalIdentifierItr1.hasNext())
					{
						ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) userParticipantMedicalIdentifierItr1.next();
						if(participantIdentifier.getSite() != null && participantIdentifier.getSite().getId()!= null)
						{
							String siteId = participantIdentifier.getSite().getId().toString();
							String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();

							if ( siteId.equals(existingSiteId) && existingmedicalRecordNo != null)
							{
								tempParticipantMedicalIdentifierWeight = checkPMI(medicalRecordNo,existingmedicalRecordNo);
								if(pointsForPMIExact==tempParticipantMedicalIdentifierWeight)
								{
									userParticipantMedicalIdentifier1.remove(participantIdentifier);
									tempPMIflag=true;
									break;
								}
							
							}
							else
							{
								tempParticipantMedicalIdentifierWeight = pointsForPMIPartial;
								//tempPMIflag=false;
							}
						}
					}
					if(!tempPMIflag)
					{
						tempPMIflag1 = false;
					}
					participantMedicalIdentifierWeight = participantMedicalIdentifierWeight+ tempParticipantMedicalIdentifierWeight;
				}
			}
			int length = existingParticipantMedicalIdentifier.size();
			if((participantMedicalIdentifierWeight==(pointsForPMIExact*length))&&(userParticipantMedicalIdentifier.size()>existingParticipantMedicalIdentifier.size()))
			{
				length = userParticipantMedicalIdentifier.size();
			}
					int weight =0;
					if(length !=0)
					{
						weight = participantMedicalIdentifierWeight/length;
					}
					if(weight == pointsForPMIExact)
					{
						participantMedicalIdentifierWeight = pointsForPMIExact;
					}
					else if(weight< pointsForPMIExact && weight !=0)
					{
						participantMedicalIdentifierWeight = pointsForPMIPartial;
					}
					else if(weight == 0)
					{
						participantMedicalIdentifierWeight = 0;
					}
					
					if(tempPMIflag1)
					{
						if(isSSNTemp==MatchingStatus.e)
						{
							isSSNPMITemp = MatchingStatus.EXACT;
						}
						else if (isSSNPMITemp==MatchingStatus.NOMATCH)
						{
							isSSNPMITemp = MatchingStatus.PARTIAL;
						}
					}
					
			return participantMedicalIdentifierWeight;
		}
		return 0;
	}*/

	private int setIsSSNPMIandPMIWeightMethod(boolean exactMatchFlag,boolean partialMatchFlag, boolean noMatchFlag) 
	{
		int participantMedicalIdentifierWeight=0;
		if(exactMatchFlag)
		{
			participantMedicalIdentifierWeight = pointsForPMIExact;
			if(isSSNTemp==MatchingStatus.EXACT)
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.EXACT;
			}
			else if(isSSNTemp==MatchingStatus.PARTIAL)
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH;
			}
			else if(isSSNTemp==MatchingStatus.NOMATCH)
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.ONEMATCHOTHERNULL;
			}
		}
		else if (partialMatchFlag)
		{
			participantMedicalIdentifierWeight = pointsForPMIPartial;
			if(isSSNTemp==MatchingStatus.EXACT)
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH;
			}
			else 
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.NOMATCH;
			}
		}
		else if(noMatchFlag)
		{
			participantMedicalIdentifierWeight = 0;
			if(isSSNTemp==MatchingStatus.EXACT)
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.ONEMATCHOTHERNULL;
			}
			else 
			{
				isSSNPMITemp = MatchingStatusForSSNPMI.NOMATCH;
			}
		}
		return participantMedicalIdentifierWeight;
	}
}