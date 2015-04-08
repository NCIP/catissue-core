angular.module('os.administrative.role.list', ['os.administrative.models'])
  .controller('RoleListCtrl', function($scope, $state, Role) {
    
    var loadRoles = function() {
      Role.query().then(function(result) {
        $scope.roles = result;
      });
    };

    $scope.showRoleOverview = function(role) {
      $state.go('role-detail.overview', {roleId:role.id});
    };    
   
    loadRoles();
  });
