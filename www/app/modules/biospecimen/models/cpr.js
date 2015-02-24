angular.module('os.biospecimen.models.cpr', 
  [
    'os.common.models', 
    'os.biospecimen.models.participant',
    'os.biospecimen.models.form'
  ])
  .factory('CollectionProtocolRegistration', function(osModel, Participant, Form) {
    var CollectionProtocolRegistration = 
      osModel(
        'collection-protocol-registrations',
        function(cpr) {
          cpr.participant = new Participant(cpr.participant);
        }
      );
 
    CollectionProtocolRegistration.listForCp = function(cpId, includeStats) {
      return CollectionProtocolRegistration.query({cpId: cpId, includeStats: !!includeStats});
    };

    CollectionProtocolRegistration.prototype.$saveProps = function() {
      this.participant = this.participant.$saveProps();
      return this;
    };

    CollectionProtocolRegistration.prototype.getForms = function() {
      return Form.listFor(CollectionProtocolRegistration.url(), this.$id());
    };

    CollectionProtocolRegistration.prototype.getRecords = function(formCtxId) {
      return Form.listRecords(CollectionProtocolRegistration.url(), this.$id(), formCtxId);
    };

    return CollectionProtocolRegistration;
  });
