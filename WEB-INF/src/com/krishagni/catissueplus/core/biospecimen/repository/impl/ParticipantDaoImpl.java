
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ParticipantDaoImpl extends AbstractDao<Participant> implements ParticipantDao {

	@Override
	public Participant getParticipant(Long id) {
		//		String hql = "select part from "+Participant.class.getName()+" part join part.pmiCollection pmi where part.firstName ='Fred' and " +
		//				"pmi.medicalRecordNumber = '87782'";
		//		sessionFactory.getCurrentSession().createQuery("select part from "+Participant.class.getName()+" part join part.pmiCollection pmi where part.gender ='Unspecified' and " +
		//				"pmi.medicalRecordNumber = '121'").list()
		return (Participant) sessionFactory.getCurrentSession().get(Participant.class, id);
	}

	@Override
	public boolean isSsnUnique(String socialSecurityNumber) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_ID_BY_SSN);
		query.setString("ssn", socialSecurityNumber);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isPmiUnique(String siteName, String mrn) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PMI_ID_BY_SITE_MRN);
		query.setString("siteName", siteName);
		query.setString("mrn", mrn);
		return query.list().isEmpty() ? true : false;
	}

	private static final String FQN = Participant.class.getName();

	private static final String GET_PARTICIPANT_ID_BY_SSN = FQN + ".getParticipantIdBySSN";

	private static final String GET_PMI_ID_BY_SITE_MRN = FQN + ".getPmiIdBySiteMrn";

	@Override
	public List<Participant> getMatchingParticipants(DetachedCriteria criteria) {

		return criteria.getExecutableCriteria(sessionFactory.getCurrentSession()).list();
	}

}
