
package krishagni.plugin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import krishagni.catissueplus.dto.CollectionProtocolRegistrationDTO;
import krishagni.catissueplus.dto.ConsentResponseDTO;
import krishagni.catissueplus.dto.MedicalIdentifierDTO;
import krishagni.catissueplus.dto.ParticipantDetailsDTO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.MySQLDAOImpl;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class StagingParticipantDAO
{

    private static String selectEpicParticipantCount = "select count(participantId) as count from (select participant.PART_SOURCE_ID as participantId from STAGING_CAT_PARTICIPANT  participant left join STAGING_CATISSUE_PAT_ENROLL cpr on cpr.PART_SOURCE_ID = participant.PART_SOURCE_ID  left join STAGING_CATISSUE_CONSENTS consent on consent.PART_SOURCE_ID = participant.PART_SOURCE_ID  where (participant.UPDATE_FLAG = 1 or consent.UPDATE_FLAG = 1 or cpr.UPDATE_FLAG = 1) group by (participant.PART_SOURCE_ID))  table1";
    private static String selectEpicParticipantDetailsMysql = "select participant.PART_SOURCE_ID,participant. FIRST_NAME,participant.MIDDLE_NAME,participant.LAST_NAME,participant.DATE_OF_BIRTH,participant.GENDER,participant.VITAL_STATUS,participant.DEATH_DATE,participant.STATUS,participant.PAT_UPDATE_DATE,participant.UPDATE_FLAG from STAGING_CATISSUE_PARTICIPANT participant left join STAGING_CATISSUE_PAT_ENROLL cpr on cpr.PART_SOURCE_ID = participant.PART_SOURCE_ID  left join STAGING_CATISSUE_CONSENTS consent on consent.PART_SOURCE_ID = participant.PART_SOURCE_ID  where (participant.UPDATE_FLAG = 1 or consent.UPDATE_FLAG = 1 or cpr.UPDATE_FLAG = 1) group by (participant.PART_SOURCE_ID) order by PART_SOURCE_ID LIMIT ?,? ";
    private static String selectEpicParticipantDetailsOracle = "select * from (select participant.PART_SOURCE_ID,participant.PART_SOURCE,mappinginfo.CATISSUE_PART_ID,history.PART_SOURCE_ID,participant.FIRST_NAME,participant.MIDDLE_NAME,participant.LAST_NAME,participant.DATE_OF_BIRTH,participant.GENDER,participant.VITAL_STATUS,participant.DEATH_DATE,participant.STATUS,participant.PAT_UPDATE_DATE,participant.UPDATE_FLAG from STAGING_CAT_PARTICIPANT participant left join STAGING_EPIC_Identity_ID_HX history on history.NEW_PART_SOURCE_ID = participant.PART_SOURCE_ID and history.PART_SOURCE = participant.PART_SOURCE left join STAGING_PART_INFO_MAPPING mappinginfo on (mappinginfo.SOURCE_PART_ID = participant.PART_SOURCE_ID and mappinginfo.PART_SOURCE = participant.PART_SOURCE) or (mappinginfo.SOURCE_PART_ID = history.PART_SOURCE_ID and mappinginfo.PART_SOURCE = history.PART_SOURCE) left join STAGING_CATISSUE_PAT_ENROLL cpr on cpr.PART_SOURCE_ID = participant.PART_SOURCE_ID  left join STAGING_CATISSUE_CONSENTS consent on consent.PART_SOURCE_ID = participant.PART_SOURCE_ID  where (participant.UPDATE_FLAG = 1 or consent.UPDATE_FLAG = 1 or cpr.UPDATE_FLAG = 1)  group by (participant.PART_SOURCE_ID,participant.FIRST_NAME,participant.MIDDLE_NAME,participant.LAST_NAME,participant.DATE_OF_BIRTH,participant.GENDER,participant.VITAL_STATUS,participant.DEATH_DATE,participant.STATUS,participant.PAT_UPDATE_DATE,participant.UPDATE_FLAG,history.OLD_PART_SOURCE_ID,mappinginfo.CATISSUE_PART_ID,participant.PART_SOURCE) order by participant.PART_SOURCE_ID) table1";
    private static String updateParticipantAuditTable = "insert into STAGING_PARTICIPANT_AUDIT (PART_SOURCE_ID,UPDATED_ON,STATUS,COMMENTS,PART_SOURCE) values(?,?,?,?,?)";
    private static String updateCatissueJobAudit = "insert into STAGING_JOB_AUDIT (RUN_ON,ADDED_COUNT,EDITED_COUNT,FAILED_COUNT) values(?,?,?,?)";
    private static String selectMrnDetails = "select mrn.SITE_NAME,mrn.MRN_VALUE  from STAGING_CATISSUE_MRN mrn"
            + "  where mrn.PART_SOURCE_ID = ? and mrn.PART_SOURCE = ? ";
    private static String selectRaceDetails = "select RACE_VALUE from STAGING_CATISSUE_RACE where PART_SOURCE_ID = ? and PART_SOURCE = ? ";
    private static String selectCprDetail = "select enroll.REGISTRATION_DATE,enroll.CONSENT_DATE,enroll.IRB_ID from  "
            + " STAGING_CATISSUE_PAT_ENROLL enroll  where "
            + " enroll.PART_SOURCE_ID = ? and enroll.PART_SOURCE = ? and enroll.UPDATE_FLAG = 1";
    private static String selectConsentDetails = "select CONSENT_STATEMENT,CONSENT_RESPONSE,IRB_ID  from STAGING_CATISSUE_CONSENTS where  PART_SOURCE_ID = ? and PART_SOURCE = ? and UPDATE_FLAG = 1";
    private static String fetchPatCatIdUpdate = "select PART_SOURCE_ID,OLD_MRN,NEW_MRN from STAGING_EPIC_Identity_ID_HX where  NEW_PART_SOURCE_ID = ?";

    public String getOldStaginParticipantId(Connection conn, String epicParticipantId) throws DAOException,
            SQLException
    {
        final List<ColumnValueBean> auditValueBeansList = new ArrayList<ColumnValueBean>();
        auditValueBeansList.add(new ColumnValueBean(epicParticipantId));
        ResultSet oldIdRS = null;
        String oldId = null;
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(fetchPatCatIdUpdate);
            statement.setString(1, epicParticipantId);
            oldIdRS = statement.executeQuery();

            if (oldIdRS.next())
            {
                oldId = oldIdRS.getString("PART_SOURCE_ID");
            }
        }
        finally
        {
            if (oldIdRS != null)
            {
                oldIdRS.close();
                statement.close();
            }

        }
        return oldId;
    }

    public int getEpicParticipantCount(Connection conn) throws DAOException, SQLException
    {
        ResultSet countRs = null;
        PreparedStatement statement = null;
        try
        {
            String selectEpicParticipantCount = "select count(participantId) as count from (select participant.PART_SOURCE_ID as participantId from STAGING_CATISSUE_PATIENT  participant left join STAGING_CATISSUE_PAT_ENROLL cpr on cpr.PART_SOURCE_ID = participant.PART_SOURCE_ID  left join STAGING_CATISSUE_CONSENTS consent on consent.PART_SOURCE_ID = participant.PART_SOURCE_ID  where (participant.UPDATE_FLAG = 1 or consent.UPDATE_FLAG = 1 or cpr.UPDATE_FLAG = 1) group by (participant.PART_SOURCE_ID))  table1";
            statement = conn.prepareStatement(selectEpicParticipantCount);
            countRs = statement.executeQuery();
            countRs.next();
            return countRs.getInt("count");

        }
        finally
        {
            if (countRs != null)
            {
                countRs.close();
                statement.close();
            }
        }

    }
    
   
    public ResultSet getEpicParticipantDetails(Connection conn) throws DAOException, SQLException
    {
        final List<ColumnValueBean> auditValueBeansList = new ArrayList<ColumnValueBean>();
        PreparedStatement statement = null;
        String selectEpicParticipantDetailsOracle = "select participant.PART_SOURCE_ID,participant.PART_SOURCE,mappinginfo.CATISSUE_PART_ID,history.CHANGE_TYPE,history.PART_SOURCE_ID OLD_PART_SOURCE_ID,participant.FIRST_NAME,participant.MIDDLE_NAME,participant.LAST_NAME,participant.DATE_OF_BIRTH,participant.GENDER,participant.VITAL_STATUS,participant.DEATH_DATE,participant.STATUS,participant.PAT_UPDATE_DATE,participant.UPDATE_FLAG,participant.ETHNICITY from STAGING_CATISSUE_PATIENT participant left join STAGING_PART_ID_HISTORY history on history.NEW_PART_SOURCE_ID = participant.PART_SOURCE_ID and history.PART_SOURCE = participant.PART_SOURCE left join STAGING_PART_INFO_MAPPING mappinginfo on (mappinginfo.PART_SOURCE_ID = participant.PART_SOURCE_ID and mappinginfo.PART_SOURCE = participant.PART_SOURCE) left join STAGING_CATISSUE_PAT_ENROLL cpr on cpr.PART_SOURCE_ID = participant.PART_SOURCE_ID  left join STAGING_CATISSUE_CONSENTS consent on consent.PART_SOURCE_ID = participant.PART_SOURCE_ID  where (participant.UPDATE_FLAG = 1 or consent.UPDATE_FLAG = 1 or cpr.UPDATE_FLAG = 1)  group by (participant.PART_SOURCE_ID,participant.FIRST_NAME,participant.MIDDLE_NAME,participant.LAST_NAME,participant.DATE_OF_BIRTH,participant.GENDER,participant.VITAL_STATUS,participant.DEATH_DATE,participant.STATUS,participant.PAT_UPDATE_DATE,participant.UPDATE_FLAG,history.PART_SOURCE_ID,mappinginfo.CATISSUE_PART_ID,participant.PART_SOURCE,history.CHANGE_TYPE,participant.ETHNICITY) order by participant.PART_SOURCE_ID";
        statement = conn.prepareStatement(selectEpicParticipantDetailsOracle);
        return statement.executeQuery();
    }

    public void updateParticipantAudit(Connection conn,String staginPartIdSource, String epicParticipantId, String successMsg, String errorMsg)
            throws SQLException
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(updateParticipantAuditTable);
            statement.setString(1, epicParticipantId);
            statement.setTimestamp(2,new java.sql.Timestamp(System.currentTimeMillis()));
            statement.setString(3, successMsg);
            statement.setString(4, errorMsg);
            statement.setString(5,staginPartIdSource);
            statement.executeUpdate();
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }

    }

    public void updateCatissueStagingAudit(Connection conn, int added_count, int edited_count, int failed_count)
            throws SQLException
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(updateCatissueJobAudit);
            statement.setTimestamp(1,new java.sql.Timestamp(System.currentTimeMillis()));
            statement.setInt(2, added_count);
            statement.setInt(3, edited_count);
            statement.setInt(4, failed_count);
            statement.executeUpdate();
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }

    }

    public void populateParticipantDetails(Connection conn, String epicParticipantId, String partIdSource, ParticipantDetailsDTO participantDetailsDTO)
            throws DAOException, SQLException
    {
        ResultSet participantDetailsRS = null;
        PreparedStatement statement = null;

        try
        {
            String selectMrnDetails = "select patient.FIRST_NAME, patient.MIDDLE_NAME,patient.LAST_NAME,patient.DATE_OF_BIRTH,patient.GENDER,patient.VITAL_STATUS,patient.DEATH_DATE,patient.ETHNICITY from STAGING_CATISSUE_PATIENT patient"
                    + "  where patient.PART_SOURCE_ID = ? and patient.PART_SOURCE = ? ";
            statement = conn.prepareStatement(selectMrnDetails);
            statement.setString(1, epicParticipantId);
            statement.setString(2, partIdSource);
            participantDetailsRS = statement.executeQuery();
            while (participantDetailsRS.next())
            {
                participantDetailsDTO.setFirstName(participantDetailsRS.getString("FIRST_NAME"));
                participantDetailsDTO.setMiddleName(participantDetailsRS.getString("MIDDLE_NAME"));
                participantDetailsDTO.setLastName(participantDetailsRS.getString("LAST_NAME"));
                participantDetailsDTO.setBirthDate(participantDetailsRS.getDate("DATE_OF_BIRTH"));
                participantDetailsDTO.setGender(participantDetailsRS.getString("GENDER"));
                participantDetailsDTO.setVitalStatus(participantDetailsRS.getString("VITAL_STATUS"));
                participantDetailsDTO.setDeathDate(participantDetailsRS.getDate("DEATH_DATE"));
                participantDetailsDTO.setEthnicity(participantDetailsRS.getString("ETHNICITY"));
            }
        }
        finally
        {
            if (participantDetailsRS != null)
            {
                participantDetailsRS.close();
                statement.close();
            }
        }

        
    }

    public List<MedicalIdentifierDTO> getMrnDetailsDTOList(Connection conn, String epicParticipantId,
            String partIdSource) throws DAOException, SQLException
    {
        final ColumnValueBean valueBean = new ColumnValueBean(epicParticipantId);
        final List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
        valueBeansList.add(valueBean);
        valueBeansList.add(new ColumnValueBean(partIdSource));
        ResultSet mrnRS = null;
        List<MedicalIdentifierDTO> medicalIdentifierDTOList = new ArrayList<MedicalIdentifierDTO>();
        PreparedStatement statement = null;

        try
        {
            String selectMrnDetails = "select mrn.SITE_NAME,mrn.MRN_VALUE  from STAGING_CATISSUE_MRN mrn"
                    + "  where mrn.PART_SOURCE_ID = ? and mrn.PART_SOURCE = ? ";
            statement = conn.prepareStatement(selectMrnDetails);
            statement.setString(1, epicParticipantId);
            statement.setString(2, partIdSource);
            mrnRS = statement.executeQuery();
            while (mrnRS.next())
            {
                MedicalIdentifierDTO medicalIdentifierDTO = new MedicalIdentifierDTO();
                medicalIdentifierDTO.setMrnValue(mrnRS.getString("MRN_VALUE"));
                medicalIdentifierDTO.setSiteName(mrnRS.getString("SITE_NAME"));
                medicalIdentifierDTOList.add(medicalIdentifierDTO);

            }
        }
        finally
        {
            if (mrnRS != null)
            {
                mrnRS.close();
                statement.close();
            }
        }

        return medicalIdentifierDTOList;
    }

    public List<String> getRaceList(Connection conn, String epicParticipantId, String partIdSource)
            throws SQLException, DAOException
    {
        final ColumnValueBean valueBean = new ColumnValueBean(epicParticipantId);
        final List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
        valueBeansList.add(valueBean);
        valueBeansList.add(new ColumnValueBean(partIdSource));
        ResultSet raceRS = null;
        List<String> raceList = new ArrayList<String>();
        List<MedicalIdentifierDTO> medicalIdentifierDTOList = new ArrayList<MedicalIdentifierDTO>();
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(selectRaceDetails);
            statement.setString(1, epicParticipantId);
            statement.setString(2, partIdSource);
            raceRS = statement.executeQuery();
            while (raceRS.next())
            {
                raceList.add(raceRS.getString("RACE_VALUE"));
            }
        }
        finally
        {
            if (raceRS != null)
            {
                raceRS.close();
                statement.close();
            }
        }

        return raceList;
    }

    public List<CollectionProtocolRegistrationDTO> getCPRDetailDTOList(Connection conn, String staginParticipantId,
            String partIdSource) throws DAOException, SQLException
    {
        final List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
        valueBeansList.add(new ColumnValueBean(staginParticipantId));
        valueBeansList.add(new ColumnValueBean(partIdSource));

        ResultSet cpDetailsRS = null;
        List<CollectionProtocolRegistrationDTO> cprDTOList = new ArrayList<CollectionProtocolRegistrationDTO>();
        PreparedStatement statement = null;

        try
        {
            statement = conn.prepareStatement(selectCprDetail);
            statement.setString(1, staginParticipantId);
            statement.setString(2, partIdSource);

            cpDetailsRS = statement.executeQuery();
            while (cpDetailsRS.next())
            {
                CollectionProtocolRegistrationDTO collectionProtocolRegistrationDTO = new CollectionProtocolRegistrationDTO();
                collectionProtocolRegistrationDTO.setIrbID(cpDetailsRS.getString("IRB_ID"));
                collectionProtocolRegistrationDTO.setRegistrationDate(cpDetailsRS.getDate("REGISTRATION_DATE"));
                collectionProtocolRegistrationDTO.setConsentSignatureDate(cpDetailsRS.getDate("CONSENT_DATE"));
                collectionProtocolRegistrationDTO.setConsentResponseDTOList(getConsentDetails(conn,
                        staginParticipantId, partIdSource, cpDetailsRS.getString("IRB_ID")));
                cprDTOList.add(collectionProtocolRegistrationDTO);
            }

        }
        finally
        {
            if (cpDetailsRS != null)
            {
                cpDetailsRS.close();
                statement.close();
            }

        }
        return cprDTOList;
    }

    private ArrayList<ConsentResponseDTO> getConsentDetails(Connection conn, String stagingParticipantId,
            String partIdSource, String irbId) throws DAOException, SQLException
    {
        ResultSet consentDetailsRs = null;
        final List<ColumnValueBean> valueBeansListForConsent = new ArrayList<ColumnValueBean>();
        valueBeansListForConsent.add(new ColumnValueBean(stagingParticipantId));
        valueBeansListForConsent.add(new ColumnValueBean(partIdSource));
        PreparedStatement statement = null;
        try
        {
            ArrayList<ConsentResponseDTO> consentTierDTOList = new ArrayList<ConsentResponseDTO>();
            statement = conn.prepareStatement(selectConsentDetails);
            statement.setString(1, stagingParticipantId);
            statement.setString(2, partIdSource);
            consentDetailsRs = statement.executeQuery();
            while (consentDetailsRs.next())
            {
                if (consentDetailsRs.getString("IRB_ID").equals(irbId))
                {
                    ConsentResponseDTO consentTierDTO = new ConsentResponseDTO();
                    consentTierDTO.setConsentStatment(consentDetailsRs.getString("CONSENT_STATEMENT"));
                    consentTierDTO.setParticipantResponses(consentDetailsRs.getString("CONSENT_RESPONSE"));
                    consentTierDTOList.add(consentTierDTO);
                }
            }
            return consentTierDTOList;
        }
        finally
        {
            if (consentDetailsRs != null)
            {
                consentDetailsRs.close();
                statement.close();
            }

        }
    }

    public void updateStagingPartMapping(Connection conn, Long catissuePartID, String sourcePartID, String parrtSource)
            throws DAOException, SQLException
    {
        String updateStagingPartMappingTable = "delete from STAGING_PART_INFO_MAPPING where PART_SOURCE_ID = ? and PART_SOURCE = ? ";
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(updateStagingPartMappingTable);
            statement.setString(1, sourcePartID);
            statement.setString(2, parrtSource);
            statement.executeUpdate();
            insertStagingPartMapping(conn, catissuePartID, sourcePartID, parrtSource);
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }

    }

    public void insertStagingPartMapping(Connection conn, Long catissuePartID, String sourcePartID, String parrtSource)
            throws DAOException, SQLException
    {
        String updateStagingPartMappingTable = "insert into STAGING_PART_INFO_MAPPING ( CATISSUE_PART_ID , PART_SOURCE_ID , PART_SOURCE) values (?,?,?)";
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(updateStagingPartMappingTable);
            statement.setLong(1, catissuePartID);
            statement.setString(2, sourcePartID);
            statement.setString(3, parrtSource);
            statement.executeUpdate();
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }

    }

    public Long getCatissuePartId(Connection conn, String sourcePartID, String parrtSource) throws DAOException,
            SQLException
    {

        ResultSet catissueIdRS = null;
        PreparedStatement statement = null;
        Long catissueId = null;

        try
        {
            String selectMrnDetails = "select CATISSUE_PART_ID  from STAGING_PART_INFO_MAPPING "
                    + "  where PART_SOURCE_ID = ? and PART_SOURCE = ? ";
            statement = conn.prepareStatement(selectMrnDetails);
            statement.setString(1, sourcePartID);
            statement.setString(2, parrtSource);
            catissueIdRS = statement.executeQuery();
            if (catissueIdRS.next())
            {
                catissueId = catissueIdRS.getLong("CATISSUE_PART_ID");
            }
        }
        finally
        {
            if (catissueIdRS != null)
            {
                catissueIdRS.close();
                statement.close();
            }
        }

        return catissueId;

    }

}
