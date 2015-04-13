
package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;

public class ParticipantLookupLogicImpl implements ParticipantLookupLogic {

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public MatchedParticipants getMatchingParticipants(ParticipantDetail participant) {
		ParticipantDao dao = daoFactory.getParticipantDao();
				
		if (StringUtils.isNotBlank(participant.getEmpi())) {
			Participant matched = dao.getByEmpi(participant.getEmpi());
			if (matched != null) {
				List<ParticipantDetail> result = ParticipantDetail.from(Collections.singletonList(matched), false);
				return new MatchedParticipants("empi", result);
			}
		}
		
		if (StringUtils.isNotBlank(participant.getSsn())) {
			Participant matched = dao.getBySsn(participant.getSsn());
			if (matched != null) {
				List<ParticipantDetail> result = ParticipantDetail.from(Collections.singletonList(matched), false);
				return new MatchedParticipants("ssn", result);
			}
		}
		
		if (participant.getPmis() != null) {
			List<Participant> matched = dao.getByPmis(participant.getPmis());
			if (matched != null && !matched.isEmpty()) {
				return new MatchedParticipants("pmi", ParticipantDetail.from(matched, false));
			}
		}
		
		if (StringUtils.isNotBlank(participant.getLastName()) && participant.getBirthDate() != null) {
			List<Participant> matched = dao.getByLastNameAndBirthDate(participant.getLastName(), participant.getBirthDate());
			if (matched != null && !matched.isEmpty()) {
				return new MatchedParticipants("lnameAndDob", ParticipantDetail.from(matched, false));
			}
		}
		
		return new MatchedParticipants("none", Collections.<ParticipantDetail>emptyList());		
	}
}
