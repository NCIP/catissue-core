
angular.module('os.administrative.user.permissions', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('UserPermissionsCtrl', function($scope, user, PvManager, CollectionProtocol, Role, User) {

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
      $scope.sites.splice(0, 0, "All");

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
      if(userCPRole.site == "All" && userCPRole.cp == "All") {
        $scope.addMorePermissions = true;
      }
      $scope.user.removePermission(userCPRole);
    };

    $scope.getCps = function(site) {
      // If All Site is selected then CP dropdown should show only "All" protocols.
      if(site == 'All') {
        $scope.siteCpMaps[site] = [{id: -1, shortTitle: 'All'}];
      }
      else {
        CollectionProtocol.getCps(site).then(function(cpList) {
          $scope.siteCpMaps[site] = cpList;
        });
        $scope.addMorePermissions = true;
      }
    }

    $scope.updatePermissions = function(newCPRole) {
      if(newCPRole.cp == "All" && newCPRole.site == "All") {
        $scope.user.userCPRoles = [{site:newCPRole.site, cp:newCPRole.cp, roleName:newCPRole.roleName}];
        $scope.addMorePermissions = false;
      }
      else if(newCPRole.cp == "All") {
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