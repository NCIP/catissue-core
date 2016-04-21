angular.module('os.fields', [])
  .factory('FieldsSvc', function() {
    var renderers = {};

    var el = angular.element;

    function label(caption, cls) {
      var label = el('<label/>').append(caption);
      if (!!cls) {
        label.addClass(cls);
      }

      return label;
    }

    function span(content) {
      return el('<span/>').append(content);
    }

    function div(cls) {
      var divEl = el('<div/>');
      if (!!cls) { 
        divEl.addClass(cls);
      }

      return divEl;
    }

    function notImplemented(fieldName, field) {
      return div('form-value').append(span((field ? field.type : 'Unknown') + ' not yet implemented'));
    }

    function createErrorEl(opts) {
      var attrs = {
        'os-field-error': '',
        'field': opts.formName + '.' + opts.fieldName
      };

      return div().attr(attrs);
    }

    function createInputEl(opts) {
      var renderer = renderers[opts.field.type];
      if (!renderer) {
        renderer = notImplemented;
      }

      var divEl = div().append(renderer(opts));
      if (opts.showError != false) {
        divEl.append(createErrorEl(opts));
      }

      return divEl;
    }

    function addShowIf(el, opts) {
      if (!opts.field.showIf || !opts.field.showIf.op) {
        return el;
      }

      var objName = opts.objName || 'obj';
      var conds = opts.field.showIf.rules.map(
        function(rule) {
          return objName + '.' + rule.field + ' ' + rule.op + ' ' + rule.value;
        }
      );

      var relOp = (opts.field.showIf.op == 'OR') ? ' || ' : ' && ';
      el.attr('ng-if', conds.join(relOp));
      return el;
    }


    function addInputElAttrs(el, opts) {
      el.attr('name', opts.fieldName);

      var field = opts.field;
      if (field.optional == false) {
        el.attr('required', 'true')
      }

      if (opts.mdInput) {
        el.attr('os-md-input', '');
      }

      if (opts.showPlaceholder != false) {
        el.attr('placeholder', field.caption)
      }

      return el;
    }

    function attrName(opts) {
      var objName = opts.objName || 'obj';
      return objName + '.' + opts.field.name;
    }

    function objLookupMap(objs, prop) {
      prop = prop || 'name';

      var map = {};
      angular.forEach(objs,
        function(obj) {
          if (obj[prop]) {
            map[obj[prop]] = obj;
          }
        }
      );

      return map;
    }

    function overrideField(field, baseFieldsMap, overriddenFieldsMap) {
      if (!field.baseField) {
        return field;
      }

      var baseField = angular.copy(baseFieldsMap[field.baseField] || {});
      if (!!baseField.showIf && !field.showIf) {
        angular.forEach(baseField.showIf.rules,
          function(rule) {
            if (overriddenFieldsMap[rule.field]) {
              rule.field = overriddenFieldsMap[rule.field].name;
            }

            if (overriddenFieldsMap[rule.value]) {
              rule.value = overriddenFieldsMap[rule.value].name;
            }
          }
        )
      }

      return angular.extend(field, angular.extend(baseField, field));
    }

    return {
      registerRenderer: function(type, renderer) {
        renderers[type] = renderer;
      },

      getRenderer: function(type) {
        return renderers[type];
      },

      commonFns: function() {
        return {
          createInputEl: createInputEl,

          label: label,

          span: span,

          div: div,

          addInputElAttrs: addInputElAttrs,

          addShowIf: addShowIf,

          attrName: attrName,

          objLookupMap: objLookupMap,

          overrideField: overrideField
        }
      }
    }
  })

  .factory('SimpleFieldsLayout', function(FieldsSvc) {
    var fns = FieldsSvc.commonFns();

    function renderLabel(opts) {
      return fns.label(opts.field.caption, 'col-xs-3 control-label');
    }

    function renderInputEl(opts) {
      return fns.div('col-xs-6').append(fns.createInputEl(opts));
    }

    function renderField(opts) {
      var fg = fns.div('form-group').append(renderLabel(opts)).append(renderInputEl(opts))
      return fns.addShowIf(fg, opts);
    }

    function render(scope, formName, baseFields, fields) {
      var baseFieldsMap       = fns.objLookupMap(baseFields);
      var overriddenFieldsMap = fns.objLookupMap(fields, 'baseField');

      var copts = {scope: scope, formName: formName};
      var idx = 0;
      return fields.map(
        function(field) {
          field = fns.overrideField(field, baseFieldsMap, overriddenFieldsMap);
          var opts = angular.extend({fieldName: 'fld' + idx, field: field, fieldNo: idx++}, copts);
          return renderField(opts);
        }
      );
    }

    return {
      render: render
    }
  })

  .factory('TableFieldsLayout', function(FieldsSvc) {
    var fns = FieldsSvc.commonFns();

    var el = angular.element;

    function table() {
      return el('<table class="os-table bulk-edit os-border os-table-muted-hdr"/>');
    }

    function thead() {
      return el('<thead class="os-table-head"/>');
    }

    function tbody() {
      return el('<tbody class="os-table-body"/>');
    }

    function tr() {
      return el('<tr class="row"/>');
    }

    function th(widthPct, minWidth, caption) {
      var css = {width: widthPct + '%', 'min-width': minWidth};
      return el('<th class="col os-padding-lr-10"/>').css(css).append(fns.span(caption));
    }
 

    function td(widthPct, minWidth, content) {
      var css = {width: widthPct + '%', 'min-width': minWidth, overflow: 'visible'};
      return el('<td class="col os-padding-lr-10"/>').css(css).append(content);
    }

    function minWidth(field) {
      return !!field.minWidth ? field.minWidth : '160px';
    }

    function renderHeaderRow(fields) {
      var width = 95 / fields.length;

      var headerEls = fields.map(
        function(field) {
          return th(width, minWidth(field), field.caption);
        }
      );
      headerEls.push(th(5, '45px', '&nbsp;'));
      return tr().append(headerEls);
    }

    function renderInputRow(formName, fields) {
      var width = 95 / fields.length;
      var rowForm = 'row' + formName; 

      var inputRow = tr()
        .attr('ng-repeat', 'record in obj')
        .attr('ng-form', rowForm)

      var copts = {formName: rowForm, objName: 'record', mdInput: true, showPlaceholder: false};
      var emptyEl = fns.span('&nbsp;');
      angular.forEach(fields, function(field, index) {
        var opts = {fieldNo: index, field: field, fieldName: 'fld' + index};
        var el = emptyEl;
        if (field.type != 'radio') {
          el = fns.createInputEl(angular.extend(opts, copts));
        }

        inputRow.append(td(width, minWidth(field), el));
      })

      var removeBtn = el('<button class="btn os-btn-text"/>')
        .attr('ng-click', 'removeRow($index)')
        .append(el('<span class="fa fa-remove"/>'));
      inputRow.append(td(5, '45px', removeBtn));

      return inputRow;
    }

    function renderAnotherRow(fields) {
      return tr().append(
        el('<td class="col"/>')
          .attr('colspan', fields.length + 1)
          .append(el('<a/>').attr('ng-click', 'onAddRow()').append("Add Another"))
      );
    }

    function copyFieldValue(src, field, dest) {
      var parts = field.split(".", 2);
      var fieldName = parts[0];

      if (!src[fieldName]) {
        return;
      }

      if (parts.length == 1) {
        dest[fieldName] = src[fieldName];
        return;
      }

      if (!dest[fieldName]) {
        dest[fieldName] = {};
      }

      copyFieldValue(src[fieldName], field.substring(fieldName.length + 1), dest[fieldName]);
    }

    function getFieldValues(record, fields) {
      var result = {};

      angular.forEach(fields,
        function(field) {
          copyFieldValue(record, field, result);
        }
      );

      return result;
    }


    function merge(src, dst, deep) {
      var h = dst.$$hashKey;

      if (!angular.isObject(src) && !angular.isFunction(src)) {
        return dst;
      }

      var keys = Object.keys(src);
      for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        var value = src[key];

        if (deep && angular.isObject(value)) {
          if (angular.isDate(value)) {
            dst[key] = new Date(value.valueOf());
          } else {
            if (!angular.isObject(dst[key])) {
              dst[key] = angular.isArray(value) ? [] : {};
            }

            merge(value, dst[key], true);
          }
        } else {
          dst[key] = value;
        }
      }

      if (h) {
        dst.$$hashKey = h;
      } else {
        delete dst.$$hashKey;
      }

      return dst;
    }

    function copyFieldValues(srcRec, fields, records) {
      var toCopy = getFieldValues(srcRec, fields);

      angular.forEach(records,
        function(record, index) {
          if (index == 0) {
            return;
          }

          merge(toCopy, record, true);
        }
      );
    }

    function watchChangesInGrpFieldValues(scope, grpFields) {
      if (grpFields.length == 0) {
        return;
      }

      var fieldNames = grpFields.map(
        function(field) {
          return field.name;
        }
      );

      scope.$watch('obj[0]',
        function(newRec) {
          copyFieldValues(newRec, fieldNames, scope.obj);
        },
        true
      );

      scope.$watch('obj.length',
        function(newLen, oldLen) {
          if (newLen > oldLen) {
            var toCopy = getFieldValues(scope.obj[0], fieldNames);
            merge(toCopy, scope.obj[newLen - 1], true);
          }
        }
      );
    }

    function flatten(groups) {
      var result = [];
      angular.forEach(groups,
        function(group) {
          angular.forEach(group.fields,
            function(rowFields) {
              result = result.concat(rowFields);
            }
          );
        }
      );

      return result;
    }

    function renderGroupRow(scope, formName, rowNo, rowFields, widthCls) {
      var copts = {scope: scope, formName: formName, mdInput: true, objName: 'obj[0]'};

      var fg = fns.div('form-group');
      var row = fns.div('row').append(fns.div('col-xs-12').append(fg));

      angular.forEach(rowFields,
        function(field, idx) {
          var opts = angular.extend({fieldName: 'grpFld-' + rowNo + '-' + idx, field: field, fieldNo: idx}, copts);
          var fieldDiv = fns.div(widthCls).append(fns.createInputEl(opts));
          if (field.width) {
            fieldDiv.css('width', field.width);
          }

          fns.addShowIf(fieldDiv, opts);
          fg.append(fieldDiv);
        }
      );

      return row;
    }

    function renderGroup(scope, group, idx) {
      var formName = 'grp' + idx;

      var maxElsPerRow = 0;
      angular.forEach(group.fields,
        function(rowFields) {
          if (rowFields.length > maxElsPerRow) {
            maxElsPerRow = rowFields.length;
          }
        }
      );

      var widthCls = 'col-xs-' + Math.floor(12 / maxElsPerRow);
      var rowEls = [];
      angular.forEach(group.fields,
        function(rowFields, rowNo) {
          rowEls.push(renderGroupRow(scope, formName, rowNo, rowFields, widthCls));
        }
      );

      return fns.div('os-section')
        .attr('ng-form', 'grp' + idx)
        .append(fns.label(group.title))
        .append(rowEls);
    }

    function renderGroups(scope, groups) {
      var groupEls = [];
      angular.forEach(groups,
        function(group, idx) {
          groupEls.push(renderGroup(scope, group, idx));
        }
      );

      return groupEls;
    }

    function render(scope, formName, baseFields, fields) {
      var groups = fields.groups;
      var tabFields = fields.table;

      var flattenedGrpFields = flatten(groups);
      watchChangesInGrpFieldValues(scope, flattenedGrpFields);

      var allFields           = flattenedGrpFields.concat(tabFields);
      var baseFieldsMap       = fns.objLookupMap(baseFields);
      var overriddenFieldsMap = fns.objLookupMap(allFields, 'baseField');

      allFields = allFields.map(
        function(field) {
          return fns.overrideField(field, baseFieldsMap, overriddenFieldsMap);
        }
      );

      var groupEls = renderGroups(scope, groups);
      var theadEl = thead().append(renderHeaderRow(tabFields));
      var tbodyEl = tbody().append(renderInputRow(formName, tabFields))
        .append(renderAnotherRow(tabFields));

      var tableEl = table().append(theadEl).append(tbodyEl);
      return fns.div('os-x-scroll').append(groupEls).append(tableEl);
    }

    return {
      render: render
    }
  })

  .directive('osFormFields', function($compile, SimpleFieldsLayout, TableFieldsLayout) {
    return {
      restrict: 'E',

      require: '^form',

      scope: {
        baseFields : '=',

        fields   : '=',

        obj      : '=',

        multiple : '=',

        onAddRow : '&'
      },

      replace: 'true',

      link: function(scope, element, attrs, formCtrl) {
        scope.addRow = function() {
          if (typeof scope.onAddRow == 'function') {
            scope.onAddRow();
          } else {
            scope.obj.push({});
          }
        }

        scope.removeRow = function(index) {
          scope.obj.splice(index, 1);
          if (scope.obj.length == 0) {
            scope.addRow();
          }
        }

        var fields = [];
        var formName = attrs.name + 'Form';
        if (scope.multiple == true) {
          fields = TableFieldsLayout.render(scope, formName, scope.baseFields, scope.fields);
        } else {
          fields = SimpleFieldsLayout.render(scope, formName, scope.baseFields, scope.fields);
        }

        var fieldsDiv = angular.element('<div/>').attr('ng-form', formName).append(fields);
        element.replaceWith(fieldsDiv).removeAttr('osFormFields');

        $compile(fieldsDiv)(scope);
      }
    }
  });
