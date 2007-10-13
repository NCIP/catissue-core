package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.HashMap;
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
	 * Unique Serial verson uid.
	 */
	private static final long serialVersionUID = -7978857673984149449L;	
	public static final String ADD_USER_ACTION = "ADD";
	public static final String UPDATE_USER_ACTION = "UPDATE";
	public static final String REQUEST_TYPE_MULTI_SPECIMENS= "Multiple Specimen";
	public static final String REQUEST_TYPE_COLLECTION_PROTOCOL= "Collection Protocol";
	public static final String REQUEST_TYPE_ANTICIPAT_SPECIMENS= "anticipatory specimens";
	
	private List<GenericSpecimen> specimenList = null;
	private List<GenericSpecimen> aliquotList = null; 
	private List<GenericSpecimen> derivedList = null; 
	private String eventId= null;
	private String selectedSpecimenId= null;
	private String userAction;
	private String requestType;
	private Object summaryObject = null;
	private String lastSelectedSpecimenId = null;
	private String containerMap;
	private String targetSuccess;
	private String submitAction;
	
	private boolean showCheckBoxes = true;
	private boolean showbarCode = true;
	private boolean showLabel = true;

	
	private HashMap<String, String> titleMap = new HashMap<String, String>();
	
	private static String collectionProtocolStatus = "";
	
	public  ViewSpecimenSummaryForm()
	{
		titleMap.put(REQUEST_TYPE_MULTI_SPECIMENS, "Specimen details");
		titleMap.put(REQUEST_TYPE_COLLECTION_PROTOCOL, "Specimen requirement(s)");
		titleMap.put(REQUEST_TYPE_ANTICIPAT_SPECIMENS, "Specimen details");
		specimenList = new ArrayList<GenericSpecimen> ();
		aliquotList = new ArrayList<GenericSpecimen> ();
		derivedList = new ArrayList<GenericSpecimen> ();
		userAction = ADD_USER_ACTION;
		requestType = REQUEST_TYPE_COLLECTION_PROTOCOL;
		collectionProtocolStatus = "";
	
	}
	public void reset(ActionMapping mapping, ServletRequest request){
		specimenList = new ArrayList<GenericSpecimen> ();
		aliquotList = new ArrayList<GenericSpecimen> ();
		derivedList = new ArrayList<GenericSpecimen> ();		
	}
	
	public String getTitle(){
		return titleMap.get(requestType);
	}
	public String getUserAction() {
		return userAction;
	}
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	public String getRequestType() {
		return requestType;
	}
	
	public void switchUserAction(){

		if ( UPDATE_USER_ACTION.equals(this.userAction)){
			this.userAction = ADD_USER_ACTION;
		}
		else
		{
			this.userAction = UPDATE_USER_ACTION;
		}
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
	public Object getSummaryObject() {
		return summaryObject;
	}
	public void setSummaryObject(Object summaryObject) {
		this.summaryObject = summaryObject;
	}
	public String getLastSelectedSpecimenId() {
		return lastSelectedSpecimenId;
	}
	public void setLastSelectedSpecimenId(String lastEventId) {
		this.lastSelectedSpecimenId = lastEventId;
	}
	public String getContainerMap() {
		return containerMap;
	}
	public void setContainerMap(String containerMap) {
		this.containerMap = containerMap;
	}
	public String getTargetSuccess() {
		return targetSuccess;
	}
	public void setTargetSuccess(String targetSuccess) {
		this.targetSuccess = targetSuccess;
	}
	public String getSubmitAction() {
		return submitAction;
	}
	public void setSubmitAction(String submitAction) {
		this.submitAction = submitAction;
	}
	public String getCollectionProtocolStatus() {
		return collectionProtocolStatus;
	}
	public static void setCollectionProtocolStatus(String collectionProtocolStatus) {
		ViewSpecimenSummaryForm.collectionProtocolStatus = collectionProtocolStatus;
	}
	public boolean getShowCheckBoxes() {
		return showCheckBoxes;
	}
	public void setShowCheckBoxes(boolean showCheckBoxes) {
		this.showCheckBoxes = showCheckBoxes;
	}
	public boolean getShowbarCode() {
		return showbarCode;
	}
	public void setShowbarCode(boolean showbarCode) {
		this.showbarCode = showbarCode;
	}
	public boolean getShowLabel() {
		return showLabel;
	}
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}
	
}
