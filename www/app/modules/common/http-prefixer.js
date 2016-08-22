angular.module('openspecimen')
  .filter('osHttpPrefixer', function() {
    return function(input) {
      if (!input || input.indexOf("http://") == 0) {
        return input;
      }

      return 'http://' + input;
    };
  });
