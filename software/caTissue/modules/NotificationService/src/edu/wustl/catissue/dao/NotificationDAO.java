package edu.wustl.catissue.dao;

import java.util.Collection;

import edu.wustl.catissuecore.domain.ccts.Notification;

/**
 * @author Denis G. Krylov
 *
 */
public interface NotificationDAO {

	public void save(Notification notification);
	
	public Collection<Notification> findByExample(Notification notification);
	
	public int getCountByExample(Notification notification);

}