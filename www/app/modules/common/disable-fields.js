
angular.module('openspecimen')
  .directive('osDisableFields', function() {
    var inputTags = 'input,os-date-picker,os-pvs,os-sites,os-select,fieldset'
  
    function ngModelAttr(el) {
      return el.attr('ng-model');
    }

    var elDataAttrs = {
      'INPUT': ngModelAttr,

      'OS-DATE-PICKER': function(el) {
        return el.attr('date');
      },

      'OS-PVS': ngModelAttr,

      'OS-SITES': ngModelAttr,

      'OS-SELECT': ngModelAttr,

      'FIELDSET': function(el) {
        return el.attr('record-name');
      }
    }

    return {
      restrict: 'A',

      compile: function(tElem, tAttrs) {
        if (!tAttrs.osDisableFields) {
          // no options provided
          return;
        }

        angular.forEach(tElem.find(inputTags),
          function(el) {
            var attrFn = elDataAttrs[el.tagName];
            if (!attrFn) {
              return;
            }

            el = angular.element(el);

            var attr = attrFn(el);
            if (!attr) {
              return;
            }

            var expr = tAttrs.osDisableFields + '.fields["' + attr + '"]';
            if (!!el.attr('ng-disabled')) {
              expr = '(' + el.attr('ng-disabled') + ') || ' + expr;
            }

            el.attr('ng-disabled', expr);
          }
        );
      }
    }
  });
