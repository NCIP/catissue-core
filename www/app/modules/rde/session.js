
angular.module('os.rde')
  .factory('RdeSession', function(osModel, RdeApis) {
    var RdeSession = new osModel('rde-sessions');

    RdeSession.prototype.getVisitBarcodes = function() {
      if (!this.data || !this.data.visits || this.data.visits.length == 0) {
        return [{visitDate: new Date()}];
      }

      return this.data.visits.map(
        function(v) {
          return {barcode: v.name, visitDate: new Date(v.visitDate)}
        }
      );
    }

    RdeSession.prototype.getParticipants = function() {
      if (!this.data || !this.data.participants || this.data.participants.length == 0) {
        return [{regDate: new Date()}];
      } else {
        angular.forEach(this.data.participants,
          function(p) {
            p.regDate = new Date(p.regDate);
          }
        );

        return this.data.participants;
      }
    }

    RdeSession.prototype.loadVisitsSpmns = function(input) {
      if (input.visitsSpmns && input.visitsSpmns.length > 0) {
        return input.visitsSpmns;
      }

      if (!this.data || !this.data.visits || this.data.visits.length == 0) {
        return [];
      }

      var visitNames = this.data.visits.map(function(v) { return v.name; });
      return RdeApis.getVisits(visitNames).then(
        function(visitsSpmns) {
          return (input.visitsSpmns = angular.copy(visitsSpmns));
        }
      );
    }

    return RdeSession;
  });
