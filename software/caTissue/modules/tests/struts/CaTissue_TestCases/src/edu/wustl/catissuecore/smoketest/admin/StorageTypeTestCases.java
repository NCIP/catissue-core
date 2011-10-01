package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class StorageTypeTestCases extends CaTissueSuiteSmokeBaseTest
{
	public StorageTypeTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public StorageTypeTestCases(String name)
	{
		super(name);
	}
	public StorageTypeTestCases()
	{
		super();
	}


	public void testStorageTypeAdd()

	{

	String[] arr = getDataObject().getValues();


	StorageTypeForm storageTypeForm = new StorageTypeForm();

	setRequestPathInfo("/StorageType");

	addRequestParameter("operation","add");

	addRequestParameter("pageOf","pageOfStorageType");

	setActionForm(storageTypeForm);

	actionPerform();

	verifyForward("pageOfStorageType");

	verifyTilesForward("pageOfStorageType",".catissuecore.storageTypeDef");


	storageTypeForm=(StorageTypeForm)getActionForm();

	storageTypeForm.setType(arr[0]);

	storageTypeForm.setDefaultTemperature(arr[1]);

	storageTypeForm.setOneDimensionLabel(arr[2]);

	storageTypeForm.setOneDimensionCapacity(Integer.parseInt(arr[3].trim()));

	storageTypeForm.setTwoDimensionLabel(arr[4]);

	storageTypeForm.setTwoDimensionCapacity(Integer.parseInt(arr[5].trim()));


	long[] arr1={Long.parseLong(arr[6].trim())};

	storageTypeForm.setHoldsStorageTypeIds(arr1);


	String[] arr2={(arr[7].trim())};

	storageTypeForm.setHoldsSpecimenClassTypes(arr2);


	String[] arr4={(arr[8].trim())};

	storageTypeForm.setHoldsTissueSpType(arr4);


	String[] arr5={(arr[9].trim())};

	storageTypeForm.setHoldsFluidSpType(arr5);


	String[] arr6={(arr[10].trim())};

	storageTypeForm.setHoldsCellSpType(arr6);


	String[] arr7={(arr[11].trim())};

	storageTypeForm.setHoldsMolSpType(arr7);




	setRequestPathInfo("/StorageTypeAdd");

	addRequestParameter("operation","add");

	addRequestParameter("pageOf","pageOfStorageType");

	setActionForm(storageTypeForm);

	actionPerform();

	verifyForward("success");

	verifyActionMessages(new String[]{"object.add.successOnly"});


	}





public void testStorageTypeWithContainer()
{

		String[] arr = getDataObject().getValues();

		StorageTypeForm storageTypeForm = new StorageTypeForm();

		setRequestPathInfo("/StorageType");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageType");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("pageOfStorageType");
		verifyTilesForward("pageOfStorageType",".catissuecore.storageTypeDef");


		storageTypeForm=(StorageTypeForm)getActionForm();

		storageTypeForm.setType(arr[0]);
		storageTypeForm.setDefaultTemperature(arr[1]);
		storageTypeForm.setOneDimensionLabel(arr[2]);
		storageTypeForm.setOneDimensionCapacity(Integer.parseInt(arr[3].trim()));
		storageTypeForm.setTwoDimensionLabel(arr[4]);
		storageTypeForm.setTwoDimensionCapacity(Integer.parseInt(arr[5].trim()));




		String[] arr2={(arr[6].trim())};
		storageTypeForm.setHoldsSpecimenClassTypes(arr2);

		String[] arr4={(arr[7].trim())};
		storageTypeForm.setHoldsTissueSpType(arr4);

		String[] arr5={(arr[8].trim())};
		storageTypeForm.setHoldsFluidSpType(arr5);

		String[] arr6={(arr[9].trim())};
		storageTypeForm.setHoldsCellSpType(arr6);

		String[] arr7={(arr[10].trim())};
		storageTypeForm.setHoldsMolSpType(arr7);


		setRequestPathInfo("/StorageTypeAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageType");
		setActionForm(storageTypeForm);
		actionPerform();

		StorageContainerForm storageContainerForm = new StorageContainerForm();

		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfStorageType");
		setActionForm(storageContainerForm);
		actionPerform();

		setRequestPathInfo("/StorageContainer");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();



		storageContainerForm = (StorageContainerForm)getActionForm();

		storageContainerForm.setSelectedContainerName(arr[11]);
		storageContainerForm.setSiteId(Long.parseLong(arr[12].trim()));
		storageContainerForm.setNoOfContainers(Integer.parseInt(arr[13].trim()));


		setRequestPathInfo("/StorageContainerAdd");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.successOnly"});


	}


public void testStorageContainerAddWithSimilarContainer()
{

		String[] arr = getDataObject().getValues();

		StorageContainerForm storageContainerForm = new StorageContainerForm();

		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();

		setRequestPathInfo("/StorageContainer");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();

		storageContainerForm=(StorageContainerForm)getActionForm();

		storageContainerForm.setTypeName(arr[0]);
		storageContainerForm.setDefaultTemperature(arr[1]);
		storageContainerForm.setOneDimensionLabel(arr[2]);
		storageContainerForm.setOneDimensionCapacity(Integer.parseInt(arr[3].trim()));
		storageContainerForm.setTwoDimensionLabel(arr[4]);
		storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(arr[5].trim()));
		storageContainerForm.setNoOfContainers(Integer.parseInt(arr[6].trim()));
		storageContainerForm.setSelectedContainerName(arr[7]);
		storageContainerForm.setParentContainerSelected(arr[7]);
		storageContainerForm.setSiteId(Long.parseLong(arr[8].trim()));


		String[] arr2={(arr[9].trim())};
		storageContainerForm.setHoldsSpecimenClassTypes(arr2);

		String[] arr4={(arr[10].trim())};
		storageContainerForm.setHoldsTissueSpType(arr4);

		String[] arr5={(arr[11].trim())};
		storageContainerForm.setHoldsFluidSpType(arr5);

		String[] arr6={(arr[12].trim())};
		storageContainerForm.setHoldsCellSpType(arr6);

		String[] arr7={(arr[13].trim())};
		storageContainerForm.setHoldsMolSpType(arr7);

		storageContainerForm.setTypeId(Long.parseLong(arr[14].trim()));


		addRequestParameter("typeId","23");
		addRequestParameter("noOfContainers","3");


		setRequestPathInfo("/CreateSimilarContainers");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfCreateSimilarContainers");
		setActionForm(storageContainerForm);
		actionPerform();


		setRequestPathInfo("/SimilarContainersAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf", "pageOfCreateSimilarContainers");
		setActionForm(storageContainerForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"similarContaienrs.add.success"});

	}





	public void testStorageTypeEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "StorageType");
		addRequestParameter("pageOf", "pageOfStorageType" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyTilesForward("pageOfSite",".catissuecore.editSearchPageDef");
		verifyNoActionErrors();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "StorageType");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "StorageType."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
		simpleQueryInterfaceForm.setPageOf("pageOfStorageType");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SpreadsheetView");
		actionPerform();
		verifyTilesForward("pageOfStorageType",".catissuecore.editSearchResultsDef");

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();


		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("pageOf", "pageOfStorageType");
		actionPerform();
		verifyForward("pageOfStorageType");


		StorageTypeForm storageTypeForm = (StorageTypeForm) getActionForm() ;
		setRequestPathInfo("/StorageType");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("menuSelected", "6");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyTilesForward("pageOfStorageType",".catissuecore.storageTypeDef");

		storageTypeForm.setType(arr[3]);
		storageTypeForm.setDefaultTemperature(arr[4]);
		storageTypeForm.setOneDimensionLabel(arr[5]);
		storageTypeForm.setOneDimensionCapacity(Integer.parseInt(arr[6].trim()));
		storageTypeForm.setTwoDimensionLabel(arr[7]);
		storageTypeForm.setTwoDimensionCapacity(Integer.parseInt(arr[8].trim()));

		long[] arr1={Long.parseLong(arr[9].trim())};
		storageTypeForm.setHoldsStorageTypeIds(arr1);

		String[] arr2={(arr[10].trim())};
		storageTypeForm.setHoldsSpecimenClassTypes(arr2);

		String[] arr3={(arr[11].trim())};
		storageTypeForm.setHoldsTissueSpType(arr3);

		String[] arr4={(arr[12].trim())};
		storageTypeForm.setHoldsFluidSpType(arr4);

		String[] arr5={(arr[13].trim())};
		storageTypeForm.setHoldsCellSpType(arr5);

		String[] arr6={(arr[14].trim())};
		storageTypeForm.setHoldsMolSpType(arr6);

		storageTypeForm.setOperation("edit");
		setRequestPathInfo("/StorageTypeEdit");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}


}
