<script language="JavaScript" type="text/javascript" src="jss/shippingtracking/addMoreScript.js"></script>
<script>
		
	function addMoreSpecimenDetails()
	{
		var specimenCounter=parseInt(document.getElementById("specimenCounter").value);
		var tbodyObject=document.getElementById("specimenRowsContainer");
		var rows=tbodyObject.rows;
		var previousRowCount=rows.length;
		var newRow=tbodyObject.insertRow(rows.length);
		var name="",id="";
		var thisRowNum=previousRowCount+1;
		var cellInnerHTML="";
		
		var checkBoxCell=newRow.insertCell(0);
		name="specimenDetails(specimenCheckbox_"+thisRowNum+")";
		id="specimenCheckbox_"+thisRowNum;
		cellInnerHTML="<input type='checkbox' name='"+name+"' id='"+id+"'>";
		checkBoxCell.innerHTML=cellInnerHTML;
		
		var operation=document.getElementById("operation").value;
		
		var checkBoxCell=newRow.insertCell(1);
		name="specimenDetails(specimenLabel_"+thisRowNum+")";
		labelId="specimenLabel_"+thisRowNum;
		var disabled=document.getElementById("SpecimenLabel").checked;
		if(disabled || operation=='add')
		{
			cellInnerHTML="<input type='text' name='"+name+"' id='"+labelId+"' size='30' class='black_ar' onkeydown=\"return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')\">";
		}
		else
		{
			cellInnerHTML="<input type='text' name='"+name+"' id='"+labelId+"' size='30' class='black_ar' disabled='"+(!disabled)+"' onkeydown=\"return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')\">";
		}
		checkBoxCell.innerHTML=cellInnerHTML;	
		
		
		if(operation=='edit')
		{
			var checkBoxCell=newRow.insertCell(2);
			name="specimenDetails(specimenBarcode_"+thisRowNum+")";
			id="specimenBarcode_"+thisRowNum;
			disabled=document.getElementById("SpecimenBarcode").checked;
			if(disabled || operation=='add')
			{
				cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' onkeydown=\"return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')\">";
			}
			else
			{
				cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' disabled='"+(!disabled)+"' onkeydown=\"return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')\">";
			}
			checkBoxCell.innerHTML=cellInnerHTML;	
		}
		
		document.getElementById("specimenCounter").value=thisRowNum;
		document.getElementById(labelId).focus=true;
	}
	
	function changeSpecimenLabelChoice(clickedObj)
	{
		var operation=document.getElementById("operation").value;
		if(operation=='edit')
		{
			if(clickedObj.checked==true)
			{
				var numOfControls=parseInt(document.getElementById("specimenCounter").value);
				//Enable the corresponding choice textboxes
				if(clickedObj.value=='SpecimenLabel')
				{
					enableControls(true,"specimenLabel_",numOfControls);
					enableControls(false,"specimenBarcode_",numOfControls);
				}
				else if(clickedObj.value=='SpecimenBarcode')
				{
					enableControls(false,"specimenLabel_",numOfControls);
					enableControls(true,"specimenBarcode_",numOfControls);
				}
			}
			else
			{
				//Disable the corresponding choice textboxes			
			}
		}
	}
	
	function enableControls(isDisabled,idPrefix,controlCount)
	{
		if(controlCount>0)
		{
			for(controlCounter=1;controlCounter<=controlCount;controlCounter++)
			{
				document.getElementById(idPrefix+controlCounter).disabled=(!isDisabled);
			}
		}
	}
</script>

<html:hidden property="specimenCounter" styleId="specimenCounter"/>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<tr class="addl_table_head">
    	<td height="25" colspan="3" align="left" valign="middle" class="tr_bg_border">
    		<span class="black_ar_b">Specimens</span>
    	</td>
	</tr>
	<tr class="addl_table_head">
    	<td height="25" colspan="3" align="left" valign="middle" class="tr_bg_border">
    		<logic:notEqual name="operation" value='view'>
				<bean:message key="createShipment.specimen.labelchoicequestion" bundle="msg.shippingtracking"/> &nbsp;&nbsp;
				<html:radio property="specimenLabelChoice" styleId="SpecimenLabel" value="SpecimenLabel" onclick="changeSpecimenLabelChoice(this)"/> <bean:message key="createShipment.specimen.label" bundle="msg.shippingtracking"/>
				<html:radio property="specimenLabelChoice" styleId="SpecimenBarcode" value="SpecimenBarcode" onclick="changeSpecimenLabelChoice(this)"/> <bean:message key="createShipment.name" bundle="msg.shippingtracking"/>
				<script>
						var spLabelRadio=document.getElementById("SpecimenLabel");
						var spBarcodeRadio=document.getElementById("SpecimenBarcode");
						if(spLabelRadio.checked==false && spBarcodeRadio.checked==false)
						{
								spLabelRadio.checked=true;
						}
				</script>
			</logic:notEqual>	
			<logic:equal name="operation" value='view'>
				<html:hidden property="specimenLabelChoice" styleId="specimenLabelChoice"/>
			</logic:equal>
    	</td>
	</tr>
    <tr class="addl_table_head">
		<logic:notEqual name="operation" value='view'>
	    	<td align="left" valign="middle" class="black_ar"><b class="black_ar_s"><bean:message key="shipment.contentsSelect" bundle="msg.shippingtracking"/>&nbsp;&nbsp; </b></td>
		</logic:notEqual>
		<td height="25" align="left" valign="middle" class="black_ar"><b class="black_ar_s"> <bean:message key="shipment.contentsLabelOrBarcode" bundle="msg.shippingtracking"/> </b></td>
		<td width="25%" align="left" valign="middle" class="black_ar">&nbsp;</td>
    </tr>
    <!-- Iterate for each specimen -->
    
		<jsp:useBean id="noOfSpecimens" type="java.lang.Integer"/>
			<c:if test="${noOfSpecimens==0}">
					<c:set var="noOfSpecimens" value="1"/>
			</c:if>

			<script>
					var specimenCount=parseInt(document.getElementById("specimenCounter").value);
					if(specimenCount==0)
							specimenCount=1;

					document.getElementById("specimenCounter").value=specimenCount;
			</script>
	</table>
	<table cellpadding="3">
	<tbody id="specimenRowsContainer">
    <c:forEach var="specimenNumber" begin="1" end="${noOfSpecimens}">
    	<tr>
    		<logic:notEqual name="operation" value='view'>
			    <td align="left" valign="top" class="black_ar">
			    		<c:set var="checkBoxName">specimenDetails(specimenCheckbox_<c:out value="${specimenNumber}"/>)</c:set>
						<jsp:useBean id="checkBoxName" type="java.lang.String"/>
						
						<c:set var="checkBoxId">specimenCheckbox_<c:out value="${specimenNumber}"/></c:set>
						<jsp:useBean id="checkBoxId" type="java.lang.String"/>
						
			    		<html:checkbox  styleId="<%=checkBoxId%>" property="<%=checkBoxName%>"/>
			   	</td>
			</logic:notEqual> 
		    <td height="30" align="left" valign="top">
		    	<label>	
		    		<c:set var="labelName">specimenDetails(specimenLabel_<c:out value="${specimenNumber}"/>)</c:set>
					<jsp:useBean id="labelName" type="java.lang.String"/>
					
					<c:set var="labelId">specimenLabel_<c:out value="${specimenNumber}"/></c:set>
					<jsp:useBean id="labelId" type="java.lang.String"/>
					
		    		<html:text styleClass="black_ar" size="30" styleId="<%=labelId%>" property="<%=labelName%>" onkeydown="return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')"/>	    	
			    </label>
			</td>
		    <td align="left" valign="top">
		    	<c:if test="${operation=='edit' or operation=='view'}">
		    		<label>	
			    		<c:set var="specimenBarcodeName">specimenDetails(specimenBarcode_<c:out value="${specimenNumber}"/>)</c:set>
						<jsp:useBean id="specimenBarcodeName" type="java.lang.String"/>
						
						<c:set var="specimenBarcodeId">specimenBarcode_<c:out value="${specimenNumber}"/></c:set>
						<jsp:useBean id="specimenBarcodeId" type="java.lang.String"/>
						
			    		<html:text styleClass="black_ar" size="30" styleId="<%=specimenBarcodeId%>" property="<%=specimenBarcodeName%>" onkeydown="return addMoreRows(this,event,'specimenRowsContainer','specimenCounter')"/>	    	
				    </label>
		    	</c:if>
				<c:if test="${operation!='edit'}">	
					&nbsp;
				</c:if>
		    </td>
	    </tr>
    </c:forEach>
    </tbody>
    </table>
	<!-- End of iterate for each specimen -->
	<logic:notEqual name="operation" value='view'>
		<table cellpadding="3">
		    <tr>
			    <td height="40" colspan="4" >
					<html:link href="javascript:addMoreSpecimenDetails()" styleId="addMoreSpecimen">
						<img src="images/shippingtracking/ic_add.gif" alt="Add More"  align="absmiddle">
					</html:link>
			    	<img src="images/shippingtracking/sep.gif" width="1" height="19" hspace="5" align="absmiddle">
			    	<html:link href="javascript:deleteCheckedRows('specimenRowsContainer','#',document.forms[0].specimenCounter,'specimenCheckbox_',false,'',false);" styleId="deleteSpecimen">
				    	<img src="images/shippingtracking/ic_delete.gif" alt="Delete"  align="absmiddle">
				    </html:link>
			    </td>
		    </tr>
		</table>
	</logic:notEqual>