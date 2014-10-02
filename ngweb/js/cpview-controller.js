angular.module('plus.cpview', [])

.controller('CpViewController', 
  ['$scope', '$window', '$timeout', 'repository', '$filter',  
  function($scope, $window, $timeout, repository, $filter) {

  $scope.datePattern = datePattern;
  $scope.selectedCp = selectionCp;
  $scope.selectedParticipant = selParticipant;
  $scope.selectedSubCpId="";

  if (selParticipant.id == -1) { // How to do it angular way ??
    var $remote = $('#remote');
	$remote.removeAttr('value');
  }

  $scope.initSel = function(elem, callback){
    if ($scope.selectedParticipant.id ==-1) {
      return;
    }
    var participantId = $scope.selectedParticipant.id.split(',')[0];
    var cpId = $scope.selectedCp.id;
    repository.getParticipantById(cpId, participantId).success(function(result) {
      var participant = {
        id: result.id + "," + result.cprId,
        text: result.firstName + "," + result.lastName + '(' + result.ppId + ')'
      }
      callback(participant);
    });
  }

  repository.getAllCps()           // TODO: Revisit
    .success(function(result) {
      $scope.cps = result;
    });

  $scope.participantList =[];
  $scope.defaultParticipantList = [];

  $scope.onCpSelect = function(selected, redirect) {
  $scope.selectedSubCpId="";
    if (selected.id != "null" && redirect != false ) {
      var url = "CPDashboardAction.do?isSystemDashboard=false&cpSearchCpId="+selected.id;
      $('#cpFrameNew').attr('src',url);
	}
	
    $scope.selectedCp = {id: selected.id, shortTitle: selected.text};

    $scope.searchParticipant(selected.id, "", undefined);
    $scope.participantList = [];
    $scope.selectedParticipant={};

    $scope.tree=[];
  };

  $scope.registerParticipant = function() {
    $scope.selectedParticipant={};
    $scope.tree = [];
    var url = "QueryParticipant.do?operation=add&pageOf=pageOfParticipantCPQuery&clearConsentSession=true&cpSearchCpId=" +
               $scope.selectedCp.id + "&refresh=true";
    $('#cpFrameNew').attr('src',url);
  }

  $scope.searchParticipant = function (id, query, callback) {
    if (id == undefined  && query.length == 0 && $scope.defaultParticipantList .length > 0) {
      callback($scope.defaultParticipantList);
    } else {
      if (id == undefined) {
        id = $scope.selectedCp.id;
      }

      repository.getRegisteredParticipants(id, query).success(function(result) {
        $scope.participantList = [];
        for (var i = 0; i < result.length; ++i) {
          var participant = {
            id: result[i].id + "," + result[i].cprId,
            text: result[i].lastName + "," + result[i].firstName + '(' + result[i].ppId + ')'
          }
          $scope.participantList.push(participant);
        }
        if (query.length == 0) {
          $scope.defaultParticipantList = $scope.participantList;
        }
        if (callback != undefined) {
          callback($scope.participantList);
        }
      });
    }
  }

  $scope.onParticipantSelect = function(selected, selectedScg,directReq) {
    var ids = selected.id.split(',');
    var participantId = ids[0], cprId = ids[1];
    $scope.selectedCprId = cprId;
	$scope.selectedSubCpId="";
    $scope.tree=[];
	
    if (!selectedScg) {
      var url = "QueryParticipantSearchForView.do?pageOf=newParticipantViewPage&operation=edit&cpSearchCpId=" + 
                 $scope.selectedCp.id + "&id=" + participantId;
      $('#cpFrameNew').attr('src',url);		
    }
      
    $scope.selectedParticipant = selected;
	var participantId = selected.id.split(',')[0];
	if(!directReq){
	var cpTree = [];
	$scope.populateCPTree($scope.selectedCp.id,participantId,$scope.selectedCprId,cpTree);
	$scope.tree = cpTree;
	}else{
    return repository.getCollectionGroups($scope.selectedCprId).then(function(result) {
      var image;
      var collectionStatus;

      var tree = [];
      var scgs = result.data;
      for (var i = 0; i < scgs.length; ++i) {
        var scg = scgs[i];
        tree.push(
          {    		 
              id: scg.instanceType + scg.id + scg.eventId, 
              level: 1, 
              scgId: scg.id,
              type: 'scg',
              name: $scope.getScgLabel(scg),
              collectionStatus: $scope.getStatusIcon(scg.collectionStatus),
              tooltip: $scope.getScgTooltip(scg),
              eventId: scg.eventId,
              instance: scg.instanceType,
              nodes: [],
              state: 'closed'
          });
      }
      $scope.tree = tree;
      return tree;
    });
    }
	
  }

  $scope.viewParticipant = function(){
    var participantId = $scope.selectedParticipant.id.split(',')[0];
    var participantId 
    var url = "QueryParticipantSearchForView.do?pageOf=newParticipantViewPage&operation=edit&cpSearchCpId=" + 
              $scope.selectedCp.id + "&id=" + participantId;
    $('#cpFrameNew').attr('src',url);
  }
    
  if ($scope.selectedParticipant && $scope.selectedParticipant.id != -1) {
    $scope.onCpSelect({id: $scope.selectedCp.id, text: $scope.selectedCp.shortTitle});
    var scgTreeQ = $scope.onParticipantSelect(selParticipant, selectedScg,true);
    scgTreeQ.then(function() { $scope.handleDirectObjectLoad(); });
  }

  $scope.handleDirectObjectLoad = function(displayNode) {
    if (!selectedScg) {
      return;
    }

    var scgNode = null;
    var tree = $scope.tree;
    for (var i = 0; i < tree.length; i++) {
      if (tree[i].scgId == selectedScg.id) {
        scgNode = tree[i];
        break;
      }
    }

    if (!selectedSpecimen && displayNode != false) {
      $scope.displayNode(scgNode);
      return;
    } 

    scgNode.state = 'opened';
    var scgId = selectedScg.id;
	
    repository.getSpecimens(scgId).success(function(result) {
      scgNode.nodes = $scope.getSpecimenTree(result,'scg');
      scgNode.childrenProbed = true;
      if (scgNode.nodes.length == 0) {
        scgNode.state = 'disabled';
      }

      var path = [];
      $scope.getSpecimenNodePath(selectedSpecimen, scgNode.nodes, path);  

      var j = 0;
      var currNode = scgNode;
      for (var i = 0; i < path.length; ++i) {
        currNode.state = 'opened';
        var nodes = currNode.nodes;
        for (var j = 0; j < nodes.length; ++j) {
          if (path[i] == nodes[j].id) {
            currNode = nodes[j];
            break;
          }
        }
      }
      if (displayNode != false) {
    	  $scope.displayNode(currNode);
      }
    });
  };

  $scope.getSpecimenNodePath = function(specimen, specimens, path) {
    for (var i = 0; i < specimens.length; ++i) {
      path.push(specimens[i].id);
      if (specimen.id == specimens[i].specimenId) {
    	specimens[i].state = 'opened';
        return true;
      }

      if ($scope.getSpecimenNodePath(specimen, specimens[i].nodes, path)) {
        return true;
      }

      path.pop();
    }

    return false;
  };

  $scope.getSpecimenTree = function(specimens,parentType) {
    var specimenNodes = [];

    if (specimens == null || specimens == undefined) {
      return specimenNodes;
    }

    for (var i = 0; i < specimens.length; ++i) {
      var specimen = specimens[i];
      var name = $scope.getSpecimenName(specimen);
	  var scgId = parentType == 'cpe' ? '' : specimen.scgId;
      var specimenNode = {
    		  id: specimen.instanceType + specimen.id + specimen.requirementId,
    	        name: name,
    	        tooltip: 'Label: ' + name + ' Type: ' + specimen.specimenType,
    	        collectionStatus: $scope.getStatusIcon(specimen.collectionStatus),
    	        type: 'specimen',
    	        instance: specimen.instanceType,
    			specimenId: specimen.id,
    			scgId: scgId,
    			parentId: specimen.parentId,
    			requirementId: specimen.requirementId,
    	        nodes: $scope.getSpecimenTree(specimen.children,parentType),
    	        specimenType: specimen.specimenType,
    	        state: 'closed'
      }

      specimenNode.childrenProbed = true;
      if (specimenNode.nodes.length == 0) {
        specimenNode.state = 'disabled';
      }
      specimenNodes.push(specimenNode);
    }

    return specimenNodes;
  };
  
  $scope.getCpDependents = function(childCPList,dataNode) {
    var childCPNodes = [];

    if (childCPList == null || childCPList == undefined) {
      return childCPNodes;
    }

    for (var i = 0; i < childCPList.length; ++i) {
      var childCP = childCPList[i];
      var name = $scope.getChildCPLabel(childCP);
      var childCPNode = {
    		  id: 'childCP' + childCP.id + childCP.title, 
              scgId: childCP.id,
              type: 'childCP',
              cpType: childCP.cpType,
              name: $scope.getChildCPLabel(childCP),
              collectionStatus: $scope.getStatusIcon(childCP.collectionStatus),
              tooltip: $scope.getChildCPTooltip(childCP),
              eventId: childCP.title,
              instance: 'childCP',
              nodes: [],
              state: 'closed'
      }

      
      childCPNodes.push(childCPNode);
	  dataNode.nodes.push(childCPNode);
    }

    return childCPNodes;
  };
  
  $scope.getScgDependents = function(scgs,dataNode) {
    var scgNodes = [];

    if (scgs == null || scgs == undefined) {
      return scgNodes;
    }

    for (var i = 0; i < scgs.length; ++i) {
      var scg = scgs[i];
      var name = $scope.getScgLabel(scg);
      var scgNode = {
    		  id: scg.instanceType + scg.id + scg.eventId, 
              
              scgId: scg.id,
              type: 'scg',
              name: $scope.getScgLabel(scg),
              collectionStatus: $scope.getStatusIcon(scg.collectionStatus),
              tooltip: $scope.getScgTooltip(scg),
              eventId: scg.eventId,
              instance: scg.instanceType,
              nodes: [],
              state: 'closed'
      }

      scgNodes.push(scgNode);
	  dataNode.nodes.push(scgNode);
    }

    return scgNodes;
  };
  
  $scope.getSpecimenName = function(specimen) {
    var name = specimen.label ? specimen.label : specimen.specimenClass;
    if (specimen.requirementLabel) {
      name = name + "(" + specimen.requirementLabel + ")";
    } else {
        name = name + "(" + specimen.specimenType + ")";
    }
    return name;
  };

  $scope.onNodeToggle = function(data) {
    if (data.state == 'closed') {
      data.state = 'opened';

      if (data.childrenProbed == undefined) {
    	  var scgId = data.scgId;
          if (data.type == 'scg') {
  		  var objectType="";
  		  
  	if(data.instance == 'cpe'){
  		objectType = 'cpe';
  		scgId = data.eventId;
  	}
          repository.getSpecimens(scgId,objectType).success(function(result) {
            data.nodes = $scope.getSpecimenTree(result,objectType);
            data.childrenProbed = true;
            if (data.nodes.length == 0) {
              data.state = 'disabled';
            }
          });
        }
		if (data.type == 'childCP') {
  		  var objectType="";
  		  $scope.selectedSubCpId=data.scgId;
  		scgId = data.eventId;
		var cpNodes=[];
		var scgNodes=[];
		var nodes=[];
		if(data.regDate != '' && data.regDate){
		  var participantId = $scope.selectedParticipant.id.split(',')[0];
		  $scope.populateCPTree(data.scgId,participantId,data.regId,data.nodes);
		  }
        }
      }
    } else if (data.state == 'opened') {
      data.state = 'closed';
    }
  };

  $scope.displayNode = function(data) {
    $scope.selectedNode = data;
	
	
    if(data.type == 'scg') {
      var participantId = $scope.selectedParticipant.id.split(',')[0];
      var ids = data.id.split(',');
      $scope.treeReload = false;
	  var cpId = $scope.selectedCp.id;
	  
	  if($scope.selectedSubCpId)
	  {
		cpId = $scope.selectedSubCpId;
	  }
  	  var url="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=false&operation=edit&id="
          	 + data.scgId + "&cpSearchCpId=" + cpId + "&clickedNodeId="+ data.scgId;
  	  if(data.instance=='cpe')
  	  {
  		url="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroupCPQuery&cpId="+cpId+"&pId="+participantId+"&requestFrom=participantView&cpeId="+data.eventId;
  	  }
      $('#cpFrameNew').attr('src',url);
    } else if(data.type == 'specimen'){
      var ids = data.id.split(',');
      var url = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id=" + data.specimenId + 
      "&refresh=false& " + "cpSearchCpId=" + $scope.selectedCp.id;
		if(data.instance=='requirement')
		{
		url="QueryCreateSpecimenFromRequirement.do?scgId="+data.scgId+"&parentId="+data.parentId+"&requirementId="+data.requirementId;
		}		
      $('#cpFrameNew').attr('src',url);
    } 
	else {
      //var ids = data.id.split(',');
	  
	  var participantId = $scope.selectedParticipant.id.split(',')[0];
	  
      var url = "CPQuerySubCollectionProtocolRegistration.do?pageOf=pageOfCollectionProtocolRegistrationCPQuery&refresh=false&operation=add&cpSearchParticipantId="+participantId+"&cpSearchCpId="+data.scgId+"&participantId="+participantId+"&clickedNodeId="+data.id+"&regDate=&parentCPId="+$scope.selectedCp.id;
	  
		
      $('#cpFrameNew').attr('src',url);
    }
  }


  $scope.getStatusIcon = function(collectionStatus, cpType) {
    var statusIcon;
    if(collectionStatus == 'Complete' || collectionStatus == 'Collected') {
      statusIcon = 'fa fa-circle complete';
    } else if(collectionStatus == 'Not Collected' || collectionStatus == 'Closed') {
      statusIcon = 'fa fa-circle not-collected';
    } else if (collectionStatus == 'Distributed') {
      statusIcon = 'fa fa-circle distributed';
    }else if (collectionStatus == 'NotRegistered') {
      statusIcon = 'fa fa-circle not-registered';
    } else if (collectionStatus == 'Registered' && cpType && cpType=='Phase') {
      statusIcon = 'fa fa-circle registered_Phase';
    } else if (collectionStatus == 'Registered' && cpType && cpType=='Arm') {
        statusIcon = 'fa fa-circle registered_Arm';
	} else {
      statusIcon = 'fa fa-circle pending';
    } 
    return statusIcon;
  };

  $scope.getScgLabel = function(scg) {
    var date = scg.collectionDate ? scg.collectionDate : scg.registrationDate;
    var dateStr = $filter('date')(date, $scope.datePattern);
    return "T" + scg.eventPoint + ": " + scg.collectionPointLabel + ": " + dateStr;
  }
  
  $scope.getChildCPLabel = function(scg) {
    var title = scg.shortTitle;
    
    return title;
  }
  
  

  $scope.getChildCPTooltip = function(scg) {
    var date = scg.regDate ? scg.regDate : '';
    var dateStr = $filter('date')(date, $scope.datePattern);
    var htmlToolTip = 
    	"<table style=\"font-size: 12px\"><tbody>" +
    	  "<tr><td><b><i class=\"pull-left\">Title : </i></b></td><td class=\"pull-left\">"+ scg.title + "</td></tr>" +
    	  "<tr><td><b><i class=\"pull-left\">Rgistration Date: </i></b></td><td class=\"pull-left\">"+dateStr+"</td></tr>" +
    	"</tbody></table>";

    return htmlToolTip;
  }
  
  $scope.getScgTooltip = function(scg) {
    var date = scg.collectionDate ? scg.collectionDate : scg.registrationDate;
    var dateStr = $filter('date')(date, $scope.datePattern);
    var htmlToolTip = 
    	"<table style=\"font-size: 12px\"><tbody>" +
    	  "<tr><td><b><i class=\"pull-right\">Event Point : </i></b></td><td class=\"pull-left\">"+ scg.eventPoint + " (" + scg.collectionPointLabel  + ") "+ "</td><tr/>" +
    	  "<tr><td><b><i class=\"pull-right\">Collection date: </i></b></td><td class=\"pull-left\">"+ dateStr + "</td><tr/>" +
    	"</tbody></table>";

    return htmlToolTip;
  }
  
  
  $scope.populateCPTree = function(cpId,participantId,cprId,cpTree){
	repository.getChildCps(cpId).then(function(result) {
      var image;
      var collectionStatus;
	
      var tree = [];
      var childCP = result.data;
	  var childCPList=[];
	  
	  if(childCP.length >= 1){
		repository.getChildCpRegistrations(participantId,cpId).then(function(result) {
			var childCPReg = result.data;
			
			
			for (var i = 0; i < childCP.length; ++i) {
			var regDate = '';
			var regId = '';
			var subCP = childCP[i];
			for (var j = 0; j < childCPReg.length; ++j) {
				var subReg = childCPReg[j];
				if(subCP.id == subReg.cpId){
				regDate = subReg.registrationDate;
				regId = subReg.id;
				}
			}
			childCPList[i]= {
			  id: subCP.id, 
              shortTitle: subCP.shortTitle,
              regDate: regDate,
              collectionStatus: regDate == '' ? 'NotRegistered' : 'Registered',
              title: subCP.title,
              cpType: subCP.cpType,
			  regId: regId
			};
			
		}
		for (var i = 0; i < childCPList.length; ++i) {
        var scg = childCPList[i];
        cpTree.push(
          {    		 
              id: 'childCP' + scg.id + scg.title, 
              scgId: scg.id,
              type: 'childCP',
              name: $scope.getChildCPLabel(scg),
              collectionStatus: $scope.getStatusIcon(scg.collectionStatus,scg.cpType),
              tooltip: $scope.getChildCPTooltip(scg),
              cpType: scg.cpType,
              eventId: scg.title,
			  regId: scg.regId,
			  regDate: scg.regDate,
              instance: 'childCP',
              nodes: [],
              state: 'closed'
          });
      }
	  $scope.populateScgTree(cprId,cpTree);
	  })
	  }
	  else{
		$scope.populateScgTree(cprId,cpTree);
	  }
    });
  }
  
  $scope.populateScgTree = function(cprId,cpTree){
	repository.getCollectionGroups(cprId).then(function(result) {
      var image;
      var collectionStatus;

      var tree = [];
      var scgs = result.data;
      for (var i = 0; i < scgs.length; ++i) {
        var scg = scgs[i];
        cpTree.push(
          {    		 
              id: scg.instanceType + scg.id + scg.eventId, 
              scgId: scg.id,
              type: 'scg',
              name: $scope.getScgLabel(scg),
              collectionStatus: $scope.getStatusIcon(scg.collectionStatus),
              tooltip: $scope.getScgTooltip(scg),
              eventId: scg.eventId,
              instance: scg.instanceType,
              nodes: [],
              state: 'closed'
          });
      }
    });
  }
}]);