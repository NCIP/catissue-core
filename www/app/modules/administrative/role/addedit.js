
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function($scope, $state, role, PvManager) {
    
    var init = function() {
      $scope.role = role;
      if (!role.acls) {
        $scope.role.addResource($scope.role.newResource($scope.role));
      }
      $scope.resources =  PvManager.getPvs('resources');
      $scope.permissions = PvManager.getPvs('permissions');
    }

    $scope.addResource = function() {
      $scope.role.addResource($scope.role.newResource($scope.role));
    };

    $scope.removeResource = function(index) {
      $scope.role.removeResource(index);
    };
  
    $scope.createRole = function() {
      var role = angular.copy($scope.role);
      role.$saveOrUpdate().then(
        function(savedRole) {
          $state.go('role-detail.overview', {roleId: savedRole.id});
        }
      );
      $state.go('role-detail.overview', {roleId: 1}); // Temporary to show overview page on save
    };
    
    init();
  });
