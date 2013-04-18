
package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.SCGEventPointDTO;
import edu.wustl.catissuecore.dto.SCGSummaryDTO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class SCGDAO
{

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

		String hql = "select scg.name ,scg.specimenCollectionSite.id,scg.collectionStatus"
				+ " from edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup as ascg,edu.wustl.catissuecore.domain.SpecimenCollectionGroup "
				+ "as scg where ascg.id=scg.id and ascg.activityStatus = 'Active' and scg.id = ?";

		String rercEventhql = "select sep.timestamp,sep.user.id from edu.wustl.catissuecore.domain.SpecimenEventParameters as sep,"
				+ "edu.wustl.catissuecore.domain.ReceivedEventParameters rep where sep.id=rep.id and sep.specimenCollectionGroup.id = ?";

		String collectedEventhql = "select sep.timestamp,sep.user.id from edu.wustl.catissuecore.domain.SpecimenEventParameters as sep,"
				+ "edu.wustl.catissuecore.domain.CollectionEventParameters cep where sep.id=cep.id and sep.specimenCollectionGroup.id = ?";

		//  String receivedEvntHql = 

		ColumnValueBean columnValueBean = new ColumnValueBean(scgId);
		List<ColumnValueBean> columnValueBeans = new ArrayList();
		columnValueBeans.add(columnValueBean);

		List scgs = dao.executeQuery(hql, columnValueBeans);
		for (Object scg : scgs)
		{
			Object[] scgData = (Object[]) scg;
			scgSummDto.setScgName((String) scgData[0]);
			scgSummDto.setSite((Long) scgData[1]);
			scgSummDto.setScgId(scgId);
			scgSummDto.setCollectionStatus((String)scgData[2]);
		}

		List receivedEvent = dao.executeQuery(rercEventhql, columnValueBeans);
		final Calendar cal = Calendar.getInstance();
		if (receivedEvent.isEmpty())
		{
			scgSummDto.setReceivedDate(cal.getTime()); //set current date
		}
		else
		{
			for (Object recEvent : receivedEvent)
			{
				Object[] recEventData = (Object[]) recEvent;
				scgSummDto.setReceivedDate((Date) recEventData[0]);
				scgSummDto.setReceiver((Long) recEventData[1]);
			}
		}

		List collectedEvent = dao.executeQuery(collectedEventhql, columnValueBeans);
		if (collectedEvent.isEmpty())
		{
			scgSummDto.setCollectedDate(cal.getTime()); //set current date
		}
		else
		{
			for (Object colEvent : collectedEvent)
			{
				Object[] colEventData = (Object[]) colEvent;
				scgSummDto.setCollectedDate((Date) colEventData[0]);
				scgSummDto.setCollector((Long) colEventData[1]);
			}
		}

		return scgSummDto;
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
						setReceivedEventParameter(summaryDto, eventParameter);
					}
					if (eventParameter instanceof CollectionEventParameters)
					{
						setCollectedEventParam(summaryDto, eventParameter);
					}
				}
			}
			dao.update(persistentSCG);
		}

	}

	private void setCollectedEventParam(SCGSummaryDTO summaryDto,
			SpecimenEventParameters eventParameter)
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
	}

	private void setReceivedEventParameter(SCGSummaryDTO summaryDto,
			SpecimenEventParameters eventParameter)
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
	}

}
