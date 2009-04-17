/*
 * Created on Jun 22, 2006
 */
package edu.wustl.catissuecore.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.tag.NLevelCustomCombo;

/**
 * @author chetan_bh
 * JSP Tag for generating HTML equivalent for storage container location component (Container Map).
 * This component consists of NLevelCustomCombo and a button component.
 *  
 */
public class ContainerMap extends NLevelCustomCombo {
	
	private transient Logger logger = Logger.getCommonLogger(ContainerMap.class);
	/**
	 * Name for the button component.
	 */
	private String buttonName;
	
	/**
	 * OnClick event handler.
	 */
	private String buttonOnClick = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";
	
	/**
	 * style class to apply for this button
	 */
	private String buttonStyleClass;
	
	/**
	 * A boolean value to disable/enable button.
	 */
	private boolean buttonDisabled;
	
	/**
	 * Id attribute for this button component.
	 */
	private String id;
	
	/**
	 * Value attribute for this button component, which is used as buttons display name.
	 */
	private String value;
	
	
	/**
	 * A call back function which sets buttonName attribute.
	 * @param value name for the button.
	 */	
	public void setButtonName(String value)
	{
		buttonName = value;
	}
	
	/**
	 * A call back function which sets buttonOnClick attribute.
	 */
	public void setButtonOnClick(String value) {
		if(value != null && !value.equals(""))
			this.buttonOnClick = value;
	}	
	
	/**
	 * A call back function which sets Id attribute.
	 * @param
	 */
	public void setId(String value) {
		this.id = value;
	}
	
	/**
	 * A call back function which sets value attribute.
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * A call back function which sets disabled attribute.
	 * @param value
	 */
	public void setButtonDisabled(boolean value) 
	{
		this.buttonDisabled = value;
	}
	
	/**
	 * A call back function to set style class for button component.
	 * @param value style class to set
	 */
	public void setButtonStyleClass(String value) {
		this.buttonStyleClass = value;
	}
	
	/**
	 * A call back function which is executed when JSP runtime encouters
	 * a open tag for this custom tag.
	 */
	public int doStartTag() throws JspException {
		//	 Call to NLevelCustomCombo's doStartTag();
		return super.doStartTag();		
	}
	
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			
			out.print("<td class=\""+tdStyleClass+"\"> <input type=\"button\"" +
							" property=\"mapButton\" " +
							" class=\""+buttonStyleClass+"\" " +
							" id=\""+id+"\" " +
							" value=\""+value+"\" " +
							//" onclick=\"StorageMapWindow('ShowFramedPage.do?pageOf=pageOfStorageLocation&amp;storageType=','name','810','320','yes');return false\" " +
							"onclick= \""+buttonOnClick+" \"" +
							(buttonDisabled?"disabled=\"true\"":"")+
							" name=\""+buttonName+"\" >"+
							
						" </td>");
			out.println("</tr></table> ");
		}catch(IOException io)
		{
			logger.error(io);
		}
		// Call to NLevelCustomCombo's doEndTag();
		return super.doEndTag();
	}
	
	private void print(String[] strArray)
	{
		 logger.info("-------------" + strArray.length + "-----------");
		for(int i = 0; i< strArray.length; i++)
		{
			logger.info(i + " : " + strArray[i]);
		}
		logger.info("----------------------------------------------");
	}
	
}
