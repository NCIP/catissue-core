
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User, SavedQuery, Site) {
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.institutes = [];
      $scope.sites = [];
      $scope.queryList = [];
      loadInstitutes();
      loadSites($scope.distributionProtocol.instituteName);
      loadQueries();
      $scope.distInstList = [];
      $scope.distSiteList = [];
      $scope.distributionProtocol.addDistSite($scope.distributionProtocol.newDistSite());
    }
    
    function loadInstitutes() {
      Institute.query().then(
        function(institutes) {
          $scope.institutes = institutes;
          $scope.distInstList = getInstNames(institutes);
          var distInstNames = $scope.distributionProtocol.getDistInstNames();
          angular.forEach(distInstNames, getDistSites);
          filterAvailableDistInst();
        }
      );
    }

    
    function loadQueries(searchTerm) {
      $scope.queryList = SavedQuery.list({searchString: searchTerm});
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
    
    function getInstNames (institutes) {
      var instNames = [];
      angular.forEach(institutes, function (inst) {
        instNames.push(inst.name);
      });
      
      return instNames;
    }
    
    function filterAvailableDistInst () {
      var instituteNames = $scope.distributionProtocol.getDistInstNames();
      var institutes = getInstNames($scope.institutes);
      $scope.distInstList = institutes.filter(
        function (inst) {
          return instituteNames.indexOf(inst) == -1;
        }
      );
    }
    
    $scope.onDistInstSelect = function (instituteName, index) {
      $scope.distributionProtocol.distributingSites[index].sites = [];
      $scope.distSiteList.splice(index, 1);
      getDistSites(instituteName, index);
      filterAvailableDistInst();
    };
    
    function loadSites(instituteName) {
      Site.listForInstitute(instituteName).then(
        function (sites) {
          $scope.sites = sites;
        }
      );
    }
    
    function getDistSites(instituteName, index) {
      Site.listForInstitute(instituteName).then(
        function (sites) {
          $scope.distSiteList.splice(index, 0, sites);
        }
      );
    }
    
    $scope.addIfLastDistSite = function (index) {
      var dp = $scope.distributionProtocol;
      if (index === dp.distributingSites.length - 1) {
        dp.addDistSite(dp.newDistSite());
      }
    }
    
    $scope.removeDistSite = function (distSite, index) {
      var dp = $scope.distributionProtocol;
      dp.removeDistSite(distSite);
      if (dp.distributingSites.length == 0) {
        dp.addDistSite(dp.newDistSite());
      }
      
      $scope.distSiteList.splice(index, 1);
      filterAvailableDistInst();
    }

    $scope.loadQueries = loadQueries;
    
    init();
  });
