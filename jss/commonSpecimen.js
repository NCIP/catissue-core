//Common functions from Newspecimen.jsp and createSpecimen.jsp 
var containerDropDownInfo, scGrid;
//Set last refresh time
if(window.parent!=null)
{
	if(window.parent.lastRefreshTime!=null)
	{
		window.parent.lastRefreshTime = new Date().getTime();
	}
}

function doOnLoad()
{
	var url=getActionToDoURL();
	//Drop Down components information
	containerDropDownInfo = {gridObj:"storageContainerGrid", gridDiv:"storageContainer", dropDownId:"storageContainerDropDown", pagingArea:"storageContainerPagingArea", infoArea:"storageContainerInfoArea", onOptionSelect:"containerOnRowSelect", actionToDo:url, callBackAction:onContainerListReady, visibilityStatusVariable:scGridVisible, propertyId:'selectedContainerName'};
	// initialising grid
	scGrid = initDropDownGrid(containerDropDownInfo,false); 
}