<!-- 
	This JSP page is to create/display similar containers from/of Parent Storage Container.
	Author : Chetan B H
	Date   : 
-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility" %>
<%@ page import="edu.wustl.catissuecore.actionForm.SimilarContainersForm" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
        //String operation = (String) request.getAttribute(Constants.OPERATION);
	
		Object obj = request.getAttribute("storageContainerForm");
		
		String label1 = null;
		String label2 = null;
		
		SimilarContainersForm form;
		if(obj != null && obj instanceof SimilarContainersForm)
		{
			form = (SimilarContainersForm)obj;
			
			//label1 = form.getOneDimensionLabel();
			//label2 = form.getTwoDimensionLabel();

			if(label1 == null)
			{
				label1 = "Dimension One";
				label2 = "Dimension Two";
			}
		}else
		{
			form = (SimilarContainersForm)request.getAttribute("storageContainerForm");
		}
		if(label1 == null)
		{
				label1 = "Dimension One";
				label2 = "Dimension Two";
		}		
%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	
	
	<script language="JavaScript">
		function onCreate()
		{
			var action = '<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>';			
			action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_SIMILAR_CONTAINERS%>' + "&menuSelected=7";
			//alert("action "+action);
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onRadioButtonClick(element, row)
		{
			var elmtname = element.name;			
			
			var listBox	= document.getElementById("siteId_"+row);
			
			var parentContBox = document.getElementById("customListBox_"+row+"_0");
			var xContBox = document.getElementById("customListBox_"+row+"_1");
			var yContBox = document.getElementById("customListBox_"+row+"_2");
			var mapButton = document.getElementById("Map_"+row);
			if(element.value == 1)
			{
				listBox.disabled = false;
				
				parentContBox.disabled = true;
				xContBox.disabled = true;
				yContBox.disabled = true;
				mapButton.disabled = true;
				//document.forms[0].Map.disabled = true;
			}
			else
			{
				parentContBox.disabled = false;
				xContBox.disabled = false;
				yContBox.disabled = false;
				mapButton.disabled = false;
				//document.forms[0].customListBox_1_0.disabled = false;
				//document.forms[0].customListBox_1_1.disabled = false;
				//document.forms[0].customListBox_1_2.disabled = false;
				//document.forms[0].Map.disabled = false;
				
				listBox.disabled = true;
			}
		}
		
		function onClick(element)
		{
			var elementCP = document.getElementById("collectionIds");
			var elementHolds = document.getElementById("holdsStorageTypeIds");
			var elementSpecimenHolds = document.getElementById("holdsSpecimenClassTypeIds");
			//alert("elementCP "+elementCP+"  elementholds "+elementHolds);
			elementCP.disabled = false;
			elementHolds.disabled = false;
			elementSpecimenHolds.disabled = false;
			
			//action = "<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>";
			//action = action + "?pageOf="+"<%=Constants.PAGEOF_CREATE_SIMILAR_CONTAINERS%>"+"&menuSelected=7";
			var action = document.forms[0].action; //"<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>";
			action = action + "?pageOf="+"<%=Constants.PAGEOF_CREATE_SIMILAR_CONTAINERS%>"+"&operation=add&menuSelected=7";
			document.forms[0].action = action
			document.forms[0].submit;			
		}
		
		function onSiteChange(element, row)
		{
			var containerNameElement = document.getElementById("containerName_"+row);
						
			var selectedIndex = element.selectedIndex;
			var selected_text = element.options[selectedIndex].text;
			
			var value = containerNameElement.value;
			var index = value.indexOf("_");
			var suffix = value.substring(index+1);
			//alert("suffix "+suffix+" selected_text "+selected_text);
			containerNameElement.value = selected_text+"_"+suffix;
		}
	</script>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.SIMILAR_CONTAINERS_ADD_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	<tr>
		<td class="formMessage" colspan="4">
			* indicates a required field
		</td>
	</tr>
	
		
	<%-- <tr>
		<td class="formTitle" height="20" colspan="4">
			<bean:message key="similarcontainers.createTitle"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel" >
			<bean:message key="similarcontainers.noOfContainers"/>
		</td>
		<td class="formField" >
			<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" styleId="noOfContainers" property="noOfContainers"/>
		</td>
	</tr>
	
	<tr>
		<td colspan="2">
			&nbsp;
		</td>
		<td colspan="2" align="right">
			<html:button styleClass="actionButton" property="submitPage" onclick="onCreate()" >
				<bean:message key="buttons.create"/>
			</html:button>
		</td>
	</tr> --%>
	
	</table>
</td>
</tr>

<%
	SimilarContainersForm simForm = (SimilarContainersForm)request.getAttribute("similarContainersForm");
	
	String sType = simForm.getStorageContainerType();
	String siteName = simForm.getSiteName();
	//System.out.println("sTyep ----<>----- "+sType);
	//System.out.println("sName ----<>----- "+siteName);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String checkButtonStatus = Integer.toString(simForm.getCheckedButton());
	
	String noOfContainers = simForm.getNoOfContainers();
	int maxIdentifier = Integer.parseInt((String)request.getAttribute(Constants.MAX_IDENTIFIER));
	//maxIdentifier++;
	if(!Constants.PAGEOF_SIMILAR_CONTAINERS.equals(pageOf))
	{
%>
    <tr>	
	   <td colspan="4">
			&nbsp;
	   </td>
    </tr>
   
    <tr>
	  <%
	  	String typeId = ((Long)request.getAttribute("typeId")).toString();
	  	String siteId = ((Long)request.getAttribute("siteId")).toString();
	  	
	  %>
	  	<td>
	  		<html:hidden property="typeId" value="<%=typeId%>"/>
	  	</td>
    </tr>
    <tr>
		<td>
			<html:hidden property="systemIdentifier" />
		</td>
    </tr>
    <tr>
		<td>
			<html:hidden property="siteId" value="<%=siteId%>"/>
		</td>
    </tr>
    
  	<tr>
		<td>
			<html:hidden property="noOfContainers" value="<%=noOfContainers%>" />
		</td>
	</tr>
  	
  	<tr>
		<td>
			<html:hidden property="value(checkedButton)" value="<%=checkButtonStatus%>"/>
		</td>
    </tr>
  	
    <tr>
    <td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	  
	  <tr>
		<td class="formTitle" height="20" colspan="6">
			<bean:message key="similarcontainers.title"/>
		</td>
	  </tr>
	  
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="type">
				<bean:message key="storageContainer.type"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="storageContainerType" property="storageContainerType" readonly="true"/>
		</td>
	  </tr>
	  
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="temperature">
				<bean:message key="storageContainer.temperature"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="defaultTemperature" property="defaultTemperature" />
		</td>
	  </tr>
	  
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="collectionProtocolTitle">
				<bean:message key="storageContainer.collectionProtocolTitle"/> 
			</label>
		</td>
		
		<td class="formField" >
<!-- Mandar : 434 : for tooltip -->
				<html:select property="collectionIds" styleClass="formFieldSized" styleId="collectionIds" size="4"
					 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" multiple="true" >
					<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>
				</html:select>
				&nbsp;							
		</td>
		
	  </tr>
	  
	  <tr>
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="holds">
				<bean:message key="storageContainer.holds"/> 
			</label>
		</td>
		<td class="formField" >
				<html:select property="holdsStorageTypeIds" styleClass="formFieldVerySmallSized" styleId="holdsStorageTypeIds" size="4" multiple="true"
					onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
					<html:options collection="<%=Constants.HOLDS_LIST1%>" labelProperty="name" property="value"/>
				</html:select>
				&nbsp;
				<html:select property="holdsSpecimenClassTypeIds" styleClass="formFieldVerySmallSized" styleId="typeId" size="4" multiple="true"
					 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
					<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
				</html:select>
				&nbsp;
		</td>
	  </tr>
	  
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">			
				&nbsp;<%=label1%>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity" />
		</td>
	  </tr>
	  
	  <tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			&nbsp;
			<%
				if(label2 != null || ( label2 != null && !(label2.equals("")) ) )
				{
			%>
			<%=label2%>
			<%
				}
			%>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity" />
		</td>
	  </tr>
	  
	  </table>
	<%-- </table> --%>
	<%-- 
	<tr>
	<td colspan="4"> --%>
		</td>
	</tr>
	
	<tr>
	<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	  <tr>
		<td class="formLeftSubTableTitle" width="5">
	     	#
	    </td>
	    <td class="formRightSubTableTitle">*
			<bean:message key="storageContainer.containerName"/>
		</td>
		<td class="formRightSubTableTitle">
			<bean:message key="storageContainer.barcode"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="storageContainer.parentContainer"/>
		</td>
	  </tr>
	  
	  <%-- n-combo-box start --%>
	  
						<%
							Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
							//System.out.println("dataMap -> "+dataMap.size());
							session.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,dataMap);							
							
							String[] labelNames = {"ID","Pos1","Pos2"};
							labelNames = Constants.STORAGE_CONTAINER_LABEL;
							String[] attrNames = { "parentContainerId", "positionDimensionOne", "positionDimensionTwo"};
							
							String[] initValues = new String[3];
							//initValues[0] = Integer.toString((int)form.getParentContainerId());
							////initValues[0] = form.getPositionInParentContainer();
							//initValues[1] = Integer.toString(form.getPositionDimensionOne());
							//initValues[2] = Integer.toString(form.getPositionDimensionTwo());
							
							//String rowNumber = "1";
							String styClass = "formFieldSized5";
							String tdStyleClass = "customFormField";
							//boolean disabled = true;
							String onChange = "onCustomListBoxChange(this)";
							//String onChange = "onCustomListBoxChange(this);onParentContainerChange(this)";
							//boolean buttonDisabled = true;
							//String buttonOnClicked  = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";
							String buttonOnClicked = "StorageMapWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=customListBox_1_0&amp;xDimStyleId=customListBox_1_1&amp;yDimStyleId=customListBox_1_2&amp;storageType=','name','810','320','yes');return false";
							String noOfEmptyCombos = "3";
							
						%>
					
				<%=ScriptGenerator.getJSForOutermostDataTable()%>
				<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
	  
	  <%
		form = simForm;
		int counter=0;
		
		if(form != null)
		{
			counter = Integer.parseInt(form.getNoOfContainers());
		}
		
		//System.out.println("counter  <<-->> "+counter);
		String contName = siteName+"_"+sType+"_";
		
		List initValuesList = (List)request.getAttribute("initValues");
		
		for(int i=1;i<=counter;i++)
		{
			String containerNameKey = "value(simCont:" + i + "_name)";
			String barcodeKey = "value(simCont:" + i + "_barcode)";
			String siteKey = "value(simCont:" + i + "_siteId)";
			//String checkButtonKey = "value(simCont:" + i + "_checkButton)";
			
			String contNameSId = "containerName_"+i;
			String barSId = "barcode_"+i;
			//String checkButtSId = "checkedButton_"+i;
			String siteSId = "siteId_"+i;
			//String onClick = "onRadioButtonClick(this,"+i+")";  // For radio button
			String rowNumber = ""+i;
			String buttonId = "Map_"+i;
			//String onSiteChange = "onSiteChange(this,"+i+")";
			
			attrNames[0] = "value(simCont:"+i+"_parentContainerId)";
			attrNames[1] = "value(simCont:"+i+"_positionDimensionOne)";
			attrNames[2] = "value(simCont:"+i+"_positionDimensionTwo)";
			if(initValuesList != null)
			{
				initValues = (String[])initValuesList.get(i-1);
			}
			
	   		if(checkButtonStatus.equals("2"))
	   		{
	   %>
	   				<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
	   <% 
	   		}
	   %>
	   
	    <tr>
	   		
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>
		    <% 
		    	if(i == 1 && (simForm.getContainerName() != null) && !(simForm.getContainerName().equals("")) )
		    	{
		    %>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="40" styleId="<%=contNameSId%>" property="<%=containerNameKey%>" />
			</td>
			<% 
		    	}else{
		    %>
			<td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="40" styleId="<%=contNameSId%>" property="<%=containerNameKey%>" value="<%=(contName+maxIdentifier)%>" />
			</td>
			
			<% 
		    	}
		    %>
		    <td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="<%=barSId%>" property="<%=barcodeKey%>" />
			</td>
			
			<td class="formField">
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formFieldBottom">
							<logic:equal name="value(checkedButton)" value="1">
								<html:select property="<%=siteKey%>" styleClass="formField" styleId="<%=siteSId%>" size="1" value="<%=siteId%>"> <%-- onchange="<%=onSiteChange%>" --%>
									<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
								</html:select>
							</logic:equal>
						</td>
					</tr>
					<tr>
						<td class="formFieldNoBorders">
							<logic:equal name="value(checkedButton)" value="2">
								<ncombo:containermap dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>" 
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"											
											
											buttonName="Map" 
											buttonStyleClass="actionButton"
											value="Map"
											id="<%=buttonId%>"
											formLabelStyle="formLabelBorderless"
											buttonOnClick = "<%=buttonOnClicked%>" />
							</logic:equal>
						</td>
					</tr>
				</table>
			</td>
	   </tr>
	   
	  <%
		  	maxIdentifier++;
		} //For
	  %>
	</table>
	</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	
	<tr>
  		<td align="right" colspan="3">
		<!-- action buttons begins -->
		<table cellpadding="4" cellspacing="0" border="0">
		<tr>
			<td>
			<html:submit styleClass="actionButton" onclick="onClick(this)">
				<bean:message key="buttons.submit"/>
			</html:submit>
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
		
	} //If pageOf != PAGEOF_SIMILAR_CONTAINERS
%>
</table>
</html:form>