angular.module('openspecimen')
  .filter('osHttpPrefixer', function() {
    return function(input) {
      if (!input || input.startsWith("http://")) {
        return input;
      }

      return 'http://' + input;
    };
  });
