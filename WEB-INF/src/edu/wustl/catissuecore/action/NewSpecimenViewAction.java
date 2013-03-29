package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenViewForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.dto.NewSpecimenDTO;
import edu.wustl.catissuecore.util.global.Constants;

public class NewSpecimenViewAction extends Action
{
	public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
				NewSpecimenViewForm  newSpecimenViewForm =(NewSpecimenViewForm) form;
			    String pageOf = request.getParameter(Constants.PAGE_OF);
				newSpecimenViewForm.setIdValue(request.getParameter("id"));
				newSpecimenViewForm.setParticipantId(request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID));				
				newSpecimenViewForm.setCpId(request.getParameter(Constants.CP_SEARCH_CP_ID));
				
				NewSpecimenBizLogic bizLogic =new NewSpecimenBizLogic();
				NewSpecimenDTO specimenDTOObject =bizLogic.getSpecimenDTO(new Long(request.getParameter("id")));
				specimenDTOObject.setSpecimenCollectionGroup("CollectionProtocol_1_1 ");
				specimenDTOObject.setLineage("New");
				specimenDTOObject.setClassValue("Fluid");
				specimenDTOObject.setTypeValue("Milk");
				specimenDTOObject.setTissueSite("Accessory sinus, NOS");
				specimenDTOObject.setTissueSide("Not Applicable");
				specimenDTOObject.setPathologicalStatus("Malignant, Pre-Invasive");
				specimenDTOObject.setCreatedDate("01-15-2013");
				specimenDTOObject.setInitialQuantity("0.0");
				specimenDTOObject.setAvailableQuantity("0.0");
				specimenDTOObject.setConcentrationValue("0.5");
				specimenDTOObject.setCollectionStatus("Collected");
				specimenDTOObject.setIsAvailableValue(true);
				specimenDTOObject.setStoragePosition("In Transit_Freezer_1:a,H");
				specimenDTOObject.setLabel("20_FixedCell");
				specimenDTOObject.setBarcode("6");
				
				newSpecimenViewForm.setSpecimenCollectionGroup(specimenDTOObject.getSpecimenCollectionGroup());
				newSpecimenViewForm.setLineage(specimenDTOObject.getLineage());
				newSpecimenViewForm.setClassValue(specimenDTOObject.getClassValue());
				newSpecimenViewForm.setTypeValue(specimenDTOObject.getTypeValue());
				newSpecimenViewForm.setTissueSite(specimenDTOObject.getTissueSite());
				newSpecimenViewForm.setTissueSide(specimenDTOObject.getTissueSide());
				newSpecimenViewForm.setPathologicalStatus(specimenDTOObject.getPathologicalStatus());
				newSpecimenViewForm.setCreatedDate(specimenDTOObject.getCreatedDate());
				newSpecimenViewForm.setInitialQuantity(specimenDTOObject.getInitialQuantity());
				newSpecimenViewForm.setAvailableQuantity(specimenDTOObject.getAvailableQuantity());
				newSpecimenViewForm.setConcentrationValue(specimenDTOObject.getConcentrationValue());
				newSpecimenViewForm.setCollectionStatus(specimenDTOObject.getCollectionStatus());
				newSpecimenViewForm.setIsAvailableValue(specimenDTOObject.getIsAvailableValue());
				newSpecimenViewForm.setStoragePosition(specimenDTOObject.getStoragePosition());
				newSpecimenViewForm.setLabel(specimenDTOObject.getLabel());
				newSpecimenViewForm.setBarcode(specimenDTOObject.getBarcode());
				
				return mapping.findForward(pageOf);
	 }
}
