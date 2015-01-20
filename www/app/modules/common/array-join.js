angular.module('openspecimen')
  .filter('osArrayJoin', function() {
    return function(collection, fun) {
      if(!fun) {
         return collection.join(", ");
      }

      var result = [];
      angular.forEach(collection, function(item) {
        result.push(fun(item));
      });

      return result.join(", ");
    }
  });


