
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, distributionProtocol, DeleteUtil, $modal, $translate) {
    $scope.distributionProtocol = distributionProtocol;
    
    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {onDeleteState: 'dp-list'});
    }
    
    $scope.getDistSiteText = function(distSites) {
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
      
      return allSites.join(', ');
    };
    
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
    
  });
