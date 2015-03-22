
angular.module('os.biospecimen.extensions.list', ['os.biospecimen.models'])
  .controller('FormsListCtrl', function($scope) {
    $scope.forms = $scope.object.getForms();
    $scope.records = $scope.object.getRecords();
  });
