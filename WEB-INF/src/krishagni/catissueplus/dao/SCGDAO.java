package krishagni.catissueplus.dao;

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
}
