
angular.module('os.administrative.user', 
  [
    'os.administrative.user.dropdown',
    'os.administrative.user.list',
    'os.administrative.user.addedit',
    'os.administrative.user.detail',
    'os.administrative.user.roles',
    'os.administrative.user.password',
    'os.administrative.user.displayname',
    'os.common.import'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('user-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // User Authorization Options
          $scope.userResource = {
            createOpts: {resource: 'User', operations: ['Create']},
            updateOpts: {resource: 'User', operations: ['Update']},
            deleteOpts: {resource: 'User', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })
      .state('user-list', {
        url: '/users',
        templateUrl: 'modules/administrative/user/list.html',
        controller: 'UserListCtrl',
        parent: 'user-root'
      })
      .state('user-addedit', {
        url: '/user-addedit/:userId',
        templateUrl: 'modules/administrative/user/addedit.html',
        resolve: {
          user: function($stateParams, User) {
            if ($stateParams.userId) { 
              return User.getById($stateParams.userId);
            }
            return new User();
          }
        },
        controller: 'UserAddEditCtrl',
        parent: 'user-root'
      })
      .state('user-import', {
        url: '/users-import?objectType',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          importDetail: function($stateParams) {
            var objectType = $stateParams.objectType;
            var title = undefined;
            if (objectType == 'user') {
              title = 'user.bulk_import_users';
            } else if (objectType == 'userRoles') {
              title = 'user.bulk_import_user_roles';
            }

            return {
              breadcrumbs: [{state: 'user-list', title: 'user.list'}],
              objectType: objectType,
              title: title,
              onSuccess: {state: 'user-list'}
            };
          }
        },
        parent: 'user-root'
      })
      .state('user-import-jobs', {
        url: '/users-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'user-list', title: 'user.list'}],
              title: 'user.bulk_import_jobs',
              objectTypes: ['user', 'userRoles']
            }
          }
        },
        parent: 'user-root'
      })
      .state('user-detail', {
        url: '/users/:userId',
        templateUrl: 'modules/administrative/user/detail.html',
        resolve: {
          user: function($stateParams, User) {
            return User.getById($stateParams.userId);
          }
        },
        controller: 'UserDetailCtrl',
        parent: 'user-root'
      })
      .state('user-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/user/overview.html',
        parent: 'user-detail'
      })
      .state('user-detail.roles', {
        url: '/roles',
        templateUrl: 'modules/administrative/user/roles.html',
        resolve: {
          userRoles: function(user) {
            return user.getRoles();
          },
          
          currentUserInstitute: function(currentUser) {
            return currentUser.getInstitute();
          }
        },
        controller: 'UserRolesCtrl',
        parent: 'user-detail'
      })
      .state('user-password', {
        url: '/user-password-change/:userId',
        templateUrl: 'modules/administrative/user/password.html',
        resolve: {
          user: function($stateParams, User) {
            return User.getById($stateParams.userId);
          }
        },
        controller: 'UserPasswordCtrl',
        parent: 'user-root'
      })
  });

