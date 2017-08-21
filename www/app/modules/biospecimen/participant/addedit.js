
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('ParticipantAddEditCtrl', function(
    $scope, $state, $stateParams, $translate, $modal, $q,
    cp, cpr, extensionCtxt, hasDict, twoStepReg,
    mrnAccessRestriction, addPatientOnLookupFail, lockedFields,
    CpConfigSvc, CollectionProtocolRegistration, Participant,
    Site, PvManager, ExtensionsUtil, Alerts) {

    var availableSites = [];
    var inputParticipant = null;

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.pid = undefined;
      $scope.allowIgnoreMatches = true;

      $scope.disableFieldOpts = {}
      if (!!cpr.id && cpr.participant.source != 'OpenSpecimen') {
        $scope.disableFieldOpts = {
          fields: getStaticFields(lockedFields),
          disable: !!cpr.id,
          customFields: getCustomFields(lockedFields)
        }
      }

      $scope.cp = cp;
      $scope.cpr = angular.copy(cpr);

      $scope.partCtx = {
        obj: {cpr: $scope.cpr, cp: cp},
        inObjs: ['cpr'],
        twoStepReg: !cpr.id && (twoStepReg && $stateParams.twoStep == 'true'),
        mrnAccessRestriction: mrnAccessRestriction
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

    function checkPreRegParticipants(matchedParticipants) {
      $scope.partCtx.hasPreRegParticipants = false;

      var matchingCp = function(cpr) { return cpr.cpId == $scope.cpId };
      angular.forEach(matchedParticipants,
        function(matchedPart) {
          matchedPart.preReg = (matchedPart.participant.registeredCps || []).filter(matchingCp).length > 0;
          if (matchedPart.preReg) {
            $scope.partCtx.hasPreRegParticipants = true;
          }
        }
      );
    }

    function copyStaticField(src, dest, lockedFields, field, isArray) {
      var fqn = 'cpr.participant.' + field;
      if (lockedFields.indexOf(fqn) != -1) {
        return; // field is locked. Cannot overwrite its value.
      }

      if (isArray) {
        if (!src[field] || src[field].length == 0) {
          return; // source array is either empty or undefined.
        }

        if (!!dest[field] && dest[field].length > 0) {
          return; // destination array is non-empty.
        }
      } else if (!src[field] || !!dest[field]) {
        return;   // either source field value is empty or destination field value is non-empty.
      }

      dest[field] = src[field]; // copy
    }

    function copyStaticFields(src, dest, lockedFields) {
      var primitiveFields = [
        'firstName', 'lastName', 'middleName', 'birthDate', 'deathDate',
        'gender', 'vitalStatus', 'ethnicity', 'uid', 'empi'
      ];
      angular.forEach(primitiveFields,
        function(field) {
          copyStaticField(src, dest, lockedFields, field, false);
        }
      );

      var arrayFields = ['races', 'pmis'];
      angular.forEach(arrayFields,
        function(field) {
          copyStaticField(src, dest, lockedFields, field, true);
        }
      );
    }

    $scope.pmiText = function(pmi) {
      return pmi.siteName + (pmi.mrn ? " (" + pmi.mrn + ")" : "");
    }

    $scope.addPmiIfLast = function(idx) {
      if (!$scope.partCtx.twoStepReg && lockedFields.indexOf('cpr.participant.pmis') != -1) {
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
              return;
            }

            if (!$scope.cpr.id) {
              checkPreRegParticipants(result);
            }

            $scope.allowIgnoreMatches = true;
            angular.forEach(result,
              function(match) {
                if (!match.participant.id && match.participant.source != 'OpenSpecimen') {
                  //
                  // Ask API to not use existing participant ID
                  //
                  match.participant.id = -1;
                }

                if (match.matchedAttrs.length > 1 || match.matchedAttrs[0] != 'lnameAndDob') {
                  $scope.allowIgnoreMatches = false;
                }
              }
            );

            $scope.matchedParticipants = result;
            inputParticipant = $scope.cpr.participant;
          }
        );
      }
    };

    $scope.matchedAttrText = function(attr) {
      return $translate.instant("participant.matching_attr." + attr);
    }

    $scope.selectParticipant = function(participant) {
      $scope.selectedParticipant = $scope.cpr.participant = participant;
    };

    $scope.lookupAgain = function() {
      $scope.cpr.participant = inputParticipant;
      $scope.matchedParticipants = $scope.selectedParticipant = undefined;
      $scope.allowIgnoreMatches = true;
    };

    $scope.ignoreMatchesAndRegister = function() {
      $scope.cpr.participant = inputParticipant;
      registerParticipant();
    };

    $scope.registerUsingSelectedParticipant = function() {
      var selectedPart = $scope.selectedParticipant;

      var promise;
      if (inputParticipant.source != selectedPart.source) {
        promise = CpConfigSvc.getLockedParticipantFields(selectedPart.source);
      } else {
        var q = $q.defer();
        q.resolve(lockedFields);
        promise = q.promise;
      }

      promise.then(
        function(lockedFields) {
          copyStaticFields(inputParticipant, selectedPart, lockedFields);
          $scope.cpr.participant = new Participant(selectedPart);
          registerParticipant();
        }
      );
    };

    $scope.confirmMerge = function() {
      var modalInstance = $modal.open({
        templateUrl: "modules/biospecimen/participant/confirm-merge.html",
        controller: function($scope, $state, $modalInstance, cpr) {
          $scope.cpr = cpr;
          $scope.params = {
            cpr: cpr,
            url: $state.href('participant-detail.overview', {cpId: cpr.cpId, cprId: cpr.id})
          };

          $scope.ok = function() {
            $modalInstance.close(true);
          }

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        },
        resolve: {
          cpr: function() {
            return cpr;
          }
        }
      });

      modalInstance.result.then(
        function() {
          $scope.registerUsingSelectedParticipant(); 
        }
      );
    }

    $scope.lookup = function() {
      $scope.cpr.participant.getMatchingParticipants().then(
        function(result) {
          if (!result || result.length == 0) {
            if (addPatientOnLookupFail) {
              $scope.partCtx.twoStepReg = false;
              $scope.partCtx.showNoMatchWarning = true;
            } else {
              Alerts.error('participant.no_matching_participant');
            }
          } else {
            checkPreRegParticipants(result);

            $scope.allowIgnoreMatches = false;
            $scope.matchedParticipants = result;

            inputParticipant = $scope.cpr.participant;

            if (result.length == 1 && !result[0].preReg) {
              $scope.selectParticipant(result[0].participant);
            }
          }
        }
      );
    }

    init();
  });
