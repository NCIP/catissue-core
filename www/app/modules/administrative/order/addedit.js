
angular.module('os.administrative.order.addedit', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('OrderAddEditCtrl', function(
    $scope, $state, $translate, order, Institute,
    Specimen, SpecimensHolder, Site, DistributionProtocol, DistributionOrder, Alerts, Util) {
    
    function init() {
      $scope.order = order;
      $scope.dpList = [];
      $scope.instituteNames = [];
      $scope.siteList = [];
      $scope.userFilterOpts = {};
      $scope.input = {allItemStatus: false};

      loadDps();
      loadInstitutes();
      setUserAndSiteList(order);

      if (!order.id && angular.isArray(SpecimensHolder.getSpecimens())) {
        order.orderItems = getOrderItems(SpecimensHolder.getSpecimens());
        SpecimensHolder.setSpecimens(null);
      }
      
      if (!order.executionDate) {
        order.executionDate = new Date();
      }
      
      setHeaderStatus();
    }

    function loadDps(name) {
      var filterOpts = {activityStatus: 'Active', query: name};
      DistributionProtocol.query(filterOpts).then(
        function(dps) {
          $scope.dpList = dps;
        }
      );
    }
    
    function loadInstitutes () {
      Institute.query().then(
        function (institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    function loadSites(instituteName) {
      Site.listForInstitute(instituteName, true).then(
        function(sites) {
          $scope.siteList = sites;
        }
      );    
    }
    
    function setUserFilterOpts(institute) {
      $scope.userFilterOpts = {institute: institute};
    }

    function setUserAndSiteList(order) {
      var instituteName = order.instituteName;
      setUserFilterOpts(instituteName);
      loadSites(instituteName);
    }

    function getOrderItems(specimens) {
      return specimens.map(
        function(specimen) {
          return {
            specimen: specimen,
            quantity: specimen.availableQty,
            status: 'DISTRIBUTED_AND_CLOSED'
          };
        }
      );
    }

    function saveOrUpdate(order) {
      order.siteId = undefined;
      angular.copy(order).$saveOrUpdate().then(
        function(savedOrder) {
          $state.go('order-detail.overview', {orderId: savedOrder.id});
        }
      );
    };
    
    function setHeaderStatus() {
      var isOpenPresent = false;
      angular.forEach($scope.order.orderItems,
        function(item) {
          if (item.status == 'DISTRIBUTED') {
            isOpenPresent = true;
          }
        }
      );

      if (isOpenPresent) {
        $scope.input.allItemStatus = false;
      } else {
        $scope.input.allItemStatus = true;
      }
    }

    $scope.onDpSelect = function() {
      $scope.order.instituteName = $scope.order.distributionProtocol.instituteName;
      $scope.order.siteName = $scope.order.distributionProtocol.defReceivingSiteName;
      $scope.order.requester = $scope.order.distributionProtocol.principalInvestigator;
      setUserAndSiteList($scope.order);
    }
    
    $scope.onInstSelect = function () {
      var instName = $scope.order.instituteName;
      loadSites(instName);
      setUserFilterOpts(instName);
      $scope.order.siteName = '';
      $scope.order.requester = '';
    }

    $scope.addSpecimens = function(labels) {
      return Specimen.listForDp(labels, $scope.order.distributionProtocol.id).then(
        function (specimens) {
          Util.appendAll($scope.order.orderItems, getOrderItems(specimens));
          return true;
        }
      );
    }

    $scope.removeOrderItem = function(orderItem) {
      var idx = order.orderItems.indexOf(orderItem);
      if (idx != -1) {
        order.orderItems.splice(idx, 1);
      }
    }

    $scope.distribute = function() {
      $scope.order.status = 'EXECUTED';
      saveOrUpdate($scope.order);
    }

    $scope.saveDraft = function() {
      $scope.order.status = 'PENDING';
      saveOrUpdate($scope.order);
    }

    $scope.passThrough = function() {
      return true;
    }
    
    $scope.setStatus = function(item) {
      if (item.quantity == item.specimen.availableQty) {
        item.status = 'DISTRIBUTED_AND_CLOSED';
      } else {
        item.status = 'DISTRIBUTED';
      }
      
      setHeaderStatus();
    }
    
    $scope.setHeaderStatus = setHeaderStatus;

    $scope.toggleAllItemStatus = function() {
      var allStatus;
      if ($scope.input.allItemStatus) {
        allStatus = 'DISTRIBUTED_AND_CLOSED';
      } else {
        allStatus = 'DISTRIBUTED';
      }
      
      angular.forEach($scope.order.orderItems,
        function(item) {
          if (item.quantity != item.specimen.availableQty) {
            item.status = allStatus;
          }
        }
      );
    }
    
    $scope.searchDp = loadDps;
    
    init();
  });
