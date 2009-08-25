
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

	public void valueBound(HttpSessionBindingEvent e)
	{

	}

	/**
	 * This method is used to perform cleanup like deleting all temprary folders and tables created for the user.
	 * @param session Object of HttpSessionBindingEvent.
	 */

	public void valueUnbound(HttpSessionBindingEvent e)
	{

	}

	private void cleanUp()
	{

	}

}