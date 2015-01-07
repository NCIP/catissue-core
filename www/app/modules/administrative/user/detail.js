
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function($scope, $q, user, Institute, Site, CollectionProtocol, Role, PvManager) {
    $scope.user = user;
    
    PvManager.loadPvs($scope, 'domains');
  
    $scope.sites = PvManager.getSites();
    
    Institute.list().then(function(institutes) {
      $scope.institutes = institutes;
    });
        
    CollectionProtocol.query().then(function(cps) {
      $scope.cps = cps;
    });
    
    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
