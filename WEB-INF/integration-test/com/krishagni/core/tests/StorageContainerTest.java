package com.krishagni.core.tests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.StorageContainerTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class StorageContainerTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private StorageContainerService scSvc;
	
	@Autowired
	private ApplicationContext appctx;
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	private void validateUser(UserSummary user, Long id, String firstName, String lastName, String loginName) {
		Assert.assertEquals("Userid mismatch", user.getId(), id);
		Assert.assertEquals("Firstname mismatch", user.getFirstName(), firstName);
		Assert.assertEquals("Lastname mismatch", user.getLastName(), lastName);
		Assert.assertEquals("LoginName mismatch", user.getLoginName(), loginName);
	}
	
	/*
	 * Create Storage Container Api tests
	 */
	@Test
	@DatabaseSetup("storage-container-test/create-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/create-sc-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addStorageContainerTest() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));

		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Container details can't be null", container);
		Assert.assertEquals("container id not generated!", true, container.getId() > 0L );
		Assert.assertEquals("Temprature mismatch", input.getTemperature(), container.getTemperature());
		Assert.assertEquals("One dim labelling scheme mismatch", input.getDimensionOneLabelingScheme(), 
				container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Two dim labelling scheme mismatch", input.getDimensionTwoLabelingScheme(), 
				container.getDimensionTwoLabelingScheme());
		Assert.assertEquals("One dim capacity mismatch", input.getDimensionOneCapacity(), container.getDimensionOneCapacity());
		Assert.assertEquals("Two dim capacity mismatch", input.getDimensionTwoCapacity(), container.getDimensionTwoCapacity());
		Assert.assertEquals("ActivityStatus mismatch" , "Active" , container.getActivityStatus());
		Assert.assertEquals("Free positions mismatch" , 4L, container.getFreePositions());
		Assert.assertNull("Child container should be empty!", container.getChildContainers());
		Assert.assertEquals("Allowed specimen type should be empty!", 3L, container.getAllowedSpecimenTypes().size());
		Assert.assertEquals("Bile specimen type not found", true, container.getAllowedSpecimenTypes().contains("Bile"));
		Assert.assertEquals("Dna specimen type not found", true, container.getAllowedSpecimenTypes().contains("DNA"));
		Assert.assertEquals("Bone marrow plasma type not found", true, container.getAllowedSpecimenTypes().contains("Bone Marrow Plasma"));
		
		Assert.assertEquals("Allowed speicmen class should be empty!", 3L, container.getAllowedSpecimenClasses().size());
		Assert.assertEquals("Cell class not found", true, container.getAllowedSpecimenClasses().contains("Cell"));
		Assert.assertEquals("Fluid class not found", true, container.getAllowedSpecimenClasses().contains("Fluid"));
		Assert.assertEquals("Molecular class not found", true, container.getAllowedSpecimenClasses().contains("Molecular"));
		
		Assert.assertEquals("Allowed cp's should be empty!", 3L, container.getAllowedCollectionProtocols().size());
		Assert.assertEquals("short-title-1 not found", true, container.getAllowedCollectionProtocols().contains("short-title-1"));
		Assert.assertEquals("short-title-2 not found", true, container.getAllowedCollectionProtocols().contains("short-title-2"));
		Assert.assertEquals("short-title-3 not found", true, container.getAllowedCollectionProtocols().contains("short-title-3"));
		
		Assert.assertEquals("Free-positions mismatch", new Integer(4), new Integer(container.getFreePositions()));
		Assert.assertNull("Storage position should be null!", container.getPosition());
		Assert.assertEquals("container name mismatch", input.getName(), container.getName());
		Assert.assertEquals("barcode mismatch", input.getBarcode(), container.getBarcode());
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
		validateUser(container.getCreatedBy(), 1L , "ADMIN" , "ADMIN" , "admin@admin.com");
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
	}
	
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/create-child-sc-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addChildStorageContainerTest() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Container details can't be null", container);
		Assert.assertEquals("container id not generated!", true, container.getId() > 0L );
		Assert.assertEquals("Temprature mismatch", input.getTemperature(), container.getTemperature());
		Assert.assertEquals("One dim labelling scheme mismatch", input.getDimensionOneLabelingScheme(), 
				container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Two dim labelling scheme mismatch", input.getDimensionTwoLabelingScheme(), 
				container.getDimensionTwoLabelingScheme());
		Assert.assertEquals("One dim capacity mismatch", input.getDimensionOneCapacity(), container.getDimensionOneCapacity());
		Assert.assertEquals("Two dim capacity mismatch", input.getDimensionTwoCapacity(), container.getDimensionTwoCapacity());
		Assert.assertEquals("ActivityStatus mismatch" , "Active" , container.getActivityStatus());
		Assert.assertEquals("Free positions mismatch" , 4L, container.getFreePositions());
		Assert.assertNull("Child container should be empty!", container.getChildContainers());
		Assert.assertEquals("Allowed specimen type should be empty!", 3L, container.getAllowedSpecimenTypes().size());
		Assert.assertEquals("Bile specimen type not found", true, container.getAllowedSpecimenTypes().contains("Bile"));
		Assert.assertEquals("Dna specimen type not found", true, container.getAllowedSpecimenTypes().contains("DNA"));
		Assert.assertEquals("Bone marrow plasma type not found", true, container.getAllowedSpecimenTypes().contains("Bone Marrow Plasma"));
		
		Assert.assertEquals("Allowed speicmen class should be empty!", 3L, container.getAllowedSpecimenClasses().size());
		Assert.assertEquals("Cell class not found", true, container.getAllowedSpecimenClasses().contains("Cell"));
		Assert.assertEquals("Fluid class not found", true, container.getAllowedSpecimenClasses().contains("Fluid"));
		Assert.assertEquals("Molecular class not found", true, container.getAllowedSpecimenClasses().contains("Molecular"));
		
		Assert.assertEquals("Allowed cp's should be empty!", 3L, container.getAllowedCollectionProtocols().size());
		Assert.assertEquals("short-title-1 not found", true, container.getAllowedCollectionProtocols().contains("short-title-1"));
		Assert.assertEquals("short-title-2 not found", true, container.getAllowedCollectionProtocols().contains("short-title-2"));
		Assert.assertEquals("short-title-3 not found", true, container.getAllowedCollectionProtocols().contains("short-title-3"));
		
		Assert.assertEquals("container name mismatch", input.getName(), container.getName());
		Assert.assertEquals("barcode mismatch", input.getBarcode(), container.getBarcode());
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
		validateUser(container.getCreatedBy(), 1L , "ADMIN" , "ADMIN" , "admin@admin.com");
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-in-already-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildStorageContainerInAlreadyOccupiedPosition() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("test-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.setPosition(StorageContainerTestData.getPosition("a", "a")); //this location is already occupied in the db setup!
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NO_FREE_SPACE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-in-already-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildContainerInvalidPositionOne() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("test-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.setPosition(StorageContainerTestData.getPosition("j", "a")); //'j' dones't exists in 2*2 container 
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NO_FREE_SPACE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-in-already-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildContainerInvalidPositionTwo() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("test-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.setPosition(StorageContainerTestData.getPosition("a", "j")); //'j' dones't exists in 2*2 container 
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NO_FREE_SPACE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-in-already-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildContainerInvalidPositionOneAndTwo() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("test-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.setPosition(StorageContainerTestData.getPosition("i", "j")); //'i' and 'j' dones't exists in 2*2 container 
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NO_FREE_SPACE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-in-fully-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildContainerInAFullyOccupiedParentContainer() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("test-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NO_FREE_SPACE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addStorageContainerWithInvalidInputs() {
		/*
		 * contains negative senarios for
		 * empty container name
		 * invalid dimension capacity
		 * invalid labelling scheme
		 * invalid site and invalid parent container
		 * invalid created by
		 * invalid activitystatus
		 */
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		input.setName("");
		input.setDimensionOneCapacity(-1);
		input.setDimensionTwoCapacity(0);
		input.setDimensionOneLabelingScheme("invalid-labelling-scheme");
		input.setDimensionTwoLabelingScheme("invalid-labelling-scheme");
		input.setSiteName("invalid-sitename");
		input.setParentContainerName("invalid-parent-container");
		input.setCreatedBy(CommonUtils.getUser(-1L, "", "", ""));
		input.setActivityStatus("invalid-activity-status");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.REQUIRED_SITE_OR_PARENT_CONT, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, UserErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void addChildStorageContainerWithInvalidParentContainer() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		input.setParentContainerName("FREEZER-1001121"); 
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.PARENT_CONT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test 
	@DatabaseSetup("storage-container-test/create-child-occupy-first-available-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/create-child-occupy-first-available-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addChildContainerOccupyFirstAvailablePosition() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.getAllowedCollectionProtocols().clear();
		input.setBarcode("random-barcode-cc-dd-ee-ff");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		StorageContainerDetail output = resp.getPayload();
		Assert.assertEquals("position1 mismatch: ", "b", output.getPosition().getPosOne());
		Assert.assertEquals("position2 mismatch: ", "a", output.getPosition().getPosTwo());
	}
	
	@Test 
	@DatabaseSetup("storage-container-test/create-child-occupy-the-only-available-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/create-child-occupy-the-only-available-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addChildContainerOccupyTheOnlyAvailablePosition() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("default-container", 2, 2);
		input.setParentContainerName("FREEZER-100");
		input.getAllowedCollectionProtocols().clear();
		input.setBarcode("random-barcode-cc-dd-ee-ff");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.createStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		StorageContainerDetail output = resp.getPayload();
		Assert.assertEquals("position1 mismatch: ", "a", output.getPosition().getPosOne());
		Assert.assertEquals("position2 mismatch: ", "a", output.getPosition().getPosTwo());
	}
	
	//TODO: add testcase for allowed specimen class, allowed type and allowed cps once validPV's are in place
	
	/*
	 * Update Container Api tests
	 */
	@Test
	@DatabaseSetup("storage-container-test/update-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-sc-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerBasicAttributes() {
		/*
		 * This test methods updates the following attributes of storage container
		 * name, barcode, temprature, comments
		 */
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("updated-container", 2, 2);
		input.setId(1L);
		input.setBarcode("updated-barcode");
		input.setTemperature(77.0D);
		input.setComments("updated-comments");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Container details can't be null", container);
		Assert.assertEquals("container id not generated!", true, container.getId() > 0L );
		Assert.assertEquals("Temprature mismatch", input.getTemperature(), container.getTemperature());
		Assert.assertEquals("One dim labelling scheme mismatch", input.getDimensionOneLabelingScheme(), 
				container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Two dim labelling scheme mismatch", input.getDimensionTwoLabelingScheme(), 
				container.getDimensionTwoLabelingScheme());
		Assert.assertEquals("One dim capacity mismatch", input.getDimensionOneCapacity(), container.getDimensionOneCapacity());
		Assert.assertEquals("Two dim capacity mismatch", input.getDimensionTwoCapacity(), container.getDimensionTwoCapacity());
		Assert.assertEquals("ActivityStatus mismatch" , "Active" , container.getActivityStatus());
		Assert.assertEquals("Free positions mismatch" , 4L, container.getFreePositions());
		Assert.assertNull("Child container should be empty!", container.getChildContainers());

		Assert.assertEquals("Allowed specimen type should be empty!", 0L, container.getAllowedSpecimenTypes().size());
		Assert.assertEquals("Allowed speicmen class should be empty!", 0L, container.getAllowedSpecimenClasses().size());
		Assert.assertEquals("Allowed cp's should be empty!", 0L, container.getAllowedCollectionProtocols().size());
		
		Assert.assertEquals("Free-positions mismatch", new Integer(4), new Integer(container.getFreePositions()));
		Assert.assertNull("Storage position should be null!", container.getPosition());
		Assert.assertEquals("container name mismatch", input.getName(), container.getName());
		Assert.assertEquals("barcode mismatch", input.getBarcode(), container.getBarcode());
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
		validateUser(container.getCreatedBy(), 1L , "ADMIN" , "ADMIN" , "admin@admin.com");
		Assert.assertEquals("comments mismatch", input.getComments(), container.getComments());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-sc-capacity-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerUpdateStorageCapaicty() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("updated-container", 2, 2);
		input.setId(1L);
		input.setDimensionOneCapacity(4);
		input.setDimensionTwoCapacity(4);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertEquals("dimension one capacity mismatch" , 4, container.getDimensionOneCapacity());
		Assert.assertEquals("dimension one capacity mismatch" , 4, container.getDimensionTwoCapacity());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/create-child-in-fully-occupied-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/create-child-in-fully-occupied-initial.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkFilledupPositions() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("updated-container", 2, 2);
		input.setId(1L);
		input.setDimensionOneCapacity(1);
		input.setDimensionTwoCapacity(4);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("random-barcode-asdf-1001-213-1243");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.CANNOT_SHRINK_CONTAINER, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-sc-capacity-1x3-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkCapacity() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("updated-container", 2, 2);
		input.setId(1L);
		input.setDimensionOneCapacity(1);
		input.setDimensionTwoCapacity(3);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertEquals("dimension one capacity mismatch" , 1, container.getDimensionOneCapacity());
		Assert.assertEquals("dimension one capacity mismatch" , 3, container.getDimensionTwoCapacity());
	}

	@Test
	@DatabaseSetup("storage-container-test/update-container-partially-filled-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-container-partially-filled-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupied() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("updated-container", 2, 2);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneCapacity(2);
		input.setDimensionTwoCapacity(2);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		StorageContainerDetail container = resp.getPayload();
		Assert.assertEquals("Position1 mismatch!", 2, container.getDimensionOneCapacity());
		Assert.assertEquals("Position1 mismatch!", 2, container.getDimensionTwoCapacity());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-numbers-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-uppercaps-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedNumberingToUpperCaps() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.UPPER_CASE_ALPHA_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.UPPER_CASE_ALPHA_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.UPPER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.UPPER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-uppercaps-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-lowercaps-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedUpperCapsToLowerCaps() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-uppercaps-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-numbers-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedUpperCapsToNum() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.NUMBER_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.NUMBER_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-numbers-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-roman-upper-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedNumberingToUpperRoman() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.UPPER_CASE_ROMAN_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.UPPER_CASE_ROMAN_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.UPPER_CASE_ROMAN_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.UPPER_CASE_ROMAN_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-roman-upper-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-numbers-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedUpperRomanToNumbering() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.NUMBER_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.NUMBER_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-labelling-roman-upper-initial.xml") 
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-labelling-lowercaps-initial.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateStorageContainerShrinkPartiallyOccupiedUpperRomanToLowerAlpha() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 200, 200);
		input.setId(1L);
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setDimensionOneLabelingScheme(StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME);
		input.setDimensionTwoLabelingScheme(StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME);
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		StorageContainerDetail container = resp.getPayload();
	
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Dimension one labelling mismatch: ", 
				StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, container.getDimensionTwoLabelingScheme());
		
		for (int i=0; i<4; i++) {
			//the containers are stored from 5831 onwards in the parent rack
			Assert.assertEquals("Integer was not found" , true, container.getOccupiedPositions().contains(i+5831));
		}
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-site-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-site-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateContainerMoveToDifferentLocation() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("CHILD-CONTAINER", 2, 2);
		input.setId(3L);
		input.setName("CHILD-CONTAINER");
		input.setBarcode("BARCODE-102");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setParentContainerName("FREEZER-101");
		input.setSiteName("SITE2");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertEquals("parent container mismatch!", "FREEZER-101", container.getParentContainerName());
		Assert.assertEquals("dimension one capacity mismatch" , "SITE2", container.getSiteName());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-site-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-site-moveout-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateContainerMoveOutStorageContainer() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("CHILD-CONTAINER", 2, 2);
		input.setId(3L);
		input.setName("CHILD-CONTAINER");
		input.setBarcode("BARCODE-102");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setParentContainerName("");
		input.setSiteName("SITE2");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNull("parent container mismatch!", container.getParentContainerName());
		Assert.assertEquals("dimension one capacity mismatch" , "SITE2", container.getSiteName());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-sc-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/disable-sc-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateContainerDisableStorageContainer() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setActivityStatus("Disabled");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertEquals("dimension one capacity mismatch" , "Disabled", container.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("storage-container-test/disable-occupied-container-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void updateContainerDisableOccupiedBySpecimen() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.setActivityStatus("Disabled");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.REF_ENTITY_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-allowed-types-classes-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-allowed-types-classes-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void udpateAllowedSpecimenClassAndTypes() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		input.getAllowedSpecimenTypes().add("Cerebrospinal Fluid");
		input.getAllowedSpecimenTypes().add("Bile");
		input.getAllowedSpecimenTypes().add("Buffy coat");
		
		input.getAllowedSpecimenClasses().add("Tissue");
		input.getAllowedSpecimenClasses().add("Dry Tissue");
		input.getAllowedSpecimenClasses().add("Molecular");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenTypes().contains("Cerebrospinal Fluid"));
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenTypes().contains("Bile"));
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenTypes().contains("Buffy coat"));
		
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenClasses().contains("Tissue"));
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenClasses().contains("Dry Tissue"));
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedSpecimenClasses().contains("Molecular"));
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-allowed-types-classes-specimen-voilates-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void udpateAllowedSpecimenClassAndTypesSpecimenVoilates() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		input.getAllowedSpecimenTypes().add("Cerebrospinal Fluid");
		input.getAllowedSpecimenTypes().add("Bile");
		input.getAllowedSpecimenTypes().add("Buffy coat");
		
		input.getAllowedSpecimenClasses().add("Tissue");
		input.getAllowedSpecimenClasses().add("Dry Tissue");
		input.getAllowedSpecimenClasses().add("Molecular");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.RESTRICTIVE_SPECIMEN_CLASS_AND_TYPE, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("storage-container-test/update-allowed-cps-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	@ExpectedDatabase(value="storage-container-test/update-allowed-cps-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateAllowedCps() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		input.getAllowedCollectionProtocols().add("short-title-3");
		input.getAllowedCollectionProtocols().add("short-title-4");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedCollectionProtocols().contains("short-title-3"));
		Assert.assertEquals("Allowed specimen class not found", true , container.getAllowedCollectionProtocols().contains("short-title-4"));
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-allowed-specimen-voilates-cp-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void updateAllowedCpsSpecimenVoilatesAllowedCps() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		
		input.getAllowedCollectionProtocols().add("short-title-3");
		input.getAllowedCollectionProtocols().add("short-title-4");
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.RESTRICTIVE_CP, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-sc-check-for-cycle.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void updateScCheckForCycles() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setParentContainerName("child-2");
		
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.HIERARCHY_CONTAINS_CYCLE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/update-sc-check-for-cycle.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void updateScCheckForCyclesForSameNode() {
		StorageContainerDetail input = StorageContainerTestData.getStorageContainer("FREEZER-100", 2, 2);
		input.setId(1L);
		input.setName("FREEZER-100");
		input.setBarcode("BARCODE-100");
		input.setTemperature(20.0D);
		input.setComments("DEFAULT-COMMENTS");
		input.getAllowedSpecimenClasses().clear();
		input.getAllowedCollectionProtocols().clear();
		input.getAllowedSpecimenTypes().clear();
		input.setParentContainerName("FREEZER-100");
		
		
		ResponseEvent<StorageContainerDetail> resp = scSvc.updateStorageContainer(getRequest(input));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.HIERARCHY_CONTAINS_CYCLE, ErrorType.USER_ERROR);
	}
	
	
	/*
	 * Get Storage Container API tests
	 */
	@Test
	@DatabaseSetup("storage-container-test/get-storage-container-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void getStorageContainerTest() {
		ResponseEvent<StorageContainerDetail> resp = scSvc.getStorageContainer(getRequest(1L));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		StorageContainerDetail container = resp.getPayload();
		Assert.assertNotNull("Container details can't be null", container);
		Assert.assertEquals("container id not generated!", true, container.getId() > 0L );
		Assert.assertEquals("Temprature mismatch", new Double(20.0), container.getTemperature());
		Assert.assertEquals("One dim labelling scheme mismatch", StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, 
				container.getDimensionOneLabelingScheme());
		Assert.assertEquals("Two dim labelling scheme mismatch", StorageContainer.LOWER_CASE_ALPHA_LABELING_SCHEME, 
				container.getDimensionTwoLabelingScheme());
		Assert.assertEquals("One dim capacity mismatch", 2, container.getDimensionOneCapacity());
		Assert.assertEquals("Two dim capacity mismatch", 2, container.getDimensionTwoCapacity());
		Assert.assertEquals("ActivityStatus mismatch" , "Active" , container.getActivityStatus());
		Assert.assertEquals("Free positions mismatch" , 0L, container.getFreePositions());
		Assert.assertNull("Child container should be empty!", container.getChildContainers());
		
		Assert.assertEquals("Allowed specimen type should be empty!", 3L, container.getAllowedSpecimenTypes().size());
		Assert.assertEquals("Bile specimen type not found", true, container.getAllowedSpecimenTypes().contains("Bile"));
		Assert.assertEquals("Dna specimen type not found", true, container.getAllowedSpecimenTypes().contains("DNA"));
		Assert.assertEquals("Bone marrow plasma type not found", true, container.getAllowedSpecimenTypes().contains("Bone Marrow Plasma"));
		
		Assert.assertEquals("Allowed speicmen class should be empty!", 3L, container.getAllowedSpecimenClasses().size());
		Assert.assertEquals("Cell class not found", true, container.getAllowedSpecimenClasses().contains("Cell"));
		Assert.assertEquals("Fluid class not found", true, container.getAllowedSpecimenClasses().contains("Fluid"));
		Assert.assertEquals("Molecular class not found", true, container.getAllowedSpecimenClasses().contains("Molecular"));
		
		Assert.assertEquals("Allowed cp's should be empty!", 3L, container.getAllowedCollectionProtocols().size());
		Assert.assertEquals("short-title-1 not found", true, container.getAllowedCollectionProtocols().contains("short-title-1"));
		Assert.assertEquals("short-title-2 not found", true, container.getAllowedCollectionProtocols().contains("short-title-2"));
		Assert.assertEquals("short-title-3 not found", true, container.getAllowedCollectionProtocols().contains("short-title-3"));
		
		Assert.assertEquals("position 1 not occupied", true, container.getOccupiedPositions().contains(1));
		Assert.assertEquals("position 1 not occupied", true, container.getOccupiedPositions().contains(2));
		Assert.assertEquals("position 1 not occupied", true, container.getOccupiedPositions().contains(3));
		Assert.assertEquals("position 1 not occupied", true, container.getOccupiedPositions().contains(4));
		
		Assert.assertNull("Storage position should be null!", container.getPosition());
		Assert.assertEquals("container name mismatch", "FREEZER-100", container.getName());
		Assert.assertEquals("barcode mismatch", "BARCODE-100", container.getBarcode());
		Assert.assertEquals("comments mismatch", "DEFAULT-COMMENTS", container.getComments());
		validateUser(container.getCreatedBy(), 1L , "ADMIN" , "ADMIN" , "admin@admin.com");
	}
	
	@Test
	public void getNonExistingContainer() {
		ResponseEvent<StorageContainerDetail> resp = scSvc.getStorageContainer(getRequest(1L));
		
		TestUtils.recordResponse(resp); 
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, StorageContainerErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("storage-container-test/get-storage-containers-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void getStorageContainers() {
		StorageContainerListCriteria input = new StorageContainerListCriteria();
		input.topLevelContainers(true);
		ResponseEvent<List<StorageContainerSummary>> resp = scSvc.getStorageContainers(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Count of parent containes mismatch!", 3, resp.getPayload().size());
		
		Set<String> containers = new HashSet<String>();
		
		for (StorageContainerSummary summary : resp.getPayload()) {
			containers.add(summary.getName());
		}
		
		Assert.assertEquals("Container 100 not found: ", true, containers.contains("FREEZER-100"));
		Assert.assertEquals("Container 101 not found: ", true, containers.contains("FREEZER-101"));
		Assert.assertEquals("Container 102 not found: ", true, containers.contains("FREEZER-102"));
	}
	
	@Test
	@DatabaseSetup("storage-container-test/get-storage-containers-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void getContainersBySiteName() {
		StorageContainerListCriteria input = new StorageContainerListCriteria();
		input.siteName("SITE2");
		ResponseEvent<List<StorageContainerSummary>> resp = scSvc.getStorageContainers(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Count of parent containes mismatch!", 2, resp.getPayload().size());
		
		Set<String> containers = new HashSet<String>();
		
		for (StorageContainerSummary summary : resp.getPayload()) {
			containers.add(summary.getName());
		}
		
		Assert.assertEquals("Container 101 not found: ", true, containers.contains("FREEZER-101"));
		Assert.assertEquals("Container 102 not found: ", true, containers.contains("FREEZER-102"));
	}
	
	@Test
	@DatabaseSetup("storage-container-test/get-storage-containers-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void getContainersByParent() {
		StorageContainerListCriteria input = new StorageContainerListCriteria();
		input.parentContainerId(1L);
		ResponseEvent<List<StorageContainerSummary>> resp = scSvc.getStorageContainers(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Count of parent containes mismatch!", 2, resp.getPayload().size());
		
		Set<String> containers = new HashSet<String>();
		
		for (StorageContainerSummary summary : resp.getPayload()) {
			containers.add(summary.getName());
		}
		
		Assert.assertEquals("Container 101 not found: ", true, containers.contains("child-1"));
		Assert.assertEquals("Container 102 not found: ", true, containers.contains("child-2"));
	}
	
	@Test
	@DatabaseSetup("storage-container-test/get-storage-containers-initial.xml")
	@DatabaseTearDown("storage-container-test/generic-teardown.xml")
	public void getStorageContainersByName() {
		StorageContainerListCriteria input = new StorageContainerListCriteria();
		input.query("FREEZER");
		ResponseEvent<List<StorageContainerSummary>> resp = scSvc.getStorageContainers(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertNotNull("Response not exepcted to be null!", resp);
		Assert.assertEquals("Operation failured", true, resp.isSuccessful());
		
		Assert.assertEquals("Count of parent containes mismatch!", 3, resp.getPayload().size());
		
		Set<String> containers = new HashSet<String>();
		
		for (StorageContainerSummary summary : resp.getPayload()) {
			containers.add(summary.getName());
		}
		
		Assert.assertEquals("Container 100 not found: ", true, containers.contains("FREEZER-100"));
		Assert.assertEquals("Container 101 not found: ", true, containers.contains("FREEZER-101"));
		Assert.assertEquals("Container 102 not found: ", true, containers.contains("FREEZER-102"));
	}
}
