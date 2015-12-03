angular.module('os.biospecimen.models.specimenreq', ['os.common.models'])
  .factory('SpecimenRequest', function(osModel, $http) {
    var SpecimenRequest = osModel('specimen-requests');

    SpecimenRequest.getRequestFormIds = function(listId) {
      return $http.get(SpecimenRequest.url() + '/form-ids', {params: {listId: listId}}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return SpecimenRequest;
  }
);
