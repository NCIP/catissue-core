
package krishagni.catissueplus.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;
import krishagni.catissueplus.dto.MedicalIdentifierDTO;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class ParticipantDAO
{

    public Long getParticipantIdFromMRN(String mrnValue, String siteName, HibernateDAO hibernateDao)
            throws ApplicationException
    {
        Long participantId = null;
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, mrnValue));
        params.put("1", new NamedQueryParam(DBTypes.STRING, siteName));
        Collection consentResponse = hibernateDao.executeNamedQuery("selectParticipantIdFromMrnAndSite", params);
        Iterator ite = consentResponse.iterator();
        if (ite.hasNext())
        {
            participantId = (Long) ite.next();

        }

        return participantId;
    }

    public void deleteMRN(ParticipantMedicalIdentifier mrnObj, HibernateDAO hibernateDao) throws ApplicationException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, mrnObj.getMedicalRecordNumber()));
        params.put("1", new NamedQueryParam(DBTypes.STRING, mrnObj.getSite().getName()));
        Collection mrnCollection = hibernateDao.executeNamedQuery("selectMRNFromMrnAndSite", params);
        Iterator ite = mrnCollection.iterator();
        while (ite.hasNext())
        {
            ParticipantMedicalIdentifier deleteObj = (ParticipantMedicalIdentifier) ite.next();
            hibernateDao.delete(mrnObj);

        }
        //        selectMRNFromMrnAndSite
    }

    public Long getParticipantIdFromMRN(String mrnValue, String siteName) throws ApplicationException

    {
        HibernateDAO hibernateDao = null;
        try
        {
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession(null);
            return getParticipantIdFromMRN(mrnValue, siteName, hibernateDao);
        }
        finally
        {
            AppUtility.closeDAOSession(hibernateDao);
        }

    }

    public void updateParticipantActivitStatus(HibernateDAO hibernateDAO, Long participantID, String status)
            throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.STRING, status));
        params.put("2", new NamedQueryParam(DBTypes.LONG, participantID));
        try
        {
            hibernateDAO.executeUpdateWithNamedSQLQuery("updateParticipantActivityStatus", params);
        }
        catch (SQLException ex)
        {
            new DAOException(null, ex, null);
        }

    }

    public boolean hasSpecimen(HibernateDAO hibernateDAO, Long participantId) throws DAOException
    {
        boolean result = false;

        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, participantId));

        List<Integer> resultList = hibernateDAO.executeNamedQuery("getParticipantSpecimenCount", params);

        if (resultList != null && !resultList.isEmpty())
        {
            result = resultList.get(0) > 0 ? true : false;
        }
        return result;

    }

    public Participant getParticipantById(Long id) throws ApplicationException
    {
        HibernateDAO hibernateDao = null;
        Participant participant = null;
        try
        {
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession(null);
            participant = (Participant) hibernateDao.retrieveById(Participant.class.getName(), id);
        }
        finally
        {
            AppUtility.closeDAOSession(hibernateDao);
        }
        return participant;
    }

    public void populateCPTitlePPID(HibernateDAO hibernateDAO, ParticipantDetailsDTO participantDetailsDTO)
            throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, participantDetailsDTO.getId()));
        Collection<?> cprDetailsList =hibernateDAO.executeNamedQuery("getCPTiltePPIDfromPID", params);
        List<CollectionProtocolRegistrationDTO> cprDTOList = new ArrayList<CollectionProtocolRegistrationDTO>();
        Iterator<?> cprDetailIterator = cprDetailsList.iterator();
        String ppidString = "";
        String cpTtileString = "";

        while (cprDetailIterator.hasNext())
        {
            Object[] valArr = (Object[]) cprDetailIterator.next();
            ppidString += valArr[0].toString();
            cpTtileString += valArr[1].toString();
            if (cprDetailIterator.hasNext())
            {
                ppidString += ",";
                cpTtileString += ",";

            }

        }
        participantDetailsDTO.setPpidsString(ppidString);
        participantDetailsDTO.setCpTitles(cpTtileString);

    }
}
