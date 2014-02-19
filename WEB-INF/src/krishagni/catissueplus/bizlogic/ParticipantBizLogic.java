
package krishagni.catissueplus.bizlogic;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import krishagni.catissueplus.dao.CollectionProtocolDAO;
import krishagni.catissueplus.dao.ParticipantDAO;
import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;
import krishagni.catissueplus.dto.ConsentResponseDTO;
import krishagni.catissueplus.dto.ConsentTierDTO;
import krishagni.catissueplus.dto.MedicalIdentifierDTO;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;
import krishagni.catissueplus.dto.ParticpantResponseDTO;
import krishagni.catissueplus.dto.ParticpantResponseDTO.StatusEnum;
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
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class ParticipantBizLogic
{

    public ParticpantResponseDTO registerParticipantFromDTO(String userName, ParticipantDetailsDTO participantDTO) throws SQLException,
            ApplicationException
    {
        Participant participant = null;
        edu.wustl.catissuecore.bizlogic.ParticipantBizLogic participantBizLogic = new edu.wustl.catissuecore.bizlogic.ParticipantBizLogic();
        ParticipantDAO participantDAO = new ParticipantDAO();
        participantDAO.populateParticipantId(participantDTO);
        ParticpantResponseDTO particpantResponseDTO = new ParticpantResponseDTO();
        if (participantDTO.getId() != null)
        {
            participant = getParticipantById(participantDTO.getId());
            populateParticipant(participantDTO, participant);
            participantBizLogic.updateParticipant(userName, participant);
            particpantResponseDTO.setStatusEnum(StatusEnum.MODIFIED);
        }
        else
        {
            participant = new Participant();
            populateParticipant(participantDTO, participant);
            participantBizLogic.registerParticipant(userName, participant);
            particpantResponseDTO.setStatusEnum(StatusEnum.ADDED);
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
        if(participantDTO.getEthnicity()!=null)
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
        if(cprDTOObj.getCpId()==null){
            cp.setId((new CollectionProtocolDAO()).getCPID(cprDTOObj));
        }else{
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
            if(consentTierResponse.getResponse()==null){
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

    public void populateMrnList(List<MedicalIdentifierDTO> mrnDTOList, Participant participant) throws SQLException, ApplicationException
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

    }

    private boolean checkAndUpdateMRN(Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection,
            MedicalIdentifierDTO mrnDTOObj)
    {
        boolean result = false;
        Iterator<ParticipantMedicalIdentifier> mrnItr = participantMedicalIdentifierCollection.iterator();
        while (mrnItr.hasNext())
        {
            ParticipantMedicalIdentifier mrnObj = mrnItr.next();
            if ((mrnDTOObj.getSiteId()!=null && mrnObj.getSite().getId().compareTo(mrnDTOObj.getSiteId()) == 0)
                    || (mrnObj.getSite().getName()!=null && mrnObj.getSite().getName().equals(mrnDTOObj.getSiteName())))
            {
                mrnObj.setMedicalRecordNumber(mrnDTOObj.getMrnValue());
                result = true;
                break;
            }

        }
        return result;

    }

    private ParticipantMedicalIdentifier getParticipantMedicalIdentifier(MedicalIdentifierDTO mrnDTOObj,
            Participant participant) throws SQLException, ApplicationException
    {
        final ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
        participantMedicalIdentifier.setMedicalRecordNumber(mrnDTOObj.getMrnValue());
        participantMedicalIdentifier.setParticipant(participant);
        Site site = new Site();
        if(mrnDTOObj.getSiteId()==null){
            site.setId((new SiteDAO()).getSiteIdByName(mrnDTOObj.getSiteName()));
        }else{
            site.setId(mrnDTOObj.getSiteId());
        }
        participantMedicalIdentifier.setSite(site);
        return participantMedicalIdentifier;
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
    
  /*  public void mergeParticipant(HibernateDAO hibernateDAO,Long primaryParticipantID, Long secondaryParticipantID) throws DAOException{
        CollectionProtocolRegistrationDAO cprDAO = new  CollectionProtocolRegistrationDAO();
        List<CollectionProtocolRegistrationDTO> primaryPartCPRDTOList = cprDAO.getCprDTOListByParticipantID(hibernateDAO, primaryParticipantID);
        List<CollectionProtocolRegistrationDTO> secondaryPartCPRDTOList = cprDAO.getCprDTOListByParticipantID(hibernateDAO, secondaryParticipantID);
        
        for(int i=0;i<secondaryPartCPRDTOList.size();i++){
            CollectionProtocolRegistrationDTO secCPRObj = secondaryPartCPRDTOList.get(i);
            CollectionProtocolRegistrationDTO priCPRObj = getCPRFromSameCP(primaryPartCPRDTOList,secCPRObj);
            if(priCPRObj!=null){
                mergeSCGOfCPR(hibernateDAO,priCPRObj.getCprId(),secCPRObj.getCprId());
            }else{
                Long cprID = secondaryPartCPRDTOList.get(i).getCprId();
                cprDAO.updateCPRParticipantID(hibernateDAO,cprID,primaryParticipantID);
            }
        }
        ParticipantDAO participantDAO = new ParticipantDAO();
        participantDAO.updateParticipantActivitStatus(hibernateDAO, secondaryParticipantID, Constants.ACTIVITY_STATUS_CLOSED);
        
    }
    
    public void mergeSCGOfCPR(HibernateDAO hibernateDAO,Long primaryCprId,Long secondaryCprId) throws DAOException{
        SCGDAO scgDAO = new  SCGDAO();
        List<Long> scgIDList = scgDAO.getSCGIDListFromCPRID(secondaryCprId,hibernateDAO);
        for(int i = 0 ;i< scgIDList.size();i++){
            scgDAO.updateScgWithCprID(hibernateDAO,primaryCprId,scgIDList.get(i));
        }        
    }
    public CollectionProtocolRegistrationDTO getCPRFromSameCP(List<CollectionProtocolRegistrationDTO> cprDTOList,CollectionProtocolRegistrationDTO cprDTO){
        for(int i=0; i < cprDTOList.size();i++){
           CollectionProtocolRegistrationDTO priCPRObj = cprDTOList.get(i);
           if(priCPRObj.getCprId().compareTo(cprDTO.getCprId())==0 && priCPRObj.getCpId().compareTo(cprDTO.getCpId())==0){
               return priCPRObj;
           }
        }
        return null;
    }
    
    
    public static void executeMethod(){
        
        HibernateDAO hibernateDAO = null;

        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            ParticipantBizLogic bizlogic = new ParticipantBizLogic();
            bizlogic.mergeParticipant(hibernateDAO,1l,2l);
        }catch(Exception ex){
            
        }
    }*/
}
