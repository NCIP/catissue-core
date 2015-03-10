
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function(
    $scope, $state, $filter, role,
    Permission, Resource) {
    
    var init = function() {
      $scope.role = role;
      loadPvs();
      getSelectedPermissions($scope.role);
      if (!role.acls) {
        $scope.addResource();
      }
    }

    function loadPvs() {
       $scope.resources =  Resource.list();
       var permissionsOrder =  Permission.getOrderedPermissions();
       var unsortedPermissions = Permission.list();
       $scope.sortedPermissions = [];
       angular.forEach(unsortedPermissions, function(permission) {
         $scope.sortedPermissions[permissionsOrder.indexOf(permission)] = {name: permission, selected: false, disabled: false};
       });
    }

    function getSelectedPermissions(role) {
      angular.forEach(role.acls, function(acl) {
        var permissions = $scope.sortedPermissions.map(
          function(permission) {
            var selected = acl.permissions.indexOf(permission.name) != -1;
            var disabled = false;
            if (permission.name == 'Read' || permission.name == 'Update') {
              disabled = acl.permissions.indexOf('Create') != -1;
            }

            return {name: permission.name, selected: selected, disabled: disabled}
          }
        );
        acl.permissions = permissions;
      });
    }

    $scope.addResource = function() {
      $scope.role.addResource($scope.role.newResource(angular.copy($scope.sortedPermissions)));
    };

    $scope.removeResource = function(index) {
      $scope.role.removeResource(index);
      if ($scope.role.acls.length == 0) {
        $scope.addResource();
      }
    };
  
    $scope.saveRole = function() {
      var role = angular.copy($scope.role);
      role.$saveOrUpdate().then(
        function(savedRole) {
          $state.go('role-detail.overview', {roleId: savedRole.id});
        }
      );
      $state.go('role-detail.overview', {roleId: 1}); // Temporary to show overview page on save
    };

    $scope.setPermissions = function(permission, permissions) {
      if (permission.name != 'Create') {
        return;
      }

      angular.forEach(permissions, function(p) {
        if (p.name == 'Read' || p.name == 'Update') {
          p.selected = permission.selected;
          p.disabled = true;
        }
      });
    }

    init();
  });
