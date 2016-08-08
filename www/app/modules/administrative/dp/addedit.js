
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, $translate, $q, currentUser, distributionProtocol, extensionCtxt,
    DistributionProtocol, Institute, User, SavedQuery, Site, ExtensionsUtil) {
    
    var availableInstituteNames = [];
    var availableInstSites = {};
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(distributionProtocol, extensionCtxt);
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
          setDefaultDistInst();
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
          $scope.instSiteMap[site.instituteName] = getSites(site.instituteName, true);
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
    
    function setDefaultDistInst() {
      if (!currentUser.admin && !distributionProtocol.id) {
        distributionProtocol.distributingSites = [{
          instituteName: $scope.currentUser.instituteName,
          sites: []
        }];

        $scope.onDistInstSelect(0);
      }
    }

    function loadSites(instituteName) {
      $scope.sites = getSites(instituteName, false);
    }
    
    function getSites(instituteName, isAddAll) {
      var sites = isAddAll ? [$scope.all_sites] : [];
      if (availableInstSites[instituteName] && availableInstSites[instituteName].length != 0) {
        Array.prototype.push.apply(sites, availableInstSites[instituteName]);
      } else {
        Site.listForInstitute(instituteName).then(
          function(result) {
            availableInstSites[instituteName] = result;
            Array.prototype.push.apply(sites, result);
          }
        );
      }
      
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
    
    function isAllSitesPresent(index) {
      var dp = $scope.distributionProtocol;
      var sites = dp.distributingSites[index].sites;
      if (sites.indexOf($scope.all_sites) > -1) {
        return true;
      }
      
      return false;
    }
    
    $scope.createDp = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var dp = angular.copy($scope.distributionProtocol);
      updateDistSites(dp);
      if (formCtrl) {
        dp.extensionDetail = formCtrl.getFormData();
      }

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
      $scope.instSiteMap[instituteName] = getSites(instituteName, true);
      
      dp.distributingSites[index].sites = [];
      filterAvailableInstituteNames();
    }
    
    $scope.onDistSiteSelect = function(index) {
      var dp = $scope.distributionProtocol;
      var institute = dp.distributingSites[index].instituteName;
      if (isAllSitesPresent(index)) {
        dp.distributingSites[index].sites = [$scope.all_sites];
        $scope.instSiteMap[institute] = [];
      }
    }
    
    $scope.onDistSiteRemove = function(index) {
      var dp = $scope.distributionProtocol;
      var institute = dp.distributingSites[index].instituteName;
      if (!isAllSitesPresent(index)) {
        $scope.instSiteMap[institute] = getSites(institute, true);
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
