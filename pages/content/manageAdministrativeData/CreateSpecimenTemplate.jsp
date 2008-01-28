<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ include file="/pages/content/common/CollectionProtocolCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm"%>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ page import="edu.wustl.catissuecore.bean.DeriveSpecimenBean"%>

<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>


<%
List storageContainerList	= (List)request.getAttribute("storageContainerList");
Object obj = request.getAttribute("createSpecimenTemplateForm");
String mapKey = (String)request.getAttribute("key");
String nodeKey = (String)request.getAttribute("mapkey");
String operation = (String)request.getAttribute(Constants.OPERATION);
String operationType =null;
boolean disabled = false;
HttpSession newSession = request.getSession();
CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean)newSession.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
operationType = collectionProtocolBean.getOperation();
if(operationType!=null && operationType.equals("update"))
{
	disabled = true;
}

if(nodeKey!=null)
{
	mapKey = (String)request.getAttribute("mapkey");
	operation = "edit";
}
CreateSpecimenTemplateForm form =null;
int noOfDeriveSpecimen = 0;
Map mapOfDeriveSpecimen = null;
if(obj != null && obj instanceof CreateSpecimenTemplateForm)
{
	form = (CreateSpecimenTemplateForm)obj;
	mapOfDeriveSpecimen = form.getDeriveSpecimenValues();
	noOfDeriveSpecimen = form.getNoOfDeriveSpecimen();
}	



String unitSpecimen = "";
if(form != null)
{
		if(form.getClassName().equals("Tissue"))
		{
			if((form.getType()!=null) && (form.getType().equals(Constants.FROZEN_TISSUE_SLIDE)||form.getType().equals(Constants.FIXED_TISSUE_BLOCK)||form.getType().equals(Constants.FROZEN_TISSUE_BLOCK)||form.getType().equals(Constants.NOT_SPECIFIED)||form.getType().equals(Constants.FIXED_TISSUE_SLIDE)))
			{
				unitSpecimen = Constants.UNIT_CN;
			}
			else if((form.getType()!=null) && (form.getType().equals(Constants.MICRODISSECTED)))
			{
				unitSpecimen = Constants.UNIT_CL;
			}
			else 
			{
				unitSpecimen = Constants.UNIT_GM;
			}
				
		}
		if(form.getClassName().equals("Fluid"))
		{
			unitSpecimen = Constants.UNIT_ML;
		}
		if(form.getClassName().equals("Cell"))
		{
			unitSpecimen = Constants.UNIT_CC;
		}
		if(form.getClassName().equals("Molecular"))
		{
			unitSpecimen = Constants.UNIT_MG;
		}
}


%>
<%!
	boolean readOnlyValue=false,readOnlyForAll=false;
	boolean concReadOnly=true;
%>

<%!
	private String changeUnit(String specimenType, String subTypeValue)
	{
		if (specimenType == null)
			return "";
		if(specimenType.equals("Fluid"))
			return Constants.UNIT_ML;
		else if(specimenType.equals("Tissue"))
		{
			if(subTypeValue.equals(Constants.FROZEN_TISSUE_SLIDE) || subTypeValue.equals(Constants.FIXED_TISSUE_BLOCK) || subTypeValue.equals(Constants.FROZEN_TISSUE_BLOCK) || subTypeValue.equals(Constants.NOT_SPECIFIED)|| subTypeValue.equals(Constants.FIXED_TISSUE_SLIDE) )
				return Constants.UNIT_CN;
			else if (subTypeValue.equals(Constants.MICRODISSECTED))
			{
				return Constants.UNIT_CL;
			}
			else	
				return Constants.UNIT_GM;
		}
		else if(specimenType.equals("Cell"))
			return Constants.UNIT_CC;
		else if(specimenType.equals("Molecular"))
		{
			concReadOnly=false;
			return Constants.UNIT_MG;
		}
		else
			return " ";
			
	}
%>
<%
	List listOfspecimenClass = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);

	List listOfspecimenType = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);

	HashMap mapOfspecimenType = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);

%>


<SCRIPT LANGUAGE="JavaScript">

<%

	Iterator specimenTypeIterator = mapOfspecimenType.keySet().iterator();
	int classCount=0;
	for(classCount=1;classCount<listOfspecimenClass.size();classCount++  )
	{
		String keyObj = (String)((NameValueBean)listOfspecimenClass.get(classCount)).getName() ;
		List subList = (List)mapOfspecimenType.get(keyObj);
		String arrayData = "";
		for(int listSize=0;listSize<subList.size();listSize++ )
		{
			if(listSize == subList.size()-1 )
				arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\"";
			else
    			arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\",";   
		}
%>
		var <%=keyObj%>Array = new Array(<%=arrayData%>);
<%	    		
	}

%>	

		function typeChangeCP(element,arrayName)
		{ 
			var i = (element.name).lastIndexOf("_");
			var combo = (element.name).substring(0,i);
			var specimenTypeCombo = combo + "_specimenType)";
			ele = document.getElementById(specimenTypeCombo);
			//To Clear the Combo Box
			ele.options.length = 0;
			
			//ele.options[0] = new Option('-- Select --','-1');
			var j=0;
			//Populating the corresponding Combo Box
			arrayName.sort();
			for(i=0;i<arrayName.length;i++)
			{
					ele.options[j++] = new Option(arrayName[i],arrayName[i]);
			}
		}



	//Mandar 25-Apr-06 Bug id:1414 :- Tissue Type updated
	var subTypeData1 = "<%=Constants.FROZEN_TISSUE_SLIDE%>";
	var subTypeData2 = "<%=Constants.FIXED_TISSUE_BLOCK%>";
	var subTypeData3 = "<%=Constants.FROZEN_TISSUE_BLOCK%>";
	var subTypeData4 = "<%=Constants.NOT_SPECIFIED%>";
	var subTypeData5 = "<%=Constants.MICRODISSECTED%>";
	var subTypeData6 = "<%=Constants.FIXED_TISSUE_SLIDE%>";


// units array
	var ugul = new Array(7);
	ugul[0]=" ";
	ugul[1]="<%=Constants.UNIT_ML%>";
	ugul[2]="<%=Constants.UNIT_GM%>";
	ugul[3]="<%=Constants.UNIT_CC%>";
	ugul[4]="<%=Constants.UNIT_MG%>";
	ugul[5]="<%=Constants.UNIT_CN%>";
	ugul[6]="<%=Constants.UNIT_CL%>";
	
// Changes unit on subtype list changed
/*	
	Function updated to adjust New Tissue Types. 	
	mandar: 25-Apr-06 : bug 1414:
*/
	function onSubTypeChangeUnitforCP(typeList,element,unitspan)
	{
		var classList = document.getElementById(typeList);
		var className = classList.options[classList.selectedIndex].text;
		var selectedOption = element.options[element.selectedIndex].text;
	
		if(className == "Tissue" && (selectedOption == subTypeData1 || selectedOption == subTypeData2 || selectedOption == subTypeData3 || selectedOption == subTypeData4 || selectedOption == subTypeData6))
		{
			document.getElementById(unitspan).innerHTML = ugul[5];
		}	
		else 
		{
			if(className == "Tissue")
			{
				if(selectedOption == subTypeData5)
				{
					document.getElementById(unitspan).innerHTML = ugul[6];
				}
				else
				{
					document.getElementById(unitspan).innerHTML = ugul[2];
				}
			}	
		}
			
	}

// changes unit on specimen class changed.
	function changeUnit(listname,unitspan,concentration,subTypeListName)
	{
		var list = document.getElementById(listname);
		var selectedOption = list.options[list.selectedIndex].text;
		document.getElementById(concentration).disabled = true;
		var subTypeList = document.getElementById(subTypeListName);

		if(selectedOption == "-- Select --")
		document.getElementById(unitspan).innerHTML = ugul[0];
		if(selectedOption == "Fluid")
		{
			document.getElementById(unitspan).innerHTML = ugul[1];
			typeChangeCP(list,FluidArray);
		}
		if(selectedOption == "Tissue")
		{
			document.getElementById(unitspan).innerHTML = ugul[2];
			typeChangeCP(list,TissueArray);
		}
		if(selectedOption == "Cell")
		{
			document.getElementById(unitspan).innerHTML = ugul[3];
			typeChangeCP(list,CellArray);
		}
		if(selectedOption == "Molecular")
		{
			document.getElementById(unitspan).innerHTML = ugul[4];
			document.getElementById(concentration).disabled = false;
			typeChangeCP(list,MolecularArray);
		}
	}

	function saveSpecimens()
	{
		var action ="SaveSpecimenRequirements.do?pageOf=specimenRequirement&redirectTo=defineEvents&key="+"<%=mapKey%>"+"&operation="+"<%=operation%>";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
	function addNewEvent()
	{
			window.parent.frames['SpecimenRequirementView'].location="ProtocolEventsDetails.do?pageOf=newEvent";
	}
	
	function ViewSummary()
	{
		var action ="GenericSpecimenSummary.do?Event_Id="+"<%=mapKey%>";
		document.forms[0].action = action;
		document.forms[0].submit();
	}

	function clearTypeCombo()
	{
		document.getElementById("type").value = "";
	}
	
</script>

<head>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<script language="JavaScript">
	
	function insRow(subdivtag)
	{
		var noOfDeriveSpecimen = parseInt(document.forms[0].noOfDeriveSpecimen.value);
		noOfDeriveSpecimen = noOfDeriveSpecimen + 1;
		document.forms[0].noOfDeriveSpecimen.value = noOfDeriveSpecimen;
		var sname = "";

		var r = new Array(); 
		r = document.getElementById(subdivtag).rows;
		var q = r.length;
		var x=document.getElementById(subdivtag).insertRow(0);

		// srno
		var spreqno=x.insertCell(0)
		spreqno.className="formFieldNoBordersSimple";
		var rowno=q+1;
		var srIdentifier = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_id)";
		var cell1 = "<input type='hidden' name='" + srIdentifier + "' value='' id='" + srIdentifier + "'>";
		spreqno.innerHTML="" + rowno+"." + cell1;
		
		//type
		var spreqtype=x.insertCell(1)
		spreqtype.className="formFieldNoBordersSimple";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenClass)";
		var objunit = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_unit)";
		var concentration = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";
		var specimenClassName = objname;
		var objsubtype = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";

		sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"','"+concentration+"','"+objsubtype +"') class='formFieldSized10' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
		<%for(int i=0;i<specimenClassList.size();i++)
		{
			String specimenClassLabel = "" + ((NameValueBean)specimenClassList.get(i)).getName();
			String specimenClassValue = "" + ((NameValueBean)specimenClassList.get(i)).getValue();
		%>
			sname = sname + "<option value='<%=specimenClassValue%>'><%=specimenClassLabel%></option>";
		<%}%>
		sname = sname + "</select>";
		 
		spreqtype.innerHTML="" + sname;
		
		//subtype
		var spreqsubtype=x.insertCell(2)
		spreqsubtype.className="formFieldNoBordersSimple";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";
		var functionName = "onSubTypeChangeUnitforCP('" + specimenClassName + "',this,'" + objunit + "')" ;
		
		sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onChange=" + functionName + " onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
		
		sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";

		sname = sname + "</select>"
		
		spreqsubtype.innerHTML="" + sname;
		
		//Storage Location
		var spreqsubtype=x.insertCell(3)
		spreqsubtype.className="formFieldNoBordersSimple";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_storageLocation)";
		
		sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";

		<%
			for(int i=0;i<storageContainerList.size();i++)
		{
			String storageContainerLabel = "" + ((NameValueBean)storageContainerList.get(i)).getName();
			String storageContainerValue = "" + ((NameValueBean)storageContainerList.get(i)).getValue();
		%>
			sname = sname + "<option value='<%=storageContainerValue%>'><%=storageContainerLabel%></option>";
		<%}%>

		sname = sname + "</select>"
		
		spreqsubtype.innerHTML="" + sname;

		//qty
		var spreqqty=x.insertCell(4)
		spreqqty.className="formFieldNoBordersSimple";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_quantity)";

		sname="<input type='text' name='" + objname + "' value='0' maxlength='10' class='formFieldSized5' id='" + objname + "'>"        	
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
						
		spreqqty.innerHTML="" + sname;

		//Concentration
		var spreqqty=x.insertCell(5)
		spreqqty.className="formFieldNoBordersSimple";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";

		sname="<input type='text' name='" + objname + "' value='0'  maxlength='10' class='formFieldSized5' id='" + objname + "'>"        	
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
						
		spreqqty.innerHTML="" + sname;
		
		//checkbox
		var checkb=x.insertCell(6);
		checkb.className="formFieldNoBordersSimple";
		checkb.colSpan=2;
		sname="";
		var name = "checkBox_" + rowno;
		sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>";
		checkb.innerHTML=""+sname;
	}

	window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&key=<%=mapKey%>";
</script>

</head>
<body>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>


<html:form action="CreateSpecimenTemplate.do">
<table summary="" cellpadding="3" cellspacing="0" border="0" width="800">
 <tr>
  <td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
	<tr>
		<td>
			<html:hidden property="noOfDeriveSpecimen"/>
		</td>
	</tr>
	</table>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
		<tr>
			<td class="formTitle" height="20" width="100%" colspan="7">
				<%
				if(operation.equals("add"))
				{
				%>
					<bean:message key="cpbasedentry.addspecimenrequirements"/>							
				<%
				}
				else
				{
				%>
					<bean:message key="cpbasedentry.editspecimenrequirements"/>	<%=form.getDisplayName()%>													
				<%
				}
				%>
				
			</td>
		</tr>
		<tr>
		 	<td class="formFieldNoBordersSimple" width="5">*</td>
		    <td class="formFieldNoBordersSimple">
		     	<label for="className">
		     		<b><bean:message key="specimen.type"/></b>
		     	</label>
		    </td>
		    <td class="formFieldNoBordersSimple">
			<%
				String classValue = (String)form.getClassName();
				specimenTypeList = (List)specimenTypeMap.get(classValue);
				boolean subListEnabled = false;
				if(specimenTypeList == null)
				{
					specimenTypeList = new ArrayList();
					specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
				}
				if(Constants.ALIQUOT.equals(form.getLineage()))
				{
					specimenTypeList = new ArrayList();
					specimenTypeList.add(new NameValueBean(form.getType(),form.getType()));
				}
				pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
				String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
				String readOnlyForAliquot = "false";
				String readOnlyForSpecimen = "false";
				if(Constants.ALIQUOT.equals(form.getLineage())&&operation.equals(Constants.EDIT)) 
				{
				      readOnlyForAliquot = "true";
				}
				if(!Constants.DERIVED_SPECIMEN.equals(form.getLineage())&&operation.equals(Constants.EDIT)) 
				{
				      readOnlyForSpecimen = "true";
				}
			%>
			    <autocomplete:AutoCompleteTag property="className"
						  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
						  initialValue="<%=form.getClassName()%>"
						  readOnly="<%=readOnlyForSpecimen%>"
						  onChange="onTypeChange(this);clearTypeCombo()"
				/>
	       	</td>
		    <td class="formFieldNoBordersSimple" width="5">*</td>
		    <td class="formFieldNoBordersSimple">
		     	<label for="type">
		     		<b><bean:message key="specimen.subType"/></b>
		     	</label>
		    </td>				    
		    <td class="formFieldNoBordersSimple" >
		    
		   <autocomplete:AutoCompleteTag property="type"
						  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
						  initialValue="<%=form.getType()%>"
						  onChange="<%=subTypeFunctionName%>"
						  readOnly="<%=readOnlyForAliquot%>"
						  dependsOn="<%=form.getClassName()%>"
	        />
	
	       	</td>
		</tr>
		<tr>
		     <td class="formFieldNoBordersSimple" width="5">*</td>
		     <td class="formFieldNoBordersSimple">
				<label for="tissueSite">
					<b><bean:message key="specimen.tissueSite"/></b>
				</label>
			</td>
		     <td class="formFieldNoBordersSimple" >
	            <autocomplete:AutoCompleteTag property="tissueSite"
						  size="150"
						  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
						  initialValue="<%=form.getTissueSite()%>"
						  readOnly="<%=readOnlyForAliquot%>"
					    />
				<%
					String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
				%>
				<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
					<img src="images\Tree.gif" border="0" width="24" height="18" title='Tissue Site Selector'>
				</a>
	       	 </td>
		     <td class="formFieldNoBordersSimple" width="5">*</td>
		     <td class="formFieldNoBordersSimple">
				<label for="tissueSide">
					<b><bean:message key="specimen.tissueSide"/></b>
				</label>
			</td>
		     <td class="formFieldNoBordersSimple" >
			  <autocomplete:AutoCompleteTag property="tissueSide"
						  optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
						  initialValue="<%=form.getTissueSide()%>"
						  readOnly="<%=readOnlyForAliquot%>"
					    />
	       	  </td>
		</tr>
		<tr>
		    <td class="formFieldNoBordersSimple" width="5">*</td>
		    <td class="formFieldNoBordersSimple">
				<label for="pathologicalStatus">
					<b><bean:message key="specimen.pathologicalStatus"/></b>
				</label>
			</td>
			<td class="formFieldNoBordersSimple" >
				<autocomplete:AutoCompleteTag property="pathologicalStatus"
							  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
							  initialValue="<%=form.getPathologicalStatus()%>"
							  readOnly="<%=readOnlyForAliquot%>"
				/>
	       	</td>
			
			<td class="formFieldNoBordersSimple" width="5">*</td>
			<td class="formFieldNoBordersSimple">
				<label>
					<b><bean:message key="cpbasedentry.storagelocation"/></b>
				</label>
			</td>
			<td class="formFieldNoBordersSimple">
				<autocomplete:AutoCompleteTag property="storageLocationForSpecimen"
						  optionsList = "<%=request.getAttribute("storageContainerList")%>"
						  initialValue="<%=form.getStorageLocationForSpecimen()%>"
				/>
			</td>
  		</tr>
	    
	    <td class="formFieldNoBordersSimple" width="5">*</td>
		 <td class="formFieldNoBordersSimple">
			<label for="quantity">
				<b><bean:message key="specimen.quantity"/></b>
			</label>
		</td>
	    <td class="formFieldNoBordersSimple" >
	     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity"/>
	     	<span id="unitSpan"><%=unitSpecimen%></span>
	     	<html:hidden property="unit"/>
	    </td>
		<td class="formFieldNoBordersSimple" width="5">
	     	&nbsp;
	    </td>
	    <td class="formFieldNoBordersSimple">
			<label for="concentration">
				<bean:message key="specimen.concentration"/>
			</label>
		</td>
		<td class="formFieldNoBordersSimple" >
		<%
			boolean concentrationDisabled = true;
			if(form.getClassName().equals("Molecular") && !Constants.ALIQUOT.equals(form.getLineage()))
				concentrationDisabled = false;
		%>
     		<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="concentration" property="concentration" 
     		readonly="<%=readOnlyForAll%>" disabled="<%=concentrationDisabled%>"/>
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
		</td>
	 </tr>
	 <tr>	
		<html:hidden property="collectionEventId" />
		<html:hidden property="collectionEventSpecimenId" />
		<td class="formFieldNoBordersSimple" width="5">*</td>
			<td class="formFieldNoBordersSimple"> 
			<label for="user">
				<b><bean:message key="specimen.collectedevents.username"/> </b>
			</label>
		</td>						
		<td class="formFieldNoBordersSimple">
			<autocomplete:AutoCompleteTag property="collectionEventUserId"
						  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
						  initialValue="<%=new Long(form.getCollectionEventUserId())%>"
						  staticField="false"
			/>		
		</td>		
		<!-- RecievedEvent fields -->
		<html:hidden property="receivedEventId" />
		<html:hidden property="receivedEventSpecimenId" />
		<td class="formFieldNoBordersSimple" width="5">*</td>
		<td class="formFieldNoBordersSimple">
			<label for="type">
				<b><bean:message key="specimen.receivedevents.username"/> </b>
			</label>
		</td>
		<td class="formFieldNoBordersSimple">
			<autocomplete:AutoCompleteTag property="receivedEventUserId"
				  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
				  initialValue="<%=new Long(form.getReceivedEventUserId())%>"
				  staticField="false"
		/>	
	</tr>
	<tr>
	 	<td class="formFieldNoBordersSimple" width="5">*</td>
		<td class="formFieldNoBordersSimple">
			<label for="collectionprocedure">
				<b><bean:message key="cpbasedentry.collectionprocedure"/></b>
			</label>
		</td>
		<td class="formFieldNoBordersSimple">
				<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
						  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
						  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
		/>							
		</td>						
		
		<!-- RecievedEvent fields -->
		<td class="formFieldNoBordersSimple" width="5"rowspan="2">*</td>
		<td class="formFieldNoBordersSimple"rowspan="2"> 
			<label for="quality">
				<b><bean:message key="cpbasedentry.receivedquality"/></b>
			</label>
		</td>						
		<!-- receivedeventparameters.receivedquality -->
		<td class="formFieldNoBordersSimple"rowspan="2">
			<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
						  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
						  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
		/>
		</td>
	</tr>
	
	<!-- CollectionEvent fields -->	
	<tr>							
		<td class="formFieldNoBordersSimple" width="5">*</td>
		<td class="formFieldNoBordersSimple">
			<label for="container">
				<b><bean:message key="cpbasedentry.collectioncontainer"/></b>
			</label>
		</td>
		<td class="formFieldNoBordersSimple">
			<autocomplete:AutoCompleteTag property="collectionEventContainer"
					  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
					  initialValue="<%=form.getCollectionEventContainer()%>"
	    />
		</td>
	</tr>	
	</table>
	&nbsp;
	<table summary="" cellpadding="3" cellspacing="1" border="0" width="700" id="aliquotId">
		
		<tr>
			<td class="formTitle" height="20" width="700" colspan="7">
				<bean:message key="cpbasedentry.aliquots"/>							
			</td>
		</tr>
		<tr>					
			<td class="formFieldNoBordersSimple">
				 <label>
			   		<bean:message key="aliquots.noOfAliquots"/>
				 </label>
			</td>
			<td class="formFieldNoBordersSimple">
	            <html:text styleClass="formFieldSized5" styleId="noOfAliquots" property="noOfAliquots"/>
			</td>
			<td class="formFieldNoBordersSimple">
				 <label>
			   		<bean:message key="aliquots.qtyPerAliquot"/>
				 </label>
			</td>
	         <td class="formFieldNoBordersSimple">
				<html:text styleClass="formFieldSized5" styleId="quantityPerAliquot" property="quantityPerAliquot"/>
		    </td>
			<td class="formFieldNoBordersSimple">
				<label>
					<b><bean:message key="cpbasedentry.storagelocation"/></b>
				</label>
			</td>
			<td class="formFieldNoBordersSimple">
					<autocomplete:AutoCompleteTag property="storageLocationForAliquotSpecimen"
						  optionsList = "<%=request.getAttribute("storageContainerList")%>"
						  initialValue="<%=form.getStorageLocationForAliquotSpecimen()%>"
				/>
			</td>
		</tr>								
	</table>	
	&nbsp;
	<table summary="" cellpadding="3" cellspacing="0" border="0" width=700>
		<tr>
			<td colspan="5" class="formTitle">
				<b><bean:message key="cpbasedentry.derivespecimens"/></b>
			</td>
			<td class="formTitle">	
				<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" disabled="<%=disabled%>" onclick="insRow('DeriveSpecimenBean')"/>
			</td>
			<td class="formTitle" align="Right">
			<%String deleteSpecimenRequirements = "deleteChecked('DeriveSpecimenBean','CreateSpecimenTemplate.do?operation="+operation+"&pageOf=delete',document.forms[0].noOfDeriveSpecimen,'checkBox_',false)"; %>
				<html:button property="deleteSpecimenReq" styleClass="actionButton" onclick="<%= deleteSpecimenRequirements %>" disabled="<%=disabled%>">
						<bean:message key="buttons.delete"/>
				</html:button>
			</td>
		</tr>
	    <tr> 
			<td class="formLeftSubTitle">
				<bean:message key="collectionprotocol.specimennumber" />
			</td>
			<td class="formLeftSubTitle">* 
				<bean:message key="collectionprotocol.specimenclass" />
			</td>
			<td class="formLeftSubTitle">* 
				<bean:message key="collectionprotocol.specimetype" />
			</td>
			<td class="formLeftSubTitle">* 
				<b><bean:message key="cpbasedentry.storagelocation"/></b>
			</td>
			<td class=formLeftSubTitle>*
				<bean:message key="collectionprotocol.quantity" />
			</td>
			<td class=formLeftSubTitle>*
				<b><bean:message key="cpbasedentry.concentration"/></b>
			</td>
			
			<td class="formLeftSubTitle">
				<label for="delete" align="center">
					<bean:message key="addMore.delete" />
				</label>
			</td>
		</tr><!-- SUB TITLES END -->
		
			<script> document.forms[0].noOfDeriveSpecimen.value = <%=noOfDeriveSpecimen%> </script>
			<TBODY id="DeriveSpecimenBean">
			<%

				for(int rowno=1;rowno<=noOfDeriveSpecimen;rowno++)
				{
					String id = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_id)";
					String specimenClass = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenClass)";
					String classKey = "DeriveSpecimenBean:" + rowno + "_specimenClass";
					String unit = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_unit)";
					String specimenUnit = "DeriveSpecimenBean:" + rowno + "_unit";
					String specimenType = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";
					String srSubTypeKeyName = "DeriveSpecimenBean:" + rowno + "_specimenType";
					String storageLocation = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_storageLocation)";
					String quantity = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_quantity)";
					String quantityvalue = "DeriveSpecimenBean:" + rowno + "_quantity";
					String concentration = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";
					String MolecularConc = "DeriveSpecimenBean:" + rowno + "_concentration";
					String chk = "checkBox_" + rowno;
					String changeClass = "changeUnit('"+specimenClass+"','"+unit+"','"+concentration+"','"+specimenType+"')";
					String changeType = "onSubTypeChangeUnitforCP('"+specimenClass+"','" + unit+ "')";
			%>
				<TR>
				<td class="formFieldNoBordersSimple">
					<%=rowno%>.
					<html:hidden property="<%=id%>" />
				</td>
				<%
					String className = (String)form.getDeriveSpecimenValue(classKey);
					String typeclassValue = (String)form.getDeriveSpecimenValue(srSubTypeKeyName);
					specimenTypeList = (List)specimenTypeMap.get(className);
					boolean subListEnabled1 = false;
					if(specimenTypeList == null)
					{
						specimenTypeList = new ArrayList();
						specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
						specimenClassList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
					}
					pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
					pageContext.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
				%>
				<td class="formFieldNoBordersSimple">		
					<html:select property= "<%=specimenClass%>"
									styleClass="formFieldSized10" 
									styleId="<%=specimenClass%>" size="1"
									onchange="<%=changeClass%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
				
				<td class="formFieldNoBordersSimple">
					<html:select property="<%=specimenType%>" 
									styleClass="formFieldSized10" 
									styleId="<%=specimenType%>"  size="1"
									onchange="<%=changeType%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
				 
				<td class="formFieldNoBordersSimple">
					<html:select property="<%=storageLocation%>" 
									styleClass="formFieldSized10" 
									styleId="<%=storageLocation%>" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection= "storageContainerList" labelProperty="name" property="value"/>
					</html:select>
				</td>
				
				<td class="formFieldNoBordersSimple">
				<%
						
					String qtyValue = (String)form.getDeriveSpecimenValue(quantityvalue);
					String concValue = (String)form.getDeriveSpecimenValue(MolecularConc);
					String strHiddenUnitValue = "" + changeUnit(className,typeclassValue);
					if(qtyValue == null || qtyValue.equals("")) 
					{
						qtyValue="0";
					}
					if(concValue == null || concValue.equals(""))
					{
						concValue="0";
					}
				%>
				<html:text styleClass="formFieldSized5" size="30"  maxlength="10" 
							styleId="<%=quantity%>" 
							property="<%=quantity%>" 
							value="<%=qtyValue%>" />
					<span id="<%=unit%>">
						<%=strHiddenUnitValue%>
					</span>
				</td>
				
				<td class="formFieldNoBordersSimple">
					<html:text styleClass="formFieldSized5" size="30"  maxlength="10" 
							styleId="<%=concentration%>" 
							property="<%=concentration%>" 
							disabled="<%=concReadOnly%>"
							value="<%=concValue%>"
							/>
				</td>
				
				<td class="formFieldNoBordersSimple" width="5">
					<input type=checkbox name="<%=chk%>" id="<%=chk%>" >
				</td>					
			</TR>	
			 <%
				}
			%>
		</TBODY>
	</table>
	&nbsp;&nbsp;
	<table>
		<tr>
			<td align="right" colspan="6">
				<html:button styleClass="actionButton" property="submitPage" onclick="saveSpecimens()" disabled="<%=disabled%>">
					<bean:message key="cpbasedentry.savespecimenrequirements"/>
				</html:button>
			<td>
		<tr>
	</table>
	</td>
</tr>	
</table>
</html:form>
</body>

