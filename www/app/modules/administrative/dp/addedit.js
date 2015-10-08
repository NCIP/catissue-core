
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User, SavedQuery, Site, $translate) {
    
    var availableInstituteNames = [];
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.sites = [];
      $scope.queryList = [];
      $scope.all_sites = $translate.instant('dp.all_sites');
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
      $scope.instSiteMap = dp.distributingSites;
      angular.forEach(dp.distributingSites, function (site, index) {
        if (site.sites.indexOf($scope.all_sites) == -1) {
          getSites(site.instituteName).then(
            function(sites) {
              $scope.instSiteMap[site.instituteName] = [$scope.all_sites].concat(sites);
            }
          );
        }
      });
      
      filterAvailableInstituteNames();
    }
        
    function getInstSiteMapFromDistSites (dp) {
      var result = [];
      angular.forEach(dp.distributingSites, function (instituteSites, institute) {
        if (!instituteSites || instituteSites.length == 0) {
          result.push({instituteName: institute, sites: [$scope.all_sites]});
        } else {
          result.push({instituteName: institute, sites: instituteSites});
        }
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
      getSites(instituteName).then(
        function (sites) {
          $scope.sites = sites;
        }
      );
    }
    
    function getSites(instituteName) {
      return Site.listForInstitute(instituteName).then(
        function(result) {
          return result;
        }
      );
    }
    
    function newDistSite () {
      var dp = $scope.distributionProtocol;
      if (!dp.distributingSites) {
        dp.distributingSites = [];
      }
      
      dp.distributingSites.push({instituteName: '', sites: []});
    }
    
    function updateDistSites (dp) {
      var sites = {};
      angular.forEach(dp.distributingSites, function (distSite) {
        if (distSite.sites.indexOf($scope.all_sites) > -1) {
          sites[distSite.instituteName] = [];
        } else {
          sites[distSite.instituteName] = distSite.sites;
        }
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
      $scope.piFilterOpts = {institute: $scope.distributionProtocol.instituteName};
      $scope.distributionProtocol.defReceivingSiteName = undefined;
      loadSites($scope.distributionProtocol.instituteName);
    }
    
    $scope.onDistInstSelect = function (index) {
      var dp = $scope.distributionProtocol;
      var instituteName = dp.distributingSites[index].instituteName;
      var sites = $scope.instSiteMap[instituteName];
      if (!sites || sites.length == 0) {
        getSites(instituteName).then(
          function(sites) {
            $scope.instSiteMap[instituteName] = [$scope.all_sites].concat(sites);
          }
        );
      }
      
      dp.distributingSites[index].sites = [];
      filterAvailableInstituteNames();
    }
    
    $scope.onDistSiteSelect = function(index) {
      var dp = $scope.distributionProtocol;
      var institute = dp.distributingSites[index].instituteName;
      var sites = dp.distributingSites[index].sites;
      var presentAllSites = false;
      if (sites.indexOf($scope.all_sites) > -1) {
        presentAllSites = true;
      }
      
      if (presentAllSites) {
        dp.distributingSites[index].sites = [$scope.all_sites];
        $scope.instSiteMap[institute] = [];
      }
    }
    
    $scope.onDistSiteRemove = function(index) {
      var dp = $scope.distributionProtocol;
      var institute = dp.distributingSites[index].instituteName;
      var sites = dp.distributingSites[index].sites;
      var absentAllSites = false;
      if (sites.indexOf($scope.all_sites) == -1) {
        absentAllSites = true;
      }
      
      if (absentAllSites) {
        if (!$scope.instSiteMap[institute] || $scope.instSiteMap[institute].length == 0) {
          getSites(institute).then(
            function(sites) {
              $scope.instSiteMap[institute] = [$scope.all_sites].concat(sites);
            }
          );
        }
      }
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
