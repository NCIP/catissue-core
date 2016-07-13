
angular.module('os.administrative.user.list', ['os.administrative.models'])
  .controller('UserListCtrl', function(
    $scope, $state, $rootScope, $modal,
    osRightDrawerSvc, Institute, User, PvManager, Util, Alerts, ListPagerOpts) {

    var pagerOpts;    
    var pvInit = false;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getUsersCount});
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
        pagerOpts.refreshOpts(result);
      });
    };

    function getUsersCount() {
      return User.getCount($scope.userFilterOpts)
    }
    
    $scope.showUserOverview = function(user) {
      $state.go('user-detail.overview', {userId:user.id});
    };

    $scope.broadcastAnnouncement = function() {
      $modal.open({
        templateUrl: 'modules/administrative/user/announcement.html',
        controller: 'AnnouncementCtrl'
      }).result.then(
        function(announcement) {
          User.broadcastAnnouncement(announcement).then(
            function(resp) {
              Alerts.success('user.announcement.success');
            }
          );
        }
      );
    }

    init();
  });
