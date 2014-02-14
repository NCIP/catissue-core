
package com.krishagni.catissueplus.core.domain.factory.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;
import com.krishagni.catissueplus.core.repository.DaoFactory;

import edu.wustl.catissuecore.domain.Participant;

public class ParticipantFactoryImpl implements ParticipantFactory {

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private final String EMPTY_STRING = "";

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
		boolean result = true;
		try {
			Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");
			Matcher mat = pattern.matcher(ssn);
			result = mat.matches();
		}
		catch (Exception exp) {
			result = false;
		}
		participant.setSocialSecurityNumber(ssn);
	}

	private void setName(Participant participant, ParticipantDetails details) {
		participant.setFirstName(StringUtils.isBlank(details.getFirstName()) ? EMPTY_STRING : details.getFirstName());
		participant.setLastName(StringUtils.isBlank(details.getLastName()) ? EMPTY_STRING : details.getLastName());
		participant.setMiddleName(StringUtils.isBlank(details.getMiddleName()) ? EMPTY_STRING : details.getMiddleName());
	}

	private void setDates(Participant participant, ParticipantDetails details) {
		Date birthDate = details.getDob();
		Date deathDate = details.getDeathDate();

		if (birthDate != null && birthDate.before(new Date())) {
			participant.setBirthDate(birthDate);
		}

		if (deathDate != null && "Death".equals(participant.getVitalStatus()) && birthDate.before(deathDate)) {
			participant.setDeathDate(deathDate);
		}

	}

	private void setVitalStatus(Participant participant, ParticipantDetails details) {
		if (StringUtils.isNotEmpty(details.getVitalStatus()) && validatePv(details.getVitalStatus(), "Race")) {
			participant.setVitalStatus(details.getVitalStatus());
		}
	}

	private void setGender(Participant participant, ParticipantDetails details) {
		if (StringUtils.isNotBlank(details.getGender()) && validatePv(details.getGender(), "Gender")) {
			participant.setGender(details.getGender());
		}
	}

	private void setRace(Participant participant, ParticipantDetails details) {
		participant.setRaceCollection(details.getRace());
	}

	private void setEthnicity(Participant participant, ParticipantDetails details) {
		participant.setEthnicity(details.getEthnicity());
	}

	private void setPmi(Participant participant, ParticipantDetails details) {

	}

	private boolean validatePv(String vitalStatus, String type) {
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		List pvList = pvManager.getPermissibleValueList(type);
		for (Object object : pvList) {

		}

		return false;
	}

}
