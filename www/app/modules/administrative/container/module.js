
angular.module('os.administrative.container',
  [
    'ui.router',
    'os.administrative.container.list',
    'os.administrative.container.addedit',
    'os.administrative.container.detail',
    'os.administrative.container.specimenTypeDropdown'
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
        url: '/container-addedit/:containerId',
        templateUrl: 'modules/administrative/container/addedit.html',
        resolve: {
          container: function($stateParams, Container) {
            if ($stateParams.containerId) {
              return Container.getById($stateParams.containerId);
            }
            return new Container();
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
        controller: function() {
        },
        parent: 'container-detail'
      })
  });
