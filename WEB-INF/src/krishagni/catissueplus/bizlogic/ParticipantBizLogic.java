
package krishagni.catissueplus.bizlogic;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import krishagni.catissueplus.dao.CollectionProtocolDAO;
import krishagni.catissueplus.dao.ParticipantDAO;
import krishagni.catissueplus.dao.SCGDAO;
import krishagni.catissueplus.dao.CollectionProtocolRegistrationDAO;
import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;
import krishagni.catissueplus.dto.ConsentResponseDTO;
import krishagni.catissueplus.dto.ConsentTierDTO;
import krishagni.catissueplus.dto.MedicalIdentifierDTO;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;
import krishagni.catissueplus.dto.ParticipantResponseStatusEnum;
import krishagni.catissueplus.dto.ParticpantResponseDTO;
import krishagni.catissueplus.dao.SiteDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class ParticipantBizLogic
{

    public ParticpantResponseDTO updateParticipantFromDTO(String userName, ParticipantDetailsDTO participantDTO)
            throws SQLException, ApplicationException
    {
        Participant participant = null;
        edu.wustl.catissuecore.bizlogic.ParticipantBizLogic participantBizLogic = new edu.wustl.catissuecore.bizlogic.ParticipantBizLogic();
        ParticipantDAO participantDAO = new ParticipantDAO();
        populateParticipantId(participantDTO);
        ParticpantResponseDTO particpantResponseDTO = new ParticpantResponseDTO();
        if (participantDTO.getId() != null)
        {
            participant = participantDAO.getParticipantById(participantDTO.getId()); // sri:move method to dao
            populateParticipant(participantDTO, participant);
            participantBizLogic.updateParticipant(userName, participant);
            particpantResponseDTO.setParticipantResponseStatusEnum(ParticipantResponseStatusEnum.MODIFIED);
        }
        else
        {
            participant = new Participant();
            populateParticipant(participantDTO, participant);
            participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
            participantBizLogic.registerParticipant(userName, participant);
            particpantResponseDTO.setParticipantResponseStatusEnum(ParticipantResponseStatusEnum.ADDED);
            participantDTO.setId(participant.getId());
        }
        particpantResponseDTO.setParticipantDetailsDTO(participantDTO);
        return particpantResponseDTO;

    }

    public void populateParticipant(ParticipantDetailsDTO participantDTO, Participant participant) throws SQLException,
            ApplicationException
    {
        if (participantDTO.getFirstName() != null)
        {
            participant.setFirstName(participantDTO.getFirstName());
        }
        if (participantDTO.getMiddleName() != null)
        {
            participant.setMiddleName(participantDTO.getMiddleName());
        }
        if (participantDTO.getLastName() != null)
        {
            participant.setLastName(participantDTO.getLastName());
        }
        if (participantDTO.getBirthDate() != null)
        {
            participant.setBirthDate(participantDTO.getBirthDate());
        }
        if (participantDTO.getGender() != null)
        {
            participant.setGender(participantDTO.getGender());
        }
        if (participantDTO.getVitalStatus() != null)
        {
            participant.setVitalStatus(participantDTO.getVitalStatus());
        }

        if (participantDTO.getActivityStatus() != null)
        {
            participant.setActivityStatus(participantDTO.getActivityStatus());
        }

        if (participantDTO.getDeathDate() != null)
        {
            participant.setDeathDate(participantDTO.getDeathDate());
        }
        if (participantDTO.getEthnicity() != null)
        {
            participant.setEthnicity(participantDTO.getEthnicity());
        }

        if (participantDTO.getMedicalIdentifierList() != null && !participantDTO.getMedicalIdentifierList().isEmpty())
        {
            populateMrnList(participantDTO.getMedicalIdentifierList(), participant);
        }

        if (participantDTO.getRaceCollection() != null && !participantDTO.getRaceCollection().isEmpty())
        {
            populateRaceCollection(participantDTO.getRaceCollection(), participant);
        }
        if (participantDTO.getCollectionProtocolRegistrationDTOList() != null
                && !participantDTO.getCollectionProtocolRegistrationDTOList().isEmpty())
        {
            populateCPRList(participantDTO.getCollectionProtocolRegistrationDTOList(), participant);
        }

    }

    private void populateCPRList(List<CollectionProtocolRegistrationDTO> cprDTOList, Participant participant)
            throws SQLException, ApplicationException
    {
        Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant
                .getCollectionProtocolRegistrationCollection();
        for (int i = 0; i < cprDTOList.size(); i++)
        {
            CollectionProtocolRegistrationDTO cprDTOObj = cprDTOList.get(i);
            if (collectionProtocolRegistrationCollection.isEmpty()
                    || !checkAndUpdateCPR(collectionProtocolRegistrationCollection, cprDTOObj))
            {
                collectionProtocolRegistrationCollection.add(getCPRObjet(cprDTOObj, participant));
            }
        }
    }

    private boolean checkAndUpdateCPR(
            Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection,
            CollectionProtocolRegistrationDTO cprDTOObj) throws DAOException, SQLException
    {
        boolean result = false;
        Iterator<CollectionProtocolRegistration> cprItr = collectionProtocolRegistrationCollection.iterator();
        while (cprItr.hasNext())
        {
            CollectionProtocolRegistration cprObj = cprItr.next();
            if ((cprDTOObj.getCpId() != null && cprObj.getCollectionProtocol().getId().compareTo(cprDTOObj.getCpId()) == 0)
                    || (cprObj.getCollectionProtocol().getIrbIdentifier() != null && cprObj.getCollectionProtocol()
                            .getIrbIdentifier().equals(cprDTOObj.getIrbID())))
            {
                cprObj.setRegistrationDate(cprDTOObj.getRegistrationDate());
                cprObj.setConsentSignatureDate(cprDTOObj.getConsentSignatureDate());
                updateConsentTierResponseCollection(cprObj.getConsentTierResponseCollection(), cprDTOObj);
                result = true;
                break;
            }

        }
        return result;

    }

    private void updateConsentTierResponseCollection(Collection consentTierResponseCollection,
            CollectionProtocolRegistrationDTO cprDTOObj) throws DAOException, SQLException
    {
        final Iterator iter = consentTierResponseCollection.iterator();
        while (iter.hasNext())
        {
            final ConsentTierResponse consentTierRespone = (ConsentTierResponse) iter.next();
            List<ConsentResponseDTO> responseDtoList = cprDTOObj.getConsentResponseDTOList();

            for (int j = 0; j < responseDtoList.size(); j++)
            {
                if (responseDtoList.get(j).getConsentStatment()
                        .equals(consentTierRespone.getConsentTier().getStatement()))
                {
                    consentTierRespone.setResponse(responseDtoList.get(j).getParticipantResponses());
                    break;
                }
            }

        }

    }

    private CollectionProtocolRegistration getCPRObjet(CollectionProtocolRegistrationDTO cprDTOObj,
            Participant participant) throws SQLException, ApplicationException
    {
        CollectionProtocol cp = new CollectionProtocol();
        if (cprDTOObj.getCpId() == null)
        {
            cp.setId((new CollectionProtocolDAO()).getCPID(cprDTOObj));
        }
        else
        {
            cp.setId(cprDTOObj.getCpId());
        }
        CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
        cpr.setCollectionProtocol(cp);
        cpr.setRegistrationDate(cprDTOObj.getRegistrationDate());
        cpr.setParticipant(participant);
        cpr.setConsentSignatureDate(cprDTOObj.getConsentSignatureDate());
        List<ConsentTierDTO> consentTierDtoList = (new CollectionProtocolDAO()).getConsentTierDtoFromCpId(cp.getId());
        cpr.setConsentTierResponseCollection(getConsentTierResponseCollection(consentTierDtoList, cprDTOObj));
        return cpr;

    }

    private Collection getConsentTierResponseCollection(List<ConsentTierDTO> consentTierDtoList,
            CollectionProtocolRegistrationDTO cprDtoObj) throws DAOException, SQLException
    {
        Collection consentTierResponseCollection = new HashSet();
        List<ConsentResponseDTO> responseDtoList = cprDtoObj.getConsentResponseDTOList();
        for (int i = 0; i < consentTierDtoList.size(); i++)
        {
            final ConsentTierResponse consentTierResponse = new ConsentTierResponse();
            for (int j = 0; j < responseDtoList.size(); j++)
            {
                if (responseDtoList.get(j).getConsentStatment().equals(consentTierDtoList.get(i).getConsentStatment()))
                {
                    consentTierResponse.setResponse(responseDtoList.get(j).getParticipantResponses());
                    break;
                }
                else
                {
                    consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
                }

            }
            if (consentTierResponse.getResponse() == null)
            {
                consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
            }

            ConsentTier consentTier = new ConsentTier();
            consentTier.setId(consentTierDtoList.get(i).getId());
            consentTierResponse.setConsentTier(consentTier);
            consentTierResponseCollection.add(consentTierResponse);

        }
        return consentTierResponseCollection;

    }

    private void populateRaceCollection(List<String> raceCollection, Participant participant)
    {

        Collection<Race> participantRaceCollection = participant.getRaceCollection();
        for (int i = 0; i < raceCollection.size(); i++)
        {
            if (raceCollection.isEmpty() || !checkForRace(participantRaceCollection, raceCollection.get(i)))
            {
                participantRaceCollection.add(getRaceObject(raceCollection.get(i), participant));
            }
        }
    }

    private boolean checkForRace(Collection raceCollection, String raceValue)
    {
        boolean result = false;
        Iterator raceItr = raceCollection.iterator();
        while (raceItr.hasNext())
        {
            Race raceObj = (Race) raceItr.next();
            result = raceObj.getRaceName().equalsIgnoreCase(raceValue);
            if (result)
            {
                break;
            }
        }
        return result;

    }

    private Race getRaceObject(String raceValue, Participant participant)
    {
        final Race race = new Race();
        race.setRaceName(raceValue);
        race.setParticipant(participant);
        return race;
    }

    public static void init(List<MedicalIdentifierDTO> mrnDTOList, Participant participant)throws Exception{
        ParticipantBizLogic partBizlogic = new ParticipantBizLogic();
        partBizlogic.populateMrnList(mrnDTOList, participant);
    }
    
    public void populateMrnList(List<MedicalIdentifierDTO> mrnDTOList, Participant participant) throws SQLException,
            ApplicationException
    {
        Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = participant
                .getParticipantMedicalIdentifierCollection();
        for (int i = 0; i < mrnDTOList.size(); i++)
        {
            MedicalIdentifierDTO mrnDTOObj = mrnDTOList.get(i);
            if (participantMedicalIdentifierCollection.isEmpty()
                    || !checkAndUpdateMRN(participantMedicalIdentifierCollection, mrnDTOObj))
            {
                participantMedicalIdentifierCollection.add(getParticipantMedicalIdentifier(mrnDTOObj, participant));
            }

        }
        
        Collection<ParticipantMedicalIdentifier> deleteMRNCollection = new LinkedHashSet<ParticipantMedicalIdentifier>();
        Iterator<ParticipantMedicalIdentifier> itr = participantMedicalIdentifierCollection.iterator();
        while (itr.hasNext())
        {
            ParticipantMedicalIdentifier mrnObj = itr.next();
            boolean mrnFoundFlag = false;
            for (int i = 0; i < mrnDTOList.size(); i++)
            {
                if (mrnObj.getId()==null || mrnDTOList.get(i).getSiteName().equals(mrnObj.getSite().getName()))
                {
                    mrnFoundFlag = true;
                    break;
                }
            }
            if (!mrnFoundFlag)
            {
                deleteMRNCollection.add(mrnObj);
            }

        }
        
        participantMedicalIdentifierCollection.removeAll(deleteMRNCollection);
        
    }
   

    private boolean checkAndUpdateMRN(Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection,
            MedicalIdentifierDTO mrnDTOObj)
    {
        boolean result = false;
        Iterator<ParticipantMedicalIdentifier> mrnItr = participantMedicalIdentifierCollection.iterator();
        while (mrnItr.hasNext())
        {
            ParticipantMedicalIdentifier mrnObj = mrnItr.next();
            if ((mrnDTOObj.getSiteId() != null && mrnObj.getSite().getId().compareTo(mrnDTOObj.getSiteId()) == 0)
                    || (mrnObj.getSite()!=null && mrnObj.getSite().getName() != null && mrnObj.getSite().getName()
                            .equals(mrnDTOObj.getSiteName())))
            {
                mrnObj.setMedicalRecordNumber(mrnDTOObj.getMrnValue());
                result = true;
                break;
            }

        }
        
        return result;

    }
    
    public void deleteMRN(Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection,
            List<MedicalIdentifierDTO> mrnDTOList) throws ApplicationException
    {
        HibernateDAO hibernateDAO = null;
        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            
            Collection<ParticipantMedicalIdentifier> deleteMRNCollection = new LinkedHashSet<ParticipantMedicalIdentifier>();
            Iterator<ParticipantMedicalIdentifier> itr = participantMedicalIdentifierCollection.iterator();
            while (itr.hasNext())
            {
                ParticipantMedicalIdentifier mrnObj = itr.next();
                boolean mrnFoundFlag = false;
                for (int i = 0; i < mrnDTOList.size(); i++)
                {
                    if (mrnDTOList.get(i).getSiteName().equals(mrnObj.getSite().getName()))
                    {
                        mrnFoundFlag = true;
                        break;
                    }
                }
                if (!mrnFoundFlag)
                {
                    new ParticipantDAO().deleteMRN(mrnObj, hibernateDAO);
//                    hibernateDAO.delete(mrnObj);
                }

            }
            hibernateDAO.commit();
        }
        finally
        {
            if (hibernateDAO != null)
            {
                AppUtility.closeDAOSession(hibernateDAO);
            }
        }
    }

    private ParticipantMedicalIdentifier getParticipantMedicalIdentifier(MedicalIdentifierDTO mrnDTOObj,
            Participant participant) throws SQLException, ApplicationException
    {
        final ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
        participantMedicalIdentifier.setMedicalRecordNumber(mrnDTOObj.getMrnValue());
        participantMedicalIdentifier.setParticipant(participant);
        Site site = new Site();
        if (mrnDTOObj.getSiteId() == null)
        {
            site.setId((new SiteDAO()).getSiteIdByName(mrnDTOObj.getSiteName()));
            site.setName(mrnDTOObj.getSiteName());
        }
        else
        {
            site.setId(mrnDTOObj.getSiteId());
        }
        participantMedicalIdentifier.setSite(site);
        return participantMedicalIdentifier;
    }

    public boolean hasSpecimen(Long participantId) throws ApplicationException //sri: move to dao
    {

        HibernateDAO hibernateDAO = null;
        boolean result = false;
        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            ParticipantDAO participantDAO = new ParticipantDAO();
            result = participantDAO.hasSpecimen(hibernateDAO, participantId);
        }
        finally
        {
            if (hibernateDAO != null)
            {
                AppUtility.closeDAOSession(hibernateDAO);
            }
        }
        return result;
    }
    
    public void populateParticipantId(ParticipantDetailsDTO participantDetailsDTO) throws ApplicationException
    {
        Long participantId = null;
        if(participantDetailsDTO.getId()!=null){
            return;
        }
        if (participantDetailsDTO.getMedicalIdentifierList() != null
                && !participantDetailsDTO.getMedicalIdentifierList().isEmpty())
        {
            for (int i = 0; i < participantDetailsDTO.getMedicalIdentifierList().size(); i++)
            {
                MedicalIdentifierDTO medicalIdentifierDTO = participantDetailsDTO.getMedicalIdentifierList().get(i);
                participantId = new ParticipantDAO().getParticipantIdFromMRN(medicalIdentifierDTO.getMrnValue(),
                        medicalIdentifierDTO.getSiteName());
                if (participantId != null)
                {
                    break;
                }

            }
            participantDetailsDTO.setId(participantId);
        }
    }
   public void populateCPTitlePPID(ParticipantDetailsDTO participantDetailsDTO) throws ApplicationException{
       HibernateDAO hibernateDAO = null;
       try
       {
           hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
           ParticipantDAO participantDAO = new ParticipantDAO();
           participantDAO.populateCPTitlePPID(hibernateDAO, participantDetailsDTO);
           
       }
       finally
       {
           if (hibernateDAO != null)
           {
               AppUtility.closeDAOSession(hibernateDAO);
           }
       }
   }
   public void deleteParticipantMrn(){
       
   }
}
