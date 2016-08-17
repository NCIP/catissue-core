angular.module('openspecimen')
  .directive('osChart', function() {
    return {
      restrict: 'E',

      replace: 'true',

      scope: {
        data: '=',
        options: '='
      },

      link: function(scope, element, attrs) {
        scope.options.type = scope.options.type || 'line';

        scope.override = [];
        scope.$watch('data.values',
          function(vals) {
            scope.override = vals.map(
              function() {
                return {pointRadius: 0, pointHitRadius: 10};
              }
            );
          }
        );
      },

      template:
        '<div>' +
        '  <canvas class="chart-base" chart-type="options.type" chart-data="data.values" ' +
        '    chart-labels="data.categories" chart-series="data.series" chart-options="options" ' +
        '    chart-legend="true" chart-dataset-override="override">' +
        '  </canvas>' +
        '</div>'
    }
  });
