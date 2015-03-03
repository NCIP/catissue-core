
angular.module('os.biospecimen.cp.import', ['os.biospecimen.models'])
  .controller('CpImportCtrl', function($scope, $state, CollectionProtocol, FileSvc) {
    $scope.import = {};

    $scope.importCp = function() {
      if (!$scope.import.cpDef) {
        return;
      }

      var url = CollectionProtocol.url() + 'definition';
      FileSvc.upload(url, $scope.import.cpDef).then(
        function(result) {
          $state.go('cp-detail.overview', {cpId: result.data.id});
        }
      );
    }
  });
