angular.module('os.biospecimen.specimenlist.name', [])
  .directive('osSpmnListName', function($rootScope) {
    var pattern = /\$\$\$\$user_\d+/;

    function getListType(list) {
      if (list === undefined || list === null || pattern.exec(list.name) == null) {
        return 'none';
      }

      if (list.owner.id == $rootScope.currentUser.id) {
        return 'selfDefaultList';
      } else {
        return 'othersDefaultList';
      }
    }

    return {
      restrict: 'E',

      templateUrl: 'modules/biospecimen/specimen-list/name.html',

      scope: {
        list: '='
      },

      link: function(scope, element, attr) {
        scope.$watch('list', function(newVal) {
          scope.listType = getListType(newVal);
        });
      }
    }
  });
