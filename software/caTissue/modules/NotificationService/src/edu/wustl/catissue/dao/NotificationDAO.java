/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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