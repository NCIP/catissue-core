<jsp:directive.page import="edu.wustl.common.util.global.ApplicationProperties"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete" %>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm"%>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>
<%@ page import="edu.wustl.catissuecore.bean.DeriveSpecimenBean"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<script language="JavaScript" type="text/javascript" src="javascripts/de/prototype.js"></script>
<%
List storageContainerList	= (List)request.getAttribute("storageContainerList");
List labelGenTypeList	= (List)request.getAttribute("labelGenTypeList");
Object obj = request.getAttribute("createSpecimenTemplateForm");
String mapKey = (String)request.getAttribute("key");
String nodeKey = (String)request.getAttribute("mapkey");
String operation = (String)request.getAttribute(Constants.OPERATION);
String operationType =null;
boolean disabled = false;
operationType = (String)request.getAttribute("opr");
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

	function deleteEvent()
	{
		var answer = confirm ("Are you sure want to delete specimen?")
		if(answer)
		{
			document.forms[0].target = '_top';
			var action ="DeleteNodeFromCP.do?pageOf=specimenRequirement&key="+"<%=mapKey%>"+"&operation=edit";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	}
	function saveSpecimens()
	{
		var action ="SaveSpecimenRequirements.do?pageOf=specimenRequirement&redirectTo=defineEvents&key="+"<%=mapKey%>"+"&operation="+"<%=operation%>"+"&isPersistent=${requestScope.isPersistent}";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
	function addNewEvent()
	{
			window.parent.frames['CPTreeView'].location="ProtocolEventsDetails.do?pageOf=newEvent";
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
function setStudyFormValues(combobox,rowno)
{
		var studyformObj = Ext.getCmp('ds_'+rowno);

	studyformObj.store.proxy.conn.url = "ClinincalStatusComboAction.do?requestFor=specimenType&selSpecClass="+combobox;
	studyformObj.setValue("--Select--",false) ;
	studyformObj.store.reload();
}


	function insRow(subdivtag)
	{
		var noOfDeriveSpecimen = parseInt(document.forms[0].noOfDeriveSpecimen.value);
		noOfDeriveSpecimen = noOfDeriveSpecimen + 1;
		document.forms[0].noOfDeriveSpecimen.value = noOfDeriveSpecimen;
		var sname = "";
		var nameForCombo="";
		var comboDataStorName="";

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

		nameForCombo = "combo_"+objname;
		comboDataStorName = "ds_"+rowno;
		sname = "<input type='text' name='"+nameForCombo+"' id='"+nameForCombo+"' size='6'/>";
		
		
		spreqtype.innerHTML="" + sname + cell1;
		
		var testCombo="Ext.onReady(function(){var myUrl= 'ClinincalStatusComboAction.do?requestFor=specimenClass';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: '"+objname+"' ,displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: '"+nameForCombo+"'});combo.on('expand', function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle('width', '250');combo.innerList.setStyle('width', '250');}else{combo.list.setStyle('width', 'auto');combo.innerList.setStyle('width', 'auto');}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});combo.on('select',function(){setStudyFormValues(combo.value,rowno);});});";
eval(testCombo);

		//subtype
		var spreqsubtype=x.insertCell(2)
		spreqsubtype.className="black_ar";
		spreqsubtype.Wrap = "True";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";
		
		nameForCombo = "combo_"+objname;
		comboDataStorName = "ds_"+rowno;
		sname = "<input type='text' name='"+nameForCombo+"' id='"+nameForCombo+"' size='6'/>";
		spreqsubtype.innerHTML="" + sname;
		var testVar = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenClass)";
		var testCombo="Ext.onReady(function(){var myUrl= 'ClinincalStatusComboAction.do?requestFor=specimenType&selSpecClass=collectionProtocolId';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: '"+objname+"' ,displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: '"+nameForCombo+"',fields: ['id_cp'],id: comboDataStorName});combo.on('expand', function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle('width', '250');combo.innerList.setStyle('width', '250');}else{combo.list.setStyle('width', 'auto');combo.innerList.setStyle('width', 'auto');}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});";
eval(testCombo);

		//Storage Location
		var spreqsubtype=x.insertCell(3)
		spreqsubtype.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_storageLocation)";

		nameForCombo = "combo_"+objname;
		comboDataStorName = "ds_"+rowno;
		sname = "<input type='text' name='"+nameForCombo+"' id='"+nameForCombo+"' size='6'/>";
		spreqsubtype.innerHTML="" + sname;
		
		var testCombo="Ext.onReady(function(){var myUrl= 'ClinincalStatusComboAction.do?requestFor=storageLocation';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: '"+objname+"' ,displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: '"+nameForCombo+"'});combo.on('expand', function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle('width', '250');combo.innerList.setStyle('width', '250');}else{combo.list.setStyle('width', 'auto');combo.innerList.setStyle('width', 'auto');}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});";
eval(testCombo);

		//qty
		var spreqqty=x.insertCell(4)
		spreqqty.className="black_ar";
		sname="";
		objname = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_quantity)";

		sname="<input type='text' name='" + objname + "' style='text-align:right' value='0' maxlength='10' size='3' class='black_ar' id='" + objname + "'>"
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"

		spreqqty.innerHTML="" + sname;

		//Concentration
		var spreqqty=x.insertCell(5)
		spreqqty.className="black_ar";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";

		sname="<input type='text' name='" + objname + "' style='text-align:right' value='0' size='3'  maxlength='10' class='black_ar' id='" + objname + "'>"
		sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"

		spreqqty.innerHTML="" + sname;

		//Events
		var spreqqty=x.insertCell(6)
		spreqqty.className="black_ar";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_creationEvent)";
		nameForCombo = "combo_"+objname;
		comboDataStorName = "ds_ce_"+rowno;
		sname = "<input type='text' name='"+nameForCombo+"' id='"+nameForCombo+"' size='6'/>";
		spreqqty.innerHTML="" + sname;
		var processingSPPName = "";
		
		if(document.getElementById('processingSPPForSpecimen') != null)
	   			processingSPPName = document.getElementById('processingSPPForSpecimen').value;
				
		var testCombo="Ext.onReady(function(){var myUrl= 'ClinincalStatusComboAction.do?requestFor=specimenEvent&processingSPPName='+processingSPPName;var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: '"+objname+"' ,displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',selectOnFocus:'true',applyTo: '"+nameForCombo+"',fields: ['id_cp'],id: comboDataStorName});combo.on('expand', function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle('width', '250');combo.innerList.setStyle('width', '250');}else{combo.list.setStyle('width', 'auto');combo.innerList.setStyle('width', 'auto');}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});";
eval(testCombo);


		//processing SPP
		var spreqqty=x.insertCell(7)
		spreqqty.className="black_ar";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_processingSPP)";
		
		nameForCombo = "combo_"+objname;
		comboDataStorName = "ds_"+rowno;
		sname = "<input type='text' name='"+nameForCombo+"' id='"+nameForCombo+"' size='6'/>";
		
		spreqqty.innerHTML="" + sname;

		var testCombo="Ext.onReady(function(){var myUrl= 'ClinincalStatusComboAction.do?requestFor=processingSPP';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: '"+objname+"' ,displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: '"+nameForCombo+"'});combo.on('expand', function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle('width', '250');combo.innerList.setStyle('width', '250');}else{combo.list.setStyle('width', 'auto');combo.innerList.setStyle('width', 'auto');}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});";
eval(testCombo);

						<%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>
		//Label Format
		var spreqqty=x.insertCell(8)
		spreqqty.className="black_ar";
		sname="";
		objname ="deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_labelFormat)";

		sname="<input type='text' name='" + objname + "' size='25'  maxlength='255' class='black_ar' id='" + objname + "'>"
		sname = sname + "&nbsp;"

		spreqqty.innerHTML="" + sname;
<%}%>
	}
	window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&key=<%=mapKey%>&operation=${requestScope.operation}";
	function resetDSforDerive(element)
	{
		var testvar = element.name;
		testvar = element.value;
		var rows = document.forms[0].noOfDeriveSpecimen.value;
		
		for(var i=1; i<=rows; i++)
		{
				var studyformObj = Ext.getCmp('ds_ce_'+i);
				if(studyformObj != null)
				{

			studyformObj.store.proxy.conn.url = "ClinincalStatusComboAction.do?requestFor=specimenEvent&processingSPPName="+element.value;
			studyformObj.setValue("--Select--",false) ;
			studyformObj.store.reload();
			}
		}
			var studyformObj = Ext.getCmp('aliquot_CE');

			studyformObj.store.proxy.conn.url = "ClinincalStatusComboAction.do?requestFor=specimenEvent&processingSPPName="+element.value;
			studyformObj.setValue("--Select--",false) ;
			studyformObj.store.reload();
		
		
		
	}
</script>

</head>
<body>

<html:form action="CreateSpecimenTemplate.do" styleId = "createSpecimenTemplateForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						 <tr>
							<td valign="bottom" ><img src="images/uIEnhancementImages/cp_specimen.gif" alt="Specimen Requirements" width="158" height="20" /><a href="#"></a></td>
							<td width="90%" valign="bottom" class="cp_tabbg">&nbsp;</td>
		                  </tr>
	              </table>
			</td>
		</tr>
		<html:hidden property="noOfDeriveSpecimen"/>
			<logic:equal name="isParticipantReg" value="true">
				<%@ include file="/pages/content/manageAdministrativeData/PersistentTemplateSpecimen.jsp" %>
			</logic:equal>
			<logic:notEqual name="isParticipantReg" value="true">
				<%@ include file="/pages/content/manageAdministrativeData/NonPersistentTemplateSpecimen.jsp" %>
			</logic:notEqual>
	</table>
</html:form>
</body>
<script>
function labelGenTypechanged(selectBox,labelFormatId)
{
	if(selectBox.value == 2)
	{

		document.getElementById(labelFormatId).value="";
		document.getElementById(labelFormatId).disabled=false;
	}
	else
	{
		document.getElementById(labelFormatId).value="";
		document.getElementById(labelFormatId).disabled=true;
	}

}
function labelGenTypechangedWithId(typechangeId,labelFormatId)
{
	var value= document.getElementById(typechangeId).value;

	if(value == 2)
	{

		document.getElementById(labelFormatId).value="";
		document.getElementById(labelFormatId).disabled=false;
	}
	else
	{
		document.getElementById(labelFormatId).value="";
		document.getElementById(labelFormatId).disabled=true;
	}
}
</script>

