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
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.util;

public interface Roles extends edu.wustl.security.global.Roles
{
	public static final String PI = "PI";
    public static final String READ_ONLY = "READ_ONLY";
    public static final String USE_ONLY = "USE_ONLY";
    public static final String UPDATE_ONLY = "UPDATE_ONLY";
    public static final String COORDINATOR = "Coordinator";
    public static final String SUPERVISOR = "Supervisor";
}
