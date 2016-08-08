
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, $modal, $translate, currentUser, distributionProtocol, DeleteUtil) {
    $scope.distributionProtocol = distributionProtocol;
    $scope.distributingSites = '';
    
    function init() {
      $scope.isEditAllowed = isEditAllowed()
    }
    
    function isEditAllowed() {
      var sites = distributionProtocol.distributingSites; // {institute: [sites]}
      return currentUser.admin ||
        (currentUser.instituteAdmin && Object.keys(sites).length == 1);
    }


    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {onDeleteState: 'dp-list'});
    }
    
    function getDistSiteText(distSites) {
      var str = '';
      var allSites = [];
      angular.forEach(distSites,
        function(sites, inst) {
          str = '('+ inst +': ';
          if (sites.length > 0) {
            str += sites.join(', ');
          } else {
            str += $translate.instant('dp.all_sites');
          }
          
          str += ')';
          allSites.push(str);
        }
      );
      
      $scope.distributingSites = allSites.join(', ');
    }
    
    $scope.closeDp = function () {
      DeleteUtil.confirmDelete({
        entity: distributionProtocol,
        templateUrl: 'modules/administrative/dp/close.html',
        delete: function () {
          distributionProtocol.close().then(function(dp) {
            $scope.distributionProtocol = dp;
          })
        }
      });
    }
    
    $scope.reopenDp = function () {
      distributionProtocol.reopen().then(function (dp) {
        $scope.distributionProtocol = dp;
      });
    }
    
    getDistSiteText($scope.distributionProtocol.distributingSites);
    
    init();
    
  });
