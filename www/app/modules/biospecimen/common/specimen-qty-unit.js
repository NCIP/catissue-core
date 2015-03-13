angular.module('os.biospecimen.common.specimenQtyUnit', [])
  .directive('osSpecimenQtyUnit', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var units = {
          'Cell': 'Cells',
          'Fluid': 'ml',
          'Molecular': '&#181;g',
          'Tissue': 'gm'
        }

        var specimenClass = attrs.specimenClass;
        if (specimenClass) {
          scope.$watch(specimenClass, function(newVal) {
            if (newVal) {
              element.html(units[newVal]);
            } 
          });
        }
      }
    }
  });
