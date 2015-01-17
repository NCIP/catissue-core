angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    User, Site, Institute, Role, PvManager, Alerts) {
 
    var init = function() {
      $scope.user = new User();
      $scope.siteCpMaps = {};
      $scope.user.superAdmin = false;
      $scope.addMorePermissions = true;
      $scope.addPermission();
      loadPvs();
    };
    
    function loadPvs() {
      $scope.domains = PvManager.getPvs('domains');
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0, 0, "All");
      
      $scope.institutes = [];      
      Institute.list().then(
        function(instituteList) {
          angular.forEach(instituteList,
            function(institute) {
              $scope.institutes.push(institute.name);
            }
          )
        }
      );
  
      $scope.roles = [];
      Role.list().then(
        function(roleList) {
          angular.forEach(roleList,
            function(role) {
              $scope.roles.push(role.name);
            }
          )
        }
      );
    }
    
    $scope.setForm = function(form) {
      if(form.$name == "userForm") {
        $scope.userForm = form;
      }
      else {
        $scope.userRoleForm = form;
      }
    };
  
    var validation = function(form) {
      if(!form.$valid) {
        Alerts.error("There are validation errors as highlighted below. Please correct them");
        return false;
      }
      return true
    }
    
    $scope.validateProfile = function() {
      $scope.userForm.submitted = true;
      var result = validation($scope.userForm)
      return result;
    };
    
    $scope.validatePermissions = function() {
      $scope.userRoleForm.submitted = true;
      var result = validation($scope.userRoleForm);
      return result;
    };
      
    $scope.addPermission = function() { 
      $scope.user.addPermission($scope.user.newPermission());
    };
    
    $scope.removePermission = function(userCPRole) {  
      if(userCPRole.site == "All" && userCPRole.cpTitle == "All") {
        $scope.addMorePermissions = true;
      }
      $scope.user.removePermission($scope.user.userCPRoles.indexOf(userCPRole));
    };
   
    $scope.loadDepartments = function(instituteName) {
      $scope.user.deptName = undefined;
      $scope.departments = [];
      
      Institute.getDepartments(instituteName).then(
        function(deptList) {
          angular.forEach(deptList,
            function(department) {
              $scope.departments.push(department.name);
            }
          )
        }
      );
    };
    
    $scope.getCps = function(newCPRole) {
      newCPRole.cpTitle = undefined;
      if($scope.siteCpMaps[newCPRole.site] == undefined && newCPRole.site == 'All') {
        $scope.siteCpMaps[newCPRole.site] = [{id: -1, shortTitle: 'All'}];
        $scope.addMorePermissions = false;
      }
      else {
        Site.getCps(newCPRole.site).then(function(cpList) {
          $scope.siteCpMaps[newCPRole.site] = cpList;
          $scope.addMorePermissions = true;
        });
      }
    }
    
    $scope.updateUserCPRoleList = function(newCPRole) {
      if(newCPRole.cpTitle == "All" && newCPRole.site == "All") {
        $scope.user.userCPRoles = [];
        $scope.user.userCPRoles[0] = {site:newCPRole.site, cpTitle:newCPRole.cpTitle, roleName:newCPRole.roleName};
        $scope.addMorePermissions = false;
      }
      else if(newCPRole.cpTitle == "All") {
        var currCPRoleIndex = $scope.user.userCPRoles.indexOf(newCPRole);
        for(var i= $scope.user.userCPRoles.length - 1; i >= 0; i--) {
          if(i != currCPRoleIndex && newCPRole.site == $scope.user.userCPRoles[i].site) {
            $scope.user.removePermission($scope.user.userCPRoles[i]); 
          }
        }
      }
    }
               
    $scope.createUser = function() {    
      if (!$scope.user.superAdmin) {
        if(!$scope.validatePermissions()) {
          return false;
        }
      }
      
      $scope.user.userCPRoles = [];//TODO remove it after completion of backend code
      delete $scope.user.isSuperAdmin;//TODO remove it after completion of backend code
      
      var user = angular.copy($scope.user);
      user.$saveOrUpdate().then(
        function(savedUser) {
          $state.go('user-detail.overview', {userId: savedUser.id});
        }
      );
    };
     
    init();
  });
