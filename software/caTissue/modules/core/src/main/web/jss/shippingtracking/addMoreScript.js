function  deleteCheckedRows(subdivtag,action,countElement,checkName,isOuterTable,pageOf,isFirstCellSerialCount)
{
	
	var r = new Array(); 
	
	/** element of tbody    **/
	var element = document.getElementById(subdivtag);
	
	
	/** number of rows present    **/
	var counts = countElement.value;
	if(counts == undefined){
	
		var cnt = document.getElementById(countElement);
		
		/** number of rows present(counts) when countElement is again element    **/
		counts = cnt.value;
	}

	var oldRowsCount=counts;
	var rowIdsDeleted='';

	/** number if rows deleted**/
	var delCounts = 0;

	var remainingRowIds="";
	
	/** checking whether checkbox is checked or not **/
	var status = false;
	
	for(i=1;i <= counts;i++)
	{
		
		/** creating checkbox name**/
		itemCheck = checkName+i;
		var chk = document.getElementById(itemCheck);
		
		if(!(chk==undefined)&&(counts-delCounts)>1)
		{
			if(chk.checked==true){
				var pNode = null;
				var k = 0;
			
				/** condition for checking whether outerTable's delete is clicked or not **/
				if(isOuterTable) {

					tableId = "table_" + i;
					var table = document.getElementById(tableId);
					// md 21 mar start
					var currentRow = table.parentNode.parentNode;
					k = currentRow.rowIndex;
					pNode = element.parentNode;
					pNode.deleteRow(k);
					rowIdsDeleted=rowIdsDeleted+","+i;
	//				delCounts++;
					// md 21 mar end
					
					/** removing table from tbody tag(div)   **/
					// 21 mar commented by md: element.removeChild(table);
	
				}	
				else {

					/** getting table ref from tbody    **/
					pNode = element.parentNode;
				
					/** curent row of table ref **/
					var currentRow = chk.parentNode.parentNode;
					k = currentRow.rowIndex;
					rowIdsDeleted=rowIdsDeleted+","+i;
				
					/** deleting row from table **/
					pNode.deleteRow(k);
				}
				delCounts++;
			//	status = true;
			}
			else
			{
				remainingRowIds=remainingRowIds+","+i;
			}
			
		}
	}
	
	
	if(countElement.value == undefined){
		/** setting number of rows present in form   **/
		cnt.value = counts - delCounts;
	}
	else
		/** setting number of rows present in form   **/
		countElement.value = (countElement.value - delCounts);
	
	// Patch-Id: Improve_Space_Usability_On_Specimen_Page_1 
	// Description: if page of pageOfNewSpecimen then no need to call action
	if(pageOf=='pageOfNewSpecimen')
	{
		status=false;
	}
	if(status){
		/** set action when checkbox is clicked **/
		document.forms[0].action = action;
		document.forms[0].submit();
	}
		
	reorderRemainingRows(subdivtag,oldRowsCount,countElement.value,checkName,remainingRowIds,isFirstCellSerialCount);
}

function reorderRemainingRows(subdivtag,oldRowsCount,noOfRows,checkName,remainingRowIds,isFirstCellSerialCount)
{
	var tableContainer=document.getElementById(subdivtag);
	var rowsArArray=tableContainer.rows;
	var counter=0;
	var newName="";
	var newId="";
	for(rowCounter=0;rowCounter<noOfRows;rowCounter++)
	{
			var row=tableContainer.rows[rowCounter];
			if(isFirstCellSerialCount==true)
			{
				row.cells[0].innerHTML=rowCounter+1;
			}
			var controlsArray=row.getElementsByTagName("input");
			var splittedControlName=(controlsArray[0].name).split("_");
			var splittedControlId=(controlsArray[0].id).split("_");

			var controlNo=(splittedControlName[splittedControlName.length-1]).split(")");
			var temp=""+(rowCounter+1);
			if(controlNo[0]!=temp)
			{
				for(controlIndex=0;controlIndex<controlsArray.length;controlIndex++)
				{
					splittedControlName=(controlsArray[controlIndex].name).split("_");
					splittedControlId=(controlsArray[controlIndex].id).split("_");

					splittedControlName[splittedControlName.length-1]=(counter+1);
					splittedControlId[splittedControlId.length-1]=(counter+1);

					for(nameCounter=0;nameCounter<splittedControlName.length;nameCounter++)
					{
						newName=newName+"_"+splittedControlName[nameCounter];
					}
					if((splittedControlName[0]+"_")!=checkName)
					{
						newName=newName+")";
					}
					newName=newName.substr(1);

					for(idCounter=0;idCounter<splittedControlId.length;idCounter++)
					{
						newId=newId+"_"+splittedControlId[idCounter];
					}
					newId=newId.substr(1);

					controlsArray[controlIndex].id=newId;
					controlsArray[controlIndex].name=newName;
					newName="";
					newId="";
				}
			}
		counter++;
	}
}

function addRow(divName)
{
		if(divName=='specimenRowsContainer')
		{
			addMoreSpecimenDetails();
		}
		if(divName=='containerRowsContainer')
		{
			addMoreContainerDetails();
		}
}

function addMoreRows(currentElement,eventGenerated,divName,counterName)
{
	var keyCode;
	if(eventGenerated!=undefined)
	{
		if(window.event) // IE
		{
			keyCode = eventGenerated.keyCode;
		}
		else if(eventGenerated.which) // Netscape/Firefox/Opera
					keyCode = eventGenerated.which;
	}
	//ASCII code for tab is 9
	if(eventGenerated!=undefined && keyCode==9)
	{
		if(currentElement!=undefined)
		{
			var counterValue=parseInt(document.getElementById(counterName).value);
			var controlName=currentElement.id;
			var splittedcontrolaName=controlName.split("_");
			var controlNumber=parseInt((splittedcontrolaName)[splittedcontrolaName.length-1]);
			if(controlNumber==counterValue)
			{
				addRow(divName);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	else
	{	
		return true;
	}
}