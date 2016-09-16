
angular.module('os.administrative.user.roles', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('UserRolesCtrl', function(
    $scope, $translate, $filter,
    user, userRoles, currentUser, currentUserInstitute,
    CollectionProtocol, Site, Role, AuthorizationService) {
    
    function init() {
      $scope.currentRole = {};
      $scope.userRoles = {};
      createUserRolesJson(userRoles);

      $scope.addMode = false;
      $scope.all = $translate.instant('user.role.all');
      loadPvs();
      $scope.userRolesList = getUserRolesList($scope.userRoles);
    }

    function createUserRolesJson(userRoles) {
      angular.forEach(userRoles, function(userRole) {
        updateUserRoles(userRole);
      });
    }

    function loadPvs() {
      $scope.roles = [];
      Role.query().then(
        function(result) {
          $scope.roles = result.map(function(r) { return r.name; });
        }
      );
    }

    $scope.loadSites = function(searchTerm) {
      var query = undefined;
      if ($scope.currentUser.admin) {
        query = Site.listForInstitute($scope.user.instituteName, false, searchTerm);
      } else {
        query = Site.listForUsers('Update', searchTerm);
      }

      $scope.sites = [$scope.all];
      query.then(function(sites) {
        $scope.sites = $scope.sites.concat(sites);
        setSitePvs();
      });
    }
    
    $scope.showAddRole = function() {
      $scope.addMode = true;
      $scope.currentRole = user.newRole();
      $scope.cps = [];
    }

    $scope.showEditRole = function(role) {
      $scope.loadCps(role.site);
      setSitePvs();

      $scope.currentRole = angular.copy(role);
      //If site or cp not exist means "All Sites" or "All CPs" is selected.
      if (!$scope.currentRole.site) {
        $scope.currentRole.site = $scope.all;
      }
      if (!$scope.currentRole.collectionProtocol) {
        $scope.currentRole.collectionProtocol = $scope.all;
      }

      $scope.addMode = false;
    }

    $scope.saveOrUpdateRole = function() {
      $scope.currentRole.$saveOrUpdate().then(
        function(role) {
          updateUserRoles(role);
          $scope.userRolesList = getUserRolesList($scope.userRoles);

          $scope.currentRole = {};
          $scope.addMode = false;
        }
      );
    }

    $scope.removeRole = function(role) {
      role.$remove().then(function(role) {
        deleteRole($scope.userRoles, role);
        $scope.userRolesList = getUserRolesList($scope.userRoles);
      })
    }

    $scope.revertEdit = function() {
      $scope.addMode = false;
      $scope.currentRole = {};
    }

    $scope.loadCps = function(site) {
      var cpsToRemove = [];

      angular.forEach($scope.userRoles.allSites, function(role) {
        cpsToRemove.push(role.collectionProtocol);
      });

      angular.forEach($scope.userRoles.siteCpRoles, function(role) {
        cpsToRemove.push(role.collectionProtocol);
      });

      var cpListOpts = {detailedList: false};
      if (site != $scope.all) {
        cpListOpts["repositoryName"] = site;
      }

      CollectionProtocol.list(cpListOpts).then(
        function(result) {
          $scope.cps = [];
          angular.forEach(result, function(cp) {
            if (cpsToRemove.indexOf(cp.shortTitle) == -1 || $scope.currentRole.collectionProtocol == cp.shortTitle) {
              $scope.cps.push(cp.shortTitle);
            }
          });

          if (result.length == $scope.cps.length) {
            $scope.cps.splice(0, 0, $scope.all);
          }
      });
    }

    function updateUserRoles(userRole) {
      // In Case of Update, remove old role from json.
      if (userRole.id) {
        for (var key in $scope.userRoles) {
          var deleted = deleteRoleById($scope.userRoles[key], userRole);
          if (deleted) {
            break;
          }
        }
      }

      var roleType = getRoleType(userRole);
      var roles = $scope.userRoles[roleType] || [];
      roles.push(formatRoleToUiModel(userRole));
      $scope.userRoles[roleType] = roles;
    }

    function deleteRole(userRoles, role) {
      var roleType = getRoleType(role);
      var roles = userRoles[roleType];
      deleteRoleById(roles, role);
    }

    function deleteRoleById(roles, role) {
      for (var i = roles.length - 1; i >= 0; i--) {
        if (roles[i].id == role.id) {
          roles.splice(i, 1);
          return true;
        }
      }

      return false;
    }

    function formatRoleToUiModel(userRole) {
      userRole.role = userRole.role.name;
      userRole.site = userRole.site ? userRole.site.name : $scope.all;
      userRole.collectionProtocol = userRole.collectionProtocol ? userRole.collectionProtocol.shortTitle : $scope.all;
      return userRole;
    }

    function getRoleType(userRole) {
      var roleType = undefined;
      if (!userRole.site && !userRole.collectionProtocol) {
        roleType = 'all';
      } else if (!userRole.site) {
        roleType = 'allSites';
      } else if (!userRole.collectionProtocol) {
        roleType = 'allCps';
      } else {
        roleType = 'siteCpRoles';
      }

      return roleType;
    }

    function getUserRolesList(userRoles) {
      var userRolesList = [];
      var sameInstitute = (currentUserInstitute.name == user.instituteName);
      angular.forEach(userRoles, function(roles) {
        angular.forEach(roles, function(role) {
          role.isUpdateAllowed = isRoleUpdateAllowed(role, sameInstitute);
          userRolesList.push(role);
        })
      });
          
      var sortedUserRoles = userRolesList.sort(function(role1, role2) {
        return role1.id - role2.id;
      });
      
      return sortedUserRoles;
    }
    
    function isRoleUpdateAllowed(role, sameInstitute) {
      if (currentUser.admin) {
        return true;
      }
      
      var updateOpts = $scope.userResource.updateOpts;
      if (role.site && role.site != $scope.all) {
        return AuthorizationService.isAllowed(angular.extend({sites: [role.site]}, updateOpts));
      } else if (sameInstitute) {
        return AuthorizationService.isAllowed(updateOpts);
      }
      
      return false;
    }

    function setSitePvs() {
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
