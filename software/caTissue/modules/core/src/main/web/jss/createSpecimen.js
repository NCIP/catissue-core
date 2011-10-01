		function onRadioButtonClick(element)
		{
     		if(element.value == 1)
			{
				document.forms[0].parentSpecimenLabel.disabled = false;
				document.forms[0].parentSpecimenBarcode.disabled = true;
			}
			else
			{
				document.forms[0].parentSpecimenBarcode.disabled = false;
				document.forms[0].parentSpecimenLabel.disabled = true;
			}
		}		 
		function resetVirtualLocated()
		{
		    try
			{
			var radioArray = document.getElementsByName("stContSelection");	
			radioArray[0].checked= true;
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;

			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
			}
			catch(e)
			{
			}
		}

		function closeWindow()
		{
		  window.close();
		}
		
		function onCheckboxButtonClick(chkBox)
		{
			if(chkBox.checked)
			{				
				document.forms[0].moreButton.disabled=true;
				document.forms[0].noOfAliquots.disabled=false;
				document.forms[0].quantityPerAliquot.disabled=false;
			}
			else
			{				
				document.forms[0].moreButton.disabled=false;
				document.forms[0].noOfAliquots.disabled=true;
				document.forms[0].quantityPerAliquot.disabled=true;
			}
		}	
		
		function setSubmitted(forwardTo,printaction,nextforwardTo)
		{
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{
			  setSubmittedForPrint(forwardTo,printaction,nextforwardTo);
			}
			else
			{
			  setSubmittedFor(forwardTo,nextforwardTo);
			}
		}
		function setVirtuallyLocated(element,multipleSpecimen)
		{
			var containerName = document.getElementById("customListBox_1_0");
			var pos1 = document.getElementById("customListBox_1_1");
			var pos2 = document.getElementById("customListBox_1_2");
			if(element.checked)
			{
				containerName.disabled = true;
				pos1.disabled = true;
				pos2.disabled = true;
				document.forms[0].mapButton.disabled = true;
			}
			else
			{
				onClassOrLabelOrBarcodeChange(multipleSpecimen,element);
			}
		} 
		function onAddNewButtonClicked()
		{
		    var action = "NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&rowSelected=-1&addNew=true&operation=add";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function classChangeForMultipleSpecimen()
		{
		 	var action ='NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&specimenAttributeKey=' + document.getElementById("specimenAttributeKey").value + '&derivedSpecimenCollectionGroup=' + document.getElementById("derivedSpecimenCollectionGroup").value + '&retainForm=true';
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	
		function insExIdRow(subdivtag)
	{
		var val = parseInt(document.forms[0].exIdCounter.value);
		val = val + 1;
		document.forms[0].exIdCounter.value = val;
		
		var r = new Array(); 
		r = document.getElementById(subdivtag).rows;
		var q = r.length;
		var rowno = q + 1;
		var x=document.getElementById(subdivtag).insertRow(0);
	
		//var spreqno=x.insertCell(0);
		//spreqno.className="black_ar";
		//sname=(q+1);
		//var identifier = "externalIdentifierValue(ExternalIdentifier:" + (q+1) +"_id)";
		//var hiddenTag = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
		//spreqno.innerHTML="" + rowno + "." + hiddenTag;
	
		// First Cell
		var checkb=x.insertCell(0);
		checkb.className="black_ar";
		checkb.colSpan=1;
		sname="";
		var name = "chk_ex_"+ rowno;
		sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteExId,document.forms[0].exIdCounter,'chk_ex_')\">";
		checkb.innerHTML=""+sname;

		//Second Cell
		var spreqtype=x.insertCell(1);
		spreqtype.className="black_ar";
		spreqtype.colSpan=1;
		sname="";
		
		var name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_name)";
		sname="<input type='text' name='" + name + "'  maxlength='50' size='25' class='black_ar' id='" + name + "'>";      
	
		spreqtype.innerHTML="" + sname;
	
		//Third Cell
		var spreqsubtype=x.insertCell(2);
		spreqsubtype.className="blue_ar";
		spreqsubtype.colSpan=1;
		sname="";
		
		name = "externalIdentifierValue(ExternalIdentifier:" + rowno +"_value)";
		sname= "";
			
		sname="<input type='text' name='" + name + "' maxlength='50' size='15' class='black_ar' id='" + name + "'>"   
		
		spreqsubtype.innerHTML="" + sname;
	}
			