package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;



public class CaCoreAppServiceDelegatorTestCase extends CaTissueSuiteBaseTest
{
	 /**
	  * Search all participant and check if PHI data is visible
	  */
	  public void testGetNullObjects()
	  {
		try
		{
			new CaCoreAppServicesDelegator().delegateGetObjects(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(), null);
		}
		 catch(Exception e){

		     System.out
					.println("ScientistRoleTestCases.testGetObjects()"+e.getMessage());
			 e.printStackTrace();
			 assertTrue(e.getMessage(), true);
		 }
	  }
		 /**
		  * Search all participant and check if PHI data is visible
		  */
		  public void testGetObjectsWithNullId()
		  {
			try
			{
				Participant p = new Participant();
				new CaCoreAppServicesDelegator().delegateGetObjects(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(), p);
			}
			 catch(Exception e){

			     System.out
						.println("ScientistRoleTestCases.testGetObjects()"+e.getMessage());
				 e.printStackTrace();
				 assertTrue(e.getMessage(), true);
			 }
		  }

	  public void testDelegateLogin()
	  {
		try
		{
			Boolean flag = new CaCoreAppServicesDelegator().delegateLogin(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(), "Login123");
			if(!flag)
			{
				assertFalse("Cannot Login", true);
			}
			flag = new CaCoreAppServicesDelegator().delegateLogin("catissue@catissue.com", "Login123");
			if(flag)
			{
				assertFalse("Logged in with invalid user", true);
			}
		}
		 catch(Exception e){

		     System.out
					.println("ScientistRoleTestCases.testDelegateLogin()"+e.getMessage());
			 e.printStackTrace();
			 assertTrue(e.getMessage(), true);
		 }
	  }

	  public void testDelegateGetSpecimenCollectionGroupLabel()
	  {
		try
		{
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
			CollectionProtocol cp = new CollectionProtocol();
			cp.setTitle("Lung cancer Collection protocol");
			Participant p = new Participant();
			p.setId(34L);
			cpr.setParticipant(p);
			cpr.setCollectionProtocol(cp);
			scg.setCollectionProtocolRegistration(cpr);
			String label = new CaCoreAppServicesDelegator().delegateGetSpecimenCollectionGroupLabel(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(), scg);
			if(label==null)
			{
				assertFalse("SCG label not generated", true);
			}
		}
		 catch(Exception e){

		     System.out
					.println("ScientistRoleTestCases.testdelegateGetSpecimenCollectionGroupLabel()"+e.getMessage());
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	  }
	  public void testDelegateGetDefaultValue()
	  {
		try
		{

			String label = new CaCoreAppServicesDelegator().delegateGetDefaultValue(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(), Constants.DEFAULT_SPECIMEN);
			if(label==null)
			{
				assertFalse("SCG label not generated", true);
			}
		}
		 catch(Exception e){

		     System.out
					.println("ScientistRoleTestCases.testDelegateGetDefaultValue()"+e.getMessage());
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);

		 }
	  }
		public void testAuditQuery()
		{
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = IdentifiedSurgicalPathologyReport.class.getName();
				hql.append("select xxTargetAliasxx.activityStatus,xxTargetAliasxx.isFlagForReview,xxTargetAliasxx.reportStatus,xxTargetAliasxx.collectionDateTime,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
				appService.auditAPIQuery(hql.toString(),CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName());
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testAuditQuery()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
		public void testDelegateRegisterParticipant()
		{
			try
			{
				Participant p = new Participant();
				p.setFirstName("participant_first_name_"+UniqueKeyGeneratorUtil.getUniqueKey());
				p.setLastName("participant_last_name_"+UniqueKeyGeneratorUtil.getUniqueKey());
				p.setActivityStatus("Active");
				CollectionProtocol cp = (CollectionProtocol)TestCaseUtility.getNameObjectMap("CollectionProtocol");
				Participant updatedP = (Participant)new CaCoreAppServicesDelegator().delegateRegisterParticipant(p, cp.getId(), CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName());
//				CollectionProtocol cp1 =  (CollectionProtocol)edu.wustl.catissuecore.testcase.bizlogic.TestCaseUtility.getObjectMap(CollectionProtocol.class);
//				new CaCoreAppServicesDelegator().delegateRegisterParticipant(p, cp1.getId(), CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName());
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testDelegateRegisterParticipant()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
		public void testDelegateGetCaTissueLocalParticipantMatchingObects()
		{
			try
			{
				Participant p = new Participant();
				p.setFirstName("participant_first_name");
				p.setLastName("participant_last_name");
				p.setActivityStatus("Active");
				CollectionProtocol cp = (CollectionProtocol)TestCaseUtility.getNameObjectMap("CollectionProtocol");
				Set<Long> cpIdSet = new HashSet<Long>();
				cpIdSet.add(cp.getId());
				List list = new CaCoreAppServicesDelegator().
					delegateGetCaTissueLocalParticipantMatchingObects(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),p, cpIdSet);
				System.out.println("Matching participant list size:"+list.size());
				if(list.size()==0)
				{
					assertFalse("No Matching Particpant found",true);
				}
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testDelegateGetCaTissueLocalParticipantMatchingObects()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}

		public void testDelegateGetParticipantMatchingObects()
		{
			try
			{
				Participant p = new Participant();
				p.setFirstName("participant_first_name");
				p.setLastName("participant_last_name");
				p.setActivityStatus("Active");
				Collection participantMedicalIdentifierCollection = new HashSet();
				ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
				Site site =(Site)  TestCaseUtility.getNameObjectMap("Site");
				pmi.setSite(site);

				System.out.println("Site is "+site.getName());
				pmi.setMedicalRecordNumber("12");
				pmi.setParticipant(p);
				participantMedicalIdentifierCollection.add(pmi);
				p.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
				List list = new CaCoreAppServicesDelegator().
				delegateGetParticipantMatchingObects(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),p,null);
				System.out.println("Matching participant list size:"+list.size());
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testDelegateGetParticipantMatchingObects()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
}
