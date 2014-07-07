
package krishagni.plugin.scheduler;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.bizlogic.ParticipantBizLogic;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;
import krishagni.catissueplus.dto.ParticpantResponseDTO;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.plugin.dao.StagingParticipantDAO;
import krishagni.plugin.dto.EpicMergeFailedDTO;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.EmailClient;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.velocity.VelocityManager;
import edu.wustl.dao.exception.DAOException;

public class StagingParticipantRegistration
{

    public static void main(String arg[])
    {
        init();
    }

    private StagingParticipantDAO epicParticipantDAO = new StagingParticipantDAO();
    private String adminUserName = XMLPropertyHandler.getValue("user.name");;
    private static final Logger LOGGER = Logger.getCommonLogger(StagingParticipantRegistration.class);
    private static int added_count;
    private static int edited_count;
    private static int failed_count;
    private final String CHANGE_TYPE_DELETED = "DELETED";
    private final String CHANGE_TYPE_MERGE = "MERGE";
    private final String CHANGE_TYPE_UNMERGE = "UNMERGE";
    private final String CHANGE_TYPE_CHANGE = "CHANGE";
    private final String MESSAGE_PARTICIPANT_ADDED = "Participant Added";
    private final String MESSAGE_PARTICIPANT_UPDATED = "Participant UPDATED";
    private final String MESSAGE_MRN_DELETED = "Participant MRN DELETED";
    private final String MESSAGE_PARTICIPANT_FAILED = "Failed";

    private Connection initConnection() throws SQLException
    {
        String url = "jdbc:oracle:thin:@" + XMLPropertyHandler.getValue("staging.database.server") + ":"
                + XMLPropertyHandler.getValue("staging.database.port") + ":"
                + XMLPropertyHandler.getValue("staging.database.name");
        String user = XMLPropertyHandler.getValue("staging.database.username");
        String password = XMLPropertyHandler.getValue("staging.database.password");
        Connection conn = null;
        conn = java.sql.DriverManager.getConnection(url, user, password);
        return conn;

    }

    public static void init()
    {
        StagingParticipantRegistration epicParticipantRegistration = new StagingParticipantRegistration();
        Connection conn = null;
        try
        {
            conn = epicParticipantRegistration.initConnection();
            epicParticipantRegistration.saveParticipant(conn);
        }
        catch (Exception ex)
        {
            LOGGER.error(ex);
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                LOGGER.error(e.getMessage(), e);
            }
            try
            {
                epicParticipantRegistration.sendEpicJobCompletionEmail(added_count, edited_count, failed_count);
                //       AppUtility.closeDAOSession(dao);
            }
            catch (Exception ex)
            {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

    }

    public void saveParticipant(Connection conn) throws Exception
    {

        added_count = 0;
        edited_count = 0;
        failed_count = 0;
        String successMsg = "";
        ResultSet epicParticipantRs = epicParticipantDAO.getEpicParticipantDetails(conn);
        ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
        List<EpicMergeFailedDTO> failedMergeRecordList = new ArrayList<EpicMergeFailedDTO>();
        while (epicParticipantRs.next())
        {
            String staginParticipantId = epicParticipantRs.getString("PART_SOURCE_ID");
            String staginPartIdSource = epicParticipantRs.getString("PART_SOURCE");
            String errorComment = null;
            ParticipantDetailsDTO participantDetailsDTO = new ParticipantDetailsDTO();
            try
            {
                Long participantId = epicParticipantRs.getObject("CATISSUE_PART_ID") != null ? epicParticipantRs
                        .getLong("CATISSUE_PART_ID") : null;
                participantDetailsDTO.setId(participantId);
                String changeType = epicParticipantRs.getString("CHANGE_TYPE");
                if ((CHANGE_TYPE_MERGE.equals(changeType) || CHANGE_TYPE_CHANGE.equals(changeType)
                        || CHANGE_TYPE_UNMERGE.equals(changeType))
                        && !staginParticipantId.equals(epicParticipantRs.getString("OLD_PART_SOURCE_ID")))
                {
                    successMsg = mergeParticipant(conn, epicParticipantRs, participantDetailsDTO, staginParticipantId,
                            staginPartIdSource, changeType, failedMergeRecordList);

                }
                else
                {
                    successMsg = updateParticipant(conn, epicParticipantRs, participantDetailsDTO, staginParticipantId,
                            staginPartIdSource, changeType);
                }
            }
            catch (ApplicationException ex)
            {
                successMsg = MESSAGE_PARTICIPANT_FAILED;
                LOGGER.error(ex.getMessage(), ex);
                errorComment = CommonUtil.getErrorMessage(ex, new Participant(), "Creating/Updating");
                failed_count++;

            }
            catch (Exception ex)
            {
                successMsg = MESSAGE_PARTICIPANT_FAILED;
                LOGGER.error(ex.getMessage(), ex);
                errorComment = ex.getMessage();
                failed_count++;
            }
            epicParticipantDAO.updateParticipantAudit(conn,staginPartIdSource, staginParticipantId, successMsg, errorComment);
            conn.commit();
        }
        epicParticipantRs.close();
        epicParticipantDAO.updateCatissueStagingAudit(conn, added_count, edited_count, failed_count);
        conn.commit();
        if (!failedMergeRecordList.isEmpty())
        {
            sendEpicFaildMergeMail(failedMergeRecordList);
        }
    }

    private String updateParticipant(Connection conn, ResultSet epicParticipantRs,
            ParticipantDetailsDTO participantDetailsDTO, String staginParticipantId, String staginPartIdSource,
            String changeType) throws SQLException, ApplicationException
    {
        String successMessage = "";
        populateParticipantDetailsDTO(conn, participantDetailsDTO, staginParticipantId, staginPartIdSource,
                epicParticipantRs);

        ParticpantResponseDTO particpantResponseDTO = new ParticipantBizLogic().updateParticipantFromDTO(adminUserName,
                participantDetailsDTO);
        successMessage = updateMappingTable(conn, particpantResponseDTO, staginParticipantId, staginPartIdSource);

        return successMessage;

    }

    /* private String deleteMRN(Connection conn, ResultSet epicParticipantRs,
             ParticipantDetailsDTO participantDetailsDTO, String staginParticipantId, String staginPartIdSource,
             String changeType) throws SQLException, ApplicationException
     {
         String successMessage = "";
         populateParticipantDetailsDTO(conn, participantDetailsDTO, staginParticipantId, staginPartIdSource,epicParticipantRs);
           
         ParticpantResponseDTO particpantResponseDTO = new ParticipantBizLogic().deleteMRN(participantDetailsDTO);
         successMessage = updateMappingTable(conn, particpantResponseDTO, staginParticipantId, staginPartIdSource);

         return successMessage;

     }*/
    private String mergeParticipant(Connection conn, ResultSet epicParticipantRs,
            ParticipantDetailsDTO participantDetailsDTO, String staginParticipantId, String staginPartIdSource,
            String changeType, List<EpicMergeFailedDTO> failedMergeRecordList) throws SQLException,
            ApplicationException
    {
        populateParticipantDetailsDTO(conn, participantDetailsDTO, staginParticipantId, staginPartIdSource,
                epicParticipantRs);
        setParticipantDetails(epicParticipantRs, participantDetailsDTO);
        ParticipantDetailsDTO oldParticipantDetailsDTO = new ParticipantDetailsDTO();
        oldParticipantDetailsDTO.setId(epicParticipantDAO.getCatissuePartId(conn,
                epicParticipantRs.getString("OLD_PART_SOURCE_ID"), staginPartIdSource));
        populateParticipantDetailsDTO(conn, oldParticipantDetailsDTO,
                epicParticipantRs.getString("OLD_PART_SOURCE_ID"), staginPartIdSource, null);
        ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
        String successMessage;
        participantBizLogic.populateParticipantId(participantDetailsDTO);
        participantBizLogic.populateParticipantId(oldParticipantDetailsDTO);
        if ((oldParticipantDetailsDTO.getId() != null
                && participantBizLogic.hasSpecimen(oldParticipantDetailsDTO.getId()))
                || (participantDetailsDTO.getId() != null && participantBizLogic.hasSpecimen(participantDetailsDTO
                        .getId())))
        {
            EpicMergeFailedDTO epicMergeFailedDTO = new EpicMergeFailedDTO();
            if(oldParticipantDetailsDTO.getId()!= null){
                participantBizLogic.populateCPTitlePPID(oldParticipantDetailsDTO);
            }
            if(participantDetailsDTO.getId()!=null){
                participantBizLogic.populateCPTitlePPID(participantDetailsDTO);
            }
            epicMergeFailedDTO.setOldPartSourceID(epicParticipantRs.getString("OLD_PART_SOURCE_ID"));
            epicMergeFailedDTO.setPartSourceID(staginParticipantId);
            epicMergeFailedDTO.setOldParticipantDetailsDTO(oldParticipantDetailsDTO);
            epicMergeFailedDTO.setParticipantDetailsDTO(participantDetailsDTO);
            epicMergeFailedDTO.setChangeType(changeType);
            epicMergeFailedDTO.setPartSource(staginPartIdSource);
            failedMergeRecordList.add(epicMergeFailedDTO);
            successMessage = "Failed because participant has collected specimen";
            failed_count++;
        }
        else
        {
            ParticpantResponseDTO particpantResponseDTO = participantBizLogic.updateParticipantFromDTO(adminUserName,
                    participantDetailsDTO);
            if ((CHANGE_TYPE_MERGE.equals(changeType) || CHANGE_TYPE_CHANGE.equals(changeType)))
            {
                oldParticipantDetailsDTO.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
            }

            participantBizLogic.updateParticipantFromDTO(adminUserName, oldParticipantDetailsDTO);
            successMessage = updateMappingTable(conn, particpantResponseDTO, staginParticipantId, staginPartIdSource);
        }
        return successMessage;

    }

    private String updateMappingTable(Connection conn, ParticpantResponseDTO particpantResponseDTO,
            String staginParticipantId, String staginPartIdSource) throws DAOException, SQLException
    {
        String successMessage = "";
        switch (particpantResponseDTO.getParticipantResponseStatusEnum())
        {
            case ADDED :
                epicParticipantDAO.insertStagingPartMapping(conn, particpantResponseDTO.getParticipantDetailsDTO()
                        .getId(), staginParticipantId, staginPartIdSource);
                successMessage = MESSAGE_PARTICIPANT_ADDED;
                added_count++;
                break;
            case MODIFIED :
                epicParticipantDAO.updateStagingPartMapping(conn, particpantResponseDTO.getParticipantDetailsDTO()
                        .getId(), staginParticipantId, staginPartIdSource);
                successMessage = MESSAGE_PARTICIPANT_UPDATED;
                edited_count++;
                break;
            case MRNDELETED :
                epicParticipantDAO.updateStagingPartMapping(conn, particpantResponseDTO.getParticipantDetailsDTO()
                        .getId(), staginParticipantId, staginPartIdSource);
                successMessage = MESSAGE_MRN_DELETED;
                edited_count++;
                break;

        }
        return successMessage;

    }

    public void populateParticipantDetailsDTO(Connection conn, ParticipantDetailsDTO participantDetailsDTO,
            String staginParticipantId, String staginPartIdSource, ResultSet epicParticipantRs) throws DAOException,
            SQLException
    {
        participantDetailsDTO.setCollectionProtocolRegistrationDTOList(epicParticipantDAO.getCPRDetailDTOList(conn,
                staginParticipantId, staginPartIdSource));
        participantDetailsDTO.setRaceCollection(epicParticipantDAO.getRaceList(conn, staginParticipantId,
                staginPartIdSource));
        participantDetailsDTO.setMedicalIdentifierList(epicParticipantDAO.getMrnDetailsDTOList(conn,
                staginParticipantId, staginPartIdSource));
        if (epicParticipantRs != null)
        {
            setParticipantDetails(epicParticipantRs, participantDetailsDTO);
        }
        else
        {
            epicParticipantDAO.populateParticipantDetails(conn, staginParticipantId, staginPartIdSource,
                    participantDetailsDTO);
        }
    }

    public void setParticipantDetails(ResultSet epicParticiapntRs, ParticipantDetailsDTO participantDetailsDTO)
            throws SQLException
    {

        participantDetailsDTO.setFirstName(epicParticiapntRs.getString("FIRST_NAME"));
        participantDetailsDTO.setMiddleName(epicParticiapntRs.getString("MIDDLE_NAME"));
        participantDetailsDTO.setLastName(epicParticiapntRs.getString("LAST_NAME"));
        participantDetailsDTO.setBirthDate(epicParticiapntRs.getDate("DATE_OF_BIRTH"));
        participantDetailsDTO.setGender(epicParticiapntRs.getString("GENDER"));
        participantDetailsDTO.setVitalStatus(epicParticiapntRs.getString("VITAL_STATUS"));
        participantDetailsDTO.setDeathDate(epicParticiapntRs.getDate("DEATH_DATE"));
        participantDetailsDTO.setEthnicity(epicParticiapntRs.getString("ETHNICITY"));

    }

    public void sendEpicFaildMergeMail(List<EpicMergeFailedDTO> failedMergeRecordList) throws Exception
    {
        Map<String, Object> failedMergeMap = new HashMap<String, Object>();
        failedMergeMap.put("epicMergeFailedDTOList", failedMergeRecordList);
        List<File> attachmentOrderCsv = new ArrayList<File>();
        File csvFile = null;
        String obj = VelocityManager.getInstance().evaluate(failedMergeMap, "EpicMergeFailed.vm");
        String currentDate = CommonUtilities.parseDateToString(new Date(), CommonServiceLocator.getInstance()
                .getDatePattern());
        csvFile = new File("FailedMergeRecord_" + currentDate + Constants.CSV_FILE_EXTENTION);
        csvFile.createNewFile();
        FileOutputStream out = new FileOutputStream(csvFile);
        out.write(obj.getBytes());
        out.close();
        attachmentOrderCsv.add(csvFile);
        String[] toEmailId = XMLPropertyHandler.getValue("epic.to.email.address").split(",");
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("currentDate", currentDate);
        contextMap.put("appUrl", CommonServiceLocator.getInstance().getAppURL());

        boolean emailStatus = EmailClient.getInstance().sendEmailWithAttachment("epic.mergeParticipantFailedTemplate",
                toEmailId, null, null, attachmentOrderCsv, contextMap, currentDate);
        if (!emailStatus)
        {
            Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")
                    + XMLPropertyHandler.getValue("email.administrative.emailAddress"));
        }

    }

    public boolean sendEpicJobCompletionEmail(int added_count, int edited_count, int failed_count)
    {

        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("addedCount", added_count);
        contextMap.put("editedCount", edited_count);
        contextMap.put("jobDate",
                CommonUtilities.parseDateToString(new Date(), CommonServiceLocator.getInstance().getDatePattern()));
        contextMap.put("failedCount", failed_count);
        contextMap.put("appUrl", CommonServiceLocator.getInstance().getAppURL());
        String[] toEmailId = XMLPropertyHandler.getValue("epic.to.email.address").split(",");

        boolean emailStatus = EmailClient.getInstance().sendEmail("epic.participantIntegrationJobTemplate", toEmailId,
                null, null, contextMap,
                CommonUtilities.parseDateToString(new Date(), CommonServiceLocator.getInstance().getDatePattern()));

        if (!emailStatus)
        {
            Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")
                    + XMLPropertyHandler.getValue("email.administrative.emailAddress"));
        }
        return emailStatus;
    }
}