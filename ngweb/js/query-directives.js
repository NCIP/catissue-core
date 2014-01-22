
angular.module("plus.aq.directives", [])
  .directive("aqFilterOps", function() {
    return {
      restrict: "A",
      scope: {
        fieldType: "="
      },
      link: function(scope, element, attrs) {
        scope.$watch('fieldType', function(newVal, oldVal) {
          if (!newVal) {
            element.find('.op').show();
            return;
          }

          if (newVal == 'STRING') {
            element.find('.string').show();
            element.find('.numeric').hide();
          } else if (newVal == 'FLOAT' || newVal == 'DATE' || newVal == 'INTEGER') {
            element.find('.numeric').show();
            element.find('.string').hide();
          } else if (newVal == 'BOOLEAN') {
            element.find('.numeric').hide();
            element.find('.string').hide();
          }
        });
      }
    };
  });
