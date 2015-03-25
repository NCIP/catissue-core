
angular.module('os.query.addedit', ['os.query.models', 'os.query.util', 'os.query.save'])
  .controller('QueryAddEditCtrl', function(
    $scope, $state, $modal, 
    cps, query, queryGlobal, Alerts,
    SavedQuery, QueryUtil, QueryExecutor) {

    function init() {
      $scope.cps = cps;
      $scope.query = query;

      $scope.queryLocal = queryGlobal.queryCtx || {
        currentFilter: {},
        id: query.id,
        title: query.title,
        filters: [],
        filtersMap: {},
        exprNodes: [],
        filterId: 0,
        selectedFields: QueryUtil.getDefSelectedFields(),
        reporting: {type: 'none'},
        selectedCp: getCp(query.cpId || -1),
        isValid: true,
        drivingForm: 'Participant',
        wideRowMode: 'DEEP'
      };
      queryGlobal.queryCtx = $scope.queryLocal;

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

    $scope.saveQuery = function() {
      var mi = $modal.open({
        templateUrl: 'modules/query/save.html',
        controller: 'QuerySaveCtrl',
        resolve: {
          queryToSave: function() {
            return SavedQuery.fromQueryCtx($scope.queryLocal);
          }
        }
      });

      mi.result.then(
        function(query) { 
          $state.go('query-list');
          Alerts.success('queries.query_saved', {title: query.title});
        }
      );
    };

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

    $scope.viewResults = function() {
      $state.go('query-results', {queryId: query.id});
    }

    init();
  });
