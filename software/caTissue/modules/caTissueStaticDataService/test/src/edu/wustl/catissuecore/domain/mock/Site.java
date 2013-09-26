/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.mock;

import edu.wustl.catissuecore.domain.CPGridGrouperPrivilege;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.User;
import org.hibernate.LazyInitializationException;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ion C. Olaru
 *         Date: 1/19/12 -10:47
 */
public class Site extends edu.wustl.catissuecore.domain.Site {

    @Override
    public Collection<User> getAssignedSiteUserCollection() {
        if (super.getAssignedSiteUserCollection() == null)
            throw new LazyInitializationException("MOCK OBJECT: Site");
        else
            return super.getAssignedSiteUserCollection();
    }
}
