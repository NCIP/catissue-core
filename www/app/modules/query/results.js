
angular.module('os.query.results', ['os.query.models'])
  .controller('QueryResultsCtrl', function(
    $scope, $state, $stateParams, $modal, 
    queryCtx, QueryUtil, QueryExecutor, SpecimenList, Alerts) {

    function init() {
      $scope.queryCtx = queryCtx;
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

      executeQuery($stateParams.editMode);
      loadAllSpecimenList();
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

    function executeQuery(editMode) {
      if (!editMode && isParameterized()) {
        showParameterizedFilters();
      } else {
        loadRecords();
      }
    }

    function loadAllSpecimenList() {
      SpecimenList.query().then(
        function(lists) {
          $scope.specimenLists = lists;
        }
      );
    }

    function isParameterized() {
      var filters = $scope.queryCtx.filters;
      for (var i = 0; i < filters.length; ++i) {
        if (filters[i].parameterized) {
          return true;
        }
      }

      return false;
    }

    function showParameterizedFilters() {
      var modal = $modal.open({
        templateUrl: 'modules/query/parameters.html',
        controller: 'ParameterizedFilterCtrl',
        resolve: {
          queryCtx: function() {
            return $scope.queryCtx;
          }
        },
        size: 'lg'
      });

      modal.result.then(
        function(result) {
          if (result) {
            $scope.queryCtx = result;
          }

          loadRecords();
        }
      );
    };

    function loadRecords() {
      var qc = $scope.queryCtx;
      $scope.showAddToSpecimenList = showAddToSpecimenList();
      $scope.resultsCtx.waitingForRecords = true;
      $scope.resultsCtx.error = false;
      QueryExecutor.getRecords(undefined, qc.selectedCp.id, getAql(), 'DEEP').then(
        function(result) {
          $scope.resultsCtx.waitingForRecords = false;
          if (qc.reporting.type == 'crosstab') {
            preparePivotTable(result);
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

    function preparePivotTable(result) {
      $scope.resultsCtx.rows = result.rows;
      $scope.resultsCtx.columnLabels = result.columnLabels;

      $scope.resultsCtx.pivotTableOpts = {
        height: '500px',
        width: '1200px',
        colHeaders: $scope.resultsCtx.columnLabels,
        numGrpCols: $scope.queryCtx.reporting.params.groupRowsBy.length,
        multipleVal: $scope.queryCtx.reporting.params.summaryFields.length > 1,
        data: $scope.resultsCtx.rows
      };
    };

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

    function showAddToSpecimenList() {
      if ($scope.queryCtx.reporting.type == 'crosstab') {
        return false;
      }

      var idx = $scope.queryCtx.selectedFields.indexOf('Specimen.label');
      if (idx != -1) {
        return true;
      }

      return false;
    };

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
      $state.go('query-addedit', {queryId: $scope.queryCtx.id});
    }

    $scope.defineView = function() {
      var mi = $modal.open({
        templateUrl: 'modules/query/define-view.html',
        controller: 'DefineViewCtrl',
        resolve: {
          queryCtx: function() {
            return $scope.queryCtx;
          }
        },
        size: 'lg'
      });

      mi.result.then(
        function(queryCtx) {
          $scope.queryCtx = queryCtx;
          QueryUtil.disableCpSelection(queryCtx);
          loadRecords();
        }
      );
    }

    $scope.rerun = function() {
      executeQuery(false);
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

    $scope.selectAllRows = function() {
      $scope.resultsCtx.gridOpts.selectAll(true);
      $scope.queryCtx.selectAll = true;
    };

    $scope.unSelectAllRows = function() {
      $scope.resultsCtx.gridOpts.selectAll(false);
      $scope.queryCtx.selectAll = false;
    };

    $scope.addSelectedSpecimensToSpecimenList = function(list) {
      var idx = $scope.queryCtx.selectedFields.indexOf('Specimen.label');
      var colName = $scope.resultsCtx.columnDefs[idx].field;
      var specimens = $scope.selectedRows.map(function(row) {
         return {label: row[colName]};
      })

      var data = {
        id: list.id,
        specimens: specimens,
        operation: 'ADD'
      }

      var specimenList = new SpecimenList(data);
      specimenList.updateSpecimens().then(function(specimens) {
        Alerts.success('specimen_list.specimens_added', {name: list.name});
      })
    }

    $scope.createNewSpecimenList = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/query/add-specimen-list.html',
        controller: 'AddEditSpecimenListCtrl',
        resolve: {
          list: function() {
            var idx = $scope.queryCtx.selectedFields.indexOf('Specimen.label');
            var colName = $scope.resultsCtx.columnDefs[idx].field;
            var specimens = $scope.selectedRows.map(function(row) {
              return {label: row[colName]};
            })
            
            return {specimens: specimens, sharedWith: []};
          }
        }
      });

      modalInstance.result.then(
        function(list) {
          $scope.specimenLists.push(list);
          Alerts.success("queries.specimen_list_created", {listName: list.name});
        }
      );
    };
 
    init();
  });
