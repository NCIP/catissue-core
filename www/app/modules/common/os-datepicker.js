angular.module ('openspecimen')
  .directive ('osDatePicker', function ($timeout) {
    var inputAttrs = ['name', 'required', 'placeholder'];
    
    function linker (scope, element, attrs) {
      scope.datePicker = {isOpen: false};
      scope.showDatePicker = function () {
        $timeout(function () {
          scope.datePicker.isOpen = true; 
        });
      };
    }
    
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'modules/common/os-datepicker.html',
      scope: {
        date: '=',
        dateFormat: '='
      },
      compile: function (tElement, tAttrs) {
        var inputEl = tElement.find('input');
        angular.forEach(inputAttrs, function(inputAttr) {
          if (angular.isDefined(tAttrs[inputAttr])) {
            inputEl.attr(inputAttr, tAttrs[inputAttr]);
          }
        });
        //putting the element manipulation in compile ensures that the directive elements will be addded
        //at the time of compilation which solved form validation problem.
        return linker;
        //if compile is provided the link will not be executed so return link function from compile
      }
    };
  });

