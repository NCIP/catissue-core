
angular.module('os.biospecimen.models.cp', ['os.common.models'])
  .factory('CollectionProtocol', function(osModel, $http, $q) {
    var CollectionProtocol =
      osModel(
        'collection-protocols',
        function(cp) {
          cp.consentModel = osModel('collection-protocols/' + cp.$id() + '/consent-tiers');
        }
      );

    CollectionProtocol.prototype.getConsentTiers = function() {
      return this.consentModel.query();
    };

    CollectionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    // Temp Added
    var cps = {
      "AKU":[{id: 1, shortTitle:'AMCS'}, {id: 2, shortTitle:'Amy CP'}],
      "ATCC": [{id: 1, shortTitle:'Arm1'}, {id: 2, shortTitle:'Arm2'}]
    };

    CollectionProtocol.listCpsForSite = function(siteName) {
      //TODO: get Site CPs by calling back end REST
      var result = angular.copy(cps[siteName] || []);
      var d = $q.defer();
      d.resolve(result);
      return d.promise;
    }

    return CollectionProtocol;
  });
