angular.module('os.biospecimen.specimenlist.name', [])
  .directive('osSpmnListName', function($rootScope) {
    return {
      restrict: 'E',

      templateUrl: 'modules/biospecimen/specimen-list/name.html',

      scope: {
        list: '='
      },

      link: function(scope, element, attr) {
        scope.$watch('list', function(newVal) {
          if (newVal == null || newVal == undefined) {
            return;
          }

          scope.listType = newVal.getListType($rootScope.currentUser);
        });
      }
    }
  });
