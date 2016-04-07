
angular.module('os.administrative.container',
  [
    'ui.router',
    'os.administrative.container.list',
    'os.administrative.container.addedit',
    'os.administrative.container.detail',
    'os.administrative.container.overview',
    'os.administrative.container.locations',
    'os.administrative.container.replicate',
    'os.administrative.container.util',
    'os.administrative.container.map'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Storage Container Authorization Options
          $scope.containerResource = {
            createOpts: {resource: 'StorageContainer', operations: ['Create']},
            updateOpts: {resource: 'StorageContainer', operations: ['Update']},
            deleteOpts: {resource: 'StorageContainer', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })
      .state('container-list', {
        url: '/containers',
        templateUrl: 'modules/administrative/container/list.html',
        controller: 'ContainerListCtrl',
        parent: 'container-root'
      })
      .state('container-addedit', {
        url: '/container-addedit/:containerId?posOne&posTwo&parentContainerId&parentContainerName&mode&containerTypeId',
        templateUrl: 'modules/administrative/container/addedit.html',
        resolve: {
          container: function($stateParams, Container) {
            if ($stateParams.containerId) {
              return Container.getById($stateParams.containerId);
            }

            return new Container({allowedCollectionProtocols: [], allowedSpecimenClasses: [], allowedSpecimenTypes: []});
          },
          containerType: function($stateParams, ContainerType) {
            if ($stateParams.containerTypeId) {
              return ContainerType.getById($stateParams.containerTypeId);
            }
            
            return null;
          }
        },
        controller: 'ContainerAddEditCtrl',
        parent: 'container-root'
      })
      .state('container-import', {
        url: '/containers-import',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'container-list', title: 'container.list'}],
              objectType: 'storageContainer',
              title: 'container.bulk_import',
              onSuccess: {state: 'container-list'}
            };
          }
        },
        parent: 'container-root'
      })
      .state('container-import-jobs', {
        url: '/containers-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'container-list', title: 'container.list'}],
              title: 'container.bulk_import_jobs',
              objectTypes: ['storageContainer']
            }
          }
        },
        parent: 'container-root'
      })
      .state('container-replicate', {
        url: '/container-replicate/:containerId',
        templateUrl: 'modules/administrative/container/replicate.html',
        controller: 'ContainerReplCtrl',
        resolve: {
          container: function($stateParams, Container) {
            return Container.getById($stateParams.containerId);
          }
        },
        parent: 'container-root'
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
        parent: 'container-root'
      })
      .state('container-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container/overview.html',
        resolve: {
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
        controller: 'ContainerLocationsCtrl',
        parent: 'container-detail'
      });
  })

  .run(function($state, $translate, QuickSearchSvc, Container, Alerts) {
    var opts = {
      template: 'modules/administrative/container/quick-search.html',
      caption: 'entities.container',
      order: 4,
      search: function(searchData) {
        Container.getByName(searchData.containerName).then(
          function(container) {
            if (container == undefined) {
              Alerts.error('search.error', {entity: 'Container', key: searchData.containerName});
              return;
            }

            $state.go('container-detail.overview', {containerId: container.id});
          }
        );
      }
    };

    QuickSearchSvc.register('container', opts);

  });
