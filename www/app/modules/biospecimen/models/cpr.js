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

    CollectionProtocolRegistration.prototype.getForms = function() {
      var result = [];
      $http.get(CollectionProtocolRegistration.url() + this.$id() + '/forms').then(
        function(resp) {
          angular.forEach(resp.data, function(form) {
            result.push(form);
          });
        }
      );

      return result;
    };

    CollectionProtocolRegistration.prototype.getRecords = function(formCtxId) {
      var result = {records: []};
      $http.get(CollectionProtocolRegistration.url() + this.$id() + '/forms/' + formCtxId + '/records')
        .then(function(resp) {
          angular.extend(result, resp.data);
        }
      );

      return result;
    };

    return CollectionProtocolRegistration;
  });
