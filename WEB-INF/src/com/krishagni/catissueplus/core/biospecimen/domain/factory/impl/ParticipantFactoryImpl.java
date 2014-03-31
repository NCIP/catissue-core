
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.Site;

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

	private final String MEDICAL_RECORD_NUMBER = "medical record number";

	private final String SITE = "site";

	private final String VITAL_STATUS_DEATH = "Death";

	@Override
	public Participant createParticipant(ParticipantDetail details) {
		Participant participant = new Participant();
		ObjectCreationException exception = new ObjectCreationException();

		setSsn(participant, details.getSsn(), exception);
		setName(participant, details, exception);
		setDates(participant, details, exception);
		setActivityStatus(participant, details, exception);
		setVitalStatus(participant, details, exception);
		setGender(participant, details, exception);
		setRace(participant, details, exception);
		setEthnicity(participant, details, exception);
		setPmi(participant, details, exception);
		exception.checkErrorAndThrow();
		return participant;
	}

	private void setSsn(Participant participant, String ssn, ObjectCreationException exception) {
		if (isBlank(ssn)) {
			return;
		}
		if (isSsnValid(ssn)) {
			participant.setSocialSecurityNumber(ssn);
		}
		else {
			exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SSN);
		}
	}

	private void setName(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		participant.setFirstName(details.getFirstName());
		participant.setLastName(details.getLastName());
		participant.setMiddleName(details.getMiddleName());
	}

	private void setDates(Participant participant, ParticipantDetail details, ObjectCreationException exception) {

		Date birthDate = details.getBirthDate();
		Date deathDate = details.getDeathDate();

		if (birthDate != null) {
			if (birthDate.after(new Date())) {
				exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE);
			}
			participant.setBirthDate(birthDate);
			if (deathDate != null) {
				if ((!VITAL_STATUS_DEATH.equals(details.getVitalStatus()) || deathDate.before(birthDate))) {
					exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
				}
				participant.setDeathDate(deathDate);
			}
		}

	}

	private void setActivityStatus(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		if (isBlank(details.getActivityStatus())) {
			participant.setActive();
			return;
		}
		if (isValidPv(details.getActivityStatus(), Status.ACTIVITY_STATUS.toString())) {
			participant.updateActivityStatus(details.getActivityStatus());
			return;
		}
		exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setVitalStatus(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		if (!isBlank(details.getVitalStatus()) && !isValidPv(details.getVitalStatus(), VITAL_STATUS)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION,VITAL_STATUS);
			return;
		}
			participant.setVitalStatus(details.getVitalStatus());
	}

	private void setGender(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		if (!isBlank(details.getGender()) && !isValidPv(details.getGender(), GENDER)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, GENDER);
			return;
		}
		participant.setGender(details.getGender());
	}

	private void setRace(Participant participant, ParticipantDetail details, ObjectCreationException exception) {

		if (details.getRace() == null || details.getRace().isEmpty()) {
			return;
		}

		Set<String> raceList = details.getRace();
		String[] races = raceList.toArray(new String[raceList.size()]);
		if (isValidPv(races, RACE)) {
			participant.setRaceColl(raceList);
			return;
		}
		exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, RACE);

	}

	private void setEthnicity(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		if (!isBlank(details.getEthnicity()) && !isValidPv(details.getEthnicity(), ETHNICITY)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, ETHNICITY);
		return;
		}
			participant.setEthnicity(details.getEthnicity());
	}

	private void setPmi(Participant participant, ParticipantDetail details, ObjectCreationException exception) {
		List<ParticipantMedicalIdentifierNumberDetail> mrns = details.getPmiCollection();
		Map<String, ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();
		if (mrns != null && mrns.size() > 0) {
			for (ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail : mrns) {
				ParticipantMedicalIdentifier medicalIdentifier = getMedicalIdentifier(medicalRecordNumberDetail,exception);
				map.put(medicalIdentifier.getSite().getName(), medicalIdentifier);
			}
			participant.setPmiCollection(map);
		}

	}

	private ParticipantMedicalIdentifier getMedicalIdentifier(
			ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail, ObjectCreationException exception) {
		Site site = daoFactory.getSiteDao().getSite(medicalRecordNumberDetail.getSiteName());
		if (site == null) {
			exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SITE);
		}
		if (isBlank(medicalRecordNumberDetail.getMrn())) {
			exception.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, MEDICAL_RECORD_NUMBER);
		}
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setSite(site);
		pmi.setMedicalRecordNumber(medicalRecordNumberDetail.getMrn());
		return pmi;
	}

	private boolean isSsnValid(String ssn) {
		boolean result = true;
		try {
			Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");
			Matcher mat = pattern.matcher(ssn);
			result = mat.matches();
		}
		catch (Exception exp) {
			result = false;
		}
		return result;
	}

//	private void addError(CatissueErrorCode event, String field) {
//		objectCreationException.addError(event, field);
//	}
//
//	private void addError(boolean isAddErro, CatissueErrorCode event, String field) {
//		if (isAddErro) {
//			addError(event, field);
//		}
//	}
}
