angular.module('os.rde')
  .controller('RdePrintSpecimenLabelsCtrl', function($scope, $state, session, visits, Specimen, RdeApis, Alerts) {
    function init() {
      var sessionData = session.data.printLabels;

      angular.forEach(visits, function(visit) {
        visit.specimensForPrint = Specimen.flatten(visit.specimens);
        angular.forEach(visit.specimensForPrint, function(spmn) {
          spmn.parent = undefined;

          if (!!spmn.pooledSpecimen) {
            spmn.pooled = true;
            spmn.pooledSpecimen = undefined;
          }

          if (angular.isDefined(spmn.selected)) {
            return;
          }

          if (sessionData) {
            spmn.selected = sessionData[visit.name].indexOf(spmn.label) != -1;
          } else {
            spmn.selected = (spmn.labelAutoPrintMode == 'ON_COLLECTION');
          }
        });

        visit.selAllForPrint = areAllSpmnsSelected(visit);
      });

      $scope.input = {visits: visits};
    }

    function getSpmnsToPrint(visit, specimens) {
      var result = [];
      angular.forEach(specimens, function(specimen) {
        if (!hasAnySpmnToPrint(specimen)) {
          return;
        }

        var spmnToPrint = {
          label: specimen.label,
          reqId: specimen.reqId,
          visitName: visit.name,
          print: specimen.selected
        };

        spmnToPrint.specimensPool = getSpmnsToPrint(visit, specimen.specimensPool);
        spmnToPrint.children = getSpmnsToPrint(visit, specimen.children);
        result.push(spmnToPrint);
      });

      return result;
    }

    function areAllSpmnsSelected(visit) {
      var allSel = true;
      for (var i = 0; i < visit.specimensForPrint.length; ++i) {
        if (!visit.specimensForPrint[i].selected) {
          allSel = false;
          break;
        }
      }

      return allSel;
    }

    function hasAnySpmnToPrint(specimen) {
      if (specimen.selected) {
        return true;
      }

      if (specimen.specimensPool) {
        for (var i = 0; i < specimen.specimensPool.length; ++i) {
          if (hasAnySpmnToPrint(specimen.specimensPool[i])) {
            return true;
          }
        }
      }

      if (specimen.children) {
        for (var i = 0; i < specimen.children.length; ++i) {
          if (hasAnySpmnToPrint(specimen.children[i])) {
            return true;
          }
        }
      }

      return false;
    }

    function saveSession(step) {
      var printLabels = {};
      angular.forEach(visits, function(visit) {
        printLabels[visit.name] = [];
        angular.forEach(visit.specimensForPrint, function(spmn) {
          if (!spmn.selected) {
            return;
          }

          printLabels[visit.name].push(spmn.label);
        });
      });

      angular.extend(session.data, {
        step: step || $state.$current.name,
        printLabels: printLabels
      });

      return session.saveOrUpdate()
    }

    $scope.toggleAllSpmnsSelForPrint = function(visit) {
      angular.forEach(visit.specimensForPrint, function(spmn) {
        spmn.selected = visit.selAllForPrint;
      });
    };

    $scope.toggleSpmnSelForPrint = function(visit, spmn) {
      if (spmn.selected) {
        visit.selAllForPrint = areAllSpmnsSelected(visit);
      } else {
        if (visit.selAllForPrint) {
          visit.selAllForPrint = false;
        }
      }
    }

    $scope.printLabels = function() {
      var spmnsToPrint = [];
      angular.forEach($scope.input.visits, function(visit) {
        spmnsToPrint = spmnsToPrint.concat(getSpmnsToPrint(visit, visit.specimens));
      });

      if (spmnsToPrint.length == 0) {
        $scope.skipPrintLabels();
        return;
      }

      RdeApis.printSpecimenLabels(spmnsToPrint).then(
        function(printJob) {
          Alerts.success('specimens.labels_print_job_created', {jobId: printJob.id});
          $scope.skipPrintLabels();
        }
      );
    }

    $scope.skipPrintLabels = function() {
      var nextStep = 'rde-select-container';
      saveSession(nextStep).then(
        function() {
          angular.forEach($scope.input.visits, function(visit) {
            delete visit.specimensForPrint;
          });

          $state.go(nextStep, {sessionId: session.uid});
        }
      );
    }

    $scope.saveSession = saveSession;

    init();
  });
