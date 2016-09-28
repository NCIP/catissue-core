
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.ETHNICITY;
import static com.krishagni.catissueplus.core.common.PvAttributes.GENDER;
import static com.krishagni.catissueplus.core.common.PvAttributes.GENOTYPE;
import static com.krishagni.catissueplus.core.common.PvAttributes.RACE;
import static com.krishagni.catissueplus.core.common.PvAttributes.VITAL_STATUS;
import static com.krishagni.catissueplus.core.common.service.PvValidator.areValid;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.DeObject;


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
		existing.setCpId(detail.getCpId());

		Participant participant = new Participant();
		BeanUtils.copyProperties(existing, participant, new String[] {"cprs"});
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		setParticipantAttrs(detail, participant, true, ose);
		
		ose.checkAndThrow();
		return participant;
		
	}
	
	private void setParticipantAttrs(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		if (participant.getId() == null && detail.getId() != null) {
			participant.setId(detail.getId());
		}

		setUid(detail, participant, partial, ose);
		setEmpi(detail, participant, partial, ose);
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
		setExtension(detail, participant, partial, ose);
	}

	private void setUid(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("uid")) {
			return;
		}
		
		String uid = detail.getUid();
		if (StringUtils.isBlank(uid)) {
			if (ConfigUtil.getInstance().getBoolSetting("biospecimen", "uid_mandatory", false)) {
				oce.addError(ParticipantErrorCode.UID_REQUIRED);
			} else {
				participant.setUid(null);
			}
			return;
		}
		
		if (!ParticipantUtil.isValidUid(uid, oce)) {
			return;
		}
		
		if (partial && !uid.equals(participant.getUid())) {
			ParticipantUtil.ensureUniqueUid(daoFactory, uid, oce);
		}
		
		participant.setUid(uid);
	}
	
	private void setEmpi(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		if (partial && !detail.isAttrModified("empi")) {
			return;
		}
		
		String empi = detail.getEmpi();
		if (StringUtils.isBlank(empi)) {
			participant.setEmpi(null);
			return;
		}

		if (!ParticipantUtil.isValidMpi(empi, ose)) {
			return;
		}
		
		if (partial && !empi.equals(participant.getEmpi())) {
			//ParticipantUtil.en
		}
		
		participant.setEmpi(empi);
	}
	
	private void setName(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		if (!partial || detail.isAttrModified("firstName")) {
			participant.setFirstName(detail.getFirstName());
		}
		
		if (!partial || detail.isAttrModified("middleName")) {
			participant.setMiddleName(detail.getMiddleName());
		}
		
		if (!partial || detail.isAttrModified("lastName")) {
			participant.setLastName(detail.getLastName());
		}		
	}	

	private void setVitalStatus(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("vitalStatus")) {
			return;
		}
		
		String vitalStatus = detail.getVitalStatus();		
		if (!isValid(VITAL_STATUS, vitalStatus)) {
			oce.addError(ParticipantErrorCode.INVALID_VITAL_STATUS);
			return;
		}
		
		participant.setVitalStatus(vitalStatus);
	}

	private void setBirthDate(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("birthDate")) {
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
		if (partial && !detail.isAttrModified("deathDate")) {
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
		if (partial && !detail.isAttrModified("activityStatus")) {
			return;
		}
				
		String status = detail.getActivityStatus();		
		if (StringUtils.isBlank(status)) {
			participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}
		
		if (!Status.isValidActivityStatus(status)) {
			oce.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		participant.setActivityStatus(status);		
	}

	private void setSexGenotype(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("sexGenotype")) {
			return;
		}
		
		String genotype = detail.getSexGenotype();		
		if (!isValid(GENOTYPE, genotype)) {
			oce.addError(ParticipantErrorCode.INVALID_GENOTYPE);
			return;
		}
		
		participant.setSexGenotype(genotype);
	}
	
	private void setGender(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("gender")) {
			return;
		}
		
		String gender = detail.getGender();		
		if (!isValid(GENDER, gender)) {
			oce.addError(ParticipantErrorCode.INVALID_GENDER);
			return;
		}
		
		participant.setGender(gender);
	}

	private void setRace(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("races")) {
			return;
		}
		
		Set<String> races = detail.getRaces();		
		if (CollectionUtils.isEmpty(races)) {
			return;
		}

		if (!areValid(RACE, races)) {
			oce.addError(ParticipantErrorCode.INVALID_RACE);
			return;
		}
		
		participant.setRaces(races);
	}

	private void setEthnicity(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException oce) {
		if (partial && !detail.isAttrModified("ethnicity")) {
			return;
		}
		
		String ethnicity = detail.getEthnicity();		
		if (!isValid(ETHNICITY, ethnicity)) {
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
		
		if (partial && !detail.isAttrModified("pmis")) {
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
		
		Set<ParticipantMedicalIdentifier> newPmis = new HashSet<ParticipantMedicalIdentifier>();		
		if (CollectionUtils.isEmpty(detail.getPmis())) {
			participant.setPmis(newPmis);
		} else {
			Set<String> siteNames = new HashSet<String>();
			boolean dupSite = false;
			
			for (PmiDetail pmiDetail : detail.getPmis()) {
				ParticipantMedicalIdentifier pmi = getPmi(pmiDetail, oce);
				if (pmi == null) {
					continue;
				}
				
				if (!dupSite && !siteNames.add(pmiDetail.getSiteName())) {
					dupSite = true;
					oce.addError(ParticipantErrorCode.DUP_MRN_SITE, pmiDetail.getSiteName());
				}
				
				pmi.setParticipant(participant);
				newPmis.add(pmi);
			}			
		}
				
		participant.setPmis(newPmis);
	}

	private ParticipantMedicalIdentifier getPmi(PmiDetail pmiDetail, OpenSpecimenException oce) {
		Site site = daoFactory.getSiteDao().getSiteByName(pmiDetail.getSiteName());		
		if (site == null) {
			oce.addError(SiteErrorCode.NOT_FOUND);
			return null;
		}
		
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setSite(site);
		pmi.setMedicalRecordNumber(pmiDetail.getMrn());
		return pmi;
	}
	
	private void setExtension(ParticipantDetail detail, Participant participant, boolean partial, OpenSpecimenException ose) {
		participant.setCpId(detail.getCpId());

		DeObject extension = DeObject.createExtension(detail.getExtensionDetail(), participant);
		participant.setExtension(extension);
	}
	
}
