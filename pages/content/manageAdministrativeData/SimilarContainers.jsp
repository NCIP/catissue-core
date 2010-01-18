<!-- 
	This JSP page is to create/display similar containers from/of Parent Storage Container.
	Author : Chetan B H
	Date   : 
-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility" %>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm" %>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>

<%
        //String operation = (String) request.getAttribute(Constants.OPERATION);
		//String containerNumber=(String)request.getAttribute("ContainerNumber");
		//List siteForParent = (List)request.getAttribute("siteForParentList");
		Object obj = request.getAttribute("storageContainerForm");
        //Added for printing
		String printAction ="printStorageContainer";
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
		int rowCounter=	(int)form.getNoOfContainers();
%>
<head>
	<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script>var imgsrc="catissuecore/images/de/";</script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/prototype.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/scr.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/combobox.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/ext-base.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/ext-all.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/combos.js"></script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/ajax.js"></script>

	<link href="css/catissue_suite.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />


	<script>
		Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Tissue';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_tissue',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'tissue'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});
	</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Fluid';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_fluid',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'fluid'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Cell';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_cell',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'cell'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Molecular';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_molecular',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'molecular'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>
	

	<script language="JavaScript">
		function onCreate()
		{
			var action = '<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>';			
			action = action + "?pageOf=" + '<%=Constants.PAGE_OF_CREATE_SIMILAR_CONTAINERS%>' + "&menuSelected=7";
			//alert("action "+action);
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		function selectSpType()
	{
		var tissue = document.getElementById('holdsTissueSpType');		
		var fluid  = document.getElementById('holdsFluidSpType');
		var cell   = document.getElementById('holdsCellSpType');
		var mol    = document.getElementById('holdsMolSpType');
		if (tissue != null)
		{
			for (i = tissue.options.length-1; i >= 0; i--)
			{
				tissue.options[i].selected=true;
			}
		}

		if (fluid != null)
		{
			for (i = fluid.options.length-1; i >= 0; i--)
			{
				fluid.options[i].selected=true;
			}
		}
		
		if (cell != null)
		{
			for (i = cell.options.length-1; i >= 0; i--)
			{
				cell.options[i].selected=true;
			}
		}
		
		if (mol != null)
		{
			for (i = mol.options.length-1; i >= 0; i--)
			{
				mol.options[i].selected=true;
			}
		}
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
			//action = action + "?pageOf="+"<%=Constants.PAGE_OF_CREATE_SIMILAR_CONTAINERS%>"+"&menuSelected=7";
			var action = document.forms[0].action; //"<%=Constants.CREATE_SIMILAR_CONTAINERS_ACTION%>";
			action = action + "?pageOf="+"<%=Constants.PAGE_OF_CREATE_SIMILAR_CONTAINERS%>"+"&operation=add&menuSelected=7";
			document.forms[0].action = action
			//Added for printing
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{
				setSubmittedForPrint('ForwardTo','<%=printAction%>','success');
			}
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
			

			var typeElement = document.forms[0].typeName.value;
			var siteElement = document.getElementById("siteId_"+i);

			if(typeElement.value != "-1" && siteElement.value != "-1" )
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
					
					
					//var siteNameElement = document.forms["storageContainerForm"].getElementById("siteId");//similarContainerMapValue(simCont:1_siteName)
					var formElements = document.forms["storageContainerForm"].elements;
					var siteNameElement = formElements["similarContainerMapValue(simCont:"+i+"_siteName)"];
					siteNameElement.value = maxSiteName;
					
					
				}	
			

		}
		function onParentContainerChange(i)
		{
		}
		function getSiteName(id)
		{
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
		
	//function containing the common code for enable and disable of auto/manual text boxes	
	function commonShowHideCode(rowNo,menuSelectedValue)
	{
		var st1 = "container_" + rowNo + "_0";
		var pos1 = "pos1_" + rowNo + "_1";
		var pos2 = "pos2_" + rowNo + "_2";
		var st2="customListBox_" + rowNo + "_0";
		var pos11="customListBox_" + rowNo + "_1";
		var pos12="customListBox_" + rowNo + "_2";
		var mapButton="mapButton_" + rowNo ;
		var stContainerNameFromMap = document.getElementById(st1);
		var pos1FromMap = document.getElementById(pos1);
		var pos2FromMap = document.getElementById(pos2);    		    		
		var stContainerNameFromDropdown = document.getElementById(st2);
		var pos1FromDropdown = document.getElementById(pos11);
		var pos2FromDropdown = document.getElementById(pos12);    		    		
		var containerMapButton =  document.getElementById(mapButton);

		var autoDiv = document.getElementById("auto_"+rowNo);
		var manualDiv = document.getElementById("manual_"+rowNo);

		if(menuSelectedValue == 1)
		{
			stContainerNameFromMap.disabled = true;
			pos1FromMap.disabled = true;
			pos2FromMap.disabled = true;

			containerMapButton.disabled = true;
			stContainerNameFromDropdown.disabled = false;
			pos1FromDropdown.disabled = false;
			pos2FromDropdown.disabled = false;

			manualDiv.style.display='none';
			autoDiv.style.display  = 'block';
		}
		else if(menuSelectedValue == 2)
		{
			stContainerNameFromMap.disabled = false;
			pos1FromMap.disabled = false;
			pos2FromMap.disabled = false;

			containerMapButton.disabled = false;
			stContainerNameFromDropdown.disabled = true;
			pos1FromDropdown.disabled = true;
			pos2FromDropdown.disabled = true;

			autoDiv.style.display  = 'none';	
			manualDiv.style.display = 'block';	
		}
	}

	function onStorageRadioClickInAliquot(element)
	{		
		elementId=element.id;
		var index1 =  elementId.lastIndexOf('_');
		var index2 =  elementId.lastIndexOf(')');
		//rowNumber of the element
		var i = (elementId).substring(index1+1,index2);
		commonShowHideCode(i,element.value);		
	}		
	
	//onload function for assigning the storagetype same as they were earlier prior when submit pressed
	function setPositionsOnLoad()
	{
		<% for(int i=1;i<=rowCounter;i++)
		{%>
			var selectBox = document.getElementById("similarContainerMapValue(radio_<%=i%>)");
			commonShowHideCode(<%=i%>,selectBox.value);			
		<%}%>
	}

	function mapButtonClickedInAliquot(frameUrl,count)
	{
	   	var storageContainer = document.getElementById("container_" + count + "_0").value;
		frameUrl+="&storageContainerName="+storageContainer;
		//Patch ID: Bug#4116_2
		openPopupWindow(frameUrl,'similarContainerPage');
    }	
	</script>

</head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<link href="css/styleSheet.css" rel="stylesheet" type="text/css" />
<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<body onload="javascript:showHide('sp_type')">
<body onload="setPositionsOnLoad()">
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<html:form action="<%=Constants.SIMILAR_CONTAINERS_ADD_ACTION%>">


<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	<%
		StorageContainerForm simForm = (StorageContainerForm)request.getAttribute("storageContainerForm");
		
		String sType = simForm.getTypeName();
		String siteName = simForm.getSiteName();
		String siteId = Long.toString(simForm.getSiteId());
		String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
		String checkButtonStatus;
		String parentContainerSelected = (String)(simForm.getParentContainerSelected());
		if("Site".equals(parentContainerSelected))
		{
			checkButtonStatus="1";
		}
		else
		{
			checkButtonStatus="2";
		}
		String noOfContainers = Integer.toString(simForm.getNoOfContainers());
		//int maxIdentifier = Integer.parseInt((String)request.getAttribute(Constants.MAX_IDENTIFIER));
	//maxIdentifier++;
		if(!Constants.PAGE_OF_SIMILAR_CONTAINERS.equals(pageOf))
		{
		%>
		<tr>
            <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                      <td width="10%" valign="bottom" ><img src="images/uIEnhancementImages/sc_similar.gif" alt="Container Info" width="127" height="20" /></td>
                      <td valign="bottom" class="cp_tabbg">&nbsp;</td>
                      </tr>
                  </table></td>
                </tr>
                <tr>
     
			<html:hidden property="id" />
			<html:hidden property="operation" value="add"/>
	
    
			<html:hidden property="siteId" value="<%=siteId%>"/>
			<html:hidden property="siteForParentContainer"/>	
	
			<html:hidden property="noOfContainers" value="<%=noOfContainers%>" />
			<html:hidden property="typeName"/>
	
  			<html:hidden property="submittedFor" value=""/>	
			<html:hidden property="forwardTo" value=""/>
			<html:hidden property="nextForwardTo" />
  	
  	
			<html:hidden property="similarContainerMapValue(checkedButton)" value="<%=checkButtonStatus%>"/>
			<html:hidden property="parentContainerSelected"/>
		</td>
    </tr>
  	
    <tr>
	    <td class="cp_tabtable"><br>
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<tr>
              		 <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                     <td width="17%" align="left" class="black_ar"><bean:message key="storageContainer.type"/></td>
					 <td width="28%""align="left"><html:select property="typeId" styleClass="formFieldSizedNew" styleId="storageContainerType" size="1"><html:options collection="<%=Constants.STORAGETYPELIST%>" labelProperty="name" property="value"/></html:select></td>
				  
                      <td width="1%" align="center" class="black_ar">&nbsp;</td>
                      <td width="17%" align="left" valign="top" class="black_ar"><label for="defaultTemperature"><bean:message key="storageContainer.temperature"/></label></td>
					 <td width="38%"align="left" class="grey_ar"><html:text styleClass="black_ar" style="text-align:right" maxlength="50"  size="15" styleId="defaultTemperature" property="defaultTemperature" /><span class="black_ar">&nbsp;<sup>0</sup>C</span></td>
				  </tr>
				   <tr>
                       <td align="center" valign="top" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></td>
					   <td align="left" valign="top" class="black_ar" onmouseover="Tip(' <%=label1%>')">
					   <%
							String displayLabel1 = label1;
							int label1Lenght=label1.length();
							if(label1Lenght >= 20)
							  displayLabel1 = label1.substring(0,20)+"...";
							else
							  displayLabel1 =label1.substring(0,label1Lenght);
						%>
						<%=displayLabel1%>
					</td>
					 <td align="left">
						<html:text styleClass="black_ar"  maxlength="50"  size="15" style="text-align:right" styleId="oneDimensionCapacity" property="oneDimensionCapacity" />
					</td>
				 
                      <td align="center" valign="top" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="4" /></td>
                      <td align="left" valign="top" class="black_ar" onmouseover="Tip(' <%=label2%>')">
						<%
							if(label2 != null || ( label2 != null && !(label2.equals("")) ) )
							{
								String displayLabel2 = label2;
								 label1Lenght=label2.length();
							    if(label1Lenght >= 20)
							     displayLabel2 = label2.substring(0,20)+"...";
							   else
							     displayLabel2 =label2.substring(0,label1Lenght);
						%>
						<%=displayLabel2%>
						<%
							}
						%>
					</td>
					 <td align="left">
						<html:text styleClass="black_ar"  maxlength="50"  size="15" style="text-align:right" styleId="twoDimensionCapacity" property="twoDimensionCapacity" />
					</td>
				  </tr>
				  <tr>
                      <td colspan="6" class="bottomtd"></td>
                  </tr>
	               <tr>
                    <td align="center" class="black_ar">&nbsp;</td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="storageContainer.collectionProtocolTitle"/></td>
					<td align="left" class="grey_ar"><html:select property="collectionIds" styleClass="formFieldSizedSC" styleId="collectionIds" size="4"	 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" multiple="true" >	<html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/></html:select>&nbsp;</td>
					<td align="center" class="black_ar" colspan="3">&nbsp;</td>
				  </tr>
				   <tr>
                    <td align="center" class="black_ar">&nbsp;</td>
                    <td align="left" valign="top" class="black_ar"><bean:message key="storageContainer.holds"/></td>
                    <td align="left" class="grey_ar" colspan="5">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
                             <td align="left" class="tabletd1">&nbsp;<bean:message key="storageContainer.containerType"/></td>
							 <td align="left" class="tabletd1"><label><html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClickOfSpecimen('Specimen')"/> <bean:message key="storageContainer.specimenClass"/><label></td>
							 <td align="left" class="tabletd1"><label><html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClickOfSpecimen('SpecimenArray')"/> <bean:message key="storageContainer.specimenArrayType"/><label></td>
							</tr>
						
							<tr>
                              <td width="26%" align="left" class="tabletd1"><html:select property="holdsStorageTypeIds" styleClass="formFieldSizedSC" styleId="holdsStorageTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection="<%=Constants.HOLDS_LIST1%>" labelProperty="name" property="value"/></html:select></td>
						      <td width="26%" align="left" class="tabletd1">
							  <logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
								<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/></html:select>
							 </logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray">
							<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSizedSC" styleId="holdsSpecimenClassTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
								<html:options collection="<%=Constants.HOLDS_LIST2%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
						</td>
						<td width="25%" align="left" class="tabletd1">
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="SpecimenArray">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSizedSC" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="specimenOrArrayType" value="Specimen">
							<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSizedSC" styleId="holdsSpecimenArrTypeIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
								<html:options collection="<%=Constants.HOLDS_LIST3%>" labelProperty="name" property="value"/>
							</html:select>
							</logic:equal>
						</td>
						
				  </tr>
				 <tr>
                      <td colspan="6" class="bottomtd"></td>
                  </tr>
				 </table></td>
				
                              </tr>
				
		<tr onclick="javascript:showHide('sp_type')">
          <td width="96%" colspan="5" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="storageType.holdsSpecimenClass"/></span></td>
          <td width="4%" align="right" class="tr_bg_blue1">
			  <a id="imgArrow_sp_type">
				  <img src="images/uIEnhancementImages/up_arrow.gif" width="80" height="9" hspace="10" border="0"/>
			  </a>
		  </td>
		</tr>
		
		<tr>
			<td colspan="6" valign="top" class="showhide1"><div id="sp_type" style="display:block">
				
				<table width="100%" colspan="6" border="0" cellpadding="2" cellspacing="0">
					 <tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsTissueSpType">
								<bean:message key="specimenclass.tissue" />
							</label>
						</td>	
						<div id="tissueDIV">
						<td width="35%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="tissue" styleClass="tabletd1" 
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('tissue','holdsTissueSpType', 'add')" 
								removeButtonOnClick="moveOptions('holdsTissueSpType','tissue', 'edit')" 
								selectIdentifier="holdsTissueSpType" 
								collection="<%=(List) request.getAttribute(Constants.TISSUE_SPECIMEN)%>" numRows="5"/>
						</td>
						</div>
					</tr>
					
					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsFluidSpType">
								<bean:message key="specimenclass.fluid" />
							</label>
						</td>	
						<td width="35%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="fluid" styleClass="tabletd1" 
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('fluid','holdsFluidSpType', 'add')" 
								removeButtonOnClick="moveOptions('holdsFluidSpType','fluid', 'edit')" 
								selectIdentifier="holdsFluidSpType" 
								collection="<%=(List) request.getAttribute(Constants.FLUID_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsCellSpType">
								<bean:message key="specimenclass.cell" />
							</label>
						</td>	
						<td width="35%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="cell" styleClass="tabletd1" 
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('cell','holdsCellSpType', 'add')" 
								removeButtonOnClick="moveOptions('holdsCellSpType','cell', 'edit')" 
								selectIdentifier="holdsCellSpType" 
								collection="<%=(List) request.getAttribute(Constants.CELL_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsMolSpType">
								<bean:message key="specimenclass.molecular" />
							</label>
						</td>	
						<td width="35%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="molecular" styleClass="tabletd1" 
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('molecular','holdsMolSpType', 'add')" 
								removeButtonOnClick="moveOptions('holdsMolSpType','molecular', 'edit')" 
								selectIdentifier="holdsMolSpType" 
								collection="<%=(List)request.getAttribute(Constants.MOLECULAR_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
					</table>
						</td>
							</tr>
						<tr><td>&nbsp;</td></tr>
		 		       <tr>
						 <td colspan="6">
				          <table width="100%" border="0" cellspacing="0" cellpadding="3">
                            <tr>
								<td colspan="2"  width="7%" class="tabletd1">#</td>
							 <% if(!Variables.isStorageContainerLabelGeneratorAvl ) {
				    	     %>                              
							   <td class="tabletd1"><strong><bean:message key="storageContainer.containerName"/></strong></td>
							  <%
							} %>
							<% if(!Variables.isStorageContainerBarcodeGeneratorAvl ) {
				    		%>
                             <td class="tabletd1"><strong><bean:message key="storageContainer.barcode"/></strong></td>
                             <%}%>
						     <% int colspanValue =0 ;
								if(Variables.isStorageContainerLabelGeneratorAvl ) {
									colspanValue++;
								}
          						if(Variables.isStorageContainerBarcodeGeneratorAvl ) {
          							colspanValue++;
          						}						    
						   %>
						   
							 <td class="tabletd1"><strong>
							<logic:equal name="storageContainerForm" property="parentContainerSelected" value="Site">
							<bean:message key="storageContainer.site"/>
							</logic:equal>
							<logic:notEqual name="storageContainerForm" property="parentContainerSelected" value="Site">
							<bean:message key="storageContainer.parentContainer"/>
							</logic:notEqual></strong></td>
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
							//String buttonOnClicked  = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','800','600','no');return false";
	 	                    //String buttonOnClicked = "StorageMapWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&amp;containerStyleId=customListBox_1_0&amp;xDimStyleId=customListBox_1_1&amp;yDimStyleId=customListBox_1_2&amp;storageType=','name','810','320','yes');return false";
							
							
						
							String noOfEmptyCombos = "3";
							
						%>
					
				<%=ScriptGenerator.getJSForOutermostDataTable()%>
			
	  
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
	
		       String buttonOnClicked = "mapButtonClickedInAliquot('"+frameUrl+"','"+i+"')"; //javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false";
		
		//falguni //generate hidden variable for sitename select combo box values
		String siteNamekey = "similarContainerMapValue(simCont:" + i + "_siteName)";
		String siteNamevalue = (String)similarContainersMap.get("simCont:"+i+"_siteName");
		%>
		<html:hidden property="<%=siteNamekey%>" value="<%=siteNamevalue%>"/>
		<%
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
						<td width="5%" class="black_ar"><%=i%>
						</td>
                           <td class="black_ar" align="left"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="5" /></td>
						    <% if(!Variables.isStorageContainerLabelGeneratorAvl ) {
				    		%>
				    	
		    				<% 
				    		if(i == 1 && (simForm.getContainerName() != null) && !(simForm.getContainerName().equals("")) )
					    	{
			    			%>
						    <td class="black_ar_t"><html:text styleClass="black_ar"  maxlength="100"  size="30" styleId="<%=contNameSId%>" property="<%=containerNameKey%>" /></td>
								<% 
		    					}else{
							    %>
							 <td class="black_ar_t">
								<html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="<%=contNameSId%>" property="<%=containerNameKey%>"/>
								&nbsp;
								<%-- <html:link href="#" styleId="newSite" onclick="<%=resetNameFunction%>"> 
								<bean:message key="StorageContainer.resetName" />
								</html:link>--%>
							</td>
			
								<% 
		    					}
							    %>
							 <%}%>  
							 <% if(!Variables.isStorageContainerBarcodeGeneratorAvl ) {
				    		%> 
						    <td class="black_ar_t"><html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="<%=barSId%>" property="<%=barcodeKey%>" /></td>
							<%
							} %>
							
							<% int colspanValue1 =0 ;
						    if(Variables.isStorageContainerLabelGeneratorAvl ) {
						    colspanValue1++;
						    }
          			        if(Variables.isStorageContainerBarcodeGeneratorAvl ) {
          			        colspanValue1++;
          			        }
						    
						   %>
							<td class="black_ar"  >
								<table summary="" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td colspan="2" align="left" class="black_ar">
										<logic:equal name="storageContainerForm" property="parentContainerSelected" value="Site">
										<html:select property="<%=siteKey%>" styleClass="formFieldSizedNew" styleId="<%=siteSId%>" size="1" onchange="<%=onSiteChange%>">
										<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>
										</html:select>
										</logic:equal>
										</td>
									
									
									
							
							<td nowrap class="black_ar">
							<logic:notEqual name="storageContainerForm" property="parentContainerSelected" value="Site">	
							  <table border="0"  cellpadding="2" cellspacing="0">
								<tr>
								<td>
								
								<!--<html:radio value="1" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/>-->

								<html:select property="<%=stContSelection%>" styleClass="black_ar"
											styleId="<%=stContSelection%>" size="1"	onmouseover="showTip(this.id)"
											onmouseout="hideTip(this.id)" onchange= "onStorageRadioClickInAliquot(this)">
										<html:options collection="storageListForTransferEvent"
														labelProperty="name" property="value" />
								</html:select>
								&nbsp;&nbsp;
								</td>
								<html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
								<td align="left" class="black_ar">
								
								<div id="auto_<%=i%>" style="display:block" >
									<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
										attributeNames="<%=attrNames%>" 
										initialValues="<%=initValues%>"  
										styleClass = "black_ar" 
										tdStyleClass = "<%=tdStyleClass%>" 
										labelNames="<%=labelNames%>" 
										rowNumber="<%=rowNumber%>" 
										onChange = "<%=onChange%>"
										formLabelStyle="nComboGroup"
										disabled = "<%=dropDownDisable%>"
										tdStyleClassArray="<%=tdStyleClassArray%>"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
								    	</tr>
										</table>
								</div>
								</td>
							
								<!--<td>
								<html:radio value="2" onclick="onStorageRadioClickInAliquot(this)" styleId="<%=stContSelection%>" property="<%=stContSelection%>"/>
								</td>-->
								<td align="left" class="black_ar">
								
								<div id="manual_<%=i%>" style="display:none" >
									<html:text styleClass="black_ar"  size="25" styleId="<%=containerStyle%>" property="<%=containerNameFromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="black_ar"  size="5" styleId="<%=pos1Style%>" property="<%=pos1FromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:text styleClass="black_ar"  size="5" styleId="<%=pos2Style%>" property="<%=pos2FromMapKey%>" disabled = "<%=textBoxDisable%>"/>
									<html:button styleClass="black_ar" styleId = "<%=containerMapStyle%>" property="<%=containerMap%>" onclick="<%=buttonOnClicked%>" disabled = "<%=textBoxDisable%>">
										<bean:message key="buttons.map"/>
									</html:button>
								</div>

								</td>
							</tr>
						</table>
						</logic:notEqual>

						</td>
						</tr>			
						</table>	
					  </td>
					</tr>		   
	  			
	   	
					  <%
					  	//maxIdentifier++;
						} //For
					  %>

                     <tr>
						<td class="dividerline" colspan="5"><span class="black_ar"></td>
					</tr>

                   <tr>
                     <table>
					   <tr>
					    <td colspan="1" width="20%" nowrap>
							<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
								<span class="black_ar">
									<bean:message key="print.checkboxLabel"/>
							</span>
					       </html:checkbox>
					    </td>							
	<!--  Added for displaying  printer type and location -->
					    <td>
					   	    <%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 		   </td>
				     </tr>
				   </table>
              	  </tr>	

			  <tr>
                <td colspan="6" class="buttonbg">
                <html:submit styleClass="blue_ar_b" onclick="onClick(this)">
                <bean:message key="buttons.submit"/></html:submit>
                </td>
	    	 </tr>			  	
					  
					</table>
					
				</td>
			</tr>	
			
			<!--
			/**
			 * Name : Vijay_Pande
			 * Reviewer Name : Sachin_Lale 
			 * Bug ID: 4141
			 * Patch ID: 4141_1 
			 * See also: -
			 * Description: Following three tags are commented to remove imbalancing os tags.
	     	*/
	     -->
		<!--	</table>
		</td>
	</tr>	-->
	
		
<%
		
	} //If pageOf != PAGEOF_SIMILAR_CONTAINERS
%>
</table>
</html:form>
<script language="JavaScript" type="text/javascript">
showPriterTypeLocation();
</script>
</body>