
angular.module('os.administrative.container.overview', ['os.administrative.models'])
  .controller('ContainerOverviewCtrl', function($scope, $q, container, childContainers, Container) {

    function init() {
      $scope.container = container;
      $scope.childContainers = Container.flatten(childContainers);
    }

    init();
  });
