import deintegration.EntityMap;
import deintegration.EntityMapRecord;
import deintegration.FormContext;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.Iterator;
import java.util.List;

import pathology_specimen.MelanomaSpecimenPathologyAnnotation;
import pathology_specimen.Specimen;



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
 * ClientDemo_CA.java demonstartes various ways to execute searches with and without
 * using Application Service Layer (convenience layer that abstracts building criteria
 * Uncomment different scenarios below to demonstrate the various types of searches
 */

public class ClientDemo_Specimen
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger =Logger.getCommonLogger(ClientDemo_Specimen.class);
	//TODO 2
	private final static String STATIC_ENTITY_CLASS_NAME = "edu.wustl.catissuecore.domain.Specimen";
	//Make sure that there is only one class in the db by this name.
	private final static String DE_CLASS_NAME = "MelanomaSpecimenPathologyAnnotation";
	static ApplicationService appServiceDeIntegration = null;
	static ApplicationService appServiceDEEntity = null;
	static ApplicationService appServiceCatissue = null;
	
	private static EntityManagerInterface entityManager = EntityManager.getInstance();
	
	public static void main(String[] args)
	{
		System.out.println("*** ClientDemo_Specimen...");

		ClientSession cs = ClientSession.getInstance();
		try
		{
			//caTissue Service
			initCaTissueService();
			//TODO 3
			cs.startSession("admin@admin.com", "Login123");
			edu.wustl.catissuecore.domain.Specimen specimen = searchSpecimen();
			if(specimen!=null)
			{
				addAnnotationToStaticEntity(specimen.getId());
				System.out.println("Added annotation");	
				//Query
				queryDEClass(specimen.getId());
			}
			
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return;
		}
		cs.terminateSession();
	}

/**
	 * @return
	 */
	//TODO 4
	private static void initCaTissueService()
	{
		appServiceCatissue = ApplicationServiceProvider.getRemoteInstance("http://localhost:8080/catissuecore/http/remoteService");
		System.out.println("appServiceCatissue = "  + appServiceCatissue);
	}
	private static void initDEService()
	{
		appServiceDEEntity = ApplicationServiceProvider.getRemoteInstance("http://localhost:8080/pathologySpecimen/http/remoteService");
	}
	/**
	 * 
	 */
	private static void initDEIntegrationService()
	{
		appServiceDeIntegration = ApplicationServiceProvider.getRemoteInstance("http://localhost:8080/deintegration/http/remoteService");
	}



	/**
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private static void addAnnotationToStaticEntity(Long specimenId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			//DE Entity Service
			initDEService();
			//TODO 5
			Object deObjectToBeCreated = getDEToBeCreated(specimenId);
			//TODO 6
			MelanomaSpecimenPathologyAnnotation createdDE = (MelanomaSpecimenPathologyAnnotation)appServiceDEEntity.createObject(deObjectToBeCreated);
			System.out.println("Newly created Annotation = "  +  createdDE);
			
			System.out.println("Updating Integration Tables");
			updateIntegrationTables(specimenId,createdDE.getId());
		}
		catch (ApplicationException e)
		{			
			e.printStackTrace();
		}
	}
	/**
	 * @param specimenCollectionGroupId
	 */
	private static void queryDEClass(Long specimenId)
	{
		Specimen specimen = new Specimen();
		specimen.setId(specimenId);

		MelanomaSpecimenPathologyAnnotation melanomaSpecimenPathologyAnnotation  = new MelanomaSpecimenPathologyAnnotation();
		melanomaSpecimenPathologyAnnotation.setSpecimen(specimen);
		melanomaSpecimenPathologyAnnotation.setDepthOfInvasion(new Double(2));
		melanomaSpecimenPathologyAnnotation.setComments("Comment");
		
		try {
			System.out.println("Searching MelanomaSpecimenPathologyAnnotation "   + melanomaSpecimenPathologyAnnotation);
			initDEService();

			List resultList = appServiceCatissue.search(MelanomaSpecimenPathologyAnnotation.class,melanomaSpecimenPathologyAnnotation);

			System.out.println("Returned melanomaSpecimenPathologyAnnotation " + resultList);
			if(resultList!=null)
			{
				Iterator resultsIterator = resultList.iterator();
				if(resultsIterator.hasNext())
				{
					MelanomaSpecimenPathologyAnnotation returnedmelanomaSpecimenPathologyAnnotation = (MelanomaSpecimenPathologyAnnotation) resultsIterator.next();
					System.out.println("returned melanomaSpecimenPathologyAnnotation " + returnedmelanomaSpecimenPathologyAnnotation);
					System.out.println("melanomaSpecimenPathologyAnnotation Id: " + returnedmelanomaSpecimenPathologyAnnotation.getId());
					System.out.println("----------------------------------------------------------------------");
					System.out.println("/n returnedmelanomaSpecimenPathologyAnnotation retrieved DepthOfInvasion : " + returnedmelanomaSpecimenPathologyAnnotation.getDepthOfInvasion());

				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
 * @param createdDE
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * @throws ApplicationException 
	 */
	private static void updateIntegrationTables(Long staticEntityId,Long deEntityId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, ApplicationException
	{
		initDEIntegrationService();
		Long particpantClassId = getEntityId(STATIC_ENTITY_CLASS_NAME);
		System.out.println("Entity Id for specimenCollectionGroup " + particpantClassId);
	
		Long smokingHistoryContainerId =getContainerId(DE_CLASS_NAME);
		System.out.println("Container Id for Person " + smokingHistoryContainerId);
		
		EntityMapRecord entityMapRecord = initEntityMapRecord(staticEntityId,deEntityId, getFormContext(particpantClassId, smokingHistoryContainerId));
		insertEntityMapRecord(entityMapRecord);
	}

	

	/**
	 * @param participantId
	 * @return
		 * @throws ApplicationException 
		 * @throws DynamicExtensionsSystemException 
	 */
	private static Object getDEToBeCreated(Long specimenId) throws ApplicationException, DynamicExtensionsSystemException
	{
		Specimen specimen = new Specimen();
		specimen.setId(specimenId);
	
		MelanomaSpecimenPathologyAnnotation melanomaSpecimenPathologyAnnotation = new MelanomaSpecimenPathologyAnnotation();
		System.out.println("Getting next id");
		Long smokingHistoryId = getNextIdentifier(DE_CLASS_NAME);
		
		melanomaSpecimenPathologyAnnotation.setSpecimen(specimen);
	
		melanomaSpecimenPathologyAnnotation.setId(smokingHistoryId);
		melanomaSpecimenPathologyAnnotation.setComments("Comments");
		melanomaSpecimenPathologyAnnotation.setDepthOfInvasion(new Double(2));
		melanomaSpecimenPathologyAnnotation.setDepthOfInvasionCannotBeDetermined(new Boolean(true));
		melanomaSpecimenPathologyAnnotation.setMitoticIndex("MitoticIndex");
		melanomaSpecimenPathologyAnnotation.setUlceration("Ulceration");
		melanomaSpecimenPathologyAnnotation.setTumorRegression("TumorRegression");
		melanomaSpecimenPathologyAnnotation.setTumorInfiltratingLymphocytes("TumorInfiltratingLymphocytes");
			
		return melanomaSpecimenPathologyAnnotation;
	}

	/**
	 * @throws ApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	private static Long getNextIdentifier(String deEntity) throws ApplicationException, DynamicExtensionsSystemException
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
	 * @param entityMapRecord
	 * @throws ApplicationException
	 */
	private static EntityMapRecord insertEntityMapRecord(EntityMapRecord entityMapRecord) throws ApplicationException
	{
		System.out.println("Creating entity map record");
		Object createdEntityMapRecord = appServiceDeIntegration.createObject(entityMapRecord);
		System.out.println("Inserted entity Map Record");
		return (EntityMapRecord)createdEntityMapRecord;

	}


	/**
	 * @param staticRecordId
	 * @param dynamicRecordId
	 * @return
	 */
	private static EntityMapRecord initEntityMapRecord(Long staticRecordId, Long dynamicRecordId,FormContext formContext)
	{

		System.out.println("Initializing entitymap record");
		EntityMapRecord entityMapRecord = new EntityMapRecord();
		entityMapRecord.setStaticEntityRecordId(staticRecordId);
		entityMapRecord.setDynamicEntityRecordId(dynamicRecordId);
		entityMapRecord.setFormContext(formContext);
		if(formContext!=null)
		{
			entityMapRecord.setFormContextId(formContext.getId());
		}
		return entityMapRecord;
	}


	/**
	 *
	 */
	private static FormContext getFormContext(Long staticEntityClassId,Long DEContainerId)
	{
		EntityMap entityMap = new EntityMap();
		entityMap.setStaticEntityId(staticEntityClassId);
		entityMap.setContainerId(DEContainerId);
		System.out.println("Searching Entity Map : staticEntityClassId= "  + staticEntityClassId + " DEContainerId="+DEContainerId);
		try {
			List resultList = appServiceDeIntegration.search(EntityMap.class,entityMap);

			Iterator resultsIterator = resultList.iterator();
			if (resultsIterator.hasNext())
			{
				EntityMap returnedEntityMap = (EntityMap) resultsIterator.next();
				System.out.println(returnedEntityMap);
				if(returnedEntityMap!=null)
				{
					Iterator iter = returnedEntityMap.getFormContextCollection().iterator();
					if(iter.hasNext())
					{
						FormContext formContext = (FormContext)iter.next();
						return formContext;
					}
				}
			}
		}
		catch (Exception e) {
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
	private static Long getContainerId(String string) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
	private static Long getEntityId(String entityName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
	private static edu.wustl.catissuecore.domain.Specimen searchSpecimen()
	{
		edu.wustl.catissuecore.domain.Specimen specimen = getSpecimenToSearch();
		try {
			System.out.println("Searching specimen "   + specimen.getId());
			List resultList = appServiceCatissue.search(edu.wustl.catissuecore.domain.Specimen.class,specimen);
			System.out.println("Returned specimen " + resultList);
			if(resultList!=null)
			{
				Iterator resultsIterator = resultList.iterator();
				if(resultsIterator.hasNext())
				{
					edu.wustl.catissuecore.domain.Specimen returnedspecimen = (edu.wustl.catissuecore.domain.Specimen) resultsIterator.next();
					System.out.println("Searched specimen " + returnedspecimen);
					return returnedspecimen;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @return
	 */
	private static edu.wustl.catissuecore.domain.Specimen getSpecimenToSearch()
	{
		edu.wustl.catissuecore.domain.Specimen specimen = new edu.wustl.catissuecore.domain.Specimen();;
		//Set parameters for participant to be searched 
		specimen.setId(2L);	//Set ID. 
		
		return specimen;
	}

}

