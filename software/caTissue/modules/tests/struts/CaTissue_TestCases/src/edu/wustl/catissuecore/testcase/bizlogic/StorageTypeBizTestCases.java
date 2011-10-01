package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;


public class StorageTypeBizTestCases extends CaTissueSuiteBaseTest {
	
		AbstractDomainObject domainObject = null;
		public void testAddStorageType()
		{
			try{
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				StorageType storagetype = BaseTestCaseUtility.initStorageType();			
				System.out.println(storagetype);
				
				storagetype = (StorageType) appService.createObject(storagetype);
				BizTestCaseUtility.setObjectMap(storagetype, StorageType.class);
				System.out.println("Object created successfully");
				assertTrue("Object added successfully", true);
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
		
		public void testSearchStorageType()
		{
			StorageType storagetype = new StorageType();
	    	Logger.out.info(" searching domain object");
	    	storagetype.setId(new Long(1));
	   
	         try {
	        	 SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	        	 String query = "from edu.wustl.catissuecore.domain.StorageType as storagetype where "
	 				+ "storagetype.id= 1";	
	        	 List resultList = appService.search(query);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 StorageType returnedSite = (StorageType) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSite.getName());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
		 		
	          }
		}
		
		public void testUpdateStorageType()
		{
			StorageType storagetype =  BaseTestCaseUtility.initStorageType();
	    	Logger.out.info("updating domain object------->"+storagetype);
		    try 
			{
		    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		    	storagetype = (StorageType) appService.createObject(storagetype);
		    	BaseTestCaseUtility.updateStorageType(storagetype);	
		    	StorageType updatedStorageType = (StorageType) appService.updateObject(storagetype);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageType);
		       	assertTrue("Domain object successfully updated ---->"+updatedStorageType, true);
		    } 
		    catch (Exception e) {
		       	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		assertFalse(e.getMessage(), true);
		    }
		}
		
		
		
		public void testWithEmptyStorageTypeName()
		{
			try{
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				StorageType storagetype = BaseTestCaseUtility.initStorageType();		
				//te.setId(new Long("1"));
				storagetype.setName("");
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Empty storagetype name should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Name is required", true);
			 }
		}
		
		public void testWithDuplicateStorageTypeName()
		{
			try{
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				StorageType storagetype = BaseTestCaseUtility.initStorageType();	
				StorageType dupStorageTypeName = BaseTestCaseUtility.initStorageType();
				dupStorageTypeName.setName(storagetype.getName());
				storagetype = (StorageType) appService.createObject(storagetype); 
				dupStorageTypeName = (StorageType) appService.createObject(dupStorageTypeName); 
				assertFalse("Test Failed. Duplicate storagetype name should throw exception", true);
			}
			 catch(Exception e){
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertTrue("Submission failed since a storagetype with the same NAME already exists" , true);
				 
			 }
		}
		
		public void testWithNegativeDimensionCapacity_StorageType()
		{
			try{
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				StorageType storagetype = BaseTestCaseUtility.initStorageType();		
				//te.setId(new Long("1"));
				Capacity capacity = new Capacity();
				capacity.setOneDimensionCapacity(new Integer(-1));
				capacity.setTwoDimensionCapacity(new Integer(-1));
				storagetype.setCapacity(capacity);		
				
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Negative Dimension capacity should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Name is required", true);
			 }
		}
		
		public void testWithEmptyDimensionLabel_StorageType()
		{
			try{
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				StorageType storagetype = BaseTestCaseUtility.initStorageType();	
				storagetype.setOneDimensionLabel("");
				storagetype.setTwoDimensionLabel("");
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Empty Text Label should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Text Label For Dimension One is required", true);
			 }
		}
		
		/**
		 * Test Storage Type add with NULL object.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithNullObject()
		{
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;
			try
			{
				bizLogic.insert(null,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Object is NULL while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * Test Storage Type add with NULL specimen class.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithNullHoldsSpeciemnClass()
		{
			StorageType sType = BaseTestCaseUtility.initStorageType();
			sType.setHoldsSpecimenClassCollection(null) ;
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;

			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertTrue("StorageType Specimen Class is NULL while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail(e.getCustomizedMsg());
			}
		}
		/**
		 * Test Storage Type add with NULL dimension.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithNullDimension()
		{
			StorageType sType = BaseTestCaseUtility.initStorageType();
			sType.setOneDimensionLabel(null);
			sType.setTwoDimensionLabel(null);
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;

			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Dimension is NULL while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * Test Storage Type add with empty type.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithEmptyType()
		{
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;
			StorageType sType = BaseTestCaseUtility.initStorageType();
			sType.setName("") ;
			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Name is Empty while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * Test Storage Type add with empty one dimension capacity.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithEmptyOneDimensionCapacity()
		{
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;
			StorageType sType = BaseTestCaseUtility.initStorageType();
			Capacity cap = new Capacity() ;
			cap.setOneDimensionCapacity(Integer.valueOf(0)) ;
			cap.setTwoDimensionCapacity(Integer.valueOf(2)) ;
			sType.setCapacity(cap);
			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Name is Empty while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				e.printStackTrace();
			}
		}
		/**
		 * Test Storage Type add with empty Two dimension capacity.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithEmptyTwoDimensionCapacity()
		{
			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;
			StorageType sType = BaseTestCaseUtility.initStorageType();
			Capacity cap = new Capacity() ;
			cap.setOneDimensionCapacity(Integer.valueOf(1)) ;
			cap.setTwoDimensionCapacity(Integer.valueOf(0)) ;
			sType.setCapacity(cap);
			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Name is Empty while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				e.printStackTrace();
			}
		}
		/**
		 * Test Storage Type add with empty one dimension label.
		 * Negative Test Case.
		 */
		
		public void testStorageTypeBizLogicAddWithEmptyTwoDimensionLabel()
		{
//			//TODO
//			fail("Need to write test case");

			StorageTypeBizLogic bizLogic = new StorageTypeBizLogic() ;
			StorageType sType = BaseTestCaseUtility.initStorageType();
			sType.setTwoDimensionLabel("") ;
			try
			{
				bizLogic.insert(sType,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
				assertFalse("StorageType Name is Empty while inserting " +
						"through BizLogic",true);
			}
			catch (BizLogicException e)
			{
				logger.info("Exception in StorageType :" + e.getMessage());
				e.printStackTrace();
			}
		}
}
