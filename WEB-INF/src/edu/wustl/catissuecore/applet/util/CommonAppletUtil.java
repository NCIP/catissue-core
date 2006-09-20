/*
 * <p>Title: CommonAppletUtil.java</p>
 * <p>Description:	This class initializes the fields of CommonAppletUtil.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 20, 2006
 */
package edu.wustl.catissuecore.applet.util;

import java.awt.Component;

import javax.swing.JApplet;

/**
 * <p> This util class is used to specify common applet level operations.
 * Here all applet related util methods should reside.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public final class CommonAppletUtil {

	/**
	 * gets the base applet from a given component
	 * @param component component
	 * @return applet  
	 */
	public static JApplet getBaseApplet(Component component) {
		while (component != null) {
			if (component instanceof JApplet) {
				return ((JApplet) component);
			}
			component = component.getParent();
		}
		return null;
	}
	
}
