
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
      and: {desc: 'and', code: 'and', symbol: 'and'},
      or: {desc: 'or', code: 'or', symbol: 'or'},
      intersect: {desc: 'intersect', code: '&#8745;', symbol: 'pand'},
      not: {desc: 'not', code: 'not', symbol: 'not'}
    };

    var getField = function(form, fieldName) {
      var result = null;
      for (var i = 0; i < form.fields.length; ++i) {
        if (form.fields[i].name == fieldName) {
          result = form.fields[i];
          break;
        }
      }
      return result;
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
          query += opsMap[exprNodes[i].value].symbol + " ";
        } else if (exprNodes[i].type == 'filter') {
          var filter = filterMap[exprNodes[i].value];
          query += filter.form + "." + filter.field.name + " ";
          query += opsMap[filter.op].symbol + " ";

          if (filter.op == 'exists' || filter.op == 'not_exists') {
            continue;
          }

          var queryVal = filter.value;
          if (filter.field.dataType == "STRING" || filter.field.dataType == "DATE") {
            if (filter.op == 'qin' || filter.op == 'not_in') {
              var quotedValues = [];
              for (var j = 0; j < queryVal.length; ++j) {
                quotedValues.push("\"" + queryVal[j] + "\"");
              }
              queryVal = quotedValues;
            } else {
              queryVal = "\"" + queryVal + "\"";
            }
          }

          if (filter.op == 'qin' || filter.op == 'not_in') {
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

      var qre = /^\(*\s*(not)?\s*\(*\s*\d+\s*\)*\s*((and|or|intersect)\s*\(*\s*(not)?\s*\(*\s*\d+\s*\)*\s*)*$/
      var result = qre.test(result.expr);
      console.log("Is Valid Query: " + result);
      return result;
    }

    $scope.getCount= function() {
      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var aql = "select count(distinct CollectionProtocolRegistration.id) as \"cprCnt\", count(distinct Specimen.id) as \"specimenCnt\" where " + query;
      $scope.queryData.notifs.showCount = true;
      $scope.queryData.notifs.waitCount = true;
      QueryService.executeQuery($scope.queryData.selectedCp.id, 'CollectionProtoocolRegistration', aql).then(function(result) {
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
      QueryService.executeQuery($scope.queryData.selectedCp.id, 'CollectionProtocolRegistration', aql).then(function(result) {
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

    $scope.opDrag = function(e) {
      var op = angular.element(e.currentTarget).attr('data-arg');
      if (!op) {
        return;
      }

      var code = opsMap[op] ? opsMap[op].code : op;
      var cls = (op == '(' || op == ')') ? 'paren-node' : 'op-node';
      return angular.element("<div/>").addClass("pull-left")
               .append(angular.element("<div/>").addClass("filter-item-valign").addClass(cls).html(code));
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
        var nodeType = nodeVal == '(' || nodeVal == ')' ? 'paren' : 'op';
        var node = {type: nodeType, value: nodeVal};

        $scope.queryData.exprNodes[ui.item.index()] = node;
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
      cpList: [],
      selectedCp: null,
      cpForms: [],
      openForm: null,
      filters: [],
      joinType: 'all',
      exprNodes: [],              //{type: filter/op/paran, val: filter id/and-or-intersect-not}
      filterId: 0,
      currFilter: {
        id:null,
        form: null,
        field: null,
        op: null,
        value: null
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

    $scope.onCpSelect = function(selectedCp) {
      $scope.queryData.selectedCp = selectedCp;
      CollectionProtocolService.getQueryForms(selectedCp.id).then(function(forms) {
        var uniqIds = {};
        $scope.queryData.cpForms = forms.filter(function(form) {
          if (selectedCp.id != -1 && form.name == 'CollectionProtocol') {
            return false;
          }
          return !uniqIds[form.containerId] && (uniqIds[form.containerId] = 1);
        });
      });
    };

    CollectionProtocolService.getCpList().then(function(cpList) {
      cpList.unshift({id: -1, shortTitle: "ALL", title: "ALL"});
      $scope.queryData.cpList = cpList;
    });

    $scope.showCpSelectCallout = function() {
      var qd = $scope.queryData;
      return qd.showCallout && !qd.selectedCp && true;
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
      if (qd.currFilter.field && qd.currFilter.op) {
        var op = qd.currFilter.op;
        return op == 'exists' || op == 'not_exists' || qd.currFilter.value ? false : true;
      } else {
        return true;
      }
    }

    $scope.onFormClick = function(form) {
      /*if ($scope.queryData.openForm == form) { // closing of form
        $scope.queryData.openForm = null;
        return;
      }*/

      $scope.queryData.openForm = form;
      if (!form.fields) {
        FormService.getFormFields(form.id).then(function(fields) {
          form.fields = fields;
        });
      }
    };

    $scope.onFieldSelect = function(fieldEl, droppableEl) {
      var fieldId   = fieldEl.attr('data-arg');
      var idx       = fieldId.indexOf(".");
      var formName  = fieldId.substring(0, idx);
      var fieldName = fieldId.substring(idx + 1);

      if ($scope.queryData.openForm.name != formName) {
        return;
      }

      var field = getField($scope.queryData.openForm, fieldName);
      if (field) {
        droppableEl.text(field.caption).removeClass("droppable").addClass("dropped");
        $scope.queryData.currFilter.form = $scope.queryData.openForm;
        $scope.queryData.currFilter.field = field;
        $scope.queryData.currFilter.op = null;
        $scope.queryData.currFilter.value = null;
        $scope.$apply($scope.queryData);
      }
    };

    $scope.addField = function($event, form, field) {
      if ($scope.queryData.openForm != form) {
        return;
      }

      var oldOffset = angular.element($event.currentTarget).offset();
      var temp = angular.element($event.currentTarget).clone().appendTo('body');
      var temp = temp.css("position", "absolute").css("left", oldOffset.left).css("top", oldOffset.top).css("z-index", 1000);

      var newOffset = $("#filterField").offset();
      temp.animate({top: newOffset.top, left: newOffset.left}, 500, function() {
        temp.remove();
        $scope.queryData.currFilter.form = form;
        $scope.queryData.currFilter.field = field;
        $scope.queryData.currFilter.op = null;
        $scope.queryData.currFilter.value = null;
        $scope.$apply($scope.queryData);
      });
    };

    $scope.onOperatorSelect = function(opEl, droppableEl) {
      var op = $scope.queryData.currFilter.op = opEl.attr('data-arg');
      $scope.$apply($scope.queryData);
      droppableEl.html(opsMap[op].code).removeClass("droppable").addClass("dropped");
    };

    $scope.addOp = function($event, op) {
      var oldOffset = angular.element($event.currentTarget).offset();
      var temp = angular.element($event.currentTarget).clone().appendTo('body');
      var temp = temp.css("position", "absolute").css("left", oldOffset.left).css("top", oldOffset.top).css("z-index", 1000);

      var id = !$scope.queryData.currFilter.op ? "#filterOpAb" : "#filterOpPr";
      var newOffset = $(id).offset();
      temp.animate({top: newOffset.top, left: newOffset.left}, 500, function() {
        temp.remove();
        $scope.queryData.currFilter.op = op;
        $scope.queryData.currFilter.value = null;
        $scope.$apply($scope.queryData);
      });
    };

    $scope.onAdvOpDrop = function(opEl, droppableEl) {
      var op = opEl.attr('data-arg');
      var idx = droppableEl.attr('data-node-pos');
      $scope.queryData.exprNodes[idx].value = op;
      $scope.$apply($scope.queryData);
    }

    $scope.getOpDesc = function(op) {
      var opObj = opsMap[op];
      return opObj ? opObj.desc : "Unknown operator";
    };

    $scope.getOpCode = function(op) {
      var opObj = opsMap[op];
      return opObj ? $sce.trustAsHtml(opObj.code) : "Unknown operator";
    };

    $scope.$watch('queryData.currFilter', function(newVal, oldVal) {
      console.log("New: " + newVal + ", Old: " + oldVal);
    });

    $scope.isUnaryOpSelected = function() {
      var currFilter = $scope.queryData.currFilter;
      return currFilter.op && (currFilter.op == 'exists' || currFilter.op == 'not_exists');
    };
        
    $scope.getValueType = function() {
      var field = $scope.queryData.currFilter.field;
      var op = $scope.queryData.currFilter.op;

      if (!field) {
        return "text";
      } else if (field.pvs && (op == "qin" || op == "not_in")) {
        return "multiSelect";
      } else if (field.pvs) {
        return "select";
      } else if (field.dataType == "DATE") {
        return "datePicker";
      } else {
        return "text";
      }
    };

    $scope.addFilter = function() {
      $scope.queryData.filterId++;
      var filter = {
        id: $scope.queryData.filterId,
        form: $scope.queryData.currFilter.form.name,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value,
        formCaption: $scope.queryData.currFilter.form.caption
      };

      if ($scope.queryData.filters.length > 0) {
        var op = $scope.queryData.joinType == 'any' ? 'or' : 'and';
        $scope.queryData.exprNodes.push({type: 'op', value: op});
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
        form: $scope.queryData.currFilter.form.name,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value,
        formCaption: $scope.queryData.currFilter.form.caption
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
      $scope.queryData.currFilter = {
        form:  {name: filter.form, caption: filter.formCaption},
        id:  filter.id,
        field: filter.field,
        op: filter.op,
        value: filter.value
      };
    };

    $scope.deleteFilter = function(filter) {
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters.splice(i, 1);
          break;
        }
      }

      for (var i = 0; i < $scope.queryData.exprNodes.length; ++i) {
        var exprNode = $scope.queryData.exprNodes[i];
        var joinType = $scope.queryData.joinType;
        if (exprNode.type == 'filter' && filter.id == exprNode.value) {
          if (i == 0 && joinType != 'adv') {
            $scope.queryData.exprNodes.splice(0, 2);
          } else if (joinType != 'adv') {
            $scope.queryData.exprNodes.splice(i - 1, 2);
          } else {
            $scope.queryData.exprNodes.splice(i, 1);
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
