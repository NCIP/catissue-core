
angular.module('openspecimen')
  .factory('Util', function($rootScope, $timeout) {
    function clear(input) {
      input.splice(0, input.length);
    };

    function unshiftAll(arr, elements) {
      Array.prototype.splice.apply(arr, [0, 0].concat(elements));
    };

    function assign(arr, elements) {
      clear(arr);
      unshiftAll(arr, elements);
    };

    function filter($scope, varName, callback) {
      $scope.$watch(varName, function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if ($scope._filterQ) {
          $timeout.cancel($scope._filterQ);
        }

        $scope._filterQ = $timeout(
          function() {
            callback(newVal);
          },
          $rootScope.global.filterWaitInterval
        );
      }, true);
    }

    return {
      clear: clear,

      unshiftAll: unshiftAll,

      assign: assign,

      filter: filter
    };
  });
