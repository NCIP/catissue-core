
angular.module('os.biospecimen.specimensrequest', 
  [
    'ui.router',
    'os.biospecimen.specimensrequest.list',
    'os.biospecimen.specimensrequest.specimens'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('specimens-request-list', {
        url: '/specimen-requests',
        templateUrl: 'modules/biospecimen/specimens-request/list.html',
        controller: 'SpecimensRequestListCtrl',
        resolve: {
          specimensRequestList: function(cp, SpecimenRequest) {
            return SpecimenRequest.listForCp(cp.id, true); 
          }
        },
        parent: 'cp-view'
      })
      .state('specimens-request-detail', {
        url: '/specimen-requests/:requestId',
        templateUrl: 'modules/biospecimen/specimens-request/detail.html',
        controller: function($scope, spmnRequest) {
          $scope.spmnRequest = spmnRequest;
        },
        resolve: {
          spmnRequest: function($stateParams, SpecimenRequest) {
            return SpecimenRequest.getById($stateParams.requestId);
          }
        },
        parent: 'cp-view'
      })
      .state('specimens-request-detail.overview', {
        url: '/specimen-requests/:requestId/overview',
        templateUrl: 'modules/biospecimen/specimens-request/overview.html',
        controller: function() { },
        resolve: {
          reqFormData: function(spmnRequest) {
            if (!spmnRequest.formData) {
              return spmnRequest.getFormData().then(
                function(formData) {
                  spmnRequest.formData = formData;
                  return formData;
                }
              );
            } else {
              return spmnRequest.formData;
            }
          }
        },
        parent: 'specimens-request-detail'
      })
      .state('specimens-request-detail.specimens', {
        url: '/specimen-requests/:requestId/specimens',
        templateUrl: 'modules/biospecimen/specimens-request/specimens.html',
        controller: 'SpmnReqSpecimensCtrl',
        parent: 'specimens-request-detail'
      })
  });
