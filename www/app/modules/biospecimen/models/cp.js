
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

    CollectionProtocol.listForRegistrations = function(siteNames) {
      var params = {siteName: siteNames, op: 'Create', resource: 'ParticipantPhi'};
      return $http.get(CollectionProtocol.url() + 'byop', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
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

    CollectionProtocol.getConsentTiers = function(cpId) {
      return $http.get(CollectionProtocol.url() + cpId + '/consent-tiers').then(function(result) { return result.data; });
    };

    CollectionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    CollectionProtocol.prototype.getUiPpidFmt = function() {
      var input = this.ppidFmt;
      if (!input) {
        return {};
      }

      var matches = input.match(/\%(\d)+d/);
      if (matches && matches.length > 1) {
        var idx = input.indexOf(matches[0]);
        return {
          prefix: input.substr(0, idx),
          suffix: input.substr(idx + matches[0].length),
          digitsWidth: parseInt(matches[1])
        };
      }

      return {prefix: input}
    }

    return CollectionProtocol;
  });
