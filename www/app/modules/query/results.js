
angular.module('os.query.results', ['os.query.models'])
  .controller('QueryResultsCtrl', function($scope, queryGlobal, QueryUtil, QueryExecutor) {
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
      var aql = QueryUtil.getDataAql(
        qc.selectedFields, 
        qc.filtersMap, 
        qc.exprNodes, 
        qc.reporting);

      $scope.resultsCtx.waitingForRecords = true;
      $scope.resultsCtx.error = false;
      QueryExecutor.getRecords(undefined, qc.selectedCp.id, aql, 'DEEP').then(
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

    init();
  });
