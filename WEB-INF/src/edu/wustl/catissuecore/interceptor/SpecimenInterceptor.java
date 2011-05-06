package edu.wustl.catissuecore.interceptor;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;

public class SpecimenInterceptor //implements InterceptProcessor{
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(Specimen.class);

	private static final String TEMP_DIR_LOCATION=System.getProperty("java.io.tmpdir");

	/*public void process(Object obj, eventType type) throws InterceptProcessorException {
		// TODO Auto-generated method stub
		Specimen specimen = (Specimen)obj;
		Long objId = specimen.getId();
		try{
		if(type.onInsert.equals(type))
		{
			System.out.println(" In specimen interceptor : insert mode");
			System.out.println("specimen id = "+objId);
		}
		if(type.OnUpdate.equals(type))
		{
			System.out.println(" In specimen interceptor :update mode");
			System.out.println("specimen id = "+objId);
		}
		ObjectFactory factory = new ObjectFactory();
		edu.wustl.catissuecore.jaxb.domain.Specimen xmlSpecimen = factory.createSpecimen();

		xmlSpecimen.setClazz(specimen.getClassName());// is this rite or should we use getSpecimenClass.
		xmlSpecimen.setId(objId);
		xmlSpecimen.setType(specimen.getSpecimenType());
		xmlSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());
		xmlSpecimen.setQuantity(specimen.getAvailableQuantity()); // need to check is quentity means avaialable quantity or not.
		xmlSpecimen.setIsAvailable(specimen.getIsAvailable());
		xmlSpecimen.setTissueSite(specimen.getSpecimenCharacteristics().getTissueSite());
		xmlSpecimen.setAccessionNumber(specimen.getSpecimenCollectionGroup().getSurgicalPathologyNumber());// what is equivalent of accession number ??
		SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
		xmlSpecimen.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		xmlSpecimen.setClinicalStatus(scg.getClinicalStatus());
		xmlSpecimen.setParticipantEmpi("001");

		SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
		if(specimenPosition!=null && specimenPosition.getStorageContainer()!=null &&  specimenPosition.getStorageContainer().getSite()!=null)
		{
			Site storageSite = specimenPosition.getStorageContainer().getSite();
			xmlSpecimen.setStorageSiteCoordinator(storageSite.getCoordinator().getLastName()+", "+storageSite.getCoordinator().getFirstName());
			xmlSpecimen.setStorageSiteName(storageSite.getName());
		}
		else
		{
			xmlSpecimen.setStorageSiteCoordinator("");
			xmlSpecimen.setStorageSiteName("");
		}

		//xmlSpecimen.setCollectionDate(new Date());// how to create the xmlGregorian calender.
		//from specimenEventCollection search collectionEvent

			for(SpecimenEventParameters eventParam : specimen.getSpecimenEventCollection())
			{
				if(eventParam instanceof CollectionEventParameters)
				{
					DatatypeFactory typeFactory = DatatypeFactory.newInstance();
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(eventParam.getTimestamp());
					XMLGregorianCalendar xmlCalendar = typeFactory.newXMLGregorianCalendar(gc);
					xmlSpecimen.setCollectionDate(xmlCalendar);
					break;
				}
			}


			String fileName = TEMP_DIR_LOCATION+"specimen"+objId+".xml";

			factory.marshelExportDataXml("edu.wustl.catissuecore.jaxb.domain", fileName, xmlSpecimen);
			throw new DAOException();
		} catch (FileNotFoundException e) {
			throw new InterceptProcessorException("001",objId,e,"Error occured while creating the xml message for specimen id "+objId);
		} catch (JAXBException e) {
			throw new InterceptProcessorException("002",objId,e,"Error occured while creating the xml message for specimen id "+objId);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			throw new InterceptProcessorException("003",objId,e,"Error occured while creating the xml message for specimen id "+objId);
		}
		catch(Exception e)
		{
			throw new InterceptProcessorException("000",objId,e,"Error occured while creating the xml message for specimen id "+objId);
		}
	}

	public void onError(String objectType,eventType type,Long objectId)
	{
		System.out.println("in error recovery method");
	}*/
}
