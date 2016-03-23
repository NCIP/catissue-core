
angular.module('os.administrative.containertype',
  [
    'ui.router',
    'os.administrative.containertype.list',
    'os.administrative.containertype.detail',
    'os.administrative.containertype.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('container-type-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          $scope.containerResource = {
            createOpts: {resource: 'StorageContainer', operations: ['Create']}
          }
        },
        parent: 'signed-in'
      })
      .state('container-type-list', {
        url:'/container-types',
        templateUrl:'modules/administrative/container-type/list.html',
        controller: 'ContainerTypeListCtrl',
        parent: 'container-type-root'
      })
      .state('container-type-addedit', {
        url: '/container-type-addedit/:containerTypeId',
        templateUrl: 'modules/administrative/container-type/addedit.html',
        resolve: {
          containerType: function($stateParams, ContainerType) {
            if ($stateParams.containerTypeId) {
              return ContainerType.getById($stateParams.containerTypeId);
            }

            return new ContainerType();
          }
        },
        controller: 'ContainerTypeAddEditCtrl',
        parent: 'container-type-root'
      })
      .state('container-type-detail', {
        url: '/container-types/:containerTypeId',
        templateUrl: 'modules/administrative/container-type/detail.html',
        resolve: {
          containerType: function($stateParams, ContainerType) {
            return ContainerType.getById($stateParams.containerTypeId);
          }
        },
        controller: 'ContainerTypeDetailCtrl',
        parent: 'container-type-root'
      })
      .state('container-type-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container-type/overview.html',
        parent: 'container-type-detail'
      })
  });

