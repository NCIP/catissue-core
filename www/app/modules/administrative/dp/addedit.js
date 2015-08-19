
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User, SavedQuery, Site) {
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.institutes = [];
      $scope.instituteList = [];
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
      $scope.instituteList = prepareInstList(institutes);
      $scope.distSites = prepareDistSites();
      $scope.instSiteMap = prepareInstSiteMap(institutes);
      angular.forEach($scope.distSites, function (site, index) {
        getSites(site.instituteName).then(
          function (sites) {
            $scope.instSiteMap[site.instituteName] = sites;
          }
        );
      });
      $scope.instituteList = filterInstList($scope.institutes, $scope.distSites);
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
      getSites(instituteName).then(
        function (sites) {
          $scope.instSiteMap[instituteName] = sites;
        }
      );
      $scope.distSites[index].sites = [];
      $scope.instituteList = filterInstList($scope.institutes, $scope.distSites);
    }
    
    $scope.addNewDistSite = function () {
      $scope.distSites = addDistSite($scope.distSites);
    }
    
    function addDistSite (distSites) {
      if (typeof distSites === 'undefined') {
        distSites = [];
      }
      distSites.push({instituteName: '', sites: []});
      
      return distSites;
    }
    
    $scope.removeDistSite = function (index) {
      $scope.distSites.splice(index,1);
      $scope.instituteList = filterInstList($scope.institutes, $scope.distSites);
      if ($scope.distSites.length === 0) {
        $scope.distSites = addDistSite($scope.distSites);
      }
    }
    
    function prepareDistSites () {
      var distributingSites = $scope.distributionProtocol.distributingSites;
      var distSites = [];
      angular.forEach(distributingSites, function (site) {
        var isInstPresent = false;
        angular.forEach(distSites, function (instSite) {
          if (instSite.instituteName === site.instituteName) {
            instSite.sites.push(site.name);
            isInstPresent = true;
          }
        });
        if (!isInstPresent) {
          distSites.push({instituteName: site.instituteName, sites: [site.name]});
        }
      });
      
      if (distSites.length == 0) {
        distSites = addDistSite(distSites);
      }
      
      
      return distSites;
    }
    
    function prepareInstList (institutes) {
      var instList = [];
      angular.forEach(institutes, function (inst) {
        instList.push(inst.name);
      });
      
      return instList;
    }
    
    function prepareInstSiteMap (institutes) {
      var instSiteMap = {};
      angular.forEach(institutes, function (inst) {
        instSiteMap[inst.name] = [];
      });
      
      return instSiteMap;
    }
    
    function filterInstList (include, exclude) {
      var newList = [];
      angular.forEach(include, function (inst) {
        var isPresent = false;
        angular.forEach(exclude, function (site) {
          if (site.instituteName === inst.name) {
            isPresent = true;
          }
        });
        if (!isPresent) {
          newList.push(inst.name);
        }
      });
      
      return newList;
    }
    
    function getInstNames (institutes) {
      var instNames = [];
      angular.forEach(institutes, function (inst) {
        instNames.push(inst.name);
      });
      
      return instNames;
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
        function (sites) {
          return sites;
        }
      );
    }
    
    function updateDistSites(dp, distSites) {
      var sites = [];
      angular.forEach(distSites, function (site) {
        angular.forEach(site.sites, function (siteName) {
          sites.push({instituteName: site.instituteName, name: siteName});
        });
      });
      dp.distributingSites = sites;
      return dp;
    }
    
    $scope.loadQueries = loadQueries;
    
    init();
  });
