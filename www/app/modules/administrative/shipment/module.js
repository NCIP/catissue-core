angular.module('os.administrative.shipment', 
  [ 
    'ui.router',
    'os.administrative.shipment.list',
    'os.administrative.shipment.addedit',
    'os.administrative.shipment.detail',
    'os.administrative.shipment.receive'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('shipment-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Shipment Authorization Options
          $scope.shipmentResource = {
            createOpts: {resource: 'Order', operations: ['Create']},
            updateOpts: {resource: 'Order', operations: ['Update']},
            deleteOpts: {resource: 'Order', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })
      .state('shipment-list', {
        url: '/shipments',
        templateUrl: 'modules/administrative/shipment/list.html',     
        controller: 'ShipmentListCtrl',
        parent: 'shipment-root'
      })
      .state('shipment-addedit', {
        url: '/shipment-addedit/:shipmentId',
        templateUrl: 'modules/administrative/shipment/addedit.html',
        resolve: {
          shipment: function($stateParams , Shipment) {
            if ($stateParams.shipmentId) {
              return Shipment.getById($stateParams.shipmentId);
            }
            return new Shipment({status: 'PENDING', shipmentItems: []});
          }
        },
        controller: 'ShipmentAddEditCtrl',
        parent: 'shipment-root'
      })
      .state('shipment-detail', {
        url: '/shipments/:shipmentId',
        templateUrl: 'modules/administrative/shipment/detail.html',
        resolve: {
          shipment: function($stateParams , Shipment) {
            return Shipment.getById($stateParams.shipmentId);
          }
        },
        controller: 'ShipmentDetailCtrl',
        parent: 'shipment-root'
      })
      .state('shipment-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/shipment/overview.html',
        parent: 'shipment-detail'
      })
      .state('shipment-receive', {
        url: '/shipments/:shipmentId/receive',
        templateUrl: 'modules/administrative/shipment/addedit.html',
        resolve: {
          shipment: function($stateParams , Shipment) {
            return Shipment.getById($stateParams.shipmentId);
          }
        },
        controller: 'ShipmentReceiveCtrl',
        parent: 'shipment-root'
      })
  });
