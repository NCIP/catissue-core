/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.util.listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * 
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CatissueCoreSessionBindingListener implements HttpSessionBindingListener
{

	public void valueBound(HttpSessionBindingEvent httpSessionBindEvent)
	{

	}

	/**
	 * This method is used to perform cleanup like deleting all temprary folders and tables created for the user.
	 * @param session Object of HttpSessionBindingEvent.
	 */

	public void valueUnbound(HttpSessionBindingEvent httpSessionBindEvent)
	{

	}

	private void cleanUp()
	{

	}

}