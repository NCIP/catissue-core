package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class StorageContainerTestCase extends CaTissueSuiteSmokeBaseTest
{
	public StorageContainerTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public StorageContainerTestCase(String name)
	{
		super(name);
	}
	public StorageContainerTestCase()
	{
		super();
	}
	public void testStorageContainerAdd()                             //Container Added only with the existing values of Storage Type
	{																// otherwise the test case will get failed
		String[] arr = getDataObject().getValues();

		StorageContainerForm storageContainerForm = new StorageContainerForm();
		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("success");

		setRequestPathInfo("/StorageContainer");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageContainer");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("pageOfStorageContainer");

		storageContainerForm=(StorageContainerForm)getActionForm();
		storageContainerForm.setTypeId(Long.parseLong(arr[0]));
		storageContainerForm.setSelectedContainerName(arr[1]);
		storageContainerForm.setSiteId(Long.parseLong(arr[2]));
		storageContainerForm.setNoOfContainers(Integer.parseInt(arr[3]));
		storageContainerForm.setDefaultTemperature(arr[4]);
		storageContainerForm.setOneDimensionCapacity(Integer.parseInt(arr[5]));
		storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(arr[6]));
		storageContainerForm.setActivityStatus(arr[7]);

		long[] arr1={Long.parseLong(arr[8].trim())};
		storageContainerForm.setHoldsStorageTypeIds(arr1);

		String[] arr2={(arr[9].trim())};
		storageContainerForm.setHoldsSpecimenClassTypes(arr2);

		String[] arr3={(arr[10].trim())};
		storageContainerForm.setHoldsTissueSpType(arr3);

		String[] arr4={(arr[11].trim())};
		storageContainerForm.setHoldsFluidSpType(arr4);

		String[] arr5={(arr[12].trim())};
		storageContainerForm.setHoldsCellSpType(arr5);

		String[] arr6={(arr[13].trim())};
		storageContainerForm.setHoldsMolSpType(arr6);


		setRequestPathInfo("/StorageContainerAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfStorageType");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}

	public void testStorageContainerEdit()
	{
			String[] arr = getDataObject().getValues();

			SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
			setRequestPathInfo("/SimpleQueryInterface");
			addRequestParameter("aliasName", "StorageContainer");
			addRequestParameter("pageOf", "pageOfStorageContainer" );
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchPageDef");
			verifyNoActionErrors();

			simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "StorageContainer");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "StorageContainer."+arr[0]+".varchar");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator",arr[1]);
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
			simpleQueryInterfaceForm.setPageOf("pageOfStorageContainer");
			setRequestPathInfo("/SimpleSearch");
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();

			setRequestPathInfo("/SpreadsheetView");
			actionPerform();
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchResultsDef");

			setRequestPathInfo("/SearchObject");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("operation", "search");
			addRequestParameter("id", arr[2]);
			actionPerform();

			setRequestPathInfo("/StorageContainerSearch");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			actionPerform();

			StorageContainerForm storageContainerForm = (StorageContainerForm) getActionForm() ;
			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation", "edit");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("menuSelected", "7");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");

			storageContainerForm.setContainerName(arr[3]);
			storageContainerForm.setSelectedContainerName(arr[4]);
			storageContainerForm.setSiteId(Long.parseLong(arr[5]));
			storageContainerForm.setDefaultTemperature(arr[6]);
			storageContainerForm.setOneDimensionCapacity(Integer.parseInt(arr[7].trim()));
			storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(arr[8].trim()));
			storageContainerForm.setActivityStatus(arr[9]);

			long[] arr1={Long.parseLong(arr[10].trim())};
			storageContainerForm.setHoldsStorageTypeIds(arr1);

			String[] arr2={(arr[11].trim())};
			storageContainerForm.setHoldsSpecimenClassTypes(arr2);

			String[] arr3={(arr[12].trim())};
			storageContainerForm.setHoldsTissueSpType(arr3);

			String[] arr4={(arr[13].trim())};
			storageContainerForm.setHoldsFluidSpType(arr4);

			String[] arr5={(arr[14].trim())};
			storageContainerForm.setHoldsCellSpType(arr5);

			String[] arr6={(arr[15].trim())};
			storageContainerForm.setHoldsMolSpType(arr6);

			storageContainerForm.setOperation("edit");
			setRequestPathInfo("/StorageContainerEdit");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyActionMessages(new String[]{"object.edit.successOnly"});
		}





		public void testStorageContainerAddWithArrayType()                             //Container Added only with the existing values of Storage Type
		{																// otherwise the test case will get failed
			String[] arr = getDataObject().getValues();

			StorageContainerForm storageContainerForm = new StorageContainerForm();
			setRequestPathInfo("/OpenStorageContainer");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");

			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");

			storageContainerForm=(StorageContainerForm)getActionForm();
			storageContainerForm.setTypeId(Long.parseLong(arr[0]));
			storageContainerForm.setSelectedContainerName(arr[1]);
			storageContainerForm.setSiteId(Long.parseLong(arr[2]));
			storageContainerForm.setNoOfContainers(Integer.parseInt(arr[3]));
			storageContainerForm.setDefaultTemperature(arr[4]);
			storageContainerForm.setOneDimensionCapacity(Integer.parseInt(arr[5]));
			storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(arr[6]));
			storageContainerForm.setActivityStatus(arr[7]);

			long[] arr1={Long.parseLong(arr[8].trim())};
			storageContainerForm.setHoldsSpecimenArrTypeIds(arr1);

			long[] arr2={Long.parseLong(arr[9].trim())};
			storageContainerForm.setHoldsStorageTypeIds(arr2);

			String[] arr3={(arr[10].trim())};
			storageContainerForm.setHoldsTissueSpType(arr3);

			String[] arr4={(arr[11].trim())};
			storageContainerForm.setHoldsFluidSpType(arr4);

			String[] arr5={(arr[12].trim())};
			storageContainerForm.setHoldsCellSpType(arr5);

			String[] arr6={(arr[13].trim())};
			storageContainerForm.setHoldsMolSpType(arr6);


			setRequestPathInfo("/StorageContainerAdd");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageType");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyActionMessages(new String[]{"object.add.successOnly"});
		}


		public void testStorageContainerViewMapWithGridView()
		{
			String[] InputData = getDataObject().getValues();

			StorageContainerForm storageContainerForm = new StorageContainerForm();
			setRequestPathInfo("/OpenStorageContainer");
			addRequestParameter("operation","showEditAPageAndMap");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");

			AdvanceSearchForm advanceSearchForm = new AdvanceSearchForm();

			setRequestPathInfo("/ShowFramedPage");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(advanceSearchForm);
			actionPerform();


			setRequestPathInfo("/StorageContainerTree");
			addRequestParameter("operation","showEditAPageAndMap");
			addRequestParameter("pageOf","pageOfStorageContainer");
			actionPerform();

			setRequestPathInfo("/storageContainerEditMessageScreen");
			actionPerform();

			setRequestPathInfo("/ShowChildNodes");
			addRequestParameter("pageOf","pageOfStorageContainer");
			addRequestParameter("nodeName",InputData[14]);
			addRequestParameter("containerId",InputData[15]);
			addRequestParameter("parentId",InputData[16]);
			actionPerform();

			setRequestPathInfo("/SearchObject");
			addRequestParameter("id",InputData[0]);
			addRequestParameter("pageOf","pageOfTreeSC");
			addRequestParameter("operation","search");
			actionPerform();

			setRequestPathInfo("/EditStorageContainerSearch");
			addRequestParameter("id",InputData[0]);
			addRequestParameter("operation","search");
			addRequestParameter("pageOf","pageOfTreeSC");
			setActionForm(storageContainerForm);
			actionPerform();

			storageContainerForm = (StorageContainerForm) getActionForm() ;

			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","pageOfTreeSC");
			setActionForm(storageContainerForm);
			actionPerform();

			setRequestPathInfo("/OpenStorageContainer");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","pageOfViewMapTab");
			setActionForm(storageContainerForm);
			actionPerform();

			setRequestPathInfo("/ShowStorageGridView");
			addRequestParameter("pageOf","pageOfStorageContainer");
			addRequestParameter("id",InputData[0]);
			actionPerform();

			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","pageOfTreeSC");
			setActionForm(storageContainerForm);
			actionPerform();

			storageContainerForm.setContainerName(InputData[1]);
			storageContainerForm.setSelectedContainerName(InputData[2]);
			storageContainerForm.setSiteId(Long.parseLong(InputData[3]));
			storageContainerForm.setDefaultTemperature(InputData[4]);
			storageContainerForm.setOneDimensionCapacity(Integer.parseInt(InputData[5].trim()));
			storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(InputData[6].trim()));
			storageContainerForm.setActivityStatus(InputData[7]);

			long[] storageTypeIds={Long.parseLong(InputData[8].trim())};
			storageContainerForm.setHoldsStorageTypeIds(storageTypeIds);

			String[] specimenClassTypes={(InputData[9].trim())};
			storageContainerForm.setHoldsSpecimenClassTypes(specimenClassTypes);

			String[] tissueSpType={(InputData[10].trim())};
			storageContainerForm.setHoldsTissueSpType(tissueSpType);

			String[] fluidSpType={(InputData[11].trim())};
			storageContainerForm.setHoldsFluidSpType(fluidSpType);

			String[] cellSpType={(InputData[12].trim())};
			storageContainerForm.setHoldsCellSpType(cellSpType);

			String[] molSpType={(InputData[13].trim())};
			storageContainerForm.setHoldsMolSpType(molSpType);


			setRequestPathInfo("/StorageContainerEdit");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(storageContainerForm);
			actionPerform();

			verifyForward("success");
			verifyActionMessages(new String[]{"object.edit.successOnly"});
		}

	
		public void testStorageContainerEditWithManualContainer()
		{
			String[] InputData = getDataObject().getValues();

			SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
			setRequestPathInfo("/SimpleQueryInterface");
			addRequestParameter("aliasName", "StorageContainer");
			addRequestParameter("pageOf", "pageOfStorageContainer" );
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchPageDef");
			verifyNoActionErrors();

			simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "StorageContainer");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "StorageContainer."+InputData[0]+".varchar");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator",InputData[1]);
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);
			simpleQueryInterfaceForm.setPageOf("pageOfStorageContainer");
			setRequestPathInfo("/SimpleSearch");
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();

			setRequestPathInfo("/SpreadsheetView");
			actionPerform();
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchResultsDef");

			setRequestPathInfo("/SearchObject");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("operation", "search");
			addRequestParameter("id", InputData[2]);
			actionPerform();

			setRequestPathInfo("/StorageContainerSearch");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			actionPerform();

			StorageContainerForm storageContainerForm = (StorageContainerForm) getActionForm() ;
			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation", "edit");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("menuSelected", "7");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");


			storageContainerForm.setActivityStatus(InputData[3]);
			storageContainerForm.setBarcode(InputData[4]);
			storageContainerForm.setCheckedButton(Integer.parseInt(InputData[5]));
			storageContainerForm.setContainerId(InputData[6]);

			storageContainerForm.setContainerName(InputData[7]);
			storageContainerForm.setDefaultTemperature(InputData[8]);
			storageContainerForm.setIsBarcodeEditable(InputData[9]);
			storageContainerForm.setIsFull(InputData[10]);
			storageContainerForm.setNoOfContainers(Integer.parseInt(InputData[11]));
			storageContainerForm.setOneDimensionCapacity(Integer.parseInt(InputData[12]));
			storageContainerForm.setParentContainerId(Integer.parseInt(InputData[13]));
			storageContainerForm.setParentContainerSelected(InputData[14]);
			storageContainerForm.setPos1(InputData[15]);
			storageContainerForm.setPos2(InputData[16]);
			storageContainerForm.setSelectedContainerName(InputData[17]);
			storageContainerForm.setSiteId(Integer.parseInt(InputData[18]));
			storageContainerForm.setSiteName(InputData[19]);
			storageContainerForm.setSpecimenOrArrayType(InputData[20]);
			storageContainerForm.setStContSelection(Integer.parseInt(InputData[21]));
			storageContainerForm.setTwoDimensionCapacity(Integer.parseInt(InputData[22]));
			storageContainerForm.setTypeId(Integer.parseInt(InputData[23]));
			storageContainerForm.setTypeName(InputData[24]);

			String[] specimenClassTypes={(InputData[25].trim())};
			storageContainerForm.setHoldsSpecimenClassTypes(specimenClassTypes);

			String[] tissueSpType={(InputData[26].trim())};
			storageContainerForm.setHoldsTissueSpType(tissueSpType);

			storageContainerForm.setOperation("edit");
			setRequestPathInfo("/StorageContainerEdit");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyActionMessages(new String[]{"object.edit.successOnly"});
		}
	


		public void testStorageContainerDisable()
		{
			String[] InputData = getDataObject().getValues();

			SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
			setRequestPathInfo("/SimpleQueryInterface");
			addRequestParameter("aliasName", "StorageContainer");
			addRequestParameter("pageOf", "pageOfStorageContainer" );
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchPageDef");
			verifyNoActionErrors();

			simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "StorageContainer");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "StorageContainer."+InputData[0]+".varchar");
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator",InputData[1]);
			simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);
			simpleQueryInterfaceForm.setPageOf("pageOfStorageContainer");
			setRequestPathInfo("/SimpleSearch");
			setActionForm(simpleQueryInterfaceForm);
			actionPerform();

			setRequestPathInfo("/SpreadsheetView");
			actionPerform();
			verifyTilesForward("pageOfStorageContainer",".catissuecore.editSearchResultsDef");

			setRequestPathInfo("/SearchObject");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("operation", "search");
			addRequestParameter("id", InputData[2]);
			actionPerform();

			setRequestPathInfo("/StorageContainerSearch");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			actionPerform();

			StorageContainerForm storageContainerForm = (StorageContainerForm) getActionForm() ;
			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation", "edit");
			addRequestParameter("pageOf", "pageOfStorageContainer");
			addRequestParameter("menuSelected", "7");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");

			storageContainerForm.setActivityStatus(InputData[3]);

			storageContainerForm.setOnSubmit("/StorageContainerDelete.do");
			storageContainerForm.setOperation("edit");

			setRequestPathInfo("/StorageContainerEdit");
			setActionForm(storageContainerForm);
			actionPerform();

			setRequestPathInfo("/StorageContainerDelete");
			actionPerform();

			verifyForward("success");
			verifyActionMessages(new String[]{"object.edit.successOnly"});
	}
	
	    public void testStorageContainerAddWithDefaultParameters()      
		{																
			String[] InputData = getDataObject().getValues();

			StorageContainerForm storageContainerForm = new StorageContainerForm();
			setRequestPathInfo("/OpenStorageContainer");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageContainer");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");

			setRequestPathInfo("/StorageContainer");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageContainer");
			addRequestParameter("typeId",InputData[0]);
			addRequestParameter("typeChange","true");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("pageOfStorageContainer");

			storageContainerForm=(StorageContainerForm)getActionForm();

			storageContainerForm.setSelectedContainerName(InputData[1]);
			storageContainerForm.setSiteId(Long.parseLong(InputData[2]));

			setRequestPathInfo("/StorageContainerAdd");
			addRequestParameter("operation","add");
			addRequestParameter("pageOf","pageOfStorageType");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyActionMessages(new String[]{"object.add.successOnly"});
		}


}
