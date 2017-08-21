angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function(
    $scope, $state, $stateParams, $q, container, containerType, barcodingEnabled,
    Container, ContainerType, CollectionProtocol, PvManager, Util, Alerts) {

    var allSpecimenTypes = undefined;
    var allowedCps = undefined;

    function init() {
      container.storageLocation = container.storageLocation || {};
      container.$$dimensionless = !!container.id && container.noOfRows == null && container.noOfColumns == null;

      $scope.container = container;

      $scope.ctx = {mode: 'single', view: '', capacityReq: !!container.capacity, barcodingEnabled: barcodingEnabled};
      if ($stateParams.mode == 'createHierarchy') {
        $scope.ctx.mode = 'hierarchy';
      }

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

      if ($stateParams.siteName && $stateParams.parentContainerName && $stateParams.parentContainerId &&
          $stateParams.posOne && $stateParams.posTwo) {
        //
        // This happens when user adds container from container map
        //

        $scope.locationSelected = true;
        container.siteName = $stateParams.siteName;
        container.storageLocation = {
          name: $stateParams.parentContainerName,
          positionX: $stateParams.posOne,
          positionY: $stateParams.posTwo,
          position: $stateParams.pos
        };
        restrictCpsAndSpecimenTypes();
      }

      loadContainerTypes();
      setContainerTypeProps(containerType);

      watchParentContainer();
    };

    function watchParentContainer() {
      $scope.$watch('container.storageLocation.name', function(newVal, oldVal) {
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
      if ($scope.container.storageLocation.name) {
        restrictCpsAndSpecimenTypes();
      } else {
        loadAllCpsAndSpecimenTypes();
      }

    };

    function loadContainerTypes() {
      $scope.containerTypes = [];
      ContainerType.query().then(function(containerTypes) {
        $scope.containerTypes = containerTypes;
      });
    }
    
    function restrictCpsAndSpecimenTypes() {
      var parentName = $scope.container.storageLocation.name;
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

      CollectionProtocol.query({repositoryName: siteName, maxResults: 1000}).then(
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

    function saveContainer() {
      var container = angular.copy($scope.container);
      container.$saveOrUpdate().then(
        function(result) {
          $state.go('container-detail.locations', {containerId: result.id});
        }
      );
    };

    function createMultipleContainers() {
      var container = $scope.container, loc = $scope.container.storageLocation;
      if (loc.name) {
        Container.getVacantPositions(loc.name, loc.positionY, loc.positionX, loc.position, container.numOfContainers).then(
          function(positions) {
            createMultipleContainers0(container, positions);
          }
        );
      } else {
        createMultipleContainers0(container, []);
      }
    }

    function createMultipleContainers0(container, positions) {
      var numContainers = container.numOfContainers;
      var containers = [];
      for (var i = 0; i < numContainers; ++i) {
        containers[i] = angular.copy(container);
        delete containers[i].numOfContainers;
        if (positions.length > 0) {
          containers[i].storageLocation = positions[i];
        }
      }

      $scope.ctx.view       = 'review_multiple_containers';
      $scope.ctx.containers = containers;
    }

    function createHierarchy() {
      Container.createHierarchy($scope.container).then(
        function(resp) {
          if (resp.length == 1) {
            //
            // created only one container. go to that container detail
            //
            $state.go('container-detail.locations', {containerId: resp[0].id});
            return;
          } else if (resp[0].storageLocation && resp[0].storageLocation.id) {
            //
            // hierarchy created under an existing container
            // go to that container detail
            //
            $state.go('container-detail.locations', {containerId: resp[0].storageLocation.id});
          } else {
            //
            // hierarchy created for top-level container. go to list view with success message
            //
            Alerts.success('container.hierarchy_created_successfully', resp[0]);
            $state.go('container-list');
          }
        }
      )
    };

    function setContainerTypeProps(containerType) {
      if (!containerType) {
        $scope.container.typeId = undefined;
        return;
      }

      if (!!$scope.container.id) {
        return;
      }

      $scope.container.typeId = containerType.id;
      $scope.container.typeName = containerType.name;
      $scope.container.noOfRows = containerType.noOfRows;
      $scope.container.noOfColumns = containerType.noOfColumns;
      $scope.container.positionLabelingMode = containerType.positionLabelingMode;
      $scope.container.rowLabelingScheme = containerType.rowLabelingScheme;
      $scope.container.columnLabelingScheme = containerType.columnLabelingScheme;
      $scope.container.temperature = containerType.temperature;
      $scope.container.storeSpecimensEnabled = containerType.storeSpecimenEnabled;
    };

    $scope.loadAllCps = loadAllCps;

    $scope.onSelectContainerType = setContainerTypeProps;

    $scope.save = function() {
      if ($scope.ctx.mode == 'single') {
        saveContainer();
      } else if ($scope.ctx.mode == 'multiple') {
        createMultipleContainers();
      } else if ($scope.ctx.mode == 'hierarchy') {
        createHierarchy();
      }
    }

    $scope.onCreateModeChange = function() {
      var attrsToDelete = $scope.ctx.mode != 'single' ? ['name', 'barcode'] : ['numOfContainers'];
      attrsToDelete.forEach(function(attr) {
        delete $scope.container[attr];
      });

      if ($scope.ctx.mode != 'single') {
        container.$$dimensionless = false;
      }
    }

    //
    // Multiple container creation logic
    //
    $scope.siteSelected = function(container) {
      container.storageLocation = {};
    }

    $scope.copyFirstSiteToAll = function() {
      var siteName = $scope.ctx.containers[0].siteName;
      angular.forEach($scope.ctx.containers,
        function(container, idx) {
          if (idx == 0 || container.siteName == siteName) {
            return;
          }

          container.siteName = siteName;
          container.storageLocation = {};
        }
      );
    }

    $scope.copyFirstParentContainerToAll = function() {
      var name = $scope.ctx.containers[0].storageLocation.name;
      angular.forEach($scope.ctx.containers,
        function(container, idx) {
          if (idx == 0 || container.storageLocation.name == name) {
            return;
          }

          container.storageLocation = {name: name};
        }
      );
    }

    $scope.removeContainer = function(idx) {
      $scope.ctx.containers.splice(idx, 1);
      if ($scope.ctx.containers.length == 0) {
        $scope.ctx.view = '';
      }
    }

    $scope.saveMultipleContainers = function() {
      Container.createContainers($scope.ctx.containers).then(
        function(result) {
          Alerts.success('container.multiple_containers_created', {count: result.length});
          $state.go('container-list');
        }
      );
    }

    $scope.setDimensionless = function() {
      $scope.container.noOfRows = $scope.container.noOfColumns = null;
      $scope.container.positionLabelingMode = 'LINEAR';
      $scope.container.storeSpecimensEnabled = true;
      $scope.container.rowLabelingScheme = $scope.container.columnLabelingScheme = 'Numbers';
    }

    init();
  });
