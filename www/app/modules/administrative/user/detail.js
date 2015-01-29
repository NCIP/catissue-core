
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function($scope, $q, user, PvManager) {
    $scope.user = user;
    $scope.domains = PvManager.getPvs('domains');
    $scope.sites = PvManager.getSites();

    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
