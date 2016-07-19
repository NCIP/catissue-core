angular.module('openspecimen')
  .directive('osList', function(CheckList) {
    return {
      restrict: 'E',

      templateUrl: 'modules/common/list.html',

      scope: {
        data: '=',

        itemSref: '@',

        cellTemplate: '@',

        enableSelection: '@',

        showItem: '&',
 
        initCtrl: '&'
      },

      controller: function($scope) {
        this.getSelectedItems = function() {
          if (!$scope.checkList) {
            return[];
          }

          return $scope.checkList.getSelectedItems()
        }

      },

      controllerAs: '$list',

      link: function(scope, element, attrs, ctrl) {
        if (scope.initCtrl) {
          scope.initCtrl({$list: ctrl});
        }

        if (scope.enableSelection == 'true' || scope.enableSelection == true) {
          scope.checkList = new CheckList([]);

          scope.$watch('data', function() {
            scope.checkList = new CheckList(scope.data.rows);
          });
        }
      }
    }
  });
