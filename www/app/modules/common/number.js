
angular.module('openspecimen')
  .filter('osNumber', function($translate) {
    return function(input, placeholder) {

      var result = placeholder || $translate.instant("common.not_specified");
      if (angular.isUndefined(input) || input === null) {
        return result;
      }

      if (angular.isNumber(input) && input > 1000000) {
        var number = input/1000000;
        return number  + "e6";
      }

      return input;
    }
  });
