
angular.module('os.query.util', [])
  .factory('QueryUtil',function($translate, $document) {
    var ops = {
      eq:          {name: "eq",         desc: "", code: "&#61;",       symbol: '=',           model: 'EQ'},
      ne:          {name: "ne",         desc: "", code: "&#8800;",     symbol: '!=',          model: 'NE',},
      lt:          {name: "lt",         desc: "", code: "&#60;",       symbol: '<',           model: 'LT'},
      le:          {name: "le",         desc: "", code: "&#8804;",     symbol: '<=',          model: 'LE'},
      gt:          {name: "gt",         desc: "", code: "&#62;",       symbol: '>',           model: 'GT'},
      ge:          {name: "ge",         desc: "", code: "&#8805;",     symbol: '>=',          model: 'GE'},
      any:         {name: "any",        desc: "", code: "all",         symbol: 'any',         model: 'ANY'},
      exists:      {name: "exists",     desc: "", code: "&#8707;",     symbol: 'exists',      model: 'EXISTS'},
      not_exists:  {name: "not_exists", desc: "", code: "&#8708;",     symbol: 'not exists',  model: 'NOT_EXISTS'},
      qin:         {name: "qin",        desc: "", code: "&#8712;",     symbol: 'in',          model: 'IN'},
      not_in:      {name: "not_in",     desc: "", code: "&#8713;",     symbol: 'not in',      model: 'NOT_IN'},
      starts_with: {name: "starts_with",desc: "", code: "&#8963;&#61;",symbol: 'starts with', model: 'STARTS_WITH'},
      ends_with:   {name: "ends_with",  desc: "", code: "&#36;&#61;",  symbol: 'ends with',   model: 'ENDS_WITH'},
      contains:    {name: "contains",   desc: "", code: "&#126;",      symbol: 'contains',    model: 'CONTAINS'},
      and:         {name: 'and',        desc: "", code: 'and',         symbol: 'and',         model: 'AND'},
      or:          {name: 'or',         desc: "", code: 'or',          symbol: 'or',          model: 'OR'},
      intersect:   {name: 'intersect',  desc: "", code: '&#8745;',     symbol: 'pand',        model: 'PAND'},
      not:         {name: 'not',        desc: "", code: 'not',         symbol: 'not',         model: 'NOT'},
      nthchild:    {name: 'nthchild',   desc: "", code: '&#xf1e0;',    symbol: 'nthchild',    model: 'NTHCHILD'},
      between:     {name: 'between',    desc: "", code: '&#xf1e0;',    symbol: 'between',     model: 'BETWEEN'}
    };

    var propIdFields = {
      'Participant.ppid': {expr: 'Participant.id', caption: '$cprId'},
      'Specimen.label': {expr: 'Specimen.id', caption: '$specimenId'},
      'Specimen.parentSpecimen.parentLabel': {expr: 'Specimen.parentSpecimen.parentId', caption: '$parentSpecimenId'}
    }

    var init = false;

    function initOpsDesc() {
      if (init) {
        return;
      }

      $translate('queries.list').then(
        function() {
          angular.forEach(ops, function(op) {
            op.desc = $translate.instant('queries.ops.' + op.name);
          });
          init = true;
        }
      );
    }

    var searchOp = function(fn) {
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
      return searchOp(function(op) { return op.symbol == symbol });
    };
      
    var getOpByModel = function(model) {
      return searchOp(function(op) { return op.model == model });
    };


    function getAllowedOps(field) {
      var result = null;
      if (field.type == "STRING") {
        result = getStringOps();
      } else {
        result = getNumericOps();
      }

      return result;
    };

    function getStringOps() {
      return [ 
        ops.eq, ops.ne, 
        ops.exists, ops.not_exists, ops.any,
        ops.starts_with, ops.ends_with, 
        ops.contains, ops.qin, ops.not_in
      ];
    };

    function getNumericOps() {
      return [
        ops.eq, ops.ne, 
        ops.lt, ops.le, 
        ops.gt, ops.ge, 
        ops.exists, ops.not_exists, ops.any,
        ops.qin, ops.not_in, 
        ops.between
      ];
    };

    function getValueType(field, op) {
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

    function isUnaryOp(op) {
      return op && (op.name == 'exists' || op.name == 'not_exists' || op.name == 'any');
    }

    function onOpSelect(filter) {
      if (!filter.op) {
        return;
      }

      if (filter.op.name == "between") {
        filter.value = [undefined, undefined];
      } else if (filter.op.name == 'qin' || filter.op.name == 'not_in') {
        filter.value = [];
      } else {
        filter.value = undefined;
      }

      filter.valueType = getValueType(filter.field, filter.op);
      filter.unaryOp = isUnaryOp(filter.op);
    }

    function hidePopovers() {
      var popups = $document.find('div.popover');
      angular.forEach(popups, function(popup) {
        angular.element(popup).scope().$hide();
      });
    }

    function getOp(name) {
      return ops[name];
    }

    function isValidQueryExpr(exprNodes) {
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

    function getFilterExpr(filter) {
      if (filter.expr) {
        return filter.expr;
      }
          
      var expr = filter.form.name + "." + filter.field.name + " ";
      expr += filter.op.symbol + " ";

      if (filter.op.name == 'exists' || filter.op.name == 'not_exists' || filter.op.name == 'any') {
        return expr;
      }

      var filterValue = filter.value;
      if (filter.field.type == "STRING" || filter.field.type == "DATE") {
        if (filter.op.name == 'qin' || filter.op.name == 'not_in' || filter.op.name == 'between') {
          filterValue = filterValue.map(function(value) { return "\"" + value + "\""; });
        } else {
          filterValue = "\"" + filterValue + "\"";
        }
      }

      if (filter.op.name == 'qin' || filter.op.name == 'not_in' || filter.op.name == 'between') {
        filterValue = "(" + filterValue.join() + ")";
      }

      return expr + filterValue;
    }

    function getWhereExpr(filtersMap, exprNodes) {
      var query = "";
      angular.forEach(exprNodes, function(exprNode) {
        if (exprNode.type == 'paren') {
          query += exprNode.value;
        } else if (exprNode.type == 'op') {
          query += ops[exprNode.value].symbol + " ";
        } else if (exprNode.type == 'filter') {
          query += " " + getFilterExpr(filtersMap[exprNode.value]) + " ";
        }
      });
      return query;
    };

    function getSelectList(selectedFields, filtersMap, addPropIds) {
      var addedIds = {}, result = "";

      angular.forEach(selectedFields, function(field) {
        if (typeof field == "string") {
          field = getFieldExpr(filtersMap, field, true);
        } else if (typeof field != "string") {
          if (field.aggFns && field.aggFns.length > 0) {
            var fieldExpr = getFieldExpr(filtersMap, field.name);
            var fnExprs = "";
            for (var j = 0; j < field.aggFns.length; ++j) {
              if (fnExprs.length > 0) {
                fnExprs += ", ";
              }

              var aggFn = field.aggFns[j];
              if (aggFn.name == 'count') {
                fnExprs += 'count(distinct ';
              } else if (aggFn.name == 'c_count') {
                fnExprs += 'c_count(distinct ';
              } else {
                fnExprs += aggFn.name + '(';
              }

              fnExprs += fieldExpr + ") as \"" + aggFn.desc + " \"";
            }

            field = fnExprs;
          } else {
            field = getFieldExpr(filtersMap, field.name, true);
          }
        }

        if (addPropIds) {
          for (var prop in propIdFields) {
            if (!propIdFields.hasOwnProperty(prop)) {
              continue;
            }

            var idField = propIdFields[prop];
            if (field != prop || addedIds[idField.expr]) {
              continue;
            }

            result += idField.expr + " as \"" + idField.caption + "\"" + ", ";
            addedIds[idField.expr] = true;
          }
        }

        result += field + ", ";
      });

      if (result) {
        result = result.substring(0, result.length - 2);
      }

      return result;
    };

    function getFieldExpr(filtersMap, fieldName, includeDesc) {
      var temporalMarker = '$temporal.';
      if (fieldName.indexOf(temporalMarker) != 0) {
        return fieldName;
      }

      var filterId = fieldName.substring(temporalMarker.length);
      var filter = filtersMap[filterId];
      var expr = getTemporalExprObj(filter.expr).lhs;
      if (includeDesc) {
        expr += " as \"" + filter.desc + "\"";
      }

      return expr;
    };

    function getTemporalExprObj(temporalExpr) {
      var re = /<=|>=|<|>|=|!=|\sbetween\s|\sany|\sexists/g
      var matches = undefined;
      if ((matches = re.exec(temporalExpr))) {
        return {
          lhs: temporalExpr.substring(0, matches.index),
          op : matches[0].trim(),
          rhs: temporalExpr.substring(matches.index + matches[0].length)
        }
      }

      return {};
    };

    function getRptExpr(selectedFields, reporting) {
      if (!reporting || reporting.type == 'none') {
        return '';
      }

      var rptFields = getReportFields(selectedFields, true);
      if (reporting.type == 'columnsummary') {
        return getColumnSummaryRptExpr(rptFields, reporting);
      }

      if (reporting.type != 'crosstab') {
        return reporting.type;
      }

      var rowIdx = getFieldIndices(rptFields, reporting.params.groupRowsBy);
      var colIdx = getFieldIndices(rptFields, [reporting.params.groupColBy]);
      colIdx = colIdx.length > 0 ? colIdx[0] : undefined;
      var summaryIdx = getFieldIndices(rptFields, reporting.params.summaryFields);
      var rollupExclIdx = getFieldIndices(rptFields, reporting.params.rollupExclFields);

      var includeSubTotals = "";
      if (reporting.params.includeSubTotals) {
        includeSubTotals = ", true";
      } 

      for (var i = 0; i < summaryIdx.length; ++i) {
        if (rollupExclIdx.indexOf(summaryIdx[i]) != -1) {
          summaryIdx[i] = -1 * summaryIdx[i];
        }
      }

      return 'crosstab(' +
               '(' + rowIdx.join(',') + '), ' + 
               colIdx + ', ' +
               '(' + summaryIdx.join(',') + ') ' + 
               includeSubTotals + 
             ')';
    }

    function getColumnSummaryRptExpr(rptFields, rpt) {
      var expr = 'columnsummary(';
      var addComma = false;
      if (rpt.params.sum && rpt.params.sum.length > 0) {
        expr += "\"sum\",";
        expr += "\"" + rpt.params.sum.length + "\",";
        var sumIdx = getFieldIndices(rptFields, rpt.params.sum);
        sumIdx = sumIdx.map(function(idx) { return "\"" + idx + "\""; });
        expr += sumIdx.join(",");
        addComma = true;
      }

      if (rpt.params.avg && rpt.params.avg.length > 0) {
        if (addComma) {
          expr += ", ";
        }
        expr += "\"avg\",";
        expr += "\"" + rpt.params.avg.length + "\",";
        var avgIdx = getFieldIndices(rptFields, rpt.params.avg);
        avgIdx = avgIdx.map(function(idx) { return "\"" + idx + "\""; });
        expr += avgIdx.join(",");
      }

      expr += ')';
      return expr;
    }

    function getReportFields(selectedFields, fresh) {
      var reportFields = [];

      angular.forEach(selectedFields, function(field) {
        var isAgg = false;
        angular.forEach(field.aggFns, function(aggFn) {
          if (fresh || aggFn.opted) {
            reportFields.push({
              id: field.name + '$' + aggFn.name,
              name: field.name,
              value: aggFn.desc,
              aggFn: aggFn.name
            });
            isAgg = true;
          }
        });

        if (!isAgg) {
          reportFields.push({
            id: field.name, 
            name: field.name, 
            value: field.form + ": " + field.label
          });
        }
      });

      return reportFields;
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

    function getCountAql(filtersMap, exprNodes) {
      var query = getWhereExpr(filtersMap, exprNodes);
      return "select count(distinct Participant.id) as \"cprCnt\", " +
                       "count(distinct Specimen.id) as \"specimenCnt\" " + 
                " where " + query;
    }

    function getDataAql(selectedFields, filtersMap, exprNodes, reporting, addLimit, addPropIds) {
      addPropIds = !!addPropIds && (!reporting || reporting.type != 'crosstab');

      var selectList = getSelectList(selectedFields, filtersMap, addPropIds);
      var where = getWhereExpr(filtersMap, exprNodes);
      var rptExpr = getRptExpr(selectedFields, reporting);
      return "select " + selectList + 
             " where " + where +
             (addLimit ? " limit 0, 10000 " : " ")  + rptExpr;
    }

    function getDefSelectedFields() {
      return [
        "Participant.id", "Participant.firstName", "Participant.lastName",
        "Participant.regDate", "Participant.ppid", "Participant.activityStatus"
      ];
    }

    function getForm(selectedCp, formName) {
      var form = undefined;
      var forms = selectedCp.forms;
      for (var i = 0; i < forms.length; ++i) {
        if (formName == forms[i].name) {
          form = forms[i];
          break;
        }
      }

      return form;
    }

    function getDateFns() {
      return [
        {label: 'current_date',    value: 'current_date()'},
        {label: 'months_between',  value: 'months_between('},
        {label: 'years_between',   value: 'years_between('},
        {label: 'minutes_between', value: 'minutes_between('},
        {label: 'round',           value: 'round('},
        {label: 'date_range',      value: 'date_range('}
      ];
    };

    function getFormsAndFnAdvise(selectedCp) {
      var forms = selectedCp.forms.map(
        function(form) {
          return {label: form.caption, value: form.name};
        }
      );
      return getDateFns().concat(forms);
    };

    function getFieldsAdvise(form) {
      console.log(form);
      var result = [];
      var fields = [].concat(form.staticFields).concat(form.extnFields);
      angular.forEach(fields, function(field) {
        if (field.type == 'DATE' || field.type == 'INTEGER' || field.type == 'FLOAT') {
          var label = field.caption;
          if (field.extensionForm) {
            label = field.extensionForm + ": " + label;
          }

          result.push({label: label, value: field.name});
        }
      });

      return result;
    };

    function getOpIdx(term) {
      var re = /[\+\-\(,<=>!]/g;
      var index = -1, numMatches = 0;
      var match;
      while ((match = re.exec(term)) != null) {
        index = match.index;
        re.lastIndex = ++numMatches;
      }
      return index;
    }

    function getUiFilter(selectedCp, filterDef) {
      if (filterDef.expr) {
        return filterDef;
      }

      var fieldName = filterDef.field;
      var dotIdx = fieldName.indexOf(".");
      var formName = fieldName.substr(0, dotIdx);

      var op = getOpByModel(filterDef.op);
      var value = undefined;
      if (op.name == 'exists' || op.name == 'not_exists' || op.name == 'any') {
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
        form: getForm(selectedCp, formName),
        fieldName: fieldName.substr(dotIdx + 1),
        parameterized: filterDef.parameterized
      };
    };

    function disableCpSelection(queryCtx) {
      if (queryCtx.selectedCp.id == -1) {
        queryCtx.disableCpSelection = false;
        return;
      }

      var filters = queryCtx.filters;
      for (var i = 0; i < filters.length; ++i) {
        if (filters[i].expr && (filters[i].expr.indexOf('.extensions.') != -1 || filters[i].expr.indexOf('.customFields.') != -1)) { 
          queryCtx.disableCpSelection = true;
          return;
        }

        if (!filters[i].expr && (filters[i].field.name.indexOf('extensions.') == 0 || filters[i].field.name.indexOf('customFields.') == 0)) {
          queryCtx.disableCpSelection = true;
          return;
        }
      }

      var selectedFields = queryCtx.selectedFields;
      for (var i = 0; i < selectedFields.length; ++i) {
        var fieldName = undefined;
        if (typeof selectedFields[i] == "string") {
          fieldName = selectedFields[i];
        } else if (typeof selectedFields[i] == "object") {
          fieldName = selectedFields[i].name;
        } 

        if (fieldName.split(".")[1] == 'extensions' || fieldName.split(".")[1] == "customFields") {
          queryCtx.disableCpSelection = true;
          return;
        }
      }

      queryCtx.disableCpSelection = false;
    }
    
    function getStringifiedValue(value) {
      return value.map(
        function(el) {
          if (el.indexOf('"') != -1) {
            return "'" + el + "'";
          } else if (el.indexOf("'") != -1) {
            return '"' + el + '"';
          } else {
            return el;
          }
        }
      ).join(", ");
    }

    return {
      initOpsDesc:         initOpsDesc,

      getAllowedOps:       getAllowedOps,
 
      getValueType:        getValueType,

      isUnaryOp:           isUnaryOp,
 
      onOpSelect:          onOpSelect,

      hidePopovers:        hidePopovers,

      getOp:               getOp,

      isValidQueryExpr:    isValidQueryExpr,

      getCountAql:         getCountAql,

      getDataAql:          getDataAql,

      getDefSelectedFields: getDefSelectedFields,

      getForm:             getForm,

      getFormsAndFnAdvise: getFormsAndFnAdvise,

      getFieldsAdvise:     getFieldsAdvise,

      getOpIdx:            getOpIdx,

      getOpByModel:        getOpByModel,

      getOpBySymbol:       getOpBySymbol,

      getUiFilter:         getUiFilter,

      getTemporalExprObj:  getTemporalExprObj,

      disableCpSelection:  disableCpSelection,

      getStringifiedValue: getStringifiedValue
    };
  });
