<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<script>
	function showPriterTypeLocation()
	{
		if(document.getElementById('printCheckbox').checked == true)
		{
			displayPrinterTypeLocation();
			if(document.getElementById('addToCart')!=null)
			{
			  document.getElementById('addToCart').disabled = true;
			}
		}
		else
		{
			document.getElementById('printerSelection').style.display="none";
			if(document.getElementById('addToCart')!=null)
			{
			  document.getElementById('addToCart').disabled = false;
			}
			setSubmittedForPrint("","","");
		}
	}
	function displayPrinterTypeLocation()
	{
		     <%
				 int listsize = Variables.printerLocationList.size(); 
			   %>
			if("<%=listsize%>" == "1" || "<%=listsize%>" == "0" )
			{
				document.getElementById('printerSelection').style.display="none";
			}
			else
			{
			   document.getElementById('printerSelection').style.display="";
			}			
	}

</script>
<div  id="printerSelection" style="">
									<table border="0">
						 				<tr>
						    				<td align="left" class="black_ar"  width="8%">
									<label for="printerType">
										<bean:message key="print.printerTypeLabel"/>
									</label>
								</td>
												<td align="left" class="black_new"> 
									<autocomplete:AutoCompleteTag property="printerType"
										optionsList="<%=Variables.printerTypeList%>"
										initialValue="<%=form.getPrinterType()%>"
										styleClass="black_ar"
										 size="23"
										/>
					        	    </td>
						   					<!--</td>-->
											<td>&nbsp;&nbsp; </td>
							   				 <td align="left" class="black_ar"  width="9%">
									           <label for="printerLocation">
										        <bean:message key="print.printerLocationLabel"/>
									          </label>
								           </td>
												<td align="left" class="black_new"> 
									<autocomplete:AutoCompleteTag property="printerLocation"
										optionsList="<%=Variables.printerLocationList%>"
										initialValue="<%=form.getPrinterLocation()%>"
										styleClass="black_ar"
										 size="23"
										/>
					        	    </td>
						   				</tr>
									</table>
								</div>
