
angular.module('openspecimen')
  .directive('osPageHeader', function($compile) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var btn = angular.element('<button/>')
          .addClass('os-nav-button')
          .append('<span class="fa fa-bars"></span>');

        element.addClass('os-page-hdr').prepend(btn).removeAttr('os-page-header');
        element.find(":header").addClass("os-title");
        $compile(btn)(scope);
      }
    };
  });
