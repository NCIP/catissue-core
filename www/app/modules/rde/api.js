angular.module('os.rde')
  .factory('RdeApis', function($http, ApiUrls) {

    var baseUrl = ApiUrls.getBaseUrl();

    function getBarcodeDetails(barcodes) {
      return $http.post(baseUrl + 'visit-barcodes/validate', barcodes).then(
        function(result) {
          var headers = [], visits = {};
          angular.forEach(result.data, function(bcDetail) {
            var detail = {};
            angular.forEach(bcDetail.parts, function(part) {
              var found = false;
              for (var i = 0; i < headers.length; ++i) {
                if (headers[i].token == part.token) {
                  found = true;
                  break;
                }
              }

              if (!found) {
                headers.push({token: part.token, caption: part.caption});
              }

              detail[part.token] = part;
            });

            visits[bcDetail.barcode] = {detail: detail, error: bcDetail.erroneous};
          });

          return {headers: headers, visits: visits};
        }
      );
    }

    function saveVisitBarcodes(visits) {
      return $http.post(baseUrl + 'visit-barcodes', visits).then(
        function(result) {
          return result.data.map(
            function(visitSpmns) {
              var visit = visitSpmns.visit;
              visit.specimens = visitSpmns.specimens;
              return visit;
            }
          );
        }   
      );  
    }

    function registerParticipants(participants) {
      return $http.post(baseUrl + 'visit-barcodes/participants', participants).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    function saveVisits(visits) {
      return $http.post(baseUrl + 'visit-barcodes/visits', visits).then(
        function(result) {
          return result.data.map(
            function(visitSpmns) {
              var visit = visitSpmns.visit;
              visit.specimens = visitSpmns.specimens;
              return visit;
            }
          );
        }   
      );
    }

    function getSpecimenVisits(specimenLabels) {
      return $http.get(baseUrl + 'visit-barcodes/visits', {params: {aliquotLabels: specimenLabels}}).then(
        function(result) {
          return result.data.map(
            function(visitSpmns) {
              var visit = visitSpmns.visit;
              visit.specimens = visitSpmns.specimens;
              return visit;
            }
          );
        }
      );
    }
 
    function savePrimarySpecimens(spmns) {
      return $http.post(baseUrl + 'visit-barcodes/primary-specimens', spmns).then(
        function(result) {
          return result.data;
        }
      );
    }

    function printSpecimenLabels(spmns) {
      return $http.post(baseUrl + 'visit-barcodes/specimen-labels-print-jobs', spmns).then(
        function(result) {
          return result.data;
        }
      );    
    }

    function validateSpecimenPositions(occupancyDetail) {
      return $http.post(baseUrl + 'visit-barcodes/validate-specimens-occupancy', occupancyDetail).then(
        function(result) {
          return result.data.positions;
        }
      );
    }

    function saveChildSpecimens(childSpmns) {
      return $http.post(baseUrl + 'visit-barcodes/child-specimens', childSpmns).then(
        function(result) {
          return result.data;
        }
      );
    }

    return {
      getBarcodeDetails: getBarcodeDetails,

      saveVisitBarcodes: saveVisitBarcodes,

      registerParticipants: registerParticipants,

      saveVisits: saveVisits,

      getSpecimenVisits: getSpecimenVisits,

      savePrimarySpecimens: savePrimarySpecimens,

      printSpecimenLabels: printSpecimenLabels,
 
      validateSpecimenPositions: validateSpecimenPositions,

      saveChildSpecimens: saveChildSpecimens
    };
  });
