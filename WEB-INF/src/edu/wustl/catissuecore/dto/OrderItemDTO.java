/**
 */

package edu.wustl.catissuecore.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class OrderItemDTO {

	private static final String EMPTY_STRING = "";
	
	String specLabel;

	String specimenType;

	String specimenClass;

	String tissueSite;

	String tissueSide;

	String externalId ="";
	String externalValue="";
//	Map<String, String> externalValues = new HashMap<String, String>();

	Double availableQuantity;

	String scgName;

	String clinicalDiagnosis;

	String clinicalStatus;

	String collectionSite;

	String cpeLabel;

	Double studyEventPoint;

	String sprNumber;

	String registrationDate;

	String ppid;

	String shortTitle;

	String title;

	String ssn;

	String firstName;

	String lastName;

	String middleName;

	String gender;

	String race;

	String ethnicity;

	String birthDate;

	String vitalStatus;

	String deathDate;

	String mrn = "";
	String siteName = "";
//	Map<String, String> mrns = new HashMap<String, String>();

	Double requestedQuantity;

	Double distributedQuantity = null;

	String status;

	String description;

	Long OrderItemId;

	Long specimenId;

	String specimenContainerName;

	String positionDimensionOne;

	String positionDimensionTwo;

	public String getSpecLabel() {
		return specLabel;
	}

	public void setSpecLabel(String specLabel) {
		this.specLabel = specLabel;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public Double getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Double getRequestedQuantity() {
		return requestedQuantity;
	}

	public void setRequestedQuantity(Double requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getOrderItemId() {
		return OrderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		OrderItemId = orderItemId;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}

	public Double getDistributedQuantity() {
		return distributedQuantity;
	}

	public void setDistributedQuantity(Double distributedQuantity) {
		this.distributedQuantity = distributedQuantity;
	}

	public String getSpecimenContainerName() {
		return specimenContainerName;
	}

	public void setSpecimenContainerName(String specimenContainerName) {
		this.specimenContainerName = specimenContainerName;
	}

	public String getPositionDimensionOne() {
		return positionDimensionOne;
	}

	public void setPositionDimensionOne(String positionDimensionOne) {
		this.positionDimensionOne = positionDimensionOne;
	}

	public String getPositionDimensionTwo() {
		return positionDimensionTwo;
	}

	public void setPositionDimensionTwo(String positionDimensionTwo) {
		this.positionDimensionTwo = positionDimensionTwo;
	}

	public String getTissueSite() {
		return tissueSite;
	}

	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}

	public String getTissueSide() {
		return tissueSide;
	}

	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}

	public String getScgName() {
		return scgName;
	}

	public void setScgName(String scgName) {
		this.scgName = scgName;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public String getCollectionSite() {
		return collectionSite;
	}

	public void setCollectionSite(String collectionSite) {
		this.collectionSite = collectionSite;
	}

	public String getCpeLabel() {
		return cpeLabel;
	}

	public void setCpeLabel(String cpeLabel) {
		this.cpeLabel = cpeLabel;
	}

	public Double getStudyEventPoint() {
		return studyEventPoint;
	}

	public void setStudyEventPoint(Double studyEventPoint) {
		this.studyEventPoint = studyEventPoint;
	}

	public String getSprNumber() {
		return sprNumber;
	}

	public void setSprNumber(String sprNumber) {
		this.sprNumber = sprNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public String getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	
//	public Map<String, String> getMrns() {
//		return mrns;
//	}
//
//	public void setMrns(Map<String, String> mrns) {
//		this.mrns = mrns;
//	}
//	
//
//	
//	public Map<String, String> getExternalValues() {
//		return externalValues;
//	}
//
//	
//	public void setExternalValues(Map<String, String> externalValues) {
//		this.externalValues = externalValues;
//	}

	
	public String getExternalId() {
		return externalId;
	}

	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	
	public String getExternalValue() {
		return externalValue;
	}

	
	public void setExternalValue(String externalValue) {
		this.externalValue = externalValue;
	}

	
	public String getMrn() {
		return mrn;
	}

	
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	
	public String getSiteName() {
		return siteName;
	}

	
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void populateSpecimen(Specimen specimen) {
		SpecimenPosition position = specimen.getSpecimenPosition();
		if(position != null){
			this.setSpecimenContainerName(position.getStorageContainer().getName());
			this.setPositionDimensionOne(position.getPositionDimensionOneString());
			this.setPositionDimensionTwo(position.getPositionDimensionTwoString());
		}
		this.setSpecLabel(specimen.getLabel());
		this.setSpecimenType(specimen.getSpecimenType());
		this.setSpecimenClass(specimen.getSpecimenClass());
		this.setTissueSite(specimen.getTissueSite());
		this.setTissueSide(specimen.getTissueSide());
		this.setAvailableQuantity(specimen.getAvailableQuantity());
//		Collection<ExternalIdentifier> exts = specimen.getExternalIdentifierCollection();
//		if(exts != null){
//			for (ExternalIdentifier exId : exts) {
//				externalValues.put(exId.getName(), exId.getValue());
//			}
//		}
		
		
	}

	public void populateScg(SpecimenCollectionGroup scg) {
		this.setScgName(scg.getName());
		this.setClinicalDiagnosis(scg.getClinicalDiagnosis()==null?EMPTY_STRING:scg.getClinicalDiagnosis());
		this.setClinicalStatus(scg.getClinicalStatus()==null?EMPTY_STRING:scg.getClinicalStatus());
		this.setCollectionSite(scg.getSpecimenCollectionSite().getName());
		this.setSprNumber(scg.getSurgicalPathologyNumber()==null?EMPTY_STRING:scg.getSurgicalPathologyNumber());
		this.setCpeLabel(scg.getCollectionProtocolEvent().getCollectionPointLabel());
		this.setStudyEventPoint(scg.getCollectionProtocolEvent().getStudyCalendarEventPoint());
		
	}

	public void populateCpr(CollectionProtocolRegistration cpr) {
		if(cpr.getRegistrationDate() == null){
			this.setRegistrationDate(cpr.getRegistrationDate()!=null?cpr.getRegistrationDate().toString():EMPTY_STRING);
		}else{
			String regDate = CommonUtilities.parseDateToString(cpr.getRegistrationDate(), CommonServiceLocator.getInstance()
					.getDatePattern());
			this.setRegistrationDate(regDate);
		}
		this.setPpid(cpr.getProtocolParticipantIdentifier()==null?EMPTY_STRING:cpr.getProtocolParticipantIdentifier());
		this.setShortTitle(cpr.getCollectionProtocol().getShortTitle());//.replace(",", "\\,"));
		this.setTitle(cpr.getCollectionProtocol().getTitle());//.replace(",", "\\,"));  
	}

	public void populateParticipant(Participant participant) {
		this.setSsn(participant.getSocialSecurityNumber()==null?EMPTY_STRING:participant.getSocialSecurityNumber());
		this.setFirstName(participant.getFirstName()==null?EMPTY_STRING:participant.getFirstName());
		this.setLastName(participant.getLastName()==null?EMPTY_STRING:participant.getLastName());
		this.setMiddleName(participant.getMiddleName()==null?EMPTY_STRING:participant.getMiddleName());
		this.setGender(participant.getGender()==null?EMPTY_STRING:participant.getGender());
		this.setEthnicity(participant.getEthnicity()==null?EMPTY_STRING:participant.getEthnicity());
		if(participant.getBirthDate() == null){
			this.setBirthDate(participant.getBirthDate()!=null?participant.getBirthDate().toString():EMPTY_STRING);
		}else{
			String birthDate = CommonUtilities.parseDateToString(participant.getBirthDate(), CommonServiceLocator.getInstance()
					.getDatePattern());
			this.setBirthDate(birthDate);
		}
		if(participant.getBirthDate() == null){
			this.setDeathDate(participant.getDeathDate()!=null?participant.getDeathDate().toString():EMPTY_STRING);
		}else{
			String deathDate = CommonUtilities.parseDateToString(participant.getDeathDate(), CommonServiceLocator.getInstance()
					.getDatePattern());
			this.setDeathDate(deathDate);
		}
		this.setVitalStatus(participant.getVitalStatus()==null?EMPTY_STRING:participant.getVitalStatus());
		Collection<Race> raceColl = participant.getRaceCollection();
		List<String> raceList = new java.util.ArrayList<String>();
		if(raceColl != null){
			for (Race race : raceColl) {
				raceList.add(race.getRaceName());
			}
			this.setRace(raceList.toString());
		}
//		Collection<ParticipantMedicalIdentifier> pmiColl = participant.getParticipantMedicalIdentifierCollection();
//		if(pmiColl != null){
//			for (ParticipantMedicalIdentifier pmi : pmiColl) {
//				if(!StringUtils.isBlank(pmi.getMedicalRecordNumber()))
//				this.mrns.put(pmi.getMedicalRecordNumber(), pmi.getSite().getName());
//			}
//		}
		}

}
