
angular.module('openspecimen')
  .directive('osMdInput', function($timeout, $document) {

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

    function handleTextArea(scope, element) {
      var threshold    = 6,
          minHeight    = element[0].offsetHeight,
          paddingLeft  = element.css('paddingLeft'),
          paddingRight = element.css('paddingRight');

      var shadow = angular.element('<div></div>').css({
        position:   'absolute',
        top:        -10000,
        left:       -10000,
        width:      element[0].offsetWidth - parseInt(paddingLeft || 0) - parseInt(paddingRight || 0),
        fontSize:   element.css('fontSize'),
        fontFamily: element.css('fontFamily'),
        lineHeight: element.css('lineHeight'),
        resize:     'none'
      });

      angular.element($document[0].body).append(shadow);

      function times (string, number) {
        for (var i = 0, r = ''; i < number; i++) {
          r += string;
        }
        return r;
      }

      function update () {
        var val = element.val().replace(/</g, '&lt;')
          .replace(/>/g, '&gt;')
          .replace(/&/g, '&amp;')
          .replace(/\n$/, '<br/>&nbsp;')
          .replace(/\n/g, '<br/>')
          .replace(/\s{2,}/g, function( space ) {
            return times('&nbsp;', space.length - 1) + ' ';
          });

        shadow.html(val);
        element.css('height', Math.max(shadow[0].offsetHeight + threshold , minHeight));
      }

      scope.$on('$destroy', function() {
        shadow.remove();
      });

      element.bind('keyup change' , update);
      update();
    }

    function linker(scope, element, attrs) {
      scope.showLabel = false;
      $timeout(onKeyUp(scope, element));
      scope.$watch(attrs.ngModel, function(newVal) {
        scope.value = newVal;
        toggleLabel(scope, !!newVal);
      });

      var textAreaEl = element.find('textarea');
      if (textAreaEl.length > 0) {
        handleTextArea(scope, textAreaEl);
      }
    };

    return {
      restrict: 'A',
      scope: true,
      compile: function(tElem, tAttrs) {
        var inputDiv = angular.element('<div/>')
          .addClass('os-md-input');

        var label = angular.element('<label/>')
          .addClass('os-md-input-label')
          .attr('ng-show', 'showLabel')
          .append(tAttrs.placeholder);

        inputDiv.append(label);
        inputDiv.append(tElem.clone().removeAttr('os-md-input').removeAttr('edit-when'));
        
        var next = tElem.next();
        if (angular.isDefined(next.hasAttribute) && next.hasAttribute('os-field-error')) {
          inputDiv.append(next.clone());
        }

        var div = undefined;
        if (tAttrs.editWhen) {
          inputDiv.attr("ng-if", tAttrs.editWhen);

          var valLabel = angular.element('<span class="os-value"/>')
            .attr("ng-if", "!(" + tAttrs.editWhen + ")")
            .attr("title", "{{" + tAttrs.ngModel + "}}")
            .append("{{" + tAttrs.ngModel + "}}");

          div = angular.element('<div class="os-md-input"></div>')
            .append(inputDiv)
            .append(valLabel);
        } else {
          div = inputDiv;
        }
 
        tElem.replaceWith(div);
        return linker;
      }
    };
  });
