
<%-- Common Functions used in CollectionProtocol and DistributionProtocol --%>
<%!
	private String changeUnit(String specimenType,String subTypeValue)
	{
		if (specimenType == null)
			return "";
		if(specimenType.equals("Fluid"))
			return Constants.UNIT_ML;
		else if(specimenType.equals("Tissue"))
		{
			if(subTypeValue.equals(Constants.SLIDE) || subTypeValue.equals(Constants.PARAFFIN_BLOCK) || subTypeValue.equals(Constants.FROZEN_BLOCK))
				return Constants.UNIT_CN;
			else	
				return Constants.UNIT_GM;
		}
		else if(specimenType.equals("Cell"))
			return Constants.UNIT_CC;
		else if(specimenType.equals("Molecular"))
			return Constants.UNIT_MG;
		else
			return " ";
			
	}
%>
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

		function typeChange(element,arrayName)
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
			document.getElementById(unitspan).innerHTML = ugul[5];
		}	
		else 
		{
			if(className == "Tissue")
			{
				document.getElementById(unitspan).innerHTML = ugul[2];
			}	
		}
			
	}

// changes unit on specimen class changed.
	function changeUnit(listname,unitspan,subTypeListName)
	{
		var list = document.getElementById(listname);
		var selectedOption = list.options[list.selectedIndex].text;
		var subTypeList = document.getElementById(subTypeListName);

		if(selectedOption == "-- Select --")
			document.getElementById(unitspan).innerHTML = ugul[0];
		if(selectedOption == "Fluid")
		{
			document.getElementById(unitspan).innerHTML = ugul[1];
			typeChange(list,FluidArray);
		}
		if(selectedOption == "Tissue")
		{
			document.getElementById(unitspan).innerHTML = ugul[2];
			typeChange(list,TissueArray);
		}
		if(selectedOption == "Cell")
		{
			document.getElementById(unitspan).innerHTML = ugul[3];
			typeChange(list,CellArray);
		}
		if(selectedOption == "Molecular")
		{
			document.getElementById(unitspan).innerHTML = ugul[4];
			typeChange(list,MolecularArray);
		}
	}

</script>