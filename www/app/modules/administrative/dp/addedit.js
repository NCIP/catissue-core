
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User, SavedQuery, Site) {
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.institutes = [];
      $scope.instituteNames = [];
      $scope.sites = [];
      $scope.queryList = [];
      $scope.instSiteMap = {};
      $scope.distSites = [];
      loadInstitutes();
      loadSites($scope.distributionProtocol.instituteName);
      loadQueries();
    }
    
    function loadInstitutes() {
      Institute.query().then(
        function(institutes) {
          $scope.institutes = institutes;
          loadDistInstSites(institutes);
        }
      );
    }

    function loadDistInstSites(institutes) {
      $scope.distSites = getInstSiteMapFromDistSites($scope.distributionProtocol);
      $scope.instSiteMap = {};
      angular.forEach($scope.distSites, function (site, index) {
        $scope.instSiteMap[site.instituteName] = getSites(site.instituteName);
      });
      $scope.instituteNames = filterInstituteNames($scope.institutes, $scope.distSites);
    }
    
    function loadQueries(searchTerm) {
      $scope.queryList = SavedQuery.list({searchString: searchTerm});
    }
    
    $scope.createDp = function() {
      var dp = angular.copy($scope.distributionProtocol);
      dp = updateDistSites(dp, $scope.distSites);
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
      var instituteName = $scope.distSites[index].instituteName;
      var sites = $scope.instSiteMap[instituteName];
      if (!sites || sites.length == 0) {
        $scope.instSiteMap[instituteName] = getSites(instituteName);
      }
      
      $scope.distSites[index].sites = [];
      $scope.instituteNames = filterInstituteNames($scope.institutes, $scope.distSites);
    }
    
    $scope.removeDistSite = function (index) {
      $scope.distSites.splice(index,1);
      $scope.instituteNames = filterInstituteNames($scope.institutes, $scope.distSites);
      if ($scope.distSites.length === 0) {
        $scope.distSites = $scope.addDistSite($scope.distSites);
      }
    }
    
    $scope.addDistSite = function (distSites) {
      if (!distSites) {
        distSites = [];
      }
      
      distSites.push({instituteName: '', sites: []});
      return distSites;
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
        result = $scope.addDistSite(result);
      }
      
      return result;
    }
    
    function getInstituteNames (institutes) {
      return institutes.map(
        function (institute) {
          return institute.name;
        }
      );
    }
    
    function filterInstituteNames (institutes, selectedInst) {
      var instituteNames = getInstituteNames(institutes);
      for (var i=0; i < selectedInst.length; i++) {
        var index = instituteNames.indexOf(selectedInst[i].instituteName);
        if (index != -1) {
          instituteNames.splice(index, 1);
        }
      }
      
      return instituteNames
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
    
    function updateDistSites(dp, distSites) {
      var sites = [];
      angular.forEach(distSites, function (site) {
        angular.forEach(site.sites, function (siteName) {
          sites.push({name: siteName});
        });
      });
      dp.distributingSites = sites;
      return dp;
    }
    
    $scope.loadQueries = loadQueries;
    
    init();
  });
