/**
 * 
 */

package edu.wustl.catissuecore.tag;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 */
public class TwoColDetailsTag extends TagSupport
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(TwoColDetailsTag.class);

	// --------------- attributes section --------------
	private String formName;

	private Collection displayList;
	String divId = "cddiv";
	String hdrKey = "summary.page.cd";

	// --------------- end of attributes section --------------

	//----------- start of get-set methods ---------------
	public Collection getDisplayList()
	{
		return this.displayList;
	}

	public String getDivId()
	{
		return this.divId;
	}

	public void setDivId(String divId)
	{
		this.divId = divId;
	}

	public String getHdrKey()
	{
		return this.hdrKey;
	}

	public void setHdrKey(String hdrKey)
	{
		this.hdrKey = hdrKey;
	}

	public void setDisplayList(final Collection displayList)
	{
		this.displayList = displayList;
	}

	public String getFormName()
	{
		return this.formName;
	}

	public void setFormName(final String formName)
	{
		this.formName = formName;
	}

	//----------- end of get-set methods ---------------
	//-------------- life cycle ----------------

	@Override
	public int doEndTag() throws JspException
	{
		this.clear();
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException
	{
		try
		{
			final JspWriter out = this.pageContext.getOut();

			out.print("");
			out.print(this.process1());
		}
		catch (final IOException ioe)
		{
			TwoColDetailsTag.logger.error(ioe.getMessage(), ioe);
			ioe.printStackTrace() ;
			throw new JspTagException("Error:IOException while writing to the user");
		}
		return SKIP_BODY;

	}

	//	private String process()
	//	{
	//		final StringBuffer strb = new StringBuffer();
	//		try
	//		{
	//			final List dispList = (List)displayList;
	//			final int listCount = dispList.size();
	//			int tissueCount = 0;
	//			
	//			if (!dispList.isEmpty())
	//			{
	//				NameValueBean bean1 = new NameValueBean();
	//				for(int cnt=0;cnt<listCount;cnt++)
	//				{
	//	
	//					NameValueBean bean = (NameValueBean)dispList.get(cnt);
	//					tissueCount++;
	//					if((cnt+1)<listCount)
	//					{
	//						bean1 = (NameValueBean)dispList.get(cnt+1);
	////						tissueCount++;
	//						cnt++;
	//					}
	//					else
	//					{
	//						bean1.setName("-");bean1.setValue("-");
	//					}
	//	
	//					final int count = tissueCount;
	//					String style="black_ar";
	//					if(count % 2 == 0)
	//					{
	//						style="tabletd1";
	//					}
	//					
	//					strb.append("<TR>");
	//					genOutput(strb, bean, style);
	//					genOutput(strb, bean1, style);
	//					strb.append("</TR>");
	//				}
	//			}		
	//		}
	//		catch(Exception e)
	//		{
	//			e.printStackTrace();	
	//		}
	//		return strb.toString();
	//	}
	//	
	private void clear()
	{
		this.formName = "";
		this.divId = "";
		this.hdrKey = "";

		if (null != this.displayList)
		{
			this.displayList.clear();
		}
	}

	//	private void genOutput(final StringBuffer sbstr, final NameValueBean nvb, final String style)
	//	{
	//		sbstr.append("<TD width='35%' class='");
	//		sbstr.append(style);
	//		sbstr.append("'>");
	//		sbstr.append(nvb.getName());
	//		sbstr.append("</TD>");
	//		sbstr.append("<TD width='15%' class='");
	//		sbstr.append(style);
	//		sbstr.append("'>");
	//		sbstr.append(nvb.getValue());
	//		sbstr.append("</TD>");
	//	}
	//		
	private String process1()
	{
		final StringBuffer strb = new StringBuffer();
		try
		{
			final List dispList = (List) this.displayList;

			if (!dispList.isEmpty())
			{
				strb.append("<div id='");
				strb.append(this.divId);
				strb
						.append("' style=\"height: 100%; width: 100%; \"><table border='0' width='100%'>");
				if (this.hdrKey.trim().length() > 0)
				{
					strb
							.append("<tr class='gray_h2_md'><td width='100%' colspan='2'  class='gray_h2_md'>");
					final String hdr = ApplicationProperties.getValue(this.hdrKey);
					strb.append(hdr);
					strb.append("</td></tr>");
				}
				String style = "black_ar";
				for (int cnt = 0; cnt < dispList.size(); cnt++)
				{
					final NameValueBean bean = (NameValueBean) dispList.get(cnt);
					style = this.getStyle((cnt + 1));
					this.createCol1(strb, bean, style);
				}
				strb.append("</table></div>");
			}
		}
		catch (final Exception e)
		{
			TwoColDetailsTag.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
		}
		return strb.toString();
	}

	/**
	 * @param counter
	 * @param style
	 * @return
	 */
	private String getStyle(int counter)
	{
		String style1 = "black_ar";
		final int count = counter;

		if (count % 2 == 0)
		{
			style1 = "tabletd1";
		}
		return style1;
	}

	private void createCol1(final StringBuffer sbstr, final NameValueBean nvb, final String style)
	{
		sbstr.append("<TR><TD class='");
		sbstr.append(style);
		sbstr.append("'width='50%'>");
		sbstr.append(nvb.getName());
		sbstr.append("</TD><TD class='");
		sbstr.append(style);
		sbstr.append("'>");
		sbstr.append(nvb.getValue());
		sbstr.append("</TD></TR>");
	}

}
