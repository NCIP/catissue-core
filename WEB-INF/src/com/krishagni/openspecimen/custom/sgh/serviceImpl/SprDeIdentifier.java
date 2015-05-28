package com.krishagni.openspecimen.custom.sgh.serviceImpl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SprDeIdentifier implements DocumentDeIdentifier {
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String deIdentify(String report, Long visitId) {
		UserListCriteria criteria = new UserListCriteria();
		String replaceString = "XXX";
		int startAt = 0;
		int maxResult = 1000;
		boolean moreUsers = true;
		
		while (moreUsers) {
			criteria.startAt(startAt)
				.maxResults(maxResult);
		
			List<UserSummary> users = daoFactory.getUserDao().getUsers(criteria);
			for (UserSummary user : users) {
				String regex = "(?i)(" +
						user.getLastName()  + "|" +
						user.getFirstName() + "|" + 
						user.getLoginName() +
						")";
				report = report.replaceAll(regex, replaceString);
			}
			
			startAt = startAt + maxResult;
			if (users.size() < maxResult) {
				moreUsers = false;
			}
		}		
		
		Visit visit = daoFactory.getVisitsDao().getById(visitId);
		Participant participant = visit.getRegistration().getParticipant();
			
		if(StringUtils.isNotBlank(participant.getLastName())) {
			report = report.replaceAll("(?i)" + participant.getLastName(), replaceString);
		}
		if(StringUtils.isNotBlank(participant.getFirstName())) {
			report = report.replaceAll("(?i)" + participant.getFirstName(), replaceString);
		}
		if(StringUtils.isNotBlank(participant.getMiddleName())) {
			report = report.replaceAll("(?i)" + participant.getMiddleName(), replaceString);
		}
		
		return report;
	}
}
