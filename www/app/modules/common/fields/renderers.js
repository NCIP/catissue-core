angular.module('os.fields')
  .factory('DefaultFieldRenderer', function(FieldsSvc) {
  
    var el = angular.element;
    var fns = FieldsSvc.commonFns();

    //
    // Text field renderer: opts.field.type == 'text'
    //
    function textFieldRenderer(opts) {
      var inputEl = el('<input type="text" class="form-control">')
        .attr('ng-model', fns.attrName(opts))

      if (!!opts.field.modelOpts) {
        inputEl.attr('ng-model-options', JSON.stringify(opts.field.modelOpts));
      }

      if (!!opts.field.pattern) {
        inputEl.attr('ng-pattern', opts.field.pattern);
      }

      return fns.addInputElAttrs(inputEl, opts);
      
    }

    //
    // Date field renderer: opts.field.type == 'date'
    //
    function dateFieldRenderer(opts) {
      var inputEl = el('<os-date-picker/>').attr('date', fns.attrName(opts));
      fns.addInputElAttrs(inputEl, opts);

      inputEl.removeAttr('os-md-input');
      if (opts.mdInput) {
        inputEl.attr('md-type', true);
      }

      return inputEl;
    }

    //
    // PV field renderer: opts.field.type == 'pvs'
    //
    function pvFieldRenderer(opts) {
      var inputEl = el('<os-pvs/>').attr({
        'ng-model': fns.attrName(opts),
        'attribute': opts.field.attr,
        'append-to-body': true
      });

      if (opts.field.multiple) {
        inputEl.attr('multiple', 'multiple');
      }

      if (opts.field.subsetBy) {
        var objName = opts.objName || 'obj';
        inputEl.attr('parent-val', objName + '.' + opts.field.subsetBy);
      }
      
      return fns.addInputElAttrs(inputEl, opts);
    }

    //
    // Dropdown field renderer: opts.field.type = 'dropdown'
    // 
    function dropdownFieldRenderer(opts) {
      var inputEl = el('<os-dropdown/>').attr({
        'ng-model': fns.attrName(opts),
        'append-to-body': true
      });

      fns.addInputElAttrs(inputEl, opts);

      inputEl.removeAttr('os-md-input');
      if (opts.mdInput) {
        inputEl.attr('md-type', true);
      }

      var listSource = opts.field.listSource;
      inputEl.attr('api-url', listSource.apiUrl);
      if (listSource.selectProp) {
        inputEl.attr('select-prop', listSource.selectProp);
      }

      if (listSource.displayProp) {
        inputEl.attr('display-prop', listSource.displayProp);
      }

      var queryParams = {};
      if (listSource.queryParams) {
        angular.extend(queryParams, listSource.queryParams.static || {});
        
        var objName = opts.objName || 'obj';
        angular.forEach(listSource.queryParams.dynamic, function(value, key) {
          queryParams[key] = '{{' + objName + '.' + value + '}}';
        });
      }

      inputEl.attr('query-params', JSON.stringify(queryParams));
      return inputEl;
    }

    //
    // Radio button field renderer: opts.field.type = 'radio'
    //
    function radioButtonFieldRenderer(opts) {
      var attrName = fns.attrName(opts);

      var inputEls = [];
      angular.forEach(opts.field.options, function(option) {
        var radioEl = el('<input type="radio">')
          .attr('name', opts.fieldName)
          .attr('ng-model', attrName)
          .attr('value', option)

        if (opts.field.optional == false) {
          radioEl.attr('required', 'true');
        }

        var label = el('<label class="radio-inline"/>')
          .append(radioEl)
          .append(fns.span(option));
        inputEls.push(label);
      });

      return inputEls;
    }

    //
    // Storage position field renderer: opts.field.type = 'storage-position'
    //
    function storagePositionFieldRenderer(opts) {
      var objName = opts.objName || 'obj';

      var inputEl = el('<os-storage-position/>')
        .attr('entity', !!opts.field.entity ? objName + '.' + opts.field.entity : objName)
        .attr('cp-id', !!opts.field.cpId ? objName + '.' + opts.field.cpId : undefined);

      if (opts.mdInput) {
        inputEl.attr('os-md-input', 'os-md-input');
      }

      if (opts.showPlaceholder == false) {
        inputEl.attr('hide-placeholder', 'hide-placeholder');
      }

      return inputEl;
    }

    //
    // Sub-form or collection field renderer: opts.field.type = 'collection'
    //
    function collectionFieldRenderer(opts) {
      var width = 94 / opts.field.fields.length;

      var headerRow = el('<tr class="row"/>');
      angular.forEach(opts.field.fields,
        function(field) {
          var col = el('<th class="col"/>')
            .css({'width': width + '%', 'min-width': '150px'})
            .append(fns.span(field.caption))
          headerRow.append(col);
        }
      );

      headerRow.append(el('<th class="col/>').css('width', '45px').append(fns.span('&nbsp;')));

      var formName = opts.field.name.replace('.', '_') + '_form';
      var inputRow = el('<tr class="row"/>')
        .attr('ng-repeat', 'collElement in obj.' + opts.field.name)
        .attr('ng-form', formName);

      var copts = {formName: formName, objName: 'collElement'};
      angular.forEach(opts.field.fields, function(field, index) {
        var opts = angular.extend({field: field, fieldName: 'sfld' + index, fieldNo: index}, copts);
        var col = el('<td class="col"/>')
          .css({'width': width + '%', 'min-width': '150px'})
          .append(FieldsSvc.createInputEl(opts));
        inputRow.append(col);
      })

      var removeBtn = el('<button class="btn btn-default"/>')
        .attr('ng-click', 'removeCollElement(\'' + opts.field.name + '\', $index)')
        .append(el('<span class="fa fa-trash"/>'));
      inputRow.append(el('<td class="col"/>').css('width', '45px').append(removeBtn));

      var addAnother = el('<a/>')
        .attr('ng-click', 'addAnother(\'' + opts.field.name + '\')')
        .append(fns.span("Add Another"));

      var anotherRow = el('<tr class="row"/>').append(
        el('<td class="col"/>')
          .attr('colspan', opts.field.fields.length + 1)
          .append(addAnother)
      );
          
      var theadEl = el('<thead class="os-table-head"/>').append(headerRow);
      var tbodyEl = el('<tbody class="os-table-body"/>').append(inputRow).append(anotherRow);
      var tableEl = el('<table class="os-table os-table-fixed os-no-border"/>').append(theadEl).append(tbodyEl);
      return el('<div/>').css('margin-left', '-8px').append(tableEl);
    } 

    var renderers = {
      'text': textFieldRenderer,
      'date': dateFieldRenderer,
      'pvs' : pvFieldRenderer,
      'dropdown': dropdownFieldRenderer,
      'radio': radioButtonFieldRenderer,
      'storage-position': storagePositionFieldRenderer,
      'collection': collectionFieldRenderer
    };

    return {
      renderers: renderers
    }
  })

  .directive('osDropdown', function($http, ApiUrls) {
    return  {
      restrict: 'E',

      scope: true,

      replace: false,

      template: function(tElem, tAttrs) {
        var attrs = {
          'select-prop': tAttrs.selectProp,
          'display-prop': tAttrs.displayProp,
          'append-to-body': true,
          'list': 'list'
        };

        var el = angular.element('<os-select/>').attr(attrs);
        if (tAttrs.mdType == 'true') {
          el.attr('os-md-input', 'os-md-input');
        }

        return el;
      },

      link: function(scope, element, attrs) {
        var apiEndpoint = ApiUrls.getBaseUrl() + attrs.apiUrl;
        attrs.$observe('queryParams', function() {
          $http.get(apiEndpoint, {params: JSON.parse(attrs.queryParams)}).then(
            function(resp) {
              scope.list = resp.data;
            }
          );
        });  
      }
    }
  })

  .run(function(FieldsSvc, DefaultFieldRenderer) {
    angular.forEach(DefaultFieldRenderer.renderers,
      function(renderer, type) {
        FieldsSvc.registerRenderer(type, renderer);
      }
    );
  });
