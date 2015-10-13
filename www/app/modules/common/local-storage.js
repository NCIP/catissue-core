
angular.module('openspecimen')
  .factory('LocalStorageSvc', function($window, Util) {
    var ls = $window.localStorage;

    var isSupported = (function() {
      var supported = 'localStorage' in $window && $window.localStorage !== null;
      if (supported) {
        ls.setItem('os.tmp', '');
        ls.removeItem('os.tmp');
      }
    }());

    function addItem(key, value) {
      if (!angular.isDefined(value)) {
        value = null;
      } else {
        value = angular.toJson(value);
      }

      ls.setItem('os.' + key, value);
    }

    function getItem(key) {
      var value = ls.getItem('os.' + key);
      if (!value || value == 'null') {
        return null;
      }

      return JSON.parse(value, function(k, v) {
        return Util.parseDate(v);
      });
    } 

    function removeItem(key) {
      ls.removeItem('os.' + key)
    }

    return {
      isSupported: function() {
        return isSupported;
      },

      addItem: addItem,
      getItem: getItem,
      removeItem: removeItem
    };
  });
