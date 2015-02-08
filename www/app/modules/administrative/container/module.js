
angular.module('os.administrative.container',
  [
    'ui.router',
    'os.administrative.container.list',
    'os.administrative.container.addedit',
    'os.administrative.container.detail',
    'os.administrative.container.overview',
    'os.administrative.container.util',
    'os.administrative.container.map'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-list', {
        url: '/containers',
        templateUrl: 'modules/administrative/container/list.html',
        controller: 'ContainerListCtrl',
        parent: 'signed-in'
      })
      .state('container-addedit', {
        url: '/container-addedit/:containerId?posOne&posTwo&parentContainerId&parentContainerName',
        templateUrl: 'modules/administrative/container/addedit.html',
        resolve: {
          container: function($stateParams, Container) {
            if ($stateParams.containerId) {
              return Container.getById($stateParams.containerId);
            }

            return new Container({allowedCollectionProtocols: [], allowedSpecimenClasses: [], allowedSpecimenTypes: []});
          } 
        },
        controller: 'ContainerAddEditCtrl',
        parent: 'signed-in'
      })
      .state('container-detail', {
        url: '/containers/:containerId',
        templateUrl: 'modules/administrative/container/detail.html',
        resolve: {
          container: function($stateParams, Container) {
            return Container.getById($stateParams.containerId);
          }
        },
        controller: 'ContainerDetailCtrl',
        parent: 'signed-in'
      })
      .state('container-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container/overview.html',
        resolve: {
          childContainers: function(container) {
            return container.getChildContainers(true); 
          }
        },
        controller: 'ContainerOverviewCtrl',
        parent: 'container-detail'
      })
      .state('container-detail.locations', {
        url: '/locations',
        templateUrl: 'modules/administrative/container/locations.html',
        resolve: {
          occupancyMap: function(container) {
            return container.getOccupiedPositions();
          }
        },
        controller: function($scope, $state, container, occupancyMap) {
          $scope.container = container;
          $scope.occupancyMap = occupancyMap;
          $scope.addContainer = function(posOne, posTwo) {
            var params = {
              posOne: posOne, posTwo: posTwo,
              parentContainerName: container.name,
              parentContainerId: container.id
            };
            $state.go('container-addedit', params);
          }
        },
        parent: 'container-detail'
      });
  });
