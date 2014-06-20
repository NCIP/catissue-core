
package edu.wustl.catissuecore.bizlogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.FileContent;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.dto.SprReportDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.security.global.Permissions;

/**
 * Used to store identified pathology report to the database
 */

public class IdentifiedSurgicalPathologyReportBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger(IdentifiedSurgicalPathologyReportBizLogic.class);

	/**
	 * Saves the Identified pathology reportobject in the database.
	 * @param dao : dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			dao.insert(report);
			final Set protectionObjects = new HashSet();
			protectionObjects.add(report);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Method to get dynamicGroup for given Report
	 * @param obj
	 *            IdentifiedSurgicalPathologyReport object
	 * @return Array of dynamicGroup
	 * @throws SMException
	 *             Security manager exception
	 * @throws DAOException
	 */
	/*
	 * private String[] getDynamicGroups(DAO dao, AbstractDomainObject obj)
	 * throws ApplicationException, ClassNotFoundException {
	 * IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=
	 * (IdentifiedSurgicalPathologyReport)obj; CollectionProtocolRegistration
	 * collectionProtocolRegistration
	 * =identifiedSurgicalPathologyReport.getSpecimenCollectionGroup
	 * ().getCollectionProtocolRegistration(); String[] dynamicGroups = new
	 * String[1]; dynamicGroups[0] =
	 * SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
	 * collectionProtocolRegistration,
	 * CSMGroupLocator.getInstance().getPGName(null
	 * ,Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol")));
	 * Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]); return
	 * dynamicGroups; }
	 */

	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			if (report.getTextContent().getId() == null)
			{
				dao.insert(report.getTextContent());
			}
			dao.update(report);

		}
		catch (final Exception ex)
		{
			this.logger
					.error("Error occured while updating IdentifiedSurgicalPathologyReport domain object"
							+ ex.getMessage(),ex);
			ex.printStackTrace() ;
		}
	}

	/**
	 * @return a collection of all identified reports
	 * @throws Exception : Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the Identified reports from the database
		final List listOfReports = bizLogic.retrieve(sourceObjectName);
		final Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			final IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) listOfReports
					.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return mapOfReports;

	}

	/**
	 * This function takes identifier as parameter and returns corresponding
	 * IdentifiedSurgicalPathologyReport
	 * @param identifier
	 *            system generated unique id for report
	 * @return Identified pathology report of given identifier
	 * @throws Exception
	 *             Generic exception
	 */
	public IdentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the identified reports from the database
		final Object object = bizLogic.retrieve(sourceObjectName, identifier);
		final IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) object;
		return report;

	}

	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}

	/**
	 * @param objName : objName
	 * @param sessionDataBean : sessionDataBean
	 * @param identifier : identifier
	 * @return boolean
	 */
	@Override
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, this
				.getReadDeniedPrivilegeName());
	}
	
	
	public Long createSprPdfReport(Long scgId, FileItem fileItem,
			SessionDataBean sessionDataBean) throws ApplicationException {
		DAO dao = null;
		Long reportId = null;
		try {
			String sprReportPath = XMLPropertyHandler.getValue(Constants.SPR_DIR_LOACTION);

			
			File destinationDir = new File(sprReportPath);
			if (!destinationDir.exists()) {
				destinationDir.mkdirs();
			}
			String fileName = scgId+"_" +fileItem.getName();
			File file = new File(destinationDir, fileName);
			fileItem.write(file);
			
			dao = AppUtility.openDAOSession(sessionDataBean);
			Map<String,Object> obj = this.checkForFileContent(scgId, dao);
			FileContent fileContent;
			if (obj.isEmpty()) {
				SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
				scg.setId(scgId);
				IdentifiedSurgicalPathologyReport sprReport = new IdentifiedSurgicalPathologyReport();
				sprReport.setSpecimenCollectionGroup(scg);
				scg.setIdentifiedSurgicalPathologyReport(sprReport);
				fileContent = new FileContent();
				fileContent.setData(fileName);
				fileContent.setSurgicalPathologyReport(sprReport);
				sprReport.setFileContent(fileContent);
				sprReport.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				this.insert(sprReport, dao, sessionDataBean);
				reportId = sprReport.getId();
			} else {
				
				IdentifiedSurgicalPathologyReport report = getReportById((Long)obj.get("reportId"));
				report.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
				if( obj.get("fileContent")==null){
					fileContent = new FileContent();
					fileContent.setData(fileName);
					fileContent.setSurgicalPathologyReport(report);
					report.setFileContent(fileContent);
					
				}else{
					fileContent = (FileContent) obj.get("fileContent");
					fileContent.setData(fileName);
					dao.update(fileContent);
				}
				dao.update(report);
				
			//	fileContent = (FileContent) obj.get("fileContent");
				
				reportId =(Long)obj.get("reportId");
			}
			
			dao.commit();
		} catch (ApplicationException e) {
			throw new BizLogicException(e.getErrorKey(), e, e.getMessage());
			
		} catch(Exception ex){
			throw new BizLogicException(null,null, ex.getMessage());
		}finally {
			if (dao != null)
				AppUtility.closeDAOSession(dao);
		}

		return reportId;
	}
	
	private Map<String,Object> checkForFileContent(Long scgId, DAO dao)
			throws ApplicationException {
		IdentifiedSurgicalPathologyReport result = new IdentifiedSurgicalPathologyReport();
		String hql = "select scg.identifiedSurgicalPathologyReport.id,scg.identifiedSurgicalPathologyReport.fileContent from SpecimenCollectionGroup as scg where scg.id=?";
		
		final ColumnValueBean columnValueBean = new ColumnValueBean(scgId);
		final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
		columnValueBeanList.add(columnValueBean);
		Map<String,Object> obj = new HashMap<String,Object>();

		final List resultList = AppUtility.executeHqlQuery(hql,
				columnValueBeanList);
		final Iterator iterator = resultList.iterator();
		if (iterator.hasNext()) {
			Object[] data = (Object[]) iterator.next();
			obj.put("fileContent", data[1]);
			obj.put("reportId",data[0]);
			
			
		}
		return obj;

	}
	
	public List<Object> getFileName(Long scgId, SessionDataBean sessionDataBean)
			throws ApplicationException {
		DAO dao = null;
		String fileName = null;
		byte[] byteArr= {};
		List<Object> retList = new ArrayList<Object>();
		try {
			dao = AppUtility.openDAOSession(sessionDataBean);

			IdentifiedSurgicalPathologyReport result = new IdentifiedSurgicalPathologyReport();
			String hql = "select fileContent.data from IdentifiedSurgicalPathologyReport as ispr where ispr.id=?";
			final ColumnValueBean columnValueBean = new ColumnValueBean(scgId);
			final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
			columnValueBeanList.add(columnValueBean);

			final List resultList = AppUtility.executeHqlQuery(hql,
					columnValueBeanList);
			final Iterator iterator = resultList.iterator();
			if (iterator.hasNext()) {
				fileName = (String) iterator.next();

			}
			String sprReportPath = XMLPropertyHandler.getValue(Constants.SPR_DIR_LOACTION);
			//Replaced '\\' with '/' as '\\' not working on linux machine.
			File file = new File(sprReportPath + "/" + fileName);
			FileInputStream fin = new FileInputStream(file);
			byteArr = IOUtils.toByteArray(fin);
			fileName = fileName.substring(fileName.indexOf("_") + 1,
					fileName.length());
			retList.add(fileName);
			retList.add(byteArr);
		
		} catch (ApplicationException ex) {
			throw new BizLogicException(ex.getErrorKey(), ex, ex.getMessage());
			
		}catch (IOException  ex) {
			throw new BizLogicException(null, null, ex.getMessage());
			
		} finally {
			if (dao != null)
				AppUtility.closeDAOSession(dao);
		}
		return retList;

	}
	
	/**
	 * @param identifiedReportId
	 *            : identifiedReportId
	 * @return Long : Long
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public Long getParticipantId(Long identifiedReportId) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IdentifiedSurgicalPathologyReportBizLogic bizLogic = (IdentifiedSurgicalPathologyReportBizLogic) factory
				.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());

		final String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();
		final String[] selectColumnName = {Constants.COLUMN_NAME_SCG_CPR_PARTICIPANT_ID};
		final String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {identifiedReportId};
		final String joinCondition = "";

		final List participantIdList = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (participantIdList != null && !participantIdList.isEmpty())
		{
			return (Long) participantIdList.get(0);
		}
		return null;
	}
	
	public void deleteReport(Long reportId ,SessionDataBean sessionDataBean) throws ApplicationException{
		DAO dao = null;
		
		try {
			IdentifiedSurgicalPathologyReport report = getReportById(reportId);
			dao = AppUtility.openDAOSession(sessionDataBean);
			dao.delete(report.getFileContent());
			dao.delete(report);
			dao.commit();
		}
		 catch (ApplicationException ex) {
				throw new BizLogicException(ex.getErrorKey(), ex, ex.getMessage());
				
			}  catch (Exception e) {
				throw new BizLogicException(null,null, e.getMessage());
			}finally {
				if (dao != null)
					AppUtility.closeDAOSession(dao);
			}
		
	}
	
	public SprReportDTO getIdentifiedReportData(Long reportId ,Long deReportId,SessionDataBean sessionDataBean) throws ApplicationException{
		DAO dao = null;
		Object[] obj = null ;
		SprReportDTO dto = new SprReportDTO();
		try {
			dao = AppUtility.openDAOSession(sessionDataBean);
			/*
			 * Below query is written to fetch Identified report details required for PDF 
			 * Below are the details fetched this query
			 * CP_title,Participant Name (PPID),participant gender,Text Content data,Participant MRN Number
			 * 
			 */
			
			String hql = "select ispr.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.title," +
					" ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.birthDate," +
					" case"+ 
					" when ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.lastName is null"+
					" then '' "+
					"else"+
					" ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.lastName"+ 
					" end"+
					"||', '||"+
					"case "+
					" when ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.firstName is null"+ 
					" then '' "+ 
					"else "+
					" ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.firstName"+ 
					" end"+
					"||'( '||"+
					" case"+
					" when ispr.specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier is null"+ 
					" then '' "+
					"else"+
					" ispr.specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier "+ 
					"end"+
					"||' )',"+
					" ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.gender, " +
					" ispr.textContent.data,ispr.reportSource.id," +
					" ispr.reportSource.id,"  + " ispr.specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier"
							+ ", ispr.specimenCollectionGroup.collectionProtocolRegistration.participant.id "+ 
					" from IdentifiedSurgicalPathologyReport as ispr " +
					" where ispr.id=? ";
			
			ColumnValueBean columnValueBean = new ColumnValueBean(reportId);
			List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
			columnValueBeanList.add(columnValueBean);

			List resultList = AppUtility.executeHqlQuery(hql,
					columnValueBeanList);
			Iterator iterator = resultList.iterator();
			
			if (iterator.hasNext()) {
				dto = new SprReportDTO();
				obj = (Object[])iterator.next();
				dto.setBirthDate(obj[1]!=null?(Date)obj[1]:null);
				dto.setCpTitle(obj[0]!=null?obj[0].toString():"");
				dto.setData(obj[4]!=null?obj[4].toString():"");
				dto.setGender(obj[3]!=null?obj[3].toString():"");
				dto.setParticipantName(obj[2]!=null?obj[2].toString():"");
				dto.setMrnString(obj[6].toString());
				dto.setConceptReferentMap(getConceptReferentMap(deReportId));
				dto.setPpid(obj[7]!=null?obj[7].toString():"");
				dto.setAge(obj[1]!=null?getAge((Date)obj[1]):0);
				String query = "select medicalRecordNumber from com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier pmi "
						+ "where pmi.participant.id = ? and pmi.site.id = ?";
				
				columnValueBeanList = new ArrayList<ColumnValueBean>();
				columnValueBeanList.add(new ColumnValueBean(obj[8]));
				columnValueBeanList.add(new ColumnValueBean(obj[6]));
				List mrnList = AppUtility.executeHqlQuery(query,
						columnValueBeanList);
				Iterator mrnItr = mrnList.iterator();
				if (mrnItr.hasNext()) {
					
					String mrnValue = (String) mrnItr.next();
					dto.setMrnString(mrnValue);
				}
			}
			
		} catch (ApplicationException ex) {
			throw new BizLogicException(ex.getErrorKey(), ex, ex.getMessage());
			
 		} finally {
			if (dao != null)
				AppUtility.closeDAOSession(dao);
		}
		return dto;
	}
	
	private Map<String,String> getConceptReferentMap(Long deReportId) throws ApplicationException{
		String hql="select distinct crc.name from ConceptReferentClassification as crc";
		List<Object[]> resultList = AppUtility.executeQuery(hql);
		Iterator iterator = resultList.iterator();
		Map<String,String> map = new HashMap<String,String>();
		String crcString = "";
		while(iterator.hasNext()){
			crcString = (String)iterator.next();
			map.put(crcString, "");
		}
		if(deReportId!=0L){	
			hql = "select cr.conceptReferentClassification.name,cr.concept.name,cr.concept.conceptUniqueIdentifier from ConceptReferent as cr where cr.deIdentifiedSurgicalPathologyReport.id = ?";
			ColumnValueBean columnValueBean = new ColumnValueBean(deReportId);
			List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
			columnValueBeanList.add(columnValueBean);
			resultList = AppUtility.executeHqlQuery(hql,
					columnValueBeanList);
			iterator = resultList.iterator();
			while(iterator.hasNext()){
				Object[] obj = (Object[])iterator.next();
				map.put(obj[0].toString(), map.get(obj[0].toString())+obj[1]+"("+obj[2]+"), ");
			}
			
		}
		return map;
		
	}
	
	public SprReportDTO getDidentifiedReportData(Long reportId,SessionDataBean sessionDataBean) throws ApplicationException{
		DAO dao = null;
		Object[] obj = null ;
		SprReportDTO dto = new SprReportDTO();
		try {
			dao = AppUtility.openDAOSession(sessionDataBean);
			
			/*
			 * Below query is written to fetch DEIdentified report details required for PDF 
			 * Below are the details fetched this query
			 * CP_title,PPID,participant gender,Text Content data
			 * 
			 */
			
			String hql = "select dispr.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.title," +
					"dispr.specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier,"+ 
					" dispr.specimenCollectionGroup.collectionProtocolRegistration.participant.gender, " +
					" dispr.textContent.data," +
					" dispr.specimenCollectionGroup.collectionProtocolRegistration.participant.birthDate" +
					" from DeidentifiedSurgicalPathologyReport as dispr where dispr.id=?";
			final ColumnValueBean columnValueBean = new ColumnValueBean(reportId);
			final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
			columnValueBeanList.add(columnValueBean);

			final List<Object[]> resultList = AppUtility.executeHqlQuery(hql,
					columnValueBeanList);
			final Iterator<Object[]> iterator = resultList.iterator();
			if (iterator.hasNext()) {
				dto = new SprReportDTO();
				obj = iterator.next();
				dto.setCpTitle(obj[0]!=null?obj[0].toString():"");
				dto.setData(obj[3]!=null?obj[3].toString():"");
				dto.setGender(obj[2]!=null?obj[2].toString():"");
				dto.setParticipantName(obj[1]!=null?obj[1].toString():"");

				dto.setConceptReferentMap(getConceptReferentMap(reportId));
				dto.setAge(obj[4]!=null?getAge((Date)obj[4]):0);
				dto.setPpid(obj[1]!=null?obj[1].toString():"");
			}
		} catch (ApplicationException ex) {
			throw new BizLogicException(ex.getErrorKey(), ex, ex.getMessage());
		} finally {
			if (dao != null)
				AppUtility.closeDAOSession(dao);
		}
		return dto;
	}
	
	public int getAge(Date birthDate){
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(birthDate);
		if (dob.after(now)) {
		  throw new IllegalArgumentException("Can't be born in the future");
		}
		int year1 = now.get(Calendar.YEAR);
		int year2 = dob.get(Calendar.YEAR);
		int age = year1 - year2;
		int month1 = now.get(Calendar.MONTH);
		int month2 = dob.get(Calendar.MONTH);
		if (month2 > month1) {
		  age--;
		} else if (month1 == month2) {
		  int day1 = now.get(Calendar.DAY_OF_MONTH);
		  int day2 = dob.get(Calendar.DAY_OF_MONTH);
		  if (day2 > day1) {
		    age--;
		  }
		}
		return age;
	}

	
	
}
