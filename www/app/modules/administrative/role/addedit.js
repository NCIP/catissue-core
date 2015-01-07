
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function($scope, $state, Role, PvManager) {
    
    var init = function() {
      $scope.role = new Role();
      $scope.role.acl = []; 
      $scope.role.addResource($scope.role.newResource($scope.role));
      $scope.resources =  PvManager.getPvs('resources');
      $scope.privileges = PvManager.getPvs('privileges');
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
    };
    
    init();
  });
