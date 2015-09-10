angular.module('os.administrative.dp.requirement', 
  [ 
    'ui.router',
    'os.administrative.dp.requirement.list',
    'os.administrative.dp.requirement.addedit',
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('req-list', {
        url: '/requirement',
        templateUrl: 'modules/administrative/dp/requirement/list.html',     
        controller: 'RequirementListCtrl',
        parent: 'dp-detail'
      })
      .state('req-addedit', {
        url: '/requirement/:reqId',
        templateUrl: 'modules/administrative/dp/requirement/addedit.html',
        controller: 'DpRequirementsAddEditCtrl',
        parent: 'dp-detail',
        resolve: {
          requirement: function($stateParams, OrderRequirement) {
            if($stateParams.reqId) {
              return OrderRequirement.getById($stateParams.reqId);
            }
            return new OrderRequirement();
          }
        }
      });
  });
