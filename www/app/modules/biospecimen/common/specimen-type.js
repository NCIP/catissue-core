angular.module('os.biospecimen.common')
  .directive('osSpecimenTypes', function(PvManager, $timeout) {
    var valuesQ = undefined;

    function transformer(pv) {
      return {specimenClass: pv.parentValue, type: pv.value};
    }

    function getSpecimenTypes() {
      return PvManager.loadPvsByParent('specimen-class', '', true, transformer);
    }

    function setType(specimen, types, type) {
      for (var i = 0; i < types.length; ++i) {
        if (types[i].type == type) {
          angular.extend(specimen, types[i]);
          break;
        }
      }
    }

    function linker(scope, element, attrs, formCtrl) {
      scope.types = [];
      valuesQ = formCtrl.getCachedValues('pvs', 'specimen-type', getSpecimenTypes);
      valuesQ.then(function(types) { scope.types = types; });

      scope.onTypeSelect = function(type) {
        angular.extend(scope.specimen, type);
      }

      scope.$on('osRecsLoaded', function() {
        var type = scope.specimen.type;
        if (!type) {
          return;
        }

        valuesQ.then(function(types) { setType(scope.specimen, types, type); });
      });
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
