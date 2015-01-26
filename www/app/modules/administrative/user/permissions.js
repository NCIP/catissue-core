
angular.module('os.administrative.user.permissions', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('UserPermissionsCtrl', function($scope, $translate, user, PvManager, CollectionProtocol, Role, User) {
    var all = $translate.instant('user.all');

    function init() {
      $scope.siteCpMaps = {};
      $scope.addMorePermissions = true;
      if(user.userCPRoles.length == 0) {
        user.addPermission(user.newPermission());
      }

      loadPvs();
    }

    function loadPvs() {
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0, 0, all);

      Role.list().then(
        function(roleList) {
          $scope.roles = [];
          angular.forEach(roleList,
            function(role) {
              $scope.roles.push(role.name);
            }
          )
        }
      );
    }

    $scope.addPermission = function() {
      user.addPermission(user.newPermission());
    }

    $scope.removePermission = function(userCPRole) {
      if(userCPRole.site == all && userCPRole.cp == all) {
        $scope.addMorePermissions = true;
      }
      $scope.user.removePermission(userCPRole);
    };

    $scope.getCps = function(site) {
      // If All Site is selected then CP dropdown should show only "All" protocols.
      if(site == all) {
        $scope.siteCpMaps[site] = [{id: -1, shortTitle: all}];
      }
      else {
        CollectionProtocol.getCps(site).then(function(cpList) {
          $scope.siteCpMaps[site] = cpList;
        });
        $scope.addMorePermissions = true;
      }
    }

    $scope.updatePermissions = function(newCPRole) {
      if(newCPRole.cp == all && newCPRole.site == all) {
        $scope.user.userCPRoles = [{site:newCPRole.site, cp:newCPRole.cp, roleName:newCPRole.roleName}];
        $scope.addMorePermissions = false;
      }
      else if(newCPRole.cp == all) {
        //If "All" CP is selected then remove already selected permissions for same site on another cps.
        for(var i= $scope.user.userCPRoles.length - 1; i >= 0; i--) {
          if(newCPRole.site == $scope.user.userCPRoles[i].site && $scope.user.userCPRoles[i].cp != "All") {
            $scope.user.removePermission($scope.user.userCPRoles[i]);
          }
        }
      }
    }

    $scope.save = function() {
      user.$update().then(
        function(savedUser) {
          $state.go('user-detail.overview', {userId: savedUser.id});
        }
      );
    }

    init();

  });