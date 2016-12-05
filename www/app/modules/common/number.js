
angular.module('openspecimen')
  .filter('osNumberInScientificNotation', function($translate, Util) {
    return function(input, placeholder) {

      var result = placeholder || $translate.instant("common.not_specified");
      if (angular.isUndefined(input) || input === null) {
        return result;
      }


      return Util.getNumberInScientificNotation(input, 1000000);
    }
  });
