angular.module('os.biospecimen.common.specimenqtyunit', [])
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

        if (attrs.osSpecimenQtyUnit) {
          scope.$watch(attrs.osSpecimenQtyUnit, function(newVal) {
            if (newVal) {
              element.html(units[newVal]);
            } 
          });
        }
      }
    }
  });
