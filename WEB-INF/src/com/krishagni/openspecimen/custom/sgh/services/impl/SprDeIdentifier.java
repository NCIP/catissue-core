package com.krishagni.openspecimen.custom.sgh.services.impl;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SprDeIdentifier implements DocumentDeIdentifier {
	private DaoFactory daoFactory;
	
	private SessionFactory sessionFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String deIdentify(String report, Map<String, Object> contextMap) {
		report = deIdentifyUsersName(report);		
		report = deIdentifyParticipantName(report, contextMap);
		report = deIdentifyOtherText(report);
		return report;
	}
	
	private String deIdentifyUsersName(String report) {
		UserListCriteria criteria = new UserListCriteria();
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
				report = report.replaceAll(regex, REPLACEMENT_STRING);
			}
			
			startAt = startAt + maxResult;
			if (users.size() < maxResult) {
				moreUsers = false;
			}
		}
		return report;
	}

	private String deIdentifyParticipantName(String report, Map<String, Object> contextMap) {
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
		
		if (regex.length() > 0) {
			regex.insert(0, "(?i)(");
			regex.append(")");
			report = report.replaceAll(regex.toString(), REPLACEMENT_STRING);
		}
		return report;
	}

	private String deIdentifyOtherText(String report) {
		List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(GET_DEIDENTIFICATION_TEXT).list();

		StringBuilder regexPhi = new StringBuilder();
		StringBuilder regexDeleteNextLine = new StringBuilder();
		StringBuilder regexDeleteLine = new StringBuilder();
		StringBuilder regexDeIdetifyWord = new StringBuilder();
		
		for (Object[] obj : list) {
			String searchText = (String) obj[0];
			boolean deleteLine = (Boolean) obj[1];
			boolean deleteNextLine = (Boolean) obj[2];
			if (deleteLine && deleteNextLine) {
				if (searchText.equals("Sex: Age: DOB:")) {
					addOr(regexPhi);
					regexPhi.append(searchText);
				} else {
					addOr(regexDeleteNextLine); 
					regexDeleteNextLine.append(searchText);
				}
			} else if (deleteLine) {
				addOr(regexDeleteLine);
				regexDeleteLine.append(searchText);
			} else {
				addOr(regexDeIdetifyWord);
				regexDeIdetifyWord.append(searchText);
			}
		}
	
		report = replaceString(report, regexPhi, REPLACE_3_LINE_REGX, true);
		report = replaceString(report, regexDeleteNextLine, REPLACE_2_LINE_REGX, true);
		report = replaceString(report, regexDeleteLine, REPLACE_1_LINE_REGX, true);
		report = replaceString(report, regexDeIdetifyWord, REPLACE_WORD, false);
		return report;
	}
	
	private String replaceString(String report, StringBuilder regxText, String regxExp, boolean delete) {
		if (regxText.length() <= 0) {
			return report;
		}
		regxExp = String.format(regxExp, regxText);
		String replacement = delete ? StringUtils.EMPTY : REPLACEMENT_STRING;
		return report.replaceAll(regxExp, replacement);
	}

	private void addOr(StringBuilder cond) {
		if (cond.length() > 0) {
			cond.append("|");
		}
	}
	
	private static final String GET_DEIDENTIFICATION_TEXT = "select string_content, delete_line, delete_next_line from catissue_deidentified_text";
	
	private static final String REPLACEMENT_STRING = "XXX";
	
	private static final String REPLACE_3_LINE_REGX = "(?i).*?(%s).*?\\n.*?\\n.*?\\n";
	
	private static final String REPLACE_2_LINE_REGX = "(?i).*?(%s).*?\\n.*?\\n";
	
	private static final String REPLACE_1_LINE_REGX = "(?i).*?(%s).*?\\n";
	
	private static final String REPLACE_WORD = "(?i).*?(%s)\\b";
	
}
