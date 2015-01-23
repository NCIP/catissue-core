angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    User, Site, Institute, Role, PvManager, Alerts) {
 
    var init = function() {
      $scope.user = new User();
      loadPvs();
    };
    
    function loadPvs() {
      $scope.domains = PvManager.getPvs('domains');

      Institute.list().then(
        function(instituteList) {
          $scope.institutes = [];
          angular.forEach(instituteList,
            function(institute) {
              $scope.institutes.push(institute.name);
            }
          )
        }
      );
    }

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
