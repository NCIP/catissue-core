angular.module('os.administrative.form.formctxts', ['os.administrative.models'])
  .controller('FormCtxtsCtrl', function($scope, $modalInstance, args, Alerts) {

    $scope.cpFormCtxts = args.formCtxts;
    angular.forEach(args.formCtxts, function(formCtx) {
      if (!formCtx.collectionProtocol.id) {
        formCtx.collectionProtocol.shortTitle = 'ALL';
      }
    });

    $scope.showFormCtxts = true;
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
         for (var i = 0; i < formCtxt.selectedCps.length; ++i) {
           cpIds.push(formCtxt.selectedCps[i].id);
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
           Alerts.success("form.form_attached");
           $modalInstance.close(true);
         },

         function() {
           Alerts.error("form.form_attachment_failed");
           $modalInstance.close(false);
         }
       );
    }

    $scope.confirmRemoveCtx = function(formCtx, $index) {
      $scope.showFormCtxts = false;
      $scope.removeCtxData = {ctx: formCtx, idx: $index};
    };

    $scope.removeCtx = function() {
      var cpId = $scope.removeCtxData.ctx.collectionProtocol.id || -1;
      var entity = $scope.removeCtxData.ctx.level;
      var formContext = $scope.form.newFormContext({form: $scope.form, cpId: cpId, entityType: entity});

      formContext.$remove().then(
        function(ctxIds) {
          $scope.formCtxts.splice($scope.removeCtxData.idx, 1);
          $scope.showFormCtxts = true;
          $scope.removeCtxData = {};
          reloadForms = true;
          
          //Utility.notify($("#ctx-alerts"), "Deleted!", "success", true);
        }
      );
    };

    $scope.cancelRemoveCtx = function() {
      $scope.showFormCtxts = true;
      $scope.removeCtxData = {};
    };


    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

  });
