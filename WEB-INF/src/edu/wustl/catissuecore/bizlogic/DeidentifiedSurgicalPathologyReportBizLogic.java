package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SemanticType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * Used to store deidentified pathology report to the database 
 *
 */
public class DeidentifiedSurgicalPathologyReportBizLogic extends IntegrationBizLogic
{	
	/**
	 * Saves the Deidentified pathology reportobject in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try{
		DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) obj;
		
		
//		For Concept HighLighter
		Collection conceptReferentCollection = new HashSet();
		ConceptReferent conceptReferent = new ConceptReferent();
		conceptReferent.setEndOffset(new Long(10));
		conceptReferent.setStartOffset(new Long(5));
		conceptReferent.setIsModifier(true);
		conceptReferent.setIsNegated(false);
		
		Concept concept = new Concept();
		concept.setConceptUniqueIdentifier("2");
		concept.setName("C001");
		SemanticType semanticType = new SemanticType();
		semanticType.setLabel("NCI_Thesaurus");
		concept.setSemanticType(semanticType);
		conceptReferent.setConcept(concept);
		
		ConceptReferentClassification conceptReferentClassification = new ConceptReferentClassification();
		conceptReferentClassification.setId(new Long(1));
		conceptReferent.setConceptReferentClassification(conceptReferentClassification);
		
		conceptReferentCollection.add(conceptReferent);
		//2nd object
		ConceptReferentClassification conceptReferentClassification1 = new ConceptReferentClassification();
		conceptReferentClassification1.setId(new Long(2));

		Concept concept1 = new Concept();
		concept1.setName("C002");
		concept1.setSemanticType(semanticType);
		
		ConceptReferent conceptRef2 = new ConceptReferent();
		conceptRef2.setEndOffset(new Long(4));
		conceptRef2.setStartOffset(new Long(0));
		conceptRef2.setConcept(concept1);
		conceptRef2.setConceptReferentClassification(conceptReferentClassification1);
		
		ConceptReferent conceptRef3 = new ConceptReferent();
		conceptRef3.setEndOffset(new Long(15));
		conceptRef3.setStartOffset(new Long(11));
		Concept concept2 = new Concept();
		concept2.setName("C003");
		concept2.setSemanticType(semanticType);
		
		
		conceptRef3.setConcept(concept2);
		conceptRef3.setConceptReferentClassification(conceptReferentClassification1);
		
		conceptReferentCollection.add(conceptRef2);
		conceptReferentCollection.add(conceptRef3);
				
		//3rd  Object
		ConceptReferentClassification conceptReferentClassification2 = new ConceptReferentClassification();
		conceptReferentClassification2.setId(new Long(3));

		Concept concept3 = new Concept();
		concept3.setName("C004");
		concept3.setSemanticType(semanticType);
		
		ConceptReferent conceptRef4 = new ConceptReferent();
		//conceptRef.setConcept(concept);
		conceptRef4.setEndOffset(new Long(20));
		conceptRef4.setStartOffset(new Long(16));
		conceptRef4.setConcept(concept3);
		conceptRef4.setConceptReferentClassification(conceptReferentClassification2);
		
		Concept concept4 = new Concept();
		concept4.setName("C005");
		concept4.setSemanticType(semanticType);
		
		ConceptReferent conceptRef5 = new ConceptReferent();
		conceptRef5.setEndOffset(new Long(25));
		conceptRef5.setStartOffset(new Long(21));
		conceptRef5.setConcept(concept4);
		conceptRef5.setConceptReferentClassification(conceptReferentClassification2);
	
		conceptReferentCollection.add(conceptRef4);
		conceptReferentCollection.add(conceptRef5);
		report.setConceptReferentCollection(conceptReferentCollection);		
		
		
		dao.insert(report, sessionDataBean, true, false);
		Set protectionObjects = new HashSet();
		protectionObjects.add(report);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try{
			DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) obj;
			if(report.getTextContent().getId()==null){
				dao.insert(report.getTextContent(), sessionDataBean, false, false);	
			}
			dao.update(report, sessionDataBean, true, false, false);

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @return a collection of all deidentified reports
	 * @throws Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the Deidentified reports from the database 
		List listOfReports = bizLogic.retrieve(sourceObjectName);
		Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) listOfReports.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return  mapOfReports;
	}
	

	/**
	 * This function takes identifier as parameter and returns corresponding DeidentifiedSurgicalPathologyReport
	 * @param identifier system generated unique id for report
	 * @return Deidentified pathology report of given identifier
	 * @throws Exception
	 */
	public DeidentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the deidentified reports from the database 
		List reportList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
		DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) reportList.get(0);
		return report;

	}
	
}
