
angular.module('os.common.search.ctrl', [])
  .controller('QuickSearchCtrl', function($scope, $document, $timeout, QuickSearchSvc) {

    function init() {
      $scope.quickSearch = {};

      $scope.ctx = {
        entities: QuickSearchSvc.getEntities(),
        tmpl: ''
      };

      $scope.searchData = {};
    }

    $scope.initSearch = function(event) {
      $scope.quickSearch.show = !$scope.quickSearch.show;
      $scope.searchData = {};

      // target.parent.parent gives main search div
      $scope.ele = angular.element(event.target.parentElement.parentElement);
      $scope.ele.bind('click', function(e) {
        e.stopPropagation();
      });

      $document.bind('click', function(e) {
        if(!$scope.quickSearch.show) {
          $document.unbind('click');
        }

        $timeout(function() {
          $scope.quickSearch.show = false;
        });

      });
    }

    $scope.onEntitySelect = function() {
      $scope.ctx.tmpl = QuickSearchSvc.getTemplate($scope.ctx.selectedEntity);
    }

    $scope.search = function() {
      QuickSearchSvc.search($scope.ctx.selectedEntity, $scope.searchData);
      $scope.quickSearch.show = !$scope.quickSearch.show;
    }

    init();
  })

