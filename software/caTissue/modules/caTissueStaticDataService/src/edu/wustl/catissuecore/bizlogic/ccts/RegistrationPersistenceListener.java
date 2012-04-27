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
