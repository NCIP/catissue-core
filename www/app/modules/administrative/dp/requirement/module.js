angular.module('os.administrative.dp.requirement',
  [
    'ui.router',
    'os.administrative.dp.requirement.list',
    'os.administrative.dp.requirement.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('req-root', {
        url: '/requirement',
        template: '<div ui-view></div>',
        abstract: true,
        parent: 'dp-detail'
      })
      .state('req-list', {
        url: '/list',
        templateUrl: 'modules/administrative/dp/requirement/list.html',
        controller: 'DprListCtrl',
        parent: 'req-root'
      })
      .state('req-addedit', {
        url: '/:reqId',
        templateUrl: 'modules/administrative/dp/requirement/addedit.html',
        controller: 'DprAddEditCtrl',
        parent: 'req-root',
        resolve: {
          dpr: function($stateParams, DistributionProtocolRequirement) {
            if ($stateParams.reqId) {
              return DistributionProtocolRequirement.getById($stateParams.reqId);
            }
            return new DistributionProtocolRequirement();
          }
        }
      });
  });

