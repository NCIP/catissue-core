
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, $sce, distributionProtocol, DeleteUtil, $modal) {
    $scope.distributionProtocol = distributionProtocol;
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.exportUrl);
    
    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {onDeleteState: 'dp-list'});
    }
    
    $scope.getDistSiteText = function (site) {
      return site.name;
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
    
  });
