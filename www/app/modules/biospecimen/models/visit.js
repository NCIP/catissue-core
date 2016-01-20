angular.module('os.biospecimen.models.visit', ['os.common.models', 'os.biospecimen.models.form'])
  .factory('Visit', function(osModel, $http, ApiUtil, CollectionProtocolEvent, Form) {
    var Visit = osModel('visits');
 
    function enrich(visits) {
      angular.forEach(visits, function(visit) {
        var actualSpecimenCnt = visit.collectedSpecimens + visit.uncollectedSpecimens;
        if (actualSpecimenCnt <= visit.anticipatedSpecimens) {
          visit.pendingSpecimens = visit.anticipatedSpecimens - actualSpecimenCnt;
          visit.totalSpecimens = visit.anticipatedSpecimens + visit.unplannedSpecimens;
        } else {
          // usually occurs when specimens got collected and requirement was deleted later
          visit.pendingSpecimens = 0;
          visit.totalSpecimens = actualSpecimenCnt + visit.unplannedSpecimens;
        }
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

          if (event.eventPoint != null) {
            event.anticipatedVisitDate = (event.eventPoint - event.offset) * 24 * 60 * 60 * 1000 + regDate;
          }
          delete event.offset;

          return new Visit(event);
        }
      );
    };

    Visit.getByNameSpr = function(opts) {
      var url = Visit.url() + 'bynamespr/';
      return $http.get(url, {params:opts}).then(function(result) {return result.data});
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

    Visit.missedVisits = function(visits) {
      return visitFilter(visits, function(visit) { return visit.status == 'Missed Collection'; });
    };

    Visit.collectVisitAndSpecimens = function(visitAndSpecimens) {
      return $http.post(Visit.url() + 'collect', visitAndSpecimens).then(ApiUtil.processResp);
    };

    Visit.prototype.getType = function() {
      return 'visit';
    }

    Visit.prototype.getDisplayName = function() {
      return this.name;
    };

    Visit.prototype.getForms = function() {
      return Form.listFor(Visit.url(), this.$id());
    };
    
    Visit.prototype.getRecords = function() {
      var url = Visit.url() + this.$id() + '/extension-records';
      return Form.listRecords(url);
    };

    Visit.prototype.getSprFileUrl = function(pdf) {
      var param = !!pdf ? '?type=pdf' : '';
      return Visit.url() + this.$id() + '/spr-file' + param;
    };

    Visit.prototype.getSprTextUrl = function() {
      return Visit.url() + this.$id() + '/spr-text';
    };

    Visit.prototype.getSprText = function() {
      return $http.get(this.getSprTextUrl()).then(function(result) { return result.data; });
    };

    Visit.prototype.updateSprText = function(sprText) {
      return $http.put(this.getSprTextUrl(), sprText).then(function(result) { return result.data; });
    };

    Visit.prototype.deleteSprFile = function() {
      return $http.delete(this.getSprFileUrl()).then(function(result) { return result.data; });
    };

    Visit.prototype.updateSprLockStatus = function(lock) {
      var url = Visit.url() + this.$id() + '/spr-lock';
      return $http.put(url, {locked: lock}).then(function(result) { return result.data; });
    };

    return Visit;
  });
