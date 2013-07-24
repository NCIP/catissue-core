package krishagni.catissueplus.dao;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;


public class SCGDAO
{

	public SpecimenCollectionGroup getSCG(Long specimenCollectionGroupId, HibernateDAO hibernateDao)
			throws DAOException
	{
		SpecimenCollectionGroup collectionGroup = new SpecimenCollectionGroup();
		Object obj = hibernateDao.retrieveById(SpecimenCollectionGroup.class.getName(), specimenCollectionGroupId);
		if (obj == null)
		{
			throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
					SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}
		return (SpecimenCollectionGroup) obj;
	}
}
