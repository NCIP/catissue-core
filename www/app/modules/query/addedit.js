
angular.module('os.query.addedit', ['os.query.models', 'os.query.util'])
  .controller('QueryAddEditCtrl', function($scope, cps, query, queryGlobal, QueryUtil) {
    function init() {
      $scope.cps = cps;
      $scope.query = query;

      $scope.queryLocal = {
        currentFilter: {},
        filters: [],
        filtersMap: {},
        exprNodes: [],
        filterId: 0
      };

      var cpId = query.cpId || -1;
      $scope.queryLocal.selectedCp = getCp(cpId);
      loadCpForms($scope.queryLocal.selectedCp);

      QueryUtil.initOpsDesc();
    }

    function loadCpForms(cp) {
      queryGlobal.loadCpForms(cp);
    }

    function getCp(cpId) {
      var cp = undefined;
      for (var i = 0; i < cps.length; ++i) {
        if (cps[i].id == cpId) {
          cp = cps[i];
          break;
        }
      }

      return cp;
    }

    $scope.loadCpForms = function() {
      loadCpForms($scope.queryLocal.selectedCp);
    }

    $scope.onFormSelect = function(form) {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      ql.searchField = '';
      if (ql.openForm) {
        ql.openForm.showExtnFields = false; // previously selected form
      }

      ql.openForm = form;
      ql.currFilter = {form: form};
      form.getFields().then(
        function(fields) {
          form.staticFields = QueryUtil.flattenStaticFields("", fields);
          form.extnForms = QueryUtil.getExtnForms("", fields);
          form.extnFields = QueryUtil.flattenExtnFields(form.extnForms);
        }
      );
    }

    $scope.onFieldSelect = function(field) {
      QueryUtil.hidePopovers();
      $scope.queryLocal.currFilter = {
        field: field, 
        op: null, 
        value: undefined,
        ops: QueryUtil.getAllowedOps(field)
      };
    }

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

    init();
  });
