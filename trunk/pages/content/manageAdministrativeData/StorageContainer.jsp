<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
		//StorageContainerForm form = (StorageContainerForm)request.getAttribute("storageContainerForm");
		
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String containerNumber=(String)request.getAttribute("ContainerNumber");
        String formName;
		List siteForParent = (List)request.getAttribute("siteForParentList");
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
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

		Map map = null;
		String label1 = null;
		String label2 = null;
		
		StorageContainerForm form;
		if(obj != null && obj instanceof StorageContainerForm)
		{
			form = (StorageContainerForm)obj;
			//map = form.getValues();

			label1 = form.getOneDimensionLabel();
			label2 = form.getTwoDimensionLabel();

			if(label1 == null)
			{
				label1 = "Dimension One";
				label2 = "Dimension Two";
			}
		}else
		{
			form = (StorageContainerForm)request.getAttribute("storageContainerForm");
		}
		
		int siteOrContainerSelected = form.getCheckedButton();
		int dropdownOrTextboxSelected = form.getStContSelection();
		
		boolean dropDownDisable = false;
		boolean textBoxDisable = false;		
		boolean containerRadioDisable = false;
		
		
		if(siteOrContainerSelected == 1)
		{
			dropDownDisable = true;
			textBoxDisable = true;			
			containerRadioDisable = true;
		}
		else if(siteOrContainerSelected == 2)
		{				
			if(dropdownOrTextboxSelected == 1)
			{
				textBoxDisable = true;
				dropDownDisable = false;	
			}								
			else if(dropdownOrTextboxSelected == 2)
			{
				dropDownDisable = true;	
				textBoxDisable = false;
			}
		}
		
%>

<head>
<style>
	.hidden
	{
	 display:none;
	}

</style>
	<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		var parentIdArray = new Array();
		var siteNameArray = new Array();
		<%
			if(siteForParent != null) 
			{
			for(int i=0; i<siteForParent.size();i++)
    		{
    			NameValueBean nvb = (NameValueBean) siteForParent.get(i);
		%>
				parentIdArray[<%=i%>] = "<%=nvb.getValue()%>";
				siteNameArray[<%=i%>] = "<%=nvb.getName()%>";
    	<%	}}%>
		
		function checkNoOfContainers(action,formField)
		{
			var operation = "<%=operation%>";
			//alert("operation "+operation);
			if(operation == "add")
			{
				if(document.forms[0].noOfContainers.value > 1)
				{
					action = "<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>";
					action = action + "?pageOf="+"<%=Constants.PAGEOF_CREATE_SIMILAR_CONTAINERS%>"+"&menuSelected=7";
				}
			}
			confirmDisable(action,formField);
		}

		function onRadioButtonClick(element)
		{
			var radioArray = document.getElementsByName("stContSelection");		 
			//if site radio button is selected.
			if(element.value == 1)
			{
			  
				document.forms[0].displaysiteId.disabled = false;
            //    document.forms[0].displaysiteId.readOnly = false;
				document.forms[0].customListBox_1_0.disabled = true;
				document.forms[0].customListBox_1_1.disabled = true;
				document.forms[0].customListBox_1_2.disabled = true;				
				document.forms[0].containerMap.disabled=true;

				document.forms[0].stContSelection[0].disabled=true;
				document.forms[0].stContSelection[1].disabled=true;

				document.forms[0].selectedContainerName.disabled = true;
				document.forms[0].pos1.disabled = true;
				document.forms[0].pos2.disabled = true;
				document.forms[0].stContSelection.disabled=true;
			}
			else //if parent container radio button is selected.
			{
			
				document.forms[0].displaysiteId.disabled = true;
				document.forms[0].stContSelection[0].disabled=false;
				document.forms[0].stContSelection[1].disabled=false;

				if (radioArray[0].checked) 
				{
				   
					document.forms[0].customListBox_1_0.disabled = false;
					document.forms[0].customListBox_1_1.disabled = false;
					document.forms[0].customListBox_1_2.disabled = false;
					document.forms[0].containerMap.disabled=true;
					document.forms[0].selectedContainerName.disabled=true;
					document.forms[0].pos1.disabled=true;
					document.forms[0].pos2.disabled=true;					
				} 
				else 
				{
				  
					document.forms[0].customListBox_1_0.disabled = true;
					document.forms[0].customListBox_1_1.disabled = true;
					document.forms[0].customListBox_1_2.disabled = true;
					document.forms[0].containerMap.disabled=false;
					document.forms[0].selectedContainerName.disabled=false;
					document.forms[0].pos1.disabled=false;
					document.forms[0].pos2.disabled=false;					
				}
				
				onParentContainerChange(element)
				//window.location.reload();
			}
		}
		
		function onTypeChange(element)
		{
			var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&typeChange=true";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		function ResetName(element)
		{
			var containerNameElement = document.getElementById("containerName");
			containerNameElement.value = "";	
			if(document.forms[0].checkedButton[0].checked==true)
			{
				onSiteChange();
			}
			else
			{
				onParentContainerChange(element);
			}	
		}
		function onSiteChange()
		{
			var typeElement = document.getElementById("typeId");
			var siteElement = document.getElementById("displaysiteId");
			var containerNameElement = document.getElementById("containerName");
			if(typeElement.value != "-1" && siteElement.value != "-1" && containerNameElement.value == "")
			{
				//Poornima:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
				//and it is giving error. So these fields are truncated in case it is longer than 40.
				//It also solves Bug 2829:System fails to create a default unique storage container name
				var maxSiteName = siteElement.value;
				var maxTypeName = typeElement.options[typeElement.selectedIndex].text;
				if(maxSiteName.length>40)
				{
					maxSiteName = maxSiteName.substring(0,39);
				}
				if(maxTypeName.length>40)
				{
					maxTypeName = maxTypeName.substring(0,39);
				}
				nameChange(maxSiteName,maxTypeName,<%=containerNumber%>);
			}
		}
		function onParentContainerChange(element)
		{
			var typeElement = document.getElementById("typeId");
			var containerName = document.getElementById("containerName");
			var radioArray = document.getElementsByName("stContSelection");				
			var parentContainer;				
			if(radioArray[0].checked)
			{
				parentContainer = document.getElementById("customListBox_1_0");				
			}
			else
			{
				parentContainer = document.getElementById("selectedContainerName");				
			}
			
			if(typeElement.value != "-1" && parentContainer.value != "-1" && containerName.value == "")
			{
				getSiteName(parentContainer.value);
				var siteName = document.forms[0].siteForParentContainer.value;

				//Poornima:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
				//and it is giving error. So these fields are truncated in case it is longer than 40.
				//It also solves Bug 2829:System fails to create a default unique storage container name
				var maxSiteName = siteName;
				var maxTypeName = typeElement.options[typeElement.selectedIndex].text;
				if(maxSiteName.length>40)
				{
					maxSiteName = maxSiteName.substring(0,39);
				}
				if(maxTypeName.length>40)
				{
					maxTypeName = maxTypeName.substring(0,39);
				}
				nameChange(siteName,maxTypeName,<%=containerNumber%>);
			}	
			//out of three drop downs if first dropdown that is the storage container name drop is changed
			//then make a server trip to get all CPs 
			if(element.name == "parentContainerId")
			{
				var action = "StorageContainer.do?operation="+document.forms[0].operation.value+"&pageOf=pageOfStorageContainer&isSiteOrParentContainerChange=true";
				document.forms[0].action = action;
				document.forms[0].submit();
			}	
			
		}
		
		
		function nameChange(siteName,typeName,Id)
		{
			if(siteName!="-1" && typeName!="-1")
			{
				document.forms[0].containerName.value=siteName+"_"+typeName+"_"+Id;
			}
		}	
		function getSiteName(id)
		{

			for(var i=0; i<parentIdArray.length; i++)
			{
				if(id == parentIdArray[i])
				{
					document.forms[0].siteForParentContainer.value = siteNameArray[i];
					break;
				}
			}		
					
		}	
		function setContainerName(maxSiteName)
			{
				var containerName = document.getElementById("containerName");		
				var containerNumber = "<%=containerNumber%>";	
				var typeElement =  document.getElementById("typeId");				
				var maxTypeName =  typeElement.options[typeElement.selectedIndex].text;
				if(maxTypeName != "-1" && maxSiteName != "-1" && containerName.value=="")
				{		
					//Jitendra:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
					//and it is giving error. So these fields are truncated in case it is longer than 40.
					//It also solves Bug 2829:System fails to create a default unique storage container name
					if(maxSiteName>40)
					{
						maxSiteName = maxSiteName.substring(0,39);
					}
					if(maxTypeName.length>40)
					{
						maxTypeName = maxTypeName.substring(0,39);
					}					
					containerName.value=maxSiteName+"_"+maxTypeName+"_"+containerNumber;
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
	var identifier = "value(StorageContainerDetails:" + (q+1) +"_id)";
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

function validate(action,formField)
{
	/*if(document.forms[0].checkedButton[1].checked==true)
	{
		if(document.forms[0].customListBox_1_0.value == -1 || document.forms[0].customListBox_1_1.value == -1 || document.forms[0].customListBox_1_2.value == -1)
		{
			alert("Please enter valid Parent Container");
			return false;
		}
	}*/
	
	if(validateAny(document.forms[0].collectionIds)==false)
	{
		alert("Please select Proper Collection Protocols");
	}
	else
	{
		if(validateAny(document.forms[0].holdsStorageTypeIds)==false)
		{
			alert("Please select Proper Storage Types");
		}
		else
		{	
			if(validateAny(document.forms[0].holdsSpecimenClassTypes)==false)
			{
				alert("please select Proper Specimen Classes");
			}
			else
			{	
				checkNoOfContainers(action,formField);
			}
		}
	}	
}

function onRadioButtonClickOfSpecimen(element)
{
	var specimenClass = document.getElementById("holdsSpecimenClassTypeIds");
	var specimenArray = document.getElementById("holdsSpecimenArrTypeIds");
	
	if(element == "Specimen")
	{
		specimenClass.disabled = false;
		specimenArray.disabled = true;
		var len = specimenArray.length;
		for (var i = 0; i < len; i++) 
		{
			specimenArray.options[i].selected = false;
		}
		
	}
	if(element == "SpecimenArray")
	{
		specimenClass.disabled = true;
		specimenArray.disabled = false;
		var len = specimenClass.length;
		for (var i = 0; i < len; i++) 
		{
			specimenClass.options[i].selected = false;
		}
	}
		
}

function onEditChange()
{

	var ele0 = document.getElementById("customListBox_1_0");
	var ele1 = document.getElementById("customListBox_1_1");
	var ele2 = document.getElementById("customListBox_1_2");
	var operation = "<%=operation%>";
	if(operation == "edit" && document.forms[0].checkedButton[1].checked==true)
	{
		ele0.remove(0);
		ele1.remove(0);
		ele2.remove(0);
	}
}
	</script>
</head>



<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>" method="post">	

	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="750">
	<!-- NEW STORAGE CONTAINER REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="containerNumber" value="<%=containerNumber%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>	
						<input type="hidden" name="radioValue">
						<html:hidden property="containerId" styleId="containerId"/>
					</td>
				</tr>
				<tr>
					<td><html:hidden property="id" />
						<html:hidden property="typeName"/>
						<html:hidden property="siteName"/>
					</td>
				</tr>
				<tr>
					<td><html:hidden property="positionInParentContainer" />
						<html:hidden property="siteForParentContainer"/>

					</td>
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
								<bean:message key="storageContainer.editTitle"/>
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
						<td class="formRequiredLabelLeftBorder" colspan=2>
							<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)" >
								<label for="siteId">
									<bean:message key="storageContainer.site" />
								</label>
							</html:radio>
						</td>
						<td class="formField" colspan="2">
							<logic:equal name="storageContainerForm" property="checkedButton" value="1">

								 <autocomplete:AutoCompleteTag property="siteId"
										  optionsList = "<%=request.getAttribute(Constants.SITELIST)%>"
										  initialValue="<%=form.getSiteId()%>"
										  styleClass="formFieldSized15"
										  staticField="false"
										  onChange="onSiteChange()"
									    />
 
							</logic:equal>
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
							
							 <autocomplete:AutoCompleteTag property="siteId"
										  optionsList = "<%=request.getAttribute(Constants.SITELIST)%>"
										  initialValue="<%=form.getSiteId()%>"
										  styleClass="formFieldSized15"
										  staticField="false"
										  onChange="onSiteChange()"
										  disabled="true"
									    />
													
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
						
						<%-- n-combo-box start --%>
						<%
							Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
							
							session.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,dataMap);							

							String[] initValues = new String[3];
							List initValuesList = (List)request.getAttribute("initValues");
							if(initValuesList != null)
							{
								initValues = (String[])initValuesList.get(0);
							}

							// labelNames = {"Name","Pos1","Pos2"};
							String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;
							String[] attrNames = { "parentContainerId", "positionDimensionOne", "positionDimensionTwo"};
							String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"}; 
							
							//String[] initValues = new String[3];
							//initValues[0] = Integer.toString((int)form.getParentContainerId());
							//initValues[0] = form.getPositionInParentContainer();
							//initValues[1] = Integer.toString(form.getPositionDimensionOne());
							//initValues[2] = Integer.toString(form.getPositionDimensionTwo());
							
							String rowNumber = "1";
							String styClass = "formFieldSized5";
							String tdStyleClass = "formField";
							boolean disabled = true;
							String onChange = "";
							onChange = "onCustomListBoxChange(this),onParentContainerChange(this)";
							//String onChange = "onCustomListBoxChange(this);onParentContainerChange()";
							boolean buttonDisabled = true;
							//String buttonOnClicked  = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";							
							String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId&amp;storageContainer=true&amp;storageType=";							
							String buttonOnClicked = "javascript:StorageMapWindow('"+frameUrl+"','name','810','320','yes');return false";
							
							String noOfEmptyCombos = "3";
							
						/*	 int radioSelected = form.getStContSelection();
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
								*/
							
							//String buttonId = "Map_1";			
							
						%>
					
						<%=ScriptGenerator.getJSForOutermostDataTable()%>
						<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
					
						<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
						<td class="formField" colSpan="2">
							<table border="0">									
								<tr>
									<td ><html:radio value="1" onclick="onStorageRadioButtonClick(this)" styleId="stContSelection" property="stContSelection" disabled= "<%=containerRadioDisable%>"/></td>
									<td>
										<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClassArray="<%=tdStyleClassArray%>"
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>"
											formLabelStyle="formLabelBorderless"
											disabled = "<%=dropDownDisable%>"
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
											</tr>
											</table>
									</td>
								</tr>
								<tr>
									<td ><html:radio value="2" onclick="onStorageRadioButtonClick(this)" styleId="stContSelection" property="stContSelection" disabled= "<%=containerRadioDisable%>"/></td>
									<td class="formLabelBorderlessLeft">
										<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos1" property="pos1" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos2" property="pos2" disabled= "<%=textBoxDisable%>"/>
										<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "<%=textBoxDisable%>">
											<bean:message key="buttons.map"/>
										</html:button>
									</td>
								</tr>								
							</table>
						</td>							
					</tr>
					<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
							<bean:message key="container.maxView"/>
						</td>
					</tr>	
					</logic:equal>
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="containerName">
								<bean:message key="storageContainer.containerName" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized15" maxlength="255"  size="30" styleId="containerName" property="containerName"/>
							&nbsp;
							<html:link href="#" styleId="newSite" onclick="ResetName(this)">
								<bean:message key="StorageContainer.resetName" />
							</html:link>
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
							<html:text styleClass="formFieldSized10"  maxlength="10" size="30" styleId="noOfContainers" property="noOfContainers" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					</logic:notEqual>
					
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="barcode">
								<bean:message key="storageContainer.barcode" />
							</label>
						</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" maxlength="255"  size="30" styleId="barcode" property="barcode"/>
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
							<%--<html:select property="isFull" styleClass="formFieldSized10" styleId="isFull" size="1" 
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.IS_CONTAINER_FULL_LIST%>" labelName="<%=Constants.IS_CONTAINER_FULL_LIST%>" />
							</html:select>--%>
							<% boolean disabledChkBox = false;%>
							<logic:equal property="isFull" name="storageContainerForm" value="true">
							<% disabledChkBox = true;%>
							</logic:equal>
							<html:checkbox property="isFull" value="true" disabled="<%=disabledChkBox%>"/>
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
					<tr>
						<td class="formTitle" colspan="5">
							<label for="restrictions">
								<bean:message key="storageContainer.restrictions" />
							</label>
						</td>
					</tr>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="collection_protocol_id">
								<bean:message key="storageContainer.collectionProtocolTitle" />
							</label>
						</td>
						<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="collectionIds" styleClass="formFieldSized" styleId="collectionIds" size="4"
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
						<table>
							<tr><td class="standardText" align="center">
									<bean:message key="storageContainer.containerType"/>
							</td>
							<td class="standardText" align="center">		
									<html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClickOfSpecimen('Specimen')"/> <bean:message key="storageContainer.specimenClass"/>
							</td>
							<td class="standardText" align="center">		
									<html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClickOfSpecimen('SpecimenArray')"/> <bean:message key="storageContainer.specimenArrayType"/>
							</td>
							</tr>
							<tr>
							<td class="formField" align="center">		
							<html:select property="holdsStorageTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsStorageTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST1%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
						<td class="formField" align="center">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenClassTypes" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray">
							<html:select property="holdsSpecimenClassTypes" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
								<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
						</td>
						<td class="formField" align="center">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
								<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							
						</td>
						</tr></table>
						
						</td>
					</tr>
					

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
					<tr>
						<td colspan="5" align="right">
								<%
						   			String action = "validate('" + formName +"',document.forms[0].activityStatus)";
						   		%>
						   			<html:button styleClass="actionButton" property="submitPage" onclick="<%=action%>">
						   				<bean:message key="buttons.submit"/>
						   			</html:button>
				   		</td>
						
					</tr>
				</table>
				
			<!-- /td-->
		</tr>

		<!-- NEW STORAGE CONTAINER REGISTRATION ends-->
	</table>
</html:form>