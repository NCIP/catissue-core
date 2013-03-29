package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.dto.SCGEventPointDTO;
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
	public List<SCGEventPointDTO> getScgEventPoint(DAO dao,Long regId) throws DAOException
	{
		List scgs = null;
		List<SCGEventPointDTO> scgEventDtolist  = new ArrayList<SCGEventPointDTO>();
		try{
		  String hql = "select scg.id,scg.name,cpe.collectionPointLabel,cpe.studyCalendarEventPoint,cpe.id" +
		  		" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg," +
		  		"edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup ascg, " +
		  		"edu.wustl.catissuecore.domain.CollectionProtocolEvent cpe where " +
		  		"scg.collectionProtocolEvent.id = cpe.id and scg.collectionProtocolRegistration.id = ? " +
		  		"and ascg.activityStatus = 'Active' and ascg.id=scg.id";
		  ColumnValueBean columnValueBean=new ColumnValueBean(regId);
	      List<ColumnValueBean>  columnValueBeans=new ArrayList();
	      columnValueBeans.add(columnValueBean);
	      scgs=dao.executeQuery(hql,columnValueBeans);
	      for(Object scgEvent:scgs )
		    {
		        //create SCGEventPointDto
				SCGEventPointDTO sgcEventDto = new SCGEventPointDTO();
				Object[] eventPointData = (Object[]) scgEvent;
				sgcEventDto.setScgId(String.valueOf(eventPointData[0]));
				sgcEventDto.setEventPointLabel(String.valueOf(eventPointData[2]));
				sgcEventDto.setScgName(String.valueOf(eventPointData[1]));
				sgcEventDto.setStudyCalendarEventPoint((Double)eventPointData[3]);
				sgcEventDto.setEventPointId(String.valueOf(eventPointData[4]));
				scgEventDtolist.add(sgcEventDto);
				
		    }
		}
		catch(DAOException daoException)
		{
			  daoException.printStackTrace();
			  throw daoException;
		}
		return scgEventDtolist;	
	}

}
