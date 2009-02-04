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
<script src="jss/ajax.js"></script>	   
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

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
			
//	formName = "DistributionSubmit.do";
	String signedConsentDate = "";
	String selectProperty="";
	
%>
<head>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>

	<%if(pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
	{
		if(request.getAttribute(Constants.SPECIMEN_ID)!= null)
		{
			String spId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			String nodeId = "Specimen_"+spId;
		%>
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');								
		</script>
	<%}}%>

<script language="JavaScript"><!--
		function onSpecimenIdChange(element)
		{	
			document.forms[0].idChange.value="true";
			setFormAction("<%=Constants.DISTRIBUTION_ACTION%>&operation="+"<%=operation%>&specimenIdKey="+element.name);
			document.forms[0].submit();
		}
	

		//Consent tracking 
		var validateBarcodeLable="<%=Constants.VALID%>";
		var noOfRows=0;
		var iCount=0;
		var barcodeLableObject="";
		var distrinutionOn=2;
		//This function will be called on clicking ViewAll
		function showAllSpecimen()
		{
			if(barcodeLableObject.value==null||barcodeLableObject.value=="")
			{
				return;
			}
			else if(!(validateBarcodeLable=="<%=Constants.INVALID%>")&&noOfRows>0)
			{
				var barcodeLableValue="";
				var verifiedRows="";
				var iCount=0;
				var addMore =document.getElementById("addMore");
				var tableRows = addMore.rows;
				noOfRows=tableRows.length;
				for(i=0;i<noOfRows;i++)
				{
					var verificationStatusKey="value(DistributedItem:"+(i+1)+"_verificationKey)";
					var status=document.getElementById(verificationStatusKey);
					barcodeLableStatus=status.value;
					if(barcodeLableStatus=="<%=Constants.VIEW_CONSENTS%>"||barcodeLableStatus=="<%=Constants.VERIFIED%>")
					{
						if(distrinutionOn==1)
						{
							var barcodeKey="value(DistributedItem:"+(i+1)+"_Specimen_barcode)";
							var barcodelabel=document.getElementById(barcodeKey);
							barcodeLableValue=barcodeLableValue+barcodelabel.value;
							if(i!=noOfRows)
							{
								barcodeLableValue=barcodeLableValue+"|";
							}
						}
						else
						{
							var LableKey="value(DistributedItem:"+(i+1)+"_Specimen_label)";
							var barcodelabel=document.getElementById(LableKey);
							barcodeLableValue=barcodeLableValue+barcodelabel.value;
							if(i!=noOfRows)
							{
								barcodeLableValue=barcodeLableValue+"|";
							}
						}
						if(barcodeLableStatus=="<%=Constants.VERIFIED%>")
						{
							verifiedRows=verifiedRows+(i-iCount)+",";
						}
					}
					else
					{
						iCount=iCount+1;
					}
				}
				if(noOfRows==iCount)
				{
					alert("No consents available");
				}
				else
				{
					var url ='Distribution.do?operation=add&pageOf=pageOfDistribution&menuSelected=16&specimenConsents=yes&verifiedRows='+verifiedRows+'&noOfRows='+noOfRows+'&labelBarcode='+distrinutionOn+'&barcodelabel='
					url+=barcodeLableValue;
					window.open(url,'ConsentVerificationForm','height=300,width=800,scrollbars=1,resizable=1');
				}
			}
		}
		//Consent tracking
		
		//function to insert a row in the inner block
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].counter.value);
			val = val + 1;
			document.forms[0].counter.value = val;
			
			var sequenceNo = parseInt(document.forms[0].outerCounter.value);	
			sequenceNo = sequenceNo + 1;
			document.forms[0].outerCounter.value=sequenceNo;
			
			var sname = "";
		    var barcodeDisabled = "";
			var labelDisabled = "";
			var quantityDisabled = "";
			var consentForSpecimen=""

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
			noOfRows=rowno;
		   	var identifier = "value(DistributedItem:" + rowno +"_id)";
		   	
			//spreqno.innerHTML="" + sname;
			if (document.forms[0].distributionType[1].checked == true) {
			     // identifier = "value(SpecimenArray:" + rowno +"_id)";
			      identifier = "value(DistributedItem:" + rowno +"_SpecimenArray_id)";
				  quantVal = "1";
			}
    		var cell1 = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML="" + cell1+  sequenceNo ;


			//Second Cell
			var spreqidentifier=x.insertCell(1);
			spreqidentifier.className="formField";
			//spreqidentifier.colspan=2;
			sname="";

			var name = "value(DistributedItem:" + rowno + "_Specimen_barcode)";
			var keyValue=name;
			sname="<input type='text'   " + barcodeDisabled  +  "  name='" + name + "'   class='formField' id='" + name + "'>";
			spreqidentifier.innerHTML="" + sname;
			
			//Second Cell
			var spreqidentifier=x.insertCell(2);
			spreqidentifier.className="formField";
			//spreqidentifier.colspan=2;
			sname="";

			var name = "value(DistributedItem:" + rowno + "_Specimen_label)";
			var labelKeyValue=name;
			sname="<input type='text' " + labelDisabled  +  "  name='" + name + "'   class='formField' id='" + name + "'>";
			spreqidentifier.innerHTML="" + sname;

			
			//3rd Cell
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
			
			//4th  Cell
			var checkb=x.insertCell(4);
			checkb.className="formField";

			sname="";
			var name = "chk_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"document.forms[0].deleteValue.disabled = false\">";
			checkb.innerHTML=""+sname;	

			//Consent Tracking Module (Virender Mehta)
			//5th  Cell
			var consent=x.insertCell(5);
			consent.className="formField";
			consent.colSpan=2;
			sname="";
			var verificationStatusKey = "value(DistributedItem:"+rowno+"_verificationKey)";
			var name = "chk_"+ rowno;
			if (document.forms[0].distributionType[0].checked == true)  
			{
				var spanTag=document.createElement("span");
				var barcodelabelStatus="barcodelabel"+rowno;
				spanTag.setAttribute("id",barcodelabelStatus);
				//Create anchor Tag
				var anchorTag = document.createElement("a");
				var barcodeStatus="barcodeStatus"+rowno;
				if (document.forms[0].distributionBasedOn[0].checked == true)  
				{
					labelbarcodeKey=keyValue;
					distrinutionBasedOn=1;
				}
				else
				{
					labelbarcodeKey=labelKeyValue;
					distrinutionBasedOn=2;
				}
				

				anchorTag.setAttribute("href", "javascript:getbarCode('"+labelbarcodeKey+"','"+barcodeStatus+"','"+verificationStatusKey+"','"+distrinutionBasedOn+"','"+false+"','"+barcodelabelStatus+"');");
				anchorTag.innerHTML="View"+"<input type='hidden' name='" + verificationStatusKey + "' value='View' id='" + verificationStatusKey + "'>";
				spanTag.appendChild(anchorTag);
				consent.appendChild(spanTag);
				document.getElementById(keyValue).onblur=function(){getbarCode(keyValue,barcodeStatus,verificationStatusKey,distrinutionBasedOn,true,barcodelabelStatus)};
				document.getElementById(labelKeyValue).onblur=function(){getbarCode(labelKeyValue,barcodeStatus,verificationStatusKey,distrinutionBasedOn,true,barcodelabelStatus)};
				anchorTag.setAttribute("id",barcodeStatus);
			}
			else
			{
				//Create spanTag
				var spanTag=document.createElement("span");
				spanTag.innerHTML="<%=Constants.NOT_APPLICABLE%>";
				consent.appendChild(spanTag);

			}
			
		}
		//This function get the barcode value and prepare URL for Submit
		function getbarCode(identifier,barcodeId,verificationKey,distrinutionBasedOn,onFocusChange,barcodelabelStatus)
		{
			var barcodelabel=document.getElementById(identifier);
			barcodeLableObject=barcodelabel;

			if(barcodelabel.value==null||barcodelabel.value=="")
			{
				alert("Please enter some value");
				return;
			}
			distrinutionOn=distrinutionBasedOn;
			var verificationkeyObj=document.getElementById(verificationKey);
			var status=verificationkeyObj.value;
			var url ='Distribution.do?operation=add&pageOf=pageOfDistribution&menuSelected=16&status='+status+'&showConsents=yes&labelBarcode='+distrinutionBasedOn+'&verificationKey='+verificationKey+'&barcodelableId='+barcodeId+'&barcodelabel=';
			url+=barcodelabel.value;
			var dataToSend='showConsents=yes&labelBarcode='+distrinutionBasedOn+'&barcodelabel='+barcodelabel.value;
			ajaxCall(dataToSend, url, barcodeId, verificationKey,onFocusChange,barcodelabelStatus);
		}
		var flag=false;
		//Ajax Code Start
		function ajaxCall(dataToSend, url, barcodeId, verificationKey,onFocusChange,barcodelabelStatus)
		{
			if(flag==true)
			{
				return;
			}
			flag=true;
			var request = newXMLHTTPReq();
			request.onreadystatechange=function(){checkForConsents(request, url, barcodeId, verificationKey,onFocusChange,barcodelabelStatus)};
			//send data to ActionServlet
			//Open connection to servlet
			request.open("POST","CheckConsents.do",true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			request.send(dataToSend);
		}

		function checkForConsents(request, url, barcodeId,verificationKey,onFocusChange,barcodelabelStatus)
		{
			
			if(request.readyState == 4)
			{  
				//Response is ready
				if(request.status == 200)
				{
					var responseString = request.responseText;
					validateBarcodeLable=responseString;
					if(responseString=="<%=Constants.DISABLED%>")//disabled
					{
						alert("Specimen has been disabled");
					}
					else if(responseString=="<%=Constants.SHOW_CONSENTS%>")//ShowConsents
					{
						if(onFocusChange=="<%=Constants.FALSE%>")//false
						{
							
							window.open(url,'ConsentVerificationForm','height=300,width=800,scrollbars=1,resizable=1');
						}
					}
					else if(responseString=="<%=Constants.INVALID%>")//Invalid
					{
						
						alert("Please enter valid barcode/label");
					}
					else if(responseString=="<%=Constants.CONSENT_WAIVED%>")//Consent Waived
					{
						document.getElementById(barcodelabelStatus).innerHTML=responseString+"<input type='hidden' name='" + verificationKey + "' value='Consent Waived' id='" + verificationKey + "'/>";
					}
					else
					{
						document.getElementById(barcodelabelStatus).innerHTML=responseString+"<input type='hidden' name='" + verificationKey + "' value='No consents' id='" + verificationKey + "'/>";
					}
					flag=false;
				}
			}
		}
	//Consent Tracking Module Virender mehta	
-->
</script>
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
					<td><html:hidden property="outerCounter" /></td>
					<td><html:hidden property="id" /></td>
					<td><html:hidden property="idChange" /></td>
					<td><html:hidden property="onSubmit" /></td>
					

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
					</html:select> &nbsp; 
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_DISTRIBUTION_CP_QUERY%>">
					<html:link href="#"
						styleId="newDistributionProtocol"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=distributionProtocol&forwardTo=distribution&addNewFor=distributionProtocolId')">
						<bean:message key="buttons.addNew" />
					</html:link>
					</logic:notEqual>
					</td>
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
					</html:select> &nbsp; 
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_DISTRIBUTION_CP_QUERY%>">
					<html:link href="#" styleId="newUser"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=distributedBy&forwardTo=distribution&addNewFor=userId')">
						<bean:message key="buttons.addNew" />
					</html:link>
					</logic:notEqual>
					</td>
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
					<td class="formField">
					
					 <autocomplete:AutoCompleteTag property="toSite"
										  optionsList = "<%=request.getAttribute(Constants.TO_SITE_LIST)%>"
										  initialValue="<%=formBean.getToSite()%>"
										  styleClass="formFieldSized"
										  staticField="false"
									    />
										
					&nbsp; 
					<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_DISTRIBUTION_CP_QUERY%>">
					<html:link href="#" styleId="newSite"
						onclick="addNewAction('DistributionAddNew.do?addNewForwardTo=toSite&forwardTo=distribution&addNewFor=toSite')">
						<bean:message key="buttons.addNew" />
					</html:link>
					</logic:notEqual>
					</td>
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
			<%
				boolean flag=true;
			%>
			<table  summary="" cellpadding="3" cellspacing="0" border="0" width="95%">
				<tr>
			        <td class="formRequiredNotice" style="border-top:1px solid #5C5C5C"  width="5">*</td>
					<td class="formRequiredLabel" style="border-top:1px solid #5C5C5C" ><label for="type"> <bean:message
						key="distribution.distributionBasedOn" /> </label></td>
					<td class="formField" style="border-top:1px solid #5C5C5C"  colspan="5"><logic:iterate id="nvb"
						name="<%=Constants.DISTRIBUTION_BASED_ON%>" >
						<%NameValueBean distributionBasedOn = (NameValueBean) nvb;%>
						<html:radio property="distributionBasedOn" disabled="<%=flag%>"
							value="<%=distributionBasedOn.getValue()%>">
							<%=distributionBasedOn.getName()%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</html:radio>
						<%
							flag=false;
						%>
					</logic:iterate></td>
				</tr>

				<!--  Distributed Item begin here -->
				<tr>
					<td class="formTitle" height="20" colspan="5">
						<bean:message key="distribution.distributedItem"/>
					</td>
					<td class="formButtonField"><html:button property="addKeyValue"
						styleClass="actionButton" onclick="if(checkDistributionBasedOn() ) { insRow('addMore');disableDistributeOptions()}">
						<bean:message key="buttons.addMore" />
					</html:button></td>
					<td class="formButtonField" align="Right"><html:button
						property="deleteValue" styleClass="actionButton"
						onclick="deleteCheckedNoSubmit('addMore','Distribution.do?operation=<%=operation%>&pageOf=pageOfDistribution&status=true',document.forms[0].counter,'chk_',false);disableDistributeOptions()"
						disabled="true">
						<bean:message key="buttons.delete"/>
					</html:button></td>
				</tr>

				<tr>
					<td class="formLeftSubTitle" width="5">#</td>
					<td class="formLeftSubTitle"  >* <bean:message
						key="distribution.distributionBasedOn.barcode" /></td>
					<td class="formLeftSubTitle" >* <bean:message
						key="distribution.distributionBasedOn.label" /></td>
					<td class="formLeftSubTitle" >* <bean:message
						key="itemrecord.quantity" /></td>
					<td class="formLeftSubTitle"><label for="delete" align="center"> <bean:message
						key="addMore.delete" /> </label></td>
					<td class="formLeftSubTitle" colspan="2">
						<div style="float:right;">
						  <html:link href="#" styleId="newUser" onclick="showAllSpecimen()">
								<bean:message key="consent.viewall"/>
						  </html:link>	
						</div>
						<div>
						<label align="center">
							<bean:message key="consent.consentforspecimen"/>
						</label>
						</div>
					</td>
					
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
							//dIdentifier = "value(SpecimenArray:" + i + "_id)";
							dIdentifier = "value(DistributedItem:" + i +"_SpecimenArray_id)";
							
							keyid = "DistributedItem:" + i +"_SpecimenArray_id";
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

					//Change added for Consent Tracking
					String barcodeStatus="barcodeStatus"+i;
					String barcodelabelStatus="barcodelabel"+i;
					String verificationStatusKey = "value(DistributedItem:" + i + "_verificationKey)";

					//Change added for Consent Tracking
					
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
*/					%>
				
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
					String labelbarcodeKey="";
					String distributionBasedOn="1";
					if (bool)
						condition = "disabled='disabled'";
					if(formBean.getDistributionBasedOn().intValue() == Constants.BARCODE_BASED_DISTRIBUTION)
					{
						labelbarcodeKey=barcodeKey;
						distributionBasedOn=Constants.BARCODE_DISTRIBUTION;//"1"
					} 
					else
					{
						labelbarcodeKey=labelKey;
						distributionBasedOn=Constants.LABLE_DISTRIBUTION;//"2"
					}
					%>
						<td class="formField" width="5"><input type=checkbox
							name="<%=check %>" id="<%=check %>" <%=condition%>
							onClick="document.forms[0].deleteValue.disabled = false;">
						</td>
						<!-- Change Added for Consent Tracking -->
						<%
							if(formBean.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE)
							{
								String verificationStatus=(String)formBean.getValue("DistributedItem:" + i + "_verificationKey");
								if(verificationStatus==null||verificationStatus.equals(Constants.VIEW_CONSENTS)||verificationStatus.equals(Constants.VERIFIED))
								{
						%>
								<td class="formField" colspan="2">
									<span id="<%=barcodelabelStatus%>">													
										<a id="<%=barcodeStatus%>" href="javascript:getbarCode('<%=labelbarcodeKey%>','<%=barcodeStatus%>','<%=verificationStatusKey%>','<%=distributionBasedOn%>','false','<%=barcodelabelStatus%>')">
										<logic:notEmpty name="distributionForm" property="<%=verificationStatusKey%>">
											<bean:write name="distributionForm" property="<%=verificationStatusKey%>"/>
										</logic:notEmpty>
										<logic:empty name="distributionForm" property="<%=verificationStatusKey%>">
											<%=Constants.VIEW_CONSENTS %>
										</logic:empty>
										</a>
										<logic:notEmpty name="distributionForm" property="<%=verificationStatusKey%>">
											<html:hidden property="<%=verificationStatusKey%>" styleId="<%=verificationStatusKey%>"  value="<%=verificationStatus%>"/>
										</logic:notEmpty>
										<logic:empty name="distributionForm" property="<%=verificationStatusKey%>">
											<html:hidden property="<%=verificationStatusKey%>" styleId="<%=verificationStatusKey%>"  value="<%=Constants.VIEW_CONSENTS %>"/>
										</logic:empty>
									</span>
								</td>
							<%
								}
								else
								{
							%>
								<td class="formField" colspan="2">
								<html:hidden property="<%=verificationStatusKey%>" styleId="<%=verificationStatusKey%>"  value="<%=verificationStatus%>"/>
									<span id="<%=barcodelabelStatus%>">													
										<lable>	
											<%=verificationStatus%>
										</lable>
									</span>
								</td>	
						<%
								}
							}
							else
							{
						%>
							<td class="formField" colspan="2">
								<span id="<%=barcodelabelStatus%>">													
										<lable>	
											<%=Constants.NOT_APPLICABLE%>
										</lable>
								</span>
							</td>	
						<%
							}
						%>

						<!-- Change Added for Consent Tracking -->
					</tr>
					<script language="JavaScript">
							barcodeLableObject=document.getElementById("<%=labelKey%>");
							validateBarcodeLable="<%=Constants.VALID%>";
							noOfRows=1;
					</script>
					
					<%
					}
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