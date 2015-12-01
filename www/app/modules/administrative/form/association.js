angular.module('os.administrative.form.formctxts', ['os.administrative.models'])
  .controller('FormCtxtsCtrl', function($scope, $modalInstance, $translate, args, Alerts) {

    var reload = false;

    function init() {
      $scope.showFormCtxts = true;
      $scope.extnEntities = [
        {entity: 'Participant', name: $translate.instant('entities.participant')},
        {entity: 'Specimen', name: $translate.instant('entities.specimen')},
        {entity: 'SpecimenCollectionGroup', name: $translate.instant('entities.visit')},
        {entity: 'SpecimenEvent', name: $translate.instant('entities.specimen_event')},
        {entity: 'SpecimenRequest', name: $translate.instant('entities.specimen_request')}
      ];
      $scope.form = args.form;
      $scope.cpList = args.cpList;

      $scope.cpFormCtxts = args.formCtxts;
      angular.forEach(args.formCtxts, function(formCtx) {
        if (!formCtx.collectionProtocol.id) {
          formCtx.collectionProtocol.shortTitle = $translate.instant('form.all');
        }

        for (var i = 0; i < $scope.extnEntities.length; i++) {
           var entity = $scope.extnEntities[i];
           if (entity.entity == formCtx.level) {
             formCtx.level = entity;
             break;
           }
        }
      });

      $scope.cpFormCtxt = {
        allProtocols: false,
        isMultiRecord: false,
        selectedCps: [],
        selectedEntity: undefined,
        isSpecimenEvent: false
      }
    }

    $scope.onEntitySelect = function(selected) {
      $scope.cpFormCtxt.isSpecimenEvent = (selected.entity == 'SpecimenEvent');
    }

    $scope.enableAttach = function(formCtxt) {
      return ((formCtxt.allProtocols || (formCtxt.selectedCps && formCtxt.selectedCps.length > 0)) 
               && !!formCtxt.selectedEntity) || formCtxt.isSpecimenEvent;
    };

    $scope.attach = function(formCtxt) {
      var cpIds = [];
      if (formCtxt.allProtocols || formCtxt.isSpecimenEvent) { 
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
      var formContext = $scope.form.newFormContext({form: $scope.form, cpId: cpId, entityType: entity.entity});

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
