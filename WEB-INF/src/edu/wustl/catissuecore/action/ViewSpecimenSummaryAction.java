package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.MultipleSpecimenList;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.action.SecureAction;

public class ViewSpecimenSummaryAction extends Action {

	

	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
try{
		
		HttpSession session = request.getSession();
		ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
		String eventId = summaryForm.getEventId();
		if(eventId==null){
			//testData(session);
			eventId = (String) request.getParameter("Event_Id");
		}
		System.out.println("Action called with event id "+ eventId);
		System.out.println("Action called with Selection id "+ 
				summaryForm.getSelectedSpecimenId());

		
		LinkedHashMap<String, GenericSpecimen> specimenMap;
		if (eventId != null) {
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_httpSession_BEAN);

		} else {
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.SPECIMEN_LIST_httpSession_BEAN);
		}

		summaryForm.setSpecimenList( specimenMap.values());
//		summaryForm.setSelectedSpecimenId("specimen1");
		String selectedSpecimenId = summaryForm.getSelectedSpecimenId();
		System.out.println(selectedSpecimenId);
		if (selectedSpecimenId != null) {
			GenericSpecimen selectedSpecimen = specimenMap.get(selectedSpecimenId);
			
			if (selectedSpecimen != null){
				HashMap<String, GenericSpecimen> aliqutesList = selectedSpecimen
						.getAliquotes();
				HashMap<String, GenericSpecimen> derivedList = selectedSpecimen
						.getDerived();
				if(aliqutesList != null){
					summaryForm.setAliquoteList(aliqutesList.values());
				}
				if(derivedList != null){				
					summaryForm.setDerivedList(derivedList.values());
				}
			}
		}

		summaryForm.setEventId(eventId);
		
		return mapping.findForward(Constants.SUCCESS);
}catch(Exception e)
{
	e.printStackTrace();
	throw e;
}
	}

	private void testData(HttpSession session) {
		MultipleSpecimenList specimenList = new MultipleSpecimenList() {

			private LinkedHashMap<String, GenericSpecimen> list = 
					new LinkedHashMap<String, GenericSpecimen>();

			{
				GenericSpecimen spec1 =getspecimenObject("");
				GenericSpecimen spec2 =getspecimenObject("");
				GenericSpecimen spec3 =getspecimenObject("");
				GenericSpecimen spec4 =getspecimenObject("");

				GenericSpecimen aliq1 =getspecimenObject(spec1.getSpecimenLabel());
				GenericSpecimen aliq2 =getspecimenObject(spec1.getSpecimenLabel());
				GenericSpecimen aliq3 =getspecimenObject(spec1.getSpecimenLabel());
				GenericSpecimen aliq4 =getspecimenObject(spec1.getSpecimenLabel());

				GenericSpecimen deri1 =getspecimenObject(spec1.getSpecimenLabel());
				GenericSpecimen deri2 =getspecimenObject(spec1.getSpecimenLabel());
				
				this.addSpecimen(spec1.getUniqueIdentifier(), spec1);
				this.addSpecimen(spec2.getUniqueIdentifier(), spec2);
				this.addSpecimen(spec3.getUniqueIdentifier(), spec3);
				this.addSpecimen(spec4.getUniqueIdentifier(), spec4);

				LinkedHashMap<String, GenericSpecimen> al = new LinkedHashMap<String, GenericSpecimen>();
				al.put(aliq1.getUniqueIdentifier(), aliq1);
				al.put(aliq2.getUniqueIdentifier(), aliq2);
				al.put(aliq3.getUniqueIdentifier(), aliq3);
				al.put(aliq4.getUniqueIdentifier(), aliq4);
				
				((SpecimenTest)spec1).setAliquots(al);
	
				LinkedHashMap<String, GenericSpecimen> dr = new LinkedHashMap<String, GenericSpecimen>();
				dr.put(deri1.getUniqueIdentifier(), deri1);
				dr.put(deri2.getUniqueIdentifier(), deri2);
				((SpecimenTest)spec1).setDeriveds(dr);
				
			}
			
			public LinkedHashMap<String, GenericSpecimen> getSpecimenList(
					String eventKey) {
				
				return list;
			}
			
			public void addSpecimen(String key, GenericSpecimen spec){
				list.put(key, spec);
				System.out.println(list.size());
			}
		};
		
		session.setAttribute(Constants.COLLECTION_PROTOCOL_httpSession_BEAN, specimenList);
		
	}
	private static int id=1;
	
	public GenericSpecimen getspecimenObject(String q) {
		
		GenericSpecimen tmp = new SpecimenTest(q);
		return tmp;
	}

	class SpecimenTest implements GenericSpecimen{
		String specName = "specimen" + id++;
		String parentName = null;
		LinkedHashMap<String, GenericSpecimen> aliquotList = null;
		LinkedHashMap<String, GenericSpecimen> derivedList = null;
		public SpecimenTest(String parentName){
			this.parentName = parentName;
		}
		public void setAliquots(LinkedHashMap<String, GenericSpecimen> al){
			aliquotList = al;
		}
		public void setDeriveds(LinkedHashMap<String, GenericSpecimen> dr){
			derivedList = dr;
		}

		
		public LinkedHashMap<String, GenericSpecimen> getAliquotes() {
			return aliquotList;
		}

		
		public String getConcentration() {
			return "";
		}

		
		public LinkedHashMap<String, GenericSpecimen> getDerived() {
			return derivedList;
		}

		
		public String getPathologyStatus() {
			return "Malignant";
		}

		
		public String getQuantity() {
			return "13";
		}

		
		public String getSpecimenClassName() {
			return "Tissue";
		}

		
		public String getSpecimenLabel() {
			return specName;
		}

		
		public String getSpecimenType() {
			return "Fix tissue";
		}

		
		public String getStorage() {
			return "";
		}

		
		public String getTissueSide() {
			return "Not specified";
		}

		
		public String getTissueSite() {
			return "Not specified";
		}

		
		public String getUniqueIdentifier() {
			return specName;
		}

		
		public String getParentName() {
			return this.parentName;
		}
		
		public void setSpecimenLabel(String label) {
			specName = label;
			
		}
		
	}
}
