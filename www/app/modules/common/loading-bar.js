angular.module('openspecimen')
  .directive('osElementLoadingBar', function(cfpLoadingBar) {
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
