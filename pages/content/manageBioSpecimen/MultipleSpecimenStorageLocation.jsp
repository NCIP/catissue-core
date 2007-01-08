<!-- 
*	This JSP page is to set storage locations for Multiple Specimen.
*	It also provides information specimen like label and barcode to the user.
*	Author : Mandar Deshmukh
*	Date   : Nov 07, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MultipleSpecimenStorageLocationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	if(request.getParameter("formSubmitted")!=null)
	{
	     pageOf = (String)request.getParameter(Constants.PAGEOF);
	}
	String submitSuccessful = (String)request.getAttribute(Constants.MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL);
%>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>

<script>
		function submitForm()
		{
		    var action = '<%=Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION%>';
			<%System.out.println("pageOf----"+ pageOf);%>
			document.forms[0].action = action + "?formSubmitted=true&operation=add&menuSelected=15&buttonClicked=submit&pageOf=" + '<%=pageOf%>';
			<%if(pageOf != null && pageOf.toLowerCase().indexOf("cpquery")!=-1) {%>
			action = '<%=Constants.CP_QUERY_MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION%>';
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.CP_QUERY_PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION%>' + "&operation=add&menuSelected=15&formSubmitted=true&buttonClicked=submit";
			<%}%>
			document.forms[0].submit();
		}
		
		function onStorageRadioClickInMultipleSpecimen(element)
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
	
		function mapButtonClickedInMultipleSpecimen(frameUrl,count)
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
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="660" height=100%>
<tr>
<td height=100% valign="top">

<html:form action="<%=Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION%>">

<%
	if(Constants.MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL.equals(submitSuccessful ) )
	{
 %>
	<table>
		<tr>
			<td>Specimens submitted successfully.</td>
		</tr>
	</table>
<%
	}
	else
	{	
 %>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="400">
	<tr>
		<td class="formMessage" colspan="3">* indicates a required field</td>
	</tr>

<%
	String operation = Utility.toString(Constants.OPERATION);
	MultipleSpecimenStorageLocationForm form = null;
	Object obj = request.getAttribute("multipleSpecimenStorageLocationForm");
	if(obj != null)
	{
		form = (MultipleSpecimenStorageLocationForm)request.getAttribute("multipleSpecimenStorageLocationForm");
		//System.out.print("\n\nForm Set\n\n");
	}
%>
	
<tr>
<td colspan="3">
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="350" height="100%">
<tr>
<td colspan=3 width="90%">&nbsp;</td>
  <td align="right" width="*">
	<!-- action buttons begins -->
			<html:button styleClass="actionButton" property="submitButton" onclick="submitForm()">
				Submit
			</html:button>
	<!-- action buttons end -->
	</td>
</tr>
	
	<tr>
		<td class="formLeftSubTableTitle" width="5">
	     	#
	    </td>
	    <td class="formRightSubTableTitle">*
			<bean:message key="specimen.label"/>
		</td>
		<td class="formRightSubTableTitle">&nbsp;
			<bean:message key="specimen.barcode"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="aliquots.location"/>
				<html:hidden property="value(Specimen_Count)"/>
		</td>
<%=ScriptGenerator.getJSForOutermostDataTable()%>
	</tr>
	<% 
		Map specimenMap = null;
		int counter=1;
		String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
		String specimenCountKey = "Specimen_Count";
		String buttonClicked = null;
		if(request.getParameter("buttonClicked")!=null)
		{
		   buttonClicked = "submit";
		}
		if(form != null)
		{
			specimenMap = form.getSpecimenOnUIMap();
			if(specimenMap.get(specimenCountKey) instanceof Integer)
				counter = ((Integer)specimenMap.get(specimenCountKey)).intValue() ;
			else if(specimenMap.get(specimenCountKey) instanceof String)
			{
				counter = Integer.parseInt(((String)specimenMap.get(specimenCountKey))) ;
			}
			//System.out.println("\n\nSpecimen Map set");
		}
		//System.out.println("\n\nSpecimen Map: "+specimenMap + "\n\n");
		/* Retrieving a map of available containers */
		Map locationDataMap = (Map) request.getSession().getAttribute(Constants.CONTAINER_MAP_KEY);
		//System.out.println("locationDataMap: "+locationDataMap);
		String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;

		int rowCount=1;	
		String rowNumber = String.valueOf(rowCount);	
		int newCount = 1;
		
		if(specimenMap != null)
		{ 
			for(int cnt=1;cnt<=counter;cnt++)
			{

					String fieldName = "value(";
					String parentKey = "Specimen:"+cnt+"_";
					String labelKey = parentKey+"Label";
					String typeKey = parentKey+"Type";
					String barKey = parentKey+"Barcode";
					String storageContainerKey = parentKey+"StorageContainer";
					String positionOneKey = parentKey+"PositionOne";
					String positionTwoKey = parentKey+"PositionTwo";
					String derivedCountKey = parentKey+"DeriveCount";

					String flabelKey = fieldName+parentKey+"Label)";
					String ftypeKey = fieldName+parentKey+"Type)";
					String fbarKey = fieldName+parentKey+"Barcode)";
					String fstorageContainerKey = fieldName+parentKey+"StorageContainer)";
					String fpositionOneKey = fieldName+parentKey+"PositionOne)";
					String fpositionTwoKey = fieldName+parentKey+"PositionTwo)";
					String fderivedCountKey = fieldName+parentKey+"DeriveCount)";

				//key for location map
				String classKey = parentKey+"ClassName";
				String fclassKey = fieldName+parentKey+"ClassName)";
				String collectionProtocolKey = parentKey+"CollectionProtocol";
				String fcollectionProtocolKey = fieldName+parentKey+"CollectionProtocol)";

				String className = (String)specimenMap.get(classKey);  
				if (className==null)
					className="";

				//---------
				String typeName = (String)specimenMap.get(typeKey);  
				if (typeName==null)
					typeName="";
				//---------

				String collectionProtocolId = (String)specimenMap.get(collectionProtocolKey);  
				if (collectionProtocolId==null)
					collectionProtocolId="";
	
				String locationMapKey = collectionProtocolId+Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_KEY_SEPARATOR+className;  
			//	System.out.println("\n>>>>>>>>>location map key : " + locationMapKey);
				Map dataMap = (Map) locationDataMap.get(locationMapKey);
				//System.out.println("\n\n--------------\nLocation Map :\n "+dataMap+"\n\n--------------\n");

	%>

<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=cnt %>.<html:hidden property="<%=fderivedCountKey%>"/>
				<html:hidden property="<%=fclassKey%>"/>
				<html:hidden property="<%=fcollectionProtocolKey%>"/>
				
		    </td>
		    <td class="formField">
			<TABLE>
			<TR>
				<TD class="formFieldNoBordersSimple">
					<html:text styleClass="formFieldSized15" maxlength="50" size="10" styleId="<%=flabelKey%>" property="<%=flabelKey%>" 
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</TD>
			</TR>
			<TR>
				<TD class="formFieldNoBordersSimple"><B>Class:</B> <%=className%></TD>
			</TR>
			</TABLE>
			</td>
			<td class="formField">
			<TABLE>
			<TR>
				<TD class="formFieldNoBordersSimple">
					<html:text styleClass="formFieldSized15" maxlength="50" size="10" styleId="<%=fbarKey%>" property="<%=fbarKey%>" 
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</TD>
			</TR>
			<TR>
				<TD class="formFieldNoBordersSimple"><B>Type:</B> <%=typeName%></TD>
			</TR>
			</TABLE>
			</td>
			<td class="formField" nowrap>
<%
	String virtuallyLocatedKey = "value(Specimen:" + cnt + "_virtuallyLocated)";
	String virtuallyLocatedStyleId = "chkBox_"+ rowCount;

	String containerKey = fstorageContainerKey;
	String pos1Key = fpositionOneKey;
	String pos2Key = fpositionTwoKey;
	
	//Keys for container if selected from Map
		String containerMap = "value(mapButton_" + newCount + ")";
		String containerMapStyle = "mapButton_" + newCount ;
		String containerIdFromMapKey = "value(Specimen:" + newCount + "_StorageContainer_id_fromMap)";
		String containerNameFromMapKey = "value(Specimen:" + newCount + "_StorageContainer_name_fromMap)";
		String pos1FromMapKey = "value(Specimen:" + newCount + "_positionDimensionOne_fromMap)";
		String pos2FromMapKey = "value(Specimen:" + newCount + "_positionDimensionTwo_fromMap)";
		String stContSelection = "value(radio_" + newCount + ")";
		String containerStyle = "container_" + newCount + "_0";
		String containerIdStyle = "containerId_" +newCount + "_0";
		String pos1Style = "pos1_" + newCount + "_1";
		String pos2Style = "pos2_" + newCount + "_2";
		String rbKey = "radio_" + newCount ;
		
		if(specimenMap.get(rbKey)==null)
		{
		  specimenMap.put(rbKey,"2");
		}
		
		int radioSelected = Integer.parseInt(specimenMap.get(rbKey).toString());
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
		

	String[] initValues = new String[3];
	initValues[0] = (String)specimenMap.get(storageContainerKey);
	initValues[1] = (String)specimenMap.get(positionOneKey);
	initValues[2] = (String)specimenMap.get(positionTwoKey);

			String noOfEmptyCombos = "3";
			String styClass = "formFieldSized5";
			String tdStyleClass = "customFormField";
			String onChange = "onCustomListBoxChange(this)";

	
	//Preparing data for custom tag
	String[] attrNames = {containerKey, pos1Key, pos2Key};

			String containerStyleId = "customListBox_" + rowNumber + "_0";
			String pos1StyleId = "customListBox_" + rowNumber + "_1";
			String pos2StyleId = "customListBox_" + rowNumber + "_2";

		
			String frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
			    + "&amp;containerStyle=" + containerStyle
				+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
				+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;

			String buttonOnClicked = "mapButtonClickedInMultipleSpecimen('"+frameUrl+"','"+newCount+"')";

//System.out.println("\n\n--------------\nData Map For NLevelCombo Main Specimen :\n "+dataMap+"\n\n--------------\n");
 %>
 
                          <table border="0">
						
							<tr>
								
								<td><html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
								<html:radio value="1" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>" /></td>
								<td class="formFieldNoBorders">
								<bean:message key="specimen.virtuallyLocated" />
								</td>
							</tr>
								<tr>
								<td ><html:radio value="2" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<td>
									<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
										attributeNames="<%=attrNames%>" 
										initialValues="<%=initValues%>"  
										styleClass = "<%=styClass%>" 
										tdStyleClass = "<%=tdStyleClass%>" 
										labelNames="<%=labelNames%>" 
										rowNumber="<%=rowNumber%>" 
										onChange = "<%=onChange%>"
										tdStyleClassArray="<%=tdStyleClassArray%>"
										formLabelStyle="formLabelBorderless"
										disabled = "<%=dropDownDisable%>"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
										</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td ><html:radio value="3" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<td class="formLabelBorderlessLeft">
									<html:text styleClass="formFieldSized10"  size="30" styleId="<%=containerStyle%>" property="<%=containerNameFromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos1Style%>" property="<%=pos1FromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos2Style%>" property="<%=pos2FromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:button styleClass="actionButton" styleId = "<%=containerMapStyle%>" property="<%=containerMap%>" onclick="<%=buttonOnClicked%>" disabled="true"disabled = "<%=textBoxDisable%>">
										<bean:message key="buttons.map"/>
									</html:button>
								</td>
							</tr>
						</table>
						</td>
		</tr>
			<%
				rowCount++;
				rowNumber = String.valueOf(rowCount);		
                newCount++;
				Object derivedCount = specimenMap.get(derivedCountKey); 
				if(derivedCount != null)
				{
					int deriveCounter = 0;
					if(derivedCount instanceof Integer)
						deriveCounter = ((Integer)derivedCount).intValue() ;
					else if(derivedCount instanceof String)
					{
						deriveCounter = Integer.parseInt(((String)derivedCount)) ;
					}
					//System.out.println("Specimen No. "+cnt+" Derived Count : "+deriveCounter);
					for(int i=1;i<=deriveCounter; i++)
					{
						String derivedPrefix = parentKey+"DerivedSpecimen:"+i+"_"; 
						String dlabelKey = derivedPrefix+"Label";
						String dtypeKey = derivedPrefix+"Type";
						String dbarKey = derivedPrefix+"Barcode";
						String dstorageContainerKey = derivedPrefix+"StorageContainer";
						String dpositionOneKey = derivedPrefix+"PositionOne";
						String dpositionTwoKey = derivedPrefix+"PositionTwo";

				//key for location map
				String dclassKey = derivedPrefix+"ClassName";
				String fdclassKey = fieldName+dclassKey+")";

				String dcollectionProtocolKey = derivedPrefix+"CollectionProtocol";
				String fdcollectionProtocolKey = fieldName+dcollectionProtocolKey+")";

				String dclassName = (String)specimenMap.get(dclassKey);  
				if (dclassName==null)
					dclassName="";

				//-----------------
				String dtypeName = (String)specimenMap.get(dtypeKey);  
				if (dtypeName==null)
					dtypeName="";
				//-----------------
				String dcollectionProtocolId = (String)specimenMap.get(dcollectionProtocolKey);  
				if (dcollectionProtocolId==null)
					dcollectionProtocolId="";
	
				String dlocationMapKey = dcollectionProtocolId+Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_KEY_SEPARATOR+dclassName;  
				//System.out.println("\n>>>>>>>>>dlocation map key : " + dlocationMapKey);
				dataMap = (Map) locationDataMap.get(dlocationMapKey);
			

					String fdlabelKey = fieldName+dlabelKey+")";
					String fdtypeKey = fieldName+dtypeKey+")";
					String fdbarKey = fieldName+dbarKey+")";
					String fdstorageContainerKey = fieldName+dstorageContainerKey+")";
					String fdpositionOneKey = fieldName+dpositionOneKey+")";
					String fdpositionTwoKey = fieldName+dpositionTwoKey+")";
			%>
		<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=cnt %>.<%=i %>
				<html:hidden property="<%=fdclassKey%>"/>
				<html:hidden property="<%=fdcollectionProtocolKey%>"/>

		    </td>
		    <td class="formField" nowrap>
			<TABLE>
			<TR>
				<TD class="formFieldNoBordersSimple">
				<html:text styleClass="formFieldSized15" maxlength="50" size="10" styleId="<%=fdlabelKey%>" property="<%=fdlabelKey%>" 
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</TD>
			</TR>
			<TR>
				<TD class="formFieldNoBordersSimple"><B>Class:</B> <%=dclassName%></TD>
			</TR>
			</TABLE>
			</td>
			<td class="formField">
			<TABLE>
			<TR>
				<TD class="formFieldNoBordersSimple">
					<html:text styleClass="formFieldSized15" maxlength="50" size="10" styleId="<%=fdbarKey%>" property="<%=fdbarKey%>" 
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
				</TD>
			</TR>
			<TR>
				<TD class="formFieldNoBordersSimple"><B>Type:</B> <%=dtypeName%></TD>
			</TR>
			</TABLE>
			</td>
			<td class="formField" nowrap>
	<%
		//code for derived specimens
		virtuallyLocatedKey = fieldName + derivedPrefix+"virtuallyLocated)";
		virtuallyLocatedStyleId = "chkBox_"+ rowCount;
	
		containerKey = fdstorageContainerKey;
		pos1Key = fdpositionOneKey;
		pos2Key = fdpositionTwoKey;
		
			//Keys for container if selected from Map
		 containerMap = "value(mapButton_" + newCount + ")";
		 containerMapStyle = "mapButton_" + newCount ;
		 containerIdFromMapKey = "value(Specimen:" + newCount + "_StorageContainer_id_fromMap)";
		 containerNameFromMapKey = "value(Specimen:" + newCount + "_StorageContainer_name_fromMap)";
		 pos1FromMapKey = "value(Specimen:" + newCount + "_positionDimensionOne_fromMap)";
		 pos2FromMapKey = "value(Specimen:" + newCount + "_positionDimensionTwo_fromMap)";
		 stContSelection = "value(radio_" + newCount + ")";
		 containerStyle = "container_" + newCount + "_0";
		 containerIdStyle = "containerId_" + newCount + "_0";
		 pos1Style = "pos1_" + newCount + "_1";
		 pos2Style = "pos2_" + newCount + "_2";
		 rbKey = "radio_" + newCount ;
		 if(buttonClicked==null)
		{
		specimenMap.put(rbKey,"2");
		}
		
		  radioSelected = Integer.parseInt(specimenMap.get(rbKey).toString());
		  dropDownDisable = false;
		  textBoxDisable = false;
								
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
		
	
		initValues = new String[3];
		initValues[0] = (String)specimenMap.get(dstorageContainerKey);
		initValues[1] = (String)specimenMap.get(dpositionOneKey);
		initValues[2] = (String)specimenMap.get(dpositionTwoKey);
	
	
		
		//Preparing data for custom tag
		String[] dattrNames = {containerKey, pos1Key, pos2Key};
	
		containerStyleId = "customListBox_" + rowNumber + "_0";
		pos1StyleId = "customListBox_" + rowNumber + "_1";
		pos2StyleId = "customListBox_" + rowNumber + "_2";
		
		   frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
			    + "&amp;containerStyle=" + containerStyle
				+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+dclassName
				+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + dcollectionProtocolId;
			
			// buttonOnClicked = "javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";
            buttonOnClicked = "mapButtonClickedInMultipleSpecimen('"+frameUrl+"','"+newCount+"')";
			//System.out.println("\n\n--------------\nData Map For NLevelCombo Derived Specimen :\n "+dataMap+"\n\n--------------\n");
	 %>
				<table border="0">
						
							<tr>
								
								<td><html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
								<html:radio value="1" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>" /></td>
								<td class="formFieldNoBorders">
								<bean:message key="specimen.virtuallyLocated" />
								</td>
							</tr>
								<tr>
								<td ><html:radio value="2" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<td>
									<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
										attributeNames="<%=dattrNames%>" 
										initialValues="<%=initValues%>"  
										styleClass = "<%=styClass%>" 
										tdStyleClass = "<%=tdStyleClass%>" 
										labelNames="<%=labelNames%>" 
										rowNumber="<%=rowNumber%>" 
										tdStyleClassArray="<%=tdStyleClassArray%>"
										onChange = "<%=onChange%>"
										formLabelStyle="formLabelBorderless"
										disabled = "<%=dropDownDisable%>"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
										</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td ><html:radio value="3" onclick="onStorageRadioClickInMultipleSpecimen(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<td class="formLabelBorderlessLeft">
									<html:text styleClass="formFieldSized10"  size="30" styleId="<%=containerStyle%>" property="<%=containerNameFromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos1Style%>" property="<%=pos1FromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=pos2Style%>" property="<%=pos2FromMapKey%>" disabled="true"disabled = "<%=textBoxDisable%>"/>
									<html:button styleClass="actionButton" styleId = "<%=containerMapStyle%>" property="<%=containerMap%>" onclick="<%=buttonOnClicked%>" disabled="true"disabled = "<%=textBoxDisable%>">
										<bean:message key="buttons.map"/>
									</html:button>
								</td>
							</tr>
						</table>

			</td>
		</tr>
			<% 		
				rowCount++;
				newCount++;
				rowNumber = String.valueOf(rowCount);				
					} // for
			 %>
			<%	} // if derived!=null %>
	<%
			} //For
		} // if specimen != null
		else
		{
	%>
		<tr><td colspan=4>No Specimens</td></tr>
	<%	} %>


	</table>
</td>
</tr>

<tr>
	<td colspan="4">&nbsp;</td>
</tr>

</table>
<%
	}   //else for submitSuccessful
 %>

</html:form>
</td>
</tr>
</table>