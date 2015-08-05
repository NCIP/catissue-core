
angular.module('openspecimen')
  .directive('osPageHeader', function($compile) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var btn = angular.element('<button/>')
          .addClass('os-nav-button')
          .append('<span class="fa fa-bars"></span>');

        var div = angular.element('<div/>')
          .css('width', '100%')
          .append(element.find(".os-btns.right"))
          .append(element.find(":header"));

        element.append(div);

        element.addClass('os-page-hdr').prepend(btn).removeAttr('os-page-header');
        element.find(":header")
          .addClass("os-title os-ellipsis")
          .hover(function() {
            var that = angular.element(this);
            that.attr("title", that.text());
          });

        if (element.find('.os-breadcrumbs').length == 0) {
          element.addClass('no-breadcrumbs');
        }

        $compile(btn)(scope);
      }
    };
  });
