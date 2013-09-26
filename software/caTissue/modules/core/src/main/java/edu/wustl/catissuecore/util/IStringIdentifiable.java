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
package edu.wustl.catissuecore.util;

/**
 * Implemented by classes, whose instances can be represented and described by a
 * single {@link String} value, which in turn could be used for comparisons, and
 * so on.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IStringIdentifiable extends Comparable<IStringIdentifiable> {
	
	
	/**
	 * @return
	 */
	String getAsString();

}
