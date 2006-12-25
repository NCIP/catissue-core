<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionForm"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<%!private String changeUnit(String specimenType, String subType) {
		if (specimenType == null)
			return "";
		if (specimenType.equals("Fluid"))
			return Constants.UNIT_ML;
		else if (specimenType.equals("Tissue")) {
			if (subType.equals(Constants.FROZEN_TISSUE_SLIDE)
					|| subType.equals(Constants.FIXED_TISSUE_BLOCK)
					|| subType.equals(Constants.FROZEN_TISSUE_BLOCK)
					|| subType.equals(Constants.NOT_SPECIFIED)
					|| subType.equals(Constants.FIXED_TISSUE_SLIDE))
				return Constants.UNIT_CN;
			else if (subType.equals(Constants.MICRODISSECTED))
				return Constants.UNIT_CL;
			else
				return Constants.UNIT_GM;
		} else if (specimenType.equals("Cell"))
			return Constants.UNIT_CC;
		else if (specimenType.equals("Molecular"))
			return Constants.UNIT_MG;
		else
			return " ";

	}
	
	String formName;

	%>
	<%String pageOf = (String) request.getAttribute(Constants.PAGEOF);%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp"%>
<%List specimenIdList = (List) request
					.getAttribute(Constants.SPECIMEN_ID_LIST);
			DistributionForm formBean = (DistributionForm) request
					.getAttribute("distributionForm");
			String operation = (String) request
					.getAttribute(Constants.OPERATION);
			String submittedFor = (String) request
					.getAttribute(Constants.SUBMITTED_FOR);
			boolean readOnlyValue = false, readOnlyForAll = false;
			if (operation.equals(Constants.EDIT)) {
				formName = Constants.DISTRIBUTION_EDIT_ACTION;
				if(pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
				{
					formName = Constants.CP_QUERY_DISTRIBUTION_EDIT_ACTION + "?pageOf="+pageOf;
				}

				readOnlyValue = true;
			} else {
			formName = Constants.DISTRIBUTION_ADD_ACTION;
			if(pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
			{
				formName = Constants.CP_QUERY_DISTRIBUTION_ADD_ACTION + "?pageOf="+pageOf;
			}

				readOnlyValue = false;
			}
			
//			formName = "DistributionSubmit.do";
%>
<head>
	<%if(pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
	{
		if(request.getAttribute(Constants.SPECIMEN_ID)!= null)
		{
			String spId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			String nodeId = "Specimen_"+spId;
		%>
		<script language="javascript">
			var cpId = window.parent.frames[0].document.getElementById("cpId").value;
			var participantId = window.parent.frames[0].document.getElementById("participantId").value;
			window.parent.frames[1].location="showTree.do?<%=Constants.CP_SEARCH_CP_ID%>="+cpId+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+participantId+"&nodeId=<%=nodeId%>";
			
		</script>
	<%}}%>

<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript"><!--
		function onSpecimenIdChange(element)
		{	
			document.forms[0].idChange.value="true";
			setFormAction("<%=Constants.DISTRIBUTION_ACTION%>&operation="+"<%=operation%>&specimenIdKey="+element.name);
			document.forms[0].submit();
		}
		//function to insert a row in the inner block
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].counter.value);
			val = val + 1;
			document.forms[0].counter.value = val;
			var sname = "";
		    var barcodeDisabled = "";
			var labelDisabled = "";
			var quantityDisabled = "";

           if (document.forms[0].distributionBasedOn[0].checked == true)  {
		             labelDisabled = "disabled";
		   }

             if (document.forms[0].distributionBasedOn[1].checked == true)  {
		             barcodeDisabled = "disabled";
		   }
		   if (document.forms[0].distributionType[1].checked == true) {
				quantityDisabled="disabled";
			}


			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(q);
			var quantVal = "";
			


            
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			var rowno=(q+1);
		    var identifier = "value(DistributedItem:" + rowno +"_id)";

	
			//spreqno.innerHTML="" + sname;
			if (document.forms[0].distributionType[1].checked == true) {
			      identifier = "value(SpecimenArray:" + rowno +"_id)";
				  quantVal = "1";

			}

    		var cell1 = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";


			
			spreqno.innerHTML="" + cell1+  rowno ;


			//Second Cell
			var spreqidentifier=x.insertCell(1);
			spreqidentifier.className="formField";
			//spreqidentifier.colspan=2;
			sname="";

			var name = "value(DistributedItem:" + rowno + "_Specimen_barcode)";
			sname="<input type='text'   " + barcodeDisabled  +  "  name='" + name + "'   class='formField' id='" + name + "'>";
			spreqidentifier.innerHTML="" + sname;
			
			//Second Cell
			var spreqidentifier=x.insertCell(2);
			spreqidentifier.className="formField";
			//spreqidentifier.colspan=2;
			sname="";

			var name = "value(DistributedItem:" + rowno + "_Specimen_label)";
			sname="<input type='text' " + labelDisabled  +  "  name='" + name + "'   class='formField' id='" + name + "'>";
			spreqidentifier.innerHTML="" + sname;

			
			//Ninth Cell
			var spreqquantity=x.insertCell(3);
			spreqquantity.className="formField";
			//spreqquantity.colspan=2;
			sname="";
		
			name = "value(DistributedItem:" + rowno + "_quantity)";
			sname= "";
			sname="<input type='text' " + quantityDisabled + " name='" + name + "' size='30' maxlength='10' class='formFieldSmallSized3' id='" + name + "' value=" + quantVal + ">";
			
			var previousQuantity = "value(DistributedItem:"+rowno+"_previousQuantity)";
			sname = sname + "<input type='hidden' name='" + previousQuantity + "' value='' id='" + previousQuantity + "'>";
			spreqquantity.innerHTML="" + sname;
			
			//Tenth  Cell
			var checkb=x.insertCell(4);
			checkb.className="formField";

			sname="";
			var name = "chk_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"document.forms[0].deleteValue.disabled = false\">";
			checkb.innerHTML=""+sname;	
		}
	
		--></script>
</head>


<%
			String currentDistributionDate = "";
			Object obj = request.getAttribute("distributionForm");
			int noOfRows = 1;
			Map map = null;

			if (obj != null && obj instanceof DistributionForm) {
				DistributionForm form = (DistributionForm) obj;
				noOfRows = form.getCounter();
				map = form.getValues();
				currentDistributionDate = form.getDateOfEvent();

				if (currentDistributionDate == null)
					currentDistributionDate = "";
			}

			String reqPath = (String) request.getAttribute(Constants.REQ_PATH);
			String appendingPath = "/Distribution.do?operation=add&pageOf=pageOfDistribution";
			if (reqPath != null)
				appendingPath = reqPath
						+ "|/Distribution.do?operation=add&pageOf=pageOfDistribution";

			if (!operation.equals("add")) {
				Object obj1 = request.getAttribute("distributionForm");
				if (obj1 != null && obj1 instanceof DistributionForm) {
					DistributionForm form1 = (DistributionForm) obj1;
					appendingPath = "/DistributionSearch.do?operation=search&pageOf=pageOfDistribution&id="
							+ form1.getId();
				}
			}
			String reportFormName = new String();
			String reportChangeAction = new String();

			%>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header"
	footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:form action="DistributionSubmit.do">
	<table  summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="100%">

		<!-- NEW distribution REGISTRATION BEGINS-->
		<tr>
			<td>

			<table  summary="" cellpadding="3" cellspacing="0" border="0"
				width="100%">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /> <html:hidden
						property="submittedFor" value="<%=submittedFor%>" /></td>
					<td><html:hidden property="counter" /></td>
					<td><html:hidden property="id" /></td>
					<td><html:hidden property="idChange" /></td>
					<td><html:hidden property="onSubmit" /></td>
					<td><html:hidden property="pageOf" /></td>

				</tr>

				<tr>
					<td class="formMessage" colspan="3">* indicates a required field</td>
				</tr>

				<tr>
					<td class="formTitle" height="20" colspan="3"><logic:equal
						name="operation" value="<%=Constants.ADD%>">
						<bean:message key="distribution.addTitle" />
					</logic:equal> <logic:equal name="operation"
						value="<%=Constants.EDIT%>">
						<bean:message key="distribution.editTitle" />&nbsp;<bean:message
							key="for.identifier" />&nbsp;<bean:write name="distributionForm"
							property="id" />
					</logic:equal></td>
				</tr>
				<!-- Speciemen/specimen array -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="type"> <bean:message
							key="distribution.distributionType" /> 
						</label>
					</td>
					<td class="formField">
						<logic:iterate id="nvb"
						name="<%=Constants.DISTRIBUTION_TYPE_LIST%>">
						<%NameValueBean distributionType = (NameValueBean) nvb;%>
						<html:radio property="distributionType"
							value="<%=distributionType.getValue()%>">
							<%=distributionType.getName()%>
						</html:radio>
					</logic:iterate>
					</td>
				</tr>

				<!-- Name of the distribution -->
				<!-- Distribution Protocol Id -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel"><label for="type"> <bean:message
						key="distribution.protocol" /> </label></td>
					<td class="formField"><!-- Mandar : 434 : for tooltip --> <html:select
						property="distributionProtocolId" styleClass="formFieldSized"
						styleId="distributionProtocolId" size="1"
						onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.DISTRIBUTIONPROTOCOLLIST%>"
							labelProperty="name" property="value" />
					</html:select> &nbsp; <html:link href="#"
						styleId="newDistributionProtocol"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=distributionProtocol&forwardTo=distribution&addNewFor=distributionProtocolId')">
						<bean:message key="buttons.addNew" />
					</html:link></td>
				</tr>

				<!-- User -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel"><label for="type"> <bean:message
						key="eventparameters.distributed.by" /> </label></td>
					<td class="formField"><!-- Mandar : 434 : for tooltip --> <html:select
						property="userId" styleClass="formFieldSized" styleId="userId"
						size="1" onmouseover="showTip(this.id)"
						onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.USERLIST%>"
							labelProperty="name" property="value" />
					</html:select> &nbsp; <html:link href="#" styleId="newUser"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=distributedBy&forwardTo=distribution&addNewFor=userId')">
						<bean:message key="buttons.addNew" />
					</html:link></td>
				</tr>

				<!-- date -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel"><label for="type"> <bean:message
						key="eventparameters.dateofevent" /> </label></td>
					<td class="formField"><!-- 				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
				<html:text styleClass="formDateSized15" maxlength="10"  size="15" styleId="dateOfEvent" property="dateOfEvent" />
				&nbsp;<bean:message key="page.dateFormat" />&nbsp;
				<a href="javascript:show_calendar('distributionForm.dateOfEvent',null,null,'MM-DD-YYYY');">
					<img src="images\calendar.gif" width=24 height=22 border=0></a>
--> <!-- Date Component BY Mandar: 18-Aug-06 --> <%if (currentDistributionDate.trim().length() > 0) {
					Integer distributionYear = new Integer(Utility
							.getYear(currentDistributionDate));
					Integer distributionMonth = new Integer(Utility
							.getMonth(currentDistributionDate));
					Integer distributionDay = new Integer(Utility
							.getDay(currentDistributionDate));
%> <ncombo:DateTimeComponent name="dateOfEvent" id="dateOfEvent"
						formName="distributionForm" month="<%= distributionMonth %>"
						year="<%= distributionYear %>" day="<%= distributionDay %>"
						value="<%=currentDistributionDate %>" styleClass="formDateSized10" />
					<%} else {

				%> <ncombo:DateTimeComponent name="dateOfEvent" id="dateOfEvent"
						formName="distributionForm" styleClass="formDateSized10" />
					<%}

				%> <bean:message key="page.dateFormat" />&nbsp;</td>
				</tr>
				
				<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.time">
					<bean:message key="eventparameters.time"/>
				</label>
			</td>
			<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				<html:select property="timeInHours" styleClass="formFieldSized5" styleId="timeInHours" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options name="<%=Constants.HOUR_LIST%>" labelName="<%=Constants.HOUR_LIST%>" />
				</html:select>&nbsp;
				<label for="eventparameters.timeinhours">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
				</label>
<!-- Mandar : 434 : for tooltip -->
				<html:select property="timeInMinutes" styleClass="formFieldSized5" styleId="timeInMinutes" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options name="<%=Constants.MINUTES_LIST%>" labelName="<%=Constants.MINUTES_LIST%>" />
				</html:select>
				<label for="eventparameters.timeinhours">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
		</tr>

				<!-- Commented By Mandar for date component -->

				<%-- fromSite
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="distribution.fromSite"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="fromSite" styleClass="formFieldSized" styleId="fromSite" size="1">
					<html:options collection="<%=Constants.FROM_SITE_LIST%>" labelProperty="name" property="value"/>
				</html:select>
				&nbsp;
				<%
					String urlToGo = "/Site.do?operation=add&pageOf=pageOfSite";
					String onClickPath = "changeUrl(this,'"+appendingPath+"')";
				%>
				<html:link page="<%=urlToGo%>" styleId="newSite" onclick="<%=onClickPath%>">
					<bean:message key="buttons.addNew" />
 				</html:link>

			</td>
		</tr>
--%>
				<!-- toSite -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel"><label for="type"> <bean:message
						key="distribution.toSite" /> </label></td>
					<td class="formField"><!-- Mandar : 434 : for tooltip --> <html:select
						property="toSite" styleClass="formFieldSized" styleId="toSite"
						size="1" onmouseover="showTip(this.id)"
						onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.TO_SITE_LIST%>"
							labelProperty="name" property="value" />
					</html:select> &nbsp; <html:link href="#" styleId="newSite"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=toSite&forwardTo=distribution&addNewFor=toSite')">
						<bean:message key="buttons.addNew" />
					</html:link></td>
				</tr>
				<!-- activitystatus -->
				<logic:equal name="<%=Constants.OPERATION%>"
					value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel"><label for="activityStatus"> <bean:message
							key="site.activityStatus" /> </label></td>
						<td class="formField"><!-- Mandar : 434 : for tooltip --> <html:select
							property="activityStatus" styleClass="formFieldSized10"
							styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>"
								labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select></td>
					</tr>
				</logic:equal>
				<!-- comments -->
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel"><label for="type"> <bean:message
						key="eventparameters.comments" /> </label></td>
					<td class="formField"><html:textarea styleClass="formFieldSized"
						styleId="comments" property="comments" /></td>
				</tr>
			</table>
			<table  cellpadding="3" cellspacing="0" border="0">
				<tr  rowspan=4>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td >&nbsp;</td>
				</tr>
			</table>
			<table  summary="" cellpadding="3" cellspacing="0" border="0"
				width="433">
						<tr>
			        <td class="formRequiredNotice" style="border-top:1px solid #5C5C5C"  width="5">*</td>
					<td class="formRequiredLabel" style="border-top:1px solid #5C5C5C" ><label for="type"> <bean:message
						key="distribution.distributionBasedOn" /> </label></td>
					<td class="formField" style="border-top:1px solid #5C5C5C"  colspan="3"><logic:iterate id="nvb"
						name="<%=Constants.DISTRIBUTION_BASED_ON%>" >
						<%NameValueBean distributionBasedOn = (NameValueBean) nvb;%>
						<html:radio property="distributionBasedOn"
							value="<%=distributionBasedOn.getValue()%>">
							<%=distributionBasedOn.getName()%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</html:radio>
					</logic:iterate></td>
				</tr>

				<!--  Distributed Item begin here -->
				<tr>
					<td class="formTitle" height="20" colspan="3"><bean:message
						key="distribution.distributedItem" /></td>
					<td class="formButtonField"><html:button property="addKeyValue"
						styleClass="actionButton" onclick="if(checkDistributionBasedOn() ) { insRow('addMore');disableDistributeOptions()}">
						<bean:message key="buttons.addMore" />
					</html:button></td>
					<td class="formButtonField" align="Right"><html:button
						property="deleteValue" styleClass="actionButton"
						onclick="rearrangeIdsForDistribution();deleteCheckedNoSubmit('addMore','Distribution.do?operation=<%=operation%>&pageOf=pageOfDistribution&status=true',document.forms[0].counter,'chk_',false);disableDistributeOptions()"
						disabled="true">
						<bean:message key="buttons.delete" />
					</html:button></td>

				</tr>
				<tr>
					<td class="formLeftSubTitle" width="5">#</td>

					<td class="formLeftSubTitle"  >* <bean:message
						key="distribution.distributionBasedOn.barcode" /></td>
					<td class="formLeftSubTitle" >* <bean:message
						key="distribution.distributionBasedOn.label" /></td>
					<td class="formLeftSubTitle">* <bean:message
						key="itemrecord.quantity" /></td>
					<td class="formLeftSubTitle"><label for="delete" align="center"> <bean:message
						key="addMore.delete" /> </label></td>
				</tr>

				<tbody id="addMore">
					<%
					boolean disableBarcode = false;
					boolean disableLabel = false;
					
					if(formBean.getDistributionBasedOn().intValue() == Constants.BARCODE_BASED_DISTRIBUTION) {
						disableLabel = true;
					} 
					
					if(formBean.getDistributionBasedOn().intValue() == Constants.LABEL_BASED_DISTRIBUTION) {
						disableBarcode = true;
					} 
					
					
					for (int i = 1; i <= noOfRows; i++) {
						String keyid = "";
						String dIdentifier = "";
						
						if(formBean.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE) {
							dIdentifier = "value(DistributedItem:" + i + "_id)";
							keyid = "DistributedItem:" + i + "_id";
						} else {
							dIdentifier = "value(SpecimenArray:" + i + "_id)";
							keyid = "SpecimenArray:" + i + "_id";
							readOnlyForAll = true;
						}
						
					
					String itemName = "value(DistributedItem:" + i
							+ "_Specimen_id)";
					String barcodeKey = "value(DistributedItem:" + i + "_Specimen_barcode)";
					String labelKey = "value(DistributedItem:" + i + "_Specimen_label)";
					
					String quantity = "value(DistributedItem:" + i
							+ "_quantity)";
					String availableQuantity = "value(DistributedItem:" + i
							+ "_availableQty)";
					String previousQuantity = "value(DistributedItem:" + i
					+ "_previousQuantity)";
					String check = "chk_" + i;


					
/*					String tissueSite = "value(DistributedItem:" + i
							+ "_tissueSite)";
					String tissueSide = "value(DistributedItem:" + i
							+ "_tissueSide)";
					String pathologicalStatus = "value(DistributedItem:" + i
							+ "_pathologicalStatus)";
					String unitSpan = "value(DistributedItem:" + i
							+ "_unitSpan)";
					String className = "value(DistributedItem:" + i
							+ "_Specimen_className)";
					String type = "value(DistributedItem:" + i
							+ "_Specimen_type)";
					String key = "DistributedItem:" + i + "_Specimen_className";
					//String unitKey = "DistributedItem:" + i + "_unit";
					//String unitProperty = "value(DistributedItem:"+i+"_unit)";
					//String fName = "onSpecimenTypeChange(this,'" + unitSpan + "','" + itemName + "','" + unitProperty + "')";
					String srKeyName = "DistributedItem:" + i + "_Specimen_id";
					//String idValue=(String)formBean.getValue(srKeyName);
					//String strUnitValue = ""+(String)formBean.getValue(unitProperty);
					String classValue = (String) formBean.getValue(key);
					key = "DistributedItem:" + i + "_Specimen_type";
					String typeValue = (String) formBean.getValue(key);
					String strUnitValue = changeUnit(classValue, typeValue);
*/					

					%>
					<tr>
					     <td class="formSerialNumberField" width="5%"><html:hidden  property="<%=dIdentifier%>" /><%=i%></td>
							<td class="formField" ><html:text styleClass="formField"
							styleId="<%=barcodeKey%>" property="<%=barcodeKey%>" disabled="<%=disableBarcode%>"
							/></td>
						<td class="formField" ><html:text styleClass="formField"
							styleId="<%=labelKey%>" property="<%=labelKey%>"
							disabled="<%=disableLabel%>"/></td>
						<td class="formField" nowrap ><html:text
							styleClass="formFieldSmallSized3" maxlength="10" size="30"
							styleId="<%=quantity%>" property="<%=quantity%>"
							disabled="<%=readOnlyForAll%>" readonly="<%=readOnlyForAll%>" />
						    </span><html:hidden
							property="<%=previousQuantity%>" /></td>
						<%
					boolean bool = Utility.isPersistedValue(map, keyid);
					String condition = "";
					if (bool)
						condition = "disabled='disabled'";

					%>
						<td class="formField" width="5"><input type=checkbox
							name="<%=check %>" id="<%=check %>" <%=condition%>
							onClick="document.forms[0].deleteValue.disabled = false;">
						</td>

					</tr>
					<%}

				%>
				</tbody>
				<!-- Distributed item End here -->
				<!-- buttons -->
				<tr>
					<td align="right" colspan="11"><!-- action buttons begins --> <%String changeAction = "setFormAction('" + formName + "');";

				%>
					<table  cellpadding="4" cellspacing="0" border="0">
						<tr>
							<%if (operation.equals(Constants.EDIT)) {
								if(formBean.getDistributionType().intValue()== Constants.SPECIMEN_DISTRIBUTION_TYPE) {
									reportFormName = Constants.DISTRIBUTION_REPORT_ACTION
									+ "?id=" + formBean.getId();
								} else {
									reportFormName = Constants.ARRAY_DISTRIBUTION_REPORT_ACTION
									+ "?id=" + formBean.getId();
								}
					reportChangeAction = "setFormAction('" + reportFormName
							+ "');";

					%>
							<td><html:submit styleClass="actionButton" value="Report"
								onclick="<%=reportChangeAction%>" /></td>
							<%}

				%>
							<!--td>
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td-->
							<td><%String action = "if( checkDistributionBasedOn() ) {rearrangeIdsForDistribution();confirmDisable('" + formName
						+ "',document.forms[0].activityStatus)}";

				%> <html:button styleClass="actionButton" property="submitPage"
								onclick="<%=action%>">
								<bean:message key="buttons.submit" />
							</html:button></td>
							<%-- td><html:reset styleClass="actionButton"/></td --%>
						</tr>
					</table>
					<!-- action buttons end --></td>
				</tr>

			</table>

			</td>
		</tr>

		<!-- NEW Distribution ends-->

	</table>
    <script>disableDistributeOptions()</script>
</html:form>