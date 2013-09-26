/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Defines an interface for field-by-field comparison of two domain objects of
 * the same type and producing comparison results.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IDomainObjectComparator<T extends AbstractDomainObject> {

	/**
	 * Returns true if it supports comparison of objects of the given type.
	 * 
	 * @param cls
	 * @return
	 */
	boolean supports(Class<T> cls);

	/**
	 * Performs a field-by-field comparison and returns a {@link List} of
	 * results.
	 * 
	 * @param oldObj
	 * @param newObj
	 * @return
	 */
	List<IDomainObjectComparisonResult> compare(T oldObj, T newObj);

}
