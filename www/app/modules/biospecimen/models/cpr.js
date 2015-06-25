angular.module('os.biospecimen.models.cpr', 
  [
    'os.common.models', 
    'os.biospecimen.models.participant',
    'os.biospecimen.models.form'
  ])
  .factory('CollectionProtocolRegistration', function($filter, $http, osModel, Participant, Form, Util) {
    var CollectionProtocolRegistration = 
      osModel(
        'collection-protocol-registrations',
        function(cpr) {
          cpr.participant = new Participant(cpr.participant);
        }
      );
 
    CollectionProtocolRegistration.listForCp = function(cpId, includeStats, filterOpts) {
      var params = {cpId: cpId, includeStats: !!includeStats};
      angular.extend(params, filterOpts || {});
      if (!!params.dob) {
        //
        // Note: yyyy-MM-dd is server date format and is not locale based
        //
        params.dob = $filter('date')(params.dob, 'yyyy-MM-dd');
      }
      return CollectionProtocolRegistration.query(params);
    };

    CollectionProtocolRegistration.prototype.getType = function() {
      return 'collection_protocol_registration';
    }

    CollectionProtocolRegistration.prototype.getDisplayName = function() {
      var str = this.ppid;
      if (!!this.participant.firstName || !!this.participant.lastName) {
        str += " (" + this.participant.firstName + " " + this.participant.lastName + ")";
      }
      return str;
    };

    CollectionProtocolRegistration.prototype.getMrnSites = function() {
      return !this.participant ? undefined : this.participant.getMrnSites();
    };

    CollectionProtocolRegistration.prototype.$saveProps = function() {
      this.participant = this.participant.$saveProps();
      return this;
    };

    CollectionProtocolRegistration.prototype.getForms = function(params) {
      return Form.listFor(CollectionProtocolRegistration.url(), this.$id(), params);
    };

    CollectionProtocolRegistration.prototype.getRecords = function() {
      var url = CollectionProtocolRegistration.url() + this.$id() + '/extension-records';
      return Form.listRecords(url);
    };

    CollectionProtocolRegistration.prototype.$saveProps = function() {
      this.participant = this.participant.$saveProps();
      return this;
    };

    CollectionProtocolRegistration.prototype.getSignedConsentFormUrl = function() {
      return CollectionProtocolRegistration.url() + this.$id() + "/consent-form";
    }

    CollectionProtocolRegistration.prototype.deleteSignedConsentForm = function() {
      return $http.delete(this.getSignedConsentFormUrl()).then(function(result){ return result.data; });
    }

    CollectionProtocolRegistration.prototype.saveConsentResponse = function() {
      //TODO: save consent response
    }

    return CollectionProtocolRegistration;
  });
