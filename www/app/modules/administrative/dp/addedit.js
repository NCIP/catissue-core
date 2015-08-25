
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
      $scope.instituteNames = getInstituteNames(institutes);
      $scope.distSites = getInstDistSitesMap();
      $scope.instSiteMap = prepareInstSiteMap(institutes);
      angular.forEach($scope.distSites, function (site, index) {
        getSites(site.instituteName).then(
          function (sites) {
            $scope.instSiteMap[site.instituteName] = sites;
          }
        );
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
      if ($scope.instSiteMap[instituteName].length == 0) {
        getSites(instituteName).then(
          function (sites) {
            $scope.instSiteMap[instituteName] = sites;
          }
        );
      }
      
      $scope.distSites[index].sites = [];
      $scope.instituteNames = filterInstituteNames($scope.institutes, $scope.distSites);
    }
    
    $scope.removeDistSite = function (index) {
      $scope.distSites.splice(index,1);
      $scope.instituteNames = filterInstituteNames($scope.institutes, $scope.distSites);
      if ($scope.distSites.length === 0) {
        $scope.distSites = addDistSite($scope.distSites);
      }
    }
    
    $scope.addNewDistSite = function () {
      $scope.distSites = addDistSite($scope.distSites);
    }
    
    function addDistSite (distSites) {
      if (!distSites) {
        distSites = [];
      }
      
      distSites.push({instituteName: '', sites: []});
      return distSites;
    }
    
    
    
    function getInstDistSitesMap () {
      var distributingSites = $scope.distributionProtocol.distributingSites;
      var distSites = [];
      angular.forEach(distributingSites, function (site) {
        var isInstPresent = false;
        for (var i = 0; i < distSites.length; i++) {
          if (distSites[i].instituteName === site.instituteName) {
            distSites[i].sites.push(site.name);
            isInstPresent = true;
            break;
          }
        }
        
        if (!isInstPresent) {
          distSites.push({instituteName: site.instituteName, sites: [site.name]});
        }
      });
      
      if (distSites.length == 0) {
        distSites = addDistSite(distSites);
      }
      
      return distSites;
    }
    
    function getInstituteNames (institutes) {
      return institutes.map(
        function (institute) {
          return institute.id;
        }
      );
    }
    
    function prepareInstSiteMap (institutes) {
      var instSiteMap = {};
      angular.forEach(institutes, function (inst) {
        instSiteMap[inst.name] = [];
      });
      
      return instSiteMap;
    }
    
    function filterInstituteNames (institutes, selectedInst) {
      var newList = [];
      angular.forEach(institutes, function (inst) {
        var isPresent = false;
        for (var i =0; i < selectedInst.length; i++) {
          if (selectedInst[i].instituteName === inst.name) {
            isPresent = true;
            break;
          }
        }
        
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
          sites.push({name: siteName});
        });
      });
      dp.distributingSites = sites;
      return dp;
    }
    
    $scope.loadQueries = loadQueries;
    
    init();
  });
