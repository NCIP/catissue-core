angular.module('os.biospecimen.models.cpr', ['os.common.models', 'os.biospecimen.models.participant'])
  .factory('CollectionProtocolRegistration', function(osModel, Participant, $http) {
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

    return CollectionProtocolRegistration;
  });
