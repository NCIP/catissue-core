/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.ccts;

import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

/**
 * @author Ion C. Olaru
 *         Date: 4/26/12 -8:40 PM
 */
public class RegistrationPersistenceListener implements PostInsertEventListener, PostUpdateEventListener {
    public void onPostInsert(PostInsertEvent postInsertEvent) {
    }

    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
    }
}
