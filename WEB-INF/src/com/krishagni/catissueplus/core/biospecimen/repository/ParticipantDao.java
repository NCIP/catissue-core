
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ParticipantDao extends Dao<Participant> {
	
	public Participant getBySsn(String ssn);
	
	public Participant getByEmpi(String empi);
	
	public List<Participant> getByLastNameAndBirthDate(String lname, Date dob);
	
	public List<Participant> getByPmis(List<PmiDetail> pmis);
	
	public List<Long> getParticipantIdsByPmis(List<PmiDetail> pmis);

	public boolean isSsnUnique(String socialSecurityNumber);

	public boolean isPmiUnique(String siteName, String mrn);
}
