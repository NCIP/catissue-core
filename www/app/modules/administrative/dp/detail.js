
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, $modal, $translate, currentUser, distributionProtocol, DeleteUtil) {
    $scope.distributionProtocol = distributionProtocol;
    
    function init() {
      $scope.isEditAllowed = isEditAllowed()
    }
    
    function isEditAllowed() {
      var sites = distributionProtocol.distributingSites; // {institute: [sites]}
      return currentUser.admin || (currentUser.instituteAdmin && Object.keys(sites).length == 1);
    }


    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {onDeleteState: 'dp-list'});
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
    
    init();
  });
