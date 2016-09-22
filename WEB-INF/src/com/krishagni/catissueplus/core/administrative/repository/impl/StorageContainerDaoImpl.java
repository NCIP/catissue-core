
package com.krishagni.catissueplus.core.administrative.repository.impl;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.events.ContainerSelectorCriteria;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.administrative.repository.ContainerRestrictionsCriteria;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {

	@Override
	public Class<?> getType() {
		return StorageContainer.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit) {
		return new ListQueryBuilder(listCrit).query()
				.setFirstResult(listCrit.startAt())
				.setMaxResults(listCrit.maxResults())
				.list();
	}
	
	@Override
	public Long getStorageContainersCount(StorageContainerListCriteria listCrit) {
		return ((Number) new ListQueryBuilder(listCrit, true).query().uniqueResult()).longValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public StorageContainer getByName(String name) {
		List<StorageContainer> result = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.list();

		return result.isEmpty() ? null : result.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	public StorageContainer getByBarcode(String barcode) {		
		List<StorageContainer> result = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("barcode", barcode))
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.list();

		return result.isEmpty() ? null : result.iterator().next();		
	}

	@Override
	public void delete(StorageContainerPosition position) {
		sessionFactory.getCurrentSession().delete(position);		
	}

	@Override
	public Map<String, Object> getContainerIds(String key, Object value) {
		return getObjectIds("containerId", key, value);
	}

	@Override
	public List<String> getNonCompliantContainers(ContainerRestrictionsCriteria crit) {
		Criteria query = getCurrentSession().createCriteria(StorageContainer.class)
			.createAlias("descendentContainers", "d")
			.add(Restrictions.eq("id", crit.containerId()))
			.setProjection(Projections.distinct(Projections.property("d.name")));

		Disjunction restriction = Restrictions.disjunction();
		if (isNotEmpty(crit.specimenClasses())) {
			query.createAlias("d.compAllowedSpecimenClasses", "spmnClass", JoinType.LEFT_OUTER_JOIN);
			restriction.add(
				Restrictions.conjunction()
					.add(Restrictions.isNotNull("spmnClass.elements"))
					.add(Restrictions.not(Restrictions.in("spmnClass.elements", crit.specimenClasses())))
			);
		}

		if (isNotEmpty(crit.specimenTypes())) {
			query.createAlias("d.compAllowedSpecimenTypes", "spmnType", JoinType.LEFT_OUTER_JOIN);
			restriction.add(
				Restrictions.conjunction()
					.add(Restrictions.isNotNull("spmnType.elements"))
					.add(Restrictions.not(Restrictions.in("spmnType.elements", crit.specimenTypes())))
			);
		}

		if (isNotEmpty(crit.collectionProtocols())) {
			query.createAlias("d.compAllowedCps", "cp", JoinType.LEFT_OUTER_JOIN);
			restriction.add(
				Restrictions.conjunction()
					.add(Restrictions.isNotNull("cp.id"))
					.add(Restrictions.not(Restrictions.in("cp.id", crit.collectionProtocolIds())))
			);
		} else {
			DetachedCriteria siteCrit = DetachedCriteria.forClass(CollectionProtocolSite.class)
				.createAlias("collectionProtocol", "cp1")
				.createAlias("site", "site1")
				.add(Restrictions.eqProperty("cp1.id", "cp.id"))
				.setProjection(Property.forName("site1.id"));

			query.createAlias("d.compAllowedCps", "cp", JoinType.LEFT_OUTER_JOIN);
			restriction.add(
				Restrictions.conjunction()
					.add(Restrictions.isNotNull("cp.id"))
					.add(Subqueries.notIn(crit.siteId(), siteCrit))
			);
		}

		return query.add(restriction).list();
	}

	@SuppressWarnings(value = "unchecked")
	@Override
	public List<String> getNonCompliantSpecimens(ContainerRestrictionsCriteria crit) {
		Criteria query = getCurrentSession().createCriteria(StorageContainer.class)
			.createAlias("descendentContainers", "d")
			.createAlias("d.occupiedPositions", "pos")
			.createAlias("pos.occupyingSpecimen", "spmn")
			.createAlias("spmn.visit", "visit")
			.createAlias("visit.registration", "cpr")
			.createAlias("cpr.collectionProtocol", "cp")
			.add(Restrictions.eq("id", crit.containerId()))
			.setProjection(Projections.distinct(Projections.property("spmn.label")));

		Disjunction restriction = Restrictions.disjunction();
		if (isNotEmpty(crit.specimenClasses())) {
			restriction.add(Restrictions.not(Restrictions.in("spmn.specimenClass", crit.specimenClasses())));
		}

		if (isNotEmpty(crit.specimenTypes())) {
			restriction.add(Restrictions.not(Restrictions.in("spmn.specimenType", crit.specimenTypes())));
		}

		if (isNotEmpty(crit.collectionProtocols())) {
			restriction.add(Restrictions.not(Restrictions.in("cp.id", crit.collectionProtocolIds())));
		} else {
			DetachedCriteria siteCrit = DetachedCriteria.forClass(CollectionProtocolSite.class)
				.createAlias("collectionProtocol", "cp1")
				.createAlias("site", "site1")
				.add(Restrictions.eqProperty("cp1.id", "cp.id"))
				.setProjection(Property.forName("site1.id"));

			query.createAlias("cp.sites", "cpSite").createAlias("cpSite.site", "site");
			restriction.add(Subqueries.propertyNotIn("site.id", siteCrit));
		}

		return query.add(restriction).list();
	}

	@Override
	public int getSpecimensCount(Long containerId) {
		return ((Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_SPECIMENS_COUNT)
			.setLong("containerId", containerId)
			.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public StorageContainerSummary getAncestorsHierarchy(Long containerId) {
		Set<Long> ancestorIds = new HashSet<>();
		Long rootId = null;

		List<Object[]> rows = getCurrentSession().getNamedQuery(GET_ANCESTORS)
			.setLong("containerId", containerId)
			.list();
		for (Object[] row : rows) {
			ancestorIds.add((Long)row[0]);
			if (row[1] == null) {
				rootId = (Long)row[0];
			}
		}

		rows = getCurrentSession().getNamedQuery(GET_ROOT_AND_CHILD_CONTAINERS)
			.setLong("rootId", rootId)
			.setParameterList("parentIds", ancestorIds)
			.list();

		Map<Long, StorageContainerSummary> containersMap = new HashMap<>();
		for (Object[] row : rows) {
			StorageContainerSummary container = createContainer(row, 10000);
			containersMap.put(container.getId(), container);
		}

		linkParentChildContainers(containersMap);
		return sortChildContainers(containersMap.get(rootId));
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<StorageContainerSummary> getChildContainers(Long containerId, int noOfColumns) {
		List<Object[]> rows = getCurrentSession().getNamedQuery(GET_CHILD_CONTAINERS)
			.setLong("parentId", containerId)
			.list();

		return rows.stream().map(row -> createContainer(row, noOfColumns))
			.sorted(this::comparePositions).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Long> getLeastEmptyContainerId(ContainerSelectorCriteria crit) {
		getCurrentSession().flush();
		return getCurrentSession().getNamedQuery(GET_LEAST_EMPTY_CONTAINER_ID)
			.setLong("cpId", crit.cpId())
			.setString("specimenClass", crit.specimenClass())
			.setString("specimenType", crit.type())
			.setInteger("minFreeLocs", crit.minFreePositions())
			.setDate("reservedLaterThan", crit.reservedLaterThan())
			.setMaxResults(crit.numContainers())
			.list();
	}

	@Override
	public int deleteReservedPositions(List<String> reservationIds) {
		return getCurrentSession().getNamedQuery(DEL_POS_BY_RSV_ID)
			.setParameterList("reservationIds", reservationIds)
			.executeUpdate();
	}

	@Override
	public int deleteReservedPositionsOlderThan(Date expireTime) {
		return getCurrentSession().getNamedQuery(DEL_EXPIRED_RSV_POS)
			.setDate("expireTime", expireTime)
			.executeUpdate();
	}

	private StorageContainerSummary createContainer(Object[] row, int noOfColumns) {
		int idx = 0;

		StorageContainerSummary container = new StorageContainerSummary();
		container.setId((Long)row[idx++]);
		container.setName((String)row[idx++]);
		container.setNoOfRows((Integer)row[idx++]);
		container.setNoOfColumns((Integer)row[idx++]);

		if (row[idx] != null) {
			StorageLocationSummary location = new StorageLocationSummary();
			location.setId((Long)row[idx++]);

			int rowNo = (Integer)row[idx++];
			int colNo = (Integer)row[idx++];
			location.setPosition((rowNo - 1) * noOfColumns + colNo);
			container.setStorageLocation(location);
		}

		return container;
	}

	private void linkParentChildContainers(Map<Long, StorageContainerSummary> containersMap) {
		for (StorageContainerSummary container : containersMap.values()) {
			StorageLocationSummary location = container.getStorageLocation();
			if (location == null) {
				// root container
				continue;
			}

			StorageContainerSummary parent = containersMap.get(location.getId());

			//
			// Get back actual position value based on parent container dimension
			//
			int rowNo = (location.getPosition() - 1) / 10000 + 1, colNo = (location.getPosition() - 1) % 10000 + 1;
			location.setPosition((rowNo - 1) * parent.getNoOfColumns() + colNo);

			if (parent.getChildContainers() == null) {
				parent.setChildContainers(new ArrayList<>());
			}

			parent.getChildContainers().add(container);
		}
	}

	private StorageContainerSummary sortChildContainers(StorageContainerSummary container) {
		if (CollectionUtils.isEmpty(container.getChildContainers())) {
			return container;
		}

		Collections.sort(container.getChildContainers(), this::comparePositions);
		container.getChildContainers().forEach(this::sortChildContainers);
		return container;
	}

	private int comparePositions(StorageContainerSummary s1, StorageContainerSummary s2) {
		return s1.getStorageLocation().getPosition() - s2.getStorageLocation().getPosition();
	}

	private class ListQueryBuilder {
		private StorageContainerListCriteria crit;
		
		private StringBuilder select = new StringBuilder();
		
		private StringBuilder from = new StringBuilder();
		
		private StringBuilder where = new StringBuilder();
		
		private Map<String, Object> params = new HashMap<String, Object>();
		
		public ListQueryBuilder(StorageContainerListCriteria crit) {
			prepareQuery(crit, false);
		}
		
		public ListQueryBuilder(StorageContainerListCriteria crit, boolean countReq) {
			prepareQuery(crit, countReq);
		}

		public Query query() {						
			addNameRestriction();		
			addSiteRestriction();
					
			addFreeContainersRestriction();
			addSpecimenRestriction();
			addCpRestriction();
			addStoreSpecimenRestriction();
			
			addParentRestriction();
			addCanHoldRestriction();
			
			String hql = new StringBuilder(select)
				.append(" ").append(from)
				.append(" ").append(where)
				.append(" order by c.id asc")
				.toString();
			
			Query query = getCurrentSession().createQuery(hql);
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (param.getValue() instanceof Collection<?>) {
					query.setParameterList(param.getKey(), (Collection<?>)param.getValue());
				} else {
					query.setParameter(param.getKey(), param.getValue());
				}				
			}
			
			return query;
		}
		
		private void prepareQuery(StorageContainerListCriteria crit, boolean countReq) {
			this.crit = crit;
				
			if (crit.hierarchical()) {
				select = new  StringBuilder(countReq ? "select count (distinct c.id)" : "select distinct c");
				from = new StringBuilder("from ").append(getType().getName()).append(" c join c.descendentContainers dc");
				where = new StringBuilder("where dc.activityStatus = :activityStatus");
			} else {
				select = new StringBuilder(countReq ? "select count(c.id)" : "select c");
				from = new StringBuilder("from ").append(getType().getName()).append(" c");
				where = new StringBuilder("where c.activityStatus = :activityStatus");						
			}

			from.append(countReq ? " left join c.position pos " : " left join fetch c.position pos ");
			params.put("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		}
		
		private void addAnd() {
			if (where.length() != 0) {
				where.append(" and ");
			}
		}
		
		private void addNameRestriction() {			
			if (StringUtils.isBlank(crit.query())) {
				return;
			}
			
			addAnd();
			where.append("upper(c.name) like :name");
			params.put("name", "%" + crit.query().toUpperCase() + "%");
		}

		private void addFreeContainersRestriction() {			
			if (!crit.onlyFreeContainers()) {
				return;
			}

			addAnd();
			if (crit.hierarchical()) {
				where.append("dc.noOfRows * dc.noOfColumns - size(dc.occupiedPositions) > 0");
			} else {
				where.append("c.noOfRows * c.noOfColumns - size(c.occupiedPositions) > 0");
			}
		}

		private void addSiteRestriction() {
			boolean blankSiteName = StringUtils.isBlank(crit.siteName());
			boolean emptySiteIds = CollectionUtils.isEmpty(crit.siteIds());			
			if (blankSiteName && emptySiteIds) {
				return;
			}
			
			from.append(" join c.site site");

			if (!blankSiteName) {
				addAnd();
				where.append("site.name = :siteName");
				params.put("siteName", crit.siteName());				
			}
			
			if (!emptySiteIds) {
				addAnd();
				where.append("site.id in (:siteIds)");
				params.put("siteIds", crit.siteIds());				
			}
		}

		private void addParentRestriction() {
			if (!crit.topLevelContainers() && crit.parentContainerId() == null) {
				return;
			}
			
			if (crit.topLevelContainers()) {
				from.append(" left join c.parentContainer pc");			
			} else if (crit.parentContainerId() != null) {
				from.append(" join c.parentContainer pc");
			} 

			addAnd();
			Long parentId = crit.parentContainerId();
			if (parentId == null) {
				where.append("pc is null");
			} else {
				where.append("pc.id = :parentId");
				params.put("parentId", parentId);						
			}
		}

		private void addCanHoldRestriction() {
			if (StringUtils.isBlank(crit.canHold())) {
				return;
			}

			from.append(" left join c.type type");
			from.append(" left join type.canHold canHold");

			addAnd();
			where.append("(type is null or canHold.name = :canHold)");
			params.put("canHold", crit.canHold());
		}

		private void addSpecimenRestriction() {			
			String specimenClass = crit.specimenClass(), specimenType = crit.specimenType();			
			if (StringUtils.isBlank(specimenClass) && StringUtils.isBlank(specimenType)) {
				return;
			}
			
			addAnd();
			if (crit.hierarchical()) {
				where.append("(:specimenClass in elements(dc.compAllowedSpecimenClasses)")
				.append(" or ")
				.append(":specimenType in elements(dc.compAllowedSpecimenTypes)")
				.append(")");				
			} else {
				where.append("(:specimenClass in elements(c.compAllowedSpecimenClasses)")
				.append(" or ")
				.append(":specimenType in elements(c.compAllowedSpecimenTypes)")
				.append(")");				
			}
			
			params.put("specimenClass", specimenClass);
			params.put("specimenType", specimenType);
		}
		
		private void addCpRestriction() {
			if (CollectionUtils.isEmpty(crit.cpIds()) && CollectionUtils.isEmpty(crit.cpShortTitles())) {
				return;
			}
			
			if (crit.hierarchical()) {
				from.append(" left join dc.compAllowedCps cp");
			} else {
				from.append(" left join c.compAllowedCps cp");
			}
			
			addAnd();

			if (CollectionUtils.isNotEmpty(crit.cpIds())) {
				where.append("(cp is null or cp.id in (:cpIds))");
				params.put("cpIds", crit.cpIds());
			} else {
				where.append("(cp is null or cp.shortTitle in (:cpShortTitles))");
				params.put("cpShortTitles", crit.cpShortTitles());
			}
		}	
		
		private void addStoreSpecimenRestriction() {			
			if (crit.storeSpecimensEnabled() == null) {
				return;
			}

			addAnd();
			if (crit.hierarchical()) {
				where.append("dc.storeSpecimenEnabled = :storeSpecimenEnabled");
			} else {
				where.append("c.storeSpecimenEnabled = :storeSpecimenEnabled");
			}
			
			params.put("storeSpecimenEnabled", crit.storeSpecimensEnabled());
		}
	}


	private static final String FQN = StorageContainer.class.getName();

	private static final String GET_SPECIMENS_COUNT = FQN + ".getSpecimensCount";

	private static final String GET_ANCESTORS = FQN + ".getAncestors";

	private static final String GET_ROOT_AND_CHILD_CONTAINERS = FQN + ".getRootAndChildContainers";

	private static final String GET_CHILD_CONTAINERS = FQN + ".getChildContainers";

	private static final String GET_LEAST_EMPTY_CONTAINER_ID = FQN + ".getLeastEmptyContainerId";

	private static final String DEL_POS_BY_RSV_ID = FQN + ".deletePositionsByReservationIds";

	private static final String DEL_EXPIRED_RSV_POS = FQN + ".deleteReservationsOlderThanTime";

}
