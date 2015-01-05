
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function($scope, $q, user, Institute, Site, CollectionProtocol, Role, PvManager) {
    $scope.user = user;
    $scope.user.acl = [];
    
    PvManager.loadPvs($scope, 'domains');
  
    Institute.query().then(function(institutes) {
      $scope.institutes = institutes;
    });
    
    Site.query().then(function(sites) {
      $scope.sites = sites;
    });
    
    CollectionProtocol.query().then(function(cps) {
      $scope.cps = cps;
    });
    
    Role.query().then(function(roles) {
      $scope.roles = roles;
    });
    
    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }
  });
