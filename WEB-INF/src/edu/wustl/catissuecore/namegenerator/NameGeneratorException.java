/**
 * 
 */
package edu.wustl.catissuecore.namegenerator;

/**
 * @author abhijit_naik
 *
 */
public class NameGeneratorException extends Exception {

	/**
	 * @param string
	 */
	public NameGeneratorException(String string) {
		super(string);
	}
	
	public NameGeneratorException(String string, Throwable th) {
		super(string,th);
	}
	
	
}
