
angular.module('os.query.addedit', ['os.query.models', 'os.query.util', 'os.query.save'])
  .controller('QueryAddEditCtrl', function(
    $scope, $state, $modal, $popover, $translate,
    cps, queryGlobal, queryCtx, Alerts,
    SavedQuery, QueryUtil, QueryExecutor) {

    var popoverOpts = {};
    var popovers = {};

    function init() {
      $scope.openForm = undefined;
      $scope.cps = cps;
      $scope.queryLocal = queryCtx;
      queryGlobal.queryCtx = $scope.queryLocal;

      initPopoverOpts();
      loadCpForms($scope.queryLocal.selectedCp);
    }

    function loadCpForms(cp) {
      queryGlobal.loadCpForms(cp);
    }

    function initPopoverOpts() {
      $translate('queries.add_filter').then(
        function(addFilter) {
          popoverOpts = {
            title: addFilter, 
            contentTemplate: 'modules/query/addedit-filter.html', 
            trigger: 'manual',
            scope: $scope
          }
        }
      );

      $scope.$on('$destroy', function() {
        angular.forEach(popovers, function(popover) {
          popover.destroy();
        });
      });
    }

    $scope.loadCpForms = function() {
      loadCpForms($scope.queryLocal.selectedCp);
    }

    $scope.onFormSelect = function(form) {
      QueryUtil.hidePopovers();

      var ql = $scope.queryLocal;
      ql.searchField = '';
      if ($scope.openForm) {
        $scope.openForm.showExtnFields = false; // previously selected form
      }

      $scope.openForm = form;
      ql.currFilter = {form: form};
      form.getFields();
    }

    $scope.onFieldSelect = function(field, event) {
      QueryUtil.hidePopovers();
      $scope.queryLocal.currFilter = {
        field: field, 
        op: null, 
        value: undefined,
        ops: QueryUtil.getAllowedOps(field)
      };

      var popover = $popover(angular.element(event.target), popoverOpts);
      popover.$promise.then(popover.toggle);
      popovers.push(popover);
    }

    $scope.onTemporalFilterSelect = function() { 
      QueryUtil.hidePopovers();
      $scope.queryLocal.currFilter = { };
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

    init();
  });
