angular.module('os.biospecimen.models.participant', ['os.common.models'])
  .factory('Participant', function(osModel, $http) {
    var Participant = osModel('participants');
 
    Participant.prototype.newPmi = function() {
      return {siteName: '', mrn: ''};
    };

    Participant.prototype.addPmi = function(pmi) {
      if (!this.pmis) {
        this.pmis = [];
      }

      this.pmis.push(pmi);
    };

    Participant.prototype.removePmi = function(pmi) {
      var idx = this.pmis ? this.pmis.indexOf(pmi) : -1;
      if (idx != -1) {
        this.pmis.splice(idx, 1);
      }

      return idx;
    };

    Participant.prototype.getPmis = function() {
      var pmis = []; 
      angular.forEach(this.pmis, function(pmi) {
        if (pmi.siteName) {
          pmis.push(pmi);
        }
      });

      return pmis;
    };

    Participant.prototype.getMrnSites = function() {
      if (!this.pmis) {
        return [];
      }

      return this.pmis.map(function(pmi) { return pmi.siteName; });
    };

    Participant.prototype.isMatchingInfoPresent = function() {
      return (this.lastName && this.birthDate) ||
             this.empi ||
             this.uid ||
             this.getPmis().length > 0;
    };

    Participant.prototype.getMatchingCriteria = function() {
      return {
        lastName: this.lastName,
        birthDate: this.birthDate,
        empi: this.empi,
        uid : this.uid,
        pmis: this.getPmis()
      };
    };

    Participant.prototype.getMatchingParticipants = function(opts) {
      opts = opts || {};

      var that = this;
      var criteria = this.getMatchingCriteria();
      return $http.post(Participant.url() + '/match', criteria)
        .then(function(result) {
          var response = result.data.filter(function(matched) {
            return !that.id || !!opts.returnThis || matched.participant.id != that.id;
          });
          
          angular.forEach(response, function(matched) {
            matched.participant = new Participant(matched.participant);
          });
          return response;
        });
    };

    Participant.prototype.$saveProps = function() {
      var pmis = this.getPmis();
      this.pmis = pmis.length == 0 ? [] : pmis;
      return this;
    };

    return Participant;
  });
