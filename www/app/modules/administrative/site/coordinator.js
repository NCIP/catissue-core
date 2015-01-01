
angular.module('os.administrative.site.coordinators', [])
  .filter('showCoordinators', function() {
    return function(coordinators) {
      if(!coordinators) {
        return "";
      }

      var result = [];
      angular.forEach(coordinators, function(coordinator) {
        result.push(coordinator.lastName + ' ' + coordinator.firstName);
      });

      return result.join(", ");
    }
  });