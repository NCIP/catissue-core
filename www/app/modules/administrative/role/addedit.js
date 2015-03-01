
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function($scope, $state, role, PvManager) {
    
    var init = function() {
      $scope.role = role;
      if (!role.acls) {
        $scope.addResource();
      }

      $scope.resources =  PvManager.getPvs('resources');
      $scope.permissions = PvManager.getPvs('permissions');
    }

    $scope.addResource = function() {
      $scope.role.addResource($scope.role.newResource($scope.role));
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

    $scope.togglePermission = function(acl, permission) {
      var idx = acl.permissions.indexOf(permission);
      if (idx > -1) {
        acl.permissions.splice(idx, 1);
        // On deselection of create , deselect read and update permissions
        if (permission == $scope.permissions.create) {
          acl.permissions.splice(acl.permissions.indexOf($scope.permissions.read), 1);
          acl.permissions.splice(acl.permissions.indexOf($scope.permissions.update), 1);
        }
      } else {
        acl.permissions.push(permission);
        // On selection of create , select read and update permissions
        if (permission == $scope.permissions.create) {
          if (acl.permissions.indexOf($scope.permissions.update) == -1) {
            acl.permissions.push($scope.permissions.update);
          }
          if (acl.permissions.indexOf($scope.permissions.read) == -1) {
            acl.permissions.push($scope.permissions.read);
          }
        }
      }
    }

    init();
  });
