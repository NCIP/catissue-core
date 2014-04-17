var hourTimeCombo,minutesTimeCombo,rhourTimeCombo,rminutesTimeCombo,siteCombo,collectCombo,receiverCombo,collectionStatusCombo;

function convertSelectToCombo()
{
 window.dhx_globalImgPath="dhtmlx_suite/imgs/";

siteCombo = dhtmlXComboFromSelect("site");
siteCombo.setSize("240");
siteCombo.attachEvent("onSelectionChange",processComboChange);
siteCombo.setName("site");
siteCombo.attachEvent("onOpen",onComboClick);
siteCombo.attachEvent("onKeyPressed",onComboKeyPress);

receiverCombo = dhtmlXComboFromSelect("receiver");
receiverCombo.setSize("240");
receiverCombo.attachEvent("onSelectionChange",processComboChange);
receiverCombo.setName("receiver");
receiverCombo.enableFilteringMode(true);
receiverCombo.attachEvent("onOpen",onComboClick);
receiverCombo.attachEvent("onKeyPressed",onComboKeyPress); 


collectCombo = dhtmlXComboFromSelect("collector");
collectCombo.setSize("240");
collectCombo.attachEvent("onSelectionChange",processComboChange);
collectCombo.setName("collector");
collectCombo.enableFilteringMode(true);
collectCombo.attachEvent("onOpen",onComboClick);
collectCombo.attachEvent("onKeyPressed",onComboKeyPress); 

collectionStatusCombo = dhtmlXComboFromSelect("collectionStatus");
collectionStatusCombo.setSize("240");
collectionStatusCombo.attachEvent("onSelectionChange",processComboChange);
collectionStatusCombo.setName("collectionStatus");
collectionStatusCombo.attachEvent("onOpen",onComboClick);
collectionStatusCombo.attachEvent("onKeyPressed",onComboKeyPress);

hourTimeCombo = dhtmlXComboFromSelect("hoursTime");
hourTimeCombo.setSize("40");
hourTimeCombo.attachEvent("onOpen",onComboClick);
hourTimeCombo.attachEvent("onKeyPressed",onComboKeyPress);

minutesTimeCombo = dhtmlXComboFromSelect("minuteTime");
minutesTimeCombo.setSize("40");
minutesTimeCombo.attachEvent("onOpen",onComboClick);
minutesTimeCombo.attachEvent("onKeyPressed",onComboKeyPress);

rhourTimeCombo = dhtmlXComboFromSelect("receivehoursTime");
rhourTimeCombo.setSize("40");
rhourTimeCombo.attachEvent("onOpen",onComboClick);
rhourTimeCombo.attachEvent("onKeyPressed",onComboKeyPress);

rminutesTimeCombo = dhtmlXComboFromSelect("receiveminuteTime");
rminutesTimeCombo.setSize("40");
rminutesTimeCombo.attachEvent("onOpen",onComboClick);
rminutesTimeCombo.attachEvent("onKeyPressed",onComboKeyPress);
}


function processComboChange()
{
 
scgDataJSON[this.name] = this.getSelectedValue();
}

function processData(obj)
{
 scgDataJSON[obj.name] = obj.value;
}

function submitSCG() {
	// set received date
	scgDataJSON["receivedDate"] = document.getElementById("receivedDate").value
			+ " " + rhourTimeCombo.getSelectedText() + ":"
			+ rminutesTimeCombo.getSelectedText();
	scgDataJSON["collectedDate"] = document.getElementById("collectedDate").value
			+ " "
			+ hourTimeCombo.getSelectedText()
			+ ":"
			+ minutesTimeCombo.getSelectedText();
	scgDataJSON["scgName"] = document.getElementById("scgName").value;
	scgDataJSON["site"] = siteCombo.getSelectedValue();
        scgDataJSON["receiver"] = receiverCombo.getSelectedValue();
        scgDataJSON["collector"] = collectCombo.getSelectedValue();
        scgDataJSON["collectionStatus"] = collectionStatusCombo.getSelectedValue();

	var response = dhtmlxAjax.postSync("SaveScgAjaxAction.do", "dataJSON="
			+ JSON.stringify(scgDataJSON));

	if (response.xmlDoc.responseText == "success") {
		document.getElementById("errorDiv").innerHTML = "";
		pageSubmit();
	} else {
		var errorList = eval(response.xmlDoc.responseText);
		var i;
		var errormsg = "";
		for (i = 0; i < errorList.length; i++) {
			errormsg = errormsg + "<div>" + errorList[i].msg + "</div>";
		}
		document.getElementById("errorDiv").style.display = "block";
		document.getElementById("errorDiv").innerHTML = document
				.getElementById("errorDiv").innerHTML
				+ errormsg;

	}

}