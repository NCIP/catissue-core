
angular.module('os.administrative.role.detail', ['os.administrative.models'])
  .controller('RoleDetailCtrl', function($scope, $q, role, PvManager) {
    $scope.role = role;
    $scope.permissions = PvManager.getPvs('permissions');

    $scope.editRole = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
