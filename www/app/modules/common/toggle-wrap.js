angular.module("openspecimen")
  .directive("osToggleWrap", function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {

        var collapsed = attrs.collapsed == 'false'? false: true;
        element.addClass("os-toggle-wrap");
        toggleWrap(collapsed);
       
        element.find(".key").click(
          function(){
            collapsed = !collapsed;
            toggleWrap(collapsed);
          }
        );

        function toggleWrap(collapsed) { 
          if (collapsed) {
            element.addClass("os-collapsed")
              .removeClass("os-expanded");
            element.find(".fa")
              .removeClass("fa-chevron-down")
              .addClass("fa-chevron-right");
          } else {
            element.addClass("os-expanded")
              .removeClass("os-collapsed");
            element.find(".fa")
              .removeClass("fa-chevron-right")
              .addClass("fa-chevron-down");
          }
        }
      }
    }
  });
