
package edu.wustl.catissuecore.bizlogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.ibm.db2.jcc.uw.classloader.e;

import edu.wustl.bulkoperator.csv.impl.CsvFileReader;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.ConsentTierDTO;
import edu.wustl.catissuecore.dto.ConsentResponseDto;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class ConsentTrackingBizLogic
{

	public final String CONSENT_LEVEL_SPECIMEN = "specimen";
	public final String CONSENT_LEVEL_PARTICIPANT = "participant";
	public final String CONSENT_LEVEL_SCG = "scg";
	
	/**
     * Logger object.
     */
    private static final Logger logger = Logger.getCommonLogger(ConsentTrackingBizLogic.class);

	public ConsentResponseDto getConsentList(String consentLevel, Long consentLevelId, DAO dao)
			throws ApplicationException
	{

		String signedConsentURL = null;
		User witness = null;
		Date consentSignDate = null;
		Iterator ite = null;
		List<ConsentTierDTO> consentTierList = null;
		List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
		parameters.add(new ColumnValueBean(consentLevelId));
		Collection consentDetails;

		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, consentLevelId));

		if (CONSENT_LEVEL_SPECIMEN.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("specimenConsentQuery", params);
			ite = consentDetails.iterator();
			consentTierList = getConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"specimenConsetTierQuery", params));

		}
		else if (CONSENT_LEVEL_PARTICIPANT.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("cprConsentQuery", params);
			ite = consentDetails.iterator();
			List<ConsentTier> cpConsentTierList =  ((HibernateDAO) dao).executeNamedQuery("cpConsentTierList", params);
			consentTierList = getCprConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"cprConsetTierQuery", params),cpConsentTierList);

		}
		else if (CONSENT_LEVEL_SCG.equals(consentLevel))
		{
			consentDetails = ((HibernateDAO) dao).executeNamedQuery("scgConsentQuery", params);
			ite = consentDetails.iterator();
			consentTierList = getConsentTierList(((HibernateDAO) dao).executeNamedQuery(
					"scgConsetTierQuery", params));
		}

		if (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			signedConsentURL = arr[0] != null ? String.valueOf(arr[0]) : null;
			witness = arr[1] != null ? (User) arr[1] : null;
			consentSignDate = arr[2] != null ? (Date) arr[2] : null;

		}
		String witnessName = "";
		Long witnessId = 0l;
		if (witness == null)
		{
			witnessName = "";

		}
		else
		{
			final String witnessFullName = witness.getLastName() + ", " + witness.getFirstName();
			witnessName = witnessFullName;
			witnessId = witness.getId();
		}

		ConsentResponseDto consentsDto = new ConsentResponseDto();
		consentsDto.setConsentTierList(consentTierList);
		consentsDto.setConsentUrl(signedConsentURL);
		consentsDto.setConsentDate(consentSignDate);
		consentsDto.setWitnessName(witnessName);
		consentsDto.setWitnessId(witnessId);
		consentsDto.setConsentLevel(consentLevel);
		consentsDto.setConsentLevelId(consentLevelId);

		return consentsDto;

	}

	private List<ConsentTierDTO> getCprConsentTierList(List consentDetailList,List<ConsentTier> cpConsentTierList)
			throws ApplicationException
	{
		List<ConsentTierDTO> consentTierList = new ArrayList<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			dto = new ConsentTierDTO();
			dto.setConsentStatment(String.valueOf(arr[0]));
			dto.setParticipantResponses(String.valueOf(arr[1]));
			dto.setId((Long) arr[2]);
			consentTierList.add(dto);
			 logger.error("Consent already present");
             logger.error(dto.getConsentStatment());
		}
		for(int i = 0;i<cpConsentTierList.size();i++){
		    boolean consentTierExists = false;
		    for(int j = 0;j<consentTierList.size();j++){
		        if(consentTierList.get(j).getId().compareTo(cpConsentTierList.get(i).getId())==0){
		            consentTierExists = true;
		            break;
		        }
		       
		    }
		    if(!consentTierExists){
		        logger.error("Newly added consent");
                logger.error(cpConsentTierList.get(i).getStatement());
		        dto = new ConsentTierDTO();
	            dto.setConsentStatment(cpConsentTierList.get(i).getStatement());
	            dto.setParticipantResponses(Constants.NOT_SPECIFIED);
	            dto.setId(cpConsentTierList.get(i).getId());
	            consentTierList.add(dto);
		    }
		}
		return consentTierList;

	}

	private List<ConsentTierDTO> getConsentTierList(List consentDetailList)
			throws ApplicationException
	{
		List<ConsentTierDTO> consentTierList = new ArrayList<ConsentTierDTO>();
		Iterator ite = consentDetailList.iterator();
		ConsentTierDTO dto;
		while (ite.hasNext())
		{
			Object[] arr = (Object[]) ite.next();
			dto = new ConsentTierDTO();
			dto.setConsentStatment(String.valueOf(arr[0]));
			dto.setParticipantResponses(String.valueOf(arr[1]));
			dto.setStatus(String.valueOf(arr[2]));
			dto.setId((Long) arr[3]);
			//	dto.setConsentStatusId((Long) arr[4]);
			consentTierList.add(dto);
		}
		return consentTierList;

	}

	public String updateConsentTier(ConsentResponseDto consentDto, DAO dao,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
	    String retString = "";
	    try{
		
		if (CONSENT_LEVEL_SPECIMEN.equals(consentDto.getConsentLevel()))
		{
			NewSpecimenBizLogic specimenBizlogic = new NewSpecimenBizLogic();
			specimenBizlogic.updateSpecimenConsentStatus(consentDto.getConsentLevelId(),
					consentDto.getConsentTierList(), consentDto.isDisposeSpecimen(), dao,
					sessionDataBean);
		}
		else if (CONSENT_LEVEL_PARTICIPANT.equals(consentDto.getConsentLevel()))
		{
			ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
			participantBizLogic.updateConsentResponse(consentDto, consentDto.isDisposeSpecimen(),
					dao, sessionDataBean);
		}
		else if (CONSENT_LEVEL_SCG.equals(consentDto.getConsentLevel()))
		{
			SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
			scgBizLogic.updateScgConsentStatus(consentDto.getConsentLevelId(),
					consentDto.getConsentTierList(), consentDto.isDisposeSpecimen(), dao,
					sessionDataBean);
		}
	    }catch(Exception ex){
	        logger.error(ex.getMessage(),ex);
	    }

		return retString;
	}
	
	public void uploadConsentResponse(byte[] consentCSVFile) throws ApplicationException{
	    BufferedReader br = null;
	    JDBCDAO jdbcDAO = AppUtility.openJDBCSession();
	    HibernateDAO hibernateDAO = null;
        Long jobId = 0l;
        Long successCount = 0l;
        Long failedCount = 0l;
	    try
        {
	        jobId = makeConsentAuditJobEntry(jdbcDAO);
	        java.io.InputStream is = null;
	        is = new java.io.ByteArrayInputStream(consentCSVFile);
    	    br = new BufferedReader(new InputStreamReader(is));
    	    String line = "";
    	    String cvsSplitBy = ",";
//    	    "IRB ID,PARTICIPANT ID,STAEMENT,RESPONSE"
    	    br.readLine();
    	    
    	    CSVReader csvReader = new CSVReader(br);
    	    List<String[]> csvValueList =  csvReader.readAll();
    	   
	        for(int i =0;i< csvValueList.size();i++ ){
	            String[] consentDetails = csvValueList.get(i);
	            if("Yes".equals(consentDetails[3])||"No".equals(consentDetails[3])||"Not Specified".equals(consentDetails[3])||"Withdrawn".equals(consentDetails[3])){
	                try{
	                hibernateDAO =(HibernateDAO) AppUtility.openDAOSession(null);
	                    
    	            Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
    	            params.put("0", new NamedQueryParam(DBTypes.STRING, consentDetails[0]));
    	            List consentList = hibernateDAO.executeNamedQuery("cpConsentSelectQuery", params);
    	            
    	            params.put("1", new NamedQueryParam(DBTypes.LONG, consentDetails[1]));
    	            List cprList = hibernateDAO.executeNamedQuery("getCPRFromCPShortTitleandPARTID", params);
    	            
    	            if(!consentList.isEmpty() && consentList !=null && cprList !=null && !cprList.isEmpty()){
    	                boolean recordUpdated  = false;
    	                for(int j=0;j<consentList.size();j++){
    	                    ConsentTier consentTier =(ConsentTier) consentList.get(j);
    	                    if(consentTier.getStatement().equals(consentDetails[2])){
    	                        CollectionProtocolRegistration cpr  = (CollectionProtocolRegistration) cprList.get(0);
    	                        Iterator<ConsentTierResponse> consentTierItr = cpr.getConsentTierResponseCollection().iterator();
    	                        while(consentTierItr.hasNext()){
    	                            ConsentTierResponse consentTierResponse = consentTierItr.next();
    	                            if(consentTierResponse.getConsentTier().getId().compareTo(consentTier.getId())==0){
    	                                consentTierResponse.setResponse(consentDetails[3]);
    	                                hibernateDAO.update(consentTierResponse);
    	                                recordUpdated = true;
    	                                break;
    	                            }
    	                        }
    	                        if(!recordUpdated){
    	                            ConsentTierResponse consentTierResponse = new ConsentTierResponse();
    	                            consentTierResponse.setResponse(consentDetails[3]);
    	                            consentTierResponse.setConsentTier(consentTier);
    	                            cpr.getConsentTierResponseCollection().add(consentTierResponse);
    	                            hibernateDAO.update(cpr);
    	                        }
    	                        hibernateDAO.commit();
    	                        successCount++;
    	                        updateCatissueStagingAudit(jdbcDAO,consentDetails[0],Long.parseLong(consentDetails[1]),consentDetails[2],"Success",null,jobId);
    	                        jdbcDAO.commit();
    	                        break;
    	                    }
    	                }
    	                if(!recordUpdated){
    	                    updateCatissueStagingAudit(jdbcDAO,consentDetails[0],Long.parseLong(consentDetails[1]),consentDetails[2],"Failure","Invalid consent statement.",jobId);
    	                }
    	                
    	            }else{
    	                failedCount++;
    	                updateCatissueStagingAudit(jdbcDAO,consentDetails[0],Long.parseLong(consentDetails[1]),consentDetails[2],"Failure","Consent Tier is not availabel or Invalid consent CP Short Title or Participant ID",jobId);
    	                jdbcDAO.commit();
    	            }
	                }catch(Exception ex){
	                    failedCount++;
	                    updateCatissueStagingAudit(jdbcDAO,consentDetails[0],Long.parseLong(consentDetails[1]),consentDetails[2],"Failure",ex.getMessage(),jobId);
	                    jdbcDAO.commit();
	                    logger.error(ex.getMessage(),ex);
	                }
	                finally{
	                    
	                    AppUtility.closeDAOSession(hibernateDAO);
	                }
	            }else{
	                failedCount++;
	                updateCatissueStagingAudit(jdbcDAO,consentDetails[0],Long.parseLong(consentDetails[1]),consentDetails[2],"Failure","Invalid consent statement response",jobId);
	                jdbcDAO.commit();
	            }
            }
	        
            
	       
        }
	    catch (IOException e)
        {
	        logger.error(e.getMessage(), e);
        }
	    catch (Exception e)
        {
	        logger.error(e.getMessage(), e);
        }
        try{
	   
            updateConsentAuditJobEntry(jdbcDAO,jobId,successCount,failedCount);
            jdbcDAO.commit();
        } catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        finally{
            AppUtility.closeJDBCSession(jdbcDAO);
        }
	
	}
	
	  public void updateCatissueStagingAudit(JDBCDAO jdbcDAO, String irbID, long participantId, String consentStatement,String status, String msg,Long job_id)
	            throws SQLException, DAOException
	  {
	        String query = "insert into CATISSUE_CONSENT_AUDIT (PART_ID,IRB_ID,CONSENT_STATEMENT,UPDATED_ON,STATUS,COMMENTS,JOB_ID) values(?,?,?,?,?,?,?)";
            List<ColumnValueBean> columnValueList = new ArrayList<ColumnValueBean>();
            columnValueList.add(new ColumnValueBean(participantId));
            columnValueList.add(new ColumnValueBean(irbID));
            columnValueList.add(new ColumnValueBean(consentStatement));
            columnValueList.add(new ColumnValueBean(new Timestamp(System.currentTimeMillis())));
            columnValueList.add(new ColumnValueBean(status));
            columnValueList.add(new ColumnValueBean(msg));
            columnValueList.add(new ColumnValueBean(job_id));
            jdbcDAO.executeUpdate(query, columnValueList);
      }

	  public Long makeConsentAuditJobEntry(JDBCDAO jdbcDAO)
              throws SQLException, DAOException
      {
          String query = "select max(job_id) from CATISSUE_CONSENT_JOB_AUDIT";
          List jobIdList = jdbcDAO.executeQuery(query);
          Long jobId = 0l;
          if(jobIdList.get(0)==null || "".equals(((ArrayList<Long>)jobIdList.get(0)).get(0))){
              jobId ++;
          }else{
              jobId =  Long.parseLong(((ArrayList)jobIdList.get(0)).get(0).toString()) +1;
          }
          
          query = "insert into CATISSUE_CONSENT_JOB_AUDIT (JOB_ID,UPDATED_ON) values(?,?)";
          
          List<ColumnValueBean> columnValueList = new ArrayList<ColumnValueBean>();
          columnValueList.add(new ColumnValueBean(jobId));
          columnValueList.add(new ColumnValueBean(new Timestamp(System.currentTimeMillis())));
          jdbcDAO.executeUpdate(query, columnValueList);
          return jobId;
      }

	  public void updateConsentAuditJobEntry(JDBCDAO jdbcDAO,Long jobId,Long successCount,Long failedCount)
              throws SQLException, DAOException
    {
          String query = "update CATISSUE_CONSENT_JOB_AUDIT  set SUCCESS_COUNT= ? ,FAILED_COUNT = ? where JOB_ID = ?";
          List<ColumnValueBean> columnValueList = new ArrayList<ColumnValueBean>();
          columnValueList.add(new ColumnValueBean(successCount));
          columnValueList.add(new ColumnValueBean(failedCount));
          columnValueList.add(new ColumnValueBean(jobId));
          jdbcDAO.executeUpdate(query, columnValueList);
    }
}
