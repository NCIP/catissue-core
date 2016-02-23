
angular.module('os.administrative.form.list', ['os.administrative.models'])
  .controller('FormListCtrl', function(
    $scope, $state, $modal, $translate, Form, FormEntityReg,
    CollectionProtocol, Util, DeleteUtil, Alerts) {

    function init() {
      $scope.formFilterOpts = {};
      $scope.entityMap = {
        Participant: 'participant', 
        Specimen: 'specimen', 
        SpecimenCollectionGroup: 'visit', 
        SpecimenEvent: 'specimen_event'
      };
      $scope.cpList = [];
      $scope.formsList = [];
      loadForms($scope.formFilterOpts);
      loadCollectionProtocols();
      Util.filter($scope, 'formFilterOpts', loadForms);
    }

    function loadForms(filterOpts) {
      Form.query(filterOpts).then(function(result) {
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
      form.$remove().then(
        function(resp) {
          Alerts.success('form.form_deleted', form);
          loadAllForms();
        }
      );
    }

    $scope.openForm = function(form) {
      $state.go('form-addedit', {formId: form.formId});
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
                cpList: $scope.cpList
              }
            },

            entities: function(FormEntityReg) {
              return FormEntityReg.getEntities();
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
      form.entityMap = $scope.entityMap;
      form.dependentEntities = [];
      form.getDependentEntities().then(
        function(result) {
          Util.unshiftAll(form.dependentEntities, result);
        } 
      );

      DeleteUtil.confirmDelete({
        entity: form,
        templateUrl: 'modules/administrative/form/confirm-delete.html',
        delete: function () { deleteForm(form); }
      });

    }

    init();
  });
