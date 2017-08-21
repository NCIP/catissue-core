
angular.module('openspecimen')
  .filter('osServerTime', function() {
    return function(input, serverOffset) {
      return +input + (new Date().getTimezoneOffset() * 60 * 1000 - serverOffset);
    }
  });
