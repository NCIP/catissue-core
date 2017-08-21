
angular.module('plus.controllers', ['checklist-model', 'ui.app'])
  .controller('QueryController', 
    ['$scope', '$sce', '$modal', '$q', '$timeout', 
     'CollectionProtocolService', 'FormsService', 'QueryService', 'UsersService', 'SpecimenListsService', 
     function($scope, $sce, $modal, $q, $timeout, CollectionProtocolService, FormsService, QueryService, UsersService, SpecimenListsService) {

    var ops = {
      eq: {name: "eq", desc: "Equals", code: "&#61;", symbol: '=', model: 'EQ'}, 
      ne: {name: "ne", desc: "Not Equals", code: "&#8800;", symbol: '!=', model: 'NE',},
      lt: {name: "lt", desc: "Less than", code: "&#60;", symbol: '<', model: 'LT'}, 
      le: {name: "le", desc: "Less than or Equals", code: "&#8804;", symbol: '<=', model: 'LE'}, 
      gt: {name: "gt", desc: "Greater than", code: "&#62;", symbol: '>', model: 'GT'}, 
      ge: {name: "ge", desc: "Greater than or Equals", code:"&#8805;", symbol: '>=', model: 'GE'},
      exists: {name: "exists", desc: "Exists", code: "&#8707;", symbol: 'exists', model: 'EXISTS'}, 
      not_exists: {name: "not_exists", desc: "Not Exists", code: "&#8708;", symbol: 'not exists', model: 'NOT_EXISTS'}, 
      qin: {name: "qin", desc: "Is One Of", code:"&#8712;", symbol: 'in', model: 'IN'}, 
      not_in: {name: "not_in", desc: "Is Not One Of", code:"&#8713;", symbol: 'not in', model: 'NOT_IN'},
      starts_with: {name: "starts_with", desc: "Starts With", code: "&#8963;&#61;", symbol: 'starts with', model: 'STARTS_WITH'}, 
      ends_with: {name: "ends_with", desc: "Ends With", code: "&#36;&#61;", symbol: 'ends with', model: 'ENDS_WITH'}, 
      contains: {name: "contains", desc: "Contains", code: "&#126;", symbol: 'contains', model: 'CONTAINS'},
      and: {name: 'and', desc: 'And', code: 'and', symbol: 'and', model: 'AND'},
      or: {name: 'or', desc: 'Or', code: 'or', symbol: 'or', model: 'OR'},
      intersect: {name: 'intersect', desc: 'Intersection', code: '&#8745;', symbol: 'pand', model: 'PAND'},
      not: {name: 'not', desc: 'Not', code: 'not', symbol: 'not', model: 'NOT'},
      nthchild: {name: 'nthchild', desc: 'Any child', code: '&#xf1e0;', symbol: 'nthchild', model: 'NTHCHILD'},
      between: {name: 'between', desc: 'Between', code: '&#xf1e0;', symbol: 'between', model: 'BETWEEN'}
    };

    var getOp = function(fn) {
      var result = undefined;
      for (var k in ops) {
        if (fn(ops[k])) {
          result = ops[k];
          break;
        }
      }

      return result;
    };

    var getOpBySymbol = function(symbol) {
      return getOp(function(op) { return op.symbol == symbol });
    };
      
    var getOpByModel = function(model) {
      return getOp(function(op) { return op.model == model });
    };

    var hidePopovers = function() {
      var popups = document.querySelectorAll('div.popover');
      for (var i = 0; i < popups.length; i++) {
        var popup = angular.element(popups[i]);
        popup.scope().tt_isOpen = false;
        popup.remove();
      }
    };

    var getValidOps = function(field) {
      var result = null;
      if (field.type == "STRING") {
        result = getStringOps();
      } else {
        result = getNumericOps();
      } 
      return result;
    };

    var getStringOps = function() {
      return [ops.eq, ops.ne, ops.exists, ops.not_exists, ops.starts_with, ops.ends_with, ops.contains, ops.qin, ops.not_in];
    };

    var getNumericOps = function() {
      return [ops.eq, ops.ne, ops.lt, ops.le, ops.gt, ops.ge, ops.exists, ops.not_exists, ops.qin, ops.not_in, ops.between];
    };

    var getFilterMap = function(filters) {
      var filterMap = {};
      for (var i = 0; i < filters.length; ++i) {
        filterMap[filters[i].id] = filters[i];
      }

      return filterMap;
    };

    var getWhereExpr = function(filterMap, exprNodes) {
      var query = "";
      for (var i = 0; i < exprNodes.length; ++i) {
        if (exprNodes[i].type == 'paren') {
          query += exprNodes[i].value;
        } else if (exprNodes[i].type == 'op') {
          query += ops[exprNodes[i].value].symbol + " ";
        } else if (exprNodes[i].type == 'filter') {
          var filter = filterMap[exprNodes[i].value];
          if (filter.expr) {
            query += " " + filter.expr + " ";
            continue;
          }
          
          query += filter.form.name + "." + filter.field.name + " ";
          query += filter.op.symbol + " ";

          if (filter.op.name == 'exists' || filter.op.name == 'not_exists') {
            continue;
          }

          var queryVal = filter.value;
          if (filter.field.type == "STRING" || filter.field.type == "DATE") {
            if (filter.op.name == 'qin' || filter.op.name == 'not_in' || filter.op.name == 'between') {
              var quotedValues = [];
              for (var j = 0; j < queryVal.length; ++j) {
                quotedValues.push("\"" + queryVal[j] + "\"");
              }
              queryVal = quotedValues;
            } else {
              queryVal = "\"" + queryVal + "\"";
            }
          }

          if (filter.op.name == 'qin' || filter.op.name == 'not_in' || filter.op.name == 'between') {
            queryVal = "(" + queryVal.join() + ")";
          }

          query += queryVal + " ";
        }
      }
      return query;
    };

    var isValidQueryExpr = function(exprNodes) {
      var parenCnt = 0, next = 'filter', last = 'filter';

      for (var i = 0; i < exprNodes.length; ++i) {
        var exprNode = exprNodes[i];

        if (exprNode.type == 'paren' && exprNode.value == '(') {
          ++parenCnt;  
          continue;
        } else if (exprNode.type == 'paren' && exprNode.value == ')' && last != 'op') {
          --parenCnt;
          if (parenCnt < 0) {
            return false;
          }
          continue;
        } else if (exprNode.type == 'op' && exprNode.value == 'nthchild' && next == 'filter') { 
          if (i + 1 < exprNodes.length) {
            var nextToken = exprNodes[i + 1];
            if (nextToken.type == 'paren' && nextToken.value == '(') {
              ++parenCnt;
              ++i;
              last = 'op';
              continue;
            }
          }

          return false; 
        } else if (exprNode.type == 'op' && exprNode.value == 'not' && next == 'filter') {
          last = 'op';
          continue;
        } else if (exprNode.type == 'op' && next != 'op') {
          return false;
        } else if (exprNode.type == 'filter' && next != 'filter') {
          return false;
        } else if (exprNode.type == 'op' && next == 'op' && exprNode.value != 'not' && exprNode.value != 'nthchild') {
          next = 'filter';
          last = 'op';
          continue;
        } else if (exprNode.type == 'filter' && next == 'filter') {
          next = 'op';
          last = 'filter';
          continue;
        } else {
          return false;
        }
      }

      return parenCnt == 0 && last == 'filter';
    };

    $scope.filterCpForm = function(form) {
      return $scope.queryData.selectedCp.id == -1 || form.name != 'CollectionProtocol';
    };

    var getCount= function(queryData) {
      var query = getWhereExpr(queryData.filtersMap, queryData.exprNodes);
      var aql = "select count(distinct Participant.id) as \"cprCnt\", count(distinct Specimen.id) as \"specimenCnt\" where " + query;
      queryData.notifs.showCount = true;
      queryData.notifs.waitCount = true;

      var cpId = queryData.selectedCp.id;
      var queryId = queryData.id;
      QueryService.executeQuery(queryId, cpId, 'Participant', aql, 'Count').then(
        function(result) {
          if (result.status != 'OK') {
            queryData.notifs.error = result.status;
          } else {
            queryData.cprCnt  = result.rows[0][0];
            queryData.specimenCnt = 0;
            for (var i = 1; i < result.rows[0].length; ++i) {
              queryData.specimenCnt += parseInt(result.rows[0][i]);
            }
            queryData.notifs.error = "";
          }
          queryData.notifs.waitCount = false;
        });
    };

    $scope.getCount= function() {
      getCount($scope.queryData);
    };


    $scope.setPagedData = function(pageNo, recCnt) {
      /* Commented out as end users do not like pagination enabled 
      var pageRows = $scope.queryData.resultData.slice((pageNo - 1) * recCnt, pageNo * recCnt);
      */
      var pageRows = $scope.queryData.resultData;
      var formatedRows = [];
       
      for (var i = 0; i < pageRows.length; ++i) {
        var row = {};
        for (var j = 0; j < pageRows[i].length; ++j) {
          row["col" + j] = pageRows[i][j];
        }
        formatedRows.push(row);
      }

      $scope.queryData.pagedData = formatedRows;
    };

    $scope.$watch('queryData.pagingOptions', function(newVal, oldVal) {
      if (newVal !== oldVal && (newVal.currentPage !== oldVal.currentPage || newVal.pageSize !== oldVal.pageSize)) {
        $scope.setPagedData(newVal.currentPage, newVal.pageSize);
      }
    }, true);

    var getTemporalExprObj = function(temporalExpr) {
      var re = /<=|>=|<|>|=|!=/g
      var matches = undefined;
      if ((matches = re.exec(temporalExpr))) {
        return {
          lhs: temporalExpr.substring(0, matches.index),
          op : matches[0],
          rhs: temporalExpr.substring(matches.index + matches[0].length)
        }
      }

      return {};
    };

    var getSelectList = function(filterMap, selectedFields) {
      var result = "";

      for (var i = 0; i < selectedFields.length; ++i) {
        var field = selectedFields[i];

        if (typeof field == "string") {
          field = getFieldExpr(filterMap, field, true);
        } else if (typeof field != "string") {
          if (field.aggFns && field.aggFns.length > 0) {
            var fieldExpr = getFieldExpr(filterMap, field.name);
            var fnExprs = "";
            for (var j = 0; j < field.aggFns.length; ++j) {
              if (fnExprs.length > 0) {
                fnExprs += ", ";
              }

              var aggFn = field.aggFns[j];
              if (aggFn.name == 'count') {
                fnExprs += 'count(distinct ';
              } else {
                fnExprs += aggFn.name + '(';
              }

              fnExprs += fieldExpr + ") as \"" + aggFn.desc + " \"";
            }

            field = fnExprs;
          } else {
            field = getFieldExpr(filterMap, field.name, true);
          }
        }

        result += field + ", ";
      }

      if (result) {
        result = result.substring(0, result.length - 2);
      }

      return result;
    };

    var getFieldExpr = function(filterMap, fieldName, includeDesc) {
      var temporalMarker = '$temporal.';
      if (fieldName.indexOf(temporalMarker) != 0) {
        return fieldName;
      }

      var filterId = fieldName.substring(temporalMarker.length);
      var filter = filterMap[filterId];
      var expr = getTemporalExprObj(filter.expr).lhs;
      if (includeDesc) {
        expr += " as \"" + filter.desc + "\"";
      }

      return expr;
    };

    var showAddToSpecimenList = function(selectList) {
      if ($scope.queryData.reporting.type == 'crosstab') {
        return false;
      }

      for (var i = 0; i < $scope.queryData.selectedFields.length; ++i) {
        if ($scope.queryData.selectedFields[i] === 'Specimen.label') {
          return true;
        }
      }
      return false;
    };

    var getFieldIndices = function(fields, reportFields) {
      var idx = [];

      if (!reportFields) {
        return idx;
      }

      for (var i = 0; i < reportFields.length; ++i) {
        var rptField = reportFields[i];
        for (var j = 0; j < fields.length; ++j) {
          var selField = fields[j];
          selField = (typeof selField == "string") ? selField : selField.id;

          if (selField == rptField.id) {
            idx.push(j + 1);
            break;
          }
        }
      }

      return idx;
    };

    var getPivotTabFields = function(selectedFields, fresh) {
      var reportFields = [];

      for (var i = 0; i < selectedFields.length; ++i) {
        var field = selectedFields[i];
        var isAgg = false;
        var len = field.aggFns ? field.aggFns.length : 0;
 
        for (var j = 0; j < len; ++j) {
          var aggFn = field.aggFns[j];
          if (fresh || aggFn.opted) {
            reportFields.push({
              id: field.name + '$' + aggFn.name,
              name: field.name,
              value: aggFn.desc,
              aggFn: aggFn.name
            });
            isAgg = true;
          }
        }

        if (!isAgg) {
          reportFields.push({id: field.name, name:  field.name, value: field.form + ": " + field.label});
        }
      }

      return reportFields;
    };

    var getRptExpr = function() {
      var reporting = $scope.queryData.reporting;
      if (!reporting || reporting.type == 'none') {
        return '';
      }

      if (reporting.type != 'crosstab') {
        return reporting.type;
      }

      var pivotTabFields = getPivotTabFields($scope.queryData.selectedFields, true);
      var rowIdx = getFieldIndices(pivotTabFields, reporting.params.groupRowsBy);
      var colIdx = getFieldIndices(pivotTabFields, [reporting.params.groupColBy]);
      colIdx = colIdx.length > 0 ? colIdx[0] : undefined;
      var summaryIdx = getFieldIndices(pivotTabFields, reporting.params.summaryFields);

      var includeSubTotals = "";
      if (reporting.params.includeSubTotals) {
        includeSubTotals = ", true";
      } 

      return 'crosstab((' + rowIdx.join(',') + '), ' + colIdx + ', (' + summaryIdx.join(',') + ') ' + includeSubTotals + ')';
    };

    $scope.getRecords = function() {
      $scope.queryData.view = 'records';

      var filterMap = getFilterMap($scope.queryData.filters);
      var query = getWhereExpr(filterMap, $scope.queryData.exprNodes);
      var selectList = getSelectList(filterMap, $scope.queryData.selectedFields);
      var rptExpr = getRptExpr();
      var aql = "select " + selectList + " where " + query + " limit 0, 10000 " + rptExpr;

      $scope.showAddToSpecimenList = showAddToSpecimenList(selectList);
      $scope.queryData.notifs.waitRecs = true;
      var cpId = $scope.queryData.selectedCp.id;
      var queryId = $scope.queryData.id;
      QueryService.executeQuery(queryId, cpId, 'Participant', aql, 'Data', true).then(function(result) {
        if (result.status != 'OK') {
          $scope.queryData.notifs.error = result.status;
          $scope.queryData.notifs.waitRecs = false;
          return;
        }

        if ($scope.queryData.reporting.type == 'crosstab') {
          showPivotTable(result);
        } else {
          showDataGrid(result);
        }

        $scope.queryData.notifs.waitRecs = false;
        $scope.queryData.notifs.error = '';
        $scope.queryData.moreData = (result.dbRowsCount >= 10000);
        $scope.selectedRows.length = 0;

      });

      if ($scope.showAddToSpecimenList && $scope.queryData.myLists === undefined) {
        SpecimenListsService.getSpecimenLists().then(
          function(lists) {
            $scope.queryData.myLists = lists;
          }
        );
      }
    };

    var showPivotTable = function(result) {
      $scope.queryData.resultData = result.rows;
      $scope.queryData.resultCols = result.columnLabels;

      $scope.queryData.pivotTableOpts = {
        height: '500px',
        width: '1200px',
        title: $scope.queryData.title || "Unsaved Query",
        colHeaders: $scope.queryData.resultCols,
        numGrpCols: $scope.queryData.reporting.params.groupRowsBy.length,
        multipleVal: $scope.queryData.reporting.params.summaryFields.length > 1,
        data: $scope.queryData.resultData
      };
    };

    var showDataGrid = function(result) {
      var colDefs = [];
      for (var i = 0; i < result.columnLabels.length; ++i) {
        colDefs.push({
          field: "col" + i, 
          displayName: result.columnLabels[i],
          width: 100, 
          headerCellTemplate: 'templates/grid-column-filter.html'
        });
      }
      $scope.queryData.resultData = result.rows;
      $scope.queryData.resultCols = colDefs;
      $scope.queryData.labelIndices = result.columnIndices;
      $scope.queryData.resultDataSize = result.rows.length;
      $scope.queryData.pagingOptions.pageSize = 100;
      $scope.queryData.pagingOptions.currentPage = 1;

        
      $scope.setPagedData(1, 100);
    };

    $scope.selectAllRows = function() {
      $scope.queryData.resultGridOpts.selectAll(true);
      $scope.queryData.selectAll = true;
    };

    $scope.unSelectAllRows = function() {
      $scope.queryData.resultGridOpts.selectAll(false);
      $scope.queryData.selectAll = false;
    };

    $scope.createNewSpecimenList = function() {
      var specimenLabels = getSpecimenLabels();

      var modalInstance = $modal.open({
        templateUrl: 'create-specimen-list.html',
        controller: CreateSpecimenListCtrl,
        resolve: {
          specimenLabels: function() {
            return specimenLabels;
          }
        }
      });

      modalInstance.result.then(
        function(list) {
          if (list) {
            Utility.notify($("#notifications"), "New specimen list created successfully", "success", true);
            $scope.queryData.myLists.push(list);
            list.specimens = undefined;
            $scope.selectedRows.splice(0, $scope.selectedRows.length);
            $scope.queryData.resultGridOpts.selectAll(false);
            $scope.queryData.selectAll = false;
          } else {
            Utility.notify(
              $("#notifications"), 
              "Error creating specimen list. Please contact system administrator", "error", true);
          }
        }
      );
    };

    var CreateSpecimenListCtrl = function($scope, $modalInstance, specimenLabels) {
      $scope.specimenList = {name: ""};
      $scope.saveSpecimenList = function() {
        var specimens = [];
        if (specimenLabels) {
          for (var i = 0; i < specimenLabels.length; ++i) {
            specimens.push({label: specimenLabels[i]});
          }
        }

        $scope.specimenList.specimens = specimens;
        SpecimenListsService.saveOrUpdate($scope.specimenList).then(
          function(specimenList) {
            $modalInstance.close(specimenList);
          }
        );
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      }
    };

    var getSpecimenLabels = function() {
      var labelIndices = $scope.queryData.labelIndices;
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

      return specimenLabels;
    };

    $scope.addSpecimensToList = function(list) {
      var specimenLabels = getSpecimenLabels();
      if (specimenLabels.length == 0) {
        return;
      }

      SpecimenListsService.addSpecimensToList(list.id, specimenLabels).then(
        function(labels) {
          if (!labels) {
            Utility.notify($("#notifications"), "Failed to add specimens to list: " + list.name, "error", true);
            return;
          }

          Utility.notify(
            $("#notifications"), 
            labels.length + " specimens added to list: " + list.name, 
            "success", true);

          $scope.selectedRows.splice(0, $scope.selectedRows.length);
          $scope.queryData.resultGridOpts.selectAll(false);
          $scope.queryData.selectAll = false;
        }
      );
    };

    $scope.exportRecords = function() {
      var filterMap = getFilterMap($scope.queryData.filters);
      var query = getWhereExpr(filterMap, $scope.queryData.exprNodes);
      var selectList = getSelectList(filterMap, $scope.queryData.selectedFields);
      var rptExpr = getRptExpr();
      var aql = "select " + selectList + " where " + query + " " + rptExpr;

      Utility.notify(
        $("#notifications"), 
        "Query results export has been initiated. Export file download should start in few moments...", 
        "success", 
        false);

      var cpId = $scope.queryData.selectedCp.id;
      var queryId = $scope.queryData.id;
      QueryService.exportQueryData(queryId, cpId, 'Participant', aql, 'Export', true).then(
        function(result) {
          if (result.completed) {
            Utility.notify($("#notifications"), "Downloading query results export data file.", "success", true);
            QueryService.downloadQueryData(result.dataFile);
          } else if (result.dataFile) {
            Utility.notify(
              $("#notifications"),
              "Export is taking longer time to finish. Link to download exported data will be sent to you by e-mail.",
              "info",
              true);
          } else {
            Utility.notify(
              $("#notifications"),
              "Error initiating export of query results. Please consult system administrator.",
              "error",
              true);
          }
        });
    };
         

    $scope.closeNotif = function(type) {
      $scope.queryData.notifs[type] = false;
    };

    $scope.redefineQuery = function() {
      clearResultData();
      $scope.queryData.view = 'query';
    };

    var clearResultData = function() {
      $scope.queryData.pagedData.length = 0;
      $scope.queryData.resultData.length = 0;
      $scope.queryData.resultCols.length = 0;
      $scope.queryData.labelIndices.length = 0;
      $scope.queryData.resultDataSize = 0;
      $scope.queryData.moreData = false;
    };

    $scope.addParen = function() {
      var node1 = {type: 'paren', value: '('};
      $scope.queryData.exprNodes.unshift(node1);
      var node2 = {type: 'paren', value: ')'};
      $scope.queryData.exprNodes.push(node2);
    };

    $scope.addOp = function(op) {
      var node = {type: 'op', value: op};
      $scope.queryData.exprNodes.push(node);
      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
    };

    $scope.opDrag = function(e) {
      var op = angular.element(e.currentTarget).attr('data-arg');
      if (!op) {
        return;
      }

      var cls = op == '()' ? 'paren-node' : 'op-node';
      op = (op == '()') ? op : ops[op].code;
      return angular.element("<div/>").addClass("pull-left")
               .append(angular.element("<div/>").addClass("filter-item-valign").addClass(cls).html(op));
    };

    $scope.toggleOp = function(index) {
      var node = $scope.queryData.exprNodes[index];
      if (node.value == 'and') {
        node.value = 'or';
      } else if (node.value == 'or') {
        node.value = 'intersect';
      } else if (node.value == 'intersect') {
        node.value = 'and';
      } 
    };

    $scope.isFilterParamterized = function(filterId) {
      var filter = $scope.queryData.filtersMap[filterId];
      return filter && filter.parameterized;
    };

    $scope.getFilterDesc = function(filterId) {
      var filter = $scope.queryData.filtersMap[filterId];
      var desc = "Unknown";
      if (filter && filter.expr && filter.desc) {
        desc = filter.desc;
      } else if (filter && filter.form && filter.field && filter.op) {
        desc = "<i>" + filter.form.caption + "  >> " + filter.field.caption + "</i> <b>" + filter.op.desc + "</b> ";
        if (filter.value) {
          desc += filter.value;
        }
      }

      return desc;
    };

    $scope.exprSortOpts = {
      placeholder: 'sortablePlaceholder',
      stop: function(event, ui) {
        if (ui.item.attr('class').indexOf('btn') < 0) {
          $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
          $scope.$apply($scope.queryData);
          return;
        }
      
        var nodeVal = ui.item.attr('data-arg');
        if (nodeVal == '()') {
          var node1 = {type: 'paren', value: '('};
          var node2 = {type: 'paren', value: ')'};
          $scope.queryData.exprNodes.splice(ui.item.index(), 1, node1, node2);
        } else {
          var node = {type: 'op', value: nodeVal};
          $scope.queryData.exprNodes[ui.item.index()] = node;
        }
         
        $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
        $scope.$apply($scope.queryData);
        ui.item.remove();
      }
    };

    $scope.removeNode = function(idx) {
      $scope.queryData.exprNodes.splice(idx, 1);
      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
    };

    var getDefaultSelectedFields = function() {
      return [
        "Participant.id", "Participant.firstName", "Participant.lastName", 
        "Participant.dateOfBirth", "Participant.ssn", "Participant.gender",
        "Participant.genotype", "Participant.race", "Participant.regDate",
        "Participant.ppid", "Participant.activityStatus"
      ];
    }

    $scope.formatDate = function(timeInMs) {
      return Utility.formatDate(timeInMs);
    };

    $scope.formatUsername = function(user) {
      var username = user.lastName;
      if (username && user.firstName) {
        username += ", ";
      }

      if (user.firstName) {
        username += user.firstName;
      }

      return username;
    };

    $scope.queryData = {
      userId: query.global.userId,
      isAdmin: query.global.isAdmin,
      datePickerOpts: {format: query.global.dateFormat},
      view: 'dashboard',
      cpList: [],
      forms: undefined,
      selectedFolderId: -1,
      pagingOptions: {
        pageSizes: [100, 200, 500],
        pageSize: 100,
        currentPage: 1
      },
      myLists: undefined,
      selectAll: false,
      reporting: {type: 'none', params: {}}
    };
    $scope.selectedRows = [];

    var startLoading = function() {
      $scope.queryData.notifs.loading = true;
    };

    var stopLoading = function() {
      $scope.queryData.notifs.loading = false;
    };

    var isParameterized = function(filters) {
      for (var i = 0; i < filters.length; ++i) {
        if (filters[i].parameterized) {
          return true;
        }
      }

      return false;
    };

    $scope.getQueryDataDefaults = function() {
      return {
        isValid: true,
        id: undefined,
        title: undefined,
        disableCpSelection: false,
        drivingForm: 'Participant',
        selectedCp: $scope.queryData.cpList[0], // assuming cp list is loaded
        selectedForm: null,
        selectedFields: getDefaultSelectedFields(),
        filters: [],
        filtersMap: {},
        joinType: 'all',
        exprNodes: [],              //{type: filter/op/paran, val: filter id/and-or-intersect-not}
        filterId: 0,
        currFilter: {
          id:null,
          form: null,
          field: null,
          op: null,
          value: null,
          ops: null
        },

        notifs: {
          showCount: false,
          waitCount: true,
          waitRecs: true
        },

        resultData: [],
        pagedData: [],
        resultCols: [],
        labelIndices: [],
        selectAll: false,
        resultDataSize: 0,
        moreData: false,

        reporting: {type: 'none', params: {}}
      };
    };

    
    angular.extend($scope.queryData, $scope.getQueryDataDefaults());

    var gridFilterPlugin = {
      init: function(scope, grid) {
        gridFilterPlugin.scope = scope;
        gridFilterPlugin.grid = grid;
        $scope.$watch(
          function() {
            var searchQuery = "";
            angular.forEach(gridFilterPlugin.scope.columns, function(col) {
              if (col.visible && col.filterText) {
                var filterText = (col.filterText.indexOf('*') == 0 ? col.filterText.replace('*', '') : col.filterText) + ";";
                searchQuery += col.displayName + ": " + filterText;
              }
            });
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

    $scope.queryData.resultGridOpts = {
      enableColumnResize: true,
      columnDefs: 'queryData.resultCols',
      showFooter: true, 
      data: 'queryData.pagedData',
      enablePaging: true,
      pagingOptions: $scope.queryData.pagingOptions,
      totalServerItems: 'queryData.resultDataSize',
      plugins: [gridFilterPlugin],
      headerRowHeight: 70,
      selectedItems: $scope.selectedRows,
      enablePaging: false
    };

    $scope.loadFolders = function() {
      QueryService.getFolders().then(function(folders) {
        var my = [], shared = [];
        for (var i = 0; i < folders.length; ++i) {
          if (folders[i].owner.id == query.global.userId) {
            my.push(folders[i]);
          } else {
            shared.push(folders[i]);
          }
        }
        $scope.queryData.myFolders = my;
        $scope.queryData.sharedFolders = shared;
      });
    };

    $scope.showQueries = function() {
      clearResultData();
      $scope.queryData = angular.extend(
        $scope.queryData, 
        {queries: [], selectedQueries: [], selectedFolderId: -1,
         myFolders: [], sharedFolders: [], 
         pageSize: 25, currentPage: 1, view: 'dashboard'});

      $scope.selectFolder(-1);
      $scope.loadFolders();
    };


    $scope.addQueriesToFolder = function(folder) {
      var queryIds = [];
      for (var i = 0; i < $scope.queryData.selectedQueries.length; ++i) {
        queryIds.push($scope.queryData.selectedQueries[i].id);
      }

      QueryService.addQueriesToFolder(folder.id, queryIds).then(
        function(data) {
          if ($scope.queryData.selectedQueries.length == 1) {
            Utility.notify($("#notifications"), "Query successfully assigned to " + folder.name, "success", true);
          } else {
            Utility.notify($("#notifications"), "Queries successfully assigned to " + folder.name, "success", true);
          }
        });
    };

    $scope.removeQueriesFromFolder = function() {
      var queryIds = [];
      for (var i = 0; i < $scope.queryData.selectedQueries.length; ++i) {
        queryIds.push($scope.queryData.selectedQueries[i].id);
      }

      QueryService.removeQueriesFromFolder($scope.queryData.selectedFolderId, queryIds).then(
        function(data) {
          $scope.selectFolder($scope.queryData.selectedFolderId);
          Utility.notify($("#notifications"), "Queries successfully deleted from folder", "success", "true");
        }
      );
    };

    $scope.selectFolder = function(folderId) {
      $scope.queryData.selectedQueries = [];
      $scope.queryData.currentPage = 1;
      $scope.queryData.selectedFolderId = folderId;
      $scope.changeQueriesPage(true);
    };

    var srchTimeout = undefined;
    $scope.searchQueries = function() {
      if (srchTimeout) {
        $timeout.cancel(srchTimeout);
      }

      srchTimeout = $timeout(function() {
        if ($scope.queryData.searchQueryTitle == $scope.queryData.tempSearchString) {
          return;
        }

        $scope.queryData.searchQueryTitle = $scope.queryData.tempSearchString;
        $scope.changeQueriesPage(true);
      }, 1000);
    };
    
    $scope.changeQueriesPage = function(countReq) {
      var folderId = $scope.queryData.selectedFolderId;
      var searchString = $scope.queryData.searchQueryTitle
      $scope.queryData.startAt = ($scope.queryData.currentPage - 1) * $scope.queryData.pageSize;
      
      var q;
      if(folderId == -1) {
        q = QueryService.getQueries(countReq, $scope.queryData.startAt, $scope.queryData.pageSize, searchString);
      } else {
        q = QueryService.getFolderQueries(folderId, countReq, $scope.queryData.startAt, $scope.queryData.pageSize, searchString);
      }

      q.then(
        function(result) {
          $scope.queryData.selectedQueries = [];

          if (countReq) {
            $scope.queryData.totalQueries = result.count;
          }
          $scope.queryData.queries = result.queries;
        });
    };

    $scope.deleteQuery = function(query) {
      var modalInstance = $modal.open({
        templateUrl: 'delete-query-confirm.html',
        controller: function($scope, $modalInstance, query) {
          $scope.query = query;

          $scope.ok = function() { 
            $modalInstance.close('ok');
          },

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        },
        resolve: {
          query: function() {
            return query;
          }
        }
      });

      modalInstance.result.then(
        function() {
          QueryService.deleteQuery(query).then(
            function() {
              Utility.notify($("#notifications"), "Query deleted!", "success", true);
              $scope.changeQueriesPage(true);
            }
          )
        }
      );
    };

    $scope.viewAuditLog = function(query) {
      $scope.query = query;
      $scope.getAuditLogs(query);
    };

 
    $scope.viewAllAuditLogs = function(type) {
      $scope.auditData = {auditLogs: [], pageSize: 25, currentPage: 1, type: type};
      if (!type) { $scope.auditData.type = 'ALL'; }
      $scope.changeAuditLogPage(true);
    };

    $scope.changeAuditLogPage = function(countReq) {
      var type = $scope.auditData.type;
      $scope.auditData.startAt = ($scope.auditData.currentPage - 1) * $scope.auditData.pageSize;

      var q = QueryService.getAllAuditLogs(type, countReq, $scope.auditData.startAt, $scope.auditData.pageSize);
      q.then(
        function(result) {
          if (result.count) {
            $scope.auditData.logCount = result.count;
          }
          $scope.auditData.auditLogs = result.auditLogs;

          if (!result.auditLogs || result.auditLogs.length == 0) {
            Utility.notify(
              $("#notifications"),
              "No audit logs to show for the selected period",
              "info",
              true);
          } else {
            $scope.queryData.view = 'log';
          }
        }
      );
    };

    $scope.getAuditLogs = function(query) {
      QueryService.getAuditLogs(query.id, 0, 100).then(
        function(auditLogs) {
          $scope.auditLogs = auditLogs;
          if (auditLogs.length > 0) {
            $scope.displayAuditLogs(auditLogs);
          } else {
            Utility.notify(
              $("#notifications"),
              "No audit logs for the selected query",
              "info",
              true);
          }
        });
    };

    $scope.displayAuditLogs = function(auditLogs) {
      var modalInstance = $modal.open({
        templateUrl: 'view-audit-logs.html',
        controller: DisplayAuditLogsCtrl,
        resolve: {
          query: function() {
            return $scope.query;
          },

          auditLogs: function() {
            return auditLogs;
          }
        }
      });
    };

    var DisplayAuditLogsCtrl = function($scope, $modalInstance, query, auditLogs) {
      $scope.query = query;
      $scope.auditLogs = auditLogs;
      
      $scope.close = function () {
        $modalInstance.dismiss('cancel');
      };
    };
 
    $scope.viewQuerySql = function(auditLog) {
      var modalInstance = $modal.open({
        templateUrl: 'view-query-sql.html',
        controller: ViewQuerySqlCtrl,
        resolve: {
          auditLogId: function () {
            return auditLog.id;
          }
        }
      });
    };
 
    var ViewQuerySqlCtrl = function($scope, $modalInstance, auditLogId) {
      $scope.auditLog = null;
 
      QueryService.getAuditLog(auditLogId).then(
        function(auditLog) {
          $scope.auditLog = auditLog;
        });
 
      $scope.close = function () {
         $modalInstance.dismiss('cancel');
      };
    };

    $scope.createQuery = function() {
      angular.extend($scope.queryData, $scope.getQueryDataDefaults());
      $scope.queryData.view = 'query';
    };

    $scope.importQuery = function() {
      var modalInstance = $modal.open({
        templateUrl: 'import-query.html',
        controller: ImportQueryCtrl
      });

      modalInstance.result.then(
        function(queryId) {
          if (queryId > 0) {
            $scope.showQueries();
            Utility.notify($("#notifications"), "Query Imported Successfully", "success", true)
          } else {
            Utility.notify($("#notifications"), "Query Import Failed", "error", true);
          }
        });
    };


    var getFilter = function(filterDef) {
      if (filterDef.expr) {
        return filterDef;
      }

      var fieldName = filterDef.field;
      var dotIdx = fieldName.indexOf(".");
      var formName = fieldName.substr(0, dotIdx);

      var op = getOpByModel(filterDef.op);
      var value = undefined;
      if (op.name == 'exists' || op.name == 'not_exists') {
        value = undefined;
      } else if (op.name != 'qin' && op.name != 'not_in' && op.name != 'between') {
        value = filterDef.values[0];
      } else {
        value = filterDef.values;
      }

      return {
        id: filterDef.id,
        op: getOpByModel(filterDef.op),
        value: value,
        form: $scope.getForm(formName),
        fieldName: fieldName.substr(dotIdx + 1),
        parameterized: filterDef.parameterized
      };
    };

    $scope.editQuery = function(query) {
      $scope.loadQuery(query).then(function(result) { 
        if (!result) { return; }

        stopLoading();
        $scope.queryData.view = 'query'; 
      });
    };

    $scope.runQuery = function(query) {
      $scope.loadQuery(query).then( function(result) { 
        if (!result) { return; }

        stopLoading();
        $scope.showRecords();
      });
    };

    $scope.showRecords = function() {
      if (isParameterized($scope.queryData.filters)) {
        runParameterizedFilters();
      } else {
        $scope.getRecords(); 
      }
    };

    var runParameterizedFilters = function() {
      var modal = $modal.open({
        templateUrl: 'parameterized-filters-modal.html',
        controller: ParameterizedFilterCtrl,
        resolve: {
          queryData: function() {
            return $scope.queryData;
          }
        },
        windowClass: 'parameterized-filters-dialog'
      });

      modal.result.then(
        function(queryData) {
          $scope.queryData = queryData;
          $scope.getRecords();
        });
    };

    var ParameterizedFilterCtrl = function($scope, $modalInstance, queryData) {
      $scope.queryData = queryData;

      
      var filters = queryData.filters;
      for (var i = 0; i < filters.length; ++i) {
        if (!filters[i].parameterized) {
          continue;
        }

        if (filters[i].expr) {
          var tObj = filters[i].tObj = getTemporalExprObj(filters[i].expr);
          tObj.ops = getNumericOps();
          filters[i].value = tObj.rhs;
          filters[i].op = getOpBySymbol(tObj.op);
        } else {
          filters[i].field.ops = getValidOps(filters[i].field);
        }
      }

      $scope.onOpSelect = function(filter) {
        return function(op) {
          onOpSelect(op, filter);
        }
      };

      $scope.isUnaryOpSelected = function(filter) {
        return isUnaryOpSelected(filter);
      };

      $scope.getValueType = function(filter) {
        return getValueType(filter);
      };
  
      $scope.ok = function() {
        var filters = queryData.filters;
        for (var i = 0; i < filters.length; ++i) {
          if (!filters[i].parameterized || !filters[i].expr) {
            continue;
          }

          var tObj = filters[i].tObj;
          filters[i].expr = tObj.lhs + ' ' + filters[i].op.symbol + ' ';
          if (filters[i].value instanceof Array) {
            filters[i].expr += filters[i].value[0] + ' and ' + filters[i].value[1];
          } else {
            filters[i].expr += filters[i].value;
          }
        }
            
        $modalInstance.close($scope.queryData);
      };

      $scope.getCount = function() {
        getCount($scope.queryData);
      }

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };
  
    };

    $scope.loadQuery = function(query) {
      angular.extend($scope.queryData, $scope.getQueryDataDefaults());

      startLoading();
      return QueryService.getQuery(query.id).then(
        function(queryDef) {
          queryDef.cpId = queryDef.cpId || -1;

          var cpList = $scope.queryData.cpList;
          var selectedCp = undefined;
          for (var i = 0; i < cpList.length; ++i) {
            if (cpList[i].id == queryDef.cpId) {
              selectedCp = cpList[i];
              break;
            }
          }

          if (!selectedCp) {
            stopLoading();
            Utility.notify($("#notifications"), "Permission Denied", "error", true);
            return false;
          }

          $scope.onCpSelect(selectedCp);

          var filters = [], filtersMap = {};
          var maxFilterId = 0;
          for (var i = 0; i < queryDef.filters.length; ++i) {
            var filter = getFilter(queryDef.filters[i]);
            filters.push(filter);
            filtersMap[filter.id] = filter;

            if (maxFilterId < queryDef.filters[i].id) {
              maxFilterId = queryDef.filters[i].id;
            }
          }

          var qs = [];
          var uniqForms = {};
          for (var i = 0; i < filters.length; ++i) {
            if (filters[i].expr) {
              continue;
            }

            var form = filters[i].form;
            if (!uniqForms[form.name]) {
              qs.push(getFormFields(selectedCp.id, form));
              uniqForms[form.name] = true;
            }
          }

          var filtersq = $q.all(qs).then(
            function() {
              for (var i = 0; i < filters.length; ++i) {
                if (filters[i].expr) {
                  continue;
                }
                filters[i].field = $scope.getFormField(filters[i].form, filters[i].fieldName) 
                if (!filters[i].field) {
                  stopLoading();
                  Utility.notify($("#notifications"), "Failed to load query. Contact system administrator", "error", true);
                  return false;
                }
              }

              $scope.disableCpSelection();
              return true;
            }
          );

          var exprNodes = [];
          for (var i = 0; i < queryDef.queryExpression.length; ++i) {
            var exprNode = queryDef.queryExpression[i];
            if (exprNode.nodeType == 'FILTER') {
              exprNodes.push({type: 'filter', value: exprNode.value});
            } else if (exprNode.nodeType == 'OPERATOR') {
              exprNodes.push({type: 'op', value: getOpByModel(exprNode.value).name});
            } else if (exprNode.nodeType == 'PARENTHESIS') {
              exprNodes.push({type: 'paren', value: exprNode.value == 'LEFT' ? '(' : ')'});
            }
          }

          var queryProps = {
            selectedFields: queryDef.selectList, filters: filters, filtersMap: filtersMap,
            exprNodes: exprNodes, id: query.id, title: query.title, 
            filterId: maxFilterId, selectedCp: selectedCp,
            reporting: queryDef.reporting || {type: 'none', params: {}}
          };

          angular.extend($scope.queryData, queryProps);
          return filtersq;
        }
      );
    };


    $scope.getUsers = function() {
      var deferred = $q.defer();
      if ($scope.queryData.users) {
        deferred.resolve($scope.queryData.users);
      } else {
        UsersService.getAllUsers().then(
          function(users) {
            for (var i = 0; i < users.length; ++i) {
              users[i].userName = users[i].firstName + " " + users[i].lastName;
            }
            $scope.queryData.users = users;
            deferred.resolve(users);
          }
        );
      }

      return deferred.promise;
    };

    $scope.createFolder = function() {
      var modalInstance = $modal.open({
        templateUrl: 'addedit-query-folder.html',
        controller: AddEditQueryFolderCtrl,
        resolve: {
          users: function() {
            return $scope.getUsers();
          },
          queries: function() {
            return $scope.queryData.selectedQueries;
          },
          folder: undefined
        }   
      });
      
      modalInstance.result.then(
        function(folderDetail) { 
          $scope.loadFolders();
          Utility.notify($("#notifications"), "Query Folder Created Successfully", "success", true);
        });
    };

    $scope.editFolder = function(folder) {
      var modalInstance = $modal.open({
        templateUrl: 'addedit-query-folder.html',
        controller: AddEditQueryFolderCtrl,
        resolve: {
          users: function() {
            return $scope.getUsers();
          },
          queries: function() {
            return [];
          },
          folder: function() {
            return QueryService.getFolder(folder.id);
          }
        }
      });

      modalInstance.result.then(
        function(folderDetail) { 
          $scope.loadFolders();
          if (folderDetail.id == $scope.queryData.selectedFolderId) {
            $scope.selectFolder(folderDetail.id);
          }

          Utility.notify($("#notifications"), "Query Folder Updated Successfully", "success", true);
        });
    };

    $scope.deleteFolder = function(folder) {
      QueryService.deleteFolder(folder.id).then(
        function() {
          $scope.loadFolders();
          if ($scope.queryData.selectedFolderId == folder.id) {
            $scope.selectFolder(-1);
          }

          Utility.notify($("#notifications"), "Query Folder Deleted Successfully", "success", true);
        });
    };

    var AddEditQueryFolderCtrl = function($scope, $modalInstance, users, queries, folder) {
      $scope.modalData = {
        queries: queries,
        sharedWith: [],
        folderId: folder ? folder.id : undefined
      };

      if (folder) {
        $scope.modalData.folderName = folder.name;
        $scope.modalData.sharedWith = folder.sharedWith;
        $scope.modalData.sharedWithAll = folder.sharedWithAll;
        $scope.modalData.queries = folder.queries;
      }

      var userList = [];
      for (var i = 0; i < users.length; ++i) {
        userList.push({id: users[i].id, text: users[i].userName}); 
      }

      $scope.initSelectedUsers = function(elem, callback) {
        var sharedWith = $scope.modalData.sharedWith;
        if (!sharedWith || sharedWith.length == 0) {
          return;
        }

        var result = [];
        for (var i = 0; i < sharedWith.length; ++i) {
          if (sharedWith[i].firstName || sharedWith[i].lastName) {
            result.push({id: sharedWith[i].id, text: sharedWith[i].firstName + ' ' + sharedWith[i].lastName});
          } else if (sharedWith[i].text) {
            result.push(sharedWith[i]);
          }
        }
        
        callback(result);
      };

      $scope.searchUser = function(unwanted, term, callback) {
        if (!term) {
          callback(userList);
          return;
        }

        UsersService.getAllUsers({searchString: term}).then(
          function(users) {
            for (var i = 0; i < users.length; ++i) {
              users[i].text = users[i].firstName + " " + users[i].lastName;
            }
            callback(users);
          }
        );
      };

      $scope.onUserSelect = function(selected) {
      };
 
      $scope.saveOrUpdateFolder = function () {
        var sharedWith = [];
        if (!$scope.modalData.sharedWithAll) {
          for (var i = 0; i < $scope.modalData.sharedWith.length; ++i) {
            sharedWith.push({id: $scope.modalData.sharedWith[i].id});
          }
        }

        var queries = [];
        for (var i = 0; i < $scope.modalData.queries.length; ++i) {
          queries.push({id: $scope.modalData.queries[i].id});
        }

        var folderDetail = {
          id: $scope.modalData.folderId,
          name: $scope.modalData.folderName,
          sharedWithAll: $scope.modalData.sharedWithAll,
          sharedWith: sharedWith,
          queries: queries
        };
        
        QueryService.saveOrUpdateQueryFolder(folderDetail).then(
          function(resp) {
            $modalInstance.close(resp);
          }
        );
      };

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      $scope.removeQuery = function(query, idx) {
        $scope.modalData.queries.splice(idx, 1);
      };
    };

    $scope.showQueries();

    var formsq = FormsService.getQueryForms().then(function(forms) {
      $scope.queryData.forms = forms;
      return forms;
    });

    CollectionProtocolService.getCpList().then(
      function(cpList) {
        formsq.then(
          function(forms) {
            var defCp = {
              id: -1, 
              shortTitle: 'None', 
              forms: angular.copy(forms)
            };
            cpList.unshift(defCp);
            $scope.queryData.cpList = cpList;
          }
        );
      }
    );

    $scope.getCp = function(cpId) {
      for (var i = 0; i < $scope.queryData.cpList.length; ++i) {
        if ($scope.queryData.cpList[i].id == cpId) {
          return $scope.queryData.cpList[i];
        }
      }

      return null;
    };

    $scope.onCpSelect = function(cp) {
      $scope.queryData.selectedCp = cp;
      if (cp.forms) {
        return;
      }

      cp.forms = angular.copy($scope.queryData.forms);
    };

    var getFormFields = function(cpId, form) {
      var deferred = $q.defer();
      if (!form.fields) {
        FormsService.getQueryFormFields(cpId, form.formId).then(
          function(fields) {
            form.fields = fields;
            form.staticFields = flattenStaticFields("", fields);
            form.extnForms = getExtnForms("", fields);
            form.extnFields = flattenExtnFields(form.extnForms);
            deferred.resolve(form.fields);
          }
        );
      } else if (!form.staticFields) {
        form.staticFields = flattenStaticFields("", fields);
        form.extnForms = getExtnForms("", fields);
        form.extnFields = flattenExtnFields(form.extnForms);
        deferred.resolve(form.fields);
      } else {
        deferred.resolve(form.fields);
      }

      return deferred.promise;
    };

    $scope.showFormSelectCallout = function() {
      var qd = $scope.queryData;
      if (qd.filters.length > 0) {
        return false;
      }
      return qd.showCallout && qd.selectedCp && !qd.openForm && !qd.currFilter.form && true;
    };
 
    $scope.showOpSelectCallout = function() {
      var qd = $scope.queryData;
      if (qd.filters.length > 0) {
        return false;
      }
      return qd.showCallout && qd.selectedCp && qd.currFilter.field && !qd.currFilter.op && true;
    };

    $scope.showValueSelectCallout = function() {
      var qd = $scope.queryData;
      if (qd.filters.length > 0) {
        return false;
      }
      var op = qd.currFilter.op;
      var unary = (op == 'exists' || op == 'not_exists');
      return qd.showCallout && qd.selectedCp && qd.currFilter.field && op && !unary && !qd.currFilter.value && true;
    };

    $scope.showAddFilterCallout = function() {
      var qd = $scope.queryData;
      if (qd.filters.length > 0) {
        return false;
      }
      return qd.showCallout && qd.selectedCp && qd.currFilter.field && qd.currFilter.op && qd.currFilter.value && true;
    };

    $scope.showAddFilterSuccessCallout = function() {
      var qd = $scope.queryData;
      if (qd.showCallout && qd.filters.length == 1) {
        return true;
      }
      return false;
    };

    $scope.disableAddFilterBtn = function() {
      var qd = $scope.queryData;
      if (qd.currFilter.expr && qd.currFilter.desc) {
        return false;
      } else if (qd.currFilter.field && qd.currFilter.op) {
        var op = qd.currFilter.op.name;
        if (op == 'exists' || op == 'not_exists') {
          return false;
        } else if (op == 'qin' || op == 'not_in') { 
          return !qd.currFilter.value || qd.currFilter.value.length == 0;
        } else if (qd.currFilter.value) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    }

    var flattenFields = function(fqn, fields) {
      var result = [];
      for (var i = 0; i < fields.length; ++i) {
        if (fields[i].type == 'SUBFORM') {
          result = result.concat(flattenFields(fqn + fields[i].name + ".", fields[i].subFields));
        } else {
          var field = angular.extend({}, fields[i]);
          field.name = fqn + fields[i].name;
          result.push(field);
        }
      }

      return result;
    };

    var filterAndFlattenFields = function(fqn, fields, filterfn) {
      var result = [];
      for (var i = 0; i < fields.length; ++i) {
        if (filterfn(fields[i])) {
          result.push(fields[i]);
        }
      }

      return flattenFields(fqn, result);
    };
      
    var flattenStaticFields = function(fqn, fields) {
      return filterAndFlattenFields(fqn, fields, 
        function(field) {
          return field.type != 'SUBFORM' || field.name != 'extensions';
        });
    };

    /*var flattenExtnFields = function(fqn, fields) {
      return filterAndFlattenFields(fqn, fields,
        function(field) {
          return field.type == 'SUBFORM' && field.name == 'extensions';
        });
    };*/

    var flattenExtnFields = function(extnForms) {
      var fields = [];
      for (var i = 0; i < extnForms.length; ++i) {
        fields = fields.concat(extnForms[i].fields);
      }

      return fields;
    };

    var getExtnForms = function(fqn, fields) {
      var extnSubForm = undefined;
      for (var i = 0; i < fields.length; ++i) {
        if (fields[i].type != 'SUBFORM' || fields[i].name != 'extensions') {
          continue;
        }

        extnSubForm = fields[i];
        break;
      }

      if (!extnSubForm) {
        return [];
      }

      var extnForms = [];
      for (var i = 0; i < extnSubForm.subFields.length; ++i) {
        var subForm = extnSubForm.subFields[i];
        var extnFields = flattenFields(fqn + "extensions." + subForm.name + ".", subForm.subFields);
        for (var j = 0; j < extnFields.length; ++j) {
          extnFields[j].extensionForm = subForm.caption;
        }
        extnForms.push({name: subForm.name, caption: subForm.caption, fields: extnFields});
      }

      return extnForms;
    };
      
    $scope.onTemporalFilterSelect = function() {
      hidePopovers();
    };

    $scope.onFormSelect = function(form) {
      form.searchField = '';
      hidePopovers();
      if ($scope.queryData.openForm) {
        $scope.queryData.openForm.showExtnFields = false; // previously selected form
      }

      $scope.queryData.openForm = form;
      $scope.queryData.currFilter.form = form;
      $scope.queryData.currFilter.field = null;
      $scope.queryData.currFilter.op = null;
      $scope.queryData.currFilter.value = null;
      if (!form.fields) {
        var cpId = $scope.queryData.selectedCp.id;
        FormsService.getQueryFormFields(cpId, form.formId).then(function(fields) {
          form.fields = fields;
          form.staticFields = flattenStaticFields("", fields);
          form.extnForms = getExtnForms("", fields);
          form.extnFields = flattenExtnFields(form.extnForms);
        });
      } else if (!form.staticFields) {
        form.staticFields = flattenStaticFields("", fields);
        form.extnForms = getExtnForms("", fields);
        form.extnFields = flattenExtnFields(form.extnForms);
      }
    };

    $scope.getForm = function(formName) {
      var form = undefined;
      var cpForms = $scope.queryData.selectedCp.forms;
      for (var i = 0; i < cpForms.length; ++i) {
        if (formName == cpForms[i].name) {
          form = cpForms[i];
          break;
        }
      } 

      return form;
    };

    $scope.getFormField = function(form, fieldName) {
      if (!form.staticFields) {
        form.staticFields = flattenStaticFields("", form.fields);
        form.extnForms = getExtnForms("", fields);
        form.extnFields = flattenExtnFields(form.extnForms);
      }

      for (var i = 0; i < form.staticFields.length; ++i) {
        if (fieldName == form.staticFields[i].name) {
          return form.staticFields[i];
        }
      }

      for (var i = 0; i < form.extnFields.length; ++i) {
        if (fieldName == form.extnFields[i].name) {
          return form.extnFields[i];
        }
      }
 
      return undefined;
    };
         
    $scope.onFieldSelect = function(field) {
      hidePopovers();
      $scope.queryData.currFilter = {};
      $scope.queryData.currFilter.field = field;
      $scope.queryData.currFilter.op = null;
      $scope.queryData.currFilter.value = null;
      $scope.queryData.currFilter.ops = getValidOps(field);
    };

    var onOpSelect = function(op, filter) {
      if (!filter) {
        filter = $scope.queryData.currFilter;
      }

      filter.op = op;
      if (op.name == "between") {
        filter.value = [null, null];
      } else {
        filter.value = null;
      }
    };

    $scope.onOpSelect = function(op) {
      onOpSelect(op);
    };

    
    var isUnaryOpSelected = function(filter) {
      if (!filter) {
        filter = $scope.queryData.currFilter;
      }

      return filter.op && (filter.op.name == 'exists' || filter.op.name == 'not_exists');
    };

    $scope.isUnaryOpSelected = function() {
      return isUnaryOpSelected();
    }
        
    var getValueType = function(filter) {
      if (!filter) {
        filter = $scope.queryData.currFilter;
      }
     
      var field = filter.field;
      var op = filter.op;

      if (!field || !op) {
        return "text";
      } else if (op && (op.name == "qin" || op.name == "not_in")) {
        if (field.lookupProps) {
          return "lookupMultiple";
        } else {
          return field.pvs ? "multiSelect" : "tagsSelect";
        }
      } else if (field.lookupProps) {
        return "lookupSingle";
      } else if (op && op.name == "between") {
        return field.type == "DATE" ? "betweenDate" : "betweenNumeric"; 
      } else if (field.pvs && !(op.name == 'contains' || op.name == 'starts_with' || op.name == 'ends_with')) {
        return "select";
      } else if (field.type == "DATE") {
        return "datePicker";
      } else {
        return "text";
      }
    };

    $scope.getValueType = function() {
      return getValueType();
    };

    $scope.getOpCode = function(opName) {
      var op = ops[opName];
      return op ? op.code : "";
    }

    $scope.disableCpSelection = function() {
      if ($scope.queryData.selectedCp.id == -1) {
        $scope.queryData.disableCpSelection = false;
        return;
      }

      var filters = $scope.queryData.filters;
      for (var i = 0; i < filters.length; ++i) {
        if (filters[i].expr) { 
          $scope.queryData.disableCpSelection = (filters[i].expr.indexOf('.extensions.') != -1);
          return;
        }

        if (filters[i].field.name.indexOf('extensions.') == 0) {
          $scope.queryData.disableCpSelection = true;
          return;
        }
      }

      var selectedFields = $scope.queryData.selectedFields;
      for (var i = 0; i < selectedFields.length; ++i) {
        if (selectedFields[i].split(".")[1] == 'extensions') {
          $scope.queryData.disableCpSelection = true;
          return;
        }
      }

       $scope.queryData.disableCpSelection = false;
    };

    $scope.addFilter = function() {
      hidePopovers();

      $scope.queryData.filterId++;
      var filter = {form: $scope.queryData.openForm};
      filter = angular.extend(filter, $scope.queryData.currFilter);
      filter.id = $scope.queryData.filterId;

      if ($scope.queryData.filters.length > 0) {
        $scope.queryData.exprNodes.push({type: 'op', value: ops.and.name});
      }

      $scope.queryData.filters.push(filter);
      $scope.queryData.filtersMap[filter.id] = filter;      
      $scope.queryData.exprNodes.push({type: 'filter', value: filter.id});
      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
      $scope.queryData.currFilter = {value: ""};

      $scope.disableCpSelection();
    };

    $scope.editFilter = function() {
      hidePopovers();

      var filter = {}
      filter = angular.extend(filter, $scope.queryData.currFilter);

      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters[i] = filter;
          $scope.queryData.filtersMap[filter.id] = filter;
          break;
        }
      }
      $scope.queryData.currFilter = {};
      $scope.disableCpSelection();
    };

    $scope.displayFilter = function(filter) {
      hidePopovers();

      $scope.queryData.currFilter = angular.copy(filter);
      if (!filter.expr) {
        $scope.queryData.currFilter.ops = getValidOps(filter.field);
      }
    };

    $scope.deleteFilter = function(filter) {
      hidePopovers();

      delete $scope.queryData.filtersMap[filter.id];
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters.splice(i, 1);
          break;
        }
      }

      var selectField = '$temporal.' + filter.id;
      for (var i = 0; i < $scope.queryData.selectedFields.length; ++i) {
        if ($scope.queryData.selectedFields[i] == selectField) {
          $scope.queryData.selectedFields.splice(i, 1);
          break;
        }
      }

      var exprNodes = $scope.queryData.exprNodes;
      for (var i = 0; i < exprNodes.length; ++i) {
        var exprNode = exprNodes[i];
        if (exprNode.type == 'filter' && filter.id == exprNode.value) {
          if (i == 0 && exprNodes.length > 1 && exprNodes[1].type == 'op') {
            exprNodes.splice(0, 2);
          } else if (i != 0 && exprNodes[i - 1].type == 'op') {
            exprNodes.splice(i - 1, 2);
          } else if (i != 0 && (i + 1) < exprNodes.length && exprNodes[i + 1].type == 'op') {
            exprNodes.splice(i, 2);
          } else {
            exprNodes.splice(i, 1);
          }
          break;
        }
      }

      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
      $scope.disableCpSelection();
    };

    $scope.cancelFilter = function() {
      hidePopovers();
      $scope.queryData.currFilter = {};
    };

    $scope.setJoinType = function(joinType) {
      $scope.queryData.joinType = joinType;
      if (joinType == 'adv') {
        return;
      }

      var op = joinType == 'all' ? 'and' : 'or';
      var newExprNodes = [];
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (i != 0) {
          newExprNodes.push({type: 'op', value: op});
        }
        newExprNodes.push({type: 'filter', value: $scope.queryData.filters[i].id});
      }
      $scope.queryData.exprNodes = newExprNodes;
      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
    };

    $scope.saveQuery = function() {
      var saveQueryModal = $modal.open({
        templateUrl: 'save-query.html',
        controller: SaveQueryCtrl,
        resolve: {
          queryData: function() {
            return $scope.queryData;
          }
        }
      });

      saveQueryModal.result.then(
        function(queryId) { 
          $scope.showQueries();
          Utility.notify($("#notifications"), "Query Saved Successfully", "success", true)
        });
    };

    var ImportQueryCtrl = function($scope, $modalInstance) {
      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $timeout(function() {
        $("#uploadQueryDef").fileupload({
          dataType: 'json',
          add: function (e, data) {            
            $("#queryDefFilename").text(data.files[0].name);
            $("#importQuery").on('click', function () {
              data.submit();
            });
          },
          done: function(e, data) { 
            if (data.result) {
              $modalInstance.close(data.result.id);
            } else {
              $modalInstance.close(-1);
            }
          },
          fail: function(e, data) { 
            $modalInstance.close(-1);
          }
        });
      });
    };

    var SaveQueryCtrl = function($scope, $modalInstance, queryData) {
      var getQueryDef = function() {
        var filters = [];
        for (var i = 0; i < queryData.filters.length; ++i) {
          var filter = queryData.filters[i];
          if (filter.expr) {
            filters.push({
              id: filter.id, 
              expr: filter.expr, 
              desc: filter.desc, 
              parameterized: filter.parameterized});
          } else {
            var values = (filter.value instanceof Array) ? filter.value : [filter.value]
            filters.push({
              id: filter.id, 
              field: filter.form.name + "." + filter.field.name, 
              op: filter.op.model, 
              values: values, 
              parameterized: filter.parameterized}); 
          }
        }

        var exprNodes = [];
        for (var i = 0; i < queryData.exprNodes.length; ++i) {
          var node = queryData.exprNodes[i];
          if (node.type == 'paren') {
            exprNodes.push({nodeType: 'PARENTHESIS', value: node.value == '(' ? 'LEFT' : 'RIGHT'});
          } else if (node.type == 'op') {
            exprNodes.push({nodeType: 'OPERATOR', value: ops[node.value].model});
          } else if (node.type == 'filter') {
            exprNodes.push({nodeType: 'FILTER', value: node.value});
          }
        }

        return {
          id: queryData.id,
          title: queryData.title,
          filters: filters, 
          queryExpression: exprNodes, 
          cpId: queryData.selectedCp.id,
          selectList: queryData.selectedFields,
          title: queryData.title, 
          drivingForm: queryData.drivingForm,
          reporting: queryData.reporting
        };
      };
      
      $scope.modalData = {
        title: queryData.title,
        id: queryData.id
      };
   

      $scope.save = function(overwrite) {
        if (overwrite) {
          queryData.id = undefined;
        }
        queryData.title = $scope.modalData.title;
        var queryDef = getQueryDef();
        
        QueryService.saveOrUpdateQuery(queryDef).then(
          function(result) {
            $modalInstance.close(result.id);      
          }
        );   
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };
    };

    $scope.handleAutocompleteKeyDown = function(event) {
      if (event.keyCode === $.ui.keyCode.TAB && angular.element(event.target).data("ui-autocomplete").menu.active) {
        event.preventDefault();
      }
    };

    var getOpIdx = function(term) {
      var re = /[\+\-\(,<=>!]/g;
      var index = -1, numMatches = 0;
      var match;
      while ((match = re.exec(term)) != null) {
        index = match.index;
        re.lastIndex = ++numMatches;
      }
      return index;
    };

    var getDateFns = function() {
      return [
        {label:'current_date', value: 'current_date()'},
        {label:'months_between', value: 'months_between('},
        {label:'years_between', value: 'years_between('},
        {label:'minutes_between', value: 'minutes_between('},
        {label:'round', value: 'round('}
      ];
    };

    var getFormsAndFnAdvise = function(selectedCp) {
      var forms = [];
      for (var i = 0; i < selectedCp.forms.length; ++i) {
        forms.push({label: selectedCp.forms[i].caption, value: selectedCp.forms[i].name});
      }
      return getDateFns().concat(forms);
    };

    var getFieldsAdvise = function(form) {
      var result = [];
      var fields = [].concat(form.staticFields).concat(form.extnFields);
      for (var i = 0; i < fields.length; ++i) {
        var field = fields[i];
        if (field.type == 'DATE' || field.type == 'INTEGER' || field.type == 'FLOAT') {
          result.push({label: field.caption, value: field.name});
        }
      }

      return result;
    };

    $scope.temporalFilterOpts = {
      options: {
        source: function(request, response) {
          var qd = $scope.queryData;
          var term = request.term.replace(/\s+/g, '');
          var srchTerm = term.substr(getOpIdx(term) + 1);
          var dotIdx = srchTerm.indexOf(".");
          if (dotIdx == -1) {
            response($scope.temporalFilterOpts.methods.filter(getFormsAndFnAdvise(qd.selectedCp), srchTerm));
          } else {
            var formName = srchTerm.substr(0, dotIdx);
            var fieldName = srchTerm.substr(dotIdx + 1).replace(/\s+/g, '');
            var form = $scope.getForm(formName);
            getFormFields(qd.selectedCp.id, form).then(
              function() {
                response($scope.temporalFilterOpts.methods.filter(getFieldsAdvise(form), fieldName));
              });
          }
        },

        focus: function(event, ui) {
          event.preventDefault();
          return false;
        },

        select: function(event, ui) {
          var cf = $scope.queryData.currFilter;
          var expr = cf.expr;
          var opIdx = getOpIdx(expr);
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
    };

    $scope.defineView = function() {
      var defineViewModal = $modal.open({
        templateUrl: 'define-view.html',
        controller: DefineViewCtrl,
        resolve: {
          queryData: function() {
            return $scope.queryData;
          }
        },
        windowClass: 'define-view-dialog'
      });

      defineViewModal.result.then(
        function(result) {
          $scope.queryData.selectedFields = result.selectedFields;
          $scope.queryData.reporting = result.reporting;
          $scope.disableCpSelection();
          $scope.getRecords(); 
        }
      );
    }

    var DefineViewCtrl = function($scope, $modalInstance, queryData) {
      var aggFns = {
        count: {name: 'count', label: 'Count'},
        sum:   {name: 'sum',   label: 'Sum'},
        min:   {name: 'min',   label: 'Min'},
        max:   {name: 'max',   label: 'Max'},
        avg:   {name: 'avg',   label: 'Avg'}
      };

      $scope.queryData = queryData;
      $scope.reporting = angular.copy(queryData.reporting);

      $scope.reportingOpts = [
        {type: 'none', label: 'None'},
        {type: 'crosstab', label: 'Crosstab'}
      ];

      $scope.createPivotTable = function(pivotTable) {
        if (!pivotTable) {
          $scope.reporting = {type: '', params: {}};
        } else {
          $scope.reporting.type = 'crosstab';
          $scope.preparePivotTableOpts();
        }
      };

      var forms = [];
      for (var i = 0; i < queryData.selectedCp.forms.length; ++i) {
        var form = { type: 'form', val: queryData.selectedCp.forms[i].caption, form: queryData.selectedCp.forms[i] };
        forms.push(form);
      }
 
      for (var i = 0; i < queryData.filters.length; ++i) {
        var filter = queryData.filters[i];
        if (filter.expr) {
          forms.push({type: 'temporal', val: filter.desc, form: filter, children: []});
        }
      }

      $scope.treeOpts = {
        treeData: forms,
        nodeTmpl: 'field-tmpl.html',

        toggleNode: function(node) {
          if (node.expanded) {
            loadNodeChildren(node);
          }
        },

        nodeChecked: function(node) {
          if (node.type == 'form') {
            loadNodeChildren(node).then(function() { nodeChecked(node); });
          } else {
            nodeChecked(node);
          }

          if (!node.checked) {
            $scope.preparePivotTableOpts();
          }
        }
      };

      var sanitizeSelectedFields = function(selectedFields) {
        if (!selectedFields) {
          return;
        }

        for (var i = 0; i < selectedFields.length; ++i) {
          var selectedField = selectedFields[i];
          delete selectedField.label;
          delete selectedField.type;

          var aggFns = selectedField.aggFns;
          if (!aggFns) {
            continue;
          }

          for (var j = aggFns.length - 1; j >= 0; j--) {
            if (!aggFns[j].opted) {
              aggFns.splice(j, 1);
            }
          }

          if (aggFns.length == 0) {
            delete selectedField.aggFns;
          }
        }
      };

      $scope.ok = function() {
        if (!$scope.selectedFields) {
          $scope.selectedFields = getSelectedFields(forms);
        }

        if (!$scope.selectedFields || $scope.selectedFields.length == 0) {
          Utility.notify($("#define-view-notif"), "No fields selected", "error", true);
          return;
        }

        var rptParams = $scope.reporting.params;
        if ($scope.reporting.type == 'crosstab') {
          if (!rptParams.groupRowsBy || rptParams.groupRowsBy.length == 0) {
            Utility.notify($("#define-view-notif"), "Rows field is not specified", "error", true);
            return;
          }

          if (!rptParams.groupColBy) {
            Utility.notify($("#define-view-notif"), "Column field is not specified", "error", true);
            return;
          }

          if (!rptParams.summaryFields || rptParams.summaryFields.length == 0) {
            Utility.notify($("#define-view-notif"), "Summary field is not specified", "error", true);
            return;
          }
        }

        sanitizeSelectedFields($scope.selectedFields);

        $modalInstance.close({
          selectedFields: $scope.selectedFields, 
          reporting: $scope.reporting
        });
      };

      $scope.cancel = function() {
        sanitizeSelectedFields($scope.selectedFields);
        $modalInstance.dismiss('cancel');
      };

      var getGroupFields = function(reportFields, exclude) {
        var result = angular.copy(reportFields);
        for (var i = 0; i < exclude.length; ++i) {
          for (var j = 0; j < result.length; ++j) {
            if (result[j].name == exclude[i].name &&
                (!exclude[i].aggFn || result[j].aggFn == exclude[i].aggFn)) {
              result.splice(j, 1);
              break;
            }
          }
        }

        return result;
      };

      var getGroupRowsBy = function(reportFields) {
        var rptParams = $scope.reporting.params;

        var excludeFields = [];
        if (rptParams.groupColBy) {
          excludeFields.push(rptParams.groupColBy);
        }

        if (rptParams.summaryFields) {
          excludeFields = excludeFields.concat(rptParams.summaryFields);
        }

        return getGroupFields(reportFields, excludeFields);
      };

      var getGroupColBy = function(reportFields) {
        var rptParams = $scope.reporting.params;
       
        var excludeFields = [];
        if (rptParams.groupRowsBy) {
          excludeFields = excludeFields.concat(rptParams.groupRowsBy);
        }

        if (rptParams.summaryFields) {
          excludeFields = excludeFields.concat(rptParams.summaryFields);
        }

        return getGroupFields(reportFields, excludeFields);
      };

      var getSummaryFields = function(reportFields) {
        var rptParams = $scope.reporting.params;
       
        var excludeFields = [];
        if (rptParams.groupRowsBy) {
          excludeFields = excludeFields.concat(rptParams.groupRowsBy);
        }

        if (rptParams.groupColBy) {
          excludeFields.push(rptParams.groupColBy);
        }

        return getGroupFields(reportFields, excludeFields);
      };

      var preparePivotTabFields = function(reportFields) {
        $scope.groupRowsBy = getGroupRowsBy(reportFields);       
        $scope.groupColBy = getGroupColBy(reportFields);       
        $scope.summaryFields = getSummaryFields(reportFields);       
      };

      var removeUnselectedFields = function(dstArr, srcArr) {
        for (var i = dstArr.length - 1; i >= 0; --i) {
          var found = false;
          for (var j = 0; j < srcArr.length; ++j) {
            if (dstArr[i].name == srcArr[j].name &&
                (!dstArr[i].aggFn || dstArr[i].aggFn == srcArr[j].aggFn)) {
              found = true;
              break;
            }
          }

          if (!found) {
            dstArr.splice(i, 1);
          }
        }

        return dstArr;
      }

      var getTypeAggFns = function(type, fns) {
        var result = undefined;
        if (type == 'INTEGER' || type == 'FLOAT') {
          result = [fns.count, fns.sum, fns.min, fns.max, fns.avg];
        } else if (type == 'STRING') {
          result = [fns.count];
        } else if (type == 'BOOLEAN') {
          result = [fns.count, fns.sum];
        } else if (type == 'DATE') {
          result = [fns.count, fns.min, fns.max];
        } else {
          result = [];
        }

        return result;
      };

      var prepareAggFns = function(fields, fns, fresh) {
        for (var i = 0; i < fields.length; ++i) {
          var typeFns = getTypeAggFns(fields[i].type, fns);

          if (!fields[i].aggFns) {
            fields[i].aggFns = angular.copy(typeFns);
            continue;
          }

          if (!fresh) {
            continue;
          }

          for (var j = 0; j < typeFns.length; ++j) {
            var found = false;
 
            for (var k = 0; k < fields[i].aggFns.length; ++k) {
              if (typeFns[j].name == fields[i].aggFns[k].name) {
                fields[i].aggFns[k].label = typeFns[j].label;
                fields[i].aggFns[k].opted = true;
                found = true;
                break;
              }
            }

            if (!found) {
              fields[i].aggFns.splice(j, 0, angular.copy(typeFns[j]));
            }
          }
        }
      };

      $scope.showCurrField = function(field) {
        $scope.currField = field;
      };

      $scope.toggleAggFn = function(field, fn) {
        if (fn.opted && !fn.desc) {
          fn.desc = field.label + " " + fn.label;
        } else if (!fn.opted) {
          fn.desc = undefined;
        }
      };

      $scope.prepareAggregateOpts = function() {
        var init = !$scope.selectedFields || $scope.selectedFields.length == 0;

        $scope.selectedFields = getSelectedFields(forms, true);
        $scope.currField = undefined;

        if (!$scope.selectedFields || $scope.selectedFields.length == 0) {
          Utility.notify($("#define-view-notif"), "No fields selected", "error", true);
          return false;
        }

        prepareAggFns($scope.selectedFields, aggFns, init);
        return true;
      };   

      $scope.preparePivotTableOpts = function() {
        if ($scope.reporting.type != 'crosstab') {
          return true;
        }

        $scope.reportFields = getPivotTabFields($scope.selectedFields, false);

        var rptParams = $scope.reporting.params;
        if (rptParams.groupRowsBy) {
          rptParams.groupRowsBy = removeUnselectedFields(rptParams.groupRowsBy, $scope.reportFields);
        }

        if (rptParams.groupColBy) {
          var result = removeUnselectedFields([rptParams.groupColBy], $scope.reportFields);
          if (result.length == 0) {
            rptParams.groupColBy = undefined;
          }
        }

        if (rptParams.summaryFields) {
          rptParams.summaryFields = removeUnselectedFields(rptParams.summaryFields, $scope.reportFields);
        }

        preparePivotTabFields($scope.reportFields);
        return true;
      };

      $scope.onGroupRowsByChange = function(newVal) {
        $scope.reporting.params.groupRowsBy = newVal;
        preparePivotTabFields($scope.reportFields);
      };

      $scope.onGroupColByChange = function(newVal) {
        $scope.reporting.params.groupColBy = newVal;
        preparePivotTabFields($scope.reportFields);
      };

      $scope.onSummaryFieldChange = function(newVal) {
        $scope.reporting.params.summaryFields = newVal;
        preparePivotTabFields($scope.reportFields);
      };

      var processFields = function(formCaption, prefix, fields, extnForm) {
        var result = [];
        for (var i = 0; i < fields.length; ++i) {
          if (fields[i].type == 'SUBFORM' && fields[i].name == 'extensions') {
            var extnForms = processFields(formCaption, prefix + fields[i].name + ".", fields[i].subFields, true);
            result = result.concat(extnForms);
          } else if (fields[i].type == 'SUBFORM') {
            var field = {type: 'subform', val: fields[i].caption, name: prefix + fields[i].name};
            var caption = formCaption;
            if (extnForm) {
              caption += ": " + fields[i].caption;
            }

            field.children = processFields(caption, prefix + fields[i].name + ".", fields[i].subFields);

            if (field.children.length == 1) {
              result.push(field.children[0]);
            } else {
              result.push(field);
            }
          } else {
            result.push({             
              type: 'field', 
              form: formCaption,
              val: fields[i].caption, 
              name: prefix + fields[i].name, 
              dataType: fields[i].type,
              children: []
            });
          }
        }

        return result;
      };

      var loadNodeChildren = function(node) {
        var deferred = $q.defer();
        if (node.type != 'form' || node.children) {
          deferred.resolve(node.children);
          return deferred.promise;
        }

        getFormFields($scope.queryData.selectedCp.id, node.form).then(
          function(fields) {
            node.children = processFields(node.form.caption, node.form.name + ".", fields);
            deferred.resolve(node.children);
          }
        );

        return deferred.promise;
      };

      var nodeChecked = function(node) {
        if (node.checked) {
          node.expanded = true;
        } else if (node.aggFn) {
          node.aggFn = '';
        }

        for (var i = 0; i < node.children.length; ++i) {
          node.children[i].checked = node.checked;
          nodeChecked(node.children[i]);
        }
      };

      var selectFields = function(selectedFieldsMap, nodes) {
        var i = 0;
        for (var formName in selectedFieldsMap) {
          var filterId = undefined;
          if (formName.indexOf('$temporal.') == 0) {
            filterId = formName.split('.')[1];
          }

          for (var j = 0; j < nodes.length; j++) {
            if (filterId && nodes[j].form.id == filterId) {
              var node = nodes.splice(j, 1)[0]; // removes node 
              nodes.splice(i, 0, node); // inserts node
              node.checked = true;
              break;
            } else if (formName == nodes[j].form.name) {
              var node = nodes.splice(j, 1)[0]; // removes node 
              nodes.splice(i, 0, node); // inserts node
              selectFormFields(selectedFieldsMap[formName], node);
              break;
            }
          } 

          ++i;
        }
      };

      var selectFormFields = function(selectedFields, node) {
        loadNodeChildren(node).then(
          function() {
            orderAndSetSelectedFields(selectedFields, node, 1);
          }
        );
      }

      var orderAndSetSelectedFields = function(selectedFields, node, level) {
        var i = 0;
        var pos = 0;
        var fields = node.children;
        while (i < selectedFields.length) {
          var currLevel = level;
          var fieldParts;
          if (typeof selectedFields[i] == "string") {
            fieldParts = selectedFields[i].split(".", 2);
          } else {
            fieldParts = selectedFields[i].name.split(".", 2);
          }

          if (fieldParts[1] == "extensions") {
            currLevel++;
          }

          var nodeIdx = getMatchingNodeIdx(selectedFields[i], fields, currLevel);
          var fieldNode = fields.splice(nodeIdx, 1)[0];
          fields.splice(pos, 0, fieldNode);
          pos++;

          if (fieldNode.type == 'subform') {
            var sfSelectedFields = [];
            while (i < selectedFields.length) {
              var name;
              if (typeof selectedFields[i] == "string") {
                name = selectedFields[i].split(".", currLevel + 1).join(".");
              } else {
                name = selectedFields[i].name.split(".", currLevel + 1).join(".");
              }

              if (name != fieldNode.name) {
                break;
              }

              sfSelectedFields.push(selectedFields[i]);
              ++i;
            }
            orderAndSetSelectedFields(sfSelectedFields, fieldNode, level + 1);
          } else {
            fieldNode.checked = true;
            if (typeof selectedFields[i] != "string") {
              fieldNode.aggFns = selectedFields[i].aggFns;
            }
            ++i;
          }
        }
      };

      var getMatchingNodeIdx = function(field, fields, level) {
        var name;
        if (typeof field == "string") {
          name = field.split(".", level + 1).join(".");
        } else {
          name = field.name.split(".", level + 1).join(".");
        } 

        var idx = -1;
        for (var i = 0; i < fields.length; ++i) {
          if (name == fields[i].name || field == fields[i].name) {
            idx = i;
            break;
          }
        }

        return idx;
      };

      var getSelectedFields = function(forms, incCaption) {
        var selected = [];
        for (var i = 0; i < forms.length; ++i) {
          if (forms[i].checked) {
            selected = selected.concat(getAllFormFields(forms[i], incCaption));
          } else if (forms[i].children) {
            var fields = forms[i].children;
            for (var j = 0; j < fields.length; ++j) {
              var field = fields[j];
              if (field.type == 'subform') {
                selected = selected.concat(getSelectedFields([field], incCaption));
              } else if (field.checked) {
                if (field.aggFns) {
                  selected.push({form: field.form, name: field.name, label: field.val, type: field.dataType, aggFns: field.aggFns});
                } else {
                  if (!incCaption) {
                    selected.push(field.name);
                  } else {
                    selected.push({form: field.form, name: field.name, label: field.val, type: field.dataType});
                  }
                }
              }
            }
          }
        }        
        
        return selected;
      };

      var getAllFormFields = function(form, incCaption) {
        if (form.type == 'temporal') {
          var name = '$temporal.' + form.form.id;
          return incCaption ? [{name: name, label: form.val, form: form.val}] : [name];
        }

        var result = [];
        for (var i = 0; i < form.children.length; ++i) {
          var field = form.children[i];
          if (field.type == 'subform') {
            result = result.concat(getAllFormFields(field, incCaption));
          } else {
            result.push(incCaption ? {form: field.form, name: field.name, label: field.val, type: field.dataType} : field.name);
          }
        }

        return result;
      };

      var selectedFieldsMap = {};
      for (var i = 0; i < queryData.selectedFields.length; ++i) {
        var selectedField = queryData.selectedFields[i];
        if (typeof selectedField == "string" && selectedField.indexOf("$temporal.") == 0) {
          selectedFieldsMap[selectedField] = selectedField;
          continue;
        }

        var form;
        if (typeof selectedField == "string") {
          form = selectedField.split(".", 1);
        } else {
          form = selectedField.name.split(".", 1);
        }
         
        if (!selectedFieldsMap[form]) {
          selectedFieldsMap[form] = [];
        }

        selectedFieldsMap[form].push(selectedField);
      }

      selectFields(selectedFieldsMap, forms);
    }
  }]);
