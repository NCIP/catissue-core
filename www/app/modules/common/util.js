
angular.module('openspecimen')
  .factory('Util', function() {
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

    return {
      clear: clear,

      unshiftAll: unshiftAll,

      assign: assign
    };
  });
