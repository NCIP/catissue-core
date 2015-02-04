angular.module('os.administrative.dp', 
  [ 
    'ui.router',
    'os.administrative.dp.list',
    'os.administrative.dp.detail',
    'os.administrative.dp.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('dp-list', {
        url: '/dps',
        templateUrl: 'modules/administrative/dp/list.html',     
        controller: 'DpListCtrl',
        parent: 'signed-in'
      })
      .state('dp-new', {
        url: '/new-dp',
        templateUrl: 'modules/administrative/dp/addedit.html',
        resolve: {
          distributionProtocol: function(DistributionProtocol) {
            return new DistributionProtocol();
          }
        },
        controller: 'DpAddEditCtrl',
        parent: 'signed-in'
      })
      .state('dp-edit', {
        url: '/dps/:dpId/edit',
        templateUrl: 'modules/administrative/dp/addedit.html',
        resolve: {
          distributionProtocol: function($stateParams , DistributionProtocol) {
            return DistributionProtocol.getById($stateParams.dpId);
          }
        },
        controller: 'DpAddEditCtrl',
        parent: 'signed-in'
      })
      .state('dp-detail', {
        url: '/dps/:dpId',
        templateUrl: 'modules/administrative/dp/detail.html',
        resolve: {
          distributionProtocol: function($stateParams , DistributionProtocol) {
            return DistributionProtocol.getById($stateParams.dpId);
          }
        },
        controller: 'DpDetailCtrl',
        parent: 'signed-in'
      })
      .state('dp-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/dp/overview.html',
        parent: 'dp-detail'
      });
  });
