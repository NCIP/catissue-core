
angular.module('os.query.expr', ['os.query.models'])
  .controller('QueryExprCtrl', function($scope, QueryUtil) {
    $scope.exprSortOpts = {
      placeholder: 'os-query-expr-node-placeholder',
      stop: function(event, ui) {
        var ql = $scope.queryLocal;
        ql.isValid = QueryUtil.isValidQueryExpr($scope.queryLocal.exprNodes);
        $scope.$apply($scope.ql);
      }
    };

    $scope.getFilterDesc = function(filterId) {
      var filter = $scope.queryLocal.filtersMap[filterId];
      var desc = "Unknown";
      if (filter && filter.expr && filter.desc) {
        desc = filter.desc;
      } else if (filter && filter.form && filter.field && filter.op) {
        desc = "<i>" + filter.form.caption + "  >> " + filter.field.caption + "</i> <b>" + filter.op.desc + "</b> ";
        if (filter.value) {
          desc += filter.value;
        }
      }

      return desc;
    };

    $scope.getOpCode = function(name) {
      return QueryUtil.getOp(name).code;
    };

    $scope.isFilterParamterized = function(filterId) {
      var filter = $scope.queryLocal.filtersMap[filterId];
      return filter && filter.parameterized;
    };

    $scope.addParen = function() {
      var node1 = {type: 'paren', value: '('};
      var node2 = {type: 'paren', value: ')'};

      $scope.queryLocal.exprNodes.unshift(node1);
      $scope.queryLocal.exprNodes.push(node2);
    };

    $scope.addOp = function(op) {
      var node = {type: 'op', value: op};
      $scope.queryLocal.exprNodes.push(node);
      $scope.queryLocal.isValid = QueryUtil.isValidQueryExpr($scope.queryLocal.exprNodes);
    };

    $scope.toggleOp = function(index) {
      var node = $scope.queryLocal.exprNodes[index];
      if (node.value == 'and') {
        node.value = 'or';
      } else if (node.value == 'or') {
        node.value = 'intersect';
      } else if (node.value == 'intersect') {
        node.value = 'and';
      }
    };

    $scope.removeNode = function(idx) {
      var exprNodes = $scope.queryLocal.exprNodes;
      exprNodes.splice(idx, 1);
      $scope.queryLocal.isValid = QueryUtil.isValidQueryExpr(exprNodes);
    };
  });
