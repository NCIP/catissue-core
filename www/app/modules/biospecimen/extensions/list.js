
angular.module('os.biospecimen.extensions.list', ['os.biospecimen.models', 'os.biospecimen.extensions.util'])
  .controller('FormsListCtrl', function($scope, $modal, ExtensionsUtil) {
    function init() {
      $scope.forms = $scope.object.getForms();
      $scope.records = $scope.object.getRecords();
    }

    $scope.deleteRecord = function(record) {
      ExtensionsUtil.deleteRecord(record, function(record) {
        var idx = $scope.records.indexOf(record);
        $scope.records.splice(idx, 1);
      }); 
    }

    init();
  });
