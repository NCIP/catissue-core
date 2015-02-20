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

    $scope.loadDepartments = function(instituteName, unsetDept) {
      Institute.getByName(instituteName).then(
        function(institutes) {
          $scope.departments = [];
          if (institutes.length == 1) {
            angular.forEach(institutes[0].departments, function(department) {
              $scope.departments.push(department.name);
            });
          }

          //This is trick to unset department on selecting institute
          if ($scope.departments.indexOf($scope.user.departmentName) == -1) {
            $scope.user.departmentName = undefined;
          }

          console.log($scope.departments);
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
