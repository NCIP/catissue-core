
angular.module('openspecimen')
  .controller('QuickSearchCtrl', function($scope, $document, QuickSearchSvc) {

    function initSelection() {
      var initialSelected = 'participant';
      $scope.ctx.selectedEntity = initialSelected;
      $scope.ctx.tmpl = QuickSearchSvc.getTemplate(initialSelected);
      $scope.searchData = {};
    }

    function init() {
      $scope.quickSearch = {};

      $scope.ctx = {
        entities: QuickSearchSvc.getEntities(),
        tmpl: ''
      };

      initSelection();
    }

    $scope.initSearch = function(event) {
      $scope.quickSearch.show = !$scope.quickSearch.show;
      initSelection();

      // target.parent.parent gives main search div
      $scope.ele = angular.element(event.target.parentElement.parentElement);
      $scope.ele.bind('click', function(e) {
        e.stopPropagation();
      });

      $document.bind('click', function(e) {
        if(!$scope.quickSearch.show) {
          $document.unbind('click');
        }

        $scope.quickSearch.show = false;
        $scope.$apply();
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

