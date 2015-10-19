angular.module('os.administrative.dp.requirement',
  [
    'ui.router',
    'os.administrative.dp.requirement.list',
    'os.administrative.dp.requirement.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('req-list', {
        url: '/requirement',
        templateUrl: 'modules/administrative/dp/requirement/list.html',
        controller: 'DprListCtrl',
        parent: 'dp-detail'
      })
      .state('req-addedit', {
        url: '/requirement/:reqId',
        templateUrl: 'modules/administrative/dp/requirement/addedit.html',
        controller: 'DprAddEditCtrl',
        parent: 'dp-detail',
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

