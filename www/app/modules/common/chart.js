angular.module('openspecimen')
  .directive('osChart', function() {
    var showLegends = ['pie', 'doughnut'];

    return {
      restrict: 'E',

      replace: 'true',

      scope: {
        data: '=',
        options: '='
      },

      link: function(scope, element, attrs) {
        var type = scope.options.type = scope.options.type || 'line';

        if (showLegends.indexOf(type) != -1) {
          scope.options.legend = {display: true, position: 'bottom'};
        }

        scope.override = [];
        scope.$watch('data',
          function() {
            if (scope.data.series && scope.data.series.length > 1) {
              scope.options.legend = {display: true, position: 'bottom'};
            }

            scope.override = scope.data.values.map(
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
