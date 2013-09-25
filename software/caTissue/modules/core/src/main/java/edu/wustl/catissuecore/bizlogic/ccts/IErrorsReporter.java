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
 * Defines an interface for callback mechanism used, for example, in
 * {@link IDataConverter} to report any error conditions, fatal or not, back to
 * the caller.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IErrorsReporter {

	/**
	 * This method is invoked when an error occurs.
	 * 
	 * @param msg
	 *            a error description
	 */
	void error(String msg);

}
