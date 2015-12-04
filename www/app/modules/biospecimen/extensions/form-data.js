
angular.module('os.biospecimen.extensions')
  .directive('osFormData', function() {

    function getSimpleFieldsEl() {
      return angular.element('<ul class="os-key-values os-one-col"/>');
    }

    function getFieldValueEl(field) {
      var el = angular.element('<li class="item"/>');
      el.append(angular.element('<span class="key key-sm"/>').text(field.caption));

      var value = field.value;
      if (value instanceof Array) {
        value = value.join(", ");
      }
      el.append(angular.element('<span class="value value-md"/>').text(value));
 
      return el;
    }

    function getSectionEl(field) {
      var title = angular.element('<h3 class="os-sub-section-title"/>').append(field.caption);
      return angular.element('<div class="os-section"/>').append(title);
    }

    function getSubFormSection(field) {
      
      var table = angular.element('<table class="os-table os-table-muted-hdr os-border"/>')
        .append(getSubFormTableHead(field.value))
        .append(getSubFormTableBody(field.value));
     
      return getSectionEl(field)
        .append(angular.element('<div/>').append(table));
    }

    function getSubFormTableHead(sfRecs) {
      var thead = angular.element('<thead class="os-table-head"/>');
      if (sfRecs.length < 1) {
        return thead;
      }

      var row = angular.element('<tr class="row"/>');
      angular.forEach(sfRecs[0].fields, function(field) {
        row.append(angular.element('<th class="col"/>').append(field.caption));
      });

      return thead.append(row);
    }

    function getSubFormTableBody(sfRecs) {
      var tbody = angular.element('<tbody class="os-table-body"/>');
      angular.forEach(sfRecs, function(rec) {
        var row = angular.element('<tr class="row"/>');
        angular.forEach(rec.fields, function(field) {
          var value = field.value;
          if (value instanceof Array) {
            value = value.join(", ");
          }
          row.append(angular.element('<td class="col"/>').append(value));
        });
        tbody.append(row);
      });

      return tbody;
    };

    return {
      restrict: 'E',

      replace: true,

      template: '<div></div>',

      scope: {
        data: '='
      },

      link: function(scope, element, attrs) {
        var fields = scope.data.fields;
        
        var els = [];
        var simpleFieldsEl = undefined;
        angular.forEach(fields, function(field) {
          if (field.type == 'subForm') {
            if (simpleFieldsEl) {
              els.push(simpleFieldsEl);
              simpleFieldsEl = undefined;
            }
            els.push(getSubFormSection(field));
          } else {
            if (!simpleFieldsEl) {
              simpleFieldsEl = getSimpleFieldsEl();
            }

            simpleFieldsEl.append(getFieldValueEl(field));
          }
        });

        if (simpleFieldsEl) {
          els.push(simpleFieldsEl);
        }

        element.append(els);
      }
    };
  });
