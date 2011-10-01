package edu.wustl.catissuecore.testcase.ordering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;

public class DirectDistributionTestCase extends CaTissueSuiteBaseTest
{

	
	public void testAddSpecimenAutoForDistribution()
	{
		NewSpecimenForm newSpecForm = new NewSpecimenForm() ;
		setRequestPathInfo("/NewSpecimenAdd");
		newSpecForm.setLabel("label_" + UniqueKeyGeneratorUtil.getUniqueKey());
		newSpecForm.setBarcode("barcode_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
		StorageContainer storageContainer = (StorageContainer)TestCaseUtility.getNameObjectMap("StorageContainer");
		
		newSpecForm.setSpecimenCollectionGroupId(""+specimenCollectionGroup.getId()) ;
		newSpecForm.setSpecimenCollectionGroupName(specimenCollectionGroup.getName()) ;
		
		newSpecForm.setStContSelection(3);

		newSpecForm.setSelectedContainerName(storageContainer.getName());
		newSpecForm.setPos1("");
		newSpecForm.setPos2("");
		
		newSpecForm.setParentPresent(false);
		newSpecForm.setTissueSide("Not Specified") ;
		newSpecForm.setTissueSite("Not Specified");
		newSpecForm.setPathologicalStatus("Not Specified");

		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		newSpecForm.setBiohazardName(biohazard.getName());
		newSpecForm.setBiohazardType(biohazard.getType());

		newSpecForm.setClassName("Fluid");
		newSpecForm.setType("Bile");
		newSpecForm.setQuantity("10") ;
		newSpecForm.setAvailable(true);
		newSpecForm.setAvailableQuantity("5");
		newSpecForm.setCollectionStatus("Collected") ;
		

		Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");

		newSpecForm.setCollectionEventId(event.getId()) ;

		newSpecForm.setCollectionEventSpecimenId(0L);
		newSpecForm.setCollectionEventdateOfEvent("01-28-2009");
		newSpecForm.setCollectionEventTimeInHours("11") ;
		newSpecForm.setCollectionEventTimeInMinutes("2") ;
		newSpecForm.setCollectionEventUserId(1L) ;
		newSpecForm.setCollectionEventCollectionProcedure("Use CP Defaults");
		newSpecForm.setCollectionEventContainer("Use CP Defaults") ;

		newSpecForm.setReceivedEventId(event.getId());
		newSpecForm.setReceivedEventDateOfEvent("01-28-2009");
		newSpecForm.setReceivedEventTimeInHours("11") ;
		newSpecForm.setReceivedEventTimeInMinutes("2") ;
		newSpecForm.setReceivedEventUserId(1L) ;
		newSpecForm.setReceivedEventReceivedQuality("Acceptable");

		newSpecForm.setOperation("add");
		newSpecForm.setPageOf("pageOfNewSpecimen");
		setActionForm(newSpecForm);
		actionPerform();
		verifyForward("success");

		NewSpecimenForm form= (NewSpecimenForm) getActionForm();
		Specimen specimen = new Specimen();
		specimen.setId(form.getId());
		specimen.setSpecimenClass( form.getClassName() );
		specimen.setSpecimenType( form.getType() );
		specimen.setActivityStatus(form.getActivityStatus());
		specimen.setAvailableQuantity(Double.parseDouble(form.getAvailableQuantity()));
		specimen.setLabel(form.getLabel());
		specimen.setBarcode(form.getBarcode());
    	specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
    	specimen.setCollectionStatus(form.getCollectionStatus());
    	specimen.setPathologicalStatus(form.getPathologicalStatus());
    	specimen.setInitialQuantity(Double.parseDouble(form.getQuantity()));
    	SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
    	specimenCharacteristics.setTissueSide(form.getTissueSide());
    	specimenCharacteristics.setTissueSite(form.getTissueSite());

    	specimen.setSpecimenCharacteristics(specimenCharacteristics);

    	TestCaseUtility.setNameObjectMap("SpecimenForDistribution",specimen);
   	}
	/**
	 * Test Department Add.
	 */
	
	
	public void testDirectDistribution()
	{
		List<String> specimenIds = new ArrayList<String>();
		//specimenIds.add("10");
		Specimen specimen = (Specimen)TestCaseUtility.getNameObjectMap("SpecimenForDistribution");
		DistributionProtocol dp = (DistributionProtocol)TestCaseUtility.getNameObjectMap("DistributionProtocol");
		Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
		specimenIds.add(""+specimen.getId());
		getSession().setAttribute(Constants.SPECIMEN_ID,specimenIds);
		setRequestPathInfo("/RequestToDistribute");
		actionPerform();
	
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/SpecDistribute");
		actionPerform();
		
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/specToDistribute");
		actionPerform();
		
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/DirectDistributeSpec");
		actionPerform();
		
		addRequestParameter("type","directDistribution");
		setRequestPathInfo("/RequestDetails");
		actionPerform();
		
		List requestDetailsList = new ArrayList();
		if(request.getAttribute(Constants.REQUEST_DETAILS_LIST) != null )
			requestDetailsList = (List) request.getAttribute(Constants.REQUEST_DETAILS_LIST);	
		else 
			requestDetailsList = (List) getSession().getAttribute(Constants.REQUEST_DETAILS_LIST);	
		
		Integer count=0;
		if(requestDetailsList!=null && requestDetailsList.size()>0 )
		{
		 count=requestDetailsList.size();	
		}
		
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm)getActionForm();
		requestDetailsForm.setDistributionProtocolId(""+dp.getId());
		requestDetailsForm.setOrderName("Order_"+requestDetailsForm.getId());
		requestDetailsForm.setValue("RequestDetailsBean:0_assignedStatus", Constants.ORDER_REQUEST_STATUS_DISTRIBUTED);
		requestDetailsForm.setValue("RequestDetailsBean:0_canDistribute","true");
		requestDetailsForm.setIsDirectDistribution(Boolean.TRUE);
		requestDetailsForm.setSite(""+site.getId());
		addRequestParameter("submittedFor","ForwardTo");
		addRequestParameter("noOfRecords",count.toString());
		
		setRequestPathInfo("/SubmitRequestDetails");
		actionPerform();
		verifyNoActionErrors();
		setRequestPathInfo("/DistributionReport");
		String dpId = (String)request.getAttribute("id");
		getRequest().setAttribute("id", new Long(dpId));
		actionPerform();
	
		ConfigureResultViewForm configureResultViewForm = (ConfigureResultViewForm)getActionForm();
		assertNotNull(configureResultViewForm.getSelectedColumnNames());
		verifyNoActionErrors();
		
		
	}
}
