package edu.wustl.catissuecore.util;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.catissuecore.dto.ParticipantAttributeDisplayInfoDTO;



public class ParticipantConfig {
	
	private Set<ParticipantAttributeDisplayInfoDTO> attributes;

	public ParticipantConfig()
	{
		attributes = new HashSet<ParticipantAttributeDisplayInfoDTO>();
	}
	public void addParticipantAttributeDisplayInfo(ParticipantAttributeDisplayInfoDTO pAttribute)
	{
		attributes.add(pAttribute);
	}
	/**
	 * @return the attributes
	 */
	public Set<ParticipantAttributeDisplayInfoDTO> getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Set<ParticipantAttributeDisplayInfoDTO> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
