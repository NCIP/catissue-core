package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ParticipantUtil {
	public static boolean ensureUniqueUid(DaoFactory daoFactory, String uid, OpenSpecimenException ose) {
		if (StringUtils.isBlank(uid)) {
			return true;
		}
		
		if (!daoFactory.getParticipantDao().isUidUnique(uid)) {
			ose.addError(ParticipantErrorCode.DUP_UID, uid);
			return false;
		}
		
		return true;
	}
	
	public static boolean ensureUniqueEmpi(DaoFactory daoFactory, String empi, OpenSpecimenException ose) {
		if (StringUtils.isBlank(empi)) {
			return true;
		}
		
		if (daoFactory.getParticipantDao().getByEmpi(empi) != null) {
			ose.addError(ParticipantErrorCode.DUP_EMPI, empi);
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
