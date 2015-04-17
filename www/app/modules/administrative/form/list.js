
angular.module('os.administrative.form.list', ['os.administrative.models'])
  .controller('FormListCtrl', function($scope, $modal, Form, CollectionProtocol, DeleteUtil) {

    function init() {
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

    $scope.extnEntities = [
      {entity: 'Participant', name: 'Participant'},
      {entity: 'Specimen', name: 'Specimen'},
      {entity: 'SpecimenCollectionGroup', name: 'Specimen Collection Group'},
      {entity: 'SpecimenEvent', name:'Specimen Event'}
    ];


    $scope.showFormContexts = function(form) {
      form.highlight = false;

      form.getFormContexts().then(function(formCtxts) {
        var formCtxtsModal = $modal.open({
          templateUrl: 'modules/administrative/form/formCtxts.html',
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

        formCtxtsModal.result.then(function(newCpCnt) {
          form.cpCount = newCpCnt;
        });
      });
    };


    $scope.deleteForm = function(form) {
      DeleteUtil.delete(form, {
        deleteWithoutCheck: true,
        onDeletion: function() {
          loadAllForms();
        }
      });
    }

    init();
  });
