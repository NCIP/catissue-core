angular.module('os.administrative.form.formctxts', ['os.administrative.models'])
  .controller('FormCtxtsCtrl', function($scope, $modalInstance, args, Alerts) {

    var map = {};
    for (var i = 0; i < args.formCtxts.length; ++i) {
      var cp = args.formCtxts[i].collectionProtocol;
      var cpId = cp.id, title = cp.shortTitle;
      var level = args.formCtxts[i].level;
      var multiRecord = args.formCtxts[i].multiRecord;
      var cpFormCtxt = map[cpId];
      if (cpFormCtxt) {
        cpFormCtxt.levels.push(level);
      } else {
        map[cpId] = {title: title ? title : "ALL", levels: [level], multiRecord: multiRecord};
      }
    }

    var result = [];
    for (var k in map) {
      result.push(map[k]);
    }

    var existingCpCount = result.length;
    if (map["-1"]) {
      existingCpCount = -1;
    }

    $scope.cpFormCtxts = result;
    $scope.form = args.form;
    $scope.cpList = args.cpList;
    $scope.extnEntities = args.extnEntities;

    $scope.cpFormCtxt = {
      allProtocols: false,
      isMultiRecord: false,
      selectedCps: [],
      selectedEntity: undefined,
      isSpecimenEvent: false
    }

    $scope.onEntitySelect = function(selected) {
        $scope.cpFormCtxt.allProtocols = $scope.cpFormCtxt.isSpecimenEvent = selected.entity == 'SpecimenEvent';
    }

    $scope.enableAttach = function(formCtxt) {
       return ((formCtxt.allProtocols || (formCtxt.selectedCps && formCtxt.selectedCps.length > 0)) && formCtxt.selectedEntity) ? true : false;
    };

    $scope.attach = function(formCtxt) {
       var cpIds = [], newCpCnt = 0;
       if (formCtxt.allProtocols) {
         cpIds = [-1];
       } else {
         var selectedCps = formCtxt.selectedCps
         for (var i = 0; i < selectedCps.length; ++i) {
           cpIds.push(selectedCps[i].id);
           if (!map[selectedCps[i].id + ""]) {
             ++newCpCnt;
          }
         }
       }

       var formContext = {
         form: $scope.form,
         cpIds: cpIds,
         entity: formCtxt.selectedEntity.entity,
         isMultiRecord: formCtxt.isMultiRecord
       }

       formContext = $scope.form.newFormContext(formContext); 
       formContext.$update().then(
         function(data) {
           var cpCnt = -1;
           if (existingCpCount != -1 && !formCtxt.allProtocols) {
             cpCnt = existingCpCount + newCpCnt;
           }

           Alerts.success("Form Successfully Attached");
           $modalInstance.close(cpCnt);
         },

         function() {
           Alerts.error("Error in attaching form. Contact administrator");
           $modalInstance.dismiss('cancel');
         }
       );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

  });
