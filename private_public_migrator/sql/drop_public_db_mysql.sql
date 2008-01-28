Drop DATABASE if exists catissue_public;    

CREATE DATABASE catissue_public;

use catissue_public;

GRANT ALL PRIVILEGES ON catissue_public.* TO 'catissue_core'@'localhost' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON catissue_public.* TO 'catissue_core'@'%' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;

commit;