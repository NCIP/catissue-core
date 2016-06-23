angular.module('os.biospecimen.common')
  .directive('osSpecimenTypes', function(PvManager) {

    function transformer(pv) {
      return {specimenClass: pv.parentValue, type: pv.value};
    }

    function getSpecimenTypes() {
      return PvManager.loadPvsByParent('specimen-class', '', true, transformer);
    }

    function linker(scope, element, attrs, formCtrl) {
      scope.types = [];
      formCtrl.getCachedValues('pvs', 'specimen-type', getSpecimenTypes).then(
        function(types) {
          scope.types = types;
        }
      );

      scope.onTypeSelect = function(type) {
        angular.extend(scope.specimen, type);
      }
    }

    return {
      restrict: 'E',

      require: '^osFormValidator',

      scope: {
        specimen: '=',
      },

      replace: true,

      template: 
        '<div>' +
        '  <os-select ng-model="specimen.type" list="types" group-by="\'specimenClass\'"' +
        '    select-prop="type" display-prop="type" on-select="onTypeSelect($item)">' +
        '  </os-select></div>' +
        '</div>',

      compile: function(tElem, tAttrs) {
        var selectEl = tElem.find('os-select');
        if (tAttrs.hasOwnProperty('mdInput')) {
          selectEl.attr('os-md-input', '');
        }

        angular.forEach(tAttrs,
          function(val, attr) {
            if (!attr.startsWith("$")) {
              selectEl.attr(attr, val);
            }
          }
        );

        return linker;
      }
    }
  });
