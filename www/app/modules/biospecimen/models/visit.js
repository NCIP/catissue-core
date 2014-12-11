angular.module('os.biospecimen.models.visit', ['os.common.models'])
  .factory('Visit', function(osModel, $http) {
    var Visit = osModel('visits');
 
    function enrich(visits) {
      angular.forEach(visits, function(visit) {
        visit.pendingSpecimens = visit.anticipatedSpecimens - (visit.collectedSpecimens + visit.uncollectedSpecimens);
        visit.totalSpecimens = visit.anticipatedSpecimens + visit.unplannedSpecimens;
      });

      return visits;
    };

    Visit.listFor = function(cprId, includeStats) {
      return Visit.query({cprId: cprId, includeStats: !!includeStats}).then(enrich);
    };

    function visitFilter(visits, filterfn) {
      var results = [];
      angular.forEach(visits, function(visit) {
        if (filterfn(visit)) {
          results.push(visit);
        }
      });

      return results;
    };

    Visit.completedVisits = function(visits) {
      return visitFilter(visits, function(visit) { return visit.status == 'Complete'; });
    };

    Visit.anticipatedVisits = function(visits) {
      return visitFilter(visits, function(visit) { return !visit.status || visit.status == 'Pending'; });
    };

    return Visit;
  });
