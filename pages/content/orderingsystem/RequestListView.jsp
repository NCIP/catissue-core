<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">
	function filteringAction()
	{
		document.forms[0].action="RequestListView.do?pageNum=1&menuSelected=17";
		document.forms[0].submit();
	}		
</script>
  <%
	  int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	  int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	  int numResultsPerPage = Constants.NUMBER_RESULTS_PER_PAGE;
	  int count = numResultsPerPage * (pageNum -1) + 1;

  %>
<head>
</head>
<body >
  <html:form action="RequestListView.do"> 
  	 <jsp:useBean id="requestListForm" class="edu.wustl.catissuecore.actionForm.RequestListFilterationForm" scope="request"/>
	 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key='orderingSystem.orderListPage.buttons.order'/></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Order List" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td colspan="2" align="left"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key='header.requestList.label'/> &nbsp;</span><span class="black_ar_s">(<% int totalRequests=requestListForm.getNewRequests()+requestListForm.getPendingRequests();%>
							<%= totalRequests %> <bean:message key='order.totalRequests'/> &nbsp;<bean:write name="requestListForm" property="newRequests"/> <bean:message key='order.totalRequestsNew'/>, <bean:write name="requestListForm" property="pendingRequests"/> <bean:message key='order.totalRequestsPending'/> )</span></td>
      </tr>
      <tr>
        <td align="right"><label> <span class="black_ar"><bean:message key="request.status.show.label" />&nbsp;</span>
              <html:select property="requestStatusSelected" name="requestListForm" styleClass="black_ar"  styleId="requestStatusSelected" size="1"   
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="filteringAction()">
								<html:options collection="<%= Constants.REQUEST_LIST %>" labelProperty="name" property="value"/>		
						</html:select>
        </label></td>
        <td width="1%"></td>
      </tr>
      <tr>
        <td colspan="2" ><table width="99%" border="0" align="center" cellpadding="4" cellspacing="0">
            <tr>
              <td colspan="5" class="black_ar_b"><custom:test name=" Request List Queue: " pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="RequestListView.do"/></td>
              <td colspan="2" align="right" class="black_ar_b"></td>
            </tr>
            <tr class="tableheading">
              <td width="2%"><bean:message key="requestlist.dataTabel.serialNo.label" /></td>
              <td width="20%"><strong><bean:message key='requestlist.dataTabel.OrderName.label'/></strong></td>
              <td width="18%"><strong><bean:message key='requestlist.dataTabel.DistributionProtocol.label'/></strong></td>
              <td width="15%"><strong><bean:message key='requestlist.dataTabel.label.PIOfDP'/></strong></td>
              <td width="15%"><strong><bean:message key='requestlist.dataTabel.label.RequestDate'/></strong></td>
              <td width="12%"><strong><bean:message key='requestlist.dataTabel.label.Status'/></strong></td>
            </tr>
            <%  List requestListData = (List)request.getAttribute("RequestList");						
					%>	
					<logic:iterate id="rowDataString" collection="<%=requestListData%>" type="edu.wustl.catissuecore.bean.RequestViewBean">
         			 <% 
					 String style1 = "black_ar";
					 if (count%2 ==0) { 
						 style1 = "tabletd1";
   					 } %>
					<tr class="<%=style1%>">
              <td ><bean:write name="rowDataString" property="serialNo"/></td>
                    <td ><a  class="view"  href="RequestDetails.do?id=<bean:write name='rowDataString' property='requestId'/>&menuSelected=17"><bean:write name="rowDataString" property="orderName"/></a></td>
                    <td  ><span class="black_ar"><bean:write name="rowDataString" property="distributionProtocol"/></span></td>
					<td ><bean:write name="rowDataString" property="requestedBy"/></td>
                    <td ><bean:write name="rowDataString" property="requestedDate"/></td>
                    <td ><bean:write name="rowDataString" property="status"/></td>
            </tr>
			<% count = count+1;%>
				  </logic:iterate>	
        </table></td>
      </tr>
	  <tr>
	  <td class="bottomtd"></td>
	  </tr>
    </table></td>
  </tr>
</table>
  </html:form>   
</body>
</html>
