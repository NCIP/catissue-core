					                               // function to set focus on first element

function setFocusOnFirstElement()
      {
            frm = document.forms[0];
            if (frm != null)
            {
                  for (i = 0 ; i < frm.elements.length; i++)
                        {
                        if (frm.elements[i].type != null &&  frm.elements[i].type != "hidden")   
                        {
                              frm.elements[i].focus();      
                              break;
                        } 
                        else {
                              continue;
                        }
                  }
            }
      }

// function for Logout tab
      function MM_swapImgRestore() 
      { 
        var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
      }

      function MM_findObj(n, d) { 

        var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

            d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

        if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

        for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);

        if(!x && d.getElementById) x=d.getElementById(n); return x;

      }

      function MM_swapImage()
       { 
        var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
         if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
      }

function openInstitutionWindow()
{
      institutionwindow=dhtmlmodal.open('Institution', 'iframe', 'Institution.do?operation=add&pageOf=pageOfPopUpInstitution','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')
    institutionwindow.onclose=function()
      { 
            return true;
      }
}

function openDepartmentWindow()
{
    departmentWindow=dhtmlmodal.open('Department', 'iframe', 'Department.do?operation=add&pageOf=pageOfPopUpDepartment&menuSelected=3&submittedFor=AddNew','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')
    departmentWindow.onclose=function()
      {
       return true;
      }
}

function openCRGWindow()
{
    crgWindow=dhtmlmodal.open('Cancer Research Group', 'iframe', 'CancerResearchGroup.do?operation=add&pageOf=pageOfPopUpCancerResearchGroup&menuSelected=4&submittedFor=AddNew','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')
    crgWindow.onclose=function()
      {
         return true;
      }
}

// function for storage container to view map
      function showStorageContainerMap()
    { 
        var frameUrl='ShowFramedPage.do?pageOf=pageOfSpecimen&storageType=-1';      
            platform = navigator.platform.toLowerCase();
            if (platform.indexOf("mac") != -1)
            {
                NewWindow(frameUrl,'name',screen.width,screen.height,'no');
            }
            else
            {
                NewWindow(frameUrl,'name','800','600','no');
           }     
     }

     //to close window
      function closeUserWindow()
      {     
              if(window.opener)
                   window.parent.close();
      }

      // for AssignPrivilege

      var request;

   function sendRequestsWithData(url,data,cpOperation)
   {
     request=newXMLHTTPReq();
     request.onreadystatechange = function requestHandler()
     {
         if(request.readyState == 4)
         {       //When response is loaded
            if(request.status == 200)
            {   
                var response = request.responseText; 
				
                var jsonResponse = eval('('+ response+')');
                var hasValue = false;

                if(jsonResponse.locations!=null)
                {
                   var num = jsonResponse.locations.length; 
				   
                   // To clear the User List
                   var selectedEditableUserId="";
				  
                   if((cpOperation == "getUsersForThisSites")||(cpOperation == "editRow"))
                   {
                       var eleOfUserSelBox = document.getElementById('userIds');
                       while(eleOfUserSelBox.length > 0)
                       {
                      	   if((eleOfUserSelBox.options[eleOfUserSelBox.length - 1].selected)==true)
                      	   {
                      	       selectedEditableUserId= eleOfUserSelBox.options[eleOfUserSelBox.length - 1].value
                           }
                           eleOfUserSelBox.remove(eleOfUserSelBox.length - 1);
                       }
                   }     
                    // To clear the CP List
                   var selectedEditableCPId="";
                   if((cpOperation == "getCPsForThisSites")||(cpOperation == "editRowForUserPage"))
                   {
                      var eleOfCPSelBox = document.getElementById('cpIds');
                      while(eleOfCPSelBox.length > 0)
                       {
                           if((eleOfCPSelBox.options[eleOfCPSelBox.length - 1].selected)==true){
                       		    selectedEditableCPId= eleOfCPSelBox.options[eleOfCPSelBox.length - 1].value
                           }
                           eleOfCPSelBox.remove(eleOfCPSelBox.length - 1);
                        }
                   }     
						
                   // To Deselect privileges
                   if((cpOperation == "editRow")||(cpOperation == "addPrivilege")||(cpOperation == "addPrivOnUserPage")||(cpOperation == "editRowForUserPage"))
                   {
                        var eleOfActionSelBox = document.getElementById('actionIds');     
                        for(var opt=0; opt<eleOfActionSelBox.length; opt++)
                        {
                            eleOfActionSelBox.options[opt].selected=false;
                        }
                   }
                    // To clear Privileges
                  if((cpOperation == "getActionsForThisRole")||(cpOperation == "editRow")||(cpOperation == "editRowForUserPage")){
					  var eleOfActionSelBox = document.getElementById('actionIds');
                      clearSelBoxList(eleOfActionSelBox);
                  }
  
                  // To Deselect Sites

                  if((cpOperation == "editRow")||(cpOperation == "editRowForUserPage")||(cpOperation == "addPrivilege")||(cpOperation == "addPrivOnUserPage"))
                   {
                      var eleOfSiteSelBox = document.getElementById('siteIds');      
                      for(var opt=0; opt<eleOfSiteSelBox.length; opt++)
                      {
                           eleOfSiteSelBox.options[opt].selected=false;
                      }
                   }

                   // To Deselect Users and Roles 
                   if(cpOperation == "addPrivilege")
                    {
                       var eleOfUserSelBox = document.getElementById('userIds');      
                       for(var opt=0; opt<eleOfUserSelBox.length; opt++){
                            eleOfUserSelBox.options[opt].selected=false;
                       }
                       var eleOfRoleSelBox = document.getElementById('roleIds');
                       eleOfRoleSelBox.options[0].selected=true;
                    }

                    // To Deselect CPs and Roles 
                    if(cpOperation == "addPrivOnUserPage")
                    {
                        var eleOfCPSelBox = document.getElementById('cpIds'); 
                        for(var opt=0; opt<eleOfCPSelBox.length; opt++){
                             eleOfCPSelBox.options[opt].selected=false;
                         }
                        var eleOfRoleSelBox = document.getElementById('roleIds');
                        eleOfRoleSelBox.options[0].selected=true;
                     }
                    if(cpOperation == "getActionsForThisCPs")
                     {
					
						var actionSelBox = document.getElementById('actionIds');
                       	clearSelBoxList(actionSelBox);
                     }
                      if((cpOperation == "getCPsForThisSites")&& num==0)
					{
						 getActionsForThisCPs('cpIds','siteIds','roleIds','cpCheckId');
					}

                     
                    for(i=0;i<num;i++)
                     {     
                      // User List for Selected Sites                             
                        if(cpOperation == "getUsersForThisSites")
                        {     
                            theValue  = jsonResponse.locations[i].locationId;
                            theText = jsonResponse.locations[i].locationName;
                             var myNewOption = new Option(theText,theValue); 
                            document.getElementById('userIds').options[i] = myNewOption;
                            if((selectedEditableUserId!=null)&&(theValue==selectedEditableUserId))
                            {
                                 document.getElementById('userIds').options[i].selected=true;
                            }
                        }

                     // Privileges List for Selected Roles
                        else if(cpOperation == "getActionsForThisRole")
                        {     
                             var roleId=document.getElementById('roleIds');                                            
                             var eleOfActionSelBox = document.getElementById('actionIds');
                             var numOfSelPrivilges=jsonResponse.locations[i].selectedActionArray.length;
                             var totalPrivileges=jsonResponse.locations[i].actionJsonArray.length;
                      //--for scientist
                            if((roleId.value)=='7')
                            {
                               for(var actionCounter=0;actionCounter<numOfSelPrivilges;actionCounter++)
                                {
                                    theValue = jsonResponse.locations[i].selectedActionArray[actionCounter].locationId;
                                    theText  =jsonResponse.locations[i].selectedActionArray[actionCounter].locationName;
                                    var myNewOption = new Option(theText,theValue);
                                    eleOfActionSelBox.options[i] = myNewOption;
                                }                         
                            }
                            //--for others
                            else
                            {
                                for(var counter1=0;counter1<totalPrivileges;counter1++)
                                {
                                    theValue = jsonResponse.locations[i].actionJsonArray[counter1].locationId;
                                    theText  =jsonResponse.locations[i].actionJsonArray[counter1].locationName;
                                    var myNewOption = new Option(theText,theValue);
                                    eleOfActionSelBox.options[counter1] = myNewOption;
                                    for(var counter2=0;counter2<numOfSelPrivilges;counter2++)
                                    {
                                        var selActionVal=jsonResponse.locations[i].selectedActionArray[counter2].locationId;
                                        if(eleOfActionSelBox.options[counter1].value == selActionVal)
                                        {
                                           eleOfActionSelBox.options[counter1].selected=true;
                                        break;
                                      }
                                    }
                                 }
                             }
                         }

             		 // Edit Row
                         else if(cpOperation == "editRow")
                          {
                           var selectedUserId=jsonResponse.locations[i].selectedUserId;
                           var usersList=jsonResponse.locations[i].userJsonArray;
                           var roleId=jsonResponse.locations[i].roleId; 
                           var sites=jsonResponse.locations[i].siteJsonArray;
                           var actions=jsonResponse.locations[i].actionJsonArray;
                           var selActions=jsonResponse.locations[i].selActionJsonArray;
                           // --for sites
 
                           var siteSelBox = document.getElementById('siteIds');
                           for(var len=0;len<sites.length;len++){
                                for(var opt=0; opt<siteSelBox.length; opt++){
                                     if(siteSelBox.options[opt].value==sites[len]){
                                        siteSelBox.options[opt].selected=true;
                                         break;
                                     }
                               }
                           }
                           //-- for user
                          for(var opt=0; opt<usersList.length; opt++)
                          {
                              theValue  = jsonResponse.locations[i].userJsonArray[opt].locationId;
                              theText = jsonResponse.locations[i].userJsonArray[opt].locationName;
                              var myNewOption = new Option(theText,theValue);     
                              document.getElementById('userIds').options[opt] = myNewOption;
                          }
                          var numOfUser=document.getElementById('userIds').length;
                          var userSelBox = document.getElementById('userIds');
                          for(var i=0;i<numOfUser;i++){
                              if(userSelBox.options[i].value==selectedUserId){
                                  document.getElementById('userIds').options[i].selected=true;
                                  var selectedUserName=userSelBox.options[i].text;
                                  break;
                              }
                          }

                          //-- for role     
                          var roleSelBox = document.getElementById('roleIds');
                          for(var opt=0; opt<roleSelBox.length; opt++){
                               if(roleSelBox.options[opt].value==roleId){
                                   roleSelBox.options[opt].selected=true;
                                   break;
                               }
                          }

                          // --for Privileges

                           var eleOfActionSelBox = document.getElementById('actionIds');
                           var numOfSelPrivilges=selActions.length;
                           var totalPrivileges=actions.length;
                                          //--for scientist
                          if((roleId)=='7')
                          {
                             for(var actionCounter=0;actionCounter<numOfSelPrivilges;actionCounter++)
                             {
                                theValue = selActions[actionCounter].actionId;
                                theText  =selActions[actionCounter].actionName;
                                var myNewOption = new Option(theText,theValue);
                                eleOfActionSelBox.options[i] = myNewOption;
                             }                         
                          }
                         //--for others
                          else
                         {
                              for(var counter1=0;counter1<totalPrivileges;counter1++)
                              {
                                theValue = actions[counter1].locationId;
                                theText  =actions[counter1].locationName;
                                 var myNewOption = new Option(theText,theValue);
                                 eleOfActionSelBox.options[counter1] = myNewOption;
                                for(var counter2=0;counter2<numOfSelPrivilges;counter2++)
                                {
                                    var selActionVal=selActions[counter2];
	                               	if(eleOfActionSelBox.options[counter1].value == selActionVal)
	                                  {
	                                     eleOfActionSelBox.options[counter1].selected=true;
	                                    break;
	                                }
                              	}
                         	 }
                          }

                                    // To display edit Message
                                          var editMessageDiv=document.getElementById("editMessageDivId");
                                          editMessageDiv.style.display='block';
                                          var message="<span class='black_ar'><strong>&nbsp;Edit Privilege For "+ selectedUserName+"</strong> </span></td></tr>";;
                                          editMessageDiv.innerHTML=message;   
                                    }

           							 // Add Privileges
                                    else if(cpOperation == "addPrivilege")
                                    {
                                          var tableId="summaryTableId";
                                          var rowId=jsonResponse.locations[i].rowId; 
                                          var userName=jsonResponse.locations[i].userName;
                                          var userId=jsonResponse.locations[i].userId;
                                          var roleName=jsonResponse.locations[i].roleName; 
                                          var sites=jsonResponse.locations[i].sites;
                                          var actions = jsonResponse.locations[i].actionName;
                                          // for update table after response
                                          var selectedRowId=document.getElementById(rowId);
                                                if(selectedRowId!=null)
                                                {
                                                      // replace row to table
                                                      var opt="updateRow";
                                                }
                                                else
                                                {
                                                      // add row to table
                                                      var opt="addRow";
                                                }
                                                addOrUpdateRowToTable(opt,tableId,rowId,userName,userId,sites,roleName,actions);
                                    }
                                    else if(cpOperation == "getCPsForThisSites")
                                    {     
									
                                          theValue  = jsonResponse.locations[i].locationId;
                                          theText = jsonResponse.locations[i].locationName;
                                          var myNewOption = new Option(theText,theValue); 
                                          document.getElementById('cpIds').options[i] = myNewOption;
                                          if((selectedEditableCPId!=null)&&(theValue==selectedEditableCPId))
                                          {
                                                document.getElementById('cpIds').options[i].selected=true;
                                          }
                                          if(i==(num-1))
                                          {
                                       	 	 getActionsForThisCPs('cpIds','siteIds','roleIds','cpCheckId');
                                       	  break;
                                          }
                                    }
                            	  // Edit Row "editRowForUserPage"

                                    else if(cpOperation == "editRowForUserPage")

                                    {
										
                                          var selectedCPId=jsonResponse.locations[i].selectedCPId;
                                          var cpList=jsonResponse.locations[i].cpJsonArray;
                                          var roleId=jsonResponse.locations[i].roleId; 
                                          var sites=jsonResponse.locations[i].siteJsonArray;
                                          var actions=jsonResponse.locations[i].actionJsonArray;
                                          var selActions=jsonResponse.locations[i].selActionJsonArray;
										  
                                          // --for sites
                                          var siteSelBox = document.getElementById('siteIds');
                                          for(var len=0;len<sites.length;len++){
                                                for(var opt=0; opt<siteSelBox.length; opt++){
                                                      if(siteSelBox.options[opt].value==sites[len]){
                                                            siteSelBox.options[opt].selected=true;
                                                            break;
                                                      }
                                                }
                                          }

                                          //-- for role     

                                          var roleSelBox = document.getElementById('roleIds');
                                          for(var opt=0; opt<roleSelBox.length; opt++){
                                                if(roleSelBox.options[opt].value==roleId){
                                                      roleSelBox.options[opt].selected=true;
                                                      break;
                                                }
                                          }

                                          // --for Privileges

                                    //    var roleId=document.getElementById('roleIds');                                            

                                          var eleOfActionSelBox = document.getElementById('actionIds');

                                          var numOfSelPrivilges=selActions.length;
                                          var totalPrivileges=actions.length;

                                          //--for scientist


                                          if((roleId)=='7')
                                          {
                                                for(var actionCounter=0;actionCounter<numOfSelPrivilges;actionCounter++)
                                                {
													
                                                      theValue = selActions[actionCounter].actionId;
                                                      theText  =selActions[actionCounter].actionName;
                                                      var myNewOption = new Option(theText,theValue);
                                                      eleOfActionSelBox.options[i] = myNewOption;
                                                }                         
                                          }

                                          //--for others

                                          else
                                          {
											  selectedOptsList(eleOfActionSelBox,actions,selActions);
                                       /*         for(var counter1=0;counter1<totalPrivileges;counter1++)
                                                {
                                                      theValue = actions[counter1].actionId;
                                                      theText  =actions[counter1].actionName;
                                                      var myNewOption = new Option(theText,theValue);
                                                      eleOfActionSelBox.options[counter1] = myNewOption;
                                                      for(var counter2=0;counter2<numOfSelPrivilges;counter2++)
                                                      {
                                                            var selActionVal=selActions[counter2];
                                                            if(eleOfActionSelBox.options[counter1].value == selActionVal)
                                                            {
                                                                  eleOfActionSelBox.options[counter1].selected=true;
                                                                  break;
                                                            }
                                                      }
                                                }*/
                                          }
                              //-- for CP

                                          for(var opt=0; opt<cpList.length; opt++)
                                          {
                                                theValue  = jsonResponse.locations[i].cpJsonArray[opt].locationId;
                                                theText = jsonResponse.locations[i].cpJsonArray[opt].locationName;
                                                var myNewOption = new Option(theText,theValue);     
                                                document.getElementById('cpIds').options[opt] = myNewOption;
                                          }
                                          var numOfCP=document.getElementById('cpIds').length;
                                          var cpSelBox = document.getElementById('cpIds');
                                          for(var i=0;i<numOfCP;i++){
                                                if(cpSelBox.options[i].value==selectedCPId){
                                                      document.getElementById('cpIds').options[i].selected=true;
                                                      var selectedCPName=cpSelBox.options[i].text;
                                                      break;
                                                }
                                          }

                                          

                  /*                // To display edit Message

                                          var editMessageDiv=document.getElementById("editMessageDivId");

                                          editMessageDiv.style.display='block';

                                                                                          

                                          var message="<span class='black_ar'><strong>&nbsp;Edit Privilege For "+ selectedUserName+"</strong> </span></td></tr>";;

                                          editMessageDiv.innerHTML=message;   

                  */                

                                    }
                                    else if(cpOperation == "addPrivOnUserPage")
                                    {
                                          var tableId="summaryTableId";
                                          var rowId=jsonResponse.locations[i].rowId; 
                                          var cpName=jsonResponse.locations[i].cpName;
                                          var cpId=jsonResponse.locations[i].cpId;
                                          var roleName=jsonResponse.locations[i].roleName; 
                                          var sites=jsonResponse.locations[i].sites;
                                          var actions = jsonResponse.locations[i].actionName;
                                          // for update table after response

                                          var selectedRowId=document.getElementById(rowId);
                                                if(selectedRowId!=null)
                                                {
                                                      // replace row to table
                                                      var opt="updateRow";
                                                }
                                                else
                                                {
                                                      // add row to table
                                                      var opt="addRow";
                                                }
                                                addOrUpdateRowToTableForUesrPage(opt,tableId,rowId,cpName,cpId,sites,roleName,actions);

                                    }
                                    else if(cpOperation == "getActionsForThisCPs")

                            		{
										var optsList=jsonResponse.locations[i].actionJsonArray;
										var selOptsList=jsonResponse.locations[i].selectedActionArray;
	                                	var actionSelBox = document.getElementById('actionIds');
	                                	
	                                	selectedOptsList(actionSelBox,optsList,selOptsList);
	                               
                             		 }
                             		                                   
										 else if(cpOperation == "getActionsForThisSites")

                            		{
                            			var optsList=jsonResponse.locations[i].actionJsonArray;
                            			var selOptsList=jsonResponse.locations[i].selectedActionArray;
                            			 var actionSelBox = document.getElementById('actionIds');
										 clearSelBoxList(actionSelBox);
	                                	 selectedOptsList(actionSelBox,optsList,selOptsList);
	                                	 
                             		 }
                                    
                              }

                        }

              }

          }

      };

            request.open("POST",url,true);

            request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

            request.send(data);

      }

   // Function to send request to get Users for selected sites.
      function getUsersForThisSites(siteObject)
      {     
         var cpOperation="getUsersForThisSites";
         var selectedSiteIds = new Array();
         var count=0;
         for (var i = 0; i < siteObject.options.length; i++)
         {
             if (siteObject.options[ i ].selected)
             {
                  selectedSiteIds[count]=(siteObject.options[i].value);
                  count=count+1;
             }
         }
        var url="ShowAssignPrivilegePage.do";
        var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds;
        sendRequestsWithData(url,data,cpOperation);
      }

       // Function to send request to get CPs for selected sites.
      function getCPsForThisSites(siteObject)
      {     
         var cpOperation="getCPsForThisSites";
         var selectedSiteIds = new Array();
         var count=0;
         for (var i = 0; i < siteObject.options.length; i++)
         {
             if (siteObject.options[ i ].selected)
             {
                 selectedSiteIds[count]=(siteObject.options[i].value);
                 count=count+1;
             }
         }
         var url="ShowAssignPrivilegePage.do?pageOf=pageOfUserAdmin";
         var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds;
         sendRequestsWithData(url,data,cpOperation);
      }

    
   // Function to send request to get Privileges Summary.

      function getUserPrivilegeSummary(operation)

      {

            var cpOperation="addPrivilege";     

            siteListCtrl=document.getElementById("siteIds");

            userListCtrl=document.getElementById("userIds");

            roleListCtrl=document.getElementById("roleIds");

            actionListCtrl=document.getElementById("actionIds");

             var selectedSiteIds = new Array();

             var selectedUserIds = new Array();

             var selectedRoleIds = new Array();

             var selectedActionIds = new Array();  

                  if(siteListCtrl.options.length>0){

                        for (var i = 0; i < siteListCtrl.options.length; i++)

                        {

                              if (siteListCtrl.options[ i ].selected){

                                  selectedSiteIds.push(siteListCtrl.options[ i ].value);

                              }

                        }

                  }

                  if(userListCtrl.options.length>0){

            

                        for (var i = 0; i < userListCtrl.options.length; i++)

                        {

                              if (userListCtrl.options[ i ].selected){

                                  selectedUserIds.push(userListCtrl.options[ i ].value);

                              }

                        }

                  }

                  if(roleListCtrl.options.length>0){

                  for (var i = 0; i < roleListCtrl.options.length; i++)

                  {

                        if (roleListCtrl.options[ i ].selected){

                            selectedRoleIds.push(roleListCtrl.options[ i ].value);

                        }

                  }  

                  }

                  if(actionListCtrl.options.length>0){

            

                        for (var i = 0; i < actionListCtrl.options.length; i++)

                        {

                              if (actionListCtrl.options[ i ].selected){

                                  selectedActionIds.push(actionListCtrl.options[ i ].value);

                              }

                        }     

                  }

            

                  var errorFlagForSite=false;

                  var errorFlagForUser=false;

                  var errorFlagForAction=false;

                  var errorFlagForRole=false;

            

                  if(selectedSiteIds.length=='0'){

                        errorFlagForSite=true;

                  }

                  if(selectedUserIds.length=='0'){

                        errorFlagForUser=true;

                  }

                  if(selectedActionIds.length=='0'){

                        errorFlagForAction=true;

                  }

                  if(selectedRoleIds[0]=='-1'){
		
                        errorFlagForRole=true;

                  }

                  

                  var divId=document.getElementById("errorMess");

                  divId.style.display="none";

                  if(errorFlagForSite==true||errorFlagForUser==true||errorFlagForAction==true||errorFlagForRole==true){

                        validateMethod(divId,errorFlagForSite,errorFlagForUser,errorFlagForRole,errorFlagForAction);

                  }

                  else{

                        var url="ShowAssignPrivilegePage.do";

                        var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds+"&selectedUserIds="+selectedUserIds+"&selectedRoleIds="+selectedRoleIds+"&selectedActionIds="+selectedActionIds+"&operation="+operation;               

                        sendRequestsWithData(url,data,cpOperation);

                  }

      }

   // Function to Add or Update  a Row

      function addOrUpdateRowToTable(opt,tableId,rowId,userName,userId,sites,roleName, actions)

      {

            var tb = document.getElementById(tableId);

            var rows = new Array(); 

            rows = tb.rows;

            var noOfRows = rows.length;   

            if(opt=="addRow")

            {

                  var row = document.getElementById(tableId).insertRow(noOfRows);

                  row.setAttribute("id",rowId);

 

                  if(noOfRows%2!=0){

                        row.className="tabletd1";

                  }

                  var chkName="chk_"+noOfRows;

 

                  

            }

            else if(opt=="updateRow")

            {

                  var row = document.getElementById(rowId);

                  for(counter=0;counter<row.cells.length;)

                  {

                        if(row.cells[counter].getElementsByTagName("INPUT")!=null){

                              if(row.cells[counter].getElementsByTagName("INPUT")[0]!=null){

                                    if(row.cells[counter].getElementsByTagName("INPUT")[0].nodeName=="INPUT"){

                                          chkName=row.cells[counter].getElementsByTagName("INPUT")[0].id;

                                    }

                              }

                        }

                         row.removeChild(row.cells[counter]);

                  }

 

                  row.setAttribute("id",rowId);

            }

              // first cell

                  var aprCheckb=row.insertCell(0);

                  aprCheckb.className="black_ar";                 

                  aprCheckb.width="6%";               

                  sname="<input type='checkbox' name='" + chkName +"' id='" + chkName +"'/>";            

                  aprCheckb.onclick=function(){enableDeleteButton('summaryTableId','deleteButtonId');} ;

                  aprCheckb.innerHTML=""+sname;

 

                  // Second Cell

                  var str=""+sites;

                  var newSites = "" ;

                  if(str.length>30){

                        newSites=str.substring(0,27)+"...";

                  }

                  else{

                        newSites=str;

                  }

                  var aprSites=row.insertCell(1);

                  aprSites.className="black_ar";

                  aprSites.width="21%";

            //    aprSites.innerHTML="<div style='word-wrap:break-word;width=24%;'>"+sites+"</div>";

                  aprSites.innerHTML="<span>"+newSites+"</span>";

                  aprSites.onmouseover=function(){Tip(sites,WIDTH,200);} ;

                              //third Cell

                  var aprUser=row.insertCell(2);

                  aprUser.className="black_ar";

                  aprUser.width="20%";

                  aprUser.innerHTML="<span>"+userName+"</span>";

 

                  //fourth Cell

                  var aprRole=row.insertCell(3);

                  aprRole.className="black_ar";

                  aprRole.width="15%";

            //    ctrl=document.getElementById('roleIds');

            //    sname=ctrl.options[ctrl.selectedIndex].text;

            //    aprRole.innerHTML="<span>"+sname+"</span>";

            aprRole.innerHTML="<span>"+roleName+"</span>";

                  

                  

                  var actionString = "" + actions;

                  var newActionsString="";

                  if(actionString.length>40){

                        newActionsString=actionString.substring(0,37)+"...";

                  }

                  else{

                        newActionsString=actionString;;

                  }

                  //Fifth Cell

                  var aprActions=row.insertCell(4);

                  aprActions.className="black_ar";

                  aprActions.width="33%";

                  aprActions.innerHTML="<span>"+newActionsString+"</span>";

                  aprActions.onmouseover=function(){Tip(actions,WIDTH,200);} ;

                  //Sixth Cell

                  var aprEdit=row.insertCell(5);

            //    aprEdit.className="view";

                  aprEdit.align="left";

                  aprEdit.width="5%";

                  aprEdit.innerHTML="<a href='#' class='view'>Edit</a>";

                  aprEdit.onclick=function(){editRow(rowId);} ;

                  

      }

   // Function to send request to edit Row.     

 function editRow(rowId)

 {

 

       var cpOperation="editRow";

       var url="ShowAssignPrivilegePage.do";

                  var data="cpOperation="+cpOperation+"&selectedRow="+rowId;

            

                  sendRequestsWithData(url,data,cpOperation);

 }

 // Function to enable delete button on select a checkBob

 function enableDeleteButton(tableId,deleteButtonId)
 {
      var tb = document.getElementById(tableId);
      var rows = new Array(); 
      rows = tb.rows;

      var noOfRows = rows.length;   
//    deleteButton=document.apForm.deleteButton;
      deleteButton=document.getElementById(deleteButtonId);

      var flag=false;/*indicates that */

      for(var i=0;i<noOfRows;i++)
      {
            var chk = document.getElementById("chk_"+i);
             if (chk.checked == true)
             {
                  deleteButton.disabled = false;
                  flag=true;
                  break;
             }
      }

      if((deleteButton.disabled == false)&&(flag==false))
      {
            deleteButton.disabled = true;
      }
 }

// Validate method  to display Error messages

function validateMethod(divId,errorFlagForSite,errorFlagForUser,errorFlagForRole,errorFlagForAction)
{
      var message="";
      if(errorFlagForSite)
      {
            message =   "<li> <font color='red'>Site is required.</font> </li>";
      }
      if(errorFlagForUser)
      {
            message = message+"<li> <font color='red'>User is required.</font> </li>";
      }

      if(errorFlagForRole)
      {
            message = message+"<li> <font color='red'>Role is required.</font> </li>";
      }
      if(errorFlagForAction)
      {
            message = message+"<li> <font color='red'>Privileges is required.</font> </li>";
      }

      if(message!=null)
      {
            divId.style.display = 'block';
            divId.innerHTML=message;
      }
}

//-- end Assign Privilege

function deleteCheckedRows(operation,tableId,deleteButtonId)
	{
	  var form=document.forms[0];
      var pageOf = form.pageOf.value; 
      /** element of tbody    **/
      var tbodyElement = document.getElementById(tableId);
      /** number of rows present    **/
      var counts = tbodyElement.rows.length;
      var deletedRowsArray=new  Array();
      var arrayCounter=0;
      var cpOperation="deleteRow";

      for(var i=0;i < counts;i++)
      {
            itemCheck ="chk_"+i;
            var chk = document.getElementById(itemCheck);
            if(chk.checked==true)
            {
                  var pNode = null;
                  var k = 0;
                        /** getting table ref from tbody    **/

                        pNode = tbodyElement.parentNode;
                        /** curent row of table ref **/
                        var currentRow = chk.parentNode.parentNode;
                        k = currentRow.rowIndex;
                        /** deleting row from table **/
                        pNode.deleteRow(k);
                  deletedRowsArray[arrayCounter]=currentRow.id;
                  arrayCounter=arrayCounter+1;
            }
            if(tbodyElement.rows.length==0){
                  var deleteButtonObj=document.getElementById(deleteButtonId);
                  deleteButtonObj.disabled = true;
            }
      }

      var index=0;
      for(var i=0;i < counts;i++)
      {
            var chk = document.getElementById("chk_"+i);
            if(chk!=null){
                  chk.id="chk_"+(i-index);

                  if((i-index)%2!=0)
                  {
                        chk.parentNode.parentNode.setAttribute("class","tabletd1");
                  }
                  else
                  {
                        chk.parentNode.parentNode.setAttribute("class","black_ar");
                  }
            }
            else
            {
                  index=index+1;
            }
      }

      var url="ShowAssignPrivilegePage.do?pageOf="+pageOf; 
      var data="cpOperation="+cpOperation+"&deletedRowsArray="+deletedRowsArray+"&operation="+operation;                
      sendRequestsWithData(url,data,cpOperation);
}

//Consent Tracking Module Virender Mehta(Start)

      //This Function will add more consent Tier 

      function addConsentTier()

      {           

            var val = parseInt(document.forms[0].consentTierCounter.value);

            val = val + 1;

            document.forms[0].consentTierCounter.value = val;

            var rowCount = document.getElementById('innertable').rows.length;

            var createRow = document.getElementById('innertable').insertRow(1);

            if(rowCount % 2 ==0)

                  createRow.className="tabletd1";

            

          var createCheckBox=createRow.insertCell(0);

            var createTextArea=createRow.insertCell(1);

            

            

            var iCount = rowCount-1;

            var consentName="consentValue(ConsentBean:"+iCount+"_statement)";

            var consentKey="consentValue(ConsentBean:"+iCount+"_consentTierID)";

                  

            createCheckBox.className="black_ar";

            createCheckBox.setAttribute('align','center');

            createTextArea.className="link";

                        

            var sname = "<input type='hidden' id='" + consentKey + "'>";                        

            createCheckBox.innerHTML="<input type='checkbox'class=black_ar name='consentcheckBoxs'id='check"+iCount+"'>";

            createTextArea.innerHTML= sname+"<textarea rows='2'class='formFieldSized' style='width:90%;' name="+consentName+"></textarea>";

      }

      

      //On selecting Select All CheckBox all the associted check box wiil be selected

      function checkAll(chkInstance)

      {

            var chkCount= document.getElementsByName('consentcheckBoxs').length;

            for (var i=0;i<chkCount;i++)

            {

                  var elements = document.getElementsByName('consentcheckBoxs');

                  elements[i].checked = chkInstance.checked;

            }

      }

 

     //This function will delete the selected consent Tier
	function deleteSelected()
	{
		var answer = confirm ("Are you sure want to delete consent(s) ?")
		if(answer)
		{
			var rowIndex = 0;	
			var rowCount=document.getElementById('innertable').rows.length;
			var removeButton = document.getElementsByName('removeButton');
			
			/** creating checkbox name**/
			var chkBox = document.getElementsByName('consentcheckBoxs');
			var lengthChk=chkBox.length;
			var j = 0;
			for(var i=0;i<lengthChk;i++)
			{
				if(chkBox[j].checked==true)
				{
					var gettable = document.getElementById('innertable');
					var currentRow = chkBox[j].parentNode.parentNode;
					rowIndex = currentRow.rowIndex;
					gettable.deleteRow(rowIndex);
				}
				else
				{
					j++;
				}	
			}
			var j = chkBox.length;
			for(var i=0;i<chkBox.length;i++)
			{
				var currentRow = chkBox[i].parentNode.parentNode;
				
			}
		}
	}	

      //This function will the called while switching between Tabs

      function switchToTab(selectedTab)
      {

            var displayKey="block";

            

            if(!document.all)

                  displayKey="table";

                  

            var displayTable=displayKey;

            var tabSelected="none";

            

            if(selectedTab=="collectionProtocolTab")

            {

                  tabSelected=displayKey;

                  displayTable="none";

            }     

            

            var display3=document.getElementById('table1');

            display3.style.display=tabSelected;

            

                  

            var display4=document.getElementById('consentTierTable');

            display4.style.display=displayTable;      

        

            var display5=document.getElementById('submittable');

            display5.style.display=tabSelected;

 

            var collectionTab=document.getElementById('collectionProtocolTab');

            var consentTab=document.getElementById('consentTab');

            

            if(selectedTab=="collectionProtocolTab")

            {

              collectionTab.innerHTML="<img src=images/uIEnhancementImages/cp_details.gif alt=Collection Protocol Details width=174 height=20 border=0 />"

              consentTab.innerHTML="<img src=images/uIEnhancementImages/cp_consents1.gif alt=Consents width=94 height=20 border=0 />"

              consentTab.className="";

            }

            else        

            {  collectionTab.innerHTML="<img src=images/uIEnhancementImages/cp_details1.gif alt=Collection Protocol Details width=174 height=20 border=0 />"

               consentTab.innerHTML="<img src=images/uIEnhancementImages/cp_consents.gif alt=Consents width=94 height=20 border=0 />"

               collectionTab.className="";

            }

            

      }

 

      //On calling this function the tab will be switched to CollectionProtocol Page

      function collectionProtocolPage()

      {

            switchToTab("collectionProtocolTab");

      }

 

      //On calling this function the tab will be switched to Consent Page     

      

//    Consent Tracking Module Virender Mehta (End)

 

//Add Bulk Specimen Virender(Start)

function showAssignPrivilegePage(operation)

{
    var action="DefineEvents.do?pageOf=pageOfAssignPrivilegePage&cpOperation=AssignPrivilegePage&operation="+operation;

      document.forms[0].action=action;

      document.forms[0].submit();

}

function consentPage()
{
      switchToTab("consentTab");
}

 function disableAllForSuperAdmin(roleObject,cpCheckId)
 {
 	 var selectedRoleType = roleObject.options[roleObject.selectedIndex].text;                             
     var selectedRoleIds = roleObject.options[roleObject.selectedIndex].value;
	 document.getElementById('actionIds').disabled=false;
     document.getElementById('siteIds').disabled=false;

     if(selectedRoleIds=="13")
     {
		deselectAllOptsOfSelBox(document.getElementById('actionIds'));
		deselectAllOptsOfSelBox(document.getElementById('siteIds'));
		deselectAllOptsOfSelBox(document.getElementById('cpIds'));
		document.getElementById(cpCheckId).checked=true;
     	document.getElementById('actionIds').disabled=true;
     	document.getElementById('siteIds').disabled=true;
     	document.getElementById('cpIds').disabled=true;
		document.getElementById(cpCheckId).disabled=true;
     }
     else
     {	
		
		 document.getElementById(cpCheckId).disabled=false;
		 if(document.getElementById(cpCheckId).checked==false)
		 {
			 document.getElementById('cpIds').disabled=false;
			 
		 }
		 else
		 {
		
			document.getElementById('cpIds').disabled=true;
		 }
     	getActionsForThisRole(roleObject,'siteIds','cpIds','cpCheckId')
     }
 }
 

 

// end here

 

 

 

 

// for user privilege--

 



 

  // Function to send request to get Privileges Summary.

      function getUserPagePrivSummary(operation)
      {
            var cpOperation="addPrivOnUserPage";      
            checkBoxCtrl=document.getElementById("cpCheckId");
            siteListCtrl=document.getElementById("siteIds");
            cpListCtrl=document.getElementById("cpIds");
            roleListCtrl=document.getElementById("roleIds");
            actionListCtrl=document.getElementById("actionIds");
            
            var isAllCPChecked=false;
            if(checkBoxCtrl.checked==true)
            {
             isAllCPChecked=true;
              
            }
            
             var selectedSiteIds = new Array();
             var selectedCPIds = new Array();
             var selectedRoleIds = new Array();
             var selectedActionIds = new Array();  

                  if(siteListCtrl.options.length>0){
                        for (var i = 0; i < siteListCtrl.options.length; i++)
                        {
                              if (siteListCtrl.options[ i ].selected){
                                  selectedSiteIds.push(siteListCtrl.options[ i ].value);
                              }
                        }
                  }

                  if(cpListCtrl.options.length>0){
                        for (var i = 0; i < cpListCtrl.options.length; i++)
                        {
                              if (cpListCtrl.options[ i ].selected){
                                  selectedCPIds.push(cpListCtrl.options[ i ].value);
                              }
                        }
                  }

                  if(roleListCtrl.options.length>0)
                  {
                  for (var i = 0; i < roleListCtrl.options.length; i++)
                  {
                        if (roleListCtrl.options[ i ].selected){
                            selectedRoleIds.push(roleListCtrl.options[ i ].value);
                        }
                  }  

                  }

                  if(actionListCtrl.options.length>0){
                        for (var i = 0; i < actionListCtrl.options.length; i++)
                        {
                              if (actionListCtrl.options[ i ].selected){
                                  selectedActionIds.push(actionListCtrl.options[ i ].value);
                              }
                        }     
                  }

                  var errorFlagForSite=false;
                  var errorFlagForCP=false;
                  var errorFlagForAction=false;
                  var errorFlagForRole=false;

                  if(selectedSiteIds.length=='0'){
                        errorFlagForSite=true;
                  }

                //  if(selectedCPIds.length=='0'){

                 //       errorFlagForCP=true;

               //   }

                  if(selectedActionIds.length=='0'){
                        errorFlagForAction=true;
                  }

                  if(selectedRoleIds[0]=='-1'){
                        errorFlagForRole=true;
                  }

                  var divId=document.getElementById("errorMess");
                  divId.style.display="none";
                  if(errorFlagForSite==true||errorFlagForAction==true||errorFlagForRole==true){
                        validateMethodForUserPriv(divId,errorFlagForSite,errorFlagForRole,errorFlagForAction);
                  }

                  else{
                        var url="ShowAssignPrivilegePage.do";
                        var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds+"&selectedCPIds="+selectedCPIds+"&selectedRoleIds="+selectedRoleIds+"&selectedActionIds="+selectedActionIds+"&isAllCPChecked="+isAllCPChecked+"&operation="+operation;                                     
                        sendRequestsWithData(url,data,cpOperation);
                  }

      }

   // Function to Add or Update  a Row

      function addOrUpdateRowToTableForUesrPage(opt,tableId,rowId,cpName,cpId,sites,roleName, actions)
      {
            var tb = document.getElementById(tableId);
            var rows = new Array(); 
            rows = tb.rows;
            var noOfRows = rows.length;  
 
            if(opt=="addRow")
            {
                  var row = document.getElementById(tableId).insertRow(noOfRows);
                  row.setAttribute("id",rowId);
                  if(noOfRows%2!=0){
                        row.className="tabletd1";
                  }
                  var chkName="chk_"+noOfRows;
            }
            else if(opt=="updateRow")
            {
                  var row = document.getElementById(rowId);
                  for(counter=0;counter<row.cells.length;)
                  {
                        if(row.cells[counter].getElementsByTagName("INPUT")!=null){
                              if(row.cells[counter].getElementsByTagName("INPUT")[0]!=null){
                                    if(row.cells[counter].getElementsByTagName("INPUT")[0].nodeName=="INPUT"){
                                          chkName=row.cells[counter].getElementsByTagName("INPUT")[0].id;
                                    }
                              }
                        }
                         row.removeChild(row.cells[counter]);
                  }
                  row.setAttribute("id",rowId);
            }

              // first cell
                  var aprCheckb=row.insertCell(0);
                  aprCheckb.className="black_ar";                 
                  aprCheckb.width="6%";               
                  sname="<input type='checkbox' name='" + chkName +"' id='" + chkName +"'/>";            
                  aprCheckb.onclick=function(){enableDeleteButton('summaryTableId','deleteButtonId');} ;
                  aprCheckb.innerHTML=""+sname;

                  // Second Cell
                  var str=""+sites;
                  var newSites = "" ;
                  if(str.length>30){
                        newSites=str.substring(0,27)+"...";
                  }
                  else{
                        newSites=str;
                  }

                  var aprSites=row.insertCell(1);
                  aprSites.className="black_ar";
                  aprSites.width="21%";
            //    aprSites.innerHTML="<div style='word-wrap:break-word;width=24%;'>"+sites+"</div>";
                  aprSites.innerHTML="<span>"+newSites+"</span>";
                  aprSites.onmouseover=function(){Tip(sites,WIDTH,200);} ;
                              //third Cell

                  var aprCP=row.insertCell(2);
                  aprCP.className="black_ar";
                  aprCP.width="20%";
                  aprCP.innerHTML="<span>"+cpName+"</span>";
                  //fourth Cell

                  var aprRole=row.insertCell(3);
                  aprRole.className="black_ar";
                  aprRole.width="15%";
            //    ctrl=document.getElementById('roleIds');

            //    sname=ctrl.options[ctrl.selectedIndex].text;

            //    aprRole.innerHTML="<span>"+sname+"</span>";

           		  aprRole.innerHTML="<span>"+roleName+"</span>";

                  var actionString = "" + actions;
                  var newActionsString="";
                  if(actionString.length>50){
                        newActionsString=actionString.substring(0,47)+"...";
                  }
                  else{
                        newActionsString=actionString;;
                  }

                  //Fifth Cell

                  var aprActions=row.insertCell(4);
                  aprActions.className="black_ar";
                  aprActions.width="33%";
                  aprActions.innerHTML="<span>"+newActionsString+"</span>";
                  aprActions.onmouseover=function(){Tip(actions,WIDTH,200);} ;

                  //Sixth Cell

                  var aprEdit=row.insertCell(5);
            //    aprEdit.className="view";
                  aprEdit.align="left";
                  aprEdit.width="5%";
                  aprEdit.innerHTML="<a href='#' class='view'>Edit</a>";
                  aprEdit.onclick=function(){editRowForUserPage(rowId);} ;
      }

      function validateMethodForUserPriv(divId,errorFlagForSite,errorFlagForRole,errorFlagForAction)
      {
	      var message="";
	      if(errorFlagForSite){
	            message =   "<li> <font color='red'>Site is required.</font> </li>";
	      }
	      if(errorFlagForRole){
	            message = message+"<li> <font color='red'>Role is required.</font> </li>";
	      }
	      if(errorFlagForAction){
	            message = message+"<li> <font color='red'>Privileges is required.</font> </li>";
	      }
	      if(message!=null){
	            divId.style.display = 'block';
	            divId.innerHTML=message;
	      }
	   }

  // Function to send request to get Privileges for selected Roles.
      function getActionsForThisRole(roleObject,siteSelBoxId,cpSelBoxId,cpCheckId)
      {         
		  var siteObject=document.getElementById(siteSelBoxId);
          var cpObject=document.getElementById(cpSelBoxId);
          var form=document.forms[0];
          var pageOf = form.pageOf.value; 
          var selectedSiteIds= null;
          var selectedCPIds= null; 
		  var isAllCPChecked=false;
	            
	      if(pageOf !="pageOfUserAdmin")
		  {
	//			 var cpOperation="getActionsForThisRoleForUser"; 
			  selectedSiteIds= new Array();
			  selectedCPIds= new Array();
		  }
		  else
		  {
			 if(cpCheckId!=null&& cpCheckId!=undefined)
			 {
				 var cpCheckBoxObj=document.getElementById(cpCheckId);
				 if(cpCheckBoxObj.checked==true)
				 {
					isAllCPChecked=true;
				 }
			  }
	//			 var cpOperation="getActionsForThisRole"; 
		     selectedSiteIds = getSelElementsList(siteObject);
		     selectedCPIds = getSelElementsList(cpObject);
		  }  
		
           var cpOperation="getActionsForThisRole";      
  //         var selectedRoleType = roleObject.options[roleObject.selectedIndex].text;                           
           var selectedRoleIds = roleObject.options[roleObject.selectedIndex].value;  
   //        var selectedRoleIds= getSelElementsList(roleObject); 
           var url="ShowAssignPrivilegePage.do?pageOf="+pageOf;   
           var data="cpOperation="+cpOperation+"&selectedRoleIds="+selectedRoleIds+"&selectedSiteIds="+selectedSiteIds+"&selectedCPIds="+selectedCPIds+"&isAllCPChecked="+isAllCPChecked;                    
		   sendRequestsWithData(url,data,cpOperation);
      }

function editRowForUserPage(rowId)
 {
       var cpOperation="editRowForUserPage";
       var url="ShowAssignPrivilegePage.do";
       var data="cpOperation="+cpOperation+"&selectedRow="+rowId;
       sendRequestsWithData(url,data,cpOperation);
 }
 
 function getCutomRole(roleSelBoxId)
 {
     var roleSelBoxObj = document.getElementById(roleSelBoxId);
	 var len = roleSelBoxObj.options.length;
	 for(var x=0; x<len;x++)
	 {
		 if(roleSelBoxObj.options[x].value=="0")
		 {
			  roleSelBoxObj.options[ x ].selected=true;
			  break;
		 }
	 }
 }
 /*
 function getActionsForThisSites(siteSelBoxObject,roleSelBoxId,cpSelBoxId)
 {
    var cpOperation="getActionsForThisSites";
 	var selectedSiteIds = new Array();
    var selectedRoleId="";
    var count=0;

	var cpSelBoxObj= document.getElementById(cpSelBoxId);
	var selectedCPIds= getSelElementsList(cpSelBoxObj);
	if(selectedCPIds!=null)
	{
		cpOperation="getActionsForThisCPs";
		var url="ShowAssignPrivilegePage.do";
		var data="cpOperation="+cpOperation+"&selectedCPIds="+selectedCPIds;
		sendRequestsWithData(url,data,cpOperation);
	}
	else
	{
		var roleSelBoxObj=document.getElementById(roleSelBoxId);
    
		selectedRoleId=  getSelElementsList(roleSelBoxObj);
	
   		selectedSiteIds= getSelElementsList(siteSelBoxObject);
    
   		var url="ShowAssignPrivilegePage.do";
		var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds+"&selectedRoleId="+selectedRoleId;
		sendRequestsWithData(url,data,cpOperation);
	}
 }
 
 */
 
 
 // function is used for both site and cp
 function getActionsForThisCPs(cpSelBoxId,siteSelBoxId,roleSelBoxId,checkBoxId)
 {
	var cpOperation="";
	
	var checkBoxCtrl=document.getElementById(checkBoxId);
	var isAllCPChecked=false;
    if(checkBoxCtrl.checked==true)
    {
       isAllCPChecked=true;
    }
	var roleSelBoxObj=document.getElementById(roleSelBoxId);
	var cpSelBoxObj= document.getElementById(cpSelBoxId);
	var siteSelBoxObj= document.getElementById(siteSelBoxId);
	 		
   	var	selectedSiteIds= getSelElementsList(siteSelBoxObj);
	var selectedRoleIds= getSelElementsList(roleSelBoxObj);
	var selectedCPIds= getSelElementsList(cpSelBoxObj);
	
	if(selectedCPIds!=null&&(selectedCPIds.length>0))
	{
		cpOperation="getActionsForThisCPs";

		var url="ShowAssignPrivilegePage.do";
		var data="cpOperation="+cpOperation+"&selectedCPIds="+selectedCPIds+"&selectedRoleIds="+selectedRoleIds;
		sendRequestsWithData(url,data,cpOperation);
	}
	else if(selectedSiteIds!=null&&(selectedSiteIds.length>0))
	{
		cpOperation="getActionsForThisSites";
		
   		var url="ShowAssignPrivilegePage.do";
		var data="cpOperation="+cpOperation+"&selectedSiteIds="+selectedSiteIds+"&selectedRoleIds="+selectedRoleIds+"&isAllCPChecked="+isAllCPChecked;
		sendRequestsWithData(url,data,cpOperation);
	}
	else
	{
		var cpOperation="getActionsForThisRole";      
        var form=document.forms[0];
        var pageOf = form.pageOf.value;                  
          
        var url="ShowAssignPrivilegePage.do?pageOf="+pageOf;   
        var data="cpOperation="+cpOperation+"&selectedRoleIds="+selectedRoleIds+"&selectedSiteIds="+selectedSiteIds+"&selectedCPIds="+selectedCPIds+"&isAllCPChecked="+isAllCPChecked;                    
		sendRequestsWithData(url,data,cpOperation);
	}
 }
 
 // To deselect All options of List
 function deselectAllOptsOfSelBox(selBoxObj)
 {
 	var len=selBoxObj.options.length;
 	for(var count=0;count<len;count++)
 	{
 		if(selBoxObj.options[count].selected==true)
 		{
 			selBoxObj.options[count].selected=false;
 		}
 	}
 }
 
 // Add new options to select box and shows specified options as selected.
 function selectedOptsList(selBoxObj,optsList,selOptsList)
 {
 	var numOfOpts=optsList.length;
 	var numOfSelOpts= selOptsList.length;
 	for(var count=0;count<numOfOpts;count++)
 	{
 	    theValue  = optsList[count].locationId;
 	    theText = optsList[count].locationName;
 	    var myNewOption = new Option(theText,theValue); 
 	    selBoxObj.options[count] = myNewOption;
 	    
 	    for(var count1=0;count1<numOfSelOpts;count1++)
 		{
 			selectedValue  = selOptsList[count1].locationId;
 	   		selectedText = selOptsList[count1].locationName;
	 	    if(selBoxObj.options[count].value==selectedValue)
	 	    {
	 	       selBoxObj.options[count].selected=true;
	 	       break;
	 	    }
	 	}
 	}
 }
 
 // To clear select Box List
  function clearSelBoxList(selBoxObj)
  {
 	while(selBoxObj.length> 0)
 	{
 		 selBoxObj.remove(selBoxObj.length - 1);
    }
  }
 
 //  disabling and enabling of selectBox on checkbox click and get Actions for Sites and role based on
 //  condition whether they are selected or not.

function eventOnChkBoxClick(checkBoxId,cpSelBoxId,siteSelBoxId,roleSelBoxId)
{
	disableSelBoxOnChk(checkBoxId,cpSelBoxId);
	getActionsForThisCPs(cpSelBoxId,siteSelBoxId,roleSelBoxId,checkBoxId);
}

 // disabling and enabling of selectBox on checkbox click.
 function disableSelBoxOnChk(checkBoxId,selectBoxId)
{
      var checkBoxObj=document.getElementById(checkBoxId);
      var selBoxObj=document.getElementById(selectBoxId);
      var selBoxLen=document.getElementById(selectBoxId).options.length;
      if((checkBoxObj.checked)==true)
      {
            for(var len=0;len<selBoxLen;len++)
            {
                  selBoxObj.options[len].selected=false;
            }
            document.getElementById(selectBoxId).disabled=true;
      }
      else
      {
            document.getElementById(selectBoxId).disabled=false;
      }
}


// To get array of ids of selected elements from Select Box
function getSelElementsList(selectBoxObj)
{
	var selectedElementIds= new Array();
	 var count=0;
	var len= selectBoxObj.options.length;
	
	 for(var i=0;i<len;i++)
	 {
	 	if (selectBoxObj.options[ i ].selected)
        {
            selectedElementIds[count]=(selectBoxObj.options[i].value);
            count=count+1;
        }
	 }
	 return selectedElementIds;
}
/*
// for Preactions performed based on operation before deleting  row .
function preDeleteActions()
{
	var form=document.forms[0];
    var operation = form.operation.value; 
	if(operation=="Add")
	{
	
	}
	else
	{
	}
}
 */