<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
		 "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SummaryForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility" %>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
			String temp = null;
			Object obj = request.getAttribute("summaryForm");
			SummaryForm summaryForm = null;
			if(obj != null && obj instanceof SummaryForm)
			{
				summaryForm = (SummaryForm)obj;
			}			
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td nowrap class="td_table_head"><span class="wh_ar_b"><bean:message key="summary.page.welcome"/></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td valign="top" class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" >&nbsp;</td>
        </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        
        <tr>
          <td height="8" ></td>
        </tr>
        <tr>
          <td align="left" class="black_ar">&nbsp;<bean:message key="summary.page.description"/></td>
        </tr>
        
        <tr>
          <td align="center" valign="top" class="showhide"><table width="100%" border="0" cellpadding="4" cellspacing="0">

		 
              <tr class="tr_bg_blue1">
                <td width="350"><span class="blue_ar_b">&nbsp;<bean:message key="summary.page.caption"/>&nbsp;</span></td>
                <td class="blue_ar_b">${summaryForm.totalSpecimenCount}</td>
              </tr>

              <tr>
                <td colspan="2"></td>
              </tr>


			   <tr>
                <td class="tableheading"><strong><bean:message key="summary.page.total.cell.count"/></strong></td>
                <td class="tableheading"><strong>${summaryForm.cellCount}</strong></td>
              </tr>
			  <%
					Collection cellType = summaryForm.getCellTypeDetails();
					int cellCount = 1;
					if (!cellType.isEmpty())
					{
						Iterator itr = cellType.iterator();
					   	while(itr.hasNext())
				    	{
				    		NameValueBean bean = (NameValueBean)itr.next();
						%>
						<tr>
						<c:set var = "count" value = "<%=cellCount%>" scope="page" />
						<c:set var="style" value="black_ar" scope="page" />
						
						<c:if test='${pageScope.count % 2 == 0}'>
							<c:set var="style" value="tabletd1" scope="page" />
						</c:if>
						<td class='${pageScope.style}'><c:out value='<%=bean.getName()%>' /></td>
						<td class='${pageScope.style}'><c:out value='<%=bean.getValue()%>' /></td></tr>
						<%
							cellCount++;
						}
					}		
				%>			  
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>

              <tr class="tableheading">
                <td><strong><bean:message key="summary.page.total.tissue.count"/></strong></td>
                <td><b>${summaryForm.tissueCount}</b></td>
              </tr>
			<%
					Collection tissueType = summaryForm.getTissueTypeDetails();
					int tissueCount = 1;
					if (!tissueType.isEmpty())
					{
						Iterator itr = tissueType.iterator();
					   	while(itr.hasNext())
				    	{
				    		NameValueBean bean = (NameValueBean)itr.next();							
						%>
						<tr>
						<c:set var = "count" value = "<%=tissueCount%>" scope="page" />
						<c:set var="style" value="black_ar" scope="page" />
						
						<c:if test='${pageScope.count % 2 == 0}'>
							<c:set var="style" value="tabletd1" scope="page" />
						</c:if>
						<td class='${pageScope.style}'><c:out value='<%=bean.getName()%>' /></td>
						<td class='${pageScope.style}'><c:out value='<%=bean.getValue()%>' /></td>
						</tr>
						<%
							tissueCount++;
						}
					}		
				%>
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>
			  <tr>
                <td class="tableheading"><strong><bean:message key="summary.page.total.fluid.count"/></strong></td>
                <td class="tableheading"><strong>${summaryForm.fluidCount}</strong></td>
              </tr>
			  <%
					Collection fluidType = summaryForm.getFluidTypeDetails();
					int fluidCount = 1;
					if (!fluidType.isEmpty())
					{
						Iterator itr = fluidType.iterator();
					   	while(itr.hasNext())
				    	{
				    		NameValueBean bean = (NameValueBean)itr.next();							
						%>
						<tr>
						<c:set var = "count" value = "<%=fluidCount%>" scope="page" />
						<c:set var="style" value="black_ar" scope="page" />
						
						<c:if test='${pageScope.count % 2 == 0}'>
							<c:set var="style" value="tabletd1" scope="page" />
						</c:if>
						<td class='${pageScope.style}'><c:out value='<%=bean.getName()%>' /></td>
						<td class='${pageScope.style}'><c:out value='<%=bean.getValue()%>' /></td></tr>
						<%	
							fluidCount++;
						}
					}		
				%>
			  <tr>
                <td colspan="2">&nbsp;</td>
              </tr>			  
			  <tr>
                <td class="tableheading"><strong><bean:message key="summary.page.total.molecular.count"/></strong></td>
                <td class="tableheading"><strong>${summaryForm.moleculeCount}</strong></td>
              </tr>
			 <%
					Collection molecularType = summaryForm.getMoleculeTypeDetails();
					int molecularCount = 1;
					if (!molecularType.isEmpty())
					{
						Iterator itr = molecularType.iterator();
					   	while(itr.hasNext())
				    	{
				    		NameValueBean bean = (NameValueBean)itr.next();					
						%>
						<tr>
						<c:set var = "count" value = "<%=molecularCount%>" scope="page" />
						<c:set var="style" value="black_ar" scope="page" />
						
						<c:if test='${pageScope.count % 2 == 0}'>
							<c:set var="style" value="tabletd1" scope="page" />
						</c:if>
						<td class='${pageScope.style}'><c:out value='<%=bean.getName()%>' /></td>
						<td class='${pageScope.style}'><c:out value='<%=bean.getValue()%>' /></td></tr>
						<%
							molecularCount++;
						}
					}		
				%>
          </table></td>
        </tr>
    </table></td>
  </tr>
</table>
</body>
</html>