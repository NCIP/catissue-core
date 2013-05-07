package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.bizlogic.AliquotBizLogic;
import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.common.util.logger.Logger;


public class AliquotBizTestCase extends CaTissueSuiteBaseTest
{
	/**
	 * Test Create Aliquots using Aliquot biz logic.
	 */
	public void testCreateAliquots()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			 SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			 System.out.println("SpecimenTestCases.testCreateAliquots(): " + scg);
			 specimenObj.setSpecimenCollectionGroup(scg);
			 Logger.out.info("Inserting domain object------->" + specimenObj);
			 System.out.println("Before Creating Tissue Specimen");
			 specimenObj.setCollectionStatus("Collected");
			 specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			 
			 Aliquot aliquot = BaseTestCaseUtility.initAliquot();
			 
			 aliquot.setSpecimen(specimenObj);
			 //Set container name to aliquot.
			 StorageContainer strCont = (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);
			 StorageContainer container = new StorageContainer();
			 container.setName(strCont.getName());
			 SpecimenPosition position = new SpecimenPosition();
			 position.setStorageContainer(container);
			 Collection<SpecimenPosition> specimenPositionColl = new LinkedHashSet<SpecimenPosition>();
			 specimenPositionColl.add(position);
			 aliquot.setSpecimenPositionCollection(specimenPositionColl);
			 System.out.println("Before Creating ALiquot");
			 
			 appService.createObject(aliquot);
			 System.out.println("After Creating ALiquot");
			 System.out.println("Success: Creating ALiquot : testCreateAliquots");
			 
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testCreateAliquots");
			 exp.printStackTrace();
			 assertFalse(exp.getMessage(),true) ;
		 }
	 }
	
	/**
	 * Test Create Aliquots using Aliquot biz logic.
	 */
	public void testAliquotsNullObject()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 AliquotBizLogic bizLogic = new AliquotBizLogic();
			 bizLogic.insert(null,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			 assertFalse("Null Aliqut Object should not be inserted ",true) ;
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testAliquotsWithNullObject");
			 exp.printStackTrace();
			 assertTrue(exp.getMessage(),true);
		 }
	 }
	/**
	 * Test Create Aliquots using Aliquot biz logic.
	 */
	public void testAliquotsWithNullSpecimen()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 AliquotBizLogic bizLogic = new AliquotBizLogic();
			 Aliquot aliquot = BaseTestCaseUtility.initAliquot();
			 
			 aliquot.setSpecimen(null);
			 bizLogic.insert(aliquot,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			 assertFalse("Aliqout with null parent specimen shoiuld not inserted through Bizlogic",true) ;			 
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testAliquotsWithNullSpecimen");
			 exp.printStackTrace();
			 assertTrue(exp.getMessage(),true);
		 }
	 }
	/**
	 * Test Create Aliquots using Aliquot biz logic.
	 */
	public void testAliquotsWithInvalidCount()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 AliquotBizLogic bizLogic = new AliquotBizLogic();
			 Aliquot aliquot = BaseTestCaseUtility.initAliquot();
			 aliquot.setCount(0);
			 aliquot.setSpecimen(null);
			 bizLogic.insert(aliquot,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			 assertFalse("Aliqout with invalid count shoiuld not inserted through Bizlogic",true) ;			 
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testAliquotsWithNullSpecimen");
			 exp.printStackTrace();
			 assertTrue(exp.getMessage(),true);
		 }
	 }
	/**
	 * Test Create Aliquots using Aliquot biz logic.
	 */
	public void testAliquotsWithInvalidQuantity()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 AliquotBizLogic bizLogic = new AliquotBizLogic();
			 Aliquot aliquot = BaseTestCaseUtility.initAliquot();
			 aliquot.setQuantityPerAliquot(-1.0);
			 aliquot.setSpecimen(null);
			 bizLogic.insert(aliquot,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			 assertFalse("Aliqout with invalid count shoiuld not inserted through Bizlogic",true) ;			 
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testAliquotsWithNullSpecimen");
			 exp.printStackTrace();
			 assertTrue(exp.getMessage(),true);
		 }
	 }
	
	public void testCreateAliquotsWithZeroQuantity()
	 {
		 Logger.out.info("creating aliquot: testCreateAliquots ------->");
		 try
		 {
			 TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			 SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			 System.out.println("SpecimenTestCases.testCreateAliquots(): " + scg);
			 specimenObj.setSpecimenCollectionGroup(scg);
			 Logger.out.info("Inserting domain object------->" + specimenObj);
			 System.out.println("Before Creating Tissue Specimen");
			 specimenObj.setCollectionStatus("Collected");
			 specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
			 
			 Aliquot aliquot = BaseTestCaseUtility.initAliquot();
			 aliquot.setQuantityPerAliquot(0.0);
			 aliquot.setSpecimen(specimenObj);
			 //Set container name to aliquot.
			 StorageContainer strCont = (StorageContainer) TestCaseUtility.getObjectMap(StorageContainer.class);
			 StorageContainer container = new StorageContainer();
			 container.setName(strCont.getName());
			 SpecimenPosition position = new SpecimenPosition();
			 position.setStorageContainer(container);
			 Collection<SpecimenPosition> specimenPositionColl = new LinkedHashSet<SpecimenPosition>();
			 specimenPositionColl.add(position);
			 aliquot.setSpecimenPositionCollection(specimenPositionColl);
			 System.out.println("Before Creating ALiquot");
			 
			 appService.createObject(aliquot);
			 System.out.println("After Creating ALiquot");
			 System.out.println("Success: Creating ALiquot : testCreateAliquots");
			 
		 }
		 catch (Exception exp)
		 {
			 System.out.println("Error: Creating ALiquot : testCreateAliquots");
			 exp.printStackTrace();
			 assertFalse(exp.getMessage(),true);
		 }
	 }
}
