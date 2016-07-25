angular.module('os.biospecimen.common.specimenunit', [])
  .factory('SpecimenUnitSvc', function($http, $q, ApiUrls) {
    var callQ = undefined;
    
    // { 'Cell': {'default': 'Cells'}, 'Tissue': {'default': 'gm', 'Fixed Tissue Block': 'blocks'}}
    var unitsMap = undefined; 

    function initCall() {
      callQ = $http.get(ApiUrls.getBaseUrl() + '/specimen-units');
      return callQ;
    }

    function initUnitsMap() {
      return callQ.then(
        function(result) {
          var tempMap = {};
          angular.forEach(result.data, function(unit) {
            var type = unit.type;
            if (!type) {
              type = 'default';
            }

            var clsUnits = tempMap[unit.specimenClass];
            if (!clsUnits) {
              tempMap[unit.specimenClass] = clsUnits = {};
            }
              
            clsUnits[type] = unit;
          });

          unitsMap = tempMap;
          return unitsMap;
        }
      );
    }

    function getUnitFromMap(cls, type) {
      var clsUnits = unitsMap[cls];
      if (!clsUnits) {
        return undefined;
      }

      var typeUnit = clsUnits.default;
      if (!!type && !!clsUnits[type]) {
        typeUnit = clsUnits[type];
      }

      return typeUnit;
    }

    function getUnit(cls, type) {
      var d = $q.defer();

      if (unitsMap) {
        var unit = getUnitFromMap(cls, type);
        d.resolve(unit);
        return d.promise;
      }

      if (!callQ) {
        initCall();
      }

      initUnitsMap().then(
        function() {
          var unit = getUnitFromMap(cls, type);
          d.resolve(unit);
        }
      );

      return d.promise;
    }

    return {
      getUnit: getUnit
    }
  })
  .directive('osSpecimenUnit', function(SpecimenUnitSvc) {
    return {
      restrict: 'E',

      template: '<span></span>',

      replace: true,

      scope: {
        specimenClass: '=',
        type: '='
      },

      link: function(scope, element, attrs) {
        scope.$watchGroup(['specimenClass', 'type'], function(newVals) {
          if (!scope.specimenClass) {
            return;
          }

          var measure = attrs.measure || 'quantity';
          SpecimenUnitSvc.getUnit(scope.specimenClass, scope.type).then(
            function(unit) {
              if (measure == "quantity") {
                element.html(unit.qtyHtmlDisplayCode || unit.qtyUnit);
              } else {
                element.html(unit.concHtmlDisplayCode || unit.concUnit);
              }
            }
          );
        });
      }
    }
  })
  .directive('osSpecimenQty', function() {
    //
    // TODO: Change this directive to os-specimen-measure on analysing the impact
    //
    return {
      restrict: 'E',

      scope: {
        quantity: '=',
        specimen: '=',
        measure: '@',
        placeholder: '@'
      },

      replace: true,

      template:
        '<div class="input-group"> ' +
          '<input type="text" ng-model="quantity" class="form-control" ' +
            'ng-pattern="/^([0-9]+|[0-9]*\.?[0-9]+[e]?[+-]?[0-9]*)$/" placeholder="{{placeholder}}"> ' +
          '<div class="input-group-addon"> ' +
            '<os-specimen-unit specimen-class="specimen.specimenClass" type="specimen.type" ' +
              'measure="{{measure || \'quantity\'}}"> ' +
            '</os-specimen-unit> ' +
          '</div> ' +
        '</div>',

      compile: function(tElem, tAttrs) {
        var inputEl = tElem.find('input');
        if (tAttrs.required != undefined) {
          inputEl.attr('required', '');
        }

        inputEl.attr('name', tAttrs.name);

        if (tAttrs.mdInput) {
          tElem.addClass('os-md-input');
          inputEl.next().addClass('os-md-input-addon');
        }

        return function() { }
      }
    }
  })
  .directive("osSpmnMeasureVal", function() {
    return {
      restrict: 'E',

      scope: {
        value   : '=',
        specimen: '=',
        measure : '@'
      },

      replace: true,

      template:
        '<span class="value value-md" ng-switch="!!value || value == 0">' +
        '  <span ng-switch-when="true">' +
        '    {{value | osNumberInScientificNotation}} ' +
        '    <os-specimen-unit specimen-class="specimen.specimenClass" type="specimen.type"' +
        '      measure="{{measure || \'quantity\'}}">' +
        '    </os-specimen-unit>' +
        '  </span>' +
        '  <span ng-switch-when="false" translate="common.not_specified"></span>' +
        '</span>'
    }
  });
