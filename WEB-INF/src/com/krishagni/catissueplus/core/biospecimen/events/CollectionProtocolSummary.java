
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class CollectionProtocolSummary implements Comparable<CollectionProtocolSummary> {
	private Long id;

	private String shortTitle;

	private String title;
	
	private UserSummary principalInvestigator;
	
	private Date startDate;
	
	private Long participantCount;
	
	private Long specimenCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UserSummary getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(UserSummary principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getParticipantCount() {
		return participantCount;
	}

	public void setParticipantCount(Long participantCount) {
		this.participantCount = participantCount;
	}

	public Long getSpecimenCount() {
		return specimenCount;
	}

	public void setSpecimenCount(Long specimenCount) {
		this.specimenCount = specimenCount;
	}
	
	@Override
	public int compareTo(CollectionProtocolSummary cpSummary) {
		return this.shortTitle.toUpperCase().compareTo(cpSummary.getShortTitle().toUpperCase());
	}
	
	public static CollectionProtocolSummary from(CollectionProtocol cp) {
		CollectionProtocolSummary result = new CollectionProtocolSummary();
		copy(cp, result);
		return result;		
	}
	
	public static <T extends CollectionProtocolSummary> void copy(CollectionProtocol cp, T detail) {
		detail.setId(cp.getId());
		detail.setShortTitle(cp.getShortTitle());
		detail.setTitle(cp.getTitle());
		detail.setStartDate(cp.getStartDate());
		detail.setPrincipalInvestigator(UserSummary.from(cp.getPrincipalInvestigator()));
	}
}
