package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class is to support the GenericSpecimenDetails tag.
 * It will contain methods to set the request attributes required by the tag.
 * @author mandar_deshmukh
 *
 */
public final class SpecimenDetailsTagUtil
{
	
	/*
	 * creates a singleton object
	 */
	private static SpecimenDetailsTagUtil speciTagutil = new SpecimenDetailsTagUtil();
	/*
	 * Private constructor
	 */
	private SpecimenDetailsTagUtil()
	{
		
	}
	
	/*
	 * returns single object 
	 */
	public static SpecimenDetailsTagUtil getInstance()
	{
		return speciTagutil;
	}
	
	
	private static final String PARENT = "Parent";
	private static final String LABEL = "Label";
	private static final String BARCODE = "Barcode";
	private static final String TYPE = "Type";
	private static final String QTY = "Quantity";
	private static final String CONCENTRATION = "Concentration";
	private static final String LOCATION = "Location";
	private static final String COLLECTED = "Collected";
	private static final String CLASS = "Class";
//	private static final String  = ;
	private static final String TSITE = "Tissue Site";
	private static final String TSIDE = "Tissue Side";
	private static final String PATH_STAT = "Pathological Status";
	
	private static final String CH_LABEL = "specimen.label";
	private static final String CH_CLASS = "specimen.type";
	private static final String CH_BARCODE = "specimen.barcode";
	private static final String CH_TYPE = "specimen.subType";
	private static final String CH_TSITE = "specimen.tissueSite";
	private static final String CH_TSIDE = "specimen.tissueSide";
	private static final String CH_PATH_STAT = "specimen.pathologicalStatus";
	private static final String CH_QTY = "anticipatorySpecimen.Quantity";
	private static final String CH_LOC = "anticipatorySpecimen.Location";
	private static final String CH_CONC = "anticipatorySpecimen.Concentration";
	
	/*
	public SpecimenDetailsTagUtil()
	{
		super();
	}*/
	
	/**
	 * 0:Parent, 1:Label, 2:Barcode, 3:SubType, 4:Qty, 5:Concentration,
	 * 6:Location, 7:Collected
	 * @param request
	 * @param summaryForm
	 */
	public static void setAnticipatorySpecimenDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm, boolean showSCLoc)
	{
		List <String>colHeaderList = new ArrayList<String>();
		List <String>subSpecimenColHeaderList = new ArrayList<String>();
		List <String>dispColumnsList = new ArrayList<String>();
		List <String>subSpecdispColumnsList = new ArrayList<String>();
		boolean showLabel = false;
		boolean showBarcode = false;
		int fwdt = 50;
		int lwdt = 12;
		int sfwdt = 48;
		int slwdt = 12;

		//columnHeaderList											DisplayColumn List
		colHeaderList.add("");									dispColumnsList.add(PARENT);	
		
		if(summaryForm.getShowLabel())
		{
			colHeaderList.add(CH_LABEL);							dispColumnsList.add(LABEL);
			showLabel = true;
		}
		if(summaryForm.getShowbarCode())
		{
			colHeaderList.add(CH_BARCODE);							dispColumnsList.add(BARCODE);
			showBarcode = true;
		}	

		colHeaderList.add(CH_TYPE);								dispColumnsList.add(TYPE);
//		//Mandar:28July08 ----- fix for bug 8195 ---- start
// currently reverted to adjust the space issue.
//		colHeaderList.add(CH_TSITE);								dispColumnsList.add(TSITE);
//		colHeaderList.add(CH_TSIDE);								dispColumnsList.add(TSIDE);
//		colHeaderList.add(CH_PATH_STAT);							dispColumnsList.add(PATH_STAT);
//		//Mandar:28July08 ----- fix for bug 8195 ---- end
		colHeaderList.add(CH_QTY);								dispColumnsList.add(QTY);
		colHeaderList.add(CH_CONC);								dispColumnsList.add(CONCENTRATION);
		colHeaderList.add(CH_LOC);								dispColumnsList.add(LOCATION);
		colHeaderList.add("anticipatorySpecimen.Collected");	dispColumnsList.add(COLLECTED);
		
		subSpecimenColHeaderList.addAll(colHeaderList);
		subSpecimenColHeaderList.set(0,"anticipatorySpecimen.Parent");
		subSpecdispColumnsList.addAll(dispColumnsList);
		if(showSCLoc && summaryForm.isMultipleSpEditMode())
		{
			colHeaderList.remove(CH_LOC);
			dispColumnsList.remove(LOCATION);
			request.setAttribute("fromMultipleSpecimen","true");
		}
		else
		{
			request.setAttribute("fromMultipleSpecimen","false");
		}

		if(!summaryForm.getShowLabel() && !(summaryForm.getShowbarCode()))
		{
			fwdt = 40;
			lwdt = 20;
			sfwdt = 42;
			slwdt = 18;
			
		}
		request.setAttribute("fCol",Integer.valueOf(fwdt));
		request.setAttribute("lCol",Integer.valueOf(lwdt));
		request.setAttribute("sfCol",Integer.valueOf(sfwdt));
		request.setAttribute("slCol",Integer.valueOf(slwdt));

		request.setAttribute("specimenList",summaryForm.getSpecimenList());
		request.setAttribute("aliquotList",summaryForm.getAliquotList());
		request.setAttribute("derivedList",summaryForm.getDerivedList());
		
		request.setAttribute("columnHeaderList",colHeaderList);
		request.setAttribute("subSpecimenColHeaderList",subSpecimenColHeaderList);
		request.setAttribute("dispColumnsList",dispColumnsList);
		request.setAttribute("subSpecdispColumnsList",subSpecdispColumnsList);
		request.setAttribute("labelShow",showLabel);
		request.setAttribute("barcodeShow",showBarcode);
		
		
		setPageData(request);
	}
 
	public static void setSpecimenSummaryDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		setSpecimenSummaryParentDetails(request, summaryForm);
		setSpecimenSummaryAliquotDetails(request, summaryForm);
		setSpecimenSummaryDerivedDetails(request,  summaryForm);
	}
	
	private static void setSpecimenSummaryParentDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List <String>colHeaderListP = new ArrayList<String>();
		List <String>displayColumnListP = new ArrayList<String>();
		List <String>colOrderListP = new ArrayList<String>();
		
		colHeaderListP.add("");	colHeaderListP.add(CH_LABEL);	//colHeaderListP.add("specimen.received");
		colHeaderListP.add(CH_CLASS);	colHeaderListP.add(CH_TYPE);
		colHeaderListP.add(CH_TSITE);	colHeaderListP.add(CH_TSIDE);
		colHeaderListP.add(CH_PATH_STAT);	colHeaderListP.add(CH_LOC);
		colHeaderListP.add(CH_QTY);

		colOrderListP.add(PARENT);			displayColumnListP.add(PARENT);
		colOrderListP.add(LABEL);			displayColumnListP.add(LABEL);
		colOrderListP.add(CLASS);			displayColumnListP.add(CLASS);
		colOrderListP.add(TYPE);			displayColumnListP.add(TYPE);
		colOrderListP.add(TSITE);			displayColumnListP.add(TSITE);
		colOrderListP.add(TSIDE);			displayColumnListP.add(TSIDE);
		colOrderListP.add(PATH_STAT);		displayColumnListP.add(PATH_STAT);
		colOrderListP.add(LOCATION);		displayColumnListP.add(LOCATION);
		colOrderListP.add(QTY);				displayColumnListP.add(QTY);
		
		request.setAttribute("specimenList",summaryForm.getSpecimenList());
		request.setAttribute("colHeaderListP",colHeaderListP);
		request.setAttribute("colOrderListP",colOrderListP);
		request.setAttribute("displayColumnListP",displayColumnListP);
	}
	
	private static void setSpecimenSummaryAliquotDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List <String>colHeaderListA = new ArrayList<String>();
		List <String>displayColumnListA = new ArrayList<String>();
		List <String>colOrderListA = new ArrayList<String>();
		
		colHeaderListA.add("anticipatorySpecimen.Parent");
		colHeaderListA.add(CH_LABEL);	
		colHeaderListA.add(CH_LOC);
		colHeaderListA.add(CH_QTY);
	
		colOrderListA.add(PARENT);		displayColumnListA.add(PARENT);
		colOrderListA.add(LABEL);		displayColumnListA.add(LABEL);
		colOrderListA.add(LOCATION);	displayColumnListA.add(LOCATION);
		colOrderListA.add(QTY);			displayColumnListA.add(QTY);
		
		request.setAttribute("aliquotList",summaryForm.getAliquotList());
		request.setAttribute("colHeaderListA",colHeaderListA);
		request.setAttribute("colOrderListA",colOrderListA);
		request.setAttribute("displayColumnListA",displayColumnListA);
	}
	private static void setSpecimenSummaryDerivedDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List <String>colHeaderListD = new ArrayList<String>();
		List <String>displayColumnListD = new ArrayList<String>();
		List <String>colOrderListD = new ArrayList<String>();
		
		colHeaderListD.add("anticipatorySpecimen.Parent");	colHeaderListD.add(CH_LABEL);	
		colHeaderListD.add(CH_CLASS);	colHeaderListD.add(CH_TYPE);
		colHeaderListD.add(CH_QTY);	colHeaderListD.add(CH_LOC);
		colHeaderListD.add(CH_CONC);
	
		colOrderListD.add(PARENT);				displayColumnListD.add(PARENT);
		colOrderListD.add(LABEL);				displayColumnListD.add(LABEL);
		colOrderListD.add(CLASS);				displayColumnListD.add(CLASS);
		colOrderListD.add(TYPE);				displayColumnListD.add(TYPE);
		colOrderListD.add(QTY);					displayColumnListD.add(QTY);
		colOrderListD.add(LOCATION);			displayColumnListD.add(LOCATION);
		colOrderListD.add(CONCENTRATION);		displayColumnListD.add(CONCENTRATION);
		
		request.setAttribute("derivedList",summaryForm.getDerivedList());
		request.setAttribute("colHeaderListD",colHeaderListD);
		request.setAttribute("colOrderListD",colOrderListD);
		request.setAttribute("displayColumnListD",displayColumnListD);
	}

	private static void setPageData(HttpServletRequest request)
	{
		String formAction = "SubmitSpecimenCollectionProtocol.do";
		if(request.getAttribute(Constants.PAGE_OF) != null)
		{
			formAction = formAction + "?pageOf="+request.getAttribute(Constants.PAGE_OF);
		}
		request.setAttribute("formAction",formAction);
		String refreshTree = "refreshTree(\'"+Constants.CP_AND_PARTICIPANT_VIEW+"\',\'"+Constants.CP_TREE_VIEW+"\',\'"+Constants.CP_SEARCH_CP_ID+"\',\'"+Constants.CP_SEARCH_PARTICIPANT_ID+"\',\'1\');";
		request.setAttribute("refreshTree",refreshTree);
		request.setAttribute("CAN_HOLD_SPECIMEN_CLASS",Constants.CAN_HOLD_SPECIMEN_CLASS);
		request.setAttribute("CAN_HOLD_SPECIMEN_TYPE",Constants.CAN_HOLD_SPECIMEN_TYPE);
		request.setAttribute("CAN_HOLD_COLLECTION_PROTOCOL",Constants.CAN_HOLD_COLLECTION_PROTOCOL);

	}	
}
