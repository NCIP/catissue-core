angular.module('os.biospecimen.models.cpr', 
  [
    'os.common.models', 
    'os.biospecimen.models.participant',
    'os.biospecimen.models.form'
  ])
  .factory('CollectionProtocolRegistration', function(osModel, Participant, Form, Util) {
    var CollectionProtocolRegistration = 
      osModel(
        'collection-protocol-registrations',
        function(cpr) {
          cpr.participant = new Participant(cpr.participant);
          cpr.registrationDate = new Date(cpr.registrationDate).toISOString();
        }
      );
 
    CollectionProtocolRegistration.listForCp = function(cpId, includeStats) {
      return CollectionProtocolRegistration.query({cpId: cpId, includeStats: !!includeStats});
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

    return CollectionProtocolRegistration;
  });
