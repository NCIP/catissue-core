
angular.module("plus.aq.directives", [])
  .directive("aqFilterOps", function() {
    return {
      restrict: "A",
      scope: {
        field: "="
      },
      link: function(scope, element, attrs) {
        scope.$watch('field', function(newVal, oldVal) {
          if (!newVal) {
            element.find('.op').show();
            return;
          }

          if (newVal.dataType == 'STRING') {
            element.find('.string').show();
            element.find('.numeric').hide();
          } else if (newVal.dataType== 'FLOAT' || newVal.dataType == 'DATE' || newVal.dataType == 'INTEGER') {
            element.find('.numeric').show();
            element.find('.string').hide();
          } else if (newVal.dataType == 'BOOLEAN') {
            element.find('.numeric').hide();
            element.find('.string').hide();
          }

          if (newVal.pvs && newVal.pvs.length > 0) {
            element.find('.pv').show();
          } else {
            element.find('.pv').hide();
          }
        });
      }
    };
  });
