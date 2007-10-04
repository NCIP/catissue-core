package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

public class CollectionProtocolUtil {

	
	private static final String MOLECULAR_SPECIMEN_CLASS = "Molecular";

	private LinkedHashMap<String, CollectionProtocolEventBean> eventBean = 
						new LinkedHashMap<String, CollectionProtocolEventBean> ();
	
	private static final String storageTypeArr[]= {"Virtual", "Auto","Manual"};
	

	public static Integer getStorageTypeValue(String type)
	{
		for (int i=0;i<storageTypeArr.length;i++)
		{
			if(storageTypeArr[i].equals(type))
			{
				return new Integer(i);
			}
		}
		
		return 0;	//default considered as 'Virtual';
	}

	public static String getStorageTypeValue(Integer type)
	{
		if (type == null)
		{
			return storageTypeArr[0]; //default considered as 'Virtual';
		}
		return storageTypeArr[type.intValue()];
	}
	
	public static CollectionProtocolBean getCollectionProtocolBean(CollectionProtocol collectionProtocol)
	{
		CollectionProtocolBean collectionProtocolBean;
		long[] protocolCoordinatorIds = null;
		collectionProtocolBean = new CollectionProtocolBean();
		collectionProtocolBean.setConsentTierCounter(collectionProtocol.getConsentTierCollection().size());
		Long id = new Long (collectionProtocol.getId().longValue());
		collectionProtocolBean.setIdentifier(id);
		
		Collection userCollection = collectionProtocol.getCoordinatorCollection();
		if(userCollection != null)
		{
			protocolCoordinatorIds = new long[userCollection.size()];
			int i=0;
			Iterator it = userCollection.iterator();
			while(it.hasNext())
			{
				User user = (User)it.next();
				protocolCoordinatorIds[i] = user.getId().longValue();
				i++;
			}
		}
		
		collectionProtocolBean.setProtocolCoordinatorIds(protocolCoordinatorIds);
		collectionProtocolBean.setPrincipalInvestigatorId(collectionProtocol.getPrincipalInvestigator().getId().longValue());
		Date date = collectionProtocol.getStartDate();
		collectionProtocolBean.setStartDate(edu.wustl.common.util.Utility.parseDateToString(date, Constants.DATE_FORMAT) );
		collectionProtocolBean.setDescriptionURL(collectionProtocol.getDescriptionURL());
		collectionProtocolBean.setUnsignedConsentURLName(collectionProtocol.getUnsignedConsentDocumentURL());
		collectionProtocolBean.setConsentWaived (collectionProtocol.getConsentsWaived().booleanValue());   
		collectionProtocolBean.setIrbID(collectionProtocol.getIrbIdentifier());
		collectionProtocolBean.setTitle(collectionProtocol.getTitle());
		collectionProtocolBean.setShortTitle(collectionProtocol.getShortTitle());
		collectionProtocolBean.setEnrollment(String.valueOf(collectionProtocol.getEnrollment()));		
		collectionProtocolBean.setConsentValues(prepareConsentTierMap(collectionProtocol.getConsentTierCollection()));
		collectionProtocolBean.setActivityStatus(collectionProtocol.getActivityStatus());
		return collectionProtocolBean;
	}

	public static Map prepareConsentTierMap(Collection consentTierColl)
	{
		Map tempMap = new HashMap();
		if(consentTierColl!=null)
		{
			Iterator consentTierCollIter = consentTierColl.iterator();			
			int i = 0;
			while(consentTierCollIter.hasNext())
			{
				ConsentTier consent = (ConsentTier)consentTierCollIter.next();
				String statement = "ConsentBean:"+i+"_statement";
				String preDefinedStatementkey = "ConsentBean:"+i+"_predefinedConsents";
				String statementkey = "ConsentBean:"+i+"_consentTierID";
				tempMap.put(statement, consent.getStatement());
				tempMap.put(preDefinedStatementkey, consent.getStatement());
				tempMap.put(statementkey, consent.getId());
				i++;
			}
		}
		return tempMap;
	}

	public static CollectionProtocolEventBean getCollectionProtocolEventBean(
			CollectionProtocolEvent collectionProtocolEvent, int counter)
	{
		CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
				
		eventBean.setStudyCalenderEventPoint(new Double
				(collectionProtocolEvent.getStudyCalendarEventPoint())
											);
		eventBean.setCollectionPointLabel(collectionProtocolEvent.getCollectionPointLabel());
		SpecimenCollectionRequirementGroup specimenRequirementGroup =
								collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
		eventBean.setClinicalDiagnosis(specimenRequirementGroup.getClinicalDiagnosis());
		eventBean.setClinicalStatus(specimenRequirementGroup.getClinicalStatus());
		eventBean.setId(collectionProtocolEvent.getId().longValue());
		eventBean.setUniqueIdentifier("E"+ counter++);
		AbstractSpecimenCollectionGroup requirementGroup =collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
		
		eventBean.setSpecimenCollRequirementGroupId(
				requirementGroup.getId().longValue());
		
		eventBean.setSpecimenRequirementbeanMap(
				getSpecimensMap(specimenRequirementGroup.getSpecimenCollection(), 
						eventBean.getUniqueIdentifier()) );

		return eventBean;
	}
	

	public static LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection specimenCollection,
			String parentUniqueId)
	{
		LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();
		
		Iterator specimenIterator = specimenCollection.iterator();
		int specCtr=0;
		while(specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen)specimenIterator.next();
			if (specimen.getParentSpecimen() == null)
			{
				SpecimenRequirementBean specBean =getSpecimenBean(specimen, null, parentUniqueId, specCtr++);

				specimenMap.put(specBean.getUniqueIdentifier(), specBean);				
			}
			
		}
		return specimenMap;
	}
	
	private static LinkedHashMap<String, GenericSpecimen> getChildAliquots(Specimen specimen, 
			String parentuniqueId)
	{
		Collection specimenChildren = specimen.getChildrenSpecimen();
		Iterator iterator = specimenChildren.iterator();
		LinkedHashMap<String, GenericSpecimen>  aliquotMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		int aliqCtr =1;
		
		while(iterator.hasNext())
		{
			Specimen childSpecimen = (Specimen) iterator.next();
			if(Constants.ALIQUOT.equals(childSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean = getSpecimenBean(
						childSpecimen, specimen.getLabel(), parentuniqueId, aliqCtr++);
				
				aliquotMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}

		
		return aliquotMap;
	}

	private static LinkedHashMap<String, GenericSpecimen> getChildDerived(Specimen specimen, 
			String parentuniqueId)
	{
		Collection specimenChildren = specimen.getChildrenSpecimen();
		Iterator iterator = specimenChildren.iterator();
		LinkedHashMap<String, GenericSpecimen>  derivedMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		int deriveCtr=1;
		while(iterator.hasNext())
		{
			Specimen childSpecimen = (Specimen) iterator.next();
			if(Constants.DERIVED_SPECIMEN.equals(childSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean = 
					getSpecimenBean(childSpecimen, specimen.getLabel(),
							parentuniqueId, deriveCtr++);
				derivedMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}


		return derivedMap;
	}	

	private static String getUniqueId(String lineage, int ctr)
	{
		if(Constants.NEW_SPECIMEN.equals(lineage))
		{
			return Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + ctr;
		}

		if(Constants.DERIVED_SPECIMEN.equals(lineage))
		{
			return Constants.UNIQUE_IDENTIFIER_FOR_DERIVE + ctr;
		}
		if(Constants.ALIQUOT.equals(lineage))
		{
			return Constants.UNIQUE_IDENTIFIER_FOR_ALIQUOT + ctr;
		}
		return null;
	}
	private static SpecimenRequirementBean getSpecimenBean(Specimen specimen, String parentName,
										String parentUniqueId, int specCtr)
	{
		
		SpecimenRequirementBean speRequirementBean = new SpecimenRequirementBean();
		speRequirementBean.setLineage(specimen.getLineage());
		speRequirementBean.setUniqueIdentifier(
				parentUniqueId + getUniqueId(specimen.getLineage(),specCtr));

		speRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN 
						+ "_" + speRequirementBean.getUniqueIdentifier());
		
		speRequirementBean.setClassName(specimen.getClassName());
		speRequirementBean.setType(specimen.getType());
		speRequirementBean.setId(specimen.getId().longValue());
		SpecimenCharacteristics characteristics = specimen.getSpecimenCharacteristics();

		if(characteristics != null)
		{
			speRequirementBean.setTissueSite(characteristics.getTissueSite());
			speRequirementBean.setTissueSide(characteristics.getTissueSide());
		}
		
		speRequirementBean.setSpecimenCharsId(specimen.getSpecimenCharacteristics().getId().longValue());
		speRequirementBean.setPathologicalStatus(specimen.getPathologicalStatus());
		
		if(MOLECULAR_SPECIMEN_CLASS.equals(specimen.getClassName()))
		{
			Double concentration = ((MolecularSpecimen)specimen).getConcentrationInMicrogramPerMicroliter();
			if (concentration != null)
			{
				speRequirementBean.setConcentration( String.valueOf(concentration.doubleValue()));
			}
		}
		
		Quantity quantity = specimen.getInitialQuantity();
		
		if(quantity != null)
		{
			if(quantity.getValue()!=null)
			{
				Double quantityValue = quantity.getValue();
				speRequirementBean.setQuantity(String.valueOf(quantityValue.doubleValue()));
			}
		}
		
		speRequirementBean.setStorageContainerForSpecimen( 
				getStorageTypeValue(specimen.getPositionDimensionOne()) );
		setSpecimenEventParameters(specimen,speRequirementBean );
		
		speRequirementBean.setParentName(parentName);
		
		LinkedHashMap<String, GenericSpecimen> aliquotMap = 
			getChildAliquots(specimen, speRequirementBean.getUniqueIdentifier());
		LinkedHashMap<String, GenericSpecimen> derivedMap =
			getChildDerived(specimen, speRequirementBean.getUniqueIdentifier());
		
		Collection aliquotCollection = aliquotMap.values();
		Collection derivedCollection = derivedMap.values();
		if (aliquotCollection != null && !aliquotCollection.isEmpty())
		{
			Iterator iterator = aliquotCollection.iterator();
			GenericSpecimen aliquotSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setStorageContainerForAliquotSpecimem(
					aliquotSpecimen.getStorageContainerForSpecimen() );
			speRequirementBean.setQuantityPerAliquot(aliquotSpecimen.getQuantity());
		}
		
		speRequirementBean.setNoOfAliquots(String.valueOf(aliquotCollection.size()));			
		speRequirementBean.setAliquotSpecimenCollection(aliquotMap);
		
		speRequirementBean.setDeriveSpecimenCollection(derivedMap);
		speRequirementBean.setNoOfDeriveSpecimen(derivedCollection.size());
		derivedMap = getDerviredObjectMap(derivedMap.values());
	    speRequirementBean.setDeriveSpecimen(derivedMap);

		if (derivedCollection != null && !derivedCollection.isEmpty())
		{
			Iterator iterator = derivedCollection.iterator();
			GenericSpecimen derivedSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setDeriveClassName(derivedSpecimen.getClassName());
			speRequirementBean.setDeriveType(derivedSpecimen.getType());
			speRequirementBean.setDeriveConcentration(derivedSpecimen.getConcentration());
			speRequirementBean.setDeriveQuantity(derivedSpecimen.getQuantity());
		}
		
		return speRequirementBean;
	}
	private static LinkedHashMap getDerviredObjectMap(Collection<GenericSpecimen> derivedCollection)
	{
		LinkedHashMap<String, String> derivedObjectMap = new LinkedHashMap<String, String> ();
		Iterator<GenericSpecimen> iterator = derivedCollection.iterator();
		int deriveCtr=1;
		while (iterator.hasNext())
		{
			SpecimenRequirementBean derivedSpecimen = (SpecimenRequirementBean) iterator.next();

			StringBuffer derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_id");
			derivedObjectMap.put(derivedSpecimenKey.toString(), String.valueOf(derivedSpecimen.getId()));
			
			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_specimenClass" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getClassName());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_specimenType" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), derivedSpecimen.getType());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_storageLocation" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), 
					derivedSpecimen.getStorageContainerForSpecimen());

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_quantity" );
			String quantity = derivedSpecimen.getQuantity();
			derivedObjectMap.put(derivedSpecimenKey.toString(), quantity);

			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_concentration" );
			
			derivedObjectMap.put(derivedSpecimenKey.toString(), 
					derivedSpecimen.getConcentration());
			derivedSpecimenKey = getKeyBase(deriveCtr);
			derivedSpecimenKey.append("_unit" );
			derivedObjectMap.put(derivedSpecimenKey.toString(), "");	
			deriveCtr++;
		}
		return derivedObjectMap;
	}

	/**
	 * @param deriveCtr
	 * @return
	 */
	private static StringBuffer getKeyBase(int deriveCtr) {
		StringBuffer derivedSpecimenKey = new StringBuffer();
		derivedSpecimenKey.append("DeriveSpecimenBean:");
		derivedSpecimenKey.append(String.valueOf(deriveCtr));
		return derivedSpecimenKey;
	}
	private  static void setSpecimenEventParameters(Specimen specimen, SpecimenRequirementBean specimenRequirementBean)
	{
		Collection eventsParametersColl = specimen.getSpecimenEventCollection();
		if(eventsParametersColl == null || eventsParametersColl.isEmpty())
		{
			return;
		}

		Iterator iter = eventsParametersColl.iterator();
		
		while(iter.hasNext())
		{
			Object tempObj = iter.next();

			if(tempObj instanceof CollectionEventParameters)
			{
				CollectionEventParameters collectionEventParameters = (CollectionEventParameters)tempObj;
				specimenRequirementBean.setCollectionEventId(collectionEventParameters.getId().longValue());
				//this.collectionEventSpecimenId = collectionEventParameters.getSpecimen().getId().longValue();
				specimenRequirementBean.setCollectionEventUserId(
						collectionEventParameters.getUser().getId().longValue());					
				specimenRequirementBean.setCollectionEventCollectionProcedure(
						collectionEventParameters.getCollectionProcedure());

				specimenRequirementBean.setCollectionEventContainer(collectionEventParameters.getContainer());
			}
			else if(tempObj instanceof ReceivedEventParameters)
			{
				ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)tempObj;

				specimenRequirementBean.setReceivedEventId(receivedEventParameters.getId().longValue());
				specimenRequirementBean.setReceivedEventUserId(
						receivedEventParameters.getUser().getId().longValue());
				specimenRequirementBean.setReceivedEventReceivedQuality(
						receivedEventParameters.getReceivedQuality());
			}
		}
		
	}
	
	/**
	 * @param mapping
	 * @param request
	 * @param cpSessionList
	 */
	public static void updateSession(HttpServletRequest request,  Long id)
			throws DAOException{
		
		List sessionCpList = new CollectionProtocolBizLogic().retrieveCP(
				CollectionProtocol.class.getName(), "id",id);

		if (sessionCpList == null || sessionCpList.size()<2){
			
			throw new DAOException("Fail to retrieve Collection protocol..");
		}
		
		HttpSession session = request.getSession();
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		CollectionProtocolBean collectionProtocolBean = 
			(CollectionProtocolBean)sessionCpList.get(0);
		collectionProtocolBean.setOperation("update");
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN,
				sessionCpList.get(0));
		
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP,
				sessionCpList.get(1));
	}


	/**
	 * @param collectionProtocolEventColl
	 * @return
	 */
	public static  LinkedHashMap<String, CollectionProtocolEventBean> getCollectionProtocolEventMap(
			Collection collectionProtocolEventColl) {
		Iterator iterator = collectionProtocolEventColl.iterator();
		LinkedHashMap<String, CollectionProtocolEventBean> eventMap = 
			new LinkedHashMap<String, CollectionProtocolEventBean>();
		int ctr=1;
		while(iterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent=
				(CollectionProtocolEvent)iterator.next();
			
			CollectionProtocolEventBean eventBean =
				CollectionProtocolUtil.getCollectionProtocolEventBean(collectionProtocolEvent,ctr++);
			eventMap.put(eventBean.getUniqueIdentifier(), eventBean);
		}
		return eventMap;
	}
	

}
