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
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
	NewSpecimenForm form = (NewSpecimenForm)request.getAttribute(Constants.NEWSPECIMEN_FORM);
	String frdTo = form.getForwardTo();
	String nodeId="";
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	boolean isAddNew = false;
	String signedConsentDate = "";
	String selectProperty="";
	String tab = (String)request.getAttribute(Constants.TAB_SELECTED);
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	
	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN;
	
	
	String currentReceivedDate = "";
	String currentCollectionDate = "";


		Long specimenEntityId = null;
				if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
		{
			specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId");
		}
		else
		{
			specimenEntityId = Utility.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
			CatissueCoreCacheManager.getInstance().addObjectToCache("specimenEntityId",specimenEntityId);		
		}
	if (form != null) 
	{
		currentReceivedDate = form.getReceivedEventDateOfEvent();
		if(currentReceivedDate == null)
				currentReceivedDate = "";
		currentCollectionDate = form.getCollectionEventdateOfEvent();
		if(currentCollectionDate == null)
				currentCollectionDate = "";
	}
	
	if (reqPath != null)
		appendingPath = reqPath + "|/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	
	   	if(!operation.equals(Constants.ADD) )
	   	{
	   		if(form != null)
	   		{
		   		appendingPath = "/NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+form.getId() ;
		   		//System.out.println("---------- NSP JSP -------- : "+ appendingPath);				
		   	}
	   	}
	
	Map map = form.getExternalIdentifier();
%>
<head>
<style>
	.hidden
	{
	 display:none;
	}
</style>

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
		if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			formName = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "Specimen_"+form.getId();
	}
	else
	{
		formName = Constants.SPECIMEN_ADD_ACTION;
		readOnlyValue=false;
		if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			formName = Constants.CP_QUERY_SPECIMEN_ADD_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "SpecimenCollectionGroup_"+form.getSpecimenCollectionGroupId();
	}

	String formNameForCal = "newSpecimenForm";
	
%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>

	<%
	
	if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
		%>
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');					
		</script>
	<%}%>

<script language="JavaScript">
	function deleteExternalIdentifiers()
	{
	<%if(!pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{%>
		deleteChecked('addExternalIdentifier','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%} else {%>
		deleteChecked('addExternalIdentifier','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%}%>
		
	}
	function deleteBioHazards()
	{
	<%if(!pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{%>
		deleteChecked('addBiohazardRow','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%} else {%>
		deleteChecked('addBiohazardRow','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%}%>
		

	}

	function mapButtonClickedOnSpecimen(frameUrl)
	{
	   	var storageContainer = document.getElementById('selectedContainerName').value;
		frameUrl+="&storageContainerName="+storageContainer;
		//alert(frameUrl);
		NewWindow(frameUrl,'name','810','320','yes');
		
	}
		
		function onCheckboxButtonClick(chkBox)
		{
			//var aliquotCountTextBox  = document.getElementById("noOfAliquots");
			//var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
			
			if(chkBox.checked)
			{
				//aliquotCountTextBox.disabled = false;
				//qtyPerAliquotTextBox.disabled = false;				
				document.forms[0].deriveButton.disabled=true;
				document.forms[0].moreButton.disabled=true;
				document.forms[0].submitAndDistributeButton.disabled=true;
			}
			else
			{
				//aliquotCountTextBox.disabled = true;
				//qtyPerAliquotTextBox.disabled = true;				
				document.forms[0].deriveButton.disabled=false;
				document.forms[0].moreButton.disabled=false;
				document.forms[0].submitAndDistributeButton.disabled=false;
			}
		}
		
		function onNormalSubmit()
		{
			var checked = false;
			if(document.forms[0].checkedButton != null)
			{
			   checked = document.forms[0].checkedButton.checked;
			}
			var operation = document.forms[0].operation.value;
			var temp = "<%=frdTo%>";
			if(checked)
			{
			<% String actionToCall = null;%>
				
				if(operation == "add")
				{								
					if(temp == "orderDetails")
					{
						setSubmittedFor('ForwardTo','orderDetails');
					}
					else
					{
						setSubmittedFor('ForwardTo','pageOfAliquot');
					}
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
					if(temp == "orderDetails")
					{
						setSubmittedFor('ForwardTo','orderDetails');
					}
					else
					{
						setSubmittedFor('ForwardTo','pageOfAliquot');
					}
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			else
			{
				if(operation == "add")
				{				
					if(temp == "orderDetails")
					{
					   	setSubmittedFor('ForwardTo','orderDetails');
					}
					else
					{
						setSubmittedFor('null','success');
					}
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
					if(temp == "orderDetails")
					{
						setSubmittedFor('ForwardTo','orderDetails');
					}
					else
					{
						setSubmittedFor('null','success');
					}
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

					}%>
					
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			
		}
		function onCollOrClassChange(element)
		{
			var specimenCollGroupElement = document.getElementById("specimenCollectionGroupId");
			var classNameElement = document.getElementById("className").value;
			classNameElement = trim(classNameElement);
			var classSet = false;
			if(classNameElement == "Fluid" || classNameElement == "Cell"||classNameElement == "Tissue"||classNameElement == "Molecular")
			{
			    classSet = true;
			}
			var value;
			
			if(specimenCollGroupElement.value != "-1" && classSet)
			{
				if(element=='1')
				{
					value=true;
				}
				else
				{
					value=false;						
				}
				<%
				String actionOnCollOrClassChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				System.out.println(actionOnCollOrClassChange);
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnCollOrClassChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}%>
				var action = "<%=actionOnCollOrClassChange%>"+"&value="+value;
				document.forms[0].action = action + "&onCollOrClassChange=true";
				document.forms[0].submit();
			}	
		}
		
		function onCollectionGroupChange(element)
		{
			var specimenCollGroupElement = document.getElementById("specimenCollectionGroupId");
			
			if(specimenCollGroupElement.value != "-1")
			{
				if(element=='1')
				{
					value=true;
				}
				else
				{
					value=false;						
				}
				<%
				actionOnCollOrClassChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnCollOrClassChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}%>
				var action = "<%=actionOnCollOrClassChange%>"+"&value="+value;
				document.forms[0].action = action + "&onCollOrClassChange=true";
				document.forms[0].submit();
			}	
		}
		
		function setVirtuallyLocated(element)
		{
			var containerName = document.getElementById("customListBox_1_0");
			var pos1 = document.getElementById("customListBox_1_1");
			var pos2 = document.getElementById("customListBox_1_2");
			if(element.checked)
			{
				containerName.disabled = true;
				pos1.disabled = true;
				pos2.disabled = true;
				document.forms[0].mapButton.disabled = true;
			}
			else
			{
				onCollOrClassChange('0');
			}
		}
		function resetVirtualLocated()
		{
			var radioArray = document.getElementsByName("stContSelection");	
			radioArray[0].checked= true;
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;

			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
		}
		function eventClicked()
		{			
               
			  // Clear the value of onSubmit 
		    document.forms[0].onSubmit.value="";
			var consentTier=document.forms[0].consentTierCounter.value;
			var answer = confirm("Do you want to submit any changes?");
			var formName;;
			<% String formNameAction = null;%>
			if (answer){
				setSubmittedFor('ForwardTo','eventParameters');
				<%
				formNameAction = "NewSpecimenEdit.do";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryNewSpecimenEdit.do";
				}%>
				formName = "<%=formNameAction%>?consentTierCounter="+consentTier;
			}
			else{
				var id = document.forms[0].id.value;			
				<%
				formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>
						
				formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15&consentTierCounter="+consentTier;				
			}			
			confirmDisable(formName,document.forms[0].activityStatus);
		}



		
// Consent Tracking Module Virender mehta	
	function switchToTab(selectedTab)
	{
		var operation = document.forms[0].operation.value;
		var displayKey="block";
		var showAlways="block";
		
		if(!document.all)
		{
			displayKey="table";
			showAlways="table";
		}
			
		var displayTable=displayKey;
		var tabSelected="none";
		
		if(selectedTab=="newSpecimenTab")
		{
			tabSelected=displayKey;
			displayTable="none";
		}	
	
		if(operation=="add")
		{
			var display4=document.getElementById('collectionEvent');
			display4.style.display=tabSelected;
		}
		var display=document.getElementById('addSpecimen');
		display.style.display=tabSelected;
		
		var display2=document.getElementById('externalIdentifiers'); 
		display2.style.display=tabSelected;
			
		var display3=document.getElementById('bioHazards'); 
		display3.style.display=tabSelected;
	
		var displayConsentTable=document.getElementById('table4');
		if(displayConsentTable!=null)
		{
			displayConsentTable.style.display=displayTable;	
		}
		
		var display5=document.getElementById('specimenPageButton');
		display5.style.display=showAlways;
		
		var aliquotTable=document.getElementById('aliquotId');
		aliquotTable.style.display=tabSelected;
				
		var collectionTab=document.getElementById('newSpecimenTab');
		var consentTab=document.getElementById('consentTab');
		
		if(selectedTab=="newSpecimenTab")
		{
			updateTab(newSpecimenTab,consentTab);
		}
		else		
		{
			updateTab(consentTab,newSpecimenTab);
		}
		
	}
	
	//This function is for changing the behaviour of TABs
	function updateTab(tab1, tab2)
	{
		tab1.onmouseover=null;
		tab1.onmouseout=null;
		tab1.className="tabMenuItemSelected";
	
		tab2.className="tabMenuItem";
		tab2.onmouseover=function() { changeMenuStyle(this,'tabMenuItemOver'),showCursor();};
		tab2.onmouseout=function() {changeMenuStyle(this,'tabMenuItem'),hideCursor();};
	}

		//This function will Switch tab to newSpecimen page
		function newspecimenPage()
		{
			switchToTab("newSpecimenTab");
		}
		//This function will switch page to consentPage
		function consentPage()
		{	
			var ind=document.getElementById('specimenCollectionGroupId');
			if(ind!=null)
			{
				var index=ind.selectedIndex;
				if(index==0)
				{
					alert("Select Specimen Collection Group");
				}
				else
				{
					checkForConsents();
				}
				
			}
			else
			{
				checkForConsents();
			}
		}
		
		function checkForConsents()
		{
			<%
				if(form.getConsentTierCounter()>0)			
				{
				%>
					switchToTab("consentTab");
				<%
				}
				else
				{
				%>
					alert("No consents available for selected Specimen Collection Group");
				<%
				}
				%>
		}

	  function showConsents()
	  {
		var showConsents = "<%=tab%>";
		if(showConsents=="<%=Constants.NULL%>" || showConsents=="specimen" || showConsents=="<%=Constants.NEWSPECIMEN_FORM%>")
		{
			newspecimenPage();
		}
		else
		{
			consentPage();			
		}
	  }
	  
	  //For View Surgical Pathology Report
		function viewSPR()
        {
			var tempId=document.forms[0].id.value;
			var consentTierCounter=document.forms[0].consentTierCounter.value;
        	var action="<%=Constants.SPR_VIEW_ACTION%>?operation=viewSPR&pageOf=pageOfNewSpecimen&id="+tempId+"&consentTierCounter="+consentTierCounter;
			document.forms[0].action=action;
			document.forms[0].submit();
        }

// Consent Tracking Module Virender mehta	 
	</script>
</head>
<body onLoad="showConsents()">

<% 
		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
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
		}

		Map mapValues = null;
		int noOfRows=0;
		ViewSurgicalPathologyReportForm formSPR=null;
		if(operation.equals("viewSPR"))
		{
			Object obj = request.getAttribute("viewSurgicalPathologyReportForm");
			if(obj != null && obj instanceof ViewSurgicalPathologyReportForm)
			{
				formName=Constants.SPR_VIEW_ACTION;
				formSPR=(ViewSurgicalPathologyReportForm)obj;
				mapValues = formSPR.getValues();
				noOfRows = formSPR.getCounter();
			}
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
				String submitAndDistribute = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
	%>
	<table summary="" cellpadding="1" cellspacing="0" border="0" class="contentPage" width="100%">
			<tr>
		    <td width = "50%">
					<table summary="" cellpadding="3" cellspacing="0" width="100%">
						<tr>
							<td colspan="6">
								<html:errors />
								<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
									<%=messageKey%>
								</html:messages>
							</td>
						</tr>		
						<tr>
							<td>
								<html:hidden property="operation" value="<%=operation%>"/>
								<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
								<%						
								if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
								{%>
								 	<html:hidden property="forwardTo" value="orderDetails"/>								
						 	  <%}else
						    	{ %>
									<html:hidden property="forwardTo" value=""/>
							  <%} %>
								<html:hidden property="virtuallyLocated"/>
								<html:hidden property="containerId" styleId="containerId"/>
								<html:hidden property="withdrawlButtonStatus"/>
								<html:hidden property="stringOfResponseKeys"/>
								<html:hidden property="applyChangesTo"/>
								<html:hidden property="consentTierCounter"/>
							</td>
							<td>

							</td>
							<td>

							</td>
							<td>
								<html:hidden property="onSubmit"/>
							</td>
							<td>
							<%						
								if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
								{%>
								 									
						 	  <%}else
						    	{ %>
									<html:hidden property="id"/>
								<%} %>
								<html:hidden property="positionInStorageContainer" />
								<html:hidden property="parentPresent" />
							</td>
						</tr>				 
						<tr>
							<td class="formMessage" colspan="6">
								<bean:message key="requiredfield.message"/>  
				</td>
		</tr>
		</table>
	<%
	if(pageView.equals("edit"))
	{
	%>
		<table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
			<tr>
				<td height="20" class="tabMenuItemSelected" onclick="newspecimenPage()" id="newSpecimenTab">
					<bean:message key="tab.specimen.details"/>
				</td>
				<%
					String eventLinkAction = "'ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&menuSelected=15&specimenId="+form.getId()+"'" ;
				%>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="eventClicked();">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewAnnotations(<%=specimenEntityId%>,document.forms[0].id.value,document.forms[0].consentTierCounter.value,'<%=staticEntityName%>')">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
					<bean:message key="consents.consents"/>            
				</td>								
		
				<td width="300" class="tabMenuSeparator" colspan="1">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
	<%
	}
	%>
<!--  Consent Tracking Module Virender mehta	 -->
	<%
	if(pageView.equals("add"))
	{
	%>
	
	 <table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
		<tr>
			<td height="20" width="9%" nowrap class="tabMenuItemSelected" onclick="newspecimenPage()" id="newSpecimenTab">
				<bean:message key="consents.newspecimen"/>
			</td>

	        <td height="20" width="9%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
	          <bean:message key="consents.consents"/>      
	        </td>								
			<td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
		</tr>
		<tr>
			<td class="tabField" colspan="4" width="*">
	<%
	}
	%>
	
				<table summary="" cellpadding="1" cellspacing="0" border="0" class="contentPage" width="100%">
					<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
					<!-- NEW SPECIMEN REGISTRATION BEGINS-->
			    	<tr>
						<td width="100%">
							<table summary="" cellpadding="1" border="0" cellspacing="0" id="addSpecimen" width="100%">
				 				<tr>
									<td class="formTitle" height="20" width="100%" colspan="7">
										<%String title = "specimen."+pageView+".title";%>
										<div style="float:right;">
											<html:link href="#" styleId="newUser" onclick="consentPage()">
												<bean:message key="consent.defineconsents" />
											</html:link>	
										</div>
										<div>
											<bean:message key="<%=title%>"/>							
										</div>
										<%--
											<span style="width:85%;">
												<bean:message key="<%=title%>"/>    
											</span>
											<span style="text-align:right;margin-left:70%;">
												<html:link href="#" styleId="newUser" onclick="consentPage()">
													<bean:message key="consent.defineconsents" />
												</html:link>	
											</span>	
										 --%>
								 	</td>
								</tr>
<!--  Consent Tracking Module Virender mehta	 -->								
								<tr>
									<td class="formRequiredNotice" >*</td>
									<%
										String specimenColSpan;
										if(operation.equals(Constants.EDIT))
										{
											specimenColSpan="1";
										}
										else
										{
											specimenColSpan="4";
										}
									%>
									<logic:equal name="newSpecimenForm" property="parentPresent" value="false">
									<td class="formRequiredLabel" nowrap>
										<label for="specimenCollectionGroupName">
											<bean:message key="specimenCollectionGroup.groupName"/>
										</label>
									</td>							
									<td class="formField" colspan="<%=specimenColSpan%>">
										<!-- Mandar : 434 : for tooltip -->
							     		<html:select property="specimenCollectionGroupId" styleClass="formFieldSized15" 
							     				styleId="specimenCollectionGroupId" size="1" 
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="onCollectionGroupChange('1');resetVirtualLocated()" >
											<html:options collection="<%=Constants.SPECIMEN_COLLECTION_GROUP_LIST%>" 
												labelProperty="name" property="value"/>		
										</html:select>
										&nbsp;
										<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
										<html:link href="#" styleId="newUser" onclick="addNewAction('NewSpecimenAddNew.do?addNewForwardTo=specimenCollectionGroup&forwardTo=createNewSpecimen&addNewFor=specimenCollectionGroupId')">
											<bean:message key="buttons.addNew" />
										</html:link>					   
				   						</logic:notEqual>
		   						<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
								<html:link href="#" styleId="newUser" onclick="addNewAction('NewSpecimenAddNew.do?addNewForwardTo=specimenCollectionGroupCPQuery&forwardTo=createNewSpecimen&addNewFor=specimenCollectionGroupId')">
									<bean:message key="buttons.addNew" />
								</html:link>					   
		   						</logic:equal>
		   						</logic:notEqual>
		   						
		   						
										 <!-- <a href="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup">
											<bean:message key="app.addNew" />
											</a> 
										-->			
						        	</td>							
									</logic:equal>
				        	
									<logic:equal name="newSpecimenForm" property="parentPresent" value="true">
						        	<td class="formRequiredLabel" >
										<label for="parentSpecimenId">
									<bean:message key="createSpecimen.parentLabel"/>
										</label>
									</td>
									
						        	<td class="formField" colspan="<%=specimenColSpan%>">
						        		<html:hidden property="specimenCollectionGroupId"/>
										<!-- Mandar : 434 : for tooltip -->
							     		<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<html:options collection="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
										</html:select>
						        	</td>
									</logic:equal>	
									
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
									<td class="formRequiredNotice">*</td>
									<td class="formRequiredLabel">
										<label for="lineage">
											<bean:message key="specimen.lineage"/>
										</label>
									</td>
									<td class="formField" >								
							     		<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="lineage" property="lineage" 
					     		readonly="true"/>														     	
									</td>
									</logic:equal>
							
								</tr>
								
								<tr>
									<td class="formRequiredNotice" >
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								    </td>
								    <td class="formRequiredLabel" >
										<label for="label">
											<bean:message key="specimen.label"/>
										</label>
									</td>
								    <td class="formField" >
						     	<html:text styleClass="formFieldSized15" size="30" maxlength="255"  styleId="label" property="label" readonly="<%=readOnlyForAll%>"/>
								    </td>							
									<td class="formRequiredNotice" width="5">&nbsp;</td>
								    <td class="formLabel">							
								    	<label for="barcode">
											<bean:message key="specimen.barcode"/>
										</label>								
									</td>
								    <td class="formField" >
								<html:text styleClass="formFieldSized15" maxlength="255"  size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
						        	</td>
								</tr>
						 
								<tr>
								 	<td class="formRequiredNotice" width="5">*</td>
								    <td class="formRequiredLabel">
								     	<label for="className">
								     		<bean:message key="specimen.type"/>
								     	</label>
								    </td>
								    <td class="formField">

								     	<%
									String classReadOnly = "false";
											if(operation.equals(Constants.EDIT))
											{
									    classReadOnly = "true";
											}
										%>
							    <autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="<%=classReadOnly%>"
									    />
				 			   
							
						        	</td>
						 
								 	<td class="formRequiredNotice" width="5">*</td>
									<td class="formRequiredLabel">
									<label for="type">
										<bean:message key="specimen.subType"/>
									</label>
									</td>				    
								    <td class="formField" >
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
										
										String readOnlyForAliquot = "false";
										if(Constants.ALIQUOT.equals(form.getLineage())&&operation.equals(Constants.EDIT)) {
										      readOnlyForAliquot = "true";
										}
									%>
						  
						   <autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>"
										  onChange="<%=subTypeFunctionName%>"
										  readOnly="<%=readOnlyForAliquot%>"
										  dependsOn="<%=form.getClassName()%>"
					        />
			
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
								    <td class="formField" >
					
                                       <autocomplete:AutoCompleteTag property="tissueSite"
										  size="150"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
										  initialValue="<%=form.getTissueSite()%>"
										  readOnly="<%=readOnlyForAliquot%>"
									    />
								
										<%
											String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
										%>
										<a href="#" onclick="javascript:NewWindow('<%=url%>','name','375','330','yes');return false">
											<img src="images\Tree.gif" border="0" width="24" height="18" title='Tissue Site Selector'>
										</a>
						        	</td>
								
								    <td class="formRequiredNotice" width="5">
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								     </td>
								     <td class="formRequiredLabel">
										<label for="tissueSide">
											<bean:message key="specimen.tissueSide"/>
										</label>
									</td>
								     <td class="formField" >
							 
							  <autocomplete:AutoCompleteTag property="tissueSide"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
										  initialValue="<%=form.getTissueSide()%>"
										  readOnly="<%=readOnlyForAliquot%>"
									    />

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
									<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								    <td colspan="4" class="formField" >
							
							<autocomplete:AutoCompleteTag property="pathologicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getPathologicalStatus()%>"
										  readOnly="<%=readOnlyForAliquot%>"
							/>
							
						        	</td>
									</logic:notEqual>
							     	
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
									<td class="formField" >
							<autocomplete:AutoCompleteTag property="pathologicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getPathologicalStatus()%>"
										  readOnly="<%=readOnlyForAliquot%>"
							/>
						        	</td>	
									<!-- activitystatus -->
									<td class="formRequiredNotice" width="5">*</td>
									<td class="formRequiredLabel" >
										<label for="activityStatus">
											<bean:message key="participant.activityStatus" />
										</label>
									</td>
									<td class="formField">
							
							<autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = "<%=request.getAttribute(Constants.ACTIVITYSTATUSLIST)%>"
										  initialValue="<%=form.getActivityStatus()%>"
										  onChange="<%=strCheckStatus%>"
							/>
							
									</td>					
									</logic:equal>
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
								    <td class="formField" >
								     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
								     	<span id="unitSpan"><%=unitSpecimen%></span>
								     	<html:hidden property="unit"/>
								    </td>
									<td class="formRequiredNotice" width="5">
								     	&nbsp;
								    </td>
								    <td class="formLabel">
										<label for="concentration">
											<bean:message key="specimen.concentration"/>
										</label>
									</td>
									<td class="formField" >
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
				 
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						
								<tr>
									<!-- Available -->
									<td class="formRequiredNotice" width="5">&nbsp;</td>
									<td class="formLabel">
										<label for="available">
											<bean:message key="specimen.available" />
										</label>
									</td>
									<td class="formField">
										<html:checkbox property="available">
										</html:checkbox>
									</td>	
									<!-- Available Quantity -->							
									<td class="formRequiredNotice" width="5">&nbsp;</td>
									<td class="formLabel" >
										<label for="availableQuantity">
											<bean:message key="specimen.availableQuantity" />
										</label>
									</td>
									<td class="formField">
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
					            String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"}; 
										//String[] initValues = new String[3];
										//initValues[0] = form.getStorageContainer();
										//initValues[1] = form.getPositionDimensionOne();
										//initValues[2] = form.getPositionDimensionTwo();
										String[] initValues = new String[3];
										List initValuesList = (List)request.getAttribute("initValues");
										if(initValuesList != null)
										{
											initValues = (String[])initValuesList.get(0);
										}
										//System.out.println("NewSpecimen :: "+initValues[0]+"<>"+initValues[1]+"<>"+initValues[2]);			
										String rowNumber = "1";
										String styClass = "formFieldSized5";
										String tdStyleClass = "customFormField";
										String onChange = "onCustomListBoxChange(this)";
										String className = form.getClassName();
										String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
										if (collectionProtocolId==null)
											collectionProtocolId="";
										if (className==null)
											className="";
								String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
											+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
											+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;
										System.out.println(frameUrl);
								String buttonOnClicked = "mapButtonClickedOnSpecimen('"+frameUrl+"')";  
								
								//"javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false"; 
								//javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";
										String noOfEmptyCombos = "3";

										boolean disabled = false;
										boolean buttonDisabled = false;
										if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
										{
											disabled = true;
										}	
								 int radioSelected = form.getStContSelection();
								boolean dropDownDisable = false;
								boolean textBoxDisable = false;
								
								if(radioSelected == 1)
								{
									dropDownDisable = true;
									textBoxDisable = true;
								}
								else if(radioSelected == 2)
								{									
									textBoxDisable = true;
								}
								else if(radioSelected == 3)
								{
									dropDownDisable = true;									
								}
								
								
									%>
						
									<%=ScriptGenerator.getJSForOutermostDataTable()%>
									<%//System.out.println("after getJSForOutermostDataTable in specimen jsp");%>
									<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
									<%//System.out.println("after getJSEquivalentFor in specimen jsp");%>
						
									<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
						
									<td class="formField" colSpan="4">
							<table border="0">
										<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
								<tr>
									<td ><html:radio value="1" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td class="formFieldNoBorders">																			
											<bean:message key="specimen.virtuallyLocated" />
									</td>
								</tr>
								<tr>
									<td ><html:radio value="2" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td>
										<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
													attributeNames="<%=attrNames%>" 
											tdStyleClassArray="<%=tdStyleClassArray%>"
													initialValues="<%=initValues%>"  
													styleClass = "<%=styClass%>" 
													tdStyleClass = "<%=tdStyleClass%>" 
													labelNames="<%=labelNames%>" 
													rowNumber="<%=rowNumber%>" 
													onChange="<%=onChange%>" 
													formLabelStyle="formLabelBorderless"
											disabled = "<%=dropDownDisable%>"
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
											</tr>
											</table>
									</td>
								</tr>
								<tr>
									<td ><html:radio value="3" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td class="formLabelBorderlessLeft">
										<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos1" property="pos1" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos2" property="pos2" disabled= "<%=textBoxDisable%>"/>
										<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "<%=textBoxDisable%>">
											<bean:message key="buttons.map"/>
										</html:button>
									</td>
								</tr>
								</logic:equal>								
								
								<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">								
								
								<%
									
									NewSpecimenForm newSpecimenForm = (NewSpecimenForm) request.getAttribute("newSpecimenForm");
								
									if(newSpecimenForm.getStContSelection() == 1)
									{%>Specimen is virtually Located <%}									
									else
									{
									%>
										<tr>											
											<td class="formLabelBorderless">
												<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" readonly= "true"/>
												<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionOne" property="positionDimensionOne" readonly= "true"/>
												<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionTwo" property="positionDimensionTwo" readonly= "true"/>
												<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "true">
													<bean:message key="buttons.map"/>
												</html:button>
											</td>
										</tr>
									<%
									}
									
								%>
								</logic:notEqual>	
								
								
								
								
							</table>

									</td>
									<%//System.out.println("End of tag in jsp");%>
									<%-- n-combo-box end --%>
								</tr>

								<!--%System.out.println("Inside if condition in jsp"+exceedsMaxLimit);
								if(exceedsMaxLimit!=null && exceedsMaxLimit.equals("true")){
									%-->
								<logic:equal name="exceedsMaxLimit" value="true">
								<tr>
									<td>
											<bean:message key="container.maxView"/>
									</td>
								</tr>
								</logic:equal>
								<!--%}%-->				 				 
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
							</table>	
											
				
				<%--<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">					
					<!-- Mandar AutoEvents start -->		
							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
					<tr>						
						<td colspan="3" width="500" class="formTitle">
							<bean:message key="specimen.collectedevents.title"/>	
						</td>
						<td colspan="3" width="500" class="formTitle">
							<bean:message key="specimen.receivedevents.title"/>						
						</td>
					</tr>
					<!-- User -->
					<tr>						
						<!-- CollectionEvent fields -->	
						
						<html:hidden property="collectionEventId" />
						<html:hidden property="collectionEventSpecimenId" />
						<td class="formRequiredNotice" width="5">*</td>
	 					<td class="formRequiredLabel"> 
							<label for="user">
								<bean:message key="specimen.collectedevents.username"/> 
							</label>
						</td>						
						<td class="formField">
							<!-- Mandar : 434 : for tooltip -->
							<html:select property="collectionEventUserId" styleClass="formFieldSized15" styleId="collectionEventUserId" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>		

						<!-- RecievedEvent fields -->
						<html:hidden property="receivedEventId" />
						<html:hidden property="receivedEventSpecimenId" />
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="type">
								<bean:message key="specimen.receivedevents.username"/> 
							</label>
						</td>
						<td class="formField">
							<!-- Mandar : 434 : for tooltip -->
							<html:select property="receivedEventUserId" styleClass="formFieldSized15" styleId="receivedEventUserId" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
						
					</tr>
					
					<!-- date -->
					<tr>
						
						<!-- CollectionEvent fields -->	
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" nowrap> 
							<label for="date">
								<bean:message key="eventparameters.dateofevent"/>															
							</label>
							<!-- <BR><small><bean:message key="page.dateFormat" /></small>  -->
						</td>
						
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
							<small><bean:message key="scecimen.dateformat" /></small>	
						</td>	

						<!-- RecievedEvent fields -->
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" nowrap>
							<label for="date">
								<bean:message key="eventparameters.dateofevent"/> 
							</label>
							<!-- <BR><small><bean:message key="scecimen.dateformat" /></small> -->
						</td>						
						<td class="formField">
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
								<small><bean:message key="scecimen.dateformat" /></small>	
						</td>	
					</tr>	
					
					<tr>					
						
						<!-- CollectionEvent fields -->	
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="collectionprocedure">
								<bean:message key="collectioneventparameters.collectionprocedure"/> 
							</label>
						</td>
						<td class="formField">
								<!-- Mandar : 434 : for tooltip -->
								<html:select property="collectionEventCollectionProcedure" styleClass="formFieldSized15" styleId="collectionEventCollectionProcedure" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<!--html:options name="<%=Constants.PROCEDURE_LIST%>" labelName="<%=Constants.PROCEDURE_LIST%>" /-->
									<html:options collection="<%=Constants.PROCEDURE_LIST%>" labelProperty="name" property="value"/>
								</html:select>							
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formRequiredNotice" width="5"rowspan="2">*</td>
						<td class="formRequiredLabel"rowspan="2"> 
							<label for="quality">
								<bean:message key="receivedeventparameters.receivedquality"/> 
							</label>
						</td>						
						<!-- receivedeventparameters.receivedquality -->
						<td class="formField"rowspan="2">
							<!-- Mandar : 434 : for tooltip -->
							<html:select property="receivedEventReceivedQuality" styleClass="formFieldSized15" styleId="receivedEventReceivedQuality" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.RECEIVED_QUALITY_LIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>
					
					<!-- CollectionEvent fields -->	
					<tr>							
							<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="container">
									<bean:message key="collectioneventparameters.container"/> 
								</label>
							</td>
							<td class="formField">
								<html:select property="collectionEventContainer" styleClass="formFieldSized15" styleId="collectionEventContainer" size="1"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<!--html:options name="<%=Constants.CONTAINER_LIST%>" labelName="<%=Constants.CONTAINER_LIST%>" /-->
										<html:options collection="<%=Constants.CONTAINER_LIST%>" labelProperty="name" property="value"/>
								</html:select>
							</td>
					</tr>	
					
					<!-- comments -->
					<tr>	
						<!-- CollectionEvent fields -->	
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>
						<td class="formField">
							<html:textarea styleClass="formFieldSized20"  styleId="collectionEventComments" property="collectionEventComments" />
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>						
						<td class="formField">
							<html:textarea styleClass="formFieldSized20"  styleId="receivedEventComments" property="receivedEventComments" />
						</td>						
						
				 	</tr>
					</logic:equal>
				
				<!-- Mandar: 11-July-06 AutoEvents end  -->
				</table>	--%>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<%@ include file="CollAndRecEvents.jsp" %>
							</logic:equal>
							<%@ include file="ExternalIdentifiers.jsp" %>
			 				
							<%@ include file="BioHazards.jsp" %>
							<!-- bioHazards -->
							
							<!-- Insert Consent Tracking Code -->
<!--  Consent Tracking Module Virender mehta	 -->														
							<%
							List requestParticipantResponse = (List)request.getAttribute(Constants.SPECIMEN_RESPONSELIST);						 	
							if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
							{
							%>
							    <%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
							<%
							}
							%>
<!--  Consent Tracking Module Virender mehta	 -->										

							<!--specimenPageButton-->
			 				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id ="specimenPageButton">
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">				 			
							<tr>					
										<td>
											<table id="aliquotId">
												<tr>					
								<td class="formFieldNoBordersBold" height="20" colspan="5">
									<html:checkbox property="checkedButton" onclick="onCheckboxButtonClick(this)">
										&nbsp; <bean:message key="specimen.aliquot.message"/>
									</html:checkbox>
							    </td>
							</tr>								
											</table>
										</td>
									</tr>
							</logic:notEqual>
							 
							 <!-- Bio-hazards End here -->	
						   	 	<tr>
							  		<td align="left" colspan="6">
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

<!--  Consent Tracking Module Virender mehta	 -->
<%
if(pageView.equals("edit")||pageView.equals("add"))
{
%>
<!--  Consent Tracking Module Virender mehta	 -->
			</td>
		</tr>
	</table>
<%
}
%>
<html:hidden property="stContSelection"/>
<html:hidden property="concentration"/>
<html:hidden property="lineage"/>
</td>
</tr>
</table>

</html:form>

</body>