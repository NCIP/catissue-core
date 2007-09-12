package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.bean.GenericSpecimen;


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
	private static final Collection<GenericSpecimen> EMPTY_COLL = new LinkedHashSet<GenericSpecimen> ();
	
	private Collection<GenericSpecimen> specimenList = EMPTY_COLL ;
	private Collection<GenericSpecimen> aliquoteList = EMPTY_COLL; 
	private Collection<GenericSpecimen> derivedList = EMPTY_COLL; 
	private String eventId= null;
	private String selectedSpecimenId= null;
	
	public String getSelectedSpecimenId() {
		return selectedSpecimenId;
	}
	
	public void setSelectedSpecimenId(String selectedSpecimenId) {
		this.selectedSpecimenId = selectedSpecimenId;
	}
	
	public Collection<GenericSpecimen> getSpecimenList() {
		return specimenList;
	}
	public void setSpecimenList(Collection<GenericSpecimen> specimenList) {
		this.specimenList = specimenList;
	}
	public Collection<GenericSpecimen> getAliquoteList() {
		return aliquoteList;
	}
	public void setAliquoteList(Collection<GenericSpecimen> aliquoteList) {
		if (aliquoteList == null){
			this.aliquoteList = EMPTY_COLL;
		}else{
			this.aliquoteList = aliquoteList;
		}
	}
	public Collection<GenericSpecimen> getDerivedList() {
		return derivedList;
	}
	public void setDerivedList(Collection<GenericSpecimen> derivedList) {
		if (derivedList == null){
			this.derivedList = EMPTY_COLL;
		}else{
			this.derivedList = derivedList;
		}
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	
}
