
angular.module('os.biospecimen.extensions.list', ['os.biospecimen.models', 'os.biospecimen.extensions.util'])
  .controller('FormsListCtrl', function($scope, $modal, ExtensionsUtil) {
    function init() {
      $scope.forms = $scope.object.getForms();
      $scope.records = $scope.object.getRecords();
    }

    function onDeleteRecord(record) {
      var records = $scope.records;
      for (var i = 0; i < records.length; ++i) {
        if (records[i] == record) {
          records.splice(i, 1);
          break;
        }
      }
    }

    $scope.deleteRecord = function(record) {
      ExtensionsUtil.deleteRecord(record, onDeleteRecord); 
    }

    init();
  });
