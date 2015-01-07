angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    User, Institute, Site, CollectionProtocol, Role, PvManager, Alerts) {
 
    var init = function() {
      $scope.user = new User();
      $scope.user.userCPRoles = [];
      $scope.user.addPermission($scope.user.newPermission());
      
      $scope.domains = PvManager.getPvs('domains');
      
      Institute.list().then(function(institutes) {
        $scope.institutes = institutes;
      });
         
      $scope.sites = PvManager.getSites();
      $scope.sites.splice(0,0,"All");
      
      CollectionProtocol.query().then(function(cps) {
        $scope.cps = cps;
        $scope.cps.splice(0,0,{"id": -1, "shortTitle": "All"});
      });
    
      Role.list().then(function(roles) {
        $scope.roles = roles;
      });
    }
    
    $scope.setForm = function(form) {
      $scope.userForm = form;
    };
  
    $scope.validateProfile = function () {
      $scope.userForm.submitted = true;
      
      if(!$scope.userForm.$valid) {
        Alerts.error("There are validation errors as highlighted below. Please correct them");
        return false;
      }
      return true;
    };
      
    $scope.addPermission = function() { 
      $scope.user.addPermission($scope.user.newPermission());
    };
    
    $scope.removePermission = function(index) {  
      $scope.user.removePermission(index);
    };
    
    $scope.resetDepartmentName = function () {
      $scope.user.deptName = undefined;
    }
    
    $scope.getDepartments = function(instituteName) {
      Institute.getDepartments(instituteName).then(function(result) {
        $scope.departments = result;
      });
    };

    $scope.updateCpList = function(item) {
      var index = $scope.cps.indexOf(item);
      $scope.cps.splice(index,1);
    }
    
    $scope.createUser = function() {    
      var user = angular.copy($scope.user);
      user.$saveOrUpdate().then(
        function(savedUser) {
          $state.go('user-detail.overview', {userId: savedUser.id});
        }
      );
    };
     
    init();

  });
