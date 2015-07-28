
angular.module('os.administrative.form.list', ['os.administrative.models'])
  .controller('FormListCtrl', function($scope, $modal, $translate, Form, 
    CollectionProtocol, Util, DeleteUtil, Alerts) {

    function init() {
      $scope.extnEntities = [
        {entity: 'Participant', name: $translate.instant('entities.participant')},
        {entity: 'Specimen', name: $translate.instant('entities.specimen')},
        {entity: 'SpecimenCollectionGroup', name: $translate.instant('entities.visit')},
        {entity: 'SpecimenEvent', name: $translate.instant('entities.specimen_event')}
      ];

      $scope.cpList = [];
      $scope.formsList = [];
      loadAllForms();
      loadCollectionProtocols();
    }

    function loadAllForms() {
      Form.query().then(function(result) {
        $scope.formsList = result;
      })
    }

    function loadCollectionProtocols() {
      CollectionProtocol.list().then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      );
    };

    function deleteForm(form) {
      form.$remove().then(function(resp) {
          Alerts.success('form.form_deleted', form);
          loadAllForms();
      });
    }

    $scope.showFormContexts = function(form) {
      form.getFormContexts().then(function(formCtxts) {
        var formCtxtsModal = $modal.open({
          templateUrl: 'modules/administrative/form/association.html',
          controller: 'FormCtxtsCtrl',

          resolve: {
            args: function() {
              return {
                formCtxts: formCtxts,
                form: form,
                cpList: $scope.cpList,
                extnEntities: $scope.extnEntities
              }
            }
          }
        });

        formCtxtsModal.result.then(function(reloadForms) {
          if (reloadForms) {
            loadAllForms();
          }
        });
      });
    };


    $scope.confirmFormDeletion = function(form) {
      form.recordStats = []
      form.getRecordStats().then(function(recordStats) {
        Util.unshiftAll(form.recordStats, recordStats);
        angular.forEach(form.recordStats, function(stat) {
          for (var i = 0; i < $scope.extnEntities.length; i++) {
            var entity = $scope.extnEntities[i];
            if (entity.entity == stat.level) {
              stat.level = entity;
              break;
            }
          }
        });
      });

      DeleteUtil.confirmDelete({
        entity: form,
        templateUrl: 'modules/administrative/form/confirm-delete.html',
        delete: deleteForm
      });

    }

    init();
  });
