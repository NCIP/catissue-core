
<%-- Common Functions used in NewSpecimen and CreateFrom Existing --%>
<%
	List specimenClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);

	List specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);

	HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);

%>


<SCRIPT LANGUAGE="JavaScript">

<%

	Iterator specimenTypeIterator = specimenTypeMap.keySet().iterator();
	int classCount=0;
	for(classCount=1;classCount<specimenClassList.size();classCount++  )
	{
		String keyObj = (String)((NameValueBean)specimenClassList.get(classCount)).getName() ;
		List subList = (List)specimenTypeMap.get(keyObj);
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

		function typeChange(arrayName)
		{ 
			var specimenTypeCombo = "type";
			ele = document.getElementById(specimenTypeCombo);
			//To Clear the Combo Box
			ele.options.length = 0;
			
			//ele.options[0] = new Option('-- Select --','-1');
			var j=0;
			//Populating the corresponding Combo Box
			for(i=0;i<arrayName.length;i++)
			{
					ele.options[j++] = new Option(arrayName[i],arrayName[i]);
			}
		}


	var subTypeData1 = "<%=Constants.SLIDE%>";
	var subTypeData2 = "<%=Constants.PARAFFIN_BLOCK%>";
	var subTypeData3 = "<%=Constants.FROZEN_BLOCK%>";
	

// units array
	var ugul = new Array(5);
	ugul[0]=" ";
	ugul[1]="<%=Constants.UNIT_ML%>";
	ugul[2]="<%=Constants.UNIT_GM%>";
	ugul[3]="<%=Constants.UNIT_CC%>";
	ugul[4]="<%=Constants.UNIT_MG%>";
	ugul[5]="<%=Constants.UNIT_CN%>";

	
// Changes unit on subtype list changed
	function onSubTypeChangeUnit(typeList,element,unitspan)
	{
		var classList = document.getElementById(typeList);
		var className = classList.options[classList.selectedIndex].text;
		var selectedOption = element.options[element.selectedIndex].text;
	
		if(className == "Tissue" && (selectedOption == subTypeData1 || selectedOption == subTypeData2 || selectedOption == subTypeData3))
		{
			document.getElementById(unitspan).innerHTML = "<%=Constants.UNIT_CN%>";
			// added for Available quantity
			var unit1= document.getElementById("unitSpan1");
			if(unit1!=null)
			{
				unit1.innerHTML = "<%=Constants.UNIT_CN%>";
			}
			document.forms[0].unit.value = "<%=Constants.UNIT_CN%>";
		}	
		else 
		{
			if(className == "Tissue")
			{
				document.getElementById(unitspan).innerHTML = "<%=Constants.UNIT_GM%>";
				// added for Available quantity
				var unit1= document.getElementById("unitSpan1");
				if(unit1!=null)
				{
					unit1.innerHTML = "<%=Constants.UNIT_GM%>";
				}
				document.forms[0].unit.value = "<%=Constants.UNIT_GM%>";
			}	
		}
	}

// changes unit when specimen class selected
		function onTypeChange(element)
		{
			var unit = document.getElementById("unitSpan");
			var unit1 = document.getElementById("unitSpan1");
			var unitSpecimen = "";
			document.forms[0].concentration.disabled = true;
			var subType = document.getElementById("type");
			subType.disabled = false;
			
			if(element.value == "Tissue")
			{
				unitSpecimen = "<%=Constants.UNIT_GM%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_GM%>";
				typeChange(TissueArray);
			}
			else if(element.value == "Fluid")
			{
				unitSpecimen = "<%=Constants.UNIT_ML%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_ML%>";
				typeChange(FluidArray);
			}
			else if(element.value == "Cell")
			{
				unitSpecimen = "<%=Constants.UNIT_CC%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_CC%>";
				typeChange(CellArray);
			}
			else if(element.value == "Molecular")
			{
				unitSpecimen = "<%=Constants.UNIT_MG%>";
				document.forms[0].unit.value = "<%=Constants.UNIT_MG%>";
				typeChange(MolecularArray);
			}
			unit.innerHTML = unitSpecimen;
			if(unit1!=null)
			{
				unit1.innerHTML = unitSpecimen;
			}				
		}

// biohazard selection
		function onBiohazardTypeSelected(element)
		{ 
			var i = (element.name).indexOf("_");
			var indColon = (element.name).indexOf(":");
			var comboNo = (element.name).substring(indColon+1,i);
			
			var comboToRefresh = "bhId" + comboNo;
			
			ele = document.getElementById(comboToRefresh);
			//To Clear the Combo Box
			ele.options.length = 0;
			
			ele.options[0] = new Option('-- Select --','-1');
			var j=1;
			//Populating the corresponding Combo Box
			for(i=0;i<idArray.length;i++)
			{
				if(typeArray[i] == element.value)
				{
					ele.options[j++] = new Option(nameArray[i],idArray[i]);
				}
			}
		}
	


</script>