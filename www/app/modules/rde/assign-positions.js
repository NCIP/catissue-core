angular.module('os.rde')
  .controller('RdeAssignPositionsCtrl', function(
    $rootScope, $scope, $state, ctx, session, container, occupancyMap, visits,
    ContainerUtil, RdeApis, Util, Alerts) {

    function init() {
      if (container) {
        container.occupiedPositions = occupancyMap;
      }

      var aliquotLabels = "";
      if (session.data.assignPositions) {
        aliquotLabels = session.data.assignPositions.labels;
      }

      $scope.input = {
        visits: visits,
        container: container,
        aliquotLabels: aliquotLabels,
        occupancyMap: occupancyMap
      }

      $scope.showUpdatedMap();
    }

    function getVirtualAliquots() {
      var labels = Util.splitStr($scope.input.aliquotLabels, /,|\t|\n/);
      return labels.map(function(label) { return { label: label }; });
    }

    function getAliquotsFromMap() {
      var aliquots = [];
      var containerName = $scope.input.container.name;
      angular.forEach($scope.input.occupancyMap, function(pos) {
        if (!!pos.id) {
          return;
        }

        aliquots.push({
          label: pos.occupyingEntityName, 
          storageLocation: {name: containerName, positionX: pos.posOne, positionY: pos.posTwo}
        });
      });

      return aliquots;
    }

    function processAliquots(aliquots) {
      var errors = [], positions = [];
      for (var i = 0; i < aliquots.length; ++i) {
        var result = processAliquot(aliquots[i]);
        if (result.status != 'ok') {
          errors.push({status: result.status, label: aliquots[i].label});
          continue;
        }

        if ($scope.input.container) {
          positions.push({
            cpShortTitle: result.visit.cpShortTitle,
            label: result.specimen.label,
            specimenClass: result.specimen.specimenClass,
            type: result.specimen.type,
            positionX: result.specimen.storageLocation.positionX,
            positionY: result.specimen.storageLocation.positionY
          });
        }
      }

      $scope.input.aliquotErrors = errors;
      if (errors.length > 0) {
        cleanupAssignedPositions();
        return;
      }

      if (!$scope.input.container) {
        gotoCollectChildSpecimens($scope.input);
        return;
      }

      var occupancyDetail = {
        containerName: $scope.input.container.name,
        positions: positions
      };

      RdeApis.validateSpecimenPositions(occupancyDetail).then(
        function(positions) {
          angular.forEach(positions, function(pos) {
            if (!!pos.errorCode || !!pos.errorMsg) {
              errors.push({status: pos.errorCode, msg: pos.errorMsg , label: pos.label});
            }
          });

          $scope.input.aliquotErrors = errors;
          if (errors.length > 0) {
            cleanupAssignedPositions();
            return;
          }

          gotoCollectChildSpecimens($scope.input);
        }
      );
    }

    function processAliquot(aliquot) {
      var resultStatuses = [ 'ok', 'spmn_already_collected', 'primary_spmn_not_collected' ];

      for (var i = 0; i < $scope.input.visits.length; ++i) {
        var result = assignPosition($scope.input.visits[i], aliquot);

        if (resultStatuses.indexOf(result.status) != -1) {
          return result;
        }
      }

      return {status: 'no_spmn_in_visit'};
    }

    function assignPosition(visit, aliquot) {
      var result = undefined;
      var anticipated = undefined;
      for (var i = 0; i < visit.specimens.length; ++i) {
        var primarySpmn = visit.specimens[i];
        anticipated = searchAnticipatedSpecimen(primarySpmn.children, aliquot);
        if (anticipated) {
          break;
        }     
      };

      if (!anticipated) {
        return {status: 'no_spmn_in_visit'};
      }

      if (anticipated.saved || anticipated.status == 'Collected') {
        return {status: 'spmn_already_collected'};
      }

      if (primarySpmn.status != 'Collected') {
        return {status: 'primary_spmn_not_collected'};
      }

      anticipated.toSave = true;
      anticipated.visitId = visit.id;
      anticipated.frozenBy = $rootScope.currentUser;       
      anticipated.frozenTime = new Date().getTime();
      angular.extend(anticipated, aliquot);
      return {status: 'ok', visit: visit, specimen: anticipated};
    }

    function searchAnticipatedSpecimen(spmnReqs, aliquot) {
      for (var i = 0; i < spmnReqs.length; ++i) {
        if (spmnReqs[i].label == aliquot.label) {
          return spmnReqs[i];
        }

        var result = searchAnticipatedSpecimen(spmnReqs[i].children, aliquot);
        if (result) {
          return result;
        }
      }

      return undefined;
    }

    function cleanupAssignedPositions() {
      angular.forEach($scope.visits, function(visit) {
        angular.forEach(visit.specimens, function(specimen) {
          cleanupSpmnPositions(specimen.children);
        });
      });
    }

    function cleanupSpmnPositions(specimens) {
      angular.forEach(specimens, function(specimen) {
        if (specimen.toSave) {
          specimen.toSave = specimen.storageLocation = undefined;
          specimen.frozenBy = specimen.frozenTime = undefined;
        }

        cleanupSpmnPositions(specimen.children);
      });
    }

    $scope.showUpdatedMap = function() {
      var container = $scope.input.container;
      if (!container) {
        return;
      }

      var result = ContainerUtil.assignPositions(
        container, 
        container.occupiedPositions, 
        $scope.input.aliquotLabels);

      $scope.input.occupancyMap = result.map;
      if (result.noFreeLocs) {
        Alerts.error("container.no_free_locs");
        return;
      }
    }

    $scope.processAliquots = function() {
      var aliquots = !$scope.input.container ? getVirtualAliquots() : getAliquotsFromMap();
      if (!aliquots || aliquots.length == 0) {
        Alerts.error("Please scan at least one aliquot label");
        return;
      }

      alert(ctx.workflow);
      if (ctx.workflow != 'process-aliquots') {
        alert("in here");
        processAliquots(aliquots);
        return;
      }

      //
      // TODO: review
      //
      var aliquotLabels =  Util.splitStr($scope.input.aliquotLabels, /,|\t|\n/);
      RdeApis.getSpecimenVisits(aliquotLabels).then(
        function(visitsSpmns) {
          ctx.visitsSpmns = visitsSpmns
          $scope.input.visits = angular.copy(ctx.visitsSpmns);
          processAliquots(aliquots);
        }
      );
    }

    function gotoCollectChildSpecimens(input) {
      var sessionData = {};
      angular.forEach(input.visits, function(visit) {
        sessionData[visit.name] = getSpecimensToSave(visit.specimens);
      });

      var nextStep = 'rde-collect-child-specimens';
      angular.extend(session.data, {
        step: nextStep,
        visits: input.visits.map(function(v) { return {name: v.name, visitDate: v.visitDate} }),
        assignPositions: {labels: input.aliquotLabels},
        collectChildSpecimens: sessionData
      });

      ctx.visitSpmns = input.visits;
      return session.$saveOrUpdate().then(
        function() {
          $state.go(nextStep, {sessionId: session.id});
        }
      );
    }

    function getSpecimensToSave(specimens) {
      var spmnsToSave = [];
      angular.forEach(specimens, function(specimen) {
        if (specimen.toSave) {
          var spmnToSave = angular.extend({}, specimen);
          delete spmnToSave.children;
          delete spmnToSave.parent;
          delete spmnToSave.pooledSpmn;
          delete spmnToSave.specimensPool;
          delete spmnToSave.childrenToReview;

          spmnsToSave.push(spmnToSave);
        }

        spmnsToSave = spmnsToSave.concat(getSpecimensToSave(specimen.children));
      });

      return spmnsToSave;
    }

    $scope.saveSession = function() {
      angular.extend(session.data, {
        step: $state.$current.name,
        assignPositions: {labels: $scope.input.aliquotLabels},
      });

      session.$saveOrUpdate();
    }

    init();
  });

