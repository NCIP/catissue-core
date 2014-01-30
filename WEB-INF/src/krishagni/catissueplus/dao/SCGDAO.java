package krishagni.catissueplus.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class SCGDAO
{

	public SpecimenCollectionGroup getSCG(Long specimenCollectionGroupId, HibernateDAO hibernateDao)
			throws DAOException
	{
		Object obj = hibernateDao.retrieveById(SpecimenCollectionGroup.class.getName(), specimenCollectionGroupId);
		if (obj == null)
		{
			throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
					SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}
		return (SpecimenCollectionGroup) obj;
	}

	public Long getCPID(String specimenCollectionGroupName,HibernateDAO hibernateDao) throws DAOException 
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, specimenCollectionGroupName==null?"":specimenCollectionGroupName));
		List cpIdCollection = hibernateDao.executeNamedQuery("getCPIdFromSCGName", params);
		if (cpIdCollection != null && cpIdCollection.size() > 0)
		{
			return Long.valueOf(cpIdCollection.get(0).toString());
		}
		return null;
	}

	public Long getCPID(Long specimenCollectionGroupId,HibernateDAO hibernateDao) throws DAOException 
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, specimenCollectionGroupId==null?0l:specimenCollectionGroupId));
		List cpIdCollection = hibernateDao.executeNamedQuery("getCPIdFromSCGId", params);
		if (cpIdCollection != null && cpIdCollection.size() > 0)
		{
			return Long.valueOf(cpIdCollection.get(0).toString());
		}
		return null;
	}
	
	public List<Long> getSCGIDListFromCPRID(Long cprID,HibernateDAO hibernateDao) throws DAOException 
	{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, cprID));
        List<Long> cpIdCollection = hibernateDao.executeNamedQuery("getCPIdFromSCGId", params);
        
        return cpIdCollection;
    }
	
	public void updateScgWithCprID(HibernateDAO hibernateDAO,Long cprID,Long scgID) throws DAOException{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG,cprID));
        params.put("2",
                new NamedQueryParam(DBTypes.LONG, scgID));
        try{
            hibernateDAO.executeUpdateWithNamedSQLQuery("updateCPRParticipantID", params);
        }catch(SQLException ex){
             new DAOException(null,ex,null);
        }
    }
    
}
