angular.module('openspecimen')
  .directive('osTextareaInputGroup', function() {
    return {
      link: function(scope, element, attrs) {
        if (!element.has('textarea')) {
          return;
        }

        var height = element.height();
        element.find('.input-group-btn button')
               .css('height', height + 'px');
      }
    }
  });
