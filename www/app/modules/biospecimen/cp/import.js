
angular.module('os.biospecimen.cp.import', ['os.biospecimen.models'])
  .controller('CpImportCtrl', function($scope, $state, $sce, CollectionProtocol) {
    $scope.cpImporter = {};
    $scope.cpImportUrl = $sce.trustAsResourceUrl(CollectionProtocol.url() + 'definition');
    

    $scope.importCp = function() {
      $scope.cpImporter.submit().then(
        function(importedCp) {
          $state.go('cp-detail.overview', {cpId: importedCp.id});
        }
      );
    }
  });
