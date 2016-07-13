
package com.krishagni.catissueplus.core.administrative.repository.impl;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
