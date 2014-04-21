var demoApp = angular.module('demoApp', ['ui', 'ui.bootstrap', "template/tree/tree_item_renderer.html","template/tree/tree_renderer.html"]);
      
demoApp.controller('CpController', function($scope,$http,repository) {
    var participants=[];
    
	$scope.show1 = function(){ap
		return true;
	}
	
	$scope.selectedCp = selectionCp;
	$scope.SelectedParticipant = selParticipant;
	$scope.initSel = function(elem, callback){
		//console.log(elem);
		//console.log($scope.SelectedParticipant);
		callback(elem);
	}
	
	
	//
	// cpSvc.getAllCps().then(function(result) {
	//   if (result.status == 'ok') {
	//      ...
	//   } else {
	//      mesage according to result.status/result.errorCode
	//   }
	// });
	//
    repository.getAllCps()
        .success(function(result) {
            var cpToReturn = [{id: "-1", name: "Select......"}];
            for (var i = 0; i < result.length; ++i) {
                cpToReturn.push({id: result[i].id, name: result[i].shortTitle});
            }
            $scope.cps = cpToReturn;
        });
          
    $scope.participantList =[];

    $scope.registerParticipant = function(){
		control.setValue();
		$scope.tree=[];
        var url = "QueryParticipant.do?operation=add&pageOf=pageOfParticipantCPQuery&clearConsentSession=true&cpSearchCpId="+$scope.selectedCp.id+"&refresh=true";
        $('#cpFrameNew').attr('src',url);
    }
        
    $scope.onCpSelect = function(selected) {
        $scope.selectedCp = {id: selected.id, name: selected.text};
        $scope.SelectedParticipant = participants[selected.id - 1]; // TODO: something that needs to be taken care of
        $scope.$apply($scope.selectedCp);               // Let's not call apply multiple times within same execution context
        $scope.$apply($scope.SelectedParticipant);
        //control.setValue();
		$scope.SelectedParticipant={};
		$scope.tree=[];
		$scope.$apply($scope.tree);
		
		$scope.$apply();
    };

    $scope.showCp = function() {
      //alert($scope.selectedCp.id);
    };

    $scope.participantSelect = function(selected) {
        var ids = selected.id.split(','); // TODO: Why both cpr and participant is needed in select id
        $scope.SelectedParticipant = {id: ids[0], name: selected.text};
        $scope.SelectedCprId = {id: ids[1]};
        $scope.tree=[];
		 var url = "QueryParticipantSearchForView.do?pageOf=newParticipantViewPage&operation=edit&cpSearchCpId="+$scope.selectedCp.id+"&id="+$scope.SelectedParticipant.id;
        $('#cpFrameNew').attr('src',url);			// TODO: Should be abstracted in separate service method
        repository.getCollectionGroups($scope.SelectedCprId.id)
            .success(function(result) {
                //var scgs = [];
				var image;
				var collectionStatus;
                for (var i = 0; i < result.length; ++i) {
					collectionStatus = result[i].collectionStatus;
					
					if(collectionStatus == 'Complete')
					{
						image = 'flexclient/biospecimen/images/Specimen.GIF';
					}
					else if(collectionStatus == 'Not Collected')
					{
						image = 'images/specimenNotCollected.gif';
					}
					else
					{
						image = 'images/pendingSpecimen.gif';	// TODO: let's not use whole images. if not avoidable, let's use sprite images
					}
					var regDate = result[i].receivedDate?result[i].receivedDate:result[i].registrationDate;
					var scgName = "T"+result[i].eventPoint+": "+result[i].collectionPointLabel+": "+regDate;
					var scgTooltip = "Event Point: "+result[i].eventPoint+";Collection point label: "+result[i].collectionPointLabel+";  Received date: "+regDate;
					
					//
					// TODO: 1. Why prefix scg, when you already have type: scg
					//       2. $scope.tree.push( {
					//            id: result[i].id,
					//            level: 1,
					//            image: imagesType[result[i].collectionStatus],
					//            name: getScgNodeName(result[i])
					//          }
					//
                    $scope.tree.push({id:'scg,'+result[i].id, level: 1, image:image,name: scgName, type:'scg',tooltip:scgTooltip,nodes: []});
                }
            });
    }
    
	if($scope.SelectedParticipant && $scope.SelectedParticipant.id != -1){
		$scope.participantSelect($scope.SelectedParticipant);
	}
    $scope.viewParticipant = function(){
        var url = "QueryParticipantSearchForView.do?pageOf=newParticipantViewPage&operation=edit&cpSearchCpId="+$scope.selectedCp.id+"&id="+$scope.SelectedParticipant.id;
        $('#cpFrameNew').attr('src',url);
    }

    $scope.participantSearch = function(term, callback) {
        var matchedParticipants = [];
        var selectCP = $scope.selectedCp;
        repository.getRegisteredParticipants(selectCP.id, term)
                .success(function(result){
					$scope.participantList=[];
					for (var i = 0; i < result.length; ++i) {
						$scope.participantList
								.push({id: result[i].id+','+result[i].cprId, 
									   text: result[i].lastName+','+result[i].firstName+'('+result[i].ppId+')'});
					}
					//console.log($scope.participantList);
					callback($scope.participantList);
				});
		//return $scope.participantList;
    }
     
    //Tree related
    $scope.level = 1;
    $scope.active_ids = {1: 1};
    
    $scope.delete = function(data) {
        
        data.nodes = [];
    };
    
    $scope.add = function(data) {
        var post = data.nodes.length + 1;
        var newName = data.name + '-' + post;
        var level = data.level + 1;
        data.nodes.push({id: post, name: newName, level: level, nodes: []});
    };
    
    $scope.show = function(data){
        if(typeof $scope.active_ids[data.level] != 'undefined') {
            return $scope.active_ids[data.level] == data.id
        }
        return false;
    }
    
	// TODO: Move all tree directive related functions out of this controller scope
    $scope.toggalePlus = function(data) {
        if($scope.lastTarget && typeof $scope.active_ids[data.level] != 'undefined' && $scope.active_ids[data.level] == data.id){
            return false;
        }
        return true;
    }
    
	
	// TODO: on-node-expand and on-node-collapse should be on interface of ka-tree-view
	// one simple way to do is 
	// every node is represented as {display name, icon, tooltip, node-data}
	// whenever a node is expanded or collapsed, node-data is passed to callback
	// the node-data alone should be sufficient to carry out further actions
    $scope.expand = function(parent, data,$event) {
		var ids = data.id.split(',');
        if(data.nodes.length == 0) {
            var level = data.level + 1;
            if(data.type == 'scg') {
				
                repository.getSpecimens(ids[1]).success(function(result){
                    for (var i = 0; i < result.length; ++i) {
						
						var name = result[i].label ? result[i].label : result[i].specimenClass;
						if(result[i].requirementLabel)
						{
							name = name+"("+result[i].requirementLabel+")";
						}
						var tooltip = "Label: "+name+" Type: "+result[i].specimenType;
                        data.nodes.push({id: 'specimen,'+result[i].id, name: name, tooltip:tooltip, level: level,type:'specimen',nodes: []});
                    }
                  });
            }
            if(data.type == 'specimen') {
                repository.getChildSpecimens(ids[1]).success(function(result){
                    for (var i = 0; i < result.length; ++i) {
						var name = result[i].label ? result[i].label : result[i].specimenClass;
						if(result[i].requirementLabel)
						{
							name = name+"("+result[i].requirementLabel+")";
						}
						var tooltip = "Label: "+name+" Type: "+result[i].specimenType;
                        data.nodes.push({id: 'specimen,'+result[i].id, name: result[i].label,tooltip:tooltip,level: level,type:'specimen',nodes: []});
                    }
                  });
            }
        }
        
        //alert(data.nodes.length);
        if(typeof $scope.active_ids[data.level] != 'undefined' && $scope.active_ids[data.level] == data.id) {
            delete $scope.active_ids[data.level];
            return;
        }
                
        $scope.active_ids[data.level] = data.id
        $($event.target)
            .removeClass("fa-plus-square-o")
            .addClass("fa-minus-square-o");
        $scope.lastTarget = $event.target;
    }
            
    $scope.displayObject = function(data){console.log(data);
		$scope.active_id = data.id;
        if(data.type == 'scg'){
			var ids = data.id.split(',');
            var url = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=false&operation=edit&id="+ids[1]+"&cpSearchParticipantId="+$scope.SelectedParticipant.id+"&cpSearchCpId="+$scope.selectedCp.id+"&clickedNodeId="+ids[1];
            $('#cpFrameNew').attr('src',url);
        }
        if(data.type == 'specimen'){
			var ids = data.id.split(',');
            var url = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+ids[1]+"&refresh=false&cpSearchParticipantId="+$scope.SelectedParticipant.id+"&cpSearchCpId="+$scope.selectedCp.id;
                $('#cpFrameNew').attr('src',url);
        }
    }
    $scope.tree = [];
    //$scope.tree = [{id:1, level: 1, name: "Node", nodes: []}, {id:2, level: 1, name: "Node-X", nodes: []}];
})
.directive("kaSelect", function() {
    return {
      restrict: "E",
      replace: "true",
      template: "<select></select>",
      scope: {
        options: "=options",
        selected: "=ngModel",
        onSelect: "&onSelect"
      },

      link: function(scope, element, attrs) {
        var config = {
          options: [], 
          id: attrs.optionId,
          value: attrs.optionValue,
          onSelect: scope.onSelect()
        };
 
        scope.select = new Select2(element, config).render();

        scope.$watch('options', function(options) {
          scope.select.options(options).selectedOpts(scope.selected).render();
        });
      }
    };
  })
.directive('kaSearch', function() {
    return {
      restrict: "E",
      replace : "true",
      template: "<input type='hidden'>",
      scope   : {
        query   : "&onQuery", 
        onSelect: "&onSelect",
		initSelection: "&onInitselectionfn",
		elem: "=ngModel"
      },
      
      link: function(scope, element, attrs) {
      //alert(control);
        control = new Select2Search(element);
      
        scope.select = control;//new Select2Search(element);
        control
          .onQuery(function(qTerm, qCallback) { 
            return scope.query()(qTerm, qCallback); 
          })
          .onChange(function(option) { 
            scope.onSelect({selected: option});
          })
		  .onInitSelection(function(elem, callback){
			console.log(elem);
			scope.initSelection()(scope.elem, callback);
		  })
          .render();
		  
		   /*scope.$watch('elem', function(elem) {
			console.log("here");
			scope.select.setValue(elem);
			//scope.$apply();
			//scope.select.select2("val", null);
        });*/
      }
    }
})
.directive('treeView', function() {
    return {
      restrict: "E",
	  replace : true,
      templateUrl: "template/tree/tree_renderer.html"
    }
});
      
demoApp.factory('repository', function($http) {
    return { 
      getAllCps: function() {
        var url = 'rest/ng/collection-protocols';
        
        return $http.get(url);
      },
      getCollectionGroups: function(cprId){
        var url = 'rest/ng/collection-protocol-registrations/'+cprId+'/specimen-collection-groups';
        return $http.get(url);
      },
      getRegisteredParticipants: function(cpId,query) {//alert('d');
        var url = 'rest/ng/collection-protocols/'+cpId+'/participants?query='+query;
        return $http.get(url);
      },
      getSpecimens: function(scgId) {
        var url = 'rest/ng/specimen-collection-groups/'+scgId+'/child-specimens';
        return $http.get(url);
      },
      getChildSpecimens: function(parentId) {
        var url = 'rest/ng/specimens/'+parentId+'/child-specimens';
        return $http.get(url);
      }
    };
 });
 
// TODO: Tree directive is lacking cohesion
angular.module("template/tree/tree_item_renderer.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/tree/tree_item_renderer.html",
    "<div>\n" +
    "<div id=\"form.id\" class=\"fa pull-left\" ng-class=\"{true: 'fa-plus-square-o', false: 'fa-minus-square-o' } [toggalePlus(data)]\" ng-click=\"expand($parent.data, data,$event)\" ng-model='form'></div> \n" +
    "<div class=\"black_ar\" ng-click=\"displayObject(data)\" ng-class=\"{'active': (data.id == active_id)}\" >\n" + 
	"<span class=\"pull-left badge\"></span><div class=\"pull-left\" tooltip-placement=\"top\" tooltip=\"{{data.tooltip}}\" >{{data.name}}</div>\n" +
	"<div class=\"clear\"></div></div></div>\n" +
    "<ul ui:sortable ng:model=\"data.nodes\" ng-show=\"show(data)\" role=\"group\"> \n" +
    "   <li ng-repeat=\"data in data.nodes\" ng-include=\"'template/tree/tree_item_renderer.html'\"></li> \n" +
    "</ul>\n" +
    "");
}]);


 
 
angular.module("template/tree/tree_renderer.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/tree/tree_renderer.html",
    "<div style=\"margin-left: -12px;\">" +
	"<ul class=\"tree\" ui:sortable ng:model=\"tree\" role=\"tree\" >" +
	"<li ng-repeat=\"data in tree\" ng-include=\"'template/tree/tree_item_renderer.html'\" role=\"tree_item\" class=\"parent_li\"></li>\n"+
    "</ul>" +
	"</div>"+
	"");
}]); 