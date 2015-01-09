angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    User, Institute, Role, PvManager, Alerts) {
 
    var init = function() {
      $scope.user = new User();
      $scope.user.userCPRoles = [];
      $scope.user.addPermission($scope.user.newPermission());
     
      $scope.domains = PvManager.getPvs('domains');
      
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0,0,"All");
      
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
  
    $scope.validateProfile = function () {
      $scope.userForm.submitted = true;
      
      if(!$scope.userForm.$valid) {
        Alerts.error("There are validation errors as highlighted below. Please correct them");
        return false;
      }
      return true;
    };
    
    $scope.validatePermissions = function () {
      $scope.userRoleForm.submitted = true;
      
      if(!$scope.userRoleForm.$valid) {
        Alerts.error("There are validation errors as highlighted below. Please correct them");
        return false;
      }
      return true;
    };
      
    $scope.addPermission = function() { 
      $scope.user.addPermission($scope.user.newPermission());
    };
    
    $scope.removePermission = function(userCPRole) {  
      $scope.user.removePermission($scope.user.userCPRoles.indexOf(userCPRole));
    };
    
    $scope.resetDepartmentName = function () {
      $scope.user.deptName = undefined;
    }
    
    $scope.getDepartments = function(instituteName) {
      Institute.getDepartments(instituteName).then(function(result) {
        $scope.departments = result;
      });
    };

    $scope.addCp = function (cpShortTitle) {
      $scope.cps.push(cpShortTitle);
    }
    
    $scope.getCps = function (siteName) {
      $scope.cps = [];
      if (siteName == "All") {
        $scope.cps.splice(0,0,"All");
      }
      else {
        Site.getCps(siteName).then(function(cpList) {
          angular.forEach(cpList, function(cp) {
            $scope.cps.push(cp.shortTitle);
          });
          $scope.cps.splice(0,0,"All");
     
          $scope.updateCpList(siteName);
        })
      }
    }
    
    $scope.updateCpList = function(siteName) {
      angular.forEach($scope.user.userCPRoles, 
        function(userCPRole) {  
          if(userCPRole.site == siteName && userCPRole.cpTitle != "All") {
            var index = $scope.cps.indexOf(userCPRole.cpTitle);
            $scope.cps.splice(index,1);
          }
          else if(userCPRole.cpTitle == "All" && userCPRole.site == siteName) {
            $scope.cps = [];
          }
        }
      );
    }
        
    $scope.checkExistingUserCPRoles = function (newCPRole) {
      if(newCPRole.cpTitle == "All") {
        for (var i= $scope.user.userCPRoles.length - 1; i >= 0; i--) {
          if($scope.user.userCPRoles[i].site == newCPRole.site && $scope.user.userCPRoles[i].cpTitle != "All") {
            var index = $scope.user.userCPRoles.indexOf($scope.user.userCPRoles[i]);
            $scope.user.userCPRoles.splice(index,1);
          }
        }
      }
    }
    
    $scope.createUser = function() {    
      if(!$scope.validatePermissions()) {
        return false;
      }
      $scope.user.userCPRoles = [];
      var user = angular.copy($scope.user);
      user.$saveOrUpdate().then(
        function(savedUser) {
          $state.go('user-detail.overview', {userId: savedUser.id});
        }
      );
    };
     
    init();
  });
