angular.module('os.administrative.shippingorder', 
  [ 
    'ui.router',
    'os.administrative.shippingorder.list',
    'os.administrative.shippingorder.addedit',
    'os.administrative.shippingorder.detail'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('shipping-order-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Shipping order Authorization Options
          $scope.shippingOrderResource = {
            createOpts: {resource: 'Order', operations: ['Create']},
            updateOpts: {resource: 'Order', operations: ['Update']},
            deleteOpts: {resource: 'Order', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })
      .state('shipping-order-list', {
        url: '/shipping-orders',
        templateUrl: 'modules/administrative/shipping-order/list.html',     
        controller: 'ShippingOrderListCtrl',
        parent: 'shipping-order-root'
      })
      .state('shipping-order-addedit', {
        url: '/shipping-order-addedit/:orderId',
        templateUrl: 'modules/administrative/shipping-order/addedit.html',
        resolve: {
          order: function($stateParams , ShippingOrder) {
            if ($stateParams.orderId) {
              return ShippingOrder.getById($stateParams.orderId);
            }
            return new ShippingOrder({status: 'PENDING', orderItems: []});
          }
        },
        controller: 'ShippingOrderAddEditCtrl',
        parent: 'shipping-order-root'
      })
      .state('shipping-order-detail', {
        url: '/shipping-orders/:orderId',
        templateUrl: 'modules/administrative/shipping-order/detail.html',
        resolve: {
          order: function($stateParams , ShippingOrder) {
            return ShippingOrder.getById($stateParams.orderId);
          }
        },
        controller: 'ShippingOrderDetailCtrl',
        parent: 'shipping-order-root'
      })
      .state('shipping-order-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/shipping-order/overview.html',
        parent: 'shipping-order-detail'
      });
  });
