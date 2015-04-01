
angular.module('os.administrative.role.detail', ['os.administrative.models'])
  .controller('RoleDetailCtrl', function($scope, $q, $translate, role, PvManager) {
    $scope.role = role;

    angular.forEach($scope.role.acl, function(ac) {
        ac.resource = {
          displayName: $translate.instant('role.resources.' + ac.resourceName),
          value: ac.resourceName
        };

        ac.operationNames = ac.operations.map(function(operation) {
          return operation.operationName;
        });
    });

    $scope.editRole = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
