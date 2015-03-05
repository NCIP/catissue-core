
angular.module('os.administrative.user.roles', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('UserRolesCtrl', function(
    $scope, $translate, $filter,
    user, userRoles,
    PvManager, CollectionProtocol, Role) {
    
    function init() {
      $scope.currentRole = {};
      $scope.userRoles = userRoles;
      $scope.addMode = false;
      $scope.all = $translate.instant('user.role.all');

      loadPvs();
      $scope.userRolesList = getUserRolesList($scope.userRoles);
    }

    function loadPvs() {
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0, 0, $scope.all);
      setSitePvs();

      $scope.roles = [];
      Role.list().then(
        function(roleList) {
          angular.forEach(roleList, function(role) {
            $scope.roles.push(role.name);
          });
        }
      );
    }

    $scope.showAddRole = function() {
      $scope.addMode = true;
      $scope.currentRole = user.newRole();
      $scope.cps = [];
      setSitePvs();
    }

    $scope.showEditRole = function(role) {
      $scope.loadCps(role.site);
      setSitePvs();

      $scope.currentRole = angular.copy(role);
      //If site or cp not exist means "All Sites" or "All CPs" is selected.
      if (!$scope.currentRole.site) {
        $scope.currentRole.site = $scope.all;
      }
      if (!$scope.currentRole.cp) {
        $scope.currentRole.cp = $scope.all;
      }

      $scope.addMode = false;
    }

    $scope.saveOrUpdateRole = function() {
      user.saveOrUpdateRole($scope.currentRole).then(
        function(role) {
          updateUserRolesJson(role);
          $scope.userRolesList = getUserRolesList($scope.userRoles);

          $scope.currentRole = {};
          $scope.addMode = false;
        }
      );
    }

    $scope.removeRole = function(role) {
      //TODO: Call REST API to remove role.
      if (role.site == $scope.all) {
        delete role.site;
      }
      if (role.cp == $scope.all) {
        delete role.cp;
      }


      deleteRole(role)
      $scope.userRolesList = getUserRolesList($scope.userRoles);
    }

    $scope.revertEdit = function() {
      $scope.addMode = false;
      $scope.currentRole = {};
    }

    $scope.loadCps = function(site) {
      var cpsToRemove = [];

      angular.forEach($scope.userRoles.allSites, function(role) {
        cpsToRemove.push(role.cp);
      });

      angular.forEach($scope.userRoles.siteCpRoles, function(role) {
        cpsToRemove.push(role.cp);
      });

      var promise = (site == $scope.all) ? CollectionProtocol.query() : CollectionProtocol.listCpsForSite(site);
      promise.then(
        function(result) {
          $scope.cps = [];
          angular.forEach(result, function(cp) {
            if (cpsToRemove.indexOf(cp.shortTitle) == -1 || $scope.currentRole.cp == cp.shortTitle) {
              $scope.cps.push(cp.shortTitle);
            }
          });

          if (result.length == $scope.cps.length) {
            $scope.cps.splice(0, 0, $scope.all);
          }
      });
    }

    function updateUserRolesJson(userRole) {
      // In Case of Update, remove old role from json.
      if ($scope.currentRole.id) {
        deleteRole(userRole);
      }
      var roleType = getRoleType(userRole);
      var roles = $scope.userRoles[roleType] || [];
      roles.push(userRole);
      $scope.userRoles[roleType] = roles;
    }

    function deleteRole(userRole) {
      var roleType = getRoleType(userRole);
      var roles = $scope.userRoles[roleType];
      for (var i = roles.length - 1; i >= 0; i--) {
        if (roles[i].id == userRole.id) {
          roles.splice(i, 1);
        }
      }
    }

    function getRoleType(userRole) {
      var roleType = undefined;
      if (!userRole.site && !userRole.cp) {
        roleType = 'all';
      } else if (!userRole.site) {
        roleType = 'allSites';
      } else if (!userRole.cp) {
        roleType = 'allCps';
      } else {
        roleType = 'siteCpRoles';
      }

      return roleType;
    }

    function getUserRolesList(userRoles) {
      var userRolesList = [];
      angular.forEach(userRoles, function(value) {
        angular.forEach(value, function(role) {
          userRolesList.push(role);
        })
      });

      var sortedUserRoles = userRolesList.sort(function(role1, role2) {
        return role1.id - role2.id;
      });

      return sortedUserRoles;
    }

    function setSitePvs() {
      // If all Sites role is exist then can add roles only on all site.
      // Hence show only "All Sites" in dropdown.
      if ($scope.userRoles.allSites &&  $scope.userRoles.allSites.length > 0) {
        $scope.sitePvs = [$scope.all];
        return;
      }

      // If role exist on individual site then hide "All Sites" option.
      $scope.sitePvs = angular.copy($scope.sites);
      if (hasRoleOnIndividualSite()) {
        $scope.sitePvs.splice(0, 1);
      }

      // Hide sites for which role is selected for all cps.
      angular.forEach($scope.userRoles.allCps, function(role) {
        var idx = $scope.sitePvs.indexOf(role.site);
        if (idx != -1) {
          $scope.sitePvs.splice(idx, 1);
        }
      });
    }

    function hasRoleOnIndividualSite() {
      return ($scope.userRoles.allCps && $scope.userRoles.allCps.length > 0) ||
        ($scope.userRoles.siteCpRoles && $scope.userRoles.siteCpRoles.length > 0) ;
    }

    $scope.allowAddRoles = function() {
      return !$scope.userRoles.all || $scope.userRoles.all.length == 0;
    }

    init();
  });
