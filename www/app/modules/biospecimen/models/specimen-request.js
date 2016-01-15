angular.module('os.biospecimen.models.specimenreq', ['os.common.models'])
  .factory('SpecimenRequest', function(osModel, $http, CollectionProtocol, User, Specimen) {
    var SpecimenRequest =
      osModel(
        'specimen-requests',
        function(spmnReq) {
          if (spmnReq.cp) {
            spmnReq.cp = new CollectionProtocol(spmnReq.cp);
          }

          if (spmnReq.requestor) {
            spmnReq.requestor = new User(spmnReq.requestor);
          }

          if (spmnReq.processedBy) {
            spmnReq.processedBy = new User(spmnReq.processedBy);
          }

          if (spmnReq.specimens) {
            spmnReq.specimens = spmnReq.specimens.map(function(spmn) { return new Specimen(spmn); });
          }
        }
      );

    SpecimenRequest.getRequestFormIds = function(listId) {
      return $http.get(SpecimenRequest.url() + '/form-ids', {params: {listId: listId}}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    SpecimenRequest.listAll = function(includeStats) {
      return SpecimenRequest.query({includeStats: includeStats});
    }

    SpecimenRequest.listForCp = function(cpId, includeStats) {
      return SpecimenRequest.query({cpId: cpId, includeStats: includeStats});
    }

    SpecimenRequest.haveRequests = function(cpId) {
      return $http.get(SpecimenRequest.url() + '/have-requests', {cpId: cpId}).then(
        function(resp) {
          return resp.data.haveRequests;
        }
      );
    }

    SpecimenRequest.prototype.getDisplayName = function() {
      return '#' + this.$id();
    }

    SpecimenRequest.prototype.getFormData = function() {
      return $http.get(SpecimenRequest.url() + '/' + this.$id() + '/form-data').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return SpecimenRequest;
  }
);
