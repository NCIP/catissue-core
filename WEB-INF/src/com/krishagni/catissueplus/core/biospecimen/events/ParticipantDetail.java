
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@ListenAttributeChanges
public class ParticipantDetail extends AttributeModifiedSupport {
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

	private String uid;

	private String activityStatus;
	
	private String empi;
	
	private boolean phiAccess;
	
	private Set<CprSummary> registeredCps;
	
	private ExtensionDetail extensionDetail;
	
	// For Update participant through BO
	private String cpShortTitle;
	
	private String ppid;

	// Used for CP based custom fields
	private Long cpId = -1L;
	
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Set<String> getRaces() {
		return races;
	}

	public void setRaces(Set<String> races) {
		this.races = races;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public List<PmiDetail> getPmis() {
		return pmis;
	}

	public void setPmis(List<PmiDetail> pmis) {
		this.pmis = pmis;
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getEmpi() {
		return empi;
	}

	public void setEmpi(String empi) {
		this.empi = empi;
	}
	
	public boolean getPhiAccess() {
		return phiAccess;
	}

	public void setPhiAccess(boolean phiAccess) {
		this.phiAccess = phiAccess;
	}

	public Set<CprSummary> getRegisteredCps() {
		return registeredCps;
	}

	public void setRegisteredCps(Set<CprSummary> registeredCps) {
		this.registeredCps = registeredCps;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}
	
	@JsonIgnore
	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	@JsonIgnore
	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public static ParticipantDetail from(Participant participant, boolean excludePhi) {
		return from(participant, excludePhi, null);
	}

	public static ParticipantDetail from(Participant participant, boolean excludePhi, List<CollectionProtocolRegistration> cprs) {
		ParticipantDetail result = new ParticipantDetail();
		result.setId(participant.getId());
		result.setFirstName(excludePhi ? "###" : participant.getFirstName());
		result.setLastName(excludePhi ? "###" : participant.getLastName());
		result.setMiddleName(excludePhi ? "###" : participant.getMiddleName());
		result.setActivityStatus(participant.getActivityStatus());
		result.setBirthDate(excludePhi ? null : participant.getBirthDate());
		result.setDeathDate(excludePhi ? null : participant.getDeathDate());
		result.setEthnicity(participant.getEthnicity());
		result.setGender(participant.getGender());
		result.setEmpi(excludePhi ? "###" : participant.getEmpi());				
		result.setPmis(PmiDetail.from(participant.getPmis(), excludePhi)); 
		result.setRaces(new HashSet<String>(participant.getRaces()));
		result.setSexGenotype(participant.getSexGenotype());
		result.setUid(excludePhi ? "###" : participant.getUid());
		result.setVitalStatus(participant.getVitalStatus());
		result.setPhiAccess(!excludePhi);
		result.setRegisteredCps(getCprSummaries(cprs));
		result.setExtensionDetail(ExtensionDetail.from(participant.getExtension(), excludePhi));
		return result;
	}
	
	public static List<ParticipantDetail> from(List<Participant> participants, boolean excludePhi) {
		List<ParticipantDetail> result = new ArrayList<ParticipantDetail>();
		for (Participant participant : participants) {
			result.add(ParticipantDetail.from(participant, excludePhi));
		}
		
		return result;
	}
	
	public static Set<CprSummary> getCprSummaries(List<CollectionProtocolRegistration> cprs) {
		if (CollectionUtils.isEmpty(cprs)) {
			return Collections.emptySet();
		}

		return cprs.stream().map(ParticipantDetail::getCprSummary).collect(Collectors.toSet());
	}

	private static CprSummary getCprSummary(CollectionProtocolRegistration cpr) {
		CprSummary cprSummary = new CprSummary();
		cprSummary.setCpId(cpr.getCollectionProtocol().getId());
		cprSummary.setCprId(cpr.getId());
		cprSummary.setCpShortTitle(cpr.getCollectionProtocol().getShortTitle());
		return cprSummary;
	}
}
