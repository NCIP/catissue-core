<!-- 
	This JSP page is to create/display aliquots from/of Parent Specimen.
	Author : Aniruddha Phadnis
	Date   : May 12, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="java.util.*"%>

<%
	/* Retrieving a map of available containers */
	Map containerMap = (Map)request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
	Object [] containerId = containerMap.keySet().toArray();
%>

<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	
	<script language="JavaScript">
		//Converting container map in JavaScript hashtable
		var availableContainerTable = new Hashtable();
		var availableDimX;
		var availableDimY;
		<%
			for(int i=0;i<containerId.length;i++)
			{
				Map dimXMap = (Map)containerMap.get(Integer.valueOf(containerId[i].toString()));
				Object [] dimX = dimXMap.keySet().toArray();
		%>
		
			//Recreating a hashtable of available X dimensions (Rows) vs. Y dimensions (Columns) in each iteration
			availableDimX = new Hashtable();
			
		<%
				for(int j=0;j<dimX.length;j++)
				{
					List dimYList = (List)dimXMap.get(Integer.valueOf(dimX[j].toString()));
		%>
					//Recreating an array of available Y dimensions (Columns)
					availableDimY = new Array();
		<%
					for(int k=0;k<dimYList.size();k++)
					{
						Integer iObj = (Integer)dimYList.get(k);
		%>
						availableDimY[<%=k%>] = "<%=String.valueOf(iObj)%>";
		<%
					} //For Loop K
		%>
					availableDimX.put("<%=dimX[j].toString()%>",availableDimY);
		<%
				} //For Loop J
		%>
				availableContainerTable.put("<%=containerId[i].toString()%>",availableDimX);
		<%
			} //For Loop I
		%>
		
		//This function populates the combo box for dimension X
		function onChangeContainerId(containerId,styleIdX,styleIdY)
		{
			var xCombo = document.getElementById(styleIdX);
			var yCombo = document.getElementById(styleIdY);
			
			//To clear the combo box of dimension X
			xCombo.options.length = 0;
			xCombo.options[0] = new Option("<%=Constants.SELECT_OPTION%>","-1");
			
			//To clear the combo box of dimension Y
			yCombo.options.length = 0;
			yCombo.options[0] = new Option("<%=Constants.SELECT_OPTION%>","-1");
			
			var identifier = containerId.options[containerId.selectedIndex].value;
			
			if(identifier != -1)
			{
				var xTable = availableContainerTable.get(identifier);
				var xTableKey = xTable.keys();
				
				for(var i=0;i<xTableKey.length;i++)
				{
					xCombo.options[i+1] = new Option(xTableKey[i],xTableKey[i]);
				}
			}
		}
		
		//This function populates the combo box for dimension Y
		function onChangeXDimension(containerStyleId,xDimList,styleIdY)
		{
			var yDimList = document.getElementById(styleIdY);
			
			//To clear the combo box of dimension Y
			yDimList.options.length = 0;
			yDimList.options[0] = new Option("<%=Constants.SELECT_OPTION%>","-1");
			
			
			var containerIdList = document.getElementById(containerStyleId);
			var containerId = containerIdList.options[containerIdList.selectedIndex].value;
			var xDim = xDimList.options[xDimList.selectedIndex].value;
			
			if(containerId != -1 && xDim != -1)
			{
				//Retrieving table of X-Y dimensions for given containerId
				var xTable = availableContainerTable.get(containerId);
				var yDimArray = xTable.get(xDim);
				
				for(var i=0;i<yDimArray.length;i++)
				{
					yDimList.options[i+1] = new Option(yDimArray[i],yDimArray[i]);
				}
			}
		}
	</script>
	
	<script language="JavaScript">
		function onCreate()
		{
			var action = '<%=Constants.CREATE_ALIQUOT_ACTION%>';
			action = action + "?pageOf=" + '<%=Constants.PAGEOF_CREATE_ALIQUOT%>' + "&menuSelected=15";
			
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].specimenId.disabled = false;
				document.forms[0].barcode.disabled = true;
			}
			else
			{
				document.forms[0].barcode.disabled = false;
				document.forms[0].specimenId.disabled = true;
			}
		}
	</script>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.ALIQUOT_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
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
		<td class="formRequiredLabelRightBorder" width="120" nowrap>
				<label for="parentId">
					<bean:message key="createSpecimen.parent"/>
				</label>
		</td>
		<td class="formField">
			<logic:equal name="aliquotForm" property="checkedButton" value="1">
				<html:select property="specimenId" styleClass="formField" styleId="specimenId" size="1">
					<html:options collection="<%=Constants.SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
				</html:select>
			</logic:equal>
			
			<logic:equal name="aliquotForm" property="checkedButton" value="2">
				<html:select property="specimenId" styleClass="formField" styleId="specimenId" size="1" disabled="true">
					<html:options collection="<%=Constants.SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
				</html:select>
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
			<html:button styleClass="actionButton" property="submitPage" onclick="onCreate()">
				<bean:message key="buttons.create"/>
			</html:button>
		</td>
	</tr>
	</table>
</td>
</tr>

<%
	AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
	String unit = "";

	if(form != null)
	{
		unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
	}

	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	if(!Constants.PAGEOF_ALIQUOT.equals(pageOf))
	{
%>

<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	<tr>
		<td>
			<%--html:hidden property="systemIdentifier" /--%>
		</td>
	</tr>
	
	<%--tr>
		<td class="formMessage" colspan="3">* indicates a required field</td>
	</tr--%>
	
	<tr>
		<td class="formTitle" height="20" colspan="3">
			<bean:message key="aliquots.title"/>
		</td>
	</tr>
	
	<%--tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="spCollectionGroupId">
				<bean:message key="specimen.specimenCollectionGroupId"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="spCollectionGroupId" property="spCollectionGroupId" readonly="true"/>
		</td>
	</tr--%>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="type">
				<bean:message key="specimen.type"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenClass" property="specimenClass" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="subType">
				<bean:message key="specimen.subType"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="type" property="type" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="tissueSite">
				<bean:message key="specimen.tissueSite"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSite" property="tissueSite" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="tissueSide">
				<bean:message key="specimen.tissueSide"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSide" property="tissueSide" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="pathologicalStatus">
				<bean:message key="specimen.pathologicalStatus"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="pathologicalStatus" property="pathologicalStatus" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="aliquots.initialAvailableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="initialAvailableQuantity" property="initialAvailableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="aliquots.currentAvailableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">*</td>
		<td class="formRequiredLabel">
			<label for="concentration">
				<bean:message key="specimen.concentration"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="concentration" property="concentration" readonly="true"/>
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
		</td>
	</tr>
	</table>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="600">
	<tr>
		<td class="formLeftSubTableTitle" width="5">
	     	#
	    </td>
	    <td class="formRightSubTableTitle">*
			<bean:message key="specimen.quantity"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="specimen.barcode"/>
		</td>
		<td class="formRightSubTableTitle">*
			<bean:message key="aliquots.location"/>
		</td>
	</tr>
	
	<%
		Map aliquotMap = new HashMap();
		int counter=0;

		if(form != null)
		{
			counter = Integer.parseInt(form.getNoOfAliquots());
			aliquotMap = form.getAliquotMap();
		}

		for(int i=1;i<=counter;i++)
		{
			Integer containerSelected = Integer.valueOf((aliquotMap.get("Specimen:" + i + "_StorageContainer_systemIdentifier")).toString());
			Integer xDimSelected = Integer.valueOf((aliquotMap.get("Specimen:" + i + "_positionDimensionOne")).toString());
			Map xMap = (Map)containerMap.get(containerSelected);
			Object [] xDimId = new Object[0];
			Object [] yDimId = new Object[0];

			if(!xMap.isEmpty())
			{
				xDimId = xMap.keySet().toArray();
				List yDimList = (List)xMap.get(xDimSelected);

				if(!yDimList.isEmpty())
				{
					yDimId = yDimList.toArray();
				}
			}

			String qtyKey = "value(Specimen:" + i + "_quantity)";
			String barKey = "value(Specimen:" + i + "_barcode)";
			String containerKey = "value(Specimen:" + i + "_StorageContainer_systemIdentifier)";
			String pos1Key = "value(Specimen:" + i + "_positionDimensionOne)";
			String pos2Key = "value(Specimen:" + i + "_positionDimensionTwo)";

			String containerStyleId = "containerStyleId" + i;
			String pos1StyleId = "pos1StyleId" + i;
			String pos2StyleId = "pos2StyleId" + i;

			String onChangeContainerId = "onChangeContainerId(this,'" + pos1StyleId + "','" + pos2StyleId + "')";
			String onChangeXDimension = "onChangeXDimension('" + containerStyleId + "',this,'" + pos2StyleId + "')";
	%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="<%=qtyKey%>" disabled="false"/>
				&nbsp; <%=unit%>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" disabled="false"/>
			</td>
			<td class="formField" nowrap>
				<html:select property="<%=containerKey%>" styleClass="formFieldSized5" styleId="<%=containerStyleId%>" size="1" onchange="<%=onChangeContainerId%>">
					<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
					<%
						//Populating the container identifiers before loading the page
						for(int count=0;count<containerId.length;count++)
						{
					%>
							<html:option value="<%=containerId[count].toString()%>"><%=containerId[count].toString()%></html:option>
					<%
						}
					%>
				</html:select>
					&nbsp;
				<html:select property="<%=pos1Key%>" styleClass="formFieldSized5" styleId="<%=pos1StyleId%>" size="1" onchange="<%=onChangeXDimension%>">
					<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
				<%
					for(int count=0;count<xDimId.length;count++)
					{
				%>
						<html:option value="<%=xDimId[count].toString()%>"><%=xDimId[count].toString()%></html:option>
				<%
					}
				%>
				</html:select>
					&nbsp;
				<html:select property="<%=pos2Key%>" styleClass="formFieldSized5" styleId="<%=pos2StyleId%>" size="1">
					<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
				<%
					for(int count=0;count<yDimId.length;count++)
					{
				%>
						<html:option value="<%=yDimId[count].toString()%>"><%=yDimId[count].toString()%></html:option>
				<%
					}
				%>
				</html:select>
					&nbsp;
				<html:button styleClass="actionButton" property="mapButton" onclick="">
					<bean:message key="buttons.map"/>
				</html:button>
			</td>
		</tr>
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
  <td align="right" colspan="3">
	<!-- action buttons begins -->
	<table cellpadding="4" cellspacing="0" border="0">
	<tr>
		<td>
			<html:submit styleClass="actionButton" >
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
	} //If pageOf != "Aliquot Page"
%>
</table>
</html:form>