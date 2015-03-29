
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantUtil;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
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

	@Override
	public Participant createParticipant(ParticipantDetail detail) {		
		Participant participant = new Participant();
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		setParticipantAttrs(detail, participant, false, ose);
		
		ose.checkAndThrow();
		return participant;
	}
	
	@Override
	public Participant createParticipant(Participant existing, ParticipantDetail detail) {
		Participant participant = new Participant();		
		BeanUtils.copyProperties(existing, participant, new String[] {"cprs"});
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		setParticipantAttrs(detail, participant, true, ose);
		
		ose.checkAndThrow();
		return participant;
		
	}
	
	private void setParticipantAttrs(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		participant.setId(detail.getId());
		setSsn(detail, participant, partial, ose);
		setName(detail, participant, partial, ose);
		setVitalStatus(detail, participant, partial, ose);
		setBirthDate(detail, participant, partial, ose);
		setDeathDate(detail, participant, partial, ose);
		setActivityStatus(detail, participant, partial, ose);
		setSexGenotype(detail, participant, partial, ose);
		setGender(detail, participant, partial, ose);
		setRace(detail, participant, partial, ose);
		setEthnicity(detail, participant, partial, ose);
		setPmi(detail, participant, partial, ose);
		
		if (!partial || detail.getModifiedAttrs().contains("empi")) {
			participant.setEmpi(detail.getEmpi());
		}				
	}

	private void setSsn(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("ssn")) {
			return;
		}
		
		String ssn = detail.getSsn();		
		if (isValidSsn(ssn)) {
			if (partial) {
				boolean isUnique = ParticipantUtil.validateSsn(
						daoFactory, 
						participant.getSocialSecurityNumber(), 
						ssn, 
						oce);
				if (!isUnique) {
					return;
				}
			}
			
			participant.setSocialSecurityNumber(ssn);
		} else {
			oce.addError(ParticipantErrorCode.INVALID_SSN);
		}
	}
	
	private void setName(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		Set<String> modifiedAttrs = detail.getModifiedAttrs();
		
		if (!partial || modifiedAttrs.contains("firstName")) {
			participant.setFirstName(detail.getFirstName());
		}
		
		if (!partial || modifiedAttrs.contains("middleName")) {
			participant.setMiddleName(detail.getMiddleName());
		}
		
		if (!partial || modifiedAttrs.contains("lastName")) {
			participant.setLastName(detail.getLastName());
		}		
	}	

	private void setVitalStatus(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("vitalStatus")) {
			return;
		}
		
		String vitalStatus = detail.getVitalStatus();
		
		if (StringUtils.isNotBlank(vitalStatus) && !isValidPv(vitalStatus, VITAL_STATUS)) {
			oce.addError(ParticipantErrorCode.INVALID_VITAL_STATUS);
			return;
		}
		
		participant.setVitalStatus(vitalStatus);
	}

	private void setBirthDate(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("birthDate")) {
			return;
		}
				
		Date birthDate = detail.getBirthDate();
		
		if (birthDate == null) {
			return;
		}
		
		if (birthDate.after(Calendar.getInstance().getTime())) {
			oce.addError(ParticipantErrorCode.INVALID_BIRTH_DATE);
			return;
		}
		
		participant.setBirthDate(birthDate);
	}
	
	private void setDeathDate(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("deathDate")) {
			return;
		}
				
		Date deathDate = detail.getDeathDate();		
		if (deathDate == null) {
			return;
		}
		
		if (participant.getBirthDate() != null && deathDate.before(participant.getBirthDate())) {
			oce.addError(ParticipantErrorCode.INVALID_DEATH_DATE);
		}
		
		// TODO: how do we set vital status to dead now?
		
		participant.setDeathDate(deathDate);
	}

	private void setActivityStatus(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("activityStatus")) {
			return;
		}
				
		String status = detail.getActivityStatus();		
		if (StringUtils.isBlank(status)) {
			participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}
		
		if (isValidPv(status, Status.ACTIVITY_STATUS.toString())) {
			participant.setActivityStatus(status);
			return;
		}
		
		oce.addError(ActivityStatusErrorCode.INVALID);
	}

	private void setSexGenotype(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("genotype")) {
			return;
		}
		
		String genotype = detail.getSexGenotype();		
		if (StringUtils.isNotBlank(genotype) && !isValidPv(genotype, SEX_GENOTYPE)) {
			oce.addError(ParticipantErrorCode.INVALID_GENOTYPE);
			return;
		}
		
		participant.setSexGenotype(genotype);
	}
	
	private void setGender(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("gender")) {
			return;
		}
		
		String gender = detail.getGender();		
		if (StringUtils.isNotBlank(gender) && !isValidPv(gender, GENDER)) {
			oce.addError(ParticipantErrorCode.INVALID_GENDER);
			return;
		}
		
		participant.setGender(gender);
	}

	private void setRace(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("races")) {
			return;
		}
		
		Set<String> races = detail.getRaces();		
		if (CollectionUtils.isEmpty(races)) {
			return;
		}

		if (!isValidPv(races.toArray(new String[0]), RACE)) {
			oce.addError(ParticipantErrorCode.INVALID_RACE);
			return;
		}
		
		participant.setRaces(races);
	}

	private void setEthnicity(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.getModifiedAttrs().contains("ethnicity")) {
			return;
		}
		
		String ethnicity = detail.getEthnicity();		
		if (StringUtils.isNotBlank(ethnicity) && !isValidPv(ethnicity, ETHNICITY)) {
			oce.addError(ParticipantErrorCode.INVALID_ETHNICITY);
			return;
		}
		
		participant.setEthnicity(ethnicity);
	}

	private void setPmi(
			ParticipantDetail detail,
			Participant participant, 	
			boolean partial,
			OpenSpecimenException oce) {
		
		if (partial && !detail.getModifiedAttrs().contains("pmis")) {
			return;
		}

		if (partial) {
			boolean unique = ParticipantUtil.ensureUniquePmis(
					daoFactory, 
					detail.getPmis(), 
					participant, 
					oce);
			if (!unique) {
				return;
			}
		}
		
		if (detail.getPmis() == null) {
			return;
		}
		
		for (PmiDetail pmiDetail : detail.getPmis()) {
			ParticipantMedicalIdentifier pmi = getPmi(pmiDetail, oce);
			participant.updatePmi(pmi);
		}
	}

	private ParticipantMedicalIdentifier getPmi(PmiDetail pmiDetail, OpenSpecimenException oce) {
		Site site = daoFactory.getSiteDao().getSiteByName(pmiDetail.getSiteName());		
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

	private boolean isValidSsn(String ssn) {
		try {
			if (StringUtils.isBlank(ssn)) {
				return true;
			}
			
			return SSN_PATTERN.matcher(ssn).matches();
		} catch (Exception exp) {
			return false;
		}
	}
	
	private final String RACE = "race";

	private final String ETHNICITY = "ethnicity";

	private final String VITAL_STATUS = "vital status";

	private final String GENDER = "gender";

	private final String SEX_GENOTYPE = "sexGenotype";

	private static final Pattern SSN_PATTERN = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");
}
