/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/*
 * <p>Title: AppletModelInterface.java</p>
 * <p>Description:	This class initializes the fields of AppletModelInterface.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */

package edu.wustl.catissuecore.applet.model;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * This Model interface is used as communication data between applet & Server side component.Here data will be
 * a map which is used to contain (key,value) pair for data.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public interface AppletModelInterface extends Serializable
{

	/**
	 * map of data which is to be used as communication object between Applet component
	 * & server component.
	 * @param data map
	 */
	void setData(Map data);

	/**
	 * Gets data as map which contains objects as value & unique keys to access
	 * this objects.
	 * @return map data
	 */
	Map getData();
}
