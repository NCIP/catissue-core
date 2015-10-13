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
  });
