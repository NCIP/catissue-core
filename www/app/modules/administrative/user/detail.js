
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function($scope, $q, user, Institute, Role, PvManager) {
    $scope.user = user;
    $scope.domains = PvManager.getPvs('domains');
    $scope.sites = PvManager.getSites();
    
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
        
    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
