package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class MatchedParticipants {	
	private String matchedAttr;

	private List<ParticipantDetail> participants;

	public MatchedParticipants(String attr, List<ParticipantDetail> participants) {
		this.matchedAttr = attr;
		this.participants = participants;
	}
	
	public String getMatchedAttr() {
		return matchedAttr;
	}

	public void setMatchedAttr(String matchedAttr) {
		this.matchedAttr = matchedAttr;
	}


	public List<ParticipantDetail> getParticipants() {
		return participants;
	}

	
	public void setParticipants(List<ParticipantDetail> participants) {
		this.participants = participants;
	}
}
