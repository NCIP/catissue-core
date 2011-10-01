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
