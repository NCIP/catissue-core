angular.module('openspecimen')
  .directive('osChart', function() {
    return {
      restrict: 'E',

      scope: {
        data: '=',

        options: '='
      },

      link: function(scope, element, attrs) {
        scope.options.type = scope.options.type || 'Line';
      },

      template:
        '<canvas class="chart-base" chart-type="options.type" chart-data="data.values" ' +
        '  chart-labels="data.categories" chart-series="data.series" chart-options="options" ' +
        '  chart-legend="true">' +
        '</canvas>'
    }
  });
