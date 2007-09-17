package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;


/**
 * action form used to carry specimens, aliquotes and derived
 * for specimen summary page.
 * @author abhijit_naik
 *
 */
public class ViewSpecimenSummaryForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7978857673984149449L;
	private static final List<GenericSpecimen> EMPTY_COLL = new ArrayList<GenericSpecimen> ();
	
	private List<GenericSpecimen> specimenList = EMPTY_COLL ;
	private List<GenericSpecimen> aliquotList = EMPTY_COLL; 
	private List<GenericSpecimen> derivedList = EMPTY_COLL; 
	private String eventId= null;
	private String selectedSpecimenId= null;
	public ViewSpecimenSummaryForm(){
		specimenList = new ArrayList<GenericSpecimen> ();
		aliquotList = new ArrayList<GenericSpecimen> ();
		derivedList = new ArrayList<GenericSpecimen> ();

	}
	public void reset(ActionMapping mapping, ServletRequest request){
		specimenList = new ArrayList<GenericSpecimen> ();
		aliquotList = new ArrayList<GenericSpecimen> ();
		derivedList = new ArrayList<GenericSpecimen> ();		
	}
	
	public String getSelectedSpecimenId() {
		return selectedSpecimenId;
	}
	public void setSelectedSpecimenId(String selectedSpecimenId) {
		this.selectedSpecimenId = selectedSpecimenId;
	}
	
	public List<GenericSpecimen> getSpecimenList() {
		return specimenList;
	}
	public void setSpecimenList(List<GenericSpecimen> specimenList) {
		if (specimenList != null)
		{
			this.specimenList = specimenList;
		}
	}
	
	public List<GenericSpecimen> getAliquotList() {
		return aliquotList;
	}
	public void setAliquotList(List<GenericSpecimen> aliquoteList) {
		if (aliquoteList != null){
			this.aliquotList = aliquoteList;
		}
	}
	public List<GenericSpecimen> getDerivedList() {
		return derivedList;
	}
	
	public void setDerivedList(List<GenericSpecimen> derivedList) {
		if (derivedList != null){
			this.derivedList = derivedList;
		}
	}
	
	public GenericSpecimen getDerived(int index){

		while(index >= derivedList.size()){
			derivedList.add( getNewSpecimen());
		}
		return derivedList.get(index);
	}

	public void setDerived(int index, GenericSpecimen derivedSpecimen){
		derivedList.add(index,derivedSpecimen);
	}

	public GenericSpecimen getAliquot(int index){

		while(index >= aliquotList.size()){
			aliquotList.add( getNewSpecimen());
		}
				
		return aliquotList.get(index);
	}

	public void setAliquot(int index, GenericSpecimen aliquotSpecimen){
		aliquotList.add(index,aliquotSpecimen);
	}

	public GenericSpecimen getSpecimen(int index){

		while(index >= specimenList.size()){
			specimenList.add( getNewSpecimen());
		}
		return specimenList.get(index);
	}

	public void setSpecimen(int index, GenericSpecimen specimen){
		aliquotList.add(index,specimen);
	}
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	private GenericSpecimen getNewSpecimen()
	{
		return new GenericSpecimenVO();
	}
}
