// Functions from JSP Files
	
		var globalOperationValue;
		
		function initializeCombo()
		{
			var genotypeCombo = dhtmlXComboFromSelect("genotype");  
			genotypeCombo.enableFilteringMode(true);
			genotypeCombo.setOptionWidth(120);
			genotypeCombo.setSize(150);
		}
	
		function getConsent(identifier,collectionProtocolId,collectionProtocolTitle,index,anchorTagKey,consentCheckStatus,operationValue)
		{
			globalOperationValue =operationValue; 
			var collectionProtocolIdValue;
			var select = document.getElementById(collectionProtocolId);
			collectionProtocolIdValue=document.getElementById(collectionProtocolId).value;
			var dataToSend="showConsents=yes&cpSearchCpId="+collectionProtocolIdValue;
			ajaxCall(dataToSend, collectionProtocolId, identifier, anchorTagKey, index,consentCheckStatus);
		}
		
		function openConsentPage(collectionProtocolId,index,responseString,collectionProtocolRegIdValue,operationValue){
			//When RegId value is not available.-Add participant page.
			if(collectionProtocolRegIdValue == "null")
			{
			 openConsentPageAjax(collectionProtocolId,index,responseString);
			 return;
			}
			//Bug:5935 collectionProtocolRegIdValue is added to display list of Specimen related to Participant.
			if(responseString == "None Defined")
			{
				return;
			}

			var collectionProtocolIdValue=document.getElementById(collectionProtocolId).value;
			if(collectionProtocolIdValue=="-1")
			{
				alert("Please select collection protocol");
				return;
			}

			var url ="ConsentDisplay.do?operation=operationValue&pageOf=pageOfConsent&index="+index+"&cpSearchCpId="+collectionProtocolIdValue+"&collectionProtocolRegIdValue="+collectionProtocolRegIdValue;
			window.open(url,'ConsentForm','height=300,width=800,scrollbars=1,resizable=1');
		}
		
		
		var flag=false;
		//Ajax Code Start
		function ajaxCall(dataToSend, collectionProtocolId, identifier,anchorTagKey,index,consentCheckStatus)
		{
			if(flag==true)
			{
				return;
			}
			flag=true;
			var request = newXMLHTTPReq();
			request.onreadystatechange=function(){checkForConsents(request, collectionProtocolId, identifier,anchorTagKey,index,consentCheckStatus)};
			//send data to ActionServlet
			//Open connection to servlet
			request.open("POST","CheckConsents.do",true);
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			request.send(dataToSend);
		}
		
		
		function checkForConsents(request, collectionProtocolId,  verificationKey, anchorTagKey,index,consentCheckStatus)
		{
			if(request.readyState == 4)
			{
				//Response is ready
				if(request.status == 200)
				{
					var responseString = request.responseText;
					validateBarcodeLable=responseString;
					var anchorTag = document.getElementById(anchorTagKey);
					var spanTag = document.getElementById(consentCheckStatus);
					var consentResponseKey = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + index +"_isConsentAvailable)";;
								
					if(responseString=="Enter Response")
					{
						if(globalOperationValue == "edit")
						{
							responseString = "Edit Response";
						}
						spanTag.innerHTML="";
						if(anchorTag == null)
						{
							anchorTag = document.createElement("a");
						}
						anchorTag.setAttribute("id",anchorTagKey);
						anchorTag.setAttribute("href", "javascript:openConsentPageAjax('"+collectionProtocolId+"','"+index+"','"+responseString+"')");
						anchorTag.innerHTML=responseString+"<input type='hidden' name='" + verificationKey + "' value='Consent' id='" + verificationKey + "'/><input type='hidden' name='" + consentResponseKey+ "' value='" +responseString+ "' id='" + consentResponseKey+ "'/>";
						spanTag.appendChild(anchorTag);
					}
					else //No Consent
					{
						spanTag.innerHTML=responseString+"<input type='hidden' name='" + verificationKey + "' value='Consent' id='" + verificationKey + "'/><input type='hidden' name='" + consentResponseKey+ "' value='" +responseString+ "' id='" + consentResponseKey+ "'/>";
					}
					flag=false;
				}
			
			}
}

		/*
		 This function is linked with new CP Participant Registration Dynamically
		*/
		function openConsentPageAjax(collectionProtocolId,index,responseString){
			if(responseString == "None Defined")
			{
				return;
			}

			var collectionProtocolIdValue=document.getElementById(collectionProtocolId).value;
			if(collectionProtocolIdValue=="-1")
			{
				alert("Please select collection protocol");
				return;
			}

			var url ="ConsentDisplay.do?operation=globalOperationValue&pageOf=pageOfConsent&index="+index+"&cpSearchCpId="+collectionProtocolIdValue;
			window.open(url,'ConsentForm','height=300,width=800,scrollbars=1,resizable=1');
		}
		
		function intOnly(field)
		{
			if(field.value.length>0)
			{
				field.value = field.value.replace(/[^\d]+/g, '');
			}
		}
		
		function deselectParticipant()
		{
			var rowCount = mygrid.getRowsNum();
			for(i=1;i<=rowCount;i++)
			{
				var cl = mygrid.cells(i,0);
				cl.setChecked(false);
			}
		}
		
		function CreateNewClick(pageOf)
		{
			document.forms[0].radioValue.value="Add";
			document.forms[0].action="ParticipantAdd.do";
			document.forms[0].forwardTo.value="ForwardTo";
			document.forms[0].participantId.value="";
			document.forms[0].id.value="0";
							
			if(pageOf=="pageOfParticipantCPQuery")
			{
				document.forms[0].action="CPQueryParticipantAdd.do";
			}
			deselectParticipant();
		}
		
		function LookupAgain()
		{
			document.forms[0].radioValue.value="Lookup";
		}

		function onVitalStatusRadioButtonClick(element)
		{
			if(element.value == "Dead")
			{
				document.forms[0].deathDate.disabled = false;
			}
			else
			{
				document.forms[0].deathDate.disabled = true;
			}
		}
			
		// function from EditParticipant.jsp File
		function checkActivityStatusForCPR()
		{
			var collectionProtocolRegistrationVal = parseInt(document.forms[0].collectionProtocolRegistrationValueCounter.value);
			var isAllActive = true;
			for(i = 1 ; i <= collectionProtocolRegistrationVal ; i++)
			{
				var name = "collectionProtocolRegistrationValue(ClinicalStudyRegistration:" + i +"_activityStatus)";
				if((document.getElementById(name) != undefined) && document.getElementById(name).value=="Disabled")
				{
					isAllActive = false;
					var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
					if (go==true)
					{
						document.forms[0].submit();
					}
					else
					{
						break;
					}
				}
			}

			if (isAllActive==true)
			{
				document.forms[0].submit();
			}
		}
		
		function showMessage(titleMessage)
		{
     		 Tip(titleMessage,BGCOLOR,'#FFFFFF',BORDERCOLOR,'#000000',FONTCOLOR,'#000000',WIDTH,'30',FOLLOWMOUSE,'FALSE');
    	}
		
		function validateRegDate(param){
			var i = 1;
			while(i <= param){
			var id  = 'collectionProtocolRegistrationValue(ClinicalStudyRegistration:' + i + '_registrationDate)';
			var date  = document.getElementById(id);
			validateDate(date);
			i++;
		  }
		}
		
			function loadParticipantTabbar()
			{
				participantTabbar = new dhtmlXTabBar("participant_tabbar", "top",25);
				participantTabbar.setSkin('default');
				participantTabbar.setImagePath("dhtmlx_suite/imgs/");
				participantTabbar.setSkinColors("#FFFFFF", "#FFFFFF");

				participantTabbar.addTab("editParticipantTab",'<span style="font-size:13px"> Edit Participant </span>', "150px");
				participantTabbar.addTab("reportsTab",'<span style="font-size:13px"> View Report(s) </span>', "150px");
				participantTabbar.addTab("annotationTab",'<span style="font-size:13px">View Annotation </span>',"150px");
				participantTabbar.addTab("consentsTab",'<span style="font-size:13px"> Consents  </span>',"150px");

				participantTabbar.setHrefMode("iframes-on-demand");
				participantTabbar.setContent("editParticipantTab", "showParticipantDiv");
				participantTabbar.setContentHref("reportsTab",showSPRTab);
				participantTabbar.setContentHref("annotationTab", showAnnotationTab);  
				participantTabbar.setContentHref("consentsTab", showConsentsTab); 
				participantTabbar.setTabActive("editParticipantTab");
			}
			
