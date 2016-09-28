
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('ParticipantAddEditCtrl', function(
    $scope, $state, $stateParams, $translate, $modal,
    cp, cpr, extensionCtxt, hasDict, lockedFields,
    CollectionProtocolRegistration, Participant,
    Site, PvManager, ExtensionsUtil) {

    var availableSites = [];

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.pid = undefined;
      $scope.allowIgnoreMatches = true;

      $scope.disableFieldOpts = {}
      if (!!cpr.id) {
        $scope.disableFieldOpts = {
          fields: getStaticFields(lockedFields),
          disable: !!cpr.id,
          customFields: getCustomFields(lockedFields)
        }
      }

      $scope.cp = cp;
      $scope.cpr = angular.copy(cpr);

      $scope.partCtx = {
        obj: {cpr: $scope.cpr}, inObjs: ['cpr']
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(
        $scope.cpr.participant, extensionCtxt, $scope.disableFieldOpts.customFields);

      if (!hasDict) {
        $scope.cpr.participant.addPmi($scope.cpr.participant.newPmi());
        loadPvs();
      }
    };

    function getStaticFields(fields) {
      var result = {};
      angular.forEach(fields,
        function(f) {
          if (f.indexOf('cpr.participant.extensionDetail.attrsMap') != 0) {
            result[f] = true;
          }
        }
      );

      return result;
    }

    function getCustomFields(fields) {
      return fields.filter(
        function(f) {
          return f.indexOf('cpr.participant.extensionDetail.attrsMap') == 0
        }
      ).map(
        function(f) {
          return f.substring('cpr.participant.extensionDetail.attrsMap.'.length);
        }
      );
    }

    function loadPvs() {
      $scope.op = !!$scope.cpr.id ? 'Update' : 'Create';
      $scope.genders = PvManager.getPvs('gender');
      $scope.vitalStatuses = PvManager.getPvs('vital-status');
    };

    function registerParticipant() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var cprToSave = angular.copy($scope.cpr);
      cprToSave.cpId = $scope.cpId;

      if (formCtrl) {
        cprToSave.participant.extensionDetail = formCtrl.getFormData();
      }

      cprToSave.$saveOrUpdate().then(
        function(savedCpr) {
          if (savedCpr.activityStatus == 'Active') {
            var registeredCps = cpr.participant.registeredCps;
            angular.extend(cpr, savedCpr);
            cpr.participant.registeredCps = registeredCps;
            $state.go('participant-detail.overview', {cprId: savedCpr.id});
          } else {
            $state.go('participant-list', {cpId: $scope.cp.id});
          }
        }
      );
    };

    $scope.pmiText = function(pmi) {
      return pmi.mrn + " (" + pmi.siteName + ")";
    }

    $scope.addPmiIfLast = function(idx) {
      if (lockedFields.indexOf('cpr.participant.pmis') != -1) {
        return;
      }

      var participant = $scope.cpr.participant;
      if (idx == participant.pmis.length - 1) {
        participant.addPmi(participant.newPmi());
      }
    };

    $scope.removePmi = function(pmi) {
      var participant = $scope.cpr.participant;
      participant.removePmi(pmi);

      if (participant.pmis.length == 0) {
        participant.addPmi(participant.newPmi());
      }
    };

    $scope.register = function() {
      var participant = $scope.cpr.participant;
      if (!participant.isMatchingInfoPresent()) {
        registerParticipant();
      } else {
        participant.getMatchingParticipants().then(
          function(result) {
            if (!result || result.length == 0) {
              registerParticipant();
            }

            $scope.allowIgnoreMatches = true;
            for (var i = 0; i < result.length; ++i) {
              var matchedAttrs = result[i].matchedAttrs;
              if (matchedAttrs.length > 1 || (matchedAttrs[0] != 'lnameAndDob')) {
                $scope.allowIgnoreMatches = false;
                break;
              }
            } 
            $scope.allowIgnoreMatches = participant.id || $scope.allowIgnoreMatches;
            $scope.matchedParticipants = result;
          }
        );
      }
    };

    $scope.matchedAttrText = function(attr) {
      return $translate.instant("participant.matching_attr." + attr);
    }

    $scope.selectParticipant = function(participant) {
      $scope.selectedParticipant = participant;
    };

    $scope.lookupAgain = function() {
      $scope.matchedParticipants = undefined;
      $scope.selectedParticipant = undefined;
      $scope.allowIgnoreMatches = true;
    };

    $scope.ignoreMatchesAndRegister = function() {
      registerParticipant();
    };

    $scope.registerUsingSelectedParticipant = function() {
      $scope.cpr.participant = new Participant($scope.selectedParticipant);
      registerParticipant();
    };

    $scope.confirmMerge = function() {
      var modalInstance = $modal.open({
        templateUrl: "modules/biospecimen/participant/confirm-merge.html",
        controller: function($scope, $modalInstance) {
          $scope.ok = function() {
            $modalInstance.close(true);
          }

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });

      modalInstance.result.then(
        function() {
          $scope.registerUsingSelectedParticipant(); 
        }
      );
    }

    init();
  });
