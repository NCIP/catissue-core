package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.TenantDetail;
import com.krishagni.catissueplus.core.administrative.services.ContainerSelectionStrategy;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

@Configurable
public class RecentlyUsedContainerSelectionStrategy implements ContainerSelectionStrategy {
	private static Log logger = LogFactory.getLog(RecentlyUsedContainerSelectionStrategy.class);

	@Autowired
	private DaoFactory daoFactory;

	@Autowired
	private SessionFactory sessionFactory;

	private CollectionProtocol cp;

	private StorageContainer recentlyUsed = null;

	@Override
	public StorageContainer getContainer(TenantDetail criteria, Boolean aliquotsInSameContainer) {
		int numFreeLocs = 1;
		if (aliquotsInSameContainer != null && aliquotsInSameContainer && criteria.getNumOfAliquots() > 1) {
			numFreeLocs = criteria.getNumOfAliquots();
		}

		StorageContainer container = recentlyUsed;
		if (container == null) {
			container = getRecentlySelectedContainer(criteria);
		}

		if (container == null) {
			return null;
		}

		if (!canContainSpecimen(container, criteria, numFreeLocs)) {
			container = nextContainer(container, criteria, numFreeLocs);
		}

		return (recentlyUsed = container);
	}

	@SuppressWarnings("unchecked")
	private StorageContainer getRecentlySelectedContainer(TenantDetail crit) {
		//
		// first lookup containers used for (cp, class, type) combination
		//
		List<StorageContainer> containers = getRecentlySelectedContainerQuery(crit)
			.add(Restrictions.eq("spmn.specimenClass", crit.getSpecimenClass()))
			.add(Restrictions.eq("spmn.specimenType", crit.getSpecimenType()))
			.list();

		if (CollectionUtils.isNotEmpty(containers)) {
			return containers.iterator().next();
		}

		//
		// when above fails, lookup containers used for cp alone
		//
		containers = getRecentlySelectedContainerQuery(crit).list();
		return CollectionUtils.isNotEmpty(containers) ? containers.iterator().next() : null;
	}

	private Criteria getRecentlySelectedContainerQuery(TenantDetail criteria) {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("activeEntity");
		return session.createCriteria(StorageContainer.class)
			.createAlias("occupiedPositions", "pos")
			.createAlias("pos.occupyingSpecimen", "spmn")
			.createAlias("spmn.visit", "visit")
			.createAlias("visit.registration", "reg")
			.createAlias("reg.collectionProtocol", "cp")
			.add(Restrictions.eq("cp.id", criteria.getCpId()))
			.addOrder(Order.desc("pos.id"))
			.setMaxResults(1);
	}

	private StorageContainer nextContainer(StorageContainer last, TenantDetail crit, int freeLocs) {
		logger.info(String.format(
			"Finding next container satisfying criteria (cp = %d, class = %s, type = %s, free locs = %d) with base %s",
			crit.getCpId(), crit.getSpecimenClass(), crit.getSpecimenType(), freeLocs, last.getName()
		));
		return nextContainer(last.getParentContainer(), last, crit, freeLocs, new HashSet<>());
	}

	private StorageContainer nextContainer(StorageContainer parent, StorageContainer last, TenantDetail criteria, int freeLocs, Set<StorageContainer> visited) {
		if (parent == null) {
			return null;
		}

		logger.info(String.format("Exploring children of %s, last = %s", parent.getName(), (last != null) ? last.getName() : "none"));

		int childIdx = -1;
		List<StorageContainer> children = parent.getChildContainersSortedByPosition();
		if (last != null) {
			for (StorageContainer container : children) {
				childIdx++;
				if (container.getPosition().getPosition() == last.getPosition().getPosition()) {
					logger.info(String.format("Found container %s at %d", container.getName(), childIdx));
					break;
				}
			}
		}

		for (int i = childIdx + 1; i < children.size(); ++i) {
			if (!visited.add(children.get(i))) {
				continue;
			}

			StorageContainer container = nextContainer(children.get(i), null, criteria, freeLocs, visited);
			if (container != null) {
				return container;
			}
		}

		logger.info("Probing whether container " + parent.getName() + " can satisfy request");
		if (canContainSpecimen(parent, criteria, freeLocs)) {
			logger.info("Selected container " + parent.getName());
			return parent;
		} else if (visited.add(parent)) {
			return nextContainer(parent.getParentContainer(), parent, criteria, freeLocs, visited);
		} else {
			logger.info("End of tree " + parent.getName());
			return null;
		}
	}

	private boolean canContainSpecimen(StorageContainer container, TenantDetail crit, int freeLocs) {
		if (cp == null) {
			cp = daoFactory.getCollectionProtocolDao().getById(crit.getCpId());
		}

		return container.canContainSpecimen(cp, crit.getSpecimenClass(), crit.getSpecimenType()) &&
				container.freePositionsCount() >= freeLocs;
	}
}
