package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenListDaoImpl extends AbstractDao<SpecimenList> implements SpecimenListDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenList> getSpecimenLists() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LISTS)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenList> getUserSpecimenLists(Long userId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LISTS_BY_USER)
				.setLong("userId", userId)
				.list();
	}

	@Override
	public Map<Long, List<Specimen>> getListCpSpecimens(Long listId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LIST_CP_SPECIMENS)
				.setLong("listId", listId)
				.list();

		Map<Long, List<Specimen>> cpSpecimens = new HashMap<Long, List<Specimen>>();
		for (Object[] row : rows) {
			Long cpId = (Long)row[0];
			Specimen specimen = (Specimen)row[1];

			List<Specimen> specimens = cpSpecimens.get(cpId);
			if (specimens == null) {
				specimens = new ArrayList<Specimen>();
				cpSpecimens.put(cpId, specimens);
			}

			specimens.add(specimen);
		}

		return cpSpecimens;
	}

	@Override
	public List<Long> getListSpecimensCpIds(Long listId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LIST_SPMNS_CP_IDS)
				.setLong("listId", listId)
				.list();
	}

	@Override
	public SpecimenList getSpecimenList(Long listId) {
		return (SpecimenList)sessionFactory.getCurrentSession()
				.get(SpecimenList.class, listId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SpecimenList getSpecimenListByName(String name) {
		List<SpecimenList> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LIST_BY_NAME)
				.setString("name", name)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public SpecimenList getDefaultSpecimenList(Long userId) {
		return getSpecimenListByName(SpecimenList.getDefaultListName(userId));
	}

	@Override
	public int getListSpecimensCount(Long listId) {
		return ((Number) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LIST_SPECIMENS_COUNT)
				.setLong("listId", listId)
				.uniqueResult())
				.intValue();
	}

	@Override
	public void deleteSpecimenList(SpecimenList list) {
		sessionFactory.getCurrentSession().delete(list);
	}

	private static final String FQN = SpecimenList.class.getName();

	private static final String GET_SPECIMEN_LISTS = FQN + ".getSpecimenLists";

	private static final String GET_SPECIMEN_LISTS_BY_USER = FQN + ".getSpecimenListsByUser";

	private static final String GET_LIST_CP_SPECIMENS = FQN + ".getListCpSpecimens";

	private static final String GET_LIST_SPMNS_CP_IDS = FQN + ".getListSpecimensCpIds";

	private static final String GET_SPECIMEN_LIST_BY_NAME = FQN + ".getSpecimenListByName";

	private static final String GET_LIST_SPECIMENS_COUNT = FQN + ".getListSpecimensCount";
}
