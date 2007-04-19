<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
</head>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
			<!-- 
							 * Name: Shital Lawhale
							 * Reviewer Name: Sachin Lale
							 * Bug ID: 3835
							 * Patch ID: 3835_1_12
							 * See also: 1_1 to 1_5
							 * Description : Added <TR> for createdOn field .				 
							-->	 

<tr>				

	 
	<td class="message" >
	<%
		
		if(createdDate == null)
		    createdDate = "";	   
		
		if(createdDate.trim().length() > 0)
		{
			Integer specimenYear = new Integer(Utility.getYear(createdDate ));
			Integer specimenMonth = new Integer(Utility.getMonth(createdDate ));
			Integer specimenDay = new Integer(Utility.getDay(createdDate ));
									
	%>
	<ncombo:DateTimeComponent name="<%= dateFormName %>"
							  id="<%= dateFormName %>"
							  formName= "<%= nameOfForm %>"
							  month= "<%= specimenMonth %>"
							  year= "<%= specimenYear %>"
							  day= "<%= specimenDay %>"
							  value="<%=createdDate %>"
							  styleClass="formDateSized10"		/>
	<% }else{  %>
	<ncombo:DateTimeComponent name="<%= dateFormName %>"
							  id="<%= dateFormName %>"
							  formName="<%= nameOfForm %>"
							  styleClass="formDateSized10" />
	<% } %> 

	<bean:message key="page.dateFormat" />&nbsp;
	</td>
</tr>
		
</table>