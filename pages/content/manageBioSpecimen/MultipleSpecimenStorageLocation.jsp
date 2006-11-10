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
	String submitSuccessful = (String)request.getAttribute(Constants.MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL);
%>
<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>

<script>
		function submitForm()
		{
			var action = '<%=Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION%>';
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION%>' + "&operation=add&menuSelected=15";
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

</script>
</head>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

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
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="660">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
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
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
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
		String specimenCountKey = "Specimen_Count";
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
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=flabelKey%>"/>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=fbarKey%>"/>
			</td>
			<td class="formField" nowrap>
<%-- 				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=fstorageContainerKey%>"/>
				<html:text styleClass="formFieldSized" maxlength="50" size="5" styleId="flabelKey" property="<%=fpositionOneKey%>"/>
				<html:text styleClass="formFieldSized" maxlength="50" size="5" styleId="flabelKey" property="<%=fpositionTwoKey%>"/>
--%>
<%
	String virtuallyLocatedKey = "value(Specimen:" + cnt + "_virtuallyLocated)";
	String virtuallyLocatedStyleId = "chkBox_"+ rowCount;

	String containerKey = fstorageContainerKey;
	String pos1Key = fpositionOneKey;
	String pos2Key = fpositionTwoKey;

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

		
			String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=" + containerStyleId + "&amp;xDimStyleId=" + pos1StyleId + "&amp;yDimStyleId=" + pos2StyleId
				+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
				+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;

			String buttonOnClicked = "javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";

//System.out.println("\n\n--------------\nData Map For NLevelCombo Main Specimen :\n "+dataMap+"\n\n--------------\n");
 %>
				<html:checkbox property="<%=virtuallyLocatedKey%>" onclick="setVirtuallyLocated(this)" value="true" styleId="<%=virtuallyLocatedStyleId%>"/><bean:message key="specimen.virtuallyLocated" />
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
		</tr>
			<%
				rowCount++;
				rowNumber = String.valueOf(rowCount);		

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
		     	<%=cnt %>]<%=i %>
				<html:hidden property="<%=fdclassKey%>"/>
				<html:hidden property="<%=fdcollectionProtocolKey%>"/>

		    </td>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=fdlabelKey%>"/>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=fdbarKey%>"/>
			</td>
			<td class="formField" nowrap>
	<%
		//code for derived specimens
		virtuallyLocatedKey = fieldName + derivedPrefix+"virtuallyLocated)";
		virtuallyLocatedStyleId = "chkBox_"+ rowCount;
	
		containerKey = fdstorageContainerKey;
		pos1Key = fdpositionOneKey;
		pos2Key = fdpositionTwoKey;
	
		initValues = new String[3];
		initValues[0] = (String)specimenMap.get(dstorageContainerKey);
		initValues[1] = (String)specimenMap.get(dpositionOneKey);
		initValues[2] = (String)specimenMap.get(dpositionTwoKey);
	
	
		
		//Preparing data for custom tag
		String[] dattrNames = {containerKey, pos1Key, pos2Key};
	
		containerStyleId = "customListBox_" + rowNumber + "_0";
		pos1StyleId = "customListBox_" + rowNumber + "_1";
		pos2StyleId = "customListBox_" + rowNumber + "_2";
	
		frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=" + containerStyleId + "&amp;xDimStyleId=" + pos1StyleId + "&amp;yDimStyleId=" + pos2StyleId
					+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+dclassName
					+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + dcollectionProtocolId;
	
				buttonOnClicked = "javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";

			//System.out.println("\n\n--------------\nData Map For NLevelCombo Derived Specimen :\n "+dataMap+"\n\n--------------\n");
	 %>
				<html:checkbox property="<%=virtuallyLocatedKey%>" onclick="setVirtuallyLocated(this)" value="true" styleId="<%=virtuallyLocatedStyleId%>"/><bean:message key="specimen.virtuallyLocated" />
				<ncombo:containermap dataMap="<%=dataMap%>" 
											attributeNames="<%=dattrNames%>" 
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


<!-- 				<html:text styleClass="formFieldSized" maxlength="50" size="10" styleId="flabelKey" property="<%=fdstorageContainerKey%>"/>
				<html:text styleClass="formFieldSized" maxlength="50" size="5" styleId="flabelKey" property="<%=fdpositionOneKey%>"/>
				<html:text styleClass="formFieldSized" maxlength="50" size="5" styleId="flabelKey" property="<%=fdpositionTwoKey%>"/>
-->
			</td>
		</tr>
			<% 		
				rowCount++;
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

<tr>
  <td align="left" colspan="2">
	<!-- action buttons begins -->
	<table cellpadding="4" cellspacing="0" border="0">
	<tr>
		<td width="50%">
			&nbsp;
		</td>
		<td align="right">
			<html:button styleClass="actionButton" property="submitButton" onclick="submitForm()">
				Submit
			</html:button>
		</td>
	</tr>
	</table>				
	<!-- action buttons end -->
	</td>
</tr>
</table>
<%
	}   //else for submitSuccessful
 %>

</html:form>