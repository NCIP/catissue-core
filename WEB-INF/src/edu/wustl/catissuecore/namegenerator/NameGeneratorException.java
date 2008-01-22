/**
 * 
 */
package edu.wustl.catissuecore.namegenerator;

/**
 * This is the Exception clas related to NameGenerator. 
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
	
	/**
	 * @param string
	 * @param th
	 */
	public NameGeneratorException(String string, Throwable th) {
		super(string,th);
	}
	
	
}
