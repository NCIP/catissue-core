/*
 * Created on Jun 22, 2006
 */
package edu.wustl.catissuecore.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


import edu.wustl.common.util.tag.NLevelCustomCombo;

/**
 * @author chetan_bh
 *
 */
public class ContainerMap extends NLevelCustomCombo {
	private String buttonName;
	private String buttonOnClick = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";
	private String buttonStyleClass;
	private boolean buttonDisabled;
	private String id;
	private String value;
	
	public void setButtonName(String value)
	{
		buttonName = value;
	}
	
	public void setButtonOnClick(String value) {
		if(value != null && !value.equals(""))
			this.buttonOnClick = value;
	}	
	
	public void setId(String value) {
		this.id = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setButtonDisabled(boolean value) {
		this.buttonDisabled = value;
	}
	
	public void setButtonStyleClass(String value) {
		this.buttonStyleClass = value;
	}
	
	public int doStartTag() throws JspException {
		
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
			io.printStackTrace();
		}
		return super.doEndTag();
	}
	
	private void print(String[] strArray)
	{
		System.out.println("-------------"+strArray.length+"-----------");
		for(int i = 0; i< strArray.length; i++)
		{
			System.out.println(i+" : "+strArray[i]);
		}
		System.out.println("----------------------------------------------");
	}
	
}
