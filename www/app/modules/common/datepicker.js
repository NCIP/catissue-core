angular.module('openspecimen')
  .directive('datepickerPopup', function ($filter, $timeout) {
    function link(scope, element, attrs, ngModel) {
      function parseDate(viewValue) {
        if (angular.isNumber(viewValue)) {
          viewValue = new Date(viewValue);
        }

        if (!viewValue) {
          ngModel.$setValidity('date', true);
          return null;
        } else if (angular.isDate(viewValue) && !isNaN(viewValue)) {
          ngModel.$setValidity('date', true);
          return viewValue;
        } else if (angular.isString(viewValue)) {
          var date = new Date(viewValue);
          if (isNaN(date)) {
            ngModel.$setValidity('date', false);
            return undefined;
          } else {
            ngModel.$setValidity('date', true);
            return date;
          }
        } else {
          ngModel.$setValidity('date', false);
          return undefined;
        }
      }

      scope.$watch(attrs.ngModel, function(val) {
        parseDate(val);
      });

      ngModel.$formatters.unshift(function (value) {
        if (!value) {
          return "";
        }
         
        if (!isNaN(parseInt(value))) {
          value = parseInt(value);
        }
        
        return new Date(value).toISOString();        
      });

      // View -> Model
      /*ngModel.$parsers.push(function(val) {
        try {
          if (!val) {
            return '';
          }

          return $filter('date')(val, 'yyyy-MM-dd');
        } catch (e) {
          return val;
        }
      });*/
    }

    return {
      restrict: 'A',
      require: 'ngModel',
      link: link
    };
  })
  
  .directive ('osDatePicker', function ($rootScope, $timeout) {
    var inputAttrs = ['name', 'required', 'placeholder'];
    
    function linker (scope, element, attrs) {
      if (!scope.dateFormat) {
        scope.dateFormat = $rootScope.global.shortDateFmt;
      }

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
      templateUrl: 'modules/common/datepicker.html',
      scope: {
        date: '=',
        dateFormat: '=?'
      },
      compile: function (tElement, tAttrs) {
        if (tAttrs.mdType == 'true') {
          tElement.children().find('div')
           .attr('os-md-input', '')
           .attr('placeholder', tAttrs.placeholder)
           .attr('ng-model', 'date')
        }

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

