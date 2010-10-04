
package client;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import pathology_scg.BaseHaematologyPathologyAnnotation;
import pathology_scg.BaseSolidTissuePathologyAnnotation;
import pathology_scg.BreastMargin;
import pathology_scg.ExtraprostaticExtension;
import pathology_scg.PancreasPathologyAnnotation;
import pathology_scg.RadicalProstatectomyPathologyAnnotation;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

/*
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import org.apache.log4j.PropertyConfigurator;
 */
/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2001-2004 SAIC. Copyrigh
 * t 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
 * and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
 * "This product includes software developed by the SAIC and the National Cancer Institute."
 * If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
 * wherever such third-party acknowledgments normally appear.
 * 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
 * 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
 * the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
 * 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 * SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author caBIO Team
 * @version 1.0
 */

/**
 * ClientDemo_SCG.java demonstartes various ways to execute searches with and without
 * using Application Service Layer (convenience layer that abstracts building criteria
 * Uncomment different scenarios below to demonstrate the various types of searches
 */

public class ClientDemo_SCG
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(ClientDemo_SCG.class);
	//TODO 2
	private final static String STATIC_ENTITY_CLASS_NAME = "edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry";
	//Make sure that there is only one class in the db by this name.
	private final static String DE_CLASS_NAME = "BaseHaematologyPathologyAnnotation";
	private static String jbossServerUrl = null;
	private static ApplicationService appServiceDEEntity = null;
	private static ApplicationService appServiceCatissue = null;
	private static String userName = null;
	private static String password = null;
	private static EntityManagerInterface entityManager = EntityManager.getInstance();

	public static void main(String[] args)
	{
		try
		{
			initialiseInstanceVariables(args);
			HttpsConnectionUtil.trustAllHttpsCertificates();
			System.out.println("*** ClientDemo_SCG...");
			initCaTissueService();
			ClientSession cs = ClientSession.getInstance();

			//TODO 3
			cs.startSession(userName, password);
			edu.wustl.catissuecore.domain.SpecimenCollectionGroup specimenCollectionGroup = searchSpecimenCollectionGroup();
			if (specimenCollectionGroup != null)
			{
				addAnnotationToStaticEntity(specimenCollectionGroup);
				System.out.println("Added annotation");
				//Query
				queryDEClass(specimenCollectionGroup.getId());

			}
			//QUERY
			queryRadicalProstatectomyPathologyAnnotation();
			queryBaseSolidTissuePathologyAnnotation();
			//Query 1
			queryPancreasPathologyAnnotation();
			cs.terminateSession();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return;
		}

	}

	private static void initialiseInstanceVariables(String[] args) throws ApplicationException
	{
		if (args.length < 3)
		{
			throw new ApplicationException("Please provide proper arguments");
		}
		jbossServerUrl = args[0];
		userName = args[1];
		password = args[2];
	}

	/**
	 * @return
	 */
	private static edu.wustl.catissuecore.domain.SpecimenCollectionGroup searchSpecimenCollectionGroup()
	{
		edu.wustl.catissuecore.domain.SpecimenCollectionGroup specimenCollectionGroup = getSpecimenCollectionGroupToSearch();
		try
		{
			System.out.println("Searching specimenCollectionGroup "
					+ specimenCollectionGroup.getId());
			List resultList = appServiceCatissue.search(
					edu.wustl.catissuecore.domain.SpecimenCollectionGroup.class,
					specimenCollectionGroup);
			System.out.println("Returned specimenCollectionGroup " + resultList);
			if (resultList != null)
			{
				Iterator resultsIterator = resultList.iterator();
				if (resultsIterator.hasNext())
				{
					edu.wustl.catissuecore.domain.SpecimenCollectionGroup returnedspecimenCollectionGroup = (edu.wustl.catissuecore.domain.SpecimenCollectionGroup) resultsIterator
							.next();
					System.out.println("Searched specimenCollectionGroup "
							+ specimenCollectionGroup);
					return returnedspecimenCollectionGroup;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private static void addAnnotationToStaticEntity(SpecimenCollectionGroup specimenCollectionGroup)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			//DE Entity Service
			initCaTissueService();
			System.out.println("Creating the new recordEntry.");
			SCGRecordEntry recEntry = getRecordEntry(specimenCollectionGroup);
			//TODO 5

			Object deObjectToBeCreated = getDEToBeCreated(recEntry.getId());
			initDEService();
			//		RadicalProstatectomyPathologyAnnotation deObjectToBeCreated = getRadicalProstatectomyPathologyAnnotation(specimenCollectionGroupId);
			//		Object deObjectToBeCreated = getBreastMargin();
			//TODO 6
			BaseHaematologyPathologyAnnotation createdDE = (BaseHaematologyPathologyAnnotation) appServiceDEEntity
					.createObject(deObjectToBeCreated);

			//		RadicalProstatectomyPathologyAnnotation createdDE = (RadicalProstatectomyPathologyAnnotation)appServiceDEEntity.createObject(deObjectToBeCreated);
			System.out.println("Newly created Annotation = " + createdDE);

		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @param createdDE
		 * @throws DynamicExtensionsApplicationException
		 * @throws DynamicExtensionsSystemException
		 * @throws ApplicationException
	 */
	private static SCGRecordEntry getRecordEntry(SpecimenCollectionGroup specimenCollectionGroup)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			ApplicationException
	{
		//initDEIntegrationService();
		Long particpantClassId = getEntityId(STATIC_ENTITY_CLASS_NAME);
		System.out.println("Entity Id for scgRecEntry " + particpantClassId);

		Long deContainerId = getContainerId(DE_CLASS_NAME);
		System.out.println("Container Id for Person " + deContainerId);

		SCGRecordEntry scgRecEntry = initRecordEntry(specimenCollectionGroup, getFormContext(
				particpantClassId, deContainerId));
		return insertRecordEntry(scgRecEntry);
	}

	/**
	 * @param scgRecEntry
	 * @param createdDE
	 * @throws ApplicationException
	 */
	private static SCGRecordEntry insertRecordEntry(SCGRecordEntry scgRecEntry)
			throws ApplicationException
	{
		System.out.println("Creating entity map record");
		Object createdEntityMapRecord = appServiceCatissue.createObject(scgRecEntry);
		System.out.println("Inserted entity Map Record");
		return (SCGRecordEntry) createdEntityMapRecord;

	}

	/**
	 * @param specimenCollectionGroup
	 * @param dynamicRecordId
	 * @return
	 * @throws ApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private static SCGRecordEntry initRecordEntry(SpecimenCollectionGroup specimenCollectionGroup,
			StudyFormContext formContext) throws DynamicExtensionsSystemException,
			ApplicationException
	{
		System.out.println("Initializing record entry");
		SCGRecordEntry recEntry = new SCGRecordEntry();
		getNextIdentifier(STATIC_ENTITY_CLASS_NAME);
		if (formContext != null)
		{
			recEntry.setActivityStatus("Active");
			//recEntry.setId(identifier);
			recEntry.setFormContext(formContext);
			recEntry.setSpecimenCollectionGroup(specimenCollectionGroup);
		}
		return recEntry;
	}

	/**
	 * @param participantId
	 * @return
		 * @throws ApplicationException
		 * @throws DynamicExtensionsSystemException
	 */
	private static Object getDEToBeCreated(Long scgRecEntryId) throws ApplicationException,
			DynamicExtensionsSystemException
	{
		pathology_scg.SCGRecordEntry deRecordEntry = new pathology_scg.SCGRecordEntry();
		deRecordEntry.setId(scgRecEntryId);
		initDEService();
		List resultsList = appServiceDEEntity.search(pathology_scg.SCGRecordEntry.class.getName(),
				deRecordEntry);
		if (resultsList.isEmpty())
		{
			throw new ApplicationException("Record entry not found.");
		}
		pathology_scg.SCGRecordEntry pathRecEntry = (pathology_scg.SCGRecordEntry) resultsList
				.iterator().next();
		//BreastMargin breastMargin = new BreastMargin();
		BaseHaematologyPathologyAnnotation baseHaematologyPathologyAnnotation = new BaseHaematologyPathologyAnnotation();
		System.out.println("Getting next id");
		Long smokingHistoryId = getNextIdentifier(DE_CLASS_NAME);
		//	breastMargin.setId(smokingHistoryId);
		//
		//	breastMargin.setMarginLocation("Location");
		//	breastMargin.setMarginStatus("Status");
		baseHaematologyPathologyAnnotation
				.setSCGRecordEntry_BaseHaematologyPathologyAnnotation(pathRecEntry);
		baseHaematologyPathologyAnnotation.setAdequacyOfSpecimen("Adequacy");
		baseHaematologyPathologyAnnotation.setBiopsyOrAspirateSite("Biopsy");
		baseHaematologyPathologyAnnotation.setComment("Comment");
		baseHaematologyPathologyAnnotation.setId(smokingHistoryId);
		baseHaematologyPathologyAnnotation.setOtherBiopsyOrAspirateSite("OtherBiopsy");
		baseHaematologyPathologyAnnotation.setOtherSpecimenProcedure("Other");
		baseHaematologyPathologyAnnotation.setSpecimenProcedure("Procedure");

		return baseHaematologyPathologyAnnotation;
	}

	/**
		 * @return
		 */
	//TODO 4
	private static void initCaTissueService()
	{
		appServiceCatissue = ApplicationServiceProvider.getRemoteInstance(jbossServerUrl
				+ "/catissuecore/http/remoteService");
		System.out.println("appServiceCatissue = " + appServiceCatissue);
	}

	private static void initDEService()
	{
		appServiceDEEntity = ApplicationServiceProvider.getRemoteInstance(jbossServerUrl
				+ "/pathologySCG/http/remoteService");
	}

	/**
	 * Query 1
	 */
	private static void queryPancreasPathologyAnnotation()
	{

		PancreasPathologyAnnotation pancreasPathologyAnnotation = new PancreasPathologyAnnotation();
		pancreasPathologyAnnotation.setSpecimenProcedure("Needle Incisional Biopsy");

		try
		{
			System.out.println("Searching pancreasPathologyAnnotation "
					+ pancreasPathologyAnnotation);
			initDEService();

			List resultList = appServiceCatissue.search(PancreasPathologyAnnotation.class,
					pancreasPathologyAnnotation);

			if (resultList != null)
			{
				Iterator resultsIterator = resultList.iterator();
				if (resultsIterator.hasNext())
				{
					PancreasPathologyAnnotation returnedpancreasPathologyAnnotation = (PancreasPathologyAnnotation) resultsIterator
							.next();
					System.out.println("Searched pancreasPathologyAnnotation "
							+ returnedpancreasPathologyAnnotation);

					initCaTissueService();

					edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg = new edu.wustl.catissuecore.domain.SpecimenCollectionGroup();
					SCGRecordEntry scgRecEntry = new SCGRecordEntry();
					scgRecEntry.setId(returnedpancreasPathologyAnnotation
							.getSCGRecordEntry_PancreasPathologyAnnotation().getId());
					scg.getScgRecordEntryCollection().add(scgRecEntry);
					List resultListSCG = appServiceCatissue.search(
							CollectionProtocolRegistration.class, scg);
					System.out.println("Returned CPR " + resultListSCG);
					if (resultListSCG != null)
					{
						Iterator iter = resultListSCG.iterator();
						while (iter.hasNext())
						{
							CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) iter
									.next();
							//due to lazy initialization error of consent tier response
							CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
							cpr.setId(collectionProtocolRegistration.getId());

							Participant p = new Participant();
							Collection<CollectionProtocolRegistration> cprColl = new HashSet<CollectionProtocolRegistration>();
							cprColl.add(cpr);

							p.setCollectionProtocolRegistrationCollection(cprColl);

							List patients = appServiceCatissue.search(Participant.class, p);
							if (patients != null)
							{
								Iterator patientsIter = patients.iterator();
								while (patientsIter.hasNext())
								{
									Participant patient = (Participant) patientsIter.next();
									Date deathDate = patient.getDeathDate();
									Date birthDate = patient.getBirthDate();
									if (deathDate != null && birthDate != null)
									{
										System.out.println("PATIENT AGE  = "
												+ (deathDate.getYear() - birthDate.getYear()));
									}
									else
									{
										System.out.println("Death Date = " + deathDate);
										System.out.println("Birth Date = " + birthDate);
									}

									//Cannot retrieve race collection due to bug in catissue APi
									//	System.out.println("PATIENT RACE = " + patient.getRaceCollection());
								}
							}

						}

					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	private static void queryBaseSolidTissuePathologyAnnotation()
	{

		BaseSolidTissuePathologyAnnotation baseSolidTissuePathologyAnnotation = new BaseSolidTissuePathologyAnnotation();
		baseSolidTissuePathologyAnnotation.setSpecimenProcedure("");

		try
		{
			System.out.println("Searching baseSolidTissuePathologyAnnotation "
					+ baseSolidTissuePathologyAnnotation);
			initDEService();

			List resultList = appServiceCatissue.search(BaseSolidTissuePathologyAnnotation.class,
					baseSolidTissuePathologyAnnotation);

			System.out.println("Returned baseSolidTissuePathologyAnnotation " + resultList);
			if (resultList != null)
			{
				Iterator resultsIterator = resultList.iterator();
				if (resultsIterator.hasNext())
				{
					BaseSolidTissuePathologyAnnotation returnedbaseSolidTissuePathologyAnnotation = (BaseSolidTissuePathologyAnnotation) resultsIterator
							.next();
					System.out.println("Searched returnedbaseSolidTissuePathologyAnnotation "
							+ returnedbaseSolidTissuePathologyAnnotation);
					System.out.println("returnedbaseSolidTissuePathologyAnnotation Id: "
							+ returnedbaseSolidTissuePathologyAnnotation.getId());
					System.out
							.println("----------------------------------------------------------------------");
					//System.out.println("/n returnedbaseSolidTissuePathologyAnnotation retrieved Participant : " + returnedbaseSolidTissuePathologyAnnotation.get);

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Query 3- Dosent work on caCORE 3.2 or Hibernate 3.0.5 and Hib 3.1.3 due to wrong Join query formed
	 */
	private static void queryRadicalProstatectomyPathologyAnnotation()
	{
		RadicalProstatectomyPathologyAnnotation radicalProstatectomyPathologyAnnotation = new RadicalProstatectomyPathologyAnnotation();
		radicalProstatectomyPathologyAnnotation.setSeminalVesicleInvasion("c");

		ExtraprostaticExtension extraprostaticExtension = new ExtraprostaticExtension();
		extraprostaticExtension.setStatus("ABC");
		//extraprostaticExtension.setId(new Long(1));
		//		extraprostaticExtension.setRadicalProstatectomyPathologyAnnotation(radicalProstatectomyPathologyAnnotation);

		radicalProstatectomyPathologyAnnotation.setExtraprostaticExtension(extraprostaticExtension);

		try
		{
			System.out.println("Searching radicalProstatectomyPathologyAnnotation "
					+ radicalProstatectomyPathologyAnnotation);
			initDEService();

			List resultList = appServiceCatissue.search(
					RadicalProstatectomyPathologyAnnotation.class,
					radicalProstatectomyPathologyAnnotation);

			System.out.println("Returned radicalProstatectomyPathologyAnnotation " + resultList);
			if (resultList != null)
			{
				Iterator resultsIterator = resultList.iterator();
				if (resultsIterator.hasNext())
				{
					RadicalProstatectomyPathologyAnnotation returnedradicalProstatectomyPathologyAnnotation = (RadicalProstatectomyPathologyAnnotation) resultsIterator
							.next();
					System.out.println("returned radicalProstatectomyPathologyAnnotation "
							+ returnedradicalProstatectomyPathologyAnnotation);
					System.out.println("radicalProstatectomyPathologyAnnotation Id: "
							+ returnedradicalProstatectomyPathologyAnnotation.getId());
					System.out
							.println("----------------------------------------------------------------------");
					System.out
							.println("/n radicalProstatectomyPathologyAnnotation retrieved SCG Record Entry: "
									+ returnedradicalProstatectomyPathologyAnnotation
											.getSCGRecordEntry_RadicalProstatectomyPathologyAnnotation()
											.getId());

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param specimenCollectionGroupId
	 */
	private static void queryDEClass(Long specimenCollectionGroupId)
	{
		try
		{
			System.out.println("Querying the DE class on the scgId.");
			Long containerId = getContainerId(DE_CLASS_NAME);
			SCGRecordEntry recEntry = new SCGRecordEntry();
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg.setId(specimenCollectionGroupId);
			recEntry.setSpecimenCollectionGroup(scg);
			StudyFormContext formContext = new StudyFormContext();
			formContext.setContainerId(containerId);
			recEntry.setFormContext(formContext);
			initCaTissueService();
			List scgRecEntryResultList = appServiceCatissue.search(SCGRecordEntry.class, recEntry);
			for (Object result : scgRecEntryResultList)
			{
				SCGRecordEntry returnedRecEntry = (SCGRecordEntry) result;
				pathology_scg.SCGRecordEntry deRecEntry = new pathology_scg.SCGRecordEntry();
				deRecEntry.setId(returnedRecEntry.getId());
				BaseHaematologyPathologyAnnotation baseHaematologyPathologyAnnotation = new BaseHaematologyPathologyAnnotation();
				baseHaematologyPathologyAnnotation
						.setSCGRecordEntry_BaseHaematologyPathologyAnnotation(deRecEntry);
				baseHaematologyPathologyAnnotation.setOtherSpecimenProcedure("Other");

				System.out.println("Searching baseHaematologyPathologyAnnotation "
						+ baseHaematologyPathologyAnnotation);
				initDEService();

				List resultList = appServiceCatissue.search(
						BaseHaematologyPathologyAnnotation.class,
						baseHaematologyPathologyAnnotation);

				System.out.println("Returned baseHaematologyPathologyAnnotation " + resultList);
				if (resultList != null)
				{
					Iterator resultsIterator = resultList.iterator();
					if (resultsIterator.hasNext())
					{
						BaseHaematologyPathologyAnnotation returnedbaseHaematologyPathologyAnnotation = (BaseHaematologyPathologyAnnotation) resultsIterator
								.next();
						System.out.println("returned baseHaematologyPathologyAnnotation "
								+ returnedbaseHaematologyPathologyAnnotation);
						System.out.println("returnedbaseHaematologyPathologyAnnotation Id: "
								+ returnedbaseHaematologyPathologyAnnotation.getId());
						System.out
								.println("----------------------------------------------------------------------");
						System.out
								.println("/n returnedbaseHaematologyPathologyAnnotation retrieved OtherSpecimenProcedure : "
										+ returnedbaseHaematologyPathologyAnnotation
												.getOtherSpecimenProcedure());

					}
				}

			}
			//baseHaematologyPathologyAnnotation.setSpecimenCollectionGroup(specimenCollectionGroup);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	private static RadicalProstatectomyPathologyAnnotation getRadicalProstatectomyPathologyAnnotation(
			Long sCGRecordId)
	{
		pathology_scg.SCGRecordEntry scg = new pathology_scg.SCGRecordEntry();
		scg.setId(sCGRecordId);

		RadicalProstatectomyPathologyAnnotation radicalProstatectomyPathologyAnnotation = new RadicalProstatectomyPathologyAnnotation();
		radicalProstatectomyPathologyAnnotation
				.setSeminalVesicleInvasion("No Seminal Vesicle present");
		radicalProstatectomyPathologyAnnotation
				.setSCGRecordEntry_RadicalProstatectomyPathologyAnnotation(scg);

		try
		{
			radicalProstatectomyPathologyAnnotation
					.setId(getNextIdentifier("RadicalProstatectomyPathologyAnnotation"));
		}
		catch (DynamicExtensionsSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ApplicationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExtraprostaticExtension extraprostaticExtension = new ExtraprostaticExtension();
		extraprostaticExtension.setStatus("Present");
		extraprostaticExtension.setId(new Long(3));
		extraprostaticExtension
				.setRadicalProstatectomyPathologyAnnotation(radicalProstatectomyPathologyAnnotation);

		radicalProstatectomyPathologyAnnotation.setExtraprostaticExtension(extraprostaticExtension);

		return radicalProstatectomyPathologyAnnotation;
	}

	/**
	 * @return
	 */
	private static Object getBreastMargin()
	{
		BreastMargin breastMargin = new BreastMargin();

		System.out.println("Getting next id");
		Long smokingHistoryId = null;
		try
		{
			smokingHistoryId = getNextIdentifier(DE_CLASS_NAME);
		}
		catch (DynamicExtensionsSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ApplicationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		breastMargin.setId(smokingHistoryId);
		//
		breastMargin.setMarginLocation("Location");
		breastMargin.setMarginStatus("Status");

		return breastMargin;
	}

	/**
	 * @throws ApplicationException
	 * @throws DynamicExtensionsSystemException
	 *
	 */
	private static Long getNextIdentifier(String deEntity) throws ApplicationException,
			DynamicExtensionsSystemException
	{
		/*DetachedCriteria maxDEIdentifierCriteria = DetachedCriteria.forClass(deClass).setProjection( Property.forName("id").count());
		List identifierList = appServiceDEEntity.query(maxDEIdentifierCriteria, deClass.getName());

		if(identifierList!=null)
		{
			Iterator identifierIterator = identifierList.iterator();
			if(identifierIterator.hasNext())
			{
				Integer maxIdentifier = (Integer)identifierIterator.next();
				System.out.println("I = " + maxIdentifier);
				Long l =new Long(maxIdentifier.intValue() + 1);
				return (l);

			}
		}
		return null;*/
		return entityManager.getNextIdentifierForEntity(deEntity);
	}

	/**
	 *
	 */
	private static StudyFormContext getFormContext(Long staticEntityClassId, Long DEContainerId)
	{
		System.out.println("Searching Form Context : staticEntityClassId= " + staticEntityClassId
				+ " DEContainerId=" + DEContainerId);

		StudyFormContext formContext = new StudyFormContext();
		formContext.setContainerId(DEContainerId);
		try
		{
			List resultList = appServiceCatissue.search(StudyFormContext.class.getName(),
					formContext);

			Iterator resultsIterator = resultList.iterator();
			if (resultsIterator.hasNext())
			{
				StudyFormContext returnedFormContext = (StudyFormContext) resultsIterator.next();
				System.out.println(returnedFormContext);
				if (returnedFormContext != null)
				{
					return returnedFormContext;

				}
			}
		}
		catch (Exception e)
		{
			//Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @param string
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Long getContainerId(String string) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Long entityId = getEntityId(string);
		return entityManager.getContainerIdForEntity(entityId);

	}

	/**
	 * @param string
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private static Long getEntityId(String entityName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		return entityManager.getEntityId(entityName);
		/*System.out.println("Here " + entityManager);

		if(entityManager!=null)
		{
			System.out.println("entity  name "  +entityName);
			EntityInterface entity = entityManager.getEntityByName(entityName);
			if(entity!=null)
			{
				System.out.println("entity "  + entity.getId());
				return entity.getId();
			}
		}
		return null;*/
	}

	/**
	 * @return
	 */
	private static edu.wustl.catissuecore.domain.SpecimenCollectionGroup getSpecimenCollectionGroupToSearch()
	{
		edu.wustl.catissuecore.domain.SpecimenCollectionGroup specimenCollectionGroup = new edu.wustl.catissuecore.domain.SpecimenCollectionGroup();;
		//Set parameters for participant to be searched
		specimenCollectionGroup.setId(2L); //Set ID.

		return specimenCollectionGroup;
	}

}
