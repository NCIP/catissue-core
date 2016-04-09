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

    function getVisitSpmns(input) {
      var visit = input.visit;
      visit.specimens = input.specimens;
      return visit;
    }

    function saveVisitBarcodes(visits) {
      var payload = visits.map(
        function(v) {
          return {barcode: v.barcode, visitDate: v.visitDate};
        }
      );

      return $http.post(baseUrl + 'visit-barcodes', payload).then(
        function(result) {
          return result.data.map(getVisitSpmns);
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
          return result.data.map(getVisitSpmns);
        }   
      );
    }

    function getVisits(visitNames) {
      return getVisitsSpecimens({visitNames: visitNames});
    }

    function getSpecimenVisits(specimenLabels) {
      return getVisitsSpecimens({aliquotLabels: specimenLabels});
    }

    function getVisitsSpecimens(params) {
      return $http.get(baseUrl + 'visit-barcodes/visits', {params: params}).then(
        function(result) {
          return result.data.map(getVisitSpmns);
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

      getVisits: getVisits,

      getSpecimenVisits: getSpecimenVisits,

      savePrimarySpecimens: savePrimarySpecimens,

      printSpecimenLabels: printSpecimenLabels,
 
      validateSpecimenPositions: validateSpecimenPositions,

      saveChildSpecimens: saveChildSpecimens
    };
  });
