
angular.module('os.query.results', ['os.query.models'])
  .controller('QueryResultsCtrl', function(
    $scope, $state, $stateParams, $modal, $document, $timeout,
    queryCtx, QueryCtxHolder, QueryUtil, QueryExecutor, SpecimenList, SpecimensHolder, Alerts) {

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
    }

    function getGridOpts() {
      return {
        columnDefs        : 'resultsCtx.columnDefs',
        data              : 'resultsCtx.rows',
        enableColumnResize: true,
        showFooter        : true,
        totalServerItems  : 'resultsCtx.numRows',
        plugins           : [gridFilterPlugin],
        headerRowHeight   : 35,
        selectedItems     : $scope.selectedRows,
        enablePaging      : false
      };
    }

    function executeQuery(editMode) {
      //if (!editMode && isParameterized()) {
      //  showParameterizedFilters();
      //} else {
        loadRecords(true);
      //}
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

    function loadRecords(initFacets) {
      var qc = $scope.queryCtx;
      $scope.showAddToSpecimenList = showAddToSpecimenList();
      $scope.resultsCtx.waitingForRecords = true;
      $scope.resultsCtx.error = false;
      QueryExecutor.getRecords(undefined, qc.selectedCp.id, getAql(true), 'DEEP').then(
        function(result) {
          $scope.resultsCtx.waitingForRecords = false;
          if (qc.reporting.type == 'crosstab') {
            preparePivotTable(result);
          } else {
            var showColSummary = qc.reporting.type == 'columnsummary';
            prepareDataGrid(showColSummary, result);
            $scope.resultsCtx.gridOpts.headerRowHeight = showColSummary ? 66 : 35;
          }
        },

        function() {
          $scope.resultsCtx.waitForRecords = false;
          $scope.resultsCtx.error = true;
        }
      );

      if (initFacets) {
        loadFacets();
      }
    }

    function loadFacets() {
      var facets = [];
      angular.forEach($scope.queryCtx.filters,
        function(filter, index) {
          if (!filter.parameterized || !!filter.expr || filter.field.type != 'STRING') {
            return;
          }

          facets.push({
            id: filter.id,
            caption: filter.field.caption,
            expr: filter.form.name + "." + filter.field.name,
            type: filter.field.type,
            show: index < 4,
            values: undefined,
            valuesQ: undefined,
            selectedValues: [],
            isOpen: false
          });
        }
      );

      $scope.resultsCtx.facets = facets;
    }

    function loadFacetValues(facet) {
      if (facet.valuesQ || facet.values) {
        return;
      }

      facet.valuesQ = QueryExecutor.getFacetValues($scope.queryCtx.selectedCp.id, [facet.expr]);
      facet.valuesQ.then(
        function(result) {
          facet.values = result[0].values.map(
            function(value) {
              return {value: value, selected: false}
            }
          );
        }
      );
    }

    function getAql(addLimit) { 
      var qc = $scope.queryCtx;
      return QueryUtil.getDataAql(
        qc.selectedFields, 
        qc.filtersMap, 
        qc.exprNodes, 
        qc.reporting,
        addLimit);
    }

    function removeSeparator(label)  {
      var idx = label.lastIndexOf("# ");
      if (idx != -1) {
        label = label.substr(idx + 2);
      }

      return label;
    }

    function getColumnWidth(text) {
      var span = angular.element('<span/>')
        .addClass('ngHeaderText')
        .css('visibility', 'hidden')
        .text(text);

      angular.element($document[0].body).append(span);
      var width = span[0].offsetWidth + 2 + 10; // 5 + 5 = 10 is padding, 2 is buffer/uncertainity
      span.remove();
      return width;
    }

    function preparePivotTable(result) {
      $scope.resultsCtx.rows = result.rows;
      $scope.resultsCtx.columnLabels = result.columnLabels;

      var numGrpCols = $scope.queryCtx.reporting.params.groupRowsBy.length;
      for (var i = 0; i < numGrpCols; ++i) {
        result.columnLabels[i] = removeSeparator(result.columnLabels[i]);
      }

      var numValueCols = $scope.queryCtx.reporting.params.summaryFields.length;
      var numRollupCols = numValueCols;
      var rollupExclFields = $scope.queryCtx.reporting.params.rollupExclFields;
      if (rollupExclFields && rollupExclFields.length > 0) {
        numRollupCols = numRollupCols - rollupExclFields.length;
      }

      $scope.resultsCtx.pivotTableOpts = {
        height: '500px',
        width: '1200px',
        colHeaders: $scope.resultsCtx.columnLabels,
        numGrpCols: numGrpCols,
        numValueCols: numValueCols,
        numRollupCols: numRollupCols,
        data: $scope.resultsCtx.rows
      };
    };

    function prepareDataGrid(showColSummary, result) {
      var idx = -1,
          summaryRow = [];

      if (showColSummary && !!result.rows && result.rows.length > 0) {
        summaryRow = result.rows.splice(result.rows.length - 1, 1)[0];
      }

      var colDefs = result.columnLabels.map(
        function(columnLabel) {
          ++idx;

          var columnLabel = removeSeparator(columnLabel);
          var width = getColumnWidth(columnLabel);
          return {
            field: "col" + idx,
            displayName: columnLabel,
            minWidth: width < 100 ? 100 : width,
            headerCellTemplate: 'modules/query/column-filter.html',
            showSummary: showColSummary,
            summary: summaryRow[idx]
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
      if ($scope.queryCtx.selectedFields.indexOf('Specimen.label') != -1) {
        return true;
      }

      return false;
    };

    function getSelectedSpecimens() {
      var labelIndices = $scope.resultsCtx.labelIndices;
      if (!labelIndices) {
        return [];
      }

      var specimenLabels = [];
      var selectedRows = $scope.selectedRows;
      for (var i = 0; i < selectedRows.length; ++i) {
        var selectedRow = selectedRows[i];
        for (var j = 0; j < labelIndices.length; ++j) {
          var label = selectedRow["col" + labelIndices[j]];
          if (!label || specimenLabels.indexOf(label) != -1) {
            continue;
          }

          specimenLabels.push(label);
        }
      }

      return specimenLabels.map(function(label) { return {label: label} });
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
      QueryExecutor.exportQueryResultsData(undefined, qc.selectedCp.id, getAql(false), 'DEEP').then(
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
      $scope.resultsCtx.selectAll = true;
    };

    $scope.unSelectAllRows = function() {
      $scope.resultsCtx.gridOpts.selectAll(false);
      $scope.resultsCtx.selectAll = false;
    };

    $scope.addSelectedSpecimensToSpecimenList = function(list) {
      var selectedSpecimens = getSelectedSpecimens();
      if (!selectedSpecimens || selectedSpecimens.length == 0) {
        Alerts.error('specimens.no_specimens_for_specimen_list');
        return;
      }

      if (!list) {
        $scope.createNewSpecimenList();
      } else {
        list.addSpecimens(selectedSpecimens).then(
          function(specimens) {
            Alerts.success('specimen_list.specimens_added', {name: list.name});
          }
        );
      }
    }

    $scope.createNewSpecimenList = function() {
      QueryCtxHolder.setCtx(queryCtx);
      SpecimensHolder.setSpecimens(getSelectedSpecimens());
      $state.go('specimen-list-addedit', {listId: ''});
    };

    $scope.toggleFacetValues = function(facet) {
      $timeout(
        function() {
          if (!facet.isOpen) {
            return;
          }

          loadFacetValues(facet);
        }
      );
    }

    $scope.toggleShowFacet = function(facet) {
      if (facet.show) {
        $timeout(
          function() {
            facet.show = true;
            facet.isOpen = true;
            $scope.toggleFacetValues(facet);
          }
        );
      } else {
        facet.show = false;
        $scope.clearFacetValueSelection(null, facet);
      }
    }

    $scope.toggleFacetValueSelection = function(facet) {
      angular.forEach($scope.queryCtx.filters, function(filter) {
        if (filter.id != facet.id) {
          return;
        }

        filter.op = QueryUtil.getOp('qin');
        filter.value = facet.values
          .filter(function(value) { return value.selected })
          .map(function(value) { return value.value });
        facet.selectedValues = filter.value;

        if (facet.selectedValues.length == 0) {
          filter.op = QueryUtil.getOp('any');
          filter.value = undefined;
        }
      });

      loadRecords(false);
    }

    $scope.clearFacetValueSelection = function($event, facet) {
      if ($event) {
        $event.stopPropagation();
      }

      angular.forEach(facet.values, function(value) {
        value.selected = false;
      });

      $scope.toggleFacetValueSelection(facet);
    }

    init();
  });
