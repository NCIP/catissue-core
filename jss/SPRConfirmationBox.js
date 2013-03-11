var dhxWins;
	var interVeil;
	var optionObject;
	function downloadReport(reportType,option){
		optionObject = option;
		interVeil=window.parent.document.getElementById("loadingDivWthBg"); //Reference "veil" div
		if(!interVeil||interVeil==null)
		{
			var element = document.createElement("div");
			element.setAttribute("id", "loadingDivWthBg");
			element.style.display="none";
			element.innerHTML= '<div class="lightbox_overlay" style="background-color: #FFFFFF;height: 100%;left: 0;opacity: 0.5;filter: alpha(opacity = 50);	position: fixed; *position: absolute; top: 0; width: 100%;">'
						'&nbsp;</div>'+
						'<div class="holder" style="border-radius: 7px;background: #6b6a63;padding: 6px; position: absolute; left: 50%; top: 50%; ">'+
							'<div id="confirmationBox"></div>'+
						'</div>';
			//'<div class="lightbox_overlay" style="background-color: #FFFFFF;height: 100%;left: 0;opacity: 0.5;filter: alpha(opacity = 50);	position: fixed; *position: absolute; top: 0; width: 100%;">&nbsp;</div><div class="holder" style="border-radius: 7px;background: #6b6a63;padding: 6px; position: absolute; left: 50%; top: 50%; "><img src="images/uIEnhancementImages/loading.gif" id="lodImg" /></div>';
			window.parent.document.body.appendChild(element);
			var interVeil=window.parent.document.getElementById("loadingDivWthBg"); //Reference "veil" div
		}
		
		if(dhxWins == undefined){
			dhxWins = new dhtmlXWindows();
			dhxWins.setSkin("dhx_skyblue");
			dhxWins.enableAutoViewport(true);
			
		}
		
		dhxWins.setImagePath("");
		if(dhxWins.window("containerPositionPopUp")==null){
			var w =250;
			var h =150;
			var x = (screen.width / 3) - (w / 2);
			var y = 0;
			dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
			dhxWins.window("containerPositionPopUp").center();
			dhxWins.window("containerPositionPopUp").allowResize();
			dhxWins.window("containerPositionPopUp").setModal(true);
			dhxWins.window("containerPositionPopUp").setText("Terms and Conditions");
			dhxWins.window("containerPositionPopUp").button("minmax1").hide();
			dhxWins.window("containerPositionPopUp").button("park").hide();
			dhxWins.window("containerPositionPopUp").button("close").hide();
			dhxWins.window("containerPositionPopUp").setIcon("images/terms-conditions.png", "images/terms-conditions.png");
			 //dhxWins.window("containerPositionPopUp").setModal(false);
			var div = document.createElement("div");
		
			div.id="popupDiv";
			div.innerHTML = "<div style='padding-left:10px;padding-top:30px;' class='black_ar'><input type='checkbox' name='termCheckbox' id='termCheckbox' value='termCheckbox' onClick='enableAgree()'>I agree to the <a href='#'>Terms & Condiitons.</a></div>"
			+"<div style='padding-left:10px;padding-top:30px;' class='black_ar'>"+
			"<input type='button' name='I Agree' onClick='downloadSpr()' id='iAgreeButton' value='I Agree' style='margin-left:45px' disabled><input type='button'  value='Cancel' name='Cancel' onClick='closeTermWindow()'style='margin-left:6px'></div>";
			document.body.appendChild(div);
			dhxWins.window("containerPositionPopUp").attachObject("popupDiv");
		}
	}
	
	function enableAgree(){
		if(document.getElementById("termCheckbox").checked){
			document.getElementById("iAgreeButton").disabled = false;
		}else{
			document.getElementById("iAgreeButton").disabled = true;
		}
	}
	
	function downloadSpr(){
		if(document.getElementById("termCheckbox").checked){
			optionObject.dwdIframe.src = "ExportSprAction.do?scgId="+optionObject.scgId+"&sprNumber="+optionObject.sprNumber+"&reportId="+optionObject.identifiedId+"&deIdentifiedId="+optionObject.deIdentifiedId+"&reportType="+optionObject.reportType;
			closeTermWindow();
		}
	}
	
	function closeTermWindow(){
		dhxWins.window("containerPositionPopUp").close();
	}
	