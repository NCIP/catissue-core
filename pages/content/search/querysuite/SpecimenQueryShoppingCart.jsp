<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%> 

<%@ page import="edu.wustl.catissuecore.actionForm.ShoppingCartForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AdvanceSearchForm"%>

<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script  language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>

<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js">
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js" type="text/javascript" charset="utf-8"></script>
<script   language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script  language="JavaScript" type="text/javascript"  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_srnd.js"></script>
    <script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
    <script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
    <link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css">
    <link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
    <link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css">
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/excells/dhtmlxgrid_excell_combo.js"></script>
<script src="jss/script.js"></script>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<link rel="stylesheet" type="text/css" href="css/advQuery/tag-popup.css" />
<script src="dhtmlxSuite_v35/dhtmlxTreeGrid/codebase/dhtmlxtreegrid.js"></script>  

<style>
.active-column-0 {width:30px}
.active-column-4 {width:150px}
.active-column-6 {width:150px}
</style>
<%
    AdvanceSearchForm form = (AdvanceSearchForm)request.getAttribute("advanceSearchForm");  
    String checkAllPages = (String)session.getAttribute("checkAllPages");
    String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
    String isSpecimenIdPresent = (String)request.getAttribute(Constants.IS_SPECIMENID_PRESENT);
    String isContainerPresent=(String)request.getAttribute(Constants.IS_CONTAINER_PRESENT);
    
    if(isSpecimenIdPresent==null)
     isSpecimenIdPresent = "";
     
    if(isContainerPresent==null)
     isContainerPresent = "";
    
    String isSpecimenArrayPresent = (String)request.getAttribute(Constants.IS_SPECIMENARRAY_PRESENT);
    
    String disabled = "";
    boolean disabledList = false;
    if(isSpecimenArrayPresent!= null && isSpecimenArrayPresent.equals("true"))
    {
        disabled = "DISABLED";
        disabledList = true;
    }
    
    String disabledOrder = "";
    String disabledShipping="";
    
    boolean disabledButton = false;
    if(isSpecimenIdPresent.equals("false"))
    {
        disabled = "DISABLED";
        disabledList = true;
        disabledButton = true;
        disabledOrder = "DISABLED";
        disabledShipping="DISABLED";
    }
    
    if(isContainerPresent.equals("true"))
    {
        disabledButton = false;
        disabledShipping="";
    }
    
    if(disabledOrder=="DISABLED" && disabledShipping=="DISABLED")
    {
        disabledButton = true;
    }

    
%>
<head>
<script language="javascript">
function deleteList()
{
    var flag = confirm("Are you sure you want to delete the selected item(s)?");
                if(flag)
                {
                    request = newXMLHTTPReq();          
                    var actionURL;
                    var handlerFunction = getReadyStateHandler(request,onResponseSet,true); 
                    request.onreadystatechange = handlerFunction;   

                    actionURL = "specListId="+ document.getElementById('tagName').value;
                    var url = "CatissueCommonAjaxAction.do?type=deleteSpecimenList";
                    <!-- Open connection to servlet -->
                    request.open("POST",url,true);  
                    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");   
                    request.send(actionURL);
                }
    }

    function onResponseSet(response) 
    {
        document.forms[0].action="ViewSpecimenList.do?operation=view";
        document.forms[0].submit();
    }
function reloadGrid(obj)
{
//alert(obj.value);

    mygrid.clearAll(); 
    mygrid.loadXML("LoadGridServlet?reqParam="+obj.value);  
}
function updateHiddenFields()
    {
        var isCreateRequestShipment = document.forms[0].chkName[4].checked || document.forms[0].chkName[5].checked || document.forms[0].chkName[3].checked;
               var isChecked = "false";
        var checkedRows = mygrid.getCheckedRows(0);
//alert(checkedRows);

        if(checkedRows.length > 0)
        {
            isChecked = "true";
            var cb = checkedRows.split(",");
    //      alert("cb.size()   "+cb.size());
            rowCount = mygrid.getRowsNum();
            var specIds="";
            for(i=0;i<cb.size();i++)
            {
            //alert(cb[i]+"   cb[i]");
                
                                if(isCreateRequestShipment == true)
                               {
                                 var cl = mygrid.cells(cb[i],10).getValue();  
                   }
                               else
                               {
                                  var cl = mygrid.cells(cb[i],9).getValue();
                               } 
                               //alert(specIds);
                if(specIds.length >0)
                specIds = specIds+","+cl;
                
                else
                specIds = cl;
                
            }
            document.forms[0].orderedString.value=specIds;
        }
        else
        {
            isChecked = "false";
        }
        
        return isChecked;
    }
function showEvents()
{
   
     var autoDiv1 = document.getElementById("eventlist1");
     var autoDiv2 = document.getElementById("eventlist2");

     var chkbox=  document.getElementById("ch1");

     if(chkbox.checked== true)
    {
           
           autoDiv1.style.display  = 'block';
           autoDiv2.style.display  = 'none';
    }
    else
   {
       
        autoDiv2.style.display  = 'block';
        autoDiv1.style.display  = 'none';
         
   }
}
function showHideComponents()
{
   showEvents();
   showPriterTypeLocation();
}
function gotoAdvanceQuery()
{
    var action = "query.do?";
    document.forms[0].action = action;
    document.forms[0].submit();
}

function onSubmit(orderedString)
{

    if(document.forms[0].chkName[2].checked == true)
    {
        if(document.getElementById('specimenEventParameter').value == "Transfer")
        {
            dobulkTransferOperations(orderedString);
        }
        else
        {
            dobulkSpecimenEventsPage('StaticForms');
        }
    }
    else if(document.forms[0].chkName[3].checked == true)
    {
          dobulkSpecimenEventsPage('DynamicForms');
    }
    else if(document.forms[0].chkName[1].checked == true)
    {
        editMultipleSp();
    }
    else if(document.forms[0].chkName[0].checked == true)
    {
        addToOrderList(orderedString);
    }
    else if(document.forms[0].chkName[4].checked == true)
    {
        //create Shipment Request
        createShipmentRequest();
    }
    else if(document.forms[0].chkName[5].checked == true)
    {
        //create Shipment
        createShipment();
    }
    else if(document.forms[0].chkName[6].checked == true)
    {
        //distribute Order
        distributeOrder();
    }
    else if(document.forms[0].chkName[7].checked == true)
    {
        printSpecimensLabels(orderedString);
    }
}

function setCheckBoxState()
        {   
            var chkBox = document.getElementById('checkAll1');
            if(chkBox != null)
            {
            chkBox.checked = true;
            rowCount = mygrid.getRowsNum();
            for(i=1;i<=rowCount;i++)
                {
                    var cl = mygrid.cells(i,0);
                    if(cl.isCheckbox())
                    cl.setChecked(true);
                }
            }
        
        }
function onResponseSetRequester(response) 
    {
        document.getElementById('messageDiv').style.display="block";
        reloadGrid(document.getElementById('tagName'));
    }
function onDelete()
        {
            var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {
                var flag = confirm("Are you sure you want to delete the selected item(s)?");
                if(flag)
                {
                    request = newXMLHTTPReq();          
        var actionURL;
        var handlerFunction = getReadyStateHandler(request,onResponseSetRequester,true);    
        request.onreadystatechange = handlerFunction;   
        var specIds = document.forms[0].orderedString.value;
        var tagId= document.getElementById('tagName').value;
        //alert("specIds    "+specIds+"      tagId     "+tagId);
        actionURL = "specId="+specIds+"&tagId="+tagId;//+ cpDetailsForm.title.value;
        var url = "CatissueCommonAjaxAction.do?type=deleteSpecimen";
        <!-- Open connection to servlet -->
        request.open("POST",url,true);  
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");   
        request.send(actionURL);
                    /*var action = "AddDeleteCart.do?operation=delete";
                    document.forms[0].action = action;
                    document.forms[0].target = "_parent";
                    document.forms[0].submit();*/
                }
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }

function onExport()
        {
            var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {
            var specIds = document.forms[0].orderedString.value;
                var tagId= document.getElementById('tagName').value;
                var action = "ExportSpecimenList.do?operation=export&specId="+specIds+"&tagId="+tagId;
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }
        
function dobulkTransferOperations(orderedString)
        {
            //orderedString.value = mygrid.getCheckedRows(0);
            var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {       
                var action = "BulkCart.do?operation=bulkTransfers&requestFromPage=specimenListView";
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }
        
function dobulkSpecimenEventsPage(formType)
        {
            var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {
                //var action = "BulkCart.do?operation=bulkDisposals&requestFromPage=specimenListView";
                var checkedRows = mygrid.getCheckedRows(0);
                var selectedEvent=document.getElementById('specimenEventParameter').value;
                if(checkedRows.length > 0)
                {
                    var cb = checkedRows.split(",");
                    var specLabels="";
                    
                    for(i=0;i<cb.size();i++)
                    {
                        var cl = mygrid.cells(cb[i],10).getValue();
                        specLabels = specLabels + cl +",";
                    }
                    if(specLabels[specLabels.length - 1] == ",")
                    {
                        specLabels=specLabels.substring(0, specLabels.length - 1);  
                    }   
                    
                }
                if(formType == 'StaticForms') {
                  var action = "QuickEvents.do?specimenLabel="+specLabels+"&specimenEventParameter="+selectedEvent+"&fromPage=SpecimenList";
                } else if(formType == 'DynamicForms') {
                  var action = "specimenEventsBulkDataEntry.do?specimenLabels="+specLabels;
                }
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }

function addToOrderList(orderedString)
        {
            //orderedString.value = mygrid.getCheckedRows(0);
            var isChecked = updateHiddenFields();
            if(isChecked == "true")
            {
                var action = "BulkCart.do?operation=addToOrderList&requestFromPage=specimenListView";
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }
        function editMultipleSp()
        {
        
             var isChecked = updateHiddenFields();
            if(isChecked == "true")
            {
                var action = "BulkCart.do?operation=editMultipleSp&requestFromPage=specimenListView";
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
        }       

function createShipmentRequest()
{
        var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {
                var action = "BulkCart.do?operation=createShipmentRequest&requestFromPage=specimenListView";
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
}

function createShipment()
{
        var isChecked = updateHiddenFields();
            
            if(isChecked == "true")
            {
                var action = "BulkCart.do?operation=createShipment&requestFromPage=specimenListView";
                document.forms[0].action = action;
                document.forms[0].submit();
            }
            else
            {
                alert("Please select at least one checkbox");
            }
    
}

function distributeOrder()
{
    var isChecked = updateHiddenFields();
       
    if(isChecked == "true")
    {
        var action = "BulkCart.do?operation=requestToDistribute&requestFromPage=specimenListView";
        document.forms[0].action = action;
        document.forms[0].submit();
    }
    else
    {
        alert("Please select at least one checkbox");
    }
    
}
function printSpecimensLabels(orderedString)
{
//   orderedString.value = mygrid.getCheckedRows(0);
    var isChecked = updateHiddenFields();
       
    if(isChecked == "true")
    {
        var action = "BulkCart.do?operation=printLabels&requestFromPage=specimenListView";
        document.forms[0].action = action;
        document.forms[0].submit();
    }
    else
    {
        alert("Please select at least one checkbox");
    }
    
}
function checkAll(element)
{
    mygrid.setEditable(true);
    var state=element.checked;
    rowCount = mygrid.getRowsNum();
    var chkName="";
    //var hidendiv = document.getElementById('hiddenTab');
    //var createRow = document.getElementById('hiddenTab').insertRow(0);
    for(i=1;i<=rowCount;i++)
    {
    
            chkName = "value1(CHK_" + i + ")";
        //var chkName = "value1(CHK_" + i + ")";
        //alert(rowCount);
        var cl = mygrid.getCheckBox(0);
        //alert(c1);
        
        //var crtdCell = createRow.insertCell(ids);
        if(cl.isCheckbox())
        {
            cl.setChecked(state);
        //  crtdCell.innerHTML="<input type='hidden' name='"+chkName +"' id='"+ids+"' value='1'>";
            //alert(createRow.innerHTML);
        }
        else
        {
            //crtdCell.innerHTML="<input type='hidden' name='"+chkName +"' id='"+ids+"' value='0'>";
        }
    }
}

function loadSpecimenGrid()
{

    mygrid = new dhtmlXGridObject("specimenGrid");
    mygrid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
    //CHKBOX,SCG_NAME,Label,Barcode,Parent_Specimen_Id,Class,Type,Avl_Quantity,Lineage,Identifier
    mygrid.setInitWidthsP("3,22,15,,15,15,,15,15,,");
    mygrid.setEditable(true);
    mygrid.setSkin("dhx_skyblue");
    mygrid.enableAutoHeight(true,280);
    mygrid.setColSorting(",str,str,str,str,str,str,str,str,str,str");
    mygrid.setHeader(",Specimen Collection Group Name,Label (Barcode),,Parent Label,Class (Type),,Quantity,Lineage,,");
    
    mygrid.attachHeader("#master_checkbox,,#text_filter,,,#text_filter,,,#select_filter,,");
     mygrid.setColAlign("center,,,,,,,,,,")
    mygrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    
    //mygrid.enableTooltips(",true,true,true,true,true,true,true,true,true");
    mygrid.enableRowsHover(true,'grid_hover')
    mygrid.init();
    //mygrid.enableSmartRendering(true,15);
    mygrid.enableSmartRendering(true,100);
    //mygrid.addRow(1,",3/23,,,1.0,Tissue(Fixed Tissue),Not Specified,Not Specified,New,2.0,Collected,2141",1);
    //mygrid.addRow(2,",3_1/24,,3,1.0,Tissue(Fixed Tissue),Not Specified,Not Specified,New,2.0,Collected,2142",2);
    var tagVal = document.getElementById('tagName').value;
    //alert(tagVal);
    mygrid.loadXML("LoadGridServlet?reqParam="+tagVal); 
    mygrid.setColumnHidden(3,true);
    mygrid.setColumnHidden(6,true);
    mygrid.setColumnHidden(9,true);
    mygrid.setColumnHidden(10,true);
    
}
    </script>
</head>
<body onload="loadSpecimenGrid();">

<html:html>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
    <%=messageKey%>
</html:messages>
<html:form action="AddDeleteCart.do">


<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b">Specimen List</span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen List" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
   
   <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      <logic:notEmpty name="dropDownList">
     <tr>
        <td colspan="2" align="left" class="toptd">
        <%@ include file="/pages/content/common/ActionErrors.jsp" %>
        <div class="messagetextsuccess" id="messageDiv" style="display:none"> <bean:message key="specimen.delete"/></div>
        </td>
      </tr>
      
      <tr>
        <!--<td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;Specimen List &nbsp;</span></td>-->
      </tr>
     
      <tr>
        <td colspan="2">
        <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
           
            <tr >
            <td width="25%">
            
            </td>
    <td width="75%">
    
    </td>
        </tr>
        <!--  **************  Code for New Grid  *********************** -->    
        <tr>
            <td class="black_ar" colspan="2">
                <label><b>Select a list to view:<b></label>
            
                <select name="specimenLists" size="1" class="formFieldSizedNew" onChange="reloadGrid(this)" id="tagName">
                <%
                    List<NameValueBean> labelList =  (List)request.getAttribute("dropDownList");
                    if(labelList !=null && labelList.size() > 0)
        {
            String tagId =(String) request.getAttribute("tagId");
            for (NameValueBean object : labelList) 
            {
                if(tagId != null && object.getValue().toString().equals(tagId.toString())){
                %>
                    <option name="specimenLists" value="<%=object.getValue()%>" SELECTED><%=object.getName()%></option>
                <%
                } else {
                %>
                
                    <option name="specimenLists" value="<%=object.getValue()%>" ><%=object.getName()%></option>
                <% }                
            }
        }
                %>
                </select>
            <%
                        String  organizeTarget = "shareSpecimenList('Are you sure you want to delete?','Are you sure you want to delete?','SpecimenListTag','SpecimenListTagItem')";
            %>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="    Delete List     " onClick="deleteList()"/>
                <input type="button" value="    Share List     " onClick="<%=organizeTarget %>"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;
            </td>
            </tr>
            <tr>
            <td colspan="2">
            <table><tr>
                <td align="left" class="black_ar" >
                    
                    </td>
                    <td valign="middle" class="black_ar" align="left">
                
                </td>
                </tr>
                </table>
                </td>
            </tr>
            <tr>
            <td  valign="top" colspan="2">
              <div id="specimenGrid" width='100%' height='300px' style='background-color:white;'>
            </td>
           </tr>
           <tr>
            <td colspan="2">&nbsp;
            <div id="hiddenDiv" style="display:hidden"> 
                <table id="hiddenTab">
                </table>
            </div>
            </td>
            </tr>
           <tr>
            <td>
            &nbsp; &nbsp;<html:button styleClass="black_ar" property="deleteCart" onclick="onDelete()">
                <bean:message key="buttons.delete"/>
            </html:button>
        
            &nbsp;<html:button styleClass="black_ar" property="exportCart" onclick="onExport()">
                <bean:message key="buttons.export"/>
            </html:button>
            </td>
            <td>
                
            </td>
           </tr>
   <!--  **************  Code for New Grid  *********************** -->
        </table></td>
      </tr>
<tr>
        <td colspan="2">
         
      </tr>
    <tr>
        <td colspan="2" class="bottomtd"></td>
    </tr>   
    
    <tr>
        
        <td colspan="2" align="left" class="tr_bg_blue1">
            <label for="selectLabel">&nbsp;<span class="blue_ar_b"><bean:message key="mylist.label.selectLabel" /> </span>
            </label>
        </td>
    </tr>
    
    <tr>
        <td colspan="2" class="black_ar">
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
          <tr>
             <td class="black_ar" width="2%"><input type="radio" name="chkName"      value="OrderSpecimen" onclick="showHideComponents()" checked=true <%=disabledOrder%> ></td>
             <td class="black_ar" width="23%" ><bean:message key="mylist.label.orderBioSpecimen"/></td>
             <td class="black_ar" width="2%"><INPUT TYPE='RADIO' NAME='chkName' onclick="showHideComponents()" value="Specimenpage" <%=disabled%> ></td>
             <td class="black_ar" width="23%" ><bean:message key="mylist.label.multipleSpecimenPage"/>
               </td>
            <td class="black_ar" width="2%"><INPUT TYPE='RADIO' NAME='chkName'     onclick="showHideComponents()" id="ch1" value="Events" <%=disabled%> ></INPUT></td>
            
            <td class="black_ar" width="15%" ><bean:message key="mylist.label.specimenEvent"/> </td>
             
        
            
           <td class="black_ar" width="33%">
               <div id="eventlist1" style="display:none">
            
               <autocomplete:AutoCompleteTag  property="specimenEventParameter" 
                          optionsList = "${requestScope.eventArray}" styleClass="black_ar" size="27"
                          initialValue="Transfer"/>
                 </div> 
             
               <div id="eventlist2" style="display:block"><input type="text" styleClass="black_ar" size="25" id="specimenEventParameter1" name="specimenEventParameter" value="Transfer" readonly="true"/></div>
             </td>
          </tr>
        
          <tr>

            <td class="black_ar">
              <input type="radio" name="chkName" value="sleBulkDataEntry" id="sle" onclick="showHideComponents()">
            </td>
            <td class="black_ar">
              Specimen Event (Dynamic)
            </td>

            <td class="black_ar"><input type="radio" name="chkName" onclick="showHideComponents()" value="requestShipment" <%=disabledShipping%> ></td>
            <td class="black_ar" ><bean:message key="shipment.request"/></td>           
            <td class="black_ar"><input type="radio" name="chkName"  value="createShipment" onclick="showHideComponents()" <%=disabledShipping%> ></td>
            <td class="black_ar" ><bean:message key="shipment.create"/></td>

            <td class="black_ar">
              <input type="radio" name="chkName"  value="distributeOrder" onclick="showHideComponents()" <%=disabledShipping%> >
              <label class="black_ar"> Distribute </label
            </td>
          </tr>
          <tr>
            <td class="black_ar">
              <input type="radio" name="chkName" value="printLabels" id="printCheckbox" onclick="showHideComponents()">
            </td>
            <td class="black_ar">
              <bean:message key="mylist.label.printLabels" />
            </td>
          </tr>
        </table>          
      </tr>
<tr>
            <td class="bottomtd"></td></tr>
<tr>
       <td colspan="2" class="buttonbg"> <html:button styleClass="blue_ar_b" property="proceed" onclick="onSubmit(this.form.orderedString)" disabled="<%=disabledButton%>" >
                <bean:message key="buttons.submit"/>    
            </html:button></td>
      </tr>
     </logic:notEmpty>
     <logic:empty name="dropDownList">

    <tr>
        <td class="bottomtd"></td>
    </tr>
     <tr>
        <td class="messagetextsuccess">&nbsp;
            There are no saved specimen lists. Please use advance query results page or any specimen collection page to create new specimen list.
        </td>
    </tr>
    <tr>
        <td class="bottomtd"></td>
    </tr>
    <tr>
        <td class="buttonbg">
            <html:button styleClass="blue_ar_b" property="" onclick="gotoAdvanceQuery()">
                <bean:message key="buttons.advanceQuery"/>  
            </html:button>
            </td>
    </tr>   
    
</logic:empty>
   </table>
  
   </td></tr>
    
    
 
    <tr>
        <td>
            <input type="hidden" name="orderedString">
        </td>
    </tr>
  </table> 
  <%
    String specId = "12";
    String  assignTargetCall = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+specId+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+specId+"')";
 %>
<%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
  
<script language="JavaScript" type="text/javascript">
function shareSpecimenList(Msg,DMsg,entityTag,entityTagItem)
{
    ajaxTreeGridInitCall(Msg,DMsg,entityTag,entityTagItem);
    var tagChkBoxAr = document.getElementsByName("tagCheckbox");
    document.getElementById("shareToCheckbox").checked = true;
}
    //showHideComponents();
</script>
    </body>
    </html:form>
</html:html>
