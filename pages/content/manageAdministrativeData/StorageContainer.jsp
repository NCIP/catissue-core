<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;

		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_CONTAINER_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_CONTAINER_ADD_ACTION;
            readOnlyValue = false;
        }
	
		Object obj = request.getAttribute("storageContainerForm");
		int noOfRows=1;
		Map map = null;
		String label1 = null;
		String label2 = null;
		
		if(obj != null && obj instanceof StorageContainerForm)
		{
			StorageContainerForm form = (StorageContainerForm)obj;
			map = form.getValues();
			noOfRows = form.getCounter();

			label1 = form.getOneDimensionLabel();
			label2 = form.getTwoDimensionLabel();

			if(label1 == null)
			{
				label1 = "Dimension One";
				label2 = "Dimension Two";
			}
		}
		
// ************ delete below code later  ********	
	//  --------- add new 
		//String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		//String appendingPath = "/StorageContainer.do?operation=add&pageOf=pageOfStorageContainer";
		//if (reqPath != null)
		//	appendingPath = reqPath + "|/StorageContainer.do?operation=add&pageOf=pageOfStorageContainer";
	
//	   	if(!operation.equals("add") )
	//   	{
	  // 		Object obj1 = request.getAttribute("storageContainerForm");
		//	if(obj1 != null && obj1 instanceof StorageContainerForm)
			//{
				//StorageContainerForm form1 = (StorageContainerForm)obj1;
//		   		appendingPath = "/StorageContainerSearch.do?operation=search&pageOf=pageOfStorageContainer&systemIdentifier="+form1.getSystemIdentifier() ;
	//	   		System.out.println("---------- SC JSP appendingPath -------- : "+ appendingPath);
		//   	}
//	   	}
			
		
		
		
%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		
		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].siteId.disabled = false;
				document.forms[0].parentContainerId.disabled = true;
				document.forms[0].positionDimensionOne.disabled = true;				
				document.forms[0].positionDimensionTwo.disabled = true;				
				document.forms[0].Map.disabled = true;
			}
			else
			{
				document.forms[0].parentContainerId.disabled = false;
				document.forms[0].positionDimensionOne.disabled = false;				
				document.forms[0].positionDimensionTwo.disabled = false;				
				document.forms[0].Map.disabled = false;
				document.forms[0].siteId.disabled = true;
				//window.location.reload();
			}
		}
		
		function onTypeChange(element)
		{
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isOnChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onSiteChange(element)
		{
			var list = document.getElementById('typeId');
			var type = list.options[list.selectedIndex].value;
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isOnChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		function onParentContainerChange(element)
		{
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isOnChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onContainerChange(element)
		{
		}

		function onNameClick(element)
		{
			if(element.value=="")
			{
				var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isOnChange=true";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
		}		
//  function to insert a row in the inner block

function insRow(subdivtag)
{

		var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
//	var x=document.getElementById(subdivtag).insertRow(q);
	var x=document.getElementById(subdivtag).insertRow(0);
	
	
	// First Cell
	var spreqno=x.insertCell(0);
	spreqno.className="formSerialNumberField";
	sname=(q+1);
	var identifier = "value(StorageContainerDetails:" + (q+1) +"_systemIdentifier)";
	sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
	spreqno.innerHTML="" + sname;

	//Second Cell
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	spreqtype.colSpan=2;
	sname="";
	
	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterName)";
	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        


	spreqtype.innerHTML="" + sname;

	//Third Cell
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";
	sname="";

	name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";
	sname= "";

	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";

	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        

	spreqsubtype.innerHTML="" + sname;
	
	//Fourth Cell
	var checkb=x.insertCell(3);
	checkb.className="formField";
	checkb.colSpan=2;
	sname="";
	var name = "chk_"+(q+1);
	sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')\">";
	checkb.innerHTML=""+sname;
	
}


/*
// using createelement functions
function insRow(subdivtag)
{

		var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	
	var q = r.length;
	var x=document.getElementById(subdivtag).insertRow(q);
	
	// First Cell
	var spreqno=x.insertCell(0);
	spreqno.className="formSerialNumberField";
	sname=(q+1);
	var textNode = document.createTextNode(sname);
	spreqno.appendChild(textNode);

// -------------------------------------------------
	//Second Cell
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	spreqtype.colSpan=2;
	sname="";
	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterName)";
	var txtInp = document.createElement('input');

	txtInp.setAttribute('type', 'text');
	txtInp.setAttribute('name', name);
	txtInp.setAttribute('class', 'formFieldSized10');
	txtInp.setAttribute('id', name );
//--		document.storageContainerForm.appendChild(txtInp);
		
	spreqtype.appendChild(txtInp);
// -------------------------------------------------


	//Third Cell
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";

	var name1 = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";
	var txtInp1 = document.createElement('input');

	txtInp1.setAttribute('type', 'text');
	txtInp1.setAttribute('name', name1);
	txtInp1.setAttribute('class', 'formFieldSized10');
	txtInp1.setAttribute('id', name1 );
	
//--		document.storageContainerForm.appendChild(txtInp1);
	spreqsubtype.appendChild(txtInp1);
// -------------------------------------------------

}
*/
	</script>
</head>



<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>" method="post">	

	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<!-- NEW STORAGE CONTAINER REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="510">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>	
					</td>
				</tr>
				<tr>
					<td><html:hidden property="systemIdentifier" /></td>
				</tr>
				<tr>
					<td><html:hidden property="positionInParentContainer" /></td>
				</tr>
				<tr>
					<td><html:hidden property="onSubmit" />
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<html:hidden property="isFull" />
						</logic:equal>
					</td>
				</tr>
					<tr>
						<td class="formMessage" colspan="5">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="5">
							<logic:equal name="operation" value="<%=Constants.ADD%>">
								<bean:message key="storageContainer.title"/>
							</logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="storageContainer.editTitle"/>&nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="storageContainerForm" property="systemIdentifier" />
							</logic:equal>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="type">
								<bean:message key="storageContainer.type" />
							</label>
						</td>
						<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="1" onchange="onTypeChange(this)"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<%-- html:options name="storageTypeIdList" labelName="storageTypeList" /--%>
								<html:options collection="<%=Constants.STORAGETYPELIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<html:link href="#" styleId="newStorageType" onclick="addNewAction('StorageContainerAddNew.do?addNewForwardTo=storageType&forwardTo=storageContainer&addNewFor=storageType')">
								<bean:message key="buttons.addNew" />
							</html:link>
						</td>
					</tr>
<!-- New row 1 -->
					<tr>
						<td class="formRequiredNoticeNoBottom" width="5">*</td>
						<td class="formRequiredLabelRightBorder" colspan=2>
							<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
								<label for="siteId">
									<bean:message key="storageContainer.site" />
								</label>
							</html:radio>
						</td>
						<td class="formField" colspan="2">
							<logic:equal name="storageContainerForm" property="checkedButton" value="1">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="siteId" styleClass="formFieldSized" styleId="siteId" size="1" onchange="onSiteChange(this)"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="siteId" styleClass="formFieldSized15" styleId="siteId" size="1" onchange="onSiteChange(this)" disabled="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							&nbsp;
							<html:link href="#" styleId="newSite" onclick="addNewAction('StorageContainerAddNew.do?addNewForwardTo=site&forwardTo=storageContainer&addNewFor=site')">
								<bean:message key="buttons.addNew" />
							</html:link>
						</td>							
					</tr>
<!-- New row two -->					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel" colspan=2>
							<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
								<label for="site">
									<bean:message key="storageContainer.parentContainer" />
								</label>
							</html:radio>							
						</td>
						<td class="formField" colspan="2">
	 						<logic:equal name="storageContainerForm" property="checkedButton" value="1">	
	 							&nbsp;<bean:message key="storageContainer.parentID" />						
				     			<html:text styleClass="formFieldSized3" maxlength="10" styleId="parentContainerId" property="parentContainerId" disabled = "true" onchange="onParentContainerChange(this)"/>
				     			&nbsp;<bean:message key="storageContainer.positionOne" />
				     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionOne" property="positionDimensionOne" disabled = "true"/>
				     			&nbsp;<bean:message key="storageContainer.positionTwo" />
				     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionTwo" property="positionDimensionTwo" disabled = "true"/>
							&nbsp;
							<html:button property="mapButton" styleClass="actionButton" styleId="Map" 
								onclick="StorageMapWindow('ShowFramedPage.do?pageOf=pageOfStorageLocation&amp;storageType=','name','810','320','yes');return false" disabled="true" >
								<bean:message key="buttons.map"/>
							</html:button>

				        	</logic:equal>
							
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
							&nbsp;<bean:message key="storageContainer.parentID" />													
			     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="parentContainerId" property="parentContainerId" onchange="onParentContainerChange(this)"/>
			     			&nbsp;<bean:message key="storageContainer.positionOne" />
			     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionOne" property="positionDimensionOne" />
			     			&nbsp;<bean:message key="storageContainer.positionTwo" />
			     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionTwo" property="positionDimensionTwo" />
							&nbsp;
							<html:button property="mapButton" styleClass="actionButton" styleId="Map" 
								onclick="StorageMapWindow('ShowFramedPage.do?pageOf=pageOfStorageLocation&amp;storageType=','name','810','320','yes');return false" >
								<bean:message key="buttons.map"/>
							</html:button>
							
							</logic:equal>
						
						</td>							
					</tr>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="containerName">
								<bean:message key="storageContainer.containerName" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="startNumber" property="containerName"/>
						</td>
					</tr>
					
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="noOfContainers">
								<bean:message key="storageContainer.noOfContainers" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10"  maxlength="10" size="30" styleId="noOfContainers" property="noOfContainers" readonly="<%=readOnlyValue%>" onchange="onContainerChange(this)"/>
						</td>
					</tr>
					</logic:notEqual>
					
					<!-- added by vaishali and have to removed it on 21st June 2006 2.18 pm
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="startNumber">
								<bean:message key="storageContainer.startNumber" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="startNumber" property="startNumber"/>
						</td>
					</tr>
					<!-- added finish -->
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="barcode">
								<bean:message key="storageContainer.barcode" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="barcode" property="barcode"/>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="defaultTemperature">
								<bean:message key="storageContainer.temperature" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10"  maxlength="10" size="30" styleId="defaultTemperature" property="defaultTemperature"/>
							°C
						</td>
					</tr>
<%-- MD : Code for isContainerfull
     Bug id 1007
 --%>			
 					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="activityStatus">
								<bean:message key="storageContainer.isContainerFull" />
							</label>
						</td>
						<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="isFull" styleClass="formFieldSized10" styleId="isFull" size="1" 
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.IS_CONTAINER_FULL_LIST%>" labelName="<%=Constants.IS_CONTAINER_FULL_LIST%>" />
							</html:select>
						</td>
					</tr>
					</logic:equal>

					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="activityStatus">
								<bean:message key="site.activityStatus" />
							</label>
						</td>
						<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
					<!-- newly added by vaishali on 22nd June 2006 1.56 pm -->
										<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="collection_protocol_id">
								<bean:message key="storageContainer.collectionProtocolTitle" />
							</label>
						</td>
						<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="4"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" multiple="true" >
								<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							
						</td>
					</tr>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="holds">
								<bean:message key="storageContainer.holds" />
							</label>
						</td>
						<td class="formField" colspan="2">

							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
						</td>
					</tr>
					
					<!-- added finish -->
					<tr>
						<td class="formTitle" colspan="5">
							<label for="capacity">
								<bean:message key="storageContainer.capacity" />
							</label>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							&nbsp;<%=label1%>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							&nbsp;
							<%
								if(label2 != null || !(label2.equals("")))
								{
							%>
							<%=label2%>
							<%
								}
							%>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="10"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
						</td>
					</tr>
						<!-- vaishali's comment '
					<tr>
						<td class="formTitle" colspan="5" nowrap>
							<label for="details">
								<bean:message key="storageContainer.details" />
							</label>
						</td>
					<td class="formTitle" align="Right">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
						<html:hidden property="counter"/>
						</td>
						<td class="formTitle" align="Right">
							<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addMore','StorageContainer.do?operation=<%=operation%>&pageOf=pageOfStorageContainer&status=true',document.forms[0].counter,'chk_',false,document.forms[0].deleteValue)"  disabled="true">
								<bean:message key="buttons.delete"/>
							</html:button>
						</td> vaishali's comment end'
					</tr>-->
					<!-- vaishali's coment				
					<tr>
						<td class="formLeftSubTableTitle" width="5">#</td>
						<td class="formRightSubTableTitle" colspan="2">
							<label for="key">
								<bean:message key="storageContainer.key" />
							</label>
						</td>
						<td class="formRightSubTableTitle">
							<label for="value">
								<bean:message key="storageContainer.value" />
							</label>
						</td>
						<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
					</tr> vaishali's comment end'-->
					<!-- vaishali's comment'
					<tbody id="addMore">
						<%
						
							for(int rowCount=noOfRows;rowCount>=1;rowCount--)
							{
								String keyName = "value(StorageContainerDetails:" + rowCount +"_parameterName)";
								String valueName = "value(StorageContainerDetails:" + rowCount +"_parameterValue)";
								String identifier = "value(StorageContainerDetails:" + rowCount +"_systemIdentifier)";
								String check = "chk_"+rowCount;
						%>
					<tr>
						<td class="formSerialNumberField" width="5"><%=rowCount%>.
							<html:hidden property="<%=identifier%>" />
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="<%=keyName%>" property="<%=keyName%>"/>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="<%=valueName%>" property="<%=valueName%>"/>
						</td>
						<%
							String key = "StorageContainerDetails:" + rowCount +"_systemIdentifier";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')">		
						</td>
						
					</tr>
						<%
							} // for
						%>

					</tbody> vaishali's comment end-->
				<%--	</logic:equal> --%>
				<!--	<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="collection_protocol_id">
								<bean:message key="storageContainer.collectionProtocolTitle" />
							</label>
						</td>
						<td class="formField" colspan="2">

							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="4"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" multiple="true">
								<%-- html:options name="storageTypeIdList" labelName="storageTypeList" /--%>
								<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="holds">
								<bean:message key="storageContainer.holds" />
							</label>
						</td>
						<td class="formField" colspan="2">

							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
						</td>
					</tr>-->
				
				</table>
				
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="500">	
					<tr>
						<td align="right">
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
								<%
						   			String action = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
						   		%>
						   			<html:button styleClass="actionButton" property="submitPage" onclick="<%=action%>">
						   				<bean:message key="buttons.submit"/>
						   			</html:button>
						   		</td>
								
							</tr>
						</table>
						<!-- action buttons end -->
						</td>
					</tr>
					
			</table>

			<!-- /td-->
		</tr>

		<!-- NEW STORAGE CONTAINER REGISTRATION ends-->
	</table>
</html:form> 