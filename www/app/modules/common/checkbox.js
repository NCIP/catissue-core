
angular.module('openspecimen')
  .directive('osCheckbox', function() {
    return {
      restrict: 'E',
      compile: function(tElem, tAttrs) {
        var inputEl = angular.element("<input type='checkbox'></input>")
        angular.forEach(tAttrs.$attr, function(value, name) {
          inputEl.attr(tAttrs.$attr[name], tAttrs[name]);
        });

        var labelEl = angular.element("<label></label>")
          .addClass("os-checkbox")
          .append(inputEl)
          .append("<span class='box'></span>")
          .append("<span class='tick'></span>");

        tElem.replaceWith(labelEl);
        return angular.noop;
      }
    };
  });
