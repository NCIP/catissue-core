
angular.module('os.biospecimen.extensions.records', [])
  .controller('FormRecordsCtrl', function($scope, $stateParams, $state) {
    $scope.formCtxId = $stateParams.formCtxId;
    $scope.formId = $stateParams.formId;

    $scope.recordsResp = $scope.object.getRecords($scope.formCtxId);
    $scope.addRecord = function() {
      $state.go($scope.extnState + 'addedit', {formId: $scope.formId, formCtxId: $scope.formCtxId});
    }
  });
