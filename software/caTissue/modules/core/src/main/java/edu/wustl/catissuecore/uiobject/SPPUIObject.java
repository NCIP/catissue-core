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
