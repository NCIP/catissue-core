
angular.module('openspecimen')
  .filter('osPrefixHttp', function() {
    return function(input) {
      if (!angular.isString(input)) {
        return input;
      }

      input = input.trim();
      if (input.indexOf('http') == 0 || input.indexOf('ftp') == 0) {
        return input;
      }

      return 'http://' + input;
    };
  });
   
