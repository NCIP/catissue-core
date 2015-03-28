package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ParticipantUtil {
	public static boolean validateSsn(DaoFactory daoFactory, String oldSsn, String newSsn, OpenSpecimenException ose) {
		if (StringUtils.isBlank(oldSsn) && !StringUtils.isBlank(newSsn)) {
			return ensureUniqueSsn(daoFactory, newSsn, ose);
		} else if (!StringUtils.isBlank(oldSsn) && !StringUtils.isBlank(newSsn) && !oldSsn.equals(newSsn)) {
			return ensureUniqueSsn(daoFactory, newSsn, ose);
		}
		
		return true;
	}

	public static boolean ensureUniqueSsn(DaoFactory daoFactory, String ssn, OpenSpecimenException ose) {
		if (!daoFactory.getParticipantDao().isSsnUnique(ssn)) {
			ose.addError(ParticipantErrorCode.DUP_SSN);
			return false;
		}
		
		return true;
	}

	public static boolean ensureUniquePmis(DaoFactory daoFactory, List<PmiDetail> pmis, Participant participant, OpenSpecimenException ose) {
		List<Long> participantIds = daoFactory.getParticipantDao().getParticipantIdsByPmis(pmis);
		if (CollectionUtils.isEmpty(participantIds)) { 
			// no one own these pmis yet
			return true;
		}
		
		if (participant.getId() == null) { // create mode
			ose.addError(ParticipantErrorCode.DUP_MRN);
			return false;
		} else {
			for (Long participantId : participantIds) {
				if (!participant.getId().equals(participantId)) {
					ose.addError(ParticipantErrorCode.DUP_MRN);
					return false;
				}
			}			
		}
		
		return true;
	}
}
