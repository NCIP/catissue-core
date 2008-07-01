
package edu.wustl.catissuecore.bizlogic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantEmpi;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.dao.HibernateDAOImpl;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.lookup.DefaultLookupResult;

/**
 * @author santosh_chandak
 * 
 * This class is for finding out the matching participant with respect to given participant. 
 * It implements the lookUp method of LookupLogic interface which returns the list of all matching 
 * participants to the given participant. 
 * 
 */
public class ParticipantLookupLogicDF implements LookupLogic
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
			+ pointsForSSNExact + pointsForGenderExact + pointsForRaceExact;
	private int cutoffPoints;
	private int totalPoints;
	private boolean isSSNOrPMI = false;
	//Instvace variable getParticipantEmpiList will return this list whenever needed
	//but this can be done within the same call
	private List<ParticipantEmpi> participantsEmpi = null;
	//participant special update list
	private List<ParticipantEmpi> participantsEmpiSpecial = new ArrayList();

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
			//return null;
		}

		// calling the searchMatchingParticipant to filter the participant list according to given cutoff value
		List participants = searchMatchingParticipant(participant, listOfParticipants);
		
		//Added by satish for getting matching participant from web service
		List participantsDF = searchMatchingParticipant(participant);
		
		
		List<ParticipantEmpi> participantEmpiList = getParticipantEmpiList(); //list of empi from web services
		List<ParticipantEmpi> participantEmpiListData = getParticipantEmpiListData();//list of empi and participant from database
		
		//finding special update case for empi participant
		for (int j = 0; j < participantEmpiList.size(); j++)
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi)participantEmpiList.get(j);
			//System.out.print("List - "+participantEmpi.getEmpi_id());
		}
		
		for (int j = 0; j < participantEmpiListData.size(); j++)
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi)participantEmpiListData.get(j);
			//System.out.print("data - "+participantEmpi.getEmpi_id()+"--"+participantEmpi.getId());
		}
				
		for (int i = 0; i < participantEmpiListData.size(); i++) 
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi)participantEmpiListData.get(i);
			//System.out.print("came-"+participantEmpi.getEmpi_id()+"--"+participantEmpi.getId());
			for (int j = 0; j < participantEmpiList.size(); j++)
			{
				ParticipantEmpi participantEmpi2 = (ParticipantEmpi) participantEmpiList.get(j);
				//System.out.print("came  44-"+participantEmpi2.getEmpi_id()+"--"+participantEmpi2.getId());
				if(participantEmpi.getEmpi_id().equals(participantEmpi2.getEmpi_id()))
				{
					
					System.out.print("came"+participantEmpi2.getEmpi_id());
					participantEmpi2.setSpecialUpdate(true);
					participantEmpi2.setId(participantEmpi.getId());
					//setting participant identifer to participant camr from web services					
					
					((Participant)((DefaultLookupResult)participantsDF.get(j)).getObject()).setId(participantEmpi.getId());
					System.out.println("test - "+participantEmpi2.getId());
					System.out.println(((Participant)((DefaultLookupResult)participantsDF.get(j)).getObject()).getId());
					//deleting the speacial case update from database list 
					for (int k = 0; k < participants.size(); k++) 
					{
						DefaultLookupResult df = (DefaultLookupResult)participants.get(k);
						Participant participantDelete = (Participant)df.getObject();
						if(participantDelete.getId().equals(participantEmpi2.getId()))
						{
							System.out.print("came#"+participantDelete.getId());
							participants.remove(k);
							break;
						}
						
					}
				}
			}
		}
		
		for (int j = 0; j < participantEmpiList.size(); j++)
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi)participantEmpiList.get(j);
			//System.out.print("Special - "+participantEmpi.getEmpi_id()+"-"+participantEmpi.getId()+"-"+participantEmpi.isSpecialUpdate());
		}
		
		participantsEmpiSpecial = participantEmpiList;
		participantsDF.addAll(participants);
		
		participantsDF.addAll(participantsEmpiSpecial);
		
		return participantsDF;

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
		if (participant.getSocialSecurityNumber() != null
				&& !participant.getSocialSecurityNumber().trim().equals(""))
		{
			totalPointsForParticipant += pointsForSSNExact;
		}
		if (participant.getGender() != null && !participant.getGender().trim().equals(""))
		{
			totalPointsForParticipant += pointsForGenderExact;
		}
		if (participant.getRaceCollection() != null
				&& participant.getRaceCollection().isEmpty() == false)
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
	
	
	 // ---------------------------------------------------
	 
	
	private List searchMatchingParticipant(Participant userParticipant)throws Exception
	{
		

		
		
		
		//impl.openSession(getSessionData(request));
		//impl.insert(empi, getSessionData(request), true, true);
		//impl.commit();
		//impl.closeSession();
		
		//return participants;
		//List participants = 
		return parseDocument();
		//return parseDocument();
	}
	
	private Document getDocument()throws Exception
	{
		//get the factory
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse("D:\\test.xml");


		}catch(ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		}catch(SAXException se) 
		{
			se.printStackTrace();
		}catch(IOException ioe) 
		{
			ioe.printStackTrace();
		}
		return dom;
		
	}
	
	private List<ParticipantEmpi> getParticipantEmpiListSpecial()throws Exception
	{
		
		
		return participantsEmpiSpecial;
	}
	
	
	private List<ParticipantEmpi> getParticipantEmpiList()throws Exception
	{
		
		for (int j = 0; j < participantsEmpi.size(); j++)
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi)participantsEmpi.get(j);
			System.out.print("List -+ "+participantEmpi.getEmpi_id());
		}
		return participantsEmpi;
	}
	
	
	private List<ParticipantEmpi> getParticipantEmpiListData()throws Exception
	{
		//List<ParticipantEmpi> participantsDF = null;
		
		System.out.println("#####searchMatchingParticipant$$$$$$$$");
		
		HibernateDAOImpl impl = new HibernateDAOImpl();
		
		Session session = DBUtil.currentSession();
		Transaction tra = session.beginTransaction();
		List participantEmpiResult = session.createQuery("from ParticipantEmpi").list();
		tra.commit();
		//session.close();
		
		for (Iterator it = participantEmpiResult.iterator(); it.hasNext();) 
		{
			ParticipantEmpi participantEmpi = (ParticipantEmpi) it.next();
			System.out.println(participantEmpi.getId()+"----"+participantEmpi.getEmpi_id());
		}
		
		return participantEmpiResult;
	}
	
	private List parseDocument()throws Exception
	{
		System.out.println("CAlled--------------------------------------------------------------");
		List participants = new ArrayList();
		participantsEmpi = new ArrayList<ParticipantEmpi>();
		
		
		//get the root element
		Document dom = getDocument();
		Element docEle = dom.getDocumentElement();

		
		//get a nodelist of  elements
		
		NodeList nl = docEle.getElementsByTagName("Patient");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) 
			{

				DefaultLookupResult result = new DefaultLookupResult();
				Participant participant = new Participant();
				//taking participant EMPI
				ParticipantEmpi pEmpi = new ParticipantEmpi();
				
				Hashtable<String, String> hash = new Hashtable<String, String>();
				//get the Patient element
				Element el = (Element)nl.item(i);

				System.out.println();
				String UID = el.getAttribute("UID");
				
				System.out.println("----------------"+UID);
				pEmpi.setEmpi_id(Long.parseLong(UID));
				participantsEmpi.add(pEmpi);
				
				String Gender = el.getAttribute("Gender");
				String DOB = el.getAttribute("DOB");
				String SSN = el.getAttribute("SSN");
				String vitalStatus = el.getAttribute("Status");
				
				//commented to set participant ID null for participant comming from web services
				//participant.setId(Long.parseLong(UID));
				participant.setGender(Gender);
				
				Date birthDate = null;
				try {
					birthDate = Utility.parseDate(DOB,Utility.datePattern(DOB));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				participant.setBirthDate(birthDate);
				
				try{
					SSN = SSN.substring(0,3)+"-"+SSN.substring(3,5)+"-"+SSN.substring(5,9);
				}catch(Exception e)
				{
					SSN = null;
				}
				
				participant.setSocialSecurityNumber(SSN);
				participant.setVitalStatus("Alive");
				participant.setActivityStatus("Active");
				
				hash.put("Gender", Gender);
				hash.put("DOB", DOB);
				hash.put("SSN", SSN);
				hash.put("vitalStatus", vitalStatus);	
				hash.put("UID", UID);
						
				
				NodeList nl2 = el.getChildNodes();
				
				for(int j = 0; j <nl2.getLength(); j++)
				{
					Node node = nl2.item(j);
					String nodeName = node.getNodeName();
					//System.out.println(node.getNodeName());
					if(nodeName.equals("Name"))
					{
						
						NamedNodeMap namedNodeMap = node.getAttributes();
						String lastName = namedNodeMap.getNamedItem("Last").getNodeValue();
						String firstName = namedNodeMap.getNamedItem("First").getNodeValue();
						participant.setFirstName(firstName);
						participant.setLastName(lastName);
						hash.put("firstName", firstName);
						hash.put("lastName", lastName);
					
					}else if(nodeName.equals("MRNs"))
					{
						
						if(node.hasChildNodes())
						{
							Collection participantMedicalIdentifierCollection  = new LinkedHashSet();
							NodeList mrnList = node.getChildNodes();
							for(int k = 0 ; k < mrnList.getLength();k++ )
							{
								Node mrnNode = mrnList.item(k);
								NamedNodeMap namedNodeMap = mrnNode.getAttributes();
								//System.out.println(mrnNode.getNodeName());
								if(mrnNode.getNodeName().equals("MRN"))
								{
									ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
									Site site = new Site();
									String Site = namedNodeMap.getNamedItem("Site").getNodeValue();
									String Value = namedNodeMap.getNamedItem("Value").getNodeValue();
									participantMedicalIdentifier.setMedicalRecordNumber(Value);
									System.out.println("Site ID");
									site.setId(3l);
									site.setName(Site);
									participantMedicalIdentifier.setSite(site);
									//participantMedicalIdentifierCollection.add(participantMedicalIdentifier);
									hash.put("Site-"+k, Site);
									hash.put("Value-"+k, Value);
								}
								
								
							}
						}
					}else if(nodeName.equals("OtherPID"))
					{
						NamedNodeMap namedNodeMap = node.getAttributes();
						String race = namedNodeMap.getNamedItem("Race").getNodeValue();
						Collection raceCollection = new HashSet();
						raceCollection.add(race);
						//participant.setRaceCollection(raceCollection);
						hash.put("race", race);
					}
					
				}
				
				
				result.setObject(participant);
				participants.add(result);
				
				System.out.println("Printing participant Value");
				Enumeration enumer = hash.keys();
				
				while(enumer.hasMoreElements())
				{
					String key = (String)enumer.nextElement();
					String value = (String)hash.get(key);
						
					System.out.println(key +"-----"+ value);
				}
				
			}
		}
		System.out.println("Printing WWWWWWW"+participants.size());
		return participants;
		
	}
	
	//-------------------------------------------------------
	private List searchMatchingParticipant(Participant userParticipant, List listOfParticipants)
			throws Exception
	{
		List participants = new ArrayList();
		Iterator itr = listOfParticipants.iterator();
		
		
		/*
		 *attributes of userParticipant : we are doing this to improve performance
		 *This way, these methods are called only twice for the userParticipant, irrespective of the number of existingParticipants
		 */
		
		String SSNLowerCase = null;
		String lastNameLowerCase = null;
		String firstNameLowerCase = null;
		String middleNameLowerCase = null;
		String gender = null;
		Date birthDate = userParticipant.getBirthDate();
		Collection raceCollection = userParticipant.getRaceCollection();
		Collection PMICollection = userParticipant.getParticipantMedicalIdentifierCollection();

		if (userParticipant.getSocialSecurityNumber() != null)
		{
			SSNLowerCase = userParticipant.getSocialSecurityNumber().trim().toLowerCase();
		}

		if (userParticipant.getLastName() != null)
		{
			lastNameLowerCase = userParticipant.getLastName().trim().toLowerCase();
		}

		if (userParticipant.getFirstName() != null)
		{
			firstNameLowerCase = userParticipant.getFirstName().trim().toLowerCase();
		}

		if (userParticipant.getMiddleName() != null)
		{
			middleNameLowerCase = userParticipant.getMiddleName().trim().toLowerCase();
		}

		if (userParticipant.getGender() != null)
		{
			gender = userParticipant.getGender();
		}

		// Iterates through all the Participants from the list 
		while (itr.hasNext())
		{
			int weight = 0; // used for calculation of total points.
			int socialSecurityNumberWeight = 0; // points of social security number
			int birthDateWeight = 0; // points of birth date
			Participant existingParticipant = (Participant) itr.next();

			// Check for the participant only in case its Activity Status = active
			if (existingParticipant.getActivityStatus() != null
					&& existingParticipant.getActivityStatus().equals(
							Constants.ACTIVITY_STATUS_ACTIVE))
			{

				/**
				 *  If user has entered Social Security Number and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (SSNLowerCase != null && !SSNLowerCase.equals("")
						&& existingParticipant.getSocialSecurityNumber() != null
						&& !existingParticipant.getSocialSecurityNumber().trim().equals(""))
				{
					socialSecurityNumberWeight = checkNumber(SSNLowerCase, existingParticipant
							.getSocialSecurityNumber().trim().toLowerCase(), true);
					weight = socialSecurityNumberWeight;
				}

				/**
				 *  If user has entered Date of Birth and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (birthDate != null && existingParticipant.getBirthDate() != null)
				{
					birthDateWeight = checkDateOfBirth(birthDate, existingParticipant
							.getBirthDate());
					weight += birthDateWeight;
				}

				/**
				 *  If user has entered Last Name and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (lastNameLowerCase != null && !lastNameLowerCase.equals("")
						&& existingParticipant.getLastName() != null
						&& !existingParticipant.getLastName().trim().equals(""))
				{
					weight += checkLastName(lastNameLowerCase, existingParticipant.getLastName()
							.trim().toLowerCase());

				}

				/**
				 *  If user has entered First Name and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (firstNameLowerCase != null && !firstNameLowerCase.equals("")
						&& existingParticipant.getFirstName() != null
						&& !existingParticipant.getFirstName().trim().equals(""))
				{
					weight += checkFirstName(firstNameLowerCase, existingParticipant.getFirstName()
							.trim().toLowerCase());
				}

				/**
				 *  Bonus points are given in case user entered LastName, FirstName, DOB match completely with
				 *  the respective fields of participant from database.
				 */
				if (weight - socialSecurityNumberWeight == pointsForLastNameExact
						+ pointsForFirstNameExact + pointsForDOBExact)
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
					weight += checkFlipped(firstNameLowerCase, lastNameLowerCase,
							existingParticipant.getFirstName(), existingParticipant.getLastName());
				}

				weight += checkParticipantMedicalIdentifier(PMICollection, existingParticipant);

				/**
				 *  check whether weight will ever reach cutoff, if it will never reach the cutoff, skip this 
				 *  participant and take next one.
				 */
				int temp = cutoffPoints - pointsForMiddleNameExact - pointsForRaceExact
						- pointsForGenderExact;

				if (weight < temp)
				{
					continue;
				}

				/**
				 *  check for possible match of middle name
				 */

				weight += checkMiddleName(middleNameLowerCase, existingParticipant.getMiddleName());

				/**
				 *  If user has Gender and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				if (gender != null && existingParticipant.getGender() != null)
				{
					weight += checkGender(gender, existingParticipant.getGender());
				}

				/**
				 *  If user has entered Race and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				weight += checkRace(raceCollection, existingParticipant.getRaceCollection());

				/**
				 *  Name: Virender Mehta
				 *  Description :If user has entered Medical Recoded No and it is present in the participant from database as well,
				 *  check for match between the two.
				 */
				//	weight += checkParticipantMedicalIdentifier(userParticipant.getParticipantMedicalIdentifierCollection(), existingParticipant);
				// If total points are greater than cutoff points, add participant to the List
				
				
				if (isSSNOrPMI || weight >= cutoffPoints)
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
	 * @param - if this boolean variable is true then points for SSN will be taken else points of PMI
	 * @return int - points for complete, partial or no match
	 */

	private int checkNumber(String userNumber, String existingNumber, boolean ssnOrPMI)
	{
		isSSNOrPMI = false;
		// complete match
		if (existingNumber.equals(userNumber))
		{
			isSSNOrPMI = true;
			if (ssnOrPMI)
			{
				return pointsForSSNExact;
			}
			else
			{
				return pointsForPMIExact;
			}
		}
		else
		// partial match
		{

			int count = 0; // to count total number of digits mismatched
			int temp = -1; // temporary variable used for storing value of other variable
			boolean areConsecutiveDigitsTransposed = false; // to check whether consecutive digits are transposed
			boolean isDifferenceOne = false; // to check whether difference of two digits is one

			if (userNumber.length() == existingNumber.length())
			{
				for (int i = 0; i < userNumber.length(); i++)
				{
					if (userNumber.charAt(i) != existingNumber.charAt(i))
					{
						if (temp == -1)
						{
							if (isDifferenceOne == false
									&& Math.abs(userNumber.charAt(i) - existingNumber.charAt(i)) == 1)
							{
								isDifferenceOne = true;
							}
							temp = i;

						}
						count++;
						if (count == 2 && i == temp + 1)
						{
							if (userNumber.charAt(i - 1) == existingNumber.charAt(i)
									&& userNumber.charAt(i) == existingNumber.charAt(i - 1))
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
			if (count == 1 && isDifferenceOne == true || areConsecutiveDigitsTransposed == true
					&& count == 2)
			{
				if (ssnOrPMI)
				{
					return pointsForSSNPartial;
				}
				else
				{
					return pointsForPMIPartial;
				}
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
		else if ((userBirthDate.getMonth() == existingBirthDate.getMonth() && userBirthDate
				.getYear() == existingBirthDate.getYear())
				|| (userBirthDate.getDate() == existingBirthDate.getDate() && userBirthDate
						.getMonth() == existingBirthDate.getMonth())
				&& (Math.abs(userBirthDate.getYear() - existingBirthDate.getYear()) <= 2)
				|| (userBirthDate.getDate() == existingBirthDate.getDate() && userBirthDate
						.getYear() == existingBirthDate.getYear()))
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
		else if (userLastName.regionMatches(true, 0, existingLastName, 0,
				matchCharactersForLastName))
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
		if (userRace != null && userRace.isEmpty() == false && existingRace != null
				&& existingRace.isEmpty() == false)
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
	 * Name : Virender Mehta
	 * This function compares the two ParticipantMedicalIdentifier. 
	 * The criteria used for partial match is --> A partial is considered if one is missing and the other is there.
	 *  (eg, missing from the input data but in the database or vice versa).
	 * 
	 * @param userParticipantMedicalIdentifier - Race of user
	 * @param existingParticipantMedicalIdentifier - Race of Participant from database
	 * @return int - points for complete, partial or no match
	 */
	private int checkParticipantMedicalIdentifier(Collection userParticipantMedicalIdentifier,
			Participant existingParticipant)
	{
		List pmiList = new ArrayList();
		Collection existingParticipantMedicalIdentifier = existingParticipant
				.getParticipantMedicalIdentifierCollection();
		existingParticipant.setParticipantMedicalIdentifierCollection(null);
		int participantMedicalIdentifierWeight = 0;
		if (userParticipantMedicalIdentifier != null
				&& existingParticipantMedicalIdentifier != null)
		{
			Iterator existingParticipantMedicalIdentifierItr = existingParticipantMedicalIdentifier
					.iterator();
			while (existingParticipantMedicalIdentifierItr.hasNext())
			{
				String existingmedicalRecordNo = (String) existingParticipantMedicalIdentifierItr
						.next();
				String existingSiteId = (String) existingParticipantMedicalIdentifierItr.next();
				Iterator userParticipantMedicalIdentifierItr = userParticipantMedicalIdentifier
						.iterator();
				while (userParticipantMedicalIdentifierItr.hasNext())
				{
					ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) userParticipantMedicalIdentifierItr
							.next();
					String siteId = participantIdentifier.getSite().getId().toString();
					String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();
					if (existingSiteId != null && siteId.equals(existingSiteId))
					{
						participantMedicalIdentifierWeight = participantMedicalIdentifierWeight
								+ checkNumber(medicalRecordNo, existingmedicalRecordNo, false);
						pmiList.add(existingmedicalRecordNo);
						pmiList.add(existingSiteId);
						existingParticipant.setParticipantMedicalIdentifierCollection(pmiList);
					}
				}
			}
			return participantMedicalIdentifierWeight;
		}
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

	private int checkFlipped(String userFirstName, String userLastName,
			String existingParticipantFirstName, String existingParticipantLastName)
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

		if (userFirstNameBlank == true && userLastNameBlank == false || userFirstNameBlank == false
				&& userLastNameBlank == true)
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
				if ((split[0].trim().equalsIgnoreCase(existingParticipantLastName) && split[1]
						.trim().equalsIgnoreCase(existingParticipantFirstName))
						|| (split[1].trim().equalsIgnoreCase(existingParticipantLastName) && split[0]
								.trim().equalsIgnoreCase(existingParticipantFirstName)))
				{
					return pointsForFirstAndLastPartial;
				}

			}
		}

		// check whether first name and last name are flipped
		if (userFirstNameBlank == false
				&& userFirstName.trim().equalsIgnoreCase(existingParticipantLastName)
				&& userLastNameBlank == false
				&& userLastName.trim().equalsIgnoreCase(existingParticipantFirstName))
		{
			return pointsForFirstAndLastPartial;
		}

		// check if user's first name matches with last name of existing participant
		if (userFirstNameBlank == false
				&& userFirstName.trim().equalsIgnoreCase(existingParticipantLastName))
		{
			return pointsForFirstNamePartial;
		}

		// check if user's last name matches with first name of existing participant
		if (userLastNameBlank == false
				&& userLastName.trim().equalsIgnoreCase(existingParticipantFirstName))
		{
			return pointsForLastNamePartial;
		}
		return 0;
	}
}