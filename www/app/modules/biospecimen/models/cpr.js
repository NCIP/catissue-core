angular.module('os.biospecimen.models.cpr', 
  [
    'os.common.models', 
    'os.biospecimen.models.participant',
    'os.biospecimen.models.form'
  ])
  .factory('CollectionProtocolRegistration', function($filter, osModel, Participant, Form, Util) {
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
        params.dob = $filter('date')(params.dob, 'yyyy-MM-dd');
      }
      return CollectionProtocolRegistration.query(params);
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
