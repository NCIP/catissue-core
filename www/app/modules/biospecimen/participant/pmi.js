
angular.module('os.biospecimen.participant.pmis', [])
  .filter('showMrns', function() {
    return function(pmis) {
      if (!pmis) {
        return "";
      }

      var result = [];
      angular.forEach(pmis, function(pmi) {
        result.push(pmi.mrn + " (" + pmi.siteName + ")");
      });

      return result.join(", ");
    };
  });
