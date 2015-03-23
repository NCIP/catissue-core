
angular.module('os.query.util', [])
  .factory('QueryUtil',function($translate, $document) {
    var ops = {
      eq:          {name: "eq",         desc: "", code: "&#61;",       symbol: '=',           model: 'EQ'},
      ne:          {name: "ne",         desc: "", code: "&#8800;",     symbol: '!=',          model: 'NE',},
      lt:          {name: "lt",         desc: "", code: "&#60;",       symbol: '<',           model: 'LT'},
      le:          {name: "le",         desc: "", code: "&#8804;",     symbol: '<=',          model: 'LE'},
      gt:          {name: "gt",         desc: "", code: "&#62;",       symbol: '>',           model: 'GT'},
      ge:          {name: "ge",         desc: "", code: "&#8805;",     symbol: '>=',          model: 'GE'},
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

    var opsInit = false;

    function initOpsDesc() {
      if (opsInit) {
        return;
      }

      $translate('queries.list').then(
        function() {
          angular.forEach(ops, function(op) {
            op.desc = $translate.instant('queries.ops.' + op.name);
          });
          opsInit = true;
        }
      );
    }

    function flattenFields(fqn, fields) {
      var result = [];
      angular.forEach(fields, function(field) {
        if (field.type == 'SUBFORM') {
          result = result.concat(flattenFields(fqn + field.name + '.', field.subFields));
        } else {
          var f = angular.extend({}, field);
          f.name = fqn + field.name;
          result.push(f);
        }
      });

      return result;
    };

    function filterAndFlattenFields(fqn, fields, filterfn) {
      var result = [];
      angular.forEach(fields, function(field) {
        if (filterfn(field)) {
          result.push(field);
        }
      });

      return flattenFields(fqn, result);
    };
      
    function flattenStaticFields(fqn, fields) {
      return filterAndFlattenFields(
        fqn, 
        fields, 
        function(field) {
          return field.type != 'SUBFORM' || field.name != 'extensions';
        });
    };

    function flattenExtnFields(extnForms) {
      var fields = [];
      angular.forEach(extnForms, function(extnForm) {
        fields = fields.concat(extnForm.fields);
      });

      return fields;
    };

    function getExtnForms(fqn, fields) {
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
        ops.exists, ops.not_exists, 
        ops.starts_with, ops.ends_with, 
        ops.contains, ops.qin, ops.not_in
      ];
    };

    function getNumericOps() {
      return [
        ops.eq, ops.ne, 
        ops.lt, ops.le, 
        ops.gt, ops.ge, 
        ops.exists, ops.not_exists, 
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
      return op && (op.name == 'exists' || op.name == 'not_exists');
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

    return {
      initOpsDesc:         initOpsDesc,

      flattenStaticFields: flattenStaticFields, 

      flattenExtnFields:   flattenExtnFields, 

      getExtnForms:        getExtnForms,

      getAllowedOps:       getAllowedOps,
 
      getValueType:        getValueType,

      isUnaryOp:           isUnaryOp,

      hidePopovers:        hidePopovers,

      getOp:               getOp,

      isValidQueryExpr:    isValidQueryExpr
    };
  });
