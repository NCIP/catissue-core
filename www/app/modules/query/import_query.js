
angular.module('os.query.importquery', ['os.query.models'])
  .controller('ImportQueryCtrl', function($scope, $modalInstance, $sce, SavedQuery, Alerts) {
    $scope.queryImporter = {};
    $scope.importQueryUrl = $sce.trustAsResourceUrl(SavedQuery.getImportQueryDefUrl());

    $scope.import = function() {
      $scope.queryImporter.submit().then(
        function(importedQuery) {
          $modalInstance.close(importedQuery);
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }
  }
);
