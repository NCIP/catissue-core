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


<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<head>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>
<script src="runtime/formats/date.js"></script>
<script src="runtime/formats/string.js"></script>
<script src="runtime/formats/number.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<script language="JavaScript">
	
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
			spreqno.innerHTML="" + rowno + ".";
		
			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			spreqtype.colSpan=1;
			sname="";
			
			var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
			sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized10' id='" + name + "'>";      
		
		
			spreqtype.innerHTML="" + sname;
		
			//Third Cell
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			spreqsubtype.colSpan=1;
			sname="";
		
			name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
			sname= "";
			
			sname="<input type='text' name='" + name + "' maxlength='50'  class='formFieldSized10' id='" + name + "'>"   
		
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_"+ rowno;
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].exIdCounter,'chk_')\">";
			checkb.innerHTML=""+sname;
		}
		
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

function classChangeForMultipleSpecimen()
{
 	var action ='NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&specimenAttributeKey=' + document.getElementById("specimenAttributeKey").value + '&derivedSpecimenCollectionGroup=' + document.getElementById("derivedSpecimenCollectionGroup").value + '&retainForm=true';
	document.forms[0].action = action;
	document.forms[0].submit();
}
			
	  function onClassOrLabelOrBarcodeChange(multipleSpecimen)
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
     else {
			if (document.getElementById("parentSpecimenBarcode").value != "") 
			{
	     	     flag = "1";
			}
		}
 	    	var classNameElement = document.getElementById("className");
			if(flag=="1" && classNameElement.value != "-1")
			{
		
				var action = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&Change=true";
				document.forms[0].action = action;
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
				containerName.disabled = false;
				pos1.disabled = false;;
				pos2.disabled = false;;
				document.forms[0].mapButton.disabled = false;
				
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
	</script>
</head>

<% 
        String[] columnList = Constants.DERIVED_SPECIMEN_COLUMNS;
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation!=null && operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.CREATE_SPECIMEN_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.CREATE_SPECIMEN_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation!=null && operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		Object obj = request.getAttribute("createSpecimenForm");
		int exIdRows=1;
		
		CreateSpecimenForm form = null;
		String unitSpecimen = "";
		Map map = null;
		if(obj != null && obj instanceof CreateSpecimenForm)
		{
			form = (CreateSpecimenForm)obj;
			map = form.getExternalIdentifier();
			exIdRows = form.getExIdCounter();
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
	
	String onChangeFunctionName = "onClassOrLabelOrBarcodeChange(" + multipleSpecimen + ");" ;
	String onClassChangeFunctionName = "onTypeChange(this);" + onChangeFunctionName;
	
%>

<%
List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
if(dataList!=null && dataList.size() != 0)
{
%>

<script>
var myData = [<%int xx;%><%for (xx=0;xx<(dataList.size()-1);xx++){%>
<%
	List row = (List)dataList.get(xx);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"],<%}%>
<%
	List row = (List)dataList.get(xx);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"]
];

var columns = [<%int k;%><%for (k=0;k < (columnList.length-1);k++){%>"<%=columnList[k]%>",<%}%>"<%=columnList[k]%>"];
</script>

<%}%>



	<html:errors />
   <html:form action="<%=action%>">
   
   <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">
<tr>
	<td>		
		&nbsp;
	</td>
</tr>

<tr>
 <td>
    <table summary="" cellpadding="3" cellspacing="0" border="0" width="550" >

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
				<div STYLE="overflow: auto; width:550; height: 200; padding:0px; margin: 0px; border: 4px solid" id="eventGrid">
				<script>
				
					//	create ActiveWidgets Grid javascript object.
					var obj = new Active.Controls.Grid;
					var string  = new Active.Formats.String;
					var number  = new Active.Formats.Number; 

					number.setTextFormat("#*");
					
					//	set number of rows/columns.
					obj.setRowProperty("count", <%=dataList.size()%>);
					obj.setColumnProperty("count", <%=columnList.length-1%>);
					var formats = [string,string,string,string];
					
					//	provide cells and headers text
	
					obj.setDataText(function(i, j){return myData[i][j]});
					obj.setColumnProperty("text", function(i){return columns[i]});
					obj.sort(3,'descending');
					
					//	set headers width/height.
					obj.setRowHeaderWidth("28px");
					obj.setColumnHeaderHeight("20px");
			
					var row = new Active.Templates.Row;
					row.setEvent("ondblclick", function(){this.action("myAction")}); 
					
					
					obj.setTemplate("row", row);
			 		obj.setAction("myAction", 
						function(src)
						{
						   var action = "NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&rowSelected=" + myData[this.getSelectionProperty("index")][4] +"&specimenAttributeKey="+document.getElementById("specimenAttributeKey").value;
				   		   document.forms[0].action = action;
				           document.forms[0].submit();
						}
						);
						
						
						//var frame = document.getElementById("newEventFrame"); frame.src = 'SearchObject.do?pageOf=' + myData[this.getSelectionProperty("index")][<%=columnList.length-1%>] + '&operation=search&id=' + myData[this.getSelectionProperty("index")][0]}); 
			
					//	write grid html to the page.
					document.write(obj);
				</script>
				</div>
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
		
	
	                   <input type="hidden" id="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
				       name="<%=Constants.SPECIMEN_ATTRIBUTE_KEY%>"
				       value="<%=request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY)%>" />

						<input type="hidden" id="derivedSpecimenCollectionGroup"
				       name="derivedSpecimenCollectionGroup"
				       value="<%=request.getParameter("derivedSpecimenCollectionGroup")%>" />
					   
					   	<input type="hidden" id="rowSelected"
				       name="rowSelected"
				       value="<%=request.getParameter("rowSelected")%>" />
	
	<%
	
	if(request.getAttribute("showDerivedPage")==null)
	{
	%>

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="550">
		   
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
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="550">
				 <tr>
					<td>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="ForwardTo"/>
						<html:hidden property="forwardTo" value="eventParameters"/>
											
						<td><html:hidden property="exIdCounter"/></td>
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
				 
				 <%
					boolean radioDisabled = false;
					if(multipleSpecimen.equals("1"))
					{
					     radioDisabled = true;
					}
				%>
				 <tr>
			     	<td class="formRequiredNoticeNoBottom" width="5">*</td>
				    <td class="formRequiredLabelLeftBorder" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)" disabled="<%=radioDisabled%>" >
				    &nbsp;
			        </html:radio>
						<label for="specimenCollectionGroupId">
							<bean:message key="createSpecimen.parentLabel"/>
						</label>
					</td>
					<td class="formField" colspan="2">
						
					<logic:equal name="createSpecimenForm" property="checkedButton" value="1">
				     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="<%=radioDisabled%>" onblur="<%=onChangeFunctionName%>"/>
			        </logic:equal>
			
			        <logic:equal name="createSpecimenForm" property="checkedButton" value="2">
			 	     <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenLabel" property="parentSpecimenLabel" disabled="true" />
			        </logic:equal>
					
		        	</td>
				 </tr>
				 
				 <tr>
			     	<td class="formRequiredNotice" width="5">&nbsp;</td>
				    <td class="formRequiredLabel" width="175">
					<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)" disabled="<%=radioDisabled%>">
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
				    <html:text styleClass="formFieldSized15"  maxlength="50"  size="30" styleId="parentSpecimenBarcode" property="parentSpecimenBarcode" disabled="false" onblur="<%=onChangeFunctionName%>"/>
			        </logic:equal>
										
		        	</td>
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
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="className" styleClass="formFieldSized15" styleId="className" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=onClassChangeFunctionName%>">
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
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="type" styleClass="formFieldSized15" styleId="type"
				     	 size="1" disabled="<%=subListEnabled%>"
				     	 onchange="<%=subTypeFunctionName%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
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
					
					String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=customListBox_1_0&amp;xDimStyleId=customListBox_1_1&amp;yDimStyleId=customListBox_1_2"
						+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
						+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;				

					String buttonOnClicked  = "javascript:NewWindow('"+url+"','name','810','320','yes');return false";
					
						String noOfEmptyCombos = "3";
					boolean disabled = false;
					if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
					{
						disabled = true;
					}	

					%>
				
					<%=ScriptGenerator.getJSForOutermostDataTable()%>
					<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
					
					<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
									
				<td class="formField" colSpan="4">
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<html:checkbox property="virtuallyLocated" onclick="setVirtuallyLocated(this)"/>
							<bean:message key="specimen.virtuallyLocated" />
						</logic:equal>	
						<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<html:checkbox property="virtuallyLocated" styleClass="hidden"/>
							<%
								CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) request.getAttribute("createSpecimenForm");
								if(createSpecimenForm != null && createSpecimenForm.isVirtuallyLocated())
								{%>Specimen is virtually Located <%}
							
							%>
						</logic:notEqual>	
								
									<ncombo:containermap dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>"
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
				 
				 <tr>
				     <td class="formTitle" height="20" colspan="2" nowrap>
				     	<bean:message key="specimen.externalIdentifier"/>
				     </td>
				     <td class="formButtonField">
				     	<html:button property="addExId" styleClass="actionButton" onclick="insExIdRow('addExternalIdentifier')">
				     		<bean:message key="buttons.addMore"/>
				     	</html:button>
				    </td>
				    <td class="formTitle" align="Right">
							<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addExternalIdentifier','CreateSpecimen.do?operation=<%=operation%>&pageOf=&status=true',document.forms[0].exIdCounter,'chk_',false)" disabled="true">
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
					    <td class="formRightSubTableTitle">
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
						String exIdentifier = "externalIdentifierValue(ExternalIdentifier:" + i +"_id)";
						String check = "chk_"+i;
				  %>
					<tr>
					 	<td class="formSerialNumberField" width="5"><%=i%>.
					 		<html:hidden property="<%=exIdentifier%>" />
					 	</td>
					    <td class="formField">
				     		<html:text styleClass="formFieldSized10"  maxlength="50" styleId="<%=exName%>" property="<%=exName%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<td class="formField">
				     		<html:text styleClass="formFieldSized10"  maxlength="50" styleId="<%=exValue%>" property="<%=exValue%>" readonly="<%=readOnlyForAll%>"/>
				    	</td>
				    	<%
							String key = "";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteValue,document.forms[0].exIdCounter,'chk_')">		
						</td>
					 </tr>
				  <% } %>
				 </tbody>
				 </table>
			 <!-- Bio-hazards End here -->

 			   	 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">		
				 	<tr>
				  		<td align="right" colspan="4">
							<%
								String changeAction = "setFormAction('"+formName+"')";
								String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
								String submitAndDistribute = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
				 			%>
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
						   			<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td class="formFieldNoBorders" nowrap>
										<html:button
											styleClass="actionButton" property="submitAndDistributeButton"
											title="<%=Constants.SPECIMEN_BUTTON_TIPS[4]%>"
											value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[4][0]%>"
											onclick="<%=submitAndDistribute%>">
										</html:button>
									</td>
									
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
	