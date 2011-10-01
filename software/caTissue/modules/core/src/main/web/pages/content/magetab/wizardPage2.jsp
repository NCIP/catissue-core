<%@page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportBizLogic"%>
<%@page import="java.util.ListIterator"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="gov.nih.nci.logging.api.util.StringUtils" %>
<%
MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)session.getAttribute(
		MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);

MagetabExportWizardBean.SseTable sseTable = wizardBean.getSseTable();
List<List<Specimen>> chains = sseTable.getSpecimenChains();
Set<Integer> chainSelections = sseTable.getChainSelections();
int maxChainLength = sseTable.getMaxChainLength();
boolean isCaArrayCompatible=wizardBean.getCaArrayEnabled()!=null?true:false;
int counter=0;
%>

<script type="text/javascript">

function checkIfAllNA(columnPosition){
    var allNA=false;
    var ct1 = 0
    var ct2 = 0;
    $j('#sdrfData tr td:nth-child('+columnPosition+')').each(function(){
         var content=$j(this).html();  
         ct1++;
         if(content.trim()=='&nbsp;N/A'){           
            ct2++;
         }
    }); 
    if (ct1 == ct2) {
    	allNA =true;
    }
    return allNA;
}
function removeColumn(columnPosition){
	//alert (columnPosition);
     $j('#sdrfData tr th:nth-child('+columnPosition+')').each(function(){
         $j(this).remove();
    });
    $j('#sdrfData tr td:nth-child('+columnPosition+')').each(function(){
         $j(this).remove();
    });   
}

function removeDuplicates(inputArray) {
    var i;
    var len = inputArray.length;
    var outputArray = [];
    var temp = {};

    for (i = 0; i < len; i++) {
        temp[inputArray[i]] = 0;
    }
    for (i in temp) {
        outputArray.push(parseInt(i));
    }
    return outputArray;
}
function displayColumns() {
	var allCols = "";
    $j('#sdrfData tr th').each(function(){
    	allCols = allCols + "XX" + $j(this).html().substring(0,8);
    });
    $j('#displayColumns').val(allCols);
}

$j(function(){
    var caArrayCompatible="true";
    
	if($j('input[type="checkbox"]').length>0){
		if($j('input[type="checkbox"]:checked').length==0)		
			$j('input[type="button"][value="Next"]').attr('disabled', 'disabled');	
            }
            else{
                //no chains so disable the next button.
                $j('input[type="button"][value="Next"]').attr('disabled', 'disabled');
            }
         if(caArrayCompatible&&$j('#sdrfData tr').length>0){
             //alert('compatible');
             //now search the table if it has any sample column which is N/A.
             var listOfColumnsIn= new Array();
             var listOfColumns;
             $j('#sdrfData tr td').each(function(){
               var content=$j(this).html();
               var index=0;
               if(content.trim()=='&nbsp;N/A'){
                    var column = $j(this).parent().children().index(this); 
                    listOfColumnsIn.push(column);
               }
               listOfColumns = removeDuplicates(listOfColumnsIn);

             });
             for(var temp in listOfColumns)
             {                 
                 $j('#sdrfData tr th:nth-child('+(listOfColumns[temp]+1)+') input[type="radio"]').attr('disabled',true);                      
             }
             //from those list check if a column is all NA then remove it.
             var numOfColumnsRemoved=0;
             
           
             
             for(var temp in listOfColumns)
             { 
            	 var columnNumber = listOfColumns[temp];
            	 columnNumber = columnNumber+1-numOfColumnsRemoved;
            	// alert (columnNumber);
            	 if(checkIfAllNA(columnNumber))
                 {                    
            		 //alert (columnNumber);
                     removeColumn(columnNumber);
                     numOfColumnsRemoved++;
                 }
             }
             
             displayColumns();


             //check if there is only one sample if so remove the checkbox externally.
             //alert($j('#sdrfData tr > td').length+' '+($j('#sdrfData tr').length-1)+' '+numOfColumnsRemoved);
	//              if($j('#sdrfData tr > td').length/($j('#sdrfData tr').length-1)==4)
	//              {
	
	//                  $j('#sdrfData tr th input[type="checkBox"]').each(function(){
	//                      $j(this).hide();
	//                  });
	//              }
         }
             
	$j('#sdrfData tr td input[type="checkbox"]').click(function(){
		if(!$j(this).is(':checked')){
			if($j('input[type="checkbox"]:checked').length==0)				
				$j('input[type="button"][value="Next"]').attr('disabled', 'disabled');		
		}
		else
			$j('input[type="button"][value="Next"]').removeAttr('disabled');			
		});	
});
</script>
<input type="hidden" name="isCaArrayCompatible" value="<%= isCaArrayCompatible %>"/>

<input type="hidden" name="displayColumns" id="displayColumns" value=""/>
<table height="100%" width="100%">
	<tr>
		<td align="left" class="tr_bg_blue1">
			<span class="blue_ar_b">
				&nbsp;Investigation Branches
			</span>
		</td>
	</tr>
	<tr>
		<td class="black_ar">
			Select the rows and columns to export
		</td>
	</tr>
	<tr height="100%">
		<td>
			<div style="height: 100%; width: 100%; overflow: auto;">
<table width="100%" id="sdrfData">
<% 
if(chainSelections.size()>0)
{
%>

<tr class="tableheading">
	<th class="black_ar_b">&nbsp;</th>
	<th class="black_ar_b">Source    <input type="checkbox" id="SoCheck" checked="true" disabled="true"/></th>
<%
for (int i = 0; i < maxChainLength - 2; i++) {
    
%>

<th class="black_ar_b">Sample      <input type="checkbox" id="sampleColumnNumber<%=i %>" checked="true" /></th>

<%
       
        
}
if (maxChainLength > 1) {
%>
	<th class="black_ar_b">Extract    <input type="checkbox" id="ExCheck" checked="true" disabled="true"/></th>
<%
}
%>
</tr>
<%

for (int i = 0; i < chains.size(); i++) {
	List<Specimen> chain = chains.get(i);
	if(chainSelections.contains(i))
	{            
            if(isCaArrayCompatible&& chain.size()<3){
                continue;
            }
            counter++;
%>
<tr>
	<td class="black_new">
		<input type="checkbox" name="chains" value="<%="chk_chain_" + i %>" <%=chainSelections.contains(i) ? " checked=\"checked\"" : "" %> />
	</td>
<%
	ListIterator<Specimen> iter = chain.listIterator();        
	Specimen specimen = iter.next();
%>
<td class="black_new"><%=MagetabExportBizLogic.getNodeName(specimen) %></td>
<%
	if (iter.hasNext()) {
		specimen = iter.next();
	} else {
		specimen = null;
	}	
	while (iter.hasNext()) {	
%>
	<td class="black_new"><%=MagetabExportBizLogic.getNodeName(specimen) %></td>
<%
		specimen = iter.next();
	}	
	for (int cnt = 0; cnt < maxChainLength - chain.size(); cnt++) {
%>
	<td class="black_new">&nbsp;N/A</td>
<%
	}	
	if (specimen != null) {
%>
	<td class="black_new"><%=MagetabExportBizLogic.getNodeName(specimen) %></td>
<%
	}
%>
</tr>
<%
}

}
}
else
{
%>
<tr>
	<td width="20px" valign="top">
		<img width="16" vspace="0" hspace="0" height="18" valign="top" alt="error messages" src="images/uIEnhancementImages/alert-icon.gif"></td>
	<td class="messagetexterror">
		<strong>Error:</strong>
	</td>
</tr>
<tr>
	<td class="messagetexterror" colspan="2">Your selection has no chains.</td>
</tr>
<%
counter=1;
}
if(counter==0)
{
%>
<tr>
	<td width="20px" valign="top">
		<img width="16" vspace="0" hspace="0" height="18" valign="top" alt="error messages" src="images/uIEnhancementImages/alert-icon.gif"></td>
	<td class="messagetexterror">
		<strong>Error:</strong>
	</td>
</tr>
<tr>
	<td class="messagetexterror" colspan="2">Your selection has no chains.</td>
</tr>
<%
}
%>
</table>
			</div>
		</td>
	</tr>
</table>
