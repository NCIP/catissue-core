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
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm"%>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ page import="edu.wustl.catissuecore.bean.DeriveSpecimenBean"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			window.parent.frames['CPTreeView'].location="ProtocolEventsDetails.do?pageOf=newEvent";
	}
	
	function viewSummary()
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
<link rel="STYLESHEET" type="text/css" href="css/catissue_suite.css">
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
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

				
//<!------------------------------------------------------------------------------------>
		//checkbox
		var rowno=q+1;
		var checkb=x.insertCell(0);
		checkb.className="black_ar";
		//checkb.colSpan=2;
		sname="";
		var name = "checkBox_" + rowno;
		sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>";
		checkb.innerHTML=""+sname;
//		alert(sname);
		// srno
		//var spreqno=x.insertCell(1)
		//spreqno.className="formFieldNoBordersSimple";
		
		var srIdentifier = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_id)";
		var cell1 = "<input type='hidden' name='" + srIdentifier + "' value='' id='" + srIdentifier + "'>";
		//spreqno.innerHTML="" + rowno+"." + cell1;
		
		//type
		var spreqtype=x.insertCell(1)
		spreqtype.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenClass)";
		var objunit = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_unit)";
		var concentration = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";
		var specimenClassName = objname;
		var objsubtype = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";

		sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"','"+concentration+"','"+objsubtype +"') class='formFieldSized8' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
		<%for(int i=0;i<specimenClassList.size();i++)
		{
			String specimenClassLabel = "" + ((NameValueBean)specimenClassList.get(i)).getName();
			String specimenClassValue = "" + ((NameValueBean)specimenClassList.get(i)).getValue();
		%>
			sname = sname + "<option value='<%=specimenClassValue%>'><%=specimenClassLabel%></option>";
		<%}%>
		sname = sname + "</select>";
		 
		spreqtype.innerHTML="" + sname + cell1;
		
		//subtype
		var spreqsubtype=x.insertCell(2)
		spreqsubtype.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";
		var functionName = "onSubTypeChangeUnitforCP('" + specimenClassName + "',this,'" + objunit + "')" ;
		
		sname= "<select name='" + objname + "' size='1' class='addRow_s' id='" + objname + "' onChange=" + functionName + " onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
		
		sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";

		sname = sname + "</select>"
		
		spreqsubtype.innerHTML="" + sname;
		
		//Storage Location
		var spreqsubtype=x.insertCell(3)
		spreqsubtype.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_storageLocation)";
		
		sname= "<select name='" + objname + "' size='1' class='formFieldSized8' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";

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
		spreqqty.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_quantity)";

		sname="<input type='text' name='" + objname + "' value='0' maxlength='10' size='12' class='black_ar' id='" + objname + "'>"        	
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
						
		spreqqty.innerHTML="" + sname;

		//Concentration
		var spreqqty=x.insertCell(5)
		spreqqty.className="black_ar";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";

		sname="<input type='text' name='" + objname + "' value='0' size='12'  maxlength='10' class='black_ar' id='" + objname + "'>"        	
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
						
		spreqqty.innerHTML="" + sname;
		
		
	}
	window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&key=<%=mapKey%>&operation=${requestScope.operation}";
</script>

</head>
<body>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>


<html:form action="CreateSpecimenTemplate.do">
<html:hidden property="noOfDeriveSpecimen"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
			<td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td valign="bottom" ><img src="images/uIEnhancementImages/cp_specimen.gif" alt="Specimen Requirements" width="158" height="20" /><a href="#"></a></td>
                    <td width="85%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                  </tr>
              </table></td>
		 </tr>
      <tr>
              <td class="cp_tabtable">
                  <br>
                  <table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
	                  <tr>
                      <td colspan="3" align="left"><table width="100%" border="0" cellpadding="3" cellspacing="0">
		                <tr>
			               <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
						   <td width="22%" align="left" class="black_ar"><bean:message key="specimen.type"/> </td>
						   <td width="33%" align="left" nowrap class="black_ar">
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
										  styleClass="formFieldSizedText"
										  
										/>
								</td>
                                <td width="1%" align="center"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
							    <td width="15%" align="left"><label for="type" class="black_ar"><bean:message key="specimen.subType"/></label></td>
                                <td width="28%" align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="type"
									  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
									  initialValue="<%=form.getType()%>"
									  onChange="<%=subTypeFunctionName%>"
									  readOnly="<%=readOnlyForAliquot%>"
									  dependsOn="<%=form.getClassName()%>"
									  styleClass="formFieldSizedText"
									  
							        />
								</td>
                              </tr>
                              <tr>
                                 <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                 <td align="left" class="black_ar"><bean:message key="specimen.tissueSite"/></td>
                                 <td width="30%" align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="tissueSite"
									  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
									  initialValue="<%=form.getTissueSite()%>"
									  readOnly="<%=readOnlyForAliquot%>"
									  styleClass="formFieldSizedText"
								      
									/>
				<%
					String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
				%>
									<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
										<img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="16" height="16" title='Tissue Site Selector' alt="Clinical Diagnosis">
									</a>
                                </td>
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                               <td align="left" class="black_ar"><bean:message key="specimen.tissueSite"/></td>
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="tissueSide"
										optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
									    initialValue="<%=form.getTissueSide()%>"
									    readOnly="<%=readOnlyForAliquot%>"
										styleClass="formFieldSizedText"
								    />
								</td>
                              </tr>
                              <tr>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><bean:message key="specimen.pathologicalStatus"/> </td>
                                <td width="30%" align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
									  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
									  initialValue="<%=form.getPathologicalStatus()%>"
									  readOnly="<%=readOnlyForAliquot%>"
									  styleClass="formFieldSizedText"
									/>
								</td>
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                               <td align="left" class="black_ar"><bean:message key="cpbasedentry.storagelocation"/></td>
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="storageLocationForSpecimen"
									  optionsList = "<%=request.getAttribute("storageContainerList")%>"
									  initialValue="<%=form.getStorageLocationForSpecimen()%>"
									  styleClass="formFieldSizedText"
									/>
								</td>
                              </tr>
                              <tr>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><bean:message key="specimen.quantity"/></td>
                                <td align="left" class="black_ar_s">	<html:text styleClass="black_ar" size="22" maxlength="10"styleId="quantity" property="quantity"/><span id="unitSpan">&nbsp;<%=unitSpecimen%></span><html:hidden property="unit"/></td>
                                <td align="center" class="black_ar">&nbsp;</td>
                                <td align="left" class="black_ar"><bean:message key="specimen.concentration"/></td>
                                <td align="left" class="black_ar_s">
			<%
				boolean concentrationDisabled = true;
				if(form.getClassName().equals("Molecular") && !Constants.ALIQUOT.equals(form.getLineage()))
				concentrationDisabled = false;
			%>
     									<html:text styleClass="black_ar" maxlength="10"  size="22"	styleId="concentration" property="concentration"  readonly="<%=readOnlyForAll%>" disabled="<%=concentrationDisabled%>"/>&nbsp;<bean:message key="specimen.concentrationUnit"/></td>
                              </tr>
                              <tr>
									<html:hidden property="collectionEventId" />
									<html:hidden property="collectionEventSpecimenId" />
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                            <td align="left" class="black_ar"><bean:message key="specimen.collectedevents.username"/></td>
                                <td align="left" class="black_ar"> 
									<autocomplete:AutoCompleteTag property="collectionEventUserId"
									  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
									  initialValue="<%=new Long(form.getCollectionEventUserId())%>"
									  staticField="false"
									  styleClass="formFieldSizedText"
									/>
								</td>
								<html:hidden property="receivedEventId" />
									<html:hidden property="receivedEventSpecimenId" />
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                               <td align="left" class="black_ar"><bean:message key="specimen.receivedevents.username"/></td>
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="receivedEventUserId"
									  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
									  initialValue="<%=new Long(form.getReceivedEventUserId())%>"
									  staticField="false"
									  styleClass="formFieldSizedText"
									/>
								</td>
                              </tr>
                              <tr>
                               <td align="center" class="black_ar">&nbsp;</td>
                              <td align="left" class="black_ar"><bean:message key="cpbasedentry.collectionprocedure"/></td>
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
									  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
									  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
									  styleClass="formFieldSizedText"
									/>
								</td>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><label for="institutionId"><bean:message key="cpbasedentry.receivedquality"/></label></td>
								<!-- receivedeventparameters.receivedquality -->
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
									  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
									  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
									  styleClass="formFieldSizedText"
									/>
								</td>
                              </tr>
                              <tr>
							  <!-- CollectionEvent fields -->
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><label for="departmentId"><bean:message key="cpbasedentry.collectioncontainer"/></label></td>
                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="collectionEventContainer"
									  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
									  initialValue="<%=form.getCollectionEventContainer()%>"
									  styleClass="formFieldSizedText"
								    />
								</td>
                                <td align="center" class="black_ar">&nbsp;</td>
                                <td align="left" class="black_ar"><label></label></td>
                                <td align="left">&nbsp;</td>
                              </tr>
                            </table>
                            <br>
                        
					</td>
                   </tr>
                   <tr onclick="javascript:showHide('derive_specimen')">
					 <td width="97%" align="left" class="tr_bg_blue1">
						<span class="blue_ar_b">
							<bean:message key="cpbasedentry.derivespecimens"/>
						</span>
					  </td>
                      <td width="3%" align="right" class="tr_bg_blue1">
						<a href="#" id="imgArrow_derive_specimen">
							<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/>
						</a>
					  </td>
                     </tr>
                       <td colspan="2" class="showhide1">
							<div id="derive_specimen" style="display:none" >
								<table width="100%" border="0" cellspacing="0" cellpadding="4">
									<tr>
                              <td width="6%" class="tableheading"><span class="black_ar_b">
                                <label for="delete" align="center"><bean:message key="addMore.delete" /></label>
                              </span></td>
                              <td width="15%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> </span><bean:message key="collectionprotocol.specimenclass" /> </span></td>
                              <td width="23%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="collectionprotocol.specimetype" /> </span></td>
                              <td width="20%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="cpbasedentry.storagelocation"/></span></td>
                              <td width="21%" class="tableheading"><span class="black_ar_b"><bean:message key="collectionprotocol.quantity" /></span></td>
                              <td width="15%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="cpbasedentry.concentration"/></span></td>
                            </tr>
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
<tr>

										
				    
					<html:hidden property="<%=id%>" />
				               
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
								<td class="black_ar"><label>
                                  <input type=checkbox name="<%=chk%>" id="<%=chk%>" >
                                </label></td>
                                <td class="black_ar" >
									<html:select property= "<%=specimenClass%>"
									styleClass="formFieldSized8" 
									styleId="<%=specimenClass%>" size="1"
									onchange="<%=changeClass%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
									</html:select>
								</td>
                                <td class="black_ar" >
									<html:select property="<%=specimenType%>" 
									styleClass="addRow_s" 
									styleId="<%=specimenType%>"  size="1"
									onchange="<%=changeType%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
									</html:select>
								</td>
                                <td class="black_ar" >
									<html:select property="<%=storageLocation%>" 
									styleClass="formFieldSized8" 
									styleId="<%=storageLocation%>" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection= "storageContainerList" labelProperty="name" property="value"/>
									</html:select>
								</td>
                                <td class="black_ar">
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
									<html:text styleClass="black_ar" size="12"  maxlength="10" 
										styleId="<%=quantity%>" property="<%=quantity%>" 
										value="<%=qtyValue%>" />
									<span id="<%=unit%>">
										<%=strHiddenUnitValue%>
									</span>
								</td>
                                <td class="black_ar">
									<html:text styleClass="black_ar" size="12"  maxlength="10" 
										styleId="<%=concentration%>" property="<%=concentration%>" 
										disabled="<%=concReadOnly%>" value="<%=concValue%>" />
								</td>
                              </tr>
			
                              
			<%
				}
			%>
							</tbody>
							<%
							String deleteSpecimenRequirements = "deleteChecked('DeriveSpecimenBean','CreateSpecimenTemplate.do?operation="+operation+"&pageOf=delete',document.forms[0].noOfDeriveSpecimen,'checkBox_',false)"; 
			%>
							<tr>
                                <td colspan="6">
									<html:button property="addSpecimenReq" styleClass="black_ar" value="Add More" disabled="<%=disabled%>" onclick="insRow('DeriveSpecimenBean')"/>
			
									<html:button property="deleteSpecimenReq" styleClass="black_ar" onclick="<%=deleteSpecimenRequirements %>" disabled="<%=disabled%>"><bean:message key="buttons.delete"/>
									</html:button>
								</td>
                              </tr>
                          </table>
						</div>
					</td>
                  </tr>
                   <tr>
                      <td colspan="2" class="bottomtd"></td>
                    </tr>
                  <tr onclick="javascript:showHide('aliquot')">
                     <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"><bean:message key="cpbasedentry.aliquots"/></span></td>
                    <td align="right" class="tr_bg_blue1">
						<a href="#" id="imgArrow_aliquot">
							<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/>
						</a>
					</td>
                 </tr>
                 <tr>
                    <td colspan="2" align="left" class="showhide1">
						<div id="aliquot" style="display:none" >
							<table width="100%" border="0" cellspacing="0" cellpadding="4">
								<tr>
		                           <td width="27%" class="black_ar" >										
									   		<bean:message key="aliquots.noOfAliquots"/>
										 &nbsp;
										 <html:text styleClass="black_ar" styleId="noOfAliquots" size="12" property="noOfAliquots" style="text-align:right" maxlength="50"/>
									</td>
		                            <td width="33%" class="black_ar">
										<label for="qtyPerAliquot">
									   		<bean:message key="aliquots.qtyPerAliquot"/>
										 </label>&nbsp;
										 <html:text styleClass="black_ar" styleId="quantityPerAliquot" size="12" property="quantityPerAliquot" style="text-align:right" maxlength="50"/>
									</td>
		                            <td width="40%" class="black_ar">
										<label for="storageLocation">
											<bean:message key="cpbasedentry.storagelocation"/>
										</label>&nbsp;
										<autocomplete:AutoCompleteTag property="storageLocationForAliquotSpecimen"
											    optionsList = "<%=request.getAttribute("storageContainerList")%>"
												initialValue="<%=form.getStorageLocationForAliquotSpecimen()%>"
												styleClass="formFieldSizedText"
												/>
									</td>
								</tr>
                          </table>
						</div>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2" class="bttomtd"></td>
                    </tr>
                    <tr>
                       <td colspan="2" class="buttonbg">
							<html:button styleClass="blue_ar_b" property="submitPage" onclick="saveSpecimens()"		disabled="<%=disabled%>">
								<bean:message key="cpbasedentry.savespecimenrequirements"/>
							</html:button>
						</td>
                    </tr>
             </table>
		</td>
	</tr>
</table>
</html:form>
</body>


