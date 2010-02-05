package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Map;

import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.AssignDataException;


public class SpecimenArrayTestCase extends CaTissueSuiteBaseTest
{

	public void testSpecimenArrayAdd()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("TissueSpecimen");

//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getLabel());
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		logger.info("######################################"+getActualForward());
		setRequestPathInfo(getActualForward());

		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "20");

		actionPerform();
		verifyNoActionErrors();

		logger.info("######################################"+getActualForward());
		setRequestPathInfo(getActualForward());

		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "20");

		actionPerform();
		verifyNoActionErrors();

		arrayForm=(SpecimenArrayForm)getActionForm();

		SpecimenArray specimenArray=new SpecimenArray();
		specimenArray.setId(arrayForm.getId());
		specimenArray.setName(arrayForm.getName());

		try
		{
			specimenArray.setAllValues(arrayForm);
		}
		catch (AssignDataException e)
		{
			logger.debug(e.getMessage(),e);
			fail("failed to assign values to Specimen Array");
		}

		TestCaseUtility.setNameObjectMap("SpecimenArrayObject",specimenArray);
		logger.info("----specimenArray Added successfully");
	}

	public void testSpecimenArrayaliquot()
	{
		SpecimenArray SpecimenArray = (SpecimenArray) TestCaseUtility.getNameObjectMap("SpecimenArrayObject");
		setRequestPathInfo("/SpecimenArrayAliquots");
		addRequestParameter("pageOf", "pageOfSpecimenArrayAliquot");
		actionPerform();
		verifyForward("pageOfSpecimenArrayAliquot");
		verifyNoActionErrors();

		SpecimenArrayAliquotForm specimenArrayAliquotForm = (SpecimenArrayAliquotForm)getActionForm();
		specimenArrayAliquotForm.setParentSpecimenArrayLabel(SpecimenArray.getName());
		specimenArrayAliquotForm.setAliquotCount("1");


		addRequestParameter("pageOf", "pageOfSpecimenArrayCreateAliquot");
		addRequestParameter("buttonClicked", "submit");
		addRequestParameter("menuSelected", "20");
		setActionForm(specimenArrayAliquotForm);
		setRequestPathInfo("/SpecimenArrayCreateAliquots");

		actionPerform();
		verifyForward("pageOfSpecimenArrayCreateAliquot");
		verifyNoActionErrors();

		specimenArrayAliquotForm = (SpecimenArrayAliquotForm)getActionForm();
		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		Map aliquotMap = specimenArrayAliquotForm.getSpecimenArrayAliquotMap();
		aliquotMap.put( "radio_1", "1" );
//		aliquotMap.put( "SpecimenArray:1_label", specimenArrayAliquotForm.get );
		aliquotMap.put( "Specimen:1_quantity", "1" );
		aliquotMap.put( "SpecimenArray:1_StorageContainer_id", ""+container.getId());
		aliquotMap.put( "SpecimenArray:1_positionDimensionOne", "22" );
		aliquotMap.put( "SpecimenArray:1_positionDimensionTwo", "22" );

		specimenArrayAliquotForm.setSpecimenArrayAliquotMap(aliquotMap);
		specimenArrayAliquotForm.setSubmittedFor("ForwardTo");

		addRequestParameter("pageOf", "pageOfSpecimenArrayCreateAliquot");
		addRequestParameter("buttonClicked", "create");
		addRequestParameter("menuSelected", "20");
		addRequestParameter("operation", "add");
		setActionForm(specimenArrayAliquotForm);
		setRequestPathInfo("/SpecimenArrayCreateAliquots");

		actionPerform();
		verifyForward("commonAddEdit");

		verifyNoActionErrors();

		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfSpecimenArrayAliquotSummary");
		addRequestParameter("operation", "add");
		addRequestParameter("buttonClicked", "none");
		addRequestParameter("menuSelected", "20");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}

	public void testSpecimenArrayAddWithoutSpecimen()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("TissueSpecimen");

//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		verifyForward("failure");
		String [] errMsg=new String[] {"errors.item"};
		verifyActionErrors(errMsg);
		assertEquals(SpecimenArrayForm.class.getName(),getActionForm().getClass().getName());

	}

	public void testSpecimenArrayAddWithWrongSpecimen()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");


//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", "abcd567");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		verifyForward("failure");
		String [] errMsg=new String[] {"errors.item"};
		verifyActionErrors(errMsg);
		assertEquals(SpecimenArrayForm.class.getName(),getActionForm().getClass().getName());

	}

	public void testSpecimenArrayAddWithWrongSpecimenType()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");


//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);specimen

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("specimen");

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getLabel());
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		verifyForward("failure");
		String [] errMsg=new String[] {"errors.item"};
		verifyActionErrors(errMsg);
		assertEquals(SpecimenArrayForm.class.getName(),getActionForm().getClass().getName());

	}

	public void testSpecimenArrayAddWithStoragePosition()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();


		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
	//	arrayForm.setStorageContainer("");

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");


//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);specimen

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("specimen");

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getLabel());
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		//verifyForward("/SpecimenArray.do?operation=add&amp;menuSelected=20");
		String [] errMsg=new String[] {"errors.item.format"};//"array.positionInStorageContainer"};
		verifyActionErrors(errMsg);
		assertEquals(SpecimenArrayForm.class.getName(),getActionForm().getClass().getName());

	}

	public void testSpecimenArrayAddWithDuplicateLabel()
	{

		//selecting the specimen array add action
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		SpecimenArrayType specimenArrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");

		//selecting array type
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();
		arrayForm.setSpecimenArrayTypeId(specimenArrayType.getId());
		arrayForm.setSubOperation("ChangeArraytype");
		addRequestParameter("menuSelected", "20");
		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("no");
		arrayForm.setForwardTo("success");
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArray");

		actionPerform();
//		verifyForward("success");
		verifyNoActionErrors();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request
		arrayForm=(SpecimenArrayForm)getActionForm();

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());

		arrayForm.setOperation("add");
		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");


//		Collection specimenArrContColl=new HashSet();
//		List specList=new ArrayList();
//		specList.add(specimen.getId());
//		specimenArrContColl.add(specList);
//		arrayForm.setSpecArrayContentCollection(specimenArrContColl);specimen

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("specimen");

		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getLabel());
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");
		//arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getId());

		SpecimenArray specimenArray=(SpecimenArray)TestCaseUtility.getObjectMap().get("SpecimenArrayObject");
		arrayForm.setName(specimenArray.getName());
		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		setActionForm(arrayForm);
		setRequestPathInfo("/SpecimenArrayAdd");


		actionPerform();
		verifyForward("failure");
		String [] errMsg=new String[] {"errors.item"};
		verifyActionErrors(errMsg);
		assertEquals(SpecimenArrayForm.class.getName(),getActionForm().getClass().getName());

	}


	public void testSpecimenArrayEdit()
	{
		SpecimenArray specimenArray=(SpecimenArray)TestCaseUtility.getObjectMap().get("SpecimenArrayObject");

		//Retrieving specimenArray object for edit
		logger.info("----specimenArray ID : " + specimenArray.getId());
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "search");
		addRequestParameter("id", specimenArray.getId().toString());
		setRequestPathInfo("/SearchObject") ;
		actionPerform();
		verifyForward("pageOfSpecimenArray");
		verifyNoActionErrors();

		setRequestPathInfo("/SpecimenArraySearch");

		actionPerform();
		verifyNoActionErrors();

		setRequestPathInfo(getActualForward());

		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "20");
		actionPerform();
		verifyNoActionErrors();

		//getting the populated SpecimenArrayForm
		SpecimenArrayForm arrayForm=(SpecimenArrayForm)getActionForm();

		StorageContainer container=(StorageContainer)TestCaseUtility.getObjectMap().get("SpecimenarrayStorageContainer");

		//Submitting the SpecimenArrayAdd request

		arrayForm.setPositionDimensionOne(1);
		arrayForm.setPositionDimensionTwo(1);
		//arrayForm.setContainerId(container.getId().toString());
		arrayForm.setStorageContainer(container.getId().toString());


		arrayForm.setName("newArray_"+UniqueKeyGeneratorUtil.getUniqueKey());
		arrayForm.setOperation("edit");

		arrayForm.setCreateSpecimenArray("yes");
		arrayForm.setForwardTo("success");
		arrayForm.setActivityStatus("Active");
		arrayForm.setIsBarcodeEditable("true");
		arrayForm.setEnterSpecimenBy("Label");
		arrayForm.setIsDefinedArray("");

		Specimen specimen=(Specimen)TestCaseUtility.getObjectMap().get("TissueSpecimen");


		Map arrayContentMap = new java.util.HashMap();//		};();//(Map) getSession().getAttribute(				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_label", specimen.getLabel());
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionOne", "1");
		arrayContentMap.put("SpecimenArrayContent:0_Specimen_barcode", "");
		arrayContentMap.put("SpecimenArrayContent:0_positionDimensionTwo","1");

		getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);

		setRequestPathInfo("/SpecimenArrayEdit");
		setActionForm(arrayForm);

		actionPerform();
		verifyNoActionErrors();

		logger.info("######################################"+getActualForward());
		setRequestPathInfo(getActualForward());

		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "20");

		actionPerform();
		verifyNoActionErrors();

		logger.info("######################################"+getActualForward());

		verifyActionMessages(new String[]{"object.edit.successOnly"});
		logger.info("#############specimenArray Updated Successfully##########");

		arrayForm=(SpecimenArrayForm)getActionForm();

		SpecimenArray specimenArray1=new SpecimenArray();
		specimenArray1.setId(arrayForm.getId());

		try
		{
			specimenArray1.setAllValues(arrayForm);
		}
		catch (AssignDataException e)
		{
			logger.debug(e.getMessage(),e);
			fail("failed to assign values to Specimen Array");
		}

		TestCaseUtility.setNameObjectMap("SpecimenArrayObject",specimenArray1);


	}


}
