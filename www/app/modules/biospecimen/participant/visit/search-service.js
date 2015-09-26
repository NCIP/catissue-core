angular.module('os.biospecimen.visit.search', [])
  .factory('VisitSearchSvc', function($state, Visit, Alerts) {
    var matchingVisits =[];
    var searchKey = undefined;

    function search(searchData) {
      Visit.getByNameSpr({visitName: searchData.visitName, sprNumber: searchData.sprNumber}).then(
        function(visits) {
          if (visits == undefined || visits.length == 0) {
            Alerts.error('search.error', {entity: 'Visit', key: searchData.visitName || searchData.sprNumber});
            return;
          } else if (visits.length > 1) {
            matchingVisits = visits;
            searchKey = searchData.visitName || searchData.sprNumber;
            $state.go('visit-search', {}, {reload: true});
          } else {
            var visit = visits[0];
            var view = undefined;
            if (searchData.visitName) {
              view ='visit-detail.overview';
            } else {
              view = 'visit-detail.spr';
            }
            $state.go(view, {cpId: visit.cpId, cprId: visit.cprId, visitId: visit.id, eventId: visit.eventId});
          }
        }
      );
    }

    function getVisits() {
      return matchingVisits;
    }

    function getSearchKey() {
      return searchKey;
    }

    return {
      getVisits: getVisits,

      getSearchKey: getSearchKey,

      search: search
    };
  })

  .controller('VisitResultsView', function($scope, $state, visits, searchKey) {
    function init() {
      $scope.visits = visits;
      $scope.searchKey = searchKey;
    }

    $scope.sprDetail = function(visit) {
      $state.go('visit-detail.spr', {cpId: visit.cpId, cprId: visit.cprId, visitId: visit.id, eventId: visit.eventId});
    }
    init();
  });



