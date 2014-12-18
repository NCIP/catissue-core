
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

public class ParticipantFactoryImpl implements ParticipantFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private final String SSN = "social security number";

	private final String RACE = "race";

	private final String ETHNICITY = "ethnicity";

	private final String VITAL_STATUS = "vital status";

	private final String BIRTH_DATE = "birth date";

	private final String DEATH_DATE = "death date";

	private final String GENDER = "gender";

	private final String SEX_GENOTYPE = "sexGenotype";

	private final String MEDICAL_RECORD_NUMBER = "medical record number";

	private final String SITE = "site";

	private final String VITAL_STATUS_DEATH = "Death";
	
	private static final Pattern SSN_PATTERN = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");

	@Override
	public Participant createParticipant(ParticipantDetail details) {
		ObjectCreationException oce = new ObjectCreationException();
		
		Participant participant = new Participant();		
		setSsn(participant, details.getSsn(), oce);
		setName(participant, details);
		setVitalStatus(participant, details.getVitalStatus(), oce);
		setBirthDate(participant, details.getBirthDate(), oce);
		setDeathDate(participant, details.getDeathDate(), oce);
		setActivityStatus(participant, details.getActivityStatus(), oce);
		setSexGenotype(participant, details.getSexGenotype(), oce);
		setGender(participant, details.getGender(), oce);
		setRace(participant, details.getRace(), oce);
		setEthnicity(participant, details.getEthnicity(), oce);
		setPmi(participant, details.getPmis(), oce);
		
		participant.setEmpi(details.getEmpi());
		
		oce.checkErrorAndThrow();
		return participant;
	}

	private void setDeathDate(Participant participant, Date deathDate, ObjectCreationException exception) {
		if (deathDate == null) {
			return;
		}
		
		if (!VITAL_STATUS_DEATH.equals(participant.getVitalStatus()) || 
				(participant.getBirthDate() != null && deathDate.before(participant.getBirthDate()))) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
		}
		
		participant.setDeathDate(deathDate);
	}

	private void setBirthDate(Participant participant, Date birthDate, ObjectCreationException exception) {
		if (birthDate == null) {
			return;
		}
		
		if (birthDate.after(Calendar.getInstance().getTime())) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE);
			return;
		}
		
		participant.setBirthDate(birthDate);
	}

	private void setName(Participant participant, ParticipantDetail detail) {
		participant.setFirstName(detail.getFirstName());
		participant.setMiddleName(detail.getMiddleName());
		participant.setLastName(detail.getLastName());
	}

	private void setSsn(Participant participant, String ssn, ObjectCreationException oce) {
		if (StringUtils.isBlank(ssn)) {
			return;
		}
		
		if (isSsnValid(ssn)) {
			participant.setSocialSecurityNumber(ssn);
		} else {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SSN);
		}
	}

	private void setActivityStatus(Participant participant, String activityStatus, ObjectCreationException oce) {
		if (StringUtils.isBlank(activityStatus)) {
			participant.setActive();
			return;
		}
		
		if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.toString())) {
			participant.updateActivityStatus(activityStatus);
			return;
		}
		
		oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setVitalStatus(Participant participant, String vitalStatus, ObjectCreationException oce) {
		if (!StringUtils.isBlank(vitalStatus) && !isValidPv(vitalStatus, VITAL_STATUS)) {
			oce.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, VITAL_STATUS);
			return;
		}
		
		participant.setVitalStatus(vitalStatus);
	}

	private void setGender(Participant participant, String gender, ObjectCreationException oce) {
		if (!StringUtils.isBlank(gender) && !isValidPv(gender, GENDER)) {
			oce.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, GENDER);
			return;
		}
		
		participant.setGender(gender);
	}

	private void setRace(Participant participant, Set<String> raceList, ObjectCreationException oce) {
		if (raceList == null || raceList.isEmpty()) {
			return;
		}

		String[] races = raceList.toArray(new String[raceList.size()]);
		if (!isValidPv(races, RACE)) {
			oce.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, RACE);
			return;
		}
		
		participant.setRaceColl(raceList);
	}

	private void setEthnicity(Participant participant, String ethnicity, ObjectCreationException oce) {
		if (!StringUtils.isBlank(ethnicity) && !isValidPv(ethnicity, ETHNICITY)) {
			oce.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, ETHNICITY);
			return;
		}
		
		participant.setEthnicity(ethnicity);
	}

	private void setPmi(
			Participant participant, 
			List<ParticipantMedicalIdentifierNumberDetail> pmis,
			ObjectCreationException oce) {
		
		if (pmis == null || pmis.isEmpty()) {
			return;
		}
		
		Map<String, ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();		
		for (ParticipantMedicalIdentifierNumberDetail pmiDetail : pmis) {
			ParticipantMedicalIdentifier pmi = getPmi(pmiDetail, oce);
			if (pmi != null) {
				map.put(pmi.getSite().getName(), pmi);
			}
		}

		participant.setPmiCollection(map);
	}

	private ParticipantMedicalIdentifier getPmi(ParticipantMedicalIdentifierNumberDetail pmiDetail, ObjectCreationException oce) {
		Site site = daoFactory.getSiteDao().getSite(pmiDetail.getSiteName());		
		if (site == null) {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SITE);
			return null;
		}
		
		if (StringUtils.isBlank(pmiDetail.getMrn())) {
			oce.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, MEDICAL_RECORD_NUMBER);
			return null;
		}
		
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setSite(site);
		pmi.setMedicalRecordNumber(pmiDetail.getMrn());
		return pmi;
	}

	private boolean isSsnValid(String ssn) {
		try {
			return SSN_PATTERN.matcher(ssn).matches();
		} catch (Exception exp) {
			return false;
		}
	}

	private void setSexGenotype(Participant participant, String sexGenotype, ObjectCreationException oce) {
		if (!StringUtils.isBlank(sexGenotype) && !isValidPv(sexGenotype, SEX_GENOTYPE)) {
			oce.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, SEX_GENOTYPE);
			return;
		}
		
		participant.setSexGenotype(sexGenotype);
	}
}
