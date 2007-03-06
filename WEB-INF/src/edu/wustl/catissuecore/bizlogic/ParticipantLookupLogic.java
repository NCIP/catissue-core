
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.language.Metaphone;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author santosh_chandak
 * 
 * This class is for finding out the matching participant with respect to given participant. 
 * It implements the lookUp method of LookupLogic interface which returns the list of all matching 
 * participants to the given participant. 
 * 
 */
public class ParticipantLookupLogic implements LookupLogic
{

	// Getting points from the xml file in static variables
	private static final int pointsForSSNExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_SSN_EXACT));
	private static final int pointsForSSNPartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_SSN_PARTIAL));
	private static final int pointsForDOBExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_DOB_EXACT));
	private static final int pointsForDOBPartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_DOB_PARTIAL));
	private static final int pointsForLastNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LAST_NAME_EXACT));
	private static final int pointsForLastNamePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LAST_NAME_PARTIAL));
	private static final int pointsForFirstNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_FIRST_NAME_EXACT));
	private static final int pointsForFirstNamePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_FIRST_NAME_PARTIAL));
	private static final int pointsForMiddleNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_MIDDLE_NAME_EXACT));
	private static final int pointsForMiddleNamePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_MIDDLE_NAME_PARTIAL));
	private static final int pointsForRaceExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_RACE_EXACT));
	private static final int pointsForRacePartial = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_RACE_PARTIAL));
	private static final int pointsForGenderExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_GENDER_EXACT));
	private static final int bonusPoints = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_BONUS));
	private static final int matchCharactersForLastName = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.PARTICIPANT_MATCH_CHARACTERS_FOR_LAST_NAME));
	private static final int cutoffPointsFromProperties = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF));
	private static final int totalPointsFromProperties = pointsForFirstNameExact + pointsForMiddleNameExact + pointsForLastNameExact
			+ pointsForDOBExact + pointsForSSNExact + pointsForGenderExact + pointsForRaceExact;
	private int cutoffPoints;
	private int totalPoints;

	/**
	 * This function first retrieves all the participants present in the PARTICIPANT table. Then it checks 
	 * for possible match of given participant and the list of participants retrieved from database. 
	 * Based on the criteria in the MPI matching algorithm, it returns the list of all matching participants. 
	 * @param params - LookupParameters
	 * @return list - List of matching Participants. 
	 */
	public List lookup(LookupParameters params) throws Exception
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

		// adjust cutoffPoints as per new total points 
		cutoffPoints = cutoffPointsFromProperties * totalPoints / totalPointsFromProperties;
		List listOfParticipants = participantParams.getListOfParticipants();

		// In case List of participants is null or empty, return the Matching Participant List as null.
		if (listOfParticipants == null || listOfParticipants.isEmpty() == true)
		{
			return null;
		}

		// calling the searchMatchingParticipant to filter the participant list according to given cutoff value
		List participants = searchMatchingParticipant(participant, listOfParticipants);

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
		if (participant.getMiddleName() != null && !participant.getMiddleName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForMiddleNameExact;
		}
		if (participant.getLastName() != null && !participant.getLastName().trim().equals(""))
		{
			totalPointsForParticipant += pointsForLastNameExact;
		}
		if (participant.getSocialSecurityNumber() != null && !participant.getSocialSecurityNumber().trim().equals(""))
		{
			totalPointsForParticipant += pointsForSSNExact;
		}
		if (participant.getGender() != null && !participant.getGender().trim().equals(""))
		{
			totalPointsForParticipant += pointsForGenderExact;
		}
		if (participant.getRaceCollection() != null && participant.getRaceCollection().isEmpty() == false)
		{
			totalPointsForParticipant += pointsForRaceExact;
		}

		return totalPointsForParticipant;
	}

	/**
	 * This function searches the participant which has matching probablity more than cutoff. The different criterias 
	 * considered for finding a possible match are
	 * 1. Social Security Number
	 * 2. Date Of Birth
	 * 3. Last Name 
	 * 4. Middle Name 
	 * 5. First Name 
	 * 6. Race
	 * 7. Gender
	 * 
	 * Points are given to complete or partial match of these parameters. If the total of all these points exceeds 
	 * the cut-off points, tha participant is considered as a match for the given participant. List of all such 
	 * participants is returned by this function.  
	 * 
	 * @param userParticipant - participant with which comparision is to be done.
	 * @param listOfParticipants - List of all participants which has atleast one matching parameter.
	 * @param cutoff - is the value such that the participants above the cutoff values are stored in List.
	 * @return list - List of matching Participants. 
	 */
	private List searchMatchingParticipant(Participant userParticipant, List listOfParticipants) throws Exception
	{
		List participants = new ArrayList();
		Iterator itr = listOfParticipants.iterator();

		// Iterates through all the Participants from the list 
		while (itr.hasNext())
		{
			int weight = 0; // used for calculation of total points.
			int socialSecurityNumberWeight = 0; // points of social security number
			int birthDateWeight = 0; // points of birth date
			Participant existingParticipant = (Participant) itr.next();

			// Check for the participant only in case its Activity Status = active
			if (existingParticipant.getActivityStatus() != null && existingParticipant.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
			{

				/**
				 *  If user has entered Social Security Number and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (userParticipant.getSocialSecurityNumber() != null && !userParticipant.getSocialSecurityNumber().trim().equals("")
						&& existingParticipant.getSocialSecurityNumber() != null && !existingParticipant.getSocialSecurityNumber().trim().equals(""))
				{
					socialSecurityNumberWeight = checkSocialSecurityNumber(userParticipant.getSocialSecurityNumber().trim().toLowerCase(),
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

				/**
				 *  Bonus points are given in case user entered LastName, FirstName, DOB match completely with
				 *  the respective fields of participant from database.
				 */
				if (weight - socialSecurityNumberWeight == pointsForLastNameExact + pointsForFirstNameExact + pointsForDOBExact)
				{
					weight += bonusPoints;
				}

				/**
				 * The first and last names are first parsed to determine if multiple names exist in either field separated by a space, dash or comma.  
				 * If so they are split and placed in the appropriate fields.  We also do a flip of the first and last names and try that because 
				 * sometimes people don’t enter them correctly (put last name in first name field and viceversa).
				 * This check is applied only when none of name or surname of user entered participant completely or partially matches with name and surname.
				 */

				if (weight == socialSecurityNumberWeight + birthDateWeight)
				{
					weight += checkFlipped(userParticipant.getFirstName(), userParticipant.getLastName(), existingParticipant.getFirstName(),
							existingParticipant.getLastName());
				}

				/**
				 *  check whether weight will ever reach cutoff, if it will never reach the cutoff, skip this 
				 *  participant and take next one.
				 */
				int temp = cutoffPoints - pointsForMiddleNameExact - pointsForRaceExact - pointsForGenderExact;

				if (weight < temp)
				{
					continue;
				}

				/**
				 *  check for possible match of middle name
				 */

				weight += checkMiddleName(userParticipant.getMiddleName(), existingParticipant.getMiddleName());

				/**
				 *  If user has Gender and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (userParticipant.getGender() != null && existingParticipant.getGender() != null)
				{
					weight += checkGender(userParticipant.getGender(), existingParticipant.getGender());
				}

				/**
				 *  If user has entered Race and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				weight += checkRace(userParticipant.getRaceCollection(), existingParticipant.getRaceCollection());

				// If total points are greater than cutoff points, add participant to the List
				if (weight >= cutoffPoints)
				{

					DefaultLookupResult result = new DefaultLookupResult();

					/**
					 *  Removed probability after discussion with Mark -- bug number 558
					 */

					// Finding the probablity.
					/*	if(totalPoints!=0)
					 {
					 
					 Double probablity = new Double((100 * weight) / totalPoints);

					 // Calculated points might be greater than total points because of BONUS points
					 if (probablity.intValue() > 100)
					 {
					 probablity = new Double(100);
					 } 
					 result.setProbablity(probablity);	
					 } */

					result.setObject(existingParticipant);
					participants.add(result);
					if (participants.size() == 100) // Return when matching participant list size becomes 100
					{
						return participants;
					}
				}
			}
		}

		return participants;
	}

	/**
	 * This function compares the two Social Security Numbers. 
	 * The criteria used for partial match is --> Mismatch of single digit with difference = 1 or any consecutive 
	 * pair of digits are transposed it is considered a partial match.  
	 * Only one occurrence of either of these is considered.
	 * 
	 * @param userSecurityNumber - Social Security Number of user
	 * @param existingSecurityNumber - Social Security Number of Participant from database
	 * @return int - points for complete, partial or no match
	 */

	private int checkSocialSecurityNumber(String userSecurityNumber, String existingSecurityNumber)
	{
		// complete match
		if (existingSecurityNumber.equals(userSecurityNumber))
		{
			return pointsForSSNExact;
		}
		else
		// partial match
		{

			int count = 0; // to count total number of digits mismatched
			int temp = -1; // temporary variable used for storing value of other variable
			boolean areConsecutiveDigitsTransposed = false; // to check whether consecutive digits are transposed
			boolean isDifferenceOne = false; // to check whether difference of two digits is one

			if (userSecurityNumber.length() == existingSecurityNumber.length())
			{
				for (int i = 0; i < userSecurityNumber.length(); i++)
				{
					if (userSecurityNumber.charAt(i) != existingSecurityNumber.charAt(i))
					{
						if (temp == -1)
						{
							if (isDifferenceOne == false && Math.abs(userSecurityNumber.charAt(i) - existingSecurityNumber.charAt(i)) == 1)
							{
								isDifferenceOne = true;
							}
							temp = i;

						}
						count++;
						if (count == 2 && i == temp + 1)
						{
							if (userSecurityNumber.charAt(i - 1) == existingSecurityNumber.charAt(i)
									&& userSecurityNumber.charAt(i) == existingSecurityNumber.charAt(i - 1))
							{
								areConsecutiveDigitsTransposed = true;
							}
							else
							{
								break;
							}
						}
					}
				}

			}

			/** 
			 * Mismatch of single digit with difference = 1 or any consecutive 
			 * pair of digits are transposed it is considered a partial match.  
			 * Only one occurrence of either of these is considered.
			 */
			if (count == 1 && isDifferenceOne == true || areConsecutiveDigitsTransposed == true && count == 2)
			{
				return pointsForSSNPartial;
			}

		}
		return 0;

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
		else if ((userBirthDate.getMonth() == existingBirthDate.getMonth() && userBirthDate.getYear() == existingBirthDate.getYear())
				|| (userBirthDate.getDate() == existingBirthDate.getDate() && userBirthDate.getMonth() == existingBirthDate.getMonth())
				&& (Math.abs(userBirthDate.getYear() - existingBirthDate.getYear()) <= 2)
				|| (userBirthDate.getDate() == existingBirthDate.getDate() && userBirthDate.getYear() == existingBirthDate.getYear()))
		{
			return pointsForDOBPartial;
		}
		return 0;
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
		else if (userLastName.regionMatches(true, 0, existingLastName, 0, matchCharactersForLastName)
				|| (new Metaphone()).isMetaphoneEqual(userLastName, existingLastName))
		{
			return pointsForLastNamePartial;
		}

		return 0;
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
		else if (userName.charAt(0) == existingName.charAt(0)) // partial match
		{
			return pointsForFirstNamePartial;
		}

		return 0;
	}

	/**
	 * This function compares the two Middle Names. 
	 * The criteria used for partial match is --> If the first character matches, or if one has it 
	 * and other does not it is considered a partial match. 
	 * 
	 * @param userMiddleName - Middle Name of user
	 * @param existingMiddleName - Middle Name of Participant from database
	 * @return int - points for complete, partial or no match
	 */
	private int checkMiddleName(String userName, String existingName)
	{
		boolean userNameBlank = false;
		boolean existingNameBlank = false;
		if (userName == null || userName.trim().equals(""))
		{
			userNameBlank = true;
		}
		if (existingName == null || existingName.trim().equals(""))
		{
			existingNameBlank = true;
		}

		/**
		 *  Removed this condition after discussion with Mark -- bug number 558
		 */
		// partial match in case one is present and other is not.
		/*		if (userNameBlank == true && existingNameBlank == false || userNameBlank == false && existingNameBlank == true)
		 {
		 return pointsForMiddleNamePartial;
		 }   */

		if (userNameBlank == false && existingNameBlank == false)
		{
			// complete match
			if (userName.trim().compareTo(existingName.trim()) == 0)
			{
				return pointsForMiddleNameExact;
			}
			else if (userName.trim().charAt(0) == existingName.trim().charAt(0)) // partial match
			{
				return pointsForMiddleNamePartial;
			}
		}

		return 0;
	}

	/**
	 * This function compares the two Races. 
	 * The criteria used for partial match is --> A partial is considered if one is missing and the other is there.
	 *  (eg, missing from the input data but in the database or viceversa).
	 * 
	 * @param userRace - Race of user
	 * @param existingRace - Race of Participant from database
	 * @return int - points for complete, partial or no match
	 */

	private int checkRace(Collection userRace, Collection existingRace)
	{

		// complete match
		if (userRace != null && userRace.isEmpty() == false && existingRace != null && existingRace.isEmpty() == false)
		{
			if (userRace.equals(existingRace))
			{
				return pointsForRaceExact;
			}
		}
		/**
		 *  Removed this condition after discussion with Mark -- bug number 558
		 */
		/*	// partial match
		 if ((userRace.isEmpty() == false && existingRace.isEmpty() == true || userRace.isEmpty() == true && existingRace.isEmpty() == false))
		 {
		 return pointsForRacePartial;
		 }*/

		return 0;
	}

	/**
	 * This function compares the two Genders. 
	 * 
	 * @param userGender - Gender of user
	 * @param existingGender - Gender of Participant from database
	 * @return int - points for complete or no match
	 */
	private int checkGender(String userGender, String existingGender)
	{
		if (userGender.equals(existingGender))
		{
			return pointsForGenderExact;
		}
		return 0;
	}

	/**
	 * This function checks whether first name and last name are flipped. 
	 * The criteria used for partial match is --> The first and last names are first parsed to determine 
	 * if multiple names exist in either field separated by a space, dash or comma.  If so they are split 
	 * and placed in the appropriate fields.  We also do a flip of the first and last names and try that 
	 * because sometimes people don’t enter them correctly (put last name in first name field and viceversa).
	 * 
	 * @param userFirstName - First Name of user
	 * @param userLastName - Last Name of user
	 * @param existingParticipantFirstName - First Name of Existing Participant
	 * @param existingParticipantLastName - Last Name of existing Participant
	 * @return int - points for complete, partial or no match
	 */

	private int checkFlipped(String userFirstName, String userLastName, String existingParticipantFirstName, String existingParticipantLastName)
	{
		boolean userFirstNameBlank = false; // tells whether first name of user is empty
		boolean userLastNameBlank = false; // tells whether first name of user is empty
		int pointsForFirstAndLastPartial = pointsForFirstNamePartial + pointsForLastNamePartial;

		if (userFirstName == null || userFirstName.trim().equals(""))
		{
			userFirstNameBlank = true;
		}
		if (userLastName == null || userLastName.trim().equals(""))
		{
			userLastNameBlank = true;
		}

		if (existingParticipantFirstName != null)
		{
			existingParticipantFirstName = existingParticipantFirstName.trim();
		}
		if (existingParticipantLastName != null)
		{
			existingParticipantLastName = existingParticipantLastName.trim();
		}

		if (userFirstNameBlank == true && userLastNameBlank == false || userFirstNameBlank == false && userLastNameBlank == true)
		{
			String temp = userFirstName;
			// temp points to the String which is not blank
			if (userFirstNameBlank == true)
			{
				temp = userLastName;
			}

			// Check if 2 names exist in either field separated by a space, dash or comma
			String split[] = temp.trim().split("[ -,]");

			/**
			 * If first name and last name entered by user in any of First Name/Last name matches with 
			 * First Name and Last name of existing participant or vice versa, points are given as sum of 
			 * points for partial match of first name and partial match of Last Name
			 */

			if (split != null && split.length == 2)
			{
				if ((split[0].trim().equalsIgnoreCase(existingParticipantLastName) && split[1].trim().equalsIgnoreCase(existingParticipantFirstName))
						|| (split[1].trim().equalsIgnoreCase(existingParticipantLastName) && split[0].trim().equalsIgnoreCase(
								existingParticipantFirstName)))
				{
					return pointsForFirstAndLastPartial;
				}

			}
		}

		// check whether first name and last name are flipped
		if (userFirstNameBlank == false && userFirstName.trim().equalsIgnoreCase(existingParticipantLastName) && userLastNameBlank == false
				&& userLastName.trim().equalsIgnoreCase(existingParticipantFirstName))
		{
			return pointsForFirstAndLastPartial;
		}

		// check if user's first name matches with last name of existing participant
		if (userFirstNameBlank == false && userFirstName.trim().equalsIgnoreCase(existingParticipantLastName))
		{
			return pointsForFirstNamePartial;
		}

		// check if user's last name matches with first name of existing participant
		if (userLastNameBlank == false && userLastName.trim().equalsIgnoreCase(existingParticipantFirstName))
		{
			return pointsForLastNamePartial;
		}
		return 0;
	}
}