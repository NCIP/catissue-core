
angular.module('plus.controllers', ['checklist-model'])
  .controller('QueryController', ['$scope', '$sce', '$modal', '$q', 'CollectionProtocolService', 'FormsService', 'QueryService', 'UsersService', function($scope, $sce, $modal, $q, CollectionProtocolService, FormsService, QueryService, UsersService) {
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
      not: {name: 'not', desc: 'Not', code: 'not', symbol: 'not', model: 'NOT'}
    };

    var getOpByModel = function(model) {
      var result = undefined;
      for (var k in ops) {
        if (ops[k].model == model) {
          result = ops[k];
          break;
        }
      }

      return result;
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
          if (filter.field.type == "STRING" || filter.field.type == "DATE") {
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
      return result;
    }

    $scope.nonCpForm = function(form) {
      return form.name != 'CollectionProtocol';
    };

    $scope.getCount= function() {
      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var aql = "select count(distinct Participant.id) as \"cprCnt\", count(distinct Specimen.id) as \"specimenCnt\" where " + query;
      $scope.queryData.notifs.showCount = true;
      $scope.queryData.notifs.waitCount = true;

      var cpId = $scope.queryData.selectedCp.id;
      QueryService.executeQuery(cpId, 'Participant', aql).then(
        function(result) {
          $scope.queryData.cprCnt  = result.rows[0][0];
          $scope.queryData.specimenCnt = result.rows[0][1];
          $scope.queryData.notifs.waitCount = false;
        });
    };


    $scope.setPagedData = function(pageNo, recCnt) {
      var pageRows = $scope.queryData.resultData.slice((pageNo - 1) * recCnt, pageNo * recCnt);
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

    $scope.getRecords = function() {
      $scope.queryData.view = 'records';

      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var selectList = $scope.queryData.selectedFields.join();
      var aql = "select " + selectList + " where " + query + " limit 0, 10000";

      var startTime = new Date();
      $scope.queryData.notifs.waitRecs = true;
      var cpId = $scope.queryData.selectedCp.id;
      QueryService.executeQuery(cpId, 'Participant', aql, true).then(function(result) {
        var endTime = new Date();
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
        $scope.queryData.resultDataSize = result.rows.length;
        $scope.queryData.pagingOptions.pageSize = 100;
        $scope.queryData.pagingOptions.currentPage = 1;

        
        $scope.setPagedData(1, 100);
        $scope.queryData.notifs.waitRecs = false;
      });
    };

    $scope.exportRecords = function() {
      var query = getWhereExpr($scope.queryData.filters, $scope.queryData.exprNodes);
      var selectList = $scope.queryData.selectedFields.join();
      var aql = "select " + selectList + " where " + query;

      Utility.notify(
        $("#notifications"), 
        "Query results export has been initiated. Export file download should start in few moments...", 
        "success", 
        false);

      var cpId = $scope.queryData.selectedCp.id;
      QueryService.exportQueryData(cpId, 'Participant', aql, true).then(
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
      $scope.queryData.pagedData = [];
      $scope.queryData.resultData = [];
      $scope.queryData.resultCols = [];
      $scope.queryData.resultDataSize = 0;
      $scope.queryData.view = 'query';
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
      if (node.value == 'and') {
        node.value = 'or';
      } else if (node.value == 'or') {
        node.value = 'intersect';
      } else if (node.value == 'intersect') {
        node.value = 'and';
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
      if (filter && filter.form && filter.field && filter.op) {
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
      view: 'dashboard',
      cpList: [],
      forms: undefined,
      selectedFolderId: -1
    };

    $scope.getQueryDataDefaults = function() {
      return {
        isValid: true,
        id: undefined,
        title: undefined,
        drivingForm: 'Participant',
        selectedCp: undefined,
        selectedForm: null,
        selectedFields: getDefaultSelectedFields(),
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
          waitCount: true,
          waitRecs: true
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
                var filterText = (col.filterText.indexOf('*') == 0 ? col.filterText.replace('*', '') : "^" + col.filterText) + ";";
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
      grid: undefined,
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
      headerRowHeight: 70
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
      $scope.queryData.queries = [];
      $scope.queryData.selectedQueries = [];
      $scope.queryData.selectedFolderId = -1;
      $scope.queryData.myFolders = [];
      $scope.queryData.sharedFolders = [];
      $scope.queryData.view = 'dashboard';
      QueryService.getQueries(0, 1000).then(function(queries) {
        $scope.queryData.queries = queries;
      });

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
      $scope.queryData.selectedFolderId = folderId;
      if (folderId != -1) {
        QueryService.getFolderQueries(folderId).then(function(queries) {
	  $scope.queryData.queries = queries;
	});
      } else {
        QueryService.getQueries(0, 1000).then(function(queries) {
          $scope.queryData.queries = queries;
        });
      }
    };

    $scope.createQuery = function() {
      angular.extend($scope.queryData, $scope.getQueryDataDefaults());
      $scope.queryData.view = 'query';
    };

    var getFilter = function(filterDef) {
      var fieldName = filterDef.field;
      var dotIdx = fieldName.indexOf(".");
      var formName = fieldName.substr(0, dotIdx);

      var op = getOpByModel(filterDef.op);
      var value = undefined;
      if (op.name == 'exists' || op.name == 'not_exists') {
        value = undefined;
      } else if (op.name != 'qin' && op.name != 'not_in') {
        value = filterDef.values[0];
      } else {
        value = filterDef.values;
      }

      return {
        id: filterDef.id,
        op: getOpByModel(filterDef.op),
        value: value,
        form: $scope.getForm(formName),
        fieldName: fieldName.substr(dotIdx + 1)
      };
    };

    $scope.editQuery = function(query) {
      $scope.loadQuery(query).then(function() { $scope.queryData.view = 'query'; });
    };

    $scope.runQuery = function(query) {
      $scope.loadQuery(query).then( function() { $scope.getRecords(); });
    };

    $scope.loadQuery = function(query) {
      angular.extend($scope.queryData, $scope.getQueryDataDefaults());
      return QueryService.getQuery(query.id).then(
        function(queryDef) {
          var cpList = $scope.queryData.cpList;
          var selectedCp = undefined;
          for (var i = 0; i < cpList.length; ++i) {
            if (cpList[i].id == queryDef.cpId) {
              selectedCp = cpList[i];
              break;
            }
          }

          return $scope.onCpSelect(selectedCp).then(
            function(forms) {
              var filters = [];
              var maxFilterId = 0;
              for (var i = 0; i < queryDef.filters.length; ++i) {
                filters.push(getFilter(queryDef.filters[i]));
                if (maxFilterId < queryDef.filters[i].id) {
                  maxFilterId = queryDef.filters[i].id;
                }
              }

              var qs = [];
              var uniqForms = {};
              for (var i = 0; i < filters.length; ++i) {
                var form = filters[i].form;
                if (!uniqForms[form.name]) {
                  qs.push(getFormFields(selectedCp.id, form));
                  uniqForms[form.name] = true;
                }
              };

              var filtersq = $q.all(qs).then(
                function() {
                  for (var i = 0; i < filters.length; ++i) {
                    filters[i].field = $scope.getFormField(filters[i].form, filters[i].fieldName) 
                  }

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
                selectedFields: queryDef.selectList, filters: filters, exprNodes: exprNodes, 
                id: query.id, title: query.title, filterId: maxFilterId, selectedCp: selectedCp
              };

              angular.extend($scope.queryData, queryProps);
              return filtersq;
            }
          );
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
          folderId: undefined
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
          folderId: function() {
            return folder.id;
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

    var AddEditQueryFolderCtrl = function($scope, $modalInstance, users, queries, folderId) {
      $scope.modalData = {
        users: users,
        queries: queries,
        sharedWith: [],
        folderId: folderId
      };

      if (folderId) {
        QueryService.getFolder(folderId).then(
          function(folderDetail) {
            $scope.modalData.folderName = folderDetail.name,
            $scope.modalData.sharedWith = folderDetail.sharedWith,
            $scope.modalData.queries = folderDetail.queries
          }
        );
      }

      $scope.saveOrUpdateFolder = function () {
        var sharedWith = [];
        for (var i = 0; i < $scope.modalData.sharedWith.length; ++i) {
          sharedWith.push({id: $scope.modalData.sharedWith[i].id});
        }

        var queries = [];
        for (var i = 0; i < $scope.modalData.queries.length; ++i) {
          queries.push({id: $scope.modalData.queries[i].id});
        }

        var folderDetail = {
          id: folderId,
          name: $scope.modalData.folderName,
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

    /*FormsService.getQueryForms().then(function(forms) {
      $scope.queryData.forms = forms;
    });*/
    CollectionProtocolService.getCpList().then(
      function(cpList) {
        $scope.queryData.cpList = cpList;
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
      var deferred = $q.defer();

      $scope.queryData.selectedCp = cp;
      if (!$scope.queryData.forms) {
        FormsService.getQueryForms().then(
          function(forms) {
            $scope.queryData.forms = forms;
            if (!cp.forms) {
              cp.forms = angular.copy(forms);
            }
            deferred.resolve(cp.forms);
          }
        );
      } else {
        if (!cp.forms) {
          cp.forms = angular.copy($scope.queryData.forms);
        }
        deferred.resolve(cp.forms);
      }

      return deferred.promise;
    };

    var getFormFields = function(cpId, form) {
      var deferred = $q.defer();
      if (!form.fields) {
        FormsService.getQueryFormFields(cpId, form.formId).then(
          function(fields) {
            form.fields = fields;
            deferred.resolve(form.fields);
          }
        );
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
      if (qd.currFilter.field && qd.currFilter.op) {
        var op = qd.currFilter.op.name;
        return op == 'exists' || op == 'not_exists' || qd.currFilter.value ? false : true;
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

    var flattenExtnFields = function(fqn, fields) {
      return filterAndFlattenFields(fqn, fields,
        function(field) {
          return field.type == 'SUBFORM' && field.name == 'extensions';
        });
    };
      
    $scope.onFormSelect = function(form) {
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
          form.extnFields = flattenExtnFields("", fields);
        });
      } else if (!form.staticFields) {
        form.staticFields = flattenStaticFields("", fields);
        form.extnFields = flattenExtnFields("", fields);
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
        form.extnFields = flattenExtnFields("", form.fields);
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

    $scope.onOpSelect = function(op) {
      $scope.queryData.currFilter.op = op;
      $scope.queryData.currFilter.value = null;
    };

    $scope.isUnaryOpSelected = function() {
      var currFilter = $scope.queryData.currFilter;
      return currFilter.op && (currFilter.op.name == 'exists' || currFilter.op.name == 'not_exists');
    };
        
    $scope.getValueType = function() {
      var field = $scope.queryData.currFilter.field;
      var op = $scope.queryData.currFilter.op;

      if (!field || !op) {
        return "text";
      } else if (field.pvs && op && (op.name == "qin" || op.name == "not_in")) {
        return "multiSelect";
      } else if (field.pvs && !(op.name == 'contains' || op.name == 'starts_with' || op.name == 'ends_with')) {
        return "select";
      } else if (field.type == "DATE") {
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
      hidePopovers();

      $scope.queryData.filterId++;
      var filter = {
        id: $scope.queryData.filterId,
        form: $scope.queryData.openForm,
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
    };

    $scope.editFilter = function() {
      hidePopovers();

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
      hidePopovers();

      $scope.queryData.currFilter = angular.copy(filter);
      $scope.queryData.currFilter.ops = getValidOps(filter.field);
    };

    $scope.deleteFilter = function(filter) {
      hidePopovers();

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

    var SaveQueryCtrl = function($scope, $modalInstance, queryData) {
      var getQueryDef = function() {
        var filters = [];
        for (var i = 0; i < queryData.filters.length; ++i) {
          var filter = queryData.filters[i];
          var values = (filter.value instanceof Array) ? filter.value : [filter.value]
          filters.push({id: filter.id, field: filter.form.name + "." + filter.field.name, op: filter.op.model, values: values}); 
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
          drivingForm: queryData.drivingForm
        };
      };
      
      $scope.modalData = {
        title: queryData.title
      };
   

      $scope.save = function() {
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


    $scope.defineView = function() {
      var defineViewModal = $modal.open({
        templateUrl: 'define-view.html',
        controller: DefineViewCtrl,
        resolve: {
          queryData: function() {
            return $scope.queryData;
          }
        }
      });

      defineViewModal.result.then(
        function(selectedFields) {
          $scope.queryData.selectedFields = selectedFields;
          $scope.getRecords(); 
        }
      );
    }

    var DefineViewCtrl = function($scope, $modalInstance, queryData) {
      $scope.queryData = queryData;

      var forms = [];
      for (var i = 0; i < queryData.forms.length; ++i) {
        var form = { type: 'form', val: queryData.forms[i].caption, form: queryData.forms[i] };
        forms.push(form);
      }


      $scope.treeOpts = {
        treeData: forms,

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
        }
      };

      $scope.ok = function() {
        var selectedFields = getSelectedFields(forms);
        $modalInstance.close(selectedFields);
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      var processFields = function(prefix, fields) {
        var result = [];
        for (var i = 0; i < fields.length; ++i) {
          var field = {type: 'field', val: fields[i].caption, name: prefix + fields[i].name};
          if (fields[i].type == 'SUBFORM') {
            field.type = 'subform';
            field.children = processFields(prefix + fields[i].name + ".", fields[i].subFields);
          } else {
            field.children = [];
          }
          result.push(field);
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
            node.children = processFields(node.form.name + ".", fields);
            deferred.resolve(node.children);
          }
        );

        return deferred.promise;
      };

      var nodeChecked = function(node) {
        if (node.checked) {
          node.expanded = true;
        }

        for (var i = 0; i < node.children.length; ++i) {
          node.children[i].checked = node.checked;
          nodeChecked(node.children[i]);
        }
      };

      var selectFields = function(selectedFieldsMap, nodes) {
        var i = 0;
        for (var formName in selectedFieldsMap) {
          for (var j = 0; j < nodes.length; j++) {
            if (formName == nodes[j].form.name) {
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
          var nodeIdx = getMatchingNodeIdx(selectedFields[i], fields, level);
          var fieldNode = fields.splice(nodeIdx, 1)[0];
          fields.splice(pos, 0, fieldNode);
          pos++;

          if (fieldNode.type == 'subform') {
            var sfSelectedFields = [];
            while (i < selectedFields.length) {
              var name = selectedFields[i].split(".", level + 1).join(".");
              if (name != fieldNode.name) {
                break;
              }
              sfSelectedFields.push(selectedFields[i]);
              ++i;
            }
            orderAndSetSelectedFields(sfSelectedFields, fieldNode, level + 1);
          } else {
            fieldNode.checked = true;
            ++i;
          }
        }
      };

      var getMatchingNodeIdx = function(fieldName, fields, level) {
        var name = fieldName.split(".", level + 1).join(".");
        var idx = -1;
        for (var i = 0; i < fields.length; ++i) {
          if (name == fields[i].name) {
            idx = i;
            break;
          }
        }

        return idx;
      };

      var getSelectedFields = function(forms) {
        var selected = [];
        for (var i = 0; i < forms.length; ++i) {
          if (forms[i].checked) {
            selected = selected.concat(getAllFormFields(forms[i]));
          } else if (forms[i].children) {
            var fields = forms[i].children;
            for (var j = 0; j < fields.length; ++j) {
              var field = fields[j];
              if (field.type == 'subform') {
                selected = selected.concat(getSelectedFields([field]));
              } else if (field.checked) {
                selected.push(field.name);
              }
            }
          }
        }        
        
        return selected;
      };

      var getAllFormFields = function(form) {
        var result = [];
        for (var i = 0; i < form.children.length; ++i) {
          var field = form.children[i];
          if (field.type == 'subform') {
            result = result.concat(getAllFormFields(field));
          } else {
            result.push(field.name);
          }
        }

        return result;
      };

      var selectedFieldsMap = {};
      for (var i = 0; i < queryData.selectedFields.length; ++i) {
        var selectedField = queryData.selectedFields[i];
        var form = selectedField.split(".", 1);
        if (!selectedFieldsMap[form]) {
          selectedFieldsMap[form] = [];
        }
        selectedFieldsMap[form].push(selectedField);
      }

      selectFields(selectedFieldsMap, forms);
    }
  }]);
