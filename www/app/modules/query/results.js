
angular.module('os.query.results', ['os.query.models'])
  .controller('QueryResultsCtrl', function($scope, $state, $modal, queryGlobal, QueryUtil, QueryExecutor, Alerts) {
    function init() {
      $scope.queryCtx = queryGlobal.queryCtx;
      $scope.selectedRows = [];

      $scope.resultsCtx = {
        waitingForRecords: true,
        error: false,
        moreData: false,
        columnDefs: [],
        rows: [],
        numRows: 0,
        labelIndices: [],
        gridOpts: getGridOpts()
      }

      loadRecords();
    }

    function getGridOpts() {
      return {
        columnDefs        : 'resultsCtx.columnDefs',
        data              : 'resultsCtx.rows',
        enableColumnResize: true,
        showFooter        : true,
        totalServerItems  : 'resultsCtx.numRows',
        plugins           : [gridFilterPlugin],
        headerRowHeight   : 70,
        selectedItems     : $scope.selectedRows,
        enablePaging      : false
      };
    }

    function loadRecords() {
      var qc = $scope.queryCtx;
      $scope.resultsCtx.waitingForRecords = true;
      $scope.resultsCtx.error = false;
      QueryExecutor.getRecords(undefined, qc.selectedCp.id, getAql(), 'DEEP').then(
        function(result) {
          $scope.resultsCtx.waitingForRecords = false;
          if (qc.reporting.type == 'crosstab') {
            // showPivotTable(result);
          } else {
            prepareDataGrid(result);
          }
        },

        function() {
          $scope.resultsCtx.waitForRecords = false;
          $scope.resultsCtx.error = true;
        }
      );
    }

    function getAql() { 
      var qc = $scope.queryCtx;
      return QueryUtil.getDataAql(
        qc.selectedFields, 
        qc.filtersMap, 
        qc.exprNodes, 
        qc.reporting);
    }

    function prepareDataGrid(result) {
      var idx = -1;
      var colDefs = result.columnLabels.map(
        function(columnLabel) {
          ++idx;
          return {
            field: "col" + idx,
            displayName: columnLabel,
            width: 100,
            headerCellTemplate: 'modules/query/column-filter.html'
          };
        }
      );

      $scope.resultsCtx.columnDefs = colDefs;
      $scope.resultsCtx.labelIndices = result.columnIndices;
      $scope.resultsCtx.rows = getFormattedRows(result.rows);
      $scope.resultsCtx.numRows = result.rows.length;
      $scope.resultsCtx.moreData = (result.dbRowsCount >= 10000);
      $scope.selectedRows.length = 0;

      /** Hack to make grid resize **/
      window.setTimeout(function(){
        $(window).resize();
        $(window).resize();
      }, 500);
    }

    function getFormattedRows(rows) {
      var formattedRows = [];
      for (var i = 0; i < rows.length; ++i) {
        var formattedRow = {};
        for (var j = 0; j < rows[i].length; ++j) {
          formattedRow["col" + j] = rows[i][j];
        }
        formattedRows.push(formattedRow);
      }
      return formattedRows;
    }

    var gridFilterPlugin = {
      init: function(scope, grid) {
        gridFilterPlugin.scope = scope;
        gridFilterPlugin.grid = grid;
        $scope.$watch(
          function() {
            var searchQuery = "";
            angular.forEach(
              gridFilterPlugin.scope.columns, 
              function(col) {
                if (col.visible && col.filterText) {
                  var filterText = '';
                  if (col.filterText.indexOf('*') == 0 ) {
                    filterText = col.filterText.replace('*', '');
                  } else {
                    filterText = col.filterText;
                  }
                  filterText += ";";
                  searchQuery += col.displayName + ": " + filterText;
                }
              }
            );
            return searchQuery;
          },

          function(searchQuery) {
            gridFilterPlugin.scope.$parent.filterText = searchQuery;
            gridFilterPlugin.grid.searchProvider.evalFilter();
          }
        );
      },
      scope: undefined,
      grid: undefined
    };

    $scope.editFilters = function() {
      $state.go('query-addedit'); //, {queryId: ''});
    }

    $scope.defineView = function() {
      var mi = $modal.open({
        templateUrl: 'modules/query/define-view.html',
        controller: 'DefineViewCtrl',
        resolve: {
          queryCtx: function() {
            return $scope.queryCtx;
          }
        }
      });

      mi.result.then(
        function(queryCtx) {
          $scope.queryCtx = queryCtx;
          //$scope.disableCpSelection();
          loadRecords();
        }
      );
    }

    $scope.downloadResults = function() {
      var qc = $scope.queryCtx;

      var alert = Alerts.info('queries.export_initiated', {}, false);  
      QueryExecutor.exportQueryResultsData(undefined, qc.selectedCp.id, getAql(), 'DEEP').then(
        function(result) {
          Alerts.remove(alert);
          if (result.completed) {
            Alerts.info('queries.downloading_data_file');
            QueryExecutor.downloadDataFile(result.dataFile);
          } else if (result.dataFile) {
            Alerts.info('queries.data_file_will_be_emailed');
          }
        },

        function() {
          Alerts.remove(alert);
        }
      );
    };

    
    init();
  });
