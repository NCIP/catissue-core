
package edu.wustl.common.util;

/**
 * Interface for storing the names of Permissions that are defined
 * in the authorization database.
 * 
 * 
 * @author Brian Husted
 */
public interface Permissions {

	public static final String READ = "READ";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String CREATE = "CREATE";
	public static final String EXECUTE = "EXECUTE";
	public static final String USE = "USE";
	public static final String ASSIGN_READ = "ASSIGN_READ";
	public static final String ASSIGN_USE = "ASSIGN_USE";
	public static final String IDENTIFIED_DATA_ACCESS = "IDENTIFIED_DATA_ACCESS";
}
