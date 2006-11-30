DROP DATABASE nightlybuildcatissue;

CREATE DATABASE nightlybuildcatissue;

GRANT ALL PRIVILEGES ON nightlybuildcatissue.* TO 'root'@'localhost' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON nightlybuildcatissue.* TO 'catissue_core' IDENTIFIED BY 'catissue_core' WITH GRANT OPTION;