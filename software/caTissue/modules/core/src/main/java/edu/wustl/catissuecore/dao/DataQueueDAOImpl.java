/**
 * 
 */
package edu.wustl.catissuecore.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;

/**
 * Data Access Object for {@link DataQueue}.
 * 
 * @author Denis G. Krylov
 * 
 */
@Transactional
public class DataQueueDAOImpl extends GenericDAOImpl implements DataQueueDAO {

	@Override
	public int getPendingIncomingDataQueueItemsCount() {
		DataQueue ex = new DataQueue();
		ex.setProcessingStatus(ProcessingStatus.PENDING);
		ex.setIncoming(true);
		return getCountByExample(ex);
	}

	@Override
	public DataQueue pickIncomingDataQueueItemForProcessing() {
		List<DataQueue> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session
								.createCriteria(DataQueue.class);
						criteria.add(
								Restrictions
										.in("processingStatus",
												new ProcessingStatus[] { ProcessingStatus.PENDING }))
								.add(Restrictions.eq("incoming", Boolean.TRUE))
								.addOrder(Order.asc("dateTime"))
								.setFirstResult(0).setMaxResults(1);
						return criteria.list();
					}
				});
		DataQueue item = null;
		if (CollectionUtils.isNotEmpty(list)) {
			item = list.get(0);
		}
		return item;
	}

	@Override
	public List<DataQueue> getPendingDataQueueItems(String gridId) {
		List<DataQueue> list = getHibernateTemplate()
				.find("from DataQueue dq where dq.notification.objectIdValue=? and dq.processingStatus=?",
						new Object[] { gridId, ProcessingStatus.PENDING });

		return list;
	}

}
