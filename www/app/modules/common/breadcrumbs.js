
angular.module('openspecimen')
  .directive("osBreadcrumbs", function($state, $interpolate) {
    function generateBreadcrumbs(state) {
      var breadcrumbs = [];

      while (state && state.name !== '') {
        if (state.breadcrumb) {
          var title = $interpolate(state.breadcrumb.title)(state.locals.globals);
          breadcrumbs.push({title: title, state: state.breadcrumb.state || state.name});
        }

        state = state.parent;
      }

      return breadcrumbs.reverse();
    };

    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'modules/common/breadcrumbs-template.html',
      link: function(scope) {
        scope.breadcrumbs = generateBreadcrumbs($state.$current);
        scope.$on('$stateChangeSuccess', function() {
          scope.breadcrumbs = generateBreadcrumbs($state.$current);
        });
      }
    };
  }
);
