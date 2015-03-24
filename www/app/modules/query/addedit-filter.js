angular.module('os.query.addeditfilter', ['os.query.models'])
  .controller('QueryAddEditFilterCtrl', function($scope, QueryUtil) {
    $scope.onOpSelect = function() {
      var filter = $scope.queryLocal.currFilter;
      if (filter.op.name == "between") {
        filter.value = [undefined, undefined];
      } else if (filter.op.name == 'qin' || filter.op.name == 'not_in') {
        filter.value = [];
      } else {
        filter.value = undefined;
      }

      var field = filter.field;
      filter.valueType = QueryUtil.getValueType(field, filter.op);
      filter.unaryOp = QueryUtil.isUnaryOp(filter.op);
    };

    $scope.disableAddEditFilterBtn = function() {
      var filter = $scope.queryLocal.currFilter;
      if (filter.expr && filter.desc) {
        return false;
      } else if (filter.field && filter.op) {
        var op = filter.op.name;
        if (op == 'exists' || op == 'not_exists') {
          return false;
        } else if (op == 'qin' || op == 'not_in') {
          return !filter.value || filter.value.length == 0;
        } else if (filter.value) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    };

    $scope.addFilter = function() {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      ql.filterId++;
      var filter = angular.extend({form: ql.openForm, id: ql.filterId}, ql.currFilter);
      if (ql.filters.length > 0) {
        ql.exprNodes.push({type: 'op', value: QueryUtil.getOp('and').name});
      }

      ql.filters.push(filter);
      ql.filtersMap[filter.id] = filter;
      ql.exprNodes.push({type: 'filter', value: filter.id});
      ql.isValid = QueryUtil.isValidQueryExpr(ql.exprNodes);
      ql.currFilter = {};

      //$scope.disableCpSelection();
    };

    $scope.displayFilter = function(filter) {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      ql.currFilter = angular.copy(filter);
      if (!filter.expr) {
        ql.currFilter.ops = QueryUtil.getAllowedOps(filter.field);
      }
    };

    $scope.editFilter = function() {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      var filter = angular.extend({}, ql.currFilter);
      for (var i = 0; i < ql.filters.length; ++i) {
        if (filter.id == ql.filters[i].id) {
          ql.filters[i] = filter;
          ql.filtersMap[filter.id] = filter;
          break;
        }
      }

      ql.currFilter = {};
      //$scope.disableCpSelection();
    };

    $scope.cancelFilter = function() {
      QueryUtil.hidePopovers();
      $scope.queryLocal.currFilter = {};
    };

    $scope.deleteFilter = function(filter) {
      QueryUtil.hidePopovers();

      //
      // Remove from filters list
      // 
      var ql = $scope.queryLocal;
      delete ql.filtersMap[filter.id];
      for (var i = 0; i < ql.filters.length; ++i) { 
        if (filter.id == ql.filters[i].id) {
          ql.filters.splice(i, 1);
          break;
        }
      }
      
      //
      // Remove temporal filter from select list
      //
      var selectField = '$temporal.' + filter.id;
      for (var i = 0; i < ql.selectedFields.length; ++i) { 
        if (ql.selectedFields[i] == selectField) {
          ql.selectedFields.splice(i, 1);
          break;
        }
      }

      //
      // Remove from query expression nodes 
      // 
      var exprNodes = ql.exprNodes;
      for (var i = 0; i < exprNodes.length; ++i) {
        var exprNode = exprNodes[i];
        if (exprNode.type != 'filter' || filter.id != exprNode.value) {
          continue;
        }

        if (i == 0 && exprNodes.length > 1 && exprNodes[1].type == 'op') {
          // first filter -> remove filter and operator to right
          exprNodes.splice(0, 2); 
        } else if (i != 0 && exprNodes[i - 1].type == 'op') {
          // operator on left -> remove filter and operator to left
          exprNodes.splice(i - 1, 2);
        } else if (i != 0 && (i + 1) < exprNodes.length && exprNodes[i + 1].type == 'op') {
          // operator on right -> remove filter and operator to right
          exprNodes.splice(i, 2);
        } else {
          // no operators before or after filter -> remove only filter
          exprNodes.splice(i, 1);
        }
        break;
      }

      // validate expression after removing filter and optional operator
      ql.isValid = QueryUtil.isValidQueryExpr(exprNodes);

      //$scope.disableCpSelection();
    };
  }
);
