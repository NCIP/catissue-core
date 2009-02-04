<!-- 
This JSP page is to create/display aliquots from/of Parent Specimen.
Author : Aniruddha Phadnis
Date   : May 12, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
String pageOf = (String)request.getAttribute(Constants.PAGEOF);
String buttonKey = "";
String parentSPId = "-1";
String CPQuery= (String)request.getAttribute(Constants.CP_QUERY);
String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
if(Constants.PAGEOF_ALIQUOT.equals(pageOf))
{
	buttonKey = "buttons.submit";
}
else if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
{
	buttonKey = "buttons.resubmit";
}
if(request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null )
{
	parentSPId = (String) request.getAttribute(Constants.PARENT_SPECIMEN_ID);
}
%>

<head>
<script src="jss/Hashtable.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>


<%if(CPQuery!= null)
{
	String nodeId="Specimen_";
	if(parentSPId!= null)
	{
		nodeId = nodeId+parentSPId;
    }
%>
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');					
		</script>
<%}%>
<script language="JavaScript">

   function onSubmit()
	{
		var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
		document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=submit";;
		<%if(CPQuery != null){%>
			var CPaction = '<%=Constants.CP_QUERY_CREATE_ALIQUOT_ACTION%>';
			document.forms[0].action = CPaction + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=submit&<%=Constants.PARENT_SPECIMEN_ID%>=<%=parentSPId%>&<%=Constants.CP_QUERY%>=<%=CPQuery%>";
								
		<%}%>
		document.forms[0].submit();
	}
	
	function onRadioButtonClick(element)
	{
		if(element.value == 1)
		{
			document.forms[0].specimenLabel.disabled = false;
			document.forms[0].barcode.disabled = true;
		}
		else
		{
			document.forms[0].barcode.disabled = false;
			document.forms[0].specimenLabel.disabled = true;
		}
	}
	
	function onCreate()
	{
		var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
		document.forms[0].submittedFor.value = "ForwardTo";
		document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=create";
		<%if(CPQuery != null){%>
		var CPaction = '<%=Constants.CP_QUERY_CREATE_ALIQUOT_ACTION%>';
		document.forms[0].action = CPaction + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=create&<%=Constants.PARENT_SPECIMEN_ID%>=<%=parentSPId%>&<%=Constants.CP_QUERY%>=<%=CPQuery%>";
							
		<%}%>
	    document.forms[0].submit();
	}
	
	function onCheckboxClicked() 
	{
	    var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
		document.forms[0].submittedFor.value = "ForwardTo";
		document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=checkbox";
		<%if(CPQuery != null){%>
			var CPaction = '<%=Constants.CP_QUERY_CREATE_ALIQUOT_ACTION%>';
			document.forms[0].action = CPaction + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=15&buttonClicked=checkbox&<%=Constants.PARENT_SPECIMEN_ID%>=<%=parentSPId%>&<%=Constants.CP_QUERY%>=<%=CPQuery%>";
							
		<%}%>
	    document.forms[0].submit();
	}
	
	function setVirtuallyLocated(element)
	{
		var elementId = element.id;
		var index = elementId.indexOf("_");
		var len = elementId.length;
		var substr = elementId.substring(index+1,len);
		
		var customListBox1 = "customListBox_"+substr+"_0";
		var customListBox2 = "customListBox_"+substr+"_1";
		var customListBox3 = "customListBox_"+substr+"_2";

		var containerName = document.getElementById(customListBox1);
		var pos1 = document.getElementById(customListBox2);
		var pos2 = document.getElementById(customListBox3);

		if(element.checked)
		{
			containerName.disabled = true;
			pos1.disabled = true;
			pos2.disabled = true;
			document.forms[0].mapButton[substr-1].disabled = true;
		}
		else
		{
			containerName.disabled = false;
			pos1.disabled = false;
			pos2.disabled = false;
			document.forms[0].mapButton[substr-1].disabled = false;
			
		} 
	}
	
	function onStorageRadioClickInAliquot(element)
	{		
		var index1 =  element.name.lastIndexOf('_');
		var index2 =  element.name.lastIndexOf(')');
		//rowNumber of the element
		var i = (element.name).substring(index1+1,index2);
		//alert("inside the javascript"+i);
		var st1 = "container_" + i + "_0";
		var pos1 = "pos1_" + i + "_1";
		var pos2 = "pos2_" + i + "_2";
		var st2="customListBox_" + i + "_0";
		var pos11="customListBox_" + i + "_1";
		var pos12="customListBox_" + i + "_2";
		var mapButton="mapButton_" + i ;
		var stContainerNameFromMap = document.getElementById(st1);
		var pos1FromMap = document.getElementById(pos1);
		var pos2FromMap = document.getElementById(pos2);    		    		
		var stContainerNameFromDropdown = document.getElementById(st2);
		var pos1FromDropdown = document.getElementById(pos11);
		var pos2FromDropdown = document.getElementById(pos12);    		    		
		var containerMapButton =  document.getElementById(mapButton);

		//alert("inside method of radio button click");
		if(element.value == 1)
		{
			stContainerNameFromMap.disabled = true;
			pos1FromMap.disabled = true;
			pos2FromMap.disabled = true;

			containerMapButton.disabled = true;
			stContainerNameFromDropdown.disabled = true;
			pos1FromDropdown.disabled = true;
			pos2FromDropdown.disabled = true;
		}
		else if(element.value == 2)
		{
			stContainerNameFromMap.disabled = true;
			pos1FromMap.disabled = true;
			pos2FromMap.disabled = true;

			containerMapButton.disabled = true;
			stContainerNameFromDropdown.disabled = false;
			pos1FromDropdown.disabled = false;
			pos2FromDropdown.disabled = false;

		}
		else
		{
			stContainerNameFromMap.disabled = false;
			pos1FromMap.disabled = false;
			pos2FromMap.disabled = false;

			containerMapButton.disabled = false;
			stContainerNameFromDropdown.disabled = true;
			pos1FromDropdown.disabled = true;
			pos2FromDropdown.disabled = true;
		}
	}		
	
	function mapButtonClickedInAliquot(frameUrl,count)
	{
	   	var storageContainer = document.getElementById("container_" + count + "_0").value;
		frameUrl+="&storageContainerName="+storageContainer;
		//alert(frameUrl);
		NewWindow(frameUrl,'name','810','320','yes');
		
    }
	
</script>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="660">
<tr>
<td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
<tr>
	<td class="formMessage" colspan="3">* indicates a required field</td>
</tr>

<tr>
	<td class="formTitle" height="20" colspan="7">
		<bean:message key="aliquots.createTitle"/>
	</td>
</tr>

<tr>
	<td class="formRequiredNoticeNoBottom">*
		<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
			&nbsp;
		</html:radio>
	</td>
	<td class="formRequiredLabelLeftBorder" width="160" nowrap>
			<label for="parentId">
				<bean:message key="createSpecimen.parentLabel"/>
			</label>
	</td>
	<td class="formField">
		<logic:equal name="aliquotForm" property="checkedButton" value="1">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenLabel" property="specimenLabel" disabled="false"/>
		</logic:equal>
		
		<logic:equal name="aliquotForm" property="checkedButton" value="2">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenLabel" property="specimenLabel" disabled="true"/>
		</logic:equal>
	</td>
	<td class="formRequiredLabelBoth" width="5">*</td>
	<td class="formRequiredLabel">
		<bean:message key="aliquots.noOfAliquots"/>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" styleId="noOfAliquots" property="noOfAliquots"/>
	</td>
</tr>

<tr>
	<td class="formRequiredNotice"><span class="hideText">*</span>
		<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
			&nbsp;
		</html:radio>
	</td>
	<td class="formRequiredLabel" width="73">
			<label for="barcode">
				<bean:message key="specimen.barcode"/>
			</label>
	</td>
	<td class="formField">
		<logic:equal name="aliquotForm" property="checkedButton" value="1">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcode" property="barcode" disabled="true"/>
		</logic:equal>
		
		<logic:equal name="aliquotForm" property="checkedButton" value="2">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcode" property="barcode"/>
		</logic:equal>
	</td>
	<td class="formLabel" colspan="2">
		<bean:message key="aliquots.qtyPerAliquot"/>
	</td>
	<td class="formField">
		<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" styleId="quantityPerAliquot" property="quantityPerAliquot"/>
	</td>
</tr>

<tr>
	<td colspan="5">
		&nbsp;
	</td>
	<td align="right">
		<html:button styleClass="actionButton" property="submitPage" onclick="onSubmit()">
			<bean:message key="<%=buttonKey%>"/>
		</html:button>
	</td>
</tr>
</table>
</td>
</tr>

<%
String operation = Utility.toString(Constants.OPERATION);

String unit = "";

if(form != null)
{
	unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
}

if(!Constants.PAGEOF_ALIQUOT.equals(pageOf))
{
%>


<tr>
<td>
        <html:hidden property="id"/>
		<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
		<html:hidden property="submittedFor"/>
<table summary="" cellpadding="5" cellspacing="0" border="0" width="660">


<tr>
	<td class="formTitle" height="20" colspan="4" >
		<bean:message key="aliquots.title"/>
	</td>
</tr>


<tr>
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="type">
			<bean:message key="specimen.type"/> 
		</label>
	</td>
	<td class="formLabelBorderlessLeft"> <%=form.getSpecimenClass()%>
		<html:hidden property="specimenClass" />
	</td>
	
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="subType">
			<bean:message key="specimen.subType"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft"> <%=form.getType()%>
		<html:hidden property="type" />
	</td>
</tr>



<tr>
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="tissueSite">
			<bean:message key="specimen.tissueSite"/> 
		</label>
	</td>
	    <td class="formLabelBorderlessLeft" colspan="3"> <%=form.getTissueSite()%>
		<html:hidden property="tissueSite" />
	    </td>
		
</tr>

<tr>
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="tissueSide">
			<bean:message key="specimen.tissueSide"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft"> <%=form.getTissueSide()%>
		<html:hidden property="tissueSide" />
	</td>
		

	<td class="formLabelBorderlessLeftAndBold">
		<label for="pathologicalStatus">
			<bean:message key="specimen.pathologicalStatus"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft"> <%=form.getPathologicalStatus()%>
		<html:hidden property="pathologicalStatus" />
	</td>
	
</tr>

<tr>
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="availableQuantity">
			<bean:message key="aliquots.initialAvailableQuantity"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft"> <%=form.getInitialAvailableQuantity()%>
		<html:hidden property="initialAvailableQuantity" />
		&nbsp; <%=unit%>
	</td>

	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="availableQuantity">
			<bean:message key="aliquots.currentAvailableQuantity"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft"> <%=form.getAvailableQuantity()%>
		<html:hidden property="availableQuantity" />
		&nbsp; <%=unit%>
	</td>
</tr>

<tr>
	
	<td class="formLabelBorderlessLeftAndBold">
		<label for="concentration">
			<bean:message key="specimen.concentration"/> 
		</label>
	</td>
	
	<td class="formLabelBorderlessLeft" colspan="3">
        <%if(form.getConcentration()!=null) {%>
     	<%=form.getConcentration()%>
		<html:hidden property="concentration" />
		&nbsp;<bean:message key="specimen.concentrationUnit"/>
		<%}%>
	</td>
</tr>
</table>

<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
<tr>
	<td class="formRightSubTableTitleWithBorder" width="5">
     	#
    </td>
    <td class="formRightSubTableTitleWithBorder">*
		<bean:message key="specimen.label"/>
	</td>
	<td class="formRightSubTableTitleWithBorder">*
		<bean:message key="specimen.quantity"/>
	</td>
	<td class="formRightSubTableTitleWithBorder">&nbsp;
		<bean:message key="specimen.barcode"/>
	</td>
	<td class="formRightSubTableTitleWithBorder">*
		<bean:message key="aliquots.location"/>
	</td>
</tr>

<%=ScriptGenerator.getJSForOutermostDataTable()%>

<%
	Map aliquotMap = new HashMap();
	int counter=0;

	if(form != null)
	{
		counter = Integer.parseInt(form.getNoOfAliquots());
		aliquotMap = form.getAliquotMap();
	}

	/* Retrieving a map of available containers */
	Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
	String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;
    String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};

	for(int i=1;i<=counter;i++)
	{
		String labelKey = "value(Specimen:" + i + "_label)";
		String qtyKey = "value(Specimen:" + i + "_quantity)";
		String barKey = "value(Specimen:" + i + "_barcode)";
		//String virtuallyLocatedKey = "value(Specimen:" + i + "_virtuallyLocated)";
		String containerKey = "value(Specimen:" + i + "_StorageContainer_id)";
		String pos1Key = "value(Specimen:" + i + "_positionDimensionOne)";
		String pos2Key = "value(Specimen:" + i + "_positionDimensionTwo)";
		String containerMap = "value(mapButton_" + i + ")";
		String containerMapStyle = "mapButton_" + i ;
		
		
		//Keys for container if selected from Map
		String containerIdFromMapKey = "value(Specimen:" + i + "_StorageContainer_id_fromMap)";
		String containerNameFromMapKey = "value(Specimen:" + i + "_StorageContainer_name_fromMap)";
		String pos1FromMapKey = "value(Specimen:" + i + "_positionDimensionOne_fromMap)";
		String pos2FromMapKey = "value(Specimen:" + i + "_positionDimensionTwo_fromMap)";
		String stContSelection = "value(radio_" + i + ")";
		String containerStyle = "container_" + i + "_0";
		String containerIdStyle = "containerId_" + i + "_0";
		String pos1Style = "pos1_" + i + "_1";
		String pos2Style = "pos2_" + i + "_2";
		String rbKey = "radio_" + i ;
		if(aliquotMap.get(rbKey)==null)
		{
		   aliquotMap.put(rbKey,"2");
	    }
		
		 int radioSelected = Integer.parseInt(aliquotMap.get(rbKey).toString());
		 boolean dropDownDisable = false;
		 boolean textBoxDisable = false;
		
        System.out.println(radioSelected);		
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
		
		
		String virtuallyLocatedStyleId = "chkBox_"+ i;
		//Preparing data for custom tag
		String[] attrNames = {containerKey, pos1Key, pos2Key};

		String[] initValues = new String[3];
		initValues[0] = (String)aliquotMap.get("Specimen:" + i + "_StorageContainer_id");
		initValues[1] = (String)aliquotMap.get("Specimen:" + i + "_positionDimensionOne");
		initValues[2] = (String)aliquotMap.get("Specimen:" + i + "_positionDimensionTwo");

		String rowNumber = String.valueOf(i);
		String noOfEmptyCombos = "3";
		String styClass = "formFieldSized5";
		String tdStyleClass = "customFormField";
		String onChange = "onCustomListBoxChangeInAliquot(this,'CreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange')";
		if(CPQuery!= null)
		{
			onChange = "onCustomListBoxChangeInAliquot(this,'CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange&CPQuery=true&"+Constants.PARENT_SPECIMEN_ID+"="+parentSPId+"')";	
		}


		String containerStyleId = "customListBox_" + rowNumber + "_0";
		String pos1StyleId = "customListBox_" + rowNumber + "_1";
		String pos2StyleId = "customListBox_" + rowNumber + "_2";

		String className = form.getSpecimenClass();
		if (className==null)
			className="";
		String collectionProtocolId =form.getSpCollectionGroupId()+"";
		if (collectionProtocolId==null)
			collectionProtocolId="";
		String frameUrl="";
		String storageContSelection=(String)form.getValue("radio_" + i);
		System.out.println("cont style:"+containerStyle);
	
			frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
			+ "&amp;containerStyle=" + containerStyle 
			+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
			+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId ;
			System.out.println("frameUrl:"+frameUrl);				
	

		  String buttonOnClicked = "mapButtonClickedInAliquot('"+frameUrl+"','"+i+"')"; //javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";
		

%>
<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
	<tr>
		<td class="formSerialNumberField" width="5">
	     	<%=i%>.
	    </td>
	    <td class="formField" nowrap>
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="label" property="<%=labelKey%>" disabled="false"/>
		</td>
		<td class="formField" nowrap>
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="<%=qtyKey%>" disabled="false"/>
			&nbsp; <%=unit%>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" disabled="false"/>
		</td>
		<td class="formField" nowrap>
						<table border="0">
						
							<tr>
								
								<td><html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
								<html:radio value="1" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>" /></td>
								<td class="formFieldNoBorders">
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">									
										<bean:message key="specimen.virtuallyLocated" />
									</logic:equal>	
								</td>
							</tr>
								<tr>
								<td ><html:radio value="2" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
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
										disabled = "<%=dropDownDisable%>"
										tdStyleClassArray="<%=tdStyleClassArray%>"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
								    	</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td ><html:radio value="3" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<td class="formLabelBorderlessLeft">
									<html:text styleClass="formFieldSized10"  size="30" styleId="<%=containerStyle%>" property="<%=containerNameFromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos1Style%>" property="<%=pos1FromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos2Style%>" property="<%=pos2FromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:button styleClass="actionButton" styleId = "<%=containerMapStyle%>" property="<%=containerMap%>" onclick="<%=buttonOnClicked%>" disabled = "<%=textBoxDisable%>">
										<bean:message key="buttons.map"/>
									</html:button>
								</td>
							</tr>
						</table>

						</td>
						<%//System.out.println("End of tag in jsp");%>
			<%-- n-combo-box end --%>
		
	</tr>
	<logic:equal name="exceedsMaxLimit" value="true">
		<tr>
			<td>
				<bean:message key="container.maxView"/>
			</td>
		</tr>
	</logic:equal>
<%
	} //For
%>
</table>
</td>
</tr>

<tr>
<td colspan="4">&nbsp;</td>
</tr>

<tr>
<td align="left" colspan="2">
<!-- action buttons begins -->
<table cellpadding="4" cellspacing="0" border="0">
<tr>

    <td colspan="3" class="formLabelNoBackGround" width="40%">
		<html:checkbox property="aliqoutInSameContainer" onclick="onCheckboxClicked()">
		<bean:message key="aliquots.storeAllAliquotes" />
		</html:checkbox>
	</td>
	<td colspan="3" class="formLabelNoBackGround" width="40%">
		<html:checkbox property="disposeParentSpecimen" >
		<bean:message key="aliquots.disposeParentSpecimen" />
		</html:checkbox>
	</td>
	<td align="right">
		<html:button styleClass="actionButton" property="submitButton" onclick="onCreate()">
			<bean:message key="buttons.create"/>
		</html:button>
	</td>
	
	
	<%--td>
		<html:reset styleClass="actionButton">
			<bean:message key="buttons.reset"/>
		</html:reset>
	</td--%> 
</tr>
	
</table>				
<!-- action buttons end -->
</td>
</tr>
<%
} //If pageOf != "Aliquot Page"
%>
</table>
<html:hidden property="specimenID"/>
<html:hidden property="spCollectionGroupId"/>
</html:form>