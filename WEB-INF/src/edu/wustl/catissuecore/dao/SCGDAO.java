
package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.SCGEventPointDTO;
import edu.wustl.catissuecore.dto.SCGSummaryDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class SCGDAO
{
	private static final Logger LOGGER = Logger.getCommonLogger(SCGDAO.class);
	/** Get event Point and scg associated with given registration id.
	 * @param regId
	 * @return
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	public List<SCGEventPointDTO> getScgEventPoint(DAO dao, Long regId) throws DAOException
	{
		List scgs = null;
		List<SCGEventPointDTO> scgEventDtolist = new ArrayList<SCGEventPointDTO>();
		String hql = "select scg.id,scg.name,cpe.collectionPointLabel,cpe.studyCalendarEventPoint,cpe.id"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg,"
				+ "edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup ascg, "
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent cpe where "
				+ "scg.collectionProtocolEvent.id = cpe.id and scg.collectionProtocolRegistration.id = ? "
				+ "and ascg.activityStatus = 'Active' and ascg.id=scg.id order by cpe.collectionPointLabel";
		ColumnValueBean columnValueBean = new ColumnValueBean(regId);
		List<ColumnValueBean> columnValueBeans = new ArrayList();
		columnValueBeans.add(columnValueBean);
		scgs = dao.executeQuery(hql, columnValueBeans);
		for (Object scgEvent : scgs)
		{
			//create SCGEventPointDto
			SCGEventPointDTO sgcEventDto = new SCGEventPointDTO();
			Object[] eventPointData = (Object[]) scgEvent;
			sgcEventDto.setScgId(String.valueOf(eventPointData[0]));
			sgcEventDto.setEventPointLabel(String.valueOf(eventPointData[2]));
			sgcEventDto.setScgName(String.valueOf(eventPointData[1]));
			sgcEventDto.setStudyCalendarEventPoint((Double) eventPointData[3]);
			sgcEventDto.setEventPointId(String.valueOf(eventPointData[4]));
			scgEventDtolist.add(sgcEventDto);

		}

		return scgEventDtolist;
	}

	public SCGSummaryDTO getScgSummary(DAO dao, Long scgId) throws DAOException
	{
		SCGSummaryDTO scgSummDto = new SCGSummaryDTO();
				
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG,scgId));
		List scgs =((HibernateDAO) dao).executeNamedQuery("getScgSummary", params);
		
		for (Object scg : scgs)
		{
			Object[] scgData = (Object[]) scg;
			scgSummDto.setScgName((String) scgData[0]);
			scgSummDto.setSite((Long) scgData[1]);	
			scgSummDto.setScgId(scgId);
			scgSummDto.setCollectionStatus((String)scgData[2]);
			scgSummDto.setEventId((Long)scgData[3]);
			scgSummDto.setCollectedDate((Date) scgData[4]);
			scgSummDto.setCollector((Long) scgData[5]);
			scgSummDto.setReceivedDate((Date) scgData[6]);
			scgSummDto.setReceiver((Long) scgData[7]);
		}

//		List receivedEvent = ((HibernateDAO) dao).executeNamedQuery("receiveEventParam", params);
//		final Calendar cal = Calendar.getInstance();
//		if (!receivedEvent.isEmpty())
//		{
//			for (Object recEvent : receivedEvent)
//			{
//				Object[] recEventData = (Object[]) recEvent;
//				scgSummDto.setReceivedDate((Date) recEventData[0]);
//				scgSummDto.setReceiver((Long) recEventData[1]);
//			}
//		}
//
//		List collectedEvent = ((HibernateDAO) dao).executeNamedQuery("collectEventParam", params);
//		if (!collectedEvent.isEmpty())
//		{
//			for (Object colEvent : collectedEvent)
//			{
//				Object[] colEventData = (Object[]) colEvent;
//				scgSummDto.setCollectedDate((Date) colEventData[0]);
//				scgSummDto.setCollector((Long) colEventData[1]);
//			}
//		}

		return scgSummDto;
	}

	public Long getEventDefaultSite(Long eventId,HibernateDAO dao) throws DAOException
	{
		Long defSite= null;
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG,eventId));
		List site = dao.executeNamedQuery("getDefaultSite", params);
		
		for(Object siteId :site)
		{
			defSite = (Long)siteId;
		}
		return defSite;
	}

	/** This method updates the scg field submitted from anticipatory specimen page.
	 * @param dao
	 * @param summaryDto
	 * @throws DAOException
	 */
	public void saveSCGSummary(DAO dao, SCGSummaryDTO summaryDto) throws DAOException
	{

		Object object;
		object = dao.retrieveById(SpecimenCollectionGroup.class.getName(), summaryDto.getScgId());

		SpecimenCollectionGroup persistentSCG = null;
		if (object != null)
		{
			persistentSCG = (SpecimenCollectionGroup) object;

			if (summaryDto.getScgName() != null)
			{//needToHandle error if empty
				persistentSCG.setName(summaryDto.getScgName());
			}

			if (summaryDto.getSite() != null)
			{ //set Site
				Site site = new Site();
				site.setId(summaryDto.getSite());
				persistentSCG.setSpecimenCollectionSite(site);
			}
			if(summaryDto.getCollectionStatus() != null)
			{
				persistentSCG.setCollectionStatus(summaryDto.getCollectionStatus());
			}
		
			Collection<SpecimenEventParameters> eventParams = persistentSCG
					.getSpecimenEventParametersCollection();

			if (eventParams.isEmpty())
			{
				ReceivedEventParameters receiveEvent = new ReceivedEventParameters();
				setReceivedEventParameter(summaryDto, receiveEvent);
				receiveEvent.setSpecimenCollectionGroup(persistentSCG);
				CollectionEventParameters collEvent = new CollectionEventParameters();
				setCollectedEventParam(summaryDto, collEvent);
				collEvent.setSpecimenCollectionGroup(persistentSCG);
				persistentSCG.getSpecimenEventParametersCollection().add(receiveEvent);
				persistentSCG.getSpecimenEventParametersCollection().add(collEvent);
			}
			else
			{

				for (SpecimenEventParameters eventParameter : eventParams)
				{
					if (eventParameter instanceof ReceivedEventParameters)
					{
						setReceivedEventParameter(summaryDto,(ReceivedEventParameters) eventParameter);
					}
					if (eventParameter instanceof CollectionEventParameters)
					{
						setCollectedEventParam(summaryDto, (CollectionEventParameters) eventParameter);
					}
				}
			}
		

			dao.update(persistentSCG);
		}

	}

	private void setCollectedEventParam(SCGSummaryDTO summaryDto,
			CollectionEventParameters eventParameter)
	{
		if (summaryDto.getCollector() != null)
		{
			User collector = new User();
			collector.setId(summaryDto.getCollector());
			eventParameter.setUser(collector);
		}
		if (summaryDto.getCollectedDate() != null)
		{
			eventParameter.setTimestamp(summaryDto.getCollectedDate());
		}
		if(eventParameter.getCollectionProcedure() == null){
			eventParameter.setCollectionProcedure(Constants.CP_DEFAULT);
		}

		if(eventParameter.getContainer() == null){
			eventParameter.setContainer(Constants.CP_DEFAULT);
		}
	}

	private void setReceivedEventParameter(SCGSummaryDTO summaryDto,
			ReceivedEventParameters eventParameter)
	{
		if (summaryDto.getReceiver() != null)
		{
			User receiver = new User();
			receiver.setId(summaryDto.getReceiver());
			eventParameter.setUser(receiver);
		}
		if (summaryDto.getReceivedDate() != null)
		{
			eventParameter.setTimestamp(summaryDto.getReceivedDate());
		}
		if(eventParameter.getReceivedQuality()==null){
			eventParameter.setReceivedQuality(Constants.CP_DEFAULT);
		}
		
	}
	
	public List<NameValueBean> getSCGNameList(HibernateDAO hibernateDao, Long registrationId) throws DAOException
	{
		List<NameValueBean> returnList = new ArrayList<NameValueBean>();
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG, registrationId));
			
			List result = hibernateDao.executeNamedQuery("getSCGNameIdPairList", params);
			Object[] obj;
			for (Object object : result)
			{
				obj = (Object[])object;
				NameValueBean bean = new NameValueBean(obj[1],obj[0]);
				returnList.add(bean);
			}
		return returnList;
	}

	public List<NameValueBean> getEventLabelsList(HibernateDAO hibernateDao, Long cpId) throws DAOException
	{
		List<NameValueBean> returnList = new ArrayList<NameValueBean>();
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, cpId));
		
		List result = hibernateDao.executeNamedQuery("getCPENameIdPairList", params);
		Object[] obj;
		for (Object object : result)
		{
			obj = (Object[])object;
			NameValueBean bean = new NameValueBean(obj[0]+"("+obj[1]+")",obj[2]);
			returnList.add(bean);
		}
		return returnList;
	}

	public List<NameValueBean> getSGCFromCPE(HibernateDAO hibernateDAO, Long cpeId, Long cprId) throws DAOException
	{
		
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, cpeId));
		params.put("1", new NamedQueryParam(DBTypes.LONG, cprId));
		
		List result = hibernateDAO.executeNamedQuery("getSCGListFromCPE", params);
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		for (Object object : result)
		{
			Object[] res = (Object[])object;
			beans.add(new NameValueBean(res[1].toString(),Long.valueOf(res[0].toString())));
		}
		// TODO Auto-generated method stub
		return beans;
	}

	public Long getCPEIdFromSCGId(HibernateDAO hibernateDAO, Long scgId) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, scgId));
		List result = hibernateDAO.executeNamedQuery("getScgSummary", params);
		if(result != null && result.size() != 0 )
		{
			Object[] obj = (Object[])result.get(0);
			return Long.valueOf(obj[3].toString());
		}
		return null;
	}

	public List<NameValueBean> getspecimenLabelsList(HibernateDAO hibernateDao, Long registrationId) throws DAOException
	{
		String hql = "select specimen.id,specimen.label from edu.wustl.catissuecore.domain.Specimen as specimen where specimen.specimenCollectionGroup.collectionProtocolRegistration.id = ? and specimen.collectionStatus='Collected' and specimen.activityStatus = 'Active' and specimen.label is not null order by specimen.label";
		ColumnValueBean bean = new ColumnValueBean(registrationId);
		List list = hibernateDao.executeQuery(hql, Arrays.asList(bean));
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		if(list != null)
		{
			for (Object object : list)
			{
				Object[] obj = (Object[]) object;
				beans.add(new NameValueBean(obj[1].toString(), Long.valueOf(obj[0].toString())));
			}
		}
		return beans;
	}

	public List<NameValueBean> getSpecimenFromCPE(HibernateDAO hibernateDAO, Long cpId, Long registrationId) throws DAOException
	{
		String hql = "select specimen.id,specimen.label from edu.wustl.catissuecore.domain.Specimen as specimen where specimen.specimenCollectionGroup.collectionProtocolRegistration.id = ? and specimen.specimenCollectionGroup.collectionProtocolEvent.id = ? and specimen.collectionStatus='Collected' and specimen.activityStatus = 'Active' and specimen.label is not null order by specimen.label";
		List<ColumnValueBean> valueBeans = new ArrayList<ColumnValueBean>();
		valueBeans.add(new ColumnValueBean(registrationId));
		valueBeans.add(new ColumnValueBean(cpId));
		List list = hibernateDAO.executeQuery(hql, valueBeans);
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		if(list != null)
		{
			for (Object object : list)
			{
				Object[] obj = (Object[]) object;
				beans.add(new NameValueBean(obj[1].toString(), Long.valueOf(obj[0].toString())));
			}
		}
		return beans;
	}
	
	public List<SpecimenEventParameters> getSCGEvents(SpecimenCollectionGroup specimenCollGroup) throws ApplicationException
	{
		final String hql = "select  scg.collectionTimestamp, scg.collector.id, scg.collectionComments, scg.collectionProcedure, scg.collectionContainer, " +
				"scg.receivedQuality, scg.receivedTimestamp, scg.receiver.id, scg.receivedComments from "
				+ SpecimenCollectionGroup.class.getName()
				+ " as scg where scg.id= "
				+ specimenCollGroup.getId().toString();

		final List scgEventList = AppUtility.executeQuery(hql);
		if(scgEventList != null && !scgEventList.isEmpty())
		{
			Object[] obj = (Object[])scgEventList.get(0);
			CollectionEventParameters collEvent = new CollectionEventParameters();
			if(obj[0] != null)
			{
				collEvent.setTimestamp((Date)obj[0]);
			}
			if(obj[1] != null)
			{
				User collector = new User();
				collector.setId(Long.valueOf(obj[1].toString()));
				collEvent.setUser(collector);
			}
			if(obj[2] != null)
			{
				collEvent.setComment(obj[2].toString());
			}
			if(obj[3] != null)
			{
				collEvent.setCollectionProcedure(obj[3].toString());
			}
			if(obj[4] != null)
			{
				collEvent.setContainer(obj[4].toString());
			}
			ReceivedEventParameters recEvent = new ReceivedEventParameters();
			if(obj[5] != null)
			{
				recEvent.setReceivedQuality(obj[5].toString());
			}
			if(obj[6] != null)
			{
				recEvent.setTimestamp((Date)obj[6]);
			}
			if(obj[7] != null)
			{
				User receiver = new User();
				receiver.setId(Long.valueOf(obj[7].toString()));
				recEvent.setUser(receiver);
			}
			if(obj[8] != null)
			{
				recEvent.setComment(obj[8].toString());
			}
			List<SpecimenEventParameters> events = new ArrayList<SpecimenEventParameters>();
			events.add(recEvent);
			events.add(collEvent);
			return events;
		}
			return null;
	}

}
