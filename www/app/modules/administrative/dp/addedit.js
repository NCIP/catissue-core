
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User, SavedQuery, Site) {
    
    var availableInstituteNames = [];
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.sites = [];
      $scope.queryList = [];
      loadInstitutes();
      loadSites($scope.distributionProtocol.instituteName);
      loadQueries();
    }
    
    function loadInstitutes() {
      $scope.institutes = [];
      $scope.instituteNames = [];
      
      Institute.query().then(
        function(institutes) {
          $scope.institutes = institutes;
          loadDistInstSites();
        }
      );
    }
    
    function loadQueries(searchTerm) {
      $scope.queryList = SavedQuery.list({searchString: searchTerm});
    }

    function loadDistInstSites() {
      var dp = $scope.distributionProtocol;
      getInstSiteMapFromDistSites(dp);
      $scope.instSiteMap = {};
      angular.forEach(dp.distributingSites, function (site, index) {
        $scope.instSiteMap[site.instituteName] = getSites(site.instituteName);
      });
      
      filterAvailableInstituteNames();
    }
        
    function getInstSiteMapFromDistSites (dp) {
      var map = {};
      angular.forEach(dp.distributingSites, function (site) {
        if (!map[site.instituteName]) {
          map[site.instituteName] = {instituteName: site.instituteName, sites: []};
        }
        
        map[site.instituteName].sites.push(site.name);
      });
      
      var result = [];
      angular.forEach(map, function (instituteSites) {
        result.push(instituteSites);
      });
      
      if (result.length == 0) {
        result = [{instituteName: '', sites: []}];
      }
      
      dp.distributingSites = result;
    }
    
    function filterAvailableInstituteNames () {
      if (availableInstituteNames.length == 0 ) {
        availableInstituteNames = Institute.getNames($scope.institutes);
      }
      
      var selectedInst = getDistInstitutes();
      $scope.instituteNames = availableInstituteNames.filter(
        function(name) {
          return selectedInst.indexOf(name) == -1;
        }
      )
    }
    
    function getDistInstitutes () {
      var dp = $scope.distributionProtocol;
      if (!dp.distributingSites) {
        dp.distributingSites = [];
      }
      
      return dp.distributingSites.map(function(instSite) { return instSite.instituteName});
    }
    
    function loadSites(instituteName) {
      $scope.sites = getSites(instituteName);
    }
    
    function getSites(instituteName) {
      var sites = [];
      Site.listForInstitute(instituteName).then(
        function (result) {
          angular.forEach(result, function (site) {
            sites.push(site);
          });
        }
      );
      
      return sites;
    }
    
    function newDistSite () {
      var dp = $scope.distributionProtocol;
      if (!dp.distributingSites) {
        dp.distributingSites = [];
      }
      
      dp.distributingSites.push({instituteName: '', sites: []});
    }
    
    function updateDistSites (dp) {
      var sites = [];
      angular.forEach(dp.distributingSites, function (distSite) {
        angular.forEach(distSite.sites, function (siteName) {
          sites.push({name: siteName});
        });
      });
      
      dp.distributingSites = sites;
    }
    
    $scope.createDp = function() {
      var dp = angular.copy($scope.distributionProtocol);
      updateDistSites(dp);
      dp.$saveOrUpdate().then(
        function(savedDp) {
          $state.go('dp-detail.overview', {dpId: savedDp.id});
        }
      );
    };

    $scope.onInstituteSelect = function() {
      $scope.distributionProtocol.principalInvestigator = undefined;
      $scope.piFilterOpts.institute = $scope.distributionProtocol.instituteName;
      $scope.distributionProtocol.defReceivingSiteName = undefined;
      loadSites($scope.distributionProtocol.instituteName);
    }
    
    $scope.onDistInstSelect = function (index) {
      var dp = $scope.distributionProtocol;
      var instituteName = dp.distributingSites[index].instituteName;
      var sites = $scope.instSiteMap[instituteName];
      if (!sites || sites.length == 0) {
        $scope.instSiteMap[instituteName] = getSites(instituteName);
      }
      
      dp.distributingSites[index].sites = [];
      filterAvailableInstituteNames();
    }
    
    $scope.removeDistSite = function (index) {
      var dp = $scope.distributionProtocol;
      dp.distributingSites.splice(index, 1);
      if (dp.distributingSites.length == 0) {
        newDistSite();
      }
      
      filterAvailableInstituteNames();
    }
    
    $scope.addDistSite = newDistSite;

    $scope.loadQueries = loadQueries;
    
    init();
  });
