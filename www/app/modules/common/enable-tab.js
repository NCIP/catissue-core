angular.module('openspecimen')
  .directive('osEnableTab', function() {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element.keydown(
          function(event) {
            if (event.keyCode === 9) { // tab was pressed
              var val = this.value,
                  start = this.selectionStart,
                  end = this.selectionEnd;

              this.value = val.substring(0, start) + '\t' + val.substring(end);
              this.selectionStart = this.selectionEnd = start + 1;
              return false;
            }
          }
        );
      }
    }
  });
