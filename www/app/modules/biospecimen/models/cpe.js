angular.module('os.biospecimen.models.cpe', ['os.common.models'])
  .factory('CollectionProtocolEvent', function($http, osModel, PvManager) {
    var Cpe = osModel(
      'collection-protocol-events', 
      function(cpe) {
        cpe.copyAttrsIfNotPresent(getDefaultProps());
      }
    );

    function getDefaultProps() {
      var notSpecified = PvManager.notSpecified();
      return {
        clinicalDiagnosis: notSpecified,
        clinicalStatus: notSpecified
      }
    }
 
    Cpe.listFor = function(cpId) {
      return Cpe.query({cpId: cpId});
    };

    Cpe.prototype.copy = function(copyFrom) {
      return $http.post(Cpe.url() + copyFrom  + '/copy', this.$saveProps()).then(Cpe.modelRespTransform);
    };

    Cpe.prototype.delete = function() {
      return $http.delete(Cpe.url() + this.$id());
    };

    return Cpe;
  });
