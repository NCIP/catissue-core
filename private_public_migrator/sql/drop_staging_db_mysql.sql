Drop DATABASE if exists staging;    

CREATE DATABASE staging;

use staging;

GRANT ALL PRIVILEGES ON staging.* TO 'catissue_core'@'localhost' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON staging.* TO 'catissue_core'@'%' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;

commit;