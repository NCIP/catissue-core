
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;

@JsonIgnoreProperties({"modifiedAttrs"})
public class ParticipantDetail {
	private Long id;
	
	private String firstName;

	private String lastName;
	
	private String middleName;

	private Date birthDate;

	private Date deathDate;

	private String gender;

	private Set<String> races;

	private String vitalStatus;

	private List<PmiDetail> pmis;

	private String sexGenotype;

	private String ethnicity;

	private String ssn;

	private String activityStatus;
	
	private String empi;
	
	private Set<String> modifiedAttrs = new HashSet<String>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		modifiedAttrs.add("firstName");
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		modifiedAttrs.add("lastName");
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		modifiedAttrs.add("middleName");
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		modifiedAttrs.add("birthDate");
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
		modifiedAttrs.add("deathDate");
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
		modifiedAttrs.add("gender");
	}

	public Set<String> getRaces() {
		return races;
	}

	public void setRaces(Set<String> races) {
		this.races = races;
		modifiedAttrs.add("races");
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
		modifiedAttrs.add("vitalStatus");
	}

	public List<PmiDetail> getPmis() {
		return pmis;
	}

	public void setPmis(List<PmiDetail> pmis) {
		this.pmis = pmis;
		modifiedAttrs.add("pmis");
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
		modifiedAttrs.add("genotype");
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
		modifiedAttrs.add("ethnicity");
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
		modifiedAttrs.add("ssn");
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
		modifiedAttrs.add("activityStatus");
	}

	public String getEmpi() {
		return empi;
	}

	public void setEmpi(String empi) {
		this.empi = empi;
		modifiedAttrs.add("empi");
	}
	
	public Set<String> getModifiedAttrs() {
		return modifiedAttrs;
	}

	public static ParticipantDetail from(Participant participant) {
		ParticipantDetail result = new ParticipantDetail();
		result.setId(participant.getId());
		result.setFirstName(participant.getFirstName());
		result.setLastName(participant.getLastName());
		result.setMiddleName(participant.getMiddleName());
		result.setActivityStatus(participant.getActivityStatus());
		result.setBirthDate(participant.getBirthDate());
		result.setDeathDate(participant.getDeathDate());
		result.setEthnicity(participant.getEthnicity());
		result.setGender(participant.getGender());
		result.setEmpi(participant.getEmpi());				
		result.setPmis(PmiDetail.from(participant.getPmis())); 
		result.setRaces(new HashSet<String>(participant.getRaces()));
		result.setSexGenotype(participant.getSexGenotype());
		result.setSsn(participant.getSocialSecurityNumber());
		result.setVitalStatus(participant.getVitalStatus());
		return result;
	}
	
	public static List<ParticipantDetail> from(List<Participant> participants) {
		List<ParticipantDetail> result = new ArrayList<ParticipantDetail>();
		for (Participant participant : participants) {
			result.add(ParticipantDetail.from(participant));
		}
		
		return result;
	}
}
