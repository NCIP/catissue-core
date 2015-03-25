
angular.module('os.query.addedit', ['os.query.models', 'os.query.util'])
  .controller('QueryAddEditCtrl', function($scope, cps, query, queryGlobal, QueryUtil, QueryExecutor) {
    function init() {
      $scope.cps = cps;
      $scope.query = query;

      $scope.queryLocal = {
        currentFilter: {},
        filters: [],
        filtersMap: {},
        exprNodes: [],
        filterId: 0,
        selectedFields: [],
        isValid: true
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

    $scope.getCount = function() {
      var ql = $scope.queryLocal;
      var aql = QueryUtil.getCountAql(ql.filtersMap, ql.exprNodes);

      ql.waitingForCnt = true;
      ql.countResults = undefined;
      QueryExecutor.getCount(undefined, ql.selectedCp.id, aql).then(
        function(result) {
          ql.waitingForCnt = false;
          ql.countResults = result;
        },

        function(result) {
          ql.waitingForCnt = false;
        }
      );
    }

    $scope.closePopover = function() {
      QueryUtil.hidePopovers();
    }

    init();
  });
