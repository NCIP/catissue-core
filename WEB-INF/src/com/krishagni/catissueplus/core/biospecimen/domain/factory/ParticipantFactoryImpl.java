
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.events.MedicalRecordNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;

public class ParticipantFactoryImpl implements ParticipantFactory {

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private final String EMPTY_STRING = "";

	private final String SSN = "social security number";

	private final String RACE = "race";

	private final String ETHNICITY = "ethnicity";

	private final String VITAL_STATUS = "vital status";

	private final String BIRTH_DATE = "birth date";

	private final String DEATH_DATE = "death date";

	private final String GENDER = "gender";

	private final String MEDICAL_RECORD_NUMBER = "medical record number";

	private final String SITE = "site";

	@Override
	public Participant createParticipant(ParticipantDetails details) {
		Participant participant = new Participant();

		setSsn(participant, details.getSsn());
		setName(participant, details);
		setDates(participant, details);
		setVitalStatus(participant, details);
		setGender(participant, details);
		setRace(participant, details);
		setEthnicity(participant, details);
		setPmi(participant, details);
		return participant;
	}

	private void setSsn(Participant participant, String ssn) {
		if (StringUtils.isBlank(ssn)) {
			return;
		}
		if (isSsnValid(ssn)) {
			participant.setSocialSecurityNumber(ssn);
		}
		else {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, SSN);
		}
	}

	private void setName(Participant participant, ParticipantDetails details) {
		participant.setFirstName(StringUtils.isBlank(details.getFirstName()) ? EMPTY_STRING : details.getFirstName());
		participant.setLastName(StringUtils.isBlank(details.getLastName()) ? EMPTY_STRING : details.getLastName());
		participant.setMiddleName(StringUtils.isBlank(details.getMiddleName()) ? EMPTY_STRING : details.getMiddleName());
	}

	private void setDates(Participant participant, ParticipantDetails details) {

		Date birthDate = details.getDob();
		Date deathDate = details.getDeathDate();

		if (birthDate != null) {
			if (birthDate.after(new Date())) {
				reportError(ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE);
			}
			participant.setBirthDate(birthDate);
			if (deathDate != null) {
				if ((!"Death".equals(participant.getVitalStatus()) || deathDate.before(birthDate))) {
					reportError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
				}
				participant.setDeathDate(deathDate);
			}
		}
		else if (deathDate != null) {
			reportError(ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE);
		}

	}

	private void setVitalStatus(Participant participant, ParticipantDetails details) {
		if (StringUtils.isNotBlank(details.getVitalStatus())) {
			ensureValidPermissibleValue(details.getVitalStatus(), VITAL_STATUS);
			participant.setVitalStatus(details.getVitalStatus());
		}
	}

	private void setGender(Participant participant, ParticipantDetails details) {
		if (StringUtils.isNotBlank(details.getGender())) {
			ensureValidPermissibleValue(details.getGender(), GENDER);
			participant.setGender(details.getGender());
		}
	}

	private void setRace(Participant participant, ParticipantDetails details) {
		List<String> raceList = details.getRace();
		if (raceList != null && raceList.size() > 0) {
			for (String race : raceList) {
				ensureValidPermissibleValue(race, RACE);
			}
			participant.setRaceCollection(raceList);
		}

	}

	private void setEthnicity(Participant participant, ParticipantDetails details) {
		if (StringUtils.isNotBlank(details.getEthnicity())) {
			ensureValidPermissibleValue(details.getEthnicity(), ETHNICITY);
			participant.setEthnicity(details.getEthnicity());
		}
	}

	private void setPmi(Participant participant, ParticipantDetails details) {
		List<MedicalRecordNumberDetail> mrns = details.getMrns();
		List<ParticipantMedicalIdentifier> medicalIdentifiers = new ArrayList<ParticipantMedicalIdentifier>();
		if (mrns != null && mrns.size() > 0) {
			for (MedicalRecordNumberDetail medicalRecordNumberDetail : mrns) {
				medicalIdentifiers.add(getMedicalIdentifier(medicalRecordNumberDetail));
			}
		}

	}

	private ParticipantMedicalIdentifier getMedicalIdentifier(MedicalRecordNumberDetail medicalRecordNumberDetail) {
		Site site = daoFactory.getSiteDao().getSite(medicalRecordNumberDetail.getSiteId());
		if (site == null) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, SITE);
		}
		if (StringUtils.isBlank(medicalRecordNumberDetail.getMrn())) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, MEDICAL_RECORD_NUMBER);
		}
		ParticipantMedicalIdentifier medicalIdentifier = new ParticipantMedicalIdentifier();
		medicalIdentifier.setSite(site);
		medicalIdentifier.setMedicalRecordNumber(medicalRecordNumberDetail.getMrn());
		return medicalIdentifier;
	}

	private void ensureValidPermissibleValue(String value, String type) {
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		List<String> pvList = pvManager.getPermissibleValueList(type);
		if (pvList.contains(value)) {
			return;
		}
		reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, type);
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

}
