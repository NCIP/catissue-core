
angular.module('os.administrative.container.overview', ['os.administrative.models'])
  .controller('ContainerOverviewCtrl', function($scope, $q, $translate, container, Container) {
    var placeholder = {};

    function init() {
      placeholder = {name: $translate.instant('common.loading')};
      container.isOpened = true;
      $scope.container = container;

      $scope.childContainers = [];
      $scope.loadChildren(container);
    }

    $scope.loadChildren = function(container) {
      container.lazyLoadFlattenedChildren($scope.childContainers, placeholder);
    };

    init();
  });
