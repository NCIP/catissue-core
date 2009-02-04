<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.beans.SessionDataBean;"%>


<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
    <tr>
    	<td>
          	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="40%">
            	<tr>
              		<td class="welcomeTitle" ><bean:message key="summary.page.welcome"/></td>
            	</tr>
            	<tr>
              		<td class="formMessage">&nbsp;</td>
            	</tr>
            	<tr>
              		<td  class="formMessage"><bean:message key="summary.page.description"/></td>
            	</tr>
            	<tr>
              		<td class="formMessage">&nbsp;</td>
            	</tr>


            	<tr>
            		<td>
                  		<table class="tbBorders" summary="" cellpadding="0" cellspacing="0" border="0" width="80%" >
<!-- FIRST ROW total specimens -->	
						<TR class="mainTotal">
							<TD COLSPAN=3>
								<%=Constants.TOTAL%>&nbsp;<%=Constants.SPECIMENS%>
							</TD>
							<TD>
								<BIG><%= request.getAttribute(Constants.SPECIMEN_COUNT)%></BIG>
							</TD>
<!-- FRIST ROW END -->	</TR>		

<!-- SEPARATOR -->
						<TR>
							<TD COLSPAN=4>						
								<HR>
							</TD>
						</TR>
						

<!-- 2ND ROW cell specimen -->
						<TR>
							<TD>&nbsp;</TD>
							<TD COLSPAN=2 class="classTotalText">
								<%=Constants.TOTAL%>&nbsp;<%=Constants.CELL%>&nbsp;<%=Constants.SPECIMENS%>
							</TD>
							<TD class="classTotalData">
								<%= request.getAttribute(Constants.CELL+Constants.SPECIMEN_TYPE_COUNT)%>
							</TD>
<!-- 2ND ROW END -->	</TR>

<!-- SEPARATOR -->
						<TR>
							<TD COLSPAN=4>						
								<HR>
							</TD>
						</TR>
						
<!-- 4TH ROW TISSUE specimen -->
						<TR>
							<TD>&nbsp;</TD>
							<TD COLSPAN=2 class="classTotalText">
								<%=Constants.TOTAL%>&nbsp;<%=Constants.TISSUE%>&nbsp;<%=Constants.SPECIMENS%>
							</TD>
							<TD class="classTotalData">
								<%= request.getAttribute(Constants.TISSUE+Constants.SPECIMEN_TYPE_COUNT)%>
							</TD>
<!-- 4TH ROW END -->	</TR>

<%
	Collection tissueDetails = (Collection)request.getAttribute(Constants.TISSUE+Constants.SPECIMEN_TYPE_DETAILS);
	if(tissueDetails != null)
	{
        	Iterator itr = tissueDetails.iterator();
        	while(itr.hasNext())
        	{
        		NameValueBean bean = (NameValueBean)itr.next(); 
%>
<!--  TISSUE specimen DETAILS -->
						<TR>
							<TD COLSPAN=2>&nbsp;&nbsp;</TD>
							<TD class="detailsData">
								<%=bean.getName()%>			
							</TD>
							<TD class="detailsCount">
								<%=bean.getValue()%>
							</TD>
						</TR>
<!--  TISSUE specimen DETAILS end -->
<%        		
        	}
 	}		
%>
<!-- SEPARATOR -->
						<TR>
							<TD COLSPAN=4>						
								<HR>
							</TD>
						</TR>


<!-- 5TH ROW FLUID specimen -->
						<TR>
							<TD>&nbsp;</TD>
							<TD COLSPAN=2 class="classTotalText">
								<%=Constants.TOTAL%>&nbsp;<%=Constants.FLUID%>&nbsp;<%=Constants.SPECIMENS%>
							</TD>
							<TD class="classTotalData">
								<%= request.getAttribute(Constants.FLUID+Constants.SPECIMEN_TYPE_COUNT)%>
							</TD>
<!-- 5TH ROW END -->	</TR>

<%
	Collection fluidDetails = (Collection)request.getAttribute(Constants.FLUID+Constants.SPECIMEN_TYPE_DETAILS);
	if(fluidDetails != null)
	{
        	Iterator itr = fluidDetails.iterator();
        	while(itr.hasNext())
        	{
        		NameValueBean bean = (NameValueBean)itr.next(); 
%>
<!--  fluid specimen DETAILS -->
						<TR>
							<TD COLSPAN=2>&nbsp;&nbsp;</TD>
							<TD class="detailsData">
								<%=bean.getName()%>			
							</TD>
							<TD class="detailsCount">
								<%=bean.getValue()%>
							</TD>
						</TR>
<!--  fluid specimen DETAILS end -->
<%        		
        	}
 	}		
%>
<!-- SEPARATOR -->
						<TR>
							<TD COLSPAN=4>						
								<HR>
							</TD>
						</TR>


<!-- 6TH ROW MOLECULAR specimen -->
						<TR>
							<TD>&nbsp;</TD>
							<TD COLSPAN=2 class="classTotalText">
								<%=Constants.TOTAL%>&nbsp;<%=Constants.MOLECULAR%>&nbsp;<%=Constants.SPECIMENS%>
							</TD>
							<TD class="classTotalData">
								<%= request.getAttribute(Constants.MOLECULAR+Constants.SPECIMEN_TYPE_COUNT)%>
							</TD>
<!-- 6TH ROW END -->	</TR>

<%
	Collection molecularDetails = (Collection)request.getAttribute(Constants.MOLECULAR+Constants.SPECIMEN_TYPE_DETAILS);
	if(molecularDetails != null)
	{
        	Iterator itr = molecularDetails.iterator();
        	while(itr.hasNext())
        	{
        		NameValueBean bean = (NameValueBean)itr.next(); 
%>
<!--  molecular specimen DETAILS -->
						<TR>
							<TD COLSPAN=2>&nbsp;&nbsp;</TD>
							<TD class="detailsData">
								<%=bean.getName()%>			
							</TD>
							<TD class="detailsCount">
								<%=bean.getValue()%>
							</TD>
						</TR>
<!--  molecular specimen DETAILS end -->
<%        		
        	}
 	}		
%>
<!-- SEPARATOR -->
						<TR>
							<TD COLSPAN=4>						
								<HR>
							</TD>
						</TR>


						
						</table>
					</td>
				</tr>
           </table>
      	</td>
    </tr>
</table>