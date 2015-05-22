angular.module('os.administrative.order', 
  [ 
    'ui.router',
    'os.administrative.order.list',
    'os.administrative.order.detail',
    'os.administrative.order.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('order-list', {
        url: '/orders',
        templateUrl: 'modules/administrative/order/list.html',     
        controller: 'OrderListCtrl',
        parent: 'signed-in'
      })
      .state('order-addedit', {
        url: '/order-addedit/:orderId',
        templateUrl: 'modules/administrative/order/addedit.html',
        resolve: {
          order: function($stateParams , DistributionOrder) {
            if ($stateParams.orderId) {
              return DistributionOrder.getById($stateParams.orderId);
            }
            return new DistributionOrder({status: 'PENDING', orderItems: []});
          }
        },
        controller: 'OrderAddEditCtrl',
        parent: 'signed-in'
      })
      .state('order-detail', {
        url: '/orders/:orderId',
        templateUrl: 'modules/administrative/order/detail.html',
        resolve: {
          order: function($stateParams , DistributionOrder) {
            return DistributionOrder.getById($stateParams.orderId);
          }
        },
        controller: 'OrderDetailCtrl',
        parent: 'signed-in'
      })
      .state('order-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/order/overview.html',
        parent: 'order-detail'
      });
  });
