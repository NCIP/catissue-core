angular.module('openspecimen')
  .filter('osArrayJoin', function() {
    return function(collection, fun) {
      var result = [];
      angular.forEach(collection, function(item) {
        result.push(fun(item));
      });

      return result.join(", ");
    }
  });


