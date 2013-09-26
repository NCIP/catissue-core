/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.api.testcases;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.common.util.logger.Logger;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

public class ComplexCollectionProtocolTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication 
{
	public ComplexCollectionProtocolTestCases() {
		super();
	}
	
	
	
	
	
	
	public void testAddComplexCollectionProtocol()
	{
		try
		 {
			
			CollectionProtocol collectionProtocol = ComplexCollectionProtocolUtility.initComplexCollectionProtocol();			
			collectionProtocol = (CollectionProtocol) insert(collectionProtocol);
			TestCaseUtility.setObjectMap(collectionProtocol, CollectionProtocol.class);
//			assertTrue("Object added successfully", true);
			
			CollectionProtocol collectionProtocol1 = ComplexCollectionProtocolUtility.initComplexCollectionProtocol1();			
			collectionProtocol1 = (CollectionProtocol) insert(collectionProtocol1);
			TestCaseUtility.setObjectMap(collectionProtocol1, CollectionProtocol.class);
//			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 //assertFalse("could not add object", true);
//			 fail("could not add object"+e.getMessage());
		 }
	}
	
	
}
