
angular.module('os.biospecimen.extensions.list', ['os.biospecimen.models'])
  .controller('FormsListCtrl', function($scope, $modal, Form, Alerts) {
    function init() {
      $scope.forms = $scope.object.getForms();
      $scope.records = $scope.object.getRecords();
    }

    function deleteRecord(record) {
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

    $scope.deleteRecord = function(record) {
      console.log(record);
      var modalInstance = $modal.open({
        templateUrl: 'delete_extension_record.html',
        controller: function($scope, $modalInstance) {
          $scope.record = record;

          $scope.yes = function() {
            $modalInstance.close(true);
          }

          $scope.no = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });

      modalInstance.result.then(
        function() {
          deleteRecord(record);
        }
      );
    };


    init();
  });
