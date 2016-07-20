angular.module('openspecimen')
  .directive('osListFilters', function($q, $rootScope, AuthorizationService) {

    function getFilter(filterCfg, filter) {
      var values;

      if (filterCfg.dataType == 'INTEGER' ||
          filterCfg.dataType == 'FLOAT' ||
          filterCfg.dataType == 'DATE') {

        var min = filter.min, max = filter.max;

        if (!min && min != 0 && !max && max != 0) {
          return null;
        }

        if (filterCfg.dataType == 'DATE') {
          if (!!min) {
            min = "\"" + min + "\"";
          }

          if (!!max) {
            max = "\"" + max + "\"";
          }
        }

        values = [min, max];
      } else {
        if (!filter) {
          return null;
        }

        values = [filter];
      }

      return {expr: filterCfg.expr, values: values};
    }

    return {
      restrict: 'E',

      templateUrl: 'modules/common/list-filters.html',

      scope: {
        filtersCfg: '=',

        filters: '=',

        loadValues: '&',

        initCtrl: '&'
      },

      controller: function($scope) {
        this.getFilters = function() {
          var result = [];

          angular.forEach($scope.filters,
            function(filter, idx) {
              filter = getFilter($scope.filtersCfg[idx], filter);
              if (!!filter) {
                result.push(filter);
              }
            }
          );

          return result;
        }
      },

      controllerAs: '$listFilters',

      link: function(scope, element, attrs, ctrl) {
        scope.ctx = {
          global: $rootScope.global,
          hasPhiAccess: AuthorizationService.hasPhiAccess()
        };

        if (scope.initCtrl) {
          scope.initCtrl({$listFilters: ctrl});
        }
       
        scope.loadFilterValues = function(filterCfg) {
          if ((filterCfg.values && filterCfg.values.length > 0) || !scope.loadValues) {
            return;
          }

          if (!filterCfg.valuesQ) {
            filterCfg.valuesQ = scope.loadValues({expr: filterCfg.expr});
          }

          $q.when(filterCfg.valuesQ).then(
            function(values) {
              filterCfg.values = values;
            }
          );
        }
      }
    }
  });
