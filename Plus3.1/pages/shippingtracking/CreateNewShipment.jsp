<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.util.shippingtracking.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><tiles:getAsString name="title" ignore="true" /></title>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" media="screen"/> 
<link href="css/shippingtracking/addl_s_t.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
function showHide(elementid){
if (document.getElementById(elementid).style.display == 'none'){
document.getElementById(elementid).style.display = '';
} else {
document.getElementById(elementid).style.display = 'none';
}
}
//-->
</SCRIPT>

<%
	Object obj=request.getAttribute("shipmentForm");
	String shipmentDate="";
	if(obj instanceof ShipmentForm)
	{
		shipmentDate=((ShipmentForm)obj).getSendDate();
	}
%>
</head>
<body>

<table width="100%">
	<tr>
		<td width="100%">
			<html:errors/>
			<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer" bundle="msg.shippingtracking">
				<%=messageKey%>
			</html:messages>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<html:form action="/CreateNewShipment">
		
		<c:set var="operation" value="${requestScope.operation}"/>
		<jsp:useBean id="operation" type="java.lang.String"/>
		
		<html:hidden property="operation" styleId="operation" value="<%=operation%>"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0">
		      <tr>
		        <td colspan="2" class="toptd"></td>
		      </tr>
		      <tr>
		        <td colspan="2" class="tablepadding"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="whitetable_bg">
		          <tr>
		            <td height="18" colspan="2" align="left"><table width="100%" border="0" cellpadding="3" cellspacing="0">
		                <tr>
		                  <td><span class=" grey_ar_s">&nbsp;<img src="images/shippingtracking/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />&nbsp; <bean:message key="requiredfield.message" bundle="msg.shippingtracking"/></span></td>
		                </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="25" colspan="2" align="left" valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="createNewShipment" bundle="msg.shippingtracking"/></span></td>
		          </tr>
		          <tr>
		          	<td colspan="2">
		          <!-- include shipemnt details jsp here-->
		          		<%@ include file="/pages/shippingtracking/ShipmentDetails.jsp" %>
		          	</td>
		          </tr>  
		         </table></td>
		          </tr>
		          <tr>
		          	<td colspan="2">
		          <!-- import contents jsps -->
		          		<%@ include file="/pages/shippingtracking/NewShipmentContents.jsp" %>
		          	</td>
		          </tr>
		          <tr>
		            <td colspan="2" align="left">&nbsp;</td>
		          </tr>
		          <tr >
		            <td height="30" colspan="2" class="buttonbg">
		            	<html:submit styleClass="blue_ar_b" accesskey="A"><bean:message key="buttons.create" bundle="msg.shippingtracking"/></html:submit>
		            </td>
		          </tr>
		        </table>        
		        </td>
		      </tr>
		    </table></td>
		  </tr>
		</table>
		</html:form>
		</td>
	</tr>
</table>
</body>
</html>
