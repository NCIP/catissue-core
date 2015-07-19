angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function(
    $scope, $state, $stateParams, $q, container, 
    Site, Container, CollectionProtocol, PvManager, Util) {

    var allSpecimenTypes = undefined;
    var allowedCps = undefined;
    var parentContainerName = undefined;

    function init() {
      container.parentContainerId = undefined;
      $scope.container = container;
      parentContainerName = container.parentContainerName;

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
      
      var op = !!$scope.container.id ? 'Update' : 'Create';
      $scope.sites = [];
      Site.listForContainers(op).then(function(sites) {
        $scope.sites = sites;
      });

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
      var parentCps = parentContainer.calcAllowedCollectionProtocols;
      if (parentCps.length > 0) {
        $scope.cps = parentCps;
      } else {
        loadAllCps(parentContainer.siteName);
      } 

      $scope.container.allowedCollectionProtocols = allowedCps; 
    };

    function loadAllCps(siteName) {
      siteName = !siteName ? $scope.container.siteName : siteName;

      CollectionProtocol.query({repositoryName: siteName}).then(
        function(cps) {
          $scope.cps = cps.map(function(cp) { return cp.shortTitle; });

          // fix - pre-selected cps were getting cleared
          $scope.container.allowedCollectionProtocols = allowedCps; 
        }
      );
    };

    function restrictSpecimenTypes(parentContainer) {
      if (allSpecimenTypes) {
        filterSpecimenTypes(parentContainer);
      } else {
        loadAllSpecimenTypes().then(
          function() { 
            filterSpecimenTypes(parentContainer); 
          }
        );
      }
    };

    function filterSpecimenTypes(parentContainer) {
      var allowedClasses = parentContainer.calcAllowedSpecimenClasses;
      var allowedTypes = parentContainer.calcAllowedSpecimenTypes;
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
        var d = $q.defer();
        d.resolve(allSpecimenTypes);
        return d.promise;
      }

      return PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          allSpecimenTypes = specimenTypes;
          Util.assign($scope.specimenTypes, specimenTypes);
          return allSpecimenTypes;
        }
      );
    };
          
    //
    // invoked when a site is selected on UI
    // 
    $scope.loadContainers = function(siteName) {
      Container.listForSite(siteName, true, true).then(
        function(result) {
          $scope.containers = [];
          angular.forEach(result, function(container) {
            $scope.containers.push(container.name);
          });
        }
      );

      loadAllCps(siteName);
    };

    $scope.save = function() {
      var container = angular.copy($scope.container);
      if (container.id && container.parentContainerName != parentContainerName) {
        //
        // if parent container is changed, we need to invalidate 
        // existing container position
        // 
        //
        container.position = undefined;
      }

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
