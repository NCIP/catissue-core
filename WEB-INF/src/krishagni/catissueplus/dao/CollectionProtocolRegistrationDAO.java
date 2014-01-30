package krishagni.catissueplus.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class CollectionProtocolRegistrationDAO
{
    public List<CollectionProtocolRegistrationDTO> getCprDTOListByParticipantID(HibernateDAO hibernateDAO,Long participantID) throws DAOException{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, participantID));
        Collection<?>   cprDetailsList = hibernateDAO.executeNamedQuery("getCPRDetailsByPID", params);
        List<CollectionProtocolRegistrationDTO> cprDTOList = new ArrayList<CollectionProtocolRegistrationDTO>();
        Iterator<?> cprDetailIterator = cprDetailsList.iterator();
        while(cprDetailIterator.hasNext()){
            Object[] valArr = (Object[]) cprDetailIterator.next();
            if (valArr != null)
            {
                CollectionProtocolRegistrationDTO cprObj = new CollectionProtocolRegistrationDTO();
                cprObj.setCprId(Long.valueOf(valArr[1].toString()));
                cprObj.setCpId(Long.valueOf(valArr[2].toString()));
                cprDTOList.add(cprObj);
            }
        }
        
        
        
        return cprDTOList;
    }
    
    public void updateCPRParticipantID(HibernateDAO hibernateDAO,Long cprID,Long participantID) throws DAOException{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG,participantID));
        params.put("2",
                new NamedQueryParam(DBTypes.LONG, cprID));
        try{
            hibernateDAO.executeUpdateWithNamedSQLQuery("updateCPRParticipantID", params);
        }catch(SQLException ex){
             new DAOException(null,ex,null);
        }
    }
    

}
