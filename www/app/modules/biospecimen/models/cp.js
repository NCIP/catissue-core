
angular.module('os.biospecimen.models.cp', ['os.common.models'])
  .factory('CollectionProtocol', function(osModel, $http, $q, Form) {
    var CollectionProtocol =
      osModel(
        'collection-protocols',
        function(cp) {
          cp.consentModel = osModel('collection-protocols/' + cp.$id() + '/consent-tiers');

          cp.consentModel.prototype.getDisplayName = function() {
            return this.statement;
          }

          cp.consentModel.prototype.getType = function() {
            return 'consent';
          }
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

    CollectionProtocol.getSopDocUploadUrl = function() {
      return CollectionProtocol.url() + "sop-documents";
    }

    CollectionProtocol.getWorkflows = function(cpId) {
      return $http.get(CollectionProtocol.url() + cpId + '/workflows').then(
        function(result) {
          return result.data;
        }
      )
    }

    CollectionProtocol.saveWorkflows = function(cpId, workflows) {
      return $http.put(CollectionProtocol.url() + cpId  + '/workflows', workflows).then(
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
    
    CollectionProtocol.prototype.getSopDocDownloadUrl = function() {
      return CollectionProtocol.url() + this.$id() + "/sop-document";
    }

    CollectionProtocol.prototype.copy = function(copyFrom) {
      return $http.post(CollectionProtocol.url() + copyFrom  + '/copy', this.$saveProps()).then(CollectionProtocol.modelRespTransform);
    };

    CollectionProtocol.prototype.getConsentTiers = function() {
      return this.consentModel.query();
    };

    CollectionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    CollectionProtocol.prototype.updateConsentsWaived = function() {
      var params = {consentsWaived: this.consentsWaived};
      return $http.put(CollectionProtocol.url() + this.$id() + "/consents-waived", params).then(
        function(resp) {
          return new CollectionProtocol(result.data);
        }
      );
    }

    CollectionProtocol.prototype.getWorkflows = function() {
      return CollectionProtocol.getWorkflows(this.$id());
    }

    CollectionProtocol.prototype.saveWorkflows = function(workflows) {
      return CollectionProtocol.saveWorkflows(this.$id(), workflows);
    }

    CollectionProtocol.prototype.getRepositoryNames = function() {
      if (!this.cpSites) {
        return [];
      }
      return this.cpSites.map(function(cpSite) { return cpSite.siteName; });
    }

    CollectionProtocol.prototype.getCatalogQuery = function() {
      return $http.get(CollectionProtocol.url() + this.$id() + '/catalog-query').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    CollectionProtocol.prototype.getCatalogSetting = function() {
      return $http.get(CollectionProtocol.url() + this.$id() + '/catalog-settings').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    CollectionProtocol.prototype.saveCatalogSetting = function(setting) {
      var payload = angular.extend({cp: {id: this.$id()}}, setting);
      return $http.put(CollectionProtocol.url() + this.$id() + '/catalog-settings', payload).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    CollectionProtocol.prototype.deleteCatalogSetting = function() {
      return $http['delete'](CollectionProtocol.url() + this.$id() + '/catalog-settings');
    }

    CollectionProtocol.prototype.getForms = function(entityTypes) {
      var params = {entityType: entityTypes};
      return $http.get(CollectionProtocol.url() + this.$id() + '/forms', {params: params}).then(
        function(resp) {
          return resp.data.map(
            function(form) {
              form.id = form.formId;
              return new Form(form);
            }
          );
        }
      );
    };

    CollectionProtocol.prototype.$remove = function() {
      return $http['delete'](CollectionProtocol.url() + this.$id() + '?forceDelete=true').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return CollectionProtocol;
  });
