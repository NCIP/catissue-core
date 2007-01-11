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
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm" %>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
        //String operation = (String) request.getAttribute(Constants.OPERATION);
		String containerNumber=(String)request.getAttribute("ContainerNumber");
		List siteForParent = (List)request.getAttribute("siteForParentList");
		Object obj = request.getAttribute("storageContainerForm");
		
		String label1 = null;
		String label2 = null;
		
		StorageContainerForm form;
		if(obj != null && obj instanceof StorageContainerForm)
		{
			form = (StorageContainerForm)obj;
			
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
%>
<head>
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
			var elementSpecimenHolds = document.getElementById("holdsSpecimenClassTypes");
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
		
		function onSiteChange1(element, row)
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
		
		function onSiteChange(i)
		{

			var containerNameElement = document.getElementById("containerName_"+i);
			if(containerNameElement.value == "")
			{
				<% long tempContainerNumber = new Long(containerNumber).longValue();%>
				var tempContainerNumber1 = <%=tempContainerNumber%>;
				for(var j=1;j<i;j++)
				{
					tempContainerNumber1++;
				}
				var typeElement = document.forms[0].typeName.value;
				var siteElement = document.getElementById("siteId_"+i);

				if(typeElement.value != "-1" && siteElement.value != "-1" && containerNameElement.value == "")
				{
					//Poornima:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
					//and it is giving error. So these fields are truncated in case it is longer than 40.
					//It also solves Bug 2829:System fails to create a default unique storage container name
					var maxSiteName = siteElement.options[siteElement.selectedIndex].text;
					var maxTypeName = typeElement;
					if(maxSiteName.length>40)
					{
						maxSiteName = maxSiteName.substring(0,39);
					}
					if(maxTypeName.length>40)
					{
						maxTypeName = maxTypeName.substring(0,39);
					}

					containerNameElement.value=maxSiteName+"_"+maxTypeName+"_"+tempContainerNumber1;
				}	
			}

		}
		
		function onParentContainerChange(i)
		{
			var containerNameElement = document.getElementById("containerName_"+i);
			if(containerNameElement.value == "")
			{
				var tempContainerNumber1 = <%=tempContainerNumber%>;
				for(var j=1;j<i;j++)
				{
					tempContainerNumber1++;
				}
				var parentContainerId = document.getElementById("customListBox_"+i+"_0");

				getSiteName(parentContainerId.options[parentContainerId.selectedIndex].value);

				var typeElement = document.forms[0].typeName.value;
				if(typeElement.value != "" && parentContainerId.value != "-1" && containerNameElement.value == "")
				{
					//Poornima:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
					//and it is giving error. So these fields are truncated in case it is longer than 40.
					//It also solves Bug 2829:System fails to create a default unique storage container name
					var maxSiteName = document.forms[0].siteForParentContainer.value;
					var maxTypeName = typeElement;
					if(maxSiteName.length>40)
					{
						maxSiteName = maxSiteName.substring(0,39);
					}
					if(maxTypeName.length>40)
					{
						maxTypeName = maxTypeName.substring(0,39);
					}
					containerNameElement.value = maxSiteName + "_"+maxTypeName+"_"+tempContainerNumber1;
				}
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
		function resetName(i)
		{
			var containerNameElement = document.getElementById("containerName_"+i);
			containerNameElement.value = "";
			if(document.forms[0].checkedButton.value == "1")
			{
				onSiteChange(i)
			}
			else
			{
				onParentContainerChange(i);
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

		 if(element.value == 1)
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

<html:form action="<%=Constants.SIMILAR_CONTAINERS_ADD_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="750">
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="660">
				<tr>
					<td class="formMessage" colspan="4">
						* indicates a required field
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<%
		StorageContainerForm simForm = (StorageContainerForm)request.getAttribute("storageContainerForm");
	
		String sType = simForm.getTypeName();
		String siteName = simForm.getSiteName();
		String siteId = Long.toString(simForm.getSiteId());
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		String checkButtonStatus = Integer.toString(simForm.getCheckedButton());
	
		String noOfContainers = Integer.toString(simForm.getNoOfContainers());
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
	  	
    </tr>
    <tr>
		<td>
			<html:hidden property="id" />
			<html:hidden property="operation" value="add"/>
		</td>
    </tr>
    <tr>
		<td>
			<html:hidden property="siteId" value="<%=siteId%>"/>
			<html:hidden property="siteForParentContainer"/>	
		</td>
    </tr>
    
  	<tr>
		<td>
			<html:hidden property="noOfContainers" value="<%=noOfContainers%>" />
			<html:hidden property="typeName"/>
		</td>
	</tr>
  	
  	<tr>
		<td>
			<html:hidden property="similarContainerMapValue(checkedButton)" value="<%=checkButtonStatus%>"/>
			<html:hidden property="checkedButton"/>
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
					<td class="formField" colspan="2">
						<%--<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="storageContainerType" property="typeName" readonly="true"/>--%>
					<html:select property="typeId" styleClass="formFieldSized" styleId="storageContainerType" size="1">
								<html:options collection="<%=Constants.STORAGETYPELIST%>" labelProperty="name" property="value"/>
							</html:select>
					</td>
				  </tr>
	  
				  <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="temperature">
						<bean:message key="storageContainer.temperature"/> 
						</label>
					</td>
					<td class="formField" colspan="2">
						<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="defaultTemperature" property="defaultTemperature" />
					</td>
				  </tr>
	  
				  <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="collectionProtocolTitle">
						<bean:message key="storageContainer.collectionProtocolTitle"/> 
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
	  
			<%--	  <tr>--%>
				  <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="holds">
						<bean:message key="storageContainer.holds"/> 
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
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">			
						&nbsp;<%=label1%>
					</td>
					<td class="formField" colspan="2">
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
					<td class="formField" colspan="2">
						<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity" />
					</td>
				  </tr>
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
							<logic:equal name="storageContainerForm" property="checkedButton" value="1">
							<bean:message key="storageContainer.site"/>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
							<bean:message key="storageContainer.parentContainer"/>
							</logic:equal>
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
							String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
							String[] initValues = new String[3];
							//initValues[0] = Integer.toString((int)form.getParentContainerId());
							////initValues[0] = form.getPositionInParentContainer();
							//initValues[1] = Integer.toString(form.getPositionDimensionOne());
							//initValues[2] = Integer.toString(form.getPositionDimensionTwo());
							
							//String rowNumber = "1";
							String styClass = "formFieldSized5";
							String tdStyleClass = "customFormField";
							//boolean disabled = true;

							//String onChange = "onCustomListBoxChange(this);onParentContainerChange(this)";
							//boolean buttonDisabled = true;
							//String buttonOnClicked  = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false";
	 	                    //String buttonOnClicked = "StorageMapWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=customListBox_1_0&amp;xDimStyleId=customListBox_1_1&amp;yDimStyleId=customListBox_1_2&amp;storageType=','name','810','320','yes');return false";
							
							
						
							String noOfEmptyCombos = "3";
							
						%>
					
				<%=ScriptGenerator.getJSForOutermostDataTable()%>
				<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
	  
			  <%
				form = simForm;
				int counter=0;
		Map similarContainersMap = form.getSimilarContainersMap(); 
				if(form != null)
				{
					counter = (int)form.getNoOfContainers();
				}
		
				//System.out.println("counter  <<-->> "+counter);
				String contName = siteName+"_"+sType+"_";
		
				List initValuesList = (List)request.getAttribute("initValues");
		
				for(int i=1;i<=counter;i++)
				{
					String onChange = "onCustomListBoxChange(this),onParentContainerChange('"+ i+"')";
					String containerNameKey = "similarContainerMapValue(simCont:" + i + "_name)";
					String barcodeKey = "similarContainerMapValue(simCont:" + i + "_barcode)";
					String siteKey = "similarContainerMapValue(simCont:" + i + "_siteId)";
					//String checkButtonKey = "value(simCont:" + i + "_checkButton)";
			
					String contNameSId = "containerName_"+i;
					String barSId = "barcode_"+i;
					//String checkButtSId = "checkedButton_"+i;
					String siteSId = "siteId_"+i;
					//String onClick = "onRadioButtonClick(this,"+i+")";  // For radio button
					String rowNumber = ""+i;
					String buttonId = "Map_"+i;
					String onSiteChange = "onSiteChange('"+ i+"')";
					
					
					
		String containerMap = "similarContainerMapValue(mapButton_" + i + ")";
		String containerMapStyle = "mapButton_" + i ;
		
		
		//Keys for container if selected from Map
		String containerIdFromMapKey = "similarContainerMapValue(simCont:" + i + "_StorageContainer_id_fromMap)";
		String containerNameFromMapKey = "similarContainerMapValue(simCont:" + i + "_StorageContainer_name_fromMap)";
		String pos1FromMapKey = "similarContainerMapValue(simCont:" + i + "_positionDimensionOne_fromMap)";
		String pos2FromMapKey = "similarContainerMapValue(simCont:" + i + "_positionDimensionTwo_fromMap)";
		String stContSelection = "similarContainerMapValue(radio_" + i + ")";
		String containerStyle = "container_" + i + "_0";
		String containerIdStyle = "containerId_" + i + "_0";
		String pos1Style = "pos1_" + i + "_1";
		String pos2Style = "pos2_" + i + "_2";
		String rbKey = "radio_" + i ;
		
		long storageType = form.getTypeId();
		
		
			String frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
			                   + "&amp;containerStyle=" + containerStyle + "&amp;storageType=" + storageType ;
			        System.out.println("frameUrl:"+frameUrl);				
	
		       String buttonOnClicked = "mapButtonClickedInAliquot('"+frameUrl+"','"+i+"')"; //javascript:NewWindow('"+frameUrl+"','name','810','320','yes');return false";
		
		
	    if(similarContainersMap.get(rbKey)==null)
		{
		   similarContainersMap.put(rbKey,"1");
	    } 
	    
		 int radioSelected = Integer.parseInt(similarContainersMap.get(rbKey).toString());
		 boolean dropDownDisable = false;
		 boolean textBoxDisable = false;
								
			if(radioSelected == 1)
			{									
				textBoxDisable = true;
			}
			else if(radioSelected == 2)
			{
				dropDownDisable = true;									
		    }					
	
				
					String resetNameFunction = "resetName('"+ i+"')";
					attrNames[0] = "similarContainerMapValue(simCont:"+i+"_parentContainerId)";
					attrNames[1] = "similarContainerMapValue(simCont:"+i+"_positionDimensionOne)";
					attrNames[2] = "similarContainerMapValue(simCont:"+i+"_positionDimensionTwo)";
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
								<html:text styleClass="formFieldSized10"  maxlength="100"  size="40" styleId="<%=contNameSId%>" property="<%=containerNameKey%>" />
								&nbsp;
								<html:link href="#" styleId="newSite" onclick="<%=resetNameFunction%>">
								<bean:message key="StorageContainer.resetName" />
								</html:link>
				
							</td>
								<% 
		    					}else{
							    %>
							<td class="formField" nowrap>
								<html:text styleClass="formFieldSized10"  maxlength="255"  size="40" styleId="<%=contNameSId%>" property="<%=containerNameKey%>"/>
								&nbsp;
								<html:link href="#" styleId="newSite" onclick="<%=resetNameFunction%>">
								<bean:message key="StorageContainer.resetName" />
								</html:link>
							</td>
			
								<% 
		    					}
							    %>
						    <td class="formField">
							<html:text styleClass="formFieldSized10"  maxlength="255"  size="30" styleId="<%=barSId%>" property="<%=barcodeKey%>" />
							</td>
			
							<td class="formField">
								<table summary="" cellpadding="3" cellspacing="0" border="0">
									<tr>
										<td class="formFieldNoBordersSimple">
										<logic:equal name="storageContainerForm" property="checkedButton" value="1">
										<html:select property="<%=siteKey%>" styleClass="formField" styleId="<%=siteSId%>" size="1" onchange="<%=onSiteChange%>">
										<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
										</html:select>
										</logic:equal>
										</td>
									</tr>
									
									
								
										<td nowrap>
										<logic:equal name="storageContainerForm" property="checkedButton" value="2">	
										
										
					    	  <table border="0">
								<tr>
								<td ><html:radio value="1" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
								<html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
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
								<td ><html:radio value="2" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/></td>
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

			<tr>
			   	<td align="right" colspan="6">
	   			<html:submit styleClass="actionButton" onclick="onClick(this)">
					<bean:message key="buttons.submit"/>
				</html:submit>
				</td>
	    	 </tr>			  	
					  
					</table>
					
				</td>
			</tr>	
			
			</table>
		</td>
	</tr>	
	
		
<%
		
	} //If pageOf != PAGEOF_SIMILAR_CONTAINERS
%>
</table>
</html:form>