
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
