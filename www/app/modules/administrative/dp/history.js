
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, DistributionOrder, dpId) {
    $scope.orders = [{
      "name": "Distributed to Prof Tin",
      "executionDate": "1441174401000",
      "specimens": {
                     "specimenType": "DNA",
                     "tissueSite": "Lung",
                     "pathologyStatus": "Malignant"
                   },
      "specimenCnt": 20
    },
    {
      "name": "Distributed to Prof Tin",
      "executionDate": "1362002400000",
      "specimens": {
                     "specimenType": "RNA",
                     "tissueSite": "Lung",
                     "pathologyStatus": "Malignant"
                   },
      "specimenCnt": 508
    }];
  });
