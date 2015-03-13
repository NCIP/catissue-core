angular.module('os.biospecimen.models.cpe', ['os.common.models'])
  .factory('CollectionProtocolEvent', function($http, osModel, PvManager) {
    var defaultData;
    var Cpe = osModel(
      'collection-protocol-events', 
      function(cpe) {
        angular.extend(cpe, getDefaultData());
      }
    );

    var getDefaultData = function() {
      if (defaultData) {
        return defaultData;
      }

      var notSpecified = PvManager.notSpecified();
      defaultData = {
        clinicalDiagnosis: notSpecified,
        clinicalStatus: notSpecified
      }
 
      return defaultData;
    }
 
    Cpe.listFor = function(cpId) {
      return Cpe.query({cpId: cpId});
    };

    Cpe.prototype.copy = function(copyFrom) {
      return $http.post(Cpe.url() + copyFrom  + '/copy', this.$saveProps()).then(Cpe.modelRespTransform);
    };

    return Cpe;
  });
