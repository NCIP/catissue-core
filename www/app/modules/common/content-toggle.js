angular.module("openspecimen")
  .directive("osContentToggle", function($compile) {
    function getToggleButton(title) {
      return '<button class="os-btn-section-collapse os-btn-transparent" ng-click="ovCollapsed = !ovCollapsed">' +
        '  <span ng-if="!ovCollapsed" class="fa fa-chevron-circle-down"></span>' +
        '  <span ng-if="ovCollapsed" class="fa fa-chevron-circle-right"></span>' +
        '</button>' +
        '<strong class="key key-sm">'+ title +'</strong>';
    }

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        scope.ovCollapsed = attrs.showByDefault == "collapsed" ? true : false;
        var btn = getToggleButton(attrs.osContentToggle);
        element.children()
          .attr("ng-hide", "ovCollapsed");

        element.prepend(btn);

        $compile(element.contents())(scope);
      }
    }
  });
