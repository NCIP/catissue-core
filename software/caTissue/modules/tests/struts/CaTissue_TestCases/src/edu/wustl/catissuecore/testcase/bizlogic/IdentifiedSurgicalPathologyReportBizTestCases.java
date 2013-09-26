/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class IdentifiedSurgicalPathologyReportBizTestCases extends CaTissueSuiteBaseTest {

	AbstractDomainObject domainObject = null;
	public void testAddIdentifiedSurgicalPathologyReport()
	{
		try{
			IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport= BaseTestCaseUtility.initIdentifiedSurgicalPathologyReport();			
			System.out.println(identifiedSurgicalPathologyReport);
			SpecimenCollectionGroup specimenCollectionGroup=identifiedSurgicalPathologyReport.getSpecimenCollectionGroup();
			specimenCollectionGroup=(SpecimenCollectionGroup)appService.updateObject(specimenCollectionGroup);
			identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.createObject(identifiedSurgicalPathologyReport);
			BizTestCaseUtility.setObjectMap(identifiedSurgicalPathologyReport, IdentifiedSurgicalPathologyReport.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 System.out
					.println("IdentifiedSurgicalPathologyReportTestCases.testAddIdentifiedSurgicalPathologyReport()"+e.getMessage());
			 assertFalse(e.getMessage(), true);
		 }
	}
	
	public void testUpdateIdentifiedSurgicalPathologyReport()
	{
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=new IdentifiedSurgicalPathologyReport();
    	Logger.out.info("updating domain object------->"+identifiedSurgicalPathologyReport);
	    try 
		{
	    	identifiedSurgicalPathologyReport=BaseTestCaseUtility.updateIdentifiedSurgicalPathologyReport(identifiedSurgicalPathologyReport);	
	    	IdentifiedSurgicalPathologyReport updatedIdentifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.updateObject(identifiedSurgicalPathologyReport);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedIdentifiedSurgicalPathologyReport);
	       	assertTrue("Domain object successfully updated ---->"+updatedIdentifiedSurgicalPathologyReport, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	       	System.out
					.println("IdentifiedSurgicalPathologyReportTestCases.testUpdateIdentifiedSurgicalPathologyReport()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse(e.getMessage(), true);
	    }
	}
}
