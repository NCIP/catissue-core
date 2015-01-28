
angular.module('os.biospecimen.cp.specimens', ['os.biospecimen.models'])
  .controller('CpSpecimensCtrl', function(
    $scope, $state, $stateParams, $filter, $timeout,
    cp, events, specimenRequirements,
    Specimen, SpecimenRequirement, PvManager) {

    if (!$stateParams.eventId && !!events && events.length > 0) {
      $state.go('cp-detail.specimen-requirements', {eventId: events[0].id});
      return;
    }

    function init() {
      $scope.cp = cp;
      $scope.events = events;
      $scope.eventId = $stateParams.eventId;

      $scope.specimenRequirements = Specimen.flatten(specimenRequirements);

      $scope.view = 'list_sr';
      $scope.sr = {};
      $scope.aliquot = {};
      $scope.derivative = {};

      $scope.errorCode = '';
    }

    function addToSrList(sr) {
      specimenRequirements.push(sr);
      specimenRequirements = $filter('orderBy')(specimenRequirements, ['type', 'id']);
      $scope.specimenRequirements = Specimen.flatten(specimenRequirements);
    };

    function addChildren(parent, children) {
      if (!parent.children) {
        parent.children = [];
      }

      angular.forEach(children, function(child) {
        parent.children.push(child);
      });

      parent.children = $filter('orderBy')(parent.children, ['type', 'id']);
      $scope.specimenRequirements = Specimen.flatten(specimenRequirements);
    };

    var pvsLoaded = false;
    function loadPvs() {
      if (pvsLoaded) {
        return;
      }

      $scope.specimenClasses = PvManager.getPvs('specimen-class');
      $scope.specimenTypes = [];

      $scope.$watch('sr.specimenClass', function(newVal, oldVal) {
        if (!newVal || newVal == oldVal) {
          return;
        }

        $scope.specimenTypes = PvManager.getPvsByParent('specimen-class', newVal);
        $scope.sr.type = '';
      });

      $scope.anatomicSites = PvManager.getPvs('anatomic-site');
      $scope.lateralities = PvManager.getPvs('laterality');
      $scope.pathologyStatuses = PvManager.getPvs('pathology-status');
      $scope.storageTypes = PvManager.getPvs('storage-type');
      $scope.collectionProcs = PvManager.getPvs('collection-procedure');
      $scope.collectionContainers = PvManager.getPvs('collection-container');
      pvsLoaded = true;
    };

    $scope.loadSpecimenTypes = function(specimenClass) {
      $scope.specimenTypes = PvManager.getPvsByParent('specimen-class', specimenClass);
    };

    $scope.openSpecimenNode = function(sr) {
      sr.isOpened = true;
    };

    $scope.closeSpecimenNode = function(sr) {
      sr.isOpened = false;
    };

    $scope.showAddSr = function() {
      $scope.view = 'addedit_sr';
      $scope.sr = new SpecimenRequirement({eventId: $scope.eventId});
      loadPvs();
    };

    $scope.revertEdit = function() {
      $scope.view = 'list_sr';
      $scope.parentSr = null;
    };

    $scope.createSr = function() {
      $scope.sr.$saveOrUpdate().then(
        function(result) {
          addToSrList(result);
          $scope.view = 'list_sr';
        }
      );
    };

    ////////////////////////////////////////////////
    //
    //  Aliquot logic
    //
    ////////////////////////////////////////////////
    $scope.showCreateAliquots = function(sr) {
      $scope.parentSr = sr;
      $scope.view = 'addedit_aliquot';
      $scope.aliquot = {};
      loadPvs();
    };

    $scope.createAliquots = function() {
      if (!$scope.parentSr.hasSufficientQty($scope.aliquot)) {
        $scope.errorCode = 'srs.errors.insufficient_qty';
        $timeout(function() { $scope.errorCode = '' }, 3000);
        return;
      }
      
      $scope.parentSr.createAliquots($scope.aliquot).then(
        function(aliquots) {
          addChildren($scope.parentSr, aliquots);
          $scope.parentSr.isOpened = true;

          $scope.aliquot = {};
          $scope.parentSr = undefined;
          $scope.view = 'list_sr';
        }
      );
    };

    ////////////////////////////////////////////////
    //
    //  Derivative logic
    //
    ////////////////////////////////////////////////
    $scope.showCreateDerived = function(sr) {
      $scope.parentSr = sr;
      $scope.view = 'addedit_derived';
      $scope.derivative = {};
      loadPvs();
    };

    $scope.createDerivative = function() {
      $scope.parentSr.createDerived($scope.derivative).then(
        function(derived) {
          addChildren($scope.parentSr, [derived]);
          $scope.parentSr.isOpened = true;

          $scope.derivative = {};
          $scope.parentSr = undefined;
          $scope.view = 'list_sr';
        }
      );
    };

    init();
  });
