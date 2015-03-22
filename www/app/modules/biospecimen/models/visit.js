angular.module('os.biospecimen.models.visit', ['os.common.models', 'os.biospecimen.models.form'])
  .factory('Visit', function(osModel, $http, ApiUtil, CollectionProtocolEvent, Form) {
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

    Visit.getAnticipatedVisit = function(eventId, regDate) {
      return CollectionProtocolEvent.getById(eventId).then(
        function(event) {
          event.eventId = event.id;
          event.site = event.defaultSite;
          event.cpTitle = event.collectionProtocol;
         
          delete event.id;
          delete event.defaultSite;
          delete event.collectionProtocol;
          delete event.specimenRequirements;
          
          if (typeof regDate == 'string') {
            regDate = Date.parse(regDate);
          } else if (regDate instanceof Date) {
            regDate = regDate.getTime();
          }

          event.anticipatedVisitDate = event.eventPoint * 24 * 60 * 60 * 1000 + regDate;
          return new Visit(event);
        }
      );
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

    Visit.collectVisitAndSpecimens = function(visitAndSpecimens) {
      return $http.post(Visit.url() + 'collect', visitAndSpecimens).then(ApiUtil.processResp);
    };

    Visit.prototype.getForms = function() {
      return Form.listFor(Visit.url(), this.$id());
    };
    
    Visit.prototype.getRecords = function() {
      var url = Visit.url() + this.$id() + '/extension-records';
      return Form.listRecords(url);
    };

    return Visit;
  });
