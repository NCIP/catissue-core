
angular.module('os.query.list', ['os.query.models'])
  .controller('QueryListCtrl', function($scope, SavedQuery) {
    function init() {
      $scope.queryList = SavedQuery.list();
    }

    init();
  });
