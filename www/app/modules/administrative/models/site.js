angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = new osModel('sites');

    Site.prototype.getType = function() {
      return 'site';
    }

    Site.prototype.getDisplayName = function() {
      return this.name;
    }

    Site.listForCps = function(opName) {
      var opts = {resource: 'CollectionProtocol', operation: opName};
      return getSites(opts);
    }

    Site.listForParticipants = function(opName) {
      var opts = {resource: 'ParticipantPhi', operation: opName};
      return getSites(opts);
    }

    Site.listForContainers = function(opName) {
      var opts = {resource: 'StorageContainer', operation: opName};
      return getSites(opts);
    }

    Site.listForUsers = function(opName) {
      var opts = {resource: 'User', operation: opName};
      return getSites(opts);
    }

    Site.listForInstitute = function(instituteName) {
       var opts = {institute: instituteName}
       return getSites(opts);
    }

    Site.list = function(opts) {
      return getSites(opts);
    }

    function getSites(opts) {
      return Site.query(opts).then(
        function(sites) {
          return sites.map(function(site) { return site.name; });
        }
      );
    }

    return Site;
  });
