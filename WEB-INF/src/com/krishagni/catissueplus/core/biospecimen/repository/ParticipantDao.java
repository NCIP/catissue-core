
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ParticipantDao extends Dao<Participant> {

	public Participant getParticipant(Long id);
	
	public Participant getBySsn(String ssn);
	
	public List<Participant> getByLastNameAndBirthDate(String lname, Date dob);
	
	public List<Participant> getByPmis(List<ParticipantMedicalIdentifierNumberDetail> pmis);

	public boolean isSsnUnique(String socialSecurityNumber);

	public boolean isPmiUnique(String siteName, String mrn);
}
