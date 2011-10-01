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
