
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantUtil;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.MpiGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class Participant extends BaseExtensionEntity {
	private static final String DEF_SOURCE = "OpenSpecimen";

	private static final String ENTITY_NAME = "participant";

	public static final String EXTN = "ParticipantExtension";

	private String source = DEF_SOURCE;

	private String lastName;

	private String firstName;

	private String middleName;

	private Date birthDate;

	private String gender;

	private String sexGenotype;

	private Set<String> races = new HashSet<>();

	private String ethnicity;

	private String uid;

	private String activityStatus;

	private Date deathDate;

	private String vitalStatus;
	
	private String empi;
	
	private Set<ParticipantMedicalIdentifier> pmis = new HashSet<>();

	private Set<CollectionProtocolRegistration> cprs = new HashSet<>();

	private transient Long cpId = -1L;

	public String getSource() {
		return StringUtils.isBlank(source) ? DEF_SOURCE : source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
	}

	public Set<String> getRaces() {
		return races;
	}

	public void setRaces(Set<String> races) {
		this.races = races;
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
		if (this.activityStatus != null && this.activityStatus.equals(activityStatus)) {
			return;
		}
		
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (this.activityStatus != null && Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete();
		}		

		this.activityStatus = activityStatus;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public String getEmpi() {
		return empi;
	}

	public void setEmpi(String empi) {
		this.empi = empi;
	}
	
	public Set<ParticipantMedicalIdentifier> getPmis() {
		return pmis;
	}

	public void setPmis(Set<ParticipantMedicalIdentifier> pmis) {
		this.pmis = pmis;
	}

	@NotAudited
	public Set<CollectionProtocolRegistration> getCprs() {
		return cprs;
	}

	public void setCprs(Set<CollectionProtocolRegistration> cprs) {
		this.cprs = cprs;
	}
	
	public void update(Participant participant) {
		setFirstName(participant.getFirstName());
		setLastName(participant.getLastName());
		setMiddleName(participant.getMiddleName());
		setUid(participant.getUid());
		setEmpi(participant.getEmpi());
		setActivityStatus(participant.getActivityStatus());
		setSexGenotype(participant.getSexGenotype());
		setVitalStatus(participant.getVitalStatus());
		setGender(participant.getGender());
		setEthnicity(participant.getEthnicity());
		setBirthDate(participant.getBirthDate());
		setDeathDate(participant.getDeathDate());
		setExtension(participant.getExtension());
		CollectionUpdater.update(getRaces(), participant.getRaces());
		updatePmis(participant);
	}

	public void updateActivityStatus(String activityStatus) {
		setActivityStatus(activityStatus);
	}
	
	public void setActive() {
		setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.activityStatus);
	}

	public void delete() {
		checkActiveDependents();

		disableMrns();		
		setUid(Utility.getDisabledValue(getUid(), 50));
		setEmpi(Utility.getDisabledValue(getEmpi(), 50));
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	public void updatePmi(ParticipantMedicalIdentifier pmi) {
		ParticipantMedicalIdentifier existing = getPmiBySite(getPmis(), pmi.getSite().getName());
		if (existing == null) {
			pmi.setParticipant(this);
			getPmis().add(pmi);
		} else {
			existing.setMedicalRecordNumber(pmi.getMedicalRecordNumber());
		}
	}
	
	public Set<Site> getMrnSites() {
		Set<Site> result = new HashSet<Site>();
		for (ParticipantMedicalIdentifier pmi : getPmis()) {
			result.add(pmi.getSite());
		}
		
		return result;
	}
	
	public void setEmpiIfEmpty() {
		MpiGenerator generator = ParticipantUtil.getMpiGenerator();
		if (generator == null) {
			return;
		}

		if (StringUtils.isNotBlank(empi)) {
			throw OpenSpecimenException.userError(ParticipantErrorCode.MANUAL_MPI_NOT_ALLOWED);
		}
		
		setEmpi(generator.generateMpi());
	}

	@Override
	public String getEntityType() {
		return EXTN;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	@Override
	public Long getCpId() {
		return cpId;
	}

	public List<ParticipantMedicalIdentifier> getPmisOrderedById() {
		return getPmis().stream()
			.sorted((p1, p2) -> ObjectUtils.compare(p1.getId(), p2.getId()))
			.collect(Collectors.toList());
	}
	
	public CollectionProtocolRegistration getCpr(CollectionProtocol cp) {
		return getCprs().stream().filter(cpr -> cpr.getCollectionProtocol().equals(cp)).findFirst().orElse(null);
	}

	public static String getEntityName() {
		return ENTITY_NAME;
	}

	private void updatePmis(Participant participant) {
		for (ParticipantMedicalIdentifier pmi : participant.getPmis()) {
			ParticipantMedicalIdentifier existing = getPmiBySite(getPmis(), pmi.getSite().getName());
			if (existing == null) {
				ParticipantMedicalIdentifier newPmi = new ParticipantMedicalIdentifier();
				newPmi.setParticipant(this);
				newPmi.setSite(pmi.getSite());
				newPmi.setMedicalRecordNumber(pmi.getMedicalRecordNumber());
				getPmis().add(newPmi);				
			} else {
				existing.setMedicalRecordNumber(pmi.getMedicalRecordNumber());
			}
		}
		
		Iterator<ParticipantMedicalIdentifier> iter = getPmis().iterator();
		while (iter.hasNext()) {
			ParticipantMedicalIdentifier existing = iter.next();			
			if (getPmiBySite(participant.getPmis(), existing.getSite().getName()) == null) {
				iter.remove();
			}			
		}		
	}

	private void disableMrns() {
		for (ParticipantMedicalIdentifier pmi : getPmis()) {
			pmi.setMedicalRecordNumber(Utility.getDisabledValue(pmi.getMedicalRecordNumber(), 255));
		}
	}

	private void checkActiveDependents() {
		for (CollectionProtocolRegistration cpr : getCprs()) {
			if (cpr.isActive()) {
				throw OpenSpecimenException.userError(ParticipantErrorCode.REF_ENTITY_FOUND);
			}
		}
	}
	
	private ParticipantMedicalIdentifier getPmiBySite(Collection<ParticipantMedicalIdentifier> pmis, String siteName) {
		ParticipantMedicalIdentifier result = null;
		
		for (ParticipantMedicalIdentifier pmi : pmis) {
			if (pmi.getSite().getName().equals(siteName)) {
				result = pmi;
				break;				
			}
		}
		
		return result;
	}
}
