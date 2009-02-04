/*
 * This class populates the data for the storage location page of multiple specimens.
 * 
 * Created on Nov 6, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.MultipleSpecimenStorageLocationForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;


/*
 * This class populates the data for the storage location page of multiple specimens.
 * 
 * Created on Nov 6, 2006
 * @author mandar_deshmukh
 */
public class MultipleSpecimenStorageLocationAction extends BaseAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// TODO Auto-generated method stub		
		// -----------------------
		String target = "success";
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF,pageOf);
		MultipleSpecimenStorageLocationForm aForm = (MultipleSpecimenStorageLocationForm)form;
		
		HashMap specimenMap = getSpecimenMap(request);
		request.setAttribute(Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP,specimenMap);
		aForm.setSpecimenMap( specimenMap);
		aForm.populateSpecimenOnUIMap(request);  

		/*if(!(Constants.PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION.equals(request.getParameter(Constants.PAGEOF))))
		{
			MultipleSpecimenStorageLocationForm aForm = (MultipleSpecimenStorageLocationForm)form;
			
			HashMap specimenMap = getSpecimenMap(request);
			request.setAttribute(Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP,specimenMap);
			aForm.setSpecimenMap( specimenMap);
			aForm.populateSpecimenOnUIMap(request);  
			
		}
		else
		{
			//TODO
			
		} */
		// -----------------------
		return mapping.findForward(target ) ;
	}

	private List getDerivedSpecimenList(int deriveCount)
	{
		List derivedList = new ArrayList();
		for(int i = 1;i<=deriveCount;i++)
		{
			derivedList.add(getSpecimen(i,"Derived"));
		}
		return derivedList; 
	}
	
	private TissueSpecimen getSpecimen(int i,String prefix)
	{
		TissueSpecimen specimen = new TissueSpecimen();
		specimen.setId(new Long(i) );
		specimen.setLabel(prefix+"Specimen Label "+i); 
		specimen.setBarcode(prefix+"Barcode "+i );
		specimen.setType("Fresh Tissue" );
		
		StorageContainer sc = new StorageContainer();
		sc.setId(new Long(i));
		specimen.setStorageContainer(sc );
		specimen.setPositionDimensionOne(new Integer((i+i)) ); 
		specimen.setPositionDimensionTwo(new Integer((i+i)) ); 
		
		return specimen ;
	}

	private HashMap getSpecimenMap(HttpServletRequest request)
	{
		HashMap specimenMap = new HashMap();
		specimenMap = (HashMap)request.getSession().getAttribute(Constants.SPECIMEN_MAP_KEY);  
		return specimenMap; 
	}

// ------------------------------------------	
	//to test the flow creates dummy data
	private HashMap getSpecimenMap1()
	{
		HashMap specimenMap = new HashMap();
		for(int i=1;i<5;i++)
		{
			Specimen specimen = getSpecimen(i,"");
			int dc = i; 
			Logger.out.debug("\ndc: "+dc);
			List derivedList = getDerivedSpecimenList(dc );
			specimenMap.put(specimen,derivedList);
			
		}
		return specimenMap; 
	}
}
