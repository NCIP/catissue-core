angular.module('os.biospecimen.models.cpe', ['os.common.models'])
  .factory('CollectionProtocolEvent', function($http, osModel) {
    var Cpe = osModel('collection-protocol-events');
 
    Cpe.listFor = function(cpId) {
      return Cpe.query({cpId: cpId});
    };

    Cpe.prototype.copy = function(copyFrom) {
      return $http.post(Cpe.url() + copyFrom  + '/copy', this.$saveProps()).then(Cpe.modelRespTransform);
    };

    return Cpe;
  });
