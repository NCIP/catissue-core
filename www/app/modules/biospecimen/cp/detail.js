
angular.module('os.biospecimen.cp.detail', ['os.biospecimen.models'])
  .controller('CpDetailCtrl', function($scope, $state, $q, cp, CollectionProtocol) {
    $scope.cp = cp;

    $scope.downloadUri = CollectionProtocol.url() + cp.id + '/definition';

    $scope.editCp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
