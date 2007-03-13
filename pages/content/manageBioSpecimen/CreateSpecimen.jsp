
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.RequestDetailsForm"%>


<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<head>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>


<% 
        String[] columnList1 = Constants.DERIVED_SPECIMEN_COLUMNS;
		List columnList = new ArrayList();
		for(int i=0;i<columnList1.length;i++)
		{
			columnList.add(columnList1[i]);
		}

		String operation = (String)request.getAttribute(Constants.OPERATION);
		Integer identifierFieldIndex = new Integer(4);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
		boolean readOnlyValue=false,readOnlyForAll=false;
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		if(operation!=null && operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.CREATE_SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.CREATE_SPECIMEN_ADD_ACTION;
			if(pageOf!=null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
			{
				formName = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION ;
			}
			readOnlyValue=false;
		}

		if (operation!=null && operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}



		Object obj = request.getAttribute("createSpecimenForm");
		int exIdRows=1;
		
		CreateSpecimenForm form = null;
		String unitSpecimen = "";
		Map map = null;
		String frdTo = "";
		if(obj != null && obj instanceof CreateSpecimenForm)
		{
			form = (CreateSpecimenForm)obj;
			map = form.getExternalIdentifier();
			exIdRows = form.getExIdCounter();
			frdTo = form.getForwardTo();

			if(form.getUnit() != null)
				unitSpecimen = form.getUnit();
		}
	
	String multipleSpecimen = "0";
	String action = Constants.CREATE_SPECIMEN_ADD_ACTION;
	if(request.getAttribute("multipleSpecimen")!=null) 
	{
	   multipleSpecimen = "1";
	   action = "DerivedMultipleSpecimenAdd.do?retainForm=true";
	}
	
	String onCheckboxChange = "setVirtuallyLocated(this,"+multipleSpecimen+")" ;
	//String onClassChangeFunctionName = "onTypeChange(this);" + onChangeFunctionName;
	
%>




	<script language="JavaScript">
	
		function onRadioButtonClick(element)
		{
     		if(element.value == 1)
			{
				document.forms[0].parentSpecimenLabel.disabled = false;
				document.forms[0].parentSpecimenBarcode.disabled = true;
			}
			else
			{
				document.forms[0].parentSpecimenBarcode.disabled = false;
				document.forms[0].parentSpecimenLabel.disabled = true;
			}
		}		 
		function resetVirtualLocated()
		{
		    try
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
			catch(e)
			{
			}
		}
		
		function isLabelBarcodeOrClassChange()
		{
			var parentLabelElement = document.getElementById("parentSpecimenLabel");
			var parentBarcodeElement = document.getElementById("parentSpecimenBarcode");
			var classNameElement = document.getElementById("className");
			
			if((parentLabelElement.value != "-1" || parentBarcodeElement.value != "-1") && classNameElement.value != "-1")
			{
		
				<% String actionToCall2 = null;
					actionToCall2 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
					if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY)){
					actionToCall2 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION+"?pageOf="+Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY+"&operation=add&virtualLocated=false";
				}%>
				var action = "<%=actionToCall2%>";
				document.forms[0].action = action + "&isLabelBarcodeOrClassChange=true";
				document.forms[0].submit();
			}	
		}
		
		function classChangeForMultipleSpecimen()
		{
		
		 	var action ='NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&specimenAttributeKey=' + document.getElementById("specimenAttributeKey").value + '&derivedSpecimenCollectionGroup=' + document.getElementById("derivedSpecimenCollectionGroup").value + '&retainForm=true';
			document.forms[0].action = action;
			document.forms[0].submit();
		}
			
	  	function onClassOrLabelOrBarcodeChange(multipleSpecimen,element)
		{
			if(multipleSpecimen == "1")
				{
				   classChangeForMultipleSpecimen();
				}
	
		    var radioArray = document.getElementsByName("checkedButton");
		 	var flag = "0";
 			if (radioArray[0].checked) 
			{
			  if(document.getElementById("parentSpecimenLabel").value!= "") 
			  {
				   flag = "1";
			  }
			} 
		     else 
		     {
				if (document.getElementById("parentSpecimenBarcode").value != "") 
				{
	     	    	 flag = "1";
				}
			}
 	    	var classNameElement = document.getElementById("className");
			if(flag=="1" && classNameElement.value != "-1")
			{
				<% String actionToCall1 = null;
					actionToCall1 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
					if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY)){
					actionToCall1 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION+"?operation=add";
				}%>
				document.forms[0].action = "<%=actionToCall1%>";
				document.forms[0].submit();
			}	
			else
			{
				alert("Please enter Parent Label/Barcode and Specimen Class");
				element.checked=true;
			}
		}
		
		function setVirtuallyLocated(element,multipleSpecimen)
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
				onClassOrLabelOrBarcodeChange(multipleSpecimen,element);
//				containerName.disabled = false;
//				pos1.disabled = false;;
//				pos2.disabled = false;;
//				document.forms[0].mapButton.disabled = false;
				
			}
		} 
		
		function onAddNewButtonClicked()
		{
		    var action = "NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&rowSelected=-1&addNew=true&operation=add";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function closeWindow()
		{
		  window.close();
		}
		function deleteExternalIdentifiers()
		{
			<% if(multipleSpecimen.equals("1")){%>
				deleteChecked('addExternalIdentifier','NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&status=true&retainForm=true',document.forms[0].exIdCounter,'chk_ex_',false);
			<%}else {			
				if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY)){%>
					deleteChecked('addExternalIdentifier','CPQueryCreateSpecimen.do?pageOf=pageOfCreateSpecimenCPQuery&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
				<%} else {%>
					deleteChecked('addExternalIdentifier','CreateSpecimen.do?pageOf=pageOfCreateSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
				<%}%>
			<%}%>
		}
		
		function onCheckboxButtonClick(chkBox)
		{
			if(chkBox.checked)
			{
				document.forms[0].submitAndDistributeButton.disabled=true;
				document.forms[0].moreButton.disabled=true;
				
				
			}
			else
			{
				document.forms[0].submitAndDistributeButton.disabled=false;
				document.forms[0].moreButton.disabled=false;
			}
		}
		function onNormalSubmit()
		{
			<% String actionToCall = null;
				actionToCall = "AddSpecimen.do";
				if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY)){
				actionToCall = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION;
			}%>
			var checked = document.forms[0].aliCheckedButton.checked;
			if(checked)
			{
				setSubmittedFor('ForwardTo','pageOfAliquot');
				confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
			
			}
			else
			{	
				var temp = "<%=frdTo%>";				
				if(temp == "orderDetails")
				{
					setSubmittedFor('ForwardTo','orderDetails');
				}
				else
				{
					setSubmittedFor('ForwardTo','eventParameters');
				}
				confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
			
			}
		}
		
			function mapButtonClickedOnSpecimen(frameUrl)
	{
	   	var storageContainer = document.getElementById('selectedContainerName').value;
		frameUrl+="&storageContainerName="+storageContainer;
		//alert(frameUrl);
		window.open(frameUrl,'','scrollbars=yes,menubar=no,height=320,width=810,resizable=yes,toolbar=no,location=no,status=no');
		//NewWindow(frameUrl,'name','810','320','yes');
		
    }
	</script>
	
		<%if(pageOf!=null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
		{
			if(request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null )
			{
				Long parentSpecimenId = (Long) request.getAttribute(Constants.PARENT_SPECIMEN_ID);
				String nodeId = "Specimen_"+parentSpecimenId.toString();	
		%>
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');					
		</script>
		
	<%}}%>
</head>


<%
List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
%>



	<html:errors />
   <html:form action="<%=action%>">
   
                      <input type="hidden" id="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
				       name="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
				       value="<%=request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY)%>" />

						<input type="hidden" id="derivedSpecimenCollectionGroup"
				       name="derivedSpecimenCollectionGroup"
				       value="<%=request.getParameter("derivedSpecimenCollectionGroup")%>" />
					   
					   	<input type="hidden" id="rowSelected"
				       name="rowSelected"
				       value="<%=request.getParameter("rowSelected")%>" />
   
   <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="580">
<tr>
	<td>		
		&nbsp;
	</td>
</tr>

                     

<tr>
 <td>
    <table summary="" cellpadding="3" cellspacing="0" border="0" width="580" >

<%
	if(dataList!=null && dataList.size() != 0)
	{
	   
		String title = "Derived Specimens For This Parent Specimen";

%>
         <tr>
		 <td width="100%" align="right">
		 	    <html:button property="addNewDerived" styleClass="actionButton" onclick="onAddNewButtonClicked();">
				<bean:message key="buttons.addNew"/>
				</html:button>
		 </td>
		 </tr>
   	 	<tr>
			<td class="formTitle" height="20" >
			
				<%=title%>
			</td>
		</tr>
		
   	 	<tr>
			<td>
				<script>
				    var specimenAttributeKey = document.getElementById("specimenAttributeKey");
					if(specimenAttributeKey!=null)
					{
			          parent.window.opener.document.applets[0].setButtonCaption(specimenAttributeKey.value);
					}
					function derivedSpecimenGrid(id)
					{
						var cl = mygrid.cells(id,4);
						var rowSelected = cl.getValue();
						var c2 = mygrid.cells(id,0);
						var eventId = c2.getValue();

					//	alert("eventId : "+eventId + " , pageOf: "+pageOf);
						var url = "NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&rowSelected=" + rowSelected +"&specimenAttributeKey="+document.getElementById("specimenAttributeKey").value;
//						var url = "SearchObject.do?pageOf="+pageOf+"&operation=search&id="+eventId;
						document.forms[0].action = url;
				        document.forms[0].submit();
					} 				

					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "derivedSpecimenGrid";	
					var gridFor="<%=Constants.GRID_FOR_DERIVED_SPECIMEN%>";
				</script>
				<%@ include file="/pages/content/search/GridPage.jsp" %>
			</td>
		</tr>
</table>
	</td>
	</tr>
	<tr>
		 <td width="100%" align="right">
		 	    <html:button property="closebutton" styleClass="actionButton" onclick="closeWindow();">
				<bean:message key="buttons.close"/>
				</html:button>
		 </td>
        </tr>
	
	</table>
<% } 
%>
		
	
	                 
	
	<%
	
	if(request.getAttribute("showDerivedPage")==null)
	{
	%>

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="580">
		   
		   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<%
						String changeAction = "setFormAction('MakeParticipantEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
				 	%>
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   			<bean:message key="<%=editViewButton%>"/>
						   		</html:submit>
						   	</td>
							<%-- td>
								<html:reset styleClass="actionButton">
									<bean:message key="buttons.export"/>
								</html:reset>
							</td --%>
						</tr>
					</table>
					<!-- action buttons end -->
				  </td>
				  </tr>
				</table>
			   </td>
			</tr>
			</logic:equal>
			
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="580">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="ForwardTo"/>
						<!-- While coming from Ordering System----Added by Ashish 1/2/07 -->
						<%						
						if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
							{%>
								 <html:hidden property="forwardTo" value="orderDetails"/>								
						  <%}else
						    { %>
								 <html:hidden property="forwardTo" value="eventParameters"/>
						   <%} %>
						<html:hidden property="multipleSpecimen" value="<%=multipleSpecimen%>"/>
						<html:hidden property="containerId" styleId="containerId"/>
						<td></td>
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="positionInStorageContainer" />
					</td>
				  </tr>
				 
				<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				 <tr>
					<td>
						<html:hidden property="sysmtemIdentifier"/>
					</td>
				 </tr>
				</logic:equal>

				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>
				 	</logic:notEqual>
				 <tr>
				     <td class="formTitle" height="20" colspan="6">
				     <%String title = "createSpecimen.derived.title";%>
				     	<bean:message key="<%=title%>"/>
				     </td>
				 </tr>
				 
				<%				if(!multipleSpecimen.equals("1"))
					{
        			 %>	
				 <tr>
			     	<td class="formRequiredNoticeNoBottom" width="5">*</td>
				    <td class="formRequiredLabelLeftBorder" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)"  >
				    &nbsp;
			        </html:radio>
						<label for="specimenCollectionGroupId">
							<bean:message key="createSpecimen.parentLabel"/>
						</label>
					</td>
					<td class="formField" colspan="2">
						
					<logic:equal name="createSpecimenForm" property="checkedButton" value="1">
				     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="false" />
			        </logic:equal>
			
			        <logic:equal name="createSpecimenForm" property="checkedButton" value="2">
			 	     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="true" />
			        </logic:equal>
					
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formRequiredLabel" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
				    &nbsp;
			        </html:radio>
						<label for="specimenCollectionGroupId">
							<bean:message key="createSpecimen.parentBarcode"/>
						</label>
					</td>
					<td class="formField" colspan="2">
					
					<logic:equal name="createSpecimenForm" property="checkedButton" value="1">
				    <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenBarcode" property="parentSpecimenBarcode" disabled="true" />
			        </logic:equal>
			
			        <logic:equal name="createSpecimenForm" property="checkedButton" value="2">
				    <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenBarcode" property="parentSpecimenBarcode" disabled="false" />
			        </logic:equal>
										
		        	</td>
				 </tr>
				<%}%> 
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
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="50"  styleId="label" property="label" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="className">
				     		<bean:message key="specimen.type"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="2">
					
					 <autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="<%=readOnlyForAll + ""%>"
									    />

		        	</td>
				 </tr>
				 
				 <tr>
				    <td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="type">
				     		<bean:message key="specimen.subType"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="2">
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
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')";
					%>
				    <!-- --------------------------------------- -->
					
					  <autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>"
										  onChange="<%=subTypeFunctionName%>"
										  readOnly="<%=subListEnabled + ""%>"
										  dependsOn="<%=form.getClassName()%>"
					        />
					
					
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
				    <%
						if(unitSpecimen.equals(Constants.UNIT_MG))
						{
					%>
				    		<td class="formField" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="false"/>
								&nbsp;<bean:message key="specimen.concentrationUnit"/>
				   			</td>
				    <%
						}
						else
						{
					%>
							<td class="formField" colspan="2">
				     			<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="concentration" property="concentration" readonly="<%=readOnlyForAll%>" disabled="true"/>
				     			&nbsp;<bean:message key="specimen.concentrationUnit"/>
				    		</td>
					<%
						}
					%>
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
				    <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
				     	<span id="unitSpan"><%=unitSpecimen%></span>
				     	<html:hidden property="unit"/>
				    </td>
				 </tr>
				
				<% // storage location should not be shown in case of multiple specimen
				   if(!multipleSpecimen.equals("1"))
				   {
				%>
				
				<tr>
				 	<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
					   <label for="className">
					   		<bean:message key="specimen.positionInStorageContainer"/>
					   </label>
					</td>
					
					<%-- n-combo-box start --%>
					<%
						Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
							
						String[] labelNames = {"ID", "Pos1", "Pos2"};
						labelNames = Constants.STORAGE_CONTAINER_LABEL;
						String[] attrNames = {"storageContainer", "positionDimensionOne", "positionDimensionTwo"};
						
					//String[] initValues = new String[3];
					//initValues[0] = form.getStorageContainer();
					//initValues[1] = form.getPositionDimensionOne();
					//initValues[2] = form.getPositionDimensionTwo();
						String[] initValues = new String[3];
							List initValuesList = (List)request.getAttribute("initValues");
							String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
							if(initValuesList != null)
							{
								initValues = (String[])initValuesList.get(0);
							}
					//System.out.println("NewSpecimen :: "+initValues[0]+"<>"+initValues[1]+"<>"+initValues[2]);			
						String rowNumber = "1";
						String styClass = "formFieldSized5";
						String tdStyleClass = "customFormField";
						String onChange = "onCustomListBoxChange(this)";
					
					String className = (String) request.getAttribute(Constants.SPECIMEN_CLASS_NAME);
					if (className==null)
						className="";

					String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
					if (collectionProtocolId==null)
						collectionProtocolId="";
					
					String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
						+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
						+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;				
				  //  String buttonOnClicked  = "window.open('"+url+"','','scrollbars=yes,menubar=no,height=320,width=810,resizable=yes,toolbar=no,location=no,status=no');return false";
    				String buttonOnClicked = "mapButtonClickedOnSpecimen('"+url+"')";  
					String noOfEmptyCombos = "3";
					boolean disabled = false;
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
					<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
					
					<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
									
				<td class="formField" colSpan="4">
						
						<table border="0">
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
						<tr>
							<td ><html:radio value="1" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
							<td class="formFieldNoBorders">																			
									<bean:message key="specimen.virtuallyLocated" />											
							</td>
						</tr>
						<tr>
							<td ><html:radio value="2" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
							<td>
								<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
									attributeNames="<%=attrNames%>" 
									initialValues="<%=initValues%>"  
									styleClass = "<%=styClass%>" 
									tdStyleClass = "<%=tdStyleClass%>" 
									labelNames="<%=labelNames%>" 
									rowNumber="<%=rowNumber%>" 
									onChange = "<%=onChange%>"
									formLabelStyle="formLabelBorderless"
									tdStyleClassArray="<%=tdStyleClassArray%>"
									disabled = "<%=dropDownDisable%>"
									noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
									</tr>
									</table>
							</td>
						</tr>
						<tr>
							<td ><html:radio value="3" onclick="onRadioButtonGroupClickForDerived(this)" styleId="stContSelection" property="stContSelection"/></td>
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
						
						<!--
						<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">								

						<%
							if(form.getStContSelection() == 1)
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
						-->
						</table>					
				</td>	
				<%-- n-combo-box end --%>
				
				 </tr>
				 <%}%>
					<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
								<bean:message key="container.maxView"/>
						</td>
					</tr>
					</logic:equal>

				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="barcode">
							<bean:message key="specimen.barcode"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
						<html:text styleClass="formFieldSized"  maxlength="50" size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
						<label for="comments">
							<bean:message key="specimen.comments"/>
						</label>
					</td>
				    <td class="formField" colspan="2">
				    	<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				  				<%@ include file="ExternalIdentifiers.jsp" %>
				 </table>
				 
			 <!-- Bio-hazards End here -->
			 	<%				if(!multipleSpecimen.equals("1"))
					{
      			 %>	
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">				 			
				<table cellpadding="4" cellspacing="0">
					<tr>					
						<td class="formFieldNoBordersBold" height="20" colspan="5">
						
						<%--<html:checkbox property="aliCheckedButton" onclick="onCheckboxButtonClick(this)">
						&nbsp; <bean:message key="specimen.aliquot.message"/>
						</html:checkbox>--%>
						<input type="checkbox" name="aliCheckedButton" onclick="onCheckboxButtonClick(this)" /> &nbsp; <bean:message key="specimen.aliquot.message"/>
	    				</td>
					</tr>								
					
				</table>
				</logic:notEqual>
				<%}%>
								
 			   	 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">		
				 	<tr>
				  		<td align="right" colspan="4">
							<%
								String changeAction = "setFormAction('"+formName+"')";
								String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
								String submitAndDistribute = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
								String addMoreSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";
								String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;		

				 			%>
							<!-- action buttons begins -->
				<%				if(!multipleSpecimen.equals("1"))
					{
        			 %>			
							
							<table cellpadding="4" cellspacing="0">
								<tr>
						   			<td>
						   				<html:button styleClass="actionButton" property="submitButton" onclick="onNormalSubmit()">
						   					<bean:message key="buttons.submit"/>
						   				</html:button>
						   			</td>
									
						
									<td class="formFieldNoBorders" nowrap>
										<html:button
											styleClass="actionButton" property="submitAndDistributeButton"
											title="<%=Constants.SPECIMEN_BUTTON_TIPS[4]%>"
											value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[4][0]%>"
											onclick="<%=submitAndDistribute%>">
										</html:button>
									</td>
									<td class="formFieldNoBorders" nowrap>
										<html:button
											styleClass="actionButton" property="moreButton"
											title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
											value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
											onclick="<%=addMoreSubmit%>"
			>
			</html:button>
		</td>
					<%
					}
					else
					{
					%>
								<table cellpadding="4" cellspacing="0" width="100%">
								<tr>
						   			<td align="right">
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td width="2%">
						   				&nbsp;
						   			</td>

              <%
			  }
			  %>								
									<%-- <td colspan="3">
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> --%> 
									<%--td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.moreSpecimen"/>
										</html:reset>
									</td--%>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 </logic:notEqual>
				 
				</logic:notEqual>				
			 
			 <!-- NEW SPECIMEN REGISTRATION ends-->
	</table>
	<%
	}
	%>

	
 </html:form>
	