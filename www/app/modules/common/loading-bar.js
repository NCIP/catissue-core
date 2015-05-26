angular.module('openspecimen')
  .directive('osLoadingBar', function(cfpLoadingBar) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        cfpLoadingBar.start();

        element.load(function() {
          cfpLoadingBar.complete()
        });
      }
    }
  })
