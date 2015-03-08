
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function($scope, $state, $filter, role, PvManager) {
    
    var init = function() {
      $scope.role = role;
      loadPvs();
      populateUIModel($scope.role);
      if (!role.acls) {
        $scope.addResource();
      }
    }

    function loadPvs() {
       $scope.resources =  PvManager.getPvs('resources');
       var permissionPvs = PvManager.getPvs('permissions');
       $scope.permissionPvs = orderPermissionPvs(permissionPvs);
    }

    // Set Order of Permission Pvs as Read, Create, Update, Delete to show on UI
    function orderPermissionPvs(permissionPvs) {
      var permissions = [];
      if (permissionPvs.indexOf('Read') != -1) {
        permissions.push('Read');
      }
      if (permissionPvs.indexOf('Create') != -1) {
        permissions.push('Create');
      }
      if (permissionPvs.indexOf('Update') != -1) {
        permissions.push('Update');
      }
      if (permissionPvs.indexOf('Delete') != -1) {
        permissions.push('Delete');
      }

      return permissions;
    }

    function populateUIModel(role) {
      angular.forEach(role.acls, function(acl) {
        var permissions = $scope.permissionPvs.map(
          function(permission) {
            var selected = acl.permissions.indexOf(permission) != -1;
            return {name: permission, selected: selected};
          }
        );
        acl.permissions = permissions;
      });
    }

    $scope.addResource = function() {
      $scope.role.addResource($scope.role.newResource($scope.permissionPvs));
    };

    $scope.removeResource = function(index) {
      $scope.role.removeResource(index);
      if ($scope.role.acls.length == 0) {
        $scope.addResource();
      }
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

    $scope.setPermissions = function(permission, permissions) {
      if (permission.name == 'Create') {
        angular.forEach(permissions, function(p) {
          if (p.name == 'Read' || p.name == 'Update') {
            p.selected = permission.selected;
          }
        });
      }
    }

    $scope.isDisable = function(permission, permissions) {
      if (permission == 'Read' || permission.name == 'Update') {
        for (var i = 0 ; i < permissions.length; i++) {
          if (permissions[i].name == 'Create') {
            return permissions[i].selected;
          }
        }
      }
    }

    init();
  });
