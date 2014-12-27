angular.module('os.biospecimen.models.cpe', ['os.common.models'])
  .factory('CollectionProtocolEvent', function(osModel) {
    var Cpe = osModel('collection-protocol-events');
 
    Cpe.listFor = function(cpId) {
      return Cpe.query({cpId: cpId});
    };

    return Cpe;
  });
