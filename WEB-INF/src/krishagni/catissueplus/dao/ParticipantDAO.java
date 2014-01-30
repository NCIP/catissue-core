package krishagni.catissueplus.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import krishagni.catissueplus.dto.ParticipantDetailsDTO;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class ParticipantDAO
{
    public Long getParticipantIdFromMRN(String mrnValue, String siteName,HibernateDAO hibernateDao) throws ApplicationException
    {
        Long participantId = null;
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, mrnValue));
        params.put("1", new NamedQueryParam(DBTypes.STRING, siteName));
        Collection consentResponse = hibernateDao.executeNamedQuery("selectParticipantIdFromMrnAndSite",
                params);
        Iterator ite = consentResponse.iterator();
        if (ite.hasNext())
        {
            participantId = (Long) ite.next();

        }
       
        return participantId;
    }
    
    public Long getParticipantIdFromMRN(String mrnValue, String siteName) throws ApplicationException
            
    {
        HibernateDAO hibernateDao = null;
        try{
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession(null);
            return getParticipantIdFromMRN(mrnValue,siteName,hibernateDao);
        }finally{
            AppUtility.closeDAOSession(hibernateDao);
        }
        
        
    }
    
    public void populateParticipantId(ParticipantDetailsDTO participantDetailsDTO) throws ApplicationException{
        if(participantDetailsDTO.getLookUpMRNNumber()!=null && participantDetailsDTO.getLookUpSiteName()!=null){
            participantDetailsDTO.setId(getParticipantIdFromMRN(participantDetailsDTO.getLookUpMRNNumber(),participantDetailsDTO.getLookUpSiteName()));
        }
    }
    
    public void updateParticipantActivitStatus(HibernateDAO hibernateDAO,Long participantID,String status) throws DAOException{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.STRING,status));
        params.put("2",
                new NamedQueryParam(DBTypes.LONG, participantID));
        try{
            hibernateDAO.executeUpdateWithNamedSQLQuery("updateParticipantActivityStatus", params);
        }catch(SQLException ex){
             new DAOException(null,ex,null);
        }

    }

}
