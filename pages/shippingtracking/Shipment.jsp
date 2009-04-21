<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm"%>
<%@ page import="edu.wustl.common.util.Utility"%>

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

function openInEdit(id)
{
	document.forms[0].action="/SNTSearchObject.do?pageOf=pageOfShipment&id="+id;
	document.forms[0].submit();
}

function createShipment()
{
	document.forms[0].action="CreateNewShipment.do?aliasName=Shipment";
	document.forms[0].submit();
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
				<td class="td_color_bfdcf3">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_table_head"><span class="wh_ar_b"><bean:message
										key="shipment.name" bundle="msg.shippingtracking"/></span></td>
									<td align="right"><img
										src="images/uIEnhancementImages/table_title_corner2.gif"
										alt="Page Title" width="31" height="24" /></td>
								</tr>
							</table>
					</td>
	</tr>
	<tr>
		<td width="100%">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<c:set var="operation" value="${requestScope.operation}"/>
		<jsp:useBean id="operation" type="java.lang.String"/>
		
		<c:set var="formAction" value="/CreateNewShipment"/>
		<jsp:useBean id="formAction" type="java.lang.String"/>
		
		<c:if test="${operation=='add'}">
			<c:set var="formAction" value="/CreateNewShipment"/>
		</c:if>
		
		<c:if test="${operation=='edit'}">
			<c:set var="formAction" value="/EditShipment"/>
		</c:if>
	
		<html:form action="<%=formAction%>">
		
		<html:hidden property="operation" styleId="operation" value="<%=operation%>"/>
		<html:hidden property="id" styleId="id"/>
		<html:hidden property="activityStatus" styleId="activityStatus"/>
		<html:hidden property="createdDate" styleId="createdDate"/>
		<html:hidden property="shipmentRequestId" styleId="shipmentRequestId"/>
		
		<c:set var="noOfSpecimens" value="${shipmentForm.specimenCounter}"/>
		<c:set var="noOfContainers" value="${shipmentForm.containerCounter}"/>  
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0">
		      <tr>
		        <td colspan="2" class="toptd"></td>
		      </tr>
		      <tr>
		        <td colspan="2" class="tablepadding"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="whitetable_bg">
		          <tr>
		            <td height="25" colspan="2" align="left" valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
			            	<c:if test="${operation=='add'}">
				            	<bean:message key="createNewShipment" bundle="msg.shippingtracking"/>
							</c:if>
							
							<c:if test="${operation=='edit'}">
								<bean:message key="edit.shipment" bundle="msg.shippingtracking"/>
							</c:if>
		            	</span>
		            </td>
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
		          		<%@ include file="/pages/shippingtracking/ShipmentContents.jsp" %>
		          	</td>
		          </tr>
		          <tr>
		            <td colspan="2" align="left">&nbsp;</td>
		          </tr>
		          <tr >
		            <td height="30" colspan="2" class="buttonbg">
			            	<c:if test="${operation=='add'}">
				            	<html:button property="submitShipment" styleId="submitShipment" styleClass="blue_ar_b" accesskey="A" onclick="createShipment()"><bean:message key="buttons.create"/></html:button>
							</c:if>
							<c:if test="${operation=='createShipment'}">
				            	<html:button property="submitShipment" styleId="submitShipment" styleClass="blue_ar_b" accesskey="A" onclick="createShipment()"><bean:message key="buttons.create"/></html:button>
							</c:if>
							<c:if test="${operation=='edit'}">
								<html:button property="submitShipment" styleId="submitShipment" styleClass="blue_ar_b" accesskey="A" onclick="createShipment()"><bean:message key="buttons.edit"/></html:button>
							</c:if>
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
