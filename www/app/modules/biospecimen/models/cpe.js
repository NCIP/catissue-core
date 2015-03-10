angular.module('os.biospecimen.models.cpe', ['os.common.models'])
  .factory('CollectionProtocolEvent', function($http, osModel) {
    var defaultValue = 'Not Specified';
    var Cpe = osModel('collection-protocol-events', undefined, {
                clinicalDiagnosis: defaultValue,
                clinicalStatus: defaultValue
              });
 
    Cpe.listFor = function(cpId) {
      return Cpe.query({cpId: cpId});
    };

    Cpe.prototype.copy = function(copyFrom) {
      return $http.post(Cpe.url() + copyFrom  + '/copy', this.$saveProps()).then(Cpe.modelRespTransform);
    };

    return Cpe;
  });
