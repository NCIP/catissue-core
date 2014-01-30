package krishagni.catissueplus.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;
import krishagni.catissueplus.dto.ConsentTierDTO;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class CollectionProtocolDAO
{
    public List<ConsentTierDTO> getConsentTierDtoFromCpId(Long cpId) throws ApplicationException
    {
        HibernateDAO hibernateDAO  = null;
        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
            params.put("0", new NamedQueryParam(DBTypes.LONG, cpId));
            List consentTierDetail = hibernateDAO.executeNamedQuery("getConsentTierDetails",
                    params);
            List<ConsentTierDTO> consentTierDTOList = new ArrayList<ConsentTierDTO>();
            for (int i = 0; i < consentTierDetail.size(); i++)
            {
                Object[] objectArrya = (Object[]) consentTierDetail.get(i);
                ConsentTierDTO consentTierDTO = new ConsentTierDTO();
                consentTierDTO.setId(Long.parseLong(objectArrya[0].toString()));
                consentTierDTO.setConsentStatment(objectArrya[1].toString());
                consentTierDTOList.add(consentTierDTO);
            }
            return consentTierDTOList;
        }
        catch (BizLogicException daoException)
        {
            throw new BizLogicException(ErrorKey.getErrorKey("biz.disableobj.error"), null, null);
        }
        finally
        {
            AppUtility.closeDAOSession(hibernateDAO);
        }
    }

    public Long getCPID(CollectionProtocolRegistrationDTO cprDTOObj) throws ApplicationException{
        HibernateDAO hibernateDao = null;
        Long cpID = null;
        try{
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession(null);
            if(cprDTOObj.getIrbID()!=null){
                cpID = getCPIDFromIRBID(hibernateDao,cprDTOObj.getIrbID());
            }
        }finally{
            AppUtility.closeDAOSession(hibernateDao);
        }
        return cpID;
    }
    public Long getCPIDFromIRBID(HibernateDAO hibernateDao, String irbID) throws ApplicationException
    {
        Long cpID = null;
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, irbID));
        Collection resultColl = hibernateDao.executeNamedQuery("selectCPIDfromIRBID",
                params);
        Iterator ite = resultColl.iterator();
        if (ite.hasNext())
        {
            cpID = (Long) ite.next();

        }
       
        return cpID;
    }
}
