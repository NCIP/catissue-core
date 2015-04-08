
angular.module('os.biospecimen.extensions.list', ['os.biospecimen.models'])
  .controller('FormsListCtrl', function($scope, Form, Alerts) {
    function init() {
      $scope.forms = $scope.object.getForms();
      $scope.records = $scope.object.getRecords();
    }

    $scope.deleteRecord = function(record) {
      Form.deleteRecord(record.formId, record.recordId).then(
        function() {
          var records = $scope.records;
          for (var i = 0; i < records.length; ++i) {
            if (records[i] == record) {
              records.splice(i, 1);
              break;
            }
          }

          Alerts.success('extensions.record_deleted');
        }
      );
    }

    init();
  });
