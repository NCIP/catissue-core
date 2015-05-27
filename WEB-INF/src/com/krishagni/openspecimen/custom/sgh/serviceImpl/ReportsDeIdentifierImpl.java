package com.krishagni.openspecimen.custom.sgh.serviceImpl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ReportsDeIdentifier;

public class ReportsDeIdentifierImpl implements ReportsDeIdentifier {
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String deIdentify(String report, Long visitId) {
		String replaceString = "XXX";
		List<User> allUsers = daoFactory.getUserDao().getAllUsers();
		for (User user: allUsers) {
			report = report.replaceAll("(?i)" + user.getLastName(), replaceString);
			report = report.replaceAll("(?i)" + user.getFirstName(), replaceString);
			report = report.replaceAll("(?i)" + user.getLoginName(), replaceString);
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
