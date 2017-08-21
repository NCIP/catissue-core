
angular.module('openspecimen')
  .directive('osPageHeader', function($compile) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var navBtn = angular.element('<button/>')
          .addClass('os-nav-button')
          .append('<span class="fa fa-bars"></span>');

        var navEl = angular.element('<div class="os-nav-button-wrapper"/>')
          .append(navBtn);

        var contentEl = angular.element('<div class="os-page-header-content-wrapper"/>')
          .append(element.find('.os-page-header-content'));

        var actionsEl = angular.element('<div class="os-page-header-actions-wrapper"/>')
          .append(element.find('.os-page-header-actions'));

        var innerEl = angular.element('<div class="os-page-header-inner"/>')
          .append(contentEl).append(actionsEl);

        var headerEl = angular.element('<div class="clearfix"/>')
          .append(navEl).append(innerEl);

        element.children().remove();
        element.append(headerEl);

        element.addClass("os-page-header").removeAttr('os-page-header');

        if (element.find('.os-breadcrumbs').length == 0) {
          element.addClass('no-breadcrumbs');
        }

        $compile(navEl)(scope);
      }
    };
  });
