angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function(
    $scope, $state, $stateParams, container, 
    Site, Container, CollectionProtocol, PvManager, Util) {


    var allCps = undefined;
    var allSpecimenTypes = undefined;
    var allowedCps = undefined;

    function init() {
      $scope.container = container;

      /**
       * Some how the ui-select's multiple option is removing pre-selected items
       * when cp list is being loaded or not yet loaded...
       * Therefore we copy pre-selected cps and then use it when all CPs are loaded
       */
      allowedCps = angular.copy(container.allowedCollectionProtocols);

      $scope.cps = [];
      $scope.specimenTypes = [];
      loadPvs();

      $scope.specimenTypeSelectorOpts = {
        items: $scope.specimenTypes,
        selectedCategories: container.allowedSpecimenClasses,
        selectedCategoryItems: container.allowedSpecimenTypes,
        categoryAttr: 'parent',
        valueAttr: 'value',
        allowAll: undefined
      };

      if ($stateParams.parentContainerName && $stateParams.parentContainerId &&
          $stateParams.posOne && $stateParams.posTwo) {
        //
        // This happens when user adds container from container map
        //

        $scope.locationSelected = true;
        container.position = {posOne: $stateParams.posOne, posTwo: $stateParams.posTwo};
        container.parentContainerName = $stateParams.parentContainerName;
        restrictCpsAndSpecimenTypes();
      }

      watchParentContainer();
    };

    function watchParentContainer() {
      $scope.$watch('container.parentContainerName', function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if (!newVal) {
          loadAllCpsAndSpecimenTypes();
        } else {
          restrictCpsAndSpecimenTypes();
        } 
      });
    };

    function loadPvs() {
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');
      $scope.sites = PvManager.getSites();

      if ($scope.container.parentContainerName) {
        restrictCpsAndSpecimenTypes();
      } else {
        loadAllCpsAndSpecimenTypes();
      }

      if (!!$scope.container.siteName) {
        $scope.loadContainers($scope.container.siteName);
      }

    };

    function restrictCpsAndSpecimenTypes() {
      var parentName = $scope.container.parentContainerName;
      Container.getByName(parentName).then(
        function(parentContainer) {
          restrictCps(parentContainer);
          restrictSpecimenTypes(parentContainer);
        }
      );
    };

    function loadAllCpsAndSpecimenTypes() {
      loadAllCps(); 
      loadAllSpecimenTypes();
    };
     
    function restrictCps(parentContainer) {
      var parentCps = parentContainer.allowedCollectionProtocols;
      if (parentCps.length > 0) {
        $scope.cps = parentCps;
      } else if (!allCps) { // CPs have not been loaded
        loadAllCps();
      } else {
        $scope.cps = allCps;
      }
    };

    function loadAllCps() {
      if (allCps) {
        return;
      }

      allCps = [];
      CollectionProtocol.query().then(
        function(cps) {
          $scope.cps = allCps = cps.map(function(cp) { return cp.shortTitle; });

          // fix - pre-selected cps were getting cleared
          $scope.container.allowedCollectionProtocols = allowedCps; 
        }
      );
    };

    function restrictSpecimenTypes(parentContainer) {
      if (allSpecimenTypes) {
        filterSpecimenTypes(parentContainer);
      } else {
        allSpecimenTypes = [];
        loadAllSpecimenTypes().then(
          function() { 
            filterSpecimenTypes(parentContainer); 
          }
        );
      }
    };

    function filterSpecimenTypes(parentContainer) {
      var allowedClasses = parentContainer.allowedSpecimenClasses;
      var allowedTypes = parentContainer.allowedSpecimenTypes;
      $scope.specimenTypeSelectorOpts.allowAll = allowedClasses;

      var filtered = allSpecimenTypes.filter(
        function(specimenType) {
          return allowedClasses.indexOf(specimenType.parent) >= 0 ||
                   allowedTypes.indexOf(specimenType.value) >= 0;
        }
      );
      Util.assign($scope.specimenTypes, filtered);
    };

    function loadAllSpecimenTypes() {
      if (allSpecimenTypes) {
        return;
      }

      allSpecimenTypes = [];
      PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          allSpecimenTypes = specimenTypes;
          Util.assign($scope.specimenTypes, specimenTypes);
          return allSpecimenTypes;
        }
      );
    };
          
    $scope.loadContainers = function(siteName) {
      Container.listForSite(siteName, true, true).then(
        function(result) {
          $scope.containers = [];
          angular.forEach(result, function(container) {
            $scope.containers.push(container.name);
          });
        }
      );
    };

    $scope.save = function() {
      var container = angular.copy($scope.container);
      container.createdBy = {id: 1}; // TODO: Handle this in server side.
      container.$saveOrUpdate().then(
        function(result) {
          if (!$scope.locationSelected) {
            $state.go('container-detail.overview', {containerId: result.id});
          } else {
            $state.go('container-detail.locations', {containerId: $stateParams.parentContainerId});
          }
        }
      );
    };

    init();
  });
