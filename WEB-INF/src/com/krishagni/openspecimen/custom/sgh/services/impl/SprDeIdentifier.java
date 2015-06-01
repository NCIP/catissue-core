package com.krishagni.openspecimen.custom.sgh.services.impl;

import java.util.List;
import java.util.Map;

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
	public String deIdentify(String report, Map<String, Object> contextMap) {
		UserListCriteria criteria = new UserListCriteria();
		String replaceString = "XXX";
		int startAt = 0;
		int maxResult = 100;
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
		
		Long visitId = (Long) contextMap.get("visitId");
		Visit visit = daoFactory.getVisitsDao().getById(visitId);
		Participant participant = visit.getRegistration().getParticipant();
		
		StringBuilder regex = new StringBuilder();
		if(StringUtils.isNotBlank(participant.getLastName())) {
			regex.append(participant.getLastName());
		}
		
		if(StringUtils.isNotBlank(participant.getFirstName())) {
			addOr(regex);
			regex.append(participant.getFirstName());
		}
		
		if(StringUtils.isNotBlank(participant.getMiddleName())) {
			addOr(regex);
			regex.append(participant.getMiddleName());
		}
		
		if (regex.length() == 0) {
			return report;
		}
		
		regex.insert(0, "(?i)(");
		regex.append(")");
		report = report.replaceAll(regex.toString(), replaceString);
		
		return report;
	}

	private void addOr(StringBuilder cond) {
		if (cond.length() > 0) {
			cond.append("|");
		}
	}

}
