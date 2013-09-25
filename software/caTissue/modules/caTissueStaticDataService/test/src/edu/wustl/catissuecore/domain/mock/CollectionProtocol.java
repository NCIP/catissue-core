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
import org.hibernate.LazyInitializationException;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ion C. Olaru
 *         Date: 1/18/12 -5:13 PM
 */
public class CollectionProtocol extends edu.wustl.catissuecore.domain.CollectionProtocol {
    @Override
    public Collection<StudyFormContext> getStudyFormContextCollection() {
        if (super.getStudyFormContextCollection() == null)
            throw new LazyInitializationException("MOCK OBJECT: CollectionProtocol");
        else
            return super.getStudyFormContextCollection();
    }

    @Override
    public Set<CPGridGrouperPrivilege> getGridGrouperPrivileges() {
        if (super.getGridGrouperPrivileges() == null)
            throw new LazyInitializationException("MOCK OBJECT: CollectionProtocol");
        else
            return super.getGridGrouperPrivileges();
    }
}
