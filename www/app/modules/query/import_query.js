
angular.module('os.query.importquery', ['os.query.models'])
  .controller('ImportQueryCtrl', function($scope, $modalInstance, $sce, SavedQuery, Alerts) {
    $scope.queryImporter = {};
    $scope.importQueryUrl = $sce.trustAsResourceUrl(SavedQuery.getImportQueryDefUrl());

    $scope.import = function() {
      $scope.queryImporter.submit().then(
        function(resp) {
          $modalInstance.close(resp.result);
        },

        function() {
          Alerts.error('queries.import_failed');
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }
  }
);
