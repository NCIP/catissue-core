package edu.wustl.catissuecore.bizlogic.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class ScientistRoleCaGridTestCases extends CaTissueBaseTestCase 
{
 static ApplicationService appService = null;
 static Properties caTissueModelProp;
 static
 {
	 try
	 {
		 caTissueModelProp = new Properties();
		 caTissueModelProp.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("caTissueModel.properties"));
	 }
	 catch(IOException iexp)
	 {
		 System.out.println("ScientistRoleAPITestCases.enclosing_method()"+iexp.getMessage());
		 caTissueModelProp = null;
	 }
}
 public void setUp()
 {
	appService = ApplicationServiceProvider.getApplicationService();
	ClientSession cs = ClientSession.getInstance();
	//System.setProperty("javax.net.ssl.trustStore", "E://jboss//server//default//conf//chap8.keystore");
	try
	{ 
		cs.startSession("scientist@admin.com", "Test123");
	} 	
				
	catch (Exception ex) 
	{ 
		System.out.println(ex.getMessage()); 
		ex.printStackTrace();
		fail("Fail to create connection");
		System.exit(1);
	}
}
 
 /**
  * Search all participant and check if PHI data is visible
  */ 
  public void testCaGridQueryParticipantWithScientistLogin()
  {
	try
	{
		System.out.println("caTissueModelProp: "+caTissueModelProp);
		StringBuffer hql = new StringBuffer();
		String targetClassName = Participant.class.getName(); 
		hql.append("GridQuery:select ");
		hql.append(caTissueModelProp.get(targetClassName));
		
		hql.append(" from "+targetClassName+" xxTargetAliasxx");
		List parList = appService.query(new HQLCriteria(hql.toString()), targetClassName);
		System.out.println("Size : "+parList.size());
		for(int i=0;i<parList.size();i++)
		{
			Participant retutnParticpant = (Participant)parList.get(i);
			if(retutnParticpant.getFirstName()!=null||retutnParticpant.getLastName()!=null||
					retutnParticpant.getMiddleName()!=null||retutnParticpant.getBirthDate()!=null||
					retutnParticpant.getSocialSecurityNumber()!=null)
			{
				fail("Participant PHI data is visible to scientist");
			}
			Collection<ParticipantMedicalIdentifier> pmiCollection 
				= retutnParticpant.getParticipantMedicalIdentifierCollection();
			if(pmiCollection!=null)
			{
				for (Iterator<ParticipantMedicalIdentifier> iterator = pmiCollection.iterator();iterator.hasNext();)
			    {
			        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
			        if(participantMedId.getMedicalRecordNumber()!=null)
			        {
			        	fail("Participant PHI data is visible to scientist");
			        }
			    }
			}	
			
		    Collection<CollectionProtocolRegistration> cprCollection 
		    				= retutnParticpant.getCollectionProtocolRegistrationCollection();
		    if(cprCollection!=null)
		    {
			    for (Iterator<CollectionProtocolRegistration> iterator=cprCollection.iterator();iterator.hasNext();)
			    {
			        CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) iterator.next();
			        if(cpr.getRegistrationDate()!=null || cpr.getConsentSignatureDate()!=null ||
			        		cpr.getSignedConsentDocumentURL()!=null)
			        {
			        	fail("Participant PHI data is visible to scientist");
			        }
			    }
		    }    
		}
	 }
	 catch(Exception e){
		 
	     System.out
				.println("ScientistRoleTestCases.testCaGridQueryParticipantWithScientistLogin()"+e.getMessage());
		 e.printStackTrace();
		 assertFalse("Test failed. to search Particpant", true);
	 }
  }
  /**
   * Search all PMI and check if PHI data is visible
   */
  public void testCaGridQueryPMIWithScientistLogin()
  {
	  try
	  {
		  	StringBuffer hql = new StringBuffer();
		  	String targetClassName = ParticipantMedicalIdentifier.class.getName(); 
			hql.append("GridQuery:select ");
			hql.append(caTissueModelProp.get(targetClassName));
			hql.append(" from "+targetClassName+" xxTargetAliasxx");

			List pmiList = appService.query(new HQLCriteria(hql.toString()), targetClassName);
			for(int i=0;i<pmiList.size();i++)
			{
				 ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) pmiList.get(i);
			     if(participantMedId.getMedicalRecordNumber()!=null)
			     {
			        	fail("ParticipantMedicalIdentifier PHI data is visible to scientist");
			     }
			}
		 }
		 catch(Exception e)
		 {
			 System.out
				.println("ScientistRoleTestCases.testSearchPMIWithScientistLogin():"+e.getMessage());	
			 e.printStackTrace();
			 assertFalse("Test failed. to search PMI", true);
		 }
  }
  /**
	  * Search all CPR and check if PHI data is visible
	  *
	  */ 
	  public void testCaGridQueryProtocolRegistrationWithScientistLogin()
	  {
		try
		{
			StringBuffer hql = new StringBuffer();
		  	String targetClassName = CollectionProtocolRegistration.class.getName(); 
			hql.append("GridQuery:select ");
			hql.append(caTissueModelProp.get(targetClassName));
			hql.append(" from "+targetClassName+" xxTargetAliasxx");

			List cprList = appService.query(new HQLCriteria(hql.toString()), targetClassName);
			System.out.println("Size : "+cprList.size());
			for(int i=0;i<cprList.size();i++)
			{
				CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)cprList.get(i);
				if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
						returnedReg.getConsentSignatureDate()!=null)
				{
					fail("CollectionProtocolRegistration PHI data is visible to scientist");
				}
			}
		 }
		 catch(Exception e){
			 System.out
					.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
		     Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Test failed. to search SpecimenCollectionGroup", true);
		 }
	  }
  /**
	  * Search all SCG and check if PHI data is visible
	  *
	  */ 
	  public void testCaGridQuerySpecimenCollectionGroupWithScientistLogin()
	  {
		try{
			StringBuffer hql = new StringBuffer();
		  	String targetClassName = SpecimenCollectionGroup.class.getName(); 
			hql.append("GridQuery:select ");
			hql.append(caTissueModelProp.get(targetClassName));
			hql.append(" from "+targetClassName+" xxTargetAliasxx");

			List scgList = appService.query(new HQLCriteria(hql.toString()), targetClassName);
			System.out.println("Size : "+scgList.size());
			for(int i=0;i<scgList.size();i++)
			{
				SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup)scgList.get(i);
				if(returnedSCG.getSurgicalPathologyNumber()!=null)
				{
					fail("SpecimenCollectionGroup PHI data is visible to scientist");
				}
				CollectionProtocolRegistration returnedReg = returnedSCG.getCollectionProtocolRegistration();
				if(returnedReg!=null)
				{
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("SpecimenCollectionGroup PHI data is visible to scientist");
					}
				}	
				Collection<SpecimenEventParameters> spEvent = returnedSCG.getSpecimenEventParametersCollection();
				if(spEvent!=null)
				{
				    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
				    while(eveItr.hasNext())
				    {
				    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
				    	if(spEventParam.getTimestamp()!=null)
				    	{
				    		fail("SpecimenCollectionGroup PHI data is visible to scientist");
				    	}
				    }
				}   
			}
		 }
		 catch(Exception e){
			 System.out
					.println("ScientistRoleTestCases.testSearchSpecimenCollectionGroupWithScientistLogin() "+e.getMessage());
		     Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Test failed. to search SpecimenCollectionGroup", true);
		 }
	  }
	  /**
	   * Test search Tissue specimen and check for PHI data
	   */
	  public void testCaGridQueryTissueSpecimenWithScientistLogin()
	  {
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = TissueSpecimen.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List<Specimen> spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				validateSpecimenData(targetClassName,spCollection);
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchTissueSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search TissueSpecimen", true);
			 }
		}
		/**
		 * Test Search Molecular Specimen and test for PHI data 
		 */
	  public void testCaGridQueryMolecularSpecimenWithScientistLogin()
	  {
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = MolecularSpecimen.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List<MolecularSpecimen> spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				validateSpecimenData(targetClassName,spCollection);
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchMolecularSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search MolecularSpecimen", true);
			 }
		}
	  /**
		* Test Search Cell Specimen and test for PHI data 
		*/
	  public void testCaGridQueryCellSpecimenWithScientistLogin()
	  {
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = CellSpecimen.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List<CellSpecimen> spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				validateSpecimenData(targetClassName,spCollection);
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchCellSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search CellSpecimen", true);
			 }
		}
	  /**
		 * Test Search Fluid Specimen and test for PHI data 
		 */
	  public void testCaGridQueryFluidSpecimenWithScientistLogin()
	  {
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = FluidSpecimen.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List<FluidSpecimen> spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				validateSpecimenData(targetClassName,spCollection);
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchFluidSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse("Test failed. to search FluidSpecimen", true);
			 }
		}
	
	  
		/**
		 * Test search SpecimenArrayContent and test for PHI data
		 */
		public void testCaGridQuerySpecimenArrayContentWithScientistLogin()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = SpecimenArrayContent.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List sacCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				System.out.println("Total SpecimenArrayContent Count:"+sacCollection.size());
				Iterator itr = sacCollection.iterator();
				while(itr.hasNext())
				{
					SpecimenArrayContent spe = (SpecimenArrayContent)itr.next();
					if(spe.getSpecimen().getCreatedOn()!=null)
					{
						fail("SpecimenArrayContent ->Specimen PHI data is visible to scientist");
					}
				}
			}
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSpecimenArrayContentWithScientistLogin()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse("Test failed. to search SpecimenArrayContent", true);
			 }
			
		}
		
		/**
		 * Test search for ReceivedEventParameters and test for PHI data
		 */
		public void testCaGridQueryReceivedEventParameters()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = ReceivedEventParameters.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List recColl = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				Iterator itr = recColl.iterator();
				while(itr.hasNext())
				{
					ReceivedEventParameters rec = (ReceivedEventParameters)itr.next();
					if(rec.getTimestamp()!=null)
					{
						fail("ReceivedEventParameters PHI data is visible to scientist");
					}
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
				e.printStackTrace();
				assertFalse("Test failed. to search SpecimenArrayContent", true);
			}
		}
		/**
		 * Test search for CollectionEventParameters and test for PHI data
		 */
		public void testCaGridQueryCollectionEventParameters()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = CollectionEventParameters.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List collEveColl = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				Iterator itr = collEveColl.iterator();
				while(itr.hasNext())
				{
					CollectionEventParameters cep = (CollectionEventParameters)itr.next();
					if(cep.getTimestamp()!=null)
					{
						fail("CollectionEventParameters PHI data is visible to scientist");
					}
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
				e.printStackTrace();
				assertFalse("Test failed. to search CollectionEventParameters", true);
			}
		}
		/**
		 * Test search for PathologyReportReviewParameter and test for PHI data
		 */
		public void testCaGridQueryMoleEventparam()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = MolecularSpecimenReviewParameters.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List recColl = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				Iterator itr = recColl.iterator();
				while(itr.hasNext())
				{
					MolecularSpecimenReviewParameters mol = (MolecularSpecimenReviewParameters)itr.next();
					if(mol.getTimestamp()!=null)
					{
						fail("MolecularSpecimenReviewParameters PHI data is visible to scientist");
					}
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testsearchMoleEventparam()"+e.getMessage());
				e.printStackTrace();
				assertFalse("could not add object", true);
			}
		}
		
		/**
		 * Test search for DeidentifiedSurgicalPathologyReport and test for PHI data
		 */
		public void testCaGridQueryDeidentifiedSurgicalPathologyReport()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = DeidentifiedSurgicalPathologyReport.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				Iterator itr = spCollection.iterator();
				while(itr.hasNext())
				{
					DeidentifiedSurgicalPathologyReport deid = (DeidentifiedSurgicalPathologyReport)itr.next();
					if(deid.getCollectionDateTime()!=null || deid.getId()!=null
							|| deid.getActivityStatus()!=null||deid.getIsFlagForReview()!=null)
					{
						fail("DeIdentifiedSurgicalPathologyReport PHI data is visible to scientist");
					}
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchDeidentifiedSurgicalPathologyReport()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse("Test failed. to search deIfiedSurgicalPathologyReport", true);
			 }
		}
		/**
		 * Test search for IdentifiedSurgicalPathologyReport and test for PHI data
		 */
		public void testCaGridQueryIdentifiedSurgicalPathologyReport()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = IdentifiedSurgicalPathologyReport.class.getName(); 
				hql.append("GridQuery:select ");
				hql.append(caTissueModelProp.get(targetClassName));
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				List spCollection = appService.query(new HQLCriteria(hql.toString()), targetClassName);
				Iterator itr = spCollection.iterator();
				while(itr.hasNext())
				{
					IdentifiedSurgicalPathologyReport ispr = (IdentifiedSurgicalPathologyReport)itr.next();
					System.out.println("IdentifiedSurgicalPathologyReport : "+ ispr);
					if(ispr.getCollectionDateTime()!=null || ispr.getTextContent()!= null||
							ispr.getId()!=null || ispr.getActivityStatus()!=null)
					{
						fail("IdentifiedSurgicalPathologyReport PHI data is visible to scientist");
					}
				}
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testSearchIdentifiedSurgicalPathologyReport()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse("Test failed. to search IdentifiedSurgicalPathologyReport", true);
			 }
		}
		/**
		 * Test search for count queries with scientist user
		 */
		public void testCaGridCountQueryWithScientist()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = TissueSpecimen.class.getName(); 
				hql.append("GridQuery:select count(*)");
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				appService.query(new HQLCriteria(hql.toString()), targetClassName);
				
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testCaGridCountQueryWithScientist()"+e.getMessage());
				 e.printStackTrace();
				 assertEquals("caTissue doesnot support queries which returns result list containing instances of classes other than caTissue domain model",
						 e.getMessage());			
			 }
		}
		
		public void testAPICountQueryWithScientist()
		{
			try
			{
			  	String targetClassName = TissueSpecimen.class.getName(); 
				appService.getQueryRowCount(new TissueSpecimen(), targetClassName);
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testAPICountQueryWithScientist()"+e.getMessage());
				 e.printStackTrace();
				 assertEquals("caTissue doesnot support queries which returns result list containing instances of classes other than caTissue domain model",
						 e.getMessage());			
			 }
		}
		
		public void testAPISelectQueryWithScientist()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = MolecularSpecimen.class.getName(); 
				hql.append("select id,label");
				hql.append(" from "+targetClassName+" xxTargetAliasxx");
				appService.query(new HQLCriteria(hql.toString()), targetClassName);
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testCaGridCountQueryWithScientist()"+e.getMessage());
				 e.printStackTrace();
				 assertEquals("caTissue doesnot support queries which returns result list containing instances of classes other than caTissue domain model",
						 e.getMessage());			
			 }
		}
		
		private void validateSpecimenData(String className, List spCollection)
		{
			Iterator<Specimen> itr = spCollection.iterator();
			while(itr.hasNext())
			{
				Specimen spe = (Specimen)itr.next();
				if(spe.getCreatedOn()!=null)
				{
					fail(className+" PHI data is visible to scientist");
				}
			
				Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
				if(spEvent!=null||!spEvent.isEmpty())
				{
					Iterator<SpecimenEventParameters> iter = spEvent.iterator();
					while(iter.hasNext())
					{
							SpecimenEventParameters event = iter.next();
							fail(className+" FOr caGrid queries Evetn object should no be returned");
					}
				}
				SpecimenCollectionGroup scg =spe.getSpecimenCollectionGroup();
				if(scg!=null)
				{
					fail(className+" FOr caGrid queries SpecimenCollectionGroup object should no be returned");
				}
			}
		}
				
	}