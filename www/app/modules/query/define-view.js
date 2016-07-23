angular.module('os.query.defineview', ['os.query.models']) 
  .controller('DefineViewCtrl', function($scope, $modalInstance, $q, queryCtx, $translate, Form, Alerts) {
    var aggFns = { // TODO: Translate labels
      count: {name: 'count', label: 'Count'},
      sum:   {name: 'sum',   label: 'Sum'},
      min:   {name: 'min',   label: 'Min'},
      max:   {name: 'max',   label: 'Max'},
      avg:   {name: 'avg',   label: 'Avg'}
    };

    var forms = undefined;

    function init() {
      initAggFnsDesc();

      $scope.wideRowMode = angular.copy(queryCtx.wideRowMode); 
      $scope.reporting = angular.copy(queryCtx.reporting);
      $scope.pivotTable = (queryCtx.reporting.type == 'crosstab');
      
      $scope.reportingOpts = [];
      $translate('queries.reporting.none').then(
        function() {
          $scope.reportingOpts = [
            {type: 'none',          label: $translate.instant('queries.reporting.none')},
            {type: 'crosstab',      label: $translate.instant('queries.reporting.crosstab')},
            {type: 'columnsummary', label: $translate.instant('queries.reporting.columnsummary')}
          ];
        }
      );

      forms = getForms();
      $scope.treeOpts = getTreeOpts();
      selectFields(getSelectedFieldsMap(), forms);
    }

    function initAggFnsDesc() {
      $translate('queries.list').then(
        function() {
          angular.forEach(aggFns, function(aggFn) {
            aggFn.label = $translate.instant('queries.agg_fn_labels.' + aggFn.name);
          });
        }
      );
    }

    $scope.ok = function() {
      if (!$scope.selectedFields || $scope.selectedFields.length == 0) {
        $scope.selectedFields = getSelectedFields(forms);
      }

      if (!$scope.selectedFields || $scope.selectedFields.length == 0) {
        Alerts.error('queries.no_fields_selected');
        return;
      }

      var rptParams = $scope.reporting.params;
      if ($scope.reporting.type == 'crosstab') {
        if (!rptParams.groupRowsBy || rptParams.groupRowsBy.length == 0) {
          Alerts.error('queries.pivot_table_no_row_fields');
          return;
        }

        if (!rptParams.groupColBy) {
          Alerts.error('queries.pivot_table_no_col_field');
          return;
        }

        if (!rptParams.summaryFields || rptParams.summaryFields.length == 0) {
          Alerts.error('queries.pivot_table_no_summary_fields');
          return;
        }
      } else if ($scope.reporting.type == 'columnsummary') {
        if ((!rptParams.sum || rptParams.sum.length == 0) && 
            (!rptParams.avg || rptParams.avg.length == 0)) {
          Alerts.error('queries.no_total_or_avg_fields');
          return;
        }
      }

      sanitizeSelectedFields($scope.selectedFields);
      $modalInstance.close(angular.extend(queryCtx, {
         wideRowMode: $scope.wideRowMode, 
         selectedFields: $scope.selectedFields, 
         reporting: $scope.reporting
        })
      );
    }

    $scope.cancel = function() {
      //sanitizeSelectedFields($scope.selectedFields);
      $modalInstance.dismiss('cancel');
    };

    $scope.setWideRowsMode = function(wideRows) {
      $scope.wideRowMode = wideRows == true ? 'DEEP' : 'SHALLOW';
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

    $scope.onReportTypeSelect = function() {
      var type = $scope.reporting.type;
      if (type == 'crosstab') {
        $scope.reporting = {type: 'crosstab', params: {groupRowsBy: [], summaryFields: [], rollupExclFields: []}};
        $scope.preparePivotTableOpts();
      } else if (type == 'columnsummary') {
        $scope.reporting = {type: 'columnsummary', params: {sum: [], avg: []}};
        $scope.prepareColumnSummaryReportOpts();
      } else {
        $scope.reporting = {type: '', params: {}};
      } 
    };

    $scope.prepareAggregateOpts = function() {
      var init = !$scope.selectedFields || $scope.selectedFields.length == 0;

      $scope.selectedFields = getSelectedFields(forms, true);
      $scope.currField = undefined;

      if (!$scope.selectedFields || $scope.selectedFields.length == 0) {
        Alerts.error('queries.no_fields_selected');
        return false;
      }

      prepareAggFns($scope.selectedFields, aggFns, init);
      return true;
    };   

    $scope.prepareReportingOpts = function() {
      if ($scope.reporting.type == 'crosstab') {
        return $scope.preparePivotTableOpts();
      } else if ($scope.reporting.type == 'columnsummary') {
        return $scope.prepareColumnSummaryReportOpts();
      }

      return true;
    }

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

      if (!rptParams.rollupExclFields) {
        rptParams.rollupExclFields = [];
      } else {
        rptParams.rollupExclFields = removeUnselectedFields(rptParams.rollupExclFields, $scope.reportFields);
      }

      preparePivotTabFields($scope.reportFields);
      return true;
    };

    $scope.prepareColumnSummaryReportOpts = function() {
      if ($scope.reporting.type != 'columnsummary') {
        return true;
      }

      $scope.columnSummaryFields = getColumnSummaryFields($scope.selectedFields);
      var rptParams = $scope.reporting.params;
      if (rptParams.sum) {
        rptParams.sum = removeUnselectedFields(rptParams.sum, $scope.columnSummaryFields);
      }

      if (rptParams.avg) {
        rptParams.avg = removeUnselectedFields(rptParams.avg, $scope.columnSummaryFields);
      }

      prepareColumnSummaryFields();
      return true;
    }

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
      removeUnselectedFields($scope.reporting.params.rollupExclFields, newVal);
      preparePivotTabFields($scope.reportFields);
    };

    $scope.onColumnSumFieldsChange = function(newVal) {
      $scope.reporting.params.sum = newVal;
      prepareColumnSummaryFields();
    };

    $scope.onColumnAvgFieldsChange = function(newVal) {
      $scope.reporting.params.avg = newVal;
      prepareColumnSummaryFields();
    };

    $scope.passThrough = function() {
      return true;
    };

    function getForms() {
      var forms = queryCtx.selectedCp.forms.map(
        function(form) {
          return {type: 'form', val: form.caption, form: form};
        }
      );
 
      angular.forEach(queryCtx.filters,
        function(filter) {
          if (filter.expr) {
            forms.push({type: 'temporal', val: filter.desc, form: filter, children: []});
          }
        }
      );

      return forms;
    }
   
    function getTreeOpts() {
      return {
        treeData: forms,
        nodeTmpl: 'define-view-node.html',

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
      }
    }

    function sanitizeSelectedFields(selectedFields) {
      if (!selectedFields) {
        return;
      }

      angular.forEach(selectedFields, 
        function(selectedField) {
          delete selectedField.label;
          delete selectedField.type;

          var aggFns = selectedField.aggFns;
          if (!aggFns) {
            return;
          }

          for (var i = aggFns.length - 1; i >= 0; i--) {
            if (!aggFns[i].opted) {
              aggFns.splice(i, 1);
            }
          }

          if (aggFns.length == 0) {
            delete selectedField.aggFns;
          }
        }
      );
    }

    function getGroupFields(reportFields, exclude) {
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

    function getGroupRowsBy(reportFields) {
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

    function getGroupColBy(reportFields) {
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

    function getSummaryFields(reportFields) {
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

    function getPivotTabFields(selectedFields, fresh) {
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

    function preparePivotTabFields(reportFields) {
      $scope.groupRowsBy = getGroupRowsBy(reportFields);       
      $scope.groupColBy = getGroupColBy(reportFields);       
      $scope.summaryFields = getSummaryFields(reportFields);       
    };

    function removeUnselectedFields(dstArr, srcArr) {
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

    function getTypeAggFns(type, fns) {
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

    function prepareAggFns(fields, fns, fresh) {
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

    function processFields(formCaption, prefix, fields, extnForm) {
      var result = [];
      for (var i = 0; i < fields.length; ++i) {
        if (fields[i].type == 'SUBFORM' && Form.isExtendedField(fields[i].name)) { 
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

    function loadNodeChildren(node) {
      var deferred = $q.defer();
      if (node.type != 'form' || node.children) {
        deferred.resolve(node.children);
        return deferred.promise;
      }

      //getFormFields(queryCtx.selectedCp.id, node.form).then(
      node.form.getFields().then(
        function(fields) {
          node.children = processFields(node.form.caption, node.form.name + ".", fields);
          deferred.resolve(node.children);
        }
      );

      return deferred.promise;
    };

    function nodeChecked(node) {
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

    function selectFields(selectedFieldsMap, nodes) {
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

    function selectFormFields(selectedFields, node) {
      loadNodeChildren(node).then(
        function() {
          orderAndSetSelectedFields(selectedFields, node, 1);
        }
      );
    }

    function orderAndSetSelectedFields(selectedFields, node, level) {
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

        if (Form.isExtendedField(fieldParts[1])) {
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

    function getMatchingNodeIdx(field, fields, level) {
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

    function getSelectedFields(forms, incCaption) {
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
                selected.push({
                  form: field.form, 
                  name: field.name, 
                  label: field.val, 
                  type: field.dataType, 
                  aggFns: field.aggFns
                });
              } else {
                if (!incCaption) {
                  selected.push(field.name);
                } else {
                  selected.push({
                    form: field.form, 
                    name: field.name, 
                    label: field.val, 
                    type: field.dataType
                  });
                }
              }
            }
          }
        }
      }        
        
      return selected;
    }

    function getAllFormFields(form, incCaption) {
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
          if (incCaption) {
            result.push({
              form: field.form,
              name: field.name,
              label: field.val,
              type: field.dataType
            });
          } else {
            result.push(field.name);
          }
        }
      }

      return result;
    };

    function getSelectedFieldsMap() {
      var selectedFieldsMap = {};
      angular.forEach(queryCtx.selectedFields, 
        function(selectedField) {
          var copy = angular.copy(selectedField);
          if (typeof copy == "string" && copy.indexOf("$temporal.") == 0) {
            selectedFieldsMap[copy] = copy;
            return;
          }

          var form;
          if (typeof copy == "string") {
            form = copy.split(".", 1);
          } else {
            form = copy.name.split(".", 1);
          }
         
          if (!selectedFieldsMap[form]) {
            selectedFieldsMap[form] = [];
          }

          selectedFieldsMap[form].push(copy);
        }
      );

      return selectedFieldsMap;
    }

    function getColumnSummaryFields(selectedFields) {
      var columnSummaryFields = [];

      for (var i = 0; i < selectedFields.length; ++i) {
        var field = selectedFields[i];
        var isAgg = false;
        var len = field.aggFns ? field.aggFns.length : 0;
        for (var j = 0; j < len; ++j) {
          var aggFn = field.aggFns[j];
          if (aggFn.opted) {
            columnSummaryFields.push({
              id: field.name + '$' + aggFn.name,
              name: field.name,
              value: aggFn.desc,
              aggFn: aggFn.name
            });
            isAgg = true;
          }
        }

        if (!isAgg && (field.type == 'INTEGER' || field.type == 'FLOAT')) {
          columnSummaryFields.push({id: field.name, name: field.name, value: field.form + ": " + field.label});
        }
      }

      return columnSummaryFields;
    }

    function getColumnSumFields(reportFields) {
      var rptParams = $scope.reporting.params;

      var excludeFields = [];
      if (rptParams.avg) {
        excludeFields = excludeFields.concat(rptParams.avg);
      }

      return getGroupFields(reportFields, excludeFields);
    };

    function getColumnAvgFields(reportFields) {
      var rptParams = $scope.reporting.params;

      var excludeFields = [];
      if (rptParams.sum) {
        excludeFields = excludeFields.concat(rptParams.sum);
      }

      return getGroupFields(reportFields, excludeFields);
    };

    function prepareColumnSummaryFields() {
      $scope.columnSumFields = getColumnSumFields($scope.columnSummaryFields);
      $scope.columnAvgFields = getColumnAvgFields($scope.columnSummaryFields);
    }

    init();
  });
