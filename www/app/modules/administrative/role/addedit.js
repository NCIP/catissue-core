
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function($scope, $state, Role, PvManager) {
    
    var init = function() {
      $scope.role = new Role();
      $scope.role.acl = [{}];
      
      PvManager.loadPvs($scope, 'resources');
      PvManager.loadPvs($scope, 'privileges');
    }

    $scope.addResource = function() {
      $scope.role.acl.push({privileges: []});
    };
  
    $scope.removeResource = function(index) {
      $scope.role.acl.splice(index, 1);
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
