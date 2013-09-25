/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.uiobject;

import org.apache.struts.upload.FormFile;

import edu.wustl.common.domain.UIObject;

public class SPPUIObject implements UIObject
{
	protected FormFile xmlFileName;


	/**
	 * @return the xmlFileName
	 */
	public FormFile getXmlFileName()
	{
		return xmlFileName;
	}

	/**
	 * @param xmlFileName the xmlFileName to set
	 */
	public void setXmlFileName(FormFile xmlFileName)
	{
		this.xmlFileName = xmlFileName;
	}
}
