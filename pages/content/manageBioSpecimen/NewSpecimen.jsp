<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.NewSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>


<%
String bhIdArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_ID_LIST);
String bhNameArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_NAME_LIST);
String bhTypeArray [] = (String []) request.getAttribute(Constants.BIOHAZARD_TYPES_LIST);

List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
NewSpecimenForm form = (NewSpecimenForm)request.getAttribute("newSpecimenForm");
Map map = form.getExternalIdentifier();
%>
<head>

<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">

	
		var win = null;
		
		function onSpecimenEventUpdate(element)
		{
			var identifier = "<%=form.getSystemIdentifier()%>";
			var action = "/catissuecore/ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&specimenId=" + identifier;
			document.forms[0].action = action;
			document.forms[0].submit();
		}

		// for create from existing
		function onCreateFromExisting(element)
		{
			var identifier = "<%=form.getSystemIdentifier()%>";
			var action = "/catissuecore/CreateSpecimen.do?operation=add&pageOf="+"&specimenId=" + identifier;
			document.forms[0].action = action;
			document.forms[0].submit();
		}

		
		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}
	
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
		
		
		function onParameterChange(element)
		{
			<% if(form != null) { %>
				var specimenIdentifier = "<%=form.getSystemIdentifier()%>";
				var fromPositionData = "<%=form.getPositionInStorageContainer()%>";
				var posOne = "<%=form.getPositionDimensionOne()%>";
				var posTwo = "<%=form.getPositionDimensionTwo()%>";
				var storContId = "<%=form.getStorageContainer()%>";
				var subTypeData = "<%=form.getType()%>";
				var isRNA="false";
				if (subTypeData=="RNA")
					isRNA = "true";
				
			<% } %>
			
			var action = "";
			var addNew = document.getElementById("sepAdd");
			addNew.target="_blank";
			
			if(element.value == "Cell Specimen Review")
				action = "/catissuecore/CellSpecimenReviewParameters.do?operation=add&pageOf=pageOfCellSpecimenReviewParameters";
			else if(element.value == "Check In Check Out")
				action = "/catissuecore/CheckInCheckOutEventParameters.do?operation=add&pageOf=pageOfCheckInCheckOutEventParameters";
			else if(element.value == "Collection")
				action = "/catissuecore/CollectionEventParameters.do?operation=add&pageOf=pageOfCollectionEventParameters";
			else if(element.value == "Disposal")
				action = "/catissuecore/DisposalEventParameters.do?operation=add&pageOf=pageOfDisposalEventParameters";
			else if(element.value == "Embedded")
				action = "/catissuecore/EmbeddedEventParameters.do?operation=add&pageOf=pageOfEmbeddedEventParameters";
			else if(element.value == "Fixed")
				action = "/catissuecore/FixedEventParameters.do?operation=add&pageOf=pageOfFixedEventParameters";
			else if(element.value == "Fluid Specimen Review")
				action = "/catissuecore/FluidSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfFluidSpecimenReviewParameters";
			else if(element.value == "Frozen")
				action = "/catissuecore/FrozenEventParameters.do?operation=add&pageOf=pageOfFrozenEventParameters";
			else if(element.value == "Molecular Specimen Review")
				action = "/catissuecore/MolecularSpecimenReviewParameters.do?operation=add&pageOf=pageOfMolecularSpecimenReviewParameters";
			else if(element.value == "Procedure")
				action = "/catissuecore/ProcedureEventParameters.do?operation=add&pageOf=pageOfProcedureEventParameters";
			else if(element.value == "Received")
				action = "/catissuecore/ReceivedEventParameters.do?operation=add&pageOf=pageOfReceivedEventParameters";
			else if(element.value == "Spun")
				action = "/catissuecore/SpunEventParameters.do?operation=add&pageOf=pageOfSpunEventParameters";
			else if(element.value == "Thaw")
				action = "/catissuecore/ThawEventParameters.do?operation=add&pageOf=pageOfThawEventParameters";
			else if(element.value == "Tissue Specimen Review")
				action = "/catissuecore/TissueSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfTissueSpecimenReviewParameters";
			else if(element.value == "Transfer")
			{
				action = "/catissuecore/TransferEventParameters.do?operation=add&pageOf=pageOfTransferEventParameters";
				action = action + "&fromPositionData=" + fromPositionData;			
				action = action + "&posOne=" + posOne;
				action = action + "&posTwo=" + posTwo;
				action = action + "&storContId=" + storContId;			
				
			}	
			action = action + "&specimenId=" + specimenIdentifier;
			action = action + "&isRNA=" + isRNA;
			addNew.href = action;
			
			if(element.value == "<%=Constants.SELECT_OPTION%>")
			{
				addNew.href = "#";
				addNew.target="_parent";
			}
		}
		

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
			sname="<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "'>";      
		
		
			spreqtype.innerHTML="" + sname;
		
			//Third Cell
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=2;
			sname="";
		
			name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
			sname= "";
			
			sname="<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "'>"   
		
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_ex_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"
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
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onchange=onBiohazardTypeSelected(this)>";
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
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='bhId" + (q+1) + "'>";
			sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";			
			sname = sname + "</select>";
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_bio_"+ (q+1);
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"
			checkb.innerHTML=""+sname;
		}
	
	</script>
</head>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
			if(form.getUnit() != null)
				unitSpecimen = form.getUnit();
		}
		

%>

<html:errors />

   <html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">
			   
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
							<td>
								<html:reset styleClass="actionButton">
									<bean:message key="buttons.export"/>
								</html:reset>
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
				  </td>
				  </tr>
				</table>
			   </td>
			</tr>
			</logic:equal>		 
			
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			   	
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
			 	 
				 <tr>
				 	<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
					</td>
					<td>
						<html:hidden property="exIdCounter"/>
					</td>
					<td>
						<html:hidden property="bhCounter"/>
					</td>
					<td>
						<html:hidden property="systemIdentifier"/>
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="storageContainer" />
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="positionDimensionOne" />
					</td>
				 </tr>
				 <tr>
					<td>
						<html:hidden property="positionDimensionTwo" />
					</td>
				 </tr>
				 <tr>
					<td>
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
							<label for="specimenCollectionGroupId">
								<bean:message key="specimen.specimenCollectionGroupId"/>
							</label>
						</td>
						<td class="formField" colspan="4">
				     		<html:select property="specimenCollectionGroupId" styleClass="formFieldSized15" 
				     				styleId="specimenCollectionGroupId" size="1" >
								<html:options collection="<%=Constants.SPECIMEN_COLLECTION_GROUP_LIST%>" 
									labelProperty="name" property="value"/>		
							</html:select>

			        		<a href="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup">
	      						<bean:message key="app.addNew" />
	   						</a>
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
				     		<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>">
								<html:options collection="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
							</html:select>
			        	</td>
		        	</logic:equal>
		        	
				 </tr>
				 <tr>
				 	<td class="formRequiredNotice" width="5">*</td>
				    <td class="formRequiredLabel">
				     	<label for="className">
				     		<bean:message key="specimen.type"/>
				     	</label>
				    </td>
				    <td class="formField" colspan="4">
				     	<html:select property="className" styleClass="formFieldSized15" styleId="className" size="1" disabled="<%=readOnlyForAll%>" onchange="onTypeChange(this)">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
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
						
								if(classValue != null && classValue.equals("Cell"))
									subListEnabled = true;
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								
								String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
					%>
				    <!-- --------------------------------------- -->
				    
				     	<html:select property="type" styleClass="formFieldSized15" styleId="type" size="1"
				     	  disabled="<%=subListEnabled%>"
				     	  onchange="<%=subTypeFunctionName%>" >
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
				     	<html:select property="tissueSite" styleClass="formFieldSized15" styleId="tissueSite" size="1" disabled="<%=readOnlyForAll%>">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
						<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite";
						%>
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','250','330','no');return false">
							<img src="images\Tree.gif" border="0" width="24" height="18">
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
				     	<html:select property="tissueSide" styleClass="formFieldSized15" styleId="tissueSide" size="1" disabled="<%=readOnlyForAll%>">
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
				     	<html:select property="pathologicalStatus" styleClass="formFieldSized15" styleId="pathologicalStatus" size="1" disabled="<%=readOnlyForAll%>">
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
						
						if(unitSpecimen.equals(Constants.UNIT_MG))
							concentrationDisabled = false;
					%>
			     		<html:text styleClass="formFieldSized15" size="30" styleId="concentration" property="concentration" 
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
				     	<html:text styleClass="formFieldSized15" size="30" styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
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
						<html:text styleClass="formFieldSized15" size="30" styleId="availableQuantity" property="availableQuantity" readonly="<%=readOnlyForAll%>" />
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
				 	<td class="formField">
						<html:text styleClass="formFieldSized15" size="30" styleId="positionInStorageContainer" property="positionInStorageContainer" readonly="true"/>
					</td>
					
					<td class="formField" colspan="2">
						<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<html:button property="mapButton" styleClass="actionButton" styleId="Map"
								onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false" >
								<bean:message key="buttons.map"/>
							</html:button>
						</logic:notEqual>&nbsp;
					</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formLabel">
				    	<label for="barcode">
							<bean:message key="specimen.barcode"/>
						</label>
					</td>
				    <td class="formField" colspan="4">
						<html:text styleClass="formFieldSized" size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
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
				    	<html:textarea styleClass="formFieldSized" rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
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
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
 
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
							<html:button property="deleteExId" styleClass="actionButton" onclick="deleteChecked('addExternalIdentifier','/catissuecore/NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false)">
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
				     		<html:text styleClass="formFieldSized15" styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<td class="formField" colspan="2">
				     		<html:text styleClass="formFieldSized15" styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<%
							String exKey = "ExternalIdentifier:" + i +"_systemIdentifier";
							boolean exBool = Utility.isPersistedValue(map,exKey);
							String exCondition = "";
							if(exBool)
								exCondition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=exCondition%>>		
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
							<html:button property="deleteBiohazard" styleClass="actionButton" onclick="deleteChecked('addBiohazardRow','/catissuecore/NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false)">
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
				     		<html:select property="<%=bhType%>" styleClass="formFieldSized15" styleId="<%=bhType%>" size="1" onchange="onBiohazardTypeSelected(this)">
								<html:options collection="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelProperty="name" property="value"/>
							</html:select>
				    	</td>
				    	<td class="formField" colspan="2">
				     		<html:select property="<%=bhId%>" styleClass="formFieldSized15" styleId="<%="bhId" + i%>" size="1">
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
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=biohzCondition%>>		
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
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
						   			<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td colspan="2">
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td>
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
									<td>
										<html:button property="eventButton" styleClass="actionButton" onclick="onSpecimenEventUpdate(this)">
											<bean:message key="buttons.specimenEventParameters"/>
										</html:button>
									</td>
									<td>
										<html:button property="createFromExisting" styleClass="actionButton" onclick="onCreateFromExisting(this)">
											<bean:message key="buttons.createFrom"/>
										</html:button>
									</td>
									</logic:equal>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
	</table>
  </td>
</tr>
<!-- NEW SPECIMEN REGISTRATION ends-->
</table>
</html:form>