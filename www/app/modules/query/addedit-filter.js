angular.module('os.query.addeditfilter', ['os.query.models'])
  .controller('QueryAddEditFilterCtrl', function($scope, QueryUtil, Util) {
    $scope.onOpSelect = function() {
      QueryUtil.onOpSelect($scope.queryLocal.currFilter);
    };

    $scope.disableAddEditFilterBtn = function() {
      var filter = $scope.queryLocal.currFilter;
      if (!!filter.expr && !!filter.desc) {
        return false;
      } else if (filter.field && filter.op) {
        var op = filter.op.name;
        if (op == 'exists' || op == 'not_exists' || op == 'any') {
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
      var filter = angular.extend({form: $scope.openForm, id: ql.filterId}, ql.currFilter);
      if (filter.valueType == 'tagsSelect') {
        filter.value = Util.splitStr(filter.value, /,|\t|\n/);
      }

      if (ql.filters.length > 0) {
        ql.exprNodes.push({type: 'op', value: QueryUtil.getOp('and').name});
      }

      ql.filters.push(filter);
      ql.filtersMap[filter.id] = filter;
      ql.exprNodes.push({type: 'filter', value: filter.id});
      ql.isValid = QueryUtil.isValidQueryExpr(ql.exprNodes);
      ql.currFilter = {};

      QueryUtil.disableCpSelection(ql);
    };

    $scope.displayFilter = function(filter) {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      ql.currFilter = angular.copy(filter);
      ql.currFilter.unaryOp = QueryUtil.isUnaryOp(filter.op);
      ql.currFilter.valueType = QueryUtil.getValueType(filter.field, filter.op);
      if (ql.currFilter.valueType == 'tagsSelect') {
        ql.currFilter.value = QueryUtil.getStringifiedValue(ql.currFilter.value);
      }

      if (!filter.expr) {
        ql.currFilter.ops = QueryUtil.getAllowedOps(filter.field);
      }
    };

    $scope.editFilter = function() {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      var filter = angular.extend({}, ql.currFilter);
      if (filter.valueType == 'tagsSelect') {
        filter.value = Util.splitStr(filter.value, /,|\t|\n/);
      }

      for (var i = 0; i < ql.filters.length; ++i) {
        if (filter.id == ql.filters[i].id) {
          ql.filters[i] = filter;
          ql.filtersMap[filter.id] = filter;
          break;
        }
      }

      ql.currFilter = {};
      QueryUtil.disableCpSelection(ql);
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
      QueryUtil.disableCpSelection(ql);
    };
 
    $scope.handleAutocompleteKeyDown = function(event) {
      if (event.keyCode === $.ui.keyCode.TAB && 
          angular.element(event.target).data("ui-autocomplete").menu.active) {
        event.preventDefault();
      }
    };

    $scope.temporalFilterOpts = {
      options: {
        source: function(request, response) {
          var ql = $scope.queryLocal;
          var term = request.term.replace(/\s+/g, '');
          var srchTerm = term.substr(QueryUtil.getOpIdx(term) + 1);
          var dotIdx = srchTerm.indexOf(".");
          if (dotIdx == -1) {
            var formsAndFn = QueryUtil.getFormsAndFnAdvise(ql.selectedCp);
            response($scope.temporalFilterOpts.methods.filter(formsAndFn, srchTerm));
          } else {
            var formName = srchTerm.substr(0, dotIdx);
            var fieldName = srchTerm.substr(dotIdx + 1).replace(/\s+/g, '');
            var form = QueryUtil.getForm(ql.selectedCp, formName);
            form.getFields().then(
              function() {
                var fieldsAdvise = QueryUtil.getFieldsAdvise(form);
                response($scope.temporalFilterOpts.methods.filter(fieldsAdvise, fieldName));
              }
            );
          }
        },

        focus: function(event, ui) {
          event.preventDefault();
          return false;
        },

        select: function(event, ui) {
          var cf = $scope.queryLocal.currFilter;
          var expr = cf.expr;
          var opIdx = QueryUtil.getOpIdx(expr);
          var dotIdx = expr.lastIndexOf('.');
          if (opIdx == -1) {
            if (dotIdx == -1) {
              cf.expr = ui.item.value;
            } else {
              cf.expr = expr.substr(0, dotIdx + 1) + ui.item.value;
            }
          } else {
            if (dotIdx < opIdx) {
              cf.expr = expr.substr(0, opIdx + 1) + ' ' + ui.item.value;
            } else {
              cf.expr = expr.substr(0, dotIdx + 1) + ui.item.value;
            }
          }
             
          ui.item.value = cf.expr;
          return false;
        }
      },

      methods: {}
    }
  }
);
