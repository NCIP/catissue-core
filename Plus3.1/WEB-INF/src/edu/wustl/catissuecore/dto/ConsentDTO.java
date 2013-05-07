package edu.wustl.catissuecore.dto;

import java.util.Collection;
import java.util.List;


public class ConsentDTO
{
	List<String> specimenLabels;
    Collection<ConsentTierDTO> consentTierDTOCollection;
    
	public List<String> getSpecimenLabels() {
		return specimenLabels;
	}
	public void setSpecimenLabels(List<String> specimenLabels) {
		this.specimenLabels = specimenLabels;
	}
	public Collection<ConsentTierDTO> getConsentTierDTOCollection() {
		return consentTierDTOCollection;
	}
	public void setConsentTierDTOCollection(
			Collection<ConsentTierDTO> consentTierDTOCollection) {
		this.consentTierDTOCollection = consentTierDTOCollection;
	}
    
}
