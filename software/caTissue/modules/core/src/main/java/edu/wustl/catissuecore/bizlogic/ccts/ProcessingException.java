/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

/**
 * @author Denis G. Krylov
 *
 */
public class ProcessingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ProcessingException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ProcessingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ProcessingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
