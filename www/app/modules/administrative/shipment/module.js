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
            createOpts: {resource: 'ShippingAndTracking', operations: ['Create']},
            updateOpts: {resource: 'ShippingAndTracking', operations: ['Update']},
            deleteOpts: {resource: 'ShippingAndTracking', operations: ['Delete']},
            createUpdateOpts: {resource: 'ShippingAndTracking', operations: ['Create', 'Update']}
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
        url: '/shipment-addedit/:shipmentId?requestId',
        templateUrl: 'modules/administrative/shipment/addedit.html',
        resolve: {
          shipment: function($stateParams , Shipment) {
            if ($stateParams.shipmentId) {
              return Shipment.getById($stateParams.shipmentId);
            }

            return new Shipment({status: 'Pending', shipmentItems: []});
          },

          spmnRequest: function($stateParams, $injector, shipment) {
            var SpecimenRequest       = undefined;
            var SpecimenRequestHolder = undefined;
            if ($injector.has('spmnReqSpecimenRequest')) {
              SpecimenRequest = $injector.get('spmnReqSpecimenRequest');
              SpecimenRequestHolder = $injector.get('spmnReqHolder');
            }

            if (angular.isDefined(shipment.id)) {
              return !!shipment.request ? SpecimenRequest.getById(shipment.request.id) : undefined;
            }

            if (!angular.isDefined($stateParams.requestId)) {
              return undefined;
            }

            var request = SpecimenRequestHolder.get();
            SpecimenRequestHolder.clear();
            if (!request) {
              request = SpecimenRequest.getById($stateParams.requestId);
            }

            return request;
          },

          cp: function(spmnRequest, CollectionProtocol) {
            if (!spmnRequest) {
              return undefined;
            }

            return CollectionProtocol.getById(spmnRequest.cp.id);
          },

          isEditAllowed: function(shipment, Util) {
            var editAllowed = !shipment.status || shipment.status == 'Pending';
            return Util.booleanPromise(editAllowed);
          }
        },
        controller: 'ShipmentAddEditCtrl',
        parent: 'shipment-root'
      })
      .state('shipment-import', {
        url: '/shipment-import',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'shipment-list', title: 'shipments.list'}],
              objectType: 'shipment',
              csvType: 'MULTIPLE_ROWS_PER_OBJ',
              title: 'shipments.bulk_import',
              onSuccess: {state: 'shipment-list'}
            };
          }
        },
        parent: 'signed-in'
      })
      .state('shipment-import-jobs', {
        url: '/shipment-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'shipment-list', title: 'shipments.list'}],
              title: 'shipments.bulk_import_jobs',
              objectTypes: ['shipment']
            }
          }
        },
        parent: 'signed-in'
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
          },
          
          isReceiveAllowed: function(shipment, Util) {
            return Util.booleanPromise(shipment.status == 'Shipped');
          }
        },
        controller: 'ShipmentReceiveCtrl',
        parent: 'shipment-root'
      })
  });
