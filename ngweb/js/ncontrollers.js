
angular.module('plus.controllers', [])
  .controller('QueryController', ['$scope', '$sce', '$modal', 'CollectionProtocolService', 'FormService', 'QueryService', function($scope, $sce, $modal, CollectionProtocolService, FormService, QueryService) {
    var opsMap = {
      eq: {desc: "Equals", code: "&#61;", symbol: '='}, 
      ne: {desc: "Not Equals", code: "&#8800;", symbol: '!='}, 
      lt: {desc: "Less than", code: "&#60;", symbol: '<'}, 
      le: {desc: "Less than or Equals", code: "&#8804;", symbol: '<='}, 
      gt: {desc: "Greater than", code: "&#62;", symbol: '>'}, 
      ge: {desc: "Greater than or Equals", code:"&#8805;", symbol: '>='},
      exists: {desc: "Exists", code: "&#8707;", symbol: 'exists'}, 
      not_exists: {desc: "Not Exists", code: "&#8708;", symbol: 'not exists'}, 
      qin: {desc: "In", code:"&#8712;", symbol: 'in'}, 
      not_in: {desc: "Not In", code:"&#8713;", symbol: 'not in'},
      starts_with: {desc: "Starts With", code: "&#8963;&#61;", symbol: 'starts with'}, 
      ends_with: {desc: "Ends With", code: "&#36;&#61;", symbol: 'ends with'}, 
      contains: {desc: "Contains", code: "&#126;", symbol: 'contains'},
    };

    var ops = {
      eq: {name: "eq", desc: "Equals", code: "&#61;", symbol: '='}, 
      ne: {name: "ne", desc: "Not Equals", code: "&#8800;", symbol: '!='}, 
      lt: {name: "lt", desc: "Less than", code: "&#60;", symbol: '<'}, 
      le: {name: "le", desc: "Less than or Equals", code: "&#8804;", symbol: '<='}, 
      gt: {name: "gt", desc: "Greater than", code: "&#62;", symbol: '>'}, 
      ge: {name: "ge", desc: "Greater than or Equals", code:"&#8805;", symbol: '>='},
      exists: {name: "exists", desc: "Exists", code: "&#8707;", symbol: 'exists'}, 
      not_exists: {name: "not_exists", desc: "Not Exists", code: "&#8708;", symbol: 'not exists'}, 
      qin: {name: "qin", desc: "Is One Of", code:"&#8712;", symbol: 'in'}, 
      not_in: {name: "not_in", desc: "Is Not One Of", code:"&#8713;", symbol: 'not in'},
      starts_with: {name: "starts_with", desc: "Starts With", code: "&#8963;&#61;", symbol: 'starts with'}, 
      ends_with: {name: "ends_with", desc: "Ends With", code: "&#36;&#61;", symbol: 'ends with'}, 
      contains: {name: "contains", desc: "Contains", code: "&#126;", symbol: 'contains'},
      and: {name: 'and', desc: 'And', code: 'and', symbol: 'and'},
      or: {name: 'or', desc: 'Or', code: 'or', symbol: 'or'},
      intersect: {name: 'intersect', desc: 'Intersection', code: '&#8745;', symbol: 'pand'},
      not: {name: 'not', desc: 'Not', code: 'not', symbol: 'not'}
    };
    

    var getValidOps = function(field) {
      var result = null;
      if (field.dataType == "STRING") {
        result = getStringOps();
      } else {
        result = getNumericOps();
      } 

      if (field.pvs && field.pvs.length > 0) {
        result.push(ops.qin, ops.not_in);
      }

      return result;
    };

    var getStringOps = function() {
      return [ops.eq, ops.ne, ops.exists, ops.not_exists, ops.starts_with, ops.ends_with, ops.contains];
    };

    var getNumericOps = function() {
      return [ops.eq, ops.ne, ops.lt, ops.le, ops.gt, ops.ge, ops.exists, ops.not_exists];
    };

    var getWhereExpr = function(filters, exprNodes) {
      var filterMap = {};
      for (var i = 0; i < filters.length; ++i) {
        filterMap[filters[i].id] = filters[i];
      }

      var query = "";
      for (var i = 0; i < exprNodes.length; ++i) {
        if (exprNodes[i].type == 'paren') {
          query += exprNodes[i].value;
        } else if (exprNodes[i].type == 'op') {
          query += ops[exprNodes[i].value].symbol + " ";
        } else if (exprNodes[i].type == 'filter') {
          var filter = filterMap[exprNodes[i].value];
          query += filter.form.name + "." + filter.field.name + " ";
          query += filter.op.symbol + " ";

          if (filter.op.name == 'exists' || filter.op.name == 'not_exists') {
            continue;
          }

          var queryVal = filter.value;
          if (filter.field.dataType == "STRING" || filter.field.dataType == "DATE") {
            if (filter.op.name == 'qin' || filter.op.name == 'not_in') {
              var quotedValues = [];
              for (var j = 0; j < queryVal.length; ++j) {
                quotedValues.push("\"" + queryVal[j] + "\"");
              }
              queryVal = quotedValues;
            } else {
              queryVal = "\"" + queryVal + "\"";
            }
          }

          if (filter.op.name == 'qin' || filter.op.name == 'not_in') {
            queryVal = "(" + queryVal.join() + ")";
          }

          query += queryVal + " ";
        }
      }
      return query;
    };

    var getQueryExprForValidation = function(exprNodes) {
      var expr = [];
      var success = true, parenCnt = 0;
      for (var i = 0; i < exprNodes.length; ++i) {
        if (exprNodes[i].type == 'paren') {
          if (exprNodes[i].value == '(') {
            parenCnt++;
          } else if (exprNodes[i].value == ')') {
            parenCnt--;

            if (parenCnt < 0) {
              success = false;
              break;
            }
          }
        }

        expr.push(exprNodes[i].value)
      }

      if (success && parenCnt == 0) {
        return {status: true, expr: expr.join(" ")};
      } else {
        return {status: false};
      }
    };

    var isValidQueryExpr = function(exprNodes) {
      var result = getQueryExprForValidation(exprNodes);

      if (!result.status) {
        return false;
      }

      var qre = /^(\(*\s*)*(not)?\s*(\(*\s*)*\d+\s*(\)*\s*)*((and|or|intersect)\s*(\(*\s*)*(not)?\s*(\(*\s*)*\d+(\s*\)*)*\s*)*$/
      var result = qre.test(result.expr);
      console.log("Is Valid Query: " + result);
      return result;
    }

    $scope.getCount= function() {
      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var aql = "select count(distinct CollectionProtocolRegistration.id) as \"cprCnt\", count(distinct Specimen.id) as \"specimenCnt\" where " + query;
      $scope.queryData.notifs.showCount = true;
      $scope.queryData.notifs.waitCount = true;
      QueryService.executeQuery(-1, 'CollectionProtoocolRegistration', aql).then(function(result) {
        $scope.queryData.cprCnt  = result.rows[0][0];
        $scope.queryData.specimenCnt = result.rows[0][1];
        $scope.queryData.notifs.waitCount = false;
      });
    };


    $scope.setPagedData = function(pageNo, recCnt) {
      console.log("start");
      var pageRows = $scope.queryData.resultData.slice((pageNo - 1) * recCnt, pageNo * recCnt);
      console.log("sliced");
      var formatedRows = [];
       
      for (var i = 0; i < pageRows.length; ++i) {
        var row = {};
        for (var j = 0; j < pageRows[i].length; ++j) {
          row["col" + j] = pageRows[i][j];
        }
        formatedRows.push(row);
      }

      $scope.queryData.pagedData = formatedRows;
      console.log("setting paged data done");
    };

    $scope.$watch('queryData.pagingOptions', function(newVal, oldVal) {
      if (newVal !== oldVal && (newVal.currentPage !== oldVal.currentPage || newVal.pageSize !== oldVal.pageSize)) {
        $scope.setPagedData(newVal.currentPage, newVal.pageSize);
      }
    }, true);

    $scope.getRecords = function() {
      $scope.queryData.showRecs = true;

      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var selectList = 
        "CollectionProtocolRegistration.id, CollectionProtocolRegistration.firstName, CollectionProtocolRegistration.lastName, " +
        "CollectionProtocolRegistration.dateOfBirth, CollectionProtocolRegistration.ssn, CollectionProtocolRegistration.gender, " + 
        "CollectionProtocolRegistration.genotype, CollectionProtocolRegistration.race, CollectionProtocolRegistration.regDate, " + 
        "CollectionProtocolRegistration.ppid, CollectionProtocolRegistration.activityStatus";
      var aql = "select " + selectList + " where " + query + " limit 0, 10000";

      var startTime = new Date();
      QueryService.executeQuery(-1, 'CollectionProtocolRegistration', aql).then(function(result) {
        var endTime = new Date();
        var colDefs = [];
        for (var i = 0; i < result.columnLabels.length; ++i) {
          colDefs.push({field: "col" + i, displayName: result.columnLabels[i], width: 100});
        }
        $scope.queryData.resultData = result.rows;
        $scope.queryData.resultCols = colDefs;
        $scope.queryData.resultDataSize = result.rows.length;
        $scope.queryData.pagingOptions.pageSize = 100;
        $scope.queryData.pagingOptions.currentPage = 1;

        
        $scope.setPagedData(1, 100);
        /*if (!$scope.$$phase) {
            $scope.$apply();
        }*/
      });
    };

    $scope.closeNotif = function(type) {
      $scope.queryData.notifs[type] = false;
    };

    $scope.redefineQuery = function() {
      $scope.queryData.pagedData = [];
      $scope.queryData.resultData = [];
      $scope.queryData.resultCols = [];
      $scope.queryData.resultDataSize = 0;
      $scope.queryData.showRecs = false;
    }

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
      if (node.value == 'or') {
        node.value = 'and';
      } else if (node.value == 'and') {
        node.value = 'or';
      }
    };

    $scope.getFilterDesc = function(filterId) {
      var filter = null;
      var filters = $scope.queryData.filters;
      for (var i = 0; i < filters.length; ++i) {
        if (filters[i].id == filterId) {
          filter = filters[i];
          break;
        }
      }

      var desc = "Unknown";
      if (filter) {
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

    $scope.queryData = {
      showCallout: true,
      showRecs: false,
      isValid: true,
      forms: [],
      selectedForm: null,
      filters: [],
      joinType: 'all',
      exprNodes: [],              //{type: filter/op/paran, val: filter id/and-or-intersect-not}
      filterId: 0,
      currFilter: {
        id:null,
        form: null,
        field: null,
        op: null,
        value: null,
        ops: null,
      },

      notifs: {
        showCount: false,
        waitCount: true
      },

      resultData: [],
      pagedData: [],
      resultCols: [],
      resultDataSize: 0,
      pagingOptions: {
         pageSizes: [100, 200, 500],
         pageSize: 100,
         currentPage: 1
      }
    };

    $scope.queryData.resultGridOpts = {
      enableColumnResize: true,
      showFilter: true,
      columnDefs: 'queryData.resultCols',
      showFooter: true, 
      data: 'queryData.pagedData',
      enablePaging: true,
      pagingOptions: $scope.queryData.pagingOptions,
      totalServerItems: 'queryData.resultDataSize'
    };

    CollectionProtocolService.getQueryForms(-1).then(function(forms) {
      $scope.queryData.forms = forms;
    });

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
      if (qd.currFilter.field && qd.currFilter.op) {
        var op = qd.currFilter.op.name;
        return op == 'exists' || op == 'not_exists' || qd.currFilter.value ? false : true;
      } else {
        return true;
      }
    }

    $scope.onFormSelect = function(form) {
      $scope.queryData.currFilter.form = form;
      $scope.queryData.currFilter.field = null;
      $scope.queryData.currFilter.op = null;
      $scope.queryData.currFilter.value = null;
      if (!form.fields) {
        FormService.getFormFields(form.id).then(function(fields) {
          form.fields = fields;
        });
      }
    };

    $scope.onFieldSelect = function(field) {
      console.log("Selected field is " + JSON.stringify(field));
      $scope.queryData.currFilter.field = field;
      $scope.queryData.currFilter.op = null;
      $scope.queryData.currFilter.value = null;
      $scope.queryData.currFilter.ops = getValidOps(field);
      if (!$scope.$$phase) {
        $scope.$apply();
      }
    };

    $scope.onOpSelect = function(op) {
      $scope.queryData.currFilter.op = op;
      $scope.queryData.currFilter.value = null;
      if (!$scope.$$phase) {
        console.log("Apply Selected op: " + JSON.stringify(op));
        $scope.$apply();
      }
    };

    $scope.isUnaryOpSelected = function() {
      var currFilter = $scope.queryData.currFilter;
      return currFilter.op && (currFilter.op.name == 'exists' || currFilter.op.name == 'not_exists');
    };
        
    $scope.getValueType = function() {
      var field = $scope.queryData.currFilter.field;
      var op = $scope.queryData.currFilter.op;

      if (!field) {
        return "text";
      } else if (field.pvs && op && (op.name == "qin" || op.name == "not_in")) {
        return "multiSelect";
      } else if (field.pvs) {
        return "select";
      } else if (field.dataType == "DATE") {
        return "datePicker";
      } else {
        return "text";
      }
    };

    $scope.getOpCode = function(opName) {
      var op = ops[opName];
      return op ? op.code : "";
    }

    $scope.addFilter = function() {
      $scope.queryData.filterId++;
      var filter = {
        id: $scope.queryData.filterId,
        form: $scope.queryData.currFilter.form,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value
      };

      if ($scope.queryData.filters.length > 0) {
        $scope.queryData.exprNodes.push({type: 'op', value: ops.and.name});
      }
      $scope.queryData.filters.push(filter);
      $scope.queryData.exprNodes.push({type: 'filter', value: filter.id});
      $scope.queryData.isValid = isValidQueryExpr($scope.queryData.exprNodes);
      $scope.queryData.currFilter = {};

      if ($scope.queryData.showCallout && $scope.queryData.filters.length == 1) {
        setTimeout(function() { $scope.queryData.showCallout = false; }, 1000);
      }
    };

    $scope.editFilter = function() {
      var filter = {
        id: $scope.queryData.currFilter.id,
        form: $scope.queryData.currFilter.form,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value,
      };

      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters[i] = filter;
          break;
        }
      }
      $scope.queryData.currFilter = {};
    };

    $scope.displayFilter = function(filter) {
      $scope.queryData.currFilter = angular.copy(filter);
      $scope.queryData.currFilter.ops = getValidOps(filter.field);
    };

    $scope.deleteFilter = function(filter) {
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters.splice(i, 1);
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
    };

    $scope.clearFilter = function() {
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
  }]);
