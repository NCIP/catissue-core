angular.module('os.administrative.order', 
  [ 
    'ui.router',
    'os.administrative.order.list',
    'os.administrative.order.detail',
    'os.administrative.order.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('order-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Storage Container Authorization Options
          $scope.orderResource = {
            createOpts: {resource: 'Order', operations: ['Create']},
            updateOpts: {resource: 'Order', operations: ['Update']},
            deleteOpts: {resource: 'Order', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })
      .state('order-list', {
        url: '/orders',
        templateUrl: 'modules/administrative/order/list.html',     
        controller: 'OrderListCtrl',
        parent: 'order-root'
      })
      .state('order-addedit', {
        url: '/order-addedit/:orderId?requestorId',
        templateUrl: 'modules/administrative/order/addedit.html',
        resolve: {
          order: function($stateParams , DistributionOrder, User) {
            if ($stateParams.orderId) {
              return DistributionOrder.getById($stateParams.orderId);
            }

            var order = new DistributionOrder({status: 'PENDING', orderItems: []});
            if (!angular.isDefined($stateParams.requestorId)) {
              return order;
            }

            return User.getById($stateParams.requestorId).then(
              function(user) {
                order.requester = user;
                order.instituteName = user.instituteName;
                return order;
              }
            );
          }
        },
        controller: 'OrderAddEditCtrl',
        parent: 'order-root'
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
        parent: 'order-root'
      })
      .state('order-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/order/overview.html',
        parent: 'order-detail'
      });
  });
