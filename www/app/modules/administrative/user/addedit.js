angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $state, $stateParams,
    user, User, Institute, PvManager) {
 
    function init() {
      $scope.user = user;
      loadPvs();
    }
    
    function loadPvs() {
      $scope.domains = PvManager.getPvs('domains');

      Institute.list().then(
        function(instituteList) {
          $scope.institutes = [];
          angular.forEach(instituteList, function(institute) {
            $scope.institutes.push(institute.name);
          });
        }
      );

    }

    $scope.loadDepartments = function(instituteName) {
      $scope.user.deptName = undefined;
      $scope.departments = [];
      Institute.getDepartments(instituteName).then(
        function(deptList) {
          angular.forEach(deptList, function(department) {
            $scope.departments.push(department.name);
          });
        }
      );
    };
    
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
