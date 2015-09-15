package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.de.domain.DeObject;

public class ParticipantExtension extends DeObject {
	private Participant participant;
	
	public ParticipantExtension(Participant participant) {
		this.participant = participant;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Override
	public Long getObjectId() {
		return participant.getId();
	}

	@Override
	public String getEntityType() {
		return "ParticipantExtension";
	}

	@Override
	public String getFormName() {
		return getFormNameByEntityType();
	}

	@Override
	public Long getCpId() {
		return -1L;
	}

	@Override
	public void setAttrValues(Map<String, Object> attrValues) {
		// TODO Auto-generated method stub
	}
	
	public static ParticipantExtension getFor(Participant participant) {
		ParticipantExtension extension = new ParticipantExtension(participant);
		if (StringUtils.isBlank(extension.getFormName())) {
			return null;
		}
		
		if (participant.getId() == null) {
			return extension;
		}
		
		List<Long> recIds = extension.getRecordIds();
		if (CollectionUtils.isNotEmpty(recIds)) {
			extension.setId(recIds.iterator().next());
		}
		
		return extension;
	}
	
}
