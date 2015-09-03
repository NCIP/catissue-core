
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
        availableInstituteNames = getInstituteNames($scope.institutes);
      }
      
      var selectedInst = $scope.distributionProtocol.getInstitutes();
      $scope.instituteNames = availableInstituteNames.filter(
        function(name) {
          return selectedInst.indexOf(name) == -1;
        }
      )
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
    
    function getInstituteNames (institutes) {
      return institutes.map(
        function (institute) {
          return institute.name;
        }
      );
    }
    
    $scope.createDp = function() {
      var dp = angular.copy($scope.distributionProtocol);
      dp.$saveOrUpdate().then(
        function(savedDp) {
          $state.go('dp-detail.overview', {dpId: savedDp.id});
        }
      );
    };

    $scope.onInstituteSelect = function() {
      $scope.distributionProtocol.principalInvestigator = undefined;
      $scope.piFilterOpts = {institute: $scope.distributionProtocol.instituteName};
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
      $scope.distributionProtocol.removeDistSite(index);
      filterAvailableInstituteNames();
    }
    
    $scope.addDistSite = function () {
      $scope.distributionProtocol.newDistSite();
    }

    $scope.loadQueries = loadQueries;
    
    init();
  });
