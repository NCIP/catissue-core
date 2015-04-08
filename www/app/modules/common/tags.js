angular.module('openspecimen')
  .directive('osTags', function() {
    return {
      restrict: 'E',
      scope: {
        tags: '=',
        placeholder: '@'
      },
      replace: true,
      templateUrl: 'modules/common/tags.html',

      link: function(scope, element, attrs) {
        scope.keyPress = function(event) {
          if (event.keyCode != 13 || !scope.newTag) {
            return;
          }

          if (!scope.tags) {
            scope.tags = [];
          }

          if (scope.tags.indexOf(scope.newTag) == -1) {
            scope.tags.push(scope.newTag);
          }

          scope.newTag = "";
        };

        scope.removeTag = function(index) {
          scope.tags.splice(index, 1);
        };
      }
    };
  });
