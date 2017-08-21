
angular.module('os.query.datepicker', [])
  .directive('osQueryDatePicker', function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var options = {format: 'mm-dd-yyyy'};
        if (attrs.osQueryDatePicker) {
          options = JSON.parse(attrs.osQueryDatePicker);
        }
        element.datepicker({format: options.format, autoclose: true});
      }
    };
  });
