package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.tag.GenericSpecimenDetailsTag;

/**
 * This class is to support the GenericSpecimenDetails tag.
 * It will contain methods to set the request attributes required by the tag.
 * @author mandar_deshmukh
 *
 */
public class SpecimenDetailsTagUtil
{

	public SpecimenDetailsTagUtil()
	{
		super();
	}
	
	/**
	 * 0:Parent, 1:Label, 2:Barcode, 3:SubType, 4:Qty, 5:Concentration,
	 * 6:Location, 7:Collected
	 * @param request
	 * @param summaryForm
	 */
	public static void setAnticipatorySpecimenDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List colHeaderList = new ArrayList();
		List subSpecimenColHeaderList = new ArrayList();
		List dispColumnsList = new ArrayList();
		List displayColumnList = new ArrayList();
		
		colHeaderList.add("");	colHeaderList.add("specimen.label");	colHeaderList.add("specimen.barcode");
		colHeaderList.add("specimen.subType");	colHeaderList.add("anticipatorySpecimen.Quantity");
		colHeaderList.add("anticipatorySpecimen.Concentration"); colHeaderList.add("anticipatorySpecimen.Location");
		colHeaderList.add("anticipatorySpecimen.Collected");

		for(int i=0; i<8; i++)
		{	dispColumnsList.add(GenericSpecimenDetailsTag.COLUMN_NAMES[i]);}
		
		subSpecimenColHeaderList.addAll(colHeaderList);
		subSpecimenColHeaderList.set(0,"anticipatorySpecimen.Parent");
		
		request.setAttribute("specimenList",summaryForm.getSpecimenList());
		request.setAttribute("aliquotList",summaryForm.getAliquotList());
		request.setAttribute("derivedList",summaryForm.getDerivedList());
		
		request.setAttribute("columnHeaderList",colHeaderList);
		request.setAttribute("subSpecimenColHeaderList",subSpecimenColHeaderList);
		request.setAttribute("dispColumnsList",dispColumnsList);
	}
 
	public static void setSpecimenSummaryDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		setSpecimenSummaryParentDetails(request, summaryForm);
		setSpecimenSummaryAliquotDetails(request, summaryForm);
		setSpecimenSummaryDerivedDetails(request,  summaryForm);
	}
	
	private static void setSpecimenSummaryParentDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List colHeaderListP = new ArrayList();
		List displayColumnListP = new ArrayList();
		List colOrderListP = new ArrayList();
		
		colHeaderListP.add("");	colHeaderListP.add("specimen.label");	//colHeaderListP.add("specimen.received");
		colHeaderListP.add("specimen.type");	colHeaderListP.add("specimen.subType");
		colHeaderListP.add("specimen.tissueSite");	colHeaderListP.add("specimen.tissueSide");
		colHeaderListP.add("specimen.pathologicalStatus");	colHeaderListP.add("anticipatorySpecimen.Location");
		colHeaderListP.add("anticipatorySpecimen.Quantity");

		colOrderListP.add("Parent");				displayColumnListP.add("Parent");
		colOrderListP.add("Label");					displayColumnListP.add("Label");
		colOrderListP.add("Class");					displayColumnListP.add("Class");
		colOrderListP.add("Type");					displayColumnListP.add("Type");
		colOrderListP.add("Tissue Site");			displayColumnListP.add("Tissue Site");
		colOrderListP.add("Tissue Side");			displayColumnListP.add("Tissue Side");
		colOrderListP.add("Pathological Status");	displayColumnListP.add("Pathological Status");
		colOrderListP.add("Location");				displayColumnListP.add("Location");
		colOrderListP.add("Quantity");				displayColumnListP.add("Quantity");
		
		request.setAttribute("specimenList",summaryForm.getSpecimenList());
		request.setAttribute("colHeaderListP",colHeaderListP);
		request.setAttribute("colOrderListP",colOrderListP);
		request.setAttribute("displayColumnListP",displayColumnListP);
	}
	
	private static void setSpecimenSummaryAliquotDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List colHeaderListA = new ArrayList();
		List displayColumnListA = new ArrayList();
		List colOrderListA = new ArrayList();
		
		colHeaderListA.add("anticipatorySpecimen.Parent");
		colHeaderListA.add("specimen.label");	
		colHeaderListA.add("anticipatorySpecimen.Location");
		colHeaderListA.add("anticipatorySpecimen.Quantity");
	
		colOrderListA.add("Parent");	displayColumnListA.add("Parent");
		colOrderListA.add("Label");		displayColumnListA.add("Label");
		colOrderListA.add("Location");	displayColumnListA.add("Location");
		colOrderListA.add("Quantity");	displayColumnListA.add("Quantity");
		
		request.setAttribute("aliquotList",summaryForm.getAliquotList());
		request.setAttribute("colHeaderListA",colHeaderListA);
		request.setAttribute("colOrderListA",colOrderListA);
		request.setAttribute("displayColumnListA",displayColumnListA);
	}
	private static void setSpecimenSummaryDerivedDetails(HttpServletRequest request, ViewSpecimenSummaryForm summaryForm)
	{
		List colHeaderListD = new ArrayList();
		List displayColumnListD = new ArrayList();
		List colOrderListD = new ArrayList();
		
		colHeaderListD.add("anticipatorySpecimen.Parent");	colHeaderListD.add("specimen.label");	
		colHeaderListD.add("specimen.type");	colHeaderListD.add("specimen.subType");
		colHeaderListD.add("anticipatorySpecimen.Quantity");	colHeaderListD.add("anticipatorySpecimen.Location");
		colHeaderListD.add("anticipatorySpecimen.Concentration");
	
		colOrderListD.add("Parent");			displayColumnListD.add("Parent");
		colOrderListD.add("Label");				displayColumnListD.add("Label");
		colOrderListD.add("Class");				displayColumnListD.add("Class");
		colOrderListD.add("Type");				displayColumnListD.add("Type");
		colOrderListD.add("Quantity");			displayColumnListD.add("Quantity");
		colOrderListD.add("Location");			displayColumnListD.add("Location");
		colOrderListD.add("Concentration");		displayColumnListD.add("Concentration");
		
		request.setAttribute("derivedList",summaryForm.getDerivedList());
		request.setAttribute("colHeaderListD",colHeaderListD);
		request.setAttribute("colOrderListD",colOrderListD);
		request.setAttribute("displayColumnListD",displayColumnListD);
	}


}
