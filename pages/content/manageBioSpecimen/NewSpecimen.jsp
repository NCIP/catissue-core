<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.NewSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%
	String bhIdArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_ID_LIST);
	String bhNameArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_NAME_LIST);
	String bhTypeArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_TYPES_LIST);
	
	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
	NewSpecimenForm form = (NewSpecimenForm)request.getAttribute("newSpecimenForm");
	
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	boolean isAddNew = false;
	
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";

	String currentReceivedDate = "";
	String currentCollectionDate = "";
	if (reqPath != null)
		appendingPath = reqPath + "|/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	
	   	if(!operation.equals(Constants.ADD) )
	   	{
	   		if(form != null)
	   		{
		   		appendingPath = "/NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&systemIdentifier="+form.getSystemIdentifier() ;
		   		//System.out.println("---------- NSP JSP -------- : "+ appendingPath);
				currentReceivedDate = form.getReceivedEventDateOfEvent();
				if(currentReceivedDate == null)
					currentReceivedDate = "";

				currentCollectionDate = form.getCollectionEventdateOfEvent();
				if(currentCollectionDate == null)
					currentCollectionDate = "";

		   	}
	   	}
	
	Map map = form.getExternalIdentifier();
%>
<head>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	
	String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=false;

	if(operation.equals(Constants.EDIT))
	{
		editViewButton="buttons."+Constants.VIEW;
		formName = Constants.SPECIMEN_EDIT_ACTION;
		readOnlyValue=true;
		if(pageOf.equals(Constants.QUERY))
			formName = Constants.QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;

	}
	else
	{
		formName = Constants.SPECIMEN_ADD_ACTION;
		readOnlyValue=false;
	}


	
%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">

	
		var idArray = new Array();
		var nameArray = new Array();
		var typeArray = new Array();
		
		<%
			if(bhIdArray != null && bhTypeArray != null && bhNameArray !=null)
			{
				for(int i=0;i<bhIdArray.length;i++)
				{
		%>
					idArray[<%=i%>] = "<%=bhIdArray[i]%>";
					nameArray[<%=i%>] = "<%=bhNameArray[i]%>";
					typeArray[<%=i%>] = "<%=bhTypeArray[i]%>";
		<%
				}
			}
		%>
		
		
		

		//ADD MORE -------- EXTERNAL IDENTIFIER
		function insExIdRow(subdivtag)
		{
			var val = parseInt(document.forms[0].exIdCounter.value);
			val = val + 1;
			document.forms[0].exIdCounter.value = val;
		
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var rowno = q + 1;
			var x=document.getElementById(subdivtag).insertRow(0);
		
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			var identifier = "externalIdentifierValue(ExternalIdentifier:" + (q+1) +"_systemIdentifier)";
			var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML="" + rowno + "." + hiddenTag;
		
			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			spreqtype.colSpan=1;
			sname="";
			
			var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
			sname="<input type='text' name='" + name + "'  maxlength='50' class='formFieldSized15' id='" + name + "'>";      
		
		
			spreqtype.innerHTML="" + sname;
		
			//Third Cell
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=2;
			sname="";
		
			name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
			sname= "";
			
			sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized15' id='" + name + "'>"   
		
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_ex_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')\">";
			checkb.innerHTML=""+sname;
		}
		
		
		//ADD MORE FUNCTIONALITY FOR BIOHAZARDS
		function insBhRow(subdivtag)
		{
			var val = parseInt(document.forms[0].bhCounter.value);
			val = val + 1;
			document.forms[0].bhCounter.value = val;
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(0);
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			//var identifier = "biohazardValue(Biohazard:" + (q+1) +"_systemIdentifier)";
			//var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML= "" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "biohazardValue(Biohazard:" + (q+1) + "_type)";
		// Mandar : 434 : for tooltip 
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onchange=onBiohazardTypeSelected(this) onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%
					if(biohazardList != null && biohazardList.size() != 0)
					{
						for(int i=0;i<biohazardList.size();i++)
						{
							NameValueBean bean = (NameValueBean)biohazardList.get(i);
			%>
							sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%			}
					}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue()
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=2;
			sname="";

			name = "biohazardValue(Biohazard:" + (q+1) + "_systemIdentifier)";
			sname= "";
	//Mandar : 434 : for tooltip 		
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='bhId" + (q+1) + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";			
			sname = sname + "</select>";
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_bio_"+ (q+1);
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteBiohazard,document.forms[0].bhCounter,'chk_bio_')\">";
			checkb.innerHTML=""+sname;
		}
		
		function onCheckboxButtonClick(chkBox)
		{
			var aliquotCountTextBox  = document.getElementById("noOfAliquots");
			var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
			
			if(chkBox.checked)
			{
				aliquotCountTextBox.disabled = false;
				qtyPerAliquotTextBox.disabled = false;
				
				document.forms[0].deriveButton.disabled = true;
				//document.forms[0].eventButton.disabled = true;
				document.forms[0].scgButton.disabled = true;
			}
			else
			{
				aliquotCountTextBox.disabled = true;
				qtyPerAliquotTextBox.disabled = true;
				
				document.forms[0].deriveButton.disabled = false;
				//document.forms[0].eventButton.disabled = false;
				document.forms[0].scgButton.disabled = false;
			}
		}
		
		function onNormalSubmit()
		{
			var checked = document.forms[0].checkedButton.checked;
			var operation = document.forms[0].operation.value;
			
			if(checked)
			{
				if(operation == "Add")
				{
					setSubmittedFor('null','pageOfCreateAliquot');
					confirmDisable('NewSpecimenAdd.do',document.forms[0].activityStatus);
				}
				else
				{
					setSubmittedFor('null','pageOfCreateAliquot');
					confirmDisable('NewSpecimenEdit.do',document.forms[0].activityStatus);
				}
			}
			else
			{
				if(operation == "Add")
				{
					setSubmittedFor('null','success');
					confirmDisable('NewSpecimenAdd.do',document.forms[0].activityStatus);
				}
				else
				{
					setSubmittedFor('null','success');
					confirmDisable('NewSpecimenEdit.do',document.forms[0].activityStatus);
				}
			}
		}
	</script>
</head>

<% 
		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
			//System.out.println("form.getAvailableQuantity()--"+form.getAvailableQuantity());
			//System.out.println("form.getQuantity()--"+form.getQuantity());
	//		if(form.getUnit() != null)
	//		{
	//			unitSpecimen = form.getUnit();
	//			System.out.println("\n\n\nunitSpecimen : " +unitSpecimen);
	//		}	
	//		else
	//		{
				if(form.getClassName().equals("Tissue"))
				{
					//Mandar : 25-Apr-06 :Bug 1414
					if((form.getType()!=null) && (form.getType().equals(Constants.FROZEN_TISSUE_SLIDE)||form.getType().equals(Constants.FIXED_TISSUE_BLOCK)||form.getType().equals(Constants.FROZEN_TISSUE_BLOCK)||form.getType().equals(Constants.NOT_SPECIFIED)||form.getType().equals(Constants.FIXED_TISSUE_SLIDE)))
					{
						unitSpecimen = Constants.UNIT_CN;
					}
					else if((form.getType()!=null) && (form.getType().equals(Constants.MICRODISSECTED)))
					{
						unitSpecimen = Constants.UNIT_CL;
					}
					else 
					{
						unitSpecimen = Constants.UNIT_GM;
					}
						
				}
				if(form.getClassName().equals("Fluid"))
				{
					unitSpecimen = Constants.UNIT_ML;
				}
				if(form.getClassName().equals("Cell"))
				{
					unitSpecimen = Constants.UNIT_CC;
				}
				if(form.getClassName().equals("Molecular"))
				{
					unitSpecimen = Constants.UNIT_MG;
				}
				
		//	}
		}
		
		
		

%>


<html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">
	<%
				String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.SPECIMEN_FORWARD_TO_LIST[0][1]+"')";
				String deriveNewSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[1][1]+"')";									
				String addEventsSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[2][1]+"')";
				String addMoreSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";
				
				String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
				
				String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
				String deriveNewSubmit = deriveNewSubmitFunctionName + ","+confirmDisableFuncName;
				String addEventsSubmit = addEventsSubmitFunctionName + ","+confirmDisableFuncName;
				String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;												
	%>

	<%
	if(pageView.equals("edit"))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItemSelected">
					<bean:message key="tab.specimen.details"/>
				</td>
				<%
					String eventLinkAction = "'ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&menuSelected=15&specimenId="+form.getSystemIdentifier()+"'" ;
				%>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="<%=addEventsSubmit%>">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<%=Constants.SURGICAL_PATHOLOGY_REPORT %>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<%=Constants.CLINICAL_ANNOTATIONS %>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="2">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
	<%
	}
	%>

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">

			
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			   	
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
					<tr><td colspan="6">
						<html:errors />
						<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
							<%=messageKey%>
						</html:messages>
					</td></tr>
					<tr><td>
						<%@ include file="NewSpecimenPageButtons.jsp" %>
					</td></tr>
			 	 
				 <tr>
				 	<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value=""/>
					</td>
					<td>
						<html:hidden property="exIdCounter"/>
					</td>
					<td>
						<html:hidden property="bhCounter"/>
					</td>
					<td><html:hidden property="onSubmit"/></td>
					<td>
						<html:hidden property="systemIdentifier"/>
						<html:hidden property="positionInStorageContainer" />
						<html:hidden property="parentPresent" />
					</td>
				 </tr>

				 <tr>
					<td class="formMessage" colspan="3">* indicates a required field</td>
				 </tr>
				 <tr>
				     <td class="formTitle" height="20" colspan="6">
				     <%String title = "specimen."+pageView+".title";%>
				     	<bean:message key="<%=title%>"/>
				    	<%
							if(pageView.equals(Constants.EDIT))
							{
						%>
						     &nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="newSpecimenForm" property="systemIdentifier" />
						<%
							}
						%>
				     	
				     </td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    
					<logic:equal name="newSpecimenForm" property="parentPresent" value="false">
						<td class="formRequiredLabel">
							<label for="specimenCollectionGroupName">
								<bean:message key="specimenCollectionGroup.groupName"/>
							</label>
						</td>
						<td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
				     		<html:select property="specimenCollectionGroupId" styleClass="formFieldSized15" 
				     				styleId="specimenCollectionGroupId" size="1" 
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.SPECIMEN_COLLECTION_GROUP_LIST%>" 
									labelProperty="name" property="value"/>		
							</html:select>
							&nbsp;
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
							<html:link href="#" styleId="newUser" onclick="addNewAction('NewSpecimenAddNew.do?addNewForwardTo=specimenCollectionGroup&forwardTo=createNewSpecimen&addNewFor=specimenCollectionGroupId')">
								<bean:message key="buttons.addNew" />
							</html:link>					   
	   						</logic:notEqual>
			     <!--   		<a href="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup">
	      						<bean:message key="app.addNew" />
	   						</a> 
	   			-->			
			        	</td>
		        	</logic:equal>
		        	
		        	<logic:equal name="newSpecimenForm" property="parentPresent" value="true">
			        	<td class="formRequiredLabel">
							<label for="parentSpecimenId">
								<bean:message key="createSpecimen.parent"/>
							</label>
						</td>
						
			        	<td class="formField" colspan="4">
			        		<html:hidden property="specimenCollectionGroupId"/>
<!-- Mandar : 434 : for tooltip -->
				     		<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
							</html:select>
			        	</td>
		        	</logic:equal>
		        	
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="label">
							<bean:message key="specimen.label"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="label" property="label" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="className">
				     		<bean:message key="specimen.type"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="className" styleClass="formFieldSized15" styleId="className" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				     	<%
							String classValue = form.getClassName();
							if(operation.equals(Constants.EDIT))
							{
						%>
								<html:option value="<%=classValue%>"><%=classValue%></html:option>
						<%
							}else{
						%>
								<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						<%
							}
						%>
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="type">
				     		<bean:message key="specimen.subType"/>
				     	</label>
				    </td>				    
				    <td class="formField" colspan="4">
				    <!-- --------------------------------------- -->
				    <%
								String classValue = (String)form.getClassName();
								specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								boolean subListEnabled = false;
						
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								
								if(Constants.ALIQUOT.equals(form.getLineage()))
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(form.getType(),form.getType()));
								}
								
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								
								String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
					%>
				    <!-- --------------------------------------- -->
   <!-- Mandar : 434 : for tooltip --> 
				     	<html:select property="type" styleClass="formFieldSized15" styleId="type" size="1"
				     	  disabled="<%=subListEnabled%>"
				     	  onchange="<%=subTypeFunctionName%>" 
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	</td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSite">
							<bean:message key="specimen.tissueSite"/>
						</label>
					</td>
				     <td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="tissueSite" styleClass="formFieldSized15" styleId="tissueSite" size="1" disabled="<%=readOnlyForAll%>"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
						<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
						%>
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','250','330','no');return false">
							<img src="images\Tree.gif" border="0" width="24" height="18" title='Tissue Site Selector'>
						</a>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="tissueSide">
							<bean:message key="specimen.tissueSide"/>
						</label>
					</td>
				     <td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="tissueSide" styleClass="formFieldSized15" styleId="tissueSide" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.TISSUE_SIDE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="pathologicalStatus">
							<bean:message key="specimen.pathologicalStatus"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="pathologicalStatus" styleClass="formFieldSized15" styleId="pathologicalStatus" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<%--html:options name="pathologicalStatusList" labelName="pathologicalStatusList"/--%>
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	</td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	&nbsp;
				    </td>
				    <td class="formLabel">
						<label for="concentration">
							<bean:message key="specimen.concentration"/>
						</label>
					</td>
					<td class="formField" colspan="4">
					<%
						boolean concentrationDisabled = true;
						
						if(form.getClassName().equals("Molecular") && !Constants.ALIQUOT.equals(form.getLineage()))
							concentrationDisabled = false;
					%>
			     		<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="concentration" property="concentration" 
			     		readonly="<%=readOnlyForAll%>" disabled="<%=concentrationDisabled%>"/>
						&nbsp;<bean:message key="specimen.concentrationUnit"/>
					</td>
				 </tr>
				 <tr>
			     	<td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				    </td>
				    <td class="formRequiredLabel">
						<label for="quantity">
							<bean:message key="specimen.quantity"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
				     	<span id="unitSpan"><%=unitSpecimen%></span>
				     	<html:hidden property="unit"/>
				    </td>
				 </tr>
				 
				 <logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<!-- Available -->
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="available">
							<bean:message key="specimen.available" />
						</label>
					</td>
					<td class="formField" colspan="3">
						<html:checkbox property="available">
						</html:checkbox>
					</td>
				</tr>
				
				<!-- Available Quantity -->
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel" >
						<label for="availableQuantity">
							<bean:message key="specimen.availableQuantity" />
						</label>
					</td>
					<td class="formField" colspan="3">
						<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true" />
						<span id="unitSpan1"><%=unitSpecimen%></span>
					</td>
				</tr>
				</logic:equal>
				 
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
					   <label for="className">
					   		<bean:message key="specimen.positionInStorageContainer"/>
					   </label>
					</td>
					<%
						boolean readOnly=true;
						if(operation.equals(Constants.ADD))
							readOnly=false;
					%>
					
				<%-- n-combo-box start --%>
				<%
					Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
										
					String[] labelNames = {"ID","Pos1","Pos2"};
					labelNames = Constants.STORAGE_CONTAINER_LABEL;
					String[] attrNames = { "storageContainer", "positionDimensionOne", "positionDimensionTwo"};
					
					String[] initValues = new String[3];
					initValues[0] = form.getStorageContainer();
					initValues[1] = form.getPositionDimensionOne();
					initValues[2] = form.getPositionDimensionTwo();
					//System.out.println("NewSpecimen :: "+initValues[0]+"<>"+initValues[1]+"<>"+initValues[2]);			
					String rowNumber = "1";
					String styClass = "formFieldSized5";
					String tdStyleClass = "customFormField";
					String onChange = "onCustomListBoxChange(this)";
					
					String buttonOnClicked = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=customListBox_1_0&amp;xDimStyleId=customListBox_1_1&amp;yDimStyleId=customListBox_1_2','name','810','320','yes');return false";
					String noOfEmptyCombos = "3";
				%>
				
				<%=ScriptGenerator.getJSForOutermostDataTable()%>
				<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
				
				<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
				
				<td class="formField" colSpan="4">
						<ncombo:containermap dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange="<%=onChange%>" 
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"
											
											buttonName="mapButton" 
											value="Map"
											buttonOnClick = "<%=buttonOnClicked%>"
											formLabelStyle="formLabelBorderless"
											buttonStyleClass="actionButton" />				
				</td>
				<%-- n-combo-box end --%>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
				    	<label for="barcode">
							<bean:message key="specimen.barcode"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
						<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				    	<html:textarea styleClass="formFieldSized"  rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
					
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<!-- activitystatus -->
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="participant.activityStatus" />
						</label>
					</td>
					<td class="formField" colspan="3">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
<!-- Mandar AutoEvents start -->		
 <logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
	<tr>
					<td colspan="5" width="500" class="formTitle">Events</td>
				</tr>
				
				<tr>
					<td rowspan="2" class="formSubTitle">Received
						<html:hidden property="receivedEventSystemIdentifier" />
						<html:hidden property="receivedEventSpecimenId" />
					</td>
					<td class="formLeftSubTableTitle">* 
						<label for="type">
							<bean:message key="eventparameters.user"/> 
						</label>
					</td>
					<td class="formLeftSubTableTitle" nowrap>* 
						<label for="date">
							<bean:message key="eventparameters.dateofevent"/> 
						</label>
						&nbsp;<small><bean:message key="page.dateFormat" /></small>
					</td>
					<td class="formLeftSubTableTitle">* 
						<label for="quality">
							<bean:message key="receivedeventparameters.receivedquality"/> 
						</label>
					</td>
					<td class="formLeftSubTableTitle">
						<label for="comments">
							<bean:message key="eventparameters.comments"/> 
						</label>
					</td>
			 	</tr>

<!-- ReceivedEvent Fields -->
				 <tr>
	<!-- 1st column empty -->
<!-- 					<td class="formField">&nbsp;</td>
-->	
	<!-- User -->
				<td class="formField">
	<!-- Mandar : 434 : for tooltip -->
					<html:select property="receivedEventUserId" styleClass="formFieldSized15" styleId="receivedEventUserId" size="1"
					 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
	
	<!-- date -->
				<td class="formField" nowrap>
<%
if(currentReceivedDate.trim().length() > 0)
{
	Integer receivedYear = new Integer(Utility.getYear(currentReceivedDate ));
	Integer receivedMonth = new Integer(Utility.getMonth(currentReceivedDate ));
	Integer receivedDay = new Integer(Utility.getDay(currentReceivedDate ));
%>
<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
			  id="receivedEventDateOfEvent"
			  formName="newSpecimenForm"
			  month= "<%= receivedMonth %>"
			  year= "<%= receivedYear %>"
			  day= "<%= receivedDay %>"
			  value="<%=currentReceivedDate %>"
			  styleClass="formDateSized10"
					/>
<% 
	}
	else
	{  
 %>
<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
			  id="receivedEventDateOfEvent"
			  formName="newSpecimenForm"
			  styleClass="formDateSized10"
					/>
<% 
	} 
%> 

			</td>
	
	<!-- receivedeventparameters.receivedquality -->
				<td class="formField">
	<!-- Mandar : 434 : for tooltip -->
					<html:select property="receivedEventReceivedQuality" styleClass="formFieldSized15" styleId="receivedEventReceivedQuality" size="1"
					 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.RECEIVED_QUALITY_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
	
	<!-- comments -->
				<td class="formField">
					<html:textarea styleClass="formFieldSized20"  styleId="receivedEventComments" property="receivedEventComments" />
				</td>
			 </tr>

<!-- CollectionEvent fields -->				 

			<tr>
 					<td rowspan="2" class="formLeftSubTableTitle">*
						<html:hidden property="operation" value="<%=operation%>"/>
						<html:hidden property="collectionEventSystemIdentifier" />
						<html:hidden property="collectionEventSpecimenId" />
						 Collected:
					</td>
	
					<td class="formLeftSubTableTitle">* 
						<label for="user">
							<bean:message key="eventparameters.user"/> 
						</label>
					</td>
	
					<td class="formLeftSubTableTitle" nowrap>* 
						<label for="date">
							<bean:message key="eventparameters.dateofevent"/> 
						</label>
						&nbsp;<small><bean:message key="page.dateFormat" /></small>
					</td>
					<td class="formLeftSubTableTitle">* 
						<label for="quality">
							<bean:message key="collectioneventparameters.collectionprocedure"/> 
						</label>
					</td>
					<td class="formLeftSubTableTitle">
						<label for="comments">
							<bean:message key="eventparameters.comments"/> 
						</label>
					</td>
				 </tr>

				 <tr>
<!-- 1st col empty -->
<!-- 					<td class="formField">
-->
<!-- User -->
					<td class="formField">
		<!-- Mandar : 434 : for tooltip -->
						<html:select property="collectionEventUserId" styleClass="formFieldSized15" styleId="collectionEventUserId" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>

<!-- date -->
					<td class="formField">
<%
if(currentCollectionDate.trim().length() > 0)
{
	Integer collectionYear = new Integer(Utility.getYear(currentCollectionDate ));
	Integer collectionMonth = new Integer(Utility.getMonth(currentCollectionDate ));
	Integer collectionDay = new Integer(Utility.getDay(currentCollectionDate ));
%>
<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
			  id="collectionEventdateOfEvent"
			  formName="newSpecimenForm"
			  month= "<%= collectionMonth %>"
			  year= "<%= collectionYear %>"
			  day= "<%= collectionDay %>"
			  value="<%=currentCollectionDate %>"
			  styleClass="formDateSized10"
					/>
<% 
	}
	else
	{  
 %>
<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
			  id="collectionEventdateOfEvent"
			  formName="newSpecimenForm"
			  styleClass="formDateSized10"
					/>
<% 
	} 
%> 

					</td>

<!-- collection procedure -->
					<td class="formField">
		<!-- Mandar : 434 : for tooltip -->
						<html:select property="collectionEventCollectionProcedure" styleClass="formFieldSized15" styleId="collectionEventCollectionProcedure" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<%--html:options name="<%=Constants.PROCEDURE_LIST%>" labelName="<%=Constants.PROCEDURE_LIST%>" /--%>
							<html:options collection="<%=Constants.PROCEDURE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
						<br>
<!-- container -->
						<label for="type"><b>
							<bean:message key="collectioneventparameters.container"/></b> 
						</label><br>
						<html:select property="collectionEventContainer" styleClass="formFieldSized15" styleId="collectionEventContainer" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<%--html:options name="<%=Constants.CONTAINER_LIST%>" labelName="<%=Constants.CONTAINER_LIST%>" /--%>
							<html:options collection="<%=Constants.CONTAINER_LIST%>" labelProperty="name" property="value"/>
						</html:select>

					</td>
		
		<!-- comments -->
					<td class="formField">
						<html:textarea styleClass="formFieldSized20"  styleId="collectionEventComments" property="collectionEventComments" />
					</td>

	</tr>

</logic:equal>
<!-- Mandar: 11-July-06 AutoEvents end  -->



				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				
				<tr>
					<td class="formTitle" height="20" colspan="6">
						<html:checkbox property="checkedButton" onclick="onCheckboxButtonClick(this)">
							&nbsp; <bean:message key="aliquots.createTitle"/>
						</html:checkbox>
				    </td>
				</tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				    	<label for="noOfAliquots">
							<bean:message key="aliquots.noOfAliquots"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
					    <logic:equal name="newSpecimenForm" property="checkedButton" value="true">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="noOfAliquots" property="noOfAliquots"/>
						</logic:equal>
						
						<logic:equal name="newSpecimenForm" property="checkedButton" value="false">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="noOfAliquots" property="noOfAliquots" disabled="true"/>
						</logic:equal>
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
				    	<label for="quantityPerAliquot">
							<bean:message key="aliquots.qtyPerAliquot"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
				    	<logic:equal name="newSpecimenForm" property="checkedButton" value="true">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="quantityPerAliquot" property="quantityPerAliquot"/>
						</logic:equal>
						
						<logic:equal name="newSpecimenForm" property="checkedButton" value="false">
							<html:text styleClass="formFieldSized10" maxlength="50"  size="30" styleId="quantityPerAliquot" property="quantityPerAliquot" disabled="true"/>
						</logic:equal>
		        	</td>
				 </tr>
				 
				 </logic:notEqual>
 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.externalIdentifier"/>
				     </td>
				     <td class="formButtonField" colspan="2">
				     	<html:button property="addExId" styleClass="actionButton" onclick="insExIdRow('addExternalIdentifier')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				    <td class="formTitle" align="Right">
							<html:button property="deleteExId" styleClass="actionButton" onclick="deleteChecked('addExternalIdentifier','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false)" disabled="true">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
				 </tr>
				 
				 	<tr>
					 	<td class="formSerialNumberLabel" width="5">
					     	#
					    </td>
					    <td class="formLeftSubTableTitle">
							<bean:message key="externalIdentifier.name"/>
						</td>
					    <td class="formRightSubTableTitle" colspan="2">
							<bean:message key="externalIdentifier.value"/>
						</td>
						<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
					 </tr>
				  <tbody id="addExternalIdentifier">
				  <%
				  	for(int i=exIdRows;i>=1;i--)
				  	{
						String exName = "externalIdentifierValue(ExternalIdentifier:" + i + "_name)";
						String exValue = "externalIdentifierValue(ExternalIdentifier:" + i + "_value)";
						String exIdentifier = "externalIdentifierValue(ExternalIdentifier:" + i +"_systemIdentifier)";
						String check = "chk_ex_"+i;
				  %>
					<tr>
					 	<td class="formSerialNumberField" width="5"><%=i%>.
				 			<html:hidden property="<%=exIdentifier%>" />
				 		</td>
					    <td class="formField">
				     		<html:text styleClass="formFieldSized15" maxlength="50"  styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<td class="formField" colspan="2">
				     		<html:text styleClass="formFieldSized15" maxlength="50"  styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<%
							String exKey = "ExternalIdentifier:" + i +"_systemIdentifier";
							boolean exBool = Utility.isPersistedValue(map,exKey);
							String exCondition = "";
							if(exBool)
								exCondition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=exCondition%> onClick="enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')">		
						</td>
					 </tr>
				  <% } %>
				 </tbody>
				 
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="specimen.biohazards"/>
				     </td>
				     <td class="formButtonField" colspan="2">
				     	<html:button property="addBiohazard" styleClass="actionButton" onclick="insBhRow('addBiohazardRow')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				    <td class="formTitle" align="Right">
							<html:button property="deleteBiohazard" styleClass="actionButton" onclick="deleteChecked('addBiohazardRow','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false)" disabled="true">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
				  </tr>
				  
				  <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="biohazards.type"/>
					</td>
				    <td class="formRightSubTableTitle" colspan="2">
						<bean:message key="biohazards.name"/>
					</td>
					<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
					</td>
				 </tr>
				 
				 <tbody id="addBiohazardRow">
				 <%
				  	Map bioHazardMap = form.getBiohazard();

					for(int i=bhRows;i>=1;i--)
				  	{
						String bhType = "biohazardValue(Biohazard:" + i + "_type)";
						String bhTypeKey = "Biohazard:" + i + "_type";
						String bhId	  = "biohazardValue(Biohazard:" + i + "_systemIdentifier)";
						String biohzId = "biohazardValue(Biohazard:" + i + "_persisted)";
						String check = "chk_bio_"+i;
				  %>
					<tr>
						<td class="formSerialNumberField" width="5"><%=i%>.
					 		<html:hidden property="<%=biohzId%>" />
					 	</td>
					    <td class="formField">
<!-- Mandar : 434 : for tooltip -->
				     		<html:select property="<%=bhType%>" styleClass="formFieldSized15" styleId="<%=bhType%>" size="1" onchange="onBiohazardTypeSelected(this)"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelProperty="name" property="value"/>
							</html:select>
				    	</td>
				    	<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     		<html:select property="<%=bhId%>" styleClass="formFieldSized15" styleId="<%="bhId" + i%>" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
								<%
									/*String type = (String)request.getParameter(bhType);*/
									String type = (String)bioHazardMap.get(bhTypeKey);
									for(int x=0;x<bhIdArray.length;x++)
									{
										if(type!=null && type.equals(bhTypeArray[x]))
										{											
								%>
											<html:option value="<%=bhIdArray[x]%>"><%=bhNameArray[x]%></html:option>
								<%
										}
									}
								%>
							</html:select>
				    	</td>
				    	<%
							String biohzKey = "Biohazard:" + i + "_persisted";
				    		String condition = (String)bioHazardMap.get(biohzKey);
				    		boolean biohzBool = Boolean.valueOf(condition).booleanValue();
							String biohzCondition = "";
							if(biohzBool)
								biohzCondition = "disabled='disabled'";
						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=biohzCondition%> onClick="enableButton(document.forms[0].deleteBiohazard,document.forms[0].bhCounter,'chk_bio_')">		
						</td>
					 </tr>
				  <% } %>
				 </tbody>
				 <!-- Bio-hazards End here -->	
			   	 	<tr>
				  		<td align="right" colspan="4">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<%@ include file="NewSpecimenPageButtons.jsp" %>
				  		</td>
				 	</tr>
	</table>
  </td>
</tr>
<!-- NEW SPECIMEN REGISTRATION ends-->
</table>
<%
if(pageView.equals("edit"))
{
%>
		</td>
	</tr>
</table>
<%
}
%>
</html:form>