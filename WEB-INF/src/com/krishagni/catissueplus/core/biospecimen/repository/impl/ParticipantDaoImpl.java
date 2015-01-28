
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ParticipantDaoImpl extends AbstractDao<Participant> implements ParticipantDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public Participant getBySsn(String ssn) {		
		List<Participant> participants = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_SSN)
				.setString("ssn", ssn)
				.list();
		return participants == null || participants.isEmpty() ? null : participants.iterator().next();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Participant getByEmpi(String empi) {
		List<Participant> participants = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_EMPI)
				.setString("empi", empi)
				.list();
		return participants == null || participants.isEmpty() ? null : participants.iterator().next();
	}

	@Override
	@SuppressWarnings("unchecked")	
	public List<Participant> getByLastNameAndBirthDate(String lname, Date dob) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_LNAME_AND_DOB)
				.setString("lname", lname)
				.setDate("dob", dob)
				.list();
	}

	@Override
	@SuppressWarnings("unchecked")	
	public List<Participant> getByPmis(List<ParticipantMedicalIdentifierNumberDetail> pmis) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Participant.class)
				.createAlias("pmiCollection", "pmi")
				.createAlias("pmi.site", "site");
		
		Disjunction junction = Restrictions.disjunction();
		boolean added = false;
		for (ParticipantMedicalIdentifierNumberDetail pmi : pmis) {
			if (StringUtils.isBlank(pmi.getSiteName()) || StringUtils.isBlank(pmi.getMrn())) {
				continue;
			}
			
			junction.add(
					Restrictions.and(
							Restrictions.eq("pmi.medicalRecordNumber", pmi.getMrn()),
							Restrictions.eq("site.name", pmi.getSiteName())));
			added = true;
		}
		
		if (!added) {
			return Collections.emptyList();
		}
		
		return query.add(junction).list();						
	}
	
	@Override
	public boolean isSsnUnique(String ssn) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_ID_BY_SSN);
		query.setString("ssn", ssn);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isPmiUnique(String siteName, String mrn) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PMI_ID_BY_SITE_MRN);
		query.setString("siteName", siteName);
		query.setString("mrn", mrn);
		return query.list().isEmpty() ? true : false;
	}
	
	@Override
	public Class getType() {
		return Participant.class;
	}

	private static final String FQN = Participant.class.getName();

	private static final String GET_PARTICIPANT_ID_BY_SSN = FQN + ".getParticipantIdBySSN";

	private static final String GET_PMI_ID_BY_SITE_MRN = FQN + ".getPmiIdBySiteMrn";
	
	private static final String GET_BY_SSN = FQN + ".getBySsn";
	
	private static final String GET_BY_EMPI = FQN + ".getByEmpi";
	
	private static final String GET_BY_LNAME_AND_DOB = FQN + ".getByLnameAndDob";
}
