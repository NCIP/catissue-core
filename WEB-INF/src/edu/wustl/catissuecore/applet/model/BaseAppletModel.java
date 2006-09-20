/*
 * <p>Title: CaTissueAppletModel.java</p>
 * <p>Description:	This class initializes the fields of CaTissueAppletModel.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
package edu.wustl.catissuecore.applet.model;

import java.util.Map;

/**
 * <p> Applet Model implementation of applet model interface used as communicator
 * object between Applet & Server side component.
 * @author Ashwin Gupta
 * @version 1.1
 * @see edu.wustl.catissuecore.appletui.model.AppletModelInterface
 */

public class BaseAppletModel implements AppletModelInterface {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * map of data which is to be used as communication object between Applet component 
	 * & server component.
	 */
	private Map data;

	/**
	 * Sets data as map which contains objects as value & unique keys to access
	 * this objects.
	 * @param data map 
	 * @see edu.wustl.catissuecore.appletui.model.AppletModelInterface#setData(java.util.Map)
	 */
	public void setData(Map data) {
		this.data = data;
	}

	/**
	 * Gets data as map which contains objects as value & unique keys to access
	 * this objects.
	 * @return map data 
	 * @see edu.wustl.catissuecore.appletui.model.AppletModelInterface#getData()
	 */
	public Map getData() {
		return this.data;
	}
}
