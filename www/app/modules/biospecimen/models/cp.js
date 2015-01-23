
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

    CollectionProtocol.getCps = function(siteName) {
      //TODO: will fix this with back-end change
      var cps = {
        "In Transit": [{id: -1, shortTitle: 'All'}, {"id" : 1, "shortTitle": "Cp1"}, {"id" : 1, "shortTitle": "Cp2"}],
        "Univ of Leicester": [{id: -1, shortTitle: 'All'}, {"id" : 1, "shortTitle": "Cp1"}, {"id" : 1, "shortTitle": "Cp3"}]
      };
      var d = $q.defer();
      d.resolve(cps[siteName]);
      return d.promise;
    }

    return CollectionProtocol;
  });
