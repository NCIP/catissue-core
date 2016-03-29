angular.module('os.rde')
  .controller('rdeConfigCtrl', function($scope, cp, CollectionProtocolEvent, SpecimenRequirement, Util) {
      function init() {
        $scope.workflows = {};
        $scope.bdeWf = {name: 'bde', data: {}};
        $scope.events = [];
        $scope.ctx = {
        };

        cp.getWorkflows().then(
          function(result) {
            $scope.workflows = result.workflows;
            if (!!result.workflows.bde) {
              $scope.bdeWf = result.workflows.bde; 
              $scope.bdeWf.data = $scope.bdeWf.data || {};
              $scope.bdeWf.data.spmnScanning = $scope.bdeWf.data.spmnScanning || {};
            }
          }
        );

        CollectionProtocolEvent.listFor(cp.id).then(
          function(events) {
            $scope.events = events
            if (events.length > 0) {
              $scope.ctx.selectedEvent = events[0];
              $scope.showEventReq();
            }

            angular.forEach(events, function(evt) {
              evt.displayLabel = "T" + evt.eventPoint + ": " + evt.eventLabel;
              if (evt.code) {
                evt.displayLabel +=  " (" + evt.code + ")";
              }
            });
          }
        ); 
      }

      $scope.updateBdeWf = function() {
        if (!$scope.bdeWf.data.primarySpmnScanning) {
          $scope.bdeWf.data.spmnScanning = {};
        }

        $scope.workflows.bde = $scope.bdeWf;
        var workflows = [];
        angular.forEach($scope.workflows, function(wf) {
          workflows.push(wf);
        });

        cp.saveWorkflows(workflows);
      }

      $scope.showEventReq = function() {
        if (!$scope.ctx.selectedEvent.requirements) {
          SpecimenRequirement.listFor($scope.ctx.selectedEvent.id).then(
            function(reqs) {
              $scope.ctx.selectedEvent.requirements = reqs;
            }
          )
        }
      }

      $scope.showEditSiteNotif = function(cpSite) {
        $scope.ctx.selectedSite = cpSite;
        var notifs = $scope.bdeWf.data.emailNotifs;
        if (notifs && notifs[cpSite.id]) {
          $scope.ctx.rcptEmailIds = notifs[cpSite.id].join();
        }
      }

      $scope.revertSiteNotifEdit = function() {
        $scope.ctx.rcptEmailIds = $scope.ctx.selectedSite = undefined;
      }

      $scope.updateSiteNotif = function() {
        var emailIds = Util.splitStr($scope.ctx.rcptEmailIds, /,|;|\t|\n/);

        $scope.bdeWf.data.emailNotifs = $scope.bdeWf.data.emailNotifs || {};
        $scope.bdeWf.data.emailNotifs[$scope.ctx.selectedSite.id] = emailIds;
        $scope.updateBdeWf();
        $scope.revertSiteNotifEdit();
      }

      init();
    });
