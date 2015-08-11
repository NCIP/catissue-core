angular.module('openspecimen')
  .factory('LocationChangeListener', function($window, $translate) {
    var isLocationChangeAllowed = true;

    function init() {
      $window.onbeforeunload = function() {
        if (!isLocationChangeAllowed) {
          return $translate.instant('common.confirm_navigation');
        }
      }
    }

    function allowChange() {
      isLocationChangeAllowed = true;
    }

    function preventChange() {
      isLocationChangeAllowed = false;
    }

    function onChange(event) {
      if (!isLocationChangeAllowed && !confirm($translate.instant('common.confirm_navigation'))) {
        event.preventDefault();
      } else {
        allowChange();
      }
    }

    init();

    return {
      allowChange: allowChange,

      preventChange: preventChange,

      onChange: onChange,
    };
  })
