angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel, $http) {
    var Site = new osModel('sites');

    Site.prototype.getType = function() {
      return 'site';
    }

    Site.prototype.getDisplayName = function() {
      return this.name;
    }

    Site.getByName = function (name) {
      return $http.get(Site.url() + 'byname', {params: {name: name}}).then(
        function(result) {
          return new Site(result.data);
        }
      );
    }
    
    Site.listForCps = function(opName) {
      var opts = {resource: 'CollectionProtocol', operation: opName};
      return getSites(opts);
    }

    Site.listForParticipants = function(opName, listAll) {
      listAll = listAll || false;
      var opts = {resource: 'ParticipantPhi', operation: opName, listAll: listAll};
      return getSites(opts);
    }

    Site.listForContainers = function(opName) {
      var opts = {resource: 'StorageContainer', operation: opName};
      return getSites(opts);
    }

    Site.listForUsers = function(opName, searchTerm) {
      var opts = {resource: 'User', operation: opName, name: searchTerm};
      return getSites(opts);
    }

    Site.listForInstitute = function(instituteName, listAll, searchTerm) {
       var opts = {institute: instituteName, listAll: listAll || false, name: searchTerm};
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
