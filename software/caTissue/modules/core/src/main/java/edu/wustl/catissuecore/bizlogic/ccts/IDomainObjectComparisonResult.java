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

/**
 * Result of a field-level comparison of two objects: "old" and "new".
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IDomainObjectComparisonResult {

	/**
	 * @return
	 */
	Object getOldValue();

	/**
	 * @return
	 */
	Object getNewValue();

	/**
	 * Whether the two values are to be considered unequal, i.e. different.
	 * 
	 * @return
	 */
	boolean isDifferent();
	
	/**
	 * The name of the field that's been compared.
	 * @return
	 */
	String getFieldName();

}
