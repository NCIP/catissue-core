<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

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
			<td colspan="7" width="100%"></td>
			<%@ include file="/pages/content/orderingsystem/OrderViewGridPage.jsp" %>
            
			</tr>
            
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
