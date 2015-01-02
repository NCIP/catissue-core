
angular.module('openspecimen')
  .directive('osMdInput', function($timeout) {

    function toggleLabel(scope, show) { 
      if (scope.showLabel == show) {
        return;
      }

      $timeout(function() {
        scope.showLabel = show;
      });
    };

    function onKeyUp(scope, element) {
      return function() {
        var input = element.find('input');
        if (input.length <= 0) {
          return;
        }
          
        input.bind('keyup', function(event) {
          var val = input.val();
          var showLabel = false;
          if (angular.isDefined(val) && val.length > 0) {
            showLabel = true;
          } else if (!!scope.value) {
            showLabel = true;
          }

          toggleLabel(scope, showLabel);
        });
      };
    };

    return {
      restrict: 'A',
      scope: true,
      compile: function(tElem, tAttrs) {
        var el = angular.element('<div/>')
          .addClass('os-md-input');

        var label = angular.element('<label/>')
          .addClass('os-md-input-label')
          .attr('ng-show', 'showLabel')
          .append(tAttrs.placeholder);

        el.append(label);
        el.append(tElem.clone().removeAttr('os-md-input'));
        
        var next = tElem.next();
        if (angular.isDefined(next.hasAttribute) && next.hasAttribute('os-field-error')) {
          el.append(next.clone());
        }

        tElem.replaceWith(el);
        return function(scope, element, attrs) {
          scope.showLabel = false;
          $timeout(onKeyUp(scope, element));
          scope.$watch(attrs.ngModel, function(newVal) {
            scope.value = newVal;
            toggleLabel(scope, !!newVal);
          });
        };
      }
    };
  });
