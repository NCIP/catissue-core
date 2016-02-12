
angular.module('os.query.results', ['os.query.models'])
  .filter('osFacetRange', function() {
    return function(input) {
      var in0 = angular.isDefined(input[0]);
      var in1 = angular.isDefined(input[1]);
      if (in0 && in1) {
        return input[0] + " - " + input[1];
      } else if (in0) {
        return '>= ' + input[0];
      } else if (in1) {
        return '<= ' + input[1];
      } else {
        return 'Err';
      }
    }
  })
  .controller('QueryResultsCtrl', function(
    $scope, $state, $stateParams, $modal, $document, $timeout,
    queryCtx, cps, QueryCtxHolder, QueryUtil, QueryExecutor, SpecimenList, SpecimensHolder, Alerts) {

    var STR_FACETED_OPS = ['eq', 'qin', 'exists', 'any'];

    var RANGE_FACETED_OPS = ['le', 'ge', 'eq', 'between', 'exists', 'any'];

    function isNumber(val) {
      return !isNaN(val) && angular.isNumber(val);
    }

    function identity(val) {
      return val;
    }

    function isNotBlankStr(val) {
      return angular.isString(val) && val.trim().length > 0;
    }

    var RANGE_FNS = {
      'INTEGER': {
        parse   : parseInt,
        isValid : isNumber
      },

      'FLOAT': {
        parse   : parseFloat,
        isValid : isNumber
      },

      'DATE': {
        parse   : identity,
        isValid : isNotBlankStr
      }
    };

    function init() {
      $scope.queryCtx = queryCtx;
      $scope.cps = cps;
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
        showFooter        : false,
        totalServerItems  : 'resultsCtx.numRows',
        plugins           : [/*gridFilterPlugin*/],
        headerRowHeight   : 39,
        selectedItems     : $scope.selectedRows,
        enablePaging      : false,
        rowHeight         : 36,
        rowTemplate       :'<div ng-style="{\'cursor\': row.cursor, \'z-index\': col.zIndex() }" ' +
                              'ng-repeat="col in renderedColumns" ng-class="col.colIndex()" ' +
                              'class="ngCell {{col.cellClass}}" ng-cell> ' +
                           '</div>'
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
      QueryExecutor.getRecords(undefined, qc.selectedCp.id, getAql(true), qc.wideRowMode || 'DEEP').then(
        function(result) {
          $scope.resultsCtx.waitingForRecords = false;
          if (qc.reporting.type == 'crosstab') {
            preparePivotTable(result);
          } else {
            var showColSummary = qc.reporting.type == 'columnsummary';
            prepareDataGrid(showColSummary, result);
            $scope.resultsCtx.gridOpts.headerRowHeight = showColSummary ? 66 : 39;
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
          if (!filter.parameterized) {
            return;
          }

          var type = undefined;
          if (!!filter.expr) {
            var tObj = filter.tObj = QueryUtil.getTemporalExprObj(filter.expr);
            filter.op = QueryUtil.getOpBySymbol(tObj.op);
            if (tObj.op == 'between') {
              var rhs = tObj.rhs.trim();
              filter.value = JSON.parse('[' + rhs.substring(1, rhs.length - 1) + ']');
            } else {
              filter.value = tObj.rhs;
            }

            type = 'INTEGER';
          } else {
            type = filter.field.type;
          }

          switch (type) {
            case 'STRING':
              if (STR_FACETED_OPS.indexOf(filter.op.name) == -1) {
                return;
              }
              break;

            case 'INTEGER':
            case 'FLOAT':
            case 'DATE':
              if (RANGE_FACETED_OPS.indexOf(filter.op.name) == -1) {
                return;
              }
              break;

            default:
              return;
          }

          facets.push(getFacet(filter, index));
        }
      );

      $scope.resultsCtx.facets = facets;
      $scope.resultsCtx.hasFacets = (facets.length > 0);
    }

    function getFacet(filter, index) {
      var values = undefined;
      var type = !!filter.expr ? 'INTEGER' : filter.field.type;
      var isRangeType = !!RANGE_FNS[type];

      if (isRangeType) {
        var value = undefined;
        switch (filter.op.name) {
          case 'eq': value = [filter.value, filter.value]; break;
          case 'le': value = [undefined, filter.value]; break;
          case 'ge': value = [filter.value, undefined]; break;
          case 'between': value = filter.value; break;
        }

        if (value) {
          values = [{value: value, selected: true}];
        }
      } else if (filter.field.type == 'STRING') {
        if (typeof filter.value == "string" && filter.value.length > 0) {
          values = [{value: filter.value, selected: false}];
        } else if (filter.value instanceof Array) {
          values = filter.value.map(
            function(val) {
              return {value: val, selected: false};
            }
          );
        }
      }

      return {
        id: filter.id,
        caption: !!filter.expr ? filter.desc : filter.field.caption,
        dataType: type,
        isRange: isRangeType,
        expr: !!filter.expr ? filter.expr : (filter.form.name + "." + filter.field.name),
        type: type,
        values: values,
        valuesQ: undefined,
        selectedValues: [],
        subset: !!values,
        isOpen: false
      };
    }

    function loadFacetValues(facet) {
      if (facet.valuesQ || facet.values || facet.dataType != 'STRING') {
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
      var width = span[0].offsetWidth + 2 + 8 * 2; // 8 + 8 is padding, 2 is buffer/uncertainity
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

    function loadCpCatalog(cp) {
      if (!cp.catalogQuery) {
        Alerts.error("queries.no_catalog", cp);
        return;
      }

      $state.go('query-results', {cpId: cp.id, queryId: cp.catalogQuery.id});
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

    $scope.toggleFacetValueSelection = function(facet) {
      angular.forEach($scope.queryCtx.filters, function(filter) {
        if (filter.id != facet.id) {
          return;
        }

        var rangeFns = RANGE_FNS[facet.dataType];
        if (!!rangeFns) {
          var minMax = [undefined, undefined];
          if (facet.values[0].selected) {
            minMax = facet.values[0].value;
          }

          var validMin = rangeFns.isValid(minMax[0]);
          var validMax = rangeFns.isValid(minMax[1]);

          if (validMin && validMax) {
            filter.value = minMax;
            filter.op = QueryUtil.getOp('between');
          } else if (validMin && !validMax) {
            filter.value = minMax[0];
            filter.op = QueryUtil.getOp('ge');
          } else if (!validMin && validMax) {
            filter.value = minMax[1];
            filter.op = QueryUtil.getOp('le');
          } else {
            filter.value = undefined;
            filter.op = QueryUtil.getOp('any');
          }

          if (!filter.tObj) {
            return;
          }

          // temporal expression
          var tObj = filter.tObj;
          filter.expr = tObj.lhs + ' ' + filter.op.symbol + ' ';
          if (filter.value instanceof Array) {
            filter.expr += "(" + filter.value.join() + ")";
          } else if (angular.isDefined(filter.value)) {
            filter.expr += filter.value;
          }
        } else {
          filter.op = QueryUtil.getOp('qin');
          filter.value = facet.values
            .filter(function(value) { return value.selected })
            .map(function(value) { return value.value });
          facet.selectedValues = filter.value;

          if (facet.selectedValues.length == 0) {
            if (facet.subset) {
              filter.op = QueryUtil.getOp('qin');
              filter.value = facet.values.map(function(val) { return val.value; });
            } else {
              filter.op = QueryUtil.getOp('any');
              filter.value = undefined;
            }
          }
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

    $scope.addRangeCond = function(facet) {
      var fns = RANGE_FNS[facet.dataType];

      var min = fns.parse(facet.min);
      if (!fns.isValid(min)) {
        min = undefined;
      }

      var max = fns.parse(facet.max);
      if (!fns.isValid(max)) {
        max = undefined;
      }

      facet.min = facet.max = '';
      if (min == undefined && max == undefined) {
        return;
      }

      facet.values = [{value: [min, max], selected: true}];
      $scope.toggleFacetValueSelection(facet);
    }

    $scope.switchCatalog = function(cp) {
      if (!cp.catalogQuery) {
        cp.getCatalogQuery().then(
          function(query) {
            cp.catalogQuery = query;
            loadCpCatalog(cp);
          }
        );
      } else {
        loadCpCatalog(cp);
      }
    }

    init();
  });
