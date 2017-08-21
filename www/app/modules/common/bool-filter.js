
angular.module('openspecimen')
  .filter('osBoolValue', function($translate) {
    return function(input, trueValue, falseValue, notSpecified) {
      var key = 'common.not_specified';

      if (input == true || input == 'true') {
        key = trueValue;
      } else if (input == false || input == 'false' || !notSpecified) {
        key = falseValue;
      } else {
        key = notSpecified;
      }

      return $translate.instant(key);
    }
  });
