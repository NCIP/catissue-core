
package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;

public class ParticipantLookupLogicImpl implements ParticipantLookupLogic {

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Participant> getMatchingParticipants(ParticipantDetail detail) {
		List<Participant> matchParticipantsList = new ArrayList<Participant>();
		if ((CommonValidator.isBlank(detail.getFirstName()) && CommonValidator.isBlank(detail.getLastName()))
				&& CommonValidator.isBlank(detail.getSsn()) && detail.getPmiCollection().isEmpty()) {
			return matchParticipantsList;
		}

		return daoFactory.getParticipantDao().getMatchingParticipants(getDetachedCriteria(detail));
	}

	private DetachedCriteria getDetachedCriteria(ParticipantDetail detail) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Participant.class);

		Disjunction disjunction = Restrictions.disjunction();

		if (!CommonValidator.isBlank(detail.getFirstName()) || !CommonValidator.isBlank(detail.getLastName())) {
			Criterion firstName = Expression.eq("firstName", detail.getFirstName());
			Criterion lastName = Expression.eq("lastName", detail.getLastName());

			disjunction.add(Restrictions.and(firstName, lastName));
		}

		if (!CommonValidator.isBlank(detail.getSsn())) {
			disjunction.add(Restrictions.eq("socialSecurityNumber", detail.getSsn()));
		}

		List<ParticipantMedicalIdentifierNumberDetail> list = detail.getPmiCollection();
		if (list != null && !list.isEmpty()) {
			criteria.createAlias("pmiCollection", "pmi");
			criteria.setFetchMode("pmi", FetchMode.JOIN);
			criteria.createAlias("pmi.site", "site");
			criteria.setFetchMode("site", FetchMode.JOIN);
			for (ParticipantMedicalIdentifierNumberDetail pmi : list) {

				if (!CommonValidator.isBlank(pmi.getMrn()) && !CommonValidator.isBlank(pmi.getSiteName())) {
					Criterion pmiCrit = Expression.eq("pmi.medicalRecordNumber", pmi.getMrn());
					Criterion siteCrit = Expression.eq("site.name", pmi.getSiteName());
					disjunction.add(Restrictions.and(pmiCrit, siteCrit));
				}

			}
		}
		criteria.add(disjunction);
		return criteria;
	}

}
