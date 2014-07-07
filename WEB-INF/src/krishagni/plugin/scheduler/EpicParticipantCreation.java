
package krishagni.plugin.scheduler;

import java.util.Date;

import krishagni.plugin.dao.StagingParticipantDAO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

public class EpicParticipantCreation
{

    public static void main(String arg[])
    {
        init();
    }

    private StagingParticipantDAO epicParticipantDAO = new StagingParticipantDAO();
    //Mosin : I will set values of below attribute after reading it from property files
    private String epicSiteName = "Epic_Site";
    private String adminUserName = "admin@admin.com";

    public static void init()
    {
        JDBCDAO dao = null;

        try
        {
            dao = AppUtility.openJDBCSession();
            EpicParticipantCreation epicParticipantRegistration = new EpicParticipantCreation();
//            epicParticipantRegistration.saveParticipant(dao);
        }
        catch (ApplicationException e)
        {
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                AppUtility.closeDAOSession(dao);
            }
            catch (ApplicationException e)
            {
            }
        }

    }

    //Below method is for creating dummy data i will delete this after testing

    public void makeDatabaseEntry(JDBCDAO jdbcDao) throws DAOException
    {
        System.out.println("");
        for (int i = 1; i < 3; i++)
        {

            String insertParticipant = " insert into STAGING_CATISSUE_PARTICIPANT values( 'epic_Test_ForUpdate_"
                    + i
                    + "','epic_Test_ForUpdate_First_"
                    + i
                    + "','epic_Test_ForUpdate_Middel_"
                    + i
                    + "','epic_Test_ForUpdate_Last_"
                    + i
                    + "',STR_TO_DATE('07-05-1986', '%d-%m-%Y'),'Not Reported ','Male Gender','Alive' ,null,'Active', STR_TO_DATE('06-12-2013', '%d-%m-%Y'))";
            String insertParticipantEnroll = "insert into STAGING_CATISSUE_PAT_ENROLL values ('epic_Test_ForUpdate_" + i
                    + "','Irb_002', STR_TO_DATE('01-12-2013', '%d-%m-%Y'),'')";
            String insertRace = "insert into STAGING_CATISSUE_RACE values ('epic_Test_ForUpdate_" + i + "','White')";
            String insertMrn = "insert into STAGING_CATISSUE_MRN values('epic_Test_ForUpdate_" + i
                    + "','ATCC','epic_Test_ForUpdate_" + i + "');";
            String insertConsent = "insert into STAGING_CATISSUE_CONSENTS values ('epic_Test_ForUpdate_"
                    + i
                    + "','Consented to their specimen samples being kept for use in research to learn about, prevent, treat, or cure cancer','Yes','Irb_002')";
             
            jdbcDao.executeUpdate(insertParticipant);
            jdbcDao.executeUpdate(insertParticipantEnroll);
            jdbcDao.executeUpdate(insertRace);
            jdbcDao.executeUpdate(insertMrn);
            jdbcDao.executeUpdate(insertConsent);
        }
        // jdbcDao.commit();

    }
    
    public static void init1()
    {
        JDBCDAO dao = null;

        try
        {
            System.out.println(new Date());
            dao = AppUtility.openJDBCSession();
            EpicParticipantCreation epicParticipantRegistration = new EpicParticipantCreation();

            epicParticipantRegistration.makeDatabaseEntry(dao);
            dao.commit();

        }
        catch (ApplicationException e)
        {
        }
        /* catch (SQLException e)
         {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }*/
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            System.out.println(new Date());
            try
            {
                AppUtility.closeDAOSession(dao);
            }
            catch (ApplicationException e)
            {
            }
        }

    }
}


/*
 create table STAGING_CATISSUE_PARTICIPANT ( CAT_PAT_ID  VARCHAR(250), FIRST_NAME  VARCHAR(250),MIDDLE_NAME VARCHAR(250),LAST_NAME VARCHAR(250),DATE_OF_BIRTH DATE,ETHNICITY VARCHAR(250),GENDER VARCHAR(250),VITAL_STATUS  VARCHAR(250),DEATH_DATE DATE,STATUS   VARCHAR(250),PAT_UPDATE_DATE  DATE,UPDATE_FLAG   TINYINT(1), PRIMARY KEY (CAT_PAT_ID)
 );
CREATE INDEX CAT_PAT_ID_IDX ON STAGING_CATISSUE_PARTICIPANT (CAT_PAT_ID);


create table STAGING_CATISSUE_PAT_ENROLL (CAT_PAT_ID VARCHAR(250),IRB_ID VARCHAR(250),REGISTRATION_DATE  VARCHAR(250),RSH_PROJECT VARCHAR(250),CONSENT_DATE DATE,UPDATE_FLAG TINYINT(1), FOREIGN KEY (CAT_PAT_ID) REFERENCES STAGING_CATISSUE_PARTICIPANT(CAT_PAT_ID));

CREATE INDEX CATISSUE_PAT_ENROLL_PAT_ID_IDX ON STAGING_CATISSUE_PAT_ENROLL (CAT_PAT_ID);
CREATE INDEX CATISSUE_PAT_ENROLL_IRB_ID_IDX ON STAGING_CATISSUE_PAT_ENROLL (IRB_ID);

create table STAGING_CATISSUE_RACE (CAT_PAT_ID VARCHAR(250),RACE_VALUE VARCHAR(250), FOREIGN KEY (CAT_PAT_ID) REFERENCES STAGING_CATISSUE_PARTICIPANT(CAT_PAT_ID));
CREATE INDEX CATISSUE_RACE_PAT_ID_IDX ON STAGING_CATISSUE_RACE (CAT_PAT_ID);

create table STAGING_CATISSUE_MRN (CAT_PAT_ID VARCHAR(250),SITE_NAME VARCHAR(250),MRN_VALUE VARCHAR(250), FOREIGN KEY (CAT_PAT_ID) REFERENCES STAGING_CATISSUE_PARTICIPANT(CAT_PAT_ID));
CREATE INDEX CATISSUE_MRN_PAT_ID_IDX ON STAGING_CATISSUE_MRN (CAT_PAT_ID);

create table STAGING_CATISSUE_CONSENTS (CAT_PAT_ID VARCHAR(250),CONSENT_STATEMENT VARCHAR(2000),CONSENT_RESPONSE VARCHAR(250), IRB_ID VARCHAR(250),UPDATE_FLAG   TINYINT(1), FOREIGN KEY (CAT_PAT_ID) REFERENCES STAGING_CATISSUE_PARTICIPANT(CAT_PAT_ID));
CREATE INDEX CATISSUE_CONSENTS_PAT_ID_IDX ON STAGING_CATISSUE_CONSENTS (CAT_PAT_ID);
CREATE INDEX CATISSUE_CONSENTS_IRB_ID_IDX ON STAGING_CATISSUE_CONSENTS (IRB_ID);

create table STAGING_PARTICIPANT_AUDIT (CAT_PAT_ID VARCHAR(250),UPDATED_ON DATE,STATUS VARCHAR(250), FOREIGN KEY (CAT_PAT_ID) REFERENCES STAGING_CATISSUE_PARTICIPANT(CAT_PAT_ID),COMMENT  VARCHAR(2000));
create table STAGING_JOB_AUDIT (RUN_ON DATE,ADDED_COUNT bigint(20),EDITED_COUNT bigint(20),FAILED_COUNT bigint(20));

create table STAGING_EPIC_Identity_ID_HX (CAT_PAT_ID VARCHAR(250),LINE bigint(20),OLD_MRN VARHCAR2(250),MRN_CHG_DATE DATE,MRN_CHG_TYPE  VARHCAR2(250),NEW_MRN VARHCAR2(250),OLD_CAT_PAT_ID)


 */