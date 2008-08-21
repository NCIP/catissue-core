<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DefineArrayForm"%>
	
<%DefineArrayForm form = (DefineArrayForm)request.getAttribute("DefineArrayForm");%>


<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script>

<%String typeOf=request.getAttribute("typeOf").toString();%>
function onTypeChange()
{

	var request = newXMLHTTPReq();
	var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
		
	//no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
	request.onreadystatechange = handlerFunction;
	var action = "arrayType="+document.getElementById("arraytype").value;
	
	//Open connection to servlet
	request.open("POST","DefineArray.do",true);	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	//send data to ActionServlet
	request.send(action);

}

function onResponseUpdate(dimensionsText) 
{
	 var temp=new Array();
	 temp=dimensionsText.split(":");

     document.DefineArray.dimenmsionX.value = temp[0];
	 document.DefineArray.dimenmsionY.value = temp[1];
	 document.DefineArray.arrayClass.value=temp[2];
}
//function for cancelling the define array
function onCancel()
{
	var typeOf='<%=typeOf%>';
	var action="";
	if(typeOf=='<%=Constants.SPECIMEN_ORDER_FORM_TYPE%>')
		action="<%=Constants.ACTION_ORDER_SPECIMEN%>";
	
	if(typeOf=='<%=Constants.ARRAY_ORDER_FORM_TYPE%>')
		action="<%=Constants.ACTION_ORDER_BIOSPECIMENARRAY%>";
	
	if(typeOf=='<%=Constants.PATHOLOGYCASE_ORDER_FORM_TYPE%>')
		action="<%=Constants.ACTION_ORDER_PATHOLOGYCASE%>";
		
	document.DefineArray.action = action;		
	document.DefineArray.submit();

}

function onCreate()
{
	var typeOf='<%=typeOf%>';
	var action="";
	if(typeOf=='<%=Constants.SPECIMEN_ORDER_FORM_TYPE%>')
	action="<%=Constants.ACTION_DEFINE_ARRAY%>"+"?typeOf="+"<%=Constants.SPECIMEN_ORDER_FORM_TYPE%>";
	
	if(typeOf=='<%=Constants.ARRAY_ORDER_FORM_TYPE%>')
	action="<%=Constants.ACTION_DEFINE_ARRAY%>"+"?typeOf="+"<%=Constants.ARRAY_ORDER_FORM_TYPE%>";
	
	if(typeOf=='<%=Constants.PATHOLOGYCASE_ORDER_FORM_TYPE%>')
		action="<%=Constants.ACTION_DEFINE_ARRAY%>"+"?typeOf="+"<%=Constants.PATHOLOGYCASE_ORDER_FORM_TYPE%>";
		
	document.DefineArray.action = action;		
	document.DefineArray.submit();

}

</script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>


<%@ include file="/pages/content/common/ActionErrors.jsp" %>   
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">

<html:form action="/DefineArraySubmit.do" type="edu.wustl.catissuecore.actionForm.DefineArrayForm"
			name="DefineArray" >

<html:hidden property="arrayClass" value=""/>
<tr>	
	<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0">
			<tr>
				<td colspan='4'  class="formMessage">	
					<bean:message key="requiredfield.message"/>
				</td>
			<tr>
				<td class="formTitle" height="20" colspan="4">
					<bean:message key="orderingsystem.label.defineArray"/>
				</td>
			</tr>
			
			<!-- Get Array Name-->
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel" width="200">
					<bean:message key="orderingsystem.label.defineArrayName"/>
				</td>
				<td class="formField" colspan="2">
					<html:text styleClass="formFieldSized" maxlength="50"  size="30"  styleId="arrayName" property="arrayName"/>
				</td>
			</tr>
			
			<!-- Get Array Type-->
			<tr>
				<td class="formRequiredNotice" width="5">*</td>
				<td class="formRequiredLabel" width="200">
					<bean:message key="orderingsystem.label.defineArrayType"/>
				</td>
				<td class="formField" colspan="2">
					<html:select property="arraytype" styleClass="formFieldSized"
					styleId="arraytype" size="1"
					onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="onTypeChange()">
					<html:options collection="<%=Constants.SPECIMEN_ARRAY_TYPE_LIST%>"
					labelProperty="name" property="value" />
					</html:select>
				</td>
			</tr>
			
			<!-- Get Dimensions-->	
			<tr>
				<td class="formRequiredNotice" width="5">&nbsp;</td>
				
				<td class="formLabel" width="200">
					<bean:message key="orderingsystem.label.defineArrayDimensions"/>
				</td>
				<td class="formField" width="50">
					<html:text styleClass="formFieldVerySmallSized" maxlength="10"  size="30" styleId="dimenmsionX" property="dimenmsionX" value="<%=form.getDimenmsionX()%>" readonly="true"/>
				</td>
				<td class="formField" width="50">
					<html:text styleClass="formFieldVerySmallSized" maxlength="10"  size="30" styleId="dimenmsionY" property="dimenmsionY" value="<%=form.getDimenmsionY()%>" readonly="true"/>
				</td>
				
			</tr>

					
			<tr>
				<td align="right" colspan="3">
				<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:button styleClass="actionButton" property="orderButton" onclick="onCreate()">
								<bean:message  key="orderingsystem.button.create" />
								</html:button>
							</td>

							<td>
								<html:button styleClass="actionButton" property="cancelButton" onclick="onCancel()">
								<bean:message  key="orderingsystem.button.cancel"/>
								</html:button>
							</td>
		
						</tr>
					</table>
						
						<!-- action buttons end -->
				</td>
			</tr>

					
					
					
		</table>
	</td>
</tr>
</table>
		
</html:form>
 