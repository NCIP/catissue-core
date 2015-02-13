
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
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class ParticipantFactoryImpl implements ParticipantFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private final String RACE = "race";

	private final String ETHNICITY = "ethnicity";

	private final String VITAL_STATUS = "vital status";

	private final String GENDER = "gender";

	private final String SEX_GENOTYPE = "sexGenotype";

	private final String VITAL_STATUS_DEATH = "Death";
	
	private static final Pattern SSN_PATTERN = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");

	@Override
	public Participant createParticipant(ParticipantDetail details) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		Participant participant = new Participant();		
		setSsn(participant, details.getSsn(), ose);
		setName(participant, details);
		setVitalStatus(participant, details.getVitalStatus(), ose);
		setBirthDate(participant, details.getBirthDate(), ose);
		setDeathDate(participant, details.getDeathDate(), ose);
		setActivityStatus(participant, details.getActivityStatus(), ose);
		setSexGenotype(participant, details.getSexGenotype(), ose);
		setGender(participant, details.getGender(), ose);
		setRace(participant, details.getRace(), ose);
		setEthnicity(participant, details.getEthnicity(), ose);
		setPmi(participant, details.getPmis(), ose);
		
		participant.setEmpi(details.getEmpi());
		
		ose.checkAndThrow();
		return participant;
	}

	private void setDeathDate(Participant participant, Date deathDate, OpenSpecimenException exception) {
		if (deathDate == null) {
			return;
		}
		
		if (!VITAL_STATUS_DEATH.equals(participant.getVitalStatus()) || 
				(participant.getBirthDate() != null && deathDate.before(participant.getBirthDate()))) {
			exception.addError(ParticipantErrorCode.INVALID_DEATH_DATE);
		}
		
		participant.setDeathDate(deathDate);
	}

	private void setBirthDate(Participant participant, Date birthDate, OpenSpecimenException exception) {
		if (birthDate == null) {
			return;
		}
		
		if (birthDate.after(Calendar.getInstance().getTime())) {
			exception.addError(ParticipantErrorCode.INVALID_BIRTH_DATE);
			return;
		}
		
		participant.setBirthDate(birthDate);
	}

	private void setName(Participant participant, ParticipantDetail detail) {
		participant.setFirstName(detail.getFirstName());
		participant.setMiddleName(detail.getMiddleName());
		participant.setLastName(detail.getLastName());
	}

	private void setSsn(Participant participant, String ssn, OpenSpecimenException oce) {
		if (StringUtils.isBlank(ssn)) {
			return;
		}
		
		if (isSsnValid(ssn)) {
			participant.setSocialSecurityNumber(ssn);
		} else {
			oce.addError(ParticipantErrorCode.INVALID_SSN);
		}
	}

	private void setActivityStatus(Participant participant, String activityStatus, OpenSpecimenException oce) {
		if (StringUtils.isBlank(activityStatus)) {
			participant.setActive();
			return;
		}
		
		if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.toString())) {
			participant.updateActivityStatus(activityStatus);
			return;
		}
		
		oce.addError(ActivityStatusErrorCode.INVALID);
	}

	private void setVitalStatus(Participant participant, String vitalStatus, OpenSpecimenException oce) {
		if (!StringUtils.isBlank(vitalStatus) && !isValidPv(vitalStatus, VITAL_STATUS)) {
			oce.addError(ParticipantErrorCode.INVALID_VITAL_STATUS);
			return;
		}
		
		participant.setVitalStatus(vitalStatus);
	}

	private void setGender(Participant participant, String gender, OpenSpecimenException oce) {
		if (!StringUtils.isBlank(gender) && !isValidPv(gender, GENDER)) {
			oce.addError(ParticipantErrorCode.INVALID_GENDER);
			return;
		}
		
		participant.setGender(gender);
	}

	private void setRace(Participant participant, Set<String> raceList, OpenSpecimenException oce) {
		if (raceList == null || raceList.isEmpty()) {
			return;
		}

		String[] races = raceList.toArray(new String[raceList.size()]);
		if (!isValidPv(races, RACE)) {
			oce.addError(ParticipantErrorCode.INVALID_RACE);
			return;
		}
		
		participant.setRaceColl(raceList);
	}

	private void setEthnicity(Participant participant, String ethnicity, OpenSpecimenException oce) {
		if (!StringUtils.isBlank(ethnicity) && !isValidPv(ethnicity, ETHNICITY)) {
			oce.addError(ParticipantErrorCode.INVALID_ETHNICITY);
			return;
		}
		
		participant.setEthnicity(ethnicity);
	}

	private void setPmi(
			Participant participant, 
			List<ParticipantMedicalIdentifierNumberDetail> pmis,
			OpenSpecimenException oce) {
		
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

	private ParticipantMedicalIdentifier getPmi(ParticipantMedicalIdentifierNumberDetail pmiDetail, OpenSpecimenException oce) {
		Site site = daoFactory.getSiteDao().getSite(pmiDetail.getSiteName());		
		if (site == null) {
			oce.addError(SiteErrorCode.NOT_FOUND);
			return null;
		}
		
		if (StringUtils.isBlank(pmiDetail.getMrn())) {
			oce.addError(ParticipantErrorCode.MRN_REQUIRED);
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

	private void setSexGenotype(Participant participant, String sexGenotype, OpenSpecimenException oce) {
		if (!StringUtils.isBlank(sexGenotype) && !isValidPv(sexGenotype, SEX_GENOTYPE)) {
			oce.addError(ParticipantErrorCode.INVALID_GENOTYPE);
			return;
		}
		
		participant.setSexGenotype(sexGenotype);
	}
}
