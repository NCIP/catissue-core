angular.module('os.rde')
  .controller('RdeCollectPrimarySpmnsCtrl', function($rootScope, $scope, $state, visits, cpRdeWfCfg, Util, RdeApis) {
    function init() {
      var user = $rootScope.currentUser;
      var time = new Date().getTime();

      angular.forEach(visits, function(visit) {
        visit.collDetail = {user: user, time: time};
        visit.recvDetail = {user: user, time: time};

        visit.primarySpmnLabels = '';

        var cfg = cpRdeWfCfg[visit.cpShortTitle].data || {};
        visit.scanEnabled = (cfg.primarySpmnScanning == true);
             
        var scanRequired = false;
        angular.forEach(visit.specimens, function(specimen) {
          if (!visit.scanEnabled || (cfg.spmnScanning && cfg.spmnScanning[specimen.reqId] == false)) {
            specimen.status = 'Collected';
          } else {
            specimen.status = 'Missed Collection';
            specimen.scanEnabled = true;
            scanRequired = true;
          }
           
          angular.forEach(specimen.specimensPool, function(poolSpmn) {
            poolSpmn.status = specimen.status;
            poolSpmn.scanEnabled = specimen.scanEnabled;
          });
        });

        visit.scanEnabled = scanRequired;
        setPrimarySpmns(visit);
      });

      $scope.input = {visits: visits};
    }

    function setPrimarySpmns(visit) {
      visit.primarySpmns = [];
      angular.forEach(visit.specimens, function(specimen) {
        if (!specimen.specimensPool) {
          visit.primarySpmns.push(specimen);
        } else {
          visit.primarySpmns = visit.primarySpmns.concat(specimen.specimensPool);
        }
      });
    }

    function getSpecimenToSave(visit, spmn) {
      var spmnToSave = {
        id: spmn.id,
        label: spmn.label,
        lineage: 'New',
        initialQty: spmn.initialQty,
        visitId: visit.id,
        reqId: spmn.reqId,
        collectionEvent: visit.collDetail,
        receivedEvent: visit.recvDetail,
        status: spmn.status,
        comments: visit.comments
      };
       
      var status = 'Missed Collection';
      var specimensPool = [];
      angular.forEach(spmn.specimensPool, function(poolSpmn) {
        specimensPool.push(getSpecimenToSave(visit, poolSpmn));
      });

      if (specimensPool.length > 0) {
        spmnToSave.specimensPool = specimensPool;
      }

      if (spmnToSave.status == 'Missed Collection') {
        spmnToSave.comments = spmn.comments;
        spmnToSave.children = getMissedChildSpmns(visit, spmn.children);
      }

      return spmnToSave;
    }

    function getMissedChildSpmns(visit, childSpmns) {
      if (!childSpmns) {
        return [];
      }

      return childSpmns.map(
        function(spmn) {
          return {
            id: spmn.id,
            visitId: visit.id,
            reqId: spmn.reqId,
            label: spmn.label,
            status: 'Missed Collection',
            initialQty: spmn.initialQty,
            lineage: spmn.lineage,
            children: getMissedChildSpmns(visit, spmn.children)
          }
        }
      );
    }

    function removeMissedSpmns(visits, spmns) {
      var missedLabels = [];
      angular.forEach(spmns, function(spmn) {
        if (spmn.status == 'Missed Collection') {
          missedLabels.push(spmn.label);
        }

        angular.forEach(spmn.specimensPool, function(poolSpmn) {
          if (poolSpmn.status == 'Missed Collection') {
            missedLabels.push(poolSpmn.label);
          }
        });
      });

      if (missedLabels.length == 0) {
        return;
      }

      for (var i = 0; i < visits.length; ++i) {
        for (var j = visits[i].specimens.length - 1; j >= 0; j--) {
          var spmn = visits[i].specimens[j];
          if (missedLabels.indexOf(spmn.label) != -1) {
            visits[i].specimens.splice(j, 1);
          } else if (spmn.specimensPool) {
            for (var k = spmn.specimensPool.length - 1; k >= 0; k--) {
              if (missedLabels.indexOf(spmn.specimensPool[k].label) != -1) {
                spmn.specimensPool.splice(k, 1);
              }
            }
          }
        }
      }
    }

    $scope.parsePrimarySpmnLabels = function(visit) { // TODO: why not use primarySpmns
      var labels = Util.splitStr(visit.primarySpmnLabels, /,|\t|\n/);
      angular.forEach(visit.specimens, function(specimen) {
        if (!specimen.scanEnabled) {
          return;
        }

        var specimensPool = [specimen];
        if (specimen.specimensPool && specimen.specimensPool.length > 0) {
          specimensPool = specimen.specimensPool;
        } 

        var status = 'Missed Collection';
        angular.forEach(specimensPool, function(poolSpmn) {
          if (labels.indexOf(poolSpmn.label) == -1) {
            poolSpmn.status = 'Missed Collection';
          } else {
            poolSpmn.status = status = 'Collected';
            poolSpmn.comments = '';
          }
        });

        specimen.status = status;
      });
    }

    $scope.collectPrimarySpmns = function() {
      var spmnsToSave = [];
      angular.forEach($scope.input.visits, function(visit) {
        var visitSpmns = visit.specimens.map(
          function(spmn) {
            return getSpecimenToSave(visit, spmn);
          }
        );

        spmnsToSave = spmnsToSave.concat(visitSpmns);
      });

      RdeApis.savePrimarySpecimens(spmnsToSave).then(
        function(savedSpmns) {
          removeMissedSpmns($scope.input.visits, savedSpmns);
          $scope.ctx.visitsSpmns = $scope.input.visits;
          $state.go('rde-print-labels');
        }
      );
    }

    init();
  });
