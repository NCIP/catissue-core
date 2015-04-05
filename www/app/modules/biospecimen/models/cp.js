
angular.module('os.biospecimen.models.cp', ['os.common.models'])
  .factory('CollectionProtocol', function(osModel, $http, $q) {
    var CollectionProtocol =
      osModel(
        'collection-protocols',
        function(cp) {
          cp.consentModel = osModel('collection-protocols/' + cp.$id() + '/consent-tiers');
        }
      );

    CollectionProtocol.list = function(opts) {
      var defOpts = {detailedList: true};
      return CollectionProtocol.query(angular.extend(defOpts, opts || {}));
    }

    //
    // TODO: This should be an instance method.
    //
    CollectionProtocol.getWorkflows = function(cpId) {
      return $http.get(CollectionProtocol.url() + cpId + '/workflows').then(
        function(result) {
          return result.data;
        }
      )
    }

    CollectionProtocol.prototype.getType = function() {
      return 'collection_protocol';
    }

    CollectionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }

    CollectionProtocol.prototype.getConsentTiers = function() {
      return this.consentModel.query();
    };

    CollectionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    return CollectionProtocol;
  });
