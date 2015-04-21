
angular.module('os.common.import.importjob', ['os.common.models'])
  .factory('ImportJob', function(osModel) {
    var ImportJob = osModel('import-jobs');

    return ImportJob;
  });

