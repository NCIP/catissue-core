angular.module('openspecimen')
  .directive('osBreadcrumbs', function($compile) {
    function ellipsis() {
      return angular.element('<li/>')
        .append(angular.element('<a/>').append('...'));
    };

    return {
      restrict: 'A',
      link: function(scope, element, attr) {
        element.addClass('os-breadcrumbs');

        var items = element.find('li');
        if (items.length <= 2) {
          return;
        }

        var collapsed = ellipsis().addClass('collapsed');
        element.prepend(collapsed);
        for (var i = 0; i < items.length - 2; i++) {
          angular.element(items[i]).addClass('show-on-hover');
        }

        collapsed.on('mouseenter', function() {
          element.addClass('hover');
        });

        element.on('mouseleave', function() {
          element.removeClass('hover');
        });

        //element.removeAttr('os-breadcrumbs');
        //$compile(element)(scope);
      }
    }
  });
