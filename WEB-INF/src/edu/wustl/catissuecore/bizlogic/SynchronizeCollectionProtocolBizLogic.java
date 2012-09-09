/**
 * <p>Title: InstitutionBizLogic Class>
 * <p>Description:	InstitutionBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author
 *
 */
public class SynchronizeCollectionProtocolBizLogic extends CatissueDefaultBizLogic
{
	DAO dao = null;
	private transient final Logger logger = Logger
			.getCommonLogger(SynchronizeCollectionProtocolBizLogic.class);
	public void synchronizeCP(String title,SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			dao=getHibernateDao(getAppName(),null);
			CollectionProtocol collectionProtocol=getCollectionProtocol(title);
			synchronizeConsents(collectionProtocol,sessionDataBean);
			
			
		} catch (DAOException e)
		{
			throw new BizLogicException(e);
		} 
		finally
		{
			try {
				dao.closeSession();
			} catch (DAOException e) {
				throw new BizLogicException(e);
			}
		}
	}
	private void synchronizeConsents(CollectionProtocol collectionProtocol,SessionDataBean sessionDataBean) throws BizLogicException
	{
		Iterator<Object> cprs = null;
		try
		{
			cprs = getCPRs(collectionProtocol.getId(), dao);
			processCPRs(collectionProtocol,cprs,sessionDataBean);
		}
		catch (DAOException daoExp)
		{
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(),daoExp.getMsgValues());
		}
	}
	private Iterator<Object> getCPRs(Long cpId, DAO dao) throws DAOException
	{
		
		String getCPRQuery = "from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id=:id";
		final ColumnValueBean columnValueBean=new ColumnValueBean(cpId);
		    columnValueBean.setColumnName("id");
		List<ColumnValueBean>  columnValueBeans=new ArrayList();
		columnValueBeans.add(columnValueBean);
		
		return  null;//dao.executeParamHQLIterator(getCPRQuery,columnValueBeans);
	}
	private void processCPRs(CollectionProtocol collectionProtocol,Iterator<Object> registrations,SessionDataBean sessionDataBean) throws DAOException, BizLogicException
	{
		System.out.println("start: "+new Date());
		Collection<ConsentTier> consentTiersInCP = collectionProtocol.getConsentTierCollection();
		int count=0;
		while (registrations.hasNext()) {
			CollectionProtocolRegistration protocolRegistration=(CollectionProtocolRegistration)registrations.next();
			Collection<ConsentTierResponse> consentTierResponsesInCPR=protocolRegistration.getConsentTierResponseCollection();
			Collection consentTierStatusseCollection = new HashSet();
			for (ConsentTier individualConsentTierInCP : consentTiersInCP)
			{
				if(!responseCollectionContainsConsent(consentTierResponsesInCPR,
						individualConsentTierInCP))
				{
					ConsentTierResponse consentResponse = new ConsentTierResponse();
					consentResponse.setConsentTier(individualConsentTierInCP);
					consentResponse.setResponse("Not Specified");
					consentTierResponsesInCPR.add(consentResponse);
					
					ConsentTierStatus consentTierStatus=new ConsentTierStatus();
					consentTierStatus.setConsentTier(individualConsentTierInCP);
					consentTierStatus.setStatus("Not Specified");
					consentTierStatusseCollection.add(consentTierStatus);
					
				}
			}
			
			CollectionProtocolRegistrationBizLogic protocolRegistrationBizLogic=new CollectionProtocolRegistrationBizLogic();
			
			Collection<CollectionProtocolEvent> collectionProtocolEventsTobeCreated=updateSCGs(protocolRegistration, consentTierStatusseCollection,sessionDataBean);
			protocolRegistrationBizLogic.update(dao, protocolRegistration,dao.retrieveById(CollectionProtocolRegistration.class.getName(), protocolRegistration.getId()), sessionDataBean);
			createSpecimencollectionGroups(collectionProtocolEventsTobeCreated,protocolRegistration,sessionDataBean);
			dao.commit();
		}
		System.out.println("Done: "+new Date());
	}
	private boolean responseCollectionContainsConsent(
			Collection<ConsentTierResponse> consentTierResponsesInCPR,
			ConsentTier individualConsentTierInCP) {
		boolean containsConsentire=false;
		for (ConsentTierResponse consentTierResponse : consentTierResponsesInCPR) {
			if(consentTierResponse.getConsentTier()==individualConsentTierInCP)
			{
				containsConsentire=true;
				break;
			}
		}
		return containsConsentire;
	}
	private Collection<CollectionProtocolEvent> updateSCGs(
			CollectionProtocolRegistration protocolRegistration,
			Collection consentTierStatusseCollection,SessionDataBean sessionDataBean) throws DAOException, BizLogicException {
		Collection<CollectionProtocolEvent> collectionProtocolEvents=protocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection();
		Collection<SpecimenCollectionGroup> specimenCollectionGroups=protocolRegistration.getSpecimenCollectionGroupCollection();
		for (SpecimenCollectionGroup specimenCollectionGroup : specimenCollectionGroups) {
			 Collection<ConsentTierStatus> consentTierStatus=specimenCollectionGroup.getConsentTierStatusCollection();
			 consentTierStatus.addAll(consentTierStatusseCollection);
			 collectionProtocolEvents.remove(specimenCollectionGroup.getCollectionProtocolEvent());
		}
		return collectionProtocolEvents;
	}
	private Iterator<SpecimenCollectionGroup> getSCGList(CollectionProtocolRegistration protocolRegistration)
			throws DAOException {
		String getSCG = "from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
			+ " where scg.collectionProtocolRegistration.id=:id"; 
		final ColumnValueBean columnValueBean=new ColumnValueBean(protocolRegistration.getId());
		columnValueBean.setColumnName("id");
		List<ColumnValueBean>  columnValueBeans=new ArrayList();
		columnValueBeans.add(columnValueBean);

		return null;//(Iterator<SpecimenCollectionGroup>)dao.executeParamHQLIterator(getSCG, columnValueBeans);
	}
	
	private CollectionProtocol getCollectionProtocol(String title)
			throws DAOException {
		CollectionProtocol collectionProtocol;
		ColumnValueBean columnValueBean = new ColumnValueBean("title", title);
		List<CollectionProtocol> collectionProtocols = (List<CollectionProtocol>) dao
				.retrieve(CollectionProtocol.class.getName(), columnValueBean);
		collectionProtocol = collectionProtocols.get(0);

		return collectionProtocol;
	}
	private void createSpecimencollectionGroups(Collection<CollectionProtocolEvent> collectionProtocolEvents,CollectionProtocolRegistration collectionProtocolRegistration,SessionDataBean sessionDataBean) throws BizLogicException
	{
		SpecimenCollectionGroupBizLogic collectionGroupBizLogic=new SpecimenCollectionGroupBizLogic();
		for (CollectionProtocolEvent collectionProtocolEvent : collectionProtocolEvents) {
			SpecimenCollectionGroup specimenCollectionGroup=new SpecimenCollectionGroup();
			specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocolEvent);
			CollectionProtocolRegistration newCollectionProtocolRegistration=new CollectionProtocolRegistration();
			newCollectionProtocolRegistration.setId(collectionProtocolRegistration.getId());
			newCollectionProtocolRegistration.setProtocolParticipantIdentifier(collectionProtocolRegistration.getProtocolParticipantIdentifier());
			specimenCollectionGroup.setCollectionProtocolRegistration(newCollectionProtocolRegistration);
			specimenCollectionGroup.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
			specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
			specimenCollectionGroup.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			setChildSpecimenCollection(specimenCollectionGroup,collectionProtocolEvent,sessionDataBean);
			collectionGroupBizLogic.insert(specimenCollectionGroup, dao, sessionDataBean);
		}
		
	}
	private void setChildSpecimenCollection(SpecimenCollectionGroup specimenCollectionGroup,CollectionProtocolEvent collectionProtocolEvent,SessionDataBean sessionDataBean)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		Collection cloneSpecimenCollection = new LinkedHashSet();
		Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
		if(specimenCollection != null && !specimenCollection.isEmpty())
		{
			Iterator itSpecimenCollection = specimenCollection.iterator();
			while(itSpecimenCollection.hasNext())
			{
				SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
				if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
				{
					Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,sessionDataBean.getUserId());
					cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
					cloneSpecimenCollection.add(cloneSpecimen);
				}
			}
		}

		specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);

	}
	private static Specimen getCloneSpecimen(Map<Specimen, List<Specimen>> specimenMap, SpecimenRequirement reqSpecimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup, Long userId)
	{
		Collection childrenSpecimen = new LinkedHashSet<Specimen>();
		Specimen newSpecimen = null;
		try
		{
			newSpecimen = (Specimen) new SpecimenObjectFactory()
				.getDomainObject(reqSpecimen.getClassName(),reqSpecimen);
		}
		catch (AssignDataException e1)
		{
			e1.printStackTrace();
			return null;
		}
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(userId);
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
		newSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		if (newSpecimen.getParentSpecimen()== null)
		{
			specimenMap.put(newSpecimen, new ArrayList<Specimen>());
		}
    	else
    	{
    		specimenMap.put(newSpecimen, null);
    	}

		Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
    	if(childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		{
	    	Iterator<SpecimenRequirement> it = childrenSpecimenCollection.iterator();
	    	while(it.hasNext())
	    	{
	    		SpecimenRequirement childReqSpecimen = it.next();
	    		Specimen newchildSpecimen = getCloneSpecimen(specimenMap, childReqSpecimen,newSpecimen, specimenCollectionGroup, userId);
	    		childrenSpecimen.add(newchildSpecimen);
	    		newSpecimen.setChildSpecimenCollection(childrenSpecimen);
	    	}
		}
    	return newSpecimen;
	}

}

