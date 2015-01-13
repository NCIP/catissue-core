angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    User, Site, Institute, Role, PvManager, Alerts) {
 
    var init = function() {
      $scope.user = new User();
      $scope.addPermission();
      $scope.siteCpMaps = {};
      $scope.addMorePermissions = true;
     
      $scope.domains = PvManager.getPvs('domains');
      
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0, 0, "All");
      
      Institute.list().then(function(institutes) {
        $scope.institutes = institutes;
      });
         
      Role.list().then(function(roles) {
        $scope.roles = roles;
      });
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
    
    $scope.resetDepartmentName = function() {
      $scope.user.deptName = undefined;
    }
    
    $scope.getDepartments = function(instituteName) {
      Institute.getDepartments(instituteName).then(function(result) {
        $scope.departments = result;
      });
    };

    $scope.setSuperAdminRole = function() {
      if($scope.user.isSuperAdmin) {
        $scope.user.userCPRoles[0] = {site:"All", cpTitle:"All", roleName:"Super Administrator"};
        $scope.addMorePermissions = false;
      }
      else {
        $scope.user.userCPRoles[0] = {site:'', cpTitle:'', roleName:''};
        $scope.addMorePermissions = true;
        $scope.userRoleForm.submitted = false;
      }
    }
    
    $scope.getCps = function(newCPRole) {
      newCPRole.cpTitle = undefined;
      $scope.addMorePermissions = true;
      
      if($scope.siteCpMaps[newCPRole.site] == undefined && newCPRole.site == 'All') {
        $scope.siteCpMaps[newCPRole.site] = [{id: -1, shortTitle: 'All'}];
        $scope.addMorePermissions = false;
      }
      else if($scope.siteCpMaps[newCPRole.site] == undefined) {
        Site.getCps(newCPRole.site).then(function(cpList) {
          $scope.siteCpMaps[newCPRole.site] = cpList;
          $scope.addMorePermissions = true;
        });
      }
      else if(newCPRole.site == 'All') {
        $scope.addMorePermissions = false;
      }
    }
    
    $scope.checkExistingUserCPRoles = function(newCPRole) {
      if(newCPRole.cpTitle == "All" && newCPRole.site == "All") {
        for (var i= $scope.user.userCPRoles.length - 1; i >= 0; i--) {
          var index = $scope.user.userCPRoles.indexOf($scope.user.userCPRoles[i]);
          $scope.user.userCPRoles.splice(index,1);
        }
        $scope.user.userCPRoles[0] = {site:newCPRole.site, cpTitle:newCPRole.cpTitle, roleName:newCPRole.roleName};
        $scope.addMorePermissions = false;
      }
      else if(newCPRole.cpTitle == "All") {
        var currCPRoleIndex = $scope.user.userCPRoles.indexOf(newCPRole);
        for(var i= $scope.user.userCPRoles.length - 1; i >= 0; i--) {
          if(i != currCPRoleIndex && newCPRole.site == $scope.user.userCPRoles[i].site) {
            var index = $scope.user.userCPRoles.indexOf($scope.user.userCPRoles[i]);
            $scope.user.userCPRoles.splice(index,1)
          }
        }
      }
    }
               
    $scope.createUser = function() {    
      if(!$scope.validatePermissions()) {
        return false;
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
