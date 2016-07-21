angular.module('os.biospecimen.common')
  .factory('SpecimenTypeUtil', function(PvManager) {

    function transformer(pv) {
      return {specimenClass: pv.parentValue, type: pv.value};
    }

    function loadSpecimenTypes() {
      return PvManager.loadPvsByParent('specimen-class', '', true, transformer);
    }

    function getSpecimenTypes(formCtrl) {
      return formCtrl.getCachedValues('pvs', 'specimen-type', loadSpecimenTypes);
    }

    function setType(specimen, types, type) {
      for (var i = 0; i < types.length; ++i) {
        if (types[i].type == type) {
          angular.extend(specimen, types[i]);
          break;
        }
      }
    }

    function init(specimen, types) {
      var type = specimen.type;
      if (!type) {
        return;
      }

      setType(specimen, types, type);
    }

    return {
      setClass: function(formCtrl, specimens) {
        getSpecimenTypes(formCtrl).then(
          function(types) {
            angular.forEach(specimens, function(specimen) { init(specimen, types); });
          }
        );
      },

      getSpecimenTypes: getSpecimenTypes
    }
  })
  .directive('osSpecimenTypes', function($timeout, SpecimenTypeUtil, PvManager) {
    var valuesQ = undefined;

    function linker(scope, element, attrs, formCtrl) {
      scope.types = [];
      scope.model = {value: scope.specimen.type};

      valuesQ = SpecimenTypeUtil.getSpecimenTypes(formCtrl);
      valuesQ.then(function(types) { scope.types = types; });

      scope.onTypeSelect = function(type) {
        angular.extend(scope.specimen, type);
      }

      SpecimenTypeUtil.setClass(formCtrl, [scope.specimen]);
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
        '  <os-select ng-model="model.value" list="types" group-by="\'specimenClass\'"' +
        '    select-prop="type" display-prop="type" on-select="onTypeSelect($item)">' +
        '  </os-select>' +
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
