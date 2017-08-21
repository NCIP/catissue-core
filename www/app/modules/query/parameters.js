
angular.module('os.query.parameterized', ['os.query.models'])
  .controller('ParameterizedFilterCtrl', function($scope, $modalInstance, queryCtx, Util, QueryUtil) {
    function init() {
      $scope.queryCtx = queryCtx;
      angular.forEach(queryCtx.filters, 
        function(filter) {
          if (!filter.parameterized) {
            return;
          }

          if (filter.expr) {
            var tObj = filter.tObj = QueryUtil.getTemporalExprObj(filter.expr);
            tObj.ops = QueryUtil.getAllowedOps({type: 'NUMERIC'});
            filter.value = tObj.rhs;
            filter.op = QueryUtil.getOpBySymbol(tObj.op);
          } else {
            filter.field.ops = QueryUtil.getAllowedOps(filter.field);
          }

          filter.newOp = filter.op;
          filter.valueType = QueryUtil.getValueType(filter.field, filter.op);
          filter.unaryOp = QueryUtil.isUnaryOp(filter.op);
          if (filter.valueType == 'tagsSelect') {
            filter.value = QueryUtil.getStringifiedValue(filter.value);
          }
        }
      );
    }

    $scope.onOpSelect = function(filter) {
      if (filter.newOp == filter.op) {
        return;
      }
      filter.op = filter.newOp;
      QueryUtil.onOpSelect(filter);
    }

    $scope.ok = function() {
      angular.forEach($scope.queryCtx.filters,
        function(filter) {
          if (!filter.parameterized || (filter.valueType != 'tagsSelect' && !filter.expr)) {
            return;
          }

          if (filter.valueType == 'tagsSelect') {
            filter.value = Util.splitStr(filter.value, /,|\t|\n/);
            return;
          } 

          var tObj = filter.tObj;
          filter.expr = tObj.lhs + ' ' + filter.op.symbol + ' ';
          if (filter.value instanceof Array) {
            filter.expr += filter.value[0] + ' and ' + filter.value[1];
          } else {
            filter.expr += filter.value;
          }
        }
      );
            
      $modalInstance.close($scope.queryCtx);
    }

    $scope.cancel = function() {
      $modalInstance.close(null);
    }

    init();
  });
