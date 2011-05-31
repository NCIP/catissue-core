package edu.wustl.catissuecore.interceptor;

import java.io.FileNotFoundException;
import java.util.GregorianCalendar;

import javax.jms.JMSException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.interceptor.wmq.SpecimenWmqProcessor;
import edu.wustl.catissuecore.jaxb.domain.ObjectFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.InterceptProcessorException;
import edu.wustl.dao.interceptor.InterceptProcessor;
import edu.wustl.dao.interceptor.SaveUpdateInterceptThread.eventType;
import gov.nih.nci.system.dao.DAOException;

/**
 * This class is responsible for processing any activites that needs to be done whenever a specimen object is
 * saved or updated in the databse using hibernate.
 * @author Pavan
 *
 */
public class SpecimenInterceptor implements InterceptProcessor
{

	/**
	 * Logger
	 */
	private static Logger LOGGER = Logger.getCommonLogger(SpecimenInterceptor.class);

	/**
	 * User temp dir location.
	 */
	private static final String TEMP_DIR_LOCATION=System.getProperty("java.io.tmpdir");

	/**
	 * temporary file name prefix.
	 */
	private static final String FILE_NAME_PREFIX="specimen_";


	/**
	 * Package name for generated JAXB domain Objects.
	 */
	private static final String JAXB_PACKAGE_NAME="edu.wustl.catissuecore.jaxb.domain";
	static int no = 0;

	/**
	 * This method does the processing for given obj which is inserted or updates using hibernate session.
	 * @param obj object to be processed.
	 * @param type event type occured.
	 * @throws InterceptProcessorException exception.
	 */
	public void process(Object obj, eventType type) throws InterceptProcessorException
	{
		// TODO Auto-generated method stub
		Specimen specimen = (Specimen)obj;
		Long objId = specimen.getId();
		try
		{

			ObjectFactory factory = new ObjectFactory();
			edu.wustl.catissuecore.jaxb.domain.Specimen xmlSpecimen = factory.createSpecimen();

			updateJaxbDomainObject(specimen, xmlSpecimen);
			String fileName = TEMP_DIR_LOCATION+FILE_NAME_PREFIX+specimen.getId()+Constants.XML_SUFFIX;

			factory.marshelExportDataXml(JAXB_PACKAGE_NAME, fileName, xmlSpecimen);
			writeMessage(fileName);
			if(no%2==0)
			{
				no++;
				throw new DAOException();
			}
			no++;
		} catch (FileNotFoundException e) {
			throw new InterceptProcessorException("001",objId,e,ApplicationProperties.getValue("error.interceptor.specimen.message",objId.toString()));
		} catch (JAXBException e) {
			throw new InterceptProcessorException("001",objId,e,ApplicationProperties.getValue("error.interceptor.specimen.message",objId.toString()));
		} catch (DatatypeConfigurationException e) {
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

	/**
	 * This method will write down the contents of the given fileName
	 * @param fileName name of the file whose contents should be sent on queue.
	 * @throws JMSException exception in queue handling.
	 */
	private void writeMessage(String fileName) throws JMSException
	{
		SpecimenWmqProcessor.getInstance().writeMessageToQueue(fileName);


	}

	/**
	 * This method will set the properties for given jaxb domain object from the actual specimen object.
	 * @param specimen specimen from which toread the properties
	 * @param xmlSpecimen object which should be updated
	 * @throws DatatypeConfigurationException exception
	 */
	private void updateJaxbDomainObject(Specimen specimen,edu.wustl.catissuecore.jaxb.domain.Specimen xmlSpecimen)
			throws DatatypeConfigurationException
	{
		xmlSpecimen.setClazz(specimen.getClassName());// is this rite or should we use getSpecimenClass.
		xmlSpecimen.setId(specimen.getId());
		xmlSpecimen.setType(specimen.getSpecimenType());
		xmlSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());
		xmlSpecimen.setQuantity(specimen.getAvailableQuantity()); // need to check is quentity means avaialable quantity or not.
		xmlSpecimen.setIsAvailable(specimen.getIsAvailable());
		xmlSpecimen.setTissueSite(specimen.getSpecimenCharacteristics().getTissueSite());
		xmlSpecimen.setAccessionNumber(specimen.getSpecimenCollectionGroup().getSurgicalPathologyNumber());// what is equivalent of accession number ??
		SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
		xmlSpecimen.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		xmlSpecimen.setClinicalStatus(scg.getClinicalStatus());
		xmlSpecimen.setParticipantEmpi("007");

		SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
		if(specimenPosition!=null && specimenPosition.getStorageContainer()!=null &&  specimenPosition.getStorageContainer().getSite()!=null)
		{
			Site storageSite = specimenPosition.getStorageContainer().getSite();
			xmlSpecimen.setStorageSiteCoordinator(storageSite.getCoordinator().getLastName()+", "+storageSite.getCoordinator().getFirstName());
			xmlSpecimen.setStorageSiteName(storageSite.getName());
		}
		xmlSpecimen.setCollectionDate(getCollectionDate(specimen));
		CollectionProtocol collectionProtocol = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol();
		System.out.println(collectionProtocol.getTitle());
		System.out.println(collectionProtocol.getPrincipalInvestigator().getFirstName());
	}

	/**
	 * This method will return the Collection date for the specimen in XML format.
	 * @param specimen specimen whose collection date is needed.
	 * @return collection date in xml format.
	 * @throws DatatypeConfigurationException exception
	 */
	private XMLGregorianCalendar getCollectionDate(Specimen specimen)
			throws DatatypeConfigurationException
	{
		XMLGregorianCalendar xmlCalendar = null;
		for (SpecimenEventParameters eventParam : specimen.getSpecimenEventCollection())
		{
			if (eventParam instanceof CollectionEventParameters)
			{
				DatatypeFactory typeFactory = DatatypeFactory.newInstance();
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(eventParam.getTimestamp());
				xmlCalendar = typeFactory.newXMLGregorianCalendar(gc);

				break;
			}
		}
		return xmlCalendar;
	}

	/**
	 * This method will handle the errors occured previously during the processing of some specimens.
	 * It will again try to process those specimen.
	 * @param objectType type or class of object
	 * @param type type of event.
	 * @param objectId object identifier.
	 * @throws InterceptProcessorException exception.
	 */
	public void onError(String objectType,eventType type,Long objectId) throws InterceptProcessorException
	{
		Specimen specimen = AppUtility.getSpecimen(objectId.toString());
		process(specimen, type) ;

	}
}
