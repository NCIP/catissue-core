
angular.module('os.biospecimen.cp.detail', ['os.biospecimen.models'])
  .controller('CpDetailCtrl', function($scope, $q, cp, CollectionProtocol, PvManager, DeleteUtil) {
    $scope.cp = cp;

    $scope.downloadUri = CollectionProtocol.url() + cp.id + '/definition';

    function init() {
      $scope.sites = PvManager.getSites();
    }

    $scope.editCp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteCp = function() {
      DeleteUtil.delete($scope.cp, {onDeleteState: 'cp-list'});
    }

    init();
  });
