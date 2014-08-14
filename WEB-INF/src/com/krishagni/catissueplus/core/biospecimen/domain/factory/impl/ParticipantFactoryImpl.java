
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

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantPatchDetail;
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

	@Override
	public Participant createParticipant(ParticipantDetail details) {
		Participant participant = new Participant();
		ObjectCreationException exception = new ObjectCreationException();

		setSsn(participant, details.getSsn(), exception);
		setFirstName(participant, details.getFirstName(), exception);
		setLastName(participant, details.getLastName(), exception);
		setMiddleName(participant, details.getMiddleName(), exception);
		setVitalStatus(participant, details.getVitalStatus(), exception);
		setBirthDate(participant, details.getBirthDate(), exception);
		setDeathDate(participant, details.getDeathDate(), exception);
		setActivityStatus(participant, details.getActivityStatus(), exception);
		setSexGenotype(participant, details.getSexGenotype(), exception);
		setGender(participant, details.getGender(), exception);
		setRace(participant, details.getRace(), exception);
		setEthnicity(participant, details.getEthnicity(), exception);
		setPmi(participant, details.getPmiCollection(), exception);
		exception.checkErrorAndThrow();
		return participant;
	}

	@Override
	public Participant patchParticipant(Participant participant, ParticipantPatchDetail detail) {
		ObjectCreationException exception = new ObjectCreationException();
		if (detail.isFirstNameModified()) {
			setFirstName(participant, detail.getFirstName(), exception);
		}

		if (detail.isLastNameModified()) {
			setLastName(participant, detail.getLastName(), exception);
		}

		if (detail.isMiddleNameModified()) {
			setMiddleName(participant, detail.getMiddleName(), exception);
		}

		if (detail.isGenderModified()) {
			setGender(participant, detail.getGender(), exception);
		}

		if (detail.isSexGenotypeModified()) {
			setSexGenotype(participant, detail.getSexGenotype(), exception);
		}

		if (detail.isEthnicityModified()) {
			setEthnicity(participant, detail.getEthnicity(), exception);
		}

		if (detail.isSSNModified()) {
			setSsn(participant, detail.getSsn(), exception);
		}
		if (detail.isActivityStatusModified()) {
			setActivityStatus(participant, detail.getActivityStatus(), exception);
		}

		if (detail.isVitalSatusModified()) {
			setVitalStatus(participant, detail.getVitalStatus(), exception);
		}

		if (detail.isBirthDateModified()) {
			setBirthDate(participant, detail.getBirthDate(), exception);
		}

		if (detail.isDeathDateModified()) {
			setDeathDate(participant, detail.getDeathDate(), exception);
		}

		if (detail.isRaceModified()) {
			setRace(participant, detail.getRace(), exception);
		}
		//TODO: Revisit
//		if (detail.isPMIModified()) {
//			setPmi(participant, detail.getPmiCollection(), exception);
//		}

		exception.checkErrorAndThrow();
		return participant;
	}

	private void setDeathDate(Participant participant, Date deathDate, ObjectCreationException exception) {
		if (deathDate == null) {
			return;
		}
		if ((!VITAL_STATUS_DEATH.equals(participant.getVitalStatus()) || (participant.getBirthDate() != null && deathDate
				.before(participant.getBirthDate())))) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
		}
		participant.setDeathDate(deathDate);
	}

	private void setBirthDate(Participant participant, Date birthDate, ObjectCreationException exception) {
		if (birthDate == null) {
			return;
		}
		if (birthDate.after(new Date())) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE);
			return;
		}
		participant.setBirthDate(birthDate);
	}

	private void setLastName(Participant participant, String lastName, ObjectCreationException exception) {
		participant.setLastName(lastName);
	}

	private void setMiddleName(Participant participant, String middleName, ObjectCreationException exception) {
		participant.setMiddleName(middleName);
	}

	private void setFirstName(Participant participant, String firstName, ObjectCreationException exception) {
		participant.setFirstName(firstName);
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

	private void setActivityStatus(Participant participant, String activityStatus, ObjectCreationException exception) {
		if (isBlank(activityStatus)) {
			participant.setActive();
			return;
		}
		if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.toString())) {
			participant.updateActivityStatus(activityStatus);
			return;
		}
		exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setVitalStatus(Participant participant, String vitalStatus, ObjectCreationException exception) {
		if (!isBlank(vitalStatus) && !isValidPv(vitalStatus, VITAL_STATUS)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, VITAL_STATUS);
			return;
		}
		participant.setVitalStatus(vitalStatus);
	}

	private void setGender(Participant participant, String gender, ObjectCreationException exception) {
		if (!isBlank(gender) && !isValidPv(gender, GENDER)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, GENDER);
			return;
		}
		participant.setGender(gender);
	}

	private void setRace(Participant participant, Set<String> raceList, ObjectCreationException exception) {

		if (raceList == null || raceList.isEmpty()) {
			return;
		}

		String[] races = raceList.toArray(new String[raceList.size()]);
		if (isValidPv(races, RACE)) {
			participant.setRaceColl(raceList);
			return;
		}
		exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, RACE);

	}

	private void setEthnicity(Participant participant, String ethnicity, ObjectCreationException exception) {
		if (!isBlank(ethnicity) && !isValidPv(ethnicity, ETHNICITY)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, ETHNICITY);
			return;
		}
		participant.setEthnicity(ethnicity);
	}

	private void setPmi(Participant participant, List<ParticipantMedicalIdentifierNumberDetail> pmiCollection,
			ObjectCreationException exception) {
		List<ParticipantMedicalIdentifierNumberDetail> mrns = pmiCollection;
		Map<String, ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();
		if (mrns != null && mrns.size() > 0) {
			for (ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail : mrns) {
				ParticipantMedicalIdentifier medicalIdentifier = getMedicalIdentifier(medicalRecordNumberDetail, exception);
				if(medicalIdentifier.getSite() != null){
					map.put(medicalIdentifier.getSite().getName(), medicalIdentifier);
				}
				else{
					return;
				}
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

	private void setSexGenotype(Participant participant, String sexGenotype, ObjectCreationException exception) {
		if (!isBlank(sexGenotype) && !isValidPv(sexGenotype, SEX_GENOTYPE)) {
			exception.addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, SEX_GENOTYPE);
			return;
		}
		participant.setVitalStatus(sexGenotype);
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
