
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.ensureValidPermissibleValue;
import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
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
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

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
	
	private List<ErroneousField> erroneousFields = new ArrayList<ErroneousField>();

	@Override
	public Participant createParticipant(ParticipantDetail details,ObjectCreationException exceptionHandler) {
		Participant participant = new Participant();

		setSsn(participant, details.getSsn());
		setName(participant, details);
		setDates(participant, details);
		setActivityStatus(participant, details);
		setVitalStatus(participant, details);
		setGender(participant, details);
		setRace(participant, details);
		setEthnicity(participant, details);
		setPmi(participant, details);
		exceptionHandler.addError(erroneousFields);
		return participant;
	}

	private void setSsn(Participant participant, String ssn) {
		if (isBlank(ssn)) {
			return;
		}
		if (isSsnValid(ssn)) {
			participant.setSocialSecurityNumber(ssn);
		}
		else {
			addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SSN);
		}
	}

	private void setName(Participant participant, ParticipantDetail details) {
		participant.setFirstName(details.getFirstName());
		participant.setLastName(details.getLastName());
		participant.setMiddleName(details.getMiddleName());
	}

	private void setDates(Participant participant, ParticipantDetail details) {

		Date birthDate = details.getBirthDate();
		Date deathDate = details.getDeathDate();

		if (birthDate != null) {
			if (birthDate.after(new Date())) {
				addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE);
			}
			participant.setBirthDate(birthDate);
			if (deathDate != null) {
				if ((!"Death".equals(details.getVitalStatus()) || deathDate.before(birthDate))) {
					addError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
				}
				participant.setDeathDate(deathDate);
			}
		}

	}

	private void setActivityStatus(Participant participant, ParticipantDetail details) {
		participant.updateActivityStatus(details.getActivityStatus());
	}

	private void setVitalStatus(Participant participant, ParticipantDetail details) {
		if (!isBlank(details.getVitalStatus())) {
			ensureValidPermissibleValue(details.getVitalStatus(), VITAL_STATUS);
			participant.setVitalStatus(details.getVitalStatus());
		}
	}

	private void setGender(Participant participant, ParticipantDetail details) {
		if (!isBlank(details.getGender())) {
			ensureValidPermissibleValue(details.getGender(), GENDER);
			participant.setGender(details.getGender());
		}
	}

	private void setRace(Participant participant, ParticipantDetail details) {
		Set<String> raceList = details.getRace();
		if (raceList != null) {

			String[] races = raceList.toArray(new String[0]);
			ensureValidPermissibleValue(races, RACE);
			participant.setRaceColl(raceList);
		}

	}

	private void setEthnicity(Participant participant, ParticipantDetail details) {
		if (!isBlank(details.getEthnicity())) {
			ensureValidPermissibleValue(details.getEthnicity(), ETHNICITY);
			participant.setEthnicity(details.getEthnicity());
		}
	}

	private void setPmi(Participant participant, ParticipantDetail details) {
		List<ParticipantMedicalIdentifierNumberDetail> mrns = details.getPmiCollection();
		Map<String, ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();
		if (mrns != null && mrns.size() > 0) {
			for (ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail : mrns) {
				ParticipantMedicalIdentifier medicalIdentifier = getMedicalIdentifier(medicalRecordNumberDetail);
				map.put(medicalIdentifier.getSite().getName(), medicalIdentifier);
			}
			participant.setPmiCollection(map);
		}

	}

	private ParticipantMedicalIdentifier getMedicalIdentifier(ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail) {
		Site site = daoFactory.getSiteDao().getSite(medicalRecordNumberDetail.getSiteName());
		if (site == null) {
			addError(ParticipantErrorCode.INVALID_ATTR_VALUE, SITE);
		}
		if (isBlank(medicalRecordNumberDetail.getMrn())) {
			addError(ParticipantErrorCode.INVALID_ATTR_VALUE, MEDICAL_RECORD_NUMBER);
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
	
	private void addError(CatissueErrorCode event,String field)
	{
		erroneousFields.add(new ErroneousField(event,field));
	}
}
