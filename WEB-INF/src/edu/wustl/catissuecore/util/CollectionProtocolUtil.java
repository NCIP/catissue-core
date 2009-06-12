package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class CollectionProtocolUtil 
{

	private static Logger logger = Logger.getCommonLogger(CollectionProtocolUtil.class);
	private static final String MOLECULAR_SPECIMEN_CLASS = "Molecular";

	private LinkedHashMap<String, CollectionProtocolEventBean> eventBean = 
						new LinkedHashMap<String, CollectionProtocolEventBean> ();
	
	private static final String storageTypeArr[]= {"Virtual", "Auto","Manual"};
	public static Integer getStorageTypeValue(String type)
	{	
		int returnVal=Integer.valueOf(0);
		for (int i=0;i<storageTypeArr.length;i++)
		{
			if(storageTypeArr[i].equals(type))
			{
				returnVal=Integer.valueOf(i);
				break;
			}
		}
		
		return returnVal; 	//default considered as 'Virtual'; 
	}

	public static String getStorageTypeValue(Integer type)
	{
		String storeArr=storageTypeArr[type.intValue()];
		if (type == null)
		{
			storeArr=storageTypeArr[0]; //default considered as 'Virtual';
		}
		//if(type.intValue()>2) return storageTypeArr[1];
		return storeArr;
	}
	
	public static CollectionProtocolBean getCollectionProtocolBean(CollectionProtocol collectionProtocol)
	{
		CollectionProtocolBean collectionProtocolBean;
		long[] protocolCoordinatorIds = null;
		collectionProtocolBean = new CollectionProtocolBean();
		collectionProtocolBean.setConsentTierCounter(collectionProtocol.getConsentTierCollection().size());
		Long id = Long.valueOf(collectionProtocol.getId().longValue());
		collectionProtocolBean.setIdentifier(id);
		
		protocolCoordinatorIds = getProtocolCordnateIds(collectionProtocol, protocolCoordinatorIds);
		
		collectionProtocolBean.setProtocolCoordinatorIds(protocolCoordinatorIds);
		collectionProtocolBean.setPrincipalInvestigatorId(collectionProtocol.getPrincipalInvestigator().getId().longValue());
		Date date = collectionProtocol.getStartDate();
		collectionProtocolBean.setStartDate(edu.wustl.common.util.Utility.parseDateToString(date, Constants.DATE_FORMAT) );
		collectionProtocolBean.setDescriptionURL(collectionProtocol.getDescriptionURL());
		collectionProtocolBean.setUnsignedConsentURLName(collectionProtocol.getUnsignedConsentDocumentURL());
		if(collectionProtocol.getConsentsWaived()==null)
		{
			collectionProtocol.setConsentsWaived(false);
		}
		collectionProtocolBean.setConsentWaived (collectionProtocol.getConsentsWaived().booleanValue());
		collectionProtocolBean.setIrbID(collectionProtocol.getIrbIdentifier());
		collectionProtocolBean.setTitle(collectionProtocol.getTitle());
		collectionProtocolBean.setShortTitle(collectionProtocol.getShortTitle());
		collectionProtocolBean.setEnrollment(String.valueOf(collectionProtocol.getEnrollment()));		
		collectionProtocolBean.setConsentValues(prepareConsentTierMap(collectionProtocol.getConsentTierCollection()));
		collectionProtocolBean.setActivityStatus(collectionProtocol.getActivityStatus());
		collectionProtocolBean.setAliqoutInSameContainer(collectionProtocol.getAliquotInSameContainer().booleanValue());
		String endDate = Utility.parseDateToString(collectionProtocol.getEndDate(),CommonServiceLocator.getInstance().getDatePattern());
		collectionProtocolBean.setEndDate(endDate);
		if(collectionProtocol.getCollectionProtocolRegistrationCollection().size() > 0)
		{
			collectionProtocolBean.setParticiapantReg(true);
		}
		return collectionProtocolBean;
	}

	/**
	 * @param collectionProtocol
	 * @param protocolCoordinatorIds
	 * @return
	 */
	private static long[] getProtocolCordnateIds(CollectionProtocol collectionProtocol,
			long[] protocolCoordinatorIds)
	{
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
		return protocolCoordinatorIds;
	}

	public static Map prepareConsentTierMap(Collection consentTierColl)
	{
		Map tempMap = new LinkedHashMap();//bug 8905
		List<ConsentTier> consentsList = new ArrayList<ConsentTier>();
		if(consentTierColl!=null)
		{
			consentsList.addAll(consentTierColl);//bug 8905
			Collections.sort(consentsList,new IdComparator());//bug 8905
			//Iterator consentTierCollIter = consentTierColl.iterator();
			Iterator consentTierCollIter = consentsList.iterator();//bug 8905
			int i = 0;
			while(consentTierCollIter.hasNext())
			{
				ConsentTier consent = (ConsentTier)consentTierCollIter.next();
				String statement = "ConsentBean:"+i+"_statement";
				String statementkey = "ConsentBean:"+i+"_consentTierID";
				tempMap.put(statement, consent.getStatement());
				tempMap.put(statementkey, String.valueOf(consent.getId()));
				i++;
			}
		}
		return tempMap;
	}

	public static CollectionProtocolEventBean getCollectionProtocolEventBean(
			CollectionProtocolEvent  collectionProtocolEvent, int counter, DAO dao) throws DAOException
	{
		CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
		eventBean.setId(collectionProtocolEvent.getId().longValue());
		eventBean.setStudyCalenderEventPoint(new Double
				(collectionProtocolEvent.getStudyCalendarEventPoint()));
		eventBean.setCollectionPointLabel(collectionProtocolEvent.getCollectionPointLabel());
		eventBean.setClinicalDiagnosis(collectionProtocolEvent.getClinicalDiagnosis());
		eventBean.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
		eventBean.setId(collectionProtocolEvent.getId().longValue());
		eventBean.setUniqueIdentifier("E"+ counter++);
		eventBean.setSpecimenCollRequirementGroupId(
				collectionProtocolEvent.getId().longValue());
		eventBean.setSpecimenRequirementbeanMap(
				getSpecimensMap(collectionProtocolEvent.getSpecimenRequirementCollection(), 
						eventBean.getUniqueIdentifier()) );

		return eventBean;
	}
	
	public static List getSortedCPEventList(List genericList)
	{
		//Comparator to sort the List of Map chronologically.
		final Comparator identifierComparator = new Comparator()
		{
			public int compare(Object object1, Object object2)
			{
				Long identifier1 = null;
				Long identifier2 = null;
				
				if(object1 instanceof CollectionProtocolEvent)
				{
					identifier1 = ((CollectionProtocolEvent)object1).getId();
					identifier2 = ((CollectionProtocolEvent)object2).getId();
				}
				else if(object1 instanceof SpecimenRequirement)
				{
					identifier1 = ((SpecimenRequirement)object1).getId();
					identifier2 = ((SpecimenRequirement)object2).getId();
				}
				else if (object1 instanceof AbstractSpecimen)
				{
					identifier1 = ((AbstractSpecimen) object1).getId();
					identifier2 = ((AbstractSpecimen) object2).getId();
				}
				
				if(identifier1!= null && identifier2 != null)
				{
					return identifier1.compareTo(identifier2);
				}
				if(identifier1 ==null)
				{
					return -1;
				}
				if(identifier2 == null)
				{
					return 1;
				}
				return 0;
			}
		};
		Collections.sort(genericList, identifierComparator);
		return genericList;
	}
	public static LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection<SpecimenRequirement> reqSpecimenCollection,
			String parentUniqueId)
	{
		LinkedHashMap<String, GenericSpecimen> reqSpecimenMap = new LinkedHashMap<String, GenericSpecimen>();
		List<SpecimenRequirement> reqSpecimenList = new LinkedList<SpecimenRequirement>(reqSpecimenCollection);
		getSortedCPEventList(reqSpecimenList);
		Iterator<SpecimenRequirement> specimenIterator = reqSpecimenList.iterator();
		int specCtr=0;
		while(specimenIterator.hasNext())
		{
			SpecimenRequirement reqSpecimen = specimenIterator.next();
			if (reqSpecimen.getParentSpecimen() == null)
			{
				SpecimenRequirementBean specBean =getSpecimenBean(reqSpecimen, null, parentUniqueId, specCtr++);
				reqSpecimenMap.put(specBean.getUniqueIdentifier(), specBean);				
			}
		}
		return reqSpecimenMap;
	}
	
	private static LinkedHashMap<String, GenericSpecimen> getChildAliquots(SpecimenRequirement reqSpecimen, 
			String parentuniqueId, String parentName)
	{
		Collection reqSpecimenChildren = reqSpecimen.getChildSpecimenCollection();
		List reqSpecimenList = new LinkedList<SpecimenRequirement>(reqSpecimenChildren);
		getSortedCPEventList(reqSpecimenList);
		Iterator<SpecimenRequirement> iterator = reqSpecimenList.iterator();
		LinkedHashMap<String, GenericSpecimen>  aliquotMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		int aliqCtr =1;
		
		while(iterator.hasNext())
		{
			SpecimenRequirement childReqSpecimen = iterator.next();
			if(Constants.ALIQUOT.equals(childReqSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean = getSpecimenBean(
						childReqSpecimen, parentName, parentuniqueId, aliqCtr++);
				aliquotMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}
		return aliquotMap;
	}

	private static LinkedHashMap<String, GenericSpecimen> getChildDerived(SpecimenRequirement specimen, 
			String parentuniqueId, String parentName)
	{
		Collection specimenChildren = specimen.getChildSpecimenCollection();
		List specimenList = new LinkedList(specimenChildren);
		getSortedCPEventList(specimenList);
		Iterator<SpecimenRequirement> iterator = specimenList.iterator();
		LinkedHashMap<String, GenericSpecimen>  derivedMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		int deriveCtr=1;
		while(iterator.hasNext())
		{
			SpecimenRequirement childReqSpecimen = iterator.next();
			if(Constants.DERIVED_SPECIMEN.equals(childReqSpecimen.getLineage()))
			{
				SpecimenRequirementBean specimenBean = 
					getSpecimenBean(childReqSpecimen, parentName,
							parentuniqueId, deriveCtr++);
				derivedMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
			}
		}


		return derivedMap;
	}	

	private static String getUniqueId(String lineage, int ctr)
	{	String constantVal=null;
		if(Constants.NEW_SPECIMEN.equals(lineage))
		{
			constantVal=Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + ctr;
		}

		else if(Constants.DERIVED_SPECIMEN.equals(lineage))
		{
			constantVal=Constants.UNIQUE_IDENTIFIER_FOR_DERIVE + ctr;
		}
		else if(Constants.ALIQUOT.equals(lineage))
		{
			constantVal= Constants.UNIQUE_IDENTIFIER_FOR_ALIQUOT + ctr;
		}
		return constantVal;
	}
	private static SpecimenRequirementBean getSpecimenBean(SpecimenRequirement reqSpecimen, String parentName,
										String parentUniqueId, int specCtr)
	{
		
		SpecimenRequirementBean speRequirementBean = new SpecimenRequirementBean();
		speRequirementBean.setId(reqSpecimen.getId().longValue());
		speRequirementBean.setLineage(reqSpecimen.getLineage());
		speRequirementBean.setUniqueIdentifier(
				parentUniqueId + getUniqueId(reqSpecimen.getLineage(),specCtr));

		speRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN 
						+ "_" + speRequirementBean.getUniqueIdentifier());
		
		speRequirementBean.setClassName(reqSpecimen.getClassName());
		speRequirementBean.setType(reqSpecimen.getSpecimenType());
		speRequirementBean.setId(reqSpecimen.getId().longValue());
		SpecimenCharacteristics characteristics = reqSpecimen.getSpecimenCharacteristics();
		updateSpeRequirementBean(reqSpecimen, speRequirementBean,
				characteristics);
		
		Double quantity = reqSpecimen.getInitialQuantity();
		
		if(quantity != null)
		{
			speRequirementBean.setQuantity(String.valueOf(quantity));
		}
		
		if(reqSpecimen.getStorageType() != null)
		{
			speRequirementBean.setStorageContainerForSpecimen(reqSpecimen.getStorageType());
		}
		setSpecimenEventParameters(reqSpecimen,speRequirementBean );
		
		setAliquotAndDerivedColl(reqSpecimen, parentName, speRequirementBean);
	
		return speRequirementBean;
	}

	/**
	 * set Specimen Requirements.
	 * @param reqSpecimen
	 * @param speRequirementBean
	 * @param characteristics
	 */
	private static void updateSpeRequirementBean(
			SpecimenRequirement reqSpecimen,
			SpecimenRequirementBean speRequirementBean,
			SpecimenCharacteristics characteristics) 
	{
		if(characteristics != null)
		{
			speRequirementBean.setTissueSite(characteristics.getTissueSite());
			speRequirementBean.setTissueSide(characteristics.getTissueSide());
		}
		
		speRequirementBean.setSpecimenCharsId(reqSpecimen.getSpecimenCharacteristics().getId().longValue());
		speRequirementBean.setPathologicalStatus(reqSpecimen.getPathologicalStatus());
		if(MOLECULAR_SPECIMEN_CLASS.equals(reqSpecimen.getClassName()))
		{
			Double concentration = ((MolecularSpecimenRequirement)reqSpecimen).getConcentrationInMicrogramPerMicroliter();
			if (concentration != null)
			{
				speRequirementBean.setConcentration( String.valueOf(concentration.doubleValue()));
			}
		}
	}

	/**
	 * 
	 * @param reqSpecimen
	 * @param parentName
	 * @param speRequirementBean
	 * @return
	 */
	private static void setAliquotAndDerivedColl(
			SpecimenRequirement reqSpecimen, String parentName,
			SpecimenRequirementBean speRequirementBean)
	{
		speRequirementBean.setParentName(parentName);
		
		LinkedHashMap<String, GenericSpecimen> aliquotMap = 
			getChildAliquots(reqSpecimen, speRequirementBean.getUniqueIdentifier(),speRequirementBean.getDisplayName());
		LinkedHashMap<String, GenericSpecimen> derivedMap =
			getChildDerived(reqSpecimen, speRequirementBean.getUniqueIdentifier(), speRequirementBean.getDisplayName());
		
		Collection aliquotCollection = aliquotMap.values();
		Collection derivedCollection = derivedMap.values();
		//added method
		setQuantityPerAliquot(speRequirementBean, aliquotCollection);
		
		speRequirementBean.setNoOfAliquots(String.valueOf(aliquotCollection.size()));			
		speRequirementBean.setAliquotSpecimenCollection(aliquotMap);
		
		speRequirementBean.setDeriveSpecimenCollection(derivedMap);
		speRequirementBean.setNoOfDeriveSpecimen(derivedCollection.size());
		derivedMap = getDerviredObjectMap(derivedMap.values());
	    speRequirementBean.setDeriveSpecimen(derivedMap);
	    setDeriveQuantity(speRequirementBean, derivedCollection);
		
	}

	
	/**
	 * set specimen requirement bean by DerivedCollection.
	 * @param speRequirementBean
	 * @param derivedCollection
	 */
	private static void setDeriveQuantity(
			SpecimenRequirementBean speRequirementBean,
			Collection derivedCollection)
	{
		if (derivedCollection != null && !derivedCollection.isEmpty())
		{
			Iterator iterator = derivedCollection.iterator();
			GenericSpecimen derivedSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setDeriveClassName(derivedSpecimen.getClassName());
			speRequirementBean.setDeriveType(derivedSpecimen.getType());
			speRequirementBean.setDeriveConcentration(derivedSpecimen.getConcentration());
			speRequirementBean.setDeriveQuantity(derivedSpecimen.getQuantity());
		}
	}

	/**
	 *  set specimen requirement bean by AliquotCollection.
	 * @param speRequirementBean
	 * @param aliquotCollection
	 */
	private static void setQuantityPerAliquot(
			SpecimenRequirementBean speRequirementBean,
			Collection aliquotCollection)
	{
		if (aliquotCollection != null && !aliquotCollection.isEmpty())
		{
			Iterator iterator = aliquotCollection.iterator();
			GenericSpecimen aliquotSpecimen = (GenericSpecimen)iterator.next();
			speRequirementBean.setStorageContainerForAliquotSpecimem(
					aliquotSpecimen.getStorageContainerForSpecimen() );
			speRequirementBean.setQuantityPerAliquot(aliquotSpecimen.getQuantity());
		}
	}
	public static LinkedHashMap getDerviredObjectMap(Collection<GenericSpecimen> derivedCollection)
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
	private static StringBuffer getKeyBase(int deriveCtr)
	{
		StringBuffer derivedSpecimenKey = new StringBuffer();
		derivedSpecimenKey.append("DeriveSpecimenBean:");
		derivedSpecimenKey.append(String.valueOf(deriveCtr));
		return derivedSpecimenKey;
	}
	
	private  static void setSpecimenEventParameters(SpecimenRequirement reqSpecimen, SpecimenRequirementBean specimenRequirementBean)
	{
		Collection eventsParametersColl = reqSpecimen.getSpecimenEventCollection();
		if(eventsParametersColl == null || eventsParametersColl.isEmpty())
		{
			return;
		}

		Iterator iter = eventsParametersColl.iterator();
		
		while(iter.hasNext())
		{
			setSpecimenEvents(specimenRequirementBean, iter);
		}
		
	}

	/**
	 * set setSpeciEevntParams
	 * @param specimenRequirementBean
	 * @param iter
	 */
	private static void setSpecimenEvents(
			SpecimenRequirementBean specimenRequirementBean, Iterator iter)
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
	
	/**
	 * @param request
	 * @param cpSessionList
	 */
	public static void updateSession(HttpServletRequest request,  Long id)
			throws ApplicationException{
		
		List sessionCpList = new CollectionProtocolBizLogic().retrieveCP(id);

		if (sessionCpList == null || sessionCpList.size()<2){
			
			throw new ApplicationException(ErrorKey.getErrorKey("errors.item"),null,"Fail to retrieve Collection protocol..");
		}
		
		HttpSession session = request.getSession();
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		setPrivilegesForCP(id,session);
		CollectionProtocolBean collectionProtocolBean = 
			(CollectionProtocolBean)sessionCpList.get(0);
		collectionProtocolBean.setOperation("update");
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN,
				sessionCpList.get(0));
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP,
				sessionCpList.get(1));
		String cptitle = collectionProtocolBean.getTitle();
		String treeNode = "cpName_"+cptitle;
		session.setAttribute(Constants.TREE_NODE_ID, treeNode);
		session.setAttribute("tempKey", treeNode);
		
	}
	
	private static  void setPrivilegesForCP(Long cpId,HttpSession session) 
	{
		Map<String,SiteUserRolePrivilegeBean> map = CaTissuePrivilegeUtility.getPrivilegesOnCP(cpId); 
		session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP, map);
		
	}


	/**
	 * @param collectionProtocolEventColl
	 * @return
	 * @throws DAOException 
	 * @throws DAOException 
	 */
	public static  LinkedHashMap<String, CollectionProtocolEventBean> getCollectionProtocolEventMap(
			Collection collectionProtocolEventColl, DAO dao) throws DAOException 
	{
		Iterator iterator = collectionProtocolEventColl.iterator();
		LinkedHashMap<String, CollectionProtocolEventBean> eventMap = 
			new LinkedHashMap<String, CollectionProtocolEventBean>();
		int ctr=1;
		while(iterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent=
				(CollectionProtocolEvent)iterator.next();
			
			CollectionProtocolEventBean eventBean =
				CollectionProtocolUtil.getCollectionProtocolEventBean(collectionProtocolEvent,ctr++,dao);
			eventMap.put(eventBean.getUniqueIdentifier(), eventBean);
		}
		return eventMap;
	}
	
	public static CollectionProtocol populateCollectionProtocolObjects(HttpServletRequest request)
		throws Exception 
	{
		
		HttpSession session = request.getSession();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		
		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = (LinkedHashMap) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		if (cpEventMap == null)
		{
			throw AppUtility.getApplicationException(null, "event.req", "");
		}
		CollectionProtocol collectionProtocol = createCollectionProtocolDomainObject(collectionProtocolBean);
		Collection collectionProtocolEventList = new LinkedHashSet();
		
		Collection collectionProtocolEventBeanColl = cpEventMap.values();
		if (collectionProtocolEventBeanColl != null)
		{
			
			Iterator cpEventIterator = collectionProtocolEventBeanColl.iterator();
		
			while (cpEventIterator.hasNext()) {
		
				CollectionProtocolEventBean cpEventBean = (CollectionProtocolEventBean) cpEventIterator
						.next();
				CollectionProtocolEvent collectionProtocolEvent = getCollectionProtocolEvent(cpEventBean);
				collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
				collectionProtocolEventList.add(collectionProtocolEvent);
			}
		}	
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventList);
		return collectionProtocol;
	}

	

	/**
	 * Creates collection protocol domain object from given collection protocol bean.
	 * @param cpBean
	 * @return
	 * @throws Exception
	 */
	private static CollectionProtocol createCollectionProtocolDomainObject(
			CollectionProtocolBean cpBean) throws Exception 
	{

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(cpBean.getIdentifier());
		collectionProtocol.setActivityStatus(cpBean.getActivityStatus());
		collectionProtocol.setConsentsWaived(cpBean.isConsentWaived());
		collectionProtocol.setAliquotInSameContainer(cpBean.isAliqoutInSameContainer());
		collectionProtocol.setConsentTierCollection(collectionProtocol.prepareConsentTierCollection(cpBean.getConsentValues()));
		Collection coordinatorCollection = new LinkedHashSet();
		Collection<Site> siteCollection = new LinkedHashSet<Site>();
		setCoordinatorColl(collectionProtocol,
				coordinatorCollection, cpBean);
		
		
		setSiteColl(collectionProtocol, siteCollection, cpBean);

		collectionProtocol.setDescriptionURL(cpBean.getDescriptionURL());
		Integer enrollmentNo=null;
		try{
			enrollmentNo = Integer.valueOf(cpBean.getEnrollment());
		}catch(NumberFormatException e){
			logger.debug(e.getMessage(), e);
			enrollmentNo = Integer.valueOf(0);
		}
		collectionProtocol.setEnrollment(enrollmentNo);
		User principalInvestigator = new User();
		principalInvestigator.setId(Long.valueOf(cpBean
				.getPrincipalInvestigatorId()));

		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		collectionProtocol.setShortTitle(cpBean.getShortTitle());
		Date startDate = Utility.parseDate(cpBean.getStartDate(), Utility
				.datePattern(cpBean.getStartDate()));
		collectionProtocol.setStartDate(startDate);
		collectionProtocol.setTitle(cpBean.getTitle());
		collectionProtocol.setUnsignedConsentDocumentURL(cpBean
				.getUnsignedConsentURLName());
		collectionProtocol.setIrbIdentifier(cpBean.getIrbID());
		return collectionProtocol;
	}

	/**
	 * @param collectionProtocol
	 * @param siteCollection
	 * @param siteArr
	 */
	private static void setSiteColl(
			CollectionProtocol collectionProtocol,
			Collection<Site> siteCollection,CollectionProtocolBean cpBean)
	{
		long[] siteArr = cpBean.getSiteIds();
		if (siteArr != null)
		{
			for (int i = 0; i < siteArr.length; i++) {
				if (siteArr[i] != -1) {
					Site site = new Site();
					site.setId(Long.valueOf(siteArr[i]));
					siteCollection.add(site);
				}
			}
			collectionProtocol.setSiteCollection(siteCollection);
		}
	}

	/**
	 * 
	 * @param collectionProtocol
	 * @param coordinatorCollection
	 * @param coordinatorsArr
	 */
	private static void setCoordinatorColl(
			CollectionProtocol collectionProtocol,
			Collection coordinatorCollection, CollectionProtocolBean cpBean) 
	{
		long[] coordinatorsArr = cpBean.getProtocolCoordinatorIds();
		if (coordinatorsArr != null) 
		{
			for (int i = 0; i < coordinatorsArr.length; i++) {
				if (coordinatorsArr[i] >= 1) {
					User coordinator = new User();
					coordinator.setId(Long.valueOf(coordinatorsArr[i]));
					coordinatorCollection.add(coordinator);
				}
			}
			collectionProtocol.setCoordinatorCollection(coordinatorCollection);
		}
	}

	
	/**
	* This function used to create CollectionProtocolEvent domain object
	* from given CollectionProtocolEventBean Object.
	* @param cpEventBean 
	* @return CollectionProtocolEvent domain object.
	*/
	private static CollectionProtocolEvent getCollectionProtocolEvent(
		CollectionProtocolEventBean cpEventBean) 
	{
	
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		collectionProtocolEvent.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setCollectionPointLabel(cpEventBean.getCollectionPointLabel());
		collectionProtocolEvent.setStudyCalendarEventPoint(cpEventBean.getStudyCalenderEventPoint());
		collectionProtocolEvent.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		collectionProtocolEvent.setClinicalDiagnosis(cpEventBean.getClinicalDiagnosis());
		if (cpEventBean.getId()==-1)
		{
			collectionProtocolEvent.setId(null);
		}
		else
		{
			collectionProtocolEvent.setId(Long.valueOf(cpEventBean.getId()));
		}
		
		Collection specimenCollection =null;
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();
		
		if (specimenMap!=null && !specimenMap.isEmpty())
		{
			specimenCollection =getReqSpecimens(specimenMap.values()
					,null, collectionProtocolEvent);	
		}
		
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenCollection);
		return collectionProtocolEvent;
	}

	
	/**
	 * creates collection of Specimen domain objects 
	 * @param specimenRequirementBeanColl
	 * @param parentSpecimen
	 * @param cpEvent
	 * @return
	 */
	public static Collection getReqSpecimens(Collection specimenRequirementBeanColl, 
			SpecimenRequirement parentSpecimen, CollectionProtocolEvent cpEvent ) 
	{
		Collection<SpecimenRequirement> reqSpecimenCollection = new LinkedHashSet<SpecimenRequirement>();
		Iterator iterator = specimenRequirementBeanColl.iterator();
		while(iterator.hasNext())
		{
			SpecimenRequirementBean specimenRequirementBean =
				(SpecimenRequirementBean)iterator.next();
			SpecimenRequirement reqSpecimen = getSpecimenDomainObject(specimenRequirementBean);
			reqSpecimen.setParentSpecimen(parentSpecimen);
			if (parentSpecimen == null)
			{
					SpecimenCharacteristics specimenCharacteristics =
							new SpecimenCharacteristics();
					long id =specimenRequirementBean.getSpecimenCharsId();
					if(id != -1)
					{
						specimenCharacteristics.setId(Long.valueOf(id));
					}
					specimenCharacteristics.setTissueSide(
							specimenRequirementBean.getTissueSide());
					specimenCharacteristics.setTissueSite(
							specimenRequirementBean.getTissueSite());
					reqSpecimen.setCollectionProtocolEvent(cpEvent);					
					reqSpecimen.setSpecimenCharacteristics(specimenCharacteristics);
					//Collected and received events
					if(reqSpecimen.getId()==null)
					{
						setSpecimenEvents(reqSpecimen, specimenRequirementBean);
					}
			}
			else
			{
				reqSpecimen.setSpecimenCharacteristics(
						parentSpecimen.getSpecimenCharacteristics());
				//bug no. 7489
				//Collected and received events
				if(specimenRequirementBean.getCollectionEventContainer()!=null && specimenRequirementBean.getReceivedEventReceivedQuality()!=null)
				{
					if(reqSpecimen.getId()==null)
					{
						setSpecimenEvents(reqSpecimen, specimenRequirementBean);
					}
				}
				else
				{
					reqSpecimen.setSpecimenEventCollection(
							parentSpecimen.getSpecimenEventCollection());
				}
			}
			reqSpecimen.setLineage(specimenRequirementBean.getLineage());
			reqSpecimenCollection.add(reqSpecimen);
			Map aliquotColl = (LinkedHashMap)specimenRequirementBean.getAliquotSpecimenCollection();
			Collection childSpecimens = new HashSet();
			if(aliquotColl!=null && !aliquotColl.isEmpty())
			{
				Collection aliquotCollection= specimenRequirementBean.getAliquotSpecimenCollection().values();
				childSpecimens  =  
					getReqSpecimens(aliquotCollection, reqSpecimen, cpEvent);
				reqSpecimenCollection.addAll(childSpecimens);
			}
			Map drivedColl = (LinkedHashMap)specimenRequirementBean.getDeriveSpecimenCollection();
			if(drivedColl!=null && !drivedColl.isEmpty())
			{
				Collection derivedCollection= specimenRequirementBean.getDeriveSpecimenCollection().values();
				
				Collection derivedSpecimens = 
					getReqSpecimens(derivedCollection, reqSpecimen, cpEvent);
				if(childSpecimens == null || childSpecimens.isEmpty())
				{
					childSpecimens = derivedSpecimens;
				}
				else
				{
					childSpecimens.addAll(derivedSpecimens);
				}
				reqSpecimenCollection.addAll(childSpecimens);
			}
			reqSpecimen.setChildSpecimenCollection(childSpecimens);
		}
		return reqSpecimenCollection;
	}

	
	private static  void setSpecimenEvents(SpecimenRequirement reqSpecimen, SpecimenRequirementBean specimenRequirementBean)
	{
		//seting collection event values
		Collection<SpecimenEventParameters> specimenEventCollection = 
			new LinkedHashSet<SpecimenEventParameters>();

		if(specimenRequirementBean.getCollectionEventContainer()!=null)
		{
			CollectionEventParameters collectionEvent = new CollectionEventParameters();
			collectionEvent.setCollectionProcedure(specimenRequirementBean.getCollectionEventCollectionProcedure());
			collectionEvent.setContainer(specimenRequirementBean.getCollectionEventContainer());
			User collectionEventUser = new User();
			collectionEventUser.setId(Long.valueOf(specimenRequirementBean.getCollectionEventUserId()));
			collectionEvent.setUser(collectionEventUser);
			collectionEvent.setSpecimen(reqSpecimen);
			specimenEventCollection.add(collectionEvent);
		}
		
		//setting received event values
		
		if(specimenRequirementBean.getReceivedEventReceivedQuality()!=null)
		{
			ReceivedEventParameters receivedEvent = new ReceivedEventParameters();
			receivedEvent.setReceivedQuality(specimenRequirementBean.getReceivedEventReceivedQuality());
			User receivedEventUser = new User();
			receivedEventUser.setId(Long.valueOf(specimenRequirementBean.getReceivedEventUserId()));
			receivedEvent.setUser(receivedEventUser);
			receivedEvent.setSpecimen(reqSpecimen);
			specimenEventCollection.add(receivedEvent);
		}
		
		reqSpecimen.setSpecimenEventCollection(specimenEventCollection);
	
	}
	/**
	 * creates specimen domain object from given specimen requirement bean.
	 * @param specimenRequirementBean
	 * @return
	 */
	private static SpecimenRequirement getSpecimenDomainObject(SpecimenRequirementBean specimenRequirementBean)
	{
		NewSpecimenForm form = new NewSpecimenForm();
		form.setClassName(specimenRequirementBean.getClassName());
		SpecimenRequirement reqSpecimen=null;
		try
		{
			if(form.getClassName().equals("Tissue"))
        	{
				reqSpecimen = new TissueSpecimenRequirement();
        	}
        	else if(form.getClassName().equals("Fluid"))
        	{
        		reqSpecimen = new FluidSpecimenRequirement();
        	}
        	else if(form.getClassName().equals("Cell"))
        	{
        		reqSpecimen = new CellSpecimenRequirement();
        	}
        	else if(form.getClassName().equals("Molecular"))
        	{
        		reqSpecimen = new MolecularSpecimenRequirement();
        	}
		} 
		catch (Exception e1) 
		{
			logger.error("Error in setting Section header Priorities",e1);
			return null;
		}
		
		if (specimenRequirementBean.getId()==-1)
		{
			reqSpecimen.setId(null);
		}
		else
		{
			reqSpecimen.setId(Long.valueOf(specimenRequirementBean.getId()));
		}
		reqSpecimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		reqSpecimen.setInitialQuantity(new Double(specimenRequirementBean.getQuantity()));
		reqSpecimen.setLineage(specimenRequirementBean.getLineage());
		reqSpecimen.setPathologicalStatus(specimenRequirementBean.getPathologicalStatus());		
		reqSpecimen.setSpecimenType(specimenRequirementBean.getType());
		String storageType = specimenRequirementBean.getStorageContainerForSpecimen();
		if(specimenRequirementBean.getClassName().equalsIgnoreCase(Constants.MOLECULAR))
		{
			((MolecularSpecimenRequirement)reqSpecimen).setConcentrationInMicrogramPerMicroliter(new Double(specimenRequirementBean.getConcentration()));
		}
		reqSpecimen.setStorageType(storageType);
		reqSpecimen.setSpecimenClass(specimenRequirementBean.getClassName());
		return reqSpecimen;
	}
	
	public static CollectionProtocol getCollectionProtocolForSCG(String id) throws ApplicationException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		String sourceObjectName =  SpecimenCollectionGroup.class.getName();
		String[] whereColName = {"id"};
		String[] whereColCond = {"="};
		Object[] whereColVal = {Long.parseLong(id)};
		String [] selectColumnName = {"collectionProtocolRegistration.collectionProtocol"};
		List list = collectionProtocolBizLogic.retrieve(sourceObjectName,selectColumnName,whereColName,whereColCond,whereColVal,Constants.AND_JOIN_CONDITION);
		CollectionProtocol returnVal=null;
		if(list != null && !list.isEmpty())
		{
			CollectionProtocol cp = (CollectionProtocol) list.get(0);
			returnVal= cp;
			
		}
		return returnVal ;
	}
	//bug 8905
	/**
	 * This method is used to sort consents according to id
	 * @param consents Map
	 * @return sorted map
	 */
	public static Map sortConsentMap(Map consentsMap)
	{
		Set keys = consentsMap.keySet();
		List<String> idList = new ArrayList<String>();
		Iterator it = keys.iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			idList.add(key);
		}
		Collections.sort(idList,new IdComparator()); 
		Map idMap = new LinkedHashMap();
		Iterator idIterator = idList.iterator();
		while(idIterator.hasNext())
		{
			String id = (String)idIterator.next();
			idMap.put(id,consentsMap.get(id));
		}
		return idMap;
	}
}
