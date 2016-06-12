angular.module('os.administrative.form.formctxts', ['os.administrative.models'])
  .controller('FormCtxtsCtrl', function($scope, $modalInstance, $translate, args, cpList, entities, Alerts) {

    var reload = false;

    function init() {
      $scope.showFormCtxts = true;
      $scope.extnEntities = entities;
      $scope.form = args.form;
      $scope.cpList = cpList;

      $scope.cpFormCtxts = args.formCtxts;
      angular.forEach(args.formCtxts, function(formCtx) {
        if (!formCtx.collectionProtocol.id) {
          formCtx.collectionProtocol.shortTitle = $translate.instant('form.all');
        }

        for (var i = 0; i < $scope.extnEntities.length; i++) {
           var entity = $scope.extnEntities[i];
           if (entity.name == formCtx.level) {
             formCtx.level = entity;
             break;
           }
        }
      });

      $scope.cpFormCtxt = {
        allProtocols: false,
        isMultiRecord: false,
        selectedCps: [],
        selectedEntity: undefined
      }
    }

    $scope.enableAttach = function(formCtxt) {
      if (!formCtxt.selectedEntity) {
        return false;
      }

      if (formCtxt.selectedEntity.allCps) {
        return true;
      }

      return formCtxt.allProtocols || (formCtxt.selectedCps && formCtxt.selectedCps.length > 0);
    }

    $scope.attach = function(formCtxt) {
      var cpIds = [];
      if (formCtxt.allProtocols || formCtxt.selectedEntity.allCps) {
        cpIds = [-1];
      } else {
        for (var i = 0; i < formCtxt.selectedCps.length; ++i) {
          cpIds.push(formCtxt.selectedCps[i].id);
        }
      }

      var formContext = {
        form: $scope.form,
        cpIds: cpIds,
        entity: formCtxt.selectedEntity.name,
        isMultiRecord: formCtxt.isMultiRecord
      }

      formContext = $scope.form.newFormContext(formContext); 
      formContext.$saveOrUpdate().then(
        function(data) {
          Alerts.success("form.attached");
          $modalInstance.close(true);
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
      var formContext = $scope.form.newFormContext({form: $scope.form, cpId: cpId, entityType: entity.name});

      formContext.$remove().then(
        function() {
          $scope.cpFormCtxts.splice($scope.removeCtxData.idx, 1);
          $scope.showFormCtxts = true;
          Alerts.success("form.association_deleted", $scope.removeCtxData.ctx);
          $scope.removeCtxData = {};
          reload = true;
        }
      );
    };

    $scope.cancelRemoveCtx = function() {
      $scope.showFormCtxts = true;
      $scope.removeCtxData = {};
    };


    $scope.cancel = function() {
      $modalInstance.close(reload);
    }

    init();
  });
