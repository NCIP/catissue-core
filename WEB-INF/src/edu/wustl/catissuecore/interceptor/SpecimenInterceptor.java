package edu.wustl.catissuecore.interceptor;

import edu.wustl.dao.exception.InterceptProcessorException;
import edu.wustl.dao.interceptor.InterceptProcessor;
import edu.wustl.dao.interceptor.SaveUpdateInterceptThread.eventType;

/**
 * This class is responsible for processing any activites that needs to be done whenever a specimen object is
 * saved or updated in the databse using hibernate.
 * @author Pavan
 *
 */
public class SpecimenInterceptor implements InterceptProcessor
{

	@Override
	public void onError(String arg0, eventType arg1, Long arg2) throws InterceptProcessorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Object arg0, eventType arg1) throws InterceptProcessorException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Logger
	 *//*
	private static Logger LOGGER = Logger.getCommonLogger(SpecimenInterceptor.class);

	*//**
	 * User temp dir location.
	 *//*
	private static final String TEMP_DIR_LOCATION=System.getProperty("java.io.tmpdir");

	*//**
	 * temporary file name prefix.
	 *//*
	private static final String FILE_NAME_PREFIX="specimen_";


	*//**
	 * Package name for generated JAXB domain Objects.
	 *//*
	private static final String JAXB_PACKAGE_NAME="edu.wustl.cider.jaxb.domain";

	*//**
	 * This method does the processing for given obj which is inserted or updates using hibernate session.
	 * @param obj object to be processed.
	 * @param type event type occured.
	 * @throws InterceptProcessorException exception.
	 *//*
	public void process(Object obj, eventType type) throws InterceptProcessorException
	{
		// TODO Auto-generated method stub
		Specimen specimen = (Specimen)obj;
		if(Constants.COLLECTION_STATUS_COLLECTED.equalsIgnoreCase(specimen.getCollectionStatus()))
		{
			Long objId = specimen.getId();
			try
			{

				ObjectFactory factory = new ObjectFactory();
				ParticipantType xmlParticipant = factory.createParticipantType();
				updateJaxbDomainObject(specimen, xmlParticipant);
				if(xmlParticipant.getEmpi()==null||"".equals(xmlParticipant.getEmpi().trim()))
				{
					return;
				}
				final SpecimenMessage specimenMessage = factory.createSpecimenMessage();
				Calendar calendar = Calendar.getInstance();

				specimenMessage.setMessageTimeStamp(calendar);
				specimenMessage.setParticipant(xmlParticipant);

				String fileName = TEMP_DIR_LOCATION+File.separator+FILE_NAME_PREFIX+specimen.getId()+Constants.XML_SUFFIX;
				marshall(specimenMessage, fileName);
				if(writeMessage(fileName))
				{
					updateSpecimenCiderMessageLog(specimen,type,calendar.getTime());
					File message = new File(fileName);
					message.delete();
				}

			}catch (JAXBException e) {
				throw new InterceptProcessorException("001",objId,e,ApplicationProperties.getValue("error.interceptor.specimen.message",objId.toString()));

			}
			catch (JMSException e)
			{
					throw new InterceptProcessorException("002",objId,e,ApplicationProperties.getValue("error.interceptor.specimen.wmq",objId.toString()));
			}
			catch(Exception e)
			{
				throw new InterceptProcessorException("000",objId,e,ApplicationProperties.getValue("error.interceptor.specimen.message",objId.toString()));
			}
			LOGGER.info("Processing successfull for object id = "+objId);
		}

	}

	public static void main(String[] args) throws FileNotFoundException, JAXBException, BulkOperationException
	{
		SpecimenInterceptor interceptor = new SpecimenInterceptor();
		ObjectFactory factory = new ObjectFactory();

		ParticipantType xmlParticipant = factory.createParticipantType();

		xmlParticipant.setEmpi("007");
		final SpecimenMessage specimenMessage = factory.createSpecimenMessage();
		Calendar calendar = Calendar.getInstance();

		specimenMessage.setMessageTimeStamp(calendar);
		specimenMessage.setParticipant(xmlParticipant);


		String fileName = TEMP_DIR_LOCATION+FILE_NAME_PREFIX+"1"+Constants.XML_SUFFIX;

		interceptor.marshall(specimenMessage, fileName);
	}

	public void marshall(Object participantObj,String fileName) throws JAXBException
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        // create an Unmarshaller
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(participantObj, new File(fileName));

	}

	private void updateSpecimenCiderMessageLog(Specimen specimen, eventType type,Date timestamp) throws BizLogicException
	{
		// TODO Auto-generated method stub
		SpecimenCiderMessage ciderMessage = new SpecimenCiderMessage();
		ciderMessage.setSpecimenIdentifier(specimen.getId());
		ciderMessage.setSentDate(new Date());
		ciderMessage.setEventType(type.getTypeString());
		CatissueDefaultBizLogic bizlogic = new CatissueDefaultBizLogic();
		bizlogic.insert(ciderMessage);

	}

	*//**
	 * This method will write down the contents of the given fileName
	 * @param fileName name of the file whose contents should be sent on queue.
	 * @throws JMSException exception in queue handling.
	 *//*
	private boolean writeMessage(String fileName) throws JMSException
	{
		return SpecimenWmqProcessor.getInstance().writeMessageToQueue(fileName);


	}

	*//**
	 * This method will set the properties for given jaxb domain object from the actual specimen object.
	 * @param specimen specimen from which toread the properties
	 * @param xmlParticiapant object which should be updated
	 * @throws DatatypeConfigurationException exception
	 * @throws ApplicationException
	 *//*
	private void updateJaxbDomainObject(Specimen specimen,ParticipantType xmlParticiapant)
			throws  ApplicationException
	{
		SpecimenCollectionGroup scg = getScgFromId(specimen.getSpecimenCollectionGroup());
		CollectionProtocolRegistrationType xmlCPR = new CollectionProtocolRegistrationTypeImpl();
		CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
		boolean isCpEmpiEnabled = updateObjectForCollectionProtocol(xmlCPR, cpr);
		SpecimenCollectionGroupType xmlScg = updateObjectForScg(xmlCPR,scg);
		updateObjectForSpecimen(xmlScg,specimen);
		if(isCpEmpiEnabled)
		 {
			xmlParticiapant.setEmpi(getParticiapantEmpiId(cpr));//cpr.getParticipant().getEMpi();
		}

		xmlParticiapant.setCollectionProtocolRegistration(xmlCPR);

	}

	private void updateObjectForSpecimen(SpecimenCollectionGroupType xmlScg, Specimen specimen) throws BizLogicException, ApplicationException
	{
		SpecimenCollectionType specimenCollection = new SpecimenCollectionTypeImpl();
		xmlScg.setSpecimenCollection(specimenCollection);

		SpecimenType xmlSpecimen = new SpecimenTypeImpl();
		specimenCollection.getSpecimen().add(xmlSpecimen);

		xmlSpecimen.setClassName(specimen.getClassName());
		xmlSpecimen.setId(specimen.getId());
		xmlSpecimen.setType(specimen.getSpecimenType());
		xmlSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());
		xmlSpecimen.setQuantity(specimen.getAvailableQuantity());
		xmlSpecimen.setIsAvailable(specimen.getIsAvailable());
		xmlSpecimen.setActivityStatus(specimen.getActivityStatus());
		// add specimen characterastics
		SpecimenCharacteristicsType xmlCharacterastics = new SpecimenCharacteristicsTypeImpl();
		xmlCharacterastics.setTissueSite(specimen.getTissueSite());
		xmlSpecimen.setSpecimenCharacteristics(xmlCharacterastics);

		updateXmlObjectForPosition(specimen, xmlSpecimen);
		updateXmlObjectForCollectionDate(specimen,xmlSpecimen);

	}

	private SpecimenCollectionGroupType updateObjectForScg(
			CollectionProtocolRegistrationType xmlCPR,
			SpecimenCollectionGroup scg)
	{
		SCGCollectionType xmlScgCollection = new SCGCollectionTypeImpl();
		xmlCPR.setSCGCollection(xmlScgCollection);
		SpecimenCollectionGroupType xmlScg = new SpecimenCollectionGroupTypeImpl();
		xmlScgCollection.getSpecimenCollectionGroup().add(xmlScg);
		//need to fetch the scg if clinical diagnosis
		xmlScg.setAccessionNumber(scg.getSurgicalPathologyNumber());
		xmlScg.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		xmlScg.setClinicalStatus(scg.getClinicalStatus());
		return xmlScg;
	}

	private SpecimenCollectionGroup getScgFromId(SpecimenCollectionGroup specimenCollectionGroup) throws ApplicationException
	{
		SpecimenCollectionGroupBizLogic bizlogic = new SpecimenCollectionGroupBizLogic();
		DAO dao=null;
		SpecimenCollectionGroup scg ;
		if(specimenCollectionGroup.getClinicalDiagnosis()==null || specimenCollectionGroup.getClinicalStatus()==null)
		{
			try
			{
				dao = AppUtility.openDAOSession(null);
				scg =bizlogic.getSCGFromId(specimenCollectionGroup.getId(), null, true, dao);
			}
			finally
			{
				AppUtility.closeDAOSession(dao);
			}
		}
		else
		{
			scg = specimenCollectionGroup;
		}
		return scg;
	}

	private String getParticiapantEmpiId(
			edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr) throws BizLogicException
	{
		String empiId =null;
		if(cpr.getParticipant()==null)
		{
			ParticipantBizLogic bizLogic = new ParticipantBizLogic();
			bizLogic.executeQuery("select participant.id from "+edu.wustl.catissuecore.domain.Participant.class.getName() + " as participant where participant.collectionProtocolRegistrationCollection.id = "+cpr.getId());
			//bizLogic.getParticipantEmpiId();
		}
		else
		{
			empiId = cpr.getParticipant().getEmpiId();
		}
		return empiId;
	}

	private boolean updateObjectForCollectionProtocol(CollectionProtocolRegistrationType xmlCPR,
			CollectionProtocolRegistration cpr) throws BizLogicException
	{
		boolean isUpdated = false;
		CollectionProtocolType xmlCp = new CollectionProtocolTypeImpl();
		CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
		if(collectionProtocol.getPrincipalInvestigator()==null)
		{
			CollectionProtocolBizLogic bizlogic = new CollectionProtocolBizLogic();
			collectionProtocol = bizlogic.retrieveB(CollectionProtocol.class.getName(),collectionProtocol.getId());
		}
		xmlCp.setTitle(collectionProtocol.getTitle());
		//if participant is having empi but registered in CP whose empi is not enabled then data should not be sent to cider
		if(collectionProtocol.getIsEMPIEnabled())
		{
			isUpdated = true;
		}
		// create PI
		User principalInvestigator = collectionProtocol.getPrincipalInvestigator();
		UserType xmlPi = new UserTypeImpl();
		xmlPi.setFirstName(principalInvestigator.getFirstName());
		xmlPi.setLastName(principalInvestigator.getLastName());
		xmlPi.setEmail(principalInvestigator.getEmailAddress());

		xmlCp.setPrincipalInvestigator(xmlPi);
		xmlCPR.setCollectionProtocol(xmlCp);
		return isUpdated;

	}

	private void updateXmlObjectForCollectionDate(Specimen specimen,
			SpecimenType xmlSpecimen)
	{
		if(specimen.getSpecimenEventCollection()!=null)
		{
			for (SpecimenEventParameters eventParam : specimen.getSpecimenEventCollection())
			{
				if (eventParam instanceof CollectionEventParameters)
				{
					Calendar calendar = Calendar.getInstance();

					calendar.setTime(eventParam.getTimestamp());
					CollectionEventType xmlCollEvent = new CollectionEventTypeImpl();
					xmlCollEvent.setCollectionDate(calendar);

					SpecimenEventsType xmlSpecimenEvent = new SpecimenEventsTypeImpl();
					xmlSpecimenEvent.setCollectionEvent(xmlCollEvent);
					xmlSpecimen.setSpecimenEvents(xmlSpecimenEvent);
					break;
				}
			}
		}
	}

	private void updateXmlObjectForPosition(Specimen specimen,
			SpecimenType xmlSpecimen) throws BizLogicException, ApplicationException
	{
		Site storageSite = getStorageSite(specimen);
		if(storageSite!=null)
		{
			//create xml specimen position
			SpecimenPositionType xmlPosition = new SpecimenPositionTypeImpl();

			User siteCoordinator = storageSite.getCoordinator();
			// create storeage site then site & co-ordinator
			StorageContainerType xmlContainer = new StorageContainerTypeImpl();
			SiteType xmlSite = new SiteTypeImpl();
			xmlSite.setName(storageSite.getName());

			UserType xmlCordinator = new UserTypeImpl();
			xmlCordinator.setEmail(siteCoordinator.getEmailAddress());
			xmlCordinator.setFirstName(siteCoordinator.getFirstName());
			xmlCordinator.setLastName(siteCoordinator.getLastName());

			xmlSite.setCoordinator(xmlCordinator);
			xmlContainer.setSite(xmlSite);
			xmlPosition.setStorageContainer(xmlContainer);

			xmlSpecimen.setSpecimenPosition(xmlPosition);
		}
	}

	private Site getStorageSite(Specimen specimen) throws ApplicationException,
			BizLogicException
	{
		Site storageSite = null;
		SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
		if(specimenPosition!=null && specimenPosition.getStorageContainer()!=null)
		{
		if(specimenPosition.getStorageContainer().getSite()==null)
		{
			SiteBizLogic bizlogic =  new SiteBizLogic();
			DAO dao = AppUtility.openDAOSession(null);
			try
			{
				storageSite = bizlogic.getSite(dao, specimenPosition.getStorageContainer().getId());
			}
			finally
			{
				AppUtility.closeDAOSession(dao);
			}
		}
		else
		{
			storageSite = specimenPosition.getStorageContainer().getSite();
		}
		}
		return storageSite;
	}


	*//**
	 * This method will handle the errors occured previously during the processing of some specimens.
	 * It will again try to process those specimen.
	 * @param objectType type or class of object
	 * @param type type of event.
	 * @param objectId object identifier.
	 * @throws InterceptProcessorException exception.
	 *//*
	public void onError(String objectType,eventType type,Long objectId) throws InterceptProcessorException
	{
		Specimen specimen = AppUtility.getSpecimen(objectId.toString());
		process(specimen, type) ;

	}*/
}


