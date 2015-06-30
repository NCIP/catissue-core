
angular.module('os.administrative.user.list', ['os.administrative.models'])
  .controller('UserListCtrl', function(
    $scope, $state, $rootScope, 
    osRightDrawerSvc, Institute, User, PvManager, Util) {

    var pvInit = false;

    function init() {
      loadUsers({includeStats: true});
      initPvsAndFilterOpts();
    }
  
    function initPvsAndFilterOpts() {
      $scope.userFilterOpts = {includeStats: true};
      $scope.$on('osRightDrawerOpen', function() {
        if (pvInit) {
          return;
        }

        loadActivityStatuses();
        loadInstitutes().then(
          function(institutes) {
            if (institutes.length == 1) {
              $scope.userFilterOpts.institute = institutes[0].name;
            }

            Util.filter($scope, 'userFilterOpts', loadUsers);
          }
        );

        pvInit = true;
      });
    }
   
    function loadActivityStatuses() {
      PvManager.loadPvs('activity-status').then(
        function(result) {
          $scope.activityStatuses = [].concat(result);
          var idx = $scope.activityStatuses.indexOf('Disabled');
          if (idx != -1) {
            $scope.activityStatuses.splice(idx, 1);
          }
        }
      );
    }

    function loadInstitutes() {
      var q = undefined;
      if ($rootScope.currentUser.admin) {
        q = Institute.query();
      } else {
        q = $rootScope.currentUser.getInstitute();
      }

      return q.then(
        function(result) {
          if (result instanceof Array) {
            $scope.institutes = result;
          } else {
            $scope.institutes = [result];
          }
 
          return $scope.institutes;
        }
      );
    }

    function loadUsers(filterOpts) {
      User.query(filterOpts).then(function(result) {
        if (!$scope.users && result.length > 12) {
          //
          // Show search options when # of users are more than 12
          //
          osRightDrawerSvc.open();
        }

        $scope.users = result; 
      });
    };
    
    $scope.showUserOverview = function(user) {
      $state.go('user-detail.overview', {userId:user.id});
    };

    init();
  });
